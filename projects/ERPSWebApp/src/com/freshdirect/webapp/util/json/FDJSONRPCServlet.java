package com.freshdirect.webapp.util.json;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.AutoCompleteFacade;
import com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade;
import com.freshdirect.webapp.util.MediaAjaxFacade;
import com.metaparadigm.jsonrpc.JSONRPCServlet;


public class FDJSONRPCServlet extends JSONRPCServlet {

	private static final long serialVersionUID = -5501176789143025343L;

	private static Category LOGGER = LoggerFactory.getInstance(FDJSONRPCServlet.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassCastException {
		
		HttpSession session = request.getSession();
		
		if (session.isNew()) {
			
			FDJSONRPCBridge bridge = new FDJSONRPCBridge();
			bridge.registerObject("MediaFacade",MediaAjaxFacade.create());
			bridge.registerObject("CCLFacade",CustomerCreatedListAjaxFacade.create());
			bridge.registerObject("AutoCompleteFacade", AutoCompleteFacade.create());
			
			session.setAttribute("JSONRPCBridge",bridge);
			session.setAttribute("timeout","true");
			LOGGER.debug("Session timeout");
		}
		
		super.service(request,response);
	}
}
