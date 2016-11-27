package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.validation.CompetitorFormValidator;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */

@Controller
@RequestMapping("/editCompetitor.do")
@SessionAttributes("command")
public class EditCompInfoForm extends AbstractMktAdminForm {
	
	@Autowired
	private CompetitorFormValidator competitorFormValidator;	

	private final static Category LOGGER = LoggerFactory.getInstance(EditCompInfoForm.class);
	

	
	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model, 
			@RequestParam(value = "competitorId", required = false) String competitorId) throws ServletException {
		// get the Pet referred to by id in the request
		try {		
		LOGGER.debug("EditCompInfoForm:formBackingObject:"+competitorId);
			model.addAttribute("command", marketAdminService.getCompetitorInformation(competitorId));
			referenceData(model);
			return "editCompetitorForm";
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		}
	}
	
	protected void referenceData(ModelMap refData) throws ServletException {				
		refData.put("competitorTypes", EnumCompetitorType.getEnumList());		
	}
	
	protected void onBind(CompetitorAddressModel command, String priviledgeType) {		
		//System.out.println(" priviledgeType :"+priviledgeType);
		if(priviledgeType!=null){ 
			EnumCompetitorType enmCmpType =EnumCompetitorType.getEnum(priviledgeType);
			if(enmCmpType!=null){
				command.setCompetitorType(enmCmpType);
			}
		}				
	}
		
	
	/** Method inserts a new <code>User</code>. */
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("command") CompetitorAddressModel command,
	        BindingResult result, @RequestParam(value = "competitorId", required = false) String competitorId,
	        @RequestParam(value = "competitorType", required = false) String priviledgeType,
	        ModelMap model) throws Exception{
		
		onBind(command, priviledgeType);
		
		competitorFormValidator.validate(command, result);
		
		if(result.hasErrors()) {
			referenceData(model);
			return "editCompetitorForm";
		}
		
		// delegate the insert to the Business layer
		List modelList=new ArrayList();
		modelList.add(command);
		try{
			marketAdminService.storeCompetitorInformation(modelList);
		} catch (MktAdminApplicationException exception) {
			// TODO Auto-generated catch block
			if(exception.getExceptionList()!=null){    		     
   		     Iterator iterator=exception.getExceptionList().iterator();
   		     while(iterator.hasNext()){
   		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	     		    	     		    	 
   		    	 result.rejectValue("address1", "app.error.110",e.getPlaceHolders()," lot of address are not proper");
   		     }
   		}else{
   			result.rejectValue("address1", exception.getErrorCode(),
                   exception.getPlaceHolders(), "General application error");
   		}
    		referenceData(model);
    		return "editCompetitorForm";
		}
		model.addAttribute("roleId", command.getId());
		return "competitorRedirect";
	}

//	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return disallowDuplicateFormSubmission(request, response);
//	}

}