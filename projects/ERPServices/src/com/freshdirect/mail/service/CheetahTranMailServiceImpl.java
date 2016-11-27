package com.freshdirect.mail.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.cheetahmail.apiclient.APIClient;
import com.cheetahmail.apiclient.APIClientException;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.mail.EnumEmailType;
import com.freshdirect.mail.EnumTranEmailType;

public class CheetahTranMailServiceImpl implements TranMailServiceI{
	
	private final static Logger LOGGER = LoggerFactory.getInstance(CheetahTranMailServiceImpl.class);

	private static final String EBM_SERVICE_NAME="ebmtrigger1";
	
	public CheetahTranMailServiceImpl(){}					
	
	public String sendTranEmail(TEmailI emailInfo) throws TranEmailServiceException{
			
		        // Example of a custom, non-cheetahmail.com URI.
		        //System.setProperty("CheetahClient.forceURIBase", 
		        //"https://example.com/emb/");
		APIClient apiClient=null;
		
		String response;
		    try{    
		    	Properties defaults = new Properties(System.getProperties());
		    	Properties p = ConfigHelper.getPropertiesFromClassLoader("cc.properties", defaults);
		        System.setProperties(p);
		    	apiClient=new APIClient();	
		    	String loginResponse = apiClient.login();
		        HashMap<String, String> args = new HashMap<String, String>();		        
		        String affinilateId= System.getProperty("CheetahClient.affiliate_id");
		        if(affinilateId==null ) affinilateId="2068571857";
		        args.put("aid", affinilateId);
		        //args.put("aid", "1%2C%2C%2C2075719666%2C2068571857%2C2068571855%2C5279677650834242325402%2C110831055754623896474%2C0%2C1078611442%2C1268848575%2C0%2C3%3AhxdwiEiA4GVLgtTO%2BprcNw%2C2%3A71jfAst3RnmcE%2FmOIB6G6Q");        
		        args.put("eid", emailInfo.getTargetProgId());
		        args.put("from_address", emailInfo.getFromAddress().getAddress());
		        args.put("reply_to", emailInfo.getFromAddress().getAddress());
		        args.put("HTML",getEmailTypeValue(emailInfo.getEmailType()) );
		        if(!emailInfo.isProductionReady()) args.put("test", "1");		        
		        args.put("SUBJECT", emailInfo.getSubject());
		        args=(HashMap)fillContentParams(args, emailInfo.getEmailContent());
		        
		        //args.put("email", "skanury@freshdirect.com");
		        args.put("email", emailInfo.getRecipient());
		        
		        //APPDEV-2451 - fill in OAS parameters here
		        if(emailInfo.getOasQueryString() != null)
		        	args.put("OAS_query", emailInfo.getOasQueryString());
		        
		        LOGGER.info("Cheetah args :"+args);	
		        
		        response=apiClient.callMethodHandler(EBM_SERVICE_NAME, args);		        
		        Iterator it = emailInfo.getCCList().iterator();
		        while(it.hasNext()){
		        	String email=(String)it.next();
		        	args.put("email", email);
		        	 //System.out.println("args :"+args);	
		        	response=apiClient.callMethodHandler(EBM_SERVICE_NAME, args);
		        }
		       
		    } catch (APIClientException e){		    	
		    	e.printStackTrace();
		    	if(isTempErrorString(e.getMessage())) throw new TranEmailServiceException(ERROR_INTERNAL,e);		    	
		    	else throw new TranEmailServiceException(ERROR_EXTERNAL,e);		    	
		    }
		    catch(IOException e){
		    	e.printStackTrace();
		    	throw new TranEmailServiceException(ERROR_EXTERNAL,e);
		    }
		    catch(Exception e){		    	
		    	e.printStackTrace();
		    	throw new TranEmailServiceException(ERROR_EXTERNAL,e);
		    }		   
		    return response; 		    					
	}

	
    public boolean isTempErrorString(String string) {
        return ((string.startsWith("err:internal"))
                || (string.startsWith("err:server"))
                // err:upload is a special client-side error generated in
                // readResponse()
                || (string.startsWith("err:upload"))
                || (string.startsWith("err:param:requested data not available")) || (string
                .startsWith("500")));
    }

	
	
	
	public String getEmailTypeValue(String emailTypeStr){
		
		EnumEmailType emailType=EnumEmailType.getEnum(emailTypeStr);
		if(EnumEmailType.HTML==emailType){
		    return "1";		    
		}else if (EnumEmailType.TEXT==emailType){
			return "0";
		}else {
			return "2";
		}	
	}
	
	public Map  fillContentParams(Map params,String contents){
		
		String lines[]=contents.split("&&");
		if(lines!=null && lines.length>0){
			
			for(int i=0;i<lines.length;i++){

				String line=lines[i];
				if(line==null || line.trim().length()==0) continue;
				String value=line.substring(line.indexOf("=")+1);
				String key=line.substring(0, line.indexOf("="));				
				if(key.startsWith("ENCRYPT[")){
					key=key.substring(key.indexOf("[")+1,key.length()-1);
					value=ErpGiftCardUtil.decryptGivexNum(value);					
					System.out.println("encrypted key :"+key);
					System.out.println("encrypted value :"+value);					
				}
				params.put(key, value);	
			}						
		}
		
		/*
		StringTokenizer tokens=new StringTokenizer(contents,"&&");		
		while(tokens.hasMoreElements()){
			String line=tokens.nextToken();
			if(line==null || line.trim().length()==0) continue;
			String value=line.substring(line.indexOf("=")+1);
			String key=line.substring(0, line.indexOf("="));
			
			if(key.startsWith("ENCRYPT[")){
				key=key.substring(key.indexOf("[")+1,key.length()-1);
				value=ErpGiftCardUtil.decryptGivexNum(value);
				
				System.out.println("encrypted key :"+key);
				System.out.println("encrypted value :"+value);
				
			}
			
		    params.put(key, value);	
		}*/
		return params;
	}
	
}
