package com.freshdirect.cms.core.context;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DirectedGraph<T> {
    /**
     * Node adjacency map
     * Entries consist of a node and a list of all reachable nodes from it
     */
    private Map<T, List<T>> adjacentNodes = new HashMap<T, List<T>>();

    /**
     * Adds a directed edge to graph
     *
     * @param start start node
     * @param end end node becoming adjacent node in graph
     */
    public void addEdge(T start, T end) {
        notNull(start);
        if (end != null && !start.equals(end)) {
            List<T> neighbors = adjacentNodes.get(start);
            if (neighbors == null) {
                neighbors = new ArrayList<T>();
                adjacentNodes.put(start, neighbors);
            }

            neighbors.add(end);
        }
    }

    /**
     * This method collect all reachable nodes
     * starting from startNode
     * by traversing adjacency relationship
     *
     * @param startNode
     * @return set of nodes reachable from startNode
     */
    public Set<T> findReachableNodes(T startNode) {
        notNull(startNode, "Start node parameter required");

        ReachableNodeVisitor<T> collector = new ReachableNodeVisitor<T>() {
            @Override
            List<T> adjacentNodes(T node) {
                return adjacentNodes.get(node);
            }
        };

        collector.visit(startNode);

        Set<T> reachableNodes = collector.getVisitedNodes();

        // remove start node
        reachableNodes.remove(startNode);

        return reachableNodes;
    }

    /**
     * Find all paths between start node and destination node
     *
     * @param startNode node where the quest for finding destination begins
     * @param destinationNode destination to be reached or null to
     *
     * @return list of paths found between start end destination nodes
     */
    public List<List<T>> findPathsBetween(T startNode, final T destinationNode) {
        notNull(startNode, "Start node is required");
        notNull(destinationNode, "Destination node is required");

        PathFinder<T> pathFinder = new PathFinder<T>() {

            @Override
            boolean apply(T node) {
                if (node.equals(destinationNode)) {
                    registerPath();
                    return true;
                }

                return false;
            }

            @Override
            List<T> adjacentNodes(T node) {
                return adjacentNodes.get(node);
            }
        };

        pathFinder.visit(startNode);

        return pathFinder.getPaths();
    }

    /**
     * Discover all possible paths from a start node to all
     * reachable sink-nodes.
     * Sink node is a node without outgoing edges.
     *
     * @param startNode where the discovery starts
     * @return list of paths pointing to sink nodes
     */
    public List<List<T>> findPathsToSinkNodesStartingFrom(T startNode) {
        notNull(startNode, "Start node is required");

        // guard condition: startNode is already a sink node
        // end up here with empty result.
        if (adjacentNodes.get(startNode) == null) {
            return Collections.emptyList();
        }

        PathFinder<T> pathFinder = new PathFinder<T>() {

            @Override
            boolean apply(T node) {
                if (adjacentNodes(node) == null) {
                    registerPath();
                    return true;
                }
                return false;
            }

            @Override
            List<T> adjacentNodes(T node) {
                return adjacentNodes.get(node);
            }
        };

        pathFinder.visit(startNode);

        return pathFinder.getPaths();
    }
}
