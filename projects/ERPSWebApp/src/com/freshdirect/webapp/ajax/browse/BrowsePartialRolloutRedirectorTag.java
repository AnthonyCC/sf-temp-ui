package com.freshdirect.webapp.ajax.browse;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.RecipeDepartment;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.util.FDURLUtil;

public class BrowsePartialRolloutRedirectorTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(BrowsePartialRolloutRedirectorTag.class);

    private static final String BROWSE_PAGE_FS = "/browse.jsp?id=%s";
    private static final String OLD_DEPARTMENT_PAGE_FS = "/department.jsp?deptId=%s";
    private static final String OLD_CATEGORY_PAGE_FS = "/category.jsp?catId=%s";
    private static final String FALLBACK_PAGE = "/";

    private String id;
    private boolean oldToNewDirection;
    private FDUserI user;

    @Override
    public void doTag() throws JspException, IOException {
        final PageContext ctx = (PageContext) getJspContext();
        final HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        final boolean disabledPartialRolloutRedirector = CmsFilteringNavigator.isDisabledPartialRolloutRedirector(request);
        if (!disabledPartialRolloutRedirector && FDStoreProperties.isBrowseRolloutRedirectEnabled()) {
            
            // figure out the redirect url
            if (oldToNewDirection) {
                String redirectUrl = String.format(BROWSE_PAGE_FS, id);

                final String originalUrl = request.getRequestURI();

                redirectUrl = FDURLUtil.decorateRedirectUrl(redirectUrl, request);

                LOGGER.debug("Redirecting from " + originalUrl + " to " + redirectUrl);

                // To ensure that https requests get redirect to https correctly
                redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
                HttpServletResponse httpServletResponse = (HttpServletResponse) ctx.getResponse();
                httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpServletResponse.setHeader("Location", redirectUrl);
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

    public void setUser(FDUserI user) {
        this.user = user;
    }

    public boolean isOldToNewDirection() {
        return oldToNewDirection;
    }

    public void setOldToNewDirection(boolean oldToNewDirection) {
        this.oldToNewDirection = oldToNewDirection;
    }

}
