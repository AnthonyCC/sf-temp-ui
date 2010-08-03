package com.freshdirect.weblogic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.python.util.InteractiveInterpreter;

import weblogic.management.scripting.utils.WLSTInterpreter;

public class DomainCreator {
	private static final String WEBLOGIC_HOME = "weblogic.home";

	private static final String DOMAIN_NAME = "domain.name";

	private static final String DOMAIN_HOME = "domain.home";

	private static final String DOMAIN_HOME_IGNORE_SOURCE_REPOSITORY_CHECK = "domain.home.ignoreSourceRepositoryCheck";

	private static final String DOMAIN_OVERWRITE = "domain.overwrite";

	private static final String SERVER_HOST = "server.host";

	private static final String SERVER_PORT = "server.port";

	
	private static final String DOMAIN_TEMPLATE_FILE = "/common/templates/domains/wls.jar";

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

	private InteractiveInterpreter interpreter;

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

	private void create() throws CreateDomainException {
		if (domainFile.exists()) {
			System.out.println("deleting previous domain directory...");
			try {
				FileUtils.deleteDirectory(domainFile);
			} catch (IOException e) {
				throw new CreateDomainException("cannot delete previous domain directory (" + domainFile.getAbsolutePath() + ")");
			}
			System.out.println("deleted previous domain directory");
		}

		System.out.println("starting interpreter");
		interpreter = new WLSTInterpreter();
		System.out.println("started interpreter");

		System.out.println("loading template (wls.jar)");
		readTemplate(weblogicHome + DOMAIN_TEMPLATE_FILE);
		System.out.println("loaded template (wls.jar)");

		System.out.println("setting domain name");
		set("Name", domainName);
		System.out.println("set domain name");
		
		System.out.println("setting host name");
		set("AdminServerName", serverHost);
		cd("Servers/AdminServer");
		set("Name", serverHost);
		set("ListenAddress","");
		set("ListenPort", serverPort);
		cd("/");
		System.out.println("set host name");

		cd("/");
		cd("Security/" + domainName + "/User/weblogic");
		cmo_setPassword("weblogic");
		
		System.out.println("writing domain to disk");
		setOption("OverwriteDomain", "true");
		writeDomain(domainFile.getAbsolutePath());
		dumpStack();
		closeTemplate();
		System.out.println("written domain to disk");
	}
	
	private void exec(String expression) {
		StringBuilder buf = new StringBuilder();
		buf.append("try:\r\n");
		buf.append("\t");
		buf.append(expression);
		buf.append("\r\n");
		buf.append("except:");
		buf.append("\t");
		buf.append("print \"error\"");
		buf.append("\r\n");
		interpreter.exec(buf.toString());
	}

	private void readTemplate(String templatePath) {
		exec("readTemplate(\"" + templatePath + "\")");
	}

	private void dumpStack() {
		exec("dumpStack()");
	}

	@SuppressWarnings("unused")
	private void ls() {
		exec("ls()");
	}
	
	@SuppressWarnings("unused")
	private void ls(String path) {
		exec("ls(\"" + path + "\")");
	}

	private void cd(String path) {
		exec("cd(\"" + path + "\")");
	}

	private void set(String path, Object value) {
		exec("set(\"" + path + "\", \"" + value + "\")");
	}

	private void cmo_setPassword(String password) {
		exec("cmo.setPassword(\"" + password + "\")");
	}

	private void setOption(String key, Object value) {
		exec("setOption(\"" + key + "\", \"" + value + "\")");
	}

	private void writeDomain(String path) {
		exec("writeDomain(\"" + path + "\")");
	}

	private void closeTemplate() {
		exec("closeTemplate()");
	}
}
