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
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ChangedContentNode;
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
	public ContentNodeI getContentNode(ContentKey cKey, DraftContext draftContext) {
		Map<String, ContentNodeI> nodes = new LinkedHashMap<String, ContentNodeI>();
		boolean found = false;
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			if (service.getTypeService().getContentTypeDefinition(cKey.getType()) == null) {
				continue;
			}
			ContentNodeI node = service.getContentNode(cKey, draftContext);
			if (node == null) {
				node = service.createPrototypeContentNode(cKey, draftContext);
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
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {

		Set<ContentKey> foundKeys = new HashSet<ContentKey>(keys.size());

		/** Map of ContentKey -> (Map of String (service name) -> ContentNodeI) */
		Map<ContentKey, Map<String, ContentNodeI>> nodeMapsByKey = new HashMap<ContentKey, Map<String, ContentNodeI>>(keys.size());
		for (Iterator<ContentServiceI> j = contentServices.iterator(); j.hasNext();) {
			ContentServiceI service = (ContentServiceI) j.next();

			long t = System.currentTimeMillis();

			Map<ContentKey, ContentNodeI> serviceNodes = service.getContentNodes(keys, draftContext);

			t = System.currentTimeMillis() - t;
			LOGGER.debug("getContentNodes() " + service.getName() + " " + keys.size() + " took " + t);

			for (Iterator<ContentKey> k = keys.iterator(); k.hasNext();) {
				ContentKey key = (ContentKey) k.next();
				ContentNodeI node = (ContentNodeI) serviceNodes.get(key);
				if (node == null) {
					node = service.createPrototypeContentNode(key, draftContext);
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
	public ContentNodeI createPrototypeContentNode(ContentKey cKey, DraftContext draftContext) {
		Map<String, ContentNodeI> nodes = new HashMap<String, ContentNodeI>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			ContentNodeI newNode = service.createPrototypeContentNode(cKey, draftContext);
			if (newNode != null)
				nodes.put(service.getName(), newNode);
		}
		if (nodes.size() == 0) {
			return null;
		}
		return new CompositeContentNode(this, cKey, nodes);
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		Set<ContentKey> set = new HashSet<ContentKey>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			set.addAll(service.getContentKeysByType(type, draftContext));
		}
		return set;
	}

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
		Set<ContentKey> set = new HashSet<ContentKey>();
		for (Iterator<ContentServiceI> i = contentServices.iterator(); i.hasNext();) {
			ContentServiceI service =  i.next();
			set.addAll(service.getContentKeys(draftContext));
		}
		return set;
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		Set<ContentKey> keySet = new HashSet<ContentKey>();
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			Set<ContentKey> keys = service.getParentKeys(key, draftContext);

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
		
		CmsResponseI response = new CmsResponse();

		return response;
	}

	/**
	 * @return Map of String (serviceName) -> CmsRequest
	 */
    private Map<String, CmsRequest> decomposeRequest(CmsRequestI request) {
        Map<String, CmsRequest> reqByService = new HashMap<String, CmsRequest>();

        // decompose request with composite nodes
        for (ContentNodeI node : request.getNodes()) {
            if (node instanceof ChangedContentNode) {
                decomposeChangedContentNode((ChangedContentNode) node, request, reqByService);
            } else if (node instanceof CompositeContentNode) {
                decomposeNode((CompositeContentNode) node, request, reqByService);
            } else {
                throw new IllegalArgumentException("Node " + node + " is not a composite");
            }
        }
        return reqByService;
    }

    private CmsRequest createServiceSpecificCmsRequest(final CmsRequestI originalRequest, final String serviceName, Map<String, CmsRequest> reqByService) {
        CmsRequest serviceReq = (CmsRequest) reqByService.get(serviceName);
        if (serviceReq == null) {
            serviceReq = new CmsRequest(originalRequest.getUser(), originalRequest.getSource(), originalRequest.getDraftContext(), originalRequest.getRunMode());
            reqByService.put(serviceName, serviceReq);
        }
        return serviceReq;
    }
    
    /**
     * Break down a composite content node to single content nodes
     * 
     * @param compositeNode
     * @param request original CMS request
     * @param reqByService map of service names and CMS requests
     */
    private void decomposeNode(CompositeContentNode compositeNode, CmsRequestI request, Map<String, CmsRequest> reqByService) {
        for (Map.Entry<String, ContentNodeI> e : compositeNode.getNodes().entrySet()) {
            String serviceName = (String) e.getKey();
            ContentNodeI serviceNode = (ContentNodeI) e.getValue();

            final CmsRequest serviceReq = createServiceSpecificCmsRequest(request, serviceName, reqByService);

            serviceReq.addNode(serviceNode);
        }

    }    
    
    /**
     * Break down a changed composite content node to single content nodes
     * 
     * @param compositeNode
     * @param request original CMS request
     * @param reqByService map of service names and CMS requests
     */
    private void decomposeChangedContentNode(ChangedContentNode changedNode, CmsRequestI request, Map<String, CmsRequest> reqByService) throws IllegalArgumentException {
        Map<String, Object> changedAttributes = changedNode.getChanges();

        ContentNodeI node = changedNode.getWrappedNode();
        if (!(node instanceof CompositeContentNode)) {
            throw new IllegalArgumentException("Changed Node " + node + " is not a composite");
        }
        
        final CompositeContentNode compositeNode = (CompositeContentNode) node;

        for (Map.Entry<String, ContentNodeI> e : compositeNode.getNodes().entrySet()) {
            final String serviceName = (String) e.getKey();
            final ContentNodeI serviceNode = (ContentNodeI) e.getValue();

            // create a service specific CMS request
            final CmsRequest serviceReq = createServiceSpecificCmsRequest(request, serviceName, reqByService);

            Set<String> attrNames = new HashSet<String>();
            attrNames.addAll(changedAttributes.keySet());
            attrNames.retainAll(serviceNode.getDefinition().getAttributeNames());

            if (attrNames.isEmpty()) {
                // no change can be applied to the decomposed node
                serviceReq.addNode(serviceNode);
            } else {
                // transfer applicable changed values
                Map<String,Object> serviceSpecificChanges = new HashMap<String,Object>();
                for (final String name : attrNames) {
                    serviceSpecificChanges.put(name, changedAttributes.get(name));
                }

                if (serviceSpecificChanges.isEmpty()) {
                    serviceReq.addNode( serviceNode );
                } else {
                    // re-wrap single node
                    ChangedContentNode cNode = new ChangedContentNode(serviceNode, serviceSpecificChanges );
                    serviceReq.addNode( cNode );
                }
                
            }
        }

    }
    
	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		Map<String, ContentNodeI> nodes = new LinkedHashMap<String, ContentNodeI>();
		boolean found = false;
		for (int i = 0; i < contentServices.size(); i++) {
			ContentServiceI service = (ContentServiceI) contentServices.get(i);
			if (service.getTypeService().getContentTypeDefinition(key.getType()) == null) {
				continue;
			}
			ContentNodeI node = service.getRealContentNode(key, draftContext);
			if (node == null) {
				node = service.createPrototypeContentNode(key, draftContext);
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