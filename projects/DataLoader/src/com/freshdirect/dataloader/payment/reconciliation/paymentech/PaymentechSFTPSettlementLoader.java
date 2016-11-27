package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.FileContext;
import com.freshdirect.dataloader.payment.PaymentFileType;
import com.freshdirect.dataloader.payment.SFTPFileProcessor;
import com.freshdirect.dataloader.payment.reconciliation.SapFileBuilder;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechFINParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechPDEParser;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;
import com.freshdirect.sap.ejb.SapException;

public class PaymentechSFTPSettlementLoader {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechSettlementLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	// donot remove spaces as these message strings are padded with spaces to 96
	//										            1         2         3         4         5         6         7         8         9
	// 										   123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456
	private static final String RFR_REQUEST = "PID=944633 FRESHDIR SID=944633 DELIMITD RFR                                                     \n";
	private static final String EOF 		= "EOFEOFEOF                                                                                       \n";
	
	private static final String [] VALID_PDE_TOKENS = {"HPDE0017", "RPDE0017S", "HPDE0018", "RPDE0018S", "RPDE0018D", "HPDE0020", "RPDE0022"};
	
	private static final String NO_DATA="No data to send back at this time";
	
	private static Date date = null;
	
	public static void main(String[] args) {
		PaymentechSFTPSettlementLoader loader = new PaymentechSFTPSettlementLoader();
		
		FileContext ctx=getFileContext(args);
		
		try {
			loader.loadSettlement(ctx);
		} catch (Exception e) {
			LOGGER.fatal("Failed to load settlement", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}
	}
	
	private static FileContext getFileContext(String[] args) {
		FileContext ctx=new FileContext();
		ctx.setFileType(PaymentFileType.DFR);
		ctx.setDownloadFiles(true);
		if (args.length >= 1) {
			for (String arg : args) {
				try { 
					if (arg.startsWith("remoteURL=")) {								
						ctx.setRemoteHost(arg.substring("remoteURL=".length())); 
					}  else if(arg.startsWith("remoteUser=")) {
						ctx.setUserName(arg.substring("remoteUser=".length()));  							
					} else if(arg.startsWith("remotePassword=")) {
						ctx.setPassword(arg.substring("remotePassword=".length()));  							
					} else if(arg.startsWith("privateKey=")) {
						ctx.setOpenSSHPrivateKey(arg.substring("privateKey=".length()));  							
					}else if(arg.startsWith("fetchFiles=")) {
						ctx.setDownloadFiles(Boolean.valueOf(arg.substring("fetchFiles=".length())).booleanValue());  							
					}
				} catch (Exception e) {
					System.err.println("Usage: java com.freshdirect.dataloader.payment.bin.PaymentechSFTPSettlementLoader  [fetchFiles={true | false}] [remoteURL=Value] [remoteUser=Value] [remotePassword=Value]  [privateKey=Value] [ppStmntProcessDate=yyyymmdd]");
					System.exit(-1);
				}
			}
		}
		
		ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
		if(StringUtils.isEmpty(ctx.getOpenSSHPrivateKey()))
			ctx.setOpenSSHPrivateKey(DataLoaderProperties.getWorkingDir()+DataLoaderProperties.getPaymentSFTPKey());
		if(StringUtils.isEmpty(ctx.getRemoteHost()))
			ctx.setRemoteHost(DataLoaderProperties.getPaymentSFTPHost());
		if(StringUtils.isEmpty(ctx.getUserName()))
			ctx.setUserName(DataLoaderProperties.getPaymentSFTPUser());
		if(StringUtils.isEmpty(ctx.getPassword()))
			ctx.setPassword(DataLoaderProperties.getPaymentSFTPPassword());
		LOGGER.info( "FileContext: "+ ctx );
		return ctx;
		
	}

	private void loadSettlement(FileContext ctx) throws UnknownHostException, IOException, CreateException, EJBException, RemoteException, SapException {
		String timestamp = SF.format(new Date());
		//make the dfr file
		File finFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_FIN_" + timestamp + ".dfr");
		File pdeFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_PDE_" + timestamp + ".dfr");
		
		File tmpFileOne = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_tmp_1_" + timestamp + ".dfr");
		File tmpFileTwo = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_tmp_2_" + timestamp + ".dfr"); 
		
		
		if(ctx.downloadFiles()) {
			this.downloadFile(ctx,finFile, pdeFile, tmpFileOne, tmpFileTwo);
		}

		String fileName = loadFile(finFile, pdeFile, false);
		
		
		// now ftp the settlement File to SAP
		SettlementLoaderUtil.uploadFileToSap(fileName);

		//tell sap to the file is there
		SettlementLoaderUtil.callSettlementBapi(fileName);

	}
	
	private void downloadFile(FileContext ctx,File finFile, File pdeFile, File tmpFileOne, File tmpFileTwo) throws UnknownHostException, IOException {
		//download PDE/A file from paymentech
		
			
			SFTPFileProcessor fp=new SFTPFileProcessor(ctx);
			List<String> files=fp.getFiles();
			if(files.size()<2) {
				throw new FDRuntimeException("Paymentech Reconciliation file does not have any data");
			}
			File _tmpFileOne = new File(DataLoaderProperties.getWorkingDir() + files.get(0));
			File _tmpFileTwo = new File(DataLoaderProperties.getWorkingDir() + files.get(1));
			_tmpFileOne.renameTo(tmpFileOne);
			_tmpFileTwo.renameTo(tmpFileTwo);
			
		
		InputStream is = new FileInputStream(tmpFileOne);
		BufferedReader lines = new BufferedReader(new InputStreamReader(is));
		
		String line = null;
		if((line = lines.readLine()) != null){
			if(line!= null && line.trim().startsWith("*DFRBEG")){
				if((line = lines.readLine()) != null){
					lines.close();
					is.close();
					if(line != null && !line.trim().startsWith("*DFREND")) {
						String[] tokens = line.split("\\|", 2);
						if(isValidPDEToken(tokens[0])){
							tmpFileOne.renameTo(pdeFile);
							tmpFileTwo.renameTo(finFile);
						} else {
							tmpFileOne.renameTo(finFile);
							tmpFileTwo.renameTo(pdeFile);
						}
					}else {
						tmpFileOne.renameTo(pdeFile);
						tmpFileTwo.renameTo(finFile);
					}
				}else{
					LOGGER.error("Incomplete reconciliation file");
					throw new FDRuntimeException("Incomplete reconciliation file");
				}
			}else{
				LOGGER.error("Got Paymentech file with unrecognized Data");
				if(line!=null && line.indexOf(NO_DATA)!=-1) {
					StringBuilder msg= new StringBuilder(300);
					msg.append("Paymentech Reconciliation file does not have any data at this time.\n");
					msg.append("Please verify that the settlement batch was submitted for the previous day.");
					msg.append("If settlement batch was submitted the previous day, and it contained orders,\n");
					msg.append("Please re-run the settlement loader job after 2 hours and if it still fails,");
					msg.append("Please contact Paymentech Support at 6038968320.");
					
					throw new FDRuntimeException(msg.toString());
				} else { 
					throw new FDRuntimeException("Paymentech File contained unrecognized data: "+line);
				}
			}
		} else {
			LOGGER.error("Got Empty Files from Paymentech");
			throw new FDRuntimeException("Paymentech Reconciliation file does not have any data");
		}
		
	}
	
	private boolean isValidPDEToken (String token) {
		
		for (String element : VALID_PDE_TOKENS) {
			if(element.equalsIgnoreCase(token)) {
				return true;
			}
		}
		
		return false;
	}
	
	private String loadFile(File finFile, File pdeFile, boolean buildOldSapFileFormat) throws RemoteException, EJBException, CreateException, IOException {
		
		ReconciliationSB reconSB = SettlementLoaderUtil.lookupReconciliationHome().create();
		
		SapFileBuilder builder = new SapFileBuilder();
		builder.setBuildOldSapFileFormat(buildOldSapFileFormat);
		String fileName = DataLoaderProperties.getSapFileNamePrefix() + "_Paymentech_" + SF.format(new Date()) + ".txt";
		File f = new File(DataLoaderProperties.getWorkingDir() + fileName);
		InputStream isFin = null;
		InputStream isPde = null;
		try{
		LOGGER.info("starting to load FIN File");
		
		isFin = new FileInputStream(finFile);
		
		PaymentechFINParser finParser = new PaymentechFINParser();
		PayPalReconciliationSB ppReconSB = SettlementLoaderUtil.lookupPPReconciliationHome().create();
		List<String> ppSettlementIds = null;
		if (DataLoaderProperties.isPayPalSettlementEnabled()) {
			ppSettlementIds = ppReconSB.acquirePPLock(null);
			finParser.setClient(new PaymentechFINParserClient(builder, reconSB, ppReconSB, ppSettlementIds));
		} else {
			finParser.setClient(new PaymentechFINParserClient(builder, reconSB));
		}
		
		finParser.parseFile(isFin);
		
		if (DataLoaderProperties.isPayPalSettlementEnabled()) {
			ppReconSB.releasePPLock(ppSettlementIds);
		}
		
		LOGGER.info("Finished loading FIN File");
		//mask CC number in the downloaded FIN file.
		SettlementLoaderUtil.maskCCPaymentech(finFile);
		LOGGER.info("Finished masking cc number for File "+finFile.getName());
		
		LOGGER.info("starting to load PDE File");
		
		isPde = new FileInputStream(pdeFile);
		
		PaymentechPDEParser pdeParser = new PaymentechPDEParser();
		pdeParser.setClient(new PaymentechPDEParserClient(builder, reconSB));
		pdeParser.parseFile(isPde);
		
		LOGGER.info("Finished loading PDE File");
		//mask CC number in the downloaded PDE file.
		SettlementLoaderUtil.maskCCPaymentech(pdeFile);
		LOGGER.info("Finished masking cc number for File "+pdeFile.getName());
		
		}finally{
			if(isFin != null) isFin.close();
			if(isPde != null) isPde.close();
			builder.writeTo(f);
		}
		return fileName;
	}
	
}
