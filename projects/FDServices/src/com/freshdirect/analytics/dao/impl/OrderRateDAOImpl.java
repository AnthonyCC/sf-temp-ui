package com.freshdirect.analytics.dao.impl;

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

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.analytics.dao.IOrderRateDAO;
import com.freshdirect.analytics.model.DateRangeVO;
import com.freshdirect.analytics.model.OrderData;
import com.freshdirect.analytics.model.OrderRateVO;
import com.freshdirect.analytics.model.PlantDispatchData;
import com.freshdirect.analytics.util.DateUtil;
import com.freshdirect.analytics.util.OrderRateUtil;

public class OrderRateDAOImpl implements IOrderRateDAO {

	private static final Category LOGGER = Logger.getLogger(OrderRateDAOImpl.class);
		
	private static String GET_ORDERSBY_DATE_CUTOFF = "select count(*) order_count, di.zone zone ,di.cutofftime cutoff from cust.sale s , cust.salesaction sa," +
			" cust.deliveryinfo di where s.id = sa.sale_id and s.cromod_date = sa.action_date and s.type='REG' and s.status <>'CAN' AND S.CROMOD_DATE<sysdate and sa.action_type " +
			"in ('CRO','MOD') and sa.requested_date = to_date(?,'mm/dd/yyyy') and sa.id = di.salesaction_id   group by di.zone, di.cutofftime  " +
			"order by di.zone, di.cutofftime asc";
	
	private static String GET_ORDERSBY_ZONE_TIMESLOT = "select count(*) order_count, di.zone zone ,di.starttime timeslot_start, di.cutofftime cutoff from " +
			"cust.sale s , cust.salesaction sa, cust.deliveryinfo di where s.id = sa.sale_id and s.cromod_date = sa.action_date and s.type='REG' and " +
			"s.status <>'CAN' AND S.CROMOD_DATE<sysdate and sa.action_type in ('CRO','MOD') and sa.requested_date = to_date(?,'mm/dd/yyyy') and zone = ? and sa.id = di.salesaction_id" +
			"  group by zone, di.starttime,cutofftime  order by di.zone,di.starttime, di.cutofftime asc";
	
	private static String GET_RESOURCES_DATE_CUTOFF = "select sum(resource_count) resource_count, area, cutoff_datetime  " +
			"from transp.wave_instance w where delivery_date = to_date(?,'mm/dd/yyyy') and status = 'SYN' " +
			"group by area,cutoff_datetime   " +
			"order by area,cutoff_datetime  asc";
	
	
	private static String GET_CURRENT_CAPACITY_CUTOFF = "select sum(capacity) capacity,zone,cutoff from mis.order_rate o, (select max(snapshot_time) sh, " +
			"O.CUTOFF co from mis.order_rate o where o.delivery_date = to_date(?,'mm/dd/yyyy') group by O.CUTOFF) t  where o.delivery_date = " +
			"to_date(?,'mm/dd/yyyy') AND o.snapshot_time = t.sh and o.cutoff = t.co group by zone, cutoff order by zone, cutoff";

	private static String GET_CURRENT_CAPACITY_TIMESLOT = "select sum(capacity) capacity, sum(orders_expected) projected, zone, timeslot_start, cutoff from mis.order_rate o," +
			"(select max(snapshot_time) sh, O.CUTOFF co from mis.order_rate o where o.delivery_date = to_date(?,'mm/dd/yyyy') and " +
			"o.zone = ? group by O.CUTOFF) t  where o.delivery_date = to_date(?,'mm/dd/yyyy') and zone =? AND " +
			"o.snapshot_time = t.sh and o.cutoff = t.co group by zone, timeslot_start, cutoff order by zone, timeslot_start, cutoff";
	
	private static String GET_PROJECTED_ORDERS_CUTOFF= "select sum(orders_expected) projected,zone,cutoff from mis.order_rate o, (select max(snapshot_time) sh, " +
			"O.CUTOFF co from mis.order_rate o where o.delivery_date = to_date(?,'mm/dd/yyyy') group by O.CUTOFF) t  where o.delivery_date = " +
			"to_date(?,'mm/dd/yyyy')   AND o.snapshot_time = t.sh and o.cutoff = t.co group by zone,cutoff order by zone,cutoff";
	
	private static final String EXCEPTION_QUERY = "select delivery_date from MIS.ORDER_RATE_EXCEPTIONS";
		
	private static final String CAPACITY_QUERY_FORECAST = "select capacity,order_count, " +
			"delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date in ( ?, ?) and " +
			" ((snapshot_time between ? and ?) or (snapshot_time between ? and ?)) and zone = ?";
	
	private static final String CAPACITY_QUERY_FORECAST_EX = "select sum(capacity) capacity,sum(order_count) order_count, snapshot_time from MIS.order_rate where delivery_date in" +
			" (?,?) and  ((snapshot_time between ? and ?) or (snapshot_time between ? and ?)) group by snapshot_time order by snapshot_time asc";
	
	
	private static final String CURRENT_DATE_ORDER_RATE = "select  snapshot_time, zone, cutoff, sum(order_count) order_count, " +
			"sum(capacity) capacity from mis.order_rate where snapshot_time > to_date(?,'mm/dd/yyyy')  and snapshot_time < to_date(?,'mm/dd/yyyy') and delivery_date = to_date(?,'mm/dd/yyyy') " +
			"and zone = ? group by snapshot_time, cutoff, zone order by snapshot_time, cutoff, zone asc";
	
	private static final String CURRENT_DATE_ORDER_RATE_EX = "select  snapshot_time, sum(order_count) order_count, sum(capacity) capacity from mis.order_rate " +
			"where snapshot_time > to_date(?,'mm/dd/yyyy')  and snapshot_time < to_date(?,'mm/dd/yyyy') and delivery_date = to_date( ?,'mm/dd/yyyy') group by snapshot_time order by snapshot_time asc";
	
	private static final String MAX_SNAPSHOT_DELIVERY_DATE = "select o.capacity, o.snapshot_time, o.timeslot_start, o.timeslot_end, o.cutoff, o.zone from " +
			"mis.order_rate o ,(select max(snapshot_time) sh, O.CUTOFF co from mis.order_rate o where o.delivery_date = to_date(?,'mm/dd/yyyy') " +
			"and o.zone = ? and snapshot_time >=to_date(?,'mm/dd/yyyy') group by O.CUTOFF) t where o.delivery_date = to_date(?,'mm/dd/yyyy') and o.zone = ? and O.SNAPSHOT_TIME=t.sh " +
			"and O.CUTOFF=t.co";
	
	private static final String MAX_SNAPSHOT_DELIVERY_DATE_EX = "select sum(o.capacity) capacity, max(o.snapshot_time) snapshot_time from mis.order_rate o , " +
			"(select max(o1.snapshot_time) sh,o1.cutoff co  from mis.order_rate o1 where o1.delivery_date = to_date(?,'mm/dd/yyyy') and " +
			"o1.snapshot_time >=to_date(?,'mm/dd/yyyy') group by o1.cutoff) t where o.delivery_date = to_date(?,'mm/dd/yyyy') and " +
			"O.SNAPSHOT_TIME=t.sh and O.CUTOFF=t.co";
	
	private static final String ORDER_COUNT_QRY = "select sum(order_count) oCount, o.zone, o.cutoff from mis.order_rate o where o.snapshot_time " +
			"<=to_date(?,'mm/dd/yyyy') and o.delivery_date = to_date(?,'mm/dd/yyyy') and o.zone = ? group by  o.zone,o.cutoff order by o.zone,o.cutoff asc";
	
	private static final String ORDER_COUNT_QRY_EX = "select sum(order_count) oCount from mis.order_rate o where o.snapshot_time " +
			"<=to_date(?,'mm/dd/yyyy') and o.delivery_date = to_date(?,'mm/dd/yyyy')";


	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public List<OrderRateVO> getCurrentOrdersByZoneCutoff(final String deliveryDate)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ORDERSBY_DATE_CUTOFF);
	                ps.setString(1, deliveryDate);
	                return ps;
	            }
	        };
				
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
		       		    	{
		    	
		       		    		OrderRateVO vo = new OrderRateVO();
		    					vo.setOrderCount(rs.getFloat("order_count"));
		    					vo.setZone(rs.getString("zone"));
		    					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
		    					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    					voList.add(vo);
		       		    	}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );
		return voList;
			
		
	}
	public List<OrderRateVO> getResourcesByZoneCutoff(final String deliveryDate)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_RESOURCES_DATE_CUTOFF);
	                ps.setString(1, deliveryDate);
	                return ps;
	            }
	        };
				
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
		       		    	{
		    	
		       		    		OrderRateVO vo = new OrderRateVO();
		    					vo.setCapacity(rs.getInt("resource_count"));
		    					vo.setZone(rs.getString("area"));
		    					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff_datetime").getTime()));
		    					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff_datetime").getTime())));
		    					voList.add(vo);
		       		    	}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );
				
		return voList;
			
		
	}
	
	
	public List<OrderRateVO> getCurrentOrdersByZoneTimeslot(final String deliveryDate, final String zone)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		
		final DateFormat df = new SimpleDateFormat("hh:mm a");
	
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ORDERSBY_ZONE_TIMESLOT);
	                ps.setString(1, deliveryDate);
					ps.setString(2, zone);
	                return ps;
	            }
	        };
				
				 jdbcTemplate.query(creator,
			       		  new RowCallbackHandler() 
			        		{
			       		      public void processRow(ResultSet rs) throws SQLException 
			       		      {

			       		    	do 
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
			       		    	   while(rs.next());
			    		
			       		      }
			        		}
			 );
				
		return voList;
			
		
	}
	
	
	public List<OrderRateVO> getCapacityByZoneCutoff(final String deliveryDate)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_CURRENT_CAPACITY_CUTOFF);
	                ps.setString(1, deliveryDate);
					ps.setString(2, deliveryDate);
	                return ps;
	            }
	        };
	   	 jdbcTemplate.query(creator,
	       		  new RowCallbackHandler() 
	        		{
	       		      public void processRow(ResultSet rs) throws SQLException 
	       		      {

	       		    	do 
	       		    	{
	    	
	       		    		OrderRateVO vo = new OrderRateVO();
	    					vo.setCapacity(rs.getInt("capacity"));
	    					vo.setZone(rs.getString("zone"));
	    					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
	    					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
	    					voList.add(vo);
	       		    	}
	       		    	   while(rs.next());
	    		
	       		      }
	        		}
	 );	
				
		return voList;
			
		
	}
	
	public List<OrderRateVO> getCapacityByZoneTimeslot(final String deliveryDate, final String zone)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();
		
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_CURRENT_CAPACITY_TIMESLOT);
	                ps.setString(1, deliveryDate);
					ps.setString(2, zone);
					ps.setString(3, deliveryDate);
					ps.setString(4, zone);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
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
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );	
			
		return voList;
			
		
	}
	
	public List<OrderRateVO> getProjectedOrdersByZoneCutoff(final String deliveryDate)
	{
		final List<OrderRateVO> voList = new ArrayList<OrderRateVO>();

		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_PROJECTED_ORDERS_CUTOFF);
	                ps.setString(1, deliveryDate);
					ps.setString(2, deliveryDate);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
		       		    	{
		    	
		       		    		OrderRateVO vo = new OrderRateVO();
		    					vo.setOrderCount(rs.getFloat("projected"));
		    					vo.setZone(rs.getString("zone"));
		    					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
		    					vo.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    					
		    					voList.add(vo);
		       		    	}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );		
			
		return voList;
			
		
	}
	
	
	private Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> getForecastCapacityMap(final Date day1, final Date day2, final String zone)
	{
		
		final Map<DateRangeVO,  Map<String, Map<Date, Integer[]>>> capacityMap = new HashMap<DateRangeVO,  Map<String, Map<Date, Integer[]>>>();
		final Calendar cal=Calendar.getInstance();
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(CAPACITY_QUERY_FORECAST);
	                ps.setDate(1, new java.sql.Date(day1.getTime()));
	    			ps.setDate(2, new java.sql.Date(day2.getTime()));
	    			cal.setTimeInMillis(day1.getTime());
	    			cal.set(Calendar.DATE, -1);
	    			ps.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
	    			ps.setDate(4, new java.sql.Date(day1.getTime()));
	    			cal.setTimeInMillis(day2.getTime());
	    			cal.set(Calendar.DATE, -1);
	    			ps.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
	    			ps.setDate(6, new java.sql.Date(day2.getTime()));
	    			ps.setString(7, zone);
	                return ps;
	            }
	        };
	        
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
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
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );		
		
		return capacityMap;
	}
	
	private Map<Date, Integer[]> getFDTotalForecastCapacityMap(final Date day1, final Date day2)
	{
		
		final  Map<Date, Integer[]> capacityMap = new HashMap<Date, Integer[]>();
		final Calendar cal=Calendar.getInstance();
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(CAPACITY_QUERY_FORECAST_EX);
	                ps.setDate(1, new java.sql.Date(day1.getTime()));
	    			ps.setDate(2, new java.sql.Date(day2.getTime()));
	    			cal.setTimeInMillis(day1.getTime());
	    			cal.set(Calendar.DATE, -1);
	    			ps.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
	    			ps.setDate(4, new java.sql.Date(day1.getTime()));
	    			cal.setTimeInMillis(day2.getTime());
	    			cal.set(Calendar.DATE, -1);
	    			ps.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
	    			ps.setDate(6, new java.sql.Date(day2.getTime()));
	                return ps;
	            }
	        };
	        
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
		       		    	{
		       		    		Date snapshot = rs.getTimestamp("snapshot_time");
		       					Integer capacity = rs.getInt("capacity");
		       					Integer orders = rs.getInt("order_count");
		       					Integer[] metrics = new Integer[]{capacity,orders};
		       					capacityMap.put(snapshot, metrics);
		       				}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );		
		
		return capacityMap;
	}
	
	public Set<Date>  getExceptions(){
		final Set<Date> exceptions = new HashSet<Date>();
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(EXCEPTION_QUERY);
	               
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() {
		       		      public void processRow(ResultSet rs) throws SQLException{
		       		    	do{
		       		    		exceptions.add(new Date(rs.getDate("delivery_date").getTime()));
		       				} while(rs.next());
		       		      }
		        		}
		 );		
		return exceptions;
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
		if(vo.getStartTime()!=null)
		voCopy.setStartTimeFormatted(tdf.format(vo.getStartTime()));
		if(vo.getCutoffTime()!=null)
		voCopy.setCutoffTimeFormatted(tdf.format(vo.getCutoffTime()));

		return voCopy;
	}
	public List<OrderRateVO> getForecastData(final String deliveryDate, final Date baseDate, final String zone,Date day1,Date day2) 
	{
		
		final List<OrderRateVO> inputList = new ArrayList<OrderRateVO>();
		List<OrderRateVO> outputList = new ArrayList<OrderRateVO>();
		final DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.DATE, -1);
		Date minSnapshotDate = cal.getTime();
		final String minSnapshotDateStr = df.format(minSnapshotDate);
		Date snapshot = null;
		
		if(!"0".equals(zone))
		{
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(MAX_SNAPSHOT_DELIVERY_DATE);
	                ps.setString(1, deliveryDate);
	    			ps.setString(2, zone);
	    			ps.setString(3, minSnapshotDateStr);
	    			ps.setString(4, deliveryDate);
	    			ps.setString(5, zone);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() 
		        		{
		       		      public void processRow(ResultSet rs) throws SQLException 
		       		      {

		       		    	do 
		       		    	{
		       					OrderRateVO vo = new OrderRateVO();
		       					vo.setBaseDate(baseDate);
		   
		       					vo.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
		       					vo.setSnapshotTimeFmt(sdf.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
		       					vo.setStartTime(new Date(rs.getTimestamp("timeslot_start").getTime()));
		       					vo.setEndTime(new Date(rs.getTimestamp("timeslot_end").getTime()));
		       					vo.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
		       				
		       					vo.setCapacity(rs.getInt("capacity"));
		       					vo.setZone(rs.getString("zone"));
		       					inputList.add(vo);
		       					
		       				}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );		
			
			Map<DateRangeVO, Map<String, Map<Date, Integer[]>>> forecastMap = getForecastCapacityMap(day1, day2, zone);
			
			
			
			Iterator<OrderRateVO> voIterator = inputList.iterator();
			
			while(voIterator.hasNext())
			{
				OrderRateVO tempVO = voIterator.next();
				boolean done =false;
				
				
				
				int days1 = (int)DateUtil.getDiffInDays(tempVO.getBaseDate(), day1) * -1;
				int days2 = (int)DateUtil.getDiffInDays(tempVO.getBaseDate(), day2) * -1;
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
		else
		{
			PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(MAX_SNAPSHOT_DELIVERY_DATE_EX);
	                ps.setString(1, deliveryDate);
	    			ps.setString(2, minSnapshotDateStr);
	    			 ps.setString(3, deliveryDate);
	                return ps;
	            }
	        };
			
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler(){
		       		      public void processRow(ResultSet rs) throws SQLException{
		       		    	do {
		       		    		if(rs.getTimestamp("snapshot_time")!=null){
		       					OrderRateVO vo = new OrderRateVO();
		       					vo.setBaseDate(baseDate);
		       					vo.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
		       					vo.setSnapshotTimeFmt(sdf.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
		       					vo.setCapacity(rs.getInt("capacity"));
		       					inputList.add(vo);
		       		    		}
		       				}
		       		    	while(rs.next());
		       		      }
		        		}
		 );		
			
			Map<Date, Integer[]> forecastMap = null;
			
			if(inputList.size()>0) forecastMap = getFDTotalForecastCapacityMap(day1, day2);
			
			Iterator<OrderRateVO> voIterator = inputList.iterator();
			
			while(voIterator.hasNext())
			{
				OrderRateVO tempVO = voIterator.next();
				int days1 = (int)DateUtil.getDiffInDays(baseDate, day1) * -1;
				int days2 = (int)DateUtil.getDiffInDays(baseDate, day2) * -1;
				Date snapshot7 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days1);
				Date snapshot14 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days2);
				
				snapshot = tempVO.getSnapshotTime();
				
				while(snapshot.before(baseDate))
				{
					OrderRateVO outputVO = copyBaseAttributes(tempVO);	
					
					snapshot =  OrderRateUtil.addTime(snapshot);
					snapshot7 = OrderRateUtil.addTime(snapshot7);
					snapshot14 = OrderRateUtil.addTime(snapshot14);
					if(forecastMap.get(snapshot7)!=null
							 && forecastMap.get(snapshot14)!=null)
				
						outputVO.setProjectedRate(OrderRateUtil.roundValue( new Float(forecastMap.get(snapshot7)[1] 
								+ forecastMap.get(snapshot14)[1]) / 2));
					
					else
						outputVO.setProjectedRate(0);
					
								
					float weightedProjection = 0;
				
					if(forecastMap.get(snapshot7)!=null
							 && forecastMap.get(snapshot14)!=null
							 && (forecastMap.get(snapshot7)[0] + forecastMap.get(snapshot14)[0] > 0) )
					
							weightedProjection = OrderRateUtil.roundValue(new Float( outputVO.getProjectedRate() * outputVO.getCapacity() /
									((forecastMap.get(snapshot7)[0]+ forecastMap.get(snapshot14)[0])/2)));
					
					else
							weightedProjection = outputVO.getProjectedRate();
					
					outputVO.setWeightedProjectRate(weightedProjection);
					
					
					
					outputVO.setSnapshotTime(snapshot);
					outputVO.setSnapshotTimeFmt(sdf.format(snapshot));
					
					outputList.add(outputVO);
					
				}	
			}
			
		}
		 return outputList;
		
	}
	
	
	
	public List<OrderRateVO> getCurrentOrderRateBySnapshot(final String currentDate,final String deliveryDate,  final String zone) throws ParseException 
	{
		
		final DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		
		final List<OrderRateVO> dataList = new ArrayList<OrderRateVO>();
		
			 if(!"0".equals(zone))
			 {
				 PreparedStatementCreator creator=new PreparedStatementCreator() {
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement ps =
			                    connection.prepareStatement(CURRENT_DATE_ORDER_RATE);
			                ps.setString(1, currentDate);
					    	ps.setString(2, deliveryDate);
					    	ps.setString(3, deliveryDate);
					    	ps.setString(4, zone);
			                return ps;
			            }
			        };
			        
			        jdbcTemplate.query(creator,
				       		  new RowCallbackHandler() 
				        		{
				       		      public void processRow(ResultSet rs) throws SQLException 
				       		      {

				       		    	do 
				       		    	{
				    		    		OrderRateVO data = new OrderRateVO();
				    		    		data.setOrderCount(rs.getFloat("order_count"));
				    		    		data.setCutoffTime(new Date(rs.getTimestamp("cutoff").getTime()));
				    		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
				    		    		data.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
				    		    		data.setSnapshotTimeFmt(sdf.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
				    		    		data.setZone(rs.getString("zone"));
				    		    		data.setCapacity(rs.getInt("capacity"));
				    		    		dataList.add(data);
				    		    	}
				       		    	   while(rs.next());
				    		
				       		      }
				        		}
				 );		
				
		    	
			 }
			 else
			 {
				 PreparedStatementCreator creator=new PreparedStatementCreator() {
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement ps =
			                    connection.prepareStatement(CURRENT_DATE_ORDER_RATE_EX);
			                ps.setString(1, currentDate);
					    	ps.setString(2, deliveryDate);
					    	ps.setString(3, deliveryDate);
			                return ps;
			            }
			        };
			        jdbcTemplate.query(creator,
				       		  new RowCallbackHandler() 
				        		{
				       		      public void processRow(ResultSet rs) throws SQLException 
				       		      {

				       		    	do 
				       		    	{
							    		OrderRateVO data = new OrderRateVO();
							    		data.setOrderCount(rs.getFloat("order_count"));
							    		data.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
							    		data.setSnapshotTimeFmt(sdf.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
							    		data.setCapacity(rs.getInt("capacity"));
							    		dataList.add(data);
							    	}
				       		    	   while(rs.next());
				    		
				       		      }
				        		}
				 );		
			 }		
		
		 return dataList;
		
	}
	public List<OrderData> getOrderCount(final String currentDate,final String deliveryDate,  final String zone) throws ParseException 
	{
		
		final List<OrderData> dataList = new ArrayList<OrderData>();
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 
			 if(!"0".equals(zone))
			 {
				 PreparedStatementCreator creator=new PreparedStatementCreator() {
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement ps =
			                    connection.prepareStatement(ORDER_COUNT_QRY);
			                ps.setString(1, currentDate);
			                ps.setString(2, deliveryDate);
							ps.setString(3, zone);
							
			                return ps;
			            }
			        };
			        jdbcTemplate.query(creator,
				       		  new RowCallbackHandler() 
				        		{
				       		      public void processRow(ResultSet rs) throws SQLException 
				       		      {

				       		    	do 
				       		    	{
				    					OrderData data = new OrderData();
				    					data.setOrderCount(rs.getFloat("oCount"));
				    					data.setCutoff(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
				    					data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
				    					
				    					data.setZone(rs.getString("zone"));
				    					dataList.add(data);
				    				}
				       		    	   while(rs.next());
				    		
				       		      }
				        		}
				 );		
				
				
			 }
			 else
			 {
				 PreparedStatementCreator creator=new PreparedStatementCreator() {
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement ps =
			                    connection.prepareStatement(ORDER_COUNT_QRY_EX);
			                ps.setString(1, currentDate);
			                ps.setString(2, deliveryDate);
							
			                return ps;
			            }
			        };
			        jdbcTemplate.query(creator,
				       		  new RowCallbackHandler() 
				        		{
				       		      public void processRow(ResultSet rs) throws SQLException 
				       		      {

				       		    	do 
				       		    	{
										OrderData data = new OrderData();
										data.setOrderCount(rs.getFloat("oCount"));
										dataList.add(data);
									}
				       		    	   while(rs.next());
				    		
				       		      }
				        		}
				 );		
			 }
		
		 return dataList;
	}
	public List<PlantDispatchData> getPlantDispatchData(String deliveryDate) {

		List<PlantDispatchData> voList = new ArrayList<PlantDispatchData>();
		return voList;
		
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
