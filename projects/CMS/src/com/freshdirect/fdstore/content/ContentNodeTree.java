package com.freshdirect.fdstore.content;

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

public class ContentNodeTree {

    private static class TreeElementComparator implements Comparator<TreeElement> {
        Comparator<ContentNodeModel> comp;
        
        public TreeElementComparator(Comparator<ContentNodeModel> comp) {
            this.comp = comp;
        }
        
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
        
        public boolean hasNext() {
            return position<list.size();
        }
        
        public TreeElement next() {
        	TreeElement result = list.get(position);
            position++;
            return result;
        }
        
        public TreeElement peekNext() {
            return list.get(position);
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class TreeElement {
        ContentNodeModel model;
        Set<TreeElement>              children;
        int depth;
        int childCount;

        public TreeElement(ContentNodeModel model, TreeElementComparator comparator) {
            this.model = model;
            if (comparator==null) {
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
            return childCount;
        }
        
        public void incrementChildCount() {
            childCount++;
        }
        
        public Collection<TreeElement> getChildren() {
            return children;
        }
        
        public String toString() {
            return "["+model.getContentKey()+':'+model.getFullName()+",deep child:"+childCount+",child:"+children.size()+']';
        }
    }
    
    public static interface TreeElementFilter {
        public boolean accept(TreeElement element);
    }
    
    public static class UniqueProductFilter implements TreeElementFilter {
        Set<String> productIds = new HashSet<String>();
        public boolean accept(TreeElement element) {
            if (element.getModel() instanceof ProductModel) {
                String id = element.getModel().getContentKey().getId();
                if (!productIds.contains(id)) {
                    productIds.add(id);
                    return true;
                }
            }
            return false;
        }
    }

    
    final Set<TreeElement>                   	roots;
    final Map<String, TreeElement>				nodes              = new HashMap<String, TreeElement>();
    Set<String>                    				expandedElementIds;
    int                         				expandNodesToDepth = -1;
    final TreeElementComparator 				childComparator;
    
    
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
     * @param model
     * @return 
     */
    public TreeElement addNode(ContentNodeModel model) {
        ContentNodeModel current = model;
        TreeElement prev = null;
        //System.out.println("addNode "+model);
        TreeElement firstCreated = null;
        while (current != null) {
            TreeElement element = getTreeElement(current);
            if (element != null) {
                if (firstCreated==null) {
                    firstCreated = element;
                }
                incrementParents(element);
                if (prev != null) {
                    element.addChild(prev);
                }
                return firstCreated;
            }
            element = createElement(current);
            if (firstCreated==null) {
                firstCreated = element;
            }
            element.incrementChildCount();
            if (prev != null) {
                element.addChild(prev);
            }
            current = getParent(current);
            prev = element;
        }
        roots.add(prev);
        return firstCreated;
    }

    public void addChildNode(ContentNodeModel model, String childId) {
        TreeElement parent = getTreeElement(model);
        TreeElement child = getTreeElement(childId);
        if (child!=null) {
            if (parent==null) {
                parent = addNode(model);
                parent.addChild(child);
            } else {
                parent.addChild(child);
                incrementParents(parent);
            }
        }
    }
    
    /**
     * remove nodes which not accepted by the filter, and append their child nodes to the original parent node.
     * 
     * @param filter
     */
    public void purge(TreeElementFilter filter) {
        for (Iterator<TreeElement> iter = new HashSet<TreeElement>(roots).iterator(); iter.hasNext(); ) {
            TreeElement element = iter.next();
            purgeElement(filter, element, null);
        }
    }
    
    private void purgeElement(TreeElementFilter filter, TreeElement element,TreeElement  parent) {
        if (filter.accept(element)) {
            // recurse ...
            for (Iterator<TreeElement> iter= new HashSet<TreeElement>(element.getChildren()).iterator();iter.hasNext();) {
                TreeElement child = iter.next();
                purgeElement(filter, child, element);
            }
        } else {
            //System.out.println("REMOVE node :" +element.getModel().getFullName() +" parent:"+parent);
            // remove from the tree
            if (parent==null) {
                roots.remove(element);
                roots.addAll(element.getChildren());
            } else {
                parent.getChildren().remove(element);
                parent.getChildren().addAll(element.getChildren());
            }
            // recurse with the new parent
            for (Iterator<TreeElement> iter= element.getChildren().iterator();iter.hasNext();) {
                TreeElement child = iter.next();
                purgeElement(filter, child, parent);
            }
        }
    }

    protected void incrementParents(TreeElement element) {
        do {
            element.incrementChildCount();
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
        Object element = nodes.get(model.getContentKey().getId());
        if (element instanceof TreeElement) {
            return (TreeElement) element;
        }
		return null;
    }
    
    protected TreeElement getParentElement(TreeElement element) {
        ContentNodeModel parent = getParent(element.getModel());
        return parent!=null ? getTreeElement(parent) : null; 
    }

    protected TreeElement createElement(ContentNodeModel model) {
        TreeElement treeElement = new TreeElement(model, childComparator);
        nodes.put(model.getContentKey().getId(), treeElement);
        return treeElement;
    }

    public ContentNodeModel getParent(ContentNodeModel model) {
        return model.getParentNode();
    }
    
    /**
     * Collect a list of nodes, started from the rootId which are accepted by the filter.
     * @param rootId
     * @param filter
     * @return a list of ContentNodeModel
     */
    public List<ProductModel> collectChildNodes(String rootId, TreeElementFilter filter) {
        TreeElement root = nodes.get(rootId);
        if (root!=null) {
            List<ProductModel> result = new ArrayList<ProductModel>(nodes.size());
            iterate(result, root, filter);
            return result;
        }
		return Collections.<ProductModel>emptyList();
    }
    
    private void iterate(List<ProductModel> result, TreeElement root, TreeElementFilter filter) {
        if (filter.accept(root)) {
            result.add((ProductModel)root.getModel());
        }
        for (Iterator<TreeElement> iter = root.getChildren().iterator();iter.hasNext();) {
            TreeElement child = iter.next();
            iterate(result, child, filter);
        }
    }

    public NodeIterator iterator() {
        return iterator(null);
    }
    /**
     * Return a NodeIterator which goes through the nodes, which the TreeElementFilter is accepted by. If the TreeElementFilter rejects one node, then their descendants is rejected
     * also, automatically.
     * 
     * @param filter
     * @return
     */
    public NodeIterator iterator(TreeElementFilter filter) {
        List<TreeElement> orderedNodes = new ArrayList<TreeElement>(nodes.size());
        iterate(orderedNodes, roots.iterator(), 0, filter);
        
        return new NodeIterator(orderedNodes);
    }

    private void iterate(List<TreeElement> collectTo, Iterator<TreeElement> iterator, int depth, TreeElementFilter filter) {
        for (; iterator.hasNext();) {
            TreeElement element = iterator.next();
            element.setDepth(depth);
            if (filter==null || filter.accept(element)) {
                collectTo.add(element);
                if (isExpanded(element)) {
                    iterate(collectTo, element.getChildren().iterator(), depth+1, filter);
                }
            }
        }
    }

    protected boolean isExpanded(TreeElement element) {
        if (expandNodesToDepth>=0) {
            if (element.getDepth()<expandNodesToDepth) {
                return true;
            }
        }
        if (expandedElementIds != null) {
            return expandedElementIds.contains(element.getModel().getContentKey().getId());
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
            initDepthFields(element.getChildren().iterator(), depth+1);
        }
    }
    
}
