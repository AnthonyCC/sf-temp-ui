package com.freshdirect.dataloader.sap.jco;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.sap.mw.jco.JCO;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.bapi.BapiFunctionI;

public class BapiErpsBatch implements BapiFunctionI {

	private static Category LOGGER = LoggerFactory.getInstance( BapiErpsBatch.class );

	private SapBatchListenerI listener;

	public BapiErpsBatch(SapBatchListenerI listener) {
		this.listener = listener;	
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_BATCH");
		fmeta.addInfo("DESTINATION",	JCO.TYPE_CHAR, 128, 0, 0,	JCO.IMPORT_PARAMETER, null);
		fmeta.addInfo("PREFIX",			JCO.TYPE_CHAR, 128, 0, 0,	JCO.IMPORT_PARAMETER, null);
		fmeta.addInfo("RETURN",			JCO.TYPE_CHAR, 255, 0, 0,	JCO.EXPORT_PARAMETER, null);
		return fmeta;	
	}

	public JCO.MetaData[] getStructureMetaData() {
		return null;	
	}
	
	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		String destination = input.getString("DESTINATION");
		String prefix = input.getString("PREFIX");
		String firstLine = "";
		try {

			this.listener.processErpsBatch(destination, prefix);
			output.setValue("S", "RETURN");

		} catch (LoaderException le) {
			LOGGER.warn("Error occured processing batch", le);

			BadDataException[] bdes = le.getBadDataExceptions();
			if (bdes!=null) {
				for (int i=0; i<bdes.length; i++) {
					LOGGER.warn("Nested bad data exception", bdes[i]);
					if(i == 0) {
						if(bdes[0] != null)
							firstLine = bdes[0].toString();
					}
				}
			}
			
			// !!! make BDEs part of the SAP error msg

			String errorMsg = ( le.getNestedException()==null ? le : le.getNestedException() ).toString();
			String returnErr = firstLine + ": " + errorMsg;
			returnErr = returnErr.substring(0, Math.min(255, returnErr.length()));
			LOGGER.info("Error message to SAP: '"+returnErr+"'");
			output.setValue(returnErr, "RETURN");
		}
	}
}

