package com.freshdirect.webservices;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.analytics.dao.IBounceDAO;
import com.freshdirect.analytics.dao.IOrderRateDAO;
import com.freshdirect.analytics.dao.IRollDAO;
import com.freshdirect.analytics.model.BounceData;
import com.freshdirect.analytics.model.OrderData;
import com.freshdirect.analytics.model.OrderRateVO;
import com.freshdirect.analytics.model.PlantDispatchData;
import com.freshdirect.analytics.model.RollData;
import com.freshdirect.analytics.util.OrderRateUtil;

public class EventServiceImpl implements EventService {

	
	private static final Category LOGGER = Logger.getLogger(EventServiceImpl.class);
	private static final String DEFAULT_ZONE = "002";
	
	private IBounceDAO bounceDAO;
	private IRollDAO rollDAO;
	private IOrderRateDAO orderRateDAO;
	
	public String getResourceCacheKey(){
		return "com.freshdirect.webservices.EventService";
	}
	public List<BounceData> getBounce(String deliveryDate,String zone)
	{
		LOGGER.info("Begin getBounce. Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();

		List<BounceData> dataList = null;
		dataList = bounceDAO.getData(deliveryDate,zone);
		 
		//print3(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getBounce. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<RollData> getRoll(String deliveryDate,String zone)
	{
		
		List<RollData> dataList = null;
		LOGGER.info("Begin getRoll. Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();

		dataList = rollDAO.getData(deliveryDate,zone);
	
		//print2(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getRoll. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<BounceData> getBounceByZone(String deliveryDate)
	{
		
		List<BounceData> dataList = null;
		long start_time = System.currentTimeMillis();

		LOGGER.info("Begin getBounceByZone. Delivery Date "+deliveryDate);
		
		dataList = bounceDAO.getDataByZone(deliveryDate);
		
		//print3(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getBounceByZone. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<RollData> getRollByZone(String deliveryDate)
	{
		
		List<RollData> dataList = null;
		LOGGER.info("Begin getRollByZone. Delivery Date "+deliveryDate);
		long start_time = System.currentTimeMillis();

		dataList = rollDAO.getDataByZone(deliveryDate);
		
		//print2(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getRollByZone. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}

	public List<OrderRateVO> getCurrentOrdersByZoneCutoff(String deliveryDate)
	{
		
		List<OrderRateVO> dataList = null;
		LOGGER.info("Begin getCurrentOrdersByZoneCutoff. Delivery Date "+deliveryDate);
		long start_time = System.currentTimeMillis();

		dataList = orderRateDAO.getCurrentOrdersByZoneCutoff(deliveryDate);
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getCurrentOrdersByZoneCutoff. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	public List<OrderRateVO> getResourcesByZoneCutoff(String deliveryDate)
	{
		
		List<OrderRateVO> dataList = null;
		LOGGER.info("Begin getResourcesByZoneCutoff. Delivery Date "+deliveryDate);
		long start_time = System.currentTimeMillis();

		dataList = orderRateDAO.getResourcesByZoneCutoff(deliveryDate);
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getResourcesByZoneCutoff. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	public List<OrderRateVO> getCurrentOrdersByZoneTimeslot(String deliveryDate, String zone)
	{
		
		List<OrderRateVO> dataList = null;
		long start_time = System.currentTimeMillis();

		if("0".equals(zone))
			return dataList;
		LOGGER.info("Begin getCurrentOrdersByZoneTimeslot. Delivery Date "+deliveryDate+" Zone "+zone);
		
		dataList = orderRateDAO.getCurrentOrdersByZoneTimeslot(deliveryDate, getDefaultZone(zone));
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getCurrentOrdersByZoneTimeslot. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	private String getDefaultZone(String zone)
	{
		if(zone ==null ||"".equals(zone)) return DEFAULT_ZONE;
		return zone;
		
	}
	public List<OrderRateVO> getCapacityByZoneTimeslot(String deliveryDate, String zone)
	{
		
		List<OrderRateVO> dataList = null;
		if("0".equals(zone))
			return dataList;
		LOGGER.info("Begin getCapacityByZoneTimeslot. Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();
		
		dataList = orderRateDAO.getCapacityByZoneTimeslot(deliveryDate, getDefaultZone(zone));
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getCapacityByZoneTimeslot. Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<OrderRateVO> getCapacityByZoneCutoff(String deliveryDate)
	{
		
		List<OrderRateVO> dataList = null;
		LOGGER.info("Begin getCapacityByZoneCutoff. Delivery Date "+deliveryDate);
		long start_time = System.currentTimeMillis();

		dataList = orderRateDAO.getCapacityByZoneCutoff(deliveryDate);
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		
		LOGGER.info("End getCapacityByZoneCutoff. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	

	public List<OrderRateVO> getProjectedOrdersByZoneCutoff(String deliveryDate)
	{
		
		List<OrderRateVO> dataList = null;
		
		LOGGER.info("Begin getProjectedOrdersByZoneCutoff. Delivery Date "+deliveryDate);
		long start_time = System.currentTimeMillis();

		dataList = orderRateDAO.getProjectedOrdersByZoneCutoff(deliveryDate);
			
	
		 
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getProjectedOrdersByZoneCutoff. Delivery Date "+deliveryDate+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
		
	}
	
	public List<OrderRateVO> getCurrentOrderRateBySnapshot(String currentDate, String deliveryDate, String zone) throws ParseException
	{
		LOGGER.info("Begin getCurrentOrderRateBySnapshot. Current Day "+currentDate+" Delivery Date "+deliveryDate+" Zone "+zone);
		

		
		List<OrderRateVO> dataList = new ArrayList<OrderRateVO>();
		long start_time = System.currentTimeMillis();

		dataList = orderRateDAO.getCurrentOrderRateBySnapshot(currentDate,deliveryDate, getDefaultZone(zone));
		
		//print1(dataList);
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getCurrentOrderRateBySnapshot. Current Day "+currentDate+" Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
	}

	public List<OrderData> getOrderCount(String currentDate, String deliveryDate, String zone) throws ParseException
	{
		LOGGER.info("Begin getOrderCount. Current Day "+currentDate+" Delivery Date "+deliveryDate+" Zone "+zone);
		long start_time = System.currentTimeMillis();

		
		List<OrderData> dataList = new ArrayList<OrderData>();
		
		
		dataList = orderRateDAO.getOrderCount(currentDate,deliveryDate, getDefaultZone(zone));
		
		long end_time = System.currentTimeMillis();

		LOGGER.info("End getOrderCount. Current Day "+currentDate+" Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));
		return dataList;
	}
	
	public List<PlantDispatchData> getPlantDispatchData(String deliveryDate)
	{
		LOGGER.info("Begin getPlantDispatchData. Current Day "+deliveryDate);
		

		
		List<PlantDispatchData> dataList = new ArrayList<PlantDispatchData>();
		
		dataList = orderRateDAO.getPlantDispatchData(deliveryDate);
		
		LOGGER.info("End getPlantDispatchData. Delivery Date "+deliveryDate);
		return dataList;
	}
	

	public String getRefreshTime()
	{
		DateFormat df = new SimpleDateFormat("hh:mm a");
		return df.format(new Date());
	}
	
	public List<String> getBaseDates(String deliveryDate) throws ParseException{
		Set<Date> exceptions = orderRateDAO.getExceptions();
		List<String> baseDates = new ArrayList<String>(2);
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		cal.setTime(df.parse(deliveryDate));
		baseDates.add(df.format(OrderRateUtil.getSample(cal,exceptions)));
		baseDates.add(df.format(OrderRateUtil.getSample(cal,exceptions)));
		Collections.sort(baseDates);
		return baseDates;
	}
	
	public List<OrderRateVO> getForecast(String deliveryDate, String zone, String prevDay1, String prevDay2) throws ParseException
	{
		LOGGER.info("Start getForecast. ");
		long start_time = System.currentTimeMillis();
		
		
		
		
		List<OrderRateVO> dataList = null;
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date1, date2;
		final Date date;
		
		date = df.parse(deliveryDate);
		
		cal.setTime(df.parse(deliveryDate));
		
		Set<Date> exceptions = orderRateDAO.getExceptions();
		if(prevDay1 == null || prevDay2 == null || "".equals(prevDay2) || "".equals(prevDay1))
		{
				date1 = OrderRateUtil.getSample(cal,exceptions);
				date2 = OrderRateUtil.getSample(cal,exceptions);
		}
		else
		{
				 date1 = df.parse(prevDay1);
				 date2 = df.parse(prevDay2);
		}
			
		LOGGER.info("Day1 "+date1+" Day2 "+date2+" Delivery Date "+deliveryDate+" Zone "+zone+" "+Long.toString(System.currentTimeMillis()-start_time));
			
		dataList = orderRateDAO.getForecastData(deliveryDate, date, getDefaultZone(zone),date1,date2);
		//print1(dataList);
			
		long end_time = System.currentTimeMillis();
			
		LOGGER.info("End getForecast. Day1 "+date1+" Day2 "+date2+" Delivery Date "+deliveryDate+" Zone "+zone+" Total Time "+Long.toString(end_time-start_time));

		return dataList;
		
	
		
	}
	
	 public String getConnection() throws Exception {
	       
	    	InitialContext initCtx;
	    	Context envCtx;

	    	initCtx = new InitialContext();
	    	DataSource ds = (DataSource)initCtx.lookup("java:comp/env/fdwsdatasource");
	    	Connection conn = null; 
	    	conn = ds.getConnection();
	    	if(conn!=null)
	    		return "success";
	    	else
	    		return null;
	    }
	private static void print1(List<OrderRateVO> volist)
	{	System.out.println("snapshot\tzone\ttimeslot_start\ttimeslot_end\tcutoff\tprojected_rate\tweighted_projected_rate\tcapacity\tordercount");
		for(OrderRateVO vo: volist )
		{
		
			System.out.println(vo.getSnapshotTimeFmt()+"\t"+vo.getZone()+"\t"+vo.getStartTime()+"\t"+vo.getStartTimeFormatted()+"\t"+vo.getEndTime()+"\t"+vo.getCutoffTime()+"\t"+vo.getCutoffTimeFormatted()+"\t"+vo.getProjectedRate()
					+"\t"+vo.getWeightedProjectRate()+"\t"+vo.getCapacity()+"\t"+vo.getOrderCount());
		}
	}
	
	private static void print2(List<RollData> volist)
	{	
		System.out.println("count\tzone\tcutoff\t\tTime ");
		for(RollData vo: volist )
		{
		
			System.out.println(vo.getCnt()+"\t"+vo.getZone()+"\t"+vo.getCutOff()+"\t"+vo.getCutoffTimeFormatted()+"\t"+vo.getSnapshotTime()+"\t"+vo.getSnapshotTimeFormatted()+"\t");
		}
	}
	private static void print3(List<BounceData> volist)
	{	
		System.out.println("count\tzone\tcutoff\t\tTime ");
		for(BounceData vo: volist )
		{
		
			System.out.println(vo.getCnt()+"\t"+vo.getZone()+"\t"+vo.getCutOff()+"\t"+vo.getCutoffTimeFormatted()+"\t"+vo.getSnapshotTime()+"\t"+vo.getSnapshotTimeFormatted()+"\t");
		}
	}
	public IBounceDAO getBounceDAO() {
		return bounceDAO;
	}
	public void setBounceDAO(IBounceDAO bounceDAO) {
		this.bounceDAO = bounceDAO;
	}
	public IRollDAO getRollDAO() {
		return rollDAO;
	}
	public void setRollDAO(IRollDAO rollDAO) {
		this.rollDAO = rollDAO;
	}
	public IOrderRateDAO getOrderRateDAO() {
		return orderRateDAO;
	}
	public void setOrderRateDAO(IOrderRateDAO orderRateDAO) {
		this.orderRateDAO = orderRateDAO;
	}
}
