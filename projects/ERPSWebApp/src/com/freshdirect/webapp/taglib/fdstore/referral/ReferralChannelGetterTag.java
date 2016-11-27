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
import com.freshdirect.fdstore.referral.ReferralChannel;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ReferralChannelGetterTag extends AbstractGetterTag implements SessionName {
	
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
				if(this.pageName!=null && "ref_cha2".equalsIgnoreCase(pageName))
				{
					if("create".equalsIgnoreCase(actionName))
					{
						resultObj=createReferralChannel(request,result);
					}
					if("update".equalsIgnoreCase(actionName))
					{
						resultObj=updateReferralChannel(request,result);
					}
					if("save".equalsIgnoreCase(actionName))
					{
						resultObj=saveReferralChannel(request,result);
					}
					if("delete".equalsIgnoreCase(actionName))
					{															
						resultObj=deleteReferralChannel(request,result);
					}					
					
					if(resultObj==null)
						resultObj=new ReferralChannel();
				}
				
			} catch (FDException e) {
				// TODO Auto-generated catch block
				throw new JspException(e);
			}						
			
			pageContext.setAttribute("result", result);
			return resultObj;
	 }

		public ReferralChannel deleteReferralChannel(HttpServletRequest request,ActionResult result) throws FDException
		{
					ReferralChannel channel=null;
					String refProgIds[]=request.getParameterValues("selectRefCha");
					if(refProgIds!=null && refProgIds.length>0)
					{
						Collection collection=FDReferralProgramManager.getReferralProgarmforRefChannel(refProgIds);
						if(collection.isEmpty())
						{
							FDReferralProgramManager.removeReferralChannel(refProgIds);	
							request.setAttribute("hasError","false");
							channel=new ReferralChannel();
						}
						else
						{								
						    result.addError(true,"delResult","Cannot delete the Referral Channel which is being Used.");
						    //channel=manager.getReferralChannel(refProgIds[0]);
						    channel=FDReferralProgramManager.getReferralChannleModel(refProgIds[0]);
						    request.setAttribute("actionType","save");						    
						}						
					}					 
					return channel;
		}
		 
		 
		public ReferralChannel updateReferralChannel(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralProgram program=null;			   
			   String refChaId=request.getParameter("selectRefCha");			   
			   ReferralChannel channel=FDReferralProgramManager.getReferralChannleModel(refChaId);
			   request.setAttribute("actionType","save");
			   return channel;
			   
		}

		public ReferralChannel saveReferralChannel(HttpServletRequest request,ActionResult result) throws FDException 
		{
			   ReferralChannel channel=null;
			   HttpSession session = pageContext.getSession();	
			   ReferralChannelForm form=new ReferralChannelForm();		   
			   form.populateForm(request);
			   form.validateForm(result);
			   if(result.isSuccess())
			   {	
				 channel=form.getReferralChannel();
				 FDReferralProgramManager.updateReferralChannel(channel);				 			
				 channel=new ReferralChannel();
				 request.setAttribute("hasError","false");
			   }
			   else
			   {				   
				   channel=form.getReferralChannel();
				   request.setAttribute("actionType","save");
				   request.setAttribute("hasError","true");
			   }			   
			   return channel;			   
		}

		
	public ReferralChannel createReferralChannel(HttpServletRequest request,ActionResult result) throws FDException 
	{
		   ReferralChannel channel=null;
		   HttpSession session = pageContext.getSession();	
		   ReferralChannelForm form=new ReferralChannelForm();		   
		   form.populateForm(request);
		   form.validateForm(result);
		   if(result.isSuccess())
		   {	
			 channel=FDReferralProgramManager.createReferralChannel(form.getReferralChannel());				 
			 channel= new ReferralChannel();			
			 request.setAttribute("hasError","false");
		   }
		   else
		   {			   
			   channel=form.getReferralChannel();
			   request.setAttribute("hasError","true");
			   System.out.println("failure11"+form.getName());
		   }		   
		   return channel;
	}
	
	 
	 
	 public static class ReferralChannelForm extends ReferralChannel implements WebFormI {

		
		 	
		 	private String id=null;
		 	private String name=null;
			private String description=null;
			private String type=null;
			private String actionName=null;
			
		 	public void populateForm(HttpServletRequest request) {
		 		id= request.getParameter("refChaId");
		 		name  = request.getParameter("refChaName");		 		
		 		type= request.getParameter("refChaType");
		 		description=request.getParameter("refChaDesc"); 		 				 		
		 		actionName=request.getParameter("actionName");		 				 				 				 				 		
		 	}


		 	public void validateForm(ActionResult result)  {
		 		result.addError(name==null || name.trim().length() < 1,
		 		"name", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 				 			
		 		result.addError(description==null || description.length() < 1,
		 		"description", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);

		 		result.addError(type==null || type.length() < 1,
		 		"type", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 				 				 		
		 		if(!"save".equalsIgnoreCase(actionName))
		 		{
		 			if(name!=null && type!=null)
		 			{
		 				try
		 				{
					 	result.addError(FDReferralProgramManager.isReferralChannelNameAndTypeExist(name,type),
						"name", "<br> Referral Channel Name and Type already Exists"
						);
		 				}catch(FDResourceException e){
		 					throw new FDRuntimeException(e);
		 				}
		 			}
		 		}
		 		
		 	}
		 	
	
		 	public ReferralChannel getReferralChannel() throws FDResourceException
		 	{
		 		if(id!=null && id.trim().length()>0)
		 			this.setPK(new PrimaryKey(id));
		 		
		 		if(name!=null)
		 		    super.setName(this.name);
		 		if(this.description!=null)
		 		     super.setDescription(this.description);
		 		
		 		if(this.type!=null) super.setType(this.type);		 		
		 		
		 		return this;
		 	}	 
	 }
	 
	 	

	 public static class TagEI extends AbstractGetterTag.TagEI 
	 {						
		public String getResultType() {
			return "com.freshdirect.fdstore.referral.ReferralChannel";
		}  
	 }
	      	 
}


