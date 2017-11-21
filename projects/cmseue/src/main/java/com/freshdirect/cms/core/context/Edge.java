package com.freshdirect.cms.core.context;

/**
 * A very simple generic directed edge implementation
 *
 * @author segabor
 *
 * @param <T> vertex type
 */
public final class Edge<T> {

    private final T start;
    private final T end;

    public Edge(T startKey, T endKey) {
        this.start = startKey;
        this.end = endKey;
    }

    public boolean isCycle() {
        return start != null && end != null && start.equals(end);
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Edge<?> other = (Edge<?>) obj;
        if (end == null) {
            if (other.end != null) {
                return false;
            } else if (other.start.equals(start)) {
                return true;
            }
        } else if (!end.equals(other.end)) {
            return false;
        }
        if (start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!start.equals(other.start)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return start + " -> " + end;
    }
}
