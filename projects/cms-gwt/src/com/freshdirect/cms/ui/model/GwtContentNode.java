package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;

public class GwtContentNode implements Serializable {
	
    public final static char SEPARATOR = ':';
    
	private static final long serialVersionUID = -350789619809583065L;
	
	private String nodeKey;
	private String label;
	private String type;
	
	/**
	 * Map of original attributes. ( attributeKey -> ContentNodeAttributeI )
	 */
	private Map<String, ContentNodeAttributeI> originalAttributes = new HashMap<String, ContentNodeAttributeI>();
	
	/**
	 * 	Map of changed attribute values. ( attributeKey -> Serializable )
	 */
	private Map<String, Serializable> changedValues = new HashMap<String, Serializable>();
	
	
	public GwtContentNode() {
		nodeKey = "";
		label = "";
		type = "";
	}
	
	/**
	 * 
	 * @param key a full content key, from ContentKey.getEncodedId()
	 */
    public GwtContentNode(String key) {     
        this.nodeKey = key;
        label = "";
        this.type = key.substring(0, key.indexOf(SEPARATOR));
    }

	/**
	 * 
	 * @param type a content type.
	 * @param id an id of a content node, the string after the separator character.
	 */
	public GwtContentNode(String type, String id) {		
		this.nodeKey = type + SEPARATOR + id;
		label = "";
		this.type = type;
	}
	
	public void setOriginalAttribute(String key, ContentNodeAttributeI value) {
		originalAttributes.put(key, value);
	}
	
	public void setOriginalAttributes (HashMap<String, ContentNodeAttributeI> attr) {
		originalAttributes = attr;
	}
	
	public void setLabel(String l) {
		label = l;
	}
	
	public String getLabel() {	
		return label;
	}
	
	/**
	 * A full content key, like 'Product:abcdefg'.
	 */
	public String getKey() {
		return nodeKey;
	}
	
	/**
	 * A content type, like 'Product'.
	 */
	public String getType() {
		return type;
	}

	/**
	 *	Returns the set of attribute keys. 
	 */
	public Set<String> getAttributeKeys() {
		return originalAttributes.keySet();
	}
	
	/**
	 * Returns the map of the original attributes. 
	 */
	public Map<String, ContentNodeAttributeI> getOriginalAttributes() {
		return originalAttributes;
	}	
	
	/**
	 * Returns an original attribute.
	 */
	public ContentNodeAttributeI getOriginalAttribute( String attrKey ) {
		return originalAttributes.get( attrKey );
	}

	/**
	 * Returns an attribute value, changed value if it has been changed, original value otherwise.
	 */
	public Serializable getAttributeValue( String attributeKey ) {		
	    if ( changedValues.containsKey( attributeKey ) ) {
            return changedValues.get( attributeKey );
        } else {
        	ContentNodeAttributeI attr = originalAttributes.get( attributeKey ); 
        	if ( attr != null && attr instanceof ProductConfigAttribute ) {
        		return attr;
        	}
            return attr == null ? null : attr.getValue();
        }
	}
	
	/**
	 * Changes the value of an attribute. 
	 */
	public void changeValue( String attributeKey, Serializable newValue ) {
		System.err.println(attributeKey + " changeValue: " + newValue);
		changedValues.put( attributeKey, newValue );
	}


	/**
	 * Reset attribute
	 * 
	 * @param attributeKey Attribute key
	 */
	public void reset(String attributeKey) {
		if (changedValues.containsKey(attributeKey)) {
			changedValues.remove(attributeKey);
		}
	}

	/** 
	 * Returns the Set of the changed attributes keys.  
	 */
	public Set<String> getChangedValueKeys() {
	    return changedValues.keySet();
	}

    /**
     * Returns an original attribute value
     * 
     * @param attrKey
     * @return
     */
    public Serializable getOriginalAttributeValue(String attrKey) {
        ContentNodeAttributeI attributeI = originalAttributes.get(attrKey);
        if ( attributeI instanceof ProductConfigAttribute ) 
        	return (ProductConfigAttribute)attributeI;
        
        return attributeI != null ? attributeI.getValue() : null;
    }
    
    public ContentNodeModel toContentNodeModel() {
    	return new ContentNodeModel( type, label, nodeKey );
    }

}
