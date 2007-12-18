package com.freshdirect.mktAdmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Gopal
 */
public class MarketAdminController extends MultiActionController implements InitializingBean {
	

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


	public MarketAdminServiceIntf getMarketAdminService() {
		return marketAdminService;
	}			

	public void setMarketAdminService(MarketAdminServiceIntf marketAdminService) {
		this.marketAdminService = marketAdminService;
	}
	
	
	
	
}