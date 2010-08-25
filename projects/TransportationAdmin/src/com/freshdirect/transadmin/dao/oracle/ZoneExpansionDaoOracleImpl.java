package com.freshdirect.transadmin.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.RollbackException;
import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.ZoneExpansionDaoI;
import com.freshdirect.transadmin.datamanager.model.WorkTableModel;
import com.freshdirect.transadmin.datamanager.model.ZoneWorktableModel;

public class ZoneExpansionDaoOracleImpl implements ZoneExpansionDaoI{
	
	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ZoneExpansionDaoOracleImpl.class);
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String CHECK_POLYGONS=
		"select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) as TEMP from DLV.ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.CT_ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.CTCOS_ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.COS_ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.NJ_ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.HAMP_ZONE_WORKTABLE"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE' union all"
			+" select code, name, SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) from DLV.GEO_RESTRICTION_BOUNDARY"
			+" where SDO_GEOM.VALIDATE_GEOMETRY(geoloc, 0.5) <> 'TRUE'";
	
	public Collection checkPolygons(){
		final List result=new ArrayList();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(CHECK_POLYGONS);
	                return ps;
	            }  
	    };
			
		jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						ZoneWorktableModel workTableInfo;
						do {
							workTableInfo=new ZoneWorktableModel(rs.getString("CODE"),rs.getString("NAME"), "TRUE".equals(rs.getString("TEMP")));
							result.add(workTableInfo);
						}while(rs.next());
					}
				});
		return result;
	}
	
	private static String ZONE_WORK_TABLE = "select CODE, NAME from DLV.";
	
	public Collection getZoneWorkTableInfo(final String worktable){
		final Set result=new HashSet();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(ZONE_WORK_TABLE+worktable);
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						WorkTableModel workTableInfo;
						do{
							workTableInfo=new WorkTableModel(rs.getString("CODE"),rs.getString("NAME"));
							result.add(workTableInfo);
						}while(rs.next());
					}
				});
		return result;
	}
	
	private static String ZONE_TABLE_INFO="select z.zone_code as code, z.name"
								+" from dlv.zone z, dlv.region r, dlv.region_data rd"
								+" where r.id=rd.region_id"
								+" and Z.REGION_DATA_ID IN (select id from dlv.region_data where start_date =(select max(start_date) from dlv.region_data where region_id=? and start_date <=trunc(sysdate+8)) and region_id=?)"
								+" and Z.REGION_DATA_ID = rd.id";
	
	public Collection  getZoneRegionInfo(final String regionId){
		final Set result=new HashSet();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(ZONE_TABLE_INFO);
	                ps.setString(1, regionId);
	                ps.setString(2, regionId);
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						WorkTableModel workTableInfo;
						do{
							workTableInfo=new WorkTableModel(rs.getString("code"),rs.getString("NAME"));
							result.add(workTableInfo);
						}while(rs.next());
					}
				});
		return result;
	}

	
	@Override
	public void refreshDev(final String worktable) throws DataAccessException {
		CallableStatementCreator creator=new CallableStatementCreator(){
			public CallableStatement createCallableStatement(Connection connection) throws SQLException{
				CallableStatement cs=
						connection.prepareCall("{ call DLV.REFRESH_WORKTABLE(?) }");
				cs.setString(1, worktable);
				return cs;
			}
			
		};
		List params=new ArrayList();
		jdbcTemplate.call(creator,params);
		
	}
	
	public void refreshProd(final String worktable) throws DataAccessException {
		CallableStatementCreator creator=new CallableStatementCreator(){
			public CallableStatement createCallableStatement(Connection connection) throws SQLException{
				CallableStatement cs=
						connection.prepareCall("{ call DLV.REFRESH_WORKTABLE(?) }");
				cs.setString(1, worktable);
				return cs;
			}
			
		};
		List params=new ArrayList();
		jdbcTemplate.call(creator,params);
		
	}
	
	private static String SELECT_DELIVERY_CHARGE="select unique(rd.delivery_charges) as DeliveryFee" 
	+" from dlv.region_data rd, dlv.region r"
	+" where  RD.ID IN (select id from dlv.region_data where start_date =(select max(start_date) from dlv.region_data where region_id=? and start_date <=trunc(sysdate+8))and region_id=?)"
	+" and r.id=RD.REGION_ID"
	+" and region_id=?";

	public String getDeliveryCharge(final String regionId){
		final StringBuffer result = new StringBuffer();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(SELECT_DELIVERY_CHARGE);
	                ps.setString(1, regionId);
	                ps.setString(2, regionId);
	                ps.setString(3, regionId);
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						do{
							result.append(rs.getString("DeliveryFee"));
						}while(rs.next());
					}
				});
		return result.toString();
	}

	
	//Insert new region data ID
	private static String INSERT_REGION_DATA="insert into dlv.region_data (id, region_id, start_date, delivery_charges)"
				+" values (dlv.system_seq.nextval, ?, trunc(sysdate+8), ?)";
	
	public void insertNewRegionDataId(final String regionId, final String dlvCharge){
			
		this.jdbcTemplate.update(INSERT_REGION_DATA, new Object[]{regionId, dlvCharge});

	}
	
	
	//Only when new zone is selected
	private static String MAX_REGION_ID="select id from dlv.region_data where region_id=? order by start_date desc, id desc";
	
	private static String INSERT_NEW_ZONE="insert into dlv.zone (id, name, geoloc, zone_code, region_data_id, plan_id, ct_active, ct_release_time)"
		+" select dlv.system_seq.nextval, name, geoloc, code, XYZ,"
		+" (select plan_id from dlv.zone where zone_code = z.code and region_data_id = ?) as plan_id,"
		+" ' ' as ct_active,"
		+" 0 as ct_release_time"
		+" from DLV.WORKTABLE z where"
		+" z.code = ?";
	
	public void insertNewZone(final String worktable, final String regionId, final String zoneCode){
		
		final List result=new ArrayList();
				
		PreparedStatementCreator ctr=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(MAX_REGION_ID);
                ps.setString(1, regionId);
               // ps.execute();
                return ps;
            }  
		};
		
		jdbcTemplate.query(ctr, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				do{
					String id=rs.getString("id");
					result.add(id);
				}while(rs.next());
			}
			
		});
		
		String maxId= (String)result.get(0);
		String secondMaxId=(String)result.get(1);
		
		String INSERT_NEW_ZONE_01 = INSERT_NEW_ZONE.replace("XYZ", maxId);
		
		String INSERT_NEW_ZONE_02 = INSERT_NEW_ZONE_01.replace("WORKTABLE", worktable);
		
		System.out.println("INSERT NEW ZONE QUERY"+INSERT_NEW_ZONE_02);
		
		this.jdbcTemplate.update(INSERT_NEW_ZONE_02, new Object[]{secondMaxId, zoneCode});
		System.out.println("Zone inserted");
		LOGGER.debug("NEW ZONE INSERTED");
		
	}
	
	//checking if exists deletes the previous the corresponding zone from zone_desc & Transp.zone
	public void deleteFromZoneDesc(String zoneCode){
		
		this.jdbcTemplate.update("delete from dlv.zone_desc where zone_code=?", new Object[]{zoneCode});
	}
	
	public void deleteFromTranspZone(String zoneCode){
		
		this.jdbcTemplate.update("delete from TRANSP.zone where zone_code=?", new Object[]{zoneCode});
	}
	
	
	
	//insert new zone into ZONE_DESC table
	private static String INSERT_ZONE_DESC=	" insert into dlv.zone_desc values (?, ' ')";
	
	public void insertIntoZoneDesc(final String zoneCode){
		
		this.jdbcTemplate.update(INSERT_ZONE_DESC, new Object[]{zoneCode});
	}
	
	//insert new zone into TRANSP.ZONE table
	private static String INSERT_ZONE_TRANSPZONE=" insert into transp.zone select" 
		+" (select code from DLV.WORKTABLE where code=?) as zone_code, (select name from DLV.WORKTABLE where code=?) as name, UNATTENDED, ZONE_TYPE, AREA," 
		+" REGION, OBSOLETE, PRIORITY, stem_from_time, stem_to_time, stem_time, cos_enabled, servicetime_type FROM"
		+" transp.zone x where x.ZONE_CODE = (select zone_code from transp.zone where rownum=1)";
		
	
	public void insertIntoTranspZone(final String zoneCode, final String worktable){
		
		String INSERT_ZONE_TRANSPZONE_01=INSERT_ZONE_TRANSPZONE.replace("WORKTABLE", worktable);
		
		this.jdbcTemplate.update(INSERT_ZONE_TRANSPZONE_01, new Object[]{zoneCode, zoneCode});
	}
	
	
	
	//Common zones selected
	private static String INSERT_COMMON_ZONES="insert into dlv.zone (id, name, geoloc, zone_code, region_data_id, plan_id, ct_active, ct_release_time)"
		+" select dlv.system_seq.nextval, name, geoloc, code, XYZ,"
		+" (select plan_id from dlv.zone where zone_code = z.code"
		+" and region_data_id = ?) as plan_id,"
		+" (select ct_active from dlv.zone where zone_code = z.code"
		+" and region_data_id = ?) as ct_active,"
		+" (select ct_release_time from dlv.zone where zone_code = z.code"
		+" and region_data_id = ?) as ct_release_time"
		+" from DLV.WORKTABLE z where"
		+" z.code = ?";

	public void insertCommonZoneSelected(final String worktable, final String regionId, final String zoneCode){
	
		final List result=new ArrayList();
		
			PreparedStatementCreator ctr=new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(MAX_REGION_ID);
					ps.setString(1, regionId);
					return ps;
				}  
			};
			
			jdbcTemplate.query(ctr, new RowCallbackHandler(){
				public void processRow(ResultSet rs) throws SQLException{
					do{
						String id=rs.getString("id");
						result.add(id);
					}while(rs.next());
				}
			
			});
		
		String maxId= (String)result.get(0);
		String secondMaxId=(String)result.get(1);
		
		String INSERT_COMMON_ZONES_01 = INSERT_COMMON_ZONES.replace("XYZ", maxId);
		
		String INSERT_COMMON_ZONES_02 = INSERT_COMMON_ZONES_01.replace("WORKTABLE", worktable);
		
		this.jdbcTemplate.update(INSERT_COMMON_ZONES_02, new Object[]{secondMaxId, secondMaxId, secondMaxId,zoneCode});
		
		LOGGER.debug("ZONE INFO FROM WORKTABLE INSERTED");

	}
	
	//Unchecked zones from zone table
	private final static String INSERT_REMAINING_ZONES=
		" insert into dlv.zone (id, name, geoloc, zone_code, region_data_id, plan_id, ct_active, ct_release_time)"
			+" select dlv.system_seq.nextval,"
		    +" (select name from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as name,"
		    +" (select geoloc from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as geoloc,"
		    +" (select zone_code from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as zone_code,"
		    +" XYZ,"  
            +" (select plan_id from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as plan_id,"
		    +" (select ct_active from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as ct_active,"
		    +" (select ct_release_time from dlv.zone where zone_code = z.zone_code and region_data_id = z.region_data_id) as ct_release_time"
	    +" from dlv.zone z where zone_code = ? and region_data_id = ?";
		
	public void insertUncheckedZones(final String zoneCode, final String regionId){
		final List result=new ArrayList();
		
		PreparedStatementCreator ctr=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(MAX_REGION_ID);
				ps.setString(1, regionId);
				return ps;
			}  
		};
		
		jdbcTemplate.query(ctr, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				do{
					String id = rs.getString("id");
					result.add(id);
				}while(rs.next());
			}
		
		});
		
		String maxId= (String)result.get(0);
		String secondMaxId=(String)result.get(1);
		
		String INSERT_REMAINING_ZONES_01=INSERT_REMAINING_ZONES.replace("XYZ", maxId);
		
		this.jdbcTemplate.update(INSERT_REMAINING_ZONES_01, new Object[]{zoneCode, secondMaxId});
		
		
	}
	
	private final static String MULTIPLE_UPDATES = "update dlv.region_data set start_date = (select min(start_date)-1 from dlv.region_data where region_id = ?)" 
							+" where start_date = trunc(sysdate+8) and region_id = ? and id not in" 
							+" (select max(id) from dlv.region_data where region_id = ? and start_date  = trunc(sysdate+8))";
	
	public void updateMultipleDays(String regionId){
		
		this.jdbcTemplate.update(MULTIPLE_UPDATES, new Object[] {regionId, regionId, regionId});
	}
	
	//Zone Expansion
	private final static String ZONE_EXPANSION="update dlv.zone set geoloc=(select geoloc from DLV.WORKTABLE where code=?) where region_data_id=? and zone_code=?";
	
	public void doExpansion(String worktable, final String regionId, String zoneCode){
		
		final List result=new ArrayList();
		
		PreparedStatementCreator ctr=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(MAX_REGION_ID);
				ps.setString(1, regionId);
				return ps;
			}  
		};
		
		jdbcTemplate.query(ctr, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				do{
					String id = rs.getString("id");
					result.add(id);
				}while(rs.next());
			}
		
		});
		
		String maxId= (String)result.get(0);
		
		String ZONE_EXPANSION_01 = ZONE_EXPANSION.replace("WORKTABLE", worktable);
		
		this.jdbcTemplate.update(ZONE_EXPANSION_01, new Object[]{zoneCode,maxId,zoneCode});
		
	}
	
	private static String DELETE_TIMESLOTS = "delete from dlv.timeslot where base_date <= trunc(sysdate) and resource_id in"
				+" (select id from dlv.planning_resource where zone_code=? and day <=trunc(sysdate))";
	
    public void deleteTimeslot(String zoneCode) {
    	
    	this.jdbcTemplate.update(DELETE_TIMESLOTS,new Object[]{zoneCode});
    }
	
    private static String DELETE_TRUCKRESOURCE = "delete from dlv.truck_resource where resource_id in" 
    	+" (select id from dlv.planning_resource where zone_code=? and day <=trunc(sysdate))";
    
	public void deleteTrunkResource(String zoneCode){
		this.jdbcTemplate.update(DELETE_TRUCKRESOURCE,new Object[]{zoneCode});
	}
	
	private static String DELETE_PLANNING_RESOURCE="delete from dlv.planning_resource where day <= trunc(sysdate) and zone_code=?";
	
	public void deletePlanningResource(String zoneCode){
		this.jdbcTemplate.update(DELETE_TRUCKRESOURCE, new Object[]{zoneCode});		
	}
	
	
	public void updateTimeslot(String zoneCode){
		this.jdbcTemplate.update(
		   "update dlv.timeslot set base_date=base_date-7 where resource_id in (select id from dlv.planning_resource where zone_code=? and day > trunc(sysdate))"
								, new Object[] {zoneCode});
	}
	
	
	public void updatePlanningResource(String zoneCode){
		this.jdbcTemplate.update(
		   "update dlv.planning_resource set day=day-7 where zone_code=? and day > trunc(sysdate)", new Object[] {zoneCode});
	}
	
	private final static String START_DATE_QUERY ="select id from dlv.region_data where region_id = ? and start_date <= trunc(sysdate+8) and start_date > trunc(sysdate)";
	
	public List getStartDateForRegion(final String regionId){
		final List result=new ArrayList();
		
		PreparedStatementCreator ctr=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(START_DATE_QUERY);
				ps.setString(1, regionId);
				return ps;
			}  
		};
		
		jdbcTemplate.query(ctr, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				do{
					String id=rs.getString("id");
					result.add(id);
				}while(rs.next());
			}
		
		});
		return result;
	}
	public void updateStartDate(String regionId){
		this.jdbcTemplate.update(
				   "update dlv.region_data set START_DATE=trunc(sysdate)-1 where region_id=? and start_date = trunc(sysdate)", new Object[] {regionId});
	}
	
	
	//make dev live
	public void makeDevLive(final String regionId){
		
		final List result=new ArrayList();
		
		PreparedStatementCreator ctr=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(MAX_REGION_ID);
				ps.setString(1, regionId);
				return ps;
			}  
		};
		
		jdbcTemplate.query(ctr, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				do{
					String id=rs.getString("id");
					result.add(id);
				}while(rs.next());
			}
		
		});
	
	    String maxId= (String)result.get(0);
		
		this.jdbcTemplate.update(
		   "update dlv.region_data set start_date=trunc(sysdate) where id=?", new Object[] {maxId});
	}
	
	
	
	//Geo Restrictions
	
	private static String GRO_RESTRICTION_WORK_TABLE = "select CODE, NAME from DLV.GEO_RESTRICTION_WORKTAB";
	
	public Collection getGeoRestrictionWorkTableInfo(){
		final Set result=new HashSet();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GRO_RESTRICTION_WORK_TABLE);
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						WorkTableModel workTableInfo;
						do{
							workTableInfo=new WorkTableModel(rs.getString("CODE"),rs.getString("NAME"));
							result.add(workTableInfo);
						}while(rs.next());
					}
				});
		return result;
	}
	
	private static String GEO_RESTRICTION_BOUNDARY_INFO = "select CODE, NAME from DLV.GEO_RESTRICTION_BOUNDARY";
	
	public Collection  getGeoRestrictionBoundaryInfo(){
		final Set result=new HashSet();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GEO_RESTRICTION_BOUNDARY_INFO);
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						WorkTableModel workTableInfo;
						do{
							workTableInfo=new WorkTableModel(rs.getString("code"),rs.getString("NAME"));
							result.add(workTableInfo);
						}while(rs.next());
					}
				});
		return result;
	}
	
	//Insert new Geo Restriction
	public void insertNewGeoRestriction(final String zoneCode){
		
		this.jdbcTemplate.update(
				   "INSERT into DLV.GEO_RESTRICTION_BOUNDARY select code, name, geoloc from DLV.GEO_RESTRICTION_WORKTAB t where t.code=?", new Object[] {zoneCode});
	}
	
	
	//Update Geo Restriction
	public void updateGeoRestriction(final String zoneCode){
		
		this.jdbcTemplate.update(
				   "UPDATE DLV.GEO_RESTRICTION_BOUNDARY t set t.geoloc =(select geoloc from DLV.GEO_RESTRICTION_WORKTAB where code=?) where t.code=?", new Object[] {zoneCode, zoneCode});
	}
	
	@Override
	public void refreshGeoRestrictionWorktable(final String worktable) throws DataAccessException {
		CallableStatementCreator creator=new CallableStatementCreator(){
			public CallableStatement createCallableStatement(Connection connection) throws SQLException{
				CallableStatement cs=
						connection.prepareCall("{ call DLV.REFRESH_WORKTABLE(?) }");
				cs.setString(1, worktable);
				return cs;
			}
			
		};
		List params=new ArrayList();
		jdbcTemplate.call(creator,params);
		
	}
	
}