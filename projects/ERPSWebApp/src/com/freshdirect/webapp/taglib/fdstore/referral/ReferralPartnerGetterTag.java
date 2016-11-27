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
import com.freshdirect.fdstore.referral.ReferralPartner;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ReferralPartnerGetterTag extends AbstractGetterTag implements SessionName {


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
				if(this.pageName!=null && "ref_part2".equalsIgnoreCase(pageName))
				{
					if("create".equalsIgnoreCase(actionName))
					{
						resultObj=createReferralPartner(request,result);
					}
					if("update".equalsIgnoreCase(actionName))
					{
						resultObj=updateReferralPartner(request,result);
					}
					if("save".equalsIgnoreCase(actionName))
					{
						resultObj=saveReferralPartner(request,result);
					}
					if("delete".equalsIgnoreCase(actionName))
					{											
						resultObj=deleteReferralPartner(request,result);						
					}			

					if(resultObj==null)
						resultObj=new ReferralPartner();
				}
				
			} catch (FDException e) {
				//e.printStackTrace();
				throw new JspException(e);
			}						
			
			pageContext.setAttribute("result", result);
			return resultObj;
	}

		public ReferralPartner deleteReferralPartner(HttpServletRequest request,ActionResult result) throws FDException
		{
			String refProgIds[]=request.getParameterValues("selectRefPart");
			ReferralPartner partner=null;
			if(refProgIds!=null && refProgIds.length>0)
			{
				Collection collection=FDReferralProgramManager.getReferralProgarmforRefPartner(refProgIds);
				if(collection.isEmpty())
				{
					FDReferralProgramManager.removeReferralPartner(refProgIds);					
					request.setAttribute("hasError","false");
					partner=new ReferralPartner();
				}
				else
				{								
				    result.addError(true,"delResult","Cannot delete the Referral Partner which is being Used.");
				    partner=FDReferralProgramManager.getReferralPartnerModel(refProgIds[0]);
				    request.setAttribute("actionType","save");
				}									
			}					  		
			return partner;
		}
		 
		 
		public ReferralPartner updateReferralPartner(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralPartner partner=null;			   
			   String refPartId=request.getParameter("selectRefPart");			   
			   partner=FDReferralProgramManager.getReferralPartnerModel(refPartId);
			   request.setAttribute("actionType","save");
			   return partner;			   
		}

		public ReferralPartner saveReferralPartner(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralPartner partner=null;			   
			   ReferralPartnerForm form=new ReferralPartnerForm();		   
			   form.populateForm(request);
			   form.validateForm(result);
			   if(result.isSuccess())
			   {	
				 partner=form.getReferralPartner();
				 FDReferralProgramManager.updateReferralPartner(partner);							 				 			 
				 partner=new ReferralPartner();
				 request.setAttribute("hasError","false");
			   }
			   else
			   {				   
				   partner=form.getReferralPartner();
				   request.setAttribute("actionType","save");
				   request.setAttribute("hasError","true");
			   }			   
			   return partner;			   
		}

		
	public ReferralPartner createReferralPartner(HttpServletRequest request,ActionResult result) throws FDException 
	{
		   ReferralPartner partner=null;
		   HttpSession session = pageContext.getSession();	
		   ReferralPartnerForm form=new ReferralPartnerForm();		   
		   form.populateForm(request);
		   form.validateForm(result);
		   if(result.isSuccess())
		   {	
			 partner=FDReferralProgramManager.createReferralPartner(form.getReferralPartner());	
			 partner= new ReferralPartner();	
			 request.setAttribute("hasError","false");
		   }
		   else
		   {			   
			   partner=form.getReferralPartner();
			   request.setAttribute("hasError","true");
		   }
		   
		   return partner;
	}
	
	 
	 
	 public static class ReferralPartnerForm extends ReferralPartner implements WebFormI {
		 
		 	private String id=null;
		 	private String name=null;
			private String description=null;			
			private String actionName=null;
			
		 	public void populateForm(HttpServletRequest request) {
		 		id= request.getParameter("refPartId");
		 		name  = request.getParameter("refPartName");		 		
		 		description=request.getParameter("refPartDesc"); 		 				 		
		 		actionName=request.getParameter("actionName");	
		 	}


		 	public void validateForm(ActionResult result) {
		 		
		 		result.addError(name==null || name.trim().length() < 1,
		 		"name", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		if(!"save".equalsIgnoreCase(actionName))
		 		{
				 	try {
						result.addError(FDReferralProgramManager.isReferralPartnerNameExist(name),
						"name", "<br> Referral Partner Name already Exists"
						);
					} catch (FDResourceException e) {
						// TODO Auto-generated catch block
						throw new FDRuntimeException(e);
					}		 						 	
		 		}
		 			 		
		 		result.addError(description==null || description.length() < 1,
		 		"description", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 				 				 		
		 	}
		 	
	
		 	public ReferralPartner getReferralPartner() throws FDResourceException
		 	{
		 		if(id!=null && id.trim().length()>0)
		 			this.setPK(new PrimaryKey(id));
		 		
		 		if(name!=null)
		 		    super.setName(this.name);
		 		
		 		if(this.description!=null)
		 		     super.setDescription(this.description);		 				 
		 		
		 		return this;
		 	}	 
	 }
	 	 		 	 

	 public static class TagEI extends AbstractGetterTag.TagEI 
	 {						
		public String getResultType() {
			System.out.println("ReferralPartner is getting called****");
			return "com.freshdirect.fdstore.referral.ReferralPartner";
		}  
	 }	      	  
}


