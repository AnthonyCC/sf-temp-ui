package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.validation.CompetitorFormValidator;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */


@Controller
@RequestMapping("/addCompetitor.do")
@SessionAttributes("command")
public class AddCompInfoForm extends AbstractMktAdminForm {

	private final static Category LOGGER = LoggerFactory.getInstance(AddCompInfoForm.class);
	
	@Autowired
	private CompetitorFormValidator competitorFormValidator;

		
	
	protected void referenceData(ModelMap refData) throws ServletException {				
		refData.put("competitorTypes", EnumCompetitorType.getEnumList());		
	}
	
	protected void onBind(CompetitorAddressModel command, String priviledgeType) {
		CompetitorAddressModel model = (CompetitorAddressModel) command;				
		if(priviledgeType!=null){ 
			EnumCompetitorType enmCmpType = EnumCompetitorType.getEnum(priviledgeType);
			if(enmCmpType!=null){
				model.setCompetitorType(enmCmpType);
			}
		}				
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model) throws ServletException {
		
		model.addAttribute("command", new CompetitorAddressModel());
		referenceData(model);
		return "addCompetitorForm";
	}
		
	

	/** Method inserts a new <code>User</code>. */
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("command") CompetitorAddressModel command, BindingResult result,
			@RequestParam(value = "competitorType", required = false) String priviledgeType,
	        ModelMap model) throws Exception{
		
		
		onBind(command, priviledgeType);
		
		competitorFormValidator.validate(command, result);
		
		if(result.hasErrors()) {
			model.addAttribute("command", command);
    		referenceData(model);
    		return "addCompetitorForm";
		}
		
		// delegate the insert to the Business layer
		List modelList=new ArrayList();
		modelList.add(command);
		try{
			marketAdminService.addCompetitorInformation(modelList);
		}     	catch(MktAdminApplicationException exception){        		
    		LOGGER.debug("inside MktAdminApplicationException :"+exception.getExceptionList());    		
    		if(exception.getExceptionList()!=null){    		     
    		     Iterator iterator=exception.getExceptionList().iterator();
    		     while(iterator.hasNext()){
    		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	     		    	     		    	 
    		    	 result.rejectValue("address1", e.getErrorCode(),e.getPlaceHolders()," lot of address are not proper");
    		     }
    		}else{
    		result.rejectValue("address1", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}
    		model.addAttribute("command", command);
    		referenceData(model);
    		return "addCompetitorForm";
    	}
		
		
		
		return "competitorRedirect";
	}

//	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return disallowDuplicateFormSubmission(request, response);
//	}

}