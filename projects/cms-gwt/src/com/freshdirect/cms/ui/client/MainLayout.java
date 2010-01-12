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
		
		this.setLayout( new BorderLayout() );
		
		header = new PageHeader();
		masked = false;
		
		mainPanel = new LayoutContainer();
		mainPanel.setLayout(new CardLayout());
		mainPanel.addStyleName( "white-background" );
		
		// ============ layout ============
		
		BorderLayoutData pageHeaderData = new BorderLayoutData(LayoutRegion.NORTH);
		pageHeaderData.setSize(18);		
		pageHeaderData.setCollapsible(false);
		pageHeaderData.setFloatable(false);
		pageHeaderData.setSplit(false);
		
		add( header, pageHeaderData );	 // north
		
		add( mainPanel, new BorderLayoutData(LayoutRegion.CENTER) ); // center	
		
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

	private MessageBox	progressBar;
	private boolean masked;

	private Anchor manageStoreLink;
	private Anchor bulkLoadLink;
	private Anchor administrationLink;
	private Anchor publishLink;
	private Anchor changesLink;
	
    
    /**
     * Display progress panel
     * 
     * @param title Message title
     * @param message Message
     * @param progressText Message text (?)
     */
    public void startProgress(String title, String message, String progressText) {
        stopProgress();
		progressBar = MessageBox.progress( title, message, progressText );
		progressBar.setModal( true );
        progressBar.getProgressBar().auto();
        progressBar.show();
    }
    
    public void stopProgress() {
		if ( progressBar != null ) {
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
		
		((CardLayout)mainPanel.getLayout()).setActiveItem(view);
	}
    
    public void onValueChange(ValueChangeEvent<String> event) {    	
        String historyToken = event.getValue();
        if (!isRendered()) {
        	return;
        }

        if (historyToken == null || historyToken.length() == 0) {
        	activateLink(manageStoreLink);
        	switchView(ManageStoreView.getInstance());        	
        	return;
        }
        
        if(historyToken.equals("manage-store")) {
        	activateLink(manageStoreLink);
        	switchView(ManageStoreView.getInstance());
            return;
        }       
        
        if ("bulk-loader".equals(historyToken)) {
            GwtUser currentUser = CmsGwt.getCurrentUser();
            if (currentUser!=null && currentUser.isAllowedToWrite()) {
            	activateLink(bulkLoadLink);
            	switchView(BulkLoaderView.getInstance());
            }
            return;
        }
        if ("administration".equals(historyToken)) {
            GwtUser currentUser = CmsGwt.getCurrentUser();
            if (currentUser!=null && currentUser.isAdmin()) {
            	activateLink(administrationLink);
            	switchView(AdministrationView.getInstance());
            }
            return;
        }
        if ("publish".equals(historyToken)) {
            GwtUser currentUser = CmsGwt.getCurrentUser();
            if (currentUser!=null && currentUser.isAllowedToWrite()) {
            	activateLink(publishLink);
            	switchView(PublishView.getInstance());            	
            }
            return;
        }
		if ( "changeset-query".equals( historyToken ) ) {
			activateLink( changesLink );
			switchView( ChangeSetQueryView.getInstance() );
			return;
		}

        
        activateLink(manageStoreLink);
    	switchView(ManageStoreView.getInstance());
        
        if (historyToken.startsWith("search/")) { 
            return;
        }
        
        int sep = historyToken.indexOf('$');
        if (sep != -1) {
            String currentKey = historyToken.substring(sep + 1);
            ManageStoreView.getInstance().loadNode(currentKey, null);
        } else {
        	ManageStoreView.getInstance().loadNode(historyToken, null);
        }        
        
    }
        
    // =============== ACTION METHODS ===============        
        
    /**
     * Add the necessary buttons, and widgets to the header panel.
     */
    public void userChanged() {
        GwtUser currentUser = CmsGwt.getCurrentUser();
        if (currentUser == null) {
            MessageBox.alert( "Authentication error", "Couldn't authenticate user.", null );
            return;
        }
        
        manageStoreLink = new Anchor("Manage Store", "#manage-store");
        manageStoreLink.addStyleName("commandLink");
        manageStoreLink.addClickHandler(commandLinkHandler);        
        this.header.addToButtonPanel(manageStoreLink);        
        
        
        if (currentUser.isAllowedToWrite()) {
            bulkLoadLink = new Anchor("Bulk Load", "#bulk-loader");
            bulkLoadLink.addStyleName("commandLink");
            bulkLoadLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(bulkLoadLink);
        }
		
        changesLink = new Anchor( "Changes", "#changeset-query" );
		changesLink.addStyleName( "commandLink" );
		changesLink.addClickHandler( commandLinkHandler );
		this.header.addToButtonPanel( changesLink );    

        if (currentUser.isAllowedToWrite()) {
            publishLink = new Anchor("Publish", "#publish");
            publishLink.addStyleName("commandLink");
            publishLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(publishLink);
        }
        
		if (currentUser.isAdmin()) {
            administrationLink = new Anchor("Administration", "#administration");
            administrationLink.addStyleName("commandLink");
            administrationLink.addClickHandler(commandLinkHandler);
            this.header.addToButtonPanel(administrationLink);
        }
       
        Anchor hp = new Anchor("Logout", "logout.jsp");
        hp.addStyleName("logoutLink");
        this.header.addToButtonPanel(hp);
        
        StringBuilder s = new StringBuilder().append(currentUser.getName());
        if (currentUser.isAdmin() || currentUser.isAllowedToWrite()) {
            s.append(" (");
            if (currentUser.isAllowedToWrite()) {
                s.append("editor");
            }
            if (currentUser.isAllowedToWrite() && currentUser.isAdmin()) {
                s.append(' ');
            }
            if (currentUser.isAdmin()) {
                s.append("admin");
            }
            s.append(')');
        }
        header.setUserInfo(s.toString());
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

	public boolean isMasked() {		
		return masked;
	}
}
