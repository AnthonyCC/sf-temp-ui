package com.freshdirect.cms.application;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;

/**
 * Interface that all services that contribute content must implement.
 * Provides CRUD methods to a content repository.
 */
public interface ContentServiceI {

	/**
	 * Get globally unique service name.
	 * 
	 * @return service name (never null)
	 */
	public String getName();

	/**
	 * Get all content keys in the repository.
	 * 
	 * @return Set of ContentKey (never null)
	 */
	public Set<ContentKey> getContentKeys(DraftContext draftContext);

	/**
	 * Get content keys matching given type.  
	 * 
	 * @return Set of ContentKey (never null)
	 */
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext);

	/**
	 * Get keys that have a navigable relationship TO the given key.
	 * 
	 * @param key ContentKey (never null)
	 * @return Set of ContentKey (never null)
	 */
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext);

	/**
	 * @return ContentNodeI (or null if not found, or type is not supported)
	 */
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext);
	
	/**
	 * @param key
	 * @return real content nodes (persisted in CMS DB). ERPS only content nodes are not returned!
	 */
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext);
	
	/**
	 * Retrieve multiple nodes.
	 * 
	 * @param keys Set of ContentKey
	 * @return Map of ContentKey -> ContentNodeI (never null)
	 */
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext);
	
	/**
	 * Retrieve nodes of a certain type based on filter criteria.
	 * 
	 * @param type content type (never null)
	 * @param criteria filter predicate (never null)
	 * @return Map of ContentKey -> ContentNodeI (never null)
	 */
	public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate criteria, DraftContext draftContext);

	/**
	 * Instantiate a new blank content object. The object is not yet stored at this point.
	 * Duplicate keys are not checked for.
	 * 
	 * @return ContentNodeI (or null if type is not supported)
	 */
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext);

	/**
	 * Process a request to store/update/delete content objects.
	 * Caller must ensure the request contains only nodes that originated from this service.
	 * Validation warnings will get collected in the response.
	 * 
	 * @param request (never null)
	 * @return CmsResponseI (never null)
	 * 
	 * @throws CmsRuntimeException if an infrastructural error occurs
	 */
	public CmsResponseI handle(CmsRequestI request);

	/**
	 * Get type service that describes the types provided by this content service. 
	 * 
	 * @return ContentTypeServiceI (never null)
	 */
	public ContentTypeServiceI getTypeService();

}