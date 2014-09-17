package com.freshdirect.webapp.search;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;

public class SearchRedesignRedirectorTag extends BodyTagSupport {

	private static final long serialVersionUID = 7904206526600184592L;
	private static final Logger LOGGER = LoggerFactory.getInstance(SearchRedesignRedirectorTag.class);
	private static final String FALLBACK_PAGE = "/";
	private static final String NEW_SEARCH_PAGE = "/srch.jsp";
	
	private FilteringFlowType pageType;
	private FDUserI user;
	private boolean redirected = false;

	public int doStartTag() throws JspException {
		if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.searchredesign2014, user)) {

			PageContext ctx = (PageContext) pageContext;
			String redirectUrl = null;

			switch(pageType) {
				case SEARCH:
					String searchParams = ((HttpServletRequest) ctx.getRequest()).getParameter("searchParams");
					redirectUrl = NEW_SEARCH_PAGE + "?searchParams=" + (searchParams == null ? "" : searchParams);
					break;
				case NEWPRODUCTS:
					redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.NEWPRODUCTS.toString().toLowerCase();
					break;
				case PRES_PICKS:
					String id = ((HttpServletRequest) ctx.getRequest()).getParameter("id");
					if ("picks_love".equals(id)) {
						redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.PRES_PICKS.toString().toLowerCase() + "&id=picks_love";
					}
					break;
				case ECOUPON:
					redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.ECOUPON.toString().toLowerCase();
					break;
				default:
					redirectUrl = FALLBACK_PAGE;
					break;
			}
			
			if (redirectUrl != null) {
				String originalUrl = ((HttpServletRequest) ctx.getRequest()).getRequestURI();
				if (ctx.getRequest().getParameter("cm_vc") != null) {
					redirectUrl = redirectUrl + "&cm_vc=" + ctx.getRequest().getParameter("cm_vc");
				}
				LOGGER.debug("Redirecting from " + originalUrl + " to " + redirectUrl);
				ServletRequest request = ctx.getRequest();
				// To ensure that https requests get redirect to https correctly
				redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
				redirected = true;
				try {
					((HttpServletResponse) ctx.getResponse()).sendRedirect(redirectUrl);
				} catch (IOException ioe) {
					throw new JspException(ioe);
				}
				return SKIP_PAGE;
			}
		}
		return EVAL_BODY_BUFFERED;
	}

	public FDUserI getUser() {
		return user;
	}

	public void setUser(FDUserI user) {
		this.user = user;
	}

	public FilteringFlowType getPageType() {
		return pageType;
	}

	public void setPageType(FilteringFlowType pageType) {
		this.pageType = pageType;
	}

    public int doEndTag() throws JspException {
   	 if (this.redirected) {
            return SKIP_PAGE;
        } else {
       	 return super.doEndTag();
        }
      
   }

}
