package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.transadmin.model.IWaveInstanceSource;
import com.freshdirect.transadmin.model.WaveInstance;
import com.freshdirect.transadmin.model.WaveInstancePublish;
import com.freshdirect.transadmin.service.DispatchManagerI;

@SuppressWarnings("rawtypes")
public class WaveUtil {	
	
	public static List<List<WaveInstance>> getWavesForPublish(Collection sourceData, Date deliveryDate
													, String actionBy, EnumWaveInstancePublishSrc source
													, DispatchManagerI dispManager) {
		
		List<List<WaveInstance>> waveInstancesResult = new ArrayList<List<WaveInstance>>();
		
		List<WaveInstance> waveInstancesToSave = new ArrayList<WaveInstance>();
		List<WaveInstance> waveInstancesToDelete = new ArrayList<WaveInstance>();
		waveInstancesResult.add(waveInstancesToSave);
		waveInstancesResult.add(waveInstancesToDelete);
		
		Set<String> zonesForConsolidation = new HashSet<String>();
		Map<String, List<IWaveInstanceSource>> waveMapping = new HashMap<String, List<IWaveInstanceSource>>();
		
		Collection waveSourceForCalc = null;
		// Group Wave Instance Sources By Zone
		if(sourceData != null) {
			Iterator _itr = sourceData.iterator();
			while(_itr.hasNext()) {
				IWaveInstanceSource instanceSource = (IWaveInstanceSource)_itr.next();
				if(instanceSource.getZone() != null && 
						(instanceSource.getDestinationFacility()==null || 
						(instanceSource.getDestinationFacility()!=null && instanceSource.getDestinationFacility().getTrnFacilityType()!=null && 
						!EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(instanceSource.getDestinationFacility().getTrnFacilityType().getName())))) { 
					if(instanceSource.needsConsolidation()) {
						zonesForConsolidation.add(instanceSource.getZone().getZoneCode());
					}
					if(!waveMapping.containsKey(instanceSource.getZone().getZoneCode())) {						
						waveMapping.put(instanceSource.getZone().getZoneCode(), new ArrayList<IWaveInstanceSource>());
					}
					waveMapping.get(instanceSource.getZone().getZoneCode()).add(instanceSource);
				}
			}
		}
		// Calculate and consolidate Wave Instance as needed
		for(Map.Entry<String, List<IWaveInstanceSource>> waveByZone: waveMapping.entrySet()) {	
			waveSourceForCalc = waveByZone.getValue();
			if(zonesForConsolidation.contains(waveByZone.getKey())) {
				waveSourceForCalc = consolidateWaveInstance(waveSourceForCalc);
			}
			List<List<WaveInstance>> _tmpResult = calculateWaveInstance(waveSourceForCalc, deliveryDate, waveByZone.getKey()
																			, actionBy, source, dispManager);
			waveInstancesToSave.addAll(_tmpResult.get(0));
			waveInstancesToDelete.addAll(_tmpResult.get(1));
		}
		return waveInstancesResult;
	}
	
	

	private static List<IWaveInstanceSource> consolidateWaveInstance(Collection waveInstanceSources) {
		List<IWaveInstanceSource> result = null;
		if(waveInstanceSources != null) {
			result = new ArrayList<IWaveInstanceSource>();
			
			Map<Date, IWaveInstanceSource> cutOffConsolidation = new HashMap<Date, IWaveInstanceSource>();
			Iterator _waveInstItr = waveInstanceSources.iterator();
			IWaveInstanceSource _tmpWaveInstance = null;
			IWaveInstanceSource _rootWaveInstance = null;
			
			while(_waveInstItr.hasNext()) {
				_tmpWaveInstance = (IWaveInstanceSource)_waveInstItr.next();
				if(cutOffConsolidation.containsKey(_tmpWaveInstance.getCutOffTime())) {
					_rootWaveInstance = cutOffConsolidation.get(_tmpWaveInstance.getCutOffTime());
					if(_tmpWaveInstance.getStartTime().before(_rootWaveInstance.getStartTime())) {
						_rootWaveInstance.setStartTime(_tmpWaveInstance.getStartTime());
					}
					if(_tmpWaveInstance.getEndTime().after(_rootWaveInstance.getEndTime())) {
						_rootWaveInstance.setEndTime(_tmpWaveInstance.getEndTime());
					}
					if(_tmpWaveInstance.getNoOfResources() > _rootWaveInstance.getNoOfResources()) {
						_rootWaveInstance.setNoOfResources(_tmpWaveInstance.getNoOfResources());
					}
				} else {
					cutOffConsolidation.put(_tmpWaveInstance.getCutOffTime(), _tmpWaveInstance);
				}
			}
			for(Map.Entry<Date, IWaveInstanceSource> cutOffEntry : cutOffConsolidation.entrySet()) {
				result.add(cutOffEntry.getValue());
			}
		}
		return result;
	}
	
	private static List<List<WaveInstance>> calculateWaveInstance(Collection waveInstanceSources
													, Date deliveryDate, String zone
													, String actionBy, EnumWaveInstancePublishSrc source
													, DispatchManagerI dispManager) {
		
		Collection waveInstances = dispManager.getWaveInstance(deliveryDate, zone);
		Map<WaveInstanceKey, WaveInstance> waveMappingCurrent = new HashMap<WaveInstanceKey, WaveInstance>();
		Set<WaveInstanceKey> foundCurrentKeys = new HashSet<WaveInstanceKey>();
		Map<WaveInstanceKey, WaveInstance> waveMappingOrphans = new HashMap<WaveInstanceKey, WaveInstance>();
		
		WaveInstanceKey key = null;
		WaveInstance _tmpWaveInstance = null;

		if(waveInstances != null) {
			Iterator<WaveInstance> _waveInstItr = waveInstances.iterator();
			// Loop to create the map of WaveInstanceKey and corresponding IWaveInstanceSource (For Current WaveInstances in DB)
			while(_waveInstItr.hasNext()) {
				_tmpWaveInstance = _waveInstItr.next();
				key = new WaveInstanceKey();
				key.setDeliveryDate(_tmpWaveInstance.getDeliveryDate());
				key.setDispatchTime(_tmpWaveInstance.getDispatchTime());
				key.setEndTime(_tmpWaveInstance.getEndTime());
				key.setMaxTime(_tmpWaveInstance.getMaxTime());
				key.setCutOffTime(_tmpWaveInstance.getCutOffTime());
				key.setZone(_tmpWaveInstance.getArea());
				key.setOriginFacility(_tmpWaveInstance.getOriginFacility());
				key.setDestinationFacility(_tmpWaveInstance.getDestinationFacility());
				key.setEquipmentType(_tmpWaveInstance.getEquipmentType());
				waveMappingCurrent.put(key, _tmpWaveInstance);
			}
		}

		Map<WaveInstanceKey, Integer> waveMappingNew = new HashMap<WaveInstanceKey, Integer>();

		if(waveInstanceSources != null) {
			Iterator<IWaveInstanceSource> _itr = waveInstanceSources.iterator();
			// Loop to create the map of WaveInstanceKey and corresponding IWaveInstanceSource (For New WaveInstances)
			while(_itr.hasNext()) {
				IWaveInstanceSource instanceSource = _itr.next();
				if(instanceSource.isValidSource()) {
					key = new WaveInstanceKey();
					key.setDeliveryDate(instanceSource.getDeliveryDate());
					key.setDispatchTime(instanceSource.getStartTime());
					key.setEndTime(instanceSource.getEndTime());
					key.setMaxTime(instanceSource.getMaxReturnTime());
					key.setCutOffTime(instanceSource.getCutOffTime());
					key.setZone(instanceSource.getZone().getZoneCode());
					key.setOriginFacility(instanceSource.getOriginFacility());
					key.setEquipmentType(instanceSource.getEquipmentTypeS());
					
					int initialResource = 0;
					if(waveMappingNew.containsKey(key)) {
						initialResource = initialResource + waveMappingNew.get(key);
					}
					waveMappingNew.put(key, initialResource + instanceSource.getNoOfResources1());
					if(waveMappingCurrent.containsKey(key)) {
						foundCurrentKeys.add(key);
					}
				}
			}
		}
		// Find me Orphans Please
		for(Map.Entry<WaveInstanceKey, WaveInstance> waveMppEntry : waveMappingCurrent.entrySet()) {
			if(!foundCurrentKeys.contains(waveMppEntry.getKey())) {
				waveMappingOrphans.put(waveMppEntry.getKey(), waveMppEntry.getValue());
			}
		}

		List<List<WaveInstance>> waveInstancesResult = new ArrayList<List<WaveInstance>>();
		
		List<WaveInstance> waveInstancesToSave = new ArrayList<WaveInstance>();
		List<WaveInstance> waveInstancesToDelete = new ArrayList<WaveInstance>();
				
		waveInstancesResult.add(waveInstancesToSave);
		waveInstancesResult.add(waveInstancesToDelete);
				
		for(Map.Entry<WaveInstanceKey, Integer> waveMppEntry : waveMappingNew.entrySet()) {
			_tmpWaveInstance = waveMappingCurrent.get(waveMppEntry.getKey());
			if(_tmpWaveInstance == null) {
				WaveInstance _tmpOrphan = null;
				int intCount = 0;
				while(waveMappingOrphans.keySet().size() > intCount) {
					_tmpOrphan = waveMappingOrphans.get(waveMappingOrphans.keySet().toArray()[intCount]);
					if(_tmpOrphan != null 
								&& _tmpOrphan.getCutOffTime() != null 
									&& _tmpOrphan.getCutOffTime().equals(waveMppEntry.getKey().getCutOffTime())) {
						_tmpWaveInstance = waveMappingOrphans.remove(waveMappingOrphans.keySet().toArray()[intCount]);
						break;//Found an orphan break
					} else {
						intCount++;
					}					
				}
			}
			if(_tmpWaveInstance == null) {
				_tmpWaveInstance = new WaveInstance();						
			} 
			_tmpWaveInstance.setOriginFacility(waveMppEntry.getKey().getOriginFacility());
			_tmpWaveInstance.setArea(waveMppEntry.getKey().getZone());				
			_tmpWaveInstance.setCutOffTime(waveMppEntry.getKey().getCutOffTime());
			_tmpWaveInstance.setDeliveryDate(waveMppEntry.getKey().getDeliveryDate());
			_tmpWaveInstance.setDispatchTime(waveMppEntry.getKey().getDispatchTime());
			_tmpWaveInstance.setEndTime(waveMppEntry.getKey().getEndTime());
			_tmpWaveInstance.setMaxTime(waveMppEntry.getKey().getMaxTime());
			_tmpWaveInstance.setEquipmentType(waveMppEntry.getKey().getEquipmentType());
					
			_tmpWaveInstance.setModifiedTime(new Date());				
			_tmpWaveInstance.setChangedBy(actionBy);
			_tmpWaveInstance.setNoOfResources(waveMppEntry.getValue());
			_tmpWaveInstance.setSource(source.getName());
			_tmpWaveInstance.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
			_tmpWaveInstance.setTodrestriction(waveMppEntry.getKey().getOriginFacility().getTodrestriction());
			waveInstancesToSave.add(_tmpWaveInstance);
		}
		for(Map.Entry<WaveInstanceKey, WaveInstance> waveMppCurrEntry : waveMappingOrphans.entrySet()) {
			_tmpWaveInstance = waveMppCurrEntry.getValue();
			if(_tmpWaveInstance.getReferenceId() != null) {
				_tmpWaveInstance.setModifiedTime(new Date());				
				_tmpWaveInstance.setChangedBy(actionBy);
				_tmpWaveInstance.setNoOfResources(0); // 0 out capacity for already linked orphans
				_tmpWaveInstance.setSource(source.getName());
				_tmpWaveInstance.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
				waveInstancesToSave.add(_tmpWaveInstance);
			} else {
				waveInstancesToDelete.add(_tmpWaveInstance);
			}
		}
		return waveInstancesResult;
	}
	
	public static void recalculateWave(DispatchManagerI dispatchManagerService, Map<Date, Set<String>> deliveryMapping
			, String userId, EnumWaveInstancePublishSrc source) {
		List<WaveInstance> saveWaveInstances = new ArrayList<WaveInstance>();
		List<WaveInstance> deleteWaveInstances  = new ArrayList<WaveInstance>();
		
		if(deliveryMapping != null) {
			for(Map.Entry<Date, Set<String>> deliveryMapEntry : deliveryMapping.entrySet()) {
				Collection wavePublishes = dispatchManagerService.getWaveInstancePublish(deliveryMapEntry.getKey());
				if(wavePublishes != null && wavePublishes.size() > 0) {
					WaveInstancePublish wavePublish = (WaveInstancePublish)wavePublishes.iterator().next();
					if(wavePublish.getSource() != null && wavePublish.getSource().equals(source)) {
						if(deliveryMapEntry.getKey() != null && deliveryMapEntry.getValue() != null) {
							Collection dataToProcess = null;
							for(String zone: deliveryMapEntry.getValue()) {
								if(source.equals(EnumWaveInstancePublishSrc.PLAN)) {
									dataToProcess = dispatchManagerService.getPlan(deliveryMapEntry.getKey(), zone);
								} else {
									dataToProcess = dispatchManagerService.getScrib(deliveryMapEntry.getKey(), zone);
								}
								List<List<WaveInstance>> waveInstancesResult = WaveUtil.getWavesForPublish(dataToProcess
																				, deliveryMapEntry.getKey()
																				, userId, source
																				, dispatchManagerService);
								if(waveInstancesResult.get(1).size() > 0) {
									deleteWaveInstances.addAll(waveInstancesResult.get(1));
								}
								if(waveInstancesResult.get(0).size() > 0) {
									saveWaveInstances.addAll(waveInstancesResult.get(0));
								}
							}
						}						
					}
				}
			}
			dispatchManagerService.saveWaveInstances(saveWaveInstances, deleteWaveInstances);
		}
	}
	
	public static void recalculateWave(DispatchManagerI dispatchManagerService, IWaveInstanceSource previousModel
										, IWaveInstanceSource currentModel
										, EnumWaveInstancePublishSrc source, String userId) {

		Map<Date, Set<String>> deliveryMapping = new HashMap<Date, Set<String>>();
		if(previousModel != null && previousModel.getZone() != null && !DispatchPlanUtil.isShuttlePlan(previousModel)) {
			if(!deliveryMapping.containsKey(previousModel.getDeliveryDate())) {
				deliveryMapping.put(previousModel.getDeliveryDate(), new HashSet<String>());
			}
			deliveryMapping.get(previousModel.getDeliveryDate()).add(previousModel.getZone().getZoneCode());
		}
		if(currentModel != null && currentModel.getZone() != null && !DispatchPlanUtil.isShuttlePlan(currentModel)) {
			if(!deliveryMapping.containsKey(currentModel.getDeliveryDate())) {
				deliveryMapping.put(currentModel.getDeliveryDate(), new HashSet<String>());
			}
			deliveryMapping.get(currentModel.getDeliveryDate()).add(currentModel.getZone().getZoneCode());
		}		
		WaveUtil.recalculateWave(dispatchManagerService, deliveryMapping,  userId, source);
	}
}
