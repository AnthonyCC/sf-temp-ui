package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;
import com.freshdirect.mktAdmin.util.FileParser;
import com.freshdirect.mktAdmin.util.FileParserFactory;
import com.freshdirect.mktAdmin.validation.ReferralFormValidator;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */

@Controller
@RequestMapping({"/createRefPromo.do","/viewRefPromo.do","/editRefPromo.do","/viewCustList.do"})
@SessionAttributes("command")
public class ReferralAdminController extends AbstractMktAdminForm {
	
	
	@Autowired
	private MarketAdminServiceIntf marketAdminService;	
	@Autowired
	private ReferralFormValidator referralFormValidator;




	private final static Category LOGGER = LoggerFactory.getInstance(ReferralAdminController.class);
		
	
	@SuppressWarnings("unchecked")
	protected void referenceData(ModelMap refData, 
			String ref_id, HttpServletRequest request) throws ServletException {
		//Map refData = new HashMap();
		request.getSession().removeAttribute("success");
		if(request.getRequestURI().indexOf("viewRefPromo.do") != -1) {
			//This is a view page, return the list of all promotions
			try {
				refData.put("promotions", marketAdminService.getAllRefPromotions());
			} catch (MktAdminApplicationException e) {
				LOGGER.error("Error loading promotions", e);
			}		 
		} else if(request.getRequestURI().indexOf("viewCustList.do") != -1) {
			//This is a view for cusotmer list
			try {
				refData.put("custList", marketAdminService.getRefPromoUserList(ref_id));
			} catch (MktAdminApplicationException e) {
				LOGGER.error("Error loading promotions", e);
			}	
		} else {
			try {
				System.out.println(marketAdminService.getReferralPromotions());
				refData.put("promotions", marketAdminService.getReferralPromotions());
				if(request.getRequestURI().indexOf("editRefPromo.do") != -1) {					
					refData.put("refPromo", marketAdminService.getRefPromotionInfo(ref_id));
				}
			} catch (MktAdminApplicationException e) {
				LOGGER.error("Error loading promotions", e);
			}
		}		
	}
	
	
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model,
			@RequestParam(value = "ref_id", required = false) String ref_id, 
			HttpServletRequest request)
			throws Exception {

		if (ref_id != null && ref_id.length() > 0) {
			ReferralAdminModel rAdm = marketAdminService.getRefPromotionInfo(ref_id);
			if (rAdm != null) {
				model.addAttribute("command", rAdm);
				referenceData(model, ref_id, request);
				return getFormView(request);
			}		
		}
		referenceData(model, ref_id, request);
		model.addAttribute("command", new ReferralAdminModel());
		return getFormView(request);		
	}
	
	private String getFormView(HttpServletRequest request) {
		
		if(request.getRequestURI().contains("viewRefPromo.do")) {
			return "viewRefPromoForm";
		} else if(request.getRequestURI().contains("createRefPromo.do")) {
			return "addRefPromoForm";
		} else if(request.getRequestURI().contains("editRefPromo.do")) {
			return "editRefPromoForm";
		} else if(request.getRequestURI().contains("viewCustList.do")) {
			return "viewCustListForm";
		}
	
		return null;
	}
	
	private String getSuccessView(HttpServletRequest request) {

		if (request.getRequestURI().contains("viewRefPromo.do")) {
			return "viewRefRedirect";
		} else if (request.getRequestURI().contains("createRefPromo.do")) {
			return "addRefRedirect";
		} else if (request.getRequestURI().contains("editRefPromo.do")) {
			return "editRefRedirect";
		} else if (request.getRequestURI().contains("viewCustList.do")) {
			return "viewCustListRedirect";
		}

		return null;
	}

	/** Method inserts a new <code>User</code>. */
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("command") ReferralAdminModel command,
	        BindingResult result, @RequestParam(value = "action", required = false) String action,
	        @RequestParam(value = "ref_id", required = false) String ref_id,
	        HttpServletRequest request,
	        ModelMap model) throws Exception{		
		
		request.getSession().removeAttribute("showApprove");
		request.getSession().removeAttribute("success");
		
		if(request.getRequestURI().contains("createRefPromo.do") || 
				request.getRequestURI().contains("editRefPromo.do")) {
			referralFormValidator.validate(command, result);
			if (result.hasErrors()) {

				model.addAllAttributes(result.getModel());
				referenceData(model, ref_id, request);
				return getFormView(request);
			}
		}		
    
		
		
		String username = request.getRemoteUser();
		
		String modify = request.getParameter("modifybutton");
		if("Modify".equals(modify)){
			referenceData(model, ref_id, request);
			model.addAttribute("command", command);
			return getFormView(request);
		}

		if("deleteRefs".equals(action)) {
			//delete the selected promotions
			List<String> deleteIds = new ArrayList<String>();
			Enumeration params = request.getParameterNames();
			while(params.hasMoreElements()) {
				String name = (String) params.nextElement();
				if(name.startsWith("delete_")) {
					deleteIds.add(request.getParameter(name));
				}
			}
			marketAdminService.deleteRefPromos(deleteIds, username);
			request.getSession().setAttribute("success", "true");
			model.addAttribute("command", new ReferralAdminModel());
			model.addAttribute("promotions", marketAdminService.getAllRefPromotions());
			return getSuccessView(request);
		} else {			
			String referral_id = command.getReferralId();
			
			if(command.getDefaultPromo()) {
				//Check if an active default promo already exists
				if(marketAdminService.defaultPromoExists(referral_id)) {
					//display error message
					result.rejectValue("defaultPromo","defaultPromo","An active default promo already exists. Expire the existing promo to create a new default promo.");
					referenceData(model, ref_id, request);
					model.addAttribute("command", command);
					return getFormView(request);
				}
			}
			
			if("editRef".equals(request.getParameter("action"))) {
				//edit the promotion
				command.setAddByUser(username);
				marketAdminService.editRefPromo(command);
				request.getSession().setAttribute("success", "true");
			} else if ("addRef".equals(request.getParameter("action"))) {
				//insert the promotion
				command.setAddByUser(username);
				referral_id = marketAdminService.createRefPromo(command);
				request.getSession().setAttribute("success", "true");
			} else {
				//send it to approve
				if(!command.getDefaultPromo()) {
					Collection<String> collection=new ArrayList<String>();
					if(model.isEmpty()) {
						result.rejectValue("userListFile","fields.userListFile","Customer List file is required.");
						referenceData(model, ref_id, request);
						model.addAttribute("command", command);
						return getFormView(request);
					}
					FileParser parser=FileParserFactory.getFileParser(command.getFileType());
					collection = parser.parseRefFile(command);
					command.setUserCollection(collection);
				}
				
				request.getSession().setAttribute("showApprove", "true");
				request.getSession().setAttribute("model", command);
				return getFormView(request);
			}
			
			if(!command.getDefaultPromo() && ("editRef".equals(action) || "addRef".equals(action))) {
				//Collection<String> collection=new ArrayList<String>();
				//FileParser parser=FileParserFactory.getFileParser(model.getFileType());
				//collection = parser.parseRefFile(model);
				
				List invalidCustomers = marketAdminService.addReferralCustomers(command.getUserCollection(), referral_id);
				request.getSession().setAttribute("invalidusers", invalidCustomers);
			}			
						
			request.getSession().setAttribute("model", command);
		}
		model.addAttribute("command", command);
		return getSuccessView(request);
	}

//	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		return disallowDuplicateFormSubmission(request, response);
//	}
	
	

}