package com.freshdirect.framework.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


/**
 * Abstract state graph implementation.
 * 
 * @author segabor
 *
 * @param <T> Label Type
 */
public class StateGraph<T> {
	/**
	 * Start Label
	 */
	final protected T start;
	
	/**
	 * Set of all possible states
	 */
	final protected Set<T> states;
	
	/**
	 * Map of directed edges connecting states.
	 * Each entry is a couple of Start State to possible End States. 
	 */
	final protected Map<T,Set<T>> edges;

	/**
	 * Terminal states.
	 */
	final protected Set<T> terminals;

	/**
	 * Current state
	 */
	protected T state;
	
	protected StateChangeDelegate<T> delegate;
	
	protected Set<StateChangeListener<T>> listeners = new HashSet<StateChangeListener<T>>();

	/**
	 * 
	 * @param states set of available states
	 * @param edges map of state to state phases
	 * @param start initial state
	 * @param terminals terminal states
	 */
	public StateGraph(Set<T> states, Map<T,Set<T>> edges, T start, Set<T> terminals) {
		this.states = states;
		this.edges = edges;

		this.start = start;
		this.terminals = terminals != null ? terminals : Collections.<T>emptySet();
		
		validate(states, edges, start);
		
		// set initial state
		this.state = this.start;
	}


	/**
	 * @param states
	 * @param edges
	 * @param start
	 */
	private void validate(Set<T> states, Map<T, Set<T>> edges, T start) {
		if (states == null || states.size() < 2)
			throw new IllegalArgumentException("Empty states set");
		
		if (edges == null || edges.size() == 0)
			throw new IllegalArgumentException("Edges set is empty");
		
		if (start == null || !states.contains(start))
			throw new IllegalArgumentException("No start label or states does not contain");
		
		// check graph properties
		if (!edges.keySet().contains(start)) {
			throw new IllegalArgumentException("No edge direct from " + start);
		}
		if (edges.values().contains(start)) {
			throw new IllegalArgumentException("Edge may not point to initial label " + start);
		}
		
		for (T t : this.terminals) {
			if (edges.keySet().contains(t)) {
				throw new IllegalArgumentException("Edge may not direct from terminal label " + t);
			}
		}
		
		if (this.states.size() != this.edges.keySet().size() + this.terminals.size()) {
			throw new IllegalArgumentException("Problem with number of edges");
		}
	}
	
	
	public StateGraph(Set<T> states, Map<T,Set<T>> edges, T start, T terminal) {
		this.states = states;
		this.edges = edges;

		this.start = start;
		
		if (terminal != null) {
			this.terminals = new HashSet<T>(1);
			this.terminals.add(terminal);
		} else {
			this.terminals = Collections.<T>emptySet();
		}

		validate(states, edges, start);
		
		// set initial state
		this.state = this.start;
	}
	
	/**
	 * Get current state
	 * @return
	 */
	public T getState() {
		return this.state;
	}
	
	/**
	 * Set current state.
	 * Use {@link StateGraph#setNextState(Object)} instead for safe state transition.
	 * 
	 * @param state
	 */
	protected void setState(T state) {
		this.state = state;
	}


	/**
	 * Set arbitrary state without validating.
	 * Use it to set first state other than initial. Then use {@link StateGraph#setNextState(Object)} method to step over a next safe state.
	 * 
	 * 
	 * @param state
	 */
	public void setStateUnsafe(T state) {
		this.setState(state);
	}
	
	
	/**
	 * Get all available states
	 * @return
	 */
	public Set<T> getStates() {
		return states;
	}

	/**
	 * Return state to states mapping
	 * @return
	 */
	public Map<T, Set<T>> getEdges() {
		return edges;
	}


	/**
	 * returns initial state
	 * @return
	 */
	public T getStart() {
		return start;
	}
	
	/**
	 * returns terminal states
	 * @return
	 */
	public Set<T> getTerminalStates() {
		return terminals;
	}
	
	/**
	 * Move on to next state if possible.
	 * 
	 * @param s
	 */
	public void setNextState(T s) {
		if (s == null || !this.states.contains(s))
			throw new IllegalArgumentException("Invalid state " + s);
		
		T oldState = getState();

		// loop not allowed
		if (oldState == s)
			return;

		if (edges.get(oldState) == null || !edges.get(oldState).contains(s))
			throw new IllegalArgumentException(s + " is not a subsequent state of " + this.state);


		// already in terminal state
		if (this.terminals.contains(oldState)) {
			for (StateChangeListener<T> l : listeners) {
				l.failedToEnterState(s);
			}
			return;
		}


		// notify listeners that graph will enter to a next state
		for (StateChangeListener<T> l : listeners) {
			l.willLeaveState(oldState);
			l.willEnterState(s);
		}

		boolean doStateTransition = true;
		if (delegate != null) {
			doStateTransition = delegate.changeState(oldState, s);
		}

		if (doStateTransition) {
			setState(s);

			// notify listeners that graph entered to a next state
			for (StateChangeListener<T> l : listeners) {
				l.didLeaveState(oldState);
				l.didEnterState(s);
			}
		} else {
			for (StateChangeListener<T> l : listeners) {
				l.failedToEnterState(s);
			}
		}
	}



	/**
	 * Checks if state graph is in terminal state
	 * @return
	 */
	public boolean isTerminal() {
		final T s = getState();
		return this.terminals.contains(s);
	}


	/**
	 * Returns a set of states where graph can go from actual state.
	 * @return
	 */
	public Set<T> getSubsequentStates() {
		final T s = getState();
		
		if (getEdges().get(s) != null) {
			return getEdges().get(s);
		}

		return Collections.<T>emptySet();
	}
	
	
	public void addListener(StateChangeListener<T> l) {
		listeners.add(l);
	}
	
	public void removeListener(StateChangeListener<T> l) {
		listeners.remove(l);
	}

	public StateChangeDelegate<T> delegate() {
		return this.delegate;
	}
	
	public void setDelegate(StateChangeDelegate<T> delegate) {
		this.delegate = delegate;
	}
	

	/**
	 * Utility method to create edges map from a simple list
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static<T> Map<T,Set<T>> toEdgesMap(T[] list) {
		if (list.length < 2)
			return null;

		Map<T,Set<T>> e = new HashMap<T,Set<T>>();
		
		int n=0;
		while (n<list.length) {
			T head = list[n++];
			Set<T> tail = new HashSet<T>();

			while (n<list.length && list[n] != null) {
				tail.add(list[n++]);
			}
			
			e.put(head, tail);
			
			if (n<list.length && list[n] == null)
				n++;
		}
		
		return e;
	}
}
