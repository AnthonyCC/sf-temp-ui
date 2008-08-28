package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextWalker;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Ensures that the <code>PRIMARY_HOME</code> attribute points
 * to a valid parent for nodes of type <code>Product</code>. 
 */
public class PrimaryHomeValidator implements ContentValidatorI {
	private static final ContentKey FD_ROOT_KEY = ContentKey.decode("Store:FreshDirect");
	private final List deptKeys;
	
	private class ContextComparator implements Comparator {
		ContextService svc;
		
		public ContextComparator(ContextService service) {
			this.svc = service;
		}
		
		
		public int compare(Object o1, Object o2) {
			NodeWalker nw1 = new NodeWalker(svc.getContextualizedContentNode((Context) o1)).walkWithMe();
			NodeWalker nw2 = new NodeWalker(svc.getContextualizedContentNode((Context) o2)).walkWithMe();

			int v1 = nw1.getRank(deptKeys);
			int v2 = nw2.getRank(deptKeys);

			return v2-v1;
		}
	}

	
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
				Collections.sort(ctxs, new ContextComparator(myService)); // sort keys by rank
				// new primary home := parent (category) node of the best scored contextualized node (that is a product)
				ContentKey ph = myService.getContextualizedContentNode((Context)ctxs.get(0)).getParentNode().getKey();

				
				
				// ++++ DEBUG
				/// debugPriHomeSelection(ctxs, myService, node);

				
				
				// --- old code --
				// ContentKey ph = (ContentKey) parentKeys.iterator().next();

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
	
	
	/**
	 * Debug primary home selection logic.
	 * 
	 * @param ctxs leaf's all contexts
	 * @param svc 
	 * @param leaf
	 * 
	 */
	private void debugPriHomeSelection(List ctxs, ContextService svc, ContentNodeI leaf) {
		final int s = deptKeys.size();

		System.out.println("[XXXX] Ordered list of departments");
		for (int z=0; z<s; z++) {
			int p = (s-1)-z;
			System.out.println("[" + p + "]: " + ((ContentKey)deptKeys.get(z)).getContentNode().getLabel());
		}

		System.out.println("\n\n");
		System.out.println("[XXXX] Sorted list of possible parents of " + leaf.getLabel());
		for (Iterator x=ctxs.iterator(); x.hasNext();) {
			Context ctx = (Context) x.next();
			NodeWalker nw = new NodeWalker(svc.getContextualizedContentNode(ctx)).walkWithMe();
			System.out.println(ctx.getLabel() + " dept: " + nw.getTerminalNode().getLabel() + "; rank: " + nw.getRank(deptKeys));
		}
		
		ContentKey bestKey = svc.getContextualizedContentNode((Context)ctxs.get(0)).getParentNode().getKey();
		
		System.out.println("THE WINNER IS: " + bestKey);
		System.out.println("\n\n");
	}
}



class NodeWalker extends ContextWalker {
	public NodeWalker(ContextualContentNodeI node) {
		super(node);
	}

	public NodeWalker walkWithMe() {
		super.walk(); return this;
	}

	
	public boolean shouldStop(ContentNodeI n) {
		return FDContentTypes.DEPARTMENT.equals(n.getDefinition().getType());
	}

	// returns true if node is hidden OR NOT searchable
	public boolean test(ContentNodeI n) {
		Map attrs = n.getAttributes();
		AttributeI a_hu = (AttributeI) attrs.get("HIDE_URL");
		AttributeI a_ns = (AttributeI) attrs.get("NOT_SEARCHABLE");

		return ( (a_hu != null && a_hu.getValue() != null) ||
			(a_ns != null && Boolean.TRUE.equals(a_ns.getValue()) )
		);
	}

	public boolean isValid() {
		return !testResult;
	}


	// call only after context walked
	// RANK(node) = size(departments)*(1=node is valid, otherwise 0)
	//               + position of department in which node is
	public int getRank(List deptKeys) {
		final int s = deptKeys.size();
		return (isValid() ? s : 0 ) + (s-1)-deptKeys.indexOf(getTerminalNode().getKey());
	}
}
