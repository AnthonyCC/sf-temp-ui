package com.freshdirect.cms.ui.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.NodeTree;
import com.freshdirect.cms.ui.client.publish.ChangeHistoryPopUp;
import com.freshdirect.cms.ui.client.publish.PublishHistoryPanel;
import com.freshdirect.cms.ui.client.treetable.EditorTree;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Hyperlink;

public class MainLayout extends Viewport implements ValueChangeHandler<String> {
	private PageHeader header;
	private ContentPanel mainPanel;
	private NodeTree treePanel;
	private SimpleComboBox<String> contextDropdown;
	private ToolBar contentToolBar;
	
	private GwtNodeData currentNode = null;
	
 
	public MainLayout() {
		super();	
		
		this.setLayout( new BorderLayout() );
		
		header = new PageHeader();
		
		mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		mainPanel.setBorders(false);
		mainPanel.setBodyBorder(false);
		mainPanel.setScrollMode(Scroll.AUTO);	
//		mainPanel.setLayout( new FitLayout() );
		
		mainPanel.addListener(Events.AfterLayout, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				mainPanel.enable();
			}			
		});

		
		// ============ node tree ============
		
		treePanel = new NodeTree();

                treePanel.addNodeSelectListener(new NodeTree.NodeSelectListener() {
                    @Override
                    public void nodeSelected(final ContentNodeModel node) {
                        final ContentNodeModel parent = treePanel.getSelectedParent();
                        if (node != null) {
                            MainLayout.this.nodeSelected(node.getKey(), parent);
                        }
                    };
                });		
		
		// ============ extra buttons in node tree header ============
		
		// hide tree panel's header
		treePanel.setHeaderVisible( false );
		
		// TODO Bulk editor is disabled for now
		
//		ToolButton bulkButton = new ToolButton("bulk-edit", new SelectionListener<IconButtonEvent>() {
//		    @Override public void componentSelected( IconButtonEvent ce ) {
//				String key = "";
//				if ( treePanel.getSelectedItem() != null ) {
//					key = treePanel.getSelectedItem().getKey();
//				}
//				History.newItem("bulk/" + key);
//		    }
//		});
//		bulkButton.setToolTip( new ToolTipConfig("Bulk Editor", "blah blah blah") );  
//		treePanel.getHeader().addTool( bulkButton );
		
		
		// ============ status bar ============		
		
		ToolBar statusToolBar = new ToolBar();
		statusField = new Status();
		statusField.setAutoWidth( true );		
		statusField.setText( "Welcome to CMS-GWT!" );		
		statusToolBar.add( statusField );
		
		statusToolBar.add( new FillToolItem() );
		
//		progressBar = new ProgressBar();	
//		progressBar.setWidth( 200 );		
//		statusToolBar.add( progressBar );
		
		mainPanel.setBottomComponent( statusToolBar );
		
		
		// ============ layout ============
		
		BorderLayoutData pageHeaderData = new BorderLayoutData(LayoutRegion.NORTH);
		pageHeaderData.setSize(30);
		
		BorderLayoutData treeLayoutData = new BorderLayoutData(LayoutRegion.WEST);
		treeLayoutData.setCollapsible(true);
		treeLayoutData.setSplit(true);
		treeLayoutData.setSize( 300 );
		treeLayoutData.setMinSize( 100 );
		treeLayoutData.setMaxSize( 500 );
		treeLayoutData.setMargins( new Margins( 0, 5, 0, 0 ) );

		
		add( header, pageHeaderData );		
		add( treePanel, treeLayoutData );
		add( mainPanel, new BorderLayoutData(LayoutRegion.CENTER) );
		
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
	}


	
	// ============ utility methods for status field and progress bar ================

	private static Status		statusField;
	private static MessageBox	progressBar;
    
    public static void setStatus( String msg ) {
    	statusField.setText( msg );
    }    
    
    public static void startProgress(String title, String message, String progressText) {
        stopProgress();
        progressBar = MessageBox.progress(title, message, progressText);
        // if ( progressBar == null ) {
        //      progressBar = MessageBox.progress( title, message, progressText );
        // } else {
        //      progressBar.setTitle( title );
        //      progressBar.setMessage( message );
        //      progressBar.setProgressText( progressText );
        // }
        progressBar.setModal(true);
        progressBar.getProgressBar().auto();
        progressBar.show();
    }
    
    public static void stopProgress() {
		if ( progressBar != null ) {
			progressBar.close();
		}
	}

	// ============ ============ ================
    
	
	
	
	public void loadBulkEdit(String nodeKey) {
		ContentNodeModel selected = null;
		
		if (!nodeKey.equals("")) {
			selected = new ContentNodeModel("", "", nodeKey);
		}
		
		mainPanel.removeAll();				
		ToolBar tb = new ToolBar();
		
		Button saveButton =
		new Button("Save", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				mainPanel.removeAll();
				treePanel.deselectAll();
				History.newItem(null);
			}
		});
		saveButton.addStyleName( "save-button-text" );
		tb.add(saveButton);
		
		mainPanel.add(tb);
		ContentPanel ec = new ContentPanel();
		ec.setHeading("Bulk edit");
		ec.add(new EditorTree(selected));						
		mainPanel.add(ec);
		mainPanel.layout();
	}
	
	

    /**
     * Initiates downloading the given content node.
     * 	
     * @param nodeKey
     */
    public void loadNode(final String nodeKey) {
        setStatus( "Loading " + nodeKey + " ... " );
        startProgress( "Load", "Loading " + nodeKey,  "loading..." );

        CmsGwt.getContentService().getNodeData( nodeKey, new BaseCallback<GwtNodeData>() {

            @Override
            public void errorOccured(Throwable error) {
                setStatus( "Loading failed." );
                stopProgress();
            }

            public void onSuccess( GwtNodeData currentNodeData ) {
                mainPanel.disable();
                mainPanel.removeAll();
                
            	currentNode = currentNodeData;
                WorkingSet.clear();
            	
                final HtmlContainer contentHeader = new HtmlContainer(currentNode.getHeaderMarkup());

                contentToolBar = new ToolBar();
                contentToolBar.addStyleName("main-toolbar");                
                                
                // ============ context dropdown ============
                GwtNodeContext ctx = currentNode.getContexts();
                
                contextDropdown = new SimpleComboBox<String>();
                contextDropdown.addStyleName("context-dropdown");
                final List<String> contextsList = new ArrayList<String>(ctx.size());
                
                for (String path : ctx.getPaths()) {
                    contextDropdown.add(ctx.getLabel(path));
                    contextsList.add(path);
                }
                
                contextDropdown.setEmptyText("Select active context");
                
                contextDropdown.setEditable(false);
                contextDropdown.setForceSelection(true);
				
				//FIXME context dropdown width
//                contextDropdown.setAutoWidth( true );
                //contextDropdown.setWidth(500);                
                
                contextDropdown.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
                        int selectedIndex = contextDropdown.getSelectedIndex();
                        if (selectedIndex == -1) {
                            return;
                        }
                        String path = contextsList.get(selectedIndex);
                        contextChangeAction(path);
                    }
                });
                
                contentToolBar.add( contextDropdown );
                
                
                ToolButton synchronizeButton = new ToolButton("x-tool-refresh");                
                synchronizeButton.setToolTip( new ToolTipConfig("Synchronize", "Synchronize the node tree with the selected path.") );  
                synchronizeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        int index = contextDropdown.getSelectedIndex();
                        if (index == -1) {
                            MessageBox.alert("Info", "First select a context, then push the synchronize button to navigate in the tree.", null);
                        } else {
                            String path = contextsList.get(index);
                            treePanel.synchronize(path);
                        }
                    };
                });
                contentToolBar.add(synchronizeButton);

                // context ...
                int index = -1;
                String contextPath = treePanel.getSelectedPath();

                if (contextPath != null)
                    index = contextsList.indexOf(contextPath);

                if (index == -1) {
                    contextPath = currentNodeData.getDefaultContextPath();
                }

                if (contextPath != null) {
                    index = contextsList.indexOf(contextPath);
                }
                if (index != -1) {
                    contextDropdown.setFireChangeEventOnSetValue(false);
                    contextDropdown.setValue(contextDropdown.getStore().getAt(index));
                    // contextDropdown.setFireChangeEventOnSetValue( true );
                }
        		
                // ============ save & discard buttons ============ 
                contentToolBar.add( new FillToolItem() );

                Button viewHistoryButton = new Button("View History", new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        viewHistory();
                    }

                });
                contentToolBar.add(viewHistoryButton);
                
                if (!currentNodeData.isReadonly()) {
                    Button saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            saveAction();
                        }
                    });
                    saveButton.setToolTip( new ToolTipConfig( "Save", "Saves the workset." ) );
                    saveButton.addStyleName( "save-button-text" );
                    contentToolBar.add( saveButton ); 
                    
                    Button discardButton = new Button("Discard", new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            nodeSelected(currentNode.getNode().getKey(), null);
                        }
                    });
                    discardButton.setToolTip( new ToolTipConfig( "Discard", "Discard the changes in this workset." ) );
                    discardButton.addStyleName( "discard-button-text" );
                    contentToolBar.add( discardButton ); 
                }
  
                mainPanel.add(contentToolBar);
                mainPanel.add(contentHeader);

                currentNodeData.setupUI( mainPanel, contextPath );

                mainPanel.layout();
                
                setStatus( "Loaded successfully." );
                stopProgress();
            }
        });
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String historyToken = event.getValue();

        if (historyToken == null || historyToken.length() == 0) {
            return;
        }
        if ("administration".equals(historyToken)) {
            GwtUser currentUser = CmsGwt.getCurrentUser();
            if (currentUser!=null && currentUser.isAdmin()) {
                AdminWindow aw = new AdminWindow(null);
                aw.show();
            }
            // this is needed, to allow opening up the admin window multiple times
            History.newItem(null);
            return;
        }
        if ("publish".equals(historyToken)) {
            GwtUser currentUser = CmsGwt.getCurrentUser();
            if (currentUser!=null && currentUser.isAllowedToWrite()) {
                showPublishPanel();
            }
            return;
        }

        if (historyToken.startsWith("bulk/")) {
            loadBulkEdit(historyToken.substring(5));
            return;
        }
        if (historyToken.startsWith("search/")) { 
        	setStatus( "Searching..." );
        	treePanel.search(historyToken.substring(7));
            return;
        }
        int sep = historyToken.indexOf('$');
        if (sep != -1) {
            //String parentKey = historyToken.substring(0, sep);
            String currentKey = historyToken.substring(sep + 1);
            loadNode(currentKey);
        } else {
            loadNode(historyToken);
        }
    }

    
    // =============== ACTION METHODS =============== 
    
    
    private void viewHistory() {
        if (currentNode != null) {
            setStatus("Loading history...");
            startProgress("Load", "Loading history of " + currentNode.getNode().getKey(), "loading...");
            final String label = currentNode.getNode().getLabel() + " " + currentNode.getNode().getType() + " [" + currentNode.getNode().getKey() + "]";

            CmsGwt.getContentService().getChangeSets(new ChangeSetQuery().setByKey(currentNode.getNode().getKey()),
                    new BaseCallback<ChangeSetQueryResponse>() {
                        @Override
                        public void errorOccured(Throwable error) {
                            setStatus("Error loading history.");
                            stopProgress();
                        }

                        public void onSuccess(ChangeSetQueryResponse result) {
                            setStatus("History loaded successfully.");
                            stopProgress();
                            ChangeHistoryPopUp cp = new ChangeHistoryPopUp(result, label);
                            cp.show();
                        }
                    });
        }
    }
    
    private void showPublishPanel() {
    	//FIXME quiet discard???
        discardAction();
        startProgress( "Load", "Loading publish history",  "loading..." );
        
        CmsGwt.getContentService().getPublishHistory(
            new BaseCallback<List<GwtPublishData>>() {
                @Override
                public void onSuccess(List<GwtPublishData> result) {
                    stopProgress();
                    PublishHistoryPanel php = new PublishHistoryPanel(result);
                    mainPanel.add(php);
                    mainPanel.layout();
                    setStatus("Publish History loaded.");
                }

                @Override
                public void errorOccured(Throwable error) {
                    stopProgress();
                    setStatus("Error loading publish history.");
                }
            });
    }

    
    private void saveAction() {
    	setStatus( "Saving ..." );
    	startProgress( "Save", "Saving changes.", "saving..." );

    	// new nodes are already in the workingset    	
        if ( currentNode != null ) {
        	// add this node
            WorkingSet.add( currentNode.getNode() );
            
            // add any nodes that were changed because of VariationMatrix or CustomGrid components 
            currentNode.collectValuesFromFields();
            
            // TODO check this functionality : collectValuesFromFields does change the currentnode, and has some side effects ...
            // important thing is : adds changed nodes to the workingset!
        }
        
        CmsGwt.getContentService().save( WorkingSet.getWorkingSet(), new BaseCallback<GwtSaveResponse>() {
            public void onSuccess(GwtSaveResponse result) {
                if (result.isOk()) {
                    WorkingSet.clear();
                    History.newItem(null);
                    mainPanel.removeAll();
                    currentNode = null;
                    
                    if (result.getChangeSet() != null) {
                        ChangeHistoryPopUp cp = new ChangeHistoryPopUp(new ChangeSetQueryResponse(result.getChangeSet()), "saved nodes");
                        cp.show();
                    }
                    mainPanel.layout();

                    
                    //nodeTree.getSelectionModel().deselectAll();
                    setStatus( "Saved succesfully." );
                    stopProgress();
                    MessageBox.info("Save", "Successful. Saved with id:" + result.getChangesetId(), null);
                    
                } else {
                	
                    StringBuilder s = new StringBuilder("Error:");
                    for (GwtValidationError g : result.getValidationMessages()) {
                        s.append(g.getHumanReadable());
                    }
                    setStatus( "Save failed : validation errors." );
                    stopProgress();
                    MessageBox.alert("Validation Errors", s.toString(), null);
                }
            }
            @Override
            public void errorOccured(Throwable error) {
                setStatus( "Save failed." );
                stopProgress();
            }
        });
    }
    
    private void discardAction() {
        setStatus("Changes were discarded.");
        WorkingSet.clear();
        History.newItem(null);
        mainPanel.removeAll();
    }
    
    @SuppressWarnings( "unchecked" )
	private void contextChangeAction(String ctxPath) {
        setStatus("Context changed to : " + currentNode.getContexts().getLabel(ctxPath));

        Map<String, ContentNodeAttributeI> attributes = currentNode.getNode().getOriginalAttributes();
        Map<String, ContentNodeAttributeI> inheritedAttrs = currentNode.getContexts().getInheritedAttributes(ctxPath);

        if (inheritedAttrs == null || attributes == null) {
            return;
        }

        for (String key : attributes.keySet()) {
            ContentNodeAttributeI attribute = attributes.get(key);
            Field<Serializable> fieldObject = (Field<Serializable>) attribute.getFieldObject();

            if (fieldObject != null && fieldObject instanceof InheritanceField && attribute.isInheritable() && inheritedAttrs.containsKey(key)) {
                InheritanceField<Serializable> inheritanceField = (InheritanceField<Serializable>) fieldObject;

                ContentNodeAttributeI inhAttr = inheritedAttrs.get(key);
                if (inhAttr != null && inheritanceField.isOverrideValue()) {

                    inheritanceField.setInheritedValue(inhAttr.getValue());
                }
            }
        }
    }

    void openNode(String key, final ContentNodeModel parent) {
        StringBuilder command = new StringBuilder(64);
        if (parent != null) {
            command.append(parent.getKey());
            command.append('$');
        }
        command.append(key);
        String c = command.toString();
        if (c.equals(History.getToken())) {
            History.fireCurrentHistoryState();
        } else {
            History.newItem(c);
        }
    }
    
    protected boolean isFormChanged() {
        return (currentNode != null) && !currentNode.isReadonly() && currentNode.isChanged();
    }

    /**
     * Add the necessary buttons, and widgets to the header panel.
     */
    public void userChanged() {
        GwtUser currentUser = CmsGwt.getCurrentUser();
        if (currentUser == null) {
            // TODO error handling?
            return;
        }
        if (currentUser.isAdmin()) {
            Hyperlink hp = new Hyperlink("Administration", "administration");
            hp.addStyleName("commandLink");
            this.header.addToButtonPanel(hp);
        }
        
        if (currentUser.isAllowedToWrite()) {
            Hyperlink hp = new Hyperlink("Publish", "publish");
            hp.addStyleName("commandLink");
            this.header.addToButtonPanel(hp);
        }
        
        Anchor hp = new Anchor("Logout", "logout.jsp");
        hp.addStyleName("commandLink");
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

    protected void nodeSelected(final String key, final ContentNodeModel parent) {
        if (key != null) {
            if (isFormChanged()) {
                MessageBox.confirm("Discard changes", "You have unsaved changes. Do you want to discard them?", new Listener<MessageBoxEvent>() {
                    public void handleEvent(MessageBoxEvent be) {
                        if (be.getButtonClicked().getText().toLowerCase().trim().equals("yes")) {
                            openNode(key, parent);
                        }
                    };
                });
            } else {
                openNode(key, parent);
            }
        }
    }
    
}
