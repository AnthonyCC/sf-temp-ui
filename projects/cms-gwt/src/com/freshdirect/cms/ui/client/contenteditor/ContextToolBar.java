package com.freshdirect.cms.ui.client.contenteditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;

public class ContextToolBar extends LayoutContainer {
	private static class Cache {
		private Map<String,String> map = new HashMap<String, String>();
		
		/**
		 * Retrieve last used context for the given content ID
		 * 
		 * @param contentId
		 * @param context
		 */
		public String retrieveContext(GwtNodeData contentNode) {
            if (contentNode != null && contentNode.getNode() != null) {
            	String contentKey = contentNode.getNode().getKey();
            	
            	return map.get(contentKey);
            }
            return null;
		}

		/**
		 * Save selected context for a content ID
		 * @param contentId
		 * @return
		 */
		public void saveContext(GwtNodeData contentNode, String context) {
            if (contentNode != null && contentNode.getNode() != null) {
            	String contentKey = contentNode.getNode().getKey();
            	
    			map.put(contentKey, context);
            }
		}

		public boolean removeContext(GwtNodeData contentNode) {
			if (contentNode != null && contentNode.getNode() != null) {
            	String contentKey = contentNode.getNode().getKey();

            	return map.remove(contentKey) != null;
			}
			return false;
		}
	}


	private final GwtNodeData contentNode;
	private static Cache CONTEXT_CACHE = new Cache();


	// it can be null
	private String lastSelectedContext = null;
	
	public ContextToolBar(final GwtNodeData contentNode, final NodeTree treePanel) {
		this.contentNode = contentNode;
		
		setLayout(new HBoxLayout());
        GwtNodeContext ctx = contentNode.getContexts();
        
        final List<String> contextPathsList = new ArrayList<String>(ctx.size());
        final List<String> contextLabelsList = new ArrayList<String>(ctx.size());
        Listener<BaseEvent> synchronizeButtonClick = null;

        for (String path : ctx.getPaths()) {
            contextPathsList.add(path);
            contextLabelsList.add(ctx.getLabel(path));
        }
        
        ToolButton synchronizeButton = new ToolButton("synchronize-button");
        synchronizeButton.setHeight(16);
        synchronizeButton.setWidth(16);
        HBoxLayoutData buttonMargin = null;





        if( ctx.size() > 1 ) {
        	// phase1 - setup widget
            final SimpleComboBox<String> contextDropdown = new SimpleComboBox<String>();
            
            contextDropdown.setEmptyText("Select active context");
            contextDropdown.setWidth(500);            
            contextDropdown.setEditable(false);
            contextDropdown.setForceSelection(true);
        	contextDropdown.setTriggerAction(TriggerAction.ALL);
            
            contextDropdown.add( contextLabelsList );
  
            contextDropdown.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {
                @Override
                public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
                    int selectedIndex = contextDropdown.getSelectedIndex();
                    if (selectedIndex == -1) {
                        return;
                    }
                    String path = contextPathsList.get(selectedIndex);
                    
                    // -- FDX extension --
                    
                    // Extract store key (Store:FreshDirect) and store in a central place
    	            String _storeKey = GwtNodeContext.extractRootKey(path);
    	            if (!_storeKey.startsWith("Store:")) {
    	            	_storeKey = null;
    	            }
    	            ManageStoreView.getInstance().setStoreKey(_storeKey);
    	            contentNode.changeContext(path, _storeKey);
    	            
    	            ManageStoreView.getInstance().updatePreviewLink();
    	            
    	            lastSelectedContext = path;
                }
            });
            add( contextDropdown );
            
            // phase2 - setup sync button
            synchronizeButtonClick = new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    int index = contextDropdown.getSelectedIndex();
                    if (index == -1) {
                        MessageBox.alert("Info", "First select a context, then push the synchronize button to navigate in the tree.", null);
                    } else {
                        String path = contextPathsList.get(index);
                        treePanel.synchronize(path);
                    }
                };
            };

            // phase3 - set button margin
            buttonMargin = new HBoxLayoutData(3, 0, 0, 2);

            // phase4 - misc
            //   select context ...
            {
            	int index = -1;

            	String contextPath = CONTEXT_CACHE.retrieveContext(contentNode);
            	
                if (contextPath == null) {
                	contextPath = treePanel.getSelectedPath();
                }

                // check if context list has the selected path
	            if (contextPath != null)
	                index = contextPathsList.indexOf(contextPath);

	            // no, fall back to context default
	            if (index == -1) {
	                contextPath = contentNode.getDefaultContextPath();
	            }
	
	            // if there is anything, set it
	            if (contextPath != null) {
	                index = contextPathsList.indexOf(contextPath);
		            if (index != -1) {
		                contextDropdown.setValue( contextDropdown.getStore().getAt(index) );
		                
		                // store selection in cache
	    	            lastSelectedContext = contextPath;
		            }
	            }
            }

            
        } else if ( ctx.size() == 1 ) {
        	// phase1 - setup widget
        	
        	final LabelField contextField = new LabelField(); 
        	
        	contextField.setValue(contextLabelsList.get(0));
        	contextField.setHeight(22);
        	contextField.setReadOnly(true);
        	
            add(contextField);
        	
            // phase2 - setup sync button
            synchronizeButtonClick = new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    String path = contextPathsList.get(0);
                    treePanel.synchronize(path);
                };
            };
            
            // phase3 - set button margin
            buttonMargin = new HBoxLayoutData(2, 0, 0, 2);

            // phase4 - misc 
            final String _cpath = contextPathsList.get(0);
            final String _rootKey = GwtNodeContext.extractRootKey( _cpath );
            if (_rootKey != null && _rootKey.startsWith("Store:")) {
            	ManageStoreView.getInstance().setStoreKey(_rootKey);
	            ManageStoreView.getInstance().updatePreviewLink();
            } else {
            	ManageStoreView.getInstance().setStoreKey( null );
	            ManageStoreView.getInstance().updatePreviewLink();
            }

    	} else {	    	
            final String contextPath = treePanel.getSelectedPath();
            if ( contextPath != null ) {
            	// phase1 - setup widget
	            final String contextLabel = contextPath.replace( TreeContentNodeModel.pathSeparator, " > " );
            	final LabelField contextField = new LabelField(); 
            	
            	contextField.setValue( contextLabel );
				contextField.setHeight( 22 );
				contextField.setReadOnly( true );            	
            	
                add(contextField);

                // phase2 - setup sync button
                synchronizeButtonClick = new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        treePanel.synchronize( contextPath );
                    };
                };
                
                // phase3 - set button margin
                buttonMargin = new HBoxLayoutData(2, 0, 0, 2);
                
                // phase4 - misc
            }
            
	    }
        
        // if all above options failed - we have nothing to display
        if ( synchronizeButtonClick != null  ) {
            synchronizeButton.setToolTip( new ToolTipConfig("Synchronize", "Synchronize the node tree with the selected path.") );  
            synchronizeButton.addListener(Events.OnClick, synchronizeButtonClick );

            add(synchronizeButton, buttonMargin);			
        }

        // saved contexts are intended for single-use
        CONTEXT_CACHE.removeContext(contentNode);
	}

	/**
	 * Remember context when saving node
	 */
	public void saveContext() {
		if (contentNode != null && lastSelectedContext != null
				&& "Product".equalsIgnoreCase( contentNode.getNode().getType() ) ) {
			CONTEXT_CACHE.saveContext(contentNode, lastSelectedContext);
		}
	}
}
