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

    private static class TreeElementComparator implements Comparator {
        Comparator comp;
        
        public TreeElementComparator(Comparator comp) {
            this.comp = comp;
        }
        
        public int compare(Object o1, Object o2) {
            return comp.compare(((TreeElement)o1).getModel(), ((TreeElement)o2).getModel());
        }
    }
    
    public static class NodeIterator implements Iterator {
        List list;
        int position = 0;
        
        NodeIterator(List list) {
            this.list = list;
        }
        
        public boolean hasNext() {
            return position<list.size();
        }
        
        public Object next() {
            Object result = list.get(position);
            position++;
            return result;
        }
        
        public Object peekNext() {
            return list.get(position);
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class TreeElement {
        ContentNodeModel model;
        Set              children;
        int depth;
        int childCount;

        public TreeElement(ContentNodeModel model, TreeElementComparator comparator) {
            this.model = model;
            if (comparator==null) {
                children = new HashSet();
            } else { 
                children = new TreeSet(comparator);
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
        

        // Collection<TreeElement>
        public Collection getChildren() {
            return children;
        }
        
        public String toString() {
            return "["+model.getContentKey()+':'+model.getFullName()+",deep child:"+childCount+",child:"+children.size()+']';
        }
    }
    
    public static interface TreeElementFilter {
        public boolean accept(TreeElement element);
    }
    
    final Set                   roots;                              // Set<TreeElement>
    final Map                   nodes              = new HashMap(); // Map<String,TreeElement>
    Set                         expandedElementIds;
    int                         expandNodesToDepth = -1;
    final TreeElementComparator childComparator;
    
    
    public ContentNodeTree() {
        roots = new HashSet(); // Set<TreeElement>
        childComparator = null;
    }
    
    public ContentNodeTree(Comparator rootComparator, Comparator childComparator) {
        this.roots = new TreeSet(new TreeElementComparator(rootComparator));
        this.childComparator = new TreeElementComparator(childComparator);
    }
    
    
    /**
     * Add node to the tree structure. 
     * @param model
     */
    public void addNode(ContentNodeModel model) {
        ContentNodeModel current = model;
        TreeElement prev = null;
        while (current != null) {
            TreeElement element = getTreeElement(current);
            if (element != null) {
                incrementParents(element);
                if (prev != null) {
                    element.addChild(prev);
                }
                return;
            }
            element = createElement(current);
            element.incrementChildCount();
            if (prev != null) {
                element.addChild(prev);
            }
            current = getParent(current);
            prev = element;
        }
        roots.add(prev);
    }
    
    
    public void addChildNode(String parentNodeId, String childId) {
        TreeElement parent = getTreeElement(parentNodeId);
        TreeElement child = getTreeElement(childId);
        if (parent!=null && child!=null) {
            parent.addChild(child);
            incrementParents(parent);
        }
    }
    
    /**
     * remove nodes which not accepted by the filter, and append their child nodes to the original parent node.
     * 
     * @param filter
     */
    public void purge(TreeElementFilter filter) {
        for (Iterator iter = new HashSet(roots).iterator(); iter.hasNext(); ) {
            TreeElement element = (TreeElement) iter.next();
            purgeElement(filter, element, null);
        }
    }
    
    private void purgeElement(TreeElementFilter filter, TreeElement element,TreeElement  parent) {
        if (filter.accept(element)) {
            // recurse ...
            for (Iterator iter= new HashSet(element.getChildren()).iterator();iter.hasNext();) {
                TreeElement child = (TreeElement) iter.next();
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
            for (Iterator iter= element.getChildren().iterator();iter.hasNext();) {
                TreeElement child = (TreeElement) iter.next();
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
    public Set getRoots() {
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
        } else {
            return null;
        }
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
    public List collectChildNodes(String rootId, TreeElementFilter filter) {
        TreeElement root = (TreeElement) nodes.get(rootId);
        if (root!=null) {
            List result = new ArrayList(nodes.size());
            iterate(result, root, filter);
            return result;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    private void iterate(List result, TreeElement root, TreeElementFilter filter) {
        if (filter.accept(root)) {
            result.add(root.getModel());
        }
        for (Iterator iter = root.getChildren().iterator();iter.hasNext();) {
            TreeElement child = (TreeElement) iter.next();
            iterate(result, child, filter);
        }
    }

    public NodeIterator iterator() {
        return iterator(null);
    }
    /**
     * Return a NodeIterator which goes through the nodes, which the TreeElementFilter is accepted by. If the TreeElementFilter rejects one node, then their descendants is rejected
     * also, automaticly.
     * 
     * @param filter
     * @return
     */
    public NodeIterator iterator(TreeElementFilter filter) {
        List orderedNodes = new ArrayList(nodes.size());
        iterate(orderedNodes, roots.iterator(), 0, filter);
        
        return new NodeIterator(orderedNodes);
    }

    private void iterate(List collectTo, Iterator iterator, int depth, TreeElementFilter filter) {
        for (; iterator.hasNext();) {
            TreeElement element = (TreeElement) iterator.next();
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
        if (expandedElementIds !=null) {
            if (expandedElementIds.contains(element.getModel().getContentKey().getId())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
    
    public void setExpandedElementIds(Set expandedElementIds) {
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

    private void initDepthFields(Iterator iterator, int depth) {
        for (; iterator.hasNext();) {
            TreeElement element = (TreeElement) iterator.next();
            element.setDepth(depth);
            initDepthFields(element.getChildren().iterator(), depth+1);
        }
    }
    
}
