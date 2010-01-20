package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.freshdirect.cms.ui.client.fields.ChangeTrackingField;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.fields.SaveListenerField;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.google.gwt.i18n.client.DateTimeFormat;


public class GwtNodeData implements Serializable {

	private static final long	serialVersionUID	= -5024274253307157621L;
	
	GwtContentNode node;
	TabDefinition tabDefinition;
	GwtNodeContext contexts;
	
	String previewUrl;
	
	boolean readonly;

	private String currentContext;
	
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


	

	public boolean hasTabs() {
		return !(tabDefinition == null || tabDefinition.getTabIds() == null || tabDefinition.getTabIds().size() == 0);
	}

	/**
	 * This method collects the values from the UI fields into the attributes before sending to the server.
	 * @return the extra, related nodes.
	 *  
	 */
	public void collectValuesFromFields() {
		for ( Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet() ) {
			if ( !e.getValue().isReadonly() ) {
				Field<Serializable> fieldObject = e.getValue().getFieldObject();
				// field object can be null, if the field not rendered
				if ( fieldObject != null ) {
					getValueFromField( e.getKey(), e.getValue().getFieldObject() );
				} 
			}
		}
	}
	
	private void getValueFromField( String name, Field<Serializable> fieldObject ) {
		if ( fieldObject instanceof SaveListenerField ) {
			// onSave will add nodes to the workingset
			( (SaveListenerField)fieldObject ).onSave();
		}
		if ( fieldObject instanceof ChangeTrackingField ) {
			if ( ( (ChangeTrackingField)fieldObject ).isFieldValueChanged() ) {
				Serializable value = ( (ChangeTrackingField)fieldObject ).getChangedValue();
				this.node.changeValue( name, value );
			}
		} else {
			Serializable value = (Serializable)fieldObject.getValue();
			if ( value instanceof ProductConfigAttribute ) {
				ProductConfigAttribute pcAttr = (ProductConfigAttribute)value; 
				this.node.changeValue( name, pcAttr.getValue() ); // Sku
				this.node.changeValue( "QUANTITY", pcAttr.getQuantity() );
				this.node.changeValue( "SALES_UNIT", pcAttr.getSalesUnit() );
				this.node.changeValue( "OPTIONS", pcAttr.getConfigOptionsString() );
			} else {
				this.node.changeValue( name, value );
			}
		}
	}
	

	/**
	 * Returns node value suitable as form item
	 * 
	 * @param attributeKey
	 * @return
	 */
	public Serializable getFormValue(String attributeKey) {
        Serializable value = this.node.getAttributeValue(attributeKey);
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) {
            value = null;
        }
        return value;
	}
	
	public Serializable getFieldValue(String attributeKey) {
		ContentNodeAttributeI attr = this.node.getOriginalAttribute(attributeKey);
		Field<Serializable> field= attr.getFieldObject();
		if(field == null) {
			return attr.getValue();
		}
		return field.getValue();
	}


	public boolean isChanged() {
		for ( Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet() ) {
			if (isDirty(e.getKey(), e.getValue()))
				return true;
		}
		return false;
	}    
    
	public boolean isAttributeChanged(String attributeKey) {
		final ContentNodeAttributeI a = node.getOriginalAttribute(attributeKey);
		return a != null && isDirty(attributeKey, a);
	}

	protected boolean isDirty(String attributeKey, ContentNodeAttributeI attr) {
		Field<Serializable> fieldObject = attr.getFieldObject();
		// field object can be null, if the field not rendered
		if ( fieldObject != null ) {
			Serializable value = fieldObject.getValue();
			Serializable oldValue = node.getOriginalAttributeValue( attributeKey );
			if ( !equal( value, oldValue ) ) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean equal( Serializable value, Serializable oldValue ) {
		if ( value != null ) {
			if ( value instanceof String ) {
				value = ( (String)value ).replace( '\n', ' ' );
			}
			if ( oldValue instanceof String ) {
				oldValue = ( (String)oldValue ).replace( '\n', ' ' );
			}
			if ( value instanceof Date && oldValue instanceof Date ) {
				DateTimeFormat dateFormat = DateTimeFormat.getMediumDateFormat();
				value = dateFormat.format( (Date)value );
				oldValue = dateFormat.format( (Date)oldValue );
			}
			return value.equals( oldValue );
		} else {
			return oldValue == null;
		}
	}



	/**
	 * Adjust field values of inherited attributes according to the given context path
	 * 
	 * @param contextPath
	 */
	@SuppressWarnings( "unchecked" )
	public void changeContext(String contextPath) {
		setCurrentContext( contextPath );
        Map<String, ContentNodeAttributeI> attributes = getNode().getOriginalAttributes();
        Map<String, ContentNodeAttributeI> inheritedAttrs = getContexts().getInheritedAttributes(getCurrentContext());

        if (inheritedAttrs == null || attributes == null) {
            return;
        }

        for (String key : attributes.keySet()) {
            ContentNodeAttributeI attribute = attributes.get(key);
            Field<Serializable> fieldObject = (Field<Serializable>) attribute.getFieldObject();

            if (fieldObject != null && fieldObject instanceof InheritanceField && attribute.isInheritable() ) {

                InheritanceField<Serializable> inheritanceField = (InheritanceField<Serializable>) fieldObject;

                ContentNodeAttributeI inhAttr = inheritedAttrs.get(key);
                if (inhAttr != null ) {
                    inheritanceField.setInheritedValue(inhAttr.getValue());
                }
                else {
                	inheritanceField.setInheritedValue(null);
                }
            }
        }
	}

	public String getCurrentContext() {
		if (currentContext == null) {
			return getDefaultContextPath();
		}
		return currentContext;
	}
	
	public void setCurrentContext(String context) {
		if ( context != null ) {
			currentContext = context;
		} else {
			currentContext = getDefaultContextPath();
		}
	}
}
