package com.freshdirect.cms.ui.client.changehistory;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
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

public class ChangeHistory extends LayoutContainer {
	
	ChangeSetQuery query;
	ChangeSetQueryResponse response;

	private List<HistoryListener> historyListeners = new ArrayList<HistoryListener>();

	public ChangeHistory(ChangeSetQuery query) {
		super();
		this.query = query;
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

	public ChangeHistory(ChangeSetQueryResponse response) {
		super();
		this.query = null;
		setLayout(new BorderLayout());
		init( response );
	}
	
	public void init(ChangeSetQueryResponse result) {

		this.response = result;		
		final BasePagingLoader<BasePagingLoadResult<BaseModelData>> loader = 
			new BasePagingLoader<BasePagingLoadResult<BaseModelData>>( new ChangeSetLoader( result ) );
        loader.setRemoteSort(true);
        loader.setSortDir(SortDir.ASC);
        loader.setSortField("date");
        
        ListStore<BaseModelData> store = new ListStore<BaseModelData>(loader);
        store.setSortField("date");
        store.setSortDir(SortDir.ASC);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        
        // ============ USER ============
        columns.add(new ColumnConfig("user", "User", 60));
        
        // ============ DATE ============
        {
            ColumnConfig cc = new ColumnConfig("date", "Creation Date", 130 );
            cc.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());

            columns.add(cc);
        }
        
        // ============ CHANGETYPE ============
        columns.add(noSort(new ColumnConfig("changeType", "Type", 50)));

        // ============ CONTENTNODE ============
        if ( query == null || query.getContentKey() == null ) {
            ColumnConfig cc = noSort(new ColumnConfig("key", "Content", 150));
            cc.setRenderer(Renderers.GRID_LINK_RENDERER);
            columns.add(cc);
        }
        
        // ============ ATTRIBUTE ============
        columns.add(noSort(new ColumnConfig("attribute", "Attribute", 150)));
        
        // ============ OLD VALUE ============
        columns.add(colourColumn( RED, noSort(new ColumnConfig("old", "Old Value", 200)) ));
        
        // ============ NEW VALUE ============
        columns.add(colourColumn( GREEN, noSort(new ColumnConfig("new", "New Value", 200)) ));

        
		final PagingToolBar toolBar = new PagingToolBar( 20 );
		toolBar.bind( loader );
		BorderLayoutData north = new BorderLayoutData(LayoutRegion.NORTH);
		north.setSize(28);
		add(toolBar, north);

		Grid<BaseModelData> grid = new Grid<BaseModelData>( store, new ColumnModel( columns ) );
		grid.setStripeRows( true );
		grid.setAutoExpandColumn( "new" );
		grid.addStyleName( "grid-cell-wrap" );
		grid.getView().setEmptyText( "No data to display." );
		
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
		
		for ( HistoryListener listener : historyListeners ) {
			listener.onHistoryLoaded();
		}		
	}
	
	public void addHistoryListener(HistoryListener listener) {
		if (!historyListeners.contains(listener)) {
			historyListeners.add(listener);
		}
	}
	public void removeHistoryListener(HistoryListener listener) {
		historyListeners.remove(listener);
	}

	public String getLabel() {
		return response != null ? response.getLabel() : "Change History";
	}
	
	public HtmlContainer createHeader() {
		return new HtmlContainer("<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\">"
				+ "<tbody><tr>" + "<td valign=\"bottom\">"
				+ "<h1 class=\"view-title\">" + getLabel() + "</h1>" + "</td>"
				+ "<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">"
				+ "<img width=\"75\" height=\"66\" src=\"img/banner_publish.gif\"/>" + "</td>" + "</tr>" + "</tbody></table>");		
	}
	
	static ColumnConfig noSort(ColumnConfig cc) {
        cc.setSortable(false);
        return cc;
    }
	
	public static final String	RED		= "#ffeeee;";
	public static final String	GREEN	= "#eeffee;";
	
	static ColumnConfig colourColumn(  String colour, ColumnConfig cc ) {
		cc.setStyle( "background-color:" + colour );
		return cc;
	}
	
}
