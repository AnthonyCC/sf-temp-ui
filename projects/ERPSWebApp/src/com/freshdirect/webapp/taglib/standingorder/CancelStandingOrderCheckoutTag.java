package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.ShoppingCartUtil;

public class CancelStandingOrderCheckoutTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 3075335802986323182L;

	@Override
	public int doStartTag() throws JspException {
		final HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		EnumCheckoutMode origMode = user.getCheckoutMode();
		FDStandingOrder so = user.getCurrentStandingOrder();
		
		/* "chkplc".equalsIgnoreCase(request.getParameter("trk")) &&  <<< vmi magia a cancel gombhoz */
		user.setCurrentStandingOrder(null);
		if ( !EnumCheckoutMode.NORMAL.equals( origMode ) ) {
			
			// RESET
			user.setCheckoutMode(EnumCheckoutMode.NORMAL);

			if ( origMode == EnumCheckoutMode.MODIFY_SO ) {
				// RESTORE ORIGINAL CART
				try {
					ShoppingCartUtil.restoreCart(session);
				} catch (FDAuthenticationException e) {
					throw new JspException(e);
				} catch (FDResourceException e) {
					throw new JspException(e);
				}
			}
			
			String redirectUrl;
			
			if ( origMode == EnumCheckoutMode.MODIFY_SO ) {
				redirectUrl = FDURLUtil.getStandingOrderLandingPage(so, null); 
			} else {
				redirectUrl = FDURLUtil.getStandingOrderMainPage();				
			}
			
			// REDIRECT ...
			try {
				((HttpServletResponse)pageContext.getResponse()).sendRedirect( redirectUrl );
			} catch (IOException e1) {
			}
		}

		return super.doStartTag();
	}
	
	public static class TagEI extends TagExtraInfo {
        @Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[0]; // empty data
        }
	}
}
