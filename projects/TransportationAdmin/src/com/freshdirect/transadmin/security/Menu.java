package com.freshdirect.transadmin.security;

import java.io.Serializable;


public class Menu implements Serializable, Cloneable, Comparable {
	
	private String menuId;
	private String menuLink;
	private String menuTitle;
	private String menuOrientation;
	
	Menu(String menuId, String menuTitle, String menuLink, String menuOrientation) {
		this.menuId = menuId;
		this.menuLink = menuLink;
		this.menuTitle = menuTitle;
		this.menuOrientation = menuOrientation;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuLink() {
		return menuLink;
	}
	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Menu) {
			return this.getMenuId().equalsIgnoreCase(((Menu)o).getMenuId());
		} else {
			return false;
		}
	}
	
	public String getMenuTitle() {
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}
	public String getMenuOrientation() {
		return menuOrientation;
	}
	public void setMenuOrientation(String menuOrientation) {
		this.menuOrientation = menuOrientation;
	}
	public String toString() {
		// TODO Auto-generated method stub
		return menuId+","+menuTitle+","+menuLink+","+menuOrientation+"\n";
	}
	public Object clone() {
		// TODO Auto-generated method stub
		return new Menu(menuId, menuTitle, menuLink, menuOrientation);
	}
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return menuId.compareTo(((Menu)o).getMenuId());
	}
	
}
