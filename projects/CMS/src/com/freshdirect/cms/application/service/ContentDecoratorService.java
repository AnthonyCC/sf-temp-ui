/*
 * Created on Feb 8, 2005
 */
package com.freshdirect.cms.application.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.node.NodeWrapperI;

/**
 * Proxy content service that invokes a series of content decorators on
 * retrieved nodes.
 * 
 * @TODO refactor node wrapping
 * @FIXME only the first decorator can affect a node
 * 
 * @see com.freshdirect.cms.application.service.ContentDecoratorI
 */
public class ContentDecoratorService extends ProxyContentService {

	private final ContentDecoratorI[] decorators;

	public ContentDecoratorService(ContentServiceI service, List decorators) {
		super(service);
		this.decorators = (ContentDecoratorI[]) decorators.toArray(new ContentDecoratorI[decorators.size()]);
	}

	public ContentNodeI getContentNode(ContentKey key) {
		ContentNodeI node = super.getContentNode(key);
		if (node != null) {
			ContentNodeI decoratedNode = decorate(node);
			if (decoratedNode != null) {
				node = decoratedNode;
			}
		}
		return node;
	}

	public Map getContentNodes(Set keys) {
		Map m = super.getContentNodes(keys);
		for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			ContentNodeI node = (ContentNodeI) entry.getValue();
			ContentNodeI decoratedNode = decorate(node);
			if (decoratedNode != null) {
				entry.setValue(decoratedNode);
			}
		}
		return m;
	}

	public CmsResponseI handle(CmsRequestI request) {
		// FIXME node wrapping is a bit of a hack
		// it's to fulfill the contract "you handle what you produced"
		CmsRequest r = new CmsRequest(request.getUser());
		for (Iterator i = request.getNodes().iterator(); i.hasNext(); ) {
			ContentNodeI node = (ContentNodeI)i.next();
			if (node instanceof NodeWrapperI) {
				node = ((NodeWrapperI)node).getWrappedNode();
			}
			r.addNode(node);
		}
		return super.handle(r);
	}
	
	private ContentNodeI decorate(ContentNodeI node) {
		for (int i = 0; i < decorators.length; i++) {
			ContentNodeI n = decorators[i].decorateNode(node);
			if (n != null) {
				return n;
			}
		}
		return null;
	}

}