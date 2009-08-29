package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
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
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
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

        setScrollMode(Scroll.NONE);
        setLayout(new FitLayout());

        BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(new ChangeSetLoader(response));
        store = new ListStore<BaseModelData>(loader);
        store.setSortField("date");

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("user", "User", 80));
        {
            ColumnConfig cc = new ColumnConfig("date", "Creation Date", 120);
            cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());

            columns.add(cc);
        }
        columns.add(noSort(new ColumnConfig("type", "Type", 70)));
        columns.add(noSort(new ColumnConfig("key", "Content Key", 150)));
        columns.add(noSort(new ColumnConfig("attribute", "Attribute", 150)));
        columns.add(noSort(new ColumnConfig("old", "Old Value", 200)));
        columns.add(noSort(new ColumnConfig("new", "New Value", 400)));

        final PagingToolBar toolBar = new PagingToolBar(20);
        toolBar.bind(loader);
        setBottomComponent(toolBar);

        ColumnModel cm = new ColumnModel(columns);
        grid = new Grid<BaseModelData>(store, cm);
        grid.setStripeRows(true);
        // if auto height is set to true, then there will be no scrollbar!
        // grid.setAutoHeight(true);
        add(grid);

        loader.load(0, 20);
    }
    
    static ColumnConfig noSort(ColumnConfig cc) {
        cc.setSortable(false);
        return cc;
    }

}
