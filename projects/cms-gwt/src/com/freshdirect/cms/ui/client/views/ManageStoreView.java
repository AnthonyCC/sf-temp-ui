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
import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.google.gwt.user.client.History;

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
	private GwtContextualizedNodeData comparedNode = null;
	
	protected ManageStoreView() {
		super();	
		
		this.setLayout( new BorderLayout() );
		
		setBorders(false);
		
		// ============ node tree ============		
		treePanel = new NodeTree();
        treePanel.addNodeSelectListener(new NodeTree.NodeSelectListener() {
            @Override
            public void nodeSelected(final TreeContentNodeModel node) {
                final ContentNodeModel parent = treePanel.getSelectedParent();
                if (node != null) {
                    ManageStoreView.getInstance().nodeSelected(node.getKey(), parent, true);
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
    public void loadNode(final String nodeKey, String path) {
        CmsGwt.getContentService().getNodeData( nodeKey, path, new GetNodeAction(nodeKey));
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
        return (currentNode != null) && !currentNode.isReadonly() && currentNode.isChanged();
    }

	public void nodeSelected( final String key, final ContentNodeModel parent, boolean dirtyWarning ) {
		if ( key != null ) {
            if ( dirtyWarning && ( !WorkingSet.isEmpty() || isFormChanged() ) ) {
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
	
	/**
	 * Determines whether node is already edited / changed
	 * 
	 * @return the state of dirtyness
	 */
	public boolean isNodeEditorDirty() {
		return !WorkingSet.isEmpty() || isFormChanged();
	}

	public GwtNodeData getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(GwtNodeData currentNode) {
		this.currentNode = currentNode;
	}
	
	public NodeTree getTreePanel() {
		return treePanel;
	}

	public GwtContextualizedNodeI getCurrentContextualizedNode() {
		if( currentNode != null) {
			String path = null;
			if( treePanel != null ) {
				path = treePanel.getSelectedPath();
			}
			return new GwtContextualizedNodeData(currentNode, path);
		}
		return null;
	}

	protected void cleanup() {
		if(detailPanel !=null) {
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

	public GwtContextualizedNodeData getComparedNode() {
		return comparedNode;
	}


	public void setComparedNode(GwtContextualizedNodeData comparedNode) {
		this.comparedNode = comparedNode;
	}


	public NodeEditorMode getEditorMode() {
		return editorMode;
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
}
