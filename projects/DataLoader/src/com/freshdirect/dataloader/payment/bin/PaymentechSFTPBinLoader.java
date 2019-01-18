package com.freshdirect.dataloader.payment.bin;
//com.freshdirect.dataloader.payment.bin.PaymentechSFTPBinLoader

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.FileContext;
import com.freshdirect.dataloader.payment.PaymentFileType;
import com.freshdirect.dataloader.payment.SFTPFileProcessor;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.sap.ejb.SapException;

public class PaymentechSFTPBinLoader /*implements BINLoader*/ {
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechBinLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	                                                                                    
	       //										            1         2         3         4         5         6         7         8
	      // 										   12345678901234567890123456789012345678901234567890123456789012345678901234567890
	private static final String BIN_RFR_REQUEST = "PID=226042 FRESHRFR SID=226042 FRESHBIN RFR                                    \n";
	private static final String BIN_EOF 		= "EOFEOFEOF                                                                       \n";
	
	private static final String NO_DATA="No data to send back at this time";
	
	public static void main(String[] args) {
		
		
		FileContext ctx=getFileContext(args);
		
		
		try {
			PaymentechSFTPBinLoader.loadBINs(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.fatal("Failed to load bin files", e);
			
		}
	}
	
	private static void loadBINs(FileContext ctx) throws UnknownHostException, IOException, CreateException, EJBException, RemoteException, SapException, FDResourceException {
		String timestamp = SF.format(new Date());

		//make the dfr file
		System.out.println("DataLoaderProperties.getWorkingDir() :"+DataLoaderProperties.getWorkingDir());
		File binFileA = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_BIN_A_" + timestamp + ".dfr");
		File binFileB = new File(DataLoaderProperties.getWorkingDir() + "Paymentech_BIN_B_" + timestamp + ".dfr"); 

		
		boolean getFileFromProcessor=ctx.downloadFiles();
		
		if(getFileFromProcessor) {
			downloadFile(ctx,binFileA, binFileB);
		}
		loadBinFiles(binFileA, binFileB);
	}
	
	public static void parseFile(InputStream fileStream, BINContext context) {
		
		BufferedReader lines = null;
		boolean processedHeader=false;
		List<BINInfo> binInfos=new ArrayList<BINInfo>(5000);
		int recordCount=0;
		int counter=0;
		EnumCardType cardType=null;
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
					if(isVISA(line))
						cardType=EnumCardType.VISA;
					else 
						cardType=EnumCardType.MC;
					
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
					binInfos.add(new BINInfo(Long.parseLong(lowRange),Long.parseLong(highRange),Long.parseLong(seq),cardType));
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
	
	private static boolean isTrailerRecord(String line) {
		return line.startsWith("99999999")?true:false;
	}
	
	private static boolean isHeaderRecord(String line) {
		return (isVISA(line) || isMC(line))?true:false;
	}
	
	private static boolean isVISA(String header) {
		return header.startsWith("00000000VIUSDBTBIN");
	}
	
	private static boolean isMC(String header) {
		return header.startsWith("00000000MCUSDBTBIN");
	}
	
	private static void processHeader(String header, EnumCardType cardType) throws BadDataException {
		if(EnumCardType.VISA.equals(cardType)|| EnumCardType.MC.equals(cardType)) {
			
		} else {
			throw new BadDataException("Invalid cardType :"+cardType.getDisplayName());
		}
		
	}
	public static void loadBinFiles(File visaBinFile, File mcBinFile) throws CreateException,  FDResourceException, IOException {
		
			
		
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
		
		parseFile(isVisa, context);
		if(context.getExceptions().size()==0)
			visaBINInfo=context.getBinInfos();
		
		context.setCardType(EnumCardType.MC);
		parseFile(isMC, context);
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
	
	private static FileContext getFileContext(String[] args) {
		
		
		FileContext ctx=new FileContext();
		ctx.setFileType(PaymentFileType.BIN);
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
					System.err.println("Usage: java com.freshdirect.dataloader.payment.bin.PaymentechSFTPBinLoader  [fetchFiles={true | false}] [remoteURL=Value] [remoteUser=Value] [remotePassword=Value]  [privateKey=Value]");
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
	private static void downloadFile(FileContext ctx, File visaBinFile, File mcBinFile) throws UnknownHostException, IOException {
		
			/*FileContext ctx=new FileContext();
			ctx.setFileType(PaymentFileType.BIN);
			ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
			ctx.setOpenSSHPrivateKey(DataLoaderProperties.getWorkingDir()+DataLoaderProperties.getPaymentSFTPKey());
			ctx.setRemoteHost(DataLoaderProperties.getPaymentSFTPHost());
			ctx.setUserName(DataLoaderProperties.getPaymentSFTPUser());
			ctx.setPassword(DataLoaderProperties.getPaymentSFTPPassword());*/
			SFTPFileProcessor fp=new SFTPFileProcessor(ctx);
			List<String> files=fp.getFiles();
			if(files.size()<2) {
				throw new FDRuntimeException("No BIN data.");
			}
			File _tmpFileOne = new File(DataLoaderProperties.getWorkingDir() + files.get(0));
			File _tmpFileTwo = new File(DataLoaderProperties.getWorkingDir() + files.get(1));
			_tmpFileOne.renameTo(visaBinFile);
			_tmpFileTwo.renameTo(mcBinFile);
	}
	

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
}

