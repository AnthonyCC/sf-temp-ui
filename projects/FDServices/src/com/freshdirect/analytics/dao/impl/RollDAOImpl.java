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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.analytics.dao.IRollDAO;
import com.freshdirect.analytics.model.RollData;


public class RollDAOImpl implements IRollDAO {

		
	private static final String ROLL_SELECT="select avg(unavailable_pct)* count(distinct(customer_id))/100 cnt, createdate,  zone, cutoff from mis.roll_event " +
			"where to_char(delivery_date, 'mm/dd/yyyy') = ? and zone=? and unavailable_pct >0 group by zone, cutoff,  createdate " +
			"order by zone, cutoff, createdate asc";
	
	private static final String ROLL_SELECT_EX="select avg(unavailable_pct)* count(distinct(customer_id))/100 cnt, createdate from mis.roll_event " +
			"where to_char(delivery_date, 'mm/dd/yyyy') = ? and unavailable_pct >0 group by createdate " +
			"order by  createdate asc";
	
	private static final String ROLL_SELECT_BYZONE =" select avg(unavailable_pct)* count(distinct(customer_id))/100 cnt, zone, cutoff, sector from mis.roll_event " +
			"where to_char(delivery_date, 'mm/dd/yyyy') = ? and unavailable_pct >0 group by zone, cutoff, sector" +
			" order by  zone,cutoff, sector";

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public List<RollData> getData(final String deliveryDate, final String zone) 
	{
		final Calendar cal = Calendar.getInstance();
		
		final DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		
		final List<RollData> dataList = new ArrayList<RollData>();
		
		if(!"0".equals(zone))
		{
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(ROLL_SELECT);
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
		    		    		RollData data = new RollData();
		    		    		data.setCnt(rs.getFloat("cnt"));
		    		    		data.setCutOff(new Date(rs.getTimestamp("cutoff").getTime()));
		    		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    		    		data.setZone(rs.getString("zone"));
		    		    		cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
		    		    		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)%30);
		    		    		data.setSnapshotTime(cal.getTime());
		    		    		data.setSnapshotTimeFormatted(sdf.format(cal.getTime()));
		    		    		
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
		                    connection.prepareStatement(ROLL_SELECT_EX);
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
			    		    		RollData data = new RollData();
			    		    		data.setCnt(rs.getFloat("cnt"));
			    		    		cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
			    		    		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)%30);
			    		    		data.setSnapshotTime(cal.getTime());
			    		    		data.setSnapshotTimeFormatted(sdf.format(cal.getTime()));
			    		    		
			    		    		dataList.add(data);
			    		    		
			    		    	}
			       		    	   while(rs.next());
			    		
			       		      }
			        		}
			 );	
		}
		 return dataList;
		
	}
	
	
	public List<RollData> getDataByZone(final String deliveryDate) 
	{
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		final List<RollData> dataList = new ArrayList<RollData>();
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(ROLL_SELECT_BYZONE);
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
		    		    		RollData data = new RollData();
		    		    		data.setCnt(rs.getFloat("cnt"));
		    		    		data.setCutOff(new Date(rs.getTimestamp("cutoff").getTime()));
		    		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    		    		data.setZone(rs.getString("zone"));
		    		    		data.setSector(rs.getString("sector"));
		    		    		dataList.add(data);
		    		    		
		    		    	}
		       		    	   while(rs.next());
		    		
		       		      }
		        		}
		 );		
		    	
		 return dataList;
		
	}
	
}

