package com.freshdirect.fdstore.temails;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.temails.cheetah.CheetahTEmailContextImpl;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.mail.EnumTEmailProviderType;
import com.freshdirect.mail.EnumTranEmailType;
import com.freshdirect.temails.TEmailEngineI;
import com.freshdirect.temails.TEmailsRegistry;

public final class TEmailContentFactory {

	private static TEmailContentFactory factory=null;
	public static final SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d yyyy");
	public static final SimpleDateFormat DT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	
	private TEmailContentFactory()
	{
		
	}
	
	public static  TEmailContentFactory getInstance(){
	
		if(factory==null){
			factory=new TEmailContentFactory();
		}
		return factory;
	}
		
	public  TEmailI createTransactionEmailModel(TEmailTemplateInfo info, Map input) 
	{
		
		
		// get the context;
		// get the formated email data		
		TEmailContextI context=TEmailsUtil.getTranEmailContext(info.getTransactionType(),info.getProvider(),input);
		
		TEmailEngineI engine= TEmailsRegistry.getTEmailsEngine(info.getProvider().getName());				
		String content=(String)engine.formatTemplates(context, info.getTemplateId());					
		// create the TEMAILINFOMODEL data and set everything
		// return the same				
		return  TEmailsUtil.createTransEmailModel(info,input,content);
	}
	
		    				
}
