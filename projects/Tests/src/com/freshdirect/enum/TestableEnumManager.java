package com.freshdirect.enum;

import java.sql.Connection;
import java.util.List;

public class TestableEnumManager extends EnumManager {

	private final Connection conn;

	public TestableEnumManager(Connection conn) {
		super(null);
		this.conn = conn;
	}

	public List loadEnums(Class daoClass) {
		try {
			EnumDAOI dao = (EnumDAOI) daoClass.newInstance();
			return dao.loadAll(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
