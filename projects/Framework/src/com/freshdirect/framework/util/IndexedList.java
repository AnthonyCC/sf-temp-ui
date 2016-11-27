package com.freshdirect.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class IndexedList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -46138039166997432L;

	HashMap<E, TreeSet<Integer>> index;

	public IndexedList() {
		super();
		index = new HashMap<E, TreeSet<Integer>>();
	}

	public IndexedList(Collection<? extends E> c) {
		super(c);
		index = new HashMap<E, TreeSet<Integer>>();
		int i = 0;
		for (E e : c)
			addIndex(e, i++);
	}

	public IndexedList(int initialCapacity) {
		super(initialCapacity);
		index = new HashMap<E, TreeSet<Integer>>(initialCapacity);
	}

	protected void addIndex(E e, int index) {
		if (!this.index.containsKey(e))
			this.index.put(e, new TreeSet<Integer>());
		this.index.get(e).add(index);
	}

	private void addIndexFromIndex(int index) {
		for (int i = index; i < size(); i++)
			addIndex(get(i), i);
	}

	protected void removeIndex(E e, int index) {
		if (this.index.get(e).size() == 1)
			this.index.remove(e);
		else
			this.index.get(e).remove(index);
	}

	private void removeIndexFromIndex(int index) {
		for (int i = index; i < size(); i++)
			removeIndex(get(i), i);
	}

	@Override
	public boolean add(E e) {
		int index = size();
		boolean ret = super.add(e);
		addIndex(e, index);
		return ret;
	}

	@Override
	public void add(int index, E element) {
		removeIndexFromIndex(index);
		super.add(index, element);
		addIndexFromIndex(index);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int index = size();
		boolean ret = super.addAll(c);
		for (E e : c)
			addIndex(e, index++);
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (c.size() != 0) {
			removeIndexFromIndex(index);
			boolean ret = super.addAll(index, c);
			addIndexFromIndex(index);
			return ret;
		} else
			return false;
	}

	@Override
	public void clear() {
		super.clear();
		index.clear();
	}

	@Override
	public Object clone() {
		// not the most efficient one but it works
		return new IndexedList<E>(this);
	}

	@Override
	public boolean contains(Object o) {
		return index.containsKey(o);
	}

	@Override
	public int indexOf(Object o) {
		TreeSet<Integer> indexes = index.get(o);
		if (indexes != null)
			return indexes.first();
		else
			return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		TreeSet<Integer> indexes = index.get(o);
		if (indexes != null)
			return indexes.last();
		else
			return -1;
	}

	@Override
	public E remove(int index) {
		removeIndexFromIndex(index);
		E ret = super.remove(index);
		addIndexFromIndex(index);
		return ret;
	}

	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index != -1) {
			remove(index);
			return true;
		} else
			return false;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		if (fromIndex < 0 || fromIndex >= size() || toIndex > size() || toIndex < fromIndex)
			throw new IndexOutOfBoundsException();
		removeIndexFromIndex(fromIndex);
		super.removeRange(fromIndex, toIndex);
		addIndexFromIndex(fromIndex);
	}

	@Override
	public E set(int index, E element) {
		E o = get(index);
		removeIndex(o, index);
		o = super.set(index, element);
		addIndex(element, index);
		return o;
	}
}
