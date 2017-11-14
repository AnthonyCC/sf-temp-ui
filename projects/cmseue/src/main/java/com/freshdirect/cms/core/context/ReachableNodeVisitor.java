package com.freshdirect.cms.core.context;

import java.util.HashSet;
import java.util.Set;

public abstract class ReachableNodeVisitor<T> extends GraphVisitor<T> {

    private Set<T> visitedNodes = new HashSet<T>();

    @Override
    boolean apply(T node) {
        visitedNodes.add(node);
        return false;
    }

    public Set<T> getVisitedNodes() {
        return visitedNodes;
    }
}
