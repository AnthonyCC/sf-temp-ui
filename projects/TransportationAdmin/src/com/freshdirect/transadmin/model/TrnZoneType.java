package com.freshdirect.transadmin.model;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.web.model.IToolTip;

public class TrnZoneType implements java.io.Serializable, TrnBaseEntityI {
	
	private String zoneTypeId;
	private String name;
	private String description;
	private Set zonetypeResources = new HashSet(0);
	

	public TrnZoneType() {
	}

	public TrnZoneType(String zoneTypeId, String name) {
		this.zoneTypeId = zoneTypeId;
		this.name = name;
	}

	public TrnZoneType(String zoneTypeId, String name, String description,
			Set zonetypeResources) {
		this.zoneTypeId = zoneTypeId;
		this.name = name;
		this.description = description;
		this.zonetypeResources = zonetypeResources;
		
	}

	public String getZoneTypeId() {
		return this.zoneTypeId;
	}

	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getZonetypeResources() {
		return this.zonetypeResources;
	}

	public void setZonetypeResources(Set zonetypeResources) {
		this.zonetypeResources = zonetypeResources;
	}

	

	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}
	public IToolTip getNameEx() {
		return new Tooltip(this.getName(), this.getName());
	}
	
	class Tooltip implements IToolTip, Comparable {
		
		Object value = null;
		String toolTip = null;
		
		Tooltip(Object value, String tooltip) {
			this.value = value;
			this.toolTip = tooltip;
		}

		

		public Object getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}



		public String getToolTip() {
			return toolTip;
		}



		public void setToolTip(String toolTip) {
			this.toolTip = toolTip;
		}
		
		public String toString() {
			return getValue().toString();
		}



		public int compareTo(Object o) {
			if(o instanceof Tooltip) {
				String _val=((Tooltip)o).getValue().toString();
				return ((String)value).compareTo(_val);
			}
			return 0;
			
		}
	}

}
