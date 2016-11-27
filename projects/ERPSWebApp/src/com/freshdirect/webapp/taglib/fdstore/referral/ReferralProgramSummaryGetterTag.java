/**
 * 
 * SiteEmailControllerTag.java
 * Created Dec 6, 2002
 */
package com.freshdirect.webapp.taglib.fdstore.referral;

/**
 *
 *  @author knadeem
 */
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.referral.FDReferralProgramManager;
import com.freshdirect.fdstore.referral.ReferralSearchCriteria;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ReferralProgramSummaryGetterTag extends AbstractGetterTag implements SessionName {
	 
 	 private String pageName=null;
 	 private static final String REF_PRG_PAGE_NAME="ref_prg1";
 	private static final String REF_CHA_PAGE_NAME="ref_cha1";
 	private static final String REF_CAMP_PAGE_NAME="ref_camp1";
 	private static final String REF_OBJ_PAGE_NAME="ref_obj1";
 	private static final String REF_PART_PAGE_NAME="ref_part1";
 	
	 public String getPageName() {
			return pageName;
		}

		public void setPageName(String pageName) {
			this.pageName = pageName;
		}
	 
	 protected Object getResult() throws Exception {
		    try {
				Collection result=null;
				ActionResult errorResult=new ActionResult();
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();				
				ReferralSearchCriteria criteria=constructReferralSearchCriteria(request);
				pageContext.setAttribute("SearchCriteria",criteria);
				if(this.pageName!=null && REF_PRG_PAGE_NAME.equalsIgnoreCase(pageName)){				
					result=FDReferralProgramManager.getReferralPrograms(criteria);				
				}
				if(this.pageName!=null && REF_CHA_PAGE_NAME.equalsIgnoreCase(pageName)){					
					result=FDReferralProgramManager.getReferralChannels(criteria);				
				}
				if(this.pageName!=null && REF_CAMP_PAGE_NAME.equalsIgnoreCase(pageName)){								
					result=FDReferralProgramManager.getReferralCampaigns(criteria);				
				}
				if(this.pageName!=null && REF_OBJ_PAGE_NAME.equalsIgnoreCase(pageName)){							
					result=FDReferralProgramManager.getReferralObjective(criteria);				
				}
				if(this.pageName!=null && REF_PART_PAGE_NAME.equalsIgnoreCase(pageName)){			
					result=FDReferralProgramManager.getReferralPartners(criteria);				
				}								
				pageContext.setAttribute("summaryResult", errorResult);
				return result;
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
	}
	
	 
	public ReferralSearchCriteria constructReferralSearchCriteria(HttpServletRequest request){
		ReferralSearchCriteria criteria=new ReferralSearchCriteria();
		String soryColumnName=request.getParameter("SORT_COLUMN_NME");
		if(soryColumnName==null || soryColumnName.trim().length()==0){
			soryColumnName="ID";
		}
		criteria.setSortByColumnName(soryColumnName);
		if(request.getParameter("START_INDEX")!=null && request.getParameter("START_INDEX").trim().length()>0)
		{
			criteria.setStartIndex(Integer.parseInt(request.getParameter("START_INDEX")));
			
		}
		else
		{
			criteria.setStartIndex(0);			
		}
		if(request.getParameter("RCD_SIZE")!=null && request.getParameter("RCD_SIZE").trim().length()>0)
		{
			criteria.setTotalRcdSize(Integer.parseInt(request.getParameter("RCD_SIZE")));
		}

		return criteria;
	}
	 	 	 		 		 	 	
	 public static class TagEI extends AbstractGetterTag.TagEI {						
		public String getResultType() {
			return "java.util.List";
		}  
	 }	      	  

}


