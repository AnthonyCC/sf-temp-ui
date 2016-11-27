package com.freshdirect.webservices;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.analytics.dao.IBounceDAO;
import com.freshdirect.analytics.dao.IOrderRateDAO;
import com.freshdirect.analytics.dao.IPlantDispatchDAO;
import com.freshdirect.analytics.dao.IRollDAO;
import com.freshdirect.analytics.model.BounceData;
import com.freshdirect.analytics.model.OrderData;
import com.freshdirect.analytics.model.OrderRateVO;
import com.freshdirect.analytics.model.PlantDispatchData;
import com.freshdirect.analytics.model.RollData;
import com.freshdirect.analytics.util.OrderRateUtil;

public class PlantDispatchServiceImpl implements PlantDispatchService {

	
	private static final Category LOGGER = Logger.getLogger(PlantDispatchServiceImpl.class);
	private IPlantDispatchDAO plantDispatchDAO;
	
	public String getResourceCacheKey(){
		return "com.freshdirect.webservices.PlantDispatchService";
	}
	public List<PlantDispatchData> getDispatchVolume(String deliveryDateStr)
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		List<PlantDispatchData> dataList = null;
		try{
		Date deliveryDate = df.parse(deliveryDateStr);
		LOGGER.info("deliveryDate "+deliveryDate);
		dataList = plantDispatchDAO.getData(deliveryDate);
		LOGGER.info("dataList"+dataList);
		int cumlPlannedCapacity=0, cumlOrders=0;
		
		if(dataList!=null)
		{
			for(PlantDispatchData data: dataList)
			{
				data.setCumlplannedCapacity(data.getPlannedCapacity()+cumlPlannedCapacity);
				data.setTotalOrders(data.getOrders()+cumlOrders);
				cumlPlannedCapacity  += data.getPlannedCapacity();
				cumlOrders += data.getOrders();
			}
		}
		}catch(Exception e){
			LOGGER.info(e.getMessage());e.printStackTrace();
		}
		return dataList;
	}
	
	public String getRefreshTime()
	{
		DateFormat df = new SimpleDateFormat("hh:mm:ss a");
		return df.format(new Date());
	}
	
	public String getConnection() throws Exception {
	       
	    	InitialContext initCtx;
	    	
	    	initCtx = new InitialContext();
	    	DataSource ds = (DataSource)initCtx.lookup("java:comp/env/fdwsdatasource");
	    	Connection conn = null; 
	    	conn = ds.getConnection();
	    	if(conn!=null)
	    		return "success";
	    	else
	    		return null;
	    }
	public IPlantDispatchDAO getPlantDispatchDAO() {
		return plantDispatchDAO;
	}
	public void setPlantDispatchDAO(IPlantDispatchDAO plantDispatchDAO) {
		this.plantDispatchDAO = plantDispatchDAO;
	}

	
}
