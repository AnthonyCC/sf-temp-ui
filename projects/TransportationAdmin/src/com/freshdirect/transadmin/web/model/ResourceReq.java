package com.freshdirect.transadmin.web.model;

import com.freshdirect.transadmin.util.EnumResourceType;

public class ResourceReq extends BaseCommand {
	
	private Integer max;
	private Integer req; 
	private EnumResourceType role;
	
	/**
	 * @return the max
	 */
	public Integer getMax() {
		if(max!=null)
			return max;
		return new Integer(0);
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(Integer max) {
		this.max = max;
	}
	/**
	 * @return the req
	 */
	public Integer getReq() {
		if(req!=null)
			return req;
		return new Integer(0);
	}
	/**
	 * @param req the req to set
	 */
	public void setReq(Integer req) {
		this.req = req;
	}
	/**
	 * @return the roleID
	 */
	public EnumResourceType getRole() {
		return role;
	}
	/**
	 * @param roleID the roleID to set
	 */
	public void setRole(EnumResourceType role) {
		this.role = role;
	}
	
}
