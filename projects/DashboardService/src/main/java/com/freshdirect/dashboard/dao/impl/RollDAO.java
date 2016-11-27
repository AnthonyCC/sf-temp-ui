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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.freshdirect.dashboard.dao.IRollDAO;
import com.freshdirect.dashboard.model.RollData;
import com.freshdirect.dashboard.util.DateUtil;

@Repository
public class RollDAO implements IRollDAO {
	
	private static final String ROLL_SELECT = "SELECT AVG (unavailable_pct) * COUNT (DISTINCT (customer_id)) / 100 cnt, "+
			 " createdate, zone, cutoff, "+
			 " SUM (AVG (unavailable_pct) * COUNT (DISTINCT (customer_id)) / 100) OVER (PARTITION BY cutoff ORDER BY createdate, zone, cutoff) AS total_cnt "+
			 " FROM mis.roll_event "+
			 " WHERE TO_CHAR (delivery_date, 'mm/dd/yyyy') = ? "+
			 " AND zone = ? "+
			 " AND unavailable_pct > 0 "+
			 " GROUP BY zone, cutoff, createdate "+
			 " ORDER BY zone, cutoff, createdate ASC";

	private static final String ROLL_SELECT_EX = "SELECT AVG (unavailable_pct) * COUNT (DISTINCT (customer_id)) / 100 cnt, "+
			 " createdate, "+
			 " SUM (AVG (unavailable_pct) * COUNT (DISTINCT (customer_id)) / 100) OVER (ORDER BY createdate) AS total_cnt "+
			 " FROM mis.roll_event "+
			 " WHERE TO_CHAR (delivery_date, 'mm/dd/yyyy') =  ? AND unavailable_pct > 0 "+
			 " GROUP BY createdate "+
			 " ORDER BY createdate ASC";

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public List<RollData> getRollByZone(final String deliveryDate, final String zone) {
		final Calendar cal = Calendar.getInstance();

		final List<RollData> dataList = new ArrayList<RollData>();

		if (zone != null && !"".equals(zone)) {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(ROLL_SELECT);
					ps.setString(1, deliveryDate);
					ps.setString(2, zone);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {

					do {
						RollData data = new RollData();
						data.setCnt(rs.getFloat("total_cnt"));
						data.setCutOff(DateUtil.getNormalDate(new Date(rs.getTimestamp("cutoff").getTime())));
						data.setZone(rs.getString("zone"));
						cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
						cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE) % 30);
						data.setSnapshotTime(cal.getTime());

						dataList.add(data);

					} while (rs.next());
				}
			});
		} else {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection
							.prepareStatement(ROLL_SELECT_EX);
					ps.setString(1, deliveryDate);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {

					do {
						RollData data = new RollData();
						data.setCnt(rs.getFloat("total_cnt"));
						cal.setTime(new Date(rs.getTimestamp("createdate").getTime()));
						cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE) % 30);
						data.setSnapshotTime(cal.getTime());

						dataList.add(data);

					} while (rs.next());

				}
			});
		}
		return dataList;
	}
}

