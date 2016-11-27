package com.freshdirect.athena.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.Parameter;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Row;
import com.freshdirect.athena.data.Variable;
import com.freshdirect.athena.util.TypeUtil;
import com.freshdirect.athena.util.XmlTagUtil;

public class DBCall implements ICall  {
	
	private static final Logger LOGGER = Logger.getLogger(JCOCall.class);
	
	private Connection dbConnection;
	
	public DBCall(Connection dbConnection) {
		super();
		this.dbConnection = dbConnection;
	}

	@Override
	public Data getData(Api api, List<Parameter> params) {
		
		LOGGER.debug("DBCall,getData() =>"+api.getName());
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Data result = new Data();
		Variable variable = new Variable(api.getEndpoint());
		result.addVariable(variable);
		
		try {
			
			ps = dbConnection.prepareStatement(api.getCall() );
			setParameters(ps, params);
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			
			List<Row> rows = new ArrayList<Row>();
			variable.setRow(rows);
			
			while (rs.next()) {
				Row record = new Row();
				rows.add(record);
				for (int i = 0; i < md.getColumnCount(); i++) {
					switch (md.getColumnType(i + 1)) {
					case java.sql.Types.INTEGER:
					case java.sql.Types.DOUBLE:
					case java.sql.Types.SMALLINT:
					case java.sql.Types.FLOAT:
					case java.sql.Types.TINYINT:
					case java.sql.Types.NUMERIC:
					case java.sql.Types.REAL:
						record.addColumn(new Double(rs.getDouble(i + 1)));
						break;
					case java.sql.Types.TIMESTAMP:
						record.addColumn(rs.getTimestamp(i + 1));
						break;
					default:
						record.addColumn(rs.getString(i + 1));
						break;
					}
				}
			}
			
			result = XmlTagUtil.addLastRefresh(result);

		} catch (Exception ex) {			
			ex.printStackTrace();
		} finally {
			try {				
				if(rs != null) {
					rs.close();
				}
				if(dbConnection != null) {
					dbConnection.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	private void setParameters(PreparedStatement ps, List<Parameter> params) throws SQLException {
		if(params != null) {
			int parameterIndex = 0;
			for(Parameter param : params) {
				switch (param.getParameterType()) {
				case INTEGER:
					ps.setInt(++parameterIndex, TypeUtil.getInt(param.getParameterValue()));
					break;
				case DOUBLE:
					ps.setDouble(++parameterIndex, TypeUtil.getInt(param.getParameterValue()));
					break;
				case FLOAT:
					ps.setFloat(++parameterIndex, TypeUtil.getFloat(param.getParameterValue()));
					break;
				case TIMESTAMP: 
					ps.setTimestamp(++parameterIndex, new java.sql.Timestamp(
														TypeUtil.getTimestampMilliseconds(param.getParameterValue())));
					break;
				case DATE:
					ps.setDate(++parameterIndex, new java.sql.Date(TypeUtil.getDateMilliseconds(param.getParameterValue())));
					break;
				case STRING:
					ps.setString(++parameterIndex, param.getParameterValue());
					break;
				default:
					ps.setString(++parameterIndex, param.getParameterValue());
					break;
				}
			}
		}
	}

}
