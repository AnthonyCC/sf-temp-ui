package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.GwtPublishData;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author zsombor
 * 
 */
public class PublishHistoryPanel extends ContentPanel {

    private static final class ShowChangeHistoryCallback implements AsyncCallback<ChangeSetQueryResponse> {
        private final String id;
        private final boolean showConfigmatonPopup;

        private ShowChangeHistoryCallback() {
            this.id = "latest";
            this.showConfigmatonPopup = true;
        }
        
        private ShowChangeHistoryCallback(String id) {
            this.id = id;
            this.showConfigmatonPopup = false;
        }

        
        @Override
        public void onFailure(Throwable caught) {
            MainLayout.stopProgress();
            MessageBox.alert("Error", "Showing changes for publish:" + id + ", " + caught.getMessage(), null);
            caught.printStackTrace();
        }

        @Override
        public void onSuccess(ChangeSetQueryResponse result) {
            MainLayout.stopProgress();
            if (showConfigmatonPopup) {
                PublishConfirmationPopup popup = new PublishConfirmationPopup(result);
                popup.show();
            } else {
                ChangeHistoryPopUp popup = new ChangeHistoryPopUp(result, "Publish "+id);
                popup.show();
            }
        }
    }

    ListStore<GwtPublishData> store;
    private Grid<GwtPublishData> grid;

    public PublishHistoryPanel(List<GwtPublishData> datas) {
        setHeading("Publish History");

        BasePagingLoader<BasePagingLoadResult<GwtPublishData>> loader = new BasePagingLoader<BasePagingLoadResult<GwtPublishData>> (new PagingModelMemoryProxy(datas));
        store = new ListStore<GwtPublishData>(loader);
        
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("id", "ID", 50));
        columns.add(new ColumnConfig("status", "Status", 100));
        columns.add(new ColumnConfig("created", "Publish On", 150));
        columns.add(new ColumnConfig("publisher", "Publish By", 150));
        columns.add(new ColumnConfig("comment", "Comment", 300));
        {
            ColumnConfig c = new ColumnConfig("button", "Details", 50);
            c.setRenderer(new GridCellRenderer<GwtPublishData> () {
                @Override
                public Object render(GwtPublishData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtPublishData> store,
                        Grid<GwtPublishData> grid) {
                    final String id = model.getId();
                    return new Button("Details", new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            ChangeSetQuery q = new ChangeSetQuery();
                            q.setPublishId(id);
                            q.setRange(0, 20);
                            MainLayout.startProgress("Details", "Downloading changes in publish "+id, "loading...");
                            CmsGwt.getContentService().getChangeSets(q, new ShowChangeHistoryCallback(id));
                        } 
                    });
                }
            });
            columns.add(c);
        }
        
        final PagingToolBar toolBar = new PagingToolBar(20);  
        toolBar.bind(loader);
        setBottomComponent(toolBar);

        getHeader().addTool(new Button("Start Publish", new SelectionListener<ButtonEvent> () {
            @Override
            public void componentSelected(ButtonEvent ce) {
                ChangeSetQuery q = new ChangeSetQuery();
                q.setPublishId("latest");
                q.setRange(0, 20);
                MainLayout.startProgress("Details", "Downloading recent changes since last publish", "loading...");
                CmsGwt.getContentService().getChangeSets(q, new ShowChangeHistoryCallback());
            }
        }));
        
        ColumnModel cm = new ColumnModel(columns);
        grid = new Grid<GwtPublishData>(store, cm);
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("comment");
        grid.setAutoHeight(true);

        add(grid);
        loader.load(0, 20);

    }
}
