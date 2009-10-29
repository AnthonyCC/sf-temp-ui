package com.freshdirect.cms.ui.client.publish;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ChangeSetGroupingPanel extends ContentPanel {

    public final static GridCellRenderer<ContentNodeModel> GRID_LINK_RENDERER = new GridCellRenderer<ContentNodeModel>() {
        public Object render(final ContentNodeModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ContentNodeModel> store,
                final Grid<ContentNodeModel> grid) {
            final HorizontalPanel hp = new HorizontalPanel();
            Button historyButton = new Button("History", new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    MainLayout.viewHistory(model);
                }
            });

            hp.add(model.renderLinkComponent());
            hp.add(historyButton);

            if (model.get("previewLink") != null) {
                final HtmlContainer previewLink = new HtmlContainer();
                previewLink.setHtml("<a class=\"previewLink\" href=\"" + model.get("previewLink") + "\" target=\"_blank\" title=\"Preview...\">Preview...</a>");
                hp.add(previewLink);
            }

            return hp;
        }
    };
    
    Grid<BaseModelData> grid;
    GroupingStore<BaseModelData> store;

    public ChangeSetGroupingPanel(GwtChangeSet change) {
        this(new ChangeSetQueryResponse(change));
    }

    public ChangeSetGroupingPanel(ChangeSetQueryResponse response) {
        super();
		setHeading( "Change Set" );

		setScrollMode( Scroll.AUTO );
		setLayout( new FitLayout() );

		BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(
				new ChangeSetLoader( response ) );
		loader.setRemoteSort( true );
		loader.setSortDir( SortDir.ASC );
		loader.setSortField( "date" );
        
		store = new GroupingStore<BaseModelData>( loader );
		store.setSortField( "date" );
		store.setSortDir( SortDir.ASC );
		store.groupBy( "key" );

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        
        // ============ USER ============
        columns.add(new ColumnConfig("user", "User", 50));
        
        // ============ DATE ============
        {
            ColumnConfig cc = new ColumnConfig("date", "Creation Date", 100);
            cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());

            columns.add( noGroup( cc ) );
        }
        
        // ============ CHANGETYPE ============
        columns.add(noSort(new ColumnConfig("changeType", "Type", 50)));

        // ============ CONTENTNODE ============
        {
            ColumnConfig cc = noSort(new ColumnConfig("key", "Content", 250));
            cc.setRenderer(GRID_LINK_RENDERER);
            columns.add(cc);
        }
        
        // ============ ATTRIBUTE ============
		columns.add( noSort( new ColumnConfig( "attribute", "Attribute", 100 ) ) );
        
        // ============ OLD VALUE ============
		columns.add( noGroup( noSort( new ColumnConfig( "old", "Old Value", 200 ) ) ) );
        
        // ============ NEW VALUE ============
		columns.add( noGroup( noSort( new ColumnConfig( "new", "New Value", 200 ) ) ) );
		
        
		final PagingToolBar toolBar = new PagingToolBar( 20 );
		toolBar.bind( loader );
		setBottomComponent( toolBar );

		final ColumnModel columnModel = new ColumnModel( columns );
		grid = new Grid<BaseModelData>( store, columnModel );
		grid.setStripeRows( true );
		grid.setAutoExpandColumn( "new" );
		grid.setAutoHeight( true );
		grid.setAutoWidth( true );
		
		final GroupingView view = new GroupingView();
		view.setShowGroupedColumn( true );
		view.setForceFit( false );
		view.setGroupRenderer( new GridGroupRenderer() {			
			@Override public String render( GroupColumnData data ) {
				if ( data.field.equals( "key" ) ) {
					for ( ModelData model : data.models ) {
						if ( ! (model instanceof ContentNodeModel) )
							continue;
						final ContentNodeModel cnModel = (ContentNodeModel)model; 
						
						if ( cnModel.getKey().equals( data.group ) ) {							
							return cnModel.getLabel() + " [" + cnModel.getKey() + "]";		
						}
					}
				} 
				return data.group;
			}
		});
		grid.setView( view );
		
		add( grid );
		loader.load( 0, 20 );
    }
    
	static ColumnConfig noSort( ColumnConfig cc ) {
		cc.setSortable( false );
		return cc;
	}

	static ColumnConfig noGroup( ColumnConfig cc ) {
		cc.setGroupable( false );
		return cc;
	}
}
