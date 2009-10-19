package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.OneToManyModel;

public class Renderers {

    private Renderers() {
    }

    public static class LabelRenderer implements GridCellRenderer<OneToManyModel> {
        String modelProperty;
        String columnLabel;

        LabelRenderer(String modelProperty, String columnLabel) {
            this.modelProperty = modelProperty;
            this.columnLabel = columnLabel;
        }

        @Override
        public Object render(OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<OneToManyModel> store,
                Grid<OneToManyModel> grid) {
            return model.get(modelProperty);
        }
        
        @Override
        public String toString() {
        	return columnLabel;
        }
    }

    public static GridCellRenderer<ModelData> ROW_INDEX = new GridCellRenderer<ModelData>() {
        public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
            return String.valueOf(rowIndex + 1);
        }
    };

    /**
     * Renders a link to the row content node model. This can be used, when
     * content nodes are displayed with their properties, so the
     * ContentNodeModel contains "key", "label", and "id" values.
     */
    public final static GridCellRenderer<ContentNodeModel> GRID_LINK_RENDERER = new GridCellRenderer<ContentNodeModel>() {
        public Object render(ContentNodeModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ContentNodeModel> store,
                Grid<ContentNodeModel> grid) {
            return model.renderLinkComponent();
        }
    };

    
    /**
     * Renders a link to a content node model, which stored as the property of
     * the model.
     */
    public final static GridCellRenderer<BaseModelData> GRID_LINK_FROM_PROPERTY_RENDERER = new GridCellRenderer<BaseModelData>() {
        public Object render(BaseModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BaseModelData> store,Grid<BaseModelData> grid) {
            Object rmodel = model.get(property);
            if (rmodel instanceof ContentNodeModel) {
                return ((ContentNodeModel) rmodel).renderLinkComponent();
            } else {
                return rmodel != null ? rmodel.toString() : "<i>null</i>";
            }
        }
    };


}
