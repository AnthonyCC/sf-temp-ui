package com.freshdirect.analytics.dao.impl;

/**
 * 
 * @author tbalumuri
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.analytics.dao.IPlantDispatchDAO;
import com.freshdirect.analytics.model.PlantDispatchData;

public class PlantDispatchDAOImpl implements IPlantDispatchDAO  {

	private static final String PLANTDISPATCH_VOLUME = "SELECT * FROM MIS.DISPATCH_VOLUME  DV WHERE DISPATCH_DATE = ? AND SNAPSHOT_TIME = " +
			"(SELECT MAX(SNAPSHOT_TIME) FROM MIS.DISPATCH_VOLUME WHERE DISPATCH_DATE = DV.DISPATCH_DATE ) ORDER BY DV.DISPATCH_TIME ASC";
       
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public List<PlantDispatchData> getData(final Date deliveryDate) 
	{
		final List<PlantDispatchData> dataList = new ArrayList<PlantDispatchData>();
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(PLANTDISPATCH_VOLUME);
	                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
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
		    	
		       		    		PlantDispatchData data = new PlantDispatchData();
		    		    		data.setDispatchTime(df.format(new Date(rs.getTimestamp("dispatch_time").getTime())));
		    		    		data.setPlantCapacity(rs.getInt("plant_capacity"));
		    		    		data.setPlannedCapacity(rs.getInt("planned_capacity"));
		    		    		data.setTrucks(rs.getInt("trucks"));
		    		    		data.setOrders(rs.getInt("orders"));
		    		    		dataList.add(data);
		       		    		
		       		    	}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );	
		    	
		 return dataList;
		
	}

}

