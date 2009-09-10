package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.EarlyWarningCommand;
import com.freshdirect.transadmin.web.model.UnassignedCommand;

public class CapacityController extends AbstractMultiActionController {
	
	private DomainManagerI domainManagerService;
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView earlyWarningHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String rDate = request.getParameter("rDate");
		String cutOff = request.getParameter("cutOff");
		String rType = request.getParameter("rType");
		
		ModelAndView mav = new ModelAndView("earlyWarningView");
		
		Map<String, List<Capacity>> capacityMapping = null;
		
		if("T".equalsIgnoreCase(rType)) {			
			processEarlyWarning(mav, new TimeEarlyWarningFormatter(), 
					executeEarlyWarningTime(mav, rDate, cutOff, rType));
		} else {
			processEarlyWarning(mav, new OrderEarlyWarningFormatter(), 
					executeEarlyWarningOrder(mav, rDate, cutOff, rType));
		}
		
		
		if(TransStringUtil.isEmpty(rDate)) {
			rDate = TransStringUtil.getCurrentDate();
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("cutOff", cutOff);
		mav.getModel().put("rType", rType);
		mav.getModel().put("cutoffs", domainManagerService.getCutOffs());
		
		return mav;
	}
	
	private Map<String, List<Capacity>> executeEarlyWarningOrder(ModelAndView mav, String rDate, String cutOff, String rType) {
		
		Map<String, List<Capacity>> capacityMapping = new TreeMap<String, List<Capacity>>();
				
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
								
				Map<String, List<IDeliveryWindowMetrics>> slotsByZone = deliveryProxy.getTimeslotsByDateEx
																			(deliveryDate, getCutOffTime(cutOff), null);
								
				Iterator<String> _itr = slotsByZone.keySet().iterator();
								
				String _zoneCode = null;
								
				List<IDeliveryWindowMetrics> metrics = null;
												
				while(_itr.hasNext()) {
					_zoneCode = _itr.next();										
					metrics = slotsByZone.get(_zoneCode);
					if(!capacityMapping.containsKey(_zoneCode)) {
						capacityMapping.put(_zoneCode, new ArrayList<Capacity>());
					}
									
					if(metrics != null) {
						
						Iterator<IDeliveryWindowMetrics> _metricsItr = metrics.iterator();						
						IDeliveryWindowMetrics _metrics = null;
						Capacity _capacity = null;
						
						while(_metricsItr.hasNext()) {							
							_metrics = _metricsItr.next();
							
							_capacity = new Capacity();
							capacityMapping.get(_zoneCode).add(_capacity);
							
							_capacity.setDeliveryStartTime(_metrics.getDeliveryStartTime());
							_capacity.setDeliveryEndTime(_metrics.getDeliveryEndTime());
							_capacity.setTotalCapacity(_metrics.getOrderCapacity());
							_capacity.setTotalAllocated(_metrics.getTotalAllocatedOrders());
							_capacity.setTotalConfirmed(_metrics.getTotalConfirmedOrders());							
							
						}						
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} 
		return capacityMapping;
	}

	private Map<String, List<Capacity>> executeEarlyWarningTime(ModelAndView mav, String rDate, String cutOff, String rType) {
		
		Map<String, List<Capacity>> capacityMapping = new TreeMap<String, List<Capacity>>();
		
		
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
				
				Map<String, List<IDeliverySlot>> slotsByZone = deliveryProxy.getTimeslotsByDate
																	(deliveryDate, getCutOffTime(cutOff), null);
								
				Iterator<String> _itr = slotsByZone.keySet().iterator();
				
				Map<String, Zone> areaMapping = getZoneMapping();
				
				String _zoneCode = null;
				IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
				_schId.setDeliveryDate(deliveryDate);
				
				IZoneModel _zModel = new ZoneModel();
				IAreaModel _aModel = new AreaModel();
				_zModel.setArea(_aModel);
				
				_schId.setArea(_aModel);				
				List<IDeliveryWindowMetrics> metrics = null;							
				
				while(_itr.hasNext()) {
					
					_zoneCode = _itr.next();
					Zone _refZone = areaMapping.get(_zoneCode);
					_aModel.setAreaCode(_refZone.getArea().getCode());
					_aModel.setDepot("X".equalsIgnoreCase(_refZone.getArea().getIsDepot()) ? true : false);
					_zModel.setZoneNumber(_refZone.getZoneCode());
					_schId.setArea(_aModel);
					
					_schId.setRegionId(RoutingUtil.getRegion(_zModel));
					metrics = proxy.retrieveCapacityMetrics(_schId, slotsByZone.get(_zoneCode));
					if(!capacityMapping.containsKey(_zoneCode)) {
						capacityMapping.put(_zoneCode, new ArrayList<Capacity>());
					}	
					
					if(metrics != null) {
						
						Iterator<IDeliveryWindowMetrics> _metricsItr = metrics.iterator();						
						IDeliveryWindowMetrics _metrics = null;
						Capacity _capacity = null;
						
						 
						while(_metricsItr.hasNext()) {							
							_metrics = _metricsItr.next();
							_capacity = new Capacity();
							capacityMapping.get(_zoneCode).add(_capacity);
							
							_capacity.setDeliveryStartTime(_metrics.getDeliveryStartTime());
							_capacity.setDeliveryEndTime(_metrics.getDeliveryEndTime());
							_capacity.setTotalCapacity(_metrics.getTotalCapacityTime());
							_capacity.setTotalAllocated(_metrics.getConfirmedServiceTime() 
															+ _metrics.getConfirmedTravelTime()
															+ _metrics.getReservedServiceTime()
															+ _metrics.getReservedTravelTime());
							_capacity.setTotalConfirmed(_metrics.getConfirmedServiceTime() 
															+ _metrics.getConfirmedTravelTime());							
						}						
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}						
		}
		return capacityMapping;
	}
	
	private void processEarlyWarning(ModelAndView mav, EarlyWarningFormatter formatter
										, Map<String, List<Capacity>> capacityMapping)  {
		
		List<EarlyWarningCommand> capacity = new ArrayList<EarlyWarningCommand>();
		Map<String, Capacity> regionCapacity = new TreeMap<String, Capacity>();
		List<EarlyWarningCommand> regCapacity = new ArrayList<EarlyWarningCommand>();
		
		Map<String, Zone> zoneMapping = getZoneMapping();
		Iterator<String> _capItr = capacityMapping.keySet().iterator();
		String _zoneCode = null;
		Capacity _capacity = null;
		try {
			while(_capItr.hasNext()) {
				_zoneCode = _capItr.next();
				Zone _refZone = zoneMapping.get(_zoneCode);
				Iterator<Capacity> _childItr = capacityMapping.get(_zoneCode).iterator();
				
				double totalCapacity = 0;
				double totalConfirmed = 0;
				double totalAllocated = 0;
				double percentageConfirmed = 0;
				double percentageAllocated = 0;
				
				EarlyWarningCommand _displayCommand = null;
				EarlyWarningCommand _timeslotCommand = null;
				List<EarlyWarningCommand> timeslotDetails = null;
				
				_displayCommand =  new EarlyWarningCommand();
				timeslotDetails = new ArrayList<EarlyWarningCommand>();
				_displayCommand.setTimeslotDetails(timeslotDetails);
				_displayCommand.setCode(_refZone.getArea().getCode());
				_displayCommand.setName(_refZone.getArea().getName());
				
				while(_childItr.hasNext()) {
					_capacity = _childItr.next();
					
					totalCapacity = totalCapacity + _capacity.getTotalCapacity();
					totalConfirmed = totalConfirmed + _capacity.getTotalConfirmed();							
					totalAllocated = totalAllocated + _capacity.getTotalAllocated();
					
					_timeslotCommand = new EarlyWarningCommand();
					timeslotDetails.add(_timeslotCommand);
					_timeslotCommand.setName(RoutingDateUtil.formatDateTime
							(_capacity.getDeliveryStartTime(), _capacity.getDeliveryEndTime()));
					_timeslotCommand.setTotalCapacity(formatter.formatCapacity(_capacity.getTotalCapacity()));
					_timeslotCommand.setConfirmedCapacity(formatter.formatCapacity(_capacity.getTotalConfirmed()));
					_timeslotCommand.setAllocatedCapacity(formatter.formatCapacity(_capacity.getTotalAllocated()));
					_timeslotCommand.setPercentageConfirmed("0%");
					_timeslotCommand.setPercentageAllocated("0%");
					if(_capacity.getTotalCapacity() > 0) {
						_timeslotCommand.setPercentageConfirmed(""+Math.round((_capacity.getTotalConfirmed()/_capacity.getTotalCapacity())*100.0)+"%");
						_timeslotCommand.setPercentageAllocated(""+Math.round((_capacity.getTotalAllocated()/_capacity.getTotalCapacity())*100.0)+"%");								
					}
				}
				if(totalCapacity > 0) {
					percentageConfirmed = (totalConfirmed/totalCapacity)*100.0;
					percentageAllocated = (totalAllocated/totalCapacity)*100.0;
				}
				_displayCommand.setTotalCapacity(formatter.formatCapacity(totalCapacity));
				_displayCommand.setConfirmedCapacity(formatter.formatCapacity(totalConfirmed));
				_displayCommand.setAllocatedCapacity(formatter.formatCapacity(totalAllocated));
				_displayCommand.setPercentageConfirmed(""+Math.round(percentageConfirmed)+"%");
				_displayCommand.setPercentageAllocated(""+Math.round(percentageAllocated)+"%");
				capacity.add(_displayCommand);
				
				if(!regionCapacity.containsKey(_refZone.getRegion().getName())) {
					regionCapacity.put(_refZone.getRegion().getName(), new Capacity());
				}
				Capacity _tmpRegCapacity = regionCapacity.get(_refZone.getRegion().getName());
				_tmpRegCapacity.setTotalCapacity(_tmpRegCapacity.getTotalCapacity()+totalCapacity);
				_tmpRegCapacity.setTotalAllocated(_tmpRegCapacity.getTotalAllocated()+totalAllocated);
				_tmpRegCapacity.setTotalConfirmed(_tmpRegCapacity.getTotalConfirmed()+totalConfirmed);
			}
			
			Iterator<String> _regItr = regionCapacity.keySet().iterator();
			String _regName = null;
			Capacity _regCap = null;
			EarlyWarningCommand _regDisplayCommand = null; 
			
			double totalCapacity = 0;
			double totalConfirmed = 0;
			double totalAllocated = 0;
			double percentageConfirmed = 0;
			double percentageAllocated = 0;
			
			while(_regItr.hasNext()) {
				
				double _regPerConfirmed = 0;
				double _regPerAllocated = 0;
				_regName = _regItr.next();
				_regCap = regionCapacity.get(_regName);
				
				totalCapacity = totalCapacity + _regCap.getTotalCapacity();
				totalConfirmed = totalConfirmed + _regCap.getTotalConfirmed();							
				totalAllocated = totalAllocated + _regCap.getTotalAllocated();
				
				_regDisplayCommand =  new EarlyWarningCommand();
				_regDisplayCommand.setName(_regName);
				_regDisplayCommand.setTotalCapacity(formatter.formatCapacity(_regCap.getTotalCapacity()));
				_regDisplayCommand.setAllocatedCapacity(formatter.formatCapacity(_regCap.getTotalAllocated()));
				_regDisplayCommand.setConfirmedCapacity(formatter.formatCapacity(_regCap.getTotalConfirmed()));
				
				if(_regCap.getTotalCapacity() > 0) {
					_regPerConfirmed = (_regCap.getTotalConfirmed()/_regCap.getTotalCapacity())*100.0;
					_regPerAllocated = (_regCap.getTotalAllocated()/_regCap.getTotalCapacity())*100.0;
				}
				
				_regDisplayCommand.setPercentageConfirmed(""+Math.round(_regPerConfirmed)+"%");
				_regDisplayCommand.setPercentageAllocated(""+Math.round(_regPerAllocated)+"%");
				regCapacity.add(_regDisplayCommand);
			}
			_regDisplayCommand =  new EarlyWarningCommand();
			_regDisplayCommand.setName("");
			_regDisplayCommand.setTotalCapacity(formatter.formatCapacity(totalCapacity));
			_regDisplayCommand.setAllocatedCapacity(formatter.formatCapacity(totalAllocated));
			_regDisplayCommand.setConfirmedCapacity(formatter.formatCapacity(totalConfirmed));
			if(totalCapacity > 0) {
				percentageConfirmed = (totalConfirmed/totalCapacity)*100.0;
				percentageAllocated = (totalAllocated/totalCapacity)*100.0;
			}
			_regDisplayCommand.setPercentageConfirmed(""+Math.round(percentageConfirmed)+"%");
			_regDisplayCommand.setPercentageAllocated(""+Math.round(percentageAllocated)+"%");
			regCapacity.add(_regDisplayCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.getModel().put("earlywarnings", capacity );
		mav.getModel().put("earlywarnings_region", regCapacity );
	}
	
	/**
	 * Custom handler for early warning
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView unassignedHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String rDate = request.getParameter("rDate");
		List<UnassignedCommand> unassigneds = new ArrayList<UnassignedCommand>();
		
		ModelAndView mav = new ModelAndView("unassignedView");
		
		if(TransStringUtil.isEmpty(rDate)) {
			rDate = TransStringUtil.getCurrentDate();
		}
		
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
						
				List<IOrderModel> orders = deliveryProxy.getUnassigned(deliveryDate, null, null);
				
				Iterator<IOrderModel> _itr = orders.iterator();
				IOrderModel _order = null;
				UnassignedCommand _unassigned = null;
				
				while(_itr.hasNext()) {
					_order = _itr.next();
					_unassigned = new UnassignedCommand();
					
					_unassigned.setCreateModTime(_order.getCreateModifyTime());
					_unassigned.setCustomerId(_order.getCustomerNumber());
					_unassigned.setOrderId(_order.getOrderNumber());
					_unassigned.setTimeWindow(RoutingDateUtil.formatDateTime(_order.getDeliveryInfo().getDeliveryStartTime()
								, _order.getDeliveryInfo().getDeliveryEndTime()));
					_unassigned.setUnassignedTime(_order.getUnassignedTime());
					_unassigned.setUnassignedAction(_order.getUnassignedAction());
					
					_unassigned.setReservationId(_order.getDeliveryInfo().getReservationId());
					_unassigned.setZone(_order.getDeliveryInfo().getDeliveryZone().getZoneNumber());
					unassigneds.add(_unassigned);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("unassigneds", unassigneds );
		return mav;
	}
	
	class Capacity {
		
		private Date deliveryStartTime;
		private Date deliveryEndTime;
		
		public Date getDeliveryStartTime() {
			return deliveryStartTime;
		}
		public void setDeliveryStartTime(Date deliveryStartTime) {
			this.deliveryStartTime = deliveryStartTime;
		}
		public Date getDeliveryEndTime() {
			return deliveryEndTime;
		}
		public void setDeliveryEndTime(Date deliveryEndTime) {
			this.deliveryEndTime = deliveryEndTime;
		}
		private double totalCapacity = 0;
		private double totalConfirmed = 0;
		private double totalAllocated = 0;
		
		public double getTotalCapacity() {
			return totalCapacity;
		}
		public void setTotalCapacity(double totalCapacity) {
			this.totalCapacity = totalCapacity;
		}
		public double getTotalConfirmed() {
			return totalConfirmed;
		}
		public void setTotalConfirmed(double totalConfirmed) {
			this.totalConfirmed = totalConfirmed;
		}
		public double getTotalAllocated() {
			return totalAllocated;
		}
		public void setTotalAllocated(double totalAllocated) {
			this.totalAllocated = totalAllocated;
		}
	}
	
	interface EarlyWarningFormatter {
		
		String formatCapacity(double value);
	}
	
	class TimeEarlyWarningFormatter implements EarlyWarningFormatter{
		
		public String formatCapacity(double value) {
			return ""+TransStringUtil.formatIntoHHMM(value);
		}
	}
	
	class OrderEarlyWarningFormatter implements EarlyWarningFormatter{
		
		public String formatCapacity(double value) {
			return ""+Math.round(value);
		}
	}
	
	protected Map getZoneMapping() {
		
		Map<String, Zone> result = new HashMap<String, Zone>();
		Collection zones = domainManagerService.getZones();
		Zone tmpZone = null;
		if(zones != null) {
			Iterator<Zone> iterator = zones.iterator();
			while(iterator.hasNext()) {
				tmpZone = (Zone)iterator.next();				
				if(tmpZone != null && tmpZone.getArea() != null) {
					result.put(tmpZone.getZoneCode(), tmpZone);
				}
			}
		}
		return result;
	}
	
	protected Date getCutOffTime(String cutOff) {
		TrnCutOff tmpModel = getDomainManagerService().getCutOff(cutOff);
		if(tmpModel != null) {
			return tmpModel.getCutOffTime().getAsDate();
		}
		return null;
	}

}
