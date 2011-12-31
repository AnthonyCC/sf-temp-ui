package com.freshdirect.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderRateDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(OrderRateDAO.class);
	
	
	
	private static final String ORDER_RATE_QUERY_BY_TIMESLOT_SNAPSHOT = "select t.base_date , to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') starttime , t.capacity, " +
			"to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') endtime , t.zone_code zone , to_date(to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') cutofftime, t.capacity, " +
			" count(a.id) as \"oCount\" from (select t.base_date, t.start_time, t.end_time, t.capacity, t.cutoff_time, z.zone_code  from dlv.timeslot t, dlv.zone z where t.zone_ID = z.ID and " +
			"to_date(to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM') > SYSDATE ) t  " +
			"left outer join (SELECT s.ID  , DI.STARTTIME, DI.ENDTIME, DI.CUTOFFTIME, DI.ZONE FROM cust.sale s," +
			" cust.salesaction sa, cust.deliveryinfo di WHERE s.ID=sa.SALE_ID AND s.CUSTOMER_ID=sa.CUSTOMER_ID" +
			" AND DI.SALESACTION_ID = SA.ID and s.CROMOD_DATE=sa.ACTION_DATE AND sa.ACTION_TYPE IN ('CRO','MOD')" +
			" AND sa.REQUESTED_DATE > TRUNC(SYSDATE) AND s.type='REG' AND S.CROMOD_DATE BETWEEN (SYSDATE-1/48) " +
			" AND SYSDATE) a on to_char(a.STARTTIME, 'MM/DD/YYYY HH:MI:SS AM') = to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM') " +
			" and  to_char(a.ENDTIME, 'MM/DD/YYYY HH:MI:SS AM') = to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM') " +
			" and a.zone = t.zone_code group by t.base_date,  to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM') ," +
			" to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM') , " +
			"to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM') ,  t.zone_code, t.capacity";

	private static final String ORDER_RATE_SNAPSHOT_INSERT = "INSERT INTO MIS.ORDER_RATE(CAPACITY, ZONE, CUTOFF, " +
			"TIMESLOT_START, TIMESLOT_END, ORDER_COUNT,PROJECTED_COUNT, DELIVERY_DATE, SNAPSHOT_TIME, ACTUAL_SO, PROJECT_SO, WEIGHTED_PROJECTED_COUNT, ORDERS_EXPECTED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String AVG_ORDER_RATE_QUERY = "select order_count, snapshot_time from ((select order_count, snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM') = ? and to_char(timeslot_end,'MM/DD/YYYY HH:MI:SS AM')  = ? and zone = ?) union (select order_count,snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM')  = ? and to_char(timeslot_end,'MM/DD/YYYY HH:MI:SS AM')  = ? and zone = ?))";
	
	private static final String CURRENT_ORDER_COUNT = "select sum(o.order_count) oCount, o.timeslot_start, o.timeslot_end, o.zone  " +
		"from MIS.order_rate o where delivery_date >= ? group by o.timeslot_start, o.timeslot_end, o.zone";
    
	private static final String HOLIDAY_QUERY = "select sum(order_count) as oCount, delivery_date from MIS.order_rate group by delivery_date having sum(order_count) = 0";

	private static final String CAPACITY_QUERY = "select capacity,order_count, " +
			"delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date >= ?";
	
	private static final String ORDER_DATA_QUERY = "select orders, projorders, projso, ts,te, a.zone, capacity, orders_expected, utilization from " +
			"(select  timeslot_start, timeslot_end, zone,  sum(order_count) orders, sum(weighted_projected_count) projorders, max(project_so) projso, " +
			"to_char(timeslot_start,'hh:mi am') ts, to_char(timeslot_end, 'hh:mi am') te from mis.order_rate where to_char(timeslot_start, 'mm/dd/yyyy') = " +
			"? group by timeslot_Start, timeslot_end, zone) a, (select timeslot_start, timeslot_end, zone, capacity, orders_expected, case when capacity<>0 " +
			"then (orders_expected*100/capacity)/100 else 0 end utilization from mis.order_rate where (snapshot_time,timeslot_start, timeslot_end,zone) " +
			"in (select max(snapshot_time), timeslot_Start, timeslot_end,zone from mis.order_rate where to_char(timeslot_start, 'mm/dd/yyyy') = ? " +
			"group by timeslot_start, timeslot_end, zone)   ) b where a.timeslot_start = b.timeslot_start and a.timeslot_end = b.timeslot_end " +
			"and a.zone = b.zone";
			

	private static Timestamp getTimestamp(Timestamp timeStamp, int lookback)
	{
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(timeStamp.getTime());
		cal2.add(Calendar.DATE, lookback);
		long time = cal2.getTimeInMillis();
		return new Timestamp(time);
	}
	
	public static Map getOrderRates(Connection conn, Timestamp timeStamp)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		Map returnMap = new HashMap();
		Set<Date> baseDates = new HashSet<Date>();
		
		try {
				ps = conn.prepareStatement(ORDER_RATE_QUERY_BY_TIMESLOT_SNAPSHOT);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setCapacity(rs.getInt("capacity"));
					vo.setZone(rs.getString("zone"));
					vo.setCutoffTime(rs.getTimestamp("cutofftime"));
					vo.setStartTime(rs.getTimestamp("starttime"));
					vo.setEndTime(rs.getTimestamp("endtime"));
					vo.setOrderCount(rs.getFloat("oCount"));
					vo.setBaseDate(rs.getDate("base_date"));
					vo.setSnapshotTime(timeStamp);
					voList.add(vo);
					baseDates.add(vo.getBaseDate());
				                                                                                                                        
				}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try 
				{
					rs.close();
					ps.close();
				} 
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			returnMap.put("orderRate", voList);
			returnMap.put("dates", baseDates);
		return returnMap;
			
		
	}
	
	public static Map<DateRangeVO,  Map<String, Map<Timestamp, Integer[]>>> getCapacityMap(Connection conn, Date minDate)
	{
		PreparedStatement ps =null;ResultSet rs = null;
		Map<DateRangeVO,  Map<String, Map<Timestamp, Integer[]>>> capacityMap = new HashMap<DateRangeVO,  Map<String, Map<Timestamp, Integer[]>>>();
		
		try
		{
			ps = conn.prepareStatement(CAPACITY_QUERY);
			ps.setDate(1, new java.sql.Date(minDate.getTime()));
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				DateRangeVO range = new DateRangeVO(rs.getTimestamp("timeslot_start"), rs.getTimestamp("timeslot_end"));
				String zone = rs.getString("zone");
				Timestamp snapshot = rs.getTimestamp("snapshot_time");
				Integer capacity = rs.getInt("capacity");
				Integer orders = rs.getInt("order_count");
				Integer[] metrics = new Integer[]{capacity,orders};
				if(capacityMap.get(range)!=null)
				{
					if(capacityMap.get(range).get(zone)!=null)
					{
						if(capacityMap.get(range).get(zone).get(snapshot)==null)
							capacityMap.get(range).get(zone).put(snapshot, metrics);
					}
					else
					{
						Map map = new HashMap<Timestamp, Integer>();
						map.put(snapshot, metrics);
						capacityMap.get(range).put(zone, map);
					}
				}
				else
				{
					Map map = new HashMap<Timestamp, Integer>();
					map.put(snapshot, metrics);
					Map zoneMap = new HashMap<String,Map<Timestamp, Integer>>();
					zoneMap.put(zone, map);
					capacityMap.put(range, zoneMap);
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try 
			{
				rs.close();
				ps.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return capacityMap;
	}
	
	public static Map<Date, Integer>  getHolidayMap(Connection conn)
	{
		PreparedStatement ps =null;ResultSet rs = null;
		Map<Date, Integer> holidayMap = new HashMap<Date, Integer>();
		
		try
		{
			ps = conn.prepareStatement(HOLIDAY_QUERY); 
			rs = ps.executeQuery();
			while(rs.next())
			{
				holidayMap.put(new Date(rs.getDate("delivery_date").getTime()), rs.getInt("oCount"));	
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try 
			{
				rs.close();
				ps.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return holidayMap;
	}
	
	
	public static Map<Timestamp, Float> getAverageOrderRate(Connection conn, Date snapshot7, Date startTime7, Date endTime7,
			 Date snapshot14, Date startTime14, Date endTime14, OrderRateVO vo)
	{
		long starttime = System.currentTimeMillis();
		Map<Timestamp, Float> map = null;
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try {
		
		ps = conn.prepareStatement(AVG_ORDER_RATE_QUERY);
		ps.setString(1, sdf.format(new java.sql.Timestamp(snapshot7.getTime())));
		ps.setString(2, sdf.format(new java.sql.Timestamp(startTime7.getTime())));
		ps.setString(3, sdf.format(new java.sql.Timestamp(endTime7.getTime())));
		ps.setString(4,vo.getZone());
		ps.setString(5, sdf.format(new java.sql.Timestamp(snapshot14.getTime())));
		ps.setString(6, sdf.format(new java.sql.Timestamp(startTime14.getTime())));
		ps.setString(7, sdf.format(new java.sql.Timestamp(endTime14.getTime())));
		ps.setString(8,vo.getZone());
		
		rs = ps.executeQuery();
		
		map = new HashMap<Timestamp, Float>();
		
		while(rs.next())
		{
			map.put(rs.getTimestamp("snapshot_time"), rs.getFloat("order_count"));
		}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try 
			{
				rs.close();
				ps.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println(endtime - starttime);
		return map;
	}
	
	public static Map<DateRangeVO,  Map<String, Integer>> getOrderCount(Connection conn, Date minDate)
	{
		long starttime = System.currentTimeMillis();
		String zone = null;
		Map<DateRangeVO,  Map<String, Integer>> orderCountMap = new HashMap<DateRangeVO,  Map<String, Integer>>();
		PreparedStatement ps =null;ResultSet rs = null;Integer orderCount = 0;
		try {
			ps = conn.prepareStatement(CURRENT_ORDER_COUNT);
			ps.setDate(1, new java.sql.Date(minDate.getTime()));
			rs = ps.executeQuery();
			
			while(rs.next())
			{
			DateRangeVO range = new DateRangeVO(rs.getTimestamp("timeslot_start"), rs.getTimestamp("timeslot_end"));
			zone = rs.getString("zone");
			orderCount = rs.getInt("oCount");
			
			if(orderCountMap.get(range)!=null)
			{
				
					orderCountMap.get(range).put(zone, orderCount);
			}
			else
			{
					HashMap zoneMap = new HashMap();
					zoneMap.put(zone, orderCount);
					orderCountMap.put(range,zoneMap);
			}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try 
			{
				rs.close();
				ps.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println(endtime - starttime);
		
		return orderCountMap;
		
	}
	
	public static void saveOrderRate(Connection conn, List<OrderRateVO> list)
	{
		PreparedStatement ps =null;
		try
		{
		Iterator<OrderRateVO> voIterator = list.iterator();
		ps = conn.prepareStatement(ORDER_RATE_SNAPSHOT_INSERT);
		
		while(voIterator.hasNext())
		{
			OrderRateVO vo = voIterator.next();
			ps.setInt(1, vo.getCapacity());
			ps.setString(2, vo.getZone());
			ps.setTimestamp(3, new java.sql.Timestamp(vo.getCutoffTime().getTime()));
			ps.setTimestamp(4, new java.sql.Timestamp(vo.getStartTime().getTime()));
			ps.setTimestamp(5, new java.sql.Timestamp(vo.getEndTime().getTime()));
			ps.setFloat(6, vo.getOrderCount());
			ps.setFloat(7,vo.getProjectedRate());
			ps.setDate(8, new java.sql.Date(vo.getBaseDate().getTime()));
			ps.setTimestamp(9, new java.sql.Timestamp(vo.getSnapshotTime().getTime()));
			if(vo.getSoldOutTime()!=null)
				ps.setTimestamp(10, new java.sql.Timestamp(vo.getSoldOutTime().getTime()));
			else
				ps.setNull(10, java.sql.Types.TIMESTAMP);
			if(vo.getExpectedSoldOutTime()!=null)
				ps.setTimestamp(11,  new java.sql.Timestamp(vo.getExpectedSoldOutTime().getTime()));
			else
				ps.setNull(11, java.sql.Types.TIMESTAMP);
			
			ps.setFloat(12, vo.getWeightedProjectRate());
			ps.setFloat(13, vo.getOrdersExpected());
			ps.addBatch();
		}
		ps.executeBatch();
		
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try 
			{
				ps.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static List<OrderData> getData(Connection conn, String createDate) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderData> dataList = new ArrayList<OrderData>();
		 try {
		    	ps = conn.prepareStatement(ORDER_DATA_QUERY);
		    	ps.setString(1, createDate);
		    	ps.setString(2, createDate);
		    	rs = ps.executeQuery();
		    	
		    	while(rs.next())
		    	{
		    		OrderData data = new OrderData();
		    		data.setOrderCount(rs.getDouble("orders"));
		    		data.setProjectedCount(rs.getDouble("projorders"));
		    		data.setProjectedSoldOut(rs.getDate("projso"));
		    		data.setZone(rs.getString("zone"));
		    		data.setStartTime(rs.getString("ts"));
		    		data.setEndime(rs.getString("te"));
		    		data.setCapacity(rs.getInt("capacity"));
		    		data.setOrdersExpected(rs.getDouble("orders_expected"));
		    		data.setUtilization(rs.getDouble("utilization"));
		    		dataList.add(data);
		    		
		    	}
		    	
		 }
		 catch(Exception e)
			{
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}
		    	
		 finally
			{
				try
				{
					if(rs!=null)
						rs.close();
					if(ps!=null)
						ps.close();
				}
				catch(SQLException e)
				{
					LOGGER.info("There was an exception cleaning up the resources", e);
				}
			}
		 return dataList;
		
	}
}
