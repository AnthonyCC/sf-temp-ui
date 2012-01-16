package com.freshdirect.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderRateDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(OrderRateDAO.class);
	
	
	
	private static final String ORDER_RATE_QUERY_BY_TIMESLOT_SNAPSHOT = "select t.base_date , to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') starttime , t.capacity, " +
			"to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') endtime , t.zone_code zone , to_date(to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') cutofftime, t.capacity, " +
			" count(a.id) as \"oCount\" from (select t.base_date, t.start_time, t.end_time, t.capacity, t.cutoff_time, z.zone_code  from dlv.timeslot t, dlv.zone z where t.zone_ID = z.ID and " +
			"to_date(to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM') > SYSDATE ) t  " +
			"left outer join (SELECT s.ID  , DI.STARTTIME, DI.ENDTIME, DI.CUTOFFTIME, DI.ZONE FROM cust.sale s," +
			" cust.salesaction sa, cust.deliveryinfo di WHERE s.ID=sa.SALE_ID AND s.CUSTOMER_ID=sa.CUSTOMER_ID" +
			" AND DI.SALESACTION_ID = SA.ID AND sa.ACTION_TYPE IN ('CRO')" +
			" AND sa.REQUESTED_DATE > TRUNC(SYSDATE) AND s.type='REG' AND S.CROMOD_DATE BETWEEN (SYSDATE-1/48) " +
			" AND SYSDATE) a on to_char(a.STARTTIME, 'MM/DD/YYYY HH:MI:SS AM') = to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM') " +
			" and  to_char(a.ENDTIME, 'MM/DD/YYYY HH:MI:SS AM') = to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM') " +
			" and a.zone = t.zone_code group by t.base_date,  to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM') ," +
			" to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM') , " +
			"to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM') ,  t.zone_code, t.capacity";
	
	private static String GET_ORDERSBY_DATE_CUTOFF = "select sum(order_count) order_count, zone,cutoff from mis.order_rate r where delivery_date = to_date(?,'mm/dd/yyyy')  " +
			"group by zone,cutoff  order by zone,cutoff asc";
	
	private static String GET_ORDERSBY_ZONE_TIMESLOT = "select sum(order_count) order_count, zone, timeslot_start, cutoff from mis.order_rate r where r.delivery_date = to_date(?,'mm/dd/yyyy') " +
			" and zone = ? group by zone, timeslot_start, cutoff  order by zone, timeslot_start, cutoff asc";
	
	private static String GET_RESOURCES_DATE_CUTOFF = "select sum(resource_count) resource_count, area, cutoff_datetime  " +
			"from transp.wave_instance w where delivery_date = to_date(?,'mm/dd/yyyy') and status = 'SYN' " +
			"group by area,cutoff_datetime   " +
			"order by area,cutoff_datetime  asc";
	
	
	private static String GET_CURRENT_CAPACITY_CUTOFF = "select sum(capacity) capacity,zone,cutoff from mis.order_rate r where r.delivery_date = to_date(?,'mm/dd/yyyy') AND r.snapshot_time = " +
			"(select max(snapshot_time) from mis.order_rate where DELIVERY_DATE = r.delivery_date and cutoff = r.cutoff) group by zone,cutoff";

	private static String GET_CURRENT_CAPACITY_TIMESLOT = "select sum(capacity) capacity, sum(orders_expected) projected, zone, timeslot_start, cutoff from mis.order_rate r where r.delivery_date = to_date(?,'mm/dd/yyyy') and zone =? AND r.snapshot_time = " +
			"(select max(snapshot_time) from mis.order_rate where DELIVERY_DATE = r.delivery_date and cutoff = r.cutoff) group by zone, timeslot_start, cutoff";
	
	private static String GET_PROJECTED_ORDERS_CUTOFF= "select sum(orders_expected) projected,zone,cutoff from mis.order_rate r where r.delivery_date = to_date(?,'mm/dd/yyyy') AND r.snapshot_time = " +
			"(select max(snapshot_time) from mis.order_rate where DELIVERY_DATE = r.delivery_date and cutoff = r.cutoff) group by zone,cutoff";
	
	
	
	private static final String ORDER_RATE_SNAPSHOT_INSERT = "INSERT INTO MIS.ORDER_RATE(CAPACITY, ZONE, CUTOFF, " +
			"TIMESLOT_START, TIMESLOT_END, ORDER_COUNT,PROJECTED_COUNT, DELIVERY_DATE, SNAPSHOT_TIME, ACTUAL_SO, PROJECT_SO, WEIGHTED_PROJECTED_COUNT, ORDERS_EXPECTED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String AVG_ORDER_RATE_QUERY = "select order_count, snapshot_time from ((select order_count, snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM') = ?  and zone = ?) union (select order_count,snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM')  = ? and zone = ?))";
	
	private static final String CURRENT_ORDER_COUNT = "select sum(o.order_count) oCount, o.timeslot_start, o.timeslot_end, o.zone  " +
		"from MIS.order_rate o where delivery_date >= ? group by o.timeslot_start, o.timeslot_end, o.zone";
    
	private static final String HOLIDAY_QUERY = "select sum(order_count) as oCount, delivery_date from MIS.order_rate group by delivery_date having sum(order_count) = 0";

	private static final String CAPACITY_QUERY = "select capacity,order_count, " +
			"delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date >= ?";
	
	private static final String CAPACITY_QUERY_FORECAST = "select capacity,order_count, " +
			"delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date in ( ?, ?) and zone = ?";
			
	private static final String CURRENT_DATE_ORDER_RATE = "select  snapshot_time, zone, cutoff, sum(order_count) order_count, " +
			"sum(capacity) capacity from mis.order_rate where to_char(snapshot_time,'mm/dd/yyyy') =  ? and delivery_date = to_date(?,'mm/dd/yyyy') " +
			"and zone = ? group by snapshot_time, cutoff, zone order by snapshot_time, cutoff, zone asc";
	
	private static final String MAX_SNAPSHOT_DELIVERY_DATE = "select o.capacity, o.snapshot_time, o.timeslot_start, o.timeslot_end, o.cutoff, o.zone from mis.order_rate o " +
			"where o.delivery_date = to_date(?,'mm/dd/yyyy') and o.zone = ? and o.snapshot_time = (select max(snapshot_time) snapshot from mis.order_rate o1 where " +
			"o1.delivery_date =o.delivery_date and o1.cutoff=o.cutoff)";
	
	private static final String ORDER_COUNT_QRY = "select sum(order_count) as oCount, zone,cutoff " +
			" from MIS.order_rate where delivery_date = to_date(?,'mm/dd/yyyy') and zone = ? and trunc(snapshot_time) < to_date(?,'mm/dd/yyyy') group by zone,cutoff";
	
			
	public static List<OrderRateVO> getCurrentOrdersByZoneCutoff(Connection conn, String deliveryDate)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		try {
				ps = conn.prepareStatement(GET_ORDERSBY_DATE_CUTOFF);
				ps.setString(1, deliveryDate);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setOrderCount(rs.getFloat("order_count"));
					vo.setZone(rs.getString("zone"));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	public static List<OrderRateVO> getResourcesByZoneCutoff(Connection conn, String deliveryDate)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		try {
				ps = conn.prepareStatement(GET_RESOURCES_DATE_CUTOFF);
				ps.setString(1, deliveryDate);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setCapacity(rs.getInt("resource_count"));
					vo.setZone(rs.getString("area"));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff_datetime").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff_datetime").getTime())));
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	
	
	public static List<OrderRateVO> getCurrentOrdersByZoneTimeslot(Connection conn, String deliveryDate, String zone)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		if(zone ==null ||"".equals(zone))
			zone = DEFAULT_ZONE;
		try {
				ps = conn.prepareStatement(GET_ORDERSBY_ZONE_TIMESLOT);
				ps.setString(1, deliveryDate);
				ps.setString(2, zone);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setOrderCount(rs.getFloat("order_count"));
					vo.setZone(rs.getString("zone"));
					vo.setStartTime(new Date(rs.getTimestamp("timeslot_start").getTime()));
					vo.setStartTimeFormatted(df.format(new Date(rs.getTimestamp("timeslot_start").getTime())));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	
	
	public static List<OrderRateVO> getCapacityByZoneCutoff(Connection conn, String deliveryDate)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		
		try {
				ps = conn.prepareStatement(GET_CURRENT_CAPACITY_CUTOFF);
				ps.setString(1, deliveryDate);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setCapacity(rs.getInt("capacity"));
					vo.setZone(rs.getString("zone"));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	private static final String DEFAULT_ZONE = "002";
	
	public static List<OrderRateVO> getCapacityByZoneTimeslot(Connection conn, String deliveryDate, String zone)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		if(zone ==null ||"".equals(zone))
			zone = DEFAULT_ZONE;
		try {
				ps = conn.prepareStatement(GET_CURRENT_CAPACITY_TIMESLOT);
				ps.setString(1, deliveryDate);
				ps.setString(2, zone);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setCapacity(rs.getInt("capacity"));
					vo.setOrderCount(rs.getInt("projected"));
					vo.setZone(rs.getString("zone"));
					vo.setStartTime(new Date(rs.getTimestamp("timeslot_start").getTime()));
					vo.setStartTimeFormatted(df.format(new Date(rs.getTimestamp("timeslot_start").getTime())));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	
	public static List<OrderRateVO> getProjectedOrdersByZoneCutoff(Connection conn, String deliveryDate)
	{
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("hh:mm a");
		try {
			LOGGER.info("Running query"+GET_PROJECTED_ORDERS_CUTOFF);
			
				ps = conn.prepareStatement(GET_PROJECTED_ORDERS_CUTOFF);
				ps.setString(1, deliveryDate);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setOrderCount(rs.getFloat("projected"));
					vo.setZone(rs.getString("zone"));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
					
					voList.add(vo);
				                                                                                                                        
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
		return voList;
			
		
	}
	
	
	public static Map getOrderRates(Connection conn, Date timeStamp)
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
					vo.setCutoffTime(new Date(rs.getTimestamp("cutofftime").getTime()));
					vo.setStartTime(new Date(rs.getTimestamp("starttime").getTime()));
					vo.setEndTime(new Date(rs.getTimestamp("endtime").getTime()));
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
	
	public static Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> getCapacityMap(Connection conn, Date minDate)
	{
		PreparedStatement ps =null;ResultSet rs = null;
		Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> capacityMap = new HashMap<DateRangeVO,  Map<String, Map<Date, Integer[]>>>();
		
		try
		{
			ps = conn.prepareStatement(CAPACITY_QUERY);
			ps.setDate(1, new java.sql.Date(minDate.getTime()));
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				DateRangeVO range = new DateRangeVO(new Date(rs.getTimestamp("timeslot_start").getTime()), new Date(rs.getTimestamp("timeslot_end").getTime()));
				String zone = rs.getString("zone");
				Date snapshot = rs.getTimestamp("snapshot_time");
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
						Map map = new HashMap<Date, Integer>();
						map.put(snapshot, metrics);
						capacityMap.get(range).put(zone, map);
					}
				}
				else
				{
					Map map = new HashMap<Date, Integer>();
					map.put(snapshot, metrics);
					Map zoneMap = new HashMap<String,Map<Date, Integer>>();
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
	
	

	private static Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> getForecastCapacityMap(Connection conn, Date day1, Date day2, String zone)
	{
		PreparedStatement ps =null;ResultSet rs = null;
		Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> capacityMap = new HashMap<DateRangeVO,  Map<String, Map<Date, Integer[]>>>();
		
		try
		{
			ps = conn.prepareStatement(CAPACITY_QUERY_FORECAST);
			ps.setDate(1, new java.sql.Date(day1.getTime()));
			ps.setDate(2, new java.sql.Date(day2.getTime()));
			ps.setString(3, zone);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				DateRangeVO range = new DateRangeVO(new Date(rs.getTimestamp("timeslot_start").getTime()), new Date(rs.getTimestamp("timeslot_end").getTime()));
				Date snapshot = rs.getTimestamp("snapshot_time");
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
						Map map = new HashMap<Date, Integer>();
						map.put(snapshot, metrics);
						capacityMap.get(range).put(zone, map);
					}
				}
				else
				{
					Map map = new HashMap<Date, Integer>();
					map.put(snapshot, metrics);
					Map zoneMap = new HashMap<String,Map<Date, Integer>>();
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
	
	
	public static Map<Date, Float> getAverageOrderRate(Connection conn, Date snapshot7, Date startTime7, Date endTime7,
			 Date snapshot14, Date startTime14, Date endTime14, OrderRateVO vo)
	{
		long starttime = System.currentTimeMillis();
		Map<Date, Float> map = null;
		PreparedStatement ps =null;ResultSet rs = null;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try {
		
		ps = conn.prepareStatement(AVG_ORDER_RATE_QUERY);
		ps.setString(1, sdf.format(snapshot7));
		ps.setString(2, sdf.format(startTime7));
		ps.setString(3,vo.getZone());
		ps.setString(4, sdf.format(snapshot14));
		ps.setString(5, sdf.format(startTime14));
		ps.setString(6,vo.getZone());
		
		rs = ps.executeQuery();
		
		map = new HashMap<Date, Float>();
		
		while(rs.next())
		{
			map.put(new Date(rs.getTimestamp("snapshot_time").getTime()), rs.getFloat("order_count"));
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
		Map<DateRangeVO,  Map<String, Integer>> orderCountMap = new HashMap<DateRangeVO,  Map<String, Integer>>();
		String zone = null;
		PreparedStatement ps =null;ResultSet rs = null;Integer orderCount = 0;
		try {
			ps = conn.prepareStatement(CURRENT_ORDER_COUNT);
			ps.setDate(1, new java.sql.Date(minDate.getTime()));
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				DateRangeVO range = new DateRangeVO(new Date(rs.getTimestamp("timeslot_start").getTime()), new Date(rs.getTimestamp("timeslot_end").getTime()));
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
	
	private static OrderRateVO copyBaseAttributes(OrderRateVO vo)
	{
		DateFormat tdf = new SimpleDateFormat("hh:mm a");

		OrderRateVO voCopy = new OrderRateVO();
		voCopy.setBaseDate(vo.getBaseDate());
		voCopy.setSnapshotTime(vo.getSnapshotTime());
		voCopy.setSnapshotTimeFmt(vo.getSnapshotTimeFmt());
		voCopy.setStartTime(vo.getStartTime());
		voCopy.setEndTime(vo.getEndTime());
		voCopy.setCutoffTime(vo.getCutoffTime());
		voCopy.setCapacity(vo.getCapacity());
		voCopy.setZone(vo.getZone());
		voCopy.setStartTimeFormatted(tdf.format(vo.getStartTime()));
		voCopy.setCutoffTimeFormatted(tdf.format(vo.getCutoffTime()));

		return voCopy;
	}
	public static List<OrderRateVO> getForecastData(Connection conn, String deliveryDate, String zone,Date day1,Date day2) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderRateVO> inputList = new ArrayList<OrderRateVO>();
		List<OrderRateVO> outputList = new ArrayList<OrderRateVO>();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		if(zone ==null ||"".equals(zone))
			zone = DEFAULT_ZONE;
		
		try {
			Date date = df.parse(deliveryDate);
			Date snapshot = null;
			Map<DateRangeVO, Map<String, Map<Date, Integer[]>>> forecastMap = getForecastCapacityMap(conn, day1, day2, zone);
			
			ps = conn.prepareStatement(MAX_SNAPSHOT_DELIVERY_DATE);
			ps.setString(1, deliveryDate);
			ps.setString(2, zone);
			rs = ps.executeQuery();
			while(rs.next())
			{
				OrderRateVO vo = new OrderRateVO();
				vo.setBaseDate(date);
				snapshot = new Date(rs.getTimestamp("snapshot_time").getTime());
				vo.setSnapshotTime(snapshot);
				vo.setSnapshotTimeFmt(sdf.format(snapshot));
				vo.setStartTime(new Date(rs.getTimestamp("timeslot_start").getTime()));
				vo.setEndTime(new Date(rs.getTimestamp("timeslot_end").getTime()));
				vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
			
				vo.setCapacity(rs.getInt("capacity"));
				vo.setZone(rs.getString("zone"));
				inputList.add(vo);
				
			}
				
			Iterator<OrderRateVO> voIterator = inputList.iterator();
			
			while(voIterator.hasNext())
			{
				OrderRateVO tempVO = voIterator.next();
				boolean done =false;
				
				
				
				int days1 = (int)DateUtil.diffInDays(tempVO.getBaseDate(), day1) * -1;
				int days2 = (int)DateUtil.diffInDays(tempVO.getBaseDate(), day2) * -1;
				Date snapshot7 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days1);
				Date startTime7 = OrderRateUtil.getDate(tempVO.getStartTime(),days1);
				Date endTime7 = OrderRateUtil.getDate(tempVO.getEndTime(),days1);
				Date snapshot14 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days2);
				Date startTime14 = OrderRateUtil.getDate(tempVO.getStartTime(), days2);
				Date endTime14 = OrderRateUtil.getDate(tempVO.getEndTime(),days2);

				snapshot = tempVO.getSnapshotTime();
				DateRangeVO range7 = new DateRangeVO(startTime7, endTime7);
				DateRangeVO range14 = new DateRangeVO(startTime14, endTime14);
				while(!done)
				{
					
					OrderRateVO outputVO = copyBaseAttributes(tempVO);	
					
					snapshot =  OrderRateUtil.addTime(snapshot);
					snapshot7 = OrderRateUtil.addTime(snapshot7);
					snapshot14 = OrderRateUtil.addTime(snapshot14);
					
					
					if(forecastMap.get(range7)!=null && forecastMap.get(range14)!=null
							 && forecastMap.get(range7).get(outputVO.getZone())!=null 
							&& forecastMap.get(range14).get(outputVO.getZone())!=null
							 && forecastMap.get(range7).get(outputVO.getZone()).get(snapshot7)!=null
							 && forecastMap.get(range14).get(outputVO.getZone()).get(snapshot14)!=null)
				
		
						outputVO.setProjectedRate(OrderRateUtil.roundValue( new Float(forecastMap.get(range7).get(outputVO.getZone()).get(snapshot7)[1] 
								+ forecastMap.get(range14).get(outputVO.getZone()).get(snapshot14)[1]) / 2));
					
					else
						outputVO.setProjectedRate(0);
					
					
					float weightedProjection = 0;
			
					if(forecastMap.get(range7)!=null && forecastMap.get(range14)!=null
							 && forecastMap.get(range7).get(outputVO.getZone())!=null 
							&& forecastMap.get(range14).get(outputVO.getZone())!=null
							 && forecastMap.get(range7).get(outputVO.getZone()).get(snapshot7)!=null
							 && forecastMap.get(range14).get(outputVO.getZone()).get(snapshot14)!=null
							 && (forecastMap.get(range7).get(outputVO.getZone()).get(snapshot7)[0] + forecastMap.get(range14).get(outputVO.getZone()).get(snapshot14)[0] > 0) )
					
							weightedProjection = OrderRateUtil.roundValue(new Float( outputVO.getProjectedRate() * outputVO.getCapacity() /
									((forecastMap.get(range7).get(outputVO.getZone()).get(snapshot7)[0]+ forecastMap.get(range14).get(outputVO.getZone()).get(snapshot14)[0])/2)));
					
					else
							weightedProjection = 0;
					
					outputVO.setWeightedProjectRate(weightedProjection);
		
					
					if(!(forecastMap.get(range7)!=null && forecastMap.get(range14)!=null
							 && forecastMap.get(range7).get(outputVO.getZone())!=null 
								&& forecastMap.get(range14).get(outputVO.getZone())!=null ))
						done = true;
					
					if(snapshot.getTime()>outputVO.getCutoffTime().getTime())
						done = true;
					
					outputVO.setSnapshotTime(snapshot);
					outputVO.setSnapshotTimeFmt(sdf.format(snapshot));
					
					outputList.add(outputVO);
					
				}	
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
		 return outputList;
		
	}
	
	
	
	public static List<OrderRateVO> getCurrentOrderRateBySnapshot(Connection conn, String currentDate,String deliveryDate,  String zone) throws ParseException 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		DateFormat df = new SimpleDateFormat("hh:mm a");
		if(zone ==null ||"".equals(zone))
			zone = DEFAULT_ZONE;
		List<OrderRateVO> dataList = new ArrayList<OrderRateVO>();
		 try {
		    	ps = conn.prepareStatement(CURRENT_DATE_ORDER_RATE);
		    	ps.setString(1, currentDate);
		    	ps.setString(2, deliveryDate);
		    	ps.setString(3, zone);
		    	rs = ps.executeQuery();
		    	Date snapshotTime = null;
		    	while(rs.next())
		    	{
		    		OrderRateVO data = new OrderRateVO();
		    		data.setOrderCount(rs.getFloat("order_count"));
		    		data.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    		snapshotTime = new Date(rs.getTimestamp("snapshot_time").getTime());
		    		data.setSnapshotTime(snapshotTime);
		    		data.setSnapshotTimeFmt(sdf.format(snapshotTime));
		    		data.setZone(rs.getString("zone"));
		    		data.setCapacity(rs.getInt("capacity"));
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
	public static List<OrderData> getOrderCount(Connection conn, String currentDate,String deliveryDate,  String zone) throws ParseException 
	{
			 
		PreparedStatement ps = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		List<OrderData> dataList = new ArrayList<OrderData>();
		DateFormat df = new SimpleDateFormat("hh:mm a");
		if(zone ==null ||"".equals(zone))
			zone = DEFAULT_ZONE;
		 try {	
	
			ps = conn.prepareStatement(ORDER_COUNT_QRY);
			ps.setString(1, deliveryDate);
			ps.setString(2, zone);
			ps.setString(3, currentDate);
			rs = ps.executeQuery();
			while(rs.next())
			{
				OrderData data = new OrderData();
				data.setOrderCount(rs.getFloat("oCount"));
				data.setCutoff(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
				data.setZone(rs.getString("zone"));
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
	
	/*public static void main(String s[]) throws SQLException, NamingException, IOException
	{
		try
		{
		
		Connection conn = getDataSource().getConnection();
		String dayStr1 = "11/21/2011";
		String dayStr2 = "11/22/2011";
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date day1 = df.parse(dayStr1);
		Date day2 = df.parse(dayStr2);
		List<OrderRateVO> datalist = getForecastData(conn, "12/15/2011", "515", day1, day2);
		print(datalist);
		}
		catch(Exception e)
		{
			
		}
		
	}

	private static void print(List<OrderRateVO> volist)
	{	System.out.println("snapshot\tzone\ttimeslot_start\ttimeslot_end\tcutoff\tprojected_rate\tweighted_projected_rate");
		for(OrderRateVO vo: volist )
		{
		
			System.out.println(vo.getSnapshotTimeFmt()+"\t"+vo.getZone()+"\t"+vo.getStartTime()+"\t"+vo.getEndTime()+"\t"+vo.getCutoffTime()+"\t"+vo.getProjectedRate()+"\t"+vo.getWeightedProjectRate());
		}
	}
	public static DataSource getDataSource() throws NamingException {
	        InitialContext initCtx = null;
	    try {
	        initCtx = getInitialContext();
	        return (DataSource) initCtx.lookup("fddatasource");
	    } finally {
	        if (initCtx!=null) try {
	              initCtx.close();
	        } catch (NamingException ne) {}
	    }
	 }
	   static public InitialContext getInitialContext() throws NamingException {
	        Hashtable h = new Hashtable();
	        h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	        h.put(Context.PROVIDER_URL, "t3://localhost:7001");//"t3://app01.stprd01.nyc2.freshdirect.com:7001"
	        return new InitialContext(h);
	  }
	   
	   */
	
}
