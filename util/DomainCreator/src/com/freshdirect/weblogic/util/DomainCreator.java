package com.freshdirect.weblogic.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import weblogic.management.scripting.utils.WLSTInterpreter;

import com.bea.plateng.domain.script.jython.WLSTException;

public class DomainCreator {
	private static final String WEBLOGIC_HOME = "weblogic.home";

	private static final String DOMAIN_NAME = "domain.name";

	private static final String DOMAIN_HOME = "domain.home";

	private static final String DOMAIN_HOME_IGNORE_SOURCE_REPOSITORY_CHECK = "domain.home.ignoreSourceRepositoryCheck";

	private static final String DOMAIN_OVERWRITE = "domain.overwrite";

	private static final String SERVER_HOST = "server.host";

	private static final String SERVER_PORT = "server.port";

	
	private static final String DOMAIN_TEMPLATE_FILE = "/common/templates/domains/wls.jar";

	
	static final String LINE_SEP = System.getProperty("line.separator");

	
	private static void initDefaultDomainConfig(Properties p) {
		p.put(DOMAIN_NAME, "domain");
		p.put(SERVER_HOST, "<hostname>");
		p.put(SERVER_PORT, "7001");
	}

	public static void main(String[] args) {
		DomainCreator creator;
		try {
			creator = new DomainCreator();
			creator.validateConfig();
			creator.create();
		} catch (IOException e) {
			System.err.println("Error when loading properties: " + e.getMessage());
		} catch (InvalidDomainConfigurationException e) {
			System.err.println("Error when validating properties: " + e.getMessage());
		} catch (CreateDomainException e) {
			System.err.println("Error when validating properties: " + e.getMessage());
		}
	}

	Properties properties;

	private String domainHome;

	private String domainName;

	private File domainFile;

	private String weblogicHome;

	private String serverHost;
	
	private int serverPort;


	public DomainCreator() throws IOException {
		properties = new Properties();
		initDefaultDomainConfig(properties);
		InputStream stream = null;
		try {
			stream = ClassLoader.getSystemResourceAsStream("domain.properties");
			if (stream == null)
				throw new IOException("domain.properties not found on class path");
			properties.load(stream);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
	}


	final static String MAC_OS = "Mac OS X";
	private void patchStartWebLogicScript() {
		if (MAC_OS.equals(System.getProperty("os.name")) ) {
			File file = null;
			for (File aFile : domainFile.listFiles()) {
				if ("startWebLogic.sh".equals(aFile.getName()) ) {
					file = aFile;
					break;
				}
			}
			// startWebLogic file not found, bye ...
			if (file == null)
				return;

	        try
	        {
	            StringBuilder buf = new StringBuilder();
	            BufferedReader reader = new BufferedReader(new FileReader(file));
	            String line = null;
	            while( (line = reader.readLine()) != null) {
	            	buf.append(line);
	            	buf.append(LINE_SEP);
	            	
	            	// Mac OS X needs more resources!s
	            	if (line.startsWith("DOMAIN_HOME=")) {
	            		buf.append("export USER_MEM_ARGS=\"-Xms512m -Xmx1526m -XX:MaxPermSize=256m\"");
	            		buf.append(LINE_SEP);
	            	}
	            }
	            
	            // write file back
	            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	            BufferedReader reader2 = new BufferedReader( new StringReader(buf.toString()) );
	            while( (line = reader2.readLine()) != null) {
	            	writer.append(line);
            		writer.append(LINE_SEP);
	            }
	            writer.flush();
	            writer.close();
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	        }
			
		}
	}
	
	private void validateConfig() throws InvalidDomainConfigurationException {
		// Domain HOME and PATH
		domainName = properties.getProperty(DOMAIN_NAME);
		for (char c : domainName.toCharArray())
			if (!Character.isJavaIdentifierPart(c))
				throw new InvalidDomainConfigurationException(DOMAIN_NAME + " is an invalid domain identifier");
		System.out.println(DOMAIN_NAME + ": " + domainName);

		if (!properties.containsKey(DOMAIN_HOME))
			throw new InvalidDomainConfigurationException(DOMAIN_HOME + " is undefined");
		domainHome = properties.getProperty(DOMAIN_HOME);
		File domainHomeFile = new File(domainHome);
		if (!domainHomeFile.exists())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME + " does not exist");
		if (!domainHomeFile.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME + " is not a directory");
		if (!properties.containsKey(DOMAIN_HOME_IGNORE_SOURCE_REPOSITORY_CHECK))
			validateFreshDirectSourceRepository(domainHomeFile);
		domainFile = new File(domainHomeFile, domainName);
		if (!properties.containsKey(DOMAIN_OVERWRITE)) {
			if (domainFile.exists())
				throw new InvalidDomainConfigurationException(DOMAIN_NAME + " exists under " + DOMAIN_HOME);
		}
		System.out.println(DOMAIN_HOME + ": " + domainHome);

		// WebLogic HOME
		weblogicHome = properties.getProperty(WEBLOGIC_HOME);
		File weblogicHomeFile = new File(weblogicHome);
		if (!weblogicHomeFile.exists())
			throw new InvalidDomainConfigurationException(WEBLOGIC_HOME + " does not exist");
		if (!weblogicHomeFile.isDirectory())
			throw new InvalidDomainConfigurationException(WEBLOGIC_HOME + " is not a directory");
		File wlsTemplateFile = new File(weblogicHomeFile, DOMAIN_TEMPLATE_FILE);
		if (!wlsTemplateFile.exists())
			throw new InvalidDomainConfigurationException(WEBLOGIC_HOME
					+ " does not look like a real WebLogic HOME (template does not exists)");
		if (!wlsTemplateFile.isFile())
			throw new InvalidDomainConfigurationException(WEBLOGIC_HOME
					+ " does not look like a real WebLogic HOME (template is not a regular file)");
		System.out.println(WEBLOGIC_HOME + ": " + weblogicHome);

		// Server Name
		serverHost = properties.getProperty(SERVER_HOST);
		if (serverHost.equals("<hostname>")) {
			try {
				serverHost = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				throw new InvalidDomainConfigurationException("cannot determine the local host name for " + SERVER_HOST);
			}
		} else {
			try {
				InetAddress.getByName(SERVER_HOST);
			} catch (UnknownHostException e) {
				throw new InvalidDomainConfigurationException(SERVER_HOST + " is an unknown host");
			}
		}
		System.out.println(SERVER_HOST + ": " + serverHost);

		// Server Port
		try {
			serverPort = Integer.parseInt(properties.getProperty(SERVER_PORT));
			new InetSocketAddress(serverPort);
		} catch (NumberFormatException e) {
			throw new InvalidDomainConfigurationException("cannot parse " + SERVER_PORT + " integer");
		} catch (IllegalArgumentException e) {
			throw new InvalidDomainConfigurationException(SERVER_PORT + " is out of range (out of 0-65535)");
		}
	}

	private void validateFreshDirectSourceRepository(File domainFile) throws InvalidDomainConfigurationException {
		File subdir = new File(domainFile, "projects");
		if (!subdir.exists() || !subdir.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (no projects directory)");
		subdir = new File(domainFile, "build");
		if (!subdir.exists() || !subdir.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (no build directory)");
		subdir = new File(domainFile, "release");
		if (!subdir.exists() || !subdir.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (no release directory)");
		subdir = new File(domainFile, "lib");
		if (!subdir.exists() || !subdir.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (no lib directory)");
		subdir = new File(domainFile, ".svn");
		if (!subdir.exists() || !subdir.isDirectory())
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (no .svn directory)");
		File svnEntries = new File(subdir, "entries");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(svnEntries));
			try {
				reader.readLine();
				reader.readLine();
				reader.readLine();
				reader.readLine();
				String line = reader.readLine();
				if (!line.startsWith("https://svn.freshdirect.com/appdev"))
					throw new InvalidDomainConfigurationException(DOMAIN_HOME
							+ " does not look like a FreshDirect source repository (malformed SVN repository #3)");
			} catch (IOException e) {
				throw new InvalidDomainConfigurationException(DOMAIN_HOME
						+ " does not look like a FreshDirect source repository (malformed SVN repository #2)");
			}
		} catch (FileNotFoundException e) {
			throw new InvalidDomainConfigurationException(DOMAIN_HOME
					+ " does not look like a FreshDirect source repository (malformed SVN repository)");
		}
	}



	private void create() throws CreateDomainException, IOException {
		if (domainFile.exists()) {
			System.out.println("deleting previous domain directory...");
			try {
				FileUtils.deleteDirectory(domainFile);
			} catch (IOException e) {
				throw new CreateDomainException("cannot delete previous domain directory (" + domainFile.getAbsolutePath() + ")");
			}
			System.out.println("deleted previous domain directory");
		}


		// STAGE ONE
		createStageOne();
		patchStartWebLogicScript();


		String cmd = domainFile.getAbsolutePath()+"/startWebLogic.sh" ;
		final Runtime run = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = run.exec(cmd) ;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;

		String line;
		while ( ( line = buf.readLine() ) != null ) {
			// wait for STARTED message
			if (line.contains("<BEA-000360>")) {
				break;
			} else {
				System.out.println(line);
			}
		}
		
		
		// STAGE 2
		try {
			createStageTwo();
		} catch(Exception exc) {
			System.err.println("!!! Script crashed !!!" + exc);
		}

		if (pr != null) {
			pr.destroy();
			try {
				pr.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * STAGE I.
	 * Create empty domain
	 */
	private void createStageOne() {
		System.out.println("starting interpreter");

		Cmd _cmd = new Cmd();
		
		System.out.println("started interpreter");

		System.out.println("loading template (wls.jar)");
		_cmd.readTemplate(weblogicHome + DOMAIN_TEMPLATE_FILE);
		System.out.println("loaded template (wls.jar)");

		System.out.println("setting domain name");
		_cmd.set("Name", domainName);
		System.out.println("set domain name");


		System.out.println("setting host name");
		_cmd.set("AdminServerName", serverHost);
		_cmd.cd("Servers/AdminServer");
		_cmd.set("Name", serverHost);
		_cmd.set("ListenAddress","");
		_cmd.setInt("ListenPort", serverPort);
		_cmd.cd("/");

		System.out.println("set host name");
		_cmd.cd("/");
		_cmd.cd("Security/" + domainName + "/User/weblogic");
		_cmd.cmo_setPassword("weblogic");
		
		System.out.println("writing domain to disk");
		_cmd.setOption("OverwriteDomain", "true");
		_cmd.writeDomain(domainFile.getAbsolutePath());
		_cmd.dumpStack();
		_cmd.closeTemplate();

		System.out.println("written domain to disk");
	}


	private void createStageTwo() throws WLSTException {
		InputStream is = ClassLoader.getSystemResourceAsStream("com/freshdirect/resources/stage2.py");
		
		try {
			StringBuilder sb = new StringBuilder();

			// connect
			sb.append("connect('weblogic','weblogic','t3://"+serverHost+":"+serverPort+"')").append(LINE_SEP);

			// bind parameters
			sb.append("serverName='" + serverHost + "'").append(LINE_SEP);
			sb.append("vHostName='" + ("crm"+serverHost) + "'").append(LINE_SEP);
			sb.append("vHostPort=" + 7007).append(LINE_SEP);



			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String nextLine = "";
			while ((nextLine = br.readLine()) != null) {
	   			sb.append(nextLine);
     			sb.append(LINE_SEP);
   			}
			
			// Trail commands
			sb.append("disconnect()").append(LINE_SEP);
			
			/*** DEBUG
			System.err.println("=== STAGE2 SCRIPT ===");
			System.err.println(sb.toString());
			System.err.println("=== ============= ===");
			***/
			
			Cmd _cmd = new Cmd();
			
			_cmd.unsafeExec(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	class Cmd {
		final String _sep = System.getProperty("line.separator");

		WLSTInterpreter _intr;
		
		public Cmd() {
			_intr = new WLSTInterpreter();
			_intr.setOut(System.out);
			_intr.setErr(System.err);
		}
		
		public Cmd(WLSTInterpreter intr) {
			_intr = intr;
		}



		/**
		 * Execute a line of Python code in safe way
		 * @param expression Python code line
		 */
		protected void exec(String expression) {
			StringBuilder buf = new StringBuilder();
			buf.append("try:").append(_sep);
			buf.append("\t");
			buf.append(expression);
			buf.append(_sep);
			buf.append("except:");
			buf.append("\t");
			buf.append("print \"error:\", sys.exc_info()[0]");
			buf.append(_sep);
			
			final String pyline = buf.toString();
			// System.err.println(" -> " + pyline);
			
			_intr.exec(pyline);
		}

		public void unsafeExec(String pysrc) {
			try {
				_intr.exec(pysrc);
			} catch (Exception exc) {
				System.err.println("Exception raised during exec; exc="+exc);
				_intr.exec("dumpStack()");
			}
		}


		public void readTemplate(String templatePath) {
			exec("readTemplate(\"" + templatePath + "\")");
		}

		public void dumpStack() {
			exec("dumpStack()");
		}

		public void ls() {
			exec("ls()");
		}
		
		public void ls(String path) {
			exec("ls(\"" + path + "\")");
		}

		public void cd(String path) {
			exec("cd(\"" + path + "\")");
		}

		public void set(String path, Object value) {
			exec("set(\"" + path + "\", \"" + value + "\")");
		}

		public void setInt(String path, int value) {
			exec("set(\"" + path + "\", int(" + value + "))");
		}

		public void cmo_setPassword(String password) {
			exec("cmo.setPassword(\"" + password + "\")");
		}

		public void setOption(String key, Object value) {
			exec("setOption(\"" + key + "\", \"" + value + "\")");
		}

		public void writeDomain(String path) {
			exec("writeDomain(\"" + path + "\")");
		}

		public void closeTemplate() {
			exec("closeTemplate()");
		}
	}
}
