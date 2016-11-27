package com.freshdirect.xls.datasource;

import java.sql.*;
import java.util.*;
import com.freshdirect.xls.*;

public class DatabaseDataSource extends DataSource {
  private Connection c = null;

  protected void initConnection() throws SQLException {
    if(c != null) return;

    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    c = DriverManager.getConnection(getUrl(), getUser(), getPassword());
    
  }

  /**
      param[0] = SQL Query
      param[1] = if printHeader, prints header as first row
   **/
  public List getMatrixData(String[] params) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      initConnection();
      ps = c.prepareStatement(params[0]);
      rs = ps.executeQuery();

      ResultSetMetaData md = rs.getMetaData();
      int cols = md.getColumnCount();

      ArrayList a = new ArrayList();


      if(params.length > 1) {
        if(params[1].equals("printHeader")) {
           List header = new ArrayList();
           for(int i = 0; i < cols; i++)
             header.add(md.getColumnLabel(i+1));
           a.add(header);
        }
      }

      while (rs.next()) {
        List record = new ArrayList();
        for(int i = 0; i < cols; i++) {
          switch(md.getColumnType(i+1)) {
            case java.sql.Types.INTEGER:
            case java.sql.Types.DOUBLE:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.FLOAT:
            case java.sql.Types.TINYINT:
            case java.sql.Types.NUMERIC:
            case java.sql.Types.REAL:
              record.add(new Double(rs.getDouble(i+1)));
              break;
            case java.sql.Types.TIMESTAMP:
              record.add(rs.getTimestamp(i+1));
              break;
            default:
              record.add(rs.getString(i+1));
              break;
          }
        }
        a.add(record);
      }

      return a;
    } catch(Exception ex) {
      System.out.println("Exception thrown");
      ex.printStackTrace();
    } finally {
      try {
      } catch(Exception ex) {
        
      }
    }
    return super.getMatrixData(params);
  }
}