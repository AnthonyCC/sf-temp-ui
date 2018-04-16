package com.freshdirect.webapp.ajax.informational;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class InformationalServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -1986915980748551485L;
	private static final String ACTION_ORDERMODIFY = "ordermodify";
	private static final String ACTION_ORDERMODIFY_MINIMIZE = "ordermodify_minimize";
	private static final String ACTION_ORDERMODIFY_MAXIMIZE = "ordermodify_maximize";
	private static final String ACTION_ORDERMODIFY_SEEN = "ordermodify_seen";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		InformationalData result = new InformationalData();
		FDSessionUser sessionUser = (FDSessionUser) user;
		
		try {
			String action = getAction(request);
			result.setAction(action);
			
			/* populate response based on action */
			if (user.getLevel() == FDUserI.SIGNED_IN && 
				(action.equalsIgnoreCase(ACTION_ORDERMODIFY))
			) {
				/* additional logic based on EStore */
				EnumEStoreId eStore = getEStore(request, user);
				
				int viewCount = user.getInformOrderModifyViewCount(eStore); /* auto increments */
				int viewCountLimit = FDStoreProperties.getInformOrderModifyViewCountLimit();
				
				result.setViewCount(viewCount);
				result.setViewCountLimit(viewCountLimit);
				
				if (viewCount <= viewCountLimit) { /* only load media if needed */
					result.setMedia( loadMedia(FDStoreProperties.getInformOrderModifyMediaPath()) );
				}
				
				result.setShow(sessionUser.isShowingInformOrderModify());
				
				result.setSuccess(true);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeResponseData(response, result);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		InformationalData result = new InformationalData();
		FDSessionUser sessionUser = (FDSessionUser) user;
		
		try {
			String action = getAction(request);
			result.setAction(action);
			
			/* populate response based on action */
			if ( user.getLevel() == FDUserI.SIGNED_IN && 
				(
					action.equalsIgnoreCase(ACTION_ORDERMODIFY_MINIMIZE) || 
					action.equalsIgnoreCase(ACTION_ORDERMODIFY_MAXIMIZE) || 
					action.equalsIgnoreCase(ACTION_ORDERMODIFY_SEEN)
				)
			) {
				/* additional logic based on EStore */
				EnumEStoreId eStore = getEStore(request, user);
				int viewCountLimit = FDStoreProperties.getInformOrderModifyViewCountLimit();
				
				if (action.equalsIgnoreCase(ACTION_ORDERMODIFY_MINIMIZE)) {
					user.setInformOrderModifyViewCount(eStore, 0);
				} else if (action.equalsIgnoreCase(ACTION_ORDERMODIFY_MAXIMIZE)) {
					user.setInformOrderModifyViewCount(eStore, viewCountLimit);
				} else if (action.equalsIgnoreCase(ACTION_ORDERMODIFY_SEEN)) {
					/* we want the opposite of the value sent, i.e. value=true == setShowingInformOrderModify(false)
					 * false here is turning it off and true would be turning it back on */
					boolean value = (request.getParameter("value") != null) ? !((Boolean.valueOf(request.getParameter("value"))).booleanValue()) : false;
					
					/* set/clear inform ordermodify flag */
					sessionUser.setShowingInformOrderModify(value);
				}
				
				int viewCount = user.getInformOrderModifyViewCount(eStore, false); //get new value
				
				result.setViewCount(viewCount);
				result.setViewCountLimit(viewCountLimit);
				
				if (viewCount <= viewCountLimit) { /* only load media if needed */
					result.setMedia( loadMedia(FDStoreProperties.getInformOrderModifyMediaPath()) );
				}
				
				result.setShow(sessionUser.isShowingInformOrderModify());
				
				result.setSuccess(true);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeResponseData(response, result);
	}

	/* Default to requiring GUEST (or higher) level. Exclude levels in doGet/doPost */
	@Override
	protected int getRequiredUserLevel() {		
		return FDUserI.GUEST;
	}
	
	/* get EStore, or fall back to the current context */
	private EnumEStoreId getEStore(HttpServletRequest request, FDUserI user) {
		EnumEStoreId EStore = EnumEStoreId.valueOfContentId(request.getParameter("estore"));
		return (EStore != null) ? EStore : user.getUserContext().getStoreContext().getEStoreId();
	}

	private String getAction(HttpServletRequest request) {
		String action = request.getParameter("action");
		return (action != null) ? action : "";
	}

	/* pass-through */
	private String loadMedia(String path) throws IOException, TemplateException {
		return loadMedia(path, null);
	}
	
	private String loadMedia(String path, Map parameters) throws IOException, TemplateException {
		StringWriter out = new StringWriter();
		String result = "";
		if ( path != null && !"".equals(path.trim()) ) {
			try {
				MediaUtils.render(path, out, parameters, null);
				result = out.toString();
			} finally {
				out.close();
			}
		}
		return result;
	}
	
	@Override
	protected boolean synchronizeOnUser() {
		// TODO Auto-generated method stub
		return false;
	}

}
