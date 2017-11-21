package com.freshdirect.cms.ui.editor.formdef;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterable implements Iterable<Node> {

    private class NodeListIterator implements Iterator<Node> {

        private int index=0;

        @Override
        public boolean hasNext() {
            return nodeList.getLength()>index;
        }

        @Override
        public Node next() {
            if (hasNext()) {
                Node node = nodeList.item(index++);
                return node;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final NodeList nodeList;

    public NodeListIterable(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeListIterator();
    }
}
