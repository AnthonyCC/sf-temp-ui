package com.freshdirect.cms.ui.client.publish;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;

public class ChangeHistoryPopUp extends Window {

	public ChangeHistoryPopUp( ChangeSetQueryResponse changeHistory, String label ) {
		super();
		setHeading( "Change History for : " + label );
		setLayout( new FitLayout() );
		setModal( true );
		setMaximizable( true );
		setMinimizable( true );
		setClosable( true );
		setDraggable( true );
		setResizable( true );
		setSize( 900, 600 );
		
		 
		TabPanel tabPanel = new TabPanel();	
		
		TabItem changesetTab = new TabItem( "Changeset details" );
		changesetTab.setLayout( new FitLayout() );
		changesetTab.add( new ChangeSetPanel( changeHistory ) );
		tabPanel.add( changesetTab );
		
		if ( changeHistory.getPublishMessages() != null ) {
			TabItem messagesTab = new TabItem( "Publish messages" );
			messagesTab.setLayout( new FitLayout() );
			messagesTab.add( new PublishMessagesPanel( changeHistory.getPublishMessages() ) );
			tabPanel.add( messagesTab );
		}
		
		add( tabPanel );

	}
}
