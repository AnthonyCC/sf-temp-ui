package com.freshdirect.cms.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.node.CompositeContentNode;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Provides a composite view of multiple content services. All type definitions
 * and content objects are merged. On {@link #handle(CmsRequestI)}, the node is
 * decomposed and the partial requests are flushed to the appropriate underlying
 * services that contributed to the content node.
 * 
 * @see com.freshdirect.cms.application.service.CompositeTypeService
 */
public class CompositeContentService extends AbstractContentService implements ContentServiceI {

	private final Category LOGGER = LoggerFactory.getInstance(CompositeContentService.class);
	
	private final CompositeTypeService typeService;

	private final List<ContentServiceI> contentServices;

	private final Map<String, ContentServiceI> contentServicesByName;

	/**
	 * 
	 * @param contentServices List of {@link ContentServiceI}
	 */
	public CompositeContentService(List<ContentServiceI> contentServices) {
		if (contentServices == null || contentServices.size() < 2) {
			throw new IllegalArgumentException("Need at least two content services");
		}

		this.contentServices = contentServices;
		contentServicesByName = new HashMap<String, ContentServiceI>(contentServices.size());
		List<ContentTypeServiceI> typeServices = new ArrayList<ContentTypeServiceI>(contentServices.size());

		for (ContentServiceI service : contentServices) {
			Object o = contentServicesByName.put(service.getName(), service);
			if (o != null) {
				throw new IllegalArgumentException("Duplicate service name " + service.getName());
			}
			typeServices.add(service.getTypeService());
		}

		typeService = new CompositeTypeService(typeServices);
	}

	@Override
	public ContentTypeServiceI getTypeService() {
		return typeService;
	}
	
	@Override
	public ContentNodeI getContentNode(ContentKey cKey) {
		Map<String, ContentNodeI> nodes = new LinkedHashMap<String, ContentNodeI>();
		boolean found = false;
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			if (service.getTypeService().getContentTypeDefinition(cKey.getType()) == null) {
				continue;
			}
			ContentNodeI node = service.getContentNode(cKey);
			if (node == null) {
				node = service.createPrototypeContentNode(cKey);
			} else {
				found = true;
			}
			if (node != null) {
				nodes.put(service.getName(), node);
			}
		}
		if (!found) {
			return null;
		}
		return new CompositeContentNode(this, cKey, nodes);
	}

	@Override
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys) {

		Set<ContentKey> foundKeys = new HashSet<ContentKey>(keys.size());

		/** Map of ContentKey -> (Map of String (service name) -> ContentNodeI) */
		Map<ContentKey, Map<String, ContentNodeI>> nodeMapsByKey = new HashMap<ContentKey, Map<String, ContentNodeI>>(keys.size());
		for (Iterator<ContentServiceI> j = contentServices.iterator(); j.hasNext();) {
			ContentServiceI service = (ContentServiceI) j.next();

			long t = System.currentTimeMillis();

			Map<ContentKey, ContentNodeI> serviceNodes = service.getContentNodes(keys);

			t = System.currentTimeMillis() - t;
			LOGGER.debug("getContentNodes() " + service.getName() + " " + keys.size() + " took " + t);

			for (Iterator<ContentKey> k = keys.iterator(); k.hasNext();) {
				ContentKey key = (ContentKey) k.next();
				ContentNodeI node = (ContentNodeI) serviceNodes.get(key);
				if (node == null) {
					node = service.createPrototypeContentNode(key);
				} else {
					foundKeys.add(key);
				}
				if (node != null) {
					Map<String, ContentNodeI> nodeMap = (Map<String, ContentNodeI>) nodeMapsByKey.get(node.getKey());
					if (nodeMap == null) {
						nodeMap = new LinkedHashMap<String, ContentNodeI>();
						nodeMapsByKey.put(node.getKey(), nodeMap);
					}
					nodeMap.put(service.getName(), node);
				}
			}
		}

		/** Map of ContentKey -> CompositeContentNode */
		Map<ContentKey, ContentNodeI> nodesByKey = new HashMap<ContentKey, ContentNodeI>(keys.size());
		for (Iterator<ContentKey> i = foundKeys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			Map<String, ContentNodeI> nodes = nodeMapsByKey.get(key);
			if (!nodes.isEmpty()) {
				nodesByKey.put(key, new CompositeContentNode(this, key, nodes));
			}
		}

		return nodesByKey;
	}

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey cKey) {
		Map<String, ContentNodeI> nodes = new HashMap<String, ContentNodeI>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			ContentNodeI newNode = service.createPrototypeContentNode(cKey);
			if (newNode != null)
				nodes.put(service.getName(), newNode);
		}
		if (nodes.size() == 0) {
			return null;
		}
		return new CompositeContentNode(this, cKey, nodes);
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type) {
		Set<ContentKey> set = new HashSet<ContentKey>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			set.addAll(service.getContentKeysByType(type));
		}
		return set;
	}

	@Override
	public Set<ContentKey> getContentKeys() {
		Set<ContentKey> set = new HashSet<ContentKey>();
		for (Iterator<ContentServiceI> i = contentServices.iterator(); i.hasNext();) {
			ContentServiceI service =  i.next();
			set.addAll(service.getContentKeys());
		}
		return set;
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key) {
		Set<ContentKey> keySet = new HashSet<ContentKey>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			Set<ContentKey> keys = service.getParentKeys(key);

			keySet.addAll(keys);
		}

		return keySet;
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {

		Map<String, CmsRequest> reqByService = decomposeRequest(request);

		// execute requests
		for (ContentServiceI service : contentServices) {
			CmsRequestI serviceReq = (CmsRequestI) reqByService.get(service.getName());
			if (serviceReq == null) {
				continue;
			}
			service.handle(serviceReq);
			// TODO process service response 
		}

		return new CmsResponse();
	}

	/**
	 * @return Map of String (serviceName) -> CmsRequest
	 */
	private Map<String, CmsRequest> decomposeRequest(CmsRequestI request) {
		Map<String, CmsRequest> reqByService = new HashMap<String, CmsRequest>();

		// decompose request with composite nodes
		for (ContentNodeI node : request.getNodes()) {
			if (!(node instanceof CompositeContentNode)) {
				throw new IllegalArgumentException("Node " + node + " is not a composite");
			}
			Map<String, ContentNodeI> nodes = ((CompositeContentNode) node).getNodes();
			for (Iterator<Map.Entry<String, ContentNodeI>> j = nodes.entrySet().iterator(); j.hasNext();) {
				Map.Entry<String, ContentNodeI> e = j.next();
				String serviceName = (String) e.getKey();
				ContentNodeI serviceNode = (ContentNodeI) e.getValue();

				CmsRequest serviceReq = (CmsRequest) reqByService.get(serviceName);
				if (serviceReq == null) {
					serviceReq = new CmsRequest(request.getUser());
					reqByService.put(serviceName, serviceReq);
				}
				serviceReq.addNode(serviceNode);
			}
		}

		return reqByService;
	}

	@Override
	public ContentNodeI getRealContentNode(ContentKey key) {
		Map<String, ContentNodeI> nodes = new LinkedHashMap<String, ContentNodeI>();
		boolean found = false;
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			if (service.getTypeService().getContentTypeDefinition(key.getType()) == null) {
				continue;
			}
			ContentNodeI node = service.getRealContentNode(key);
			if (node == null) {
				node = service.createPrototypeContentNode(key);
			} else {
				found = true;
			}
			if (node != null) {
				nodes.put(service.getName(), node);
			}
		}
		if (!found) {
			return null;
		}
		return new CompositeContentNode(this, key, nodes);
	}

}