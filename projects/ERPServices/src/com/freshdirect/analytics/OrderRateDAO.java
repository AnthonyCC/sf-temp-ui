package com.freshdirect.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
	
	
	
	private static final String ORDER_RATE_QUERY_BY_TIMESLOT_SNAPSHOT = "select t.base_date , t.starttime , t.capacity, t.endtime , t.zone_code zone ,t.cutofftime, " +
			"t.order_count - nvl(q2.order_count,0)  as order_count from (select nvl(order_count,0) order_count, t.* from  (select t.base_date, " +
			"to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.START_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') starttime, " +
			"to_date(to_char(t.base_date, 'MM/DD/YYYY')||' '||to_char(T.END_TIME, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') endtime, t.capacity, " +
			"to_date(to_char(t.base_date-1, 'MM/DD/YYYY')||' '|| to_char(t.cutoff_time, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') cutofftime, z.zone_code " +
			"from dlv.timeslot t, dlv.zone z where t.zone_ID = z.ID and t.base_date > SYSDATE ) t ,  ( select count(*) order_count, di.starttime, di.endtime, " +
			"di.cutofftime, SA.REQUESTED_DATE, di.zone from  cust.sale s, cust.salesaction sa, cust.deliveryinfo di WHERE s.ID=sa.SALE_ID AND s.CUSTOMER_ID=sa.CUSTOMER_ID " +
			"AND DI.SALESACTION_ID = SA.ID AND sa.ACTION_TYPE IN ('CRO','MOD') AND sa.REQUESTED_DATE > TRUNC(SYSDATE) AND s.type='REG' and s.status <>'CAN' " +
			"and S.CROMOD_DATE = SA.ACTION_DATE AND S.CROMOD_DATE <=?  group by di.starttime, di.endtime, di.cutofftime, SA.REQUESTED_DATE, di.zone) q1 " +
			"where t.starttime  = q1.STARTTIME(+) and  t.endtime = q1.endtime(+) and t.zone_code = q1.zone(+) ) t, (  select  sum(order_count) order_count, " +
			"o.timeslot_start, o.timeslot_end, o.cutoff, o.delivery_date, o.zone from mis.order_rate  o where delivery_date > trunc( sysdate) and " +
			"delivery_date <= ? group by o.timeslot_start, o.timeslot_end, o.cutoff, o.delivery_date, o.zone) q2 where t.starttime = q2.timeslot_start(+) " +
			"and t.endtime = q2.timeslot_end(+) and t.zone_code = q2.zone(+)";
	
	private static final String MAX_DELIVERY_DATE = "select max(delivery_date) from mis.order_rate";
	
	private static final String ORDER_RATE_SNAPSHOT_INSERT = "INSERT INTO MIS.ORDER_RATE(CAPACITY, ZONE, CUTOFF, " +
			"TIMESLOT_START, TIMESLOT_END, ORDER_COUNT,PROJECTED_COUNT, DELIVERY_DATE, SNAPSHOT_TIME, ACTUAL_SO, PROJECT_SO, WEIGHTED_PROJECTED_COUNT, ORDERS_EXPECTED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String AVG_ORDER_RATE_QUERY = "select order_count, snapshot_time from ((select order_count, snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM') = ?  and zone = ?) union (select order_count,snapshot_time from MIS.order_rate where to_char(snapshot_time,'MM/DD/YYYY HH:MI:SS AM') " +
			">=  ? and to_char(timeslot_start,'MM/DD/YYYY HH:MI:SS AM')  = ? and zone = ?))";
	
	private static final String CURRENT_ORDER_COUNT = "select sum(o.order_count) oCount, o.timeslot_start, o.timeslot_end, o.zone  " +
		"from MIS.order_rate o where delivery_date >= ? and delivery_date <= ? group by o.timeslot_start, o.timeslot_end, o.zone";
    
	private static final String HOLIDAY_QUERY = "select sum(order_count) as oCount, delivery_date from MIS.order_rate group by delivery_date having sum(order_count) = 0";
	
	private static final String CAPACITY_QUERY = "select capacity,order_count, " +
			"delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date in (%s)";
	
	
	private static java.sql.Date getMaxDeliveryDate(Connection conn)
	{
		
		PreparedStatement ps =null;ResultSet rs = null;
		try {
			
		ps = conn.prepareStatement(MAX_DELIVERY_DATE);
		rs = ps.executeQuery();
		if(rs.next())
			{
				return rs.getDate(1);
			}
		}
		catch(Exception e)
		{
			System.out.println("error while getting the max date");
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
		
		return null;
		
		
	}
	public static Map getOrderRates(Connection conn, Date snapshotTime)
	{
		
		
		List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		PreparedStatement ps =null;ResultSet rs = null;
		Map returnMap = new HashMap();
		Set<Date> baseDates = new HashSet<Date>();
		java.sql.Date maxDate = getMaxDeliveryDate(conn);
		try {
				ps = conn.prepareStatement(ORDER_RATE_QUERY_BY_TIMESLOT_SNAPSHOT);
				ps.setTimestamp(1, new java.sql.Timestamp(snapshotTime.getTime()));
				ps.setDate(2, maxDate);
				rs = ps.executeQuery();
				
				while(rs.next())
				{
					OrderRateVO vo = new OrderRateVO();
					vo.setCapacity(rs.getInt("capacity"));
					vo.setZone(rs.getString("zone"));
					vo.setCutoffTime(new Date(rs.getTimestamp("cutofftime").getTime()));
					vo.setStartTime(new Date(rs.getTimestamp("starttime").getTime()));
					vo.setEndTime(new Date(rs.getTimestamp("endtime").getTime()));
					vo.setOrderCount(rs.getFloat("order_count"));
					vo.setBaseDate(rs.getDate("base_date"));
					vo.setSnapshotTime(snapshotTime);
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
	
	public static String preparePlaceHolders(int length) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < length;) {
	        builder.append("?");
	        if (++i < length) {
	            builder.append(",");
	        }
	    }
	    return builder.toString();
	}

	public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
	    for (int i = 0; i < values.length; i++) {
	        preparedStatement.setObject(i + 1, values[i]);
	    }
	}
	
	public static Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> getCapacityMap(Connection conn, List sampleDates)
	{
		PreparedStatement ps =null;ResultSet rs = null;
		Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> capacityMap = new HashMap<DateRangeVO,  Map<String, Map<Date, Integer[]>>>();
		
		try
		{
			String sql = String.format(CAPACITY_QUERY, preparePlaceHolders(sampleDates.size()));
			ps = conn.prepareStatement(sql);
			setValues(ps, sampleDates.toArray());
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
		
		return map;
	}
	
	public static Map<DateRangeVO,  Map<String, Integer>> getOrderCount(Connection conn, Set<Date> baseDates)
	{
		
		
		Map<DateRangeVO,  Map<String, Integer>> orderCountMap = new HashMap<DateRangeVO,  Map<String, Integer>>();
		String zone = null;
		PreparedStatement ps =null;ResultSet rs = null;Integer orderCount = 0;
		try {
			ps = conn.prepareStatement(CURRENT_ORDER_COUNT);
			Date minDate = Collections.min(baseDates);
			Date maxDate = Collections.max(baseDates);
			ps.setDate(1, new java.sql.Date(minDate.getTime()));
			ps.setDate(2, new java.sql.Date(maxDate.getTime()));
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
	private static final String DELETE_EXCEPTIONS = "DELETE FROM MIS.ORDER_RATE_HOLIDAY";
	private static final String INSERT_EXCEPTIONS = "INSERT INTO MIS.ORDER_RATE_HOLIDAY(DELIVERY_DATE) VALUES (?)";
	
	
	public static void saveExceptions(Connection conn, Set<Date> dates) {
		PreparedStatement ps =null;
		try
		{
			ps = conn.prepareStatement(DELETE_EXCEPTIONS);
			ps.executeUpdate();
			ps.close();
			ps = conn.prepareStatement(INSERT_EXCEPTIONS);
			for(Date date: dates)
			{
				ps.setDate(1, new java.sql.Date(date.getTime()));
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
