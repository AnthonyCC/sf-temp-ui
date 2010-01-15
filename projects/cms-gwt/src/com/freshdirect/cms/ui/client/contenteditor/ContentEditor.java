package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;
import com.freshdirect.cms.ui.model.TabDefinition;

public class ContentEditor extends TabPanel implements EditorDecoratorI {
	private GwtContextualizedNodeI cn;
	
	/** Tab ID -> form */
	private HashMap<String, ContentForm>	panels;
	/** Tab ID -> tab */
	private HashMap<String, TabItem>		tabs;

	private static String 					lastActiveTab = null;


	public ContentEditor( GwtContextualizedNodeI cn ) {
		super();
		this.cn = cn;
		this.panels = new HashMap<String, ContentForm>();
		this.tabs = new HashMap<String, TabItem>();

		setBodyBorder( true );
		setBorders( false );

		setPlain(true);
		setStyleAttribute("background-color", "#EAF2FF");
		setupTabs();
				

		if ( lastActiveTab != null && tabs.containsKey( lastActiveTab )) {
			setAutoSelect( false );
			setSelection( tabs.get( lastActiveTab ) );
		}
	}

	public void setupTabs() {
		final TabDefinition tabDef = cn.getNodeData().getTabDefinition();
		
		for ( final String tabId : tabDef.getTabIds() ) {
			final String tabLabel = tabDef.getTabLabel( tabId );
			final TabItem tab = new TabItem( tabLabel );
			
			tab.addListener( Events.Select, new Listener<BaseEvent>() {
				public void handleEvent( BaseEvent be ) {
					ContentForm form;
					form = panels.get( tabLabel);
					if ( form == null ) {
						form = new ContentForm( cn, tabId );
						panels.put( tabLabel, form );
						tab.add( form );
						tab.layout();
						
					}
					lastActiveTab = tabLabel;
					// decorate attribute fields if necessary
					if (ManageStoreView.getInstance().getDetailPanel() instanceof ContentEditorPanel && ((ContentEditorPanel) ManageStoreView.getInstance().getDetailPanel()).getCompareUtil()!=null) {
						((ContentEditorPanel) ManageStoreView.getInstance().getDetailPanel()).getCompareUtil().addFormDecoration(form);
					}					
				}
			} );
			
			tab.setScrollMode(Scroll.AUTO);
			tab.addStyleName("tab-item");
			add( tab );
			tabs.put( tabLabel, tab );
		}		
	}

	public HashMap<String, ContentForm> getPanels() {
		return panels;
	}
	
	public HashMap<String, TabItem> getTabs() {
		return tabs;
	}
	
	public static String getLastActiveTab() {
		return lastActiveTab;
	}

	@Override
	public void compareDecorate(GwtContextualizedNodeData comparedNode,
			Map<String, Serializable> result) {
		// TODO Auto-generated method stub
		
	}
}
