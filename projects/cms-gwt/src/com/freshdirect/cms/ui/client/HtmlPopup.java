package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class HtmlPopup extends Window {

	public HtmlPopup( String title, String content ) {		
		setLayout( new FitLayout() );
		setMaximizable( true );
		setHeading( title );
		setScrollMode( Scroll.AUTO );
		setWidth( 600 );
		setHeight( 400 );
		add( new HtmlContainer( content ) );
		addButton( new Button( "Close", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected( ButtonEvent ce ) {
				HtmlPopup.this.hide();
			}
		} ) );
	}
}
