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
import com.freshdirect.transadmin.model.OrderRateException;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;

public class DashboardDataDAOImpl implements IDashboardDataDAO   {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final String GET_PLANT_CAPACITY = "SELECT * FROM TRANSP.PLANT_CAPACITY WHERE DAY_OF_WEEK = ? ORDER BY DISPATCH_TIME ASC";

	private static final String DELETE_PLANT_CAPACITIY = "DELETE FROM TRANSP.PLANT_CAPACITY WHERE DAY_OF_WEEK = ?";

	private static final String SAVE_PLANT_CAPACITIY = "INSERT INTO TRANSP.PLANT_CAPACITY(DISPATCH_TIME, CAPACITY, DAY_OF_WEEK) VALUES (?,?,?)";

	private static final String GET_PLANT_DISPATCH = "SELECT * FROM TRANSP.DISPATCH_MAPPING ORDER BY PLANT_DISPATCH ASC";

	private static final String DELETE_PLANT_DISPATCH = "DELETE FROM TRANSP.DISPATCH_MAPPING";

	private static final String SAVE_PLANT_DISPATCH = "INSERT INTO TRANSP.DISPATCH_MAPPING(DISPATCH_TIME, PLANT_DISPATCH) VALUES (?,?)";

	private static final String GET_ORDER_RATE_EXCEPTIONS = "SELECT * FROM MIS.ORDER_RATE_EXCEPTIONS ORDER BY DELIVERY_DATE ASC";

	private static final String DELETE_ORDER_RATE_EXCEPTIONS = "DELETE FROM MIS.ORDER_RATE_EXCEPTIONS";

	private static final String SAVE_ORDER_RATE_EXCEPTIONS = "INSERT INTO MIS.ORDER_RATE_EXCEPTIONS(DELIVERY_DATE) VALUES (?)";

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
	
	public void savePlantCapacity(final String dayOfWeek, List<PlantCapacity> capacities) throws SQLException {
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
						, dayOfWeek
							});
			}			
			sqlUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	@Override
	public Collection getPlantDispatch() throws SQLException {
		
		final List<PlantDispatch> result = new ArrayList<PlantDispatch>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_PLANT_DISPATCH);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					PlantDispatch dispatch = new PlantDispatch();
					dispatch.setDispatchTime(new RoutingTimeOfDay(rs.getTimestamp("DISPATCH_TIME")));
					dispatch.setPlantDispatch(new RoutingTimeOfDay(rs.getTimestamp("PLANT_DISPATCH")));
					result.add(dispatch);
				}				
			});
			
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}

	@Override
	public void purgePlantDispatch() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(DELETE_PLANT_DISPATCH, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	@Override
	public void savePlantCapacity(List<PlantDispatch> dispatch)
			throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate sqlUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), SAVE_PLANT_DISPATCH);
			sqlUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			sqlUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			sqlUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(PlantDispatch _dispatch : dispatch) {
				
				sqlUpdater.update(new Object[] { 
						_dispatch.getDispatchTime().getAsDate()
						, _dispatch.getPlantDispatch().getAsDate()											
							});
			}			
			sqlUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	@Override
	public Collection getOrderRateExceptions() throws SQLException {
		
		final List<OrderRateException> result = new ArrayList<OrderRateException>();
			
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					
					PreparedStatement ps = connection.prepareStatement(GET_ORDER_RATE_EXCEPTIONS);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					OrderRateException _exception = new OrderRateException();
					_exception.setExceptionDate(rs.getDate("DELIVERY_DATE"));
					result.add(_exception);
				}				
			});
			
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}

	@Override
	public void purgeOrderRateExceptions() throws SQLException {
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(DELETE_ORDER_RATE_EXCEPTIONS, new Object[] {});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	@Override
	public void saveOrderRateExceptions(List<OrderRateException> exceptions)
			throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate sqlUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), SAVE_ORDER_RATE_EXCEPTIONS);
			sqlUpdater.declareParameter(new SqlParameter(Types.DATE));
			
			sqlUpdater.compile();
					
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(OrderRateException _exception : exceptions) {
				
				sqlUpdater.update(new Object[] { 
						_exception.getExceptionDate()});
			}			
			sqlUpdater.flush();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	

}
