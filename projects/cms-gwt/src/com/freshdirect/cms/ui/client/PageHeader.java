package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.freshdirect.cms.ui.client.publish.PublishProgressListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PageHeader extends LayoutContainer implements PublishProgressListener {

    private static final String STORE_PUBLISH_IS_IN_PROGRESS = "Store publish is in progress";

    private HorizontalPanel buttonPanel;
    private HtmlContainer headerMarkup;

    public PageHeader() {
        super(new FillLayout());

        headerMarkup = new HtmlContainer("<div class=\"appHeader\">" + "<div class=\"freshLabel\">Fresh</div>" + "<div class=\"directLabel\">Direct</div>"
                + "<div class=\"normalLabel\"> - CMS Editor </div>" + "<div class=\"userLabel\" id=\"userInfo\">unknown</div>"
                + "<div class=\"normalLabel\" id=\"draftInfo\"></div>" + "<div class=\"normalLabel\" id=\"publishInfo\"></div>" + "<div id=\"buttonPanel\">BUTTONS</div></div>");

        buttonPanel = new HorizontalPanel();
        buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        headerMarkup.add(buttonPanel, "#buttonPanel");
        add(headerMarkup);

        setHeight(30);
    }

    public void setDraftModeDisplay() {
        this.setStyleName("appHeaderDraft");
    }

    public void setMainModeDisplay() {
        this.setStyleName("");
    }

    public void addToButtonPanel(Widget widget) {
        // remember to adjust cmsgwt.css #buttonPanel { width } rule, if for too many buttons doesn't have enough room.
        buttonPanel.add(widget);
        // buttonPanel.layout(true);
    }

    public void clearButtonPanel() {
        buttonPanel.clear();
    }

    public void setDraftName(String draftName) {
        headerMarkup.add(new Label("Branch - " + draftName), "#draftInfo");
    }

    public void setUserInfo(String userInfo) {
        headerMarkup.add(new Label(" " + userInfo), "#userInfo");
    }

    public void setPublishInfo(String publishInfo) {
        headerMarkup.add(new Label(" " + publishInfo), "#publishInfo");
    }

    @Override
    public void onPublishStarted() {
        setPublishInfo(STORE_PUBLISH_IS_IN_PROGRESS);
    }

    @Override
    public void onPublishFinished() {
        setPublishInfo("");
    }
}
