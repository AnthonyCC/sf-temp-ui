package com.freshdirect.xls.datasource;

import java.sql.*;
import java.util.*;
import com.freshdirect.xls.*;

public class DatabaseDataSourceKeyValue extends DataSource {
  private Connection c = null;

  protected void initConnection() throws SQLException {
    if(c != null) return;

    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    System.out.println("get connection: " + getUrl());
    System.out.println("get user: " + getUser());
    System.out.println("get password: " + getPassword());
    c = DriverManager.getConnection(getUrl(), getUser(), getPassword());
    
  }


  /**
      param[0] = SQL Query - assume key/value columns are the last columns in query
      param[1] = headerKeys (comma-separated, must match key columns)
      param[2] = if printHeader, prints header as first row
      param[3] = if onlyShowListedHeaders, don't print extraneous keys

      0-1 required
      2-3   not required

      Note, this can be used for survey data where we might get this result set:
      SURVEY CREATED_DATE KEY         VALUE
      1234   1/1/2004     Question1   Yes
      1234   1/1/2004     Question2   No
      Then we need to transform to:
      SURVEY CREATED_DATE Question1   Question2
      1234   1/1/2004     Yes         No
       
   **/
  public List getMatrixData(String[] params) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      initConnection();
      ps = c.prepareStatement(params[0]);
      rs = ps.executeQuery();

      ResultSetMetaData md = rs.getMetaData();
      int cols = md.getColumnCount() - 2;

      ArrayList a = new ArrayList();

      LinkedHashMap headerMap = new LinkedHashMap();

      StringTokenizer strTok = new StringTokenizer(params[1], ",");
      int counter = 0;
      while(strTok.hasMoreTokens()) {
         String token = strTok.nextToken();
         headerMap.put(token, new Integer(counter++));
         System.out.println("Header: " + token);
      }

      List header = new ArrayList();
      for(int i = 0; i < cols; i++)
        header.add(md.getColumnLabel(i+1));

      for(Iterator i = headerMap.keySet().iterator(); i.hasNext(); ) {
     	  String col = (String) i.next();
     	  header.add(col);
      }
      
      if(params.length > 2) {
        if(params[2].equals("printHeader")) {
           a.add(header);
        }
      }

      boolean onlyShowListed = false;
      if(params.length > 3) {
        if(params[3].equals("onlyShowListedHeaders"))
           onlyShowListed = true;
      }

      List oldRecord = header;
      while (rs.next()) {
        List record = new ArrayList();
        
        /* Add fixed columns first */
        boolean same = true;
        for(int i = 0; i < cols; i++) {
        	String val = rs.getString(i+1);
        	String oldVal = (String) oldRecord.get(i);
        	if(!val.equals(oldVal)) {
        		same = false;
        	}
        	record.add(val);
        }
        
        if(same) {
        	record = oldRecord;
        } else {
            a.add(record);
            oldRecord = record; 
            /* prefill new record */
            for(Iterator i = headerMap.keySet().iterator(); i.hasNext(); ) {
                  i.next();
            	record.add("");
            }
        }

        String key = rs.getString(cols + 1);
        String val = rs.getString(cols + 2);
        Integer position = (Integer) headerMap.get(key);
        if(position == null) {
          if(!onlyShowListed) {
        	position = new Integer(headerMap.size());
        	headerMap.put(key, position);
            header.add(key);
            record.add("");
          }
        }

        if(position != null) {
        	String complete = (String) record.get(position.intValue() + cols);
        	if( (complete == null) || (complete.equals("")) ) {
        		complete = val;
        	} else {
        		complete = val + ", " + complete;
        	}
           record.set(position.intValue() + cols, complete);
        }
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