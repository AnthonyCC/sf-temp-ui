package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.model.Region;
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
			rDate = TransStringUtil.getNextDate();
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("cutOff", cutOff);
		mav.getModel().put("rType", rType);
		mav.getModel().put("autorefresh", request.getParameter("autorefresh"));
		mav.getModel().put("cutoffs", domainManagerService.getCutOffs());
		
		return mav;
	}
	
	private IDeliverySlot matchSlotToMetrics(List<IDeliverySlot> slots, IDeliveryWindowMetrics metrics) {
		
		IDeliverySlot result = null;
		Iterator<IDeliverySlot> _itr = slots.iterator();
		IDeliverySlot _tmpSlot = null;
		
		if(slots != null) {
			while(_itr.hasNext()) {
				_tmpSlot = _itr.next();
				
				if(isMatchingTime(_tmpSlot.getStartTime(), metrics.getDeliveryStartTime())
						&& isMatchingTime(_tmpSlot.getStopTime(), metrics.getDeliveryEndTime())) {
					result = _tmpSlot;
					break;
				}
			}
		}
		return result;
	}
	
	private boolean isMatchingTime(Date date1, Date date2) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int hour1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);
		
		cal.setTime(date2);
		int hour2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);
		
		if(hour1 == hour2 && minute1 == minute2) {
			result = true; 
		}
		return result;
	}
	
	private Map<String, List<Capacity>> executeEarlyWarningOrder(ModelAndView mav, String rDate, String cutOff, String rType) {
		
		Map<String, List<Capacity>> capacityMapping = new TreeMap<String, List<Capacity>>();
				
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
				
				Map<String, List<IDeliverySlot>> refSlotsByZone = deliveryProxy.getTimeslotsByDate
																	(deliveryDate, getCutOffTime(cutOff), null);
				
				Map<String, List<IDeliveryWindowMetrics>> slotsByZone = deliveryProxy.getTimeslotsByDateEx
																			(deliveryDate, getCutOffTime(cutOff), null);
									
				Iterator<String> _itr = slotsByZone.keySet().iterator();
								
				String _zoneCode = null;
								
				List<IDeliveryWindowMetrics> metrics = null;
				IDeliverySlot _refSlot = null;
				
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
							
							_refSlot = matchSlotToMetrics(refSlotsByZone.get(_zoneCode), _metrics);
							if(_refSlot != null) {
								_capacity.setManuallyClosed(_refSlot.isManuallyClosed());
								_capacity.setDynamicActive(_refSlot.isDynamicActive());
								_capacity.setReferenceId(_refSlot.getReferenceId());
							}
							
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
												
				String _zoneCode = null;
				List<IDeliverySlot> _slots = null;
				IDeliverySlot _refSlot = null;
				
				while(_itr.hasNext()) {
					
					_zoneCode = _itr.next();
					_slots = slotsByZone.get(_zoneCode);
					List<IDeliveryWindowMetrics> metrics = null;
					IRoutingSchedulerIdentity _schId = null;
					if(_slots.size() > 0) {
						_schId = _slots.get(0).getSchedulerId();
						metrics = proxy.retrieveCapacityMetrics(_schId, _slots);
					}
					
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
							
							_refSlot = matchSlotToMetrics(_slots, _metrics);
							if(_refSlot != null) {
								_capacity.setManuallyClosed(_refSlot.isManuallyClosed());
								_capacity.setDynamicActive(_refSlot.isDynamicActive());
								_capacity.setReferenceId(_refSlot.getReferenceId());
							}
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
		Map<Region, Capacity> regionCapacity = new HashMap<Region, Capacity>();
		List<EarlyWarningCommand> regCapacity = new ArrayList<EarlyWarningCommand>();
		
		Map<String, Zone> zoneMapping = getZoneMapping();
		Iterator<String> _capItr = capacityMapping.keySet().iterator();
		String _zoneCode = null;
		Capacity _capacity = null;
		Capacity _tmpRegCapacity = null;
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
				
				int openCount = 0;
				int closedCount = 0;
				int dynamicActiveCount = 0;
				int dynamicInActiveCount = 0;
				
				while(_childItr.hasNext()) {
					_capacity = _childItr.next();
					
					totalCapacity = totalCapacity + _capacity.getTotalCapacity();
					totalConfirmed = totalConfirmed + _capacity.getTotalConfirmed();							
					totalAllocated = totalAllocated + _capacity.getTotalAllocated();
					
					_timeslotCommand = new EarlyWarningCommand();
					
					if(_capacity.isManuallyClosed()) {
						closedCount++;
					} else {
						openCount++;
					}
					if(_capacity.isDynamicActive()) {
						dynamicActiveCount++;
					} else {
						dynamicInActiveCount++;
					}
					_timeslotCommand.setOpenCount(_capacity.isManuallyClosed() ? 0 : 1);
					_timeslotCommand.setClosedCount(_capacity.isManuallyClosed() ? 1 : 0);
					
					_timeslotCommand.setDynamicActiveCount(_capacity.isDynamicActive() ? 1 : 0);
					_timeslotCommand.setDynamicInActiveCount(_capacity.isDynamicActive() ? 0 : 1);
					
					_timeslotCommand.setReferenceId(_capacity.getReferenceId());
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
				
				_displayCommand.setOpenCount(openCount);
				_displayCommand.setClosedCount(closedCount);				
				_displayCommand.setDynamicActiveCount(dynamicActiveCount);
				_displayCommand.setDynamicInActiveCount(dynamicInActiveCount);
				capacity.add(_displayCommand);
				
				if(!regionCapacity.containsKey(_refZone.getRegion())) {
					_tmpRegCapacity = new Capacity();
					_tmpRegCapacity.setManuallyClosed(true);
					regionCapacity.put(_refZone.getRegion(), _tmpRegCapacity);
				}
				_tmpRegCapacity  = regionCapacity.get(_refZone.getRegion());
				_tmpRegCapacity.setTotalCapacity(_tmpRegCapacity.getTotalCapacity()+totalCapacity);
				_tmpRegCapacity.setTotalAllocated(_tmpRegCapacity.getTotalAllocated()+totalAllocated);
				_tmpRegCapacity.setTotalConfirmed(_tmpRegCapacity.getTotalConfirmed()+totalConfirmed);
				
				_tmpRegCapacity.setOpenCount(_tmpRegCapacity.getOpenCount() + openCount);
				_tmpRegCapacity.setClosedCount(_tmpRegCapacity.getClosedCount() + closedCount);
				
				_tmpRegCapacity.setDynamicActiveCount(_tmpRegCapacity.getDynamicActiveCount() + dynamicActiveCount);
				_tmpRegCapacity.setDynamicInActiveCount(_tmpRegCapacity.getDynamicInActiveCount() + dynamicInActiveCount);				
				
			}
			
			Iterator<Region> _regItr = regionCapacity.keySet().iterator();
			Region _region = null;
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
				_region = _regItr.next();
								
				_regCap = regionCapacity.get(_region);
				
				totalCapacity = totalCapacity + _regCap.getTotalCapacity();
				totalConfirmed = totalConfirmed + _regCap.getTotalConfirmed();							
				totalAllocated = totalAllocated + _regCap.getTotalAllocated();
				
				_regDisplayCommand =  new EarlyWarningCommand();
				_regDisplayCommand.setName(_region.getName());
				_regDisplayCommand.setCode(_region.getCode());
				_regDisplayCommand.setTotalCapacity(formatter.formatCapacity(_regCap.getTotalCapacity()));
				_regDisplayCommand.setAllocatedCapacity(formatter.formatCapacity(_regCap.getTotalAllocated()));
				_regDisplayCommand.setConfirmedCapacity(formatter.formatCapacity(_regCap.getTotalConfirmed()));
				_regDisplayCommand.setRegion(true);
				_regDisplayCommand.setOpenCount(_regCap.getOpenCount());
				_regDisplayCommand.setClosedCount(_regCap.getClosedCount());
				_regDisplayCommand.setDynamicActiveCount(_regCap.getDynamicActiveCount());
				_regDisplayCommand.setDynamicInActiveCount(_regCap.getDynamicInActiveCount()); 
				
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
			_regDisplayCommand.setRegion(true);
			
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
			rDate = TransStringUtil.getNextDate();
		}
		
		if(!TransStringUtil.isEmpty(rDate)) {
			try {
				Date deliveryDate = TransStringUtil.getDate(rDate);
				
				DeliveryServiceProxy deliveryProxy = new DeliveryServiceProxy();
						
				List<IUnassignedModel> orders = deliveryProxy.getUnassigned(deliveryDate, null, null);
				
				Iterator<IUnassignedModel> _itr = orders.iterator();
				IUnassignedModel _order = null;
				UnassignedCommand _unassigned = null;
				
				while(_itr.hasNext()) {
					_order = _itr.next();
					
					_unassigned = new UnassignedCommand();
					
					_unassigned.setCreateModTime(_order.getOrder().getCreateModifyTime());
					_unassigned.setCustomerId(_order.getOrder().getCustomerNumber());
					_unassigned.setOrderId(_order.getOrder().getOrderNumber());
					_unassigned.setTimeWindow(RoutingDateUtil.formatDateTime(_order.getOrder().getDeliveryInfo().getDeliveryStartTime()
								, _order.getOrder().getDeliveryInfo().getDeliveryEndTime()));
					_unassigned.setUnassignedTime(_order.getOrder().getUnassignedTime());
					_unassigned.setUnassignedAction(_order.getOrder().getUnassignedAction());
					
					_unassigned.setReservationId(_order.getOrder().getDeliveryInfo().getReservationId());
					_unassigned.setZone(_order.getOrder().getDeliveryInfo().getDeliveryZone().getZoneNumber());
					
					_unassigned.setOrderSize(_order.getOrder().getDeliveryInfo().getOrderSize());
					_unassigned.setServiceTime(_order.getOrder().getDeliveryInfo().getServiceTime());
					
					_unassigned.setUnassignedOrderSize(_order.getOrder().getUnassignedOrderSize());
					_unassigned.setUnassignedServiceTime(_order.getOrder().getUnassignedServiceTime());
					
					_unassigned.setUpdateStatus(_order.getOrder().getUpdateStatus());
					
					_unassigned.setManuallyClosed(_order.getSlot().isManuallyClosed());
					_unassigned.setDynamicActive(_order.getSlot().isDynamicActive());
					
					_unassigned.setIsChefsTable(_order.getIsChefsTable());
					_unassigned.setIsForced(_order.getIsForced());
					unassigneds.add(_unassigned);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mav.getModel().put("rDate", rDate);
		mav.getModel().put("autorefresh", request.getParameter("autorefresh"));
		mav.getModel().put("unassigneds", unassigneds );
		return mav;
	}
	
	class Capacity {
		
		private Date deliveryStartTime;
		private Date deliveryEndTime;
		private boolean manuallyClosed;
		private String referenceId;
		private boolean dynamicActive;
		
		int openCount = 0;
		int closedCount = 0;
		int dynamicActiveCount = 0;
		int dynamicInActiveCount = 0;
		
		
		@Override
		public String toString() {
			return "Capacity [deliveryEndTime=" + deliveryEndTime
					+ ", deliveryStartTime=" + deliveryStartTime + "]";
		}
		
		
		public int getOpenCount() {
			return openCount;
		}

		public void setOpenCount(int openCount) {
			this.openCount = openCount;
		}

		public int getClosedCount() {
			return closedCount;
		}

		public void setClosedCount(int closedCount) {
			this.closedCount = closedCount;
		}

		public int getDynamicActiveCount() {
			return dynamicActiveCount;
		}

		public void setDynamicActiveCount(int dynamicActiveCount) {
			this.dynamicActiveCount = dynamicActiveCount;
		}

		public int getDynamicInActiveCount() {
			return dynamicInActiveCount;
		}

		public void setDynamicInActiveCount(int dynamicInActiveCount) {
			this.dynamicInActiveCount = dynamicInActiveCount;
		}

		public boolean isDynamicActive() {
			return dynamicActive;
		}
		public void setDynamicActive(boolean dynamicActive) {
			this.dynamicActive = dynamicActive;
		}
		public String getReferenceId() {
			return referenceId;
		}
		public void setReferenceId(String referenceId) {
			this.referenceId = referenceId;
		}
		public boolean isManuallyClosed() {
			return manuallyClosed;
		}
		public void setManuallyClosed(boolean manuallyClosed) {
			this.manuallyClosed = manuallyClosed;
		}
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
