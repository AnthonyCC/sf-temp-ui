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
import com.freshdirect.fdstore.referral.ReferralObjective;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ReferralObjectiveGetterTag extends AbstractGetterTag implements SessionName {

	
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
				if(this.pageName!=null && "ref_obj2".equalsIgnoreCase(pageName)){
					if("create".equalsIgnoreCase(actionName)){
						resultObj=createReferralObjective(request,result);
					}
					if("update".equalsIgnoreCase(actionName)){
						resultObj=updateReferralObjective(request,result);
					}
					if("save".equalsIgnoreCase(actionName)){
						resultObj=saveReferralObjective(request,result);
					}					
					if("delete".equalsIgnoreCase(actionName)){
						resultObj=deleteReferralObjective(request,result);
					}					
					if(resultObj==null){						
						resultObj=new ReferralObjective();
					}
				}
				
			} catch (FDException e) {
				// TODO Auto-generated catch block			
				throw new JspException(e);
			}									
			pageContext.setAttribute("result", result);
			return resultObj;
	}

		 
		public ReferralObjective deleteReferralObjective(HttpServletRequest request,ActionResult result) throws FDException {
				ReferralObjective objective=null;
				String refProgIds[]=request.getParameterValues("selectRefObj");
				if(refProgIds!=null && refProgIds.length>0){					
					Collection collection=FDReferralProgramManager.getReferralCampaignforRefObjective(refProgIds);
					if(collection.isEmpty()){
						FDReferralProgramManager.removeReferralObjective(refProgIds);						
						request.setAttribute("hasError","false");
						objective=new ReferralObjective();
					}
					else{								
					    result.addError(true,"delResult","Cannot delete the Referral Objective which is being Used.");
					    objective=FDReferralProgramManager.getReferralObjectiveModel(refProgIds[0]);
					    request.setAttribute("actionType","save");
					}						
				}					  		
				return objective;
		}

		 
		public ReferralObjective updateReferralObjective(HttpServletRequest request,ActionResult result) throws FDException {
			   ReferralProgram program=null;			   
			   String refObjId=request.getParameter("selectRefObj");			   
			   ReferralObjective objective=FDReferralProgramManager.getReferralObjectiveModel(refObjId);
			   request.setAttribute("actionType","save");
			   return objective;			   
		}

		public ReferralObjective saveReferralObjective(HttpServletRequest request,ActionResult result) throws FDException {
			  ReferralObjective objective=null;
			   HttpSession session = pageContext.getSession();	
			   ReferralObjectiveForm form=new ReferralObjectiveForm();		   
			   form.populateForm(request);
			   form.validateForm(result);
			   if(result.isSuccess()){	
				 objective=form.getReferralObjective();
				 FDReferralProgramManager.updateReferralObjective(objective);							 				 		 			
				 objective=new ReferralObjective();				 
				 request.setAttribute("hasError","false");
			   }
			   else {				   
				   objective=form.getReferralObjective();
				   request.setAttribute("actionType","save");
				   request.setAttribute("hasError","true");
			   }			   
			   return objective;			   
		}

		
	public ReferralObjective createReferralObjective(HttpServletRequest request,ActionResult result) throws FDException {
		   ReferralObjective objective=null;
		   HttpSession session = pageContext.getSession();	
		   ReferralObjectiveForm form=new ReferralObjectiveForm();		   
		   form.populateForm(request);
		   form.validateForm(result);
		   if(result.isSuccess()) {	
			 objective=FDReferralProgramManager.createReferralObjective(form.getReferralObjective());				 
			 objective= new ReferralObjective();
			 request.setAttribute("hasError","false");			 
		   }
		   else{			   
			  objective=form.getReferralObjective();
			  request.setAttribute("hasError","true");			  
		   }		   
		   return objective;
	}
	
	 
	 
	 public static class ReferralObjectiveForm extends ReferralObjective implements WebFormI {
				 	
		 	private String id=null;
		 	private String name=null;
			private String description=null;			
			private String actionName=null;
			
		 	public void populateForm(HttpServletRequest request) {
		 		id= request.getParameter("refObjId");
		 		name  = request.getParameter("refObjName");		 		
		 		description=request.getParameter("refObjDesc"); 		 				 		
		 		actionName=request.getParameter("actionName");		 				 				 				 				 		
		 	}


		 	public void validateForm(ActionResult result) {
		 		result.addError(name==null || name.trim().length() < 1,
		 		"name", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		if(!"save".equalsIgnoreCase(actionName)){
				 	try {
						result.addError(FDReferralProgramManager.isReferralObjectiveNameExist(name),
						"name", "<br> Referral Objective Name already Exists"
						);
					} catch (FDResourceException e) {
						throw new FDRuntimeException(e);
					}		 						 	
		 		}
		 			
		 		result.addError(description==null || description.length() < 1,
		 		"description", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 				 				 		
		 	}
		 	
	
		 	public ReferralObjective getReferralObjective() throws FDResourceException	{
		 		if(id!=null && id.trim().length()>0){
		 			this.setPK(new PrimaryKey(id));
		 		}
		 		if(name!=null){
		 		    super.setName(this.name);
		 		}
		 		if(this.description!=null){
		 		     super.setDescription(this.description);
		 		}		 		
		 		return this;
		 	}	 
	 }
	 	 		 	 

	 public static class TagEI extends AbstractGetterTag.TagEI 
	 {						
		public String getResultType() {
			return "com.freshdirect.fdstore.referral.ReferralObjective";
		}  
	 }


	
  
    	  

}


