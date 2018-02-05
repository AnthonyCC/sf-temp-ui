package com.freshdirect.mail.service;

import java.io.File;
import org.apache.commons.lang.CharEncoding;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
//import com.freshdirect.fdstore.silverpopup.util.OAuthAuthenticationRestServiceImpl;

import com.freshdirect.framework.util.log.LoggerFactory;



import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class SilverpopEmailCampaignXmlHttpsPoster {
	
	private static Category LOGGER = LoggerFactory.getInstance( SilverpopEmailCampaignXmlHttpsPoster.class );
	public static final String CHARENCODING_ISO_8859_1 = CharEncoding.ISO_8859_1;
	public static final String CHARENCODING_UTF8 = CharEncoding.UTF_8;

    /**
     *
     * Usage:
     * java PostXML http://mywebserver:80/ c:\foo.xml
     *
     * 
     *
     */
    public static void main(String[] args) throws Exception {
    	SilverpopEmailCampaignXmlHttpsPoster postxml = new SilverpopEmailCampaignXmlHttpsPoster();
    	postxml.httpsPostWatsonEmailCampaignWithToken("test input", "xyz");
  
    }


/**
 * 
 * @param input
 * @param accessToken
 * @return
 */
    public SilverpopPostReturnValue httpsPostWatsonEmailCampaignWithToken(String input, String accessToken){
        // Get target URL
    	int responseCode=-1;
    	SilverpopPostReturnValue retvalue=null;
      //  String strURLTest = "http://httpbin.org/post";
        String strURL=    FDStoreProperties.getIBMWatsonEmailCampaignUrl();
       // System.out.println("+++++++++++++++++ ibmsilverpop url is:"+strURL );


        // Prepare HTTP post
        // Request content will be retrieved directly
        // from the input stream
        // Per default, the request content needs to be buffered
        // in order to determine its length.
        // Request body buffering can be avoided when
        // content length is explicitly specified
       // Specify content type and encoding
       // If content encoding is not explicitly specified
       // ISO-8859-1 is assumed
        PostMethod post = new PostMethod(strURL);
        
       
      
    
        post.addRequestHeader("Authorization","Bearer " +accessToken );//+accessToken+ ""accessToken+

        post.setRequestBody(input);

        post.setRequestHeader(
                "Content-type", "text/xml; charset="+ CHARENCODING_ISO_8859_1);//

        // Get HTTP client
        HttpClient httpclient = new HttpClient();
        
        // Execute request
        try {
        
        	httpclient.setConnectionTimeout(5 * 1000);
       
        	

        	responseCode = httpclient.executeMethod(post);

            // Display status code
          //  System.out.println("::Response status code: " + responseCode);
            

            // Display response
//            System.out.println("::silverpop xml post Response body:::::::: ");
//            System.out.println(post.getResponseBodyAsString());
            SilverpopXmlResponseParser responseParser = new SilverpopXmlResponseParser();
            String formattedResponse= responseParser.processXmlErrors( post.getResponseBodyAsString());
//            System.err.println(this.getClass().getName()+ " formatted response from silverpop is: "+ formattedResponse);
            LOGGER.warn( "formatted response from silverpop is: "+ formattedResponse);
            SilverpopPostReturnValue silverpopReturnValue=    new  SilverpopPostReturnValue(responseCode,formattedResponse);
            retvalue= silverpopReturnValue;
           
           // System.out.println("::::END Response body: ");

        } catch (HttpException e) {
			// TODO Auto-generated catch block
        	retvalue=     new  SilverpopPostReturnValue(404,e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			retvalue=     new  SilverpopPostReturnValue(404,e.getMessage());
			e.printStackTrace();
		} finally {
            // Release current connection to the connection pool 
            // once you are done
            post.releaseConnection();
        }
        
        return (retvalue);
    }
    
}