package com.freshdirect.xls.datasource;

import java.util.*;

public class DataSource {
  private String type;
  private String name;
  private String url;
  private String user;
  private String password;

  public void setType(String type) {
    this.type = type;
  }
  public String getType() {
    return this.type;
  }

  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return this.name;
  }

  public void setUrl(String url) {
    this.url = url;
  }
  public String getUrl() {
    return this.url;
  }

  public void setUser(String user) {
    this.user = user;
  }
  public String getUser() {
    return this.user;
  }


  public void setPassword(String password) {
    this.password = password;
  }
  public String getPassword() {
    return this.password;
  }

  public List getMatrixData(String[] params) {
    List data = new ArrayList();
    List row = new ArrayList();
    row.add("noop");
    data.add(row);
    return data;
  }

  public String toString() {
    String ret = "Type: " + getType() + ";";
    ret += "Name: " + getName() + ";";
    ret += "URL: " + getUrl() + ";";
    ret += "User: " + getUser() + ";";
    ret += "Password: " + getPassword();
    return ret;
  }
}