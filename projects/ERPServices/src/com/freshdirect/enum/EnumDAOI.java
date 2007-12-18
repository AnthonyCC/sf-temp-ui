package com.freshdirect.enum;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface EnumDAOI extends Serializable {
	
	public List loadAll(Connection conn) throws SQLException;

}
