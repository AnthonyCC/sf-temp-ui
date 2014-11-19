package com.freshdirect.mobileapi.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.Html;

public class ProductUtil {
    private static final Logger LOG = Logger.getLogger(ProductUtil.class);

    public static final String MEDIA_PATH = FDStoreProperties.getMediaPath();

    public static URL resolve(String childPath) throws IOException {
        return resolve(MEDIA_PATH, childPath);
    }

    public static URL resolve(String rootPath, String childPath) throws IOException {
    	
    	//I am changing the implementation of this to create two seperate URLs and then test the strings
    	//because the previous implementation throws an Exception when the rootpath is not 
    	//local to the machine.
    	//-- Michael Cress
        URL rootPathURL = new URL(rootPath);
        if (childPath.startsWith("/")) {
            childPath = childPath.substring(1, childPath.length());
        }
        URL url = new URL(rootPathURL, childPath);

        if (!url.toString().startsWith( /*rootPath*/ rootPathURL.toString())) {
            throw new IOException("Child path not under root");
        }

        return url;
    }

    public static String readContent(URL url) throws IOException {
        //LOG.debug("Reading content from: " + url.toString());
        Writer out = new StringWriter();
        InputStream in = null;
        in = url.openStream();

        if (in == null) {
            throw new FileNotFoundException();
        }

        byte[] buf = new byte[4096];
        int i;
        while ((i = in.read(buf)) != -1) {
            out.write(new String(buf, 0, i));
        }
        in.close();
        return out.toString();
    }

    public static String readHtml(Html htmlContent){
        if ((htmlContent != null) && (htmlContent.getPath() != null)) {
            try {
            	
            	//Michael Cress
            	//Fixing the path here to use an absolute path
            	//and then using an HttpUrlConnection to read the data rather than calling the readContent method.
            	//I was getting an exception when doing that.
            	StringBuilder path = new StringBuilder();
            	path.append("http://freshdirect.com");
            	path.append( htmlContent.getPath() );	//getPath() already returns a string with a '/' prepended.

				URL url = new URL( path.toString() );
//                return readContent(ProductUtil.resolve( /*htmlContent.getPath()*/ "http://freshdirect.com",  path.toString() ));
//				return readContent( url );
            	
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
		 
				// optional default is GET
				con.setRequestMethod("GET");
		 
				//add request header
				con.setRequestProperty("User-Agent", "FreshDirect Mobile API");
		 
				int responseCode = con.getResponseCode();
		 
				if( responseCode == 200 )
				{
					BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream() ) );
					String inputLine;
					StringBuffer responseStr = new StringBuffer();
			 
					while ((inputLine = in.readLine()) != null) {
						responseStr.append(inputLine);
					}
					in.close();
	            	
	            	return responseStr.toString();
				}
            	
            } catch (IOException e) {
                LOG.warn("HTML Content file " + htmlContent.getPath() + " not found");
            }
        }
        return "";
    }

}
