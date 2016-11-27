package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.freshdirect.cms.ui.client.views.AdministrationView;
import com.freshdirect.cms.ui.client.views.BulkLoaderView;
import com.freshdirect.cms.ui.client.views.ChangeSetQueryView;
import com.freshdirect.cms.ui.client.views.DraftOverviewView;
import com.freshdirect.cms.ui.client.views.FeedPublishView;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.client.views.PublishView;
import com.freshdirect.cms.ui.model.GwtUser;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;

public class MainLayout extends Viewport implements ValueChangeHandler<String> {

    private PageHeader header;

    private LayoutContainer mainPanel;

    private Anchor activeLink;

    protected ClickHandler commandLinkHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            Anchor link = (Anchor) event.getSource();
            activateLink(link);
        }
    };

    protected MainLayout() {
        super();

        this.setLayout(new BorderLayout());

        header = new PageHeader();
        masked = false;

        mainPanel = new LayoutContainer();
        mainPanel.setLayout(new CardLayout());
        mainPanel.addStyleName("white-background");

        // ============ layout ============

        BorderLayoutData pageHeaderData = new BorderLayoutData(LayoutRegion.NORTH);
        pageHeaderData.setSize(18);
        pageHeaderData.setCollapsible(false);
        pageHeaderData.setFloatable(false);
        pageHeaderData.setSplit(false);

        add(header, pageHeaderData); // north

        add(mainPanel, new BorderLayoutData(LayoutRegion.CENTER)); // center

        History.addValueChangeHandler(this);

    }

    protected void activateLink(Anchor link) {
        if (activeLink != null) {
            activeLink.removeStyleName("commandLink-active");
        }
        activeLink = link;
        activeLink.addStyleName("commandLink-active");
    }

    private static MainLayout sharedInstance = null;

    public static MainLayout getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new MainLayout();
        }
        return sharedInstance;
    }

    // ============ utility methods for status field and progress bar ================

    private MessageBox progressBar;
    private boolean masked;

    private Anchor manageStoreLink;
    private Anchor bulkLoadLink;
    private Anchor administrationLink;
    private Anchor publishLink;
    private Anchor changesLink;
    private Anchor feedPublishLink;
    private Anchor draftOverviewLink;

    /**
     * Display progress panel
     * 
     * @param title
     *            Message title
     * @param message
     *            Message
     * @param progressText
     *            Message text (?)
     */
    public void startProgress(String title, String message, String progressText) {
        stopProgress();
        progressBar = MessageBox.progress(title, message, progressText);
        progressBar.setModal(true);
        progressBar.getProgressBar().auto();
        progressBar.show();
    }

    public void stopProgress() {
        if (progressBar != null) {
            progressBar.close();
        }
    }

    // ============ ============ ================

    public LayoutContainer getMainPanel() {
        return mainPanel;
    }

    public void switchView(LayoutContainer view) {
        if (!mainPanel.getItems().contains(view)) {
            mainPanel.add(view, new BorderLayoutData(LayoutRegion.CENTER));
        }

        ((CardLayout) mainPanel.getLayout()).setActiveItem(view);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        GwtUser currentUser = CmsGwt.getCurrentUser();
        String historyToken = event.getValue();

        if (!isRendered()) {
            return;
        }

        if (historyToken == null || historyToken.length() == 0) {
            activateLink(manageStoreLink);
            switchView(ManageStoreView.getInstance());
            return;
        }

        if (historyToken.equals("manage-store")) {
            activateLink(manageStoreLink);
            switchView(ManageStoreView.getInstance());
            return;
        }

        if ("bulk-loader".equals(historyToken)) {
            if (currentUser != null && currentUser.isHasAccessToBulkLoaderTab()) {
                activateLink(bulkLoadLink);
                switchView(BulkLoaderView.getInstance());
            }
            return;
        }

        if ("administration".equals(historyToken)) {
            if (currentUser != null && currentUser.isHasAccessToAdminTab()) {
                activateLink(administrationLink);
                switchView(AdministrationView.getInstance());
            }
            return;
        }

        if ("publish".equals(historyToken)) {
            if (currentUser != null && currentUser.isHasAccessToPublishTab()) {
                activateLink(publishLink);
                switchView(PublishView.getInstance());
            }
            return;
        }

        if ("feed-publish".equals(historyToken)) {
            if (currentUser != null && currentUser.isHasAccessToFeedPublishTab()) {
                activateLink(feedPublishLink);
                switchView(FeedPublishView.getInstance());
            }
            return;
        }

        if ("changeset-query".equals(historyToken)) {
            activateLink(changesLink);
            switchView(ChangeSetQueryView.getInstance());
            return;
        }

        if ("draft-overview".equals(historyToken)) {
            if (currentUser != null && currentUser.isHasAccessToDraftBranches()) {
                activateLink(draftOverviewLink);
                switchView(DraftOverviewView.getInstance());
            }
            return;
        }

        activateLink(manageStoreLink);
        switchView(ManageStoreView.getInstance());

        if (historyToken.startsWith("search/")) {
            return;
        }

        int sep = historyToken.indexOf('$');
        if (sep != -1) {
            historyToken = historyToken.substring(sep + 1);
        }

        ManageStoreView.getInstance().loadNode(historyToken);

    }

    // =============== ACTION METHODS ===============

    /**
     * Add the necessary buttons, and widgets to the header panel.
     */
    public void userChanged() {
        GwtUser currentUser = CmsGwt.getCurrentUser();
        if (currentUser == null) {
            MessageBox.alert("Authentication error", "Couldn't authenticate user.", null);
            return;
        }

        if (currentUser.getLoginErrorMessage() != null) {
            MessageBox.alert("Draft authorization error", currentUser.getLoginErrorMessage(), null);
        }

        manageStoreLink = new Anchor("Manage Store", "#manage-store");
        manageStoreLink.addStyleName("commandLink");
        manageStoreLink.addClickHandler(commandLinkHandler);
        this.header.addToButtonPanel(manageStoreLink);

        if (currentUser.isHasAccessToBulkLoaderTab()) {
            bulkLoadLink = new Anchor("Bulk Load", "#bulk-loader");
            bulkLoadLink.addStyleName("commandLink");
            bulkLoadLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(bulkLoadLink);
        }

        if (currentUser.isHasAccessToChangesTab() && !currentUser.isDraftActive()) {
            changesLink = new Anchor("Changes", "#changeset-query");
            changesLink.addStyleName("commandLink");
            changesLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(changesLink);
        }

        if (currentUser.isHasAccessToPublishTab() && !currentUser.isDraftActive()) {
            publishLink = new Anchor("Publish", "#publish");
            publishLink.addStyleName("commandLink");
            publishLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(publishLink);
        }

        if (currentUser.isHasAccessToFeedPublishTab() && !currentUser.isDraftActive()) {
            feedPublishLink = new Anchor("Feed Publish", "#feed-publish");
            feedPublishLink.addStyleName("commandLink");
            feedPublishLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(feedPublishLink);
        }

        if (currentUser.isHasAccessToAdminTab() && !currentUser.isDraftActive()) {
            administrationLink = new Anchor("Administration", "#administration");
            administrationLink.addStyleName("commandLink");
            administrationLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(administrationLink);
        }

        if (currentUser.isHasAccessToDraftBranches() && currentUser.isDraftActive()) {
            draftOverviewLink = new Anchor("Draft Overview", "#draft-overview");
            draftOverviewLink.addStyleName("commandLink");
            draftOverviewLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(draftOverviewLink);
        }

        final String cmsAdminURL = currentUser.getCmsAdminURL();
        if (cmsAdminURL != null) {
            final Anchor cmsAdminLink = new Anchor("CMS Admin", cmsAdminURL);
            cmsAdminLink.addStyleName("commandLink");
            cmsAdminLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(cmsAdminLink);
        }

        Anchor hp = new Anchor("Logout", "logout.jsp");
        hp.addStyleName("logoutLink");
        this.header.addToButtonPanel(hp);

        StringBuilder userInfoBuilder = new StringBuilder(currentUser.getName());
        userInfoBuilder.append(" (");
        if (currentUser.getPersonaName() == null) {
            userInfoBuilder.append("No permissions");
        } else {
            userInfoBuilder.append(currentUser.getPersonaName());
        }
        userInfoBuilder.append(")");
        this.header.setUserInfo(userInfoBuilder.toString());

        this.header.setDraftName(currentUser.getDraftName());
        if (currentUser.isDraftActive()) {
            this.header.setDraftModeDisplay();
        } else {
            this.header.setMainModeDisplay();
        }
    }

    @Override
    public El mask(String message) {
        El mask = super.mask(message);
        masked = true;
        return mask;
    }

    @Override
    public void unmask() {
        super.unmask();
        masked = false;
    }

    @Override
    public boolean isMasked() {
        return masked;
    }

}
