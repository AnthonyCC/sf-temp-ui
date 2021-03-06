package com.freshdirect.webapp.ajax.browse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class BrowsePotatoTag extends SimpleTagSupport{

	private static final Logger LOGGER = LoggerFactory.getInstance( BrowsePotatoTag.class );

	private String name = "browsePotato";

	// PDP related
	private boolean pdp = false;
	private String nodeId;

	// Special layout related
	private boolean specialLayout = false;

	@Override
	public void doTag() throws JspException, IOException {

		PageContext ctx = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();

		try {
			FDSessionUser user = (FDSessionUser) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);
			CmsFilteringNavigator nav = null;

			if(!pdp && !specialLayout){
				nav = CmsFilteringNavigator.createInstance((HttpServletRequest)ctx.getRequest(), user.getUser());
			}else{
				nav = new CmsFilteringNavigator();
				if(pdp){
					nav.setPdp(true);
					nav.setProductId(request.getParameter(QueryParameter.PRODUCT_ID));
				}else{
					nav.setSpecialPage(true);
				}
				nav.setId(nodeId);
				nav.parseFilteringFlowType(request);

				/* TEMP FIX for SUPPORT-8657 */
                ContentNodeModel node = ContentFactory.getInstance().getContentNode(nodeId);
                if (node != null
                		&& (node instanceof CategoryModel && ((CategoryModel) node).getLayout(0).getId() == 301) /* meal kits */
                ) {
					String pageSizeStr = request.getParameter("pageSize"); //allow override
					int pageSizeInt = 999; //default
					if (pageSizeStr != null) {
						try {
							pageSizeInt = Integer.parseInt(pageSizeStr);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					nav.setPageSize(pageSizeInt); //set
				} else {
					nav.setPageSize(FDStoreProperties.getBrowsePageSize());
				}

			}

			final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(nav, user);

			ctx.setAttribute(name, DataPotatoField.digBrowse(result));

		} catch (InvalidFilteringArgumentException e) {
			switch (e.getType()){
				case NODE_IS_RECIPE_DEPARTMENT:
				case SPECIAL_LAYOUT:{
					String url = e.getRedirectUrl();
					LOGGER.debug("Forwarding request to "+ url);

					try {
						request.getRequestDispatcher(url).forward(request,ctx.getResponse());
						throw new SkipPageException();
					} catch (ServletException se) {
						throw new JspException(se);
					}
				}
				case TERMINATE:{
					LOGGER.error(e.getMessage());
					break;
				}

				default:
					LOGGER.error("Invalid arguments on page " + request.getRequestURL() + " redirecting to " + e.getRedirectUrl() + ". Message: " +e.getMessage());
					if(null !=e.getRedirectUrl() && !"".equals(e.getRedirectUrl().trim())){
						((HttpServletResponse)ctx.getResponse()).sendRedirect(e.getRedirectUrl());
					} else {
						LOGGER.warn("Redirect URL not specified for : "+request.getRequestURL()+ ". Agent: "+request.getHeader("User-Agent")+". Referrer: "+request.getHeader("referer"));
						throw new FDNotFoundException("Redirect URL not specified. "+e.getMessage());
					}
					break;
			}

		} catch (FDResourceException e){
			throw new JspException(e);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPdp() {
		return pdp;
	}

	public void setPdp(boolean pdp) {
		this.pdp = pdp;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isSpecialLayout() {
		return specialLayout;
	}

	public void setSpecialLayout(boolean specialLayout) {
		this.specialLayout = specialLayout;
	}

}
