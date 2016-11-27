package com.freshdirect.athena.config;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.freshdirect.athena.connection.ConnectionType;

@Root
public class Datasource implements Serializable { 

	@Element
	private String type;
	@Element
	private String name;
	@Element(required=false)
	private String url;
	@Element(required=false)
	private String driver;
	@Element
	private String user;
	@Element
	private String password;
	
	@Element(required=false)
	private String client;
	@Element(required=false)
	private String systemno;
	
	@Element
	private int poolsize;
	

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


	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getSystemno() {
		return systemno;
	}
	public void setSystemno(String systemno) {
		this.systemno = systemno;
	}
	
	public int getPoolsize() {
		return poolsize;
	}
	public void setPoolsize(int poolsize) {
		this.poolsize = poolsize;
	}
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public ConnectionType getConnectionType () {
		return ConnectionType.valueOf(type);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + poolsize;
		result = prime * result
				+ ((systemno == null) ? 0 : systemno.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datasource other = (Datasource) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (driver == null) {
			if (other.driver != null)
				return false;
		} else if (!driver.equals(other.driver))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (poolsize != other.poolsize)
			return false;
		if (systemno == null) {
			if (other.systemno != null)
				return false;
		} else if (!systemno.equals(other.systemno))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "<b>type=</b>" + type + "</br> <b>name=</b>" + name + "</br> <b>url=</b>" + url
				+ "</br> <b>driver=</b>" + driver + "</br> <b>user=</b>" + user + "</br> <b>password=</b>"
				+ password + "</br> <b>client=</b>" + client + "</br> <b>systemno=</b>" + systemno
				+ "</br> <b>poolsize=</b>" + poolsize  ;
	}
	
	
}
