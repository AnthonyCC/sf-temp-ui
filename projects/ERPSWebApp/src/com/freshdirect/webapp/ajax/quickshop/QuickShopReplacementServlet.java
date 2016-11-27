package com.freshdirect.webapp.ajax.quickshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class QuickShopReplacementServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= 2600362489117146150L;
	
	@SuppressWarnings( "unused" )
	private static final Logger LOG = LoggerFactory.getInstance(QuickShopReplacementServlet.class);
	
	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
		
	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		
		@SuppressWarnings("unchecked")
		List<String> replacements = (List<String>)request.getSession().getAttribute( SessionName.SESSION_QS_REPLACEMENT );
		if ( replacements == null ) {
			replacements = new ArrayList<String>();
			request.getSession().setAttribute( SessionName.SESSION_QS_REPLACEMENT, replacements );
		}
		
		String itemId = request.getParameter( "itemId" );		
		if ( itemId != null ) {
			replacements.add( itemId );
		}
		
		Map<String,String> respMap = new HashMap<String,String>();
		respMap.put( "temporaryReplacement", itemId );
		writeResponseData( response, respMap );
	}
	
}
