package com.freshdirect.cms.ui.client.changehistory;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

public class ViewHistory extends Viewport implements EntryPoint, ValueChangeHandler<String>, HistoryListener {	
	
	ActionBar actionBar;
	ChangeHistory history;
	
	@Override
	public void onModuleLoad() {		
		if (RootPanel.get("viewhistory-indicator") == null) {
    		return;
    	}

		RootPanel.get().add(this);
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
		
		actionBar = new ActionBar();
		actionBar.addButton(new Button("Close", new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				close();
			}
		}));		
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String evtVal = event.getValue();
		String contentKey = evtVal.indexOf( '$' ) != -1 ? evtVal.substring( evtVal.lastIndexOf( '$' ) + 1, evtVal.length() ) : evtVal;
		
		ChangeSetQuery query = new ChangeSetQuery();
		query.setContentKey( contentKey );
		history = new ChangeHistory( query );		
		history.addHistoryListener( this );
	}
	
	@Override
	public void onHistoryLoaded() {
		
		setWindowTitle( "FreshDirect - " + history.getLabel() );
		
		removeAll();
		
		setLayout( new BorderLayout() );
		
		LayoutContainer header = new LayoutContainer( new FlowLayout() );
		header.add( history.createHeader() );
		header.add( actionBar );
		
		AbsoluteLayout wrapLayout = new AbsoluteLayout();
		LayoutContainer wrap = new LayoutContainer( wrapLayout );
		wrap.add( history, new AnchorData( "100% 100%" ) );
		wrapLayout.setPosition( history, 0, 0 );
		
		addStyleName( "white-background" );
		BorderLayoutData d = new BorderLayoutData( LayoutRegion.NORTH );
		// TODO should use DetailPanel ...?
		d.setSize( 107 );
		add( header, d );
		add( wrap, new BorderLayoutData( LayoutRegion.CENTER ) );
		
		layout();
	} 
		
	native public static void close()/*-{  
		$wnd.close();
	}-*/;

	native public static void setWindowTitle( String title )/*-{  
		$doc.title = title;
	}-*/;
}