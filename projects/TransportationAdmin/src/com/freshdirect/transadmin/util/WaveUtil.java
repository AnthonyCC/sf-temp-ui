package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.transadmin.model.IWaveInstanceSource;
import com.freshdirect.transadmin.model.WaveInstance;
import com.freshdirect.transadmin.service.DispatchManagerI;

public class WaveUtil {
	
	public static List<List<WaveInstance>> getWavesForPublish(Collection sourceData, Date deliveryDate
													, String actionBy, EnumWaveInstancePublishSrc source
													, DispatchManagerI dispManager) {
		
		List<List<WaveInstance>> waveInstancesResult = new ArrayList<List<WaveInstance>>();
		
		List<WaveInstance> waveInstancesToSave = new ArrayList<WaveInstance>();
		List<WaveInstance> waveInstancesToDelete = new ArrayList<WaveInstance>();
		waveInstancesResult.add(waveInstancesToSave);
		waveInstancesResult.add(waveInstancesToDelete);
		
		List<String> zonesForConsolidation = new ArrayList<String>();
		Map<String, List<IWaveInstanceSource>> waveMapping = new HashMap<String, List<IWaveInstanceSource>>();
		
		Collection waveSourceForCalc = null;
		if(sourceData != null) {
			Iterator _itr = sourceData.iterator();
			while(_itr.hasNext()) {
				IWaveInstanceSource instanceSource = (IWaveInstanceSource)_itr.next();
				if(instanceSource.getZone() != null) { 
					if(!waveMapping.containsKey(instanceSource.getZone().getZoneCode())) {
						if(instanceSource.needsConsolidation()) {
							zonesForConsolidation.add(instanceSource.getZone().getZoneCode());
						}
						waveMapping.put(instanceSource.getZone().getZoneCode(), new ArrayList<IWaveInstanceSource>());
					}
					waveMapping.get(instanceSource.getZone().getZoneCode()).add(instanceSource);
				}
			}
		}
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
			
			if(_waveInstItr.hasNext()) {
				_tmpWaveInstance = (IWaveInstanceSource)_waveInstItr.next();
				if(cutOffConsolidation.containsKey(_tmpWaveInstance.getCutOffTime())) {
					_rootWaveInstance = cutOffConsolidation.get(_tmpWaveInstance.getCutOffTime());
					if(_tmpWaveInstance.getStartTime().before(_rootWaveInstance.getStartTime())) {
						_rootWaveInstance.setStartTime(_tmpWaveInstance.getStartTime());
					}
					if(_tmpWaveInstance.getFirstDeliveryTime().before(_rootWaveInstance.getFirstDeliveryTime())) {
						_rootWaveInstance.setFirstDeliveryTime(_tmpWaveInstance.getFirstDeliveryTime());
					}
					if(_tmpWaveInstance.getLastDeliveryTime().after(_rootWaveInstance.getLastDeliveryTime())) {
						_rootWaveInstance.setLastDeliveryTime(_tmpWaveInstance.getLastDeliveryTime());
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
			, String actionBy, EnumWaveInstancePublishSrc source, DispatchManagerI dispManager) {
		
		Collection waveInstances = dispManager.getWaveInstance(deliveryDate, zone);
		Map<WaveInstanceKey, WaveInstance> waveMappingCurrent = new HashMap<WaveInstanceKey, WaveInstance>();
		WaveInstanceKey key = null;
		WaveInstance _tmpWaveInstance = null;

		if(waveInstances != null) {
			Iterator _waveInstItr = waveInstances.iterator();
			// Loop to create the map of WaveInstanceKey and corresponding IWaveInstanceSource (For Current WaveInstances in DB)
			while(_waveInstItr.hasNext()) {
				_tmpWaveInstance = (WaveInstance)_waveInstItr.next();
				key = new WaveInstanceKey();
				key.setDeliveryDate(_tmpWaveInstance.getDeliveryDate());
				key.setDispatchTime(_tmpWaveInstance.getDispatchTime());
				key.setFirstDeliveryTime(_tmpWaveInstance.getFirstDeliveryTime());
				key.setLastDeliveryTime(_tmpWaveInstance.getLastDeliveryTime());
				key.setCutOffTime(_tmpWaveInstance.getCutOffTime());
				key.setZone(_tmpWaveInstance.getArea());

				waveMappingCurrent.put(key, _tmpWaveInstance);
			}
		}

		Map<WaveInstanceKey, List<IWaveInstanceSource>> waveMappingNew = new HashMap<WaveInstanceKey, List<IWaveInstanceSource>>();

		if(waveInstanceSources != null) {
			Iterator _itr = waveInstanceSources.iterator();
			// Loop to create the map of WaveInstanceKey and corresponding IWaveInstanceSource (For New WaveInstances)
			while(_itr.hasNext()) {
				IWaveInstanceSource instanceSource = (IWaveInstanceSource)_itr.next();
				if(instanceSource.isValid()) {
					key = new WaveInstanceKey();
					key.setDeliveryDate(instanceSource.getDeliveryDate());
					key.setDispatchTime(instanceSource.getStartTime());
					key.setFirstDeliveryTime(instanceSource.getFirstDeliveryTime());
					key.setLastDeliveryTime(instanceSource.getLastDeliveryTime());
					key.setCutOffTime(instanceSource.getCutOffTime());
					key.setZone(instanceSource.getZone().getZoneCode());
					if(!waveMappingNew.containsKey(key)) {
						waveMappingNew.put(key, new ArrayList<IWaveInstanceSource>());
					}
					waveMappingNew.get(key).add(instanceSource);
				}
			}
		}

		List<List<WaveInstance>> waveInstancesResult = new ArrayList<List<WaveInstance>>();
		
		List<WaveInstance> waveInstancesToSave = new ArrayList<WaveInstance>();
		List<WaveInstance> waveInstancesToDelete = new ArrayList<WaveInstance>();
		List<WaveInstance> waveInstancesNew = new ArrayList<WaveInstance>();
		
		waveInstancesResult.add(waveInstancesToSave);
		waveInstancesResult.add(waveInstancesToDelete);
		
		for(Map.Entry<WaveInstanceKey, List<IWaveInstanceSource>> waveMppEntry : waveMappingNew.entrySet()) {
			_tmpWaveInstance = waveMappingCurrent.get(waveMppEntry.getKey());
			int noOfResources = 0;
			for(IWaveInstanceSource _instSrc : waveMppEntry.getValue()) {
				noOfResources = noOfResources + _instSrc.getNoOfResources();
			}
			if(_tmpWaveInstance == null) {
				WaveInstance _newWaveInstance = new WaveInstance();
				_newWaveInstance.setArea(waveMppEntry.getKey().getZone());				
				_newWaveInstance.setCutOffTime(waveMppEntry.getKey().getCutOffTime());
				_newWaveInstance.setDeliveryDate(waveMppEntry.getKey().getDeliveryDate());
				_newWaveInstance.setDispatchTime(waveMppEntry.getKey().getDispatchTime());
				_newWaveInstance.setFirstDeliveryTime(waveMppEntry.getKey().getFirstDeliveryTime());
				_newWaveInstance.setLastDeliveryTime(waveMppEntry.getKey().getLastDeliveryTime());

				_newWaveInstance.setModifiedTime(new Date());				
				_newWaveInstance.setChangedBy(actionBy);
				_newWaveInstance.setNoOfResources(noOfResources);
				_newWaveInstance.setSource(source.getName());
				_newWaveInstance.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
				waveInstancesToSave.add(_newWaveInstance);
				waveInstancesNew.add(_newWaveInstance);
			} else {
				waveMappingCurrent.remove(waveMppEntry.getKey());
				if(noOfResources != _tmpWaveInstance.getNoOfResources()) {
					_tmpWaveInstance.setModifiedTime(new Date());				
					_tmpWaveInstance.setChangedBy(actionBy);
					_tmpWaveInstance.setNoOfResources(noOfResources);
					_tmpWaveInstance.setSource(source.getName());
					_tmpWaveInstance.setStatus(EnumWaveInstanceStatus.NOTSYNCHRONIZED);
					waveInstancesToSave.add(_tmpWaveInstance);
				}
			}
		}
		for(Map.Entry<WaveInstanceKey, WaveInstance> waveMppCurrEntry : waveMappingCurrent.entrySet()) {
			waveInstancesToDelete.add(waveMppCurrEntry.getValue());
			if(waveMppCurrEntry.getValue().getReferenceId() != null) {
				if(waveInstancesNew.size() > 0) {
					waveInstancesNew.remove(0).setReferenceId(waveMppCurrEntry.getValue().getReferenceId());
				}
			}
		}
		return waveInstancesResult;
	}
}
