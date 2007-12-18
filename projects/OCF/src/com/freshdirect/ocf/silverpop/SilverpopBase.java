/**
 * @author ekracoff
 * Created on May 23, 2005*/

package com.freshdirect.ocf.silverpop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SilverpopBase {

	protected final static SilverpopServiceI SERVICE = (SilverpopServiceI) FDRegistry.getInstance().getService(
		SilverpopServiceI.class);

	protected final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	private final static Category LOGGER = LoggerFactory.getInstance(SilverpopBase.class);

	/**
	 * @throws IOException
	 * @throws DocumentException
	 * @throws FDRuntimeException
	 */
	public static Document postXml(String url, Document doc, String sessionId) throws IOException {
		try {
			int timeout = SERVICE.getTimeout();
			HttpClient httpClient = new HttpClient();
			httpClient.setHttpConnectionFactoryTimeout(timeout);
			httpClient.setTimeout(timeout);
			httpClient.setConnectionTimeout(timeout);

			String xml = prettyPrint(doc);
			if (SERVICE.isPrintXml()) {
				System.out.println(xml);
			}

			List nameValues = new ArrayList();

			nameValues.add(new NameValuePair("xml", xml));

			if (!"0".equalsIgnoreCase(sessionId)) {
				nameValues.add(new NameValuePair("jsessionid", sessionId));
			}

			NameValuePair[] data = (NameValuePair[]) nameValues.toArray(new NameValuePair[nameValues.size()]);

			PostMethod post = new PostMethod(url);
			post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setRequestBody(data);
			post.setUseExpectHeader(false);

			int responseCode = httpClient.executeMethod(post);
			String response = post.getResponseBodyAsString();

			if (responseCode != 200) {
				throw new HttpException("HTTP response not 200 OK");
			}

			Document responseDoc = DocumentHelper.parseText(response);

			post.releaseConnection();

			return responseDoc;
		} catch (DocumentException e) {
			throw new FDRuntimeException(e);
		}
	}

	protected static String prettyPrint(Document doc) throws IOException {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding("UTF-8");
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter(sw, outformat);
		writer.write(doc);
		writer.flush();
		return sw.getBuffer().toString();
	}

	protected static String login(String username, String password) throws IOException {
		Document loginDoc = DocumentHelper.createDocument();
		Element login = loginDoc.addElement("Envelope").addElement("Body").addElement("Login");
		login.addElement("USERNAME").addText(username);
		login.addElement("PASSWORD").addText(password);

		Document doc = postXml(SERVICE.getUrl(), loginDoc, "0");
		boolean success = Boolean.valueOf(doc.valueOf("/Envelope/Body/RESULT/SUCCESS")).booleanValue();
		if (success) {
			String sessionId = doc.valueOf("/Envelope/Body/RESULT/SESSIONID");
			return sessionId;
		}
		return null;
	}

	protected static String logout(String sessionId) throws IOException {
		Document logoutDoc = DocumentHelper.createDocument();
		Element logout = logoutDoc.addElement("Envelope").addElement("Body").addElement("Logout");

		Document doc = postXml(SERVICE.getUrl(), logoutDoc, sessionId);
		boolean success = Boolean.valueOf(doc.valueOf("/Envelope/Body/RESULT/SUCCESS")).booleanValue();
		if (success) {
			LOGGER.debug("Logged out");
		}
		return null;
	}

	public static File downloadFile(String filename) throws IOException {
		FTPClient ftp = null;
		try {
			ftp = getFTPConnection();

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			File localFile = new File(SERVICE.getBasePath(), filename);
			LOGGER.debug("downloading file " + filename + " to " + localFile.getAbsolutePath());
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localFile));

			boolean success = ftp.retrieveFile("/download/" + filename, os);
			LOGGER.debug(ftp.getReplyString());
			if (!success) {
				throw new IOException(ftp.getReplyString());
			}

			os.flush();
			os.close();

			return localFile;

		} finally {
			if (ftp!=null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		}
	}

	public static boolean uploadFile(String filename, InputStream input) throws IOException {
		FTPClient ftp = null;
		try {
			ftp = getFTPConnection();

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			BufferedInputStream is = new BufferedInputStream(input);

			boolean success = ftp.storeFile("/upload/" + filename, is);
			LOGGER.debug(ftp.getReplyString());
			if (!success) {
				throw new IOException(ftp.getReplyString());
			}

			is.close();

			return success;

		} finally {

			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		}
	}

	protected static FTPClient getFTPConnection() throws IOException {
		FTPClient ftp = new FTPClient();
		ftp.setDefaultTimeout(30000);
		ftp.setDataTimeout(30000);

		ftp.connect(SERVICE.getFtp());
		ftp.login(SERVICE.getUsername(), SERVICE.getPassword());
		LOGGER.debug("Connected to " + SERVICE.getFtp() + ".");
		LOGGER.debug(ftp.getReplyString());

		// After connection attempt, you should check the reply code to verify
		// success.
		int reply = ftp.getReplyCode();

		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException(ftp.getReplyString());
		}

		return ftp;
	}

	/**
	 * 
	 * @return true if complete
	 */
	protected static boolean getJobStatus(String sessionId, String jobId) throws IOException {
		Document initiateDoc = DocumentHelper.createDocument();
		Element initiate = initiateDoc.addElement("Envelope").addElement("Body").addElement("GetJobStatus");
		initiate.addElement("JOB_ID").addText(jobId);

		Document doc = postXml(SERVICE.getUrl(), initiateDoc, sessionId);
		if (SERVICE.isPrintXml()) {
			System.out.println(prettyPrint(doc));
		}
		boolean success = Boolean.valueOf(doc.valueOf("/Envelope/Body/RESULT/SUCCESS")).booleanValue();
		if (success) {
			String jobStatus = doc.valueOf("/Envelope/Body/RESULT/JOB_STATUS");
			return "COMPLETE".equals(jobStatus);
		}
		return false;
	}
	
	public static boolean isAppendTimeStamp() {
		return SERVICE.isAppendTimeStamp();
	}
}