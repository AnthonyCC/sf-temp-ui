package com.freshdirect.cms.ui.client.publish;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.fields.Renderers;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ChangeSetPanel extends ContentPanel {

    Grid<BaseModelData> grid;
    ListStore<BaseModelData> store;

    public ChangeSetPanel(GwtChangeSet change) {
        this(new ChangeSetQueryResponse(change));
    }

    public ChangeSetPanel(ChangeSetQueryResponse response) {
        super();
        setHeading("Change Set");

        setScrollMode(Scroll.AUTO);
        setLayout(new FitLayout());

        BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(new ChangeSetLoader(response));
        loader.setRemoteSort(true);
        loader.setSortDir(SortDir.ASC);
        loader.setSortField("date");
        store = new ListStore<BaseModelData>(loader);
        store.setSortField("date");
        store.setSortDir(SortDir.ASC);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        
        // ============ USER ============
        columns.add(new ColumnConfig("user", "User", 60));
        
        // ============ DATE ============
        {
            ColumnConfig cc = new ColumnConfig("date", "Creation Date", 120);
            cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());

            columns.add(cc);
        }
        
        // ============ CHANGETYPE ============
        columns.add(noSort(new ColumnConfig("changeType", "Type", 50)));

        // ============ CONTENTNODE ============
        {
            ColumnConfig cc = noSort(new ColumnConfig("contentNode", "Content", 150));
            cc.setRenderer(Renderers.GRID_LINK_RENDERER);
            columns.add(cc);
        }
        
        // ============ ATTRIBUTE ============
        columns.add(noSort(new ColumnConfig("attribute", "Attribute", 150)));
        
        // ============ OLD VALUE ============
        columns.add(noSort(new ColumnConfig("old", "Old Value", 200)));
        
        // ============ NEW VALUE ============
        columns.add(noSort(new ColumnConfig("new", "New Value", 200)));

        
		final PagingToolBar toolBar = new PagingToolBar( 20 );
		toolBar.bind( loader );
		setBottomComponent( toolBar );

		grid = new Grid<BaseModelData>( store, new ColumnModel( columns ) );
		grid.setStripeRows( true );
		grid.setAutoExpandColumn( "attribute" );
		grid.setAutoHeight( true );
		
		add( grid );
		loader.load( 0, 20 );
    }
    
    static ColumnConfig noSort(ColumnConfig cc) {
        cc.setSortable(false);
        return cc;
    }

}
