package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class PageHeader extends LayoutContainer {
	
	private HtmlContainer headerMarkup;	
	
	PageHeader() {
		headerMarkup = new HtmlContainer("<div style=\"background-color: black;color: #CCCCCC;padding-top: 2px;padding-bottom: 2px;padding-left: 5px\">" +				
				"<span style=\"color: rgb(255, 153, 51);font-weight: bold\">Fresh</span>" +
				"<span style=\"color: rgb(102, 153, 51);font-weight: bold\">Direct</span>" +
				" - Content Management System</div>");
		
		headerMarkup.setHeight(19);
		
		RowLayout layout = new RowLayout(Orientation.HORIZONTAL);		
		setLayout(layout);
		setBorders(false);		
		setHeight(19);

		add(headerMarkup, new RowData(1, 19));
	}

}
