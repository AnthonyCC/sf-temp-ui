package com.freshdirect.cms.ui.client.nodetree;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class TreeContentNodeModel extends ContentNodeModel {
	
	private static final long serialVersionUID = 6355813552570393744L;
	
	public static final String pathSeparator = "|";

	private String path;
    protected boolean hasChildren = true;
    
	
	protected TreeContentNodeModel() {
		super();
		path = pathSeparator + getKey();	//FIXME doesn't make sense, has no key, path will be invalid for sure 
	}
	
    public TreeContentNodeModel( String type, String label, String key ) {
    	super( type, label, key );    	
    }
    
	public TreeContentNodeModel( ContentNodeModel node ) {
		super( node );
		path = pathSeparator + getKey();
    }
	
	public TreeContentNodeModel( ContentNodeModel node, TreeContentNodeModel parent ) {	
		super( node );
		path = parent.path + pathSeparator + getKey();
    }
	
    public String getPath() {
    	return path;
    }
    
	public boolean hasChildren() {
		return hasChildren;
	}
	
	public void setHasChildren( boolean hasChildren ) {
		this.hasChildren = hasChildren;
	}	
}
