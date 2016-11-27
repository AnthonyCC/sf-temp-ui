package com.freshdirect.dashboard.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freshdirect.dashboard.data.response.ForecastResponse;
import com.freshdirect.dashboard.data.response.Message;
import com.freshdirect.dashboard.exception.ErrorCodeOrderRateEnum;
import com.freshdirect.dashboard.exception.ServiceException;
import com.freshdirect.dashboard.model.DashboardSummary;
import com.freshdirect.dashboard.model.ForecastModel;
import com.freshdirect.dashboard.model.OrderRateVO;
import com.freshdirect.dashboard.model.ProjectedUtilizationBase;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.model.TimeslotModel;
import com.freshdirect.dashboard.service.IEventService;
import com.freshdirect.dashboard.service.IOrderService;
import com.freshdirect.dashboard.util.DateUtil;
import com.freshdirect.dashboard.util.OrderRateUtil;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IEventService eventService;
	
	/**
     * @return the list of base dates to compare
     */
	@RequestMapping(value="/basedates", method=RequestMethod.GET)	
	public @ResponseBody List<String> getBaseDates(HttpServletRequest request, HttpServletResponse response) {
		String deliveryDate = request.getParameter("deliveryDate");
		if(deliveryDate == null || ("".equals(deliveryDate))) {
			deliveryDate = DateUtil.getNextDate();
		}
		return orderService.getBaseDates(deliveryDate);
	}

	/**
     * @return the list of PlantDispatchData
     * @throws ServiceException if the requested date is null.
     */
	@RequestMapping(value="/projectedutilization", method=RequestMethod.GET)	
	public @ResponseBody Message getProjectedUtilization(HttpServletRequest request, HttpServletResponse response) {
		
		String deliveryDate = request.getParameter("deliveryDate");
		Message responseMessage = new Message();
		List<ProjectedUtilizationBase> finalUtilizationList = new ArrayList<ProjectedUtilizationBase>();
		try {
			if(deliveryDate == null || ("".equals(deliveryDate))) {
				deliveryDate = DateUtil.getNextDate();
			}
			
			Map<Date, String> cutoffMap = orderService.getCutoffs(); 
			Map<String, Map<Date, Integer>> realtimeBounceMap = eventService.getRealTimeBounceByZone(deliveryDate);			
			Map<String, Map<Date, Integer>> customerVisitMap = eventService.getCustomerVisitCnt(deliveryDate);
			Map<String, Map<Date, Integer>> customerSOWMap = eventService.getCustomerSoldOutWindowCnt(deliveryDate);
			Map<String, Map<Date, Integer>> soWindowMap = eventService.getSOWByZone(deliveryDate);
			List<ProjectedUtilizationVO> projectedUtilizationList = orderService.getProjectedUtilization(deliveryDate);
						
			Map<String, Map<String, ProjectedUtilizationBase>> projectedUtilizationMap = new HashMap<String, Map<String,ProjectedUtilizationBase>>();
						
			ProjectedUtilizationBase _projUtilization = null;
			for (ListIterator<ProjectedUtilizationVO> i = projectedUtilizationList.listIterator(); i.hasNext();) {
				ProjectedUtilizationVO pu = i.next();
				
				if(!projectedUtilizationMap.containsKey(pu.getZone())) {
					projectedUtilizationMap.put(pu.getZone(), new HashMap<String, ProjectedUtilizationBase>());
				}	
				
				if(!projectedUtilizationMap.get(pu.getZone()).containsKey(cutoffMap.get(pu.getCutoffTime()))){
					projectedUtilizationMap.get(pu.getZone()).put(cutoffMap.get(pu.getCutoffTime()), new ProjectedUtilizationBase());
				}
				
				_projUtilization = projectedUtilizationMap.get(pu.getZone()).get(cutoffMap.get(pu.getCutoffTime()));
				if(_projUtilization != null && _projUtilization.getZone() != null) {
					
					_projUtilization.setPlannedCapacity((int)(_projUtilization.getPlannedCapacity() + pu.getPlannedCapacity()));
					_projUtilization.setConfirmedOrderCnt((int)(_projUtilization.getConfirmedOrderCnt() + pu.getConfirmedOrderCnt()));
					
					if(pu.getPlannedCapacity() > 0)
						_projUtilization.setConfirmedUtilization(_projUtilization.getConfirmedUtilization() + Math.round((float)((pu.getConfirmedOrderCnt() / pu.getPlannedCapacity()) * 100.0)));
					else
						_projUtilization.setConfirmedUtilization(_projUtilization.getConfirmedUtilization());
					_projUtilization.setAllocatedOrderCnt((int)(_projUtilization.getAllocatedOrderCnt() + pu.getAllocatedOrderCnt()));
					if(pu.getPlannedCapacity() > 0)
						_projUtilization.setAllocatedUtilization(_projUtilization.getAllocatedUtilization() + Math.round((float)((pu.getAllocatedOrderCnt() / pu.getPlannedCapacity()) * 100.0)));
					else
						_projUtilization.setAllocatedUtilization(_projUtilization.getAllocatedUtilization());
					_projUtilization.setProjectedOrderCnt(_projUtilization.getProjectedOrderCnt() + Math.round((float)((pu.getConfirmedOrderCnt() + pu.getExpectedOrderCnt()) * 100)));
					if(pu.getPlannedCapacity() > 0)
						_projUtilization.setProjectedUtilization(_projUtilization.getProjectedUtilization() + Math.round((float)((_projUtilization.getProjectedOrderCnt() / pu.getPlannedCapacity()) * 100.0)));
					else
						_projUtilization.setProjectedUtilization(_projUtilization.getProjectedUtilization());
					
					_projUtilization.setResourceCnt(_projUtilization.getResourceCnt() + pu.getResourceCnt());					
					if(pu.getResourceCnt() > 0)
						_projUtilization.setProjectedOPT(_projUtilization.getProjectedOPT() + Math.round((_projUtilization.getProjectedOrderCnt() / pu.getResourceCnt()) * 100));
					else
						_projUtilization.setProjectedOPT(_projUtilization.getProjectedOPT());
					
					_projUtilization.setSoldOutWindowCnt(_projUtilization.getSoldOutWindowCnt() + ((soWindowMap.get(pu.getZone()) != null && soWindowMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? soWindowMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0));					
					_projUtilization.setCustomerSOWCnt(_projUtilization.getCustomerSOWCnt() + ((customerSOWMap.get(pu.getZone()) != null && customerSOWMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? customerSOWMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0));
					_projUtilization.setCustomerVisitCnt(_projUtilization.getCustomerVisitCnt() + ((customerVisitMap.get(pu.getZone()) != null && customerVisitMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? customerVisitMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0));
					_projUtilization.setRealTimeBounce(_projUtilization.getRealTimeBounce() + ((realtimeBounceMap.get(pu.getZone()) != null && realtimeBounceMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? realtimeBounceMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0));					
					
				} else {
					_projUtilization.setZone(pu.getZone());
					_projUtilization.setShift(cutoffMap.get(pu.getCutoffTime()));
					_projUtilization.setHoursToCutoff(DateUtil.getDiffInHoursMinutes(new Date(), DateUtil.getActualDate(DateUtil.getDate(deliveryDate), pu.getCutoffTime())));
					_projUtilization.setPlannedCapacity((int)pu.getPlannedCapacity());
					_projUtilization.setConfirmedOrderCnt((int)pu.getConfirmedOrderCnt());
					if(pu.getPlannedCapacity() > 0)						
						_projUtilization.setConfirmedUtilization(Math.round((float)((pu.getConfirmedOrderCnt()/pu.getPlannedCapacity())*100.0)));
					else
						_projUtilization.setConfirmedUtilization(0);
					_projUtilization.setAllocatedOrderCnt((int)pu.getAllocatedOrderCnt());
					if(pu.getPlannedCapacity() > 0)
						_projUtilization.setAllocatedUtilization(Math.round((float)((pu.getAllocatedOrderCnt() / pu.getPlannedCapacity()) * 100.0)));
					else
						_projUtilization.setAllocatedUtilization(0);
					_projUtilization.setProjectedOrderCnt(Math.round((int)(pu.getConfirmedOrderCnt() + pu.getExpectedOrderCnt())));
					if(pu.getPlannedCapacity() > 0)
						_projUtilization.setProjectedUtilization(Math.round((float)((_projUtilization.getProjectedOrderCnt() / pu.getPlannedCapacity()) * 100.0)));
					else
						_projUtilization.setProjectedUtilization(0);					
					_projUtilization.setResourceCnt(pu.getResourceCnt());
					if(pu.getResourceCnt() > 0)
						_projUtilization.setProjectedOPT(Math.round((_projUtilization.getProjectedOrderCnt() / pu.getResourceCnt())));
					else
						_projUtilization.setProjectedOPT(0);
					_projUtilization.setSoldOutWindowCnt((soWindowMap.get(pu.getZone()) != null && soWindowMap.get(pu.getZone()).get(pu.getCutoffTime()) != null) 
							? soWindowMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0);
					_projUtilization.setCustomerSOWCnt((customerSOWMap.get(pu.getZone()) != null && customerSOWMap.get(pu.getZone()).get(pu.getCutoffTime()) != null) 
							? customerSOWMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0);
					_projUtilization.setCustomerVisitCnt((customerVisitMap.get(pu.getZone()) != null && customerVisitMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? customerVisitMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0);
					_projUtilization.setRealTimeBounce((realtimeBounceMap.get(pu.getZone()) != null && realtimeBounceMap.get(pu.getZone()).get(pu.getCutoffTime()) != null)
							? realtimeBounceMap.get(pu.getZone()).get(pu.getCutoffTime()) : 0);					
				}				
			}	
			
			for(Map.Entry<String, Map<String, ProjectedUtilizationBase>> entrySet : projectedUtilizationMap.entrySet()) {
				finalUtilizationList.addAll(entrySet.getValue().values());
			}
			
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServiceException(ErrorCodeOrderRateEnum.UNKNOWN_ERROR, 
					"Failed to load projected utilization data");
		}
		if(finalUtilizationList.size() > 0) {
			orderService.runCapacitySuggestedActionLogic(finalUtilizationList);
		}
			
		Collections.sort(finalUtilizationList, new ProjectedUtilizationComparator());
		responseMessage.setData(finalUtilizationList);
		responseMessage.setTotalRecords(finalUtilizationList.size());
		
		return responseMessage;
	}
	
	/**
     * @return the list of PlantDispatchData
     * @throws ServiceException if the requested date is null.
     */
	@RequestMapping(value="/zonedetail", method=RequestMethod.GET)	
	public @ResponseBody Message getZoneDetail(HttpServletRequest request, HttpServletResponse response) {
		Message responseMessage = new Message();
		String deliveryDate = request.getParameter("deliveryDate");
		String zone = request.getParameter("zone");
		try {			
			List<TimeslotModel> timeslotLst = orderService.getOrderMetricsByTimeslot(deliveryDate, zone);
			responseMessage.setData(timeslotLst);
			responseMessage.setTotalRecords(timeslotLst != null ? timeslotLst.size() : 0);
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServiceException(ErrorCodeOrderRateEnum.UNKNOWN_ERROR, 
					"Failed to load zone detail for zone: "+ zone);
		}
		return responseMessage;
	}
	
	/**
     * @return the Forecast volume for delivery date
     * @throws ServiceException if the requested date is null.
     */	
	@RequestMapping(value="/forecast", method=RequestMethod.GET)	
	public @ResponseBody ForecastResponse getForcast(HttpServletRequest request, HttpServletResponse response) {
		
		ForecastResponse responseMessage = new ForecastResponse();
		String deliveryDate = request.getParameter("deliveryDate");
		String zone = request.getParameter("zone");
		String shift = request.getParameter("shift");
		String day1 = request.getParameter("day1");
		String day2 = request.getParameter("day2");
		
		try {
			if(deliveryDate == null || ("".equals(deliveryDate))) {
				deliveryDate = DateUtil.getNextDate();
			}
			List<OrderRateVO> projectedList = orderService.getForecast(deliveryDate, zone, day1, day2);
			List<OrderRateVO> currentOrderRateList = orderService.getCurrentOrderRateBySnapshot(DateUtil.getPreviousDate(DateUtil.getDate(deliveryDate)), deliveryDate, zone);
			Map<Date, String> cutoffMap = orderService.getCutoffs();
			
			Date currentMaxSnapshot = null;
			ForecastModel _tempModel = null;
			int currentMaxOrder = 0, currentMaxCapacity = 0;
			if(currentOrderRateList != null) {
				for (ListIterator<OrderRateVO> i = currentOrderRateList.listIterator(); i.hasNext();) {
					OrderRateVO _orderRate = i.next();	
					currentMaxSnapshot = _orderRate.getSnapshotTime();
					
					if(_orderRate.getZone() == null
							|| (cutoffMap != null 
									&& cutoffMap.get(_orderRate.getCutoffTime()).equals(shift))) {
						
						currentMaxCapacity = _orderRate.getCapacity();
						currentMaxOrder = (int) _orderRate.getOrderCount();
						
						// capacity volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_orderRate.getSnapshotTime()));
						_tempModel.setVolume(_orderRate.getCapacity());
						responseMessage.getCapacity().add(_tempModel);
						
						// order volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_orderRate.getSnapshotTime()));
						_tempModel.setVolume((int) _orderRate.getOrderCount());
						responseMessage.getOrder().add(_tempModel);
					}
				}
				
				if(currentMaxSnapshot != null) {
					Date maxSnapshot = DateUtil.getMaxSnapshot(currentMaxSnapshot);
					while(currentMaxSnapshot.before(maxSnapshot)) {
						// capacity volume
						_tempModel = new ForecastModel();
						try {
							currentMaxSnapshot = OrderRateUtil.addTime(currentMaxSnapshot);
							_tempModel.setSnapshotTime(DateUtil.getServerTime(currentMaxSnapshot));
							_tempModel.setVolume(currentMaxCapacity);
							responseMessage.getCapacity().add(_tempModel);
						} catch (ParseException e) {					
							e.printStackTrace();
						}
					}
					
				}
			}
			
			if(projectedList != null) {
				Map<Date, Double> snapshotMap = new TreeMap<Date, Double>();
				for (ListIterator<OrderRateVO> i = projectedList.listIterator(); i.hasNext();) {
					OrderRateVO _orderRate = i.next();
					
					if(_orderRate.getZone() == null) {
						
						currentMaxOrder = currentMaxOrder + Math.round(_orderRate.getProjectedRate());
						// projected volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_orderRate.getSnapshotTime()));
						_tempModel.setVolume(currentMaxOrder);
						if(_tempModel.getSnapshotTime().equals("12:00 AM"))
							_tempModel.setSnapshotTime("11:59 PM");
						responseMessage.getProjection().add(_tempModel);
					}
					
					if(_orderRate.getZone() != null && (cutoffMap != null 
									&& cutoffMap.get(_orderRate.getCutoffTime()).equals(shift))) {
						
						if(!snapshotMap.containsKey(_orderRate.getSnapshotTime())) {
							snapshotMap.put(_orderRate.getSnapshotTime(), 0.0);
						}
						snapshotMap.put(_orderRate.getSnapshotTime(), snapshotMap.get(_orderRate.getSnapshotTime()).doubleValue()+_orderRate.getProjectedRate());
					}
				}
				
				for(Map.Entry<Date, Double> snapshotEntry : snapshotMap.entrySet()) {
					
					double _rate = Math.round((double) snapshotEntry.getValue());
					currentMaxOrder = (int) (currentMaxOrder + _rate);
					// projected volume
					_tempModel = new ForecastModel();
					_tempModel.setSnapshotTime(DateUtil.getServerTime(snapshotEntry.getKey()));
					_tempModel.setVolume(currentMaxOrder);
					if(_tempModel.getSnapshotTime().equals("12:00 AM")) _tempModel.setSnapshotTime("11:59 PM");
					responseMessage.getProjection().add(_tempModel);
				}
			}
			
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServiceException(ErrorCodeOrderRateEnum.UNKNOWN_ERROR, 
					"Failed to load forecast for delivery date: "+ deliveryDate);
		}
		return responseMessage;
	}
	

	/**
     * @return the Forecast volume for delivery date
     * @throws ServiceException if the requested date is null.
     */	
	@RequestMapping(value="/compareforecast", method=RequestMethod.GET)	
	public @ResponseBody ForecastResponse compareForcast(HttpServletRequest request, HttpServletResponse response) {
		
		ForecastResponse responseMessage = new ForecastResponse();
		String day1 = request.getParameter("day1");
		String day2 = request.getParameter("day2");
		
		try {
			Set<Date> exceptions = orderService.getExceptions();
			Calendar cal = Calendar.getInstance();
			if (day1 == null || day2== null || "".equals(day2)
					|| "".equals(day1)) {
				day1 = DateUtil.getDate(OrderRateUtil.getSample(cal, exceptions));
				day1 = DateUtil.getDate(OrderRateUtil.getSample(cal, exceptions));
			} 
			
			List<OrderRateVO> orderRateDay1 = orderService.getCurrentOrderRateBySnapshot(DateUtil.getPreviousDate(DateUtil.getDate(day1)), day1, null);			
			List<OrderRateVO> orderRateDay2 = orderService.getCurrentOrderRateBySnapshot(DateUtil.getPreviousDate(DateUtil.getDate(day2)), day2, null);
			
			ForecastModel _tempModel = null;
			if(orderRateDay1 != null) {
				for (ListIterator<OrderRateVO> i = orderRateDay1.listIterator(); i.hasNext();) {
					OrderRateVO _orderRate = i.next();
					
					if(_orderRate.getZone() == null) {						
						// order volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_orderRate.getSnapshotTime()));
						_tempModel.setVolume((int) _orderRate.getOrderCount());
						responseMessage.getDay1().add(_tempModel);
					}
				}
			}
			
			if(orderRateDay2 != null) {
				for (ListIterator<OrderRateVO> i = orderRateDay2.listIterator(); i.hasNext();) {
					OrderRateVO _orderRate = i.next();
					
					if(_orderRate.getZone() == null) {						
						// order volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_orderRate.getSnapshotTime()));
						_tempModel.setVolume((int) _orderRate.getOrderCount());
						responseMessage.getDay2().add(_tempModel);
					}
				}
			}
			
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServiceException(ErrorCodeOrderRateEnum.UNKNOWN_ERROR, 
					"Failed to load forecast for day1: "+ day1 +" day2: "+ day2);
		}
		return responseMessage;
	}
	
	protected class ProjectedUtilizationComparator implements Comparator<ProjectedUtilizationBase> {

		public int compare(ProjectedUtilizationBase obj1, ProjectedUtilizationBase obj2) {
			if (obj1 != null && obj2 != null && obj1.getZone() != null
					&& obj2.getZone() != null) {
				int z1 = Integer.parseInt(obj1.getZone());
				int z2 = Integer.parseInt(obj2.getZone());
				return (z1 - z2) == 0 ? obj1.getShift().compareTo(obj2.getShift()) : (z1 - z2);
			}
			return 0;
		}
	}
	
	@RequestMapping(value="/dashboardsummary", method=RequestMethod.GET)	
	public @ResponseBody DashboardSummary getDashboardsummary(HttpServletRequest request, HttpServletResponse response) {
		String deliveryDate = request.getParameter("deliveryDate");
		String day1 = request.getParameter("day1");
		String day2 = request.getParameter("day2");
		DashboardSummary summary = new DashboardSummary();
		try {
			if(deliveryDate == null || ("".equals(deliveryDate))) {
				deliveryDate = DateUtil.getNextDate();
			}			
			summary = orderService.getDashboardSummary(deliveryDate);
			int projectedOrderCnt = summary != null ? summary.getTotalOrders() : 0;
			List<OrderRateVO> projectedList = orderService.getForecast(deliveryDate, null, day1, day2);
			if(projectedList != null) {
				for (ListIterator<OrderRateVO> i = projectedList.listIterator(); i.hasNext();) {
					OrderRateVO _orderRate = i.next();
					projectedOrderCnt = projectedOrderCnt + Math.round(_orderRate.getProjectedRate());
				}
			}
			summary.setProjectedOrderCnt(projectedOrderCnt);		
			
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServiceException(ErrorCodeOrderRateEnum.UNKNOWN_ERROR, 
					"Failed to load dashboard summary for delivery date: "+ deliveryDate);
		}
		
		return summary;
	}
}
