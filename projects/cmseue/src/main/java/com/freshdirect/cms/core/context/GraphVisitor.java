package com.freshdirect.cms.core.context;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple graph visitor algorithm
 *
 * @author segabor
 *
 * @param <T>
 */
public abstract class GraphVisitor<T> {

    protected Deque<T> currentPath = new LinkedList<T>();

    /**
     * Graph visitor method.
     *
     * @param node
     */
    public void visit(T node) {
        currentPath.add(node);

        if (!apply(node)) {
            List<T> adjacents = adjacentNodes(node);
            if (adjacents != null) {
                for (T adjacentNode : adjacents) {
                    if (!currentPath.contains(adjacentNode)) {
                        visit(adjacentNode);
                    }
                }
            }
        }

        currentPath.removeLast();
    }

    /**
     * Check if visitor meets condition implemented in subclass.
     * Upon false result visitor dives deeper in graph by
     * visiting adjacent nodes.
     *
     * @return true result means positive match
     */
    abstract boolean apply(T node);

    /**
     * It returns adjacent nodes of the given node
     *
     * @param node
     * @return list of adjacents
     */
    abstract List<T> adjacentNodes(T node);
}
