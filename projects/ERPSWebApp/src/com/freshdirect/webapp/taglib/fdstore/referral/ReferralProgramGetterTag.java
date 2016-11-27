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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.referral.EnumReferralProgramStatus;
import com.freshdirect.fdstore.referral.FDReferralProgramManager;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ReferralProgramGetterTag extends AbstractGetterTag implements SessionName {

	
 	   private String pageName=null;	   
	   private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	 
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
				if(this.pageName!=null && "ref_prg2".equalsIgnoreCase(pageName))
				{
					if("create".equalsIgnoreCase(actionName)){
						resultObj=createReferralProgram(request,result);
					}
					if("update".equalsIgnoreCase(actionName)){
						resultObj=updateReferralProgram(request,result);
					}
					if("save".equalsIgnoreCase(actionName)){
						resultObj=saveReferralProgram(request,result);
					}
					if("delete".equalsIgnoreCase(actionName)){					
						String refProgIds[]=request.getParameterValues("selectRefPrg");															
						if(refProgIds!=null && refProgIds.length>0)						{
							FDReferralProgramManager.removeReferralProgram(refProgIds);	
							request.setAttribute("hasError","false");
						}					  
					}				

					if(resultObj==null){
						resultObj=new ReferralProgram();
					}
				}
			} catch (FDException e) {				
				throw new JspException(e);
			}						
			
			pageContext.setAttribute("result", result);
			return resultObj;
	}

		 
		public ReferralProgram updateReferralProgram(HttpServletRequest request,ActionResult result) throws FDException 
		{			   
			   ReferralProgram program=null;			   
			   String refProgId=request.getParameter("selectRefPrg");			   
			   ReferralProgram prg=FDReferralProgramManager.getReferralProgramModel(refProgId);
			   request.setAttribute("actionType","save");
			   return prg;
			   
		}

		public ReferralProgram saveReferralProgram(HttpServletRequest request,ActionResult result) throws FDException 
		{			   
			   ReferralProgram program=null;
			   HttpSession session = pageContext.getSession();	
			   ReferralProgramForm form=new ReferralProgramForm();		   
			   form.populateForm(request);
			   form.validateForm(result);
			   if(result.isSuccess()){	
				
				 program=form.getReferralProgram();
				 FDReferralProgramManager.updateReferralProgram(program);							 				 			 			 
				 program=new ReferralProgram();
				 request.setAttribute("hasError","false");
			   }
			   else{				   
				  
				   program=form.getReferralProgram();
				   request.setAttribute("actionType","save");
				   request.setAttribute("hasError","true");
			   }			   
			   return program;			   
		}

		
	public ReferralProgram createReferralProgram(HttpServletRequest request,ActionResult result) throws FDException {		   
		   ReferralProgram program=null;
		   ReferralProgramForm form=new ReferralProgramForm();		   
		   form.populateForm(request);
		   form.validateForm(result);
		   if(result.isSuccess()) {				 
			 program=FDReferralProgramManager.createReferralProgram(form.getReferralProgram());				 
			 program= new ReferralProgram();	
			 request.setAttribute("hasError","false");
		   }
		   else{			   			   
			   program=form.getReferralProgram();
			   request.setAttribute("hasError","true");
		   }		   
		   return program;
	}
	
	 
	 
	 public static class ReferralProgramForm extends ReferralProgram implements WebFormI {
				 	
		 	private String id=null;
		 	private String name=null;
			private String description=null;
			private String startDate=null;
			private String expDate=null;
			private String creativeDesc=null;
			private String campaignId=null;
			private String channelId=null;
			private String partnerId=null;
			private String status=null;
			private String promotionCode=null;
			private String creativeUrl="";
			private String actionName=null;				
			public void populateForm(HttpServletRequest request) {
		 		id= request.getParameter("refProgId");
		 		name  = request.getParameter("refProgName");		 		
		 		description = request.getParameter("refProgDesc");
		 		startDate=request.getParameter("refProgStartDate"); 
		 		expDate=request.getParameter("refProgExpDate");
		 		creativeDesc=request.getParameter("refProgCrtvDesc");
		 		status=request.getParameter("status");		 		
		 		channelId=request.getParameter("referralChannel");
		 		partnerId=request.getParameter("referralPartner");
		 		campaignId=request.getParameter("referralCampaign");		 		
		 		promotionCode=request.getParameter("refProgPromoCode");
		 		creativeUrl=request.getParameter("refProgCreativeUrl");
		 		actionName=request.getParameter("actionName");		 				 				 				 		
		 		
		 	}


		 	public void validateForm(ActionResult result) {
		 		result.addError(name==null || name.trim().length() < 1,
		 		"name", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		if(!"save".equalsIgnoreCase(actionName))
		 		{
				 	try {
						result.addError(FDReferralProgramManager.isReferralProgramNameExist(name),
						"name", "<br> Referral Program Name already Exists"
						);
					} catch (FDResourceException e) {
						// TODO Auto-generated catch block
						throw new FDRuntimeException(e);
					}		 						 	
		 		}
		 			
		 		result.addError(description==null || description.length() < 1,
		 		"description", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);

		 		result.addError(startDate==null || startDate.length() < 1,
		 		"startDate", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 				 				 		
		 		result.addError(expDate==null || expDate.length() < 1,
		 		"expDate", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		if(startDate!=null && startDate.trim().length()>0)	{
		 			if(this.expDate!=null && this.expDate.trim().length()>0){		 					 	
			 			try {				 				
				 		   result.addError(format.parse(startDate).after(format.parse(expDate)), "expDate", "<br> Start Date Cannot be Greater than Expiry Date");								
						} catch (ParseException e) {
							throw new FDRuntimeException ("Cannot parse Date");
						}
		 			}
		 		}		 				 		
		 		result.addError(status==null || status.length() < 1,
  			    "status", "<br>"+SystemMessageList.MSG_REQUIRED
				);
		 		
		 		if(promotionCode!=null && promotionCode.trim().length() > 1)
		 		{
		 		   result.addError(PromotionFactory.getInstance().getPromotion(promotionCode)==null,
		  		  "promotionCode", "<br> PromtionCode does not Exist "
				  );
		 		}

		 		result.addError(channelId==null || channelId.trim().length() < 1,
		 		"referralChannel", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);

		 		result.addError(partnerId==null || partnerId.trim().length() < 1,
		 		"referralPartner", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 		
		 		result.addError(campaignId==null || campaignId.trim().length() < 1,
		 		"referralCampaign", "<br>"+SystemMessageList.MSG_REQUIRED
	        	);
		 	}
		 	
	
		 	public ReferralProgram getReferralProgram() throws FDResourceException 	{
		 		if(id!=null && id.trim().length()>0)
		 			this.setPK(new PrimaryKey(id));
		 				 		
		 		if(name!=null && name.trim().length()>0){
		 		    super.setName(this.name);
		 		}
		 		if(this.description!=null){
		 		     super.setDescription(this.description);
		 		}
		 		
		 		if(this.startDate!=null && this.startDate.trim().length()>0)
		 		{
		 			try {
						super.setStartDate(format.parse(startDate));
					} catch (ParseException e) {
						throw new FDResourceException ("Cannot parse Date");
					}		 			
		 		}

		 		if(this.expDate!=null && this.expDate.trim().length()>0)
		 		{		 			
		 			try {
						super.setExpDate(format.parse(expDate));						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						throw new FDResourceException ("Cannot parse Date");		
					}		 			
		 		}

		 		if(this.creativeDesc!=null) {super.setCreativeDesc(this.creativeDesc);}		 		
		 		if(this.promotionCode!=null) {super.setPromotionCode(this.promotionCode);}
		 		if(this.creativeUrl!=null) {super.setCreativeUrl(this.creativeUrl);}
		 		if(EnumReferralProgramStatus.getEnum(this.status)!=null){
		 			super.setStatus(EnumReferralProgramStatus.getEnum(this.status));
		 		}
		 		if(this.campaignId!=null && this.campaignId.trim().length()>0 && FDReferralProgramManager.getReferralCampaignModel(this.campaignId)!=null){
		 		    super.setCampaign(FDReferralProgramManager.getReferralCampaignModel(this.campaignId));
		 		}
		 		if(this.channelId!=null && this.channelId.trim().length()>0 && FDReferralProgramManager.getReferralChannleModel(this.channelId)!=null){
		 		     super.setChannel(FDReferralProgramManager.getReferralChannleModel(this.channelId));
		 		}
		 		if(this.partnerId!=null && this.partnerId.trim().length()>0 && FDReferralProgramManager.getReferralPartnerModel(this.partnerId)!=null){
		 		   super.setPartner(FDReferralProgramManager.getReferralPartnerModel(this.partnerId));
		 		}
		 		return this;
		 	}	 
	 }
	 	 		 	 
	 public static class TagEI extends AbstractGetterTag.TagEI 
	 {						
		public String getResultType() {
			return "com.freshdirect.fdstore.referral.ReferralProgram";
		}  
	 }	      	  
}


