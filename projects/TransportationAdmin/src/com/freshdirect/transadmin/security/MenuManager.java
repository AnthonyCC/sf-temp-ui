package com.freshdirect.transadmin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.security.MenuBuilder.MenuBuilderException;

public class MenuManager {
	
	private MenuAccess menuAccess = null;
		
	private static MenuManager instance = null;
	
	public MenuManager() {
		
		try {
			menuAccess = MenuBuilder.buildMenu("com/freshdirect/transadmin/security/menu.xml");
		} catch(MenuBuilderException exp) {
			exp.printStackTrace();
		}
	}
	
	public static MenuManager getInstance() {
	      if(instance == null) {
	         instance = new MenuManager();
	      }
	      return instance;
	}
	
	public MenuGroup getMenuGroup(ServletRequest request) {
		
		if(menuAccess != null) {
			String userRole = "TrnAdmin";//SecurityManager.getUserRole(request, menuAccess.getMenuRoleMapping().keySet());
			
			HttpServletRequest httpRequest = (HttpServletRequest)request;				
			String currentRootUrl = (String)httpRequest.getAttribute("ROOT_URI");
			
			String currentMenuId = (String)menuAccess.getMenuLinkMapping().get(currentRootUrl);
			
			Map menuList = (Map)menuAccess.getMenuRoleMapping().get(userRole);
						
			if(currentMenuId != null && menuList != null) {
				updateRootMenu(menuList);
				Iterator itr = menuList.keySet().iterator();
				while(itr.hasNext()) {
					Menu rootMenu = (Menu)itr.next();
					Map subMenus = (Map)menuList.get(rootMenu);				
					if(subMenus.containsKey(currentMenuId)) {
						Collection subMenusLst = subMenus.values();						
						if(subMenusLst != null && subMenusLst.size() > 0) {
							rootMenu.setMenuLink(((Menu)subMenusLst.iterator().next()).getMenuLink());
						} else {
							rootMenu.setMenuLink("accessdenied.do");
						}
						return new MenuGroup(new TreeSet(menuList.keySet()), (Collection)subMenus.values()
												, rootMenu, ((Menu)subMenus.get(currentMenuId)));
					}
				}
				return new MenuGroup(new TreeSet(menuList.keySet()), (Collection)new ArrayList(), null, null);
			}			
		}
				
		return new MenuGroup(new HashSet(), (Collection)new ArrayList(), null, null);
    }
	
	public Set getUserRoles() {
		if(menuAccess != null) {
			return menuAccess.getMenuRoleMapping().keySet();
		} else {
			return new HashSet();
		}
	}
	
	public boolean hasAccess(ServletRequest request, String uri) {
		if(menuAccess != null) {
			String userRole = "TrnAdmin";//SecurityManager.getUserRole(request, menuAccess.getMenuRoleMapping().keySet());
			Object tmpMapping = menuAccess.getLinkRoleMapping().get(userRole);
			if(tmpMapping != null) {
				return ((List)tmpMapping).contains(uri);
			}
		}
		return true;
	}
	
	private void updateRootMenu(Map menuList) {
		Iterator itr = menuList.keySet().iterator();
		while(itr.hasNext()) {
			Menu rootMenu = (Menu)itr.next();
			Map subMenus = (Map)menuList.get(rootMenu);
			Collection subMenusLst = subMenus.values();			
			if(subMenusLst != null && subMenusLst.size() > 0) {
				rootMenu.setMenuLink(((Menu)subMenusLst.iterator().next()).getMenuLink());
			} else {
				rootMenu.setMenuLink("accessdenied.do");
			}
		}
	}
		
}
