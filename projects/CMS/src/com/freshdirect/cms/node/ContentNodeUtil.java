package com.freshdirect.cms.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.fdstore.ConfiguredProductLabelProvider;
import com.freshdirect.cms.fdstore.DomainValueLabelProvider;
import com.freshdirect.cms.fdstore.ErpMaterialLabelProvider;
import com.freshdirect.cms.fdstore.MediaLabelProvider;
import com.freshdirect.cms.fdstore.SearchRelevancyLabelProvider;
import com.freshdirect.cms.fdstore.SkuLabelProvider;
import com.freshdirect.cms.labels.AttributeLabelProvider;
import com.freshdirect.cms.labels.CompositeLabelProvider;
import com.freshdirect.cms.labels.ILabelProvider;
import com.freshdirect.cms.meta.ContentTypeUtil;

/**
 * Provides utility methods to handle content nodes.
 * 
 * @TODO move label providers to a hivemind config
 */
public class ContentNodeUtil {

	private final static ILabelProvider LABEL_PROVIDER = new CompositeLabelProvider(new ILabelProvider[] {
		new DomainValueLabelProvider(),
		new MediaLabelProvider(),
		new SkuLabelProvider(),
		new ConfiguredProductLabelProvider(),
		new ErpMaterialLabelProvider(),
		new SearchRelevancyLabelProvider(),
		new AttributeLabelProvider("FULL_NAME"),
		new AttributeLabelProvider("NAV_NAME"),
		new AttributeLabelProvider("GLANCE_NAME"),
		new AttributeLabelProvider("name"),
		new AttributeLabelProvider("title"),
		new AttributeLabelProvider("attribute")});

	/**
	 * Get a human-readable label for a content node.
	 * 
	 * @param node content node (never null)
	 * @return human readable label
	 */
	public static String getLabel(ContentNodeI node) {
		return LABEL_PROVIDER.getLabel(node);
	}

	/**
	 * Get a content node while traversing navigable nodes as well.
	 * 
	 * @FIXME eagerFetch is to pre-warm caches with related nodes as well
	 * 
	 * @param key
	 * @param navigableOnly
	 * @return
	 */
	public static ContentNodeI eagerFetch(ContentKey key, boolean navigableOnly) {
		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		if (node != null) {
			Set<ContentKey> relKeys = collectRelatedKeys(node, navigableOnly);
			CmsManager.getInstance().getContentNodes(relKeys);
		}
		return node;
	}

	/**
	 * Get all navigable keys of a node.
	 * 
	 * @param node content node, never null
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set<ContentKey> getChildKeys(ContentNodeI node) {
		return collectRelatedKeys(node, true);
	}

	/**
	 * Recursively collect all reachable keys of the specified type.
	 * 
	 * @param node root node, never null
	 * @param type content type to search (null for all types)
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set<ContentKey> collectReachableKeys(ContentNodeI node, ContentType type) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		collectReachableKeys(s, type, node);
		return s;
	}

	private static void collectReachableKeys(Set<ContentKey> collectedKeys, ContentType targetType, ContentNodeI root) {
		Set<ContentKey> children = root.getChildKeys();
		ContentTypeServiceI ts = CmsManager.getInstance().getTypeService();		
		for ( ContentKey k : children ) {
			if (targetType == null || targetType.equals(k.getType())) {
				collectedKeys.add(k);
			}
			// recursion, if node may have reachable keys of targetType
			ContentTypeDefI def = ts.getContentTypeDefinition(k.getType());
			Set<ContentType> reachableTypes = ContentTypeUtil.getReachableContentTypes(ts, def);
			if (reachableTypes.contains(targetType)) {
				collectReachableKeys(collectedKeys, targetType, k.getContentNode());
			}
		}
	}

	/**
	 * Get all reachable keys of a node.
	 * 
	 * @param node content node, never null
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set<ContentKey> getAllRelatedContentKeys(ContentNodeI node) {
		return collectRelatedKeys(node, false);
	}

	/**
	 * Add a content key to a relationship. If it's cardinality ONE, set the key as the value.
	 * For cardinality MANY, it ensures uniqueness of keys.
	 * 
	 * @param relationship
	 * @param key
	 * 
	 * @return true if the key was added/set
	 */
	public static boolean addRelationshipKey(RelationshipI relationship, ContentKey key) {
		if (EnumCardinality.MANY.equals(relationship.getDefinition().getCardinality())) {
			List<ContentKey> value = (List<ContentKey>) relationship.getValue();
			if (value == null) {
				value = new ArrayList<ContentKey>();
				relationship.setValue(value);
			} else {
				if (value.contains(key)) {
					return false;
				}
			}
			value.add(key);
		} else {
			relationship.setValue(key);
		}
		return true;
	}

	private static Set<ContentKey> collectRelatedKeys(ContentNodeI node, boolean navigableOnly) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		Map<String, AttributeI> attrs = node.getAttributes();

		for (AttributeI attr : attrs.values()) {
			if (attr instanceof RelationshipI) {
				RelationshipI rel = (RelationshipI) attr;
				RelationshipDefI relDef = (RelationshipDefI) rel.getDefinition();
				if (!navigableOnly || relDef.isNavigable()) {
					if (EnumCardinality.ONE.equals(relDef.getCardinality())) {
						if (rel.getValue() != null) {
							s.add((ContentKey) rel.getValue());
						}
					} else {
						if (rel.getValue() != null) {
							s.addAll((List<ContentKey>) rel.getValue());
						}
					}
				}
			}
		}
		return s;
	}

	/**
	 * Get an index of inverse (ie. child -> parent) relationships.
	 * 
	 * @return Map of {@link ContentKey} (child) -> Set of {@link ContentKey} (parents)
	 */
	public static Map<ContentKey, Set<ContentKey>> getParentIndex(Collection<ContentNodeI> nodes) {
		Map<ContentKey, Set<ContentKey>> parentsByKey = new HashMap<ContentKey,Set<ContentKey>> (nodes.size());

		for (ContentNodeI node : nodes) {
			Set<ContentKey> childKeys = ContentNodeUtil.getChildKeys(node);

			for (ContentKey childKey : childKeys) {
				Set<ContentKey> parentKeys = parentsByKey.get(childKey);
				if (parentKeys == null) {
					parentKeys = new HashSet<ContentKey>();
				}
				parentKeys.add(node.getKey());
				parentsByKey.put(childKey, parentKeys);
			}

		}
		return parentsByKey;
	}

	/*
	 public static boolean equalNodes(ContentNode node, Object obj) {
	 if (obj == this) {
	 return true;
	 }
	 if (obj instanceof ContentNodeI) {
	 if (!key.equals(((ContentNodeI) obj).getKey()))
	 return false;

	 ContentNodeI node = (ContentNodeI) obj;

	 Map attrs = getAttributes();
	 for (Iterator i = attrs.values().iterator(); i.hasNext();) {
	 AttributeI myAttr = (AttributeI) i.next();
	 AttributeI newAttr = node.getAttribute(myAttr.getName());
	 if (!newAttr.equals(myAttr))
	 return true;
	 }
	 return false;
	 }
	 return false;
	 }
	 */
	
	
	public static void createNode(ContentKey key, CmsRequest request, AttributeI attr){
        ContentNodeI node = key.lookupContentNode();
        if(node == null){
        	node = CmsManager.getInstance().createPrototypeContentNode(key);
            request.addNode(node);
        }
        
        setRelationshipValue(key.getId(), attr);
	}

	public static void setRelationshipValue(String value, AttributeI attr) {
		RelationshipI r = (RelationshipI) attr;
		RelationshipDefI relDef = (RelationshipDefI) r.getDefinition();
		if (relDef.getContentTypes().size() != 1) {
			throw new IllegalArgumentException(relDef.toString());
		}
		ContentType t = (ContentType) relDef.getContentTypes().iterator().next();

		ContentKey key = new ContentKey(t, value);
		if (r.getDefinition().getCardinality().equals(EnumCardinality.MANY)) {
			List<ContentKey> list = new ArrayList<ContentKey>();
			list.add(key);
			attr.setValue(list);
		} else {
			attr.setValue(key);
		}
	}
	
    /**
     * Return a string attribute
     * 
     * @param node
     * @param name
     * @return
     */
    public static String getStringAttribute(ContentNodeI node, String name) {
        AttributeI attribute = node.getAttribute(name);
        if (attribute != null) {
            Object value = attribute.getValue();
            if (value instanceof String) {
                return (String) value;
            }
        }
        return null;
    }


    /**
     * Return an integer attribute
     * 
     * @param node
     * @param name
     * @return
     */
    public static Integer getIntegerAttribute(ContentNodeI node, String name) {
        AttributeI attribute = node.getAttribute(name);
        if (attribute != null) {
            Object value = attribute.getValue();
            if (value instanceof Integer) {
                return (Integer) value;
            }
        }
        return null;
    }

    /**
     * Return a ContentKey attribute
     * 
     * @param node
     * @param name
     * @return
     */
    public static ContentKey getContentKeyAttribute(ContentNodeI node, String name) {
        AttributeI attribute = node.getAttribute(name);
        if (attribute != null) {
            Object value = attribute.getValue();
            if (value instanceof ContentKey) {
                return (ContentKey) value;
            }
        }
        return null;
    }
    
    
    
}