package com.freshdirect.webapp.taglib.menu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.storeapi.content.GlobalMenuItemModel;

/**
 * Fetches and includes the associated Global Menu Layout for the given Global Menu Layout ID.
 * 
 * @author tgelesz
 * 
**/
public class GlobalMenuItemPopupTag extends SimpleTagSupport {
	
	private static final Map<Integer,String> LAYOUT_MAP;
	private GlobalMenuItemModel globalMenuItem;
	
	static {
		LAYOUT_MAP = new HashMap<Integer, String>();
		LAYOUT_MAP.put(1, "/common/template/includes/globalnav/layout/single_section_layout.jsp");
		LAYOUT_MAP.put(2, "/common/template/includes/globalnav/layout/two_section_layout.jsp");
		LAYOUT_MAP.put(3, "/common/template/includes/globalnav/layout/four_section_layout.jsp");
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageCtx = (PageContext)getJspContext();
		ServletRequest req = pageCtx.getRequest();
		ServletResponse resp = pageCtx.getResponse();

		try {
			pageCtx.getServletContext().getRequestDispatcher(LAYOUT_MAP.get(globalMenuItem.getLayout())).include(req, resp);

		} catch (ServletException e) {
			throw new JspException(e);
		}
	}

	public GlobalMenuItemModel getGlobalMenuItem() {
		return globalMenuItem;
	}

	public void setGlobalMenuItem(GlobalMenuItemModel globalMenuItem) {
		this.globalMenuItem = globalMenuItem;
	}
	
}
