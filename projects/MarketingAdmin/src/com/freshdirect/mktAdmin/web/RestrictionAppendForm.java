package com.freshdirect.mktAdmin.web;

import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.RestrictionListAppendBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;
import com.freshdirect.mktAdmin.validation.RestrictionAppendValidator;


@Controller
@RequestMapping("/appendRestriction.do")
public class RestrictionAppendForm  {
	
	@Autowired
	private MarketAdminServiceIntf marketAdminService;	
	@Autowired
	private RestrictionAppendValidator restrictionListAppenddValidator;
	
	

	
	
	private final static Category LOGGER = LoggerFactory.getInstance(RestrictionAppendForm.class);
	

	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model, 
			@RequestParam(value = "promotionCode", required = false) String promotionCode) throws ServletException {
		// get the Pet referred to by id in the request		
			//request.setAttribute("actionTypes", EnumListUploadActionType.getEnumList());
			RestrictionListAppendBean command=new RestrictionListAppendBean();			
			System.out.println("INSIDE1 formBackingObject1 :"+promotionCode);
			if(promotionCode==null) throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));
			//if(promotionCode!=null)			     
			command.setPromotionCode(promotionCode);
			model.addAttribute("command", command);
			return "restAppendform";			
	}
	
	
	
	protected void onBind(HttpServletRequest request, Object command) {
//		FileUploadBean model = (FileUploadBean) command;
//		String fileContentType=request.getParameter("fileContentType");
//		if(fileContentType!=null){ 
//			EnumFileContentType enmFileContentType =EnumFileContentType.getEnum(fileContentType);
//			if(enmFileContentType!=null){
//				model.setFileContentType(enmFileContentType);
//			}
//		}				
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
    protected String processSubmit(@ModelAttribute("command") RestrictionListAppendBean command,
        BindingResult result, ModelMap model, HttpServletRequest request) throws Exception {
    	RestrictionListAppendBean bean=null;    	
    	
    	 onBind(request,command);
    	 
    	 restrictionListAppenddValidator.validate(command, result);
 		
 		if (result.hasErrors()) {

 			model.addAllAttributes(result.getModel());
 			return "restAppendform";
 		}
 		
    	try
    	{    		
	         // cast the beani
	        bean = (RestrictionListAppendBean) command;	       	        	        	        	        
	        marketAdminService.appendRestrictedCustomersFromEmailAddress(bean.getDecodedCustomerIds(),bean.getPromotionCode());	                                              	        	        
    	}
    	catch(MktAdminApplicationException exception){        		    		
    		if(exception.getExceptionList()!=null){    		     
    		     Iterator iterator=exception.getExceptionList().iterator();
    		     while(iterator.hasNext()){
    		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	 
    		    	 result.rejectValue("customerIds", e.getErrorCode(),e.getPlaceHolders()," lot of customer Ids are not proper");
    		     }
    		}else{
    			result.rejectValue("customerIds", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}
    		return "restAppendform";
    	}
    	
    	request.setAttribute("success","success.listappend");
    	model.addAttribute("command", bean);
        return "restAppendform";
    }

	@InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(EnumFileContentType.class, new EnumFileContentTypeSupport());		
        // now Spring knows how to handle multipart object and convert them
    }

	

}



