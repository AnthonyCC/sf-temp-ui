package com.freshdirect.routing.dao.impl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDAO {
	
	protected JdbcTemplate jdbcTemplate;
			
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public boolean getBoolean(String value) {
		return (value != null && "1".equalsIgnoreCase(value));
	}
}
