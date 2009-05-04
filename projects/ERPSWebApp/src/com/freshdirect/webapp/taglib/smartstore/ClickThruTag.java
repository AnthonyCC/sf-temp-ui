package com.freshdirect.webapp.taglib.smartstore;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.FDURLUtil;


/**
 * Tag to support generating click-thru events when customer clicks to a recommended product
 * 
 * Click-thru events are generated when customer clicks to a recommended product. This tag triggers
 * when browser leaves recommendation page (view_cart) and enters destination page (usually product.jsp).
 * 
 * 
 * @author segabor
 *
 */
public class ClickThruTag extends BodyTagSupport {
    private static Category LOGGER = LoggerFactory.getInstance( ClickThruTag.class );

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ProductModel product;
	
	public void setProduct(ProductModel product) {
		this.product = product;
	}

	/**
	 * Generate click-thru event when customer clicked to a DYF recommended item
	 * 
	 */
	public int doStartTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		
		String uri = req.getRequestURI();


		String variantId = req.getParameter("variant");
		if (variantId == null || variantId.length() == 0) {
			LOGGER.debug("Click-Thru: SKIPPED - missing variant id");
			return SKIP_BODY;
		}

		FDEventUtil.logRecommendationClickThrough(variantId, product.getContentKey());
		
		FDURLUtil.logProductClick(req);
		return SKIP_BODY;
	}

	public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[0];
        }
	}
}
