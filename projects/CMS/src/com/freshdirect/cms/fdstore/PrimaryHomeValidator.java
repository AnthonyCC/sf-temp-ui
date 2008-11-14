package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.NodeWalker;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Ensures that the <code>PRIMARY_HOME</code> attribute points
 * to a valid parent for nodes of type <code>Product</code>. 
 */
public class PrimaryHomeValidator implements ContentValidatorI {
	private static final ContentKey FD_ROOT_KEY = ContentKey.decode("Store:FreshDirect");
	private final List deptKeys;


	public PrimaryHomeValidator() {
		deptKeys = new ArrayList(CmsManager.getInstance().getContentNode(FD_ROOT_KEY).getChildKeys());
	}




	public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
		ContentType t = node.getKey().getType();
		if (FDContentTypes.PRODUCT.equals(t)) {
			ContentKey priHome = (ContentKey) node.getAttribute("PRIMARY_HOME").getValue();

			Set parentKeys = service.getParentKeys(node.getKey());
			if (parentKeys.isEmpty()) {
				// new or orphaned node, leave-as is
				return;
			}

			if (priHome == null || !parentKeys.contains(priHome)) {
				ContextService myService = new ContextService(service);
				
				/*
				 * Get all possible paths going from this node to the root.
				 * Context := <this node>->Cat(->Cat)*->Dept
				 */
				List ctxs = new ArrayList(myService.getAllContextsOf(node.getKey()));
				
				// Remove orphaned contexts
				NodeWalker.filterOrphanedParents(ctxs.iterator(), myService);


				Collections.sort(ctxs, NodeWalker.getRankedComparator(myService, deptKeys)); // sort keys by rank
				// new primary home := parent (category) node of the best scored contextualized node (that is a product)
				ContentKey ph = ctxs.size() == 0 ? null : myService
						.getContextualizedContentNode((Context) ctxs.get(0))
						.getParentNode().getKey();

				
				
				if (request == null) {
					delegate.record(node.getKey(), "PRIMARY_HOME", "Primary home should be reassigned to " + parentKeys);
					return;
				}
				
				ContentNodeI clone = node.copy();
				clone.getAttribute("PRIMARY_HOME").setValue(ph);

				request.addNode(clone);
			}
		}
	}
}
