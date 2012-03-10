package com.freshdirect.mktAdmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
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
import com.freshdirect.mktAdmin.dao.oracle.OracleMarketAdminDAOImpl;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;
import com.freshdirect.mktAdmin.util.FileParser;
import com.freshdirect.mktAdmin.util.FileParserFactory;


/**
 * JavaBean form controller that is used to add a new <code>Owner</code> to the system.
 *
 * @author Gopal
 */
public class ReferralAdminController extends AbstractMktAdminForm {

	private final static Category LOGGER = LoggerFactory.getInstance(ReferralAdminController.class);
	
	public ReferralAdminController() {
		// OK to start with a blank command object
		setCommandClass(ReferralAdminModel.class);
		// activate session form mode to allow for detection of duplicate submissions
		setSessionForm(true);
		// initialize the form from the formBackingObject
	    setBindOnNewForm(true);

	}
		
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		request.getSession().removeAttribute("success");
		if(request.getRequestURI().indexOf("viewRefPromo.do") != -1) {
			//This is a view page, return the list of all promotions
			try {
				refData.put("promotions", getMarketAdminService().getAllRefPromotions());
			} catch (MktAdminApplicationException e) {
				LOGGER.error("Error loading promotions", e);
			}		 
		} else {
			try {
				System.out.println(getMarketAdminService().getReferralPromotions());
				refData.put("promotions", getMarketAdminService().getReferralPromotions());
				if(request.getRequestURI().indexOf("editRefPromo.do") != -1) {
					String ref_id = request.getParameter("ref_id");
					refData.put("refPromo", getMarketAdminService().getRefPromotionInfo(ref_id));
				}
			} catch (MktAdminApplicationException e) {
				LOGGER.error("Error loading promotions", e);
			}
		}
		return refData;
	}
	

	/** Method inserts a new <code>User</code>. */
	protected ModelAndView onSubmit(  HttpServletRequest request,
	        HttpServletResponse response,
	        Object command,
	        BindException errors) throws Exception{
		ModelAndView modelAndView = super.onSubmit(request, response, command, errors);
		
		request.getSession().removeAttribute("showApprove");
		request.getSession().removeAttribute("success");
		
		String username = request.getRemoteUser();
		
		String modify = request.getParameter("modifybutton");
		if("Modify".equals(modify)){
			return showForm(request,response,errors);
		}

		if("deleteRefs".equals(request.getParameter("action"))) {
			//delete the selected promotions
			List<String> deleteIds = new ArrayList<String>();
			Enumeration params = request.getParameterNames();
			while(params.hasMoreElements()) {
				String name = (String) params.nextElement();
				if(name.startsWith("delete_")) {
					deleteIds.add(request.getParameter(name));
				}
			}
			getMarketAdminService().deleteRefPromos(deleteIds, username);
			request.getSession().setAttribute("success", "true");
			modelAndView.addObject("command", new ReferralAdminModel());
			modelAndView.addObject("promotions", getMarketAdminService().getAllRefPromotions());
			return modelAndView;
		} else {			
			ReferralAdminModel model = (ReferralAdminModel) command;
			String referral_id = model.getReferralId();
			
			if(model.getDefaultPromo()) {
				//Check if an active default promo already exists
				if(getMarketAdminService().defaultPromoExists(referral_id)) {
					//display error message
					errors.rejectValue("defaultPromo","defaultPromo","An active default promo already exists. Expire the existing promo to create a new default promo.");
					return showForm(request,response,errors);
				}
			}
			
			if("editRef".equals(request.getParameter("action"))) {
				//edit the promotion
				model.setAddByUser(username);
				getMarketAdminService().editRefPromo(model);
				request.getSession().setAttribute("success", "true");
			} else if ("addRef".equals(request.getParameter("action"))) {
				//insert the promotion
				model.setAddByUser(username);
				referral_id = getMarketAdminService().createRefPromo(model);
				request.getSession().setAttribute("success", "true");
			} else {
				//send it to approve
				if(!model.getDefaultPromo()) {
					Collection<String> collection=new ArrayList<String>();
					if(model.isEmpty()) {
						errors.rejectValue("userListFile","fields.userListFile","Customer List file is required.");
						return showForm(request,response,errors);
					}
					FileParser parser=FileParserFactory.getFileParser(model.getFileType());
					collection = parser.parseRefFile(model);
					model.setUserCollection(collection);
				}
				
				request.getSession().setAttribute("showApprove", "true");
				request.getSession().setAttribute("model", model);
				return showForm(request,response,errors);
			}
			
			if(!model.getDefaultPromo() && ("editRef".equals(request.getParameter("action")) || "addRef".equals(request.getParameter("action")))) {
				//Collection<String> collection=new ArrayList<String>();
				//FileParser parser=FileParserFactory.getFileParser(model.getFileType());
				//collection = parser.parseRefFile(model);
				
				List invalidCustomers = getMarketAdminService().addReferralCustomers(model.getUserCollection(), referral_id);
				request.getSession().setAttribute("invalidusers", invalidCustomers);
			}			
						
			request.getSession().setAttribute("model", model);
		}
		return new ModelAndView(getSuccessView());
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return disallowDuplicateFormSubmission(request, response);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String ref_id = request.getParameter("ref_id");
		if (ref_id != null && ref_id.length() > 0) {
			ReferralAdminModel rAdm = getMarketAdminService().getRefPromotionInfo(ref_id);
			if (rAdm != null) {
				return rAdm;
			}
		}
		System.out.println("Setting formBackingObject");
		return new ReferralAdminModel();
	}

}