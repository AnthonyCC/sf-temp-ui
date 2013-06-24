package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.constants.EnumAssetScanStatus;
import com.freshdirect.transadmin.datamanager.report.XlsAssetReport;
import com.freshdirect.transadmin.model.Asset;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelAndView assetHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		String assetType = request.getParameter("pAssetType");
		String atrType = request.getParameter("atrName");
		String atrValue = request.getParameter("atrValue");
		if(TransStringUtil.isEmpty(assetType)) {			
        	assetType = "TRUCK";
		}
		
		Collection assets = getAssetManagerService().getAssets(assetType, atrType, atrValue);
		
		if("y".equalsIgnoreCase(request.getParameter("export"))&&!TransStringUtil.isEmpty(request.getParameter("assetType"))){				
			exportAssets(request, response, assets);
			return null;
		}
		
		ModelAndView mav = new ModelAndView("assetView");
		mav.getModel().put("assetTypes",getAssetManagerService().getAssetTypes());
        mav.getModel().put("pAssetType",assetType);
        mav.getModel().put("assets", assets);
        mav.getModel().put("statuses", EnumAssetScanStatus.getEnumList());
        
		return mav;
	}


	@SuppressWarnings("rawtypes")
	private void exportAssets(HttpServletRequest request,
			HttpServletResponse response, Collection assets) {
		
		String reportFileName = "trn_assetreport_" + com.freshdirect.transadmin.security.SecurityManager.getUserName(request) + System.currentTimeMillis()+".xls";
		
		XlsAssetReport report = new XlsAssetReport();
		try { 
			report.generateAssetReport(reportFileName, assets);
	
			File outputFile = new File(reportFileName);
			response.setBufferSize((int) outputFile.length());
	
			response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
			response.setContentType("application/x-download");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setContentLength((int)outputFile.length());
			FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			assetType = "TRUCK";
		}
		
		ModelAndView mav = new ModelAndView("assetTemplateView");
		mav.getModel().put("assetTypes",getAssetManagerService().getAssetTypes());
        mav.getModel().put("assetTemplates", getAssetManagerService().getAssetTemplates(assetType));
        
		return mav;
	}
	
	public ModelAndView deleteAssetHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			Set assetSet = new HashSet();
			String arrEntityList[] = getParamList(request);
			
			if (arrEntityList != null) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) {
					Asset a = assetManagerService.getAsset(arrEntityList[intCount]);					
					assetSet.add(a);					
				}
			}
			assetManagerService.removeEntity(assetSet);
			
			saveMessage(request, getMessage("app.actionmessage.103", null));
			return assetHandler(request,response);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new RuntimeException("Error in getting Asset List");
		}
	}

}
