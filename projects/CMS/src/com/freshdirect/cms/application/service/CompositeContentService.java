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

	private final List contentServices;

	private final Map contentServicesByName;

	/**
	 * 
	 * @param contentServices List of {@link ContentServiceI}
	 */
	public CompositeContentService(List contentServices) {
		if (contentServices == null || contentServices.size() < 2) {
			throw new IllegalArgumentException("Need at least two content services");
		}

		this.contentServices = contentServices;
		contentServicesByName = new HashMap(contentServices.size());
		List typeServices = new ArrayList(contentServices.size());

		for (Iterator i = contentServices.iterator(); i.hasNext();) {
			ContentServiceI service = (ContentServiceI) i.next();
			Object o = contentServicesByName.put(service.getName(), service);
			if (o != null) {
				throw new IllegalArgumentException("Duplicate service name " + service.getName());
			}
			typeServices.add(service.getTypeService());
		}

		typeService = new CompositeTypeService(typeServices);
	}

	public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	public ContentNodeI getContentNode(ContentKey cKey) {
		Map nodes = new LinkedHashMap();
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

	public Map getContentNodes(Set keys) {

		Set foundKeys = new HashSet(keys.size());

		/** Map of ContentKey -> (Map of String (service name) -> ContentNodeI) */
		Map nodeMapsByKey = new HashMap(keys.size());
		for (Iterator j = contentServices.iterator(); j.hasNext();) {
			ContentServiceI service = (ContentServiceI) j.next();

			long t = System.currentTimeMillis();

			Map serviceNodes = service.getContentNodes(keys);

			t = System.currentTimeMillis() - t;
			LOGGER.debug("getContentNodes() " + service.getName() + " " + keys.size() + " took " + t);

			for (Iterator k = keys.iterator(); k.hasNext();) {
				ContentKey key = (ContentKey) k.next();
				ContentNodeI node = (ContentNodeI) serviceNodes.get(key);
				if (node == null) {
					node = service.createPrototypeContentNode(key);
				} else {
					foundKeys.add(key);
				}
				if (node != null) {
					Map nodeMap = (Map) nodeMapsByKey.get(node.getKey());
					if (nodeMap == null) {
						nodeMap = new LinkedHashMap();
						nodeMapsByKey.put(node.getKey(), nodeMap);
					}
					nodeMap.put(service.getName(), node);
				}
			}
		}

		/** Map of ContentKey -> CompositeContentNode */
		Map nodesByKey = new HashMap(keys.size());
		for (Iterator i = foundKeys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			Map nodes = (Map) nodeMapsByKey.get(key);
			if (!nodes.isEmpty()) {
				nodesByKey.put(key, new CompositeContentNode(this, key, nodes));
			}
		}

		return nodesByKey;
	}

	public ContentNodeI createPrototypeContentNode(ContentKey cKey) {
		Map nodes = new HashMap();
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

	public Set getContentKeysByType(ContentType type) {
		Set set = new HashSet();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			set.addAll(service.getContentKeysByType(type));
		}
		return set;
	}

	public Set getContentKeys() {
		Set set = new HashSet();
		for (Iterator i = contentServices.iterator(); i.hasNext();) {
			ContentServiceI service = (ContentServiceI) i.next();
			set.addAll(service.getContentKeys());
		}
		return set;
	}

	public Set getParentKeys(ContentKey key) {
		Set keySet = new HashSet();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			Set keys = service.getParentKeys(key);

			keySet.addAll(keys);
		}

		return keySet;
	}

	public CmsResponseI handle(CmsRequestI request) {

		Map reqByService = decomposeRequest(request);

		// execute requests
		for (Iterator i = contentServices.iterator(); i.hasNext();) {
			ContentServiceI service = (ContentServiceI) i.next();
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
	private Map decomposeRequest(CmsRequestI request) {
		Map reqByService = new HashMap();

		// decompose request with composite nodes
		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			if (!(node instanceof CompositeContentNode)) {
				throw new IllegalArgumentException("Node " + node + " is not a composite");
			}
			Map nodes = ((CompositeContentNode) node).getNodes();
			for (Iterator j = nodes.entrySet().iterator(); j.hasNext();) {
				Map.Entry e = (Map.Entry) j.next();
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

}