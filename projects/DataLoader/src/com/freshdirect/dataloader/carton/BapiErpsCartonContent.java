package com.freshdirect.dataloader.carton;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.sap.mw.jco.JCO;

import com.freshdirect.framework.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class BapiErpsCartonContent implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsCartonContent.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	static JCO.MetaData smeta = null;
	static int totalSize = 0;
	static {
		DataStructure[] sapData = new DataStructure[] {
						    //NAME      JCO_TYPE     LEN DEC DESCRIPTION
			new DataStructure("BSTNK", JCO.TYPE_CHAR, 20, 0, "Web order number"),
			new DataStructure("VBELN", JCO.TYPE_CHAR, 10, 0, "SAP order number"),
			new DataStructure("POSNR", JCO.TYPE_NUM, 6, 0, "Order item number"),
			new DataStructure("ZZCARTON", JCO.TYPE_CHAR, 10, 0, "Carton number"),
			new DataStructure("MATNR", JCO.TYPE_CHAR, 18, 0, "Material number"),
			new DataStructure("ZZBARCODE", JCO.TYPE_CHAR, 20, 0, "Packet barcode"),
			new DataStructure("ZZPCKQTY", JCO.TYPE_CHAR, 13, 0, "Packed Quantity"),
			new DataStructure("NTGEW", JCO.TYPE_CHAR, 15, 0, "Net weight of item"),
			new DataStructure("GEWEI", JCO.TYPE_CHAR, 3, 0, "Weight unit"),
			new DataStructure("ZZFREEZER", JCO.TYPE_CHAR, 1, 0, "Freezer indicator"),
			new DataStructure("ZZBEER", JCO.TYPE_CHAR, 1, 0, "Beer indicator"),
			new DataStructure("ZZPLATTER", JCO.TYPE_CHAR, 1, 0, "Platter indicator"),
		};
		smeta = new JCO.MetaData("CARTONDETAILS");
		for(int i = 0; i < sapData.length; i++) {
			smeta.addInfo(sapData[i].fieldName, sapData[i].type, sapData[i].length, totalSize, sapData[i].decimal);
			totalSize += sapData[i].length;
		}
	}

	public JCO.MetaData[] getStructureMetaData() {
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_CARTON_DETAILS");
		fmeta.addInfo("CARTON",	JCO.TYPE_TABLE,	totalSize, 0, 0, 0,	"CARTONDETAILS");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("BapiErpsCartonContent execute invoked");
		JCO.Table cartonTable = tables.getTable("CARTON");

		cartonTable.firstRow();

		// build carton details
		List cartonDetails = new ArrayList();
		List cartonInfos = new ArrayList();
		HashMap cartons = new HashMap();
		String currentOrderNo = "";
		String currentCartonNo = "";
		ErpCartonInfo cartonInfo = null;
		for (int i = 0; i < cartonTable.getNumRows(); i++) {
			
			// First retrieve all data from current row.
			String webOrderNo = cartonTable.getString("BSTNK").trim();
			String sapOrderNo = cartonTable.getString("VBELN").trim();
			String orderLineNo = StringUtil.leftPad(String.valueOf(cartonTable.getInt("POSNR")), 6, '0');
			String cartonNo = cartonTable.getString("ZZCARTON").trim();
			String materialNo = cartonTable.getString("MATNR").trim();
			String barCode = cartonTable.getString("ZZBARCODE").trim();
			double packedQty = Double.parseDouble(cartonTable.getString("ZZPCKQTY"));
			double netWeight = Double.parseDouble(cartonTable.getString("NTGEW") != null ? cartonTable.getString("NTGEW") : "0.0");
			String weightUnit = cartonTable.getString("GEWEI").trim();
			String freezer = cartonTable.getString("ZZFREEZER").trim();
			String beer = cartonTable.getString("ZZBEER").trim();
			String platter = cartonTable.getString("ZZPLATTER").trim();

			String cartonType = ErpCartonInfo.CARTON_TYPE_REGULAR;
			if(freezer.equals("X")) {
				cartonType = ErpCartonInfo.CARTON_TYPE_FREEZER;
			}
			if(beer.equals("X")) {
				cartonType = ErpCartonInfo.CARTON_TYPE_BEER;
			}
			if(platter.equals("X")) {
				cartonType = ErpCartonInfo.CARTON_TYPE_PLATTER;
			}
			
			// Now process data
			if(!currentOrderNo.equals(webOrderNo)) {
				cartonInfos = new ArrayList();
				cartons.put(webOrderNo,cartonInfos);
				currentOrderNo = webOrderNo;
			}

			if(!currentCartonNo.equals(cartonNo)) {
				cartonInfo = new ErpCartonInfo(webOrderNo, sapOrderNo, cartonNo, cartonType);
				cartonDetails = new ArrayList();
				cartonInfo.setDetails(cartonDetails);
				cartonInfos.add(cartonInfo);
				currentCartonNo = cartonNo;
			}

			ErpCartonDetails detail = new ErpCartonDetails(cartonInfo, orderLineNo, materialNo, barCode, packedQty, netWeight, weightUnit);
			cartonDetails.add(detail);
			
			cartonTable.nextRow();
		}

		try {
			LOGGER.debug("Storing carton content info");

			this.storeCartonInfo(cartons);

			output.setValue("S", "RETURN");
			output.setValue("All good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store wave info", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}
	}

	private void storeCartonInfo(Map cartons) throws NamingException, EJBException, CreateException, FinderException, FDResourceException, RemoteException {
		Context ctx = null;
		String saleId = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			ErpCustomerManagerHome mgr = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
			ErpCustomerManagerSB sb = mgr.create();
	
			for(Iterator i = cartons.keySet().iterator(); i.hasNext(); ) { 
				saleId = (String)i.next();			
				sb.updateCartonInfo(saleId, (List)cartons.get(saleId));
			}
		} catch(Exception ex) {
			throw new EJBException("Failed to store: " + saleId + "Msg: " + ex.toString());
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

class DataStructure {
	String fieldName;
	int type;
	int length;
	int decimal;
	String description;
	DataStructure (String fieldName,
				   int type,
				   int length,
				   int decimal,
				   String description) {
		this.fieldName = fieldName;
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.description = description;
	}
}