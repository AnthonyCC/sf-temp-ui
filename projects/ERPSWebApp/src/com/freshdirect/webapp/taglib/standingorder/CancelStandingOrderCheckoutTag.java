package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class CancelStandingOrderCheckoutTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 3075335802986323182L;

	@Override
	public int doStartTag() throws JspException {
		final HttpSession session = pageContext.getSession();
		try {
			String redirectUrl = StandingOrderUtil.endStandingOrderCheckoutPhase(session);
			((HttpServletResponse)pageContext.getResponse()).sendRedirect( redirectUrl );
		} catch (FDAuthenticationException e) {
			throw new JspException(e);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
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
