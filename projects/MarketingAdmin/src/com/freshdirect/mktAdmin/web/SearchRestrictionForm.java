package com.freshdirect.mktAdmin.web;

import java.util.Collection;
import java.util.Iterator;

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

import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.RestrictionSearchBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 * 
 * 
 */


@Controller
@RequestMapping("/searchRestriction.do")
public class SearchRestrictionForm extends AbstractMktAdminForm {
	
	@Autowired
	private MarketAdminServiceIntf marketAdminService;


	private final static Category LOGGER = LoggerFactory.getInstance(SearchRestrictionForm.class);

	@RequestMapping(method = RequestMethod.GET)
	protected String initForm(ModelMap model,
			@RequestParam(value = "promotionCode", required = false) String promotionCode,
			@RequestParam(value = "action_type", required = false) String actionType,
			@RequestParam(value = "customerId", required = false) String customerId,
			@RequestParam(value = "email", required = false) String email) throws ServletException {
		// get the Pet referred to by id in the request
				
		LOGGER.debug("SearchRestrictionForm:initForm:"+promotionCode);
		RestrictionSearchBean form=new RestrictionSearchBean();
		if(promotionCode!=null && promotionCode.trim().length()>0){
			try {	
			Collection promoModelList=marketAdminService.getPromotionModel(new String[]{promotionCode});
			Iterator iterator=promoModelList.iterator();
			if(iterator.hasNext()){
				form.setPromotion((FDPromotionNewModel)iterator.next());	
			}				
			//form.setSearchCount(100);			 
			if(actionType!=null && actionType.trim().length()>0 && actionType.equalsIgnoreCase("DELETE")){				
			    marketAdminService.removeRestrictedCustomers(promotionCode,customerId, email);	
			}

			form.setSerachKey("");
			form.setPromotionList(marketAdminService.getRestrictedCustomers(form));			
			} catch (MktAdminApplicationException e) {
				// TODO Auto-generated catch block
				throw new MktAdminSystemException("1001",e);
			}
			
			model.addAttribute("command", form);			
		}
		else{
			throw new MktAdminSystemException("1003",new IllegalArgumentException("promotionCode parameter is required"));
		}
		return "searchRestrictionForm";
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

	
	protected void onBind(Object command, String promotionCode) {
		RestrictionSearchBean model = (RestrictionSearchBean) command;		
		try{
		if(promotionCode!=null){		
			Collection promoModelList=marketAdminService.getPromotionModel(new String[]{promotionCode});			
			Iterator iterator=promoModelList.iterator();
			if(iterator.hasNext()){
				model.setPromotion((FDPromotionNewModel)iterator.next());	
			}
		}	
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		}	
	}
		
	
	/** Method inserts a new <code>User</code>. */
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("command") RestrictionSearchBean command,
			BindingResult result, @RequestParam(value = "promotionCode", required = false) String promotionCode,
			@RequestParam(value = "action_type", required = false) String actionType,
			@RequestParam(value = "customerId", required = false) String customerId,
			@RequestParam(value = "email", required = false) String email,
			ModelMap model) throws Exception{
		
		
		onBind(command, promotionCode);
		Collection restCustomerList=null;						
		
		try{
			restCustomerList=marketAdminService.getRestrictedCustomers(command);
			command.setPromotionList(restCustomerList);
		} catch (MktAdminApplicationException exception) {
			// TODO Auto-generated catch block			    		
    		return "searchRestrictionForm";
		}		
		//request.setAttribute("promotionList",restCustomerList);
		model.addAttribute("command", command);
		return "searchRestrictionForm";
	}

//	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return disallowDuplicateFormSubmission(request, response);
//	}

}