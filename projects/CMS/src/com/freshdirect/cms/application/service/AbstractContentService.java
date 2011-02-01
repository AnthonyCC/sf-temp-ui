package com.freshdirect.cms.application.service;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * Supporting base class for content services. Service serialization is
 * handled by saving a reference to the service name in
 * {@link #writeReplace()}. The service gets reconstructed in
 * {@link AbstractContentService.ServiceRef#readResolve()}
 * via lookup in {@link com.freshdirect.framework.conf.FDRegistry}.
 */
public abstract class AbstractContentService implements ContentServiceI, Serializable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected Object writeReplace() throws ObjectStreamException {
		if (name == null) {
			throw new InvalidObjectException("Service name not set in " + this);
		}
		return new ServiceRef(getName());
	}

	private static class ServiceRef implements Serializable {
		private final String name;

		public ServiceRef(String name) {
			this.name = name;
		}

		protected Object readResolve() throws ObjectStreamException {
			ContentServiceI service = (ContentServiceI) FDRegistry.getInstance().getService(name, ContentServiceI.class);
			if (service == null) {
				throw new InvalidObjectException("Unable to find in FDRegistry: " + name);
			}
			return service;
		}

	}
		
	public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate criteria) {
		Set<ContentKey> keys = this.getContentKeysByType(type);
		Map<ContentKey, ContentNodeI> nodes = this.getContentNodes(keys);
		CollectionUtils.filter(nodes.values(), criteria);
		return nodes;
	}

}