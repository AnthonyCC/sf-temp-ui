package com.freshdirect.webapp.crm.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.framework.util.StringUtil;

public class MenuBuilder {
	
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
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ClassLoader.getSystemResourceAsStream(xmlFile));
	}
	
	static class MenuBuilderException extends Exception {
		
	}
	
	public static void main(String a[]) throws Exception {
//		MenuBuilder.buildMenu("com\\freshdirect\\webapp\\crm\\security\\crm_roles_menu.xml");
		MenuBuilder.buildRolesMenu("crm_roles_menu.xml");
	}
	
	public static MenuAccess buildRolesMenu(String menuConfig) throws MenuBuilderException {
		
		MenuAccess menuAccess = new MenuAccess();
		
		try {
			
			Document doc = parserXML(menuConfig);
			Node rootMenus = doc.getFirstChild();
			String[] rootAccessRoles = StringUtil.decodeStrings(((Element)rootMenus).getAttribute("roles"));
			Map menuRoleMap = getMenuRole(Arrays.asList(rootAccessRoles));
			Map linkRoleMap =getLinkRole(Arrays.asList(rootAccessRoles));
			Map autoApproveLimitRoleMapping = getCreditLimitRole(Arrays.asList(rootAccessRoles));
			Map approveCreditLimitRoleMapping = getCreditLimitRole(Arrays.asList(rootAccessRoles));
			
			menuAccess.setMenuRoleMapping(menuRoleMap);
			menuAccess.setLinkRoleMapping(linkRoleMap);
			menuAccess.setAutoApproveLimitRoleMapping(autoApproveLimitRoleMapping);
			menuAccess.setApproveLimitRoleMapping(approveCreditLimitRoleMapping);
			
			NodeList nodes = ((Element)rootMenus).getChildNodes();
			
		    for (int i = 0; i < nodes.getLength(); i++) {		    	
		    	if(nodes.item(i) instanceof Element) {
			       Element rootMenuElement = (Element) nodes.item(i);
			       String roleName =rootMenuElement.getAttribute("name");
			       NodeList list =rootMenuElement.getElementsByTagName("links");
			       for(int x=0;x<list.getLength();x++){
			    	   String links = list.item(x).getFirstChild().getNodeValue();  			       
//			       	   String links = ((Element)rootMenuElement.getElementsByTagName("links").item(0)).getFirstChild().getNodeValue();
				       List accessLinks = Arrays.asList(StringUtil.decodeStrings(links.replaceAll("\n", "").replaceAll("\t", "").trim()));
				       
				       Map linksRoleMap = menuAccess.getLinkRoleMapping();
				       if(null != linksRoleMap){
				    	   if(menuAccess.getLinkRoleMapping().get(roleName) != null) {
				    		   ((List)menuAccess.getLinkRoleMapping().get(roleName)).addAll(accessLinks);
							}						
				       }
			       }
			       
			       //Approve and Auto Approve- Credit Limits
			       NodeList limitsList =rootMenuElement.getElementsByTagName("limit");
			       if(null !=limitsList){
			    	   for(int x=0;x<limitsList.getLength();x++){
			    		   if(limitsList.item(x) instanceof Element) {
						       Element limitElement = (Element) limitsList.item(x);
						       String limitName = limitElement.getAttribute("name");
						       String limitValue= limitElement.getFirstChild().getNodeValue();
						       if("autoApprove".equalsIgnoreCase(limitName)){
						    	   menuAccess.getAutoApproveLimitRoleMapping().put(roleName, limitValue);
						       }else if("approveCredit".equalsIgnoreCase(limitName)){
						    	   menuAccess.getApproveLimitRoleMapping().put(roleName, limitValue);
						       }
			    		   }
			    		   
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

	private static Map getLinkRole(List roles) {
		Map linkRoleMap = null;
		if(roles != null) {
			Iterator _itr = roles.iterator();
			linkRoleMap = new HashMap();
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				linkRoleMap.put(_tmpRole, new ArrayList());
			}
		}
		return linkRoleMap;
	}
	
	private static Map getCreditLimitRole(List roles) {
		Map creditLimitRoleMap = null;
		if(roles != null) {
			Iterator _itr = roles.iterator();
			creditLimitRoleMap = new HashMap();
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				creditLimitRoleMap.put(_tmpRole, null);
			}
		}
		return creditLimitRoleMap;
	}
}
