package com.freshdirect.cms.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.fdstore.FDContentTypes;


/**
 * This class finds products having 'broken' primary home link.
 * It is used by hidden_categories.jsp report page.
 * 
 * @author segabor
 *
 */
public class HCProducer implements Runnable {
	public static final int INIT=0;
	public static final int START=1;
	public static final int GETTING_PRODUCTS=2;
	public static final int SEARCHING_FOR_BAD_NODES=3;
	public static final int END=4;

	public int status = INIT;
	
	private CmsManager mgr;
	private ContextService svc;

	// set contains dirty nodes (categories)
	private List dirtyCats = new ArrayList();

	private List badNodes; // list of processed nodes
	
	private int max_nodes = 0;
	private int processed = 0;
	
	private boolean debug = false; // show debug messages
	
	private boolean killMePlease = false;
	
	private int statNoPriHome = 0;
	private int statNoMultipleParents = 0;
	private int statPriHomeValid = 0;
	private int statNoValidAltHome = 0;
	
	public HCProducer(CmsManager mgr, ContextService svc) {
		this.mgr = mgr;
		this.svc = svc;

		badNodes = Collections.synchronizedList(new ArrayList());
	}

	
	public HCProducer(CmsManager mgr, ContextService svc, boolean debugFlag) {
		this(mgr, svc);
		
		this.debug = debugFlag;
	}
	
	public void run() {
		status = START;

		// collect nodes for the report

		status = GETTING_PRODUCTS;
		
		/*
		 * Find bad nodes
		 */
		Collection allProds = mgr.getContentKeysByType(FDContentTypes.PRODUCT);
		
		status = SEARCHING_FOR_BAD_NODES;
		
		int k = 0;
		max_nodes = allProds.size();
		for (Iterator it=allProds.iterator(); it.hasNext() && !killMePlease; k++) {
			processed = k;
			
			ContentKey prodKey = (ContentKey) it.next();
			
			// actual node
			ContentNodeI node = mgr.getContentNode(prodKey);
			
			// primary home
			ContentKey priHomeKey = (ContentKey) node.getAttributeValue("PRIMARY_HOME");
			
			if (priHomeKey == null)
				continue;
			
			// 1. is primary home valid? - find a node with bogus HOME link

			// get all parents
			List ctxs = new ArrayList(svc.getAllContextsOf(node.getKey()));
			if (ctxs.size() < 2) {
				// no multiple parents found
				statNoMultipleParents++;
				continue;
			}
			
			Context priHomeCtx = null;
			for (Iterator cit=ctxs.iterator(); priHomeCtx == null && cit.hasNext(); ) {
				Context c = (Context) cit.next();
				if (priHomeKey.equals(c.getParentContext().getContentKey())) {
					priHomeCtx = c.getParentContext();
				}
			}
			
			if (priHomeCtx == null) {
				statNoPriHome++;
				continue;
			}
			
			HCWalker w = new HCWalker(svc.getContextualizedContentNode(priHomeCtx), dirtyCats).walkWithMe();
			if (w.isValid()) {
				statPriHomeValid++;
				continue; // this home is cool, step over
			}
			// found a corrupted home link. find a good alternative
			Context alternativeParent = null;
			for (Iterator cit=ctxs.iterator(); cit.hasNext(); ) {
				Context c = (Context) cit.next();
				
				// skip parent home context
				if (c.getParentContext().getContentKey().equals(priHomeCtx.getContentKey()))
					continue;
				
				// walk the parent
				w = new HCWalker(svc.getContextualizedContentNode(c), dirtyCats).walkWithMe();
				if (w.isValid()) {
					alternativeParent = c;
					break;
				}
			}
			
			// Dangerous case - node's home is bad actually and we failed to find a good alternative -> TOO BAD
			if (alternativeParent == null) {
				statNoValidAltHome++;
				continue;
			}

			// -> add to report list
			badNodes.add(node);
		}

		status = END;
		
		// print stats
		System.err.println("HCProducer stats:");
		System.err.println("Max: " + max_nodes + " / No Pri Home: " + statNoPriHome + " / Pri Home Valid: " + statPriHomeValid  + " / No Valid Alt Home: " + statNoValidAltHome);
	}

	public int getStatus() {
		return status;
	}
	
	public int getMaxNodes() {
		return max_nodes;
	}
	
	public int getProcessedNodes() {
		return processed;
	}
	
	public List getBadNodes() {
		return this.badNodes;
	}
	
	public void killMe() {
		// abort the process
		killMePlease = true;
	}
	
	
	public Map getStats() {
		Map smap = new HashMap();
		
		smap.put("maxNodes", new Integer(max_nodes));
		smap.put("processedNodes", new Integer(processed));
		smap.put("statNoMultipleParents", new Integer(statNoMultipleParents));
		smap.put("statNoPriHome", new Integer(statNoPriHome));
		smap.put("statPriHomeValid", new Integer(statPriHomeValid));
		smap.put("statNoValidAltHome", new Integer(statNoValidAltHome));
		
		return smap;
	}
}
