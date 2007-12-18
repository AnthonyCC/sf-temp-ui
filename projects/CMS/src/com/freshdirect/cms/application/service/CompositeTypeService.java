package com.freshdirect.cms.application.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Merges definition from multiple content type services. If a type definition
 * occurs in multiple services, their attribute definitions are merged.
 * 
 * @TODO Ensure precedence is deterministic (w/ respect to type service list ordering)
 * in case of duplicate attribute definitions.
 */
public class CompositeTypeService implements ContentTypeServiceI {

	private final Category LOGGER = LoggerFactory.getInstance(CompositeTypeService.class);
	
	/** List of {@link ContentTypeServiceI} */
	private final List contentTypeServices;

	/** Map of {@link ContentType} -> {@link ContentTypeDefI} */
	private final Map contentTypeDefMap = new HashMap();

	/**
	 * @param contentTypeServices List of {@link ContentTypeServiceI}
	 */
	public CompositeTypeService(List contentTypeServices) {
		this.contentTypeServices = contentTypeServices;
		this.initialize();
	}

	private void initialize() {
		LOGGER.info("initialize:  " + contentTypeServices);
		contentTypeDefMap.clear();
		for (Iterator i = contentTypeServices.iterator(); i.hasNext();) {
			ContentTypeServiceI contentTypeService = (ContentTypeServiceI) i.next();

			// Consolidate contentType's into master content type definitions
			Set contentTypes = contentTypeService.getContentTypes();
			for (Iterator j = contentTypes.iterator(); j.hasNext();) {
				ContentType type = (ContentType) j.next();
				addToContentTypeDefMap(contentTypeDefMap, type, contentTypeService.getContentTypeDefinition(type));
			}
		}
	}

	private void addToContentTypeDefMap(Map map, ContentType type, ContentTypeDefI def) {
		ContentTypeDef found = (ContentTypeDef) map.get(type);
		if (found == null) {
			found = new ContentTypeDef(this, type, def.getLabel());
			map.put(type, found);
		}
		Collection defs = def.getSelfAttributeDefs();
		for (Iterator i = defs.iterator(); i.hasNext();) {
			AttributeDefI attr = (AttributeDefI) i.next();
			if (attr != null) {
				if (found.getSelfAttributeDef(attr.getName()) != null) {
					LOGGER.warn("Found duplicate attribute name: " + attr.getName() + " in type: " + type.toString());
				} else {
					found.addAttributeDef((AttributeDef) attr);
				}
			}
		}
	}

	public Set getContentTypes() {
		return contentTypeDefMap.keySet();
	}

	public Set getContentTypeDefinitions() {
		return contentTypeDefMap.entrySet();
	}

	public ContentTypeDefI getContentTypeDefinition(ContentType type) {
		return (ContentTypeDefI) contentTypeDefMap.get(type);
	}

	public String generateUniqueId(ContentType type) {
		for (Iterator i = contentTypeServices.iterator(); i.hasNext();) {
			ContentTypeServiceI contentTypeService = (ContentTypeServiceI) i.next();
			String				id                 = contentTypeService.generateUniqueId(type);
			
			if (id != null) {
				return id;
			}
		}
		
		return null;
	}

	public ContentKey generateUniqueContentKey(ContentType type) {
		String id = generateUniqueId(type);
		
		return id == null ? null
				          : new ContentKey(type, id);
	}

}