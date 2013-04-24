package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Category;

import com.freshdirect.analytics.DateRangeVO;
import com.freshdirect.analytics.OrderRateDAO;
import com.freshdirect.analytics.OrderRateUtil;
import com.freshdirect.analytics.OrderRateVO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderRateSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(OrderRateSessionBean.class);
	
	
	@SuppressWarnings("unchecked")
	public void getOrderRate(Date timeStamp) throws RemoteException{
		
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
			Set<Date> exceptions = OrderRateDAO.getExceptions(conn);
			Set<Date> dates = new HashSet<Date>();
			Map<Date, Date[]> sampleMap = new HashMap<Date, Date[]>();
			while(dateIterator.hasNext())
			{
				Date baseDate = dateIterator.next();
				cal.setTime(baseDate);
				cal = DateUtil.truncate(cal);
				Date[] samples = new Date[2];
				samples[0] = OrderRateUtil.getSample(cal, exceptions);
				dates.add(samples[0]);
				samples[1] = OrderRateUtil.getSample(cal, exceptions);
				dates.add(samples[1]);
				sampleMap.put(baseDate, samples);
				
			}
			List sampleDates = new ArrayList();
			Iterator<Date> datesIterator = dates.iterator();
			while(datesIterator.hasNext())
				sampleDates.add(new java.sql.Date(datesIterator.next().getTime()));
				
			Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> capacityMap = OrderRateDAO.getCapacityMap(conn,sampleDates);
			System.out.println("[OrderRateDAO.getCapacityMap] " +(System.currentTimeMillis() - starttime)/1000);
			Map<DateRangeVO, Map<String, Integer>> orderCountMap = OrderRateDAO.getOrderCount(conn,baseDates);
			System.out.println("[OrderRateDAO.getOrderCount] " +(System.currentTimeMillis() - starttime)/1000);
			
			Iterator<OrderRateVO> voIterator = list.iterator();
			
			while(voIterator.hasNext())
			{
				OrderRateVO vo = voIterator.next();
				
				int days1 = (int)DateUtil.getDiffInDays(vo.getBaseDate(), sampleMap.get(vo.getBaseDate())[0]) * -1;
				int days2 = (int)DateUtil.getDiffInDays(vo.getBaseDate(), sampleMap.get(vo.getBaseDate())[1]) * -1;
				Date snapshot7 = OrderRateUtil.getDate(timeStamp, days1);
				Date startTime7 = OrderRateUtil.getDate(vo.getStartTime(),days1);
				Date endTime7 = OrderRateUtil.getDate(vo.getEndTime(),days1);
				Date snapshot14 = OrderRateUtil.getDate(timeStamp, days2);
				Date startTime14 = OrderRateUtil.getDate(vo.getStartTime(), days2);
				Date endTime14 = OrderRateUtil.getDate(vo.getEndTime(),days2);
				
				
				snapshot7 = OrderRateUtil.addTime(snapshot7);
				snapshot14 = OrderRateUtil.addTime(snapshot14);
				
				DateRangeVO range = new DateRangeVO(vo.getStartTime(), vo.getEndTime());
				DateRangeVO range7 = new DateRangeVO(startTime7, endTime7);
				DateRangeVO range14 = new DateRangeVO(startTime14, endTime14);
				
	
				if(capacityMap.get(range7)!=null && capacityMap.get(range14)!=null
						 && capacityMap.get(range7).get(vo.getZone())!=null 
						&& capacityMap.get(range14).get(vo.getZone())!=null
						 && capacityMap.get(range7).get(vo.getZone()).get(snapshot7)!=null
						 && capacityMap.get(range14).get(vo.getZone()).get(snapshot14)!=null)
			
	
					vo.setProjectedRate(OrderRateUtil.roundValue( new Float(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[1] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[1]) / 2));
				
				else
					vo.setProjectedRate(0);
				
				
				float weightedProjection = 0;
		
				if(capacityMap.get(range7)!=null && capacityMap.get(range14)!=null
						 && capacityMap.get(range7).get(vo.getZone())!=null 
						&& capacityMap.get(range14).get(vo.getZone())!=null
						 && capacityMap.get(range7).get(vo.getZone()).get(snapshot7)!=null
						 && capacityMap.get(range14).get(vo.getZone()).get(snapshot14)!=null
						 && (capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0] > 0) )
				
						weightedProjection = OrderRateUtil.roundValue(new Float( vo.getProjectedRate() * vo.getCapacity() / ((capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0]+ capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0])/2)));
				
				else
						weightedProjection = 0;
				
				vo.setWeightedProjectRate(weightedProjection);
				float orderCount = (orderCountMap.get(range)!=null && orderCountMap.get(range).get(vo.getZone())!=null)?orderCountMap.get(range).get(vo.getZone()):0;
				float capacity = vo.getCapacity();
				
				
				
				Date expectedSoldTime = OrderRateUtil.addTime(vo.getSnapshotTime());
				
				float rate = 0;
				float projected = 0;
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
							projected = new Float(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[1] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[1]) / 2;
							if(capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0] + capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0] > 0)
								rate += OrderRateUtil.roundValue(new Float( projected  * vo.getCapacity() / ((capacityMap.get(range7).get(vo.getZone()).get(snapshot7)[0]+ capacityMap.get(range14).get(vo.getZone()).get(snapshot14)[0])/2)));
							
							vo.setOrdersExpected(rate);
							if((orderCount + vo.getOrderCount() - capacity) <0 && (orderCount + vo.getOrderCount() + rate) - capacity >=0 && evaluate)
							{
								vo.setExpectedSoldOutTime(expectedSoldTime);
								evaluate = false;
							}
						}
						if(expectedSoldTime.getTime()>vo.getCutoffTime().getTime())
							done = true;
					
						snapshot7 = OrderRateUtil.addTime(snapshot7);
						snapshot14 = OrderRateUtil.addTime(snapshot14);
						expectedSoldTime = OrderRateUtil.addTime(expectedSoldTime);	
						
				
					}
					else
					{
						done = true;
					}
					
					
				}
				
			}
			
			save(list, conn);
			long endtime= System.currentTimeMillis();
			System.err.println("Total time for execution of order rate job for snapshot: "+timeStamp.toString()+" "+new Double(endtime-starttime));
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
	}
	

	public void save(List<OrderRateVO> voList, Connection conn)
	{
		OrderRateDAO.saveOrderRate(conn, voList);
	}

}