package com.freshdirect.webapp.ajax.browse;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.RecipeDepartment;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BrowsePartialRolloutRedirectorTag extends SimpleTagSupport{
	
	private static final Logger LOGGER = LoggerFactory.getInstance( BrowsePartialRolloutRedirectorTag.class );
	private static final String	BROWSE_PAGE_FS		= "/browse.jsp?id=%s";
	private static final String	OLD_DEPARTMENT_PAGE_FS	= "/department.jsp?deptId=%s";
	private static final String	OLD_CATEGORY_PAGE_FS	= "/category.jsp?catId=%s";
	private static final String	FALLBACK_PAGE = "/";
	
	private String id;
	private boolean oldToNewDirection;
	private FDUserI user;

	public void doTag() throws JspException, IOException {
		if (FDStoreProperties.isBrowseRolloutRedirectEnabled()) {

			String redirectUrl = null;
			boolean shouldBeOnNew = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user);
	
			if (shouldBeOnNew && oldToNewDirection) {
				redirectUrl = String.format(BROWSE_PAGE_FS, id);
	
			} else if (!shouldBeOnNew && !oldToNewDirection){
				ContentNodeModel node = ContentFactory.getInstance().getContentNode(id);
				
				if (node instanceof DepartmentModel || node instanceof RecipeDepartment){
					redirectUrl = String.format(OLD_DEPARTMENT_PAGE_FS, id);
				
				} else if (node instanceof CategoryModel){
					redirectUrl = String.format(OLD_CATEGORY_PAGE_FS, id);
				
				} else { //null or other type due to error
					redirectUrl = FALLBACK_PAGE;
				}
			}
	
			if (redirectUrl != null) {
				PageContext ctx = (PageContext) getJspContext();
				final HttpServletRequest req = (HttpServletRequest) ctx.getRequest();
				String originalUrl = req.getRequestURI();

				StringBuilder redirBuilder = new StringBuilder(redirectUrl);
				for (final String pName : new String[]{ "cm_vc", "ppPreviewId", "redirected" }) {
					if (req.getParameter(pName) != null) {
						redirBuilder.append("&")
							.append(pName)
							.append("=")
							.append(req.getParameter(pName));
					}
				}
				redirectUrl = redirBuilder.toString();

	        	LOGGER.debug("Redirecting from " +originalUrl+ " to " +redirectUrl);

	        	//To ensure that https requests get redirect to https correctly
	        	redirectUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + redirectUrl;
				((HttpServletResponse)ctx.getResponse()).sendRedirect(redirectUrl);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FDUserI getUser() {
		return user;
	}
	
	public void setUser( FDUserI user ) {
		this.user = user;
	}

	public boolean isOldToNewDirection() {
		return oldToNewDirection;
	}

	public void setOldToNewDirection(boolean oldToNewDirection) {
		this.oldToNewDirection = oldToNewDirection;
	}
	
}
