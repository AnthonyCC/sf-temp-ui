package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *	Represents tabs, sections, and fields in the content editor. 
 */
public class TabDefinition implements Serializable {

	private static final long serialVersionUID = 1710408732686470615L;
	
	/** List of tab id-s */
	private List<String> tabs = new ArrayList<String>();

	/** Maps tab id-s to tab labels	 */
	private Map<String,String> tabLabels = new HashMap<String,String>();

	/** Maps tab id-s to list of section id-s  */
	private Map<String, List<String>> sections = new HashMap<String, List<String>>();
	
	/** Maps section id-s to section labels	 */
	private Map<String,String> sectionLabels = new HashMap<String,String>();
	
	/** Maps section id-s to list of attribute keys	 */
	private Map<String, List<String>> attributeKeys = new HashMap<String, List<String>>();
	
	/**   */
	private Map<String, CustomFieldDefinition> customFields = new HashMap<String, CustomFieldDefinition> ();
	
	
	
	// ======================================== methods for constructing the tab definition ===========================================

	/**
	 * Adds a new tab.
	 * 
	 * @param tabId
	 * @param tabLabel
	 */
	public void addTab( String tabId, String tabLabel ) {
		tabs.add( tabId );
		tabLabels.put( tabId, tabLabel );
		sections.put( tabId, new ArrayList<String>() );
	}

	/**
	 * 	Adds a new section to a tab.
	 * 
	 * @param tabId	
	 * @param sectionId
	 * @param sectionLabel
	 */
	public void addSection( String tabId, String sectionId, String sectionLabel ) {
		List<String> sectionIdList = sections.get( tabId ); 
		if ( sectionIdList == null ) {
			// implicit add tab ? .. using id as label
			System.out.println("WARNING : Implicit tab add, this shouldn't happen.");
			addTab( tabId, tabId );
			sectionIdList = sections.get( tabId );
		}
		sectionIdList.add( sectionId );
		sectionLabels.put( sectionId, sectionLabel );		
		attributeKeys.put( sectionId, new ArrayList<String>() );
	}
	
	/**
	 * 	Adds an attribute(key) to a section.
	 * 
	 * @param sectionId
	 * @param attributeKey
	 */
	public void addAttributeKey( String sectionId, String attributeKey ) {
		List<String> attrKeyList = attributeKeys.get( sectionId ); 
		if ( attrKeyList == null ) {
			// implicit add section ? we have no tabId ...  
			System.out.println("WARNING : Implicit section add, this shouldn't happen.");
			addSection( "PhantomTab", sectionId, sectionId );
			attrKeyList = attributeKeys.get( sectionId );
		}
		attrKeyList.add( attributeKey );
	}

	/**
	 * 
	 * @param attribute
	 * @param fieldDef
	 */
	public void addCustomFieldDefinition( String attribute, CustomFieldDefinition fieldDef ) {
		customFields.put( attribute, fieldDef );
	}

	
	
	// ====================================== methods for getting data =============================================
	
	/**
	 * 	Get a list of tab id-s.
	 * 
	 * @return a list of tab id-s
	 */
	public List<String> getTabIds() {
		return tabs;
	}
	
	/**
	 * 	Get a tab label for a given tab id.
	 * 
	 * @param tabId id of the tab
	 * @return a tab label
	 */
	public String getTabLabel( String tabId ) {
		return tabLabels.get( tabId );		
	}
			
	
	/**
	 * 	Get a list of section id-s for a given tab id.
	 * 
	 * @param tabId	id of the tab
	 * @return a list of sections in the given tab id.
	 */
	public List<String> getSectionIds( String tabId ) {
		return sections.get( tabId );
	}
	
	/**
	 * 	Get a section label for a given section id.
	 * 
	 * @param sectionId id of the section
	 * @return a section label
	 */
	public String getSectionLabel( String sectionId ) {
		return sectionLabels.get(  sectionId ); 
	}
	
	/**
	 * 	Get a list of attribute keys for a given section.
	 * 
	 * @param sectionId id of the section
	 * @return list of attribute keys in the given section
	 */
	public List<String> getAttributeKeys( String sectionId ) {
		return attributeKeys.get( sectionId );
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public CustomFieldDefinition getCustomFieldDefinition( String name ) {
		return customFields.get( name );
	}
	
}
