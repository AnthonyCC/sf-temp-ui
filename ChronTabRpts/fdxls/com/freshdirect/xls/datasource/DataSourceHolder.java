package com.freshdirect.xls.datasource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;


import java.util.*;

public class DataSourceHolder {
  private static HashMap dataSources = new HashMap();

  private static final String TYPE = "type";
  private static final String NAME = "name";
  private static final String URL = "url";
  private static final String USER = "user";
  private static final String PASSWORD = "password";

  public static void init(List domNodes) throws Exception{
    for (Iterator iter = domNodes.iterator(); iter.hasNext(); ) {
      Node node = (Node) iter.next();
      String type = getChildNodeString(node, TYPE);
      Class c = Class.forName(type);
      DataSource d = (DataSource) c.newInstance();
      d.setType(type);
      d.setName(getChildNodeString(node, NAME));
      d.setUrl(getChildNodeString(node, URL));
      d.setUser(getChildNodeString(node, USER));
      d.setPassword(getChildNodeString(node, PASSWORD));

      dataSources.put(d.getName(), d);
    }
  }

  public static DataSource getDataSource(String name) {
    return (DataSource) dataSources.get(name);
  }

  public static String getChildNodeString(Node node, String child) {
    Node kid = node.selectSingleNode(child);
    String ret;
    if(kid == null) {
      return "";
    } else {
      ret = kid.getText();
      if(ret != null)
        ret = ret.trim();
    }
    return ret;
  }

}