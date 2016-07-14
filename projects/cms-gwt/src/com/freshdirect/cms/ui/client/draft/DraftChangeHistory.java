package com.freshdirect.cms.ui.client.draft;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DraftChangeHistory extends LayoutContainer {

    private DraftChangesLoader draftChangeLoader;
    private BaseListLoader<ListLoadResult<BaseModelData>> loader;

    public DraftChangeHistory() {
        super();
        setLayout(new BorderLayout());
        init();
    }

    public void init() {
        draftChangeLoader = new DraftChangesLoader();
        loader = new BaseListLoader<ListLoadResult<BaseModelData>>(draftChangeLoader);
        loader.setRemoteSort(false);
        loader.setSortDir(SortDir.DESC);
        loader.setSortField("createdAt");

        ListStore<BaseModelData> store = new ListStore<BaseModelData>(loader);
        store.setSortField("createdAt");
        store.setSortDir(SortDir.DESC);

        // configure columns
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        {
            ColumnConfig cc = new ColumnConfig("createdAt", "Creation Date", 130);
            cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
            columns.add(cc);
        }

        columns.add(new ColumnConfig("userName", "User", 60));
        columns.add(new ColumnConfig("contentKey", "Content Key", 60));
        columns.add(new ColumnConfig("attributeName", "Attribute", 150));
        columns.add(noSort(new ColumnConfig("changedValue", "Changed Value", 150)));
        columns.add(noSort(new ColumnConfig("validationError", "Validation Error", 150)));

        // setup grid
        Grid<BaseModelData> grid = new Grid<BaseModelData>(store, new ColumnModel(columns));
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("validationError");
        grid.addStyleName("grid-cell-wrap");
        grid.getView().setEmptyText("No data to display.");

        AbsoluteLayout wrapLayout = new AbsoluteLayout();
        LayoutContainer wrap = new LayoutContainer(wrapLayout);

        wrap.add(grid, new AnchorData("100% 100%"));
        wrapLayout.setPosition(grid, 0, 0);

        add(wrap, new BorderLayoutData(LayoutRegion.CENTER));
        layout();
    }

    public BaseListLoader<ListLoadResult<BaseModelData>> getLoader(LoaderMode mode) {
        draftChangeLoader.setLoaderMode(mode);
        return loader;
    }

    private ColumnConfig noSort(ColumnConfig cc) {
        cc.setSortable(false);
        return cc;
    }
}
