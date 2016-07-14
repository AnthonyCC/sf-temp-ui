package com.freshdirect.cms.application.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;
import com.freshdirect.framework.util.log.LoggerFactory;

public class NullContentService implements ContentServiceI {
	public static Logger LOGGER = LoggerFactory.getInstance(NullContentService.class.getSimpleName());
	
	
	public static class NullContentTypeService implements ContentTypeServiceI {
		public static Logger LOGGER = LoggerFactory.getInstance(NullContentTypeService.class.getSimpleName());

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Set<ContentType> getContentTypes() {
			LOGGER.info("getContentTypes() called");

			return Collections.emptySet();
		}

		@Override
		public String generateUniqueId(ContentType type) {
			LOGGER.info("generateUniqueId("+type+") called");

			return null;
		}

		@Override
		public ContentKey generateUniqueContentKey(ContentType type) {
			LOGGER.info("generateUniqueContentKey("+type+") called");

			return null;
		}

		@Override
		public Set<? extends ContentTypeDefI> getContentTypeDefinitions() {
			LOGGER.info("getContentTypeDefinitions() called");

			return Collections.emptySet();
		}

		@Override
		public ContentTypeDefI getContentTypeDefinition(ContentType type) {
			LOGGER.info("getContentTypeDefinition("+type+") called");

			return null;
		}

		@Override
		public BidirectionalReferenceHandler getReferenceHandler(
				ContentType type, String attribute) {
			LOGGER.info("getReferenceHandler("+type+", "+attribute+") called");

			return null;
		}

		@Override
		public Collection<BidirectionalReferenceHandler> getAllReferenceHandler() {
			LOGGER.info("BidirectionalReferenceHandler() called");

			return Collections.emptySet();
		}
		
	}


	public static NullContentTypeService TYPE = new NullContentTypeService();

	private String name;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
		LOGGER.info("getContentKeys() called");
		
		return Collections.emptySet();
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		LOGGER.info("getContentKeysByType("+type+") called");

		return Collections.emptySet();
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		LOGGER.info("getParentKeys("+key+") called");

		return Collections.emptySet();
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		LOGGER.info("getContentNode("+key+") called");

		return null;
	}

	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		LOGGER.info("getRealContentNode("+key+") called");

		return null;
	}

	@Override
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
		LOGGER.info("getContentNodes(...) called");

		return Collections.emptyMap();
	}

	@Override
	public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type,
			Predicate criteria, DraftContext draftContext) {

		LOGGER.info("queryContentNodes("+type+") called");
		return Collections.emptyMap();
	}

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
		LOGGER.info("createPrototypeContentNode("+key+") called");

		return null;
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {
		LOGGER.info("handle("+request+") called");

		return new CmsResponse();
	}

	@Override
	public ContentTypeServiceI getTypeService() {
		LOGGER.info("getTypeService() called");

		return TYPE;
	}

}
