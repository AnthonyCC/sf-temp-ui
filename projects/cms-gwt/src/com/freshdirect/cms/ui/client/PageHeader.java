package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Label;
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
                + "<div class=\"userLabel\" id=\"userInfo\">unknown</div>"
                + "<div id=\"buttonPanel\">BUTTONS</div></div>");

        buttonPanel = new LayoutContainer(new RowLayout(Orientation.HORIZONTAL));
        buttonPanel.setHeight(22);

        headerMarkup.add(buttonPanel, "#buttonPanel");

        add(headerMarkup);
        setHeight(30);
    }

    
    public void addToButtonPanel(Widget widget) {
        // remember to adjust cmsgwt.css #buttonPanel { width } rule, if for too many buttons doesn't have enough room. 
        buttonPanel.add(widget);
        buttonPanel.layout(true);
    }
    
    public void clearButtonPanel() {
        buttonPanel.removeAll();
    }

    public void setUserInfo(String txt) {
        headerMarkup.add(new Label(" "+txt), "#userInfo");
    }
    
}
