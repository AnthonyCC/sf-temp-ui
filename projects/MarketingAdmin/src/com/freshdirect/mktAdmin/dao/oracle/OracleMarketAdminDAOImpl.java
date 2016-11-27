package com.freshdirect.mktAdmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.mktAdmin.model.ReferralPromotionModel;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorStatusType;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.dao.MarketAdminDAOIntf;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;
import com.freshdirect.mktAdmin.model.RestrictedPromoCustomerModel;
import com.freshdirect.mktAdmin.util.CustomerModel;

public class OracleMarketAdminDAOImpl implements MarketAdminDAOIntf {

	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(OracleMarketAdminDAOImpl.class);
	
	public OracleMarketAdminDAOImpl()
	{		
	}
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String INSERT_COMPETITOR_LOCATION_INFORMATION="INSERT INTO MRKTING.COMPETITIVE_LOC ( MI_PRINX,"+
	"COMPETITOR_NAME, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP, COUNTRY,"+ 
	" SCRUBBED_ADDRESS, COMPETITOR_TYPE, GEOLOC, STATUS, DATE_CREATED ) VALUES ( "+
	 "?,?,?,?,?,?,?,?,?,?,?,MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL),?, SYSDATE )";
									
	public void insertCompetitorAddrModel(Collection collection) throws SQLException{
		Connection connection=null;
		try{				
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_COMPETITOR_LOCATION_INFORMATION);		
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile(); 
			
			Iterator iterator=collection.iterator();
			connection=this.jdbcTemplate.getDataSource().getConnection();
			
			while(iterator.hasNext()){
				
				CompetitorAddressModel model=(CompetitorAddressModel)iterator.next();
				
				batchUpdater.update(
				new Object[]{ SequenceGenerator.getNextId(connection, "MRKTING"), 
				model.getCompanyName(), model.getAddress1(),model.getAddress2(),model.getApartment(),model.getCity(),model.getState(),model.getZipCode(),model.getCountry(),
				model.getScrubbedStreet(),model.getCompetitorType().getName(),""+model.getAddressInfo().getLongitude(),""+model.getAddressInfo().getLatitude(),model.getStatus().getName()}
				);			
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
		
		LOGGER.debug("Data is inserted");
	}
		
	private static final String VIEW_ALL_COMPETITOR_LOCATION_QRY="SELECT * FROM MRKTING.COMPETITIVE_LOC ORDER BY MI_PRINX";
	
	public Collection getCompetitorInfo() 
	throws SQLException 
	{				         
		         //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
		         final List list = new ArrayList();		         
		         PreparedStatementCreator creator=new PreparedStatementCreator() {
		             public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
		                 PreparedStatement ps =
		                     connection.prepareStatement(VIEW_ALL_COMPETITOR_LOCATION_QRY);
		                 return ps;
		             }  
		         };
		         jdbcTemplate.query(creator, 
		        		  new RowCallbackHandler() { 
		        		      public void processRow(ResultSet rs) throws SQLException {
		        		    	
		        		    	do {
		        		    		CompetitorAddressModel model=new CompetitorAddressModel();
		        		    		model.setCompanyName(rs.getString("COMPETITOR_NAME"));
		        		    		model.setAddress1(rs.getString("ADDRESS1"));
		        		    		model.setAddress2(rs.getString("ADDRESS2"));
		        		    		model.setApartment(rs.getString("APARTMENT"));
		        		    		model.setCity(rs.getString("CITY"));
		        		    		model.setState(rs.getString("STATE"));
		        		    		model.setZipCode(rs.getString("ZIP"));
		        		    		model.setCompetitorType(EnumCompetitorType.getEnum(rs.getString("COMPETITOR_TYPE")));
		        		    		model.setCountry(rs.getString("COUNTRY"));
		        		    		model.setDateCreated(rs.getDate("DATE_CREATED"));		        		    		
		        		    		model.setStatus(EnumCompetitorStatusType.getEnum(rs.getString("STATUS")));
		        		    		model.setId(""+rs.getLong("MI_PRINX"));
		        		    		list.add(model);
		        		    	   }
		        		    	   while(rs.next());		        		    	
		        		      }
		        		  }
		        	); 
		         LOGGER.debug("OracleMarketAdminDAOImpl : getCompetitorInfo end  "+list);
		     return list;
	}
	
	
	private static final String GET_COMPETITOR_LOCATION_QRY="SELECT * FROM MRKTING.COMPETITIVE_LOC WHERE MI_PRINX=?";
	
	public CompetitorAddressModel getCompetitorInfo(String competitorId) throws SQLException{
		
		CompetitorAddressModel actor = (CompetitorAddressModel) this.jdbcTemplate.queryForObject(
			    GET_COMPETITOR_LOCATION_QRY,
			    new Object[]{competitorId},
			    new RowMapper() {

			        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			        	CompetitorAddressModel model=new CompetitorAddressModel();
    		    		model.setCompanyName(rs.getString("COMPETITOR_NAME"));
    		    		model.setAddress1(rs.getString("ADDRESS1"));
    		    		model.setAddress2(rs.getString("ADDRESS2"));
    		    		model.setApartment(rs.getString("APARTMENT"));
    		    		model.setCity(rs.getString("CITY"));
    		    		model.setState(rs.getString("STATE"));
    		    		model.setZipCode(rs.getString("ZIP"));
    		    		model.setCompetitorType(EnumCompetitorType.getEnum(rs.getString("COMPETITOR_TYPE")));
    		    		model.setCountry(rs.getString("COUNTRY"));
    		    		model.setDateCreated(rs.getDate("DATE_CREATED"));    		    		
    		    		model.setStatus(EnumCompetitorStatusType.getEnum(rs.getString("STATUS")));
    		    		model.setId(""+rs.getLong("MI_PRINX"));
			            return model;
			        }
			    });
		return actor;
	}
	
	private static final String UPDATE_COMPETITOR_LOCATION_QRY="" +
			"UPDATE MRKTING.COMPETITIVE_LOC SET COMPETITOR_NAME=?, ADDRESS1=?, ADDRESS2=?, APARTMENT=?, CITY=?, STATE=?, ZIP=?, COUNTRY=?, SCRUBBED_ADDRESS=?, COMPETITOR_TYPE=?, GEOLOC=MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL), STATUS=?  WHERE MI_PRINX=?";
	
	public void storeCompetitorInformation(Collection collection) throws SQLException{
		
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_COMPETITOR_LOCATION_QRY);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));		
		
		Iterator iterator=collection.iterator();	
		while(iterator.hasNext()){
			CompetitorAddressModel model=(CompetitorAddressModel)iterator.next();
			this.jdbcTemplate.update(UPDATE_COMPETITOR_LOCATION_QRY, 
					new Object[] {model.getCompanyName(),model.getAddress1(),model.getAddress2(),model.getApartment(),model.getCity(),model.getState(),model.getZipCode(),model.getCountry(),model.getScrubbedStreet(),model.getCompetitorType().getName(),""+model.getAddressInfo().getLongitude(),""+model.getAddressInfo().getLatitude(),model.getStatus().getName(),model.getId()});				    				   
		}
		
		batchUpdater.flush();
		LOGGER.debug("Data is updated");		
	}

	private static final String DELETE_COMPETITOR_LOCATION_QRY="" +
	"DELETE FROM MRKTING.COMPETITIVE_LOC WHERE MI_PRINX=?";
	
	public void deleteCompetitorInfo(Collection collection) throws SQLException{
		
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_COMPETITOR_LOCATION_QRY);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		Iterator iterator=collection.iterator();	
		while(iterator.hasNext()){
			String competitorId=(String)iterator.next();
			this.jdbcTemplate.update(DELETE_COMPETITOR_LOCATION_QRY, 
					new Object[] {competitorId});				    				   
		}
		
		batchUpdater.flush();
		LOGGER.debug("Data is deleted");
	}		
	
	private static final String GET_COMPETITOR_LOCATION_COUNT="SELECT COUNT(*) FROM MRKTING.COMPETITIVE_LOC WHERE COMPETITOR_NAME=? AND SCRUBBED_ADDRESS=? AND CITY=? AND STATE=? AND ZIP=? AND COUNTRY=? ";
	
	public boolean isAddressAllreadyExists(CompetitorAddressModel model) throws SQLException{
		
		int count = this.jdbcTemplate.queryForInt(GET_COMPETITOR_LOCATION_COUNT,	new Object[]{model.getCompanyName(),model.getScrubbedStreet(), model.getCity(), model.getState(), model.getZipCode(), model.getCountry()});
	    if(count==0)
	    	return false;
	    else
	        return true;
	}
	
	private static final String INSERT_CUSTOMER_LOCATION_INFORMATION="INSERT INTO MRKTING.CUSTOMER_ADDR ( MI_PRINX,"+
																	 "ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP, COUNTRY," +
																	 " SCRUBBED_ADDRESS,  GEOLOC,  DATE_CREATED ) VALUES ( " +
																	 " ?,?,?,?,?,?,?,?,?,MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL), SYSDATE )";



	public void insertCustomerAddrModel(Collection collection) throws SQLException{
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_CUSTOMER_LOCATION_INFORMATION);		
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));		
		
		batchUpdater.compile(); 
		
		Iterator iterator=collection.iterator();
		while(iterator.hasNext()){
			
			CustomerAddressModel model=(CustomerAddressModel)iterator.next();
			
			batchUpdater.update(
			new Object[]{ model.getCustomerId(),  model.getAddress1(),model.getAddress2(),model.getApartment(),model.getCity(),model.getState(),model.getZipCode(),model.getCountry(),
			model.getScrubbedStreet(),""+model.getAddressInfo().getLongitude(),""+model.getAddressInfo().getLatitude()}
			);			
		}
		batchUpdater.flush();	
		LOGGER.debug("Data is inserted");
		
	}
	
	private static final String GET_CUSTOMER_LOCATION_COUNT="SELECT COUNT(*) FROM MRKTING.CUSTOMER_ADDR WHERE MI_PRINX=?";
	
	public boolean isCustomerAllreadyExists(CustomerAddressModel model) throws SQLException{
		int count = this.jdbcTemplate.queryForInt(GET_CUSTOMER_LOCATION_COUNT,	new Object[]{model.getCustomerId()});
	    if(count==0)
	    	return false;
	    else
	        return true;
	}
	
	
	private static final String GET_PROMO_CUSTOMER="select *   from  ( select a.*, rownum rnum   from  ( "+
		"select pc.customer_id,pc.promotion_id,pc.usage_cnt,pc.expiration_date,ci.first_name,ci.last_name ,nvl(c.user_id,pc.customer_email) as user_id " +
		"from cust.promo_customer pc,cust.customer c,cust.customerinfo ci " +
		"where pc.customer_id = c.id (+) and c.id = ci.customer_id (+) and pc.promotion_id= ?";
	
	public Collection getRestrictedCustomers(String promotionId,String searchKey,long startIndex,long endIndex) throws SQLException{
		LOGGER.debug("promotionId in getRestrictedCustomers():"+promotionId);
		final StringBuffer sql=new StringBuffer(GET_PROMO_CUSTOMER);
        final List list = new ArrayList();	
        final String promoId=promotionId;
        final String searchStr=searchKey;
        final long sIndex=startIndex;
        final long eIndex=endIndex;
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            	if(searchStr!=null && searchStr.trim().length()>0){
            		sql.append(" and ( pc.customer_id=? or c.user_id=?) ");
            	}
            	sql.append(" ) a   where rownum <= "+eIndex+" ) where rnum >"+sIndex);
            	LOGGER.debug(" GET_PROMO_CUSTOMER :"+sql.toString());
                PreparedStatement ps =
                    connection.prepareStatement(sql.toString());

                ps.setString(1,promoId);
                if(searchStr!=null && searchStr.trim().length()>0){
                	ps.setString(2,searchStr);
                	ps.setString(3,searchStr);
                }
                
                
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {       		    	
       		    	do {
       		    		RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
       		    		model.setCustomerId(rs.getString("customer_id"));
       		    		model.setPromotionId(rs.getString("promotion_id"));
       		    		model.setUsageCount(rs.getInt("usage_cnt"));
       		    		model.setCustEmailAddress(rs.getString("user_id"));  
       		    		model.setFirstName(rs.getString("first_name"));
       		    		model.setLastName(rs.getString("last_name"));
       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());		        		    	
       		      }
       		  }
       	); 
        LOGGER.debug("OracleMarketAdminDAOImpl : getRestrictedCustomers end  "+list.size());
        return list;
		
	}
	
	
	private static String DELETE_PROMO_CUSTOMER="delete from cust.promo_Customer where customer_id=? and promotion_id=?";
	
	private static String DELETE_PROSPECT_CUSTOMER="delete from cust.promo_Customer where customer_email=? and promotion_id=?";
	

	public void deleteRestrictedCustomers(String promotionId, String customerId, String email) throws SQLException {
		if(customerId != null && customerId.length() > 0) {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_PROMO_CUSTOMER);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
										
			this.jdbcTemplate.update(DELETE_PROMO_CUSTOMER, 
						new Object[] {customerId,promotionId});				    				   
					
			batchUpdater.flush();
		} else {
			//delete prospect customer
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_PROSPECT_CUSTOMER);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
										
			this.jdbcTemplate.update(DELETE_PROSPECT_CUSTOMER, 
						new Object[] {email,promotionId});				    				   
					
			batchUpdater.flush();
		}
		LOGGER.debug("Data is deleted");
	}

	//private static String PLACE_HOLDER="";
	private static String GET_EXISTING_PROMO_CUSTOMER="select customer_id,promotion_id from cust.promo_customer where customer_id in ('";
	;
	
	public Collection getRestrictedCustomers(Collection collection) throws SQLException {
		// TODO Auto-generated method stub
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
		final StringBuffer customerIdStr=new StringBuffer();
		final StringBuffer sql=new StringBuffer(GET_EXISTING_PROMO_CUSTOMER);
		String tmpPromoId=""; 
		Iterator iterator=collection.iterator();
		int i=0;
        while(iterator.hasNext()){
        	RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();        	
        	i=i+1;
        	if(i==1){
        		tmpPromoId=model.getPromotionId();
        		LOGGER.debug("tmpPromoId :::"+tmpPromoId);
        		customerIdStr.append(model.getCustomerId());
				if(i!=collection.size()) customerIdStr.append("',"); 
			}
			else{
				if(i==collection.size())	
				{
					customerIdStr.append("'").append(model.getCustomerId());					
				}
				else{
					customerIdStr.append("'").append(model.getCustomerId()).append("',");					
				}				
			}        	        	
        	//tmpPromoId=model.getPromotionId();
        }
               
        final List list = new ArrayList();
        final String promotionId=tmpPromoId;
        sql.append(customerIdStr.toString()).append("') and promotion_id=?");
        LOGGER.debug("customerIdStr :"+customerIdStr.toString());
        LOGGER.debug("promotionId :"+promotionId.toString());
        LOGGER.debug("GET_EXISTING_PROMO_CUSTOMER :"+sql.toString());
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(sql.toString());
                //ps.setString(1,customerIdStr.toString().trim()); //
                ps.setString(1,promotionId);
                return ps;
            }  
        };
        
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {       		    	
       		    	do {
       		    		RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
       		    		model.setCustomerId(rs.getString("customer_id"));
       		    		model.setPromotionId(rs.getString("promotion_id"));
       		    		//model.setUsageCount(rs.getInt("usage_cnt"));
       		    		//model.setCustEmailAddress(rs.getString("user_id"));       		
       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());	        		    	
       		      }
       		  }
       	); 
        LOGGER.debug("OracleMarketAdminDAOImpl : getRestrictedCustomers end1  "+list.size());
        return list;
	}

	
	private static final String INSERT_RESTRICTED_CUSTOMER_INFORMATION="insert into cust.promo_Customer(CUSTOMER_ID,PROMOTION_ID) values(?,?)";
	
	public void insertRestrictedCustomers(Collection restCustomerModel) throws SQLException {
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_RESTRICTED_CUSTOMER_INFORMATION);		
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		batchUpdater.compile(); 
		
		Iterator iterator=restCustomerModel.iterator();
		while(iterator.hasNext()){			
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
			System.out.println("customer Id :"+model.getCustomerId()+" promotionId :"+model.getPromotionId());
			batchUpdater.update(
			new Object[]{ model.getCustomerId(),  model.getPromotionId()}
			);			
		}
		batchUpdater.flush();			
		LOGGER.debug("Data is inserted");		
	}
	
	private static final String INSERT_RESTRICTED_CUSTOMER=
			"insert into cust.promo_Customer(CUSTOMER_ID,PROMOTION_ID,EXPIRATION_DATE) " +
				"select ID, ?,? " +
				"from   cust.customer c " +
				"where  c.ID = ? " +
				"and    not exists (select 1 from cust.promo_customer " +
									"where promotion_id = ? " +
									"and customer_id = c.ID)";
	
	private static final String INSERT_RESTRICTED_PROSPECT_CUSTOMER=
		"insert into cust.promo_Customer(PROMOTION_ID,EXPIRATION_DATE,CUSTOMER_EMAIL) " +
			"select ?,?,? " +
			"from   dual " +
			"where  not exists (select 1 from cust.promo_customer " +
								"where promotion_id = ? " +
								"and customer_email = ?)";
	
	public void newInsertRestrictedCustomers(Collection restCustomerModel) throws SQLException {
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_RESTRICTED_CUSTOMER);		
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.DATE));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		
		batchUpdater.compile();
		
		BatchSqlUpdate batchUpdater1=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_RESTRICTED_PROSPECT_CUSTOMER);
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater1.declareParameter(new SqlParameter(Types.DATE));
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));		
		
		batchUpdater1.compile(); 
		
		Iterator iterator=restCustomerModel.iterator();
		List totalList = new ArrayList();
		while(iterator.hasNext()){			
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
			LOGGER.debug("customer Id :"+model.getCustomerId()+" promotionId :"+model.getPromotionId());
			totalList.add(model);
			if(model.getCustomerId() == null || model.getCustomerId().length() == 0) {
				batchUpdater1.update(
						new Object[]{ model.getPromotionId(),model.getExpirationDate(), model.getCustEmailAddress(),  model.getPromotionId(), model.getCustEmailAddress()}
						);
			} else {
				batchUpdater.update(
				new Object[]{ model.getPromotionId(),model.getExpirationDate(), model.getCustomerId(),  model.getPromotionId()}
				);
			}
		}
		batchUpdater.flush();
		batchUpdater1.flush();
		LOGGER.debug("Data is inserted");
	}

	private static final String DELETE_RESTRICTION_BATCH_QRY="delete from cust.promo_customer where customer_id=? and promotion_id=?";
	
	private static final String DELETE_RESTRICTION_BATCH_QRY1="delete from cust.promo_customer where upper(customer_email)=upper(?) and promotion_id=? and customer_id is null";
	
	public void deleteRestrictedCustomers(Collection collection) throws SQLException {
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_RESTRICTION_BATCH_QRY);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		BatchSqlUpdate batchUpdater1=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_RESTRICTION_BATCH_QRY1);
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater1.declareParameter(new SqlParameter(Types.VARCHAR));
		
		Iterator iterator=collection.iterator();	
		while(iterator.hasNext()){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
			if(model.getCustomerId() == null || model.getCustomerId().length() == 0) {
				//prospect customer
				this.jdbcTemplate.update(DELETE_RESTRICTION_BATCH_QRY1, 
						new Object[] {model.getCustEmailAddress(),model.getPromotionId()});
			} else {
				this.jdbcTemplate.update(DELETE_RESTRICTION_BATCH_QRY, 
					new Object[] {model.getCustomerId(),model.getPromotionId()});
			}
		}
		
		batchUpdater.flush();
		batchUpdater1.flush();
		
		LOGGER.debug("Data is deleted1");
		
	}

	private static final String DELETE_RESTRICTION_FOR_PROMOTION_QRY="delete from cust.promo_customer where promotion_id=?";
	
	public void deleteRestrictedCustomerForPromotion(String promotionId) throws SQLException {
		// TODO Auto-generated method stub
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_RESTRICTION_FOR_PROMOTION_QRY);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
		this.jdbcTemplate.update(DELETE_RESTRICTION_FOR_PROMOTION_QRY, 
					new Object[] {promotionId});				    				   		
		
		batchUpdater.flush();
		
		LOGGER.debug("Data is deleted1");
		
	}
	
	private static final String GET_CUSTOMER_FOR_EMAIL_QRY="select id,user_id from cust.customer where user_id in('";

	public Map getCustomerIdsForEmailAddress(String[] emailAddress) throws SQLException {
		// TODO Auto-generated method stub
		final Map customerIdMap=new HashMap();
		final StringBuffer emailIdStr=new StringBuffer();
		final StringBuffer sql=new StringBuffer(GET_CUSTOMER_FOR_EMAIL_QRY);
		
		for(int i=0;i<emailAddress.length;i++)
		{
			if(i==0){
				emailIdStr.append(emailAddress[i]);
				if(i+1!=emailAddress.length)  emailIdStr.append("',");
			}
			else{
				if(i==emailAddress.length-1)	
				{
					emailIdStr.append("'").append(emailAddress[i]);
				}
				else{
					emailIdStr.append("'").append(emailAddress[i]).append("',");
				}				
			}
		}
        
        sql.append(emailIdStr.toString()).append("')");
        LOGGER.debug("emailIdStr2 :"+emailIdStr.toString());       
        LOGGER.debug("GET_CUSTOMER_FOR_EMAIL_QRY :"+sql.toString());
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(sql.toString());                              
                return ps;
            }  
        };
        
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {       		    	
       		    	do {
       		    		
       		    		String userId=rs.getString("user_id");
       		    		String id=rs.getString("id");
       		    		customerIdMap.put(userId,id);
       		    	   }
       		    	   while(rs.next());	        		    	
       		      }
       		  }
       	); 
        LOGGER.debug("OracleMarketAdminDAOImpl : getRestrictedCustomers end1  "+customerIdMap);
        return customerIdMap;
	}
	
	
	private static final String DELETE_RESTRICETD_CUSTOMER="delete from cust.promo_customer where promotion_id=?";

	public void deleteRestrictedCustomer(String promotionId) throws SQLException {
		// TODO Auto-generated method stub
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_RESTRICETD_CUSTOMER);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
									
		this.jdbcTemplate.update(DELETE_RESTRICETD_CUSTOMER, 
					new Object[] {promotionId});				    				   
				
		batchUpdater.flush();
		LOGGER.debug("Data is deleted");		
	}

	private static final String SELECT_INVALID_CUSTOMER="select id , user_id, ? as promotion_id  from cust.customer where id in('";
	
	public Collection getInvalidCustomerIds(Collection collection) throws SQLException {
		// TODO Auto-generated method stub
		final StringBuffer customerIdStr=new StringBuffer();
		final StringBuffer sql=new StringBuffer(SELECT_INVALID_CUSTOMER);
		String tmpPromoId=null; 
		Iterator iterator=collection.iterator();
		int i=0;
        while(iterator.hasNext()){
        	RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();        	
        	i=i+1;
        	if(i==1){
        		tmpPromoId=model.getPromotionId();
        		customerIdStr.append(model.getCustomerId());
				if(i!=collection.size()) customerIdStr.append("',"); 
			}
			else{
				if(i==collection.size())	
				{
					customerIdStr.append("'").append(model.getCustomerId());					
				}
				else{
					customerIdStr.append("'").append(model.getCustomerId()).append("',");					
				}				
			}        	        	
        	tmpPromoId=model.getPromotionId();
        }
               
        final List list = new ArrayList();
        final String promotionId=tmpPromoId;
        sql.append(customerIdStr.toString()).append("')");
        LOGGER.debug("customerIdStr :"+customerIdStr.toString());
        
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(sql.toString());
                ps.setString(1,promotionId); //                
                return ps;
            }  
        };
        
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {       		    	
       		    	do {
       		    		/*RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
       		    		model.setCustomerId(rs.getString("id"));
       		    		model.setPromotionId(rs.getString("promotion_id"));
       		    		//model.setUsageCount(rs.getInt("usage_cnt"));
       		    		model.setCustEmailAddress(rs.getString("user_id"));       		
       		    		list.add(model);*/
       		    		list.add(rs.getString("id"));
       		    	   }
       		    	   while(rs.next());	        		    	
       		      }
       		  }
       	); 
        
//      remove the existing one from the pro customer model
		Iterator iterator1=collection.iterator();
		List invalidCustomerList=new ArrayList();
		while(iterator1.hasNext()){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
//			if(!list.contains(model)){
			if(!list.contains(model.getCustomerId())){
				LOGGER.debug("invalid model :"+model.getCustomerId());						
				invalidCustomerList.add(model);
			}					
		}		
		
        LOGGER.debug("OracleMarketAdminDAOImpl : getRestrictedCustomers end1  "+invalidCustomerList.size());
        return invalidCustomerList;        
	}	
		
	private static final String SELECT_INVALID_PROMO="select distinct id as promotion_id  from cust.promotion_new where id in('";
	public Collection getInvalidPromotions(Collection collection) throws SQLException {
		// TODO Auto-generated method stub
		final StringBuffer promoIdStr=new StringBuffer();
		final StringBuffer sql=new StringBuffer(SELECT_INVALID_PROMO);
		
		Iterator iterator=collection.iterator();
		int i=0;
        while(iterator.hasNext()){
        	RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();        	
        	i=i+1;
        	if(i==1){
        		
        		promoIdStr.append(model.getCustomerId());
				if(i!=collection.size()) promoIdStr.append("',"); 
			}
			else{
				if(i==collection.size())	
				{
					promoIdStr.append("'").append(model.getPromotionId());					
				}
				else{
					promoIdStr.append("'").append(model.getPromotionId()).append("',");					
				}				
			}        	        	
        	
        }
               
        final List list = new ArrayList();
       
        sql.append(promoIdStr.toString()).append("')");
        LOGGER.debug("promoIdStr :"+promoIdStr.toString());
        
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(sql.toString());
//                ps.setString(1,promotionId); //                
                return ps;
            }  
        };
        
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {       		    	
       		    	do {
       		    		/*RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
       		    		model.setCustomerId(rs.getString("id"));
       		    		model.setPromotionId(rs.getString("promotion_id"));
       		    		//model.setUsageCount(rs.getInt("usage_cnt"));
       		    		model.setCustEmailAddress(rs.getString("user_id")); */      		
       		    		list.add(rs.getString("promotion_id"));
       		    	   }
       		    	   while(rs.next());	        		    	
       		      }
       		  }
       	); 
        
//      remove the existing one from the pro customer model
		Iterator iterator1=collection.iterator();
		List invalidPromoList=new ArrayList();
		while(iterator1.hasNext()){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
			if(!list.contains(model.getPromotionId())){
				LOGGER.debug("Invalid promotion id: "+model.getPromotionId());						
				invalidPromoList.add(model);
			}					
		}		
		
        LOGGER.debug("OracleMarketAdminDAOImpl : getInvalidPromotions end1  "+invalidPromoList.size());
        return invalidPromoList;        
	}
	
	public static String upsOutageList = "select distinct TL.CUSTOMER_ID,  C.USER_ID, CI.FIRST_NAME, CI.LAST_NAME " +  
											" from MIS.TIMESLOT_event_hdr tl,  " +
											      "CUST.CUSTOMER c, " +
											      "CUST.CUSTOMERINFO ci " +
											"where TL.EVENTTYPE='GET_TIMESLOT' and TL.RESPONSE_TIME='0' " +
											"and  (select count(*) from mis.timeslot_event_dtl te where te.timeslot_log_id=tl.id)=0 "+
											"and TL.EVENT_DTM between TO_DATE(?, 'MM/DD/YYYY HH12:MI AM') " + 
											"and TO_DATE(?, 'MM/DD/YYYY HH12:MI AM') " +
											"and    tl.customer_id = c.id " +
											"and    c.id = ci.customer_id";

	
	public Collection<CustomerModel> getUpsOutageCustList(String fromDate, String endDate) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<CustomerModel> list = new ArrayList<CustomerModel>();
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(upsOutageList);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				CustomerModel cm = new CustomerModel();
				cm.setFirstName(rset.getString("FIRST_NAME"));
				cm.setLastName(rset.getString("LAST_NAME"));
				cm.setEmail(rset.getString("USER_ID"));
				cm.setCustomerId(rset.getString("CUSTOMER_ID"));
				list.add(cm);
			}
		} catch (Exception e) {
			LOGGER.error("Error getting UPSCustomer list", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	public final static String GET_PROMOTIONS = "select ID, CODE from cust.promotion_new p where REFERRAL_PROMO = 'Y'";

	public List<ReferralPromotionModel> getReferralPromotions() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<ReferralPromotionModel> list = new ArrayList<ReferralPromotionModel>();
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(GET_PROMOTIONS);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				ReferralPromotionModel rpm = new ReferralPromotionModel();
				rpm.setPromotionId(rset.getString("ID"));
				rpm.setDescription(rset.getString("CODE"));
				list.add(rpm);
			}
		} catch (Exception e) {
			LOGGER.error("Error getting UPSCustomer list", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	private static final String GET_USER = "select count(*) " +
		    "from CUST.REFERRAL_PRGM rp, " +
		    "CUST.REFERRAL_CUSTOMER_LIST rcl " +
		    "where  RCL.ERP_CUSTOMER_ID = ? " + 
		    "and    RCL.REFERAL_PRGM_ID = RP.ID " +
		    "and    trunc(RP.EXPIRATION_DATE) > trunc(sysdate) " +
		    "and    (rp.Delete_flag is null or rp.delete_flag != 'Y')"; 

	private static final String GET_USER_BY_REFID = "select count(*) " +
		    "from CUST.REFERRAL_PRGM rp, " +
		    "CUST.REFERRAL_CUSTOMER_LIST rcl " +
		    "where  RCL.ERP_CUSTOMER_ID = ? " + 
		    "and    RCL.REFERAL_PRGM_ID = RP.ID " +
		    "and    RP.ID != ? " +
		    "and    trunc(RP.EXPIRATION_DATE) > trunc(sysdate) " + 
		    "and    (rp.Delete_flag is null or rp.delete_flag != 'Y')"; 

	public boolean isValidCustomer(String cid, String referralId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			if (referralId != null && referralId.length() > 0) {
				pstmt = conn.prepareStatement(GET_USER_BY_REFID);
				pstmt.setString(2, referralId);
			} else {
				pstmt = conn.prepareStatement(GET_USER);
			}
			pstmt.setString(1, cid);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				int cnt = rset.getInt(1);
				if (cnt == 0) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error validating customer email", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	private static final String CHECK_USER = "select ID from cust.customer where upper(user_id) = upper(?)";

	public String isValidUserId(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String cid = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(CHECK_USER);
			pstmt.setString(1, email);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				cid = rset.getString(1);
			}
		} catch (Exception e) {
			LOGGER.error("Error validating customer email", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return cid;
	}

	private static final String INSERT_REFPROMO = "insert into  CUST.REFERRAL_PRGM (ID, EXPIRATION_DATE, GIVE_TEXT, GET_TEXT, DESCRIPTION, "
			+ "PROMOTION_ID, REFERRAL_FEE, DEFAULT_PROMO, SHARE_HEADER, SHARE_TEXT, GIVE_HEADER, GET_HEADER, NOTES, "
			+ "FB_IMAGE_PATH, FB_HEADLINE, FB_TEXT, TWITTER_TEXT, RL_PAGE_TEXT, RL_PAGE_LEGAL, INV_EMAIL_SUBJECT, INV_EMAIL_OFFER_TEXT, " 
			+ "INV_EMAIL_TEXT, INV_EMAIL_LEGAL, REF_CRE_EMAIL_SUB, REF_CRE_EMAIL_TEXT, add_by_date, add_by_user, sa_image_path) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, ?)";

	public String createRefPromo(ReferralAdminModel rModel) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String rcet = rModel.getReferralCreditEmailText();
		String iet = rModel.getInviteEmailText();
		String rpt = rModel.getReferralPageText();
		String ieOfferText = rModel.getInviteEmailOfferText();
		
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			String id = SequenceGenerator.getNextId(conn, "CUST");
			pstmt = conn.prepareStatement(INSERT_REFPROMO);
			pstmt.setString(1, id);
			java.util.Date convertToDate = null;
			try {
				convertToDate = format.parse(rModel.getExpirationDate());
			} catch (ParseException e) {
			}
			pstmt.setDate(2, new java.sql.Date(convertToDate.getTime()));
			pstmt.setString(3, rModel.getGiveText());
			pstmt.setString(4, rModel.getGetText());
			pstmt.setString(5, rModel.getDescription());
			pstmt.setString(6, rModel.getPromotionId());
			pstmt.setInt(7, Integer.parseInt(rModel.getReferralFee()));
			pstmt.setString(8, rModel.getDefaultPromo()?"Y":"N");
			pstmt.setString(9, rModel.getShareHeader());
			pstmt.setString(10, rModel.getShareText());
			pstmt.setString(11, rModel.getGiveHeader());
			pstmt.setString(12, rModel.getGetHeader());
			pstmt.setString(13, rModel.getNotes());
			pstmt.setString(14, rModel.getFbFile());
			pstmt.setString(15, rModel.getFbHeadline());
			pstmt.setString(16, rModel.getFbText());
			pstmt.setString(17, rModel.getTwitterText());
			pstmt.setString(18, rpt);
			pstmt.setString(19, rModel.getReferralPageLegal());
			pstmt.setString(20, rModel.getInviteEmailSubject());
			pstmt.setString(21, ieOfferText);
			pstmt.setString(22, iet);
			pstmt.setString(23, rModel.getInviteEmailLegal());
			pstmt.setString(24, rModel.getReferralCreditEmailSubject());
			pstmt.setString(25, rcet);
			pstmt.setString(26, rModel.getAddByUser());
			pstmt.setString(27, rModel.getSiteAccessImageFile());
			pstmt.execute();
			return id;
		} catch (Exception e) {
			LOGGER.error("Failed with insert into CUST.REFERRAL_PRGM", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static final String INSERT_USERS = "insert into CUST.REFERRAL_CUSTOMER_LIST(REFERAL_PRGM_ID, ERP_CUSTOMER_ID) values(?,?)";

	public List<String> addReferralCustomers(Collection<String> collection,
			String referralId) {
		System.out.println("\n\n\nStart uploading customers to campaign:" + referralId);
		long t1 = System.currentTimeMillis();
		Iterator<String> iter = collection.iterator();
		List<String> invalidUsers = new ArrayList<String>();
		try {			
			deleteRefPromoCustomers(referralId);
			BatchSqlUpdate update = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_USERS);
			update.declareParameter(new SqlParameter("referralId", Types.VARCHAR));
			update.declareParameter(new SqlParameter("cid", Types.VARCHAR));

			while (iter.hasNext()) {
				String email = (String) iter.next();
				// check if user is valid CUSTOMER				
				String cid = isValidUserId(email);
				if (cid != null) {
					// check if this email is already linked to another referral promo
					if (isValidCustomer(cid, referralId)) {
						// We have a valid user email. Insert into the table
						//System.out.println("Adding: " + email);
					    Object[] values = new Object[2];
					    values[0] = referralId;
					    values[1] = cid;
					    update.update(values);

					} else {
						invalidUsers.add(email);
					}
				} else {
					invalidUsers.add(email);
				}
			}
			update.flush();
		} catch (Exception e) {
			LOGGER.error("Failed with insert into CUST.REFERRAL_PRGM", e);
		} finally {
			long t2 = System.currentTimeMillis();
			System.out.println("Time took to upload the customers: " + (t2 - t1) + " (milliseconds)");
			System.out.println("End uploading customers to campaign:" + referralId);
		}
		
		return invalidUsers;
	}

	private static final String GET_REF_PROMOTIONS = "select  rp.DESCRIPTION, P.description as promo_description, SHARE_HEADER, SHARE_TEXT, GET_HEADER, GET_TEXT, GIVE_HEADER, GIVE_TEXT, REFERRAL_FEE,rp.EXPIRATION_DATE, " + 
            "DEFAULT_PROMO, NOTES,  rp.ID, PROMOTION_ID from CUST.REFERRAL_PRGM rp, " +
            "cust.promotion_new p " +
            "where RP.PROMOTION_ID = p.id " +
            "and (rp.Delete_flag is null or rp.delete_flag != 'Y')";

	private static final SimpleDateFormat format = new SimpleDateFormat(
			"MM/dd/yyyy");

	public List<ReferralAdminModel> getAllRefPromotions() throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<ReferralAdminModel> list = new ArrayList<ReferralAdminModel>();
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(GET_REF_PROMOTIONS);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				ReferralAdminModel rAdm = new ReferralAdminModel();
				rAdm.setReferralId(rset.getString("ID"));
				rAdm.setDescription(rset.getString("DESCRIPTION"));
				String date = format.format(rset.getDate("EXPIRATION_DATE"));
				rAdm.setExpirationDate(date);
				rAdm.setGetText(rset.getString("GET_TEXT"));
				rAdm.setGiveText(rset.getString("GIVE_TEXT"));
				rAdm.setPromotionId(rset.getString("PROMOTION_ID"));
				rAdm.setReferralFee(rset.getString("REFERRAL_FEE"));
				rAdm.setDefaultPromo(("Y".equals(rset.getString("DEFAULT_PROMO")))?true:false);
				rAdm.setShareHeader(rset.getString("SHARE_HEADER"));
				rAdm.setShareText(rset.getString("SHARE_TEXT"));
				rAdm.setGiveHeader(rset.getString("GIVE_HEADER"));
				rAdm.setGetHeader(rset.getString("GET_HEADER"));
				rAdm.setNotes(rset.getString("NOTES"));
				rAdm.setPromoDescription(rset.getString("promo_description"));
				list.add(rAdm);
			}
		} catch (Exception e) {
			LOGGER.error("Error getting Referral promo list", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	private static final String DELETE_PROMO = "update CUST.REFERRAL_PRGM set Delete_flag='Y', change_by_date = sysdate, change_by_user=? where ID = ?";

	public void deleteRefPromos(List<String> ids, String username) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			Iterator iter = ids.iterator();
			while (iter.hasNext()) {
				String id = (String) iter.next();
				pstmt = conn.prepareStatement(DELETE_PROMO);
				pstmt.setString(1, username);
				pstmt.setString(2, id);
				pstmt.execute();
			}
		} catch (Exception e) {
			LOGGER.error("Error getting Referral promo list", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}

	private static final String DELETE_PROMO_CUSTOMERS = "delete CUST.REFERRAL_CUSTOMER_LIST where REFERAL_PRGM_ID = ?";

	public void deleteRefPromoCustomers(String id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(DELETE_PROMO_CUSTOMERS);
			pstmt.setString(1, id);
			pstmt.execute();
		} catch (Exception e) {
			LOGGER.error("Error getting Referral promo list", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}

	private static final String GET_REF_PROMOTION = "select  rp.DESCRIPTION, P.CODE , SHARE_HEADER, SHARE_TEXT, GET_HEADER, GET_TEXT, GIVE_HEADER, GIVE_TEXT, " +
			"REFERRAL_FEE,rp.EXPIRATION_DATE, DEFAULT_PROMO, NOTES,  rp.ID, PROMOTION_ID, rp.FB_IMAGE_PATH, rp.FB_HEADLINE, rp.FB_TEXT, rp.TWITTER_TEXT, " + 
			"rp.RL_PAGE_TEXT, rp.RL_PAGE_LEGAL, rp.INV_EMAIL_SUBJECT, rp.INV_EMAIL_OFFER_TEXT, rp.INV_EMAIL_TEXT, rp.INV_EMAIL_LEGAL, rp.REF_CRE_EMAIL_SUB, rp.REF_CRE_EMAIL_TEXT, rp.sa_image_path " +
			"from CUST.REFERRAL_PRGM rp, " +
				  "cust.promotion_new p " +
			"where rp.id = ? " + 
			"and RP.PROMOTION_ID = p.id";

	public ReferralAdminModel getRefPromotionInfo(String id)
			throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(GET_REF_PROMOTION);
			pstmt.setString(1, id);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				ReferralAdminModel rAdm = new ReferralAdminModel();
				rAdm.setReferralId(rset.getString("ID"));
				rAdm.setDescription(rset.getString("DESCRIPTION"));
				String date = format.format(rset.getDate("EXPIRATION_DATE"));
				rAdm.setExpirationDate(date);
				rAdm.setGetText(rset.getString("GET_TEXT"));
				rAdm.setGiveText(rset.getString("GIVE_TEXT"));
				rAdm.setPromotionId(rset.getString("PROMOTION_ID"));
				rAdm.setReferralFee(rset.getString("REFERRAL_FEE"));
				rAdm.setDefaultPromo("Y".equals(rset.getString("DEFAULT_PROMO"))?true:false);
				rAdm.setShareHeader(rset.getString("SHARE_HEADER"));
				rAdm.setShareText(rset.getString("SHARE_TEXT"));
				rAdm.setGiveHeader(rset.getString("GIVE_HEADER"));
				rAdm.setGetHeader(rset.getString("GET_HEADER"));
				rAdm.setNotes(rset.getString("NOTES"));
				rAdm.setFbFile(rset.getString("FB_IMAGE_PATH"));
				rAdm.setFbHeadline(rset.getString("FB_HEADLINE"));
				rAdm.setFbText(rset.getString("FB_TEXT"));
				rAdm.setTwitterText(rset.getString("TWITTER_TEXT"));
				String rpt = rset.getString("RL_PAGE_TEXT");
				rAdm.setReferralPageText(rpt);
				rAdm.setReferralPageLegal(rset.getString("RL_PAGE_LEGAL"));
				rAdm.setInviteEmailSubject(rset.getString("INV_EMAIL_SUBJECT"));
				rAdm.setInviteEmailOfferText(rset.getString("INV_EMAIL_OFFER_TEXT"));
				String iet = rset.getString("INV_EMAIL_TEXT");
				rAdm.setInviteEmailText(iet);
				rAdm.setInviteEmailLegal(rset.getString("INV_EMAIL_LEGAL"));
				rAdm.setReferralCreditEmailSubject(rset.getString("REF_CRE_EMAIL_SUB"));
				String rcet = rset.getString("REF_CRE_EMAIL_TEXT");
				rAdm.setReferralCreditEmailText(rcet);
				rAdm.setSiteAccessImageFile(rset.getString("sa_image_path"));
				return rAdm;
			}
		} catch (Exception e) {
			LOGGER.error("Error getting referral promo for:" + id, e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static final String GET_REF_PROMO_USERS = "select upper(c.user_id) as user_id  from CUST.REFERRAL_CUSTOMER_LIST cl, cust.customer c where cl.REFERAL_PRGM_ID=? and CL.ERP_CUSTOMER_ID = c.id";        

	public String getRefPromoUsers(String id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(GET_REF_PROMO_USERS);
			pstmt.setString(1, id);
			rset = pstmt.executeQuery();
			StringBuffer sb = new StringBuffer();
			while (rset.next()) {
				sb.append(rset.getString("USER_ID"));
				sb.append(",");
			}
			return sb.toString();
		} catch (Exception e) {
			LOGGER.error("Error getting referral promo for:" + id, e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static final String UPDATE_REFPROMO = "update CUST.REFERRAL_PRGM set EXPIRATION_DATE=?, GIVE_TEXT=?, GET_TEXT=?, "
			+ "DESCRIPTION=?, PROMOTION_ID=?, REFERRAL_FEE=?, DEFAULT_PROMO=?, SHARE_HEADER=?, SHARE_TEXT=?, GIVE_HEADER=?, "
			+ "GET_HEADER=?, NOTES=?, FB_IMAGE_PATH=?, FB_HEADLINE=?, FB_TEXT=?, TWITTER_TEXT=?, RL_PAGE_TEXT=?, RL_PAGE_LEGAL=?, " 
			+ "INV_EMAIL_SUBJECT=?, INV_EMAIL_OFFER_TEXT=?, INV_EMAIL_TEXT=?, INV_EMAIL_LEGAL=?, REF_CRE_EMAIL_SUB=?, REF_CRE_EMAIL_TEXT=?, "
			+ "change_by_date = sysdate, change_by_user=?, sa_image_path=? "
			+ "where ID=?";

	public void editRefPromo(ReferralAdminModel rModel) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String rcet = rModel.getReferralCreditEmailText();
		String iet = rModel.getInviteEmailText();
		String ieOfferText = rModel.getInviteEmailOfferText();
		String rpt = rModel.getReferralPageText();
		
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(UPDATE_REFPROMO);
			java.util.Date convertToDate = null;
			try {
				convertToDate = format.parse(rModel.getExpirationDate());
			} catch (ParseException e) {
			}
			pstmt.setDate(1, new java.sql.Date(convertToDate.getTime()));
			pstmt.setString(2, rModel.getGiveText());
			pstmt.setString(3, rModel.getGetText());
			pstmt.setString(4, rModel.getDescription());
			pstmt.setString(5, rModel.getPromotionId());
			pstmt.setInt(6, Integer.parseInt(rModel.getReferralFee()));			
			pstmt.setString(7, rModel.getDefaultPromo()?"Y":"N");
			pstmt.setString(8, rModel.getShareHeader());
			pstmt.setString(9, rModel.getShareText());
			pstmt.setString(10, rModel.getGiveHeader());
			pstmt.setString(11, rModel.getGetHeader());
			pstmt.setString(12, rModel.getNotes());
			pstmt.setString(13, rModel.getFbFile());
			pstmt.setString(14, rModel.getFbHeadline());
			pstmt.setString(15, rModel.getFbText());
			pstmt.setString(16, rModel.getTwitterText());
			pstmt.setString(17, rpt);
			pstmt.setString(18, rModel.getReferralPageLegal());
			pstmt.setString(19, rModel.getInviteEmailSubject());
			pstmt.setString(20, ieOfferText);
			pstmt.setString(21, iet);
			pstmt.setString(22, rModel.getInviteEmailLegal());
			pstmt.setString(23, rModel.getReferralCreditEmailSubject());
			pstmt.setString(24, rcet);
			pstmt.setString(25, rModel.getAddByUser());
			pstmt.setString(26, rModel.getSiteAccessImageFile());
			pstmt.setString(27, rModel.getReferralId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Failed with insert into CUST.REFERRAL_PRGM", e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static final String GET_DEFAULT_PROMO = "select count(*) " +
				"from  CUST.REFERRAL_PRGM rp " +
				"where RP.DEFAULT_PROMO = 'Y' " +
				"and   trunc(RP.EXPIRATION_DATE) > trunc(sysdate) " +
				"and   RP.ID != ? " + 
				"and  (rp.Delete_flag is null or rp.delete_flag != 'Y')";
	
	public static final String GET_DEFAULT_PROMO1 = "select count(*) " +
		"from  CUST.REFERRAL_PRGM rp " +
		"where RP.DEFAULT_PROMO = 'Y' " +
		"and   trunc(RP.EXPIRATION_DATE) > trunc(sysdate)" +
		"and  (rp.Delete_flag is null or rp.delete_flag != 'Y')";
	
	public boolean defaultPromoExists (String referral_id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			if(referral_id != null) {
				pstmt = conn.prepareStatement(GET_DEFAULT_PROMO);
				pstmt.setString(1, referral_id);
			} else {
				pstmt = conn.prepareStatement(GET_DEFAULT_PROMO1);
			}
			rset = pstmt.executeQuery();
			while (rset.next()) {
				int cnt = rset.getInt(1);
				if(cnt > 0)
					return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error getting referral promo for:" + referral_id, e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	public  List<String> getRefPromoUserList(String referral_id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<String> userList = new ArrayList<String>();
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			pstmt = conn.prepareStatement(GET_REF_PROMO_USERS);
			pstmt.setString(1, referral_id);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				userList.add(rset.getString("USER_ID"));
			}
			return userList;
		} catch (Exception e) {
			LOGGER.error("Error getting referral promo for:" + referral_id, e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

}
