package com.freshdirect.dataloader.payment.reconciliation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.sap.command.SapSendSettlement;
import com.freshdirect.sap.ejb.SapException;

public class SettlementLoaderUtil {

	private static final Category LOGGER = LoggerFactory.getInstance(SettlementLoaderUtil.class);

	private static final boolean DEBUG = false;
	private static final String [] VALID_PDE_TOKENS = {"RPDE0017D",  "RPDE0018D", "RPDE0020", "RPDE0022"};
	private static final String [] VALID_FIN_TOKENS = {"RACT0010"};

	public static void main(String[] args) throws Exception {
		if (args.length == 0 || args.length != 2) {
			printHelpMessage();
			System.exit(0);
		}

		String s = args[0];
		String fileName = args[1];
		if (s == null || "".equals(s) || !s.trim().startsWith("-") || fileName == null || "".equals(fileName)) {
			printHelpMessage();
		}

		s = s.substring(1, s.length());

		if ("h".equals(s) || s.startsWith("h")) {
			System.out.println(s + " will only print help message");
			printHelpMessage();
			System.exit(0);
		}

		char[] options = s.toCharArray();
		boolean ftp = false;
		boolean bapi = false;
		boolean mask = false;
		for (int i = 0; i < options.length; i++) {
			char c = options[i];
			if ('f' == c) {
				ftp = true;
			}
			if ('b' == c) {
				bapi = true;
			}
			if ('m' == c) {
				mask = true;
			}
		}
		if (!ftp && !bapi && !mask) {
			System.out.println("Please select atleast one action to perform [f][b][m]");
			printHelpMessage();
		}

		if (ftp) {
			uploadFileToSap(fileName);
		}
		if (bapi) {
			callSettlementBapi(fileName);
		}
		if (mask) {
			maskFile(fileName);
		}

	}
	
	public static void maskFile(String fileName) throws Exception {
		File dir = new File(fileName);
		if(!dir.isDirectory()){
			if(dir.getName().indexOf("PDE")>=0 ||dir.getName().indexOf("FIN")>=0){
				maskCCPaymentech(dir);
				System.out.println("This file's CC is masked:  "+dir.getAbsolutePath());
			}else if(dir.getName().indexOf("detail_file_")>=0){
				maskCCDetail(dir);
				System.out.println("This file's CC is masked:  "+dir.getAbsolutePath());
			}
		}
		else{
			File[] list = dir.listFiles();
			for(int i=0; i<list.length;i++){
				if(list[i].getName().indexOf("PDE")>=0 ||list[i].getName().indexOf("FIN")>=0){
					maskCCPaymentech(list[i]);
					System.out.println("This file's CC is masked:  "+list[i].getAbsolutePath());
				}else if(list[i].getName().indexOf("detail_file_")>=0){
					maskCCDetail(list[i]);
					System.out.println("This file's CC is masked:  "+list[i].getAbsolutePath());
				}
			}
		}
	}

	public static void uploadFileToSap(String fileName) throws IOException {

		LOGGER.info("started uploading file to sap");
		FTPClient client = new FTPClient();
		//5 minutes
		client.setDefaultTimeout(600000);
		client.setDataTimeout(600000);
		try {
			boolean ok = true;
			FileInputStream ifs = null;

			if (DEBUG)
				LOGGER.debug("connecting...");

			client.connect(DataLoaderProperties.getSapFtpIp());
			int reply = client.getReplyCode();
			ok = FTPReply.isPositiveCompletion(reply);
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging in...");
				ok = client.login(DataLoaderProperties.getSapFtpUser(), DataLoaderProperties.getSapFtpPassword());
			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("uploading reconciliation file... " + fileName);

				ifs = new FileInputStream(new File(DataLoaderProperties.getWorkingDir() + fileName));
				ok = client.storeFile(fileName, ifs);

			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging out...");
				client.logout();
			}
		} finally {
			if (client.isConnected()) {
				try {
					if (DEBUG)
						LOGGER.debug("disconnecting...");
					client.disconnect();
				} catch (IOException ie) {
					LOGGER.warn("IOException while trying to cleanup after FTP", ie);
				}
			}
			if (DEBUG)
				LOGGER.debug("done...");
		}

		LOGGER.info("finished uploading the file to sap");
	}

	public static void callSettlementBapi(String fileName) throws SapException {
		SapSendSettlement command = new SapSendSettlement(fileName, DataLoaderProperties.getSapUploadFolder());
		command.execute();
	}

	public static ReconciliationHome lookupReconciliationHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
	
	public static void sendSettlementFailureEmail(Exception e) {
		
		StringWriter sw = new StringWriter();
		
		sw.write("---------------------------------------------\n");
		sw.write("Settlement Failed (");
		sw.write(new SimpleDateFormat().format(new Date()));
		sw.write(")\n");
		sw.write("---------------------------------------------\n");
		
		sw.write(e.getMessage() + "\n");
		e.printStackTrace(new PrintWriter(sw));
		
		ErpMailSender mailer = new ErpMailSender();
		
		try {
			mailer.sendMail(ErpServicesProperties.getSapMailFrom(), ErpServicesProperties.getSapMailTo(), ErpServicesProperties.getSapMailCC(), "Settlement Failure", sw.getBuffer().toString());
		} catch (MessagingException me) {
			LOGGER.fatal("Could not send a email for Settlement Failure", me);
		}
		
	}

	public static void maskCCPaymentech(File file) throws IOException{
		String oldName = file.getAbsolutePath();
		File tempFile = new File(DataLoaderProperties.getWorkingDir() +"temp.dfr");
		file.renameTo(tempFile);
		InputStream is = new FileInputStream(tempFile);
		BufferedReader lines = new BufferedReader(new InputStreamReader(is));
		File newFile = new File(oldName);
		
		FileOutputStream ofs = new FileOutputStream(newFile);
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(ofs));
		String maskedLine = null;
		String line = lines.readLine();
		
		while(line != null){
			String[] tokens =line.split("\\|",2);
			if(isValidPDEToken(tokens[0])){
				maskedLine = maskPDECreditCard(line);
				bfw.write(maskedLine);
				bfw.write("\n");
			}else if(isValidFINToken(tokens[0])){
				maskedLine = maskFINCreditCard(line);
				bfw.write(maskedLine);
				bfw.write("\n");
			}else{
				bfw.write(line);
				bfw.write("\n");
			}
			line = lines.readLine();
		}
		bfw.flush();
		bfw.close();
		is.close();
		lines.close();
		tempFile.delete();
	}
	
	public static void maskCCDetail(File file) throws IOException{
		String oldName = file.getAbsolutePath();
		File tempFile = new File(DataLoaderProperties.getWorkingDir() +"temp.dfr");
		file.renameTo(tempFile);
		InputStream is = new FileInputStream(tempFile);
		BufferedReader lines = new BufferedReader(new InputStreamReader(is));
		File newFile = new File(oldName);
		
		FileOutputStream ofs = new FileOutputStream(newFile);
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(ofs));
		String maskedLine = null;
		String line = lines.readLine();
		
		while(line != null){
			
			if(line.charAt(0)=='1'){
				maskedLine = maskDetailCreditCard(line);
				bfw.write(maskedLine);
				bfw.write("\n");
			}else{
				bfw.write(line);
				bfw.write("\n");
			}
			line = lines.readLine();
		}
		bfw.flush();
		bfw.close();
		is.close();
		lines.close();
		tempFile.delete();
	}

	private static void printHelpMessage() {
		System.out.println("USAGE: java com.freshdirect.dataloader.payment.reconciliation SettlementLoaderUtil -[h][f][b][m] fully/qualified/filename");
		System.out.println("h = print this help message");
		System.out.println("f = ftp file to SAP");
		System.out.println("b = make the Settlement BAPI call");
		System.out.println("m = mask CC number for all the files in specified directory or a spcific file");
		System.out.println("one of these options is required and order and case is important and h cannot be combined with f and b and m");
	}

	/** 
	 * helper method to find the naming context for locating objects on a server
	 * 
	 * @throws NamingException any problems encountered locating the remote server
	 * @return the naming context to use to locate remote components on the server
	 */
	private static Context getInitialContext() throws NamingException {

		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL()); //t3://localhost:7006
		env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
		return new InitialContext(env);

	}
	
	private static String maskPDECreditCard(String line){
		String[] tokens = line.split("\\|",0);
		String cc = tokens[10];
		if(cc.indexOf("*")<0){
			String maskedCC = maskCreditCard(cc);
			String newLine = line.replaceAll(cc,maskedCC);
			return newLine;
		}
		else
			return line;
	}

	private static String maskFINCreditCard(String line){
		String[] tokens = line.split("\\|",0);
		String cc = tokens[11];
		if(cc.indexOf("*")<0){
			String maskedCC = maskCreditCard(cc);
			String newLine = line.replaceAll(cc,maskedCC);
			return newLine;
		}else
			return line;
	}
	private static String maskDetailCreditCard(String line){

		String cc = line.substring(4,22).trim();
		if(cc.indexOf("*")<0){
			String maskedCC = maskCreditCard(cc);
			String newLine = line.replaceAll(cc,maskedCC);
			return newLine;
		}else
			return line;
	}

	
	private static String maskCreditCard(String cc){
		int length = cc.length();
		if(length<=4){
			return cc;
		}
		else{
			StringBuffer temp = new StringBuffer();
			for(int i=0;i<length-4;i++){
				temp =temp.append("*");
			}
			return temp.append(cc.substring(length-4)).toString();
		}
	}
	private static boolean isValidPDEToken (String token) {
		
		for(int i = 0; i < VALID_PDE_TOKENS.length; i++) {
			if(VALID_PDE_TOKENS[i].equalsIgnoreCase(token)) {
				return true;
			}
		}
		return false;
	}
	private static boolean isValidFINToken (String token) {
		
		for(int i = 0; i < VALID_FIN_TOKENS.length; i++) {
			if(VALID_FIN_TOKENS[i].equalsIgnoreCase(token)) {
				return true;
			}
		}
		return false;
	}

	
}
