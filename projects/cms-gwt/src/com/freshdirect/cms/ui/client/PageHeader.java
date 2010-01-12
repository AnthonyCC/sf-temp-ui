package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PageHeader extends LayoutContainer {

    private HtmlContainer headerMarkup;
    HorizontalPanel buttonPanel;

    PageHeader() {
        super(new FillLayout());
        headerMarkup = new HtmlContainer("<div class=\"appHeader\">" 
                + "<div class=\"freshLabel\">Fresh</div>" 
                + "<div class=\"directLabel\">Direct</div>"
                + "<div class=\"normalLabel\"> - Content Management System </div>" 
                + "<div class=\"userLabel\" id=\"userInfo\">unknown</div>"
                + "<div id=\"buttonPanel\">BUTTONS</div></div>");

        buttonPanel = new HorizontalPanel();
        buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        
        headerMarkup.add(buttonPanel, "#buttonPanel");

        add(headerMarkup);
        setHeight(30);
    }

    
    public void addToButtonPanel(Widget widget) {
        // remember to adjust cmsgwt.css #buttonPanel { width } rule, if for too many buttons doesn't have enough room. 
        buttonPanel.add(widget);
        //buttonPanel.layout(true);
    }
    
    public void clearButtonPanel() {
        buttonPanel.clear();
    }

    public void setUserInfo(String txt) {
        headerMarkup.add(new Label(" "+txt), "#userInfo");
    }
    
}
