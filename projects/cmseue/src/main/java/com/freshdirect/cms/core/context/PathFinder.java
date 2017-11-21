package com.freshdirect.cms.core.context;

import java.util.ArrayList;
import java.util.List;

public abstract class PathFinder<T> extends GraphVisitor<T> {

    /**
     * Collection of any path in graph
     * found by PathFinder implementation.
     */
    private List<List<T>> paths = new ArrayList<List<T>>();

    public List<List<T>> getPaths() {
        return paths;
    }

    /**
     * Takes path currently composed by PathFinder and
     * store in final result list.
     *
     * Implementations should invoke {@link #registerPath()}
     * in {@link #apply(Object)} method when currentPath is
     * considered a good match.
     */
    void registerPath() {
        paths.add(new ArrayList<T>(currentPath));
    }
}
