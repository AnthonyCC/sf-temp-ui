package com.freshdirect.dataloader.payment.bin;

import java.io.File;
import java.io.IOException;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;


public class PaymentechBINLoaderUtil {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechBINLoaderUtil.class);
	public static void main(String[] args) {
		if (args.length == 0 || args.length != 2) {
			printHelpMessage();
			System.exit(0);
		}
		File visaBinFile = new File(args[0]);
		File mcBinFile = new File(args[1]);
		//try {
			try {
				PaymentechSFTPBinLoader.loadBinFiles(visaBinFile, mcBinFile)	;
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.fatal("Failed to load bin files", e);
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.fatal("Failed to load bin files", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.fatal("Failed to load bin files", e);
			}
		
	}
	private static void printHelpMessage() {
		System.out.println("USAGE: java com.freshdirect.dataloader.payment.bin.PaymentechBINLoaderUtil [fully/qualified/VisaBINfilename] [fully/qualified/MastercardBINfilename]");
	}
}
