package com.freshdirect.cms.ui.client.nodetree;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * This model object represents a content node, it contains an key, a label, and a type.
 * It is used for example by the tree widgets.
 * 
 * @author zsombor
 *
 */

public class ContentNodeModel extends BaseModel implements Comparable<ContentNodeModel> {

    private static final long serialVersionUID = 1L;

    private boolean hasChildren = true;
    
    protected ContentNodeModel() {		
	}

    public ContentNodeModel( String label, String key ) {
    	this( key.substring( 0, key.indexOf( ':' ) ), label, key, true );    	
	}

    public ContentNodeModel( String type, String label, String key ) {
    	this( type, label, key, true );    	
    }
    
    public ContentNodeModel( String type, String label, String key, boolean hasChildren ) {
		setLabel( label );
		setKey( key );
		setType( type );
		this.hasChildren = hasChildren;
	}
	

	public void setType(String type) {
		set("type", type);
	}
	
	public void setLabel(String label) {
		set("label", label);
	}

	public void setKey(String key) {
		set("key", key);
	}	

	public String getLabel() {
		return get("label");
	}

	public String getKey() {
		return get("key");
	}	
	
	public String getType() {
		return get("type");
	}
	
	
	public boolean hasChildren() {
		return hasChildren;
	}
	
	public void setHasChildren( boolean hasChildren ) {
		this.hasChildren = hasChildren;
	}

	public boolean isMediaType() {
		String type = getType();
		return type != null && ( type.equals( "Image" ) || type.equals( "Html" ) );
	}
	
    @Override
    public String toString() {
        return "ContentNodeModel[" + getKey() + ',' + getType() + ',' + getLabel() + ']';
    }

	@Override
	public int compareTo( ContentNodeModel o ) {
		return getType().equals( o.getType() ) ? getLabel().compareTo( o.getLabel() ) : getType().compareTo( o.getType() );
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof ContentNodeModel) {
	        ContentNodeModel c = (ContentNodeModel) obj;
	        return c.getKey().equals(getKey());
	    }
	    return false;
	}
}