package com.freshdirect.mktAdmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.constants.EnumCompetitorStatusType;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;
import com.freshdirect.mktAdmin.dao.MarketAdminDAOIntf;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;
import com.freshdirect.mktAdmin.model.RestrictedPromoCustomerModel;

public  class OracleMarketAdminDAOImpl implements MarketAdminDAOIntf {

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
		"select pc.customer_id,pc.promotion_id,pc.usage_cnt,pc.expiration_date,ci.first_name,ci.last_name ,c.user_id from cust.promo_customer pc,cust.customer c,cust.customerinfo ci where c.id=pc.customer_id and ci.customer_id=c.id and pc.promotion_id= ?";
	
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
	

	public void deleteRestrictedCustomers(String promotionId, String customerId) throws SQLException {
		// TODO Auto-generated method stub
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_PROMO_CUSTOMER);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
									
		this.jdbcTemplate.update(DELETE_PROMO_CUSTOMER, 
					new Object[] {customerId,promotionId});				    				   
				
		batchUpdater.flush();
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

	private static final String DELETE_RESTRICTION_BATCH_QRY="delete from cust.promo_customer where customer_id=? and promotion_id=?";
	
	public void deleteRestrictedCustomers(Collection collection) throws SQLException {
		BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),DELETE_RESTRICTION_BATCH_QRY);
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		
		Iterator iterator=collection.iterator();	
		while(iterator.hasNext()){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
			this.jdbcTemplate.update(DELETE_RESTRICTION_BATCH_QRY, 
					new Object[] {model.getCustomerId(),model.getPromotionId()});				    				   
		}
		
		batchUpdater.flush();
		
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
       		    		RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
       		    		model.setCustomerId(rs.getString("id"));
       		    		model.setPromotionId(rs.getString("promotion_id"));
       		    		//model.setUsageCount(rs.getInt("usage_cnt"));
       		    		model.setCustEmailAddress(rs.getString("user_id"));       		
       		    		list.add(model);
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
			if(!list.contains(model)){
				LOGGER.debug("invalid model :"+model.getCustomerId());						
				invalidCustomerList.add(model);
			}					
		}		
		
        LOGGER.debug("OracleMarketAdminDAOImpl : getRestrictedCustomers end1  "+invalidCustomerList.size());
        return invalidCustomerList;        
	}	
			
}
