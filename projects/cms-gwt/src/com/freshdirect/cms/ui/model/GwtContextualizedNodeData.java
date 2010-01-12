package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Map;

import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;

/**
 *
 * Lightweight wrapper for node data-context couples
 * 
 * @author segabor
 *
 */
public class GwtContextualizedNodeData implements GwtContextualizedNodeI, Serializable {
	private static final long serialVersionUID = -4032182441953864916L;

	GwtNodeData nodeData;
	String		contextPath;

	/**
	 * DO NOT USE! Required by GWT compiler
	 */
	public GwtContextualizedNodeData() {
	}
	
	public GwtContextualizedNodeData(GwtNodeData nodeData, String contextPath) {
		this.nodeData = nodeData;
		this.contextPath = contextPath != null ? contextPath : nodeData.getDefaultContextPath();
	}

	@Override
	public GwtNodeData getNodeData() {
		return nodeData;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}


	/**
	 * Synthesize attribute value. 
	 * 
	 * @param key attribute name
	 * @return
	 */
	public Serializable getValue( String key ) {
		if ( key == null || !nodeData.getNode().getAttributeKeys().contains( key ) )
			return null;

		final Map<String, ContentNodeAttributeI> inheritedAttributes = nodeData.getContexts().getInheritedAttributes( contextPath );

		Serializable v = null;
		final ContentNodeAttributeI attr = nodeData.getNode().getOriginalAttribute( key );

		// Get value depending on its nature
		v = nodeData.getNode().getAttributeValue( key );
		if ( v == null && attr.isInheritable() ) {
			// Lookup inherited value in the context (if any)
			ContentNodeAttributeI iattr = inheritedAttributes == null ? null : inheritedAttributes.get( key );
			if ( iattr != null ) { // found inherited value
				v = iattr.getValue();
			}
		}

		return v;
	}
}
