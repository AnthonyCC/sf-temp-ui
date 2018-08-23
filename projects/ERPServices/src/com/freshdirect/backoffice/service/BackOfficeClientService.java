package com.freshdirect.backoffice.service;

import org.apache.log4j.Logger;
import com.freshdirect.ecomm.gateway.AbstractBackOfficeService;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.ResponseResult;

/**
 * 
 * @author smerugu
 *
 */

public class BackOfficeClientService  extends AbstractBackOfficeService implements IBackOfficeClientService {
	private static final Logger LOGGER = LoggerFactory.getInstance(BackOfficeClientService.class);

	private static BackOfficeClientService INSTANCE;
	private static final String SMS_RESPONSE_UPDATE = "/smsresponseupdate";
	public static IBackOfficeClientService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BackOfficeClientService();

		return INSTANCE;
	}

	@Override
	public boolean createCaseByRecievedSmsData(RecievedSmsData recievedSmsData)
			throws FDResourceException {
		boolean isSuccess=false;
		try {
			LOGGER.info("Start:::::createCaseByRecievedSmsData");
			String inputJson = buildRequest(recievedSmsData);
			ResponseResult response = this.postData(inputJson,
					getBackOfficeEndPoint(SMS_RESPONSE_UPDATE),
					ResponseResult.class);
			if(response!=null && response.getStatus().equalsIgnoreCase("SUCCESS")) {
				isSuccess=true;
				LOGGER.info("End:::::createCaseByRecievedSmsData and SMS response has been sent successfully to Backoffice mobile nubmer:"+recievedSmsData.getMobileNumber());
				
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return isSuccess;
	}

}
