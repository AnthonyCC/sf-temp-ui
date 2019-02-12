package com.freshdirect.dataloader.payment;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ecomm.gateway.ProfileCreatorService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GatewayProfileCreaterCron {
	private final static Category LOGGER = LoggerFactory.getInstance(GatewayProfileCreaterCron.class);
	
	
	public static void main(String[] args) throws IOException, NamingException, CreateException {
		if (args.length == 0 || args.length != 1) {
			printHelpMessage();
			System.exit(-1);
		}
		String batchId = args[0];
		try {
				ProfileCreatorService.getInstance().createProfiles(batchId);
			
		} catch (FDResourceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	
	private static void printHelpMessage() {
		System.out.println("USAGE: java com.freshdirect.dataloader.payment.GatewayProfileCreaterCron <batchID>");
	}
}
