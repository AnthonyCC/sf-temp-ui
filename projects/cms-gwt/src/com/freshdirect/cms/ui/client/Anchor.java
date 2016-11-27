package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.Html;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class Anchor extends Html {
	
	private String	text;
	private String	url;
	private boolean	newWindow	= false;
	
	public Anchor( String text, String url ) {
		super();
		this.text = text;
		this.url = url;		
	}

	public Anchor( String text ) {
		super();
		this.text = text;	
	}
	
	public void setNewWindow( boolean newWindow ) {
		this.newWindow = newWindow;
	}
	
	public void setUrl( String url ) {
		this.url = url;
		if ( url != null ) {
			getElement().setAttribute( "href", url );
		}
	}
	
	@Override
	protected void onRender( Element target, int index ) {
		setTagName( "a" );
		super.onRender( target, index );
		if ( text != null ) {
			getElement().setInnerHTML( text );
		}

		if ( url != null ) {
			getElement().setAttribute( "href", url );
			if ( newWindow ) {
				getElement().setAttribute( "target", "_blank" );
			}
		}

		if ( getFocusEl() != null ) {
			getFocusEl().addEventsSunk( Event.FOCUSEVENTS );
		}

		sinkEvents( Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS );
		addStyleName( "x-anchor-btn" );
		onAttach();
	}
		
}
