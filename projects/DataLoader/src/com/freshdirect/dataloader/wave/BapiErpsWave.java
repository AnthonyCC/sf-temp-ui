package com.freshdirect.dataloader.wave;

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
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.sap.mw.jco.JCO;

import com.freshdirect.framework.util.StringUtil;

public class BapiErpsWave implements BapiFunctionI {

	private final static Category LOGGER = LoggerFactory.getInstance( BapiErpsWave.class );

	private final static long TIMEOUT = 5 * 60 * 1000; 

	private final static int WAVE_LENGTH 	= 6;
	private final static int ROUTE_LENGTH 	= 6;
	private final static int STOP_LENGTH 	= 5;
	
	public JCO.MetaData[] getStructureMetaData() {
		JCO.MetaData smeta  = new JCO.MetaData("WAVELIST");
		smeta.addInfo("BSTNK",  JCO.TYPE_CHAR,  20,  0, 0);
		smeta.addInfo("VBELN",  JCO.TYPE_CHAR,  10,  20, 0);
		smeta.addInfo("ZZSWAVENO",  JCO.TYPE_NUM,  6,  20 + 10, 0);
		smeta.addInfo("ZZTRKNO",  JCO.TYPE_NUM,  6,  20 + 10 + 6, 0);
		smeta.addInfo("ZZSTOPSEQ",  JCO.TYPE_CHAR, 5,  20 + 10 + 6 + 6, 0);
		return new JCO.MetaData[] { smeta };
	}

	public JCO.MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData("ZERPS_WAVE_DETAILS");
		fmeta.addInfo("WAVE",	JCO.TYPE_TABLE,	20 + 10 + 6 + 6 + 5, 0, 0, 0,	"WAVELIST");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;	
	}

	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables) {
		LOGGER.debug("execute invoked");
		JCO.Table waveTable = tables.getTable("WAVE");

		waveTable.firstRow();

		// build waves
		Map waveEntries = new HashMap(waveTable.getNumRows());
		for (int i = 0; i < waveTable.getNumRows(); i++) {
			String webOrderNo = waveTable.getString("BSTNK");
			String sapOrderNo = waveTable.getString("VBELN");
			String waveNo = StringUtil.leftPad(String.valueOf(waveTable.getInt("ZZSWAVENO")), WAVE_LENGTH, '0');
			String routeNo = StringUtil.leftPad(String.valueOf(waveTable.getInt("ZZTRKNO")), ROUTE_LENGTH, '0');
			String stopNo = StringUtil.leftPad(waveTable.getString("ZZSTOPSEQ"), STOP_LENGTH, '0');

			LOGGER.debug(webOrderNo + "\t" + sapOrderNo + "\t" + waveNo + "\t" + routeNo+ "\t" + stopNo);

			waveEntries.put(webOrderNo, new ErpShippingInfo(waveNo, routeNo, stopNo, 0, 0, 0));

			waveTable.nextRow();
		}

		try {
			LOGGER.debug("Storing wave info");

			this.updateWaveInfo(waveEntries);

			output.setValue("S", "RETURN");
			output.setValue("It's all good...", "MESSAGE");

		} catch (Exception ex) {
			LOGGER.warn("Failed to store wave info", ex);

			String errorMsg = ex.toString();
			errorMsg = errorMsg.substring(0, Math.min(255, errorMsg.length()));
			LOGGER.info("Error message to SAP: '" + errorMsg + "'");
			output.setValue("E", "RETURN");
			output.setValue(errorMsg, "MESSAGE");
		}
	}

	private void updateWaveInfo(Map waveEntries) throws NamingException, EJBException, CreateException, FinderException, FDResourceException, RemoteException {
	Context ctx = null;
	try {
		ctx = ErpServicesProperties.getInitialContext();
		ErpCustomerManagerHome mgr = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
		ErpCustomerManagerSB sb = mgr.create();

		for(Iterator i = waveEntries.keySet().iterator(); i.hasNext(); ) { 
			String saleId = (String)i.next();			
			sb.updateWaveInfo(saleId, (ErpShippingInfo)waveEntries.get(saleId));
		}
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
