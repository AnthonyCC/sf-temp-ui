package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;

public class PageHeader extends LayoutContainer {

    private HtmlContainer headerMarkup;
    LayoutContainer buttonPanel;

    PageHeader() {
        super(new FillLayout());
        headerMarkup = new HtmlContainer("<div class=\"appHeader\">" 
                + "<div class=\"freshLabel\">Fresh</div>" 
                + "<div class=\"directLabel\">Direct</div>"
                + "<div class=\"normalLabel\"> - Content Management System </div>" 
                + "<div id=\"buttonPanel\">BUTTONS</div></div>");

        buttonPanel = new LayoutContainer(new RowLayout(Orientation.HORIZONTAL));

        headerMarkup.add(buttonPanel, "#buttonPanel");

        add(headerMarkup);
        setHeight(30);
        
        addToButtonPanel(new Button("IZe"));
    }

    public void addToButtonPanel(Widget widget) {
        buttonPanel.add(widget);
        buttonPanel.layout(true);
        
    }
    
    public void clearButtonPanel() {
        buttonPanel.removeAll();
    }

}
