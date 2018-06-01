package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.FileContext;
import com.freshdirect.dataloader.payment.PaymentFileType;
import com.freshdirect.dataloader.payment.SFTPFileProcessor;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.paypal.parsers.PayPalParser;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;
import com.freshdirect.payment.service.FDECommerceService;

public class PayPalSFTPSettlementLoader {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PayPalSFTPSettlementLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyyMMdd");
	
	static String timestamp = SF.format(new Date());
	private static boolean downloadFlag = true;
	
	private static final String INFIX = ".A";
	
	static PayPalReconciliationSB ppReconSB = null;
	static ReconciliationSB reconSB = null;
	static List<String> settlementIds = null;
	static boolean isNEwRecordRequired = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("***********************************************************************************");
		System.out.println("***   PayPal Settlement process start  (Batch Size is in props)                 ***");
		System.out.println("***********************************************************************************");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("------");
		System.out.println(" java PayPalSFTPSettlementLoader download_file rundate(yyyymmdd) override_lock ");
		System.out.println();
		System.out.println("default:");
		System.out.println(" java PayPalSFTPSettlementLoader true " +  SF.format(new Date()) + " false ");
		System.out.println("***********************************************************************************");
		
		if (args.length > 0) {
			if ("false".equalsIgnoreCase(args[0])) {
				downloadFlag = false;
			}
		}
		
		if (args.length > 1) {
			try {
				timestamp = SF.format(SF.parse(args[1]));
			} catch (ParseException pe) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE, -1);
				timestamp = SF.format(cal.getTime());
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1);
			timestamp = SF.format(cal.getTime());
		}
		
		LOGGER.info("Arguments are : downloadFlag - " + downloadFlag + " Date being processed - " + timestamp);

		try {
			PayPalSFTPSettlementLoader loader = new PayPalSFTPSettlementLoader();
			
			
			Map<String, Object> lockInfo = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
				lockInfo = FDECommerceService.getInstance().acquirePPLock(SF.parse(timestamp));
			} else {
				ppReconSB = SettlementLoaderUtil.lookupPPReconciliationHome().create();
				lockInfo = ppReconSB.acquirePPLock(SF.parse(timestamp));
			}
			
			settlementIds = (List<String>) lockInfo.get("settlementIds");
			
			PayPalSFTPSettlementLoader.isNEwRecordRequired = (Boolean) lockInfo.get("isNewRecord");
			
			loader.loadSettlements();
			if (settlementIds != null && !settlementIds.isEmpty()) {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
					FDECommerceService.getInstance().releasePPLock(settlementIds);
				} else {
					ppReconSB.releasePPLock(settlementIds);
				}
			}
				
		} catch (Exception e) {
			try {
				if (settlementIds != null && !settlementIds.isEmpty()){
					if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
						FDECommerceService.getInstance().releasePPLock(settlementIds);
					} else {
						ppReconSB.releasePPLock(settlementIds);
					}
				}
			} catch (Exception e2) {
				LOGGER.info("Exception while releasing PP lock can be ignored ", e2);
			}
			LOGGER.fatal("Failed to load settlement", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}

	}

	private void loadSettlements() throws RemoteException, EJBException, IOException, CreateException, URISyntaxException,
										ParseException {
		LOGGER.debug("Loading settlements ");
		List<File> files = null;
		if (downloadFlag) {
			files = downloadFiles();
		}
		
		if (files == null || files.isEmpty()) {
			File dir = new File(DataLoaderProperties.getWorkingDir());
			files = Arrays.asList(dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.startsWith(DataLoaderProperties.getPayPalStlmntFilePrefix() + timestamp + INFIX) &&
							(name.endsWith(DataLoaderProperties.getPayPalStlmntFileSuffix() +
												DataLoaderProperties.getPayPalStlmntFileExtn()) || name.endsWith(DataLoaderProperties.getPayPalStlmntFileSuffix() +
														DataLoaderProperties.getPayPalStlmntFileExtn().toLowerCase())))
						return true;
					return false;
				}
			}));
		}
		
		if (files.size() == 0) {
			LOGGER.warn("No file exists for today ");
		}
		for (File file: files) {
			//sanityCheck(file);
			//load the files in ERPS
			loadFile(file);
		}
		LOGGER.debug("End of Loading settlements ");
	}
	
	List<File> downloadFiles() throws IOException, URISyntaxException {
		LOGGER.info("started download of PayPal files from SFTP server");
		FileContext ctx = getFileContext();
		SFTPFileProcessor fp=new SFTPFileProcessor(ctx);
		List<File> files=fp.getPPFiles();
		if (files.isEmpty()) {
			LOGGER.error("No files exist for today or given date " + timestamp);
			throw new FDRuntimeException("No files exist for today or given date " +
												timestamp);
		}
		
		LOGGER.info("finished download of PayPal files from SFTP server");
		return files;
	}
	
	void sanityCheck(File file) throws IOException {
		InputStream is = null;
		BufferedReader lines = null;
		try {
			is = new FileInputStream(file);
			lines = new BufferedReader(new InputStreamReader(is));
			
			String line = null;
			if((line = lines.readLine()) != null){
				if(line.trim().startsWith("\"RH")){
					if((line = lines.readLine()) == null){
						LOGGER.error("Incomplete reconciliation file");
						throw new FDRuntimeException("Incomplete reconciliation file");
					}
				}else{
					LOGGER.error("Got PayPal file with unrecognized Data" + line);
					throw new FDRuntimeException("Paymentech File contained unrecognized data: "+line);
				}
			} else {
				LOGGER.error("Got Empty Files from PayPal");
				throw new FDRuntimeException("PayPal Reconciliation file does not have any data");
			}
		} catch (IOException e) {
			if (is != null)
				is.close();
			if (lines != null)
				lines.close();
		}
	}
	
	FileContext getFileContext() {
		FileContext ctx = new FileContext();
		ctx.setFileType(PaymentFileType.PAYPAL_SETTLEMENT);
		ctx.setPayPalFileDate(timestamp);
		ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
		ctx.setRemoteHost(DataLoaderProperties.getPayPalFtpIp());
		ctx.setUserName(DataLoaderProperties.getPayPalFtpUser());
		ctx.setPassword(DataLoaderProperties.getPayPalFtpPassword());
		return ctx;
	}
	
	private void loadFile(File file) 
			throws IOException, RemoteException, EJBException, CreateException, ParseException {

		LOGGER.info("starting to load PayPal settlement file");
		InputStream is = new FileInputStream(file);

		//create parser and set loader as its client
		PayPalParser parser = new PayPalParser();
		parser.setClient(new PayPalSettlementParserClient(null, reconSB, ppReconSB, settlementIds));
		
		parser.parseFile(is);

		is.close();
		
		LOGGER.info("finished loading PayPal settlement file" + file.getAbsolutePath());
	}
	
	
	/** 
	 * helper method to find the naming context for locating objects on a server
	 * 
	 * @throws NamingException any problems encountered locating the remote server
	 * @return the naming context to use to locate remote components on the server
	 */
	protected Context getInitialContext() throws NamingException {

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL()); //t3://localhost:7006
		env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
		return new InitialContext(env);

	}
}
