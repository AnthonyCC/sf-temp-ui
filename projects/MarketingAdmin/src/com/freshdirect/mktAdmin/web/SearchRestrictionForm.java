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
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.RestrictionSearchBean;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */
public class SearchRestrictionForm extends AbstractMktAdminForm {

	private final static Category LOGGER = LoggerFactory.getInstance(SearchRestrictionForm.class);
	
	public SearchRestrictionForm() {
		// OK to start with a blank command object
		setCommandClass(RestrictionSearchBean.class);
		// activate session form mode to allow for detection of duplicate submissions
		//setSessionForm(true);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		// get the Pet referred to by id in the request
		
		String promotionCode=request.getParameter("promotionCode");
		LOGGER.debug("SearchRestrictionForm:formBackingObject:"+promotionCode);
		RestrictionSearchBean form=new RestrictionSearchBean();
		if(promotionCode!=null && promotionCode.trim().length()>0){
			try {	
			Collection promoModelList=getMarketAdminService().getPromotionModel(new String[]{promotionCode});
			Iterator iterator=promoModelList.iterator();
			if(iterator.hasNext()){
				form.setPromotion((FDPromotionModel)iterator.next());	
			}				
			//form.setSearchCount(100);
			String actionType=request.getParameter("action_type"); 
			if(actionType!=null && actionType.trim().length()>0 && actionType.equalsIgnoreCase("DELETE")){
				String customerId=request.getParameter("customerId");				
			    getMarketAdminService().removeRestrictedCustomers(promotionCode,customerId);	
			}

			form.setSerachKey("");
			form.setPromotionList(getMarketAdminService().getRestrictedCustomers(form));			
			} catch (MktAdminApplicationException e) {
				// TODO Auto-generated catch block
				throw new MktAdminSystemException("1001",e);
			}
			
		}
		else{
			throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));
		}
		return form;
	}
	
	public Object getDefaultBackingObject() {
		return new RestrictionSearchBean();
	}

	/*
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("competitorTypes", EnumCompetitorType.getEnumList());		
		return refData;
	}*/

	
	protected void onBind(HttpServletRequest request, Object command) {
		RestrictionSearchBean model = (RestrictionSearchBean) command;
		String promotionCode=request.getParameter("promotionCode");
		//System.out.println(" priviledgeType :"+priviledgeType);
		try{
		if(promotionCode!=null){		
			Collection promoModelList=getMarketAdminService().getPromotionModel(new String[]{promotionCode});			
			Iterator iterator=promoModelList.iterator();
			if(iterator.hasNext()){
				model.setPromotion((FDPromotionModel)iterator.next());	
			}
		}	
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		}	
	}
		
	
	/** Method inserts a new <code>User</code>. */
	protected ModelAndView onSubmit(  HttpServletRequest request,
	        HttpServletResponse response,
	        Object command,
	        BindException errors) throws Exception{
		Collection restCustomerList=null; 
		RestrictionSearchBean model = (RestrictionSearchBean) command;				
		
		try{
			restCustomerList=getMarketAdminService().getRestrictedCustomers(model);
			model.setPromotionList(restCustomerList);
		} catch (MktAdminApplicationException exception) {
			// TODO Auto-generated catch block			    		
    		return showForm(request,response,errors);
		}		
		//request.setAttribute("promotionList",restCustomerList);
		return new ModelAndView(getFormView(), "command", model);
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}

}