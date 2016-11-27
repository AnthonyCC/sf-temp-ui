package com.freshdirect.webapp.crm.security;

import java.io.Serializable;
import java.util.Map;

public class MenuAccess implements Serializable  {
	
	Map menuRoleMapping = null;
	Map menuLinkMapping = null;
	Map linkRoleMapping = null;
	Map autoApproveLimitRoleMapping = null;
	Map approveLimitRoleMapping = null;
	
	public Map getMenuLinkMapping() {
		return menuLinkMapping;
	}
	public void setMenuLinkMapping(Map menuLinkMapping) {
		this.menuLinkMapping = menuLinkMapping;
	}
	public Map getMenuRoleMapping() {
		return menuRoleMapping;
	}
	public void setMenuRoleMapping(Map menuRoleMapping) {
		this.menuRoleMapping = menuRoleMapping;
	}
	public String toString() {
		// TODO Auto-generated method stub
		return menuRoleMapping+"->"+menuLinkMapping+"->"+linkRoleMapping+"\n";
	}
	public Map getLinkRoleMapping() {
		return linkRoleMapping;
	}
	public void setLinkRoleMapping(Map linkRoleMapping) {
		this.linkRoleMapping = linkRoleMapping;
	}
	public Map getAutoApproveLimitRoleMapping() {
		return autoApproveLimitRoleMapping;
	}
	public void setAutoApproveLimitRoleMapping(Map autoApproveLimitRoleMapping) {
		this.autoApproveLimitRoleMapping = autoApproveLimitRoleMapping;
	}
	public Map getApproveLimitRoleMapping() {
		return approveLimitRoleMapping;
	}
	public void setApproveLimitRoleMapping(Map approveLimitRoleMapping) {
		this.approveLimitRoleMapping = approveLimitRoleMapping;
	}
	
}
