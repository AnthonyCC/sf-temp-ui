package com.freshdirect.framework.util;


import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * Implements Cartesian product as a virtual list.
 * 
 * The cartesian product of n lists L1, L2, ... Ln is a set of all distinct tuples (lists), such that
 * the value in a tuple in the kth position is in Lk.
 *
 * This implementation does not store explicit tuples but produces them on demand.
 * 
 * There is NO synchronization whatsoever in this class. Concurrent access must use external means of
 * synchronization.
 * 
 * The iterator will generate the tuples in lexicographic left to right order. In other words,
 * for L1 = {1,2,3}, L2 = {1,2}, L3 = {1,2} the tuples are
 * <pre>
 *   L1 x L2 x L3 =
 *   [
 *      [1 1 1]
 *      [1 1 2]
 *      [1 2 1]
 *      [1 2 2]
 *      [2 1 1]
 *      [2 1 2]
 *      [2 2 1]
 *      [2 2 2]
 *      [3 1 1]
 *      [3 1 2]
 *      [3 2 1]
 *      [3 2 2] 
 *   ]
 * </pre>
 * 
 * Frequent use of <tt>list.get(int)</tt> is used, thus the original sequences should not be 
 * instances of <tt>LinkedList</tt> to achieve decent performance.
 * Also, no check for duplicates in the individual sets is performed, and in that case duplicate
 * tuples may be generated (this violates the mathematical definition, but could still be useful).
 * 
 * CartesianProduct implements the interfaces List and RandomAccess. Since this implementation is
 * a virtual list (the elements are calculated on demand), all modifying methods of the List interface
 * throw UnsupportedOperationExceptions. The RandomAccess interface may instruct
 * algorithms that get(int) is fast and is the preferred access method for elements.
 * 
 * CartesianProduct being a List instance may be a sequence in other cartesian products.
 * For example the cartesian product of L1 with the cartesian product of L2 and L3 is
 * <pre>
 *    L1 x (L2 x L3) =
 *    [
 *       [1 [1 1]]
 *       [1 [1 2]]
 *       [1 [2 1]]
 *       [1 [2 2]]
 *       [2 [1 1]]
 *       [2 [1 2]]
 *       [2 [2 1]]
 *       [2 [2 2]]
 *       [3 [1 1]]
 *       [3 [1 2]]
 *       [3 [2 1]]
 *       [3 [2 2]]
 *    ]
 * </pre>
 * Memory is always proportional to the original generating sequences (ie. L1, L2 and L3; L2xL3 is not 
 * explicitly enumerated). 
 *
 * @author istvan
 *
 */
public class CartesianProduct implements List, RandomAccess {
	
	private static UnsupportedOperationException unsupportedException = 
		new UnsupportedOperationException();

	
	private List sequences = new ArrayList(); // original generating sequences List<List>

	// used in calculating the period of the generating sequences, List<Integer>
	private List repCounts = new ArrayList(); 


	// total number of tuples in the product
	private int n = 0;

	/** 
	 * Add a generating sequence.
	 * @param sequence
	 */
	public void addSequence(List sequence) {
		sequences.add(sequence);
		if (n == 0) n = sequence.size();
		else n *= sequence.size();
		repCounts.add(new Integer(1));
		for(int i= repCounts.size() - 2; i>=0; --i) {
			// this looks much better with Java Generics
			// calc how many times a tuple value repeats in consecutive tuples 
			// repCounts[i] = repCounts[i+1] * sequences[i+1].size()
			repCounts.set(i,
				new Integer(((Integer)repCounts.get(i+1)).intValue()*((List)sequences.get(i+1)).size()));
		}
	}
	
	/**
	 * Returns true if the argument is a cartesian product and would generate the same tuples.
	 * @param o object
	 * @return if the argument would produce the same elements
	 */
	public boolean equals(Object o) {
		if (!(o instanceof CartesianProduct)) return false;
		CartesianProduct CP = (CartesianProduct)o;
		if (size() != CP.size()) return false;
		if (n == 0) return true; // all Cartesian Products of size 0 are the same
		if (CP.sequences.size() != sequences.size()) return false;
		for(Iterator i = sequences.iterator(), j =CP.sequences.iterator(); i.hasNext(); ) {
			if (!i.next().equals(j.next())) return false;
		}
		return true;
	}
	
	
	/**
	 * Set generating sequences.
	 * 
	 */
	public void setSequences(List[] seqs) {
		this.sequences = new ArrayList(seqs.length);
		n = 0;
		repCounts.clear();
		for(int i=0; i< seqs.length; ++i) addSequence(seqs[i]);
	}

	/**
	 * Total size of product.
	 * @return number of tuples
	 */
	public int size() { return n; }


	/**
	 * Get a particular tuple element from the set.
	 * @param k the tuple's position according to the ordering
	 * @param i the ith value in the tuple
	 * @return the ith value in tuple k
	 */
	public Object getElement(int k, int i) {
		if (k < 0 || k >= n) throw new NoSuchElementException();
		// this really needs Java 1.5 or later!
		// calculate which value to use from the ith list
		// -> sequences[i].get(k % (repCounts[i]*seqences[i].size()) / repcounts[i])
		return ((List)sequences.get(i)).get(
			(k % (((Integer)repCounts.get(i)).intValue()*((List)sequences.get(i)).size())) / 
			((Integer)repCounts.get(i)).intValue());
	}
	
	/**
	 * Get a particular tuple from the set.
	 * 
	 * @param k the tuple's position accrding to the ordering
	 * @return the kth tuple in a new instance of list
	 * @throws NoSuchElementException
	 */
	public List getTuple(int k) {
		if (k < 0 || k >= n) throw new NoSuchElementException();
		List tuple = new ArrayList(sequences.size());
		for(int i=0; i< sequences.size(); tuple.add(getElement(k,i++)));
		return tuple;
	}
	
	/**
	 * Ith tuple in product.
	 * Simply calls getTyple().
	 * @param i index in tuple
	 * @return
	 */
	public Object get(int i) {
		return getTuple(i);
	}


	/**
	 * Iterator over a cartesian product.
	 * 
	 * Also note that no synchronization is perfromed internally.
	 * 
	 */
	public class TupleIterator implements ListIterator {

		private int ci; /** current pointer position */

		private TupleIterator(int from) {
			ci = from;
		}
	       
		/**
		 * Is there a next tuple.
		 *
		 * @return whether there is a next element in the sequence
		 */
		public boolean hasNext() {
			return ci < n - 1;
		}

		/**
		 * A tuple in the product.
		 * @return a tuple (instance of List) where the ith element is from the ith sequence.
		 */
		public Object next() {
			advance();
			List result = new ArrayList(sequences.size());
			for(int i=0; i < sequences.size(); ++i) result.add(getElement(ci,i));
			return result;
		}


		/**
		 * Not implemented.
		 * 
		 * @throws UnsupportedOperationException
		 */
		public void remove() {
			throw unsupportedException;
		}

		
		/**
		 * Not implemented.
		 * 
		 * @throws UnsupportedOperationException
		 */
		public void set(Object o) {
			throw unsupportedException;
		}

		/**
		 * Not implemented.
		 * 
		 * @throws UnsupportedOperationException
		 */
		public void add(Object o) {
			throw unsupportedException;
		}

		/**
		 * Move the cursor position back without calculating the tuple.
		 */
		public void stepBack() {
			if (!hasPrevious()) throw new NoSuchElementException();
			--ci;
		}

		/**
		 * Move the pointer forward without calculating the tuple.
		 *
		 */
		public void advance() {
			if (!hasNext()) throw new NoSuchElementException();
			++ci;
		}
		
		/** 
		 * Returns the index of the current tuple pointed to.
		 * @return index of current element or -1 if next() has not been called
		 */
		public int index() {
			return ci;
		}

		/** 
		 * Set the position of the cursor.
		 *
		 * @param index index
		 */
		public void setIndex(int index) {
			if (index < -1 || index > n) throw new IndexOutOfBoundsException();
			ci = index;
		}

		/** 
		 * Returns the index of the element that would be returned by a subsequent call to next.
		 * @return index of next element
		 */
		public int nextIndex() {
			return ci+1;
		}

		/** 
		 * Returns the index of the element that would be returned by a subsequent call to previous.
		 * @return index of previous element
		 */
		public int previousIndex() {
			return ci-1;
		}

		/** Returns true if there is a previous element.
		 *
		 * @return if there is a previous element
		 */
		public boolean hasPrevious() {
			return ci > 0;
		}

		/** Returns the previous tuple.
		 *
		 * @return previous tuple
		 */
		public Object previous() {
			stepBack();
			List result = new ArrayList(sequences.size());
			for(int i=0; i < sequences.size(); ++i) result.add(getElement(ci,i));
			return result;
		}

	}
	
	/** 
	 * Get a tuple iterator over the product.
	 * @return a tuple iterator
	 */
	public Iterator iterator() { return new TupleIterator(-1); }

	/**
	 * Calculate the entire carterisan product.
	 * 
	 * This may be extremely large if the sequences are themselves large.
	 * It is probably more sensible to iterate through the test.
	 * @return the cartesian product of the sequences
	 */
	public List getExplicitProduct() {
		if (n == 0) return Collections.EMPTY_LIST;
		List result = new ArrayList((int)n);
		for(Iterator I = iterator(); I.hasNext(); result.add(new ArrayList((List)I.next())));
		return result;
	}

	/** 
	 * Is empty.
	 * 
	 * Note that the product will be empty if any of the generating sequences is empty!
	 * 
	 * @return if product is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Remove all generating sequences.
	 */
	public void clear() {
		n = 0;
		sequences.clear();
		repCounts.clear();
	}
	
	/**
	 * Get the generating sequences. 
	 * @return generating sequences (List<List>)
	 */
	public List getSequences() { return sequences; }
	
	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Object remove(int index) {
		throw unsupportedException;
	}
			
	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean add(Object o) {
		throw unsupportedException;
	}
	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */

	public void add(int index, Object element) {
		throw unsupportedException;		
	}
	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(Collection c) {
		throw unsupportedException;
	}
	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(int index, Collection c) {
		throw unsupportedException;
	}

	/**
	 * Returns true if this list contains the specified element.
	 * 
	 * This method does not enumerated the product, but will
	 * call <tt>contains</tt> on the original generating sequences.
	 * @param o tuple
	 * @return true if tuple is contained in the product.
	 */
	public boolean contains(Object o) {
		if (n == 0) return false;
		if (!(o instanceof List)) return false;
		List tuple = (List)o;
		if (tuple.size() != sequences.size()) return false;
		for(int i=0; i< tuple.size(); ++i) 
			if (!((List)sequences.get(i)).contains(tuple.get(i))) return false;
		return true;
	}

	/**
	 * Returns true if this list contains all of the specified elements.
	 * @param c elements to check
	 * @return if all elements are containd in the product.
	 * @see #contains(Object)
	 */
	public boolean containsAll(Collection c) {
		for(Iterator i = c.iterator(); i.hasNext(); ) 
			if (!contains(i.next())) return false;
		return true;
	}

	/**
	 * Returns the index of the first occurence of this element.
	 *
	 * The index is calculated without explicit enumeration, thus its
	 * complexity is proportional to the <tt>indexOf</tt> methods of
	 * the original generating sequences.
	 *
	 * @return the index of the argument in the product, or -1
	 * @see #isRemoved
	 */
	public int indexOf(Object o) {
		if (!(o instanceof List)) return -1;
		List tuple = (List)o;
		if (tuple.size() != sequences.size()) return -1;
		int result = 0;
		for(int i=0; i< sequences.size(); ++i) {
			int z = ((List)sequences.get(i)).indexOf(tuple.get(i));
			if (z == -1) return -1;
			result += ((Integer)repCounts.get(i)).intValue() * z;
		}
		return result;
	}

	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public int lastIndexOf(Object o) {
		if (!(o instanceof List)) return -1;
		List tuple = (List)o;
		if (tuple.size() != sequences.size()) return -1;
		int result = 0;
		for(int i=0; i< sequences.size(); ++i) {
			int z = ((List)sequences.get(i)).lastIndexOf(tuple.get(i));
			if (z == -1) return -1;
			result += ((Integer)repCounts.get(i)).intValue() * z;
		}
		return result;
	}

	/**
	 * Returns a list iterator over the elements of the product.
	 * 
	 * @return iterator
	 */
	public ListIterator listIterator() {
		return new TupleIterator(-1);
	}

	/**
	 * Returns a list iterator over the elements of the product starting at the index
	 * 
	 * @param index cursor position
	 * @return iterator
	 */
	public ListIterator listIterator(int index) {
		return new TupleIterator(index - 1);
	}
	

	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean remove(Object o) {
		throw unsupportedException;
	}

	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean removeAll(Collection c) {
		throw unsupportedException;
	}

	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean retainAll(Collection c) {
		throw unsupportedException;
	}

	/**
	 * Not implemented.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Object set(int index, Object element) {
		throw unsupportedException;
	}

	/**
	 * Hash code.
	 *
	 * @return hash code
	 */
	public int hashCode() {
		if (n == 0) return 0;
		int hashCode = 1;
		Iterator i = sequences.iterator();
		while (i.hasNext()) {
			List seq = (List)i.next();
			hashCode = 31*hashCode + seq.hashCode();
		}
		return hashCode;
	}

	/**
	 * Returns a sublist including the start index and upto but not including the end index.
	 *
	 * This implementation is very inefficient (which could be improved). 
	 *
	 * @param fromIndex index to start at in the product
	 * @param toIndex index after the last to be included 
	 * @return a sublist with indices [fromIndex,toIndex)
	 * @todo improve efficiency
	 * @throws IndexOutOfBoundsException when indices are out of range
	 */
	public List subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 ||  toIndex > n || fromIndex > toIndex) throw new IndexOutOfBoundsException();
		if (toIndex == fromIndex) return Collections.EMPTY_LIST;
		List result = new ArrayList(toIndex - fromIndex);
		for(int i = fromIndex; i< toIndex; ++i) {
			result.add(get(i));
		}
		return result;
	}
	
	/**
	 * Get product as array.
	 * This will cause the product to be calculated explicitly!
	 * The preferred methods of tuple access are get(int) and iterator().
	 * @return product as an array of Lists
	 */
	public Object[] toArray() {
		return getExplicitProduct().toArray();
	}

	/**
	 * Get product as an array.
	 * @return product as an array of Lists
	 * @see #toArray()
	 */
	public Object[] toArray(Object[] a) {
		if (a instanceof List[]) {
			if (a.length >= n) {
				if (a instanceof ArrayList[]) {
					for(int i=0; i< n; ++i) a[i] = get(i);
					for(int i=n; i< a.length; ++i) a[i] = null;
					return a;
				}
			} else return toArray();
		}
		
		
		throw new ClassCastException();
	}		
}
