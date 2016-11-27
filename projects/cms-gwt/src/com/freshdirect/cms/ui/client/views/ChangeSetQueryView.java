package com.freshdirect.cms.ui.client.views;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.changehistory.ChangeHistory;
import com.freshdirect.cms.ui.client.changehistory.ChangeSetQueryForm;
import com.freshdirect.cms.ui.client.changehistory.HistoryListener;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;


public class ChangeSetQueryView extends LayoutContainer implements HistoryListener {
	
    private static ChangeSetQueryView instance = null; 
    public static ChangeSetQueryView getInstance() {
    	if ( instance == null ) 
    		instance = new ChangeSetQueryView();
    	return instance;
    }    

    private ChangeSetQueryForm queryForm; 
    private ChangeHistory history;
    private LayoutContainer content;
    
	public ChangeSetQueryView() {
		super( new BorderLayout() );

		queryForm = ChangeSetQueryForm.getInstance();
		add( queryForm, new BorderLayoutData( LayoutRegion.WEST, 335 ) );
	}
	
	public void query( ChangeSetQuery query ) {
		
		if ( content != null )
			remove( content );
		
		MainLayout.getInstance().startProgress( "Query", "Query running ... ", "" );
		history = new ChangeHistory( query );
		history.addHistoryListener( this );		
	}


	@Override
	public void onHistoryLoaded() {		
		
		LayoutContainer header = new LayoutContainer( new FlowLayout() );
		header.add( history.createHeader() );
		header.add( new ActionBar() );
		
		AbsoluteLayout wrapLayout = new AbsoluteLayout();
		LayoutContainer wrap = new LayoutContainer( wrapLayout );
		wrap.add( history, new AnchorData( "100% 100%" ) );
		wrapLayout.setPosition( history, 0, 0 );
		
		content = new LayoutContainer( new BorderLayout() );
		content.addStyleName( "white-background" );
		BorderLayoutData d = new BorderLayoutData( LayoutRegion.NORTH );
		// TODO should use DetailPanel ...?
		d.setSize( 107 );
		content.add( header, d );
		content.add( wrap, new BorderLayoutData( LayoutRegion.CENTER ) );
		
		add( content, new BorderLayoutData( LayoutRegion.CENTER ) );
		layout();
		
		MainLayout.getInstance().stopProgress();
	}
	
}
