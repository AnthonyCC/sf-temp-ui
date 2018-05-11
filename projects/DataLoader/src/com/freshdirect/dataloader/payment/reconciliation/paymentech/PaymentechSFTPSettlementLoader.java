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
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechSFTPSettlementLoader.class);
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

	/**
	 *  Process list of settlement files and upload reconciliation file to SAP FTP
	 * @param ctx
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws CreateException
	 * @throws EJBException
	 * @throws RemoteException
	 * @throws SapException
	 */
	private void loadSettlement(FileContext ctx) throws UnknownHostException, IOException, CreateException, EJBException, RemoteException, SapException {
		
		if(ctx.downloadFiles()) {
			SettlementFileProcessor.processFiles(ctx);
			
			// below implementation is moved to FileDownloader class 
			//this.downloadFile(ctx,finFile, pdeFile, tmpFileOne, tmpFileTwo);
		}



	}
	
	
}
