package com.freshdirect.dataloader.payment.reconciliation.paymentech;

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
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.SapFileBuilder;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechFINParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechPDEParser;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.sap.ejb.SapException;

public class PaymentechSettlementLoader {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechSettlementLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	// donot remove spaces as these message strings are padded with spaces to 96
	//										            1         2         3         4         5         6         7         8         9
	// 										   123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456
	private static final String RFR_REQUEST = "PID=944633 FRESHDIR SID=944633 DELIMITD RFR                                                     \n";
	private static final String EOF 		= "EOFEOFEOF                                                                                       \n";
	
	private static final String [] VALID_PDE_TOKENS = {"HPDE0017", "RPDE0017S", "HPDE0018", "RPDE0018S", "RPDE0018D", "HPDE0020", "RPDE0022"};
	
	private static final String NO_DATA="No data to send back at this time";
	
	public static void main(String[] args) {
		PaymentechSettlementLoader loader = new PaymentechSettlementLoader();
		
		boolean getFileFromProcessor = true;
		boolean buildOldSapFileFormat = false;
		
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				switch (i) {
					case 0:
						if ("false".equalsIgnoreCase(arg)) {
							getFileFromProcessor = false;
						}
						break;
					case 1:
						if ("true".equalsIgnoreCase(arg)) {
							buildOldSapFileFormat = true;
						}
						break;										
				}
			}			
		}
		
		try {
			loader.loadSettlement(getFileFromProcessor, buildOldSapFileFormat);
		} catch (Exception e) {
			LOGGER.fatal("Failed to load settlement", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}
	}
	
	private void loadSettlement(boolean getFileFromProcessor, boolean buildOldSapFileFormat) throws UnknownHostException, IOException, CreateException, EJBException, RemoteException, SapException {
		String timestamp = SF.format(new Date());
		//make the dfr file
		File finFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_FIN_" + timestamp + ".dfr");
		File pdeFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_PDE_" + timestamp + ".dfr");
		
		File tmpFileOne = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_tmp_1_" + timestamp + ".dfr");
		File tmpFileTwo = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_tmp_2_" + timestamp + ".dfr"); 
		
		
		if(getFileFromProcessor) {
			this.downloadFile(finFile, pdeFile, tmpFileOne, tmpFileTwo);
		}
		
		String fileName = loadFile(finFile, pdeFile, buildOldSapFileFormat);
		
		
		// now ftp the settlement File to SAP
		SettlementLoaderUtil.uploadFileToSap(fileName);

		//tell sap to the file is there
		SettlementLoaderUtil.callSettlementBapi(fileName);
		
	}
	
	private void downloadFile(File finFile, File pdeFile, File tmpFileOne, File tmpFileTwo) throws UnknownHostException, IOException {
		//download PDE/A file from paymentech
		this.downloadFile(tmpFileOne);
		//this.downloadFile(pdeFile);
		
		//download FIN/B file from paymentech
		this.downloadFile(tmpFileTwo);
		//this.downloadFile(finFile);
		
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
	
	private void downloadFile(File dfrFile) throws UnknownHostException, IOException {
		LOGGER.info("Downloading file from Paymentech - START");
		Socket paymentech = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			int batchPort = Integer.parseInt(DataLoaderProperties.getPaymentechBatchPort());
			paymentech = new Socket(DataLoaderProperties.getPaymentechBatchIp(), batchPort);
			out = new PrintWriter(paymentech.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(paymentech.getInputStream()));
			//send RFR request to Paymentech
			LOGGER.info("Sending RFR request");
			out.print(RFR_REQUEST);
			out.print(EOF);
			out.flush();
			LOGGER.info("RFR Request finished");
			FileOutputStream ofs = new FileOutputStream(dfrFile);
			BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(ofs));
			LOGGER.info("Reading file from Paymentech and storing it");
			String line = in.readLine();
			while(line != null){
				bfw.write(line);
				bfw.write("\n");
				line = in.readLine();
			}
			bfw.flush();
			bfw.close();
			LOGGER.info("Downloading file from Paymentech - END");
			
		} finally {
			if(out != null) out.close();
			if(in != null) in.close();
			if(paymentech != null) paymentech.close();
		}
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
		finParser.setClient(new PaymentechFINParserClient(builder, reconSB));
		finParser.parseFile(isFin);
		
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
