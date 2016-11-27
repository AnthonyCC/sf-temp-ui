package com.freshdirect.sap.command;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.bapi.BapiAbapException;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiFunctionI;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.ejb.SapException;

public abstract class SapCommandSupport implements SapCommandI {

	private final static Category LOGGER = LoggerFactory.getInstance(SapCommandSupport.class);

	protected void invoke(BapiFunctionI bapi) throws SapException {

		try {

			bapi.execute();

			BapiInfo[] bapiInfos = bapi.getInfos();

			boolean success = true;
			boolean sapShutingdown = false;
			for (int i = 0; i < bapiInfos.length; i++) {
				BapiInfo bi = bapiInfos[i];
				if (BapiInfo.LEVEL_ERROR == bi.getLevel()) {
					success = false;
				}
				
				if(BapiInfo.LEVEL_INFO == bi.getLevel()) {
					if("ZWAVE/000".equals(bi.getCode()) && bi.getMessage().indexOf("PLEASE RETRY") >= 0){
						sapShutingdown = true;
					}
				}
			}
			
			if(sapShutingdown) {
				throw new RuntimeException("SAP is Shutting down");
			}
			

			if (!success) {

				StringBuffer buf = new StringBuffer("BAPI task failed in SAP. Return structure:");
				for (int i = 0; i < bapiInfos.length; i++) {
					buf.append('\n').append(bapiInfos[i].toString());
				}

				throw new SapException(buf.toString());
			}

		} catch (BapiAbapException ex) {
			LOGGER.warn("Failed to execute BAPI task", ex);
			throw new SapException("BAPI task failed: " + ex.getMessage());

		} catch (BapiException ex) {
			// there was a communication problem, the message should be repeated later
			LOGGER.warn("Critical message found with communication error, throwing RuntimeException", ex);
			throw new RuntimeException("Critical message found with communication error");
		}
	}

}
