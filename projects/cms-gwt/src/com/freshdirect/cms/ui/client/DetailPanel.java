package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DetailPanel extends LayoutContainer {

	private LayoutContainer header;
	private LayoutContainer body;
	
	public DetailPanel() {
		super();
		setLayout(new BorderLayout());
		addStyleName( "white-background" );
	}	
	
	public LayoutContainer getHeader() {
		return header;
	}

	public LayoutContainer getBody() {
		return body;
	}

	public void setHeader( HtmlContainer htmlTitle ) {
	    LayoutContainer head = new LayoutContainer(new AnchorLayout());
	    head.add(htmlTitle,new AnchorData("100%"));
		setHeader(head);
	}
	
	public void setHeader( LayoutContainer header ) {
		LayoutContainer wrapper = new LayoutContainer(new FitLayout());
		wrapper.add(header);
		wrapper.addStyleName("dp-north-wrapper");
		BorderLayoutData north = new BorderLayoutData(LayoutRegion.NORTH);
		north.setCollapsible(false);
		north.setFloatable(false);
		north.setSize(107);  // HTML markup and Action Bar size
		north.setSplit(false);
		add(wrapper, north );
		this.header = header;
	}

	public void setBody( LayoutContainer body ) {
		LayoutContainer wrapper = new LayoutContainer();
		wrapper.add(body);
		wrapper.setLayout(new FitLayout());
		wrapper.setScrollMode(Scroll.AUTO);
		add(wrapper, new BorderLayoutData(LayoutRegion.CENTER));
		this.body = body;
	}

}
