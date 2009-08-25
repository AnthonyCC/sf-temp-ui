package com.freshdirect.cms.ui.client.nodetree;

import java.util.Set;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;


/**
 * Emit a Events.Select event when a node selected and accepted. 
 * 
 * @author greg 
 *
 */
public class ContentTreePopUp extends Window {

    private Set<String>		allowedTypes;
    private NodeTree	treepanel = null;
    private Button			ok;
    private Button			cancel;

    private ContentNodeModel selected;
    
    private static ContentTreePopUp popup = null;

    
    private ContentTreePopUp(Set<String> aTypes) {
        super();
        
        ok = new Button("Ok");
        ok.disable();
        cancel = new Button("Cancel");
        
        addButton( cancel );
		addButton( ok );
		setLayout( new FillLayout() );
		setSize( 400, 600 );
		setPosition( 0, 0 );
		setModal( true );
		        
        setAllowedTypes( aTypes );
        initialize();
    }
    
    public static ContentTreePopUp getInstance( Set<String> aTypes ) {
    	if ( popup == null ) {
    		popup = new ContentTreePopUp( aTypes );
    	} else {
    		popup.setAllowedTypes( aTypes );
            popup.initialize();
    	}
    	return popup;
    }

    
    public void setAllowedTypes ( Set<String> aTypes ) {
    	allowedTypes = aTypes;
    }
    
    public Set<String> getAllowedTypes() {
        return allowedTypes;
    }

    
    public void setSelected(ContentNodeModel s) {
        selected = s;
    }

    public ContentNodeModel getSelected() {
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
    
    private void initialize() {

    	if ( treepanel == null ) {
    		treepanel = new NodeTree(allowedTypes);
    		treepanel.setHeaderVisible( false );
    	} else {
    		treepanel.setAllowedTypes( allowedTypes );
    	}
    	
        final Window w = this;

        ok.removeAllListeners();
        ok.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if ( treepanel.getSelectedItem() != null ) {
                    BaseEvent e = new BaseEvent( Events.Select );
                    setSelected( (ContentNodeModel) treepanel.getSelectedItem() );
                    e.setSource(w);
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

        if ( treepanel != null ) {
        	
//            treepanel.loadRootNodes();
            
            treepanel.removeSelectionChangedListener();
            treepanel.addSelectionChangedListener( new SelectionChangedListener<ContentNodeModel>() {
            	@Override public void selectionChanged( SelectionChangedEvent<ContentNodeModel> se ) {
            		ContentNodeModel selectedItem = se.getSelectedItem(); 
            		if ( selectedItem != null && ( allowedTypes == null || allowedTypes.contains( selectedItem.getType() ) ) ) {
            			ok.enable();
            		} else {
            			ok.disable();
            		}
            	}
            });
            
            add( treepanel );            
        }
    }
}
