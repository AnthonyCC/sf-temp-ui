package com.freshdirect.webapp.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.util.FDURLUtil;

public class SearchRedesignRedirectorTag extends BodyTagSupport {

	private static final long serialVersionUID = 7904206526600184592L;
	private static final Logger LOGGER = LoggerFactory.getInstance(SearchRedesignRedirectorTag.class);
	private static final String FALLBACK_PAGE = "/";
	private static final String NEW_SEARCH_PAGE = "/srch.jsp";
	
	private FilteringFlowType pageType;
	private FDUserI user;
	private boolean redirected = false;

	public int doStartTag() throws JspException {
		PageContext ctx = (PageContext) pageContext;
		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
		boolean disabledPartialRolloutRedirector = CmsFilteringNavigator.isDisabledPartialRolloutRedirector(request);
		if (!disabledPartialRolloutRedirector && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.searchredesign2014, user)) {

			String redirectUrl = null;

			switch(pageType) {
				case SEARCH:
					String searchParams = request.getParameter("searchParams");
					String isAutosuggest = request.getParameter("isAutosuggest");
					
					try {
						searchParams = (searchParams == null ? "" : URLEncoder.encode(searchParams, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
					}
					redirectUrl = NEW_SEARCH_PAGE + "?searchParams=" + (searchParams == null ? "" : searchParams)+"&isAutosuggest="+isAutosuggest;
					break;
				case NEWPRODUCTS:
					redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.NEWPRODUCTS.toString().toLowerCase();
					break;
				case PRES_PICKS:
					String id = request.getParameter("id");
					if ("picks_love".equals(id)) {
						redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.PRES_PICKS.toString().toLowerCase() + "&id=picks_love";
					}
					if ("prod_assort".equals(id)) {
						redirectUrl = NEW_SEARCH_PAGE + "?pageType=" + FilteringFlowType.STAFF_PICKS.toString().toLowerCase() + "&id=" + id;
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
				String originalUrl = request.getRequestURI();

				redirectUrl = FDURLUtil.decorateRedirectUrl(redirectUrl, request);

				LOGGER.debug("Redirecting from " + originalUrl + " to " + redirectUrl);

				// To ensure that https requests get redirect to https correctly
				//OPT-14 - Avoid redirect to "http://".
				String requestScheme = FDStoreProperties.getRequestSchemeForRedirectUrl();
				LOGGER.debug("Request scheme from properties for " + originalUrl + " :" +requestScheme);
				if(null != requestScheme && !"".equals(requestScheme)){
					//The property value should be either http or https.
					if((!"http".equals(requestScheme) && !"https".equals(requestScheme))){
						requestScheme = request.getScheme();
					}
					redirectUrl = requestScheme + "://" + request.getServerName() + redirectUrl;
				} else{
					redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
				}
				LOGGER.debug("Redirect url for "+ originalUrl+" is "+redirectUrl);
				//
//				redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
				redirected = true;
				HttpServletResponse httpServletResponse = (HttpServletResponse) ctx.getResponse();
				httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				httpServletResponse.setHeader("Location", redirectUrl);
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
