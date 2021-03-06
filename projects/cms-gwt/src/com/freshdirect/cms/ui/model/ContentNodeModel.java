package com.freshdirect.cms.ui.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This model object represents a content node, it contains an key, a label, and a type.
 * It is used for example by the tree widgets.
 */

public class ContentNodeModel extends BaseModel implements Comparable<ContentNodeModel>, IsSerializable {
    
    private static final long serialVersionUID = 1L;

    private String previewUrl;
    private int width = 0;
    private int height = 0;
    private String iconOverride = "";

	protected ContentNodeModel() {
	}

    public ContentNodeModel( String type, String label, String key ) {
		setLabel( label );
		setKey( key );
		setType( type );
    }

    public ContentNodeModel( ContentNodeModel node ) {
		this(node.getType(), node.getLabel(), node.getKey());
		setWidth( node.getWidth() );
		setHeight( node.getHeight() );
		setPreviewUrl( node.getPreviewUrl() );
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
	
	public String getContentId() {
		return getKey().substring( getKey().indexOf( ':' ) + 1 , getKey().length() );		
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

    public String getIconOverride() {
        return iconOverride;
    }

    public void setIconOverride(String iconOverride) {
        this.iconOverride = iconOverride;
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
	
	public boolean isNullType() {
	    return GwtContentNode.NULL_TYPE.equals(getType());
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
	
    public Widget renderLinkComponent() {
        if ( getType() == null ) {
            return null;
        }
        HtmlContainer container = new HtmlContainer( renderLink( true ) );
        String tooltip = getPreviewToolTip();
        if ( tooltip != null && tooltip.trim().length() > 0 ) {
        	container.setToolTip( tooltip );
        }
        return container;
    }

    public String renderLink() {
    	return renderLink( false );
    }
    
    /**
     * Render a table html which contains the necessary type specific icon and optional preview javascript link.
     * 
     * @param javascript
     * @return
     */
    public String renderLink( boolean javascript ) {
        StringBuilder sb = new StringBuilder( 1024 );
        sb.append("<table class=\"content-label\"><tr>");
        if ( javascript ) {
	        sb.append( "<td>" );
	        sb.append( getJavascriptPreviewLink() );
	        sb.append( "</td>" );
        }
        sb.append( "<td><img src=\"img/icons/" );
        sb.append(getType());
        sb.append(getIconOverride());
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
        return sb.toString();
    }
    
	public String getPreviewToolTip() {
		if ( checkPreviewTypes( previewUrl ) ) {
			if ( isImageType() ) {
				return "<img src=\"" + previewUrl + "\" width=\"" + width + "\" height=\"" + height + "\">";
			} else if ( isHtmlType() ) {
				return "<iframe src=\"" + previewUrl + "\" scrolling=\"no\" frameborder=\"0\" width=\"250\" height=\"145\"></iframe>";
			}			
		}
		return getKey();
	}
	
	protected static boolean checkPreviewTypes( String previewUrl ) {
		return previewUrl != null && ( previewUrl.endsWith( "gif" ) || previewUrl.endsWith( "jpg" ) || previewUrl.endsWith( "jpeg" ) || previewUrl.endsWith( "png" )
				|| previewUrl.endsWith( "htm" ) || previewUrl.endsWith( "html" ) || previewUrl.endsWith( "txt" ) );		
	}
	
	public String getJavascriptPreviewLink() {
		StringBuffer sb = new StringBuffer( 1024 );
		if ( isMediaType() && getPreviewUrl() != null ) {  
			sb.append( "<a target=\"preview\" href=\"javascript: void(0)\" onclick=\"window.open('" );
			sb.append( getPreviewUrl() );
			sb.append( "','preview','directories=no,location=no,menubar=no,status=no,titlebar=no,toolbar=no,resizable=yes,scrollbars=yes" );
			if ( isImageType() ) {
				sb.append( ",width=" );
				sb.append( width );
				sb.append( ",height=" );
				sb.append( height );
			} else if ( isHtmlType() ) {
				sb.append( ",width=600" );
				sb.append( ",height=400" );
			}
			sb.append( "', false); return false;\"><img src=\"img/image_zoom.gif\"></a>" );
		}		
		return sb.toString();
	}


	public String renderIconLink() {
		StringBuilder sb = new StringBuilder( 1024 );
        sb.append("<table class=\"content-label\"><tr>");
        sb.append("<td><a href=\"#");
        sb.append(getKey());
        sb.append("\">");
        sb.append("<img src=\"img/icons/");
        sb.append(getType());
        sb.append(getIconOverride());
        sb.append(".gif\">");
        sb.append("</a></td></tr></table>");
        return sb.toString();
	}
	
	public Widget renderIconLinkComponent() {
		if (getType() == null) {
			return null;
		}
		HtmlContainer container = new HtmlContainer(renderIconLink());
		String tooltip = getPreviewToolTip();
		if (tooltip != null && tooltip.trim().length() > 0) {
			container.setToolTip(tooltip);
		}
		return container;
	}

}