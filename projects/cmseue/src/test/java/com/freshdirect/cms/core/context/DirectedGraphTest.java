package com.freshdirect.cms.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.freshdirect.cms.category.UnitTest;
import com.google.common.collect.Sets;

@Category(UnitTest.class)
public class DirectedGraphTest {

    @Test
    public void testPathsBetweenNodes() {

        DirectedGraph<Integer> g = new DirectedGraph<Integer>();

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(2, 0);
        g.addEdge(2, 1);
        g.addEdge(1, 3);

        List<List<Integer>> paths = g.findPathsBetween(2, 3);

        assertNotNull("Unexpected null result", paths);
        assertEquals("Result should contain three context paths", 3, paths.size());
        assertTrue("Context path 2, 0, 1, 3 not found", paths.contains(Arrays.asList(new Integer[] {2, 0, 1, 3})));
        assertTrue("Context path 2, 0, 3 not found", paths.contains(Arrays.asList(new Integer[] {2, 0, 3})));
        assertTrue("Context path 2, 1, 3 not found", paths.contains(Arrays.asList(new Integer[] {2, 1, 3})));
    }

    @Test
    public void testContexts() {
        DirectedGraph<String> g = new DirectedGraph<String>();

        g.addEdge("S", "n1");
        g.addEdge("n1", "n2");
        g.addEdge("S", "n3");

        List<List<String>> paths = g.findPathsToSinkNodesStartingFrom("S");

        assertNotNull("Unexpected null result", paths);
        assertEquals("Result should contain three context paths", 2, paths.size());
        assertTrue("Context path ['S', 'n1', 'n2'] not found", paths.contains(Arrays.asList(new String[] {"S", "n1", "n2"})));
        assertTrue("Context path ['S', 'n3'] not found", paths.contains(Arrays.asList(new String[] {"S", "n3"})));
    }

    @Test
    public void testReachableNodesOnEmptyGraph() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();

        assertTrue("Reachable set must be empty in an empty graph", g.findReachableNodes(0).isEmpty());
        assertTrue("Reachable set must be empty in an empty graph", g.findPathsToSinkNodesStartingFrom(0).isEmpty());
    }

    @Test
    public void testReachableNodesOnSingleNodeGraph() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(0, null);

        assertTrue("There are no reachable nodes for node '0' in a single-node graph", g.findReachableNodes(0).isEmpty());
        assertTrue("There are no reachable nodes for non-member node '1' in a single-node graph", g.findReachableNodes(1).isEmpty());
    }

    @Test
    public void testReachableNodesOnSimpleCyclicGraph() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(0, 1);
        g.addEdge(1, 0);

        assertTrue("Node '0' has reachable nodes", !g.findReachableNodes(0).isEmpty());
        assertTrue("Unexpected reachable set yielded", g.findReachableNodes(0).equals(Sets.newHashSet(1)));

        assertTrue("Node '1' has reachable nodes", !g.findReachableNodes(1).isEmpty());
        assertTrue("Unexpected reachable set yielded", g.findReachableNodes(1).equals(Sets.newHashSet(0)));

        assertTrue("There are no reachable nodes for non-member node", g.findReachableNodes(2).isEmpty());
    }

    @Test
    public void testReachableNodesOnSimpleAcyclicGraph() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(0, 1);
        g.addEdge(1, 2);

        assertTrue("Node '0' has reachable nodes", !g.findReachableNodes(0).isEmpty());
        assertTrue("Unexpected reachable set yielded", g.findReachableNodes(0).equals(Sets.newHashSet(1, 2)));

        assertTrue("Node '1' has reachable nodes", !g.findReachableNodes(1).isEmpty());
        assertTrue("Unexpected reachable set yielded", g.findReachableNodes(1).equals(Sets.newHashSet(2)));

        assertTrue("Node '2' cannot have reachable nodes", g.findReachableNodes(2).isEmpty());
    }
}
