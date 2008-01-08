package com.freshdirect.fdstore.dcpd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Recipe;

public class DCPDQuery {
	List keys; 			// query keys
	List badKeys = Collections.EMPTY_LIST;

	List nodes = Collections.EMPTY_LIST;
	
	List deptNodes = Collections.EMPTY_LIST; 	// department model results
	List catNodes = Collections.EMPTY_LIST;		// category model results
	List rcpNodes = Collections.EMPTY_LIST;		// recipe results
	List otherNodes = Collections.EMPTY_LIST;	// other results (FDFolder, etc.)

	
	ContentFactory cf = ContentFactory.getInstance();

	public DCPDQuery(List queryKeys) {
		this.keys = queryKeys;
	}


	
	public List getBadKeys() {
		return badKeys;
	}


	public List getNodes() {
		return nodes;
	}


	public List getCatNodes() {
		return catNodes;
	}


	public List getDeptNodes() {
		return deptNodes;
	}


	public List getOtherNodes() {
		return otherNodes;
	}


	public List getRcpNodes() {
		return rcpNodes;
	}



	public boolean doQuery() {
		boolean ret = true;
		
		// reset results
		this.badKeys = new ArrayList();

		this.nodes = new ArrayList();

		this.deptNodes = new ArrayList();
		this.catNodes = new ArrayList();
		this.rcpNodes = new ArrayList();
		this.otherNodes = new ArrayList();

		
		//		 try to ...
		Iterator it=keys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			if ("".equals(key))
				continue;
			ContentNodeModel node = cf.getContentNode(key);
			if (node == null) {
				badKeys.add(key);
			} else {
				nodes.add(node);
				
				if (node instanceof DepartmentModel) {
					deptNodes.add(node);
				} else if (node instanceof CategoryModel) {
					catNodes.add(node);
				} else if (node instanceof Recipe) {
					rcpNodes.add(node);
				} else {
					otherNodes.add(node);
				}
			}
		}

		return ret;
	}


	/**
	 * Returns the count of resulted nodes
	 * @return count of results
	 */
	public int resultCount() {
		return catNodes.size() + deptNodes.size() + rcpNodes.size() + otherNodes.size();
	}
}
