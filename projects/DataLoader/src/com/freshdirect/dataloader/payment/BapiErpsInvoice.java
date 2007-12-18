package com.freshdirect.dataloader.payment;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.bapi.BapiFunctionI;

import com.sap.mw.jco.JCO;

public class BapiErpsInvoice implements BapiFunctionI {

	private static Category LOGGER = LoggerFactory.getInstance( BapiErpsInvoice.class );

	private InvoiceBatchListenerI listener;

	public BapiErpsInvoice(InvoiceBatchListenerI listener) {
		this.listener = listener;
	}

	/**
	 * @see com.freshdirect.dataloader.bapi.BapiFunctionI#getFunctionMetaData()
	 */
	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_INVOICE");
		fmeta.addInfo("FOLDER",		JCO.TYPE_CHAR, 128, 0, 0,	JCO.IMPORT_PARAMETER, null);
		fmeta.addInfo("FILENAME",	JCO.TYPE_CHAR, 128, 0, 0,	JCO.IMPORT_PARAMETER, null);
		fmeta.addInfo("RETURN",		JCO.TYPE_CHAR, 5000, 0, 0,		JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	/**
	 * @see com.freshdirect.dataloader.bapi.BapiFunctionI#getStructureMetaData()
	 */
	public JCO.MetaData[] getStructureMetaData() {
		return null;
	}

	/**
	 * @see com.freshdirect.dataloader.bapi.BapiFunctionI#execute(ParameterList, ParameterList, ParameterList)
	 */
	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		String folder = input.getString("FOLDER");
		String fileName = input.getString("FILENAME");
		try {

			this.listener.processInvoiceBatch(folder, fileName);
			output.setValue("S", "RETURN");

		} catch (LoaderException le) {
			LOGGER.warn("Error occured processing batch", le);
			String errorMsg = ( le.getNestedException()==null ? le : le.getNestedException() ).toString();
			errorMsg = errorMsg.substring(0, Math.min(5000, errorMsg.length()));
			LOGGER.info("Error message to SAP: '"+errorMsg+"'");
			output.setValue(errorMsg, "RETURN");
		}
	}

}
