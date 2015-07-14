package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;
import com.freshdirect.mktAdmin.validation.RestrictionListUploadValidator;


@Controller
@RequestMapping("/editRestriction.do")
@SessionAttributes("command")
public class RestrictionListUploadForm extends AbstractMktAdminForm {
	
	
	@Autowired
	private MarketAdminServiceIntf marketAdminService;
	
	
	@Autowired
	private RestrictionListUploadValidator restrictionListUploadValidator;


	private final static Category LOGGER = LoggerFactory.getInstance(RestrictionListUploadForm.class);
	
	
	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model, 
			@RequestParam(value = "promotionCode", required = false) String promotionCode) throws ServletException {
		// get the Pet referred to by id in the request		
			//request.setAttribute("actionTypes", EnumListUploadActionType.getEnumList());
		    RestrictionListUploadBean command=new RestrictionListUploadBean();			
			System.out.println("INSIDE1 formBackingObject1 :"+promotionCode);
			if(promotionCode==null) throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));															
			command.setPromotionCode(promotionCode);
			model.addAttribute("command", command);
			referenceData(model, promotionCode);
			return "restListUploadform";			
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List getActionType(String promotionCode){		
		System.out.println("INSIDE1 getActionType :"+promotionCode);
		List list=null;
		//if(promotionCode==null) throw new MktAdminSystemException("1001",new IllegalArgumentException("promotionCode parameter is required"));
		if(promotionCode!=null)	{						
			try {
				if(!this.marketAdminService.isRestrictedCustomersExist(promotionCode)){
				   list=new ArrayList();
				   list.add(EnumListUploadActionType.CREATE);				   
				}   	
				else
				{
					list=new ArrayList();
					list.add(EnumListUploadActionType.ADD);
					list.add(EnumListUploadActionType.DELETE);
					list.add(EnumListUploadActionType.REPLACE);					
				}
			} catch (MktAdminApplicationException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace(); ignore this
				list=new ArrayList();
			}	
			}else{//
				list=new ArrayList();
				list.add(EnumListUploadActionType.ADD);
				list.add(EnumListUploadActionType.DELETE);
				list.add(EnumListUploadActionType.REPLACE);						       
			}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected void referenceData(ModelMap refData, String promotionCode) throws ServletException {			
		refData.put("actionTypes", getActionType(promotionCode));		
	}
	
	protected void onBind(Object command, String fileContentType) {
		RestrictionListUploadBean model = (RestrictionListUploadBean) command;
		if(fileContentType!=null){ 
			EnumListUploadActionType enmFileContentType =EnumListUploadActionType.getEnum(fileContentType);
			if(enmFileContentType!=null){
				model.setActionType(enmFileContentType);
			}
		}				
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
    protected String processSubmit(@ModelAttribute("command") Object command,
        BindingResult result, @RequestParam(value = "promotionCode", required = false) String promotionCode,
        @RequestParam(value = "actionType", required = false) String actionType,
        ModelMap model, HttpServletRequest request) throws Exception {
    	RestrictionListUploadBean bean=null;    	
    	try
    	{
    		onBind(command, actionType);
    		
    		restrictionListUploadValidator.validate(command, result);
    		
    		if (result.hasErrors()) {

    			model.addAllAttributes(result.getModel());
    			referenceData(model,promotionCode);
    			return "restListUploadform";
    		}
        
	         // cast the bean
	        bean = (RestrictionListUploadBean) command;
	       	        
	        byte[] file = bean.getBytes();	        
	        LOGGER.debug("file size"+file.length+"file name :"+bean.getName());        
	        Collection collection=marketAdminService.parseMktAdminFile(bean);	                                              	        
	        LOGGER.debug("file size"+file.length+"file name :"+bean.getName()+"colect"+collection);	        
    	}
    	catch(MktAdminApplicationException exception){        		    		
    		if(exception.getExceptionList()!=null){    		     
    		     Iterator iterator=exception.getExceptionList().iterator();
    		     while(iterator.hasNext()){
    		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	 
    		    	 result.rejectValue("file", e.getErrorCode(),e.getPlaceHolders()," lot of address are not proper");
    		     }
    		}else{
    			result.rejectValue("file", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}    		    		
    		setActionMessage(request,bean);
    		referenceData(model, promotionCode);
    		return "restListUploadform";
    	}    	
    	setActionMessage(request,bean);
    	
    	request.setAttribute("actionTypes", getActionType(promotionCode));
    	model.addAttribute("command",bean);
        return "restListUploadform";
    }
    
    private void setActionMessage(HttpServletRequest request,RestrictionListUploadBean bean){
    	if(EnumListUploadActionType.ADD.getName().equals(bean.getActionType().getName()))
   	     	request.setAttribute("success","success.listaddition");
    	else if(EnumListUploadActionType.DELETE.getName().equals(bean.getActionType().getName()))
   		 	request.setAttribute("success","success.listdelete");
    	else if(EnumListUploadActionType.REPLACE.getName().equals(bean.getActionType().getName()))
    		request.setAttribute("success","success.listreplace");
    	else
    		request.setAttribute("success","success.listcreate");
    }

    
    @InitBinder
    protected void initBinder(WebDataBinder binder)
        throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(EnumListUploadActionType.class, new EnumRestrictionListActionTypeSupport());		
        // now Spring knows how to handle multipart object and convert them
    }

	

}



