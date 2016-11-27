package com.freshdirect.cms.ui.client.nodetree;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class TreeContentNodeModel extends ContentNodeModel {
	
	private static final long serialVersionUID = 6355813552570393744L;
	
	public static final String pathSeparator = "|";

    protected boolean hasChildren = true;
    

    /**
     * Invalid constructor to be Serializable, don't use this.
     */
	protected TreeContentNodeModel() {
		super();
//		path = pathSeparator + getKey();	//FIXME doesn't make sense, has no key, path will be invalid for sure
		setPath( null );
	}
	
    /**
     * Creates a TreeContentNodeModel with no path.
     * @param type
     * @param label
     * @param key
     */
    public TreeContentNodeModel( String type, String label, String key ) {
    	super( type, label, key );    	
		setPath( null );
    }
    
    /**
     * Creates a TreeContentNodeModel for root nodes (no parent)
     * @param node
     */
	public TreeContentNodeModel( ContentNodeModel node ) {
		super( node );
		setPath( pathSeparator + getKey() );
    }
	
	/**
	 * Creates a TreeContentNodeModel for any node, you have to specify the parent node.
	 * @param node
	 * @param parent
	 */
	public TreeContentNodeModel( ContentNodeModel node, TreeContentNodeModel parent ) {	
		super( node );
		if ( parent != null ) {
			setPath( parent.getPath() + pathSeparator + getKey() );			
		} else {
			setPath( pathSeparator + getKey() );
		}
    }
	
    public TreeContentNodeModel( String type, String label, String key, TreeContentNodeModel parent ) {
    	super( type, label, key );    	
		if ( parent != null ) {
			setPath( parent.getPath() + pathSeparator + getKey() );			
		} else {
			setPath( pathSeparator + getKey() );
		}
    }

    
	public void setPath(String path) {
		set("path", path);
	}	

	public String getPath() {
		return get("path");
	}
	
	public String getId() { 
		return getPath();
	}

	
	public boolean hasChildren() {
		return hasChildren;
	}
	
	public void setHasChildren( boolean hasChildren ) {
		this.hasChildren = hasChildren;
	}	
	
	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof TreeContentNodeModel && getPath() != null  )
			return getPath().equals( ((TreeContentNodeModel)obj).getPath() );			
		else 
			return false;
	}
}
