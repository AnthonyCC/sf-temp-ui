package com.freshdirect.cms.ui.client.views;

import java.io.Serializable;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.NodeEditorMode;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.action.GetNodeAction;
import com.freshdirect.cms.ui.client.changehistory.ChangesetView;
import com.freshdirect.cms.ui.client.contenteditor.ContentEditorPanel;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ManageStoreView extends LayoutContainer {

	private static ManageStoreView instance = null;
	
	public static ManageStoreView getInstance() {
		if( instance == null) {
			instance = new ManageStoreView();
		}
		return instance;
	}
	
	
	// ============ utility methods for status field and progress bar ================

	private MessageBox	progressBar;
	private boolean masked;
	private Map<String, Serializable> comparisonResult;
	private Container<?> detailPanel;
	private NodeTree treePanel;
	private NodeEditorMode editorMode;
	private GwtNodeData currentNode = null;	
	private GwtNodeData comparedNode = null;
	
	// temporary solution for storing actual context path  
	private String contextPath = null; 


	/**
	 * FDX - active Store ID
	 * Can be null if selected root is actually not a Store node
	 */
	private String storeKey;

	protected ManageStoreView() {
		super();	
		
		this.setLayout( new BorderLayout() );
		
		setBorders(false);
		
		// ============ node tree ============		
		treePanel = new NodeTree();
        treePanel.addNodeSelectListener(new NodeTree.NodeSelectListener() {
            @Override
            public void nodeSelected(final TreeContentNodeModel node) {
            	
                String path = treePanel.getSelectedPath();
                CmsGwt.log( "selected path : " + path );
                // remember last ctx path
                contextPath = path;
                
                if (node != null) {
                    ManageStoreView.getInstance().nodeSelected(node.getKey(), treePanel.getSelectedParent(), true);
                }
            }
        });				
		
		BorderLayoutData treeLayoutData = new BorderLayoutData(LayoutRegion.WEST, 300);
		treeLayoutData.setCollapsible(true);
		treeLayoutData.setSplit(true);

		treeLayoutData.setMinSize( 100 );
		treeLayoutData.setMaxSize( 500 );
		treeLayoutData.setMargins( new Margins( 0, 5, 0, 0 ) );
		
		add( treePanel, treeLayoutData ); // west			
		
	}

    
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


    /**
     * Initiates downloading the given content node.
     * 	
     * @param nodeKey
     */
	public void loadNode( final String nodeKey ) {
		
		// clear last ctx path
		String path = contextPath;
		contextPath = null;
		
		CmsGwt.getContentService().loadNodeData( nodeKey, new GetNodeAction( nodeKey, path ) );
	}

    public void scrollHack() {
    	treePanel.scrollHack();
    }
    
    public NodeTree getMainTree() {
    	return treePanel;
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
        return (currentNode != null) && currentNode.getPermission().isEditable() && currentNode.isChanged();
    }

	public void nodeSelected( final String key, final ContentNodeModel parent, boolean dirtyWarning ) {
		if ( key != null ) {
            if ( dirtyWarning && ( !WorkingSet.isEmpty() || isFormChanged() ) ) {
                MessageBox.confirm("Discard changes", "You have unsaved changes. Do you want to discard them?", new Listener<MessageBoxEvent>() {
                    @Override
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
	
	/**
	 * Determines whether node is already edited / changed
	 * 
	 * @return the state of dirtiness
	 */
	public boolean isNodeEditorDirty() {
		return !WorkingSet.isEmpty() || isFormChanged();
	}

	public GwtNodeData getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(GwtNodeData currentNode) {
		this.currentNode = null;
		this.currentNode = currentNode;
	}
	
	public NodeTree getTreePanel() {
		return treePanel;
	}

	protected void cleanup() {
		if( detailPanel != null ) {
			remove(detailPanel);		
		}
	}

	/**
	 * Setup node editor layout
	 * 
	 * This is being called when a node gets loaded
	 * 
	 */
	public void setupEditorLayout() {
		// Cleanup main panel
		cleanup();
        WorkingSet.clear();
        detailPanel = new ContentEditorPanel();
		add( detailPanel, new BorderLayoutData(LayoutRegion.CENTER) ); // center	
		layout();
		((ContentEditorPanel) detailPanel).setTreePanel(treePanel);
		((ContentEditorPanel) detailPanel).setContentNode(getCurrentNode());
		((ContentEditorPanel) detailPanel).setupLayout();
	}

	/**
	 * Shows result of a save action
	 * 
	 * @param result
	 */
	public void setupChangesetLayout(GwtChangeSet changeSet) {
		cleanup();
        setCurrentNode( null );
        detailPanel = new ChangesetView();
		add( detailPanel, new BorderLayoutData(LayoutRegion.CENTER) ); // center	
        layout();
        ((ChangesetView) detailPanel).setChangeSet(changeSet);
        ((ChangesetView) detailPanel).setupLayout();        
	}

	public GwtNodeData getComparedNode() {
		return comparedNode;
	}


	public void setComparedNode(GwtNodeData comparedNode) {
		this.comparedNode = comparedNode;
	}


	public NodeEditorMode getEditorMode() {
		return editorMode;
	}

	
	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
		CmsGwt.debug("Store key := " + storeKey);
	}

	public String getStoreKey() {
		return storeKey;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Serializable> getComparisonResult() {
		return comparisonResult;
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
/*
	public void setComparePopup(MergePreviewPanel popup) {
		comparePopup = popup;
	}

	public MergePreviewPanel getComparePopup() {
		return comparePopup;
	}
*/	
	public Container<?> getDetailPanel() {
		return detailPanel;
	}


	public void updatePreviewLink() {
		if (currentNode == null) {
			return;
		}


		final String nodeKey = currentNode.getNode().getKey();

		/// CmsGwt.debug("updatePreviewLink(" + nodeKey + ", " + storeKey + ")");

		// extract ID from key
		String storeId = null;
		if (storeKey != null) {
			storeId = storeKey.split(":")[1];
		} else {
			CmsGwt.debug("updatePreviewLink(): Content node preview will be obtained without store context");
		}

		CmsGwt.getContentService().getPreviewUrl(nodeKey, storeId, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				if (result != null) {
					CmsGwt.debug("updatePreviewLink(): update preview with link " + result);
					currentNode.setPreviewUrl(result);
					((ContentEditorPanel) detailPanel).updatePreviewLink();
				} else {
					CmsGwt.debug("updatePreviewLink(): no preview link is created");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				CmsGwt.log("updatePreviewLink(): ERROR ... " + caught.getMessage());
			}
		});
	}
}
