package com.freshdirect.mktAdmin.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;
import com.freshdirect.mktAdmin.util.CustomerModel;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Gopal
 */
public class MarketAdminController extends MultiActionController implements InitializingBean {
	
	private final static Category LOGGER = LoggerFactory.getInstance(MarketAdminController.class);
	

	private MarketAdminServiceIntf marketAdminService;
//
//	public void setOffAdminService(OFFAdminServiceIntf offAdminService) {
//		this.offAdminService = offAdminService;
//	}

	public void afterPropertiesSet() throws Exception {
//		if (offAdminService == null)
//			throw new ApplicationContextException("Must set clinic bean property on " + getClass());
	}

	// handlers

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView fileUploadHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {				
		request.setAttribute("fileContentTypes", EnumFileContentType.getEnumList());		
		return new ModelAndView("fileuploadform","command",new FileUploadBean());
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView restrictedListUploadHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {				
		request.setAttribute("actionTypes", EnumListUploadActionType.getEnumList());
		String promotionCode=request.getParameter("promotionCode");
		if(promotionCode==null) throw new MktAdminSystemException("1001",new IllegalArgumentException("promotionCode parameter is required"));
		RestrictionListUploadBean command=new RestrictionListUploadBean();
		command.setPromotionCode(promotionCode);
		return new ModelAndView("restListUploadform","command",command);
	}
		
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	private static final String FILE_NAME="RestrictedCustomer.csv";
	public ModelAndView restrictedDownloadHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			String promotionCode=request.getParameter("promotionCode");
			if(promotionCode==null) throw new MktAdminSystemException("1001",new IllegalArgumentException("promotionCode parameter is required"));
			String contents=this.marketAdminService.generateRestrictedCustomerFileContents(promotionCode,EnumFileType.CSV_FILE_TYPE);

			response.setBufferSize(contents.getBytes().length);
			response.setHeader("Content-Disposition", "attachment; filename=\""+FILE_NAME+"\"");
			response.setContentType("text/csv");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setContentLength(contents.getBytes().length);
			try {
				FileCopyUtils.copy(contents.getBytes(), response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block				
				throw new MktAdminSystemException("1001",e);
			}
			
			
			
		} catch (MktAdminApplicationException e) {
		  // TODO Auto-generated catch block
		  //e.printStackTrace(); this can happen only user enters some input else system exception
		  throw new MktAdminSystemException("1001",e);
		}

		return null;
	}

	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ModelAndView("welcomeView");
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView competitorViewHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// connect the service to get the competitor information
		Collection collection=null;
		try {
			collection=getMarketAdminService().getCompetitorInformation();
			logger.debug("Competitor View in the web :"+collection);
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); this can happen only user enters some input else system exception
			throw new MktAdminSystemException("1001",e);
		}
		return new ModelAndView("competitorView","competitors",collection);
	}

	public ModelAndView removeCompetitorHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// connect the service to get the competitor information
		List collection=null;
		try {
			String competitorId=(String)request.getParameter("competitorId");
			collection=new ArrayList();
			collection.add(competitorId);
			getMarketAdminService().removeCompetitorInformation(collection);
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); this can happen only user enters some input else system exception
			throw new MktAdminSystemException("1001",e);
		}
		request.setAttribute("success","success.listremove");
		return competitorViewHandler(request,response);
	}
	
	public ModelAndView removeRestrictedCustomerHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// connect the service to get the competitor information
		List collection=null;
		try {
			String promotionCode=(String)request.getParameter("promotionCode");
			getMarketAdminService().deleteRestrictionCustomer(promotionCode);
			request.setAttribute("success","success.listremove");
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); this can happen only user enters some input else system exception
			throw new MktAdminSystemException("1001",e);
		}
		return new ModelAndView("successView");
	}
	
	
	public ModelAndView promotionViewHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// connect the service to get the competitor information
		Collection collection=null;
		try {
			collection=getMarketAdminService().getAllPromotions();
			logger.debug("Competitor View in the web :"+collection.size());
		} catch (MktAdminApplicationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); this can happen only user enters some input else system exception
			throw new MktAdminSystemException("1001",e);
		}
		return new ModelAndView("promotionView","promotions",collection);
	}
	
	public ModelAndView upsOutageHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Collection<CustomerModel> customerList = null;
		String fromDate = request.getParameter("FromDate");
		String endDate = request.getParameter("ToDate");
		if("true".equals(request.getParameter("submission"))) {
			//process the user request
			request.getSession().removeAttribute("error");
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy hh:mm aaa");
		    java.util.Date convertedFromDate = null;
		    java.util.Date convertedToDate = null;
		    try {
				convertedFromDate = dateFormat.parse(fromDate);
				convertedToDate = dateFormat.parse(endDate);
			} catch (ParseException e) {
				request.getSession().setAttribute("error", "Enter Date in MM/DD/YYYY HH12:MM AM/PM format. Ex: 06/15/2011 05:30 PM");
				LOGGER.error("Date parse exception.", e);
			}
			if(convertedToDate != null && convertedFromDate != null) {
			    if(convertedToDate.before(convertedFromDate)) {
			    	LOGGER.debug("Wrong dates");
			    	request.getSession().setAttribute("error", "Start Date should be before End Date.");
			    } else {
			    	try {
						customerList = getMarketAdminService().getUpsOutageList(fromDate, endDate);
					} catch (MktAdminApplicationException e) {
						request.getSession().setAttribute("error", "Problem getting customer List. Check your dates and try again. If problem persists contact Appsupport.");
						LOGGER.error("Exception getting customer list", e);
					}
			    }	
			}
		} else if("xls".equals(request.getParameter("action"))) {
			System.out.println("FromDate: "+fromDate + "----toDate:" + endDate);
			if(fromDate != null && endDate != null) {
				try {
					customerList = getMarketAdminService().getUpsOutageList(fromDate, endDate);
				} catch (MktAdminApplicationException e) {
					request.getSession().setAttribute("error", "Problem getting customer List. Check your dates and try again. If problem persists contact Appsupport.");
					LOGGER.error("Exception getting customer list", e);
				}
			}
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition", "attachment; filename=UpsOutageCustList_Export.xls");
		    response.setCharacterEncoding("utf-8");		    
		} 
		if(customerList != null)
			request.getSession().setAttribute("customers", customerList);
		return new ModelAndView("upsView","customers",customerList);
	}


	public MarketAdminServiceIntf getMarketAdminService() {
		return marketAdminService;
	}			

	public void setMarketAdminService(MarketAdminServiceIntf marketAdminService) {
		this.marketAdminService = marketAdminService;
	}
	
	
	
	
}