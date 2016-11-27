package com.freshdirect.athena.config;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Parameter implements Cloneable, Serializable {
	
	@Element(required=false)
	private String name;
	@Element(required=false)
	private String type;
	@Element(required=false, name="default")
	private String value;
	
	public Parameter() {
		super();
	}
	
	public Parameter(String name, String type, String value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public ParameterType getParameterType() {
		return ParameterType.valueOf(type);
	}
	
	public String getParameterValue() {
		return getValue();
	}
	
	public String getParameterName() {
		return getName().toUpperCase();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "[name=" + name + ", type=" + type + ", value="
				+ value + "]";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new Parameter(this.getName(), this.getType(), this.getValue());
	}
	
	
	
	
}
