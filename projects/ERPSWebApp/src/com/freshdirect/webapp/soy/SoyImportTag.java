package com.freshdirect.webapp.soy;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.Buildver;
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
			PageContext ctx = (PageContext) getJspContext();
			HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
			
			StringBuilder uri = new StringBuilder();
			
			packageName = packageName.replace( '.', '/' );
			packageName = SoyTemplateEngine.cleanPackageName( packageName );
			
			uri.append(SOY_JS_BASE + packageName);
			
			if (FDStoreProperties.isBuildverEnabled()) {
				if (uri.indexOf("?") != -1)
					uri.append("&buildver=");
				else
					uri.append("?buildver=");
				uri.append(Buildver.getInstance().getBuildver());
			}
			
			String uriStr = uri.toString();
			
			/* check if package has already been loaded and don't load again
			 * if we ever need to reload loaded packages, add a "force" attrib to this tag
			 */
			if (request.getAttribute("SOY_PACKAGE_LOADED-"+uriStr) == null) {
				JspWriter out = getJspContext().getOut();
				out.write( "<script src=\""+ uriStr + "\"></script>" );
				
				/* mark package as already loaded */
				request.setAttribute("SOY_PACKAGE_LOADED-"+uriStr, true);
			}
			
		} catch ( IOException e ) {
			throw new JspException( "Failed to import package: "+packageName, e );
		} catch (RuntimeException e) {
			throw new JspException( "Failed to import package: "+packageName, e );
		}
		
	}
	
}

