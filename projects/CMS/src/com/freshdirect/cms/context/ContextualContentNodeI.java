package com.freshdirect.cms.context;

import java.util.Map;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

/**
 * A content node in a hierarchical context with attribute acquisition
 * applied. Attributes defined as inheritable will look up the parent
 * hierarchy to obtain a value.
 */
public interface ContextualContentNodeI extends ContentNodeI {

	/**
	 * Get the parent node of this contextual node.
	 * 
	 * @return parent node or null if root
	 */
	public ContextualContentNodeI getParentNode();

	/**
	 * Determine if this node is the root of the context hierarchy.
	 * 
	 * @return true if this node is the root of the context hierarchy
	 */
	public boolean isRoot();

	/**
	 * Get hierarchical context-path of this node.
	 * 
	 * @return slash-separated path, never null
	 */
	public String getPath();

	/**
	 * Get all attributes that would be inherited from the context.
	 * 
	 * @return Map of String (attr name) -> {@link AttributeI}
	 */
	public Map getInheritedAttributes();
	
	/**
	 * Get all attributes that would be inherited from the parent.
	 * 
	 * @return Map of String (attr name) -> {@link AttributeI}
	 */
	public Map getParentInheritedAttributes();

}
