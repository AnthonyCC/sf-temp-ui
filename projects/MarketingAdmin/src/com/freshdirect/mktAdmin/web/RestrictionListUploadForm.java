package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.freshdirect.fdstore.promotion.FDPromotionModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;

public class RestrictionListUploadForm extends AbstractMktAdminForm {

	private MarketAdminServiceIntf marketAdminService=null;
	
	private final static Category LOGGER = LoggerFactory.getInstance(RestrictionListUploadForm.class);
	
	public RestrictionListUploadForm()
	{
		setCommandClass(RestrictionListUploadBean.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);

	}
	
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		// get the Pet referred to by id in the request		
			//request.setAttribute("actionTypes", EnumListUploadActionType.getEnumList());
		    RestrictionListUploadBean command=new RestrictionListUploadBean();
			String promotionCode=request.getParameter("promotionCode");
			System.out.println("INSIDE1 formBackingObject1 :"+promotionCode);
			if(promotionCode==null) throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));															
			command.setPromotionCode(promotionCode);
			return command;			
	}
	
	
	private List getActionType(HttpServletRequest request){
		String promotionCode=request.getParameter("promotionCode");
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
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();				
		refData.put("actionTypes", getActionType(request));		       		
		return refData;
	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		RestrictionListUploadBean model = (RestrictionListUploadBean) command;
		String fileContentType=request.getParameter("actionType");
		if(fileContentType!=null){ 
			EnumListUploadActionType enmFileContentType =EnumListUploadActionType.getEnum(fileContentType);
			if(enmFileContentType!=null){
				model.setActionType(enmFileContentType);
			}
		}				
	}
	
    protected ModelAndView onSubmit(
        HttpServletRequest request,
        HttpServletResponse response,
        Object command,
        BindException errors) throws Exception {
    	RestrictionListUploadBean bean=null;    	
    	try
    	{    		
	         // cast the bean
	        bean = (RestrictionListUploadBean) command;
	       	        
	        byte[] file = bean.getBytes();
	        String s=null;
	        LOGGER.debug("file size"+file.length+"file name :"+bean.getName());        
	        Collection collection=marketAdminService.parseMktAdminFile(bean);	                                              	        
	        LOGGER.debug("file size"+file.length+"file name :"+bean.getName()+"colect"+collection);	        
    	}
    	catch(MktAdminApplicationException exception){        		    		
    		if(exception.getExceptionList()!=null){    		     
    		     Iterator iterator=exception.getExceptionList().iterator();
    		     while(iterator.hasNext()){
    		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	 
    		    	 errors.rejectValue("file", e.getErrorCode(),e.getPlaceHolders()," lot of address are not proper");
    		     }
    		}else{
    		errors.rejectValue("file", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}    		    		
    		setActionMessage(request,bean);
    		return showForm(request,response,errors);
    	}    	
    	setActionMessage(request,bean);
    	
    	request.setAttribute("actionTypes", getActionType(request));
        return new ModelAndView(getSuccessView(),"command",bean);
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

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(EnumListUploadActionType.class, new EnumRestrictionListActionTypeSupport());		
        // now Spring knows how to handle multipart object and convert them
    }

	public MarketAdminServiceIntf getMarketAdminService() {
		return marketAdminService;
	}

	public void setMarketAdminService(MarketAdminServiceIntf marketAdminService) {
		this.marketAdminService = marketAdminService;
	}

}



