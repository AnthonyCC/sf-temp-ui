package com.freshdirect.webapp.filters;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralHistory;
import com.freshdirect.fdstore.referral.ReferralProgramManager;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author knadeem Date May 18, 2005
 */
public class WebTrackingCodeFilter extends AbstractFilter {
	
	
	private final String filterName = this.getClass().getName();
	
	
	private static final Category LOGGER = LoggerFactory.getInstance(WebTrackingCodeFilter.class);

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
				
		String refProgId = null;
		String refTrkDtls = null;
		
		boolean userDirty = false;
		if (!this.isFilterApplied(request)) {
					
			refProgId = NVL.apply(request.getParameter("ref_prog_id"), "").trim();
			refTrkDtls = NVL.apply(request.getParameter("ref_trk_dtls"), "").trim();
			userDirty = updateUser(request, refProgId, refTrkDtls);

		}
        	
		filterChain.doFilter(request, response);		
	
		
		if (!this.isFilterApplied(request)) {
			userDirty |= updateUser(request, refProgId, refTrkDtls);
						
			if (userDirty) {
				HttpSession session = request.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				user.saveCart(true);
				session.setAttribute(SessionName.USER, user);
			}
		}
	}	
		
		
	/**
	 * @return true if the user needs to be persisted (ie. last ref tracking code was changed)
	 */
	private boolean updateUser(HttpServletRequest req, String refProgId, String refTrkDtls) {	
		HttpSession session = req.getSession(false);
		if (session == null) {
			return false;
		}
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		if (user == null) {
			return false;
		}
		
		
		if (!"".equals(refProgId) && !refProgId.equals(user.getLastRefProgId())) {
			user.setLastRefProgramId(refProgId);			
			createReferralHistory(req,refProgId,refTrkDtls,user);
			session.setAttribute("REF_PROG_ID",refProgId);	
			return true;
		}
		else if(!"".equals(refProgId))
		{
			String lastRefProgId=(String)session.getAttribute("REF_PROG_ID");
			if(lastRefProgId==null)
			{
				createReferralHistory(req,refProgId,refTrkDtls,user);
				session.setAttribute("REF_PROG_ID",refProgId);				
			}
		}
		
		return false;
	}
	
	private void createReferralHistory(HttpServletRequest req, String refProgId, String refTrkDtls,FDSessionUser user){		
		try {		
			ReferralHistory history=new ReferralHistory();
			history.setDateCreated(new Date());			
			history.setFdUserId(user.getUser().getPK().getId());
			history.setReferralProgramId(refProgId);
			ReferralProgramManager manager= ReferralProgramManager.getInstance();
			boolean isInternalInvitee=manager.isInternalReferralInvitee(refProgId);
			
			if(isInternalInvitee){
			   user.setLastRefProgInvtId(refTrkDtls);	
			   user.setLastRefTrkDtls(null);
			   history.setRefprgInvtId(refTrkDtls);
			}
			else{
			   user.setLastRefTrkDtls(refTrkDtls);
			   user.setLastRefProgInvtId(null);
			   history.setRefTrkKeyDtls(refTrkDtls);
			}
			
			FDReferralManager.createReferralHistory(history);
		} catch (FDException e) {			
			LOGGER.warn("Exception aoocured while creating the referral :"+e.getMessage()+"cookie"+user.getCookie());
		}		
	}
	
	public String getFilterName(){
		return this.filterName;
	}
}
