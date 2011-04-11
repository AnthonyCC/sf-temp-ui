package com.freshdirect.transadmin.web;

import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Sivachandar
 */
public class AssetController extends AbstractMultiActionController {


	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
	/**
	 * Custom handler for Asset
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView assetHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String assetType = request.getParameter("pAssetType");
		
		if(TransStringUtil.isEmpty(assetType)) {			
        	assetType = "GPS";
		}
		
		ModelAndView mav = new ModelAndView("assetView");
		mav.getModel().put("assetTypes",getAssetManagerService().getAssetTypes());
        mav.getModel().put("pAssetType",assetType);
        mav.getModel().put("assets", getAssetManagerService().getAssets(assetType));
        
		return mav;
	}


	/**
	 * Custom handler for List of Asset Template
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView assetTemplateHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String assetType = request.getParameter("tAssetType");
		
		if(TransStringUtil.isEmpty(assetType)) {			
			assetType = "GPS";
		}
		
		ModelAndView mav = new ModelAndView("assetTemplateView");
		mav.getModel().put("assetTypes",getAssetManagerService().getAssetTypes());
        mav.getModel().put("assetTemplates", getAssetManagerService().getAssetTemplates(assetType));
        
		return mav;
	}

}
