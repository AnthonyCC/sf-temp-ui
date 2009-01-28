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
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.FDEventUtil;


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

		// Check page URI against referer
		if (req.getHeader("Referer") != null) {
			try {
				URL refURL = new URL(req.getHeader("Referer"));

				// We are in same page, bye!
				if (refURL.getPath().startsWith(uri)) {
					LOGGER.debug("Click-Thru: SKIPPED - staying in the same page as referer was");
					return SKIP_BODY;
				}
			} catch (MalformedURLException e) {}
		} else {
			// No referer, bye!
			LOGGER.debug("Click-Thru: SKIPPED - referer IS NULL");
			return SKIP_BODY;
		}

		// No site feature, bye!
		// List of tracking codes: http://home.freshdirect.com/confluence/display/web/Tracking+Codes
		// Instead of struggling with trk codes require only the variant.
		/*** String trk_code = req.getParameter("trk");
		if (	   !"dyf".equalsIgnoreCase(trk_code)
				&& !"favorites".equalsIgnoreCase(trk_code)
				&& !"feat".equalsIgnoreCase(trk_code)) {
			LOGGER.debug("Click-Thru: SKIPPED - missing site feature");
			return SKIP_BODY;
		} ***/

		String variantId = req.getParameter("variant");
		if (variantId == null || variantId.length() == 0) {
			LOGGER.debug("Click-Thru: SKIPPED - missing variant id");
			return SKIP_BODY;
		}
		
		FDEventUtil.logRecommendationClickThrough(variantId, product.getContentKey());
		
		return SKIP_BODY;
	}

	public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[0];
        }
	}
}
