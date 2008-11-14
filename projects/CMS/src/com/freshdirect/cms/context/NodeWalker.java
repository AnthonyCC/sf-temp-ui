package com.freshdirect.cms.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.fdstore.FDContentTypes;

/**
 * Walker with two tasks
 * 1. Checks if context is NOT hidden and searchable.
 * 2. Retrieves the topmost parent node which supposed to be a Department node.
 *      Otherwise the context is orphaned.
 *
 * The walk starts at a Product node and test each nodes by going up
 * parents (Categories) until it reaches a Department node. That means
 * walk won't stop if test results positive answer.
 * 
 * Walker will stop if the next parent node is null (no Department found).
 * 
 * @author segabor
 *
 */
public class NodeWalker extends ContextWalker {
	public NodeWalker(ContextualContentNodeI node) {
		super(node);
	}

	public NodeWalker walkWithMe() {
		super.walk(); return this;
	}


	/**
	 * Stops when a department reached.
	 */
	public boolean shouldStop(ContentNodeI n) {
		return FDContentTypes.DEPARTMENT.equals(n.getDefinition().getType());
	}


	/**
	 * Tests an inner node against hidden / NOT searchable status.
	 * NOTE: it returns the reversed state (True <=> HIDDEN(node) or NOT_SEARCHABLE(node)
	 */
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


	/**
	 * Utility method.
	 * 
	 * It calculates rank of this context for Primary Home Selector
	 * 
	 * 	RANK(node) = sizeof(departments)*(1=node is valid, otherwise 0)
	 *               + position of department in which node is
	 *               
	 * NOTE: Call after context (path) is walked.
	 */
	public int getRank(List deptKeys) {
		final int n_depts = deptKeys.size();
		ContentNodeI t = getTerminalNode(); // := [department] or null (it should not be)
		return (isValid() ? n_depts : 0 ) + (n_depts-1)-(t == null ? 0 : deptKeys.indexOf(t.getKey()) );
	}


	/**
	 * Utility method that sorts out orphaned contexts.
	 * 
	 * @param it	Iterator of List<Context> set
	 * @param svc	ContextService instance.
	 * @exception UnsupportedOperationException Thrown if iterator cannot remove from the set it belongs to 
	 */
	public static void filterOrphanedParents(Iterator it, ContextService svc) throws UnsupportedOperationException {
		while (it.hasNext()) {
			Context ctx = (Context) it.next();
			NodeWalker nw1 = new NodeWalker(svc.getContextualizedContentNode(ctx)).walkWithMe();
			if (nw1.getTerminalNode() == null) {
				it.remove();
			}
		}
	}
	

	/**
	 * Returns 'visible' parents (not hidden and searchable).
	 * 
	 * @param it	Iterator of List<Context> set
	 * @param svc	ContextService instance.
	 * @return	UnsupportedOperationException Thrown if iterator cannot remove from the set it belongs to 
	 */
	public static List getVisibleParents(Iterator it, ContextService svc) {
		List ctxs = new ArrayList();
		
		while (it.hasNext()) {
			Context ctx = (Context) it.next();
			NodeWalker nw1 = new NodeWalker(svc.getContextualizedContentNode(ctx)).walkWithMe();
			if (!nw1.getTestResult()) {
				ctxs.add(ctx);
			}
		}

		return ctxs;
	}


	public static Comparator getRankedComparator(ContextService service, List deptKeys) {
		return new RankedContextComparator(service, deptKeys);
	}
}




class RankedContextComparator implements Comparator {
	ContextService	svc;
	List			deptKeys;
	
	public RankedContextComparator(ContextService service, List deptKeys) {
		this.svc = service;
		this.deptKeys = deptKeys;
	}
	
	
	public int compare(Object o1, Object o2) {
		NodeWalker nw1 = new NodeWalker(svc.getContextualizedContentNode((Context) o1)).walkWithMe();
		NodeWalker nw2 = new NodeWalker(svc.getContextualizedContentNode((Context) o2)).walkWithMe();

		int v1 = nw1.getRank(deptKeys);
		int v2 = nw2.getRank(deptKeys);

		return v2-v1;
	}
}
