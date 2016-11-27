package com.freshdirect.routing.handoff.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.ITrailerModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.routing.util.ServiceTimeUtil;

public abstract class AbstractHandOffAction {
	private final static Logger LOGGER = LoggerFactory
			.getInstance(AbstractHandOffAction.class);

	private IHandOffBatch batch;

	private String userId;

	private NumberFormat formatter = new DecimalFormat("00");

	private NumberFormat formatter1 = new DecimalFormat("0000");

	private static final String ERROR_MSG_DISPATCHSTATUSINVALIDCHG = "Dispatches marked as COMPLETE in a previous cutoff cannot be changed to PENDING";
	private static final String ERROR_MSG_NEWINVALIDPDISPATCH = "A new dispatch has been added that is earlier than previously completed dispatches";

	public AbstractHandOffAction(IHandOffBatch batch, String userId) {
		super();
		this.batch = batch;
		this.userId = userId;
	}

	public IHandOffBatch getBatch() {
		return batch;
	}

	public void setBatch(IHandOffBatch batch) {
		this.batch = batch;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static int getRouteIndex(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if (dataLst != null && dataLst.length > 1) {
				return Integer.parseInt(dataLst[1]);
			}
		} catch (Exception e) {
			// do nothing
		}
		return 0;
	}
	
	public static int getRouteIndex1(String search) {
		try {
			String dataLst = StringUtils.substring(search, 4);
			if (dataLst != null) {
				return Integer.parseInt(dataLst);
			}
		} catch (Exception e) {
			// do nothing
		}
		return 0;
	}

	public static int getTrailerIndex(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if (dataLst != null && dataLst.length > 1) {
				return Integer.parseInt(dataLst[1]);
			}
		} catch (Exception e) {
			// do nothing
		}
		return 0;
	}

	public static String splitStringForCode(String search) {
		String[] dataLst = StringUtils.split(search, "-");
		if (dataLst != null && dataLst.length > 0) {
			return dataLst[0];
		} else {
			return "000";
		}
	}

	public static int splitStringForValue(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if (dataLst != null && dataLst.length > 1) {
				return Integer.parseInt(dataLst[1]);
			}
		} catch (Exception e) {
			// do nothing
		}
		return 0;
	}

	public static String getRouteArea(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if (dataLst != null && dataLst.length > 1) {
				return dataLst[0];
			}
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}

	public String formatRouteNumber(int input) {
		return formatter.format(input);
	}

	public String formatTrailerNumber(int input) {
		return formatter1.format(input);
	}

	protected class RouteComparator1 implements Comparator<IHandOffBatchRoute> {

		public int compare(IHandOffBatchRoute obj1, IHandOffBatchRoute obj2) {

			String routeId1 = ((IHandOffBatchRoute) obj1).getRouteId();
			String routeId2 = ((IHandOffBatchRoute) obj2).getRouteId();
			return routeId1.compareTo(routeId2);
		}
	}

	protected class TrailerComparator implements Comparator<ITrailerModel> {

		public int compare(ITrailerModel obj1, ITrailerModel obj2) {

			int trailerId1 = getTrailerIndex(((ITrailerModel) obj1)
					.getTrailerId());
			int trailerId2 = getTrailerIndex(((ITrailerModel) obj2)
					.getTrailerId());
			return trailerId1 - trailerId2;
		}
	}

	protected class TrailerComparator1 implements Comparator<ITrailerModel> {

		public int compare(ITrailerModel obj1, ITrailerModel obj2) {
			if (obj1 != null && obj2 != null && obj1.getDispatchTime() != null
					&& obj2.getDispatchTime() != null
					&& obj1.getTrailerId() != null
					&& obj2.getTrailerId() != null) {
				if (obj1.getDispatchTime().getAsDate()
						.compareTo(obj2.getDispatchTime().getAsDate()) == 0) {
					return obj1.getTrailerId().compareTo(obj2.getTrailerId());
				} else {
					return obj1.getDispatchTime().getAsDate()
							.compareTo(obj2.getDispatchTime().getAsDate());
				}
			}
			return 0;
		}
	}

	protected DispatchCorrelationResult correlateDispatch(
			List<IHandOffBatchRoute> routes, Map<String, IAreaModel> areaLookup)
			throws RoutingServiceException {

		RoutingTimeOfDay rCutOff = new RoutingTimeOfDay(this.getBatch()
				.getCutOffDateTime());
		DispatchCorrelationResult result = new DispatchCorrelationResult();
		RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();

		Map<RoutingTimeOfDay, Integer> cutOffSeqMap = routingInfoProxy.getCutoffSequence();
		
		
		// Map<ZoneCode, Map<DispatchTime, Map<CutOffTime, IWaveInstance>>>
		Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedDispatchTree = null;

		/*
		 * if(RoutingServicesProperties.getRoutingBatchSyncEnabled()) {
		 * Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay,
		 * List<IWaveInstance>>>>> waveInstanceTree = routingInfoProxy
		 * .getWaveInstanceTree(this.getBatch().getDeliveryDate(), null);
		 * 
		 * if(waveInstanceTree != null && waveInstanceTree.keySet().size() > 0)
		 * { plannedDispatchTree =
		 * waveInstanceTree.get(waveInstanceTree.keySet().toArray()[0]); } }
		 * else {
		 */
		plannedDispatchTree = routingInfoProxy.getPlannedDispatchTree(this
				.getBatch().getDeliveryDate());
		// }

		List<IHandOffBatchRoute> mismatchRoutes = new ArrayList<IHandOffBatchRoute>();

		if (routes != null && areaLookup != null && plannedDispatchTree != null) {
			for (IHandOffBatchRoute routeModel : routes) {
				boolean foundWave = false;
				IAreaModel areaModel = areaLookup.get(routeModel.getArea());

				if (areaModel != null) {
					Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispatchMapping = plannedDispatchTree
							.get(routeModel.getArea());

					if (dispatchMapping != null) {
						for (Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : dispatchMapping
								.entrySet()) {
							List<IWaveInstance> waveInstances = dispEntry
									.getValue().get(rCutOff);

							if (waveInstances != null) {
								for (IWaveInstance waveInstance : waveInstances) {

									if (areaModel.isDepot()) {

										if (waveInstance.getDispatchTime() != null
												&& waveInstance
														.getDispatchTime()
														.equals(new RoutingTimeOfDay(
																routeModel
																		.getStartTime()))) {
											foundWave = true;
											routeModel
													.copyWaveProperties(waveInstance);
										}
									} else {
										if (waveInstance.getWaveStartTime() != null
												&& waveInstance
														.getWaveStartTime()
														.equals(new RoutingTimeOfDay(
																routeModel
																		.getStartTime()))
												&& waveInstance
														.getPreferredRunTime() == routeModel
														.getPreferredRunTime()
												&& waveInstance.getMaxRunTime() == routeModel
														.getMaxRunTime()
														
												&& waveInstance.getOriginFacility().equals(routeModel.getOriginId())) {
											foundWave = true;
											routeModel
													.copyWaveProperties(waveInstance);
										}
									}
								}
							}
						}
					}
				}
				if (!foundWave) {
					mismatchRoutes.add(routeModel);
				}
			}
		}

		Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus = new TreeMap<RoutingTimeOfDay, EnumHandOffDispatchStatus>();
		if (plannedDispatchTree != null) {
			for (Map.Entry<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> areaEntry : plannedDispatchTree
					.entrySet()) {
				for (Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : areaEntry
						.getValue().entrySet()) {
					for (Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> cutOffEntry : dispEntry
							.getValue().entrySet()) {
						if (!dispatchStatus.containsKey(dispEntry.getKey())) {
							dispatchStatus.put(dispEntry.getKey(),
									EnumHandOffDispatchStatus.COMPLETE);
						}
						if (RoutingDateUtil.isLaterCutoff(cutOffSeqMap, cutOffEntry.getKey(), rCutOff)) {
							dispatchStatus.put(dispEntry.getKey(),
									EnumHandOffDispatchStatus.PENDING);
						}
					}
				}
			}
		}
		// Description -> Map<DestinatinFacility, Map<DispatchTIme,
		// Map<CutOffTime, IWaveInstance>>>
		Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedTrailerDispatchTree = routingInfoProxy
				.getPlannedTrailerDispatchTree(this.getBatch()
						.getDeliveryDate(), null);
		if (plannedTrailerDispatchTree != null) {
			for (Map.Entry<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> locEntry : plannedTrailerDispatchTree
					.entrySet()) {
				for (Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : locEntry
						.getValue().entrySet()) {
					for (Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> cutOffEntry : dispEntry
							.getValue().entrySet()) {
						if (!dispatchStatus.containsKey(dispEntry.getKey())) {
							dispatchStatus.put(dispEntry.getKey(),
									EnumHandOffDispatchStatus.COMPLETE);
						}
						if (RoutingDateUtil.isLaterCutoff(cutOffSeqMap, cutOffEntry.getKey(), rCutOff)) {
							dispatchStatus.put(dispEntry.getKey(),
									EnumHandOffDispatchStatus.PENDING);
						}
					}
				}
			}
		}
		result.setDispatchStatus(dispatchStatus);
		result.setMismatchRoutes(mismatchRoutes);
		return result;
	}

	protected DispatchCorrelationResult checkRouteMismatch(
			List<IHandOffBatchRoute> routes,
			Map<String, IAreaModel> areaLookup,
			Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> currDispStatus)
			throws RoutingServiceException {

		DispatchCorrelationResult correlationResult = this.correlateDispatch(
				routes, areaLookup);
		if (correlationResult != null
				&& correlationResult.getMismatchRoutes() != null
				&& correlationResult.getMismatchRoutes().size() > 0) {
			StringBuffer errorBuf = new StringBuffer();
			for (IHandOffBatchRoute mismatchRoute : correlationResult
					.getMismatchRoutes()) {
				if (errorBuf.length() > 0) {
					errorBuf.append(",");
				}
				errorBuf.append(mismatchRoute.getRouteId()).append(
						mismatchRoute.getRoutingRouteId());
			}
			throw new RoutingServiceException("Plan Mismatch "
					+ errorBuf.toString(), null,
					IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		// APPDEV-1801 HandOff Batch Dispatch Time Validations
		if (RoutingServicesProperties
				.getRoutingBatchDispStatusValidationEnabled()) {
			validateDispatchStatusChange(currDispStatus,
					correlationResult.getDispatchStatus());
		}
		return correlationResult;
	}

	protected void validateDispatchStatusChange(
			Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> currDispStatus,
			Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> newDispStatus) {

		RoutingTimeOfDay currMostRecentComplete = null;

		if (currDispStatus != null && newDispStatus != null) {
			for (Map.Entry<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchEntry : currDispStatus
					.entrySet()) {
				if (dispatchEntry.getValue().equals(
						EnumHandOffDispatchStatus.COMPLETE)) {
					currMostRecentComplete = dispatchEntry.getKey();
					break;
				}
			}

			for (Map.Entry<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchEntry : newDispStatus
					.entrySet()) {
				EnumHandOffDispatchStatus previousStatus = null;
				if (currDispStatus.get(dispatchEntry.getKey()) != null) {
					previousStatus = currDispStatus.get(dispatchEntry.getKey());
				}
				if (previousStatus != null) {
					if (EnumHandOffDispatchStatus.COMPLETE
							.equals(previousStatus)
							&& (dispatchEntry.getValue() == null || !EnumHandOffDispatchStatus.COMPLETE
									.equals(dispatchEntry.getValue()))) {
						throw new RoutingServiceException(
								ERROR_MSG_DISPATCHSTATUSINVALIDCHG, null,
								IIssue.PROCESS_HANDOFFBATCH_ERROR);
					}
				} else {
					// This is a new dispatch being added
					if (currMostRecentComplete != null
							&& currMostRecentComplete.after(dispatchEntry
									.getKey())) {
						throw new RoutingServiceException(
								ERROR_MSG_NEWINVALIDPDISPATCH, null,
								IIssue.PROCESS_HANDOFFBATCH_ERROR);
					}
				}
			}
		}
	}

	protected OrderEstimationResult getEstimateOrderSize(IServiceTimeScenarioModel scenario, IPackagingModel tmpPackageModel) {
		OrderEstimationResult result = new OrderEstimationResult();
		if(scenario != null) {
			double cartonCount = scenario.getDefaultCartonCount(); 
			double freezerCount = scenario.getDefaultFreezerCount();
			double caseCount = scenario.getDefaultCaseCount();
			boolean isDefault = true;
			
			if(tmpPackageModel != null) {
				cartonCount = tmpPackageModel.getNoOfCartons(); 
				freezerCount = tmpPackageModel.getNoOfFreezers();
				caseCount = tmpPackageModel.getNoOfCases();
			
				isDefault = (cartonCount == 0 && freezerCount == 0 && caseCount == 0);								
				tmpPackageModel.setSource(isDefault ? EnumOrderMetricsSource.DEFAULT : EnumOrderMetricsSource.ACTUAL);
			}
			result.setPackagingModel(tmpPackageModel);
			result.setCalculatedOrderSize(ServiceTimeUtil.evaluateExpression(scenario.getOrderSizeFormula()
																					, ServiceTimeUtil.getServiceTimeFactorParams(tmpPackageModel)));
		}
		return result;
	}
	
	public Object execute() {
		long startTime = System.currentTimeMillis();
		try {
			Object result = doExecute();
			long endTime = System.currentTimeMillis();
			LOGGER.info("HandOffAction " + this.getClass().getName()
					+ " completed in" + ((endTime - startTime) / 60) + " secs");
			return result;
		} catch (Exception exp) {
			HandOffServiceProxy proxy = new HandOffServiceProxy();

			try {
				if (getFailureStatus() != null) {

					proxy.updateHandOffBatchStatus(
							this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateHandOffBatchMessage(this.getBatch()
							.getBatchId(), decodeErrorMessage(exp));
				}
			} catch (RoutingServiceException e) {
				LOGGER.error("Failure to update Handoff batch status", e);
			}

			throw new RoutingServiceException(exp,
					IIssue.PROCESS_HANDOFFBATCH_ERROR);
		} catch (Throwable e) {
			LOGGER.error("something really weird occured", e);
			throw new RoutingServiceException(new Exception(e),
					IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
	}

	protected static String decodeErrorMessage(Exception exp) {
		String strErrMessage = exp.getMessage() != null ? exp.getMessage() : "";
		if (strErrMessage == null || strErrMessage.trim().length() == 0) {
			if (exp instanceof RoutingProcessException) {
				strErrMessage = ((RoutingProcessException) exp)
						.getIssueMessage();
			}
		}
		if (strErrMessage == null || strErrMessage.trim().length() == 0) {
			strErrMessage = exp.toString();
		}
		return strErrMessage;
	}

	class DispatchCorrelationResult {

		List<IHandOffBatchRoute> mismatchRoutes;
		// DispatchTime vs DispatchStatus
		Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus = null;

		public List<IHandOffBatchRoute> getMismatchRoutes() {
			return mismatchRoutes;
		}

		public Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getDispatchStatus() {
			return dispatchStatus;
		}

		public void setMismatchRoutes(List<IHandOffBatchRoute> mismatchRoutes) {
			this.mismatchRoutes = mismatchRoutes;
		}

		public void setDispatchStatus(
				Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus) {
			this.dispatchStatus = dispatchStatus;
		}
	}

	public abstract Object doExecute() throws Exception;

	public abstract EnumHandOffBatchStatus getFailureStatus();

}
