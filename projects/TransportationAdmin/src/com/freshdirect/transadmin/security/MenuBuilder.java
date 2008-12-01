package com.freshdirect.transadmin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.framework.util.StringUtil;

public class MenuBuilder {
	
	public static MenuAccess buildMenu(String menuConfig) throws MenuBuilderException {
		
		MenuAccess menuAccess = new MenuAccess();
		
		try {
			
			Document doc = parserXML(menuConfig);
			Node rootMenus = doc.getFirstChild();
			String[] rootAccessRoles = StringUtil.decodeStrings(((Element)rootMenus).getAttribute("roles"));
			Map menuRoleMap = getMenuRole(Arrays.asList(rootAccessRoles));
			
			menuAccess.setMenuRoleMapping(menuRoleMap);
			menuAccess.setMenuLinkMapping(new HashMap());
			
			NodeList nodes = ((Element)rootMenus).getChildNodes();
			
		    for (int i = 0; i < nodes.getLength(); i++) {
		    	
		    	if(nodes.item(i) instanceof Element) {
			       Element rootMenuElement = (Element) nodes.item(i);
			       
			       Menu tmpRootMenu = new Menu(rootMenuElement.getAttribute("id"), rootMenuElement.getAttribute("title")
			    		   							, null, rootMenuElement.getAttribute("orientation"));
			       NodeList subNodes = rootMenuElement.getElementsByTagName("menu");
			       
			       
			       for (int j = 0; j < subNodes.getLength(); j++) {
			    	   
			    	   if(subNodes.item(j) instanceof Element) {
			    		   
			    		   Element subMenuElement = (Element) subNodes.item(j);
			    		   
			    		   Menu tmpSubMenu = new Menu(subMenuElement.getAttribute("id"), subMenuElement.getAttribute("title")
		   										, subMenuElement.getAttribute("link"), subMenuElement.getAttribute("orientation"));
			    		   
			    		   List accessRoles = Arrays.asList(StringUtil.decodeStrings
			    		   							(((Element)subMenuElement.getElementsByTagName("access-roles").item(0))
			    		   															.getFirstChild().getNodeValue()));
			    		   List accessUris = Arrays.asList(StringUtil.decodeStrings
  													(((Element)subMenuElement.getElementsByTagName("access-uris").item(0))
  															.getFirstChild().getNodeValue()));
			    		   populateMenuAccess(menuAccess, tmpRootMenu, tmpSubMenu, accessRoles, accessUris);
			    		   
			    	   }
			       }
			       
		    	}
		    }
		} catch (SAXException saxExp) {
			saxExp.printStackTrace();
			throw new MenuBuilderException();
		}  catch (IOException ioExp) {
			ioExp.printStackTrace();
			throw new MenuBuilderException();
		}  catch (ParserConfigurationException parseConfigExp) {
			parseConfigExp.printStackTrace();
			throw new MenuBuilderException();
		}
		
		return menuAccess;
	}
	
	//RootMenu -> Map(SubMenuId, SubMenu)
	private static void populateMenuAccess(MenuAccess menuAccess
											, Menu rootMenu, Menu subMenu
											, List accessRoles, List accessLinks) {
		
		if(menuAccess.getMenuRoleMapping() != null) {
			Iterator _itr = menuAccess.getMenuRoleMapping().keySet().iterator();
			
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				Map rootMenuMapping = (Map)menuAccess.getMenuRoleMapping().get(_tmpRole);
				if(rootMenuMapping.get(rootMenu) == null) {
					rootMenuMapping.put(rootMenu, new TreeMap());
				}
				if(accessRoles.contains(_tmpRole)) {
					((Map)rootMenuMapping.get(rootMenu)).put(subMenu.getMenuId(), subMenu);
					if(menuAccess.getLinkRoleMapping() == null) {
						menuAccess.setLinkRoleMapping(new HashMap());
					}
					if(menuAccess.getLinkRoleMapping().get(_tmpRole) == null) {
						menuAccess.getLinkRoleMapping().put(_tmpRole, new ArrayList());
					}
					((List)menuAccess.getLinkRoleMapping().get(_tmpRole)).addAll(accessLinks);
					if(accessLinks != null) {
						Iterator _itrLink = accessLinks.iterator();
						while(_itrLink.hasNext()) {
							String _tmpLink = (String)_itrLink.next();
							menuAccess.getMenuLinkMapping().put(_tmpLink, subMenu.getMenuId());
						}
					}
				}
			}
		}		
	}
	
	private static Map getMenuRole(List roles) {
		//UserRole--> RootMenu Map
		Map menuRoleMap = null;
		if(roles != null) {
			Iterator _itr = roles.iterator();
			menuRoleMap = new HashMap();
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				menuRoleMap.put(_tmpRole, new HashMap());
			}
		}
		return menuRoleMap;
	}
	
	public static Document parserXML(String xmlFile) throws SAXException, IOException, ParserConfigurationException 	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.parse(ClassUtils.getDefaultClassLoader().getResourceAsStream(xmlFile));
	}
	
	static class MenuBuilderException extends Exception {
		
	}
	
	public static void main(String a[]) throws Exception {
		MenuBuilder.buildMenu("com/freshdirect/transadmin/security/menu.xml");
	}
}
