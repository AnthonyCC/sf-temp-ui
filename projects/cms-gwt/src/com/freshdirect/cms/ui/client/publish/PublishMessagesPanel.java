package com.freshdirect.cms.ui.client.publish;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.fields.Renderers;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage.Level;

public class PublishMessagesPanel extends ContentPanel {

        ChangeSetQueryResponse changeHistory;
	ListStore<GwtPublishMessage>	store;
	Grid<GwtPublishMessage>			grid;

	public PublishMessagesPanel( ChangeSetQueryResponse changeHistory ) {
		super();
		this.changeHistory = changeHistory;
		setHeading( "Messages" );
		setScrollMode( Scroll.AUTO );
		setLayout( new FitLayout() ); 

                BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>> loader = new BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>>(
                        new PublishMessageLoader(changeHistory));
                loader.setRemoteSort(true);
                store = new ListStore<GwtPublishMessage>(loader);
        
                
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
        // ============ SEVERITY ============
        {
			ColumnConfig c = new ColumnConfig( "severity", "Severity", 80 );
			c.setRenderer( new GridCellRenderer<GwtPublishMessage>() {

				@Override
				public Object render( GwtPublishMessage model, String property, ColumnData config, int rowIndex,
						int colIndex, ListStore<GwtPublishMessage> store, Grid<GwtPublishMessage> grid ) {

					final Level severity = model.getSeverity();

					Text severityLabel = new Text( model.getSeverity().name() );
					severityLabel.setTagName( "span" );

					switch ( severity ) {
						case FAILURE:	
							severityLabel.addStyleName( "publish-failure" );
							break;							
						case ERROR:							
							severityLabel.addStyleName( "publish-error" );
							break;							
						case WARNING:							
							severityLabel.addStyleName( "publish-warning" );
							break;							
						case INFO:							
							severityLabel.addStyleName( "publish-info" );
							break;							
						case DEBUG:							
							severityLabel.addStyleName( "publish-debug" );
							break;
					}
					
					return severityLabel;
				}
			} );
			columns.add( c );
        }
		
        // ============ TIMESTAMP ============
		columns.add( new ColumnConfig( "timestamp", "Timestamp", 120 ) );
		
        // ============ CONTENTNODE ============
		ColumnConfig cc = new ColumnConfig( "key", "Content Node", 150 );
		cc.setRenderer( Renderers.GRID_LINK_RENDERER );
		columns.add( cc );
		
        // ============ MESSAGE ============
		columns.add( new ColumnConfig( "message", "Message", 150 ) );

		final PagingToolBar toolBar = new PagingToolBar( 20 );
		toolBar.bind( loader );
		setBottomComponent( toolBar );
        
		grid = new Grid<GwtPublishMessage>( store, new ColumnModel( columns ) );
		grid.setStripeRows( true );
		grid.setAutoExpandColumn( "message" );
		grid.setAutoHeight( true );

		add( grid );
        loader.load(0, 20);
	}

}
