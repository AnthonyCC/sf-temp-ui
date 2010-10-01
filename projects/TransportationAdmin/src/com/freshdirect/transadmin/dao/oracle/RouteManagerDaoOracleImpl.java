package com.freshdirect.transadmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.RouteManagerDaoI;
import com.freshdirect.transadmin.model.HTOutScanAsset;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.util.TransStringUtil;

public class RouteManagerDaoOracleImpl implements RouteManagerDaoI  {
	
	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(RouteManagerDaoOracleImpl.class);
	

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public Map getRouteNumberGroup(String date, String cutOff, String groupCode) throws DataAccessException {
		
		final Map result = new HashMap();
		final StringBuffer strBuf = new StringBuffer();
		strBuf.append("select tr.rd, tr.ci , tr.gc, count(*) from (");
		strBuf.append("select tx.route_date rd, tx.cutoff_id ci, tx.group_code gc, tx.route_id ri");
		strBuf.append(" from transp.route_mapping tx ");
		strBuf.append(" where tx.route_date='").append(date).append("'");

		if(cutOff != null) {
			strBuf.append(" and tx.cutoff_id='").append(cutOff).append("'");
		}

		if(groupCode != null) {
			strBuf.append(" and tx.group_code='").append(groupCode).append("'");
		}
		
		strBuf.append(" group by tx.route_date, tx.cutoff_id , tx.group_code, tx.route_id) tr");
		strBuf.append(" group by tr.rd, tr.ci , tr.gc");
		strBuf.append(" order by tr.rd, tr.ci , tr.gc ");
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(strBuf.toString());
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {
       		    		result.put(new RouteMappingId(rs.getDate(1), rs.getString(2), rs.getString(3), null, null)
       		    							, new Integer(rs.getInt(4)));
       		    	}  while(rs.next());
       		      }
       		   });
        return result;
	}
	
	private static String UPDATE_ROUTEMAPPING_QRY="" +
		"UPDATE TRANSP.ROUTE_MAPPING r set r.ROUTING_SESSION_ID = ? where r.ROUTE_DATE = ? " +
		"and r.CUTOFF_ID = ? and r.GROUP_CODE in (SELECT a.CODE FROM TRANSP.TRN_AREA a where a.IS_DEPOT ";//<> 'X')";
	
	public int updateRouteMapping(Date routeDate, String cutOffId, String sessionId, boolean isDepot) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(UPDATE_ROUTEMAPPING_QRY);
		if(isDepot) {
			strBuf.append("= 'X')");
		} else {
			strBuf.append("is NULL)");
		}
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),strBuf.toString());
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.DATE));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		int result = this.jdbcTemplate.update(strBuf.toString(), 
				new Object[] {sessionId, routeDate, cutOffId});	
		
		batchUpdater.flush();
		LOGGER.debug("ROUTE MAPPING UPDATED");
		return result;
	}

	public Map getHTOutScan(Date routeDate) throws DataAccessException {
		final Map result = new HashMap();
		try {
			
			final String scanStartTime= TransStringUtil.getDate(routeDate)+":12:00:00AM";
			final String scanEndTime=TransStringUtil.getDate(routeDate)+":11:59:59PM";
			final StringBuffer strBuf = new StringBuffer();
			strBuf.append("SELECT ROUTE,MIN(SCANDATE) FROM transp.assettracking WHERE ");
			strBuf.append("scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') ");
			strBuf.append("AND action='Check Out' AND asset LIKE 'HT%' GROUP BY ROUTE ");
			PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(strBuf.toString());
	                ps.setString(1, scanStartTime);
	                ps.setString(2, scanEndTime);
	                return ps;
	            }  
	        };
	        jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	
	       		    	 while(rs.next()) {
	       		    		 result.put(rs.getString(1), rs.getTimestamp(2));
	       		    	 }
	       		      }
	       		   });
	        	
			} catch (ParseException e) {
				LOGGER.warn(e.toString(), e);
			}
			return result;
	}
	
	public List getHTOutScanAsset(Date routeDate) throws DataAccessException {
		final List result = new ArrayList();
		try {
			
			final String scanStartTime = TransStringUtil.getDate(routeDate)+ ":12:00:00AM";
			final String scanEndTime = TransStringUtil.getDate(routeDate)+ ":11:59:59PM";
			final StringBuffer strBuf = new StringBuffer();
			strBuf.append("SELECT asset, MAX(SCANDATE) as SCAN_DATE FROM transp.assettracking");
			strBuf.append(" WHERE scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam')");
			strBuf.append(" AND asset LIKE 'HT%' AND assetstatus='OUT'  GROUP BY asset");
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(strBuf.toString());

					ps.setString(1, scanStartTime);
					ps.setString(2, scanEndTime);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					HTOutScanAsset scanAsset;
					do {
						scanAsset = new HTOutScanAsset(rs.getString(1),new Date(rs.getTimestamp(2).getTime()));
						result.add(scanAsset);
					} while (rs.next());
				}
			});

		} catch (ParseException e) {
			LOGGER.warn(e.toString(), e);
		}
		return result;
	}
	
	
	private static String GET_EMPS_WORKED_CONSECUTIVELY = "select  T.R from (" 
					+" (select distinct pr.resource_id as R, P.PLAN_DATE as D from transp.plan_resource pr, transp.plan p"
					+" where pr.plan_id = p.plan_id and p.plan_date BETWEEN TO_DATE(?, 'mm/dd/yyyy') AND TO_DATE(?, 'mm/dd/yyyy')) UNION"
					+" (select distinct dr.resource_id as R, d.dispatch_DATE as D from transp.dispatch_resource dr, transp.dispatch d"
					+" where dr.dispatch_id = d.dispatch_id and d.dispatch_date BETWEEN TO_DATE(?, 'mm/dd/yyyy') AND TO_DATE(?, 'mm/dd/yyyy'))"
					+" )T group by T.R having count(T.R)=6";
	
	public List getResourcesWorkedForSixConsecutiveDays(Date date) throws DataAccessException {
		final List result = new ArrayList();
		try {	
			
			final String startTime = TransStringUtil.getDate(TransStringUtil.addDays(date, -5));
			final String endTime = TransStringUtil.getDate(date);
		
			final StringBuffer strBuf = new StringBuffer(GET_EMPS_WORKED_CONSECUTIVELY);
			PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(strBuf.toString());	             
	            		ps.setString(1, startTime);	              
	            		ps.setString(2, endTime);
	            		ps.setString(3, startTime);	              
	            		ps.setString(4, endTime);
	            		return ps;
	            }  
	        };
	        jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	 do {
	       		    		 result.add(rs.getString(1));
	       		    	 } while(rs.next());
	       		      }
	       		   });
	        	
			} catch (ParseException e) {
				LOGGER.warn(e.toString(), e);
			}
		return result;
	}
	
	private static String GET_TEAM_CHANGES = "select distinct AL.NEW_VALUE from TRANSP.ACTIVITY_LOG al"
										+" where AL.LOG_DATE BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam')"
										+" and type = ? and FIELD_NAME = ? and al.new_value is NOT NULL";

	public List getDispatchTeamResourcesChanged(Date date, final String type, final String field) throws DataAccessException {
		final List result = new ArrayList();
		try {
			final String startTime = TransStringUtil.getDate(date)+":12:00:00AM";
			final String endTime = TransStringUtil.getDate(date)+":11:59:59PM";

			final StringBuffer strBuf = new StringBuffer(GET_TEAM_CHANGES);
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(strBuf.toString());
						ps.setString(1, startTime);
						ps.setString(2, endTime);
						ps.setString(3, type);
						ps.setString(4, field);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						result.add(rs.getString(1));
					} while (rs.next());
				}
			});

		} catch (ParseException e) {
			LOGGER.warn(e.toString(), e);
		}
		return result;
	}
	
	public Date getHTOutScanTimeForRoute(final String routeId) throws DataAccessException {
		
		final List result = new ArrayList();
		final StringBuffer strBuf = new StringBuffer();
		strBuf.append("SELECT MIN(SCANDATE) FROM transp.assettracking WHERE ");
		strBuf.append("ROUTE=? AND action='Check Out' AND asset LIKE 'HT%' ");
		PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(strBuf.toString());
                ps.setString(1, routeId);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	 if(rs.next()) {
       		    		 result.add( rs.getTimestamp(1));
       		    	 }
       		      }
       		   });
			return  result.size()==0? null:(Date)result.get(0);
	}
	
	//SELECT MAX(a1.SCANDATE), a1.ROUTE FROM transp.assetstatus a1
	// WHERE a1.scandate BETWEEN TO_DATE('2009/05/19:12:00:00am', 'yyyy/mm/dd:hh:mi:ssam') AND TO_DATE('2009/05/19:11:59:59pm', 'yyyy/mm/dd:hh:mi:ssam') AND a1.ASSET LIKE 'HT%'
	//AND a1.action='Check In' AND a1.ROUTE NOT IN (SELECT ROUTE FROM transp.assetstatus WHERE scandate BETWEEN TO_DATE('2009/05/19:12:00:00am', 'yyyy/mm/dd:hh:mi:ssam') AND TO_DATE('2009/05/19:11:59:59pm', 'yyyy/mm/dd:hh:mi:ssam') AND action='Check Out' AND ASSET LIKE 'HT%')
	//GROUP BY a1.ROUTE
	
	
	public Map getHTInScan(Date routeDate) throws DataAccessException {
		final Map result = new HashMap();
		try {
			
			final String scanStartTime= TransStringUtil.getDate(routeDate)+":12:00:00AM";
			final String scanEndTime=TransStringUtil.getDate(routeDate)+":11:59:59PM";
			final StringBuffer strBuf = new StringBuffer();
			strBuf.append("SELECT a1.ROUTE,MAX(a1.SCANDATE)  FROM transp.assetstatus a1 ");
			strBuf.append("WHERE a1.scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND a1.ASSET LIKE 'HT%'");
			strBuf.append("AND a1.action='Check In' AND a1.ROUTE NOT IN (SELECT ROUTE FROM transp.assetstatus WHERE scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND action='Check Out' AND ASSET LIKE 'HT%') GROUP BY a1.ROUTE");
			PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(strBuf.toString());
	                ps.setString(1, scanStartTime);
	                ps.setString(2, scanEndTime);
	                ps.setString(3, scanStartTime);
	                ps.setString(4, scanEndTime);
	                return ps;
	            }  
	        };
	        jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	
	       		    	 while(rs.next()) {
	       		    		 result.put(rs.getString(1), rs.getTimestamp(2));
	       		    	 }
	       		      }
	       		   });
	        	
			} catch (ParseException e) {
				LOGGER.warn(e.toString(), e);
			}
			return result;
	}
	
	
	
	

	
	
}
