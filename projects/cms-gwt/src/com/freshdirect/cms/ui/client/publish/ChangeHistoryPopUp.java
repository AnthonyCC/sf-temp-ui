package com.freshdirect.cms.ui.client.publish;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.freshdirect.cms.ui.client.ChangeSetPanel;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;

public class ChangeHistoryPopUp extends Window {

	public ChangeHistoryPopUp( ChangeSetQueryResponse changeHistory, String label ) {
		super();
		setHeading( "Change History for : " + label );
		setLayout( new FillLayout( Orientation.VERTICAL ) );
		setModal( true );
		setMaximizable( true );
		setMinimizable( true );
		setClosable( true );
		setDraggable( true );
		setResizable( true );
		setSize( 900, 600 );

		add( new ChangeSetPanel( changeHistory ) );

		if ( changeHistory.getPublishMessages() != null ) {
			add( new PublishMessagesPanel( changeHistory.getPublishMessages() ) );
		}
	}
}
