package com.freshdirect.cms.context;

import com.freshdirect.cms.ContentNodeI;

/**
 * ContextWalker walks the chain of nodes linked by a context. It starts from the given contextual node and walks up to the root.
 * While it is walking it tests the current node and stops the walk if reached the root object
 * or shouldStop is true.
 * 
 * @author segabor
 *
 */
public abstract class ContextWalker {
	protected ContextualContentNodeI leaf;
	protected boolean testResult = false;
	
	protected ContentNodeI terminalNode = null; // the node where walker stopped
	
	public ContextWalker(ContextualContentNodeI node) {
		this.leaf = node;
	}


	/**
	 * Up the boots and go!
	 */
	public void walk() {
		_walk(leaf);
	}


	private void _walk(ContextualContentNodeI cmsNode) {
		// test node until it's true
		if (!testResult && test(cmsNode)) {
			testResult = true;
		}



		ContextualContentNodeI parent = cmsNode.getParentNode();
		if (parent != null) {
			if (terminalNode == null && shouldStop(parent)) {
				terminalNode = parent;
			} else {
				// Go further, there is nothing to see here!
				_walk(parent);
			}
		}
	}

	/**
	 * Subclasses must implement this method.
	 * Add positive test logic here. Walker starts with 'false' result and
	 * invokes this method repeatedly until test() returns true.
	 * 
	 * @param n {@link ContentNodeI actual node to test}
	 * @return The test result
	 */
	public abstract boolean test(ContentNodeI n);
	
	/**
	 * Subclasses must implement this method.
	 * Returns true if walker must stop by reaching this node.
	 * Otherwise it stops if there are no more nodes.
	 * 
	 * @param n {@link ContentNodeI actual node to test}
	 * @return true if walker must stop before it reaches the other end of context chain.
	 */
	public abstract boolean shouldStop(ContentNodeI n);


	/**
	 * Returns the start node
	 * @return {@link ContentNodeI the leaf node}
	 */
	public ContentNodeI getLeaf() {
		return this.leaf;
	}
	
	/**
	 * Returns the test result
	 * @return
	 */
	public boolean getTestResult() {
		return this.testResult;
	}


	/**
	 * Returns the node where the walker stopped (if found any).
	 * @return
	 */
	public ContentNodeI getTerminalNode() {
		return this.terminalNode;
	}
}
