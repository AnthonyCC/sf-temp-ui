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
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */
public class AddCompInfoForm extends AbstractMktAdminForm {

	private final static Category LOGGER = LoggerFactory.getInstance(AddCompInfoForm.class);
	
	public AddCompInfoForm() {
		// OK to start with a blank command object
		setCommandClass(CompetitorAddressModel.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
	}
		
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("competitorTypes", EnumCompetitorType.getEnumList());		
		return refData;
	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		CompetitorAddressModel model = (CompetitorAddressModel) command;
		String priviledgeType=request.getParameter("competitorType");
		//System.out.println(" priviledgeType :"+priviledgeType);
		if(priviledgeType!=null){ 
			EnumCompetitorType enmCmpType =EnumCompetitorType.getEnum(priviledgeType);
			if(enmCmpType!=null){
				model.setCompetitorType(enmCmpType);
			}
		}				
	}
		
	

	/** Method inserts a new <code>User</code>. */
	protected ModelAndView onSubmit(  HttpServletRequest request,
	        HttpServletResponse response,
	        Object command,
	        BindException errors) throws Exception{
			CompetitorAddressModel model = (CompetitorAddressModel) command;
			// delegate the insert to the Business layer
			List modelList=new ArrayList();
			modelList.add(model);
		try{
			getMarketAdminService().addCompetitorInformation(modelList);
		}     	catch(MktAdminApplicationException exception){        		
    		LOGGER.debug("inside MktAdminApplicationException :"+exception.getExceptionList());    		
    		if(exception.getExceptionList()!=null){    		     
    		     Iterator iterator=exception.getExceptionList().iterator();
    		     while(iterator.hasNext()){
    		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	     		    	     		    	 
    		    	 errors.rejectValue("address1", e.getErrorCode(),e.getPlaceHolders()," lot of address are not proper");
    		     }
    		}else{
    		errors.rejectValue("address1", exception.getErrorCode(),
                    exception.getPlaceHolders(), "General application error");
    		}
    		return showForm(request,response,errors);
    	}
		
		return new ModelAndView(getSuccessView());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}