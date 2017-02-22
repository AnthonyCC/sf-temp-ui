package com.freshdirect.cms.ui.client.publish;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.fields.Renderers;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PublishMessages extends LayoutContainer {

	public PublishMessages(ChangeSetQuery query) {
		super();
		setLayout(new BorderLayout());
		CmsGwt.getContentService().getChangeSets(query, new AsyncCallback<ChangeSetQueryResponse>() {
			
			@Override
			public void onSuccess(ChangeSetQueryResponse result) {				
				init(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	public void init(ChangeSetQueryResponse changeHistory) {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		final BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>> loader = new BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>>(
                new PublishMessageLoader(changeHistory));
        loader.setRemoteSort(true);

        ListStore<GwtPublishMessage> store = new ListStore<GwtPublishMessage>(loader);
		
		// ============ SEVERITY ============
		ColumnConfig severityColumn = new ColumnConfig("severity", "Severity", 80);
		severityColumn.setRenderer(new GridCellRenderer<GwtPublishMessage>() {

				@Override
				public Object render(GwtPublishMessage model, String property, ColumnData config, int rowIndex, int colIndex, 
						ListStore<GwtPublishMessage> store,	Grid<GwtPublishMessage> grid) {

					final String severity = model.getSeverity();

					Text severityLabel = new Text(severity);
					severityLabel.setTagName("span");

					if ("FAILURE".equals(severity)){
						severityLabel.addStyleName("publish-failure");
					} else if ("ERROR".equals(severity)){
						severityLabel.addStyleName("publish-error");
					} else if ("WARNING".equals(severity)){
						severityLabel.addStyleName("publish-warning");
					} else if ("INFO".equals(severity)){
						severityLabel.addStyleName("publish-info");
					} else if ("DEBUG".equals(severity)){
						severityLabel.addStyleName("publish-debug");
					} else {
						severityLabel.addStyleName("publish-info");
					}

					return severityLabel;
				}
			});
		columns.add(severityColumn);

		// ============ TIMESTAMP ============
		ColumnConfig timeStampColumn = new ColumnConfig("timestamp", "Timestamp", 120);
		timeStampColumn.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
		columns.add(timeStampColumn);

		// ============ CONTENTNODE ============
		ColumnConfig contentKeyColumn = new ColumnConfig("key", "Content Node", 150);
		contentKeyColumn.setRenderer(Renderers.GRID_LINK_RENDERER);
		columns.add(contentKeyColumn);

		// ============ MESSAGE ============
		ColumnConfig messageColumn = new ColumnConfig("message", "Message", 150);
		messageColumn.setId("message");
		columns.add(messageColumn);
		
		// ============ STORE ID ============
		ColumnConfig storeIdColumnConfig = new ColumnConfig("storeId", "Store", 150);
		storeIdColumnConfig.setId("storeId");
		columns.add(storeIdColumnConfig);
		
		// ============ TASK ===============
		ColumnConfig taskColumnConfig = new ColumnConfig("task", "Task", 150);
		taskColumnConfig.setId("task");
		columns.add(taskColumnConfig);

		final PagingToolBar toolBar = new PagingToolBar(20);
		toolBar.bind(loader);		
		BorderLayoutData north = new BorderLayoutData(LayoutRegion.NORTH);
		north.setSize(28);
		add(toolBar, north);

		final Grid<GwtPublishMessage> grid = new Grid<GwtPublishMessage>(store, new ColumnModel(columns));
		grid.setStripeRows(true);
		grid.setAutoExpandColumn("message");	
		
		grid.addListener(Events.Attach, new Listener<GridEvent<GwtPublishMessage>>() {
			
			@Override
			public void handleEvent(GridEvent<GwtPublishMessage> be) {  				
				loader.load(0, 20);
			}  
		}); 
		
		AbsoluteLayout wrapLayout = new AbsoluteLayout();		
		LayoutContainer wrap = new LayoutContainer(wrapLayout);
		
		wrap.add(grid, new AnchorData("100% 100%"));		
		wrapLayout.setPosition(grid, 0, 0);
		
		add(wrap, new BorderLayoutData(LayoutRegion.CENTER));
		layout();
	}

}
