package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Map;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.freshdirect.cms.ui.client.contenteditor.ContentEditor;
import com.freshdirect.cms.ui.client.contenteditor.ContentForm;
import com.freshdirect.cms.ui.client.fields.ChangeTrackingField;
import com.freshdirect.cms.ui.client.fields.SaveListenerField;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;


public class GwtNodeData implements Serializable {

	private static final long	serialVersionUID	= -5024274253307157621L;
	
	GwtContentNode node;
	TabDefinition tabDefinition;
	GwtNodeContext contexts;
	
	String previewUrl;
	
	boolean readonly;
	
	public GwtNodeData() {
		readonly = false;
		tabDefinition = null;
		contexts = null;
	}
	
	public GwtNodeData(GwtContentNode n, boolean readonly) {
		this.node = n;
		this.readonly = readonly;
	}
	
	public GwtNodeData(GwtContentNode n, TabDefinition t, boolean readonly) {
		this.node = n;
		this.tabDefinition = t;
		this.readonly = readonly;
	}
	
	public GwtNodeData(GwtContentNode n, TabDefinition t, GwtNodeContext context, boolean readonly, String previewUrl) {
		this.node = n;
		this.tabDefinition = t;
		this.contexts = context;
		this.readonly = readonly;
		this.previewUrl = previewUrl;
	}
	
	
	public GwtContentNode getNode() {
		return node;
	}
	
	public void setNode(GwtContentNode node) {
		this.node = node;
	}
	
	public TabDefinition getTabDefinition() {
		return tabDefinition;
	}
	
	public void setTabDefinition( TabDefinition tabDefinition ) {
		this.tabDefinition = tabDefinition;
	}
	
	public boolean isReadonly() {
        return readonly;
    }
	
	public GwtNodeContext getContexts() {
		return contexts;
	}
	
	public void setContexts( GwtNodeContext contexts ) {
		this.contexts = contexts;
	}
	
    public String getPreviewUrl() {
        return previewUrl;
    }
	
    public String getDefaultContextPath() {
    	if ( contexts == null || contexts.size() == 0 ) 
    		return null;
    	
    	if ( contexts.size() == 1 )
    		return contexts.getPaths().iterator().next();
    	
    	ContentNodeAttributeI attr = node.getOriginalAttribute( "PRIMARY_HOME" );
    	if ( attr != null ) {
	    	ContentNodeModel primaryHome = (ContentNodeModel)attr.getValue();    
	    	String PHKey = primaryHome.getKey(); 
	    	for ( String path : contexts.getPaths() ) {
	    		if ( path.contains( PHKey ) ) {
	    			return path;
	    		}
	    	}
    	}
    	
    	return null;    	
    }
    
    public Map<String,ContentNodeAttributeI> getDefaultContextMap() {
    	return contexts.getInheritedAttributes( getDefaultContextPath() );
    }


	/**
	 * This method creates the necessary fields, and controls in the given panel.
	 * @param panel
	 */
	public void setupUI(ContentPanel panel, String contextPath ) {
        if ( tabDefinition == null || tabDefinition.getTabIds() == null || tabDefinition.getTabIds().size() == 0 ) {
            ContentForm contentForm = new ContentForm(this, contextPath);
            contentForm.addStyleName("fixed");
            panel.add(contentForm);
        } else {
            ContentEditor ce = new ContentEditor(this, contextPath);
            ce.addStyleName("fixed");
            panel.add(ce);
        }
	}
	
	/**
	 * This method collects the values from the UI fields into the attributes before sending to the server.
	 * @return the extra, related nodes.
	 *  
	 */
    public void collectValuesFromFields() {
        for (Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet()) {
            Field<Serializable> fieldObject = e.getValue().getFieldObject();
            // field object can be null, if the field not rendered
            if (fieldObject != null) {
                getValueFromField(e.getKey(), e.getValue().getFieldObject());
            } else {
                this.node.changeValue(e.getKey(), e.getValue().getValue());
            }
        }
    }
	
    private void getValueFromField(String name, Field<Serializable> fieldObject) {
        if (fieldObject instanceof SaveListenerField) {
            // onSave will add nodes to the workingset
            ((SaveListenerField) fieldObject).onSave();
        }
        if (fieldObject instanceof ChangeTrackingField) {
            if (((ChangeTrackingField) fieldObject).isFieldValueChanged()) {
                Serializable value = ((ChangeTrackingField) fieldObject).getChangedValue();
                this.node.changeValue(name, value);
            }
        } else {
            Serializable value = (Serializable) fieldObject.getValue();
            this.node.changeValue(name, value);
        }
    }
    
    public boolean isChanged() {
        for (Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet()) {
            Field<Serializable> fieldObject = e.getValue().getFieldObject();
            // field object can be null, if the field not rendered
            if (fieldObject != null) {
                Serializable value = fieldObject.getValue();
                Serializable oldValue = this.node.getOriginalAttributeValue(e.getKey());
                if (!equal(value,oldValue)){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    private boolean equal(Serializable value, Serializable oldValue) {
        if (value != null) {
            if (value instanceof String) {
                value = ((String) value).replace('\n', ' ');
            }
            if (oldValue instanceof String) {
                oldValue = ((String) oldValue).replace('\n', ' ');
            }
            return value.equals(oldValue);
        } else {
            return oldValue==null;
        }
    }

    public String getHeaderMarkup() {
        return "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" id=\"pageTitle\">" +
                "<tbody><tr><td class=\"context\"></td><td width=\"75\" valign=\"bottom\" align=\"right\" rowspan=\"2\" style=\"line-height: 0pt;\">" +
                    "<img width=\"75\" height=\"66\" src=\"img/banners/" + node.getType() + ".gif\"/></td></tr><tr><td valign=\"bottom\">" +
                        "<h1 title=\"" + node.getKey()+ "\"><nobr><span id=\"fadeError\">" + node.getLabel() + "</span></nobr></h1>" +
                    "</td></tr></tbody></table>" +
                    "<table width=\"100%\" style=\"border-collapse:collapse\"><tr><td width=\"50%\" class=\"pageHeader\">" + node.getKey().replace( ':', ' ' ) + "</td>" +
                        "<td class=\"pageHeader\"><div class=\"rigthPanel\"> " + (previewUrl!=null ? "<a class=\"previewLink\" href=\""+previewUrl+"\" target=\"_blank\" title=\"Preview...\">Preview...</a>" : "") +
             "</div></td></tr></table>";
    }

}
