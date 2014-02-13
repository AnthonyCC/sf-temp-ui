package com.freshdirect.webapp.soy;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;



public class SoyImportTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( SoyImportTag.class );
	
	/**
	 * Soy-to-Js servlet base url. 
	 * Should be consistent with actual servlet mapping in web.xml.
	 */
	public static final String SOY_JS_BASE = "/api/soy/";
		
	private String packageName;
	
	public String getPackageName() {
		return packageName;
	}	
	public void setPackageName( String packageName ) {
		this.packageName = packageName;
	}

	
	@Override
	public void doTag() throws JspException {
				
		try {
			packageName = packageName.replace( '.', '/' );
		    packageName = SoyTemplateEngine.cleanPackageName( packageName );
				
			JspWriter out = getJspContext().getOut();
			out.write(  "<script src=\""+ SOY_JS_BASE + packageName + "\"></script>" );		
			
	    } catch ( IOException e ) {
			throw new JspException( "Failed to import package: "+packageName, e );
		} catch (RuntimeException e) {
			throw new JspException( "Failed to import package: "+packageName, e );
		}
		
	}
	
}
