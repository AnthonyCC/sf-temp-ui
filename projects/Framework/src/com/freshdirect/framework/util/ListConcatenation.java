package com.freshdirect.framework.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;



/**
 * A virtual list made of lists and elements which implements the List interface.
 * 
 * Consider the code snippet.
 * <code>
 *     ListConcatenation LS = new ListConcatenation();
 *     LS.add("a");
 *     LS.addList(Arrays.asList(new String[] { "b", "c"}));
 *     LS.add("d");
 *     LS.addList(Arrays.asList(new String[] { "e", "f" }));
 *     LS.add("g");
 * </code>
 * The list LS is the list {"a","b","c","d","e","f","g"}, but the elements of ["b", "c"] and ["e","f"] are not 
 * explicitly inserted but "come from" the host list.
 * 
 * The class is perhaps the most useful with virtual lists, such as {@link CartesianProduct} which would be inefficient
 * to enumerate.
 * 
 * Only the "get" methods are implemented, other methods will throw UnsupportedOperationExceptions.
 * 
 * Access is fast: i.e. O(log(k), where k is the number of explictly stored entries, in the above example k = 5; these are
 * "a", ["b","c"], "d", ["e","f"], "g".
 * 
 * @author istvan
 *
 */
public class ListConcatenation implements List, RandomAccess {
	
	private static final UnsupportedOperationException unsupportedOperation = new UnsupportedOperationException();
	
	// marker for list inserts
	private class ListElement {
		private List list;
		
		private ListElement(List list) {
			this.list = list;
		}
	}
	
	private ArrayList elements; /// top level elements
	private int[] indexes; // indexes
	
	private int n = 0; // number of top level elements
	
	/**
	 * Update index.
	 * @param k
	 */
	private void addToIndex(int k) {
		if (elements.size() >= indexes.length) {
			int[] newIndexes = new int[3*indexes.length / 2 + 1];
			System.arraycopy(indexes,0,newIndexes,0,elements.size());
			indexes = newIndexes;
		}
		indexes[elements.size()] = (elements.size() > 0 ? indexes[elements.size()-1] : 0 ) + k;
	}
	
	/**
	 * Constructor.
	 *
	 */
	public ListConcatenation() {
		elements = new ArrayList();
		indexes = new int[16];
	}
	
	/**
	 * Constructor.
	 * @param c initial capacity for top level elements
	 */
	public ListConcatenation(int c) {
		elements = new ArrayList(c);
		indexes = new int[c];
	}
 	
	/**
	 * Add an element.
	 */
	public boolean add(Object elem) {
		addToIndex(1);
		elements.add(elem);
		++n;
		return true;
	}
	
	/**
	 * Add a list as elements.
	 * 
	 * All the list elements are (in effect) added to the list, without explicit copying.
	 * @param elems elements
	 * @return true
	 */
	public boolean addList(List elems) {
		if (elems == null) throw new NullPointerException();
		if (elems.size() == 0) return false;
		addToIndex(elems.size());
		elements.add(new ListElement(elems));
		n += elems.size();
		return true;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public void add(int arg0, Object arg1) {
		throw unsupportedOperation;

	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(Collection arg0) {
		throw unsupportedOperation;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(int arg0, Collection arg1) {
		throw unsupportedOperation;
	}

	/**
	 * Clear all elements.
	 */
	public void clear() {
		elements.clear();
		n = 0;
	}

	/**
	 * Return whether element is in list.
	 * @param value element
	 * @return if value is in the list
	 */
	public boolean contains(Object value) {
		for(Iterator i=elements.iterator(); i.hasNext(); ) {
			Object element = i.next();
			if (element instanceof ListElement) { 
				List list = ((ListElement)element).list;
				if (list.contains(element)) return true;
			} else if (element == value || (element != null && element.equals(value))) return true;
		}
		return false;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean containsAll(Collection arg0) {
		throw unsupportedOperation;
	}
	
	// find fast where the element is supposed to be
	private int binarySearch(int k) {
	    int low = 0, high = elements.size();

	    while (low <= high) {
	    	int mid = (low + high) >>> 1;
	    	int midVal = indexes[mid];

	    	if (midVal < k) low = mid + 1;
	    	else if (midVal > k) high = mid - 1;
	    	else return mid; // key found
	    }
	    return -(low + 1);  // key not found.
	}
	
	/**
	 * Get element at index.
	 * @param k index
	 * @return element
	 */
	public Object get(int k) {
		if (k < 0 || k >= n) throw new IndexOutOfBoundsException("" + k + " not in [0," + n + ')');
		int z = binarySearch(k);
		if (z >= 0) {
			Object elem = elements.get(z+1);
			if (elem instanceof ListElement) {
				List in = ((ListElement)elem).list;
				return in.get(0);
			} else return elem;
		} else {
			z = -z - 1; // this is where it would be inserted
			Object elem = elements.get(z);
			if (elem instanceof ListElement) {
				List list = ((ListElement)elem).list;
				return list.get(k - (z > 0 ? indexes[z-1] : 0));
			} else return elem;
		}
	}

	/**
	 * Find index of element.
	 * @param value element
	 * @return index of element or -1
	 */
	public int indexOf(Object value) {
		int i = 0;
		for(Iterator I = elements.iterator(); I.hasNext(); ) {
			Object elem = I.next();
			if (elem instanceof ListElement) {
				List list = ((ListElement)elem).list;
				int z = list.indexOf(value);
				if (z >= 0) return i + z;
				i += list.size();
			} else if (value == elem || (elem != null && elem.equals(value))) return i;
			else ++i;
			
		}
		return -1;
	}

	/** 
	 * @return whether list is empty
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/** 
	 * List iterator over the concatenation.
	 * @author istvan
	 *
	 */
	private class BundleIterator implements ListIterator {

		// true index in list
		private int index;
		
		private BundleIterator(int index) {
			this.index = index;
		}
		
		public boolean hasNext() {
			return index < n - 1;
		}

		public Object next() {
			if (!hasNext()) throw new NoSuchElementException();
			++index;
			return get(index);
		}

		public void remove() {
			throw unsupportedOperation;
		}

		public void add(Object value) {
			throw unsupportedOperation;
			
		}

		public boolean hasPrevious() {
			return index > 0;
		}

		public int nextIndex() {
			return index + 1;
		}

		public Object previous() {
			if (!hasPrevious()) throw new NoSuchElementException();
			--index;
			return get(index);
		}

		public int previousIndex() {
			return index - 1;
		}

		public void set(Object arg0) {
			throw unsupportedOperation;
			
		}
		
	}
	
	/**
	 * Get an iterator over the elements.
	 */
	public Iterator iterator() {
		return new BundleIterator(-1);
	}

	/**
	 * Last index of element.
	 * @param value element to be found
	 * @return last index of element or -1
	 */
	public int lastIndexOf(Object value) {
		int i = n-1;
		for(ListIterator I = elements.listIterator(elements.size()); I.hasPrevious(); ) {
			Object elem = I.previous();
			if (elem instanceof ListElement) {
				List list = ((ListElement)elem).list;
				int z = list.lastIndexOf(value);
				if (z >= 0) return i - (list.size() - z) + 1;
				i -= list.size();
			} else if (value == elem || (elem != null && elem.equals(value))) return i;
			else --i;
			
		}
		return -1;
	}

	/**
	 * Get a list iterator over the elements.
	 */
	public ListIterator listIterator() {
		return new BundleIterator(-1);
	}

	/**
	 * Get a list iterator over the elements.
	 * @param k index to start at (the first call to next will return an element under index k)
	 */
	public ListIterator listIterator(int k) {
		return new BundleIterator(k);
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public Object remove(int arg0) {
		throw unsupportedOperation;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean remove(Object arg0) {
		throw unsupportedOperation;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean removeAll(Collection arg0) {
		throw unsupportedOperation;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public boolean retainAll(Collection arg0) {
		throw unsupportedOperation;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public Object set(int arg0, Object arg1) {
		throw unsupportedOperation;
	}

	/** 
	 * @return number of elements
	 */
	public int size() {
		return n;
	}

	/**
	 * Not implemented.
	 * @throws UnsupportedOperationException
	 */
	public List subList(int i1, int i2) {
		throw unsupportedOperation;
	}

	/**
	 * Returns the elements as an array.
	 * @return as array 
	 */
	public Object[] toArray() {
		Object[] elems = new Object[size()];
		for(int i=0; i< size(); ++i) elems[i] = get(i);
		return elems;
	}

	/**
	 * Not implemented.
	 * 
	 */
	public Object[] toArray(Object[] elems) {

		if (elems.length < size()) {
			elems = (Object[])Array.newInstance(elems.getClass(), size());
		}
		for(int i=0; i< size(); ++i) elems[i] = get(i);
		return elems;
	}

}
