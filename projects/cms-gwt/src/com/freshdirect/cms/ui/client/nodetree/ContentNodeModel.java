package com.freshdirect.cms.ui.client.nodetree;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This model object represents a content node, it contains an key, a label, and a type.
 * It is used for example by the tree widgets.
 * 
 * @author zsombor
 *
 */

public class ContentNodeModel extends BaseModel implements Comparable<ContentNodeModel>, IsSerializable {

    private static final long serialVersionUID = 1L;

    private boolean hasChildren = true;
    
    private String previewUrl = null;
    private int width = 0;    
    private int height = 0; 
    
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
	
	public String getId() {
		return getKey().substring( getKey().indexOf( ':' ) + 1 , getKey().length() );
	}
	
	public boolean hasChildren() {
		return hasChildren;
	}
	
	public void setHasChildren( boolean hasChildren ) {
		this.hasChildren = hasChildren;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}
	
	public void setPreviewUrl( String previewUrl ) {
		this.previewUrl = previewUrl;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth( int width ) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight( int height ) {
		this.height = height;
	}

	
	
	
	public boolean isMediaType() {
		String type = getType();
		return type != null && ( type.equals( "Image" ) || type.equals( "Html" ) );
	}

	public boolean isImageType() {
		return "Image".equals( getType() );
	}

	public boolean isHtmlType() {
		return "Html".equals( getType() );
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
	
    public Component renderLink() {
        if (getType()==null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(512);
        sb.append("<table class=\"content-label\"><tr><td><img src=\"img/icons/");
        sb.append(getType());
        sb.append(".gif\"></td>");
        sb.append("<td><a href=\"#");
        sb.append(getKey());
        sb.append("\">");
        String label = getLabel();
        if (label != null && label.trim().length() > 0) {
            sb.append(getLabel());
        } else {
            sb.append(getKey());
        }
        sb.append("</a></td></tr></table>");

        HtmlContainer container = new HtmlContainer(sb.toString());
        if ( previewUrl != null ) {
            if (isImageType()) {
                container.setToolTip("<img src=\"" + previewUrl + "\" width=\"" + width + "\" height=\"" + height + "\">");
            }
            if (isHtmlType()) {
                container.setToolTip("<iframe src=\"" + previewUrl + "\"></iframe>");
                //container.setToolTip("<a target=\"_blank\" href=\"#\"><img src=\"img/image_zoom.gif\"></a>");
                //sb.append("<td><a target=\"_blank\" href=\"#\"><img src=\"img/image_zoom.gif\"></a></td>");
            }
        }
        
        return container;
    }

}