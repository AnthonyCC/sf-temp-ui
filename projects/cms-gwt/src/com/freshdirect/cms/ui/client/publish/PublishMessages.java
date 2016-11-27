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
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage.Level;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PublishMessages extends LayoutContainer {

	private BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>> loader;
	
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

		loader = new BasePagingLoader<BasePagingLoadResult<GwtPublishMessage>>(
                new PublishMessageLoader(changeHistory));
        loader.setRemoteSort(true);

        ListStore<GwtPublishMessage> store = new ListStore<GwtPublishMessage>(loader);
		
		// ============ SEVERITY ============
		{
			ColumnConfig c = new ColumnConfig("severity", "Severity", 80);
			c.setRenderer(new GridCellRenderer<GwtPublishMessage>() {

				@Override
				public Object render(GwtPublishMessage model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<GwtPublishMessage> store,
						Grid<GwtPublishMessage> grid) {

					final Level severity = model.getSeverity();

					Text severityLabel = new Text(model.getSeverity().name());
					severityLabel.setTagName("span");

					switch (severity) {
					case FAILURE:
						severityLabel.addStyleName("publish-failure");
						break;
					case ERROR:
						severityLabel.addStyleName("publish-error");
						break;
					case WARNING:
						severityLabel.addStyleName("publish-warning");
						break;
					case INFO:
						severityLabel.addStyleName("publish-info");
						break;
					case DEBUG:
						severityLabel.addStyleName("publish-debug");
						break;
					}

					return severityLabel;
				}
			});
			columns.add(c);
		}

		// ============ TIMESTAMP ============
		{
			ColumnConfig cc = new ColumnConfig("timestamp", "Timestamp", 120);
			cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
			columns.add(cc);
		}

		// ============ CONTENTNODE ============
		ColumnConfig cc = new ColumnConfig("key", "Content Node", 150);
		cc.setRenderer(Renderers.GRID_LINK_RENDERER);
		columns.add(cc);

		// ============ MESSAGE ============
		ColumnConfig c = new ColumnConfig("message", "Message", 150);
		c.setId("message");
		columns.add(c);

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
