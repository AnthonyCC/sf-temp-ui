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
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.referral.FDReferralProgramManager;
import com.freshdirect.fdstore.referral.ReferralCampaign;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ReferralCampaignGetterTag extends AbstractGetterTag implements SessionName {

	
 	 private String pageName=null;	
	 
	   public String getPageName() {
			return pageName;
		}


		public void setPageName(String pageName) {
			this.pageName = pageName;
		}
	 
		 protected Object getResult() throws JspException {
			 
			 Object resultObj=null;
			 HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();							
			 String actionName=request.getParameter("actionName");			 	
			 ActionResult result=new ActionResult();			 
			try {
				if(this.pageName!=null && "ref_camp2".equalsIgnoreCase(pageName))
				{
					if("create".equalsIgnoreCase(actionName))
					{
						resultObj=createReferralCampaign(request,result);
					}
					if("update".equalsIgnoreCase(actionName))
					{
						resultObj=updateReferralCCampaign(request,result);
					}
					if("save".equalsIgnoreCase(actionName))
					{
						resultObj=saveReferralCampaign(request,result);
					}
					if("delete".equalsIgnoreCase(actionName))
					{
						resultObj=deleteReferralCampaigns(request,result);
					}				
					if(resultObj==null)
						resultObj=new ReferralCampaign();
				}
				
			} catch (FDException e) {
				// TODO Auto-generated catch block				
				throw new JspException(e);
			}						
			
			pageContext.setAttribute("result", result);
			return resultObj;
	}

		 public ReferralCampaign deleteReferralCampaigns(HttpServletRequest request,ActionResult result) throws FDException
		 {
						ReferralCampaign campaign=null;
						String refProgIds[]=request.getParameterValues("selectRefCamp");
						if(refProgIds!=null && refProgIds.length>0)
						{
							Collection collection=FDReferralProgramManager.getReferralProgarmforRefCampaign(refProgIds);
							if(collection.isEmpty())
							{
								FDReferralProgramManager.removeReferralCampaign(refProgIds);								
								request.setAttribute("hasError","false");
								campaign=new ReferralCampaign();
							}
							else
							{								
							    result.addError(true,"delResult","Cannot delete the Referral Campaign which is being Used.");							    
							    campaign=FDReferralProgramManager.getReferralCampaignModel(refProgIds[0]);
							    request.setAttribute("actionType","save");
							}						
						}							 
						return campaign;
			}
		 
		public ReferralCampaign updateReferralCCampaign(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralCampaign campaign=null;			   
			   String refCampId=request.getParameter("selectRefCamp");			   
			   campaign=FDReferralProgramManager.getReferralCampaignModel(refCampId);
			   request.setAttribute("actionType","save");
			   return campaign;			   
		}

		public ReferralCampaign saveReferralCampaign(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralCampaign campaign=null;
			   HttpSession session = pageContext.getSession();	
			   ReferralCampaignForm form=new ReferralCampaignForm();		   
			   form.populateForm(request);
			   form.validateForm(result);
			   if(result.isSuccess())
			   {	
				 campaign=form.getReferralCampaign();
				 FDReferralProgramManager.updateReferralCampaign(campaign);							 				 	 			
				 campaign=new ReferralCampaign();
				 request.setAttribute("hasError","false");
			   }
			   else
			   {				   
				   campaign=form.getReferralCampaign();
				   request.setAttribute("actionType","save");
				   request.setAttribute("hasError","true");
			   }			   
			   return campaign;			   
		}

		
	public ReferralCampaign createReferralCampaign(HttpServletRequest request,ActionResult result) throws FDException 
	{
		   ReferralCampaign campaign=null;
		   HttpSession session = pageContext.getSession();	
		   ReferralCampaignForm form=new ReferralCampaignForm();		   
		   form.populateForm(request);
		   form.validateForm(result);
		   if(result.isSuccess())
		   {	
			 campaign=FDReferralProgramManager.createReferralCampaign(form.getReferralCampaign());				 
			 campaign= new ReferralCampaign();
			 request.setAttribute("hasError","false");
		   }
		   else
		   {			   
			   campaign=form.getReferralCampaign();
			   request.setAttribute("hasError","true");
		   }
		   
		   return campaign;
	}
	
	 
	 
	 public static class ReferralCampaignForm extends ReferralCampaign implements WebFormI {
				 	
		 	private String id=null;
		 	private String name=null;
			private String description=null;			
			private String objectiveId=null;
			private String actionName=null;
			
		 	public void populateForm(HttpServletRequest request) {
		 		id= request.getParameter("refCampId");
		 		name  = request.getParameter("refCampName");		 		
		 		description=request.getParameter("refCampDesc");
		 		objectiveId=request.getParameter("referralObjective");
		 		actionName=request.getParameter("actionName");		 				 				 				 				 		
		 	}


		 	public void validateForm(ActionResult result) {
		 		result.addError(name==null || name.trim().length() < 1,
		 		"name", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		if(!"save".equalsIgnoreCase(actionName))
		 		{
		 			try
		 			{
				 	result.addError(FDReferralProgramManager.isReferralCampaignNameExist(name),
					"name", "<br> Referral Campaign Name already Exists"
					);
		 			}catch(FDResourceException e)
		 			{
		 				new FDRuntimeException(e);
		 			}
		 		}
		 			
		 		result.addError(description==null || description.length() < 1,
		 		"description", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);

		 		
		 		result.addError(objectiveId==null || objectiveId.trim().length() ==0,
				"referralObjective", "<br>"+SystemMessageList.MSG_REQUIRED
			    );
				 		
		 				
		 	}
		 	
	
		 	public ReferralCampaign getReferralCampaign() throws FDResourceException
		 	{
		 		if(id!=null && id.trim().length()>0)
		 			this.setPK(new PrimaryKey(id));
		 		
		 		if(name!=null){
		 		    super.setName(this.name);
		 		}
		 		if(this.description!=null){
		 		     super.setDescription(this.description);		 				 
		 		}
		 		if(this.objectiveId!=null && this.objectiveId.trim().length()>0 && FDReferralProgramManager.getReferralObjectiveModel(this.objectiveId)!=null){		 			
		 		     super.setObjective(FDReferralProgramManager.getReferralObjectiveModel(this.objectiveId));
		 		}
		 		return this;
		 	}	 
	 }
	 	 		 	 

	 public static class TagEI extends AbstractGetterTag.TagEI 
	 {						
		public String getResultType() {
			return "com.freshdirect.fdstore.referral.ReferralCampaign";
		}  
	 }	      	  
}


