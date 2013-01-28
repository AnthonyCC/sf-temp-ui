package com.freshdirect.transadmin.dao.oracle;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.transadmin.dao.IDashboardDataDAO;
import com.freshdirect.transadmin.model.PlantCapacity;

public class DashboardDataDAOImpl implements IDashboardDataDAO   {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String GET_PLANT_CAPACITY = "SELECT * FROM TRANSP.PLANT_CAPACITY WHERE DAY_OF_WEEK = ?";

	private static final String DELETE_PLANT_CAPACITIY = "DELETE FROM TRANSP.PLANT_CAPACITY WHERE DAY_OF_WEEK = ?";

	private static final String SAVE_PLANT_CAPACITIY = "INSERT INTO TRANSP.PLANT_CAPACITY(DISPATCH_TIME, CAPACITY, DAY_OF_WEEK) VALUES (TRANSP.PLANTCAPACITYSEQ.nextval,?,?,?)";

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public Collection getPlantCapacity(final String dayOfWeek) throws SQLException {
		
		final List<PlantCapacity> result = new ArrayList<PlantCapacity>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_PLANT_CAPACITY);
					ps.setString(1, dayOfWeek);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					PlantCapacity capacity = new PlantCapacity();
					
					capacity.setDayOfWeek(rs.getString("DAY_OF_WEEK"));
					capacity.setDispatchTime(new RoutingTimeOfDay(rs.getTimestamp("DISPATCH_TIME")));
					capacity.setCapacity(rs.getInt("CAPACITY"));
					result.add(capacity);
				}				
			});
			
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}


	public void purgePlantCapacity(final String dayOfWeek) throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(DELETE_PLANT_CAPACITIY, new Object[] {dayOfWeek});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void savePlantCapacity(List<PlantCapacity> capacities) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate sqlUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), SAVE_PLANT_CAPACITIY);
			sqlUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			sqlUpdater.declareParameter(new SqlParameter(Types.INTEGER));
			sqlUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			sqlUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(PlantCapacity _capacity : capacities) {
				
				sqlUpdater.update(new Object[] { 
						_capacity.getDispatchTime().getAsDate()
						, _capacity.getCapacity()											
						, _capacity.getDayOfWeek()
							});
			}			
			sqlUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	

}
