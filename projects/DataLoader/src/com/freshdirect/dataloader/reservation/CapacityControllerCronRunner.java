package com.freshdirect.dataloader.reservation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class CapacityControllerCronRunner extends BaseCapacityCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(CapacityControllerCronRunner.class);

	private static final int DEFAULT_DAYS = 7;

	public static void main(String[] args) {

		if (!FDStoreProperties.isDynamicRoutingEnabled()) {
			return;
		}

		CapacityControllerCronRunner cron = new CapacityControllerCronRunner();
		Context ctx = null;

		boolean isTrialRun = false;
		boolean isPurgeEnabled = false;
		boolean isReverse = false;
		boolean isSendEmail = false;
		List<Date> jobDate = new ArrayList<Date>();
		Date now = Calendar.getInstance().getTime();
		
		try {
			ctx = cron.getInitialContext();
			DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
			DlvManagerSB dsb = dlh.create();


			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("jobDate=")) {
							jobDate.add(DateUtil.truncate(DateUtil.toCalendar
																(DateUtil.parse(arg.substring("jobDate=".length())))).getTime());
						} else if (arg.startsWith("trail=")) {
							isTrialRun =  Boolean.valueOf(arg.substring("trail=".length())).booleanValue(); 
						} else if (arg.startsWith("purge=")) {								
							isPurgeEnabled = Boolean.valueOf(arg.substring("purge=".length())).booleanValue(); 
						}  else if (arg.startsWith("reverse=")) {								
							isReverse = Boolean.valueOf(arg.substring("reverse=".length())).booleanValue(); 
						}  else if (arg.startsWith("sendEmail=")) {								
							isSendEmail = Boolean.valueOf(arg.substring("sendEmail=".length())).booleanValue(); 
						}
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.dataloader.reservation.CapacityControllerCronRunner [jobDate={date value}] [trail={true | false}] [purge={true | false}] [reverse={true | false}] [sendEmail={true | false}]");
						System.exit(-1);
					}
				}
			}
			
			LOGGER.info("##### CapacityControllerCronRunner Start: isTrialRun="+isTrialRun+"->isPurgeEnabled="+isPurgeEnabled+"->isSendEmail="+isSendEmail+"->isReverse="+isReverse+" ########");
			if(jobDate.size() == 0) {
				
				jobDate.addAll(dsb.getFutureTimeslotDates());
				/*int incrementBy = isReverse ? -1 : 1;
				Calendar baseDate = DateUtil.truncate(Calendar.getInstance());					
				baseDate.add(Calendar.DATE, isReverse ? 7 : 1);
				for(int i=0; i < DEFAULT_DAYS; i++) {
					jobDate.add(baseDate.getTime());
					baseDate.add(Calendar.DATE, incrementBy);
				}*/
			}

			boolean isDynaSyncEnabled = RoutingServicesProperties.getRoutingDynaSyncEnabled();
			isPurgeEnabled =  isPurgeEnabled && !isDynaSyncEnabled;
			
			Iterator<Date> _dateItr = jobDate.iterator();
			Date processDate = null;
					
			while(_dateItr.hasNext()) {
				processDate = _dateItr.next();
				try {
					Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree = dsb.retrieveWaveInstanceTree
																															(processDate, EnumWaveInstanceStatus.NOTSYNCHRONIZED);
					Set<String> inSyncZones = dsb.getInSyncWaveInstanceZones(processDate);
					
					List<DlvTimeslotModel> slots = dsb.getTimeslotsForDate(processDate);
					
					LOGGER.info("CapacityControllerCronRunner beginning to synchronize "+slots.size()
										+" timeslots for date "+processDate+ (isTrialRun ? " and is a trail run" : ""));
					TimeslotGroup group = groupDeliverySlotByZone(slots);
					Map<String, List<DlvTimeslotModel>> slotsByZone = group.getGroupByZone();
					
					if(isDynaSyncEnabled) {
						if(group.getSchedulerIds() != null && waveInstanceTree != null) {
							for(IRoutingSchedulerIdentity schedulerId : group.getSchedulerIds()) {
								if(waveInstanceTree.containsKey(schedulerId.getDeliveryDate())) {
									if(waveInstanceTree.get(schedulerId.getDeliveryDate()).containsKey(schedulerId.getArea().getAreaCode())) {
										dsb.synchronizeWaveInstance(schedulerId, waveInstanceTree, inSyncZones);
									}
								}
							}
						}	
					}
					
					Iterator<String> _itr = slotsByZone.keySet().iterator();

					String _zoneCode = null;
					List<IDeliverySlot> _routingSlots = null;
					List<DlvTimeslotModel> _dlvSlots = null;

					while(_itr.hasNext()) {

						_zoneCode = _itr.next();
						_dlvSlots = slotsByZone.get(_zoneCode);
						_routingSlots = collectRoutingSlots(_dlvSlots);

						List<IDeliveryWindowMetrics> metrics = null;
						IRoutingSchedulerIdentity _schId = null;

						if(_routingSlots.size() > 0) {
							_schId = _routingSlots.get(0).getSchedulerId();
							metrics = dsb.retrieveCapacityMetrics(_schId, _routingSlots, (isPurgeEnabled && !_dateItr.hasNext()));
							attachWindowMetrics(_dlvSlots, metrics);
						}									
					}
					recalculateCapacity(slots);
					if(!isTrialRun) {
						List<DlvTimeslotModel> filteredTimeSlots = new ArrayList<DlvTimeslotModel>();
						
						for(DlvTimeslotModel _tmpSlot : slots) {
							if(_tmpSlot.getCutoffTimeAsDate().after(now)) { // Check if the timeslot has not passed cutoff
								filteredTimeSlots.add(_tmpSlot);
							}
						}
						LOGGER.info(new StringBuilder("CapacityControllerCronRunner Slots Raw:").append(slots.size())
													.append(" Filtered:").append(filteredTimeSlots.size()));
						dsb.updateTimeslotsCapacity(filteredTimeSlots);
					}
					if(isSendEmail) { 
						email(processDate, slots, null, isTrialRun);
					}
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					LOGGER.info(new StringBuilder("CapacityControllerCronRunner failed with exception : ").append(sw.toString()).toString());
					if(isSendEmail) { 
						email(processDate, null, sw.toString(), isTrialRun);
					}
				}
			}
			if(isPurgeEnabled){
				LOGGER.debug("Fixing disassociated timeslots: START");
				dsb.fixDisassociatedTimeslots();
				LOGGER.debug("Fixing disassociated timeslots: STOP");
			}
			LOGGER.info("########################## CapacityControllerCronRunner Stop ##############################");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("CapacityControllerCronRunner failed with exception : ").append(sw.toString()).toString());
			LOGGER.error(sw);
			if(isSendEmail) { 
				email(Calendar.getInstance().getTime(), null, sw.toString(), isTrialRun);
			}
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException e) {
				LOGGER.warn("Could not close CTX due to following NamingException",	e);
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				email(Calendar.getInstance().getTime(), null, sw.toString(), isTrialRun);
			}
		}
	}
	
		
	private static TimeslotGroup groupDeliverySlotByZone(List<DlvTimeslotModel> dlvTimeSlots ) {
		
		TimeslotGroup group = new TimeslotGroup();
		
		Map<String, List<DlvTimeslotModel>> groupedSlots = new HashMap<String, List<DlvTimeslotModel>>();
		Set<IRoutingSchedulerIdentity> schedulerIds = new HashSet<IRoutingSchedulerIdentity>();
		group.setGroupByZone(groupedSlots);
		group.setSchedulerIds(schedulerIds);
		
		for(int i=0;i<dlvTimeSlots.size();i++) {
			DlvTimeslotModel dlvTimeSlot = dlvTimeSlots.get(i);
			schedulerIds.add(dlvTimeSlot.getRoutingSlot().getSchedulerId());
			
			if(groupedSlots.containsKey(dlvTimeSlot.getZoneCode())) {
				List<DlvTimeslotModel> _timeSlots = groupedSlots.get(dlvTimeSlot.getZoneCode());
				_timeSlots.add(dlvTimeSlot);
				groupedSlots.put(dlvTimeSlot.getZoneCode(), _timeSlots);
			}
			else {
				List<DlvTimeslotModel> _timeSlots = new ArrayList<DlvTimeslotModel>();
				_timeSlots.add(dlvTimeSlot);
				groupedSlots.put(dlvTimeSlot.getZoneCode(), _timeSlots);
			}
		}		
		return group;
	}
	
	static class TimeslotGroup {
		Map<String, List<DlvTimeslotModel>> groupByZone;
		Set<IRoutingSchedulerIdentity> schedulerIds;
		public Map<String, List<DlvTimeslotModel>> getGroupByZone() {
			return groupByZone;
		}
		public Set<IRoutingSchedulerIdentity> getSchedulerIds() {
			return schedulerIds;
		}
		public void setGroupByZone(Map<String, List<DlvTimeslotModel>> groupByZone) {
			this.groupByZone = groupByZone;
		}
		public void setSchedulerIds(Set<IRoutingSchedulerIdentity> schedulerIds) {
			this.schedulerIds = schedulerIds;
		}				
	}

	private static List<IDeliverySlot> collectRoutingSlots(List<DlvTimeslotModel> dlvTimeSlots) {

		List<IDeliverySlot> _slots = null;		
		if(dlvTimeSlots != null) {
			_slots = new ArrayList<IDeliverySlot>();
			Iterator<DlvTimeslotModel> _itr = dlvTimeSlots.iterator();
			while(_itr.hasNext()) {
				_slots.add(_itr.next().getRoutingSlot());
			}
		}

		return _slots;
	}

	private static void attachWindowMetrics(List<DlvTimeslotModel> dlvTimeSlots
			, List<IDeliveryWindowMetrics> metrics) {

		if(dlvTimeSlots != null && metrics != null
				&& dlvTimeSlots.size() == metrics.size()) {
			DlvTimeslotModel _dlvTimeSlot = null;
			IDeliveryWindowMetrics _metrics = null;

			for (int intCount = 0; intCount < dlvTimeSlots.size(); intCount++) { 
				_dlvTimeSlot = dlvTimeSlots.get(intCount);
				_metrics = metrics.get(intCount);				
				_dlvTimeSlot.getRoutingSlot().setDeliveryMetrics(_metrics);
			}
			
		}
		//return dlvTimeSlots;
	}

	private static void recalculateCapacity(List<DlvTimeslotModel> dlvTimeSlots) {

		DlvTimeslotModel _dlvTimeSlot = null;		
		if(dlvTimeSlots != null) {

			Iterator<DlvTimeslotModel> _itr = dlvTimeSlots.iterator();
			while(_itr.hasNext()) {
				_dlvTimeSlot = _itr.next();	
				if(_dlvTimeSlot.getRoutingSlot() != null) {
					_dlvTimeSlot.getRoutingSlot().setDeliveryMetrics(TimeslotLogic.recalculateCapacity(_dlvTimeSlot));
				}
			}
		}		
		//return _slots;
	}

	private static void email(Date processDate, List<DlvTimeslotModel> dlvTimeSlots
			, String exceptionMsg, boolean isTrialRun) {

		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Routing Capacity Synchronizer Cron : "+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");
			buff.append("<h2>").append("CapacityControllerCronRunner synchronized "+(dlvTimeSlots != null ? dlvTimeSlots.size() : "0")
					+" timeslots for date "+(processDate != null ? dateFormatter.format(processDate) : " date error")
					+ (isTrialRun ? " and is a trail run" : "")).append("</h2>");
			String currentZone = null;
			if(dlvTimeSlots != null) {
				DlvTimeslotModel _dlvTimeSlot = null;	
				Iterator<DlvTimeslotModel> _itr = dlvTimeSlots.iterator();
				buff.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
				buff.append("<tr>").append("<th>").append("ZONE").append("</th>")
				.append("<th>").append("WINDOW START").append("</th>")
				.append("<th>").append("WINDOW END").append("</th>")
				.append("<th>").append("CAPACITY").append("</th>")
				.append("<th>").append("CTCAPACITY").append("</th>")
				.append("<th>").append("ROUTING_CAPACITY").append("</th>")
				.append("<th>").append("ROUTING_CTCAPACITY").append("</th>").append("</tr>");
				while(_itr.hasNext()) {
					_dlvTimeSlot = _itr.next();	
					buff.append("<tr>")
					.append("<td>").append(_dlvTimeSlot.getZoneCode().equalsIgnoreCase(currentZone) ? "" : _dlvTimeSlot.getZoneCode()).append("</td>")
					.append("<td>").append(DateFormat.getTimeInstance().format(_dlvTimeSlot.getStartTimeAsDate())).append("</td>")
					.append("<td>").append(DateFormat.getTimeInstance().format(_dlvTimeSlot.getEndTimeAsDate())).append("</td>")
					.append("<td>").append(_dlvTimeSlot.getCapacity()).append("</td>")
					.append("<td>").append(_dlvTimeSlot.getChefsTableCapacity()).append("</td>");
					if(_dlvTimeSlot.getRoutingSlot() != null && 
							_dlvTimeSlot.getRoutingSlot().getDeliveryMetrics() != null) {
						buff.append("<td>").append(_dlvTimeSlot.getRoutingSlot().getDeliveryMetrics().getOrderCapacity()).append("</td>")
						.append("<td>").append(_dlvTimeSlot.getRoutingSlot().getDeliveryMetrics().getOrderCtCapacity()).append("</td>");
					} else {
						buff.append("<td>").append("ERROR").append("</td>")
						.append("<td>").append("ERROR").append("</td>");
					}
					buff.append("</tr>");
					currentZone = _dlvTimeSlot.getZoneCode();
				}
				buff.append("</table>");
			}
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(RoutingServicesProperties.getRoutingSubscriptionMailFrom(),
					RoutingServicesProperties.getRoutingSubscriptionMailTo(),
					RoutingServicesProperties.getRoutingSubscriptionMailCC(),
					subject, buff.toString(), true, "");
			

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Capacity cron report email: ", e);
		}
	}
}
