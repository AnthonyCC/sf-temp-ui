package com.freshdirect.mktAdmin.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionListAppendBean;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;

public class RestrictionAppendForm extends SimpleFormController {

	private MarketAdminServiceIntf marketAdminService=null;
	
	private final static Category LOGGER = LoggerFactory.getInstance(RestrictionAppendForm.class);
	
	public RestrictionAppendForm()
	{
		setCommandClass(RestrictionListAppendBean.class);
		// activate session form mode to allow for detection of duplicate submissions
		//setSessionForm(true);

	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		// get the Pet referred to by id in the request		
			//request.setAttribute("actionTypes", EnumListUploadActionType.getEnumList());
			RestrictionListAppendBean command=new RestrictionListAppendBean();
			String promotionCode=request.getParameter("promotionCode");
			System.out.println("INSIDE1 formBackingObject1 :"+promotionCode);
			if(promotionCode==null) throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));
			//if(promotionCode!=null)			     
			command.setPromotionCode(promotionCode);
			return command;			
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
	
    protected ModelAndView onSubmit(
        HttpServletRequest request,
        HttpServletResponse response,
        Object command,
        BindException errors) throws Exception {
    	RestrictionListAppendBean bean=null;    	
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
    		    	 errors.rejectValue("customerIds", e.getErrorCode(),e.getPlaceHolders()," lot of customer Ids are not proper");
    		     }
    		}else{
    		errors.rejectValue("customerIds", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}
    		return showForm(request,response,errors);
    	}
    	
    	request.setAttribute("success","success.listappend");
        return new ModelAndView(getSuccessView(),"command",bean);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(EnumFileContentType.class, new EnumFileContentTypeSupport());		
        // now Spring knows how to handle multipart object and convert them
    }

	public MarketAdminServiceIntf getMarketAdminService() {
		return marketAdminService;
	}

	public void setMarketAdminService(MarketAdminServiceIntf marketAdminService) {
		this.marketAdminService = marketAdminService;
	}

}



