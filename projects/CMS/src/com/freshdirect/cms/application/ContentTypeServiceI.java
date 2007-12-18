package com.freshdirect.cms.application;

import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;

/**
 * Repository interface for type definitions.
 */
public interface ContentTypeServiceI {

	/**
	 * Get all supported types.
	 * 
	 * @return Set of {@link ContentType}
	 */
	public Set getContentTypes();

	/**
	 * Generate a unique id for a content type, that can be used to
	 * create a new object.
	 * 
	 * @param typeDef a content type to generate the unique id for.
	 * @return a new unique ID for the given content type, or null
	 *         if unique ID generation is not configured for this type,
	 *         or if the type service doesn't support unique ID generation
	 *         for this type.
	 * @see ContentTypeDefI#isIdGenerated()
	 */
	public String generateUniqueId(ContentType type);

	/**
	 * A helper function to generate a content key with a unique id, for a specified
	 * content type.
	 * 
	 * @param type the content type to generate the content key for
	 * @return a content key for the specified type, with a unique id for that type,
	 *         or null if unique ID generation is not configured for this type,
	 *         or if the type service doesn't support unique ID generation
	 *         for this type.
	 */
	public ContentKey generateUniqueContentKey(ContentType type);
	
	/**
	 * Get definitions for all supported types.
	 * 
	 * @return Set of {@link ContentTypeDefI}
	 */
	public Set getContentTypeDefinitions();

	/**
	 * Get definition for a given type.
	 * 
	 * @param type
	 * @return type definition, or null if type is unsupported
	 */
	public ContentTypeDefI getContentTypeDefinition(ContentType type);

}