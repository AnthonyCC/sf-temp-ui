package com.freshdirect.webapp.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.freshdirect.logistics.delivery.dto.ScrubbedAddress;

/**
 * @author Aniwesh Vatsal
 *
 */
public class AddressScrubbingProcessor {

	/**
	 * @return
	 */
	public Future processCSV(Map<String,ScrubbedAddress> addressInputMap, List<ScrubbedAddress> csvAddrList){
		AddressScrubbingTask scrubbingTask = new AddressScrubbingTask(); 
		scrubbingTask.setAddressInputMap(addressInputMap);
		scrubbingTask.setCsvAddrList(csvAddrList);
		Future future = FeatureTaskThreadPool.getThreadPool().submit(scrubbingTask); 
		return future;
	}
}
