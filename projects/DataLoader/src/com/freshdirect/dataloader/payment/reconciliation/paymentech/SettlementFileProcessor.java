package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.FileContext;
import com.freshdirect.dataloader.payment.SFTPFileProcessor;
import com.freshdirect.dataloader.payment.reconciliation.SapFileBuilder;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechFINParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers.PaymentechPDEParser;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;
import com.freshdirect.sap.ejb.SapException;

public class SettlementFileProcessor {

	private static final Category LOGGER = LoggerFactory
			.getInstance(PaymentechSettlementLoader.class);
	private static final String[] VALID_PDE_TOKENS = { "HPDE0017", "RPDE0017S", "HPDE0018",
			"RPDE0018S", "RPDE0018D", "HPDE0020", "RPDE0022" };

	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	private static final String NO_DATA = "No data to send back at this time";

	private static final String FILE_NAME_A = "A";
	private static final String FILE_NAME_B = "B";

	/**
	 * @param ctx File context
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws SapException
	 * @throws CreateException
	 */
	public static void processFiles(FileContext ctx) throws UnknownHostException, IOException,
			SapException, CreateException {
		
		File finFile = null;
		File pdeFile = null;
		File tmpFileOne = null;
		File tmpFileTwo = null;

		String tempFileName = null;
 
		// download PDE/A file from paymentech
		List<String> files = downloadFile(ctx);


		if (files.size() <= 0) {
			throw new FDRuntimeException("Paymentech Reconciliation file does not have any data");
		} else if (files.size() == 1) {
			//If file size is one then try to find ot's alternative file from local repository 
			// if it exists then process settlement else throw exception
			tempFileName = retreiveAlternateFile(files.get(0));
			if (null == tempFileName) {
				throw new FDRuntimeException(
						"Paymentech Reconciliation returned only one file. Please re run the job after two hours");
			}
			files.add(tempFileName);
		}

		//create file map and assign all the file processed as false by default .
		
		Map<String, Boolean> fileProcessors = new HashMap<String, Boolean>();

		populateDefaultFiles(fileProcessors, files);

		// Process list of files 
		for (String file : files) {
			// Check whether the file is processed or not
			if (null != fileProcessors.get(file) && !fileProcessors.get(file).booleanValue()) {
				tempFileName = retreiveAlternateFile(file);
				if (null != tempFileName) {
					String timestamp = SF.format(new Date());
					// make the dfr file
					finFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_FIN_"
							+ timestamp + ".dfr");
					pdeFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_PDE_"
							+ timestamp + ".dfr");

					tmpFileOne = new File(DataLoaderProperties.getWorkingDir()
							+ "Paymentech_tmp_1_" + timestamp + ".dfr");
					tmpFileTwo = new File(DataLoaderProperties.getWorkingDir()
							+ "Paymentech_tmp_2_" + timestamp + ".dfr");

					// Follow the existing process
					File _tmpFileOne = new File(DataLoaderProperties.getWorkingDir() + file);
					File _tmpFileTwo = new File(DataLoaderProperties.getWorkingDir() + tempFileName);
					renameFile(tmpFileOne, _tmpFileOne);
					renameFile(tmpFileTwo, _tmpFileTwo);
					readFile(finFile, pdeFile, tmpFileOne, tmpFileTwo);

					// process file and mark sales orders as settled
					
					String fileName = loadFile(finFile, pdeFile, false);

					// now ftp the settlement File to SAP
					SettlementLoaderUtil.uploadFileToSap(fileName);

					// tell sap to the file is there
					 SettlementLoaderUtil.callSettlementBapi(fileName);

					// Mark the both files as processed 
					fileProcessors.put(file, true);
					fileProcessors.put(tempFileName, true);

				}

			}
		}

		// log error message for the file that are not processed
		logNotProcessedFile(fileProcessors);

	}

	/**
	 * Download files from PaymentTech SFTP HOST or from local repository
	 * @param ctx
	 * @return
	 * @throws IOException
	 */
	private static List<String> downloadFile(FileContext ctx) throws IOException {
		SFTPFileProcessor fp = new SFTPFileProcessor(ctx);
		List<String> files=new ArrayList<String>();
		//Process Extracted Files from local repository 
		//Format 232318.0000080782.012016.d.A225.dfr 
		if("true".equalsIgnoreCase(DataLoaderProperties.isProcessLocalSettlementFiles())){
			 files = fp.getLocalFiles();	
		}else{ //download it from SFTP 
			 files = fp.getFiles();
		}
		return files;
	}

	private static String loadFile(File finFile, File pdeFile, boolean buildOldSapFileFormat)
			throws RemoteException, EJBException, CreateException, IOException {

		ReconciliationSB reconSB = SettlementLoaderUtil.lookupReconciliationHome().create();

		SapFileBuilder builder = new SapFileBuilder();
		builder.setBuildOldSapFileFormat(buildOldSapFileFormat);
		String fileName = DataLoaderProperties.getSapFileNamePrefix() + "_Paymentech_"
				+ SF.format(new Date()) + ".txt";
		File f = new File(DataLoaderProperties.getWorkingDir() + fileName);
		InputStream isFin = null;
		InputStream isPde = null;
		try {
			LOGGER.info("starting to load FIN File");

			isFin = new FileInputStream(finFile);

			PaymentechFINParser finParser = new PaymentechFINParser();
			PayPalReconciliationSB ppReconSB = SettlementLoaderUtil.lookupPPReconciliationHome()
					.create();
			List<String> ppSettlementIds = null;
			if (DataLoaderProperties.isPayPalSettlementEnabled()) {
				ppSettlementIds = ppReconSB.acquirePPLock(null);
				finParser.setClient(new PaymentechFINParserClient(builder, reconSB, ppReconSB,
						ppSettlementIds));
			} else {
				finParser.setClient(new PaymentechFINParserClient(builder, reconSB));
			}

			finParser.parseFile(isFin);

			if (DataLoaderProperties.isPayPalSettlementEnabled()) {
				ppReconSB.releasePPLock(ppSettlementIds);
			}

			LOGGER.info("Finished loading FIN File");
			// mask CC number in the downloaded FIN file.
			SettlementLoaderUtil.maskCCPaymentech(finFile);
			LOGGER.info("Finished masking cc number for File " + finFile.getName());

			LOGGER.info("starting to load PDE File");

			isPde = new FileInputStream(pdeFile);

			PaymentechPDEParser pdeParser = new PaymentechPDEParser();
			pdeParser.setClient(new PaymentechPDEParserClient(builder, reconSB));
			pdeParser.parseFile(isPde);

			LOGGER.info("Finished loading PDE File");
			// mask CC number in the downloaded PDE file.
			SettlementLoaderUtil.maskCCPaymentech(pdeFile);
			LOGGER.info("Finished masking cc number for File " + pdeFile.getName());

		} finally {
			if (isFin != null)
				isFin.close();
			if (isPde != null)
				isPde.close();
			builder.writeTo(f);
		}
		return fileName;
	}

	/**
	 * @param fileProcessors
	 */
	private static void logNotProcessedFile(Map<String, Boolean> fileProcessors) {

		boolean throwException = false;
		for (Map.Entry<String, Boolean> fileMap : fileProcessors.entrySet()) {

			if (!fileMap.getValue().booleanValue()) {
				LOGGER.error("Paymentech Reconciliation : File is not processed . Please re run after two hours"
						+ fileMap.getKey());
				throwException = true;
			}
		}

		if (throwException) {
			throw new FDRuntimeException(
					"Paymentech Reconciliation returned only one file. Please re run the job after two hours");
		}
	}

	private static void populateDefaultFiles(Map<String, Boolean> fileProcessors, List<String> files) {
		for (String file : files) {
			fileProcessors.put(file, false);
		}
	}

	/**
	 * Given a file name check whether it's corresponding alternate file exist
	 * or not on local repository 
	 * Example File format : 
	 *  File A: 232318.0000080782.012016.d.A225.dfr,
	 *  File B: 232318.0000080782.012016.d.B225.dfr
	 * @param fileName
	 * @return
	 */
	private static String retreiveAlternateFile(String fileName) {
		StringBuffer downloadedFileName = new StringBuffer(fileName);
		String subFile=null;
		if (fileName.indexOf(FILE_NAME_A) > 0) {
			downloadedFileName.setCharAt(fileName.indexOf(FILE_NAME_A), 'B');
			 subFile=downloadedFileName.substring(0, fileName.indexOf(FILE_NAME_A)+1);
		} else if (fileName.indexOf(FILE_NAME_B) > 0) {
			downloadedFileName.setCharAt(fileName.indexOf(FILE_NAME_B), 'A');
			 subFile=downloadedFileName.substring(0, fileName.indexOf(FILE_NAME_B)+1);

		}
		File file = new File(DataLoaderProperties.getWorkingDir());
        for(File f: file.listFiles()){
        	 if(!f.getName().contains(".zip") && !f.getName().contains("_resp")&& f.getName().contains(".dfr") && f.getName().contains(subFile)){
        		 return f.getName();
        	 }
        }
		


		return null;
	}

	/**
	 * @param finFile
	 * @param pdeFile
	 * @param tmpFileOne
	 * @param tmpFileTwo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws FDRuntimeException
	 */
	private static void readFile(File finFile, File pdeFile, File tmpFileOne, File tmpFileTwo)
			throws FileNotFoundException, IOException, FDRuntimeException {

		InputStream is = new FileInputStream(tmpFileOne);
		BufferedReader lines = new BufferedReader(new InputStreamReader(is));

		readContent(finFile, pdeFile, tmpFileOne, tmpFileTwo, is, lines);
	}

	/**
	 * @param finFile
	 * @param pdeFile
	 * @param tmpFileOne
	 * @param tmpFileTwo
	 * @param is
	 * @param lines
	 * @throws IOException
	 * @throws FDRuntimeException
	 */
	private static void readContent(File finFile, File pdeFile, File tmpFileOne, File tmpFileTwo,
			InputStream is, BufferedReader lines) throws IOException, FDRuntimeException {
		String line = null;
		if ((line = lines.readLine()) != null) {
			if (line != null && line.trim().startsWith("*DFRBEG")) {
				if ((line = lines.readLine()) != null) {
					lines.close();
					is.close();
					if (line != null && !line.trim().startsWith("*DFREND")) {
						String[] tokens = line.split("\\|", 2);
						if (isValidPDEToken(tokens[0])) {
							renameFile(pdeFile, tmpFileOne);
							renameFile(finFile, tmpFileTwo);
						} else {
							renameFile(finFile, tmpFileOne);
							renameFile(pdeFile, tmpFileTwo);
						}
					} else {
						renameFile(pdeFile, tmpFileOne);
						renameFile(finFile, tmpFileTwo);
					}
				} else {
					LOGGER.error("Incomplete reconciliation file");
					throw new FDRuntimeException("Incomplete reconciliation file");
				}
			} else {
				LOGGER.error("Got Paymentech file with unrecognized Data");
				if (line != null && line.indexOf(NO_DATA) != -1) {
					StringBuilder msg = new StringBuilder(300);
					msg.append("Paymentech Reconciliation file does not have any data at this time.\n");
					msg.append("Please verify that the settlement batch was submitted for the previous day.");
					msg.append("If settlement batch was submitted the previous day, and it contained orders,\n");
					msg.append("Please re-run the settlement loader job after 2 hours and if it still fails,");
					msg.append("Please contact Paymentech Support at 6038968320.");

					throw new FDRuntimeException(msg.toString());
				} else {
					throw new FDRuntimeException("Paymentech File contained unrecognized data: "
							+ line);
				}
			}
		} else {
			LOGGER.error("Got Empty Files from Paymentech");
			throw new FDRuntimeException("Paymentech Reconciliation file does not have any data");
		}
	}

	/**
	 * @param tmpFileOne
	 * @param _tmpFileOne
	 */
	private static void renameFile(File tmpFileOne, File _tmpFileOne) {
		_tmpFileOne.renameTo(tmpFileOne);
	}

	private static boolean isValidPDEToken(String token) {

		for (String element : VALID_PDE_TOKENS) {
			if (element.equalsIgnoreCase(token)) {
				return true;
			}
		}

		return false;
	}

}
