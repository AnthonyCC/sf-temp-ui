package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchReportType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.datamanager.model.CancelOrderInfoModel;
import com.freshdirect.transadmin.datamanager.model.ICancelOrderInfo;
import com.freshdirect.transadmin.datamanager.report.ICrisisManagerReport;
import com.freshdirect.transadmin.datamanager.report.ReportGenerationException;
import com.freshdirect.transadmin.datamanager.report.XlsCrisisManagerReport;
import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;

public class CrisisManagerController extends AbstractMultiActionController  {
	
	private ICrisisManagerService  crisisManagerService;
	
	public ICrisisManagerService getCrisisManagerService() {
		return crisisManagerService;
	}

	public void setCrisisManagerService(ICrisisManagerService crisisManagerService) {
		this.crisisManagerService = crisisManagerService;
	}	
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView crisisMngReportHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		
		String crisisMngBatchId = request.getParameter("batchId");		
		String reportType = request.getParameter("reportType");		
		
		if(crisisMngBatchId != null && crisisMngBatchId.trim().length() > 0) {

			try {
								
				ICrisisManagerBatch batch = this.crisisManagerService.getCrisisMngBatchById(crisisMngBatchId);
				
				List<ICrisisMngBatchOrder> orders = new ArrayList<ICrisisMngBatchOrder>();
				if (batch != null
						&& EnumCrisisMngBatchType.REGULARORDER.equals(batch.getBatchType())){
					if (!(EnumCrisisMngBatchStatus.ORDERCOLECTIONCOMPLETE
							.equals(batch.getStatus()) || EnumCrisisMngBatchStatus.CANCELLED
							.equals(batch.getStatus()))) {
							orders = this.crisisManagerService.getCrisisMngBatchRegularOrder(batch.getBatchId(),true, false);
					} else {
							orders = this.crisisManagerService.getCrisisMngBatchRegularOrder(batch.getBatchId(),false, false);
					}
				} else {
					if (!(EnumCrisisMngBatchStatus.ORDERCOLECTIONCOMPLETE
							.equals(batch.getStatus()) || EnumCrisisMngBatchStatus.CANCELLED
							.equals(batch.getStatus())) && !(EnumCrisisMngBatchReportType.SOSIMULATIONREPORT.getDescription().equals(reportType))
							&& !(EnumCrisisMngBatchReportType.SOFAILUREREPORT.getDescription().equals(reportType))) {
							orders = this.crisisManagerService.getCrisisMngBatchStandingOrder(batch.getBatchId(),true, true);
					} else {
						    orders = this.crisisManagerService.getCrisisMngBatchStandingOrder(batch.getBatchId(),false, false);
					}
				}
				
				//standing order failures
				if(orders != null && EnumCrisisMngBatchReportType.SOFAILUREREPORT.getDescription().equals(reportType)){
					Iterator<ICrisisMngBatchOrder> _orderItr = orders.iterator();
					while(_orderItr.hasNext()){
						ICrisisMngBatchOrder _order = _orderItr.next();
						if(_order.getErrorHeader() == null) _orderItr.remove();
					}
				}
				Map<String, List<ICrisisManagerBatchDeliverySlot>> batchTimeslots
														= this.crisisManagerService.getCrisisMngBatchTimeslot(batch.getBatchId(), false);
				List<ICrisisManagerBatchDeliverySlot> slots 
														= new ArrayList<ICrisisManagerBatchDeliverySlot>();
				for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> _slotEntry : batchTimeslots.entrySet()){
					slots.addAll(_slotEntry.getValue());
				}
				
				Map<String, List<ICancelOrderInfo>> orderMapping = getOrderInfo(orders);
							
				String reportFileName = "";
				
				reportFileName = getBatchReport(request, reportType, batch,
						slots, orderMapping, reportFileName);			
				
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

	private String getBatchReport(HttpServletRequest request,
			String reportType, ICrisisManagerBatch batch,
			List<ICrisisManagerBatchDeliverySlot> slots,
			Map<String, List<ICancelOrderInfo>> orderMapping,
			String reportFileName) throws ReportGenerationException,
			ParseException {
		
		CrisisManagerReportData reportData = new CrisisManagerReportData();
		reportData.setBatch(batch);
		reportData.setOrders(orderMapping.get(batch.getBatchType().name()) != null ? orderMapping.get(batch.getBatchType().name()) : new ArrayList<ICancelOrderInfo>());				
		reportData.setTimeslots(slots);
		
		ICrisisManagerReport report = new XlsCrisisManagerReport();
		
		if(EnumCrisisMngBatchReportType.MARKETING.getDescription().equals(reportType)){
			reportFileName = TransportationAdminProperties.getMarketingOrderRptFileName()
														+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
														+"_batch_"+batch.getBatchId()+"_"							
														+System.currentTimeMillis()+".xls";

			report.generateMarketingReport(reportFileName, reportData);
		} else if(EnumCrisisMngBatchReportType.VOICESHOT.getDescription().equals(reportType)){					
			reportFileName = TransportationAdminProperties.getVoiceShotOrderRptFileName()
														+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
														+"_batch_"+batch.getBatchId()+"_"							
														+System.currentTimeMillis()+".xls";

			report.generateVoiceShotReport(reportFileName, reportData);
			
		} else if(EnumCrisisMngBatchReportType.TIMESLOTEXCEPTION.getDescription().equals(reportType)){
			reportFileName = TransportationAdminProperties.getTimeSlotExceptionRptFileName()
														+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
														+"_batch_"+batch.getBatchId()+"_"							
														+System.currentTimeMillis()+".xls";

			report.generateTimeSlotExceptionReport(reportFileName, reportData);
		} else if(EnumCrisisMngBatchReportType.SOSIMULATIONREPORT.getDescription().equals(reportType)){
			reportFileName = TransportationAdminProperties.getSOSimulationRptFileName()
														+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
														+"_batch_"+batch.getBatchId()+"_"							
														+System.currentTimeMillis()+".xls";

			report.generateSOSimulationReport(reportFileName, reportData);
		} else if(EnumCrisisMngBatchReportType.SOFAILUREREPORT.getDescription().equals(reportType)){
			reportFileName = TransportationAdminProperties.getSOFailureRptFileName()
														+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
														+"_batch_"+batch.getBatchId()+"_"							
														+System.currentTimeMillis()+".xls";

			report.generateSOFailureReport(reportFileName, reportData);
		}
		return reportFileName;
	}
	
	private Map<String, List<ICancelOrderInfo>> getOrderInfo(List<ICrisisMngBatchOrder> orders){
		
		Map<String, List<ICancelOrderInfo>> orderMapping = new HashMap<String, List<ICancelOrderInfo>>();
		List<ICancelOrderInfo> regularOrders = new ArrayList<ICancelOrderInfo>();
		List<ICancelOrderInfo> standingOrders = new ArrayList<ICancelOrderInfo>();
		try{
			if(orders != null && orders.size() > 0){
				Iterator<ICrisisMngBatchOrder> _orderItr = orders.iterator();
				ICancelOrderInfo orderInfo = null;
				while(_orderItr.hasNext()){
					ICrisisMngBatchOrder _order = _orderItr.next();
					if(_order.getId() == null){
						orderInfo = new CancelOrderInfoModel();
						regularOrders.add(orderInfo);
						orderInfo.setCustomerId(_order.getCustomerModel().getErpCustomerPK());
						orderInfo.setFirstName(_order.getCustomerModel().getFirstName());
						orderInfo.setLastName(_order.getCustomerModel().getLastName());
						orderInfo.setEmail(_order.getCustomerModel().getEmail());
						orderInfo
								.setPhoneNumber(_order.getCustomerModel()
										.getHomePhone() != null ? _order.getCustomerModel().getHomePhone()
																	: _order.getCustomerModel().getCellPhone() != null 
																		? _order.getCustomerModel().getCellPhone()
																			: _order.getCustomerModel().getBusinessPhone());						
					} else {						
						orderInfo = new CancelOrderInfoModel();
						standingOrders.add(orderInfo);
						orderInfo.setStandingOrderId( _order.getId());
						orderInfo.setCompanyName(_order.getCustomerModel().getCompanyName());
						orderInfo.setCustomerId(_order.getCustomerModel().getErpCustomerPK());
						orderInfo.setOrderNumber(_order.getOrderNumber());					
						orderInfo.setDeliveryWindow(TransStringUtil.getServerTime(_order.getStartTime()) + " - " + TransStringUtil.getServerTime(_order.getEndTime()) );
						orderInfo.setEmail(_order.getCustomerModel().getEmail());
						orderInfo.setCellPhone(_order.getCustomerModel().getCellPhone() != null ? _order.getCustomerModel().getCellPhone() : _order.getCustomerModel().getHomePhone());
						orderInfo.setBusinessPhone((_order.getCustomerModel().getBusinessPhone() != null && _order.getCustomerModel().getBusinessExt() != null) 
								? (_order.getCustomerModel().getBusinessPhone()+"-"+_order.getCustomerModel().getBusinessExt()) 
										: _order.getCustomerModel().getBusinessPhone() != null ? _order.getCustomerModel().getBusinessPhone() : "--");
						orderInfo.setOrderStatus(_order.getOrderStatus().getDisplayName());
						orderInfo.setLineItemCount(_order.getLineItemCount());
						orderInfo.setTempLineItemCount(_order.getTempLineItemCount());
						orderInfo.setErrorDetail(_order.getErrorHeader());
					}
				}				
				orderMapping.put(EnumCrisisMngBatchType.REGULARORDER.name(), regularOrders);
				orderMapping.put(EnumCrisisMngBatchType.STANDINGORDER.name(), standingOrders);
			}
		}catch(ParseException px){
			px.printStackTrace();
		}
		return orderMapping;
	
	}

}
	
