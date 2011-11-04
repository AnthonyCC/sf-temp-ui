package com.freshdirect.dataloader.inventory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.erp.ejb.ErpInventoryManagerHome;
import com.freshdirect.erp.ejb.ErpInventoryManagerSB;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.JCO;

public class BapiRestrictedAvailabilityChange implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance(BapiRestrictedAvailabilityChange.class);

	public JCO.MetaData[] getStructureMetaData() {
		JCO.MetaData metaMatList = new JCO.MetaData("RESTRICTAVAILABILITYLIST");
		metaMatList.addInfo("MATNR", JCO.TYPE_CHAR, 18, 0, 0);
		metaMatList.addInfo("DATE", JCO.TYPE_DATE, 8, 18, 0);
		metaMatList.addInfo("INDIC", JCO.TYPE_CHAR, 1, 26, 0); // 13.3 BCD, length 7

		return new JCO.MetaData[] { metaMatList };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZSD_RESTRICT_ATP");
		fmeta.addInfo("Z_RESTRICT_ATP", JCO.TYPE_TABLE, 18 + 8 + 1, 0, 0, 0, "RESTRICTAVAILABILITYLIST");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("execute invoked");
		JCO.Table restrictedTable = tables.getTable("Z_RESTRICT_ATP");

		restrictedTable.firstRow();
		Set<ErpRestrictedAvailabilityModel> restrictedInfos = new HashSet<ErpRestrictedAvailabilityModel>();
		Set<String> materialsDeleted = new HashSet<String>();
		List<List<String>> rawData = new ArrayList<List<String>>();
		List<String> tmpRawRowData = null;


		for (int i = 0; i < restrictedTable.getNumRows(); i++) {
			String matNo = restrictedTable.getString("MATNR");
			Date resDate = restrictedTable.getDate("DATE");
			String delIndicator = restrictedTable.getString("INDIC").trim();

			LOGGER.debug(matNo + "\t" + resDate + "\t" + delIndicator);
			
			tmpRawRowData = new ArrayList<String>();
			tmpRawRowData.add(matNo);
			tmpRawRowData.add(resDate.toString());
			tmpRawRowData.add(delIndicator);
			
			rawData.add(tmpRawRowData);

			if("X".equals(delIndicator)){
				materialsDeleted.add(matNo);
			} else {
				ErpRestrictedAvailabilityModel model = new ErpRestrictedAvailabilityModel(matNo, resDate, false);
				restrictedInfos.add(model);
				materialsDeleted.add(matNo);
			}

			restrictedTable.nextRow();
		}

		try {
			LOGGER.debug("Storing inventories");

			this.updateRestrictedInfos(restrictedInfos, materialsDeleted);

			output.setValue("S", "RETURN");
			output.setValue("It's all good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store inventories", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}finally {
			if(FDStoreProperties.isDumpGroupExportEnabled()) {
				generateInputDumpFile(rawData);
			}
		}

	}

	public boolean generateInputDumpFile(List<List<String>> rawData) {
		StringBuffer strBuf = new StringBuffer();
		for(List<String> row : rawData) {
			boolean isFirst = true;
			for(String cell : row) {
				if(!isFirst) {
					strBuf.append(",");
				}
				strBuf.append(cell);
				isFirst = false;
			}
			strBuf.append("\n");
		}
        try {
        	
        	BufferedWriter out = new BufferedWriter(new FileWriter("RESTRICTED_AVAILABILITY_EXPORT_"+System.currentTimeMillis()+".csv"));
            out.write(strBuf.toString());
            out.close();

            return true;
        } catch (Exception ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}
	
	private void updateRestrictedInfos(Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials)
		throws NamingException, EJBException, CreateException, FDResourceException, RemoteException {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpInventoryManagerHome mgr = (ErpInventoryManagerHome) ctx.lookup("freshdirect.erp.InventoryManager");
			ErpInventoryManagerSB sb = mgr.create();

			sb.updateRestrictedInfos(restrictedInfos, deletedMaterials);
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
