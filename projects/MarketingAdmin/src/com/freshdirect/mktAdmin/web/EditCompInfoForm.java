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
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */
public class EditCompInfoForm extends AbstractMktAdminForm {

	private final static Category LOGGER = LoggerFactory.getInstance(EditCompInfoForm.class);
	
	public EditCompInfoForm() {
		// OK to start with a blank command object
		setCommandClass(CompetitorAddressModel.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		// get the Pet referred to by id in the request
		try {
		String competitorId=request.getParameter("competitorId");
		LOGGER.debug("EditCompInfoForm:formBackingObject:"+competitorId);		
			return getMarketAdminService().getCompetitorInformation(competitorId);
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		}
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
			getMarketAdminService().storeCompetitorInformation(modelList);
		} catch (MktAdminApplicationException exception) {
			// TODO Auto-generated catch block
			if(exception.getExceptionList()!=null){    		     
   		     Iterator iterator=exception.getExceptionList().iterator();
   		     while(iterator.hasNext()){
   		    	 MktAdminApplicationException e=(MktAdminApplicationException)iterator.next();    		    	     		    	     		    	 
   		    	 errors.rejectValue("address1", "app.error.110",e.getPlaceHolders()," lot of address are not proper");
   		     }
   		}else{
   			errors.rejectValue("address1", exception.getErrorCode(),
                   exception.getPlaceHolders(), "General application error");
   		}
    		
    		return showForm(request,response,errors);
		}		
		return new ModelAndView(getSuccessView(), "roleId", model.getId());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}