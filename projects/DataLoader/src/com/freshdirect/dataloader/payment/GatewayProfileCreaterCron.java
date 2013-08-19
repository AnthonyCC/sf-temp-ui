package com.freshdirect.dataloader.payment;

import java.io.IOException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.ProfileCreatorHome;
import com.freshdirect.dataloader.payment.ejb.ProfileCreatorSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GatewayProfileCreaterCron {
	private final static Category LOGGER = LoggerFactory.getInstance(GatewayProfileCreaterCron.class);
	
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	public static void main(String[] args) throws IOException, NamingException, CreateException {
		if (args.length == 0 || args.length != 1) {
			printHelpMessage();
			System.exit(-1);
		}
		String batchId = args[0];
		Context ctx=getInitialContext();
		ProfileCreatorHome pcHome=(ProfileCreatorHome)ctx.lookup( "freshdirect.dataloader.ProfileCreatorCron" ) ;
		ProfileCreatorSB sb = pcHome.create();
		
		sb.createProfiles(batchId);
	
	}
	
	
	private static void printHelpMessage() {
		System.out.println("USAGE: java com.freshdirect.dataloader.payment.GatewayProfileCreaterCron <batchID>");
	}
}
