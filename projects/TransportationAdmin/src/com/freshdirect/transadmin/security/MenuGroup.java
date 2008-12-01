package com.freshdirect.transadmin.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuGroup implements Serializable  {
	
	private Set rootMenus;
	private Collection subMenus;
	private Menu currentSubMenu;
	private Menu currentRootMenu;
	
	public MenuGroup(Set rootMenus, Collection subMenus, Menu currentRootMenu, Menu currentSubMenu) {
		super();
		this.rootMenus = rootMenus;
		this.subMenus = subMenus;
		this.currentSubMenu = currentSubMenu;
		this.currentRootMenu = currentRootMenu;
	}

	public Menu getCurrentRootMenu() {
		return currentRootMenu;
	}

	public void setCurrentRootMenu(Menu currentRootMenu) {
		this.currentRootMenu = currentRootMenu;
	}

	public Menu getCurrentSubMenu() {
		return currentSubMenu;
	}

	public void setCurrentSubMenu(Menu currentSubMenu) {
		this.currentSubMenu = currentSubMenu;
	}

	
	public Set getRootMenus() {
		return rootMenus;
	}

	public void setRootMenus(Set rootMenus) {
		this.rootMenus = rootMenus;
	}

	public Collection getSubMenus() {
		return subMenus;
	}
	
	public Map groupSubMenus() {
		Map groupMap = new HashMap();
		groupMap.put("LEFT", new ArrayList());
		groupMap.put("RIGHT", new ArrayList());
		
		Collection subMenus = getSubMenus();
		Iterator itr = subMenus.iterator();
		List tmpList = null;
		while(itr.hasNext()) {
			Menu tmpSubMenu = (Menu)itr.next();
			String orientation = tmpSubMenu.getMenuOrientation();
			
			if(groupMap.containsKey(orientation)) {
				tmpList = (List)groupMap.get(orientation);				
			} else {
				tmpList = new ArrayList();
				groupMap.put(orientation, tmpList);
			}
			tmpList.add(tmpSubMenu);
		}
		return groupMap;
	}

	public void setSubMenus(Collection subMenus) {
		this.subMenus = subMenus;
	}
	
	

	
}
