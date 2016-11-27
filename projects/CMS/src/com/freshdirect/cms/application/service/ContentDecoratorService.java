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
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ChangedContentNode;
import com.freshdirect.cms.node.NodeWrapperI;

/**
 * Proxy content service that invokes a series of content decorators on retrieved nodes.
 * 
 * @TODO refactor node wrapping
 * @FIXME only the first decorator can affect a node
 * 
 * @see com.freshdirect.cms.application.service.ContentDecoratorI
 */
public class ContentDecoratorService extends ProxyContentService {

    private final ContentDecoratorI[] decorators;

    public ContentDecoratorService(ContentServiceI service, List<ContentDecoratorI> decorators) {
        super(service);
        this.decorators = (ContentDecoratorI[]) decorators.toArray(new ContentDecoratorI[decorators.size()]);
    }

    @Override
    public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
        ContentNodeI node = super.getContentNode(key, draftContext);
        if (node != null) {
            ContentNodeI decoratedNode = decorate(node, draftContext);
            if (decoratedNode != null) {
                node = decoratedNode;
            }
        }
        return node;
    }

    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
        Map<ContentKey, ContentNodeI> m = super.getContentNodes(keys, draftContext);
        for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            ContentNodeI node = (ContentNodeI) entry.getValue();
            ContentNodeI decoratedNode = decorate(node, draftContext);
            if (decoratedNode != null) {
                entry.setValue(decoratedNode);
            }
        }
        return m;
    }

    @Override
    public CmsResponseI handle(CmsRequestI request) {
        // FIXME node wrapping is a bit of a hack
        // it's to fulfill the contract "you handle what you produced"
        CmsRequest r = new CmsRequest(request.getUser(), request.getSource(), request.getDraftContext(), request.getRunMode());
        for (ContentNodeI node : request.getNodes()) {
            if (node instanceof ChangedContentNode) {
                // avoid unwrapping change node, instead unwrap inner node
                final ChangedContentNode cNode = (ChangedContentNode) node;
                if (cNode.getWrappedNode() instanceof NodeWrapperI) {
                    // clone changed node with unwrapped inner node but with same changes
                    ChangedContentNode clone = new ChangedContentNode(((NodeWrapperI) cNode.getWrappedNode()).getWrappedNode(), cNode.getChanges());
                    r.addNode(clone);
                } else {
                    r.addNode(cNode);
                }
            } else if (node instanceof NodeWrapperI) {
                r.addNode(((NodeWrapperI) node).getWrappedNode());
            } else {
                r.addNode(node);
            }
        }
        return super.handle(r);
    }

    private ContentNodeI decorate(ContentNodeI node, DraftContext draftContext) {
        for (int i = 0; i < decorators.length; i++) {
            ContentNodeI n = decorators[i].decorateNode(node, getProxiedService(), draftContext);
            if (n != null) {
                return n;
            }
        }
        return null;
    }

}
