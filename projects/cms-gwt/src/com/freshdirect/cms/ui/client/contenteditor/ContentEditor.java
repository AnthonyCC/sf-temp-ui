package com.freshdirect.cms.ui.client.contenteditor;

import java.util.HashMap;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.TabDefinition;

public class ContentEditor extends TabPanel {

	private HashMap<String, ContentForm>	panels;
	private HashMap<String, TabItem>		tabs;
	private GwtNodeData						nodeData;
	private static String 					lastActiveTab = null;

	public ContentEditor() {
		super();
		panels = new HashMap<String, ContentForm>();
		tabs = new HashMap<String, TabItem>();
		setBodyBorder( false );
		setBorders( false );
	}

	public ContentEditor( GwtNodeData nodeData, String contextPath ) {
		super();
		this.nodeData = nodeData;
		panels = new HashMap<String, ContentForm>();
		tabs = new HashMap<String, TabItem>();
		setBodyBorder( false );
		setBorders( false );
		addTabs( nodeData.getTabDefinition(), contextPath );

		if ( lastActiveTab != null && tabs.containsKey( lastActiveTab )) {
			setAutoSelect( false );
			setSelection( tabs.get( lastActiveTab ) );
		}
	}

	public void addTabs( TabDefinition tabDef, final String contextPath ) {
		
		for ( final String tabId : tabDef.getTabIds() ) {
			final String tabLabel = tabDef.getTabLabel( tabId );
			final TabItem tab = new TabItem( tabLabel );
			
			tab.addListener( Events.Select, new Listener<BaseEvent>() {
				public void handleEvent( BaseEvent be ) {
					if ( panels.get( tabLabel ) == null ) {
						ContentForm form = new ContentForm( tabId, nodeData, contextPath );
						panels.put( tabLabel, form );
						tab.add( form );
						tab.layout();
					}
					lastActiveTab = tabLabel;
				}
			} );
			
			add( tab );
			tabs.put( tabLabel, tab );
		}		
	}
}
