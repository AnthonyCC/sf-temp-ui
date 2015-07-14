package com.freshdirect.fdlogistics.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDAO {
	
	
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired		
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public boolean getBoolean(String value) {
		return (value != null && "1".equalsIgnoreCase(value));
	}

}
