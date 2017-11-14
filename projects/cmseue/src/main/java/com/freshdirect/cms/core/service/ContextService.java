package com.freshdirect.cms.core.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.context.DirectedGraph;
import com.freshdirect.cms.core.context.Edge;
import com.freshdirect.cms.core.domain.ContentKey;
import com.google.common.base.Optional;

@Service
public class ContextService {

    public DirectedGraph<ContentKey> buildGraphFromParentKeyStructure(ContentKey contentKey, Map<ContentKey, Set<ContentKey>> allParents) {
        Queue<ContentKey> keyQueue = new LinkedList<ContentKey>();
        keyQueue.add(contentKey);
        // list of key->parent key edges
        // nodes cannot be followed by are represented by key->(null) edge.
        Set<Edge<ContentKey>> edges = new HashSet<Edge<ContentKey>>();

        while (!keyQueue.isEmpty()) {
            ContentKey key = keyQueue.poll();

            Set<ContentKey> parentKeys = allParents.get(key);

            if (parentKeys == null || parentKeys.isEmpty()) {
                edges.add(new Edge<ContentKey>(key, null));
            } else {
                for (ContentKey parentKey : parentKeys) {
                    Edge<ContentKey> currentEdge = new Edge<ContentKey>(key, parentKey);
                    edges.add(currentEdge);
                }
                keyQueue.addAll(parentKeys);
            }

        }

        // build directed graph
        DirectedGraph<ContentKey> graph = new DirectedGraph<ContentKey>();
        for (Edge<ContentKey> e : edges) {
            graph.addEdge(e.getStart(), e.getEnd());
        }

        return graph;
    }

    public Optional<List<ContentKey>> selectContextOf(ContentKey contentKey, ContentKey parentKey, List<List<ContentKey>> allContextsOfContentKey) {
        Assert.notNull(contentKey);

        if (allContextsOfContentKey.isEmpty()) {
            return Optional.absent();
        } else if (parentKey == null && allContextsOfContentKey.size() > 1) {
            return Optional.absent();
        }

        List<ContentKey> selectedContext = null;
        for (List<ContentKey> context : allContextsOfContentKey) {
            if (parentKey.equals(context.get(1))) {
                selectedContext = context;
                break;
            }
        }

        return Optional.fromNullable(selectedContext);
    }

    /**
     * Collect the topmost keys of context paths.
     *
     * @param contentKey
     * @return
     */
    public Set<ContentKey> selectTopKeysOf(ContentKey contentKey, List<List<ContentKey>> contexts) {
        Set<ContentKey> topKeys = Collections.emptySet();

        if (contexts != null) {
            topKeys = new HashSet<ContentKey>(contexts.size());
            for (List<ContentKey> context : contexts) {
                if (!context.isEmpty()) {
                    topKeys.add(context.get(context.size() - 1));
                }
            }
        }

        return topKeys;
    }

    public List<List<ContentKey>> findContextsOf(ContentKey contentKey, Map<ContentKey, Set<ContentKey>> allParents) {
        DirectedGraph<ContentKey> graph = buildGraphFromParentKeyStructure(contentKey, allParents);
        return graph.findPathsToSinkNodesStartingFrom(contentKey);
    }
}
