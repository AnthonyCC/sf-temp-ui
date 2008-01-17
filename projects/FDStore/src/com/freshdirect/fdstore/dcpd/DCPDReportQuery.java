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

public class DCPDReportQuery {
	List nodes = Collections.EMPTY_LIST;
	//Holds the content keys of all the good nodes.
	List deptKeys;
	List catKeys;
	List rcpKeys;

	// list of invalid keys
	List badDeptKeys;
	List badCatKeys;
	List badRcpKeys;

	
	
	List contentKeys = Collections.EMPTY_LIST; // obsolete
	
	List deptNodes = Collections.EMPTY_LIST; 	// department model results
	List catNodes = Collections.EMPTY_LIST;		// category model results
	List rcpNodes = Collections.EMPTY_LIST;		// recipe results
	List otherNodes = Collections.EMPTY_LIST;	// other results (FDFolder, etc.)

	
	ContentFactory cf = ContentFactory.getInstance();

	public DCPDReportQuery(List deptKeys, List catKeys,	List rcpKeys) {
		this.deptKeys = deptKeys;
		this.catKeys = catKeys;
		this.rcpKeys = rcpKeys;
	}


	
	public List getNodes() {
		return nodes;
	}

	/**
	 * @return List of {@link CategoryModel} nodes
	 */
	public List getCatNodes() {
		return catNodes;
	}


	/**
	 * @return List of {@link DepartmentModel} nodes
	 */
	public List getDeptNodes() {
		return deptNodes;
	}


	/**
	 * @return List of {@link Recipe} nodes
	 */
	public List getRcpNodes() {
		return rcpNodes;
	}



	public List getDeptKeys() {
		return deptKeys;
	}



	public List getRcpKeys() {
		return rcpKeys;
	}



	public List getBadDeptKeys() {
		return badDeptKeys;
	}



	public List getBadCatKeys() {
		return badCatKeys;
	}



	public List getBadRcpKeys() {
		return badRcpKeys;
	}


	/**
	 * @deprecated
	 */
	public List getOtherNodes() {
		return otherNodes;
	}



	public List getContentKeys() {
		return contentKeys;
	}

	public boolean doQuery() {
		boolean ret = true;
		
		// reset results
		this.badDeptKeys = new ArrayList();
		this.badCatKeys = new ArrayList();
		this.badRcpKeys = new ArrayList();

		this.nodes = new ArrayList(); // all nodes
		this.contentKeys = new ArrayList(); // ??
		this.deptNodes = new ArrayList();
		this.catNodes = new ArrayList();
		this.rcpNodes = new ArrayList();

		
		// collect department nodes
		//
		Iterator it=deptKeys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			if ("".equals(key))
				continue;
			ContentNodeModel node = cf.getContentNode(key);
			if (!(node instanceof DepartmentModel)) {
				badDeptKeys.add(key);
			} else {
				nodes.add(node);
				contentKeys.add(node.getContentKey());
				deptNodes.add(node);
			}
		}


		// collect category nodes
		//
		it=catKeys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			if ("".equals(key))
				continue;
			ContentNodeModel node = cf.getContentNode(key);
			if (!(node instanceof CategoryModel)) {
				badCatKeys.add(key);
			} else {
				nodes.add(node);
				contentKeys.add(node.getContentKey());
				catNodes.add(node);
			}
		}


		// collect recipes
		//
		it=rcpKeys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			if ("".equals(key))
				continue;
			ContentNodeModel node = cf.getContentNode(key);
			if (!(node instanceof Recipe)) {
				badRcpKeys.add(key);
			} else {
				nodes.add(node);
				contentKeys.add(node.getContentKey());
				rcpNodes.add(node);
			}
		}

		return ret;
	}





	/**
	 * Returns the count of resulted nodes
	 * @return count of results
	 */
	public int resultCount() {
		return catNodes.size() + deptNodes.size() + rcpNodes.size();
	}
	
	
	public boolean hasBadKeys() {
		return badDeptKeys.size() > 0 || badCatKeys.size() > 0 || badRcpKeys.size() > 0;
	}
}
