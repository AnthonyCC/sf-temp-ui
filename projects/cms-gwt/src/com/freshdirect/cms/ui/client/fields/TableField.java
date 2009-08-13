package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TableField extends MultiField {

    private static final class TableRowLoader implements DataProxy<PagingLoadResult<? extends ModelData>> {
        TableAttribute attribute;
        public TableRowLoader(TableAttribute attribute) {
            this.attribute = attribute;
        }
        
        @Override
        public void load(DataReader<PagingLoadResult<? extends ModelData>> reader, Object loadConfig,
                AsyncCallback<PagingLoadResult<? extends ModelData>> callback) {
            final PagingLoadConfig config = (PagingLoadConfig) loadConfig;
            List<BaseModelData> result = new ArrayList<BaseModelData>(config.getLimit());
            List<Serializable[]> rows = this.attribute.getRows();
            for (int i = 0; i < config.getLimit() && config.getOffset() + i < rows.size(); i++) {
                Serializable[] serializables = rows.get(config.getOffset() + i);
                BaseModelData bmd = new BaseModelData();
                for (int j = 0; j < serializables.length; j++) {
                    bmd.set("col_" + j, serializables[j]);
                }
                result.add(bmd);
            }
            BasePagingLoadResult<BaseModelData> bplr = new BasePagingLoadResult<BaseModelData>(result, config.getOffset(), rows.size());
            callback.onSuccess(bplr);
        }
    }

    TableAttribute attribute;

    protected Grid<BaseModelData> grid;

    public TableField(TableAttribute attribute) {
        this.attribute = attribute;

        BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(
                new TableRowLoader(attribute));
        
        ListStore<BaseModelData> store = new ListStore<BaseModelData>(loader);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        for (int i = 0; i < attribute.getColumns().length; i++) {
            ContentNodeAttributeI col = attribute.getColumns()[i];
            columns.add(new ColumnConfig("col_" + i, col.getLabel(), 150));
        }

        final PagingToolBar toolBar = new PagingToolBar(20);
        toolBar.bind(loader);
        
        ContentPanel cp = new ContentPanel();
        cp.setBottomComponent(toolBar);
        cp.setAutoHeight(true);

        grid = new Grid<BaseModelData>(store, new ColumnModel(columns));
        
        grid.setAutoHeight(true);
        grid.setStripeRows(true);
        //grid.getView().setForceFit(true);
        loader.load(0, 20);

        cp.add(grid);
        
        add(new AdapterField(cp));
    }

}
