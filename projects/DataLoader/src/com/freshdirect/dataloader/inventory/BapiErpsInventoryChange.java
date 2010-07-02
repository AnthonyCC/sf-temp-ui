package com.freshdirect.dataloader.inventory;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.erp.ejb.ErpInventoryManagerHome;
import com.freshdirect.erp.ejb.ErpInventoryManagerSB;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.JCO;

public class BapiErpsInventoryChange implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance(BapiErpsInventoryChange.class);

	public JCO.MetaData[] getStructureMetaData() {
		JCO.MetaData metaMatList = new JCO.MetaData("MATLIST");
		metaMatList.addInfo("MATNR", JCO.TYPE_CHAR, 18, 0, 0);
		metaMatList.addInfo("EDATU", JCO.TYPE_DATE, 8, 18, 0);
		metaMatList.addInfo("BMENG", JCO.TYPE_BCD, 7, 18 + 8, 3); // 13.3 BCD, length 7
		metaMatList.addInfo("MEINS", JCO.TYPE_CHAR, 3, 18 + 8 + 7, 0);

		return new JCO.MetaData[] { metaMatList };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_INVENTORY_CHANGE");
		fmeta.addInfo("MATERIALS", JCO.TYPE_TABLE, 18 + 8 + 7 + 3, 0, 0, 0, "MATLIST");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("execute invoked");
		JCO.Table materialTable = tables.getTable("MATERIALS");

		materialTable.firstRow();

		// Map of String material -> List of entries
		Map<String, List> matEntries = new HashMap<String, List>(materialTable.getNumRows());
		for (int i = 0; i < materialTable.getNumRows(); i++) {
			String matNo = materialTable.getString("MATNR");
			Date startDate = materialTable.getDate("EDATU");
			double commitedQty = materialTable.getDouble("BMENG");
			String salesUnit = materialTable.getString("MEINS");

			LOGGER.debug(matNo + "\t" + startDate + "\t" + commitedQty + "\t" + salesUnit);

			List<ErpInventoryEntryModel> entries = matEntries.get(matNo);
			if (entries == null) {
				entries = new ArrayList<ErpInventoryEntryModel>();
				matEntries.put(matNo, entries);
			}
			entries.add(new ErpInventoryEntryModel(startDate, commitedQty));

			materialTable.nextRow();
		}

		// build inventories
		List<ErpInventoryModel> inventories = new ArrayList<ErpInventoryModel>();
		Date now = new Date();
		for (Object element : matEntries.entrySet()) {
			Map.Entry e = (Map.Entry) element;
			String matNo = (String) e.getKey();
			List entries = (List) e.getValue();
			inventories.add(new ErpInventoryModel(matNo, now, entries));
		}

		try {
			LOGGER.debug("Storing inventories");

			this.updateInventories(inventories);

			output.setValue("S", "RETURN");
			output.setValue("It's all good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store inventories", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}

	}

	private void updateInventories(List<ErpInventoryModel> inventories)
		throws NamingException, EJBException, CreateException, FDResourceException, RemoteException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpInventoryManagerHome mgr = (ErpInventoryManagerHome) ctx.lookup("freshdirect.erp.InventoryManager");
			ErpInventoryManagerSB sb = mgr.create();

			sb.updateInventories(inventories);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
	}

}
