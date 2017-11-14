package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.cms.core.domain.ContentKey;

public class ContentNodeTree {
	private static class TreeElementComparator implements Comparator<TreeElement> {
		Comparator<ContentNodeModel> comp;

		public TreeElementComparator(Comparator<ContentNodeModel> comp) {
			this.comp = comp;
		}

		@Override
        public int compare(TreeElement o1, TreeElement o2) {
			return comp.compare(o1.getModel(), o2.getModel());
		}
	}

	public static class NodeIterator implements Iterator<TreeElement> {
		List<TreeElement> list;
		int position = 0;

		NodeIterator(List<TreeElement> list) {
			this.list = list;
		}

		@Override
        public boolean hasNext() {
			return position < list.size();
		}

		@Override
        public TreeElement next() {
			TreeElement result = list.get(position);
			position++;
			return result;
		}

		public TreeElement peekNext() {
			return list.get(position);
		}

		@Override
        public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static class TreeElement {
		public static final Comparator<TreeElement> SORT_BY_PRODUCT_COUNT = new Comparator<TreeElement>() {
			@Override
			public int compare(TreeElement o1, TreeElement o2) {
				int d = o2.getChildCount() - o1.getChildCount();
				if (d != 0)
					return d;
				else {
					if (o1.getModel() != null && o1.getModel().getFullName() != null &&
							o2.getModel() != null && o2.getModel().getFullName() != null)
						return o1.getModel().getFullName().compareTo(o2.getModel().getFullName());
					else return 0;
				}
			}
		};

		ContentNodeModel model;
		Set<TreeElement> children;
		int depth;
		Set<ContentKey> deepChildren = new HashSet<ContentKey>();

		public TreeElement(ContentNodeModel model, TreeElementComparator comparator) {
			this.model = model;
			if (comparator == null) {
				children = new HashSet<TreeElement>();
			} else {
				children = new TreeSet<TreeElement>(comparator);
			}
		}

		public ContentNodeModel getModel() {
			return model;
		}

		public void addChild(TreeElement child) {
			this.children.add(child);
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

		public int getDepth() {
			return depth;
		}

		public int getChildCount() {
			return deepChildren.size();
		}

		public void incrementChildCount(ContentKey key) {
			// childCount++;
			deepChildren.add(key);
		}

		public Collection<TreeElement> getChildren() {
			return children;
		}

		public Set<ContentKey> getAllChildren() {
			return deepChildren;
		}

		@Override
		public String toString() {
			return "TreeElement [model=" + model + ", children=" + children + ", depth=" + depth + ", deepChildren=" + deepChildren
					+ "]";
		}
	}

	public static interface TreeElementFilter {
		public boolean accept(TreeElement element);
	}

	public static class UniqueProductFilter implements TreeElementFilter {
		Set<String> productIds = new HashSet<String>();

		@Override
        public boolean accept(TreeElement element) {
			if (element.getModel() instanceof ProductModel) {
				String id = element.getModel().getContentKey().id;
				if (!productIds.contains(id)) {
					productIds.add(id);
					return true;
				}
			}
			return false;
		}
	}

	final Set<TreeElement> roots;
	final Map<String, TreeElement> nodes = new HashMap<String, TreeElement>();
	Set<String> expandedElementIds;
	int expandNodesToDepth = -1;
	final TreeElementComparator childComparator;

	public ContentNodeTree() {
		roots = new HashSet<TreeElement>();
		childComparator = null;
	}

	public ContentNodeTree(Comparator<ContentNodeModel> rootComparator, Comparator<ContentNodeModel> childComparator) {
		this.roots = new TreeSet<TreeElement>(new TreeElementComparator(rootComparator));
		this.childComparator = new TreeElementComparator(childComparator);
	}

	/**
	 * Add node to the tree structure.
	 *
	 * @param model
	 * @return
	 */
	public TreeElement addNode(ContentNodeModel model) {
		ContentNodeModel current = model;
		TreeElement prev = null;
		// System.out.println("addNode "+model);
		TreeElement firstCreated = null;
		while (current != null) {
			TreeElement element = getTreeElement(current);
			if (element != null) {
				if (firstCreated == null) {
					firstCreated = element;
				}
				// incrementParents(element, model.getContentKey());
				if (prev != null) {
					element.addChild(prev);
				}
				return firstCreated;
			}
			element = createElement(current);
			if (firstCreated == null) {
				firstCreated = element;
			}
			// element.incrementChildCount(model.getContentKey());
			if (prev != null) {
				element.addChild(prev);
			}
			current = getParent(current);
			prev = element;
		}
		roots.add(prev);
		return firstCreated;
	}

	protected void addChildNode(ContentNodeModel model, String childId) {
		TreeElement parent = getTreeElement(model);
		TreeElement child = getTreeElement(childId);
		if (child != null) {
			if (parent == null) {
				parent = addNode(model);
				parent.addChild(child);
				incrementParents(parent, child.getModel().getContentKey());
			} else {
				parent.addChild(child);
				incrementParents(parent, child.getModel().getContentKey());
			}
		}
	}

	/**
	 * remove nodes which not accepted by the filter, and append their child nodes to the original parent node.
	 *
	 * @param filter
	 */
	public void purge(TreeElementFilter filter) {
		for (Iterator<TreeElement> iter = new HashSet<TreeElement>(roots).iterator(); iter.hasNext();) {
			TreeElement element = iter.next();
			purgeElement(filter, element, null);
		}
	}

	private void purgeElement(TreeElementFilter filter, TreeElement element, TreeElement parent) {
		if (filter.accept(element)) {
			// recurse ...
			for (Iterator<TreeElement> iter = new HashSet<TreeElement>(element.getChildren()).iterator(); iter.hasNext();) {
				TreeElement child = iter.next();
				purgeElement(filter, child, element);
			}
		} else {
			// System.out.println("REMOVE node :" +element.getModel().getFullName() +" parent:"+parent);
			// remove from the tree
			if (parent == null) {
				roots.remove(element);
				roots.addAll(element.getChildren());
			} else {
				parent.getChildren().remove(element);
				parent.getChildren().addAll(element.getChildren());
			}
			// recurse with the new parent
			for (Iterator<TreeElement> iter = element.getChildren().iterator(); iter.hasNext();) {
				TreeElement child = iter.next();
				purgeElement(filter, child, parent);
			}
		}
	}

	protected void incrementParents(TreeElement element, ContentKey key) {
		do {
			element.incrementChildCount(key);
			element = getParentElement(element);
		} while (element != null);
	}

	// Set<TreeElement>
	public Set<TreeElement> getRoots() {
		return roots;
	}

	public TreeElement getTreeElement(String contentKey) {
		Object element = nodes.get(contentKey);
		if (element instanceof TreeElement) {
			return (TreeElement) element;
		}
		return null;
	}

	public TreeElement getTreeElement(ContentNodeModel model) {
		Object element = nodes.get(model.getContentKey().id);
		if (element instanceof TreeElement) {
			return (TreeElement) element;
		}
		return null;
	}

	protected TreeElement getParentElement(TreeElement element) {
		ContentNodeModel parent = getParent(element.getModel());
		return parent != null ? getTreeElement(parent) : null;
	}

	protected TreeElement createElement(ContentNodeModel model) {
		TreeElement treeElement = new TreeElement(model, childComparator);
		nodes.put(model.getContentKey().id, treeElement);
		return treeElement;
	}

	public ContentNodeModel getParent(ContentNodeModel model) {
		return model.getParentNode();
	}

	/**
	 * Collect a list of nodes, started from the rootId which are accepted by the filter.
	 *
	 * @param rootId
	 * @param filter
	 * @return a list of ContentNodeModel
	 */
	public List<ProductModel> collectChildNodes(String rootId, TreeElementFilter filter) {
		TreeElement root = nodes.get(rootId);
		if (root != null) {
			List<ProductModel> result = new ArrayList<ProductModel>(nodes.size());
			iterate(result, root, filter);
			return result;
		}
		return Collections.<ProductModel> emptyList();
	}

	private void iterate(List<ProductModel> result, TreeElement root, TreeElementFilter filter) {
		if (filter.accept(root)) {
			result.add((ProductModel) root.getModel());
		}
		for (Iterator<TreeElement> iter = root.getChildren().iterator(); iter.hasNext();) {
			TreeElement child = iter.next();
			iterate(result, child, filter);
		}
	}

	public NodeIterator iterator(boolean sortDeptsByProductCount) {
		return iterator(null, sortDeptsByProductCount);
	}

	/**
	 * Return a NodeIterator which goes through the nodes, which the TreeElementFilter is accepted by. If the TreeElementFilter
	 * rejects one node, then their descendants is rejected also, automatically.
	 *
	 * @param filter
	 * @param sortDeptsByProductCount
	 * @return
	 */
	public NodeIterator iterator(TreeElementFilter filter, boolean sortDeptsByProductCount) {
		List<TreeElement> orderedNodes = new ArrayList<TreeElement>(nodes.size());
		Iterator<TreeElement> iterator;
		if (sortDeptsByProductCount) {
			List<TreeElement> departments = new ArrayList<TreeElement>(roots);
			Collections.sort(departments, TreeElement.SORT_BY_PRODUCT_COUNT);
			iterator = departments.iterator();
		} else
			iterator = roots.iterator();
		iterate(orderedNodes, iterator, 0, filter);

		return new NodeIterator(orderedNodes);
	}

	private void iterate(List<TreeElement> collectTo, Iterator<TreeElement> iterator, int depth, TreeElementFilter filter) {
		for (; iterator.hasNext();) {
			TreeElement element = iterator.next();
			element.setDepth(depth);
			if (filter == null || filter.accept(element)) {
				collectTo.add(element);
				if (isExpanded(element)) {
					iterate(collectTo, element.getChildren().iterator(), depth + 1, filter);
				}
			}
		}
	}

	protected boolean isExpanded(TreeElement element) {
		if (expandNodesToDepth >= 0) {
			if (element.getDepth() < expandNodesToDepth) {
				return true;
			}
		}
		if (expandedElementIds != null) {
			return expandedElementIds.contains(element.getModel().getContentKey().id);
		}
		return true;
	}

	public void setExpandedElementIds(Set<String> expandedElementIds) {
		this.expandedElementIds = expandedElementIds;
	}

	public void setExpandNodesToDepth(int expandNodesToDepth) {
		this.expandNodesToDepth = expandNodesToDepth;
	}

	/**
	 * initialize depth fields of the TreeElements
	 */
	public void initDepthFields() {
		initDepthFields(roots.iterator(), 0);
	}

	private void initDepthFields(Iterator<TreeElement> iterator, int depth) {
		for (; iterator.hasNext();) {
			TreeElement element = iterator.next();
			element.setDepth(depth);
			initDepthFields(element.getChildren().iterator(), depth + 1);
		}
	}

}
