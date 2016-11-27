package com.freshdirect.transadmin.parser;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;

public abstract class AssetBaseProcessor implements IAssetProcessor {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public static DataSource getDataSource() throws NamingException {
	        InitialContext initCtx = null;
	  try {
	      initCtx = getInitialContext();
	      return (DataSource) initCtx.lookup("fdtrndatasource");
	  } finally {
	        if (initCtx!=null) try {
	               initCtx.close();
	        } catch (NamingException ne) {}
	  }
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public InitialContext getInitialContext() throws NamingException {
	        Hashtable h = new Hashtable();
	        h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	        h.put(Context.PROVIDER_URL, "t3://crm01.crm.stprd2a.nyc2.freshdirect.com:7001"/*"t3://localhost:7001"*/);//"t3://app01.stprd01.nyc2.freshdirect.com:7001"
	        return new InitialContext(h);
	}
	
	private static final String GET_ASSETSEQ_QRY = "select TRANSP.ASSETSEQ.nextval FROM DUAL";
	
	private static final String INSERT_ASSETS = "insert into transp.asset(ASSET_ID, ASSET_NO, ASSET_TYPE, ASSET_DESCRIPTION, ASSET_STATUS, ASSET_TEMPLATE, BARCODE) values (?,?,?,?,?,?,?)";
	
	private static final String INSERT_ASSETATTRIBUTES = "insert into transp.asset_attribute (ASSET_ID, ATTRIBUTE_TYPE, ATTRIBUTE_VALUE, ATTRIBUTE_MATCH) values (?,?,?,?) ";
	
	public String getNewAssetId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_ASSETSEQ_QRY);
	}
	
	@SuppressWarnings("unchecked")
	public void logAsset(Asset record) throws SQLException {
		Connection connection = null;
			
			try {
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_ASSETS);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
				
				BatchSqlUpdate batchAttrUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_ASSETATTRIBUTES);
				batchAttrUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchAttrUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchAttrUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchAttrUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchAttrUpdater.compile();
	
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				String assetId = null;
				assetId = this.getNewAssetId();
				batchUpdater.update(new Object[]{ assetId
													, record.getAssetNo()
													, record.getAssetType().getCode()
													, record.getAssetDescription()
													, record.getAssetStatus().getName()
													, null
													, record.getBarcode()
				});
				
				Set<AssetAttribute> attributes = record.getAssetAttributes();
				if(attributes != null){
					for(AssetAttribute atrModel : attributes){
						batchAttrUpdater.update(new Object[]{ assetId
													, atrModel.getId().getAttributeType()
													, atrModel.getAttributeValue()
													, atrModel.getAttributeMatch()
											});
					}
				}				
				batchUpdater.flush();
				batchAttrUpdater.flush();
			} finally{
				if(connection!=null) connection.close();
			}		
	}
	
	private static final String GET_ASSETS = "select * from transp.asset where asset_no = ? ";
	
	private static final String CLEAR_ASSETATTRIBUTES = "delete from transp.asset_attribute where asset_id in (select asset_id from transp.asset where asset_type in ('TRUCK','TRAILER')) ";
	
	private static final String CLEAR_ASSETS = "delete from transp.asset where asset_type in ('TRUCK','TRAILER') ";
		
	public void clearAsset(final String assetNo) throws SQLException {
		
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();	
		/*	final Asset asset = new Asset();
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(GET_ASSETS);
					ps.setString(1, assetNo);
					return ps;
				}  
			};

			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
					public void processRow(ResultSet rs) throws SQLException {				    	
						do {
							asset.setAssetId(rs.getString("ASSET_ID"));
							asset.setAssetNo(rs.getString("ASSET_NO"));
						} while(rs.next());		        		    	
					}
			});*/
			
			this.jdbcTemplate.update(CLEAR_ASSETATTRIBUTES, new Object[] {});
			
			this.jdbcTemplate.update(CLEAR_ASSETS, new Object[] {});
		} finally {
			if (connection != null)
				connection.close();
		}
	}
		
	public boolean processAssetRecord(Object object) throws SQLException {
		try {
			Asset record = (Asset) object;		
			//clearAsset(record.getAssetNo());
			//clearAsset(record.getCurrentAssetNo());
			logAsset(record);
			updateVIRRecord(record);
			updateMaintenanceRecord(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private static final String UPDATE_VIRRECORD = "UPDATE TRANSP.VIRRECORD X set X.TRUCKNUMBER = ? WHERE X.TRUCKNUMBER = ? ";
	
	public void updateVIRRecord(Asset record) throws SQLException {
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_VIRRECORD);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();
				
			batchUpdater.update(new Object[]{ record.getAssetNo()											
											, record.getCurrentAssetNo()
										});		
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	private static final String UPDATE_MAINTENANCERECORD = "UPDATE TRANSP.MAINTENANCEISSUE X set X.TRUCKNUMBER = ? WHERE X.TRUCKNUMBER = ? ";
	
	public void updateMaintenanceRecord(Asset record) throws SQLException {
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), UPDATE_MAINTENANCERECORD);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();
				
			batchUpdater.update(new Object[]{ record.getAssetNo()											
											, record.getCurrentAssetNo()
										});		
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	
	
	private AssetManagerI assetManagerService;	

	private DomainManagerI domainManagerService;
	 
	public void initialize(AssetManagerI assetManagerService, DomainManagerI domainManagerService) {
		this.assetManagerService = assetManagerService;
		this.domainManagerService = domainManagerService;
		try {
			setDataSource(getDataSource());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
	}
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean processAssetRecord(Object object) throws SQLException {
		
		Asset record = (Asset) object;		
		Asset existingVO = assetManagerService.getAssetByAssetNumber(record.getAssetNo());
		
		//delete existing asset
		if(existingVO != null) {
			assetManagerService.removeEntityEx(existingVO);
		}
		
		Collection virRecords = domainManagerService.getVIRRecordByTrucNo(record.getCurrentAssetNo());
		if(virRecords != null && virRecords.size() > 0) {
			Iterator<VIRRecord> itr = virRecords.iterator();
			while(itr.hasNext()) {
				VIRRecord _r = itr.next();
				_r.setTruckNumber(record.getAssetNo());
			}
			domainManagerService.saveEntityList(virRecords);
		}
		
		Collection mRecords = domainManagerService.getMaintenanceIssueByTruckNo(record.getCurrentAssetNo());
		if(mRecords != null && mRecords.size() > 0) {
			Iterator<MaintenanceIssue> itr = mRecords.iterator();
			while(itr.hasNext()) {
				MaintenanceIssue _m = itr.next();
				_m.setTruckNumber(record.getAssetNo());
			}
			domainManagerService.saveEntityList(mRecords);
		}
		
		assetManagerService.saveAsset(record);
				
		//System.out.println("Insured Name "+record.getInsuredFirstName() +" "+ "Result -> "+ result);
		//addResult(record, true);
		return true;
	}*/
	
	public abstract void initialize();
	
	public abstract void addResult(Asset record, boolean processed);
	
	
}
