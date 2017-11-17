/**
 * 
 * TellAFriendUtil.java
 * Created Dec 9, 2002
 */
package com.freshdirect.webapp.util;

/**
 *
 *  @author knadeem
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletContext;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.mail.TellAFriendProduct;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TellAFriendUtil {
	
	private static Category LOGGER = LoggerFactory.getInstance( TellAFriendUtil.class );
	
	public static void sendTellAFriendProduct(TellAFriendProduct tafp, ServletContext ctx) throws FDResourceException {
		
		try {
			
			decorateTellAFriendProduct(tafp, ctx);
			FDCustomerManager.sendTellAFriendEmail(tafp, null);

		}catch(IOException ie){
			LOGGER.warn("Product does not have a description", ie);
			
		} finally {
			tafp.setProductDescription(null);	
		}	
	}
	
	public static String makeTellAFriendProductPreview(TellAFriendProduct tafp, ServletContext ctx) throws FDResourceException {
		String ret = ""; 
		try {
			decorateTellAFriendProduct(tafp, ctx);
			ret = FDCustomerManager.makePreviewEmail(tafp);

		}catch(IOException ie){
			LOGGER.warn("Product does not have a description", ie);
			
		}finally {
			tafp.setProductDescription(null);	
		}
		
		return ret;
	}
	
	private static void decorateTellAFriendProduct(TellAFriendProduct tafp, ServletContext ctx) throws IOException, FDResourceException {
		
		ProductModel productNode = ContentFactory.getInstance().getProductByName(tafp.getCategoryId(), tafp.getProductId());
		if(productNode != null){
			Html html = productNode.getProductDescription();
			StringBuffer sb = new StringBuffer();
			if(html != null){
				InputStream is = ctx.getResourceAsStream(html.getPath());
				if(is != null){
					Reader r = new InputStreamReader(is);
					char[] buf = new char[1024];
					int len;
					while ( (len=r.read(buf)) != -1) {
						sb.append(buf, 0, len);
					}
					r.close();
				}
			}
			tafp.setProductDescription(sb.toString());
		}else{
			tafp.setProductDescription("");
		}
	}
	
}
