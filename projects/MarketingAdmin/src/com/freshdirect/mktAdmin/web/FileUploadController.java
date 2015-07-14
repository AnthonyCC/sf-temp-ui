package com.freshdirect.mktAdmin.web;

import java.util.Collection;
import java.util.Iterator;

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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;
import com.freshdirect.mktAdmin.validation.FileUploadValidator;





@Controller
@RequestMapping("/upload.do")
public class FileUploadController {

	
	
	private final static Category LOGGER = LoggerFactory.getInstance(FileUploadController.class);
	
	@Autowired
	private MarketAdminServiceIntf marketAdminService;		
	@Autowired
	private FileUploadValidator fileUploadValidator;
	
	
	
	protected void referenceData(ModelMap refData) throws ServletException {			
		refData.addAttribute("fileContentTypes", EnumFileContentType.getEnumList());		
	}
	
	protected void onBind(String fileContentType, FileUploadBean command) {		
	  if(fileContentType!=null){ 
			EnumFileContentType enmFileContentType =EnumFileContentType.getEnum(fileContentType);
			if(enmFileContentType!=null){
				command.setFileContentType(enmFileContentType);
			}
		}				
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model) throws ServletException {
		
		model.addAttribute("command", new FileUploadBean());
		referenceData(model);
		return "fileuploadform";
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("command") FileUploadBean fileUploadBean,
			BindingResult result,
			@RequestParam(value = "fileContentType", required = false) String fileContentType,
			ModelMap model, HttpServletRequest request) throws ServletException {
		
		onBind(fileContentType, fileUploadBean);
		
		fileUploadValidator.validate(fileUploadBean, result);
		
		if (result.hasErrors()) {

			model.addAllAttributes(result.getModel());
			referenceData(model);
			return "fileuploadform";
		}
    
    	try
    	{    		
	             		
   	        
	        byte[] file = fileUploadBean.getBytes();
	        String s=null;
	        LOGGER.debug("file size"+file.length+"file name :"+fileUploadBean.getName());        
	        Collection collection=marketAdminService.parseMktAdminFile(fileUploadBean);	                                              	        
	        LOGGER.debug("file size"+file.length+"file name :"+fileUploadBean.getName()+"colect"+collection);	                                              	        
	               
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
    		model.addAttribute("command", fileUploadBean);
    		referenceData(model);
    		return "fileuploadform";
    	}
    
     request.setAttribute("success","success.fileupload");
     model.addAttribute("command", fileUploadBean);
     return "uploadRedirect";
    }

	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {		
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(EnumFileContentType.class, new EnumFileContentTypeSupport());		
        // now Spring knows how to handle multipart object and convert them
    }

	

}



