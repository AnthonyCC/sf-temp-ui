package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;

public class Utils {

    /**
     * Renders a link to the row content node model. This can be used, when
     * content nodes are displayed with their properties, so the
     * ContentNodeModel contains "key", "label", and "id" values.
     */
    public final static GridCellRenderer<ContentNodeModel> GRID_LINK_RENDERER = new GridCellRenderer<ContentNodeModel>() {
        public Object render(ContentNodeModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ContentNodeModel> store,
                Grid<ContentNodeModel> grid) {
            return renderLink(model);
        }
    };

    /**
     * Renders a link to a content node model, which stored as the property of
     * the model.
     */
    public final static GridCellRenderer<BaseModelData> GRID_LINK_FROM_PROPERTY_RENDERER = new GridCellRenderer<BaseModelData>() {
        public Object render(BaseModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<BaseModelData> store,
                Grid<BaseModelData> grid) {
            Object rmodel = model.get(property);
            if (rmodel instanceof ContentNodeModel) {
                return renderLink((ContentNodeModel) rmodel);
            } else {
                return rmodel != null ? rmodel.toString() : "<i>null</i>";
            }
        }
    };

    public static Component renderLink(ContentNodeModel model) {
        
        StringBuilder sb = new StringBuilder(512);
        sb.append("<table class=\"content-label\"><tr><td><img src=\"img/icons/");
        sb.append(model.getType());
        sb.append(".gif\"></td>");
        sb.append("<td><a href=\"#");
        sb.append(model.getKey());
        sb.append("\">");
        String label = model.getLabel();
        if (label != null && label.trim().length() > 0) {
            sb.append(model.getLabel());
        } else {
            sb.append(model.getKey());
        }
        sb.append("</a></td></tr></table>");

        HtmlContainer container = new HtmlContainer(sb.toString());
        if (model.get("path") != null) {
            if (model.isImageType()) {
                container.setToolTip("<img src=\"" + model.get("path") + "\" width=\"" + model.get("width") + "\" height=\"" + model.get("height") + "\">");
            }
            if (model.isHtmlType()) {
                container.setToolTip("<iframe src=\"" + model.get("path") + "\"></iframe>");
                //container.setToolTip("<a target=\"_blank\" href=\"#\"><img src=\"img/image_zoom.gif\"></a>");
                //sb.append("<td><a target=\"_blank\" href=\"#\"><img src=\"img/image_zoom.gif\"></a></td>");
            }
        }
        
        return container;
    }

}
