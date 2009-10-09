package com.freshdirect.cms.ui.client.publish;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;

public class ChangeHistoryPanel extends ContentPanel {

	public ChangeHistoryPanel( ChangeSetQueryResponse changeHistory, String label, boolean grouping ) {
        super();
        if (label != null) {
            setHeading("Change History for : " + label);
            setHeaderVisible(true);
        } else {
            setHeaderVisible(false);
        }
		setScrollMode( Scroll.AUTO );

        
		ContentPanel changeSetPanel;
		if ( grouping ) {
			changeSetPanel = new ChangeSetGroupingPanel( changeHistory );
		} else {
			changeSetPanel = new ChangeSetPanel( changeHistory );
		}

        if ( changeHistory.getPublishMessages() != null ) {

            TabPanel tabPanel = new TabPanel();

			TabItem changesetTab = new TabItem( "Changeset details" );
			changesetTab.setLayout( new FitLayout() );
			changesetTab.add( changeSetPanel );
			changesetTab.setAutoHeight( true );
			changesetTab.setAutoWidth( true );
			tabPanel.add( changesetTab );

			TabItem messagesTab = new TabItem( "Publish messages" );
			messagesTab.setLayout( new FitLayout() );
			messagesTab.add( new PublishMessagesPanel( changeHistory ) );
			messagesTab.setAutoHeight( true );
			messagesTab.setAutoWidth( true );
			tabPanel.add( messagesTab );
			
			add( tabPanel );
            
        } else {        	
    		setLayout( new FitLayout() );            
        	add( changeSetPanel );
        }

    }

}
