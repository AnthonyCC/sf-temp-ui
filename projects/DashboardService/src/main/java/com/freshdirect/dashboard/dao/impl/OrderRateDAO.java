package com.freshdirect.dashboard.dao.impl;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.freshdirect.dashboard.dao.IOrderRateDAO;
import com.freshdirect.dashboard.model.DashboardSummary;
import com.freshdirect.dashboard.model.DateRangeVO;
import com.freshdirect.dashboard.model.OrderRateVO;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.model.TimeslotModel;
import com.freshdirect.dashboard.util.DateUtil;
import com.freshdirect.dashboard.util.OrderRateUtil;

@Repository
public class OrderRateDAO implements IOrderRateDAO {

	private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
	
	private static String GET_PROJECTED_UNTILIZATION = "SELECT o.zone, "+
         " o.cutoff, "+
         " SUM (capacity) planned_capacity, "+
         " SUM (orders_expected) expected_orders, "+
         " X.order_count AS confirmed_orders, "+
         " SUM (total_allocation) As allocated_orders, "+
         " R.resource_count "+         
         " FROM mis.order_rate o, "+
         " ( "+
	     " 	SELECT MAX (snapshot_time) sh, O.CUTOFF cutoff "+
	     "  	FROM mis.order_rate o "+
	     "	  WHERE o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
	     "  GROUP BY O.CUTOFF "+
         " ) t, "+
         " ( "+
         " SELECT COUNT (*) order_count, di.zone zone, di.cutofftime cutoff "+
         "    FROM cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
         "   WHERE  s.id = sa.sale_id "+
         "         AND s.cromod_date = sa.action_date "+
         "         AND s.TYPE = 'REG' "+
         "         AND s.status <> 'CAN' "+
         "         AND S.CROMOD_DATE < SYSDATE "+
         "         AND sa.action_type IN ('CRO', 'MOD') "+
         "         AND sa.requested_date = TO_DATE (?, 'mm/dd/yyyy') "+
         "         AND sa.id = di.salesaction_id "+
         "  GROUP BY di.zone, di.cutofftime "+
         " ) X, "+
         " (   "+
         "  SELECT SUM (resource_count) resource_count, area, to_date(to_char(w.delivery_date-1, 'MM/DD/YYYY')||' '|| to_char(w.cutoff_datetime, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') as cutoff_datetime "+
         "    FROM transp.wave_instance w "+
         "   WHERE delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
         "  GROUP BY area, to_date(to_char(w.delivery_date-1, 'MM/DD/YYYY')||' '|| to_char(w.cutoff_datetime, 'HH:MI:SS AM'),'MM/DD/YYYY HH:MI:SS AM') "+
         " ) R "+
         " WHERE  o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
         " AND o.snapshot_time = t.sh "+
         " AND o.cutoff = t.cutoff "+
         " AND o.zone = X.zone(+) "+
         " AND o.cutoff = X.cutoff(+) "+
         " AND o.zone = R.area(+) "+
         " AND o.cutoff = R.cutoff_datetime(+) "+
         " GROUP BY o.zone, "+
         " o.cutoff, "+
         " X.order_count, "+
         " R.resource_count "+
         " ORDER BY zone, cutoff";
	
	
	 public List<ProjectedUtilizationVO> getProjectedUtilization(final String deliveryDate) throws SQLException {
			
			final List<ProjectedUtilizationVO> result = new ArrayList<ProjectedUtilizationVO>();
		
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(GET_PROJECTED_UNTILIZATION);
					ps.setString(1, deliveryDate);
					ps.setString(2, deliveryDate);
					ps.setString(3, deliveryDate);
					ps.setString(4, deliveryDate);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						ProjectedUtilizationVO vo = new ProjectedUtilizationVO();
						result.add(vo);
						vo.setZone(rs.getString("zone"));
						vo.setCutoffTime(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));						
						vo.setPlannedCapacity(rs.getDouble("planned_capacity"));						
						vo.setConfirmedOrderCnt(rs.getDouble("confirmed_orders"));
						vo.setExpectedOrderCnt(rs.getDouble("expected_orders"));
						vo.setAllocatedOrderCnt(Math.round(rs.getInt("allocated_orders")));						
						vo.setResourceCnt(rs.getInt("resource_count"));
					} while (rs.next());
				}
			});			    	
			return result;		
		}
	 
	 private static String GET_CUTOFFS_QUERY = "SELECT CUTOFF_TIME, SHIFT from TRANSP.TRN_CUTOFF order by CUTOFF_TIME";
	 
	 public Map<Date, String> getCutoffs() throws SQLException {
			
		final Map<Date, String> result = new HashMap<Date, String>();
				
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_CUTOFFS_QUERY);				
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						result.put(new java.util.Date(rs.getTimestamp("CUTOFF_TIME").getTime()), rs.getString("SHIFT"));												
					} while (rs.next());
				}
			});			    	
		return result;		
	}
	 
	 
	 private static final String GET_ORDER_METRICS_BY_TIMESLOT =  "SELECT SUM (capacity) capacity, "+
			 " SUM (orders_expected) projected, "+
			 " o.zone, "+
			 " o.timeslot_start, "+
			 " o.cutoff, "+
			 " X.confirmed_orders "+
			 " FROM MIS.ORDER_RATE o, "+
			 " (  SELECT MAX (snapshot_time) sh, O.CUTOFF co "+
			 "    FROM mis.order_rate o "+
			 "    WHERE o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
			 "           AND o.zone = ? "+
			 "  GROUP BY O.CUTOFF) t, "+
			 " (  SELECT di.zone, "+
			 "         di.starttime, "+
			 "         di.cutofftime, "+
			 "         COUNT (*) confirmed_orders "+
			 "    FROM cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
			 "   WHERE     s.id = sa.sale_id "+
			 "         AND s.cromod_date = sa.action_date "+
			 "         AND s.TYPE = 'REG' "+
			 "         AND s.status <> 'CAN' "+
			 "         AND S.CROMOD_DATE < SYSDATE "+
			 "         AND sa.action_type IN ('CRO', 'MOD') "+
			 "         AND sa.requested_date = TO_DATE (?, 'mm/dd/yyyy') "+
			 "         AND di.zone = ? "+
			 "         AND sa.id = di.salesaction_id "+
			 "  GROUP BY di.zone, di.starttime, di.cutofftime) X "+
			 " WHERE     o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
			 " AND o.zone = ? "+
			 " AND o.snapshot_time = t.sh "+
			 " AND o.cutoff = t.co "+
			 " AND o.zone = X.zone(+) "+
			 " AND o.cutoff = X.cutofftime(+) "+
			 " AND o.timeslot_start = X.starttime(+) "+
	         " GROUP BY o.zone, o.timeslot_start, o.cutoff, confirmed_orders "+
	         " ORDER BY o.zone, o.timeslot_start, o.cutoff ";
	 
	 public List<TimeslotModel> getOrderMetricsByTimeslot(final String deliveryDate, final String zone) throws SQLException {
			
			final List<TimeslotModel> result = new ArrayList<TimeslotModel>();
		
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(GET_ORDER_METRICS_BY_TIMESLOT);
					ps.setString(1, deliveryDate);
					ps.setString(2, zone);
					ps.setString(3, deliveryDate);
					ps.setString(4, zone);
					ps.setString(5, deliveryDate);
					ps.setString(6, zone);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
				do {
					TimeslotModel vo = new TimeslotModel();
					result.add(vo);

					double projected = rs.getInt("confirmed_orders") + rs.getDouble("projected");

					vo.setZone(rs.getString("zone"));
					vo.setTimeslot(new Date(rs.getTimestamp("timeslot_start").getTime()));
					vo.setCapacity(rs.getInt("capacity"));
					vo.setOrders(rs.getInt("confirmed_orders"));
					vo.setProjectedOrders(Math.round((float) projected));
					if(vo.getCapacity() > 0)
						vo.setCurrentUtilization(Math.round((float)((vo.getOrders() / (double) vo.getCapacity()) * 100.0)));
					else
						vo.setCurrentUtilization(0);
					if(vo.getCapacity() > 0)
						vo.setProjectedUtilization(Math.round((float)((vo.getProjectedOrders() / (double) vo.getCapacity()) * 100.0)));
					else
						vo.setProjectedUtilization(0);
					} while (rs.next());
				}
			});			    	
		return result;		
	 } 
	
	
	private static final String EXCEPTION_QUERY = "select delivery_date from MIS.ORDER_RATE_EXCEPTIONS";
	
	public Set<Date> getExceptions(){
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
		       		    	do {
		       		    		exceptions.add(new Date(rs.getDate("delivery_date").getTime()));
		       				} while(rs.next());
		       		      }
		        		}
		 );		
		return exceptions;
	}
		
		
	
	private static final String MAX_SNAPSHOT_DELIVERY_DATE = "SELECT o.zone, o.cutoff, o.capacity, o.timeslot_start, o.timeslot_end, o.snapshot_time "+
			" FROM mis.order_rate o, "+
			"  (  SELECT MAX (snapshot_time) sh, O.CUTOFF co "+
			" 		FROM mis.order_rate o "+
			" 		WHERE  o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
			"     	AND o.zone = ? "+
			"     	AND snapshot_time >= TO_DATE (?, 'mm/dd/yyyy') "+
			" 		GROUP BY O.CUTOFF "+
			" ) t "+
			" WHERE o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
			" AND o.zone = ? "+
			" AND O.SNAPSHOT_TIME = t.sh "+
			" AND O.CUTOFF = t.co ";

	private static final String MAX_SNAPSHOT_DELIVERY_DATE_EX = "select sum(o.capacity) capacity, max(o.snapshot_time) snapshot_time from mis.order_rate o , "
			+ "(select max(o1.snapshot_time) sh,o1.cutoff co  from mis.order_rate o1 where o1.delivery_date = to_date(?,'mm/dd/yyyy') and "
			+ "o1.snapshot_time >=to_date(?,'mm/dd/yyyy') group by o1.cutoff) t where o.delivery_date = to_date(?,'mm/dd/yyyy') and "
			+ "O.SNAPSHOT_TIME=t.sh and O.CUTOFF=t.co";

	public List<OrderRateVO> getForecastData(final String deliveryDate,
			final Date baseDate, final String zone, Date day1, Date day2) throws ParseException {

		final List<OrderRateVO> inputList = new ArrayList<OrderRateVO>();
		List<OrderRateVO> outputList = new ArrayList<OrderRateVO>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.DATE, -1);
		Date minSnapshotDate = cal.getTime();
		final String minSnapshotDateStr = DateUtil.getDate(minSnapshotDate);
		Date snapshot = null;
		
		if (zone != null && !"".equals(zone)) {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(MAX_SNAPSHOT_DELIVERY_DATE);
					ps.setString(1, deliveryDate);
					ps.setString(2, zone);
					ps.setString(3, minSnapshotDateStr);
					ps.setString(4, deliveryDate);
					ps.setString(5, zone);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {

					do {
						OrderRateVO vo = new OrderRateVO();
						vo.setBaseDate(baseDate);
						vo.setCapacity(rs.getInt("capacity"));
						vo.setZone(rs.getString("zone"));
						vo.setCutoffTimeEx(new Date(rs.getTimestamp("cutoff").getTime()));
						vo.setCutoffTime(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));
						vo.setStartTime(new Date(rs.getTimestamp("timeslot_start").getTime()));
						vo.setEndTime(new Date(rs.getTimestamp("timeslot_end").getTime()));						
						vo.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
						try {
							vo.setSnapshotTimeFmt(DateUtil.getDatewithTime(new Date(rs.getTimestamp("snapshot_time").getTime())));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						inputList.add(vo);

					} while (rs.next());

				}
			});

			Map<DateRangeVO, Map<String, Map<Date, Integer[]>>> forecastMap = getForecastCapacityMap(day1, day2, zone);

			Iterator<OrderRateVO> voIterator = inputList.iterator();

			while (voIterator.hasNext()) {
				OrderRateVO tempVO = voIterator.next();
				boolean done = false;

				int days1 = (int) DateUtil.getDiffInDays(tempVO.getBaseDate(), day1) * -1;
				int days2 = (int) DateUtil.getDiffInDays(tempVO.getBaseDate(), day2) * -1;
				Date snapshot7 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days1);
				Date startTime7 = OrderRateUtil.getDate(tempVO.getStartTime(), days1);
				Date endTime7 = OrderRateUtil.getDate(tempVO.getEndTime(), days1);
				Date snapshot14 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days2);
				Date startTime14 = OrderRateUtil.getDate(tempVO.getStartTime(), days2);
				Date endTime14 = OrderRateUtil.getDate(tempVO.getEndTime(), days2);

				snapshot = tempVO.getSnapshotTime();
				DateRangeVO range7 = new DateRangeVO(startTime7, endTime7);
				DateRangeVO range14 = new DateRangeVO(startTime14, endTime14);
				while (!done) {

					OrderRateVO outputVO = copyBaseAttributes(tempVO);

					snapshot = OrderRateUtil.addTime(snapshot);
					snapshot7 = OrderRateUtil.addTime(snapshot7);
					snapshot14 = OrderRateUtil.addTime(snapshot14);

					if (forecastMap.get(range7) != null
							&& forecastMap.get(range14) != null
							&& forecastMap.get(range7).get(outputVO.getZone()) != null
							&& forecastMap.get(range14).get(outputVO.getZone()) != null
							&& forecastMap.get(range7).get(outputVO.getZone())
									.get(snapshot7) != null
							&& forecastMap.get(range14).get(outputVO.getZone())
									.get(snapshot14) != null)

						outputVO.setProjectedRate(OrderRateUtil.roundValue(new Float(
								forecastMap.get(range7).get(outputVO.getZone())
										.get(snapshot7)[1]
										+ forecastMap.get(range14)
												.get(outputVO.getZone())
												.get(snapshot14)[1]) / 2));

					else
						outputVO.setProjectedRate(0);

					float weightedProjection = 0;

					if (forecastMap.get(range7) != null
							&& forecastMap.get(range14) != null
							&& forecastMap.get(range7).get(outputVO.getZone()) != null
							&& forecastMap.get(range14).get(outputVO.getZone()) != null
							&& forecastMap.get(range7).get(outputVO.getZone())
									.get(snapshot7) != null
							&& forecastMap.get(range14).get(outputVO.getZone())
									.get(snapshot14) != null
							&& (forecastMap.get(range7).get(outputVO.getZone())
									.get(snapshot7)[0]
									+ forecastMap.get(range14)
											.get(outputVO.getZone())
											.get(snapshot14)[0] > 0))

						weightedProjection = OrderRateUtil
								.roundValue(new Float(
										outputVO.getProjectedRate()
												* outputVO.getCapacity()
												/ ((forecastMap
														.get(range7)
														.get(outputVO.getZone())
														.get(snapshot7)[0] + forecastMap
														.get(range14)
														.get(outputVO.getZone())
														.get(snapshot14)[0]) / 2)));

					else
						weightedProjection = 0;

					outputVO.setWeightedProjectRate(weightedProjection);

					if (!(forecastMap.get(range7) != null
							&& forecastMap.get(range14) != null
							&& forecastMap.get(range7).get(outputVO.getZone()) != null && forecastMap
							.get(range14).get(outputVO.getZone()) != null))
						done = true;

					if (snapshot.getTime() > outputVO.getCutoffTimeEx().getTime())
						done = true;

					outputVO.setSnapshotTime(snapshot);
					outputVO.setSnapshotTimeFmt(DateUtil.getDatewithTime(snapshot));

					outputList.add(outputVO);

				}
			}
		} else {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(MAX_SNAPSHOT_DELIVERY_DATE_EX);
					ps.setString(1, deliveryDate);
					ps.setString(2, minSnapshotDateStr);
					ps.setString(3, deliveryDate);
					return ps;
				}
			};

			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						if (rs.getTimestamp("snapshot_time") != null) {
							OrderRateVO vo = new OrderRateVO();
							vo.setBaseDate(baseDate);
							vo.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
							try {
								vo.setSnapshotTimeFmt(DateUtil
										.getDatewithTime(new Date(rs.getTimestamp("snapshot_time").getTime())));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							vo.setCapacity(rs.getInt("capacity"));
							inputList.add(vo);
						}
					} while (rs.next());
				}
			});	
			
			Map<Date, Integer[]> forecastMap = null;

			if (inputList.size() > 0)
				forecastMap = getFDTotalForecastCapacityMap(day1, day2);

			Iterator<OrderRateVO> voIterator = inputList.iterator();

			while (voIterator.hasNext()) {
				OrderRateVO tempVO = voIterator.next();
				int days1 = (int) DateUtil.getDiffInDays(baseDate, day1) * -1;
				int days2 = (int) DateUtil.getDiffInDays(baseDate, day2) * -1;
				Date snapshot7 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days1);
				Date snapshot14 = OrderRateUtil.getDate(tempVO.getSnapshotTime(), days2);

				snapshot = tempVO.getSnapshotTime();
				
				while(snapshot.before(baseDate))
				{
					OrderRateVO outputVO = copyBaseAttributes(tempVO);	
					
					snapshot =  OrderRateUtil.addTime(snapshot);
					snapshot7 = OrderRateUtil.addTime(snapshot7);
					snapshot14 = OrderRateUtil.addTime(snapshot14);
					if (forecastMap.get(snapshot7) != null
							&& forecastMap.get(snapshot14) != null)

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
					outputVO.setSnapshotTimeFmt(DateUtil.getDatewithTime(snapshot));
					
					outputList.add(outputVO);
					
				}	
			}
			
		}
		 return outputList;
		
	}
	
	
	private static final String CAPACITY_QUERY_FORECAST = "select capacity,order_count, "
			+ "delivery_date, timeslot_start, timeslot_end, zone, snapshot_time from MIS.order_rate where delivery_date in ( ?, ?) and "
			+ " ((snapshot_time between ? and ?) or (snapshot_time between ? and ?)) and zone = ?";

	private static final String CAPACITY_QUERY_FORECAST_EX = "select sum(capacity) capacity,sum(order_count) order_count, snapshot_time from MIS.order_rate where delivery_date in"
			+ " (?,?) and  ((snapshot_time between ? and ?) or (snapshot_time between ? and ?)) group by snapshot_time order by snapshot_time asc";
	
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
	        
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			@SuppressWarnings("unchecked")
			public void processRow(ResultSet rs) throws SQLException {

				do {
		       		DateRangeVO range = new DateRangeVO(new Date(rs.getTimestamp("timeslot_start").getTime()), new Date(rs.getTimestamp("timeslot_end").getTime()));
		       		Date snapshot = rs.getTimestamp("snapshot_time");
		       		Integer capacity = rs.getInt("capacity");
		       		Integer orders = rs.getInt("order_count");
		       		Integer[] metrics = new Integer[]{capacity,orders};
					if (capacityMap.get(range) != null) {
						if (capacityMap.get(range).get(zone) != null) {
							if (capacityMap.get(range).get(zone).get(snapshot) == null)
								capacityMap.get(range).get(zone)
										.put(snapshot, metrics);
						} else {
							Map map = new HashMap<Date, Integer>();
							map.put(snapshot, metrics);
							capacityMap.get(range).put(zone, map);
						}
					} else {
						Map map = new HashMap<Date, Integer>();
						map.put(snapshot, metrics);
						Map zoneMap = new HashMap<String, Map<Date, Integer>>();
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
	
	private Map<Date, Integer[]> getFDTotalForecastCapacityMap(final Date day1,
			final Date day2) {

		final Map<Date, Integer[]> capacityMap = new HashMap<Date, Integer[]>();
		final Calendar cal = Calendar.getInstance();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(CAPACITY_QUERY_FORECAST_EX);
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
	
	private static OrderRateVO copyBaseAttributes(OrderRateVO vo) {
		DateFormat tdf = new SimpleDateFormat("hh:mm a");

		OrderRateVO voCopy = new OrderRateVO();
		voCopy.setBaseDate(vo.getBaseDate());
		voCopy.setSnapshotTime(vo.getSnapshotTime());
		voCopy.setSnapshotTimeFmt(vo.getSnapshotTimeFmt());
		voCopy.setStartTime(vo.getStartTime());
		voCopy.setEndTime(vo.getEndTime());
		voCopy.setCutoffTime(vo.getCutoffTime());
		voCopy.setCutoffTimeEx(vo.getCutoffTimeEx());
		voCopy.setCapacity(vo.getCapacity());
		voCopy.setZone(vo.getZone());
		if (vo.getStartTime() != null)
			voCopy.setStartTimeFormatted(tdf.format(vo.getStartTime()));
		if (vo.getCutoffTime() != null)
			voCopy.setCutoffTimeFormatted(tdf.format(vo.getCutoffTime()));

		return voCopy;
	}
	
	private static final String CURRENT_DATE_ORDER_RATE = "SELECT snapshot_time, "+
	         " X.zone, "+
				" x.cutoff, "+
				" x.capacity, "+
				" x.order_count, "+
				" SUM (order_count) OVER (PARTITION BY x.cutoff ORDER BY snapshot_time) + oCount "+
				" AS total_orders "+
				" FROM (  SELECT snapshot_time, "+
				"        zone, "+
				"        cutoff, "+
				"        SUM (order_count) order_count, "+
				"        SUM (capacity) capacity "+
				"   FROM mis.order_rate "+
				"  WHERE     snapshot_time > TO_DATE (?, 'mm/dd/yyyy') "+
				"        AND snapshot_time < TO_DATE (?, 'mm/dd/yyyy') "+
				"        AND delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
				"        AND zone = ? "+
				"  GROUP BY snapshot_time, cutoff, zone "+
				"  ) X, "+
				" (  SELECT SUM (order_count) oCount, o.cutoff "+
	        		 "       FROM mis.order_rate o "+
				"  WHERE o.snapshot_time <= TO_DATE (?, 'mm/dd/yyyy') "+
				"        AND o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
				"        AND o.zone = ? "+
				"   GROUP BY o.zone, o.cutoff "+
				"  ) Y "+
				"  WHERE X.cutoff = Y.cutoff "+
				" ORDER BY snapshot_time ASC ";
		
		private static final String CURRENT_DATE_ORDER_RATE_EX = "SELECT snapshot_time, "+
				" capacity, "+
				" SUM (order_count) OVER (ORDER BY snapshot_time) + oCount AS total_orders "+
				" FROM ( "+
				" SELECT snapshot_time, SUM (order_count) order_count, SUM (capacity) capacity "+
				"     FROM mis.order_rate "+
				"    WHERE  snapshot_time > TO_DATE (?, 'mm/dd/yyyy') "+
				"         AND snapshot_time < TO_DATE (?, 'mm/dd/yyyy') "+
				"         AND delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
						" GROUP BY snapshot_time "+
	            " ORDER BY snapshot_time ASC "+
				" ) X, "+
				" ( "+
				" 	SELECT SUM (order_count) oCount "+
				" 		FROM mis.order_rate o "+
				" 	WHERE o.snapshot_time <= TO_DATE (?, 'mm/dd/yyyy') "+
				"  	AND o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
				" ) Y ";

	public List<OrderRateVO> getCurrentOrderRateBySnapshot(
			final String currentDate, final String deliveryDate,
			final String zone) throws ParseException {
			
		final DateFormat df = new SimpleDateFormat("hh:mm a");

		final List<OrderRateVO> dataList = new ArrayList<OrderRateVO>();

		if (zone != null && !"".equals(zone)) {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(CURRENT_DATE_ORDER_RATE);
					ps.setString(1, currentDate);
					ps.setString(2, deliveryDate);
					ps.setString(3, deliveryDate);
					ps.setString(4, zone);
					ps.setString(5, currentDate);
					ps.setString(6, deliveryDate);
					ps.setString(7, zone);
					return ps;
				}
			};

			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						OrderRateVO data = new OrderRateVO();
						data.setZone(rs.getString("zone"));
						data.setCapacity(rs.getInt("capacity"));
						data.setOrderCount(rs.getFloat("total_orders"));
						data.setCutoffTime(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));
						data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
						data.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
						data.setSnapshotTimeFmt(df.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
						dataList.add(data);
					} while (rs.next());
				}
			});

		} else {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(CURRENT_DATE_ORDER_RATE_EX);
					ps.setString(1, currentDate);
					ps.setString(2, deliveryDate);
					ps.setString(3, deliveryDate);
					ps.setString(4, currentDate);
					ps.setString(5, deliveryDate);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						OrderRateVO data = new OrderRateVO();
						data.setCapacity(rs.getInt("capacity"));
						data.setOrderCount(rs.getFloat("total_orders"));
						data.setSnapshotTime(new Date(rs.getTimestamp("snapshot_time").getTime()));
						data.setSnapshotTimeFmt(df.format(new Date(rs.getTimestamp("snapshot_time").getTime())));
						dataList.add(data);
					} while (rs.next());
				}
			});
		}

		return dataList;
	}
	
	public static final String GET_DASHBOARD_SUMMARY = "SELECT T.capacity as plantMaxCapacity,  "+
          " X.order_count as total_orders, "+
          " R.resource_count, "+
          " Z.planned_capacity "+
          " FROM "+
          " (  "+
          " 	select pc.capacity from transp.PLANT_CAPACITY pc where pc.day_of_week = ? and pc.dispatch_time =  "+
          "   		(select max(dispatch_time) from transp.PLANT_CAPACITY where day_of_week = pc.day_of_week)  "+
          " ) T, "+ 
          " ( "+
          "  SELECT count(*) as order_count "+
          "      from cust.sale s, cust.salesaction sa, cust.deliveryinfo di  "+
          "  where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = TO_DATE (?, 'mm/dd/yyyy')  and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "+ 
          "  and s.type ='REG' and s.status <> 'CAN' and sa.id = di.salesaction_id "+
          " ) X,  "+
          " ( "+
          " 	SELECT SUM(TRUCKS) as resource_count FROM MIS.DISPATCH_VOLUME DV WHERE DISPATCH_DATE = TO_DATE (?, 'mm/dd/yyyy') AND SNAPSHOT_TIME = "+
          "  	(SELECT MAX(SNAPSHOT_TIME) FROM MIS.DISPATCH_VOLUME WHERE DISPATCH_DATE = DV.DISPATCH_DATE ) "+
          " ) R,"+
          " ( "+
          " SELECT SUM (capacity) planned_capacity "+
          "	 FROM mis.order_rate o, "+
          "     (SELECT MAX (snapshot_time) sh "+
          "       FROM mis.order_rate o "+
          "       WHERE o.delivery_date = TO_DATE (?, 'mm/dd/yyyy')) t "+
          "   WHERE o.delivery_date = TO_DATE (?, 'mm/dd/yyyy') "+
          "     AND o.snapshot_time = t.sh "+
          " ) z";
	
	public DashboardSummary getDashboardSummary(final String deliveryDate) throws SQLException {
		final DashboardSummary summary = new DashboardSummary();
		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_DASHBOARD_SUMMARY);
	                try {
						ps.setString(1, DateUtil.formatDayOfWk(DateUtil.getDate(deliveryDate)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ps.setString(2, deliveryDate);
					ps.setString(3, deliveryDate);
					ps.setString(4, deliveryDate);
					ps.setString(5, deliveryDate);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
		       		  new RowCallbackHandler() {
		       		      public void processRow(ResultSet rs) throws SQLException {
		       		    	do {
		       		    		summary.setTotalOrders(rs.getInt("total_orders"));
		       		    		summary.setMaxPlantCapacity(rs.getInt("plantMaxCapacity"));
		       		    		summary.setResourceCnt(rs.getInt("resource_count"));
		       		    		summary.setTotalPlannedCapacity(rs.getInt("planned_capacity"));
		       				} while(rs.next());
		       		      }
		        		}
		 );		
		return summary;
	}
	
	public static final String GET_LIVE_PLANNED_CAPACITY = "SELECT o.zone, o.cutoff, "+
          " SUM (capacity) planned_capacity "+
			" FROM mis.order_rate o,  "+
			" (  "+
			"   SELECT MAX (snapshot_time) sh, O.CUTOFF cutoff FROM mis.order_rate o  "+
			"    WHERE o.delivery_date = ? "+
			"  GROUP BY O.CUTOFF "+
			" ) t "+
			" WHERE  o.delivery_date = ?  "+
			" AND o.snapshot_time = t.sh  "+
			" AND o.cutoff = t.cutoff  "+
			" GROUP BY o.zone, o.cutoff "+
			" ORDER BY zone, cutoff";
	
	public List<OrderRateVO> getPlannedCapacityByZone(final Date deliveryDate) throws SQLException {
			
		final List<OrderRateVO> result = new ArrayList<OrderRateVO>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(GET_LIVE_PLANNED_CAPACITY);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));			
					ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
					return ps;
				}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						OrderRateVO data = new OrderRateVO();
						data.setZone(rs.getString("zone"));
						data.setCapacity(rs.getInt("planned_capacity"));
						data.setCutoffTime(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));			
					} while (rs.next());
				}
			});			    	
		return result;		
	 }
	
}
