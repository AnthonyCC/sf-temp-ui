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

import com.freshdirect.analytics.dao.IBounceDAO;
import com.freshdirect.analytics.model.BounceData;

public class BounceDAOImpl implements IBounceDAO  {

		
	private static final String BOUNCE_SELECT = "select count(distinct(customer_id)) cnt, createdate, zone, cutoff from mis.bounce_event where status = 'NEW' and " +
			"type in ('DELIVERYINFO', 'CHECKOUT','RESERVED_SLOT') and to_char(delivery_date, 'mm/dd/yyyy') = ? and zone=? group by  zone, cutoff,createdate " +
			"order by  zone, cutoff,createdate  asc";
	
	private static final String BOUNCE_SELECT_BYZONE = "select count(distinct(customer_id)) cnt, zone, cutoff,sector from mis.bounce_event where status = 'NEW' and " +
			"type in ('DELIVERYINFO', 'CHECKOUT','RESERVED_SLOT') and to_char(delivery_date, 'mm/dd/yyyy') = ? group by  zone, cutoff,sector " +
			"order by zone,cutoff,sector  asc";
	
	private static final String BOUNCE_SELECT_EX = "select count(distinct(customer_id)) cnt, createdate from mis.bounce_event where status = 'NEW' and " +
			"type in ('DELIVERYINFO', 'CHECKOUT','RESERVED_SLOT') and to_char(delivery_date, 'mm/dd/yyyy') = ?  group by createdate " +
			"order by  createdate  asc";
            
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	
	public List<BounceData> getData(final String deliveryDate,final String zone) 
	{
		final DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		final List<BounceData> dataList = new ArrayList<BounceData>();
		final Calendar cal = Calendar.getInstance();

		if(!"0".equals(zone))
		{
			 PreparedStatementCreator creator=new PreparedStatementCreator() {
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement ps =
		                    connection.prepareStatement(BOUNCE_SELECT);
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
		    	
					    		BounceData data = new BounceData();
					    		data.setCnt(rs.getInt("cnt"));
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
		                    connection.prepareStatement(BOUNCE_SELECT_EX);
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
		    	
					    		BounceData data = new BounceData();
					    		data.setCnt(rs.getInt("cnt"));
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
	
	public List<BounceData> getDataByZone(final String deliveryDate) 
	{
		final List<BounceData> dataList = new ArrayList<BounceData>();
		final DateFormat df = new SimpleDateFormat("hh:mm a");
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(BOUNCE_SELECT_BYZONE);
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
		    	
		       		    		BounceData data = new BounceData();
		    		    		data.setCnt(rs.getInt("cnt"));
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

