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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.freshdirect.dashboard.dao.IPlantDispatchDAO;
import com.freshdirect.dashboard.model.PlantDispatchData;
import com.freshdirect.dashboard.util.DateUtil;

@Repository
public class PlantDispatchDAO implements IPlantDispatchDAO  {

	private static final String PLANTDISPATCH_VOLUME = "SELECT * FROM MIS.DISPATCH_VOLUME DV WHERE DISPATCH_DATE = ? AND SNAPSHOT_TIME = "
			+ "(SELECT MAX(SNAPSHOT_TIME) FROM MIS.DISPATCH_VOLUME WHERE DISPATCH_DATE = DV.DISPATCH_DATE ) ORDER BY DV.DISPATCH_TIME ASC";

	private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public Map<String, List<PlantDispatchData>> getData(final Date deliveryDate) {
		
		final Map<String, List<PlantDispatchData>> dispatchMap = new TreeMap<String, List<PlantDispatchData>>();
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                    connection.prepareStatement(PLANTDISPATCH_VOLUME);
	                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
	                return ps;
	            }
	        };
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {

				do {
					PlantDispatchData data = new PlantDispatchData();
					data.setDispatchTime(rs.getTimestamp("dispatch_time"));
					data.setPlantCapacity(rs.getInt("plant_capacity"));
					data.setPlannedCapacity(rs.getInt("planned_capacity"));
					data.setTrucks(rs.getInt("trucks"));
					data.setOrders(rs.getInt("orders"));
					data.setAllocatedOrders(rs.getInt("allocated_orders"));
					data.setShift(rs.getString("shift"));
					String shift = null;
					if(data.getShift() == null) {
						try {
							shift = DateUtil.getShift(data.getDispatchTime());
							data.setShift(shift);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(!dispatchMap.containsKey(data.getShift())) {
						dispatchMap.put(data.getShift(), new ArrayList<PlantDispatchData>());
					}
					dispatchMap.get(data.getShift()).add(data);
				} while (rs.next());

			}
		});
		    	
		 return dispatchMap;		
	}

}

