package com.freshdirect.framework.core;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.ObjectNotFoundException;


public interface EntityDAOI {

	public PrimaryKey findByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException, ObjectNotFoundException;

	public void create(Connection conn, PrimaryKey pk, ModelI model) throws SQLException;

	public ModelI load(Connection conn, PrimaryKey pk) throws SQLException;

	public void store(Connection conn, ModelI model) throws SQLException;

	public void remove(Connection conn, PrimaryKey pk) throws SQLException;

}