package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.transadmin.datamanager.model.CancelOrderInfoModel;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;
import com.freshdirect.transadmin.datamanager.report.IMarketingReport;
import com.freshdirect.transadmin.datamanager.report.IVoiceShotReport;
import com.freshdirect.transadmin.datamanager.report.XlsMarketingReport;
import com.freshdirect.transadmin.datamanager.report.XlsVoiceShotReport;
import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class CrisisManagerController extends AbstractMultiActionController  {
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView marketingReportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		
		String crisisMngBatchId = request.getParameter("batchId");		
		String regularOrderKey = request.getParameter("regularOrderKey");
		String standingOrderKey = request.getParameter("standingOrderKey");
		
		if(crisisMngBatchId != null && crisisMngBatchId.trim().length() > 0) {

			try {
				
				CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy(); 
				ICrisisManagerBatch batch = proxy.getCrisisMngBatchById(crisisMngBatchId);
				
				List<ICrisisManagerBatchOrder> orders = proxy.getCrisisMngBatchOrders(crisisMngBatchId, true, false);
				
				Map<String, List<ICancelOrderInfo>> orderMapping = getOrderInfo(orders);
							
				String reportFileName = TransportationAdminProperties.getMarketingOrderRptFileName()
												+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)							
												+System.currentTimeMillis()+".xls";
				
				CrisisManagerReportData reportData = new CrisisManagerReportData();
				reportData.setRegularOrders(orderMapping.get(regularOrderKey));
				reportData.setStandingOrders(orderMapping.get(standingOrderKey));
				reportData.setBatch(batch);

				IMarketingReport report = new XlsMarketingReport();
				report.generateMarketingReport(reportFileName, reportData);				

				File outputFile = new File(reportFileName);
				response.setBufferSize((int)outputFile.length());

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
		return null;
	}
	
	/**
	 * Custom handler for voiceshotReport
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView voiceshotReportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	

		String crisisMngBatchId = request.getParameter("batchId");
		String all = request.getParameter("all");
		String regularOrderKey = request.getParameter("regularOrderKey");
		String standingOrderKey = request.getParameter("standingOrderKey");
		
		if(crisisMngBatchId != null && crisisMngBatchId.trim().length() > 0) {

			try {
				
				CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy(); 
				ICrisisManagerBatch batch = proxy.getCrisisMngBatchById(crisisMngBatchId);
				
				List<ICrisisManagerBatchOrder> orders = proxy.getCrisisMngBatchOrders(crisisMngBatchId, true, false);
				
				Map<String, List<ICancelOrderInfo>> orderMapping = getOrderInfo(orders);
							
				String reportFileName = TransportationAdminProperties.getVoiceShotOrderRptFileName()
												+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)							
												+System.currentTimeMillis()+".xls";
				
				CrisisManagerReportData reportData = new CrisisManagerReportData();
				reportData.setRegularOrders(orderMapping.get(regularOrderKey));
				reportData.setStandingOrders(orderMapping.get(standingOrderKey));
				reportData.setBatch(batch);

				IVoiceShotReport report = new XlsVoiceShotReport();
				report.generateVoiceShotReport(reportFileName, reportData);				

				File outputFile = new File(reportFileName);
				response.setBufferSize((int)outputFile.length());

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
		return null;
	}
	
	private Map<String, List<ICancelOrderInfo>> getOrderInfo(List<ICrisisManagerBatchOrder> orders){
		
		Map<String, List<ICancelOrderInfo>> orderMapping = new HashMap<String, List<ICancelOrderInfo>>();
		List<ICancelOrderInfo> regularOrders = new ArrayList<ICancelOrderInfo>();
		List<ICancelOrderInfo> standingOrders = new ArrayList<ICancelOrderInfo>();
		try{
			if(orders != null && orders.size() > 0){
				Iterator<ICrisisManagerBatchOrder> _orderItr = orders.iterator();
				ICancelOrderInfo orderInfo = null;
				while(_orderItr.hasNext()){
					ICrisisManagerBatchOrder _order = _orderItr.next();
					if(_order.getStandingOrderId() == null){
						orderInfo = new CancelOrderInfoModel();
						regularOrders.add(orderInfo);
						orderInfo.setCustomerId(_order.getErpCustomerPK());
						orderInfo.setFirstName(_order.getFirstName());
						orderInfo.setLastName(_order.getLastName());
						orderInfo.setEmail(_order.getEmail());
						orderInfo.setPhoneNumber(_order.getHomePhone() != null ? _order.getHomePhone() : _order.getCellPhone() != null ? _order.getCellPhone() : _order.getBusinessPhone());				
					} else {
						orderInfo = new CancelOrderInfoModel();
						standingOrders.add(orderInfo);
						orderInfo.setCompanyName(_order.getCompanyName());
						orderInfo.setCustomerId(_order.getErpCustomerPK());
						orderInfo.setOrderNumber(_order.getOrderNumber());					
						orderInfo.setDeliveryWindow(TransStringUtil.getServerTime(_order.getStartTime()) + " - " + TransStringUtil.getServerTime(_order.getEndTime()) );
						orderInfo.setEmail(_order.getEmail());
						orderInfo.setCellPhone(_order.getCellPhone() != null ? _order.getCellPhone() : _order.getHomePhone());
						orderInfo.setBusinessPhone(_order.getBusinessPhone() != null ? (_order.getBusinessPhone()+"-"+_order.getBusinessExt()) : "--");				
					}
				}				
				orderMapping.put("O",regularOrders);
				orderMapping.put("SO",standingOrders);
			}
		}catch(ParseException px){
			px.printStackTrace();
		}
		return orderMapping;
	
	}
	
	private class CancelOrderInfoComparator implements Comparator<CancelOrderInfoModel> {

		public int compare(CancelOrderInfoModel order1, CancelOrderInfoModel order2) {
			if(order1.getLastName()!= null &&  order2.getLastName() != null) {
				return -(order1.getLastName().compareTo(order2.getLastName()));
			}
			return 0;
		}

	}
}
	
