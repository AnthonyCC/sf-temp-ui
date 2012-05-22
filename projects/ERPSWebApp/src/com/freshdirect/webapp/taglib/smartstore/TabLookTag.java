package com.freshdirect.webapp.taglib.smartstore;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


/**
 * Helper tag that enables its content if the given look attribute matches the one
 * determined by the CART'N'TAB variant assigned to the customer
 * 
 * @author segabor
 *
 */
public class TabLookTag extends BodyTagSupport {

	private static final long serialVersionUID = 6540448900345119543L;

	private static final Logger LOGGER = Logger.getLogger(TabLookTag.class);
	
	private static final String DEBUG_PARAMETER = "_tablook";

	private static enum LookType {
		DEFAULT("tabbed"),
		FLAT("flat"),
		UNKNOWN("_unknown_");
		
		private final String name;
		
		LookType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}



	/**
	 * Look name, can be null
	 * 	Default value is "tabbed"
	 *	"flat" denotes the new flattened look
	 */
	private String look;
	
	/**
	 * If value is true then tag will behave the opposite.
	 * ie. content will be rendered if looks do not match. 
	 */
	private boolean negate = false;
	
	
	public void setLook(String look) {
		this.look = look;
	}
	
	public void setNegate(boolean negate) {
		this.negate = negate;
	}
	
	
	@Override
	public int doStartTag() throws JspException {
		final HttpSession session = pageContext.getSession();
		final FDUserI user =  (FDUserI) session.getAttribute(SessionName.USER);
		
		if (user == null) {
			throw new JspException("Missing user!");
		}

		final LookType tagLook = getTagLook();
		final LookType currentLook = getCurrentLook(pageContext.getRequest(), user);
		
		final boolean condition = (tagLook == currentLook) ^ negate;

		return condition ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}



	/**
	 * Tag wants to allow the following look
	 * 
	 * @return
	 */
	protected LookType getTagLook() {
		if (this.look == null)
			return LookType.DEFAULT;
		
		for (LookType lt : LookType.values()) {
			if (lt.getName().equalsIgnoreCase(this.look))
				return lt;
		}
		
		LOGGER.warn("look attribute cannot be identified, fall back to default value");
		
		return LookType.DEFAULT;
	}



	/**
	 * Determine the current look
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	protected LookType getCurrentLook(final ServletRequest request, final FDUserI user) {
		final String s = request.getParameter(DEBUG_PARAMETER);
		if (s != null) {
			// debug parameter
			for (LookType lt : LookType.values()) {
				if (lt.getName().equalsIgnoreCase(s))
					return lt;
			}
			LOGGER.warn("Debug look type " + s + " not found!");
			
			return LookType.UNKNOWN;
		} else {
			// determine look from variant. Currently tabbed and flat looks are supported
			
			Variant v = VariantSelectorFactory.getSelector(EnumSiteFeature.CART_N_TABS).select(user);
			
			return v.isDefaultTabLook() ? LookType.DEFAULT : LookType.FLAT;
		}
	}
}
