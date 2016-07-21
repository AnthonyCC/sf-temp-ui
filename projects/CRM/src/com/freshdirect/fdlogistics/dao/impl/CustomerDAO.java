package com.freshdirect.fdlogistics.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.freshdirect.fdlogistics.dao.ICustomerDAO;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Addresses;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.Customers;

@Component
public class CustomerDAO implements ICustomerDAO {

	private static String GET_CUSTOMERS_BYID =   "SELECT C.ID, FDC.ID FDC_ID, CI.FIRST_NAME, CI.LAST_NAME, C.USER_ID, CI.HOME_PHONE, CI.BUSINESS_PHONE, "
            + "CI.CELL_PHONE, FDE.MOBILE_NUMBER, CI.BUSINESS_EXT, "
            + "CASE WHEN EXISTS (SELECT 1 FROM CUST.PROFILE P WHERE P.CUSTOMER_ID=FDC.ID  AND P.PROFILE_NAME='ChefsTable' AND P.PROFILE_VALUE='1') " 
            + "THEN '1' ELSE '0' END AS CHEFSTABLE " 
            + "FROM CUST.CUSTOMER C, CUST.FDCUSTOMER FDC, CUST.FDCUSTOMER_ESTORE FDE " 
            + ", CUST.CUSTOMERINFO CI " 
            + "WHERE C.ID = CI.CUSTOMER_ID AND C.ID = FDC.ERP_CUSTOMER_ID AND C.ID IN (:ids) AND FDE.FDCUSTOMER_ID = FDC.ID AND FDE.E_STORE = 'FreshDirect'";
	     
	protected NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired		
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
	
	@Override
	public Customers getCustomerDetails(List<String> ids) throws SQLException {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		List<Customer> customers =  jdbcTemplate.query(GET_CUSTOMERS_BYID, parameters, 
				new RowMapper<Customer>(){
	                    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
	                    	Customer customer = new Customer(rs.getString("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("user_id"), 
	                 	    		rs.getString("home_phone"), rs.getString("business_phone"), rs.getString("cell_phone"), rs.getString("mobile_number"), rs.getString("business_ext"),
	                 	    		null, null, "1".equals(rs.getString("CHEFSTABLE")));
							return customer;
	                    }
	            });
		
		Customers result = new Customers();
		result.setCustomers(customers);
		return result;
	}
	
	private static String GET_ADDRESSES_BYID =   "SELECT * FROM CUST.ADDRESS A WHERE  A.ID IN (:ids)";
	
	@Override
	public Addresses getAddressDetails(List<String> ids) throws SQLException {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		List<Address> addresses =  jdbcTemplate.query(GET_ADDRESSES_BYID, parameters, 
				new RowMapper<Address>(){
	                    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
	                    	Address address = new Address(rs.getString("ID"), rs.getString("ADDRESS1"), rs.getString("ADDRESS2"), rs.getString("APARTMENT"), 
	                 	    		rs.getString("CITY"), rs.getString("STATE"), rs.getString("ZIP"), rs.getString("COUNTRY"), rs.getString("FIRST_NAME"), 
	                 	    		rs.getString("LAST_NAME"), rs.getString("SCRUBBED_ADDRESS"),rs.getDouble("LONGITUDE"),rs.getDouble("LATITUDE"),rs.getString("SERVICE_TYPE"),
	                 	    		rs.getString("COMPANY_NAME"));
							return address;
	                    }
	            });
		
		Addresses result = new Addresses();
		result.setAddresses(addresses);
		return result;
	}
	

	private static String GET_CUSTOMERS_BYBUILDINGLOCATION = "SELECT A.CUSTOMER_ID ID, A.FIRST_NAME, A.LAST_NAME FROM CUST.ADDRESS A WHERE A.SCRUBBED_ADDRESS = :SCRUBBED_ADDRESS"; 

	@Override
	public Customers getCustomerAddressesByBuildingLocation(
			String scrubbedAddress, String apartment, String zipcode)
			throws SQLException {
		StringBuilder sb = new StringBuilder(GET_CUSTOMERS_BYBUILDINGLOCATION);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("SCRUBBED_ADDRESS", scrubbedAddress);
		
		if(apartment != null){
			 sb.append(" AND UPPER(A.APARTMENT) = UPPER(:APARTMENT)");
			 parameters.addValue("APARTMENT", apartment);
				
		}
		List<Customer> customers =  jdbcTemplate.query(sb.toString(), parameters,  
				 new RowMapper<Customer>(){
	                    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
	                    	Customer customer = new Customer();
	                    	customer.setCustomerId(rs.getString("id"));
	                    	customer.setFirstName(rs.getString("first_name"));
	                    	customer.setLastName(rs.getString("last_name"));
							return customer;
	                    }
	                    });
		
		Customers result = new Customers();
		result.setCustomers(customers);
		return result;
		
	}
	
	
	
}
