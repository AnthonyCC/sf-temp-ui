/*
 * Created on Feb 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.cms.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author htai
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StoreTest {

	public StoreTest() {
	}
	
	private List urls = new ArrayList();
	private HashMap replaceStrings = new HashMap();
	private String[] ignorePatterns;
	private String targetUrl;
	private Cookie cookie;
	private Cookie targetCookie;
	private String baseSource;
	private String targetSource;
	private PrintWriter diffLogFile;
	private String diffLogPrepend;
	
	private HashSet cache = new HashSet();
	protected void initialize() throws Exception {
		ResourceBundle strings = ResourceBundle.getBundle(getClass().getName());		
		targetUrl = strings.getString("target");	

		String urlString = strings.getString("urls");
	    FileInputStream in = new FileInputStream(urlString);

	    DataInputStream data = new DataInputStream(in);
	    String line;
	    while( (line = data.readLine()) != null) {
	    	urls.add(line);
	    }

	    StringTokenizer tok = null;
	    
        cookie = new Cookie(strings.getString("cookieDomain"), "FDUser", strings.getString("cookie"), "/", null, false);
        targetCookie = new Cookie(strings.getString("targetCookieDomain"), "FDUser", strings.getString("targetCookie"), "/", null, false);
		
        // Fill up my patterns
		String ignorePatternStrings = strings.getString("ignorePatterns");
		tok = new StringTokenizer(ignorePatternStrings, ",");
		ignorePatterns = new String[tok.countTokens()];
		int i = 0;
		while(tok.hasMoreElements()) {
			ignorePatterns[i++] = (String) tok.nextElement();
		}

        // Fill up my replacement patterns
		String replacePatternStrings = strings.getString("replacePatterns");
		tok = new StringTokenizer(replacePatternStrings, ",");
		while(tok.hasMoreElements()) {
			StringTokenizer newTok = new StringTokenizer((String) tok.nextElement(), "=");
			
			replaceStrings.put(newTok.nextElement(), newTok.nextElement());
		}
		
		baseSource = strings.getString("baseSource");
		targetSource = strings.getString("targetSource");
		
		diffLogPrepend = strings.getString("diffLogPrepend");
		try {
			diffLogFile = new PrintWriter(new FileOutputStream(strings.getString("diffLog")), true);
		} catch(Exception ex) {
			throw new RuntimeException();
		}
	}

	public void process(boolean doprocess) throws Exception {
		initialize();
		for(int j = 0; j < urls.size(); j++) {
			String url = (String) urls.get(j);
			if(cache.contains(url))
				continue;

			System.out.println("Processing: " + url);
			cache.add(url);
			if(doprocess)
				processURL(url, targetUrl);
		}
		System.out.println("Processed URLS: " + cache.size());
	}


	protected String processURL(String url, Cookie cookie) {
		String str="";
		
        try {
            HttpState initialState = new HttpState();
            initialState.addCookie(cookie);
	        HttpClient httpclient = new HttpClient();
	        httpclient.setState(initialState);
	
	        // Randomize URL
	        url += "&" + "rnd=" + System.currentTimeMillis();
	        
 	        GetMethod httpget = new GetMethod(url);
	
	        //	      Execute HTTP GET
	        int result = httpclient.executeMethod(httpget);
	        
	        // Display status code
	        System.out.println("Response status code: " + result);
	        
	        byte[] b = httpget.getResponseBody();
	        
	        str = new String(b);
	        
	        StringReader strReader = new StringReader(str);
	        BufferedReader buffReader = new BufferedReader(strReader);
	        
	        String line = null;
	        StringBuffer outputBuffer = new StringBuffer();
	        while( (line = buffReader.readLine()) != null ) {
	        	boolean matched = false;
	        	for(int i = 0; i < ignorePatterns.length; i++) {
	        		if(line.matches(ignorePatterns[i])) {
	        			matched = true;
	        			break;
	        		}
	        	}
	        	
	        	if(!matched) {
	        		for(Iterator j = replaceStrings.keySet().iterator(); j.hasNext(); ) {
	        			String pattern = (String) j.next();
	        			line = line.replaceAll(pattern, (String) replaceStrings.get(pattern));
	        		}
	        		outputBuffer.append("\n");
	        		outputBuffer.append(line);
	        	}
	        }
	        
	        str = outputBuffer.toString();
	        
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
        return str;
	}

	protected void processURL(String url, String target) {
		// Process source URL
		String sourceData = processURL(url, cookie);
		String query = "";
		try {
			URL oldUrl = new URL(url);
			String path = oldUrl.getPath();
			query = oldUrl.getQuery();
        
			url = target + path;
			if( (query != null) && !query.equals("") ) {
				url += "?" + query;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// Now process target URL
		String targetData = processURL(url, targetCookie);
		
		// Now save the data
		query = query.replaceAll("&", "_");
		query = query.replaceAll("=", "_");
		try {
			String baseFile = baseSource + query + ".txt";
			FileOutputStream fout = new FileOutputStream(baseFile);
			fout.write(sourceData.getBytes());
			
			String targetFile = targetSource + query + ".txt";
			fout = new FileOutputStream(targetFile);
			fout.write(targetData.getBytes());
			String pre = diffLogPrepend.replaceAll("\\?", url);
			diffLogFile.println(pre + " " + baseFile + " " + targetFile );
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			StoreTest test = new StoreTest();
			if(args.length == 0) {
				test.process(true);
			} else {
				if("true".equals(args[0])) {
					test.process(true);
				} else {
					test.process(false);
				}
			}

		} catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	   
	}
}
