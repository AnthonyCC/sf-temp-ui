package com.freshdirect.dataloader.inventory;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.sap.mw.jco.JCO;

import com.freshdirect.fdstore.util.*;

public class BapiErpsInventory implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsInventory.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	public JCO.MetaData[] getStructureMetaData() {
		JCO.MetaData smeta  = new JCO.MetaData("MATLIST");
		smeta.addInfo("MATERIAL",  JCO.TYPE_CHAR,  18,  0, 0);
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_INVENTORY");
		fmeta.addInfo("MATERIALS",	JCO.TYPE_TABLE,	18, 0, 0, 0,	"MATLIST");
		fmeta.addInfo("RETURN",		JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;	
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("execute invoked");
		JCO.Table materialTable = tables.getTable("MATERIALS");

		String[] materials = new String[ materialTable.getNumRows() ];

		LOGGER.debug("material count "+materials.length);

		materialTable.firstRow();
		for (int i = 0; i<materials.length; i++) {
			materials[i] = materialTable.getString("MATERIAL");
			materialTable.nextRow();
		}

		try {
			this.processMaterials(materials);

			output.setValue("S", "RETURN");
		} catch (Exception ex) {
			LOGGER.warn("Failed to process materials", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '"+errorMsg+"'");
			output.setValue(errorMsg, "RETURN");
		}
	}

	protected void processMaterials(String[] materials) throws FDResourceException {

		if (LOGGER.isDebugEnabled()) {
			for (int i = 0; i<materials.length; i++) {
				LOGGER.debug("Got material '"+materials[i]+"'");
			}
		}

		FDIdentity identity = CartFactory.getRandomIdentity();
		LOGGER.info("Got identity "+identity);
		
		FDCartModel cart = CartFactory.createCart(identity);
		LOGGER.info("Cart created");

		cart.setOrderLines( new CartLineFactory().createOrderLines(materials) );
		LOGGER.info("Orderlines created");

		FDCustomerManager.checkAvailability(identity, cart, TIMEOUT);
		LOGGER.info("ATP done");
	}

}
