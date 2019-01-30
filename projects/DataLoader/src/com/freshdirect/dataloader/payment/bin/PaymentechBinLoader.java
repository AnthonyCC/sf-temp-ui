package com.freshdirect.dataloader.payment.bin;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.sap.ejb.SapException;

public class PaymentechBinLoader /*implements BINLoader*/ {
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechBinLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	                                                                                    
	       //										            1         2         3         4         5         6         7         8
	      // 										   12345678901234567890123456789012345678901234567890123456789012345678901234567890
	private static final String BIN_RFR_REQUEST = "PID=226042 FRESHRFR SID=226042 FRESHBIN RFR                                    \n";
	private static final String BIN_EOF 		= "EOFEOFEOF                                                                       \n";
	
	private static final String NO_DATA="No data to send back at this time";
	
	public static void main(String[] args) {
		PaymentechBinLoader loader = new PaymentechBinLoader();
		
		boolean getFileFromProcessor = true;
		
		
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				switch (i) {
					case 0:
						if ("false".equalsIgnoreCase(arg)) {
							getFileFromProcessor = false;
						}
						break;
														
				}
			}			
		}
		
		try {
			loader.loadBINs(getFileFromProcessor);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.fatal("Failed to load bin files", e);
			//SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}
	}
	
	private void loadBINs(boolean getFileFromProcessor) throws UnknownHostException, IOException, CreateException, EJBException, RemoteException, SapException, FDResourceException {
		String timestamp = SF.format(new Date());
		//String timestamp ="2013_05_30_10_24";
		//make the dfr file
		System.out.println("DataLoaderProperties.getWorkingDir() :"+DataLoaderProperties.getWorkingDir());
		File visaBinFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_VISA_" + timestamp + ".dfr");
		File mcBinFile = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_MC_" + timestamp + ".dfr");
		
		
		
		if(!getFileFromProcessor) {
			this.downloadFile(visaBinFile, mcBinFile);
		}
		loadBinFiles(visaBinFile, mcBinFile);
		//String fileName = loadFile(visaFile, mcFile, buildOldSapFileFormat);
		
		
		// now ftp the settlement File to SAP
		//SettlementLoaderUtil.uploadFileToSap(fileName);

		//tell sap to the file is there
		//SettlementLoaderUtil.callSettlementBapi(fileName);
		
	}
	
	public  void parseFile(InputStream fileStream, BINContext context) {
		
		BufferedReader lines = null;
		boolean processedHeader=false;
		List<BINInfo> binInfos=new ArrayList<BINInfo>(5000);
		int recordCount=0;
		int counter=0;
		
		String seq="";
		String lowRange="";
		String highRange="";
		try {
			lines = new BufferedReader(new InputStreamReader(fileStream));
			String line = null;
			int lineNumber = 0;
			while (null != (line = lines.readLine())) {
				++lineNumber;
				if ("".equals(line.trim()) || line.startsWith("#")){
					continue;
				}
				
				if(line.length()!=80) {
					String msg="Error at line " + lineNumber + ": Invalid data"; 
					LOGGER.debug(msg);
					context.addException(new BadDataException(msg));	
					continue;
				}
				if(isHeaderRecord(line)) {
					processedHeader=true;
				} else if (isTrailerRecord(line)){
					if(!processedHeader) {
						String msg="Error at line " + lineNumber + ": Invalid data (Trailer record without Header record.)"; 
						LOGGER.debug(msg);
						context.addException(new BadDataException(msg));
					} else {
						recordCount=Integer.parseInt(line.substring(16,24));
						
						if(recordCount!=counter) {
							String msg="Error at line " + lineNumber + ": Invalid data (Record count does not match.)"; 
							LOGGER.debug(msg);
							context.addException(new BadDataException(msg));
						} 
						context.setBinInfos(binInfos);
					}
				} else {
					seq=line.substring(0,8);
					lowRange=line.substring(8,17);
					highRange=line.substring(17,26);
					binInfos.add(new BINInfo(Long.parseLong(lowRange),Long.parseLong(highRange),Long.parseLong(seq),context.getCardType()));
					counter++;
				}
				
				
			}
		} catch (IOException ioe) {
			context.addException(new BadDataException(ioe));
		} finally {
			if (lines != null) {
				try {
					lines.close();
				} catch (IOException e) {
					context.addException(new BadDataException(e));
				}
			}
		}
		
	}
	
	private boolean isTrailerRecord(String line) {
		return line.startsWith("99999999")?true:false;
	}
	
	private boolean isHeaderRecord(String line) {
		return (line.startsWith("00000000VIUSDBTBIN") || line.startsWith("00000000MCUSDBTBIN"))?true:false;
	}
	
	
	private void processHeader(String header, EnumCardType cardType) throws BadDataException {
		if(EnumCardType.VISA.equals(cardType)|| EnumCardType.MC.equals(cardType)) {
			
		} else {
			throw new BadDataException("Invalid cardType :"+cardType.getDisplayName());
		}
		
	}
	private void loadBinFiles(File visaBinFile, File mcBinFile) throws CreateException,  FDResourceException, IOException {
		
			
		
		InputStream isVisa = null;
		InputStream isMC = null;
		List<BINInfo> visaBINInfo=null;
		List<BINInfo> mcBINInfo=null;
		try{
		LOGGER.info("starting to load FIN File");
		
		isVisa = new FileInputStream(visaBinFile);
		isMC = new FileInputStream(mcBinFile);
		BINContext context=new BINContext();
		context.setCardType(EnumCardType.VISA);
		
		this.parseFile(isVisa, context);
		if(context.getExceptions().size()==0)
			visaBINInfo=context.getBinInfos();
		
		context.setCardType(EnumCardType.MC);
		this.parseFile(isMC, context);
		if(context.getExceptions().size()==0)
			mcBINInfo=context.getBinInfos();
		
		}finally{
			if(isMC != null) isMC.close();
			if(isVisa != null) isVisa.close();
			
		}
		List<List<BINInfo>> binInfos=new ArrayList<List<BINInfo>>();
		binInfos.add(visaBINInfo);
		binInfos.add(mcBINInfo);
		
		FDECommerceService.getInstance().saveBINInfo(binInfos);
	}
	
	private void downloadFile(File visaBinFile, File mcBinFile) throws UnknownHostException, IOException {
		//download Visa BIN file from paymentech
		this.downloadFile(visaBinFile);
		
		
		//download MC BIN file from paymentech
		this.downloadFile(mcBinFile);
	}
	
	private void downloadFile(File dfrFile) throws UnknownHostException, IOException {
		System.out.println("Downloading BIN file from Paymentech - START"+dfrFile.getName());
		Socket paymentech = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			int binPort =Integer.parseInt(DataLoaderProperties.getPaymentechBinPort());//8522; //Integer.parseInt(DataLoaderProperties.getPaymentechBatchPort());
			paymentech = new Socket(DataLoaderProperties.getPaymentechBinIp(), binPort);;//new Socket(DataLoaderProperties.getPaymentechBatchIp(), batchPort);"206.253.180.137"
			out = new PrintWriter(paymentech.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(paymentech.getInputStream()));
			//send RFR request to Paymentech
			System.out.println("Sending RFR request");
			out.print(BIN_RFR_REQUEST);
			out.print(BIN_EOF);
			out.flush();
			System.out.println("RFR Request finished");
			System.out.println("Reading data from Paymentech");
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
			System.out.println("Downloading file from Paymentech - END");
			
		} finally {
			if(out != null) out.close();
			if(in != null) in.close();
			if(paymentech != null) paymentech.close();
		}
	}
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}

