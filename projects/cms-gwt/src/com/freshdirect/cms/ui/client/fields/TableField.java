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
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute.ColumnType;
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
            ColumnType[] types = attribute.getTypes();
            for (int i = 0; i < config.getLimit() && config.getOffset() + i < rows.size(); i++) {
                Serializable[] serializables = rows.get(config.getOffset() + i);
                BaseModelData bmd = new BaseModelData();
                for (int j = 0; j < serializables.length; j++) {
                    bmd.set("col_" + j, serializables[j]);
                    if (types[j].equals(TableAttribute.ColumnType.CLASS)) {
                        bmd.set("class", serializables[j]);
                    }
                }
                result.add(bmd);
            }
            BasePagingLoadResult<BaseModelData> bplr = new BasePagingLoadResult<BaseModelData>(result, config.getOffset(), rows.size());
            callback.onSuccess(bplr);
        }
    }
    
    
    private static final class RowStyler extends GridViewConfig {
        @Override
        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
            String rowClass = model.get("class");
            return rowClass != null ? "cms-table-field-" + rowClass + (rowIndex % 2 == 0 ? "-even" : "-odd") : "";
        }
    }
    
    /**
     * Renders a link to a content node model, which stored as the property of
     * the model.
     */
    public final static GridCellRenderer<BaseModelData> GRID_LINK_FROM_PROPERTY_RENDERER = new GridCellRenderer<BaseModelData>() {
        public Object render(BaseModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BaseModelData> store,Grid<BaseModelData> grid) {
            Object rmodel = model.get(property);
            if (rmodel instanceof ContentNodeModel) {
                return ((ContentNodeModel) rmodel).renderLink();
            } else {
                return rmodel != null ? rmodel.toString() : "<i>null</i>";
            }
        }
    };

    
    TableAttribute attribute;

    protected Grid<BaseModelData> grid;

    public TableField(TableAttribute attribute) {
        this.attribute = attribute;

        BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = new BasePagingLoader<BasePagingLoadResult<BaseModelData>>(new TableRowLoader(attribute));

        ColumnType[] types = attribute.getTypes();

        int groupingColumn = -1;
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ContentNodeAttributeI[] columnAttributes = attribute.getColumns();
        for (int i = 0; i < columnAttributes.length; i++) {
            ContentNodeAttributeI col = columnAttributes[i];
            if (ColumnType.GROUPING == types[i]) {
                groupingColumn = i;
            }
            ColumnConfig cc = new ColumnConfig("col_" + i, col.getLabel(), 150);
            
            if (ColumnType.KEY == types[i]) {
                cc.setRenderer(GRID_LINK_FROM_PROPERTY_RENDERER);
            } else if (ColumnType.CLASS == types[i]) {
                cc.setHidden(true);
            }
            
            columns.add(cc);
        }

        final PagingToolBar toolBar = new PagingToolBar(20);
        toolBar.bind(loader);

        ContentPanel cp = new ContentPanel();
        cp.setBottomComponent(toolBar);
        cp.setAutoHeight(true);

        ListStore<BaseModelData> store;
        GroupingView view = null;

        if (groupingColumn != -1) {
            view = new GroupingView();
            view.setShowGroupedColumn(false);
            view.setForceFit(true);

            GroupingStore<BaseModelData> groupStore = new GroupingStore<BaseModelData>(loader);
            groupStore.groupBy("col_" + groupingColumn);

            store = groupStore;
            // view.setGroupRenderer(new GridGroupRenderer() {
        } else {
            store = new ListStore<BaseModelData>(loader);
        }

        grid = new Grid<BaseModelData>(store, new ColumnModel(columns));
        if (view != null) {
            grid.setView(view);
        }
        grid.getView().setViewConfig(new RowStyler());

        grid.setAutoHeight(true);
        grid.setStripeRows(true);
        // grid.getView().setForceFit(true);
        loader.load(0, 20);

        cp.add(grid);

        add(new AdapterField(cp));
    }
    
    @Override
    public Object getValue() {
        // return the original value, because no editing is possible with this field.
        return attribute.getValue();
    }
    

}
