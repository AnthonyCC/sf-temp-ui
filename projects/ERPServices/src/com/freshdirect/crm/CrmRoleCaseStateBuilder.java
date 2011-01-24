package com.freshdirect.crm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.framework.util.StringUtil;

public class CrmRoleCaseStateBuilder {
	
	private static Map<CrmAgentRole, Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>> roleCaseStatesMap = null;
	
	public static Map<CrmAgentRole, Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>> buildCaseWorkFlow(String path){
		try {
			Document document = parseXML(path);
			Node root = document.getFirstChild();
			NodeList nodes = ((Element)root).getChildNodes();
//			Map<CrmAgentRole, Map<CrmCaseState, Map<String, CrmCaseState>>> roleCaseStatesMap = new HashMap<CrmAgentRole, Map<CrmCaseState, Map<String, CrmCaseState>>>();
			roleCaseStatesMap = new HashMap<CrmAgentRole, Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>>();
			for (int i = 0; i < nodes.getLength(); i++) {			    	
		    	if(nodes.item(i) instanceof Element) {
			       Element roleElement = (Element) nodes.item(i);
//			       String roleName = roleElement.getAttribute("name");
			       String roleNames[] = StringUtil.decodeStrings(((Element)roleElement).getAttribute("name"));
			       Map<CrmCaseState,  Map<CrmCaseActionType, CrmCaseState>> fromCaseStateMap = null;
				   fromCaseStateMap = buildCaseStatesMap(roleElement,fromCaseStateMap);	
				   for (int j = 0; j < roleNames.length; j++) {
				       CrmAgentRole crmRole = CrmAgentRole.getEnum(roleNames[j]);
				       if(crmRole!= null){				    	   
				    	   if(roleCaseStatesMap != null){
				    		   /*Map<CrmCaseState, Set<CrmCaseState>> caseStates = null;
				    		   if(roleCaseStates.containsKey(crmRole)){
				    			    caseStates = roleCaseStates.get(crmRole);				    			   
					    	   }
				    		   if(null == caseStates && roleElement.hasChildNodes()){
			    				   caseStates = new HashMap<CrmCaseState,Set<CrmCaseState>>();
				    		   }*/
				    		   roleCaseStatesMap.put(crmRole, fromCaseStateMap);
				    	   }  
				    	   
				       }
			       }
		       }			      
			}
			return roleCaseStatesMap;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static Map<CrmCaseState,  Map<CrmCaseActionType, CrmCaseState>> buildCaseStatesMap(
			Element roleElement,
			Map<CrmCaseState,  Map<CrmCaseActionType, CrmCaseState>> fromCaseStateMap) {
		if(roleElement.hasChildNodes()){
			   NodeList fromStateNodes = ((Element)roleElement).getElementsByTagName("from-state");
//			   fromCaseStateMap = new HashMap<CrmCaseState, Set<CrmCaseState>>();
			   fromCaseStateMap = new HashMap<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>();
			   for (int k = 0; k < fromStateNodes.getLength(); k++) {
				   if(fromStateNodes.item(k) instanceof Element) {
					   Element fromStateElement = (Element) fromStateNodes.item(k);
//	    					   String[] fromStateNames = StringUtil.decodeStrings(((Element)fromStateElement).getAttribute("name"));
					   String fromStateName = ((Element)fromStateElement).getAttribute("name");					   
					   CrmCaseState crmCaseState = CrmCaseState.getEnum(fromStateName);
					   //set for 'toCaseStates' for each 'fromState'.
					   /*Set<CrmCaseState> toCaseStates = new HashSet<CrmCaseState>();
					   buildToCaseStatesByFromCaseState(fromStateElement, toCaseStates);					   
					   if(null != crmCaseState){	    							   
						   fromCaseStateMap.put(crmCaseState, toCaseStates);
					   }*/	
					   
					   //Build Action for from-state.
					   Map<CrmCaseActionType,CrmCaseState> actions = new HashMap<CrmCaseActionType,CrmCaseState>();
					   if(fromStateElement.hasChildNodes()){
						   NodeList actionNodes = ((Element)fromStateElement).getElementsByTagName("action");				    						   
						   for (int m = 0; m < actionNodes.getLength(); m++) {
							   if(actionNodes.item(m) instanceof Element) {
								   Element actionElement = (Element) actionNodes.item(m);
								   String actionName = ((Element)actionElement).getAttribute("name");
								   CrmCaseActionType actionType = CrmCaseActionType.getEnum(actionName);
								   if(actionElement.hasChildNodes()){
									   NodeList toStateNodes = ((Element)actionElement).getElementsByTagName("to-state");				    						   
									   for (int l = 0; l < toStateNodes.getLength(); l++) {
										   if(toStateNodes.item(l) instanceof Element) {
											   Element toStateElement = (Element) toStateNodes.item(l);
											   String toStateName =((Element)toStateElement).getAttribute("name");					    					   
											   CrmCaseState crmCaseState1 = CrmCaseState.getEnum(toStateName);
												actions.put(actionType, crmCaseState1);											
											}
									   }   
								   }								
								}
						   }
					   }
					   if(null != crmCaseState && null!= actions && !actions.isEmpty()){
						   fromCaseStateMap.put(crmCaseState, actions);
					   }
				   }
			   }					    			   
		   }
		return fromCaseStateMap;
	}
	private static void buildToCaseStatesByFromCaseState(Element fromStateElement,
			Set<CrmCaseState> toCaseStates) {
		if(fromStateElement.hasChildNodes()){
			   NodeList toStateNodes = ((Element)fromStateElement).getElementsByTagName("to-state");				    						   
			   for (int l = 0; l < toStateNodes.getLength(); l++) {
				   if(toStateNodes.item(l) instanceof Element) {
					   Element toStateElement = (Element) toStateNodes.item(l);
					   String[] toStateNames = StringUtil.decodeStrings(((Element)toStateElement).getAttribute("name"));							    					   
					   for (int n= 0; n < toStateNames.length; n++) {
						   CrmCaseState crmCaseState = CrmCaseState.getEnum(toStateNames[n]);
						   if(null !=crmCaseState){
							   toCaseStates.add(crmCaseState);
						   }
					   }
						
					}
			   }
		   }
	}
	public static Document parseXML(String xmlFile) throws SAXException, IOException, ParserConfigurationException 	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(xmlFile));
	}
	public static void main(String a[]) throws Exception {
		CrmRoleCaseStateBuilder.buildCaseWorkFlow("C:\\CrmSecurity\\projects\\ERPSWebApp\\bin\\com\\freshdirect\\webapp\\crm\\security\\crm_case_states.xml");
	}
	public static Map<CrmAgentRole, Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>> getRoleCaseStatesMap() {
		if(null == roleCaseStatesMap){
			CrmRoleCaseStateBuilder.buildCaseWorkFlow("C:\\CrmSecurity\\projects\\ERPSWebApp\\bin\\com\\freshdirect\\webapp\\crm\\security\\crm_case_states.xml");
		}
		return roleCaseStatesMap;
	}
	public static void setRoleCaseStatesMap(
			Map<CrmAgentRole, Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>>> roleCaseStatesMap) {
		CrmRoleCaseStateBuilder.roleCaseStatesMap = roleCaseStatesMap;
	}
	
	public static Set getCaseActions(CrmAgentRole role, CrmCaseState fromState){
		Set actions = null;
		if(null != getRoleCaseStatesMap()){
			Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>> map = roleCaseStatesMap.get(role);
			if(null != map){
				for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
					CrmCaseState caseState = (CrmCaseState) iterator.next();
					if(caseState.equals(fromState)){
						actions = map.get(caseState).keySet();
						break;
					}					
				}
			}			
		}
		return actions;
	}
	
	public static CrmCaseState getCaseEndState(CrmAgentRole role, CrmCaseState fromState, CrmCaseActionType actionType){
		CrmCaseState endState = null;		
		if(null != getRoleCaseStatesMap()){
			Map<CrmCaseState, Map<CrmCaseActionType, CrmCaseState>> map = roleCaseStatesMap.get(role);
			if(null != map){
				for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
					CrmCaseState caseState = (CrmCaseState) iterator.next();
					if(caseState.equals(fromState)){
						 Map<CrmCaseActionType, CrmCaseState> actionStates = map.get(caseState);
						Set actions = map.get(caseState).keySet();
						for (Iterator iterator2 = actions.iterator(); iterator2
								.hasNext();) {
							CrmCaseActionType crmCaseActionType = (CrmCaseActionType) iterator2.next();
							if(actionType.equals(crmCaseActionType)){
								endState= actionStates.get(actionType);
								break;
							}
							
						}
						
					}					
				}
			}			
		}
		return endState;
	}

}
