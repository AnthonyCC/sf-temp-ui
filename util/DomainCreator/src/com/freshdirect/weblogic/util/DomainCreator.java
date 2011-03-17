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
import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import weblogic.management.scripting.utils.WLSTInterpreter;

public class DomainCreator {
	private static final String WEBLOGIC_HOME = "weblogic.home";

	private static final String DOMAIN_NAME = "domain.name";

	private static final String DOMAIN_HOME = "domain.home";

	private static final String DOMAIN_HOME_IGNORE_SOURCE_REPOSITORY_CHECK = "domain.home.ignoreSourceRepositoryCheck";

	private static final String DOMAIN_OVERWRITE = "domain.overwrite";

	private static final String SERVER_HOST = "server.host";

	private static final String SERVER_PORT = "server.port";
	
	private static final String SRC_HOME = "src.home";
	
	
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
			// creator.runStageThree();
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

	private String srcHome;


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


	/**
	 * Patch startWebLogic.sh
	 *  
	 * @param ptchr patcher function
	 */
	private void patchStartWebLogicScript(WLPatcher ptchr) {
		File file = null;
		for (File aFile : domainFile.listFiles()) {
			if ("startWebLogic.sh".equals(aFile.getName())) {
				file = aFile;
				break;
			}
		}
		// startWebLogic file not found, bye ...
		if (file == null)
			return;

		try {
			StringBuilder buf = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				ptchr.appendBefore(line, buf);
				
				if (!ptchr.skipLine(line)) {
					buf.append(line);
					buf.append(LINE_SEP);
				}
				
				ptchr.appendAfter(line, buf);
			}

			// write file back
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			BufferedReader reader2 = new BufferedReader(new StringReader(buf
					.toString()));
			while ((line = reader2.readLine()) != null) {
				writer.append(line);
				writer.append(LINE_SEP);
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
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
			if (domainFile.exists()) {
				// FIXME:
				// throw new InvalidDomainConfigurationException(DOMAIN_NAME + " exists under " + DOMAIN_HOME);
			}
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
				InetAddress.getByName(serverHost);
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
		

		// Src Home
		srcHome = properties.getProperty(SRC_HOME);
		if (srcHome == null) {
			srcHome = domainHome;
		}
		System.out.println(SRC_HOME + ": " + srcHome);

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

		
		// STAGE 1
		{
			long start = System.currentTimeMillis();
			runStageOne();
			long finish = System.currentTimeMillis();
			System.out.println("\n## STAGE 1 TOOK "+( (finish-start)/1000 )+" seconds ##\n\n");
		}

		// STAGE 2
		{
			long start = System.currentTimeMillis();
			runStageTwo();
			long finish = System.currentTimeMillis();
			System.out.println("\n## STAGE 2 TOOK "+( (finish-start)/1000 )+" seconds ##\n\n");
		}

		// STAGE 3
		{
			long start = System.currentTimeMillis();
			runStageThree();
			long finish = System.currentTimeMillis();
			System.out.println("\n## STAGE 3 TOOK "+( (finish-start)/1000 )+" seconds ##\n\n");
		}
	}


	
	private Process startWebLogic() {
		String cmd = domainFile.getAbsolutePath()+"/startWebLogic.sh" ;
		final Runtime run = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = run.exec(cmd) ;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		// Wait for coming up
		try {
			BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
			String line;
			while ( ( line = buf.readLine() ) != null ) {
				// wait for STARTED message
				if (line.contains("<BEA-000360>")) {
					break;
				}
			}
		} catch(IOException exc) {
			exc.printStackTrace();
		}
		return pr;
	}


	private void stopWebLogic(Process pr) {
		StringBuilder buf = new StringBuilder();
		
		bindParameters(buf);

		buf.append("connect(wl_user,wl_pwd,wl_url)").append(LINE_SEP);
		buf.append("shutdown()").append(LINE_SEP);

		Cmd cmd = new Cmd(new WLSTInterpreter());
		cmd.unsafeExec(buf.toString());
		
		if (pr != null) {
			// Wait for coming up
			try {
				BufferedReader reader = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;
				String line;
				while ( ( line = reader.readLine() ) != null ) {
					// do nothing but read all lines until it breaks
				}
			} catch(IOException exc) {
				exc.printStackTrace();
			}
		}
	}


	/**
	 * STAGE I.
	 * Create empty domain
	 */
	private void runStageOne() {
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


	/**
	 * STAGE II.
	 * Configure WebLogic domain
	 */
	private void runStageTwo() {
		// Add more memory
		//
		patchStartWebLogicScript(new WLPatcher() {
			@Override
			public void appendAfter(String line, Appendable out) {
				if (line.startsWith("DOMAIN_HOME=")) {
					try {
						out.append("export USER_MEM_ARGS=\"-Xms512m -Xmx1526m -XX:MaxPermSize=256m\"");
						out.append(LINE_SEP);
					} catch (IOException e) {}
				}
			}

			@Override
			public void appendBefore(String line, Appendable out) {}

			@Override
			public boolean skipLine(String line) {
				return false;
			}
		});
		
		
		Process pr = startWebLogic();
		try {
			// run stage2 script
			runScript(ClassLoader.getSystemResourceAsStream("com/freshdirect/resources/stage2.py"), false);
	
			// add users and roles
			runScript(ClassLoader.getSystemResourceAsStream("com/freshdirect/resources/realm.py"), false);
		} catch(Exception exc) {
			System.err.println("!!! Stage 2 crashed !!!" + exc);
		} 
		
		stopWebLogic(pr);
	}

	/**
	 * STAGE III
	 * 
	 * Populate applications
	 */
	private void runStageThree() {
		final String PSEP = System.getProperty("file.separator");
		File projectsDir = new File(srcHome + PSEP + "projects");
		
		// Extract dependendent libs from classpaths
		List<String> libs = LibUtil.collectLibs(projectsDir);
		
		patchStartWebLogicScript(new WLPatcher(libs, projectsDir) {
			@Override
			public void appendAfter(String line, Appendable out) {
				if (line.startsWith("DOMAIN_HOME=")) {
					try {
						out.append("FD_BASE=\""+srcHome+"\"").append(LINE_SEP);
						out.append("FD_LIBS=\"$FD_BASE/lib\"").append(LINE_SEP);
						out.append(LINE_SEP);

						out.append("EXT_PRE_CLASSPATH=$FD_BASE/properties:\\");
						out.append(LINE_SEP);
						for (String lib : (List<String>) param1) {
							out.append("$"+lib+":\\");
							out.append(LINE_SEP);
						}
						
						
						final String projects = "CMS,CRM,DataLoader,Delivery,DlvAdmin,DlvConfirm,ERPSAdmin,ERPSWebApp,ERPServices,FDIntegrationServices,FDStore,FDWebSite,Framework,Media,OCF,RefAdmin,Resources,RoutingServices,RulesAdmin,StandingOrdersService,Tests,Tools,TransportationAdmin,WebAppCommon,cms-gwt,listadmin,ocf-adm";

						File pDir = (File) param2;

				
						// check for binary dirs
						for (File prj : pDir.listFiles() ) {
							if (!prj.isDirectory())
								continue;

							// FIXME: check project settings to find out where bin is
							File binDir = new File(prj.getAbsolutePath() + PSEP + "bin");
							if (!binDir.isDirectory())
								continue;

							// ok, add to classpaths
							out.append("$FD_BASE/projects/"+prj.getName()+"/bin:\\");
							out.append(LINE_SEP);
						}
						
						
						// finally append Resources
						out.append("$FD_BASE/projects/Resources");
						out.append(LINE_SEP);
						
						out.append("export EXT_PRE_CLASSPATH");
						out.append(LINE_SEP);
						out.append(LINE_SEP);
						//System.err.print(out.toString());
					} catch (IOException e) {}
				}
			}

			@Override
			public void appendBefore(String line, Appendable out) {
			}

			@Override
			public boolean skipLine(String line) {
				return false;
			}
		});

	
		Process pr = startWebLogic();
		try {
			// run stage3 script
			runScript(ClassLoader.getSystemResourceAsStream("com/freshdirect/resources/stage3.py"), false);
		} catch(Exception exc) {
			System.err.println("!!! Stage 3 crashed !!!" + exc);
		} 
		
		stopWebLogic(pr);
	}
	
	
	private void runScript(InputStream is, boolean debug) {
		StringBuilder sb = new StringBuilder();

		// bind parameters
		bindParameters(sb);

		if (loadScript(sb, new InputStreamReader(is))) {
			if (debug) {
				System.err.println("<=== SCRIPT ===>");
				System.err.println(sb.toString());
				System.err.println("<=== ====== ===>");
				
			}

			new Cmd().unsafeExec(sb.toString());
		}
	}

	
	private void bindParameters(Appendable sb) {
		// bind parameters
		try {
			sb.append("wl_user='weblogic'").append(LINE_SEP);
			sb.append("wl_pwd='weblogic'").append(LINE_SEP);
			sb.append("wl_url='t3://" + serverHost + ":" + serverPort + "'").append(LINE_SEP);
	
			sb.append("domainName='" + domainName + "'").append(LINE_SEP);
			sb.append("FD_HOME='" + srcHome + "'").append(LINE_SEP);
			sb.append("serverName='" + serverHost + "'").append(LINE_SEP);
			sb.append("vHostName='" + ("crm"+serverHost) + "'").append(LINE_SEP);
			sb.append("vHostPort=" + 7007).append(LINE_SEP);
		} catch(IOException exc) {
			// don't bother with it
		}
	}


	private boolean loadScript(Appendable out, Reader scriptReader) {
		try {
			BufferedReader br = new BufferedReader(scriptReader);
	
			String nextLine = "";
			while ((nextLine = br.readLine()) != null) {
				out.append(nextLine);
				out.append(LINE_SEP);
			}
		} catch(IOException exc) {
			System.err.println("Failed to load script; exc=" + exc);
			return false;
		}
		return true;
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

abstract class WLPatcher {
	public static final String LINE_SEP = System.getProperty("line.separator");

	Object param1 = null;
	Object param2 = null;
	
	public WLPatcher() {
		this(null, null);
	}

	public WLPatcher(Object param1) {
		this(param1, null);
	}

	public WLPatcher(Object param1, Object param2) {
		this.param1 = param1;
		this.param2 = param2;
	}


	abstract public void appendBefore(String line, Appendable out);
	abstract public void appendAfter(String line, Appendable out);
	abstract public boolean skipLine(String line);
}
