package com.freshdirect.cms.ui.client.nodetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.freshdirect.cms.ui.client.CmsGwt;


/**
 * Emit a Events.Select event when a node selected and accepted. 
 * 
 * @author greg 
 *
 */
public class ContentTreePopUp extends Window {

    private Collection<String> allowedTypes;
    private NodeTree treepanel = null;
	private Button ok;
    private Button cancel;

    private List<TreeContentNodeModel> selected;

    private static ContentTreePopUp popup = null;
	private static boolean forceReload;
    
    private ContentTreePopUp( Collection<String> aTypes, boolean multiSelect ) {
        super();
        
        ok = new Button("Ok");
        ok.disable();
        cancel = new Button("Cancel");

        addButton(cancel);
        addButton(ok);
        setLayout(new FillLayout());
        setSize(400, 600);
        // setPosition( 0, 0 );
        setModal(true);

        setAllowedTypes(aTypes);
        initialize( multiSelect );
        
		treepanel.setCollapsible(false);
    }
    
    public static ContentTreePopUp getInstance(Collection<String> aTypes, boolean multiSelect) {
        if (popup == null) {
            popup = new ContentTreePopUp(aTypes, multiSelect);
        } else {
            popup.setAllowedTypes(aTypes);
            popup.initialize( multiSelect );
            // need to remove listener, because funny things will happen, if not :)
            // creating a new list for this iteration to avoid ConcurrentModificationException
            List<Listener<? extends BaseEvent>> selectListeners = new ArrayList<Listener<? extends BaseEvent>>( popup.getListeners(Events.Select) );            	
            for (Listener<? extends BaseEvent> listener : selectListeners ) {
                popup.removeListener(Events.Select, listener);
            }
        }
        return popup;
    }
    
    public void setAllowedTypes(Collection<String> aTypes) {
        allowedTypes = aTypes;
    }
    
    public Collection<String> getAllowedTypes() {
        return allowedTypes;
    }

    
    public void setSelected(List<TreeContentNodeModel> s) {
        selected = s;
    }

    public TreeContentNodeModel getSelectedItem() {
    	if ( selected.size() > 0 )
    		return selected.get( 0 );
    	else
    		return null;
    }
    public List<TreeContentNodeModel> getSelectedItems() {
        return selected;
    }

    @Override
    public void setHeading(String text) {
        StringBuilder title = new StringBuilder(text);
        if (allowedTypes != null) {
            title.append(" - [");
            boolean first = true;
            for (String type : allowedTypes) {
                if (first) {
                    first = false;
                } else {
                    title.append(',');
                }
                title.append(type);
            }
            title.append("]");
        }
        super.setHeading(title.toString());
    }
    
    private void initialize( boolean multiSelect ) {
		final boolean newPanel = treepanel == null;
		if ( newPanel ) {
			treepanel = new NodeTree( allowedTypes, multiSelect, false );
		} else {
			treepanel.setAllowedTypes( allowedTypes );
			treepanel.setMultiSelect( multiSelect );
		}


        ok.removeAllListeners();
        ok.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (treepanel.getSelectedItem() != null) {
                    BaseEvent e = new BaseEvent(Events.Select);
                    setSelected(treepanel.getSelectedItems());
                    e.setSource(ContentTreePopUp.this);
                    fireEvent(e.getType(), e);
                }
                hide();
            }
        });

        cancel.removeAllListeners();
        cancel.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                hide();
            }
        });
        
		treepanel.addNodeSelectListener( new NodeTree.NodeSelectListener() {

			@Override
			public void nodeSelected( final TreeContentNodeModel node ) {
                String path = treepanel.getSelectedPath();
                CmsGwt.log( "popup selected path : " + path );

				if ( node != null && ( allowedTypes == null || allowedTypes.contains( node.getType() ) ) ) {
                    BaseEvent e = new BaseEvent(Events.Select);
                    setSelected(treepanel.getSelectedItems());
                    e.setSource(ContentTreePopUp.this);
                    fireEvent(e.getType(), e);
                    hide();
                }
			}
		} );

        treepanel.removeSelectionChangedListener();
        treepanel.addSelectionChangedListener(new SelectionChangedListener<TreeContentNodeModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<TreeContentNodeModel> se) {
                TreeContentNodeModel selectedItem = se.getSelectedItem();
                if (selectedItem != null && (allowedTypes == null || allowedTypes.contains(selectedItem.getType()))) {
                    ok.enable();
                } else {
                    ok.disable();
                }
            }
        });
        
        if (newPanel)
        	add( treepanel );            
    }
    
    @Override
    public void show() {
    	super.show();
    	if (forceReload) {
    		treepanel.refresh(treepanel.getExpandedPaths(), true);
    		forceReload = false;
    	} else {
    		treepanel.refresh(treepanel.getExpandedPaths(), false);
        	treepanel.scrollHack();
    	}

    	/** Expand accidentally collapsed tree panel */
    	treepanel.expand();
    }

	public static void setForceReload(boolean force) {
		forceReload = force;
	}

	public NodeTree getTreepanel() {
		return treepanel;
	}

}
