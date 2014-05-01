package com.freshdirect.dashboard.dao.impl;

/**
 * 
 * @author kkanuganti
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.freshdirect.dashboard.dao.IBounceDAO;
import com.freshdirect.dashboard.model.BounceData;
import com.freshdirect.dashboard.util.DateUtil;

@Repository
public class BounceDAO implements IBounceDAO  {

	private static final String REAL_TIME_BOUNCE_SELECT = 
			 " SELECT zone, "+
		       " cutoff, "+
		       " COUNT (DISTINCT (customer_id)) CCOUNT "+
		       " FROM mis.bounce_event "+
		       " WHERE  status = 'NEW' "+
		       " AND TYPE IN ('DELIVERYINFO', 'CHECKOUT', 'RESERVED_SLOT') "+
		       " AND TO_CHAR (delivery_date, 'mm/dd/yyyy') = ? "+
		       " AND createdate >= sysdate-1/24 "+
		       " GROUP BY zone, cutoff "+
		       " ORDER BY zone, cutoff ASC ";
	
            
	private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    public Map<String, Map<Date, Integer>> getRealTimeBounceByZone(final String deliveryDate) throws SQLException {
		
		final Map<String, Map<Date, Integer>> result = new HashMap<String, Map<Date, Integer>>();
	
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(REAL_TIME_BOUNCE_SELECT);
				ps.setString(1, deliveryDate);
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					String zone = rs.getString("zone");
					Date cutoff = DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime()));
					int ccount = rs.getInt("ccount");
					
					if(!result.containsKey(zone)){
						result.put(zone, new HashMap<Date, Integer>());
					}
					if(!result.get(zone).containsKey(cutoff)){
						result.get(zone).put(cutoff, ccount);
					}
				} while (rs.next());
			}
		});
		    	
		return result;		
	}
    
    
    private static final String CUSTOMER_TIMESLOT_PAGE_VISIT = " select dtl.zone_code, DTL.CUTOFF, count(distinct hdr.id) as CCOUNT "+
    		" from mis.timeslot_event_hdr hdr, mis.timeslot_event_dtl dtl "+
    		" where dtl.timeslot_log_id = hdr.id "+
    		" AND HDR.EVENT_DTM >= sysdate-1/24 "+
    		" AND DTL.BASE_DATE = TO_DATE (?, 'mm/dd/yyyy') "+
    		" AND DTL.CUTOFF IS NOT NULL "+
    		" group by dtl.zone_code, DTL.CUTOFF "+
    		" order by dtl.zone_code, DTL.CUTOFF ";
    
    
    public Map<String, Map<Date, Integer>> getCustomerVisitCnt(final String deliveryDate) throws SQLException {
		
		final Map<String, Map<Date, Integer>> result = new HashMap<String, Map<Date, Integer>>();
	
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(CUSTOMER_TIMESLOT_PAGE_VISIT);
				ps.setString(1, deliveryDate);
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					String zone = rs.getString("zone_code");
					Date cutoff = DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime()));
					int ccount = rs.getInt("ccount");
					
					if(!result.containsKey(zone)){
						result.put(zone, new HashMap<Date, Integer>());
					}
					if(!result.get(zone).containsKey(cutoff)){
						result.get(zone).put(cutoff, ccount);
					}
				} while (rs.next());
			}
		});
		    	
		return result;		
	}
    
    
    private static final String CUSTOMER_AVERAGE_SOW = " SELECT tbl.zone_code, tbl.CUTOFF, Ceil(AVG (tbl.cnt)) CCOUNT "+
    		" FROM (  SELECT dtl.zone_code, "+
    		" DTL.CUTOFF, "+
    		" HDR.CUSTOMER_ID, "+
    		" COUNT (*) AS cnt "+
    		" FROM mis.timeslot_event_hdr hdr, mis.timeslot_event_dtl dtl "+
    		" WHERE  dtl.timeslot_log_id = hdr.id "+
    		" AND HDR.EVENT_DTM >= SYSDATE - 1 / 24 "+
    		" AND DTL.STOREFRONT_AVL IN ('S') "+
    		" AND DTL.BASE_DATE = TO_DATE (?, 'mm/dd/yyyy') "+
    		" GROUP BY dtl.zone_code, DTL.CUTOFF, HDR.CUSTOMER_ID "+
    		" ORDER BY dtl.zone_code, DTL.CUTOFF, HDR.CUSTOMER_ID) tbl "+
    		" GROUP BY tbl.zone_code, tbl.CUTOFF "+
    		" ORDER BY tbl.zone_code, tbl.CUTOFF ";
    
    public Map<String, Map<Date, Integer>> getCustomerSoldOutWindowCnt(final String deliveryDate) throws SQLException {
		
		final Map<String, Map<Date, Integer>> result = new HashMap<String, Map<Date, Integer>>();
	
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(CUSTOMER_AVERAGE_SOW);
				ps.setString(1, deliveryDate);
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					String zone = rs.getString("zone_code");
					Date cutoff = DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime()));
					int ccount = rs.getInt("ccount");
					
					if(!result.containsKey(zone)){
						result.put(zone, new HashMap<Date, Integer>());
					}
					if(!result.get(zone).containsKey(cutoff)){
						result.get(zone).put(cutoff, ccount);
					}
				} while (rs.next());
			}
		});
		    	
		return result;		
	}
    
    
    private static final String GET_SOW_SELECT = "SELECT x.zone_code zone, x.CUTOFF_TIME cutoff, (ROUND ((x.TOTAL_ALLOC / x.CAPACITY) * 100)) as totalAllocation_perc "+
    		" FROM  ( "+
            "  SELECT z.zone_code, TS.CUTOFF_TIME, TS.START_TIME, TS.END_TIME, TS.CAPACITY, "+
            "   (SELECT COUNT (*) FROM dlv.reservation WHERE  timeslot_id = ts.id  AND status_code <> '15' AND status_code <> '20' AND (chefstable = ' ' OR chefstable = 'X')  AND class IS NULL) "+
            "      AS total_alloc "+
            "  FROM dlv.timeslot ts, dlv.zone z "+
            " WHERE     ts.zone_id = z.id "+
            "   AND ts.base_date = TO_DATE (?, 'mm/dd/yyyy') "+
            "   AND ts.capacity <> 0 "+
            "   ORDER BY z.zone_code, TS.CUTOFF_TIME "+
            " ) X";
	
    public Map<String, Map<Date, Integer>> getSOWByZone(final String deliveryDate) throws SQLException {
		
		final Map<String, Map<Date, Integer>> result = new HashMap<String, Map<Date, Integer>>();
	
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_SOW_SELECT);
				ps.setString(1, deliveryDate);
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					String zone = rs.getString("zone");
					Date cutoff = new Date(rs.getTimestamp("cutoff").getTime());
					int allocationPerc = rs.getInt("totalAllocation_perc");
					
					if(!result.containsKey(zone)){
						result.put(zone, new HashMap<Date, Integer>());
					}
					if(!result.get(zone).containsKey(cutoff)){
						result.get(zone).put(cutoff, 0);
					}
					if(allocationPerc > 90) {
						result.get(zone).put(cutoff, result.get(zone).get(cutoff).intValue() + 1);
					}
					
				} while (rs.next());
			}
		});
		    	
		return result;		
	}
    
    
    
	private static final String BOUNCE_SELECT = "  SELECT createdate, "+
			 " zone, "+
			 "  cutoff, "+
			 " SUM (COUNT (DISTINCT (customer_id))) OVER (PARTITION BY cutoff ORDER BY createdate, zone, cutoff) AS total_cnt "+
			 "  FROM mis.bounce_event "+
			 " WHERE status = 'NEW' "+
			 " AND TYPE IN ('DELIVERYINFO', 'CHECKOUT', 'RESERVED_SLOT') "+
			 " AND TO_CHAR (delivery_date, 'mm/dd/yyyy') = ? "+
			 " AND zone = ? "+
			 " GROUP BY zone, cutoff, createdate "+
			 " ORDER BY zone, cutoff, createdate ASC";

	private static final String BOUNCE_SELECT_DELIVERYDATE = "  SELECT createdate, "+
         " SUM (COUNT (DISTINCT (customer_id))) OVER (ORDER BY createdate) AS total_cnt "+
			 " FROM mis.bounce_event "+
			 " WHERE  status = 'NEW' "+
			 " AND TYPE IN ('DELIVERYINFO', 'CHECKOUT', 'RESERVED_SLOT') "+
			 " AND TO_CHAR (delivery_date, 'mm/dd/yyyy') = ? "+
			 " GROUP BY createdate "+
			 " ORDER BY createdate ASC";        
		
	public List<BounceData> getBounceByZone(final String deliveryDate,final String zone) 	{
		
		final List<BounceData> dataList = new ArrayList<BounceData>();
		final Calendar cal = Calendar.getInstance();

		if (zone != null && !"".equals(zone)) {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(BOUNCE_SELECT);
					ps.setString(1, deliveryDate);
					ps.setString(2, zone);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {

					do {
						BounceData data = new BounceData();
						dataList.add(data);
						
						data.setCnt(rs.getInt("total_cnt"));
						data.setCutOff(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));						
						data.setZone(rs.getString("zone"));
						cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
						cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE) % 30);
						data.setSnapshotTime(cal.getTime());						
						
					} while (rs.next());
				}
			});
		} else {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(BOUNCE_SELECT_DELIVERYDATE);
					ps.setString(1, deliveryDate);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {

					do {
						BounceData data = new BounceData();
						dataList.add(data);
						
						data.setCnt(rs.getInt("total_cnt"));
						cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
						cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE) % 30);
						data.setSnapshotTime(cal.getTime());
					} while (rs.next());

				}
			});
		}
		return dataList;
		
	}
	
}

