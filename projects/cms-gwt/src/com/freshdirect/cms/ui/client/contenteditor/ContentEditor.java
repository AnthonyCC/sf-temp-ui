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
	private GwtNodeData						nodeData;

	public ContentEditor() {
		super();
		panels = new HashMap<String, ContentForm>();
		setBodyBorder( false );
		setBorders( false );
	}

	public ContentEditor( GwtNodeData nodeData, String contextPath ) {
		super();
		this.nodeData = nodeData;
		panels = new HashMap<String, ContentForm>();
		setBodyBorder( false );
		setBorders( false );
		addTabs( nodeData.getTabDefinition(), contextPath );
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
				}
			} );
			
			add( tab );
		}
	}

}
