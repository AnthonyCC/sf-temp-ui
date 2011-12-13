package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import org.apache.log4j.Category;

import weblogic.auddi.util.Logger;

import com.freshdirect.analytics.DateRangeVO;
import com.freshdirect.analytics.OrderRateDAO;
import com.freshdirect.analytics.OrderRateVO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderRateSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(OrderRateSessionBean.class);
	
	private static Timestamp getDate(Timestamp date, int lookback)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.DATE, lookback);
		return new java.sql.Timestamp(cal.getTimeInMillis());
	}
	private Float roundValue(double value)
	{
		float val = 0;
		try
		{
		DecimalFormat df = new DecimalFormat("#.##");
		val =  Float.valueOf(df.format(value)).floatValue();
		}
		catch(NumberFormatException nfe)
		{
			Logger.info("Exception while rounding the value: "+value);
		}
		return val;
	}
	
	public Date getSample(Calendar cal, Map holidayMap)
	{
		cal.add(Calendar.DATE, -7);
		while(holidayMap!= null && holidayMap.get(cal.getTime()) != null)
		{
			cal.add(Calendar.DATE, -7);
		}
		return cal.getTime();
		
	}
	@SuppressWarnings("unchecked")
	public void getOrderRate(Timestamp timeStamp) throws RemoteException{
		
		final String METHODNAME = "getOrderRate()";
		LOGGER.info("start: "+METHODNAME );
		
		Calendar cal = Calendar.getInstance();
		long starttime = System.currentTimeMillis();
		Connection conn = null;
		try 
		{
			conn  = this.getConnection();
				
			Map orderRateMap  = OrderRateDAO.getOrderRates(conn,timeStamp);
			
			List<OrderRateVO> list =  (List<OrderRateVO>) orderRateMap.get("orderRate");
			Set<Date> baseDates =  (Set<Date>) orderRateMap.get("dates");
			Iterator<Date> dateIterator = baseDates.iterator();
			Map<Date, Integer> holidayMap = OrderRateDAO.getHolidayMap(conn);
			List<Date> dates = new ArrayList<Date>();
			Map<Date, Date[]> sampleMap = new HashMap<Date, Date[]>();
			while(dateIterator.hasNext())
			{
				Date baseDate = dateIterator.next();
				cal.setTime(baseDate);
				cal = DateUtil.truncate(cal);
				Date[] samples = new Date[2];
				samples[0] = getSample(cal, holidayMap);
				dates.add(samples[0]);
				samples[1] = getSample(cal, holidayMap);
				dates.add(samples[1]);
				sampleMap.put(baseDate, samples);
				
			}
			
			Date minDate = Collections.min(dates);
				
			Map<DateRangeVO,  Map<String, Map<Timestamp, Integer[]>>> capacityMap = OrderRateDAO.getCapacityMap(conn,minDate);
			Map<DateRangeVO, Map<String, Integer>> orderCountMap = OrderRateDAO.getOrderCount(conn,minDate);
			
			
			Iterator<OrderRateVO> voIterator = list.iterator();
			
			while(voIterator.hasNext())
			{
				OrderRateVO vo = voIterator.next();
				
				int days1 = (int)DateUtil.diffInDays(vo.getBaseDate(), sampleMap.get(vo.getBaseDate())[0]) * -1;
				int days2 = (int)DateUtil.diffInDays(vo.getBaseDate(), sampleMap.get(vo.getBaseDate())[1]) * -1;
				Timestamp snapshot7 = getDate(timeStamp, days1);
				Timestamp startTime7 = getDate(vo.getStartTime(),days1);
				Timestamp endTime7 = getDate(vo.getEndTime(),days1);
				Timestamp snapshot14 = getDate(timeStamp, days2);
				Timestamp startTime14 = getDate(vo.getStartTime(), days2);
				Timestamp endTime14 = getDate(vo.getEndTime(),days2);
				
				
				snapshot7 = (Timestamp) addTime(snapshot7);
				snapshot14 = (Timestamp) addTime(snapshot14);
				
				DateRangeVO range = new DateRangeVO(vo.getStartTime(), vo.getEndTime());
				DateRangeVO range7 = new DateRangeVO(startTime7, endTime7);
				DateRangeVO range14 = new DateRangeVO(startTime14, endTime14);
				
	
				if(capacityMap.get(range7)!=null && capacityMap.get(range14)!=null
						 && capacityMap.get(range7).get(vo.getZone())!=null 
						&& capacityMap.get(range14).get(vo.getZone())!=null
						 && capacityMap.get(range7).get(vo.getZone()).get(snapshot7)!=null
						 && capacityMap.get(range14).get(vo.getZone()).get(snapshot14)!=null)
			
	
					vo.setProjectedRate(roundValue( new Float(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[1] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[1]) / 2));
				
				else
					vo.setProjectedRate(0);
				
				
				float weightedProjection = 0;
		
				if(capacityMap.get(range7)!=null && capacityMap.get(range14)!=null
						 && capacityMap.get(range7).get(vo.getZone())!=null 
						&& capacityMap.get(range14).get(vo.getZone())!=null
						 && capacityMap.get(range7).get(vo.getZone()).get(snapshot7)!=null
						 && capacityMap.get(range14).get(vo.getZone()).get(snapshot14)!=null
						 && (capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0] > 0) )
				
						weightedProjection = roundValue(new Float( vo.getProjectedRate() * vo.getCapacity() / ((capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0]+ capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0])/2)));
				
				else
						weightedProjection = 0;
				
				vo.setWeightedProjectRate(weightedProjection);
				float orderCount = (orderCountMap.get(range)!=null && orderCountMap.get(range).get(vo.getZone())!=null)?orderCountMap.get(range).get(vo.getZone()):0;
				float capacity = vo.getCapacity();
				
				
				
				Timestamp expectedSoldTime = addTime(vo.getSnapshotTime());
				
				float rate = 0;
				boolean done = false, evaluate = true;
				if(orderCount - capacity <0 && (orderCount+vo.getOrderCount() - capacity) >=0)
				{
					vo.setSoldOutTime(vo.getSnapshotTime());
					vo.setExpectedSoldOutTime(vo.getSnapshotTime());
					evaluate = false;
				}
				while(!done)
				{
				
					if(capacityMap.get(range7)!=null && capacityMap.get(range14)!=null
							 && capacityMap.get(range7).get(vo.getZone())!=null 
								&& capacityMap.get(range14).get(vo.getZone())!=null )
					{
						
						if(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)!=null
								 && capacityMap.get(range14).get(vo.getZone()).get(snapshot14)!=null)
						{
							rate += roundValue( new Float(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[1]+ capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[1]) /2);
							
							vo.setOrdersExpected(rate);
							if((orderCount + vo.getOrderCount() - capacity) <0 && (orderCount + vo.getOrderCount() + rate) - capacity >=0 && evaluate)
							{
								vo.setExpectedSoldOutTime(expectedSoldTime);
								evaluate = false;
							}
						}
						if(expectedSoldTime.getTime()>vo.getCutoffTime().getTime())
							done = true;
					
						snapshot7 = addTime(snapshot7);
						snapshot14 = addTime(snapshot14);
						expectedSoldTime = addTime(expectedSoldTime);	
						
				
					}
					else
					{
						done = true;
					}
					
					
				}
				
			}
			
			save(list);
			long endtime= System.currentTimeMillis();
			System.err.println("Total time for execution of order rate job for snapshot: "+new Date(timeStamp.getTime()).toString()+" "+new Double(endtime-starttime));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		
		LOGGER.info("end: "+METHODNAME);
		
		
	}
	
	private Timestamp addTime(Timestamp time)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 30);
		return new java.sql.Timestamp(cal.getTimeInMillis());
		
	}
	public void save(List<OrderRateVO> voList)
	{
		Connection conn = null;
		
		try {
			conn = getConnection();
			OrderRateDAO.saveOrderRate(conn, voList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		
	}

}