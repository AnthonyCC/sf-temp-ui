package com.freshdirect.cms;

import java.util.Collection;
import java.util.Set;

/**
 * Schema definition for a {@link com.freshdirect.cms.ContentType}.
 */
public interface ContentTypeDefI {

	/**
	 * Get the {@link ContentType} this definition describes.
	 * 
	 * @return content type
	 */
	public ContentType getType();

	/**
	 * Get the machine-readable name of the content type.
	 * 
	 * @return name String, never null
	 */
	public String getName();

	/**
	 * Get human-readable display label of the content type
	 * 
	 * @return label String, never null
	 */
	public String getLabel();

	/**
	 * Tell if the content type uses automatically generated IDs.
	 * 
	 * @return true if the content type uses automatically generated IDs.
	 * @see ContentTypeServiceI#generateUniqueId(ContentTypeDefI)
	 */
	public boolean isIdGenerated();
	
	/**
	 * Get the schema definition of a named attribute.
	 * 
	 * @param name attribute name (never null)
	 * @return the attribute definition, or null if no such attribute is defined
	 */
	public AttributeDefI getSelfAttributeDef(String name);

	/**
	 * Get all attributes defined on this content type.
	 * 
	 * @return Collection of {@link AttributeDefI}, never null
	 */
	public Collection getSelfAttributeDefs();

	//
	// convenience
	//

	/**
	 * Get the definition of a specific attribute, with
	 * contextual acquisition (inheritance) applied. 
	 * 
	 * @param name attribute name
	 * @return the attribute definition, or null if no such attribute is defined
	 */
	public AttributeDefI getAttributeDef(String name);

	/**
	 * Get the names of all attributes defined on this content type, with
	 * contextual acquisition (inheritance) applied. 
	 * 
	 * @return Set of String
	 */
	public Set getAttributeNames();

}