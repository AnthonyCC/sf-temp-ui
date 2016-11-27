package com.freshdirect.dashboard.dao.impl;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseDAO extends JdbcDaoSupport {
	
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	void init(){
		setDataSource(dataSource);
	}
}
