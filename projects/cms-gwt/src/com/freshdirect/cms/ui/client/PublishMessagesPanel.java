package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.GwtPublishMessage;

public class PublishMessagesPanel extends ContentPanel {

    ListStore<ContentNodeModel> store;
    Grid<ContentNodeModel> grid;

    public PublishMessagesPanel(ChangeSetQueryResponse changeHistory) {
        super();
        setHeading("Messages");

        setScrollMode(Scroll.AUTO);
        setLayout(new FitLayout());

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("severity", "Severity", 80));
        columns.add(new ColumnConfig("date", "Timestamp", 120));
        ColumnConfig cc = new ColumnConfig("key", "Content Node", 150);
        cc.setRenderer(OneToManyRelationField.GRID_LINK_RENDERER);
        columns.add(cc);
        columns.add(new ColumnConfig("message", "Message", 150));

        store = new ListStore<ContentNodeModel>();

        for (GwtPublishMessage p : changeHistory.getPublishMessages()) {
            ContentNodeModel b = new ContentNodeModel(p.getContentType(), p.getContentId(), p.getContentId());
            b.set("severity", p.getSeverity().name());
            b.set("date", p.getTimestamp());
            b.set("key", p.getContentId());
            b.set("message", p.getMessage());
            store.add(b);
        }

        ColumnModel cm = new ColumnModel(columns);
        grid = new Grid<ContentNodeModel>(store, cm);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("message");
        grid.setAutoHeight(true);

        add(grid);
    }

}
