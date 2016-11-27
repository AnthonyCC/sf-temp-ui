package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.commons.collections.functors.TruePredicate;

/**
 * Helper for table layouts.
 * 
 * The helper chops a collection up and determines row boundaries for tabulated displays.
 * Optionally predicates can be supplied to decide whether an item should be in the collection
 * and whether it should occupy a row by itself.
 * 
 * Row boundaries can be set as column count or reaching a given width.
 * 
 * Layout is determined at construction.
 * 
 * @author istvan
 *
 */
public class TableLayoutHelper {
	
	/**
	 * Functor to calculate the display width of an object.
	 * 
	 */
	public interface WidthCalculator {
		
		/**
		 * Get width of object
		 * @param o object
		 * @return width
		 */
		public int getWidth(Object o);
	}
	
	// returns 1
	private static WidthCalculator one = new WidthCalculator() {
		public int getWidth(Object o) { return 1; }
	};
	
	// Da rows
	private List rows = new ArrayList();
	
	// size of full rows
	private List rowSizes = new ArrayList();
	
	// add row (no empty rows)
	private List addRow(List currentRow, int currentRowWidth) {
	    if (!currentRow.isEmpty()) {
	    	rows.add(currentRow);
	    	rowSizes.add(new Integer(currentRowWidth));
	    	return new ArrayList();
	    } else return currentRow;
	}
	
	/** Constructor.
	 * 
	 * @param i iterator of collection
	 * @param p predicate which evaluates whether item should be included
	 * @param ownRow predicate which evaluates whether item should have its own row
	 * @param wcalc a functor that calculates the width of an object
	 * @param maxWidth maximum width per row
	 */
	public TableLayoutHelper(Iterator i, Predicate p, Predicate ownRow, WidthCalculator wcalc, int maxWidth) {
		// all constructors should call this constructor
		
		int currentRowWidth = 0;
		List currentRow = new ArrayList();
		while( i.hasNext()) {
			Object o = i.next();
			if (p.evaluate(o)) { // o is in
				if (ownRow.evaluate(o)) { // o occupies a row by itself
					currentRow = addRow(currentRow,currentRowWidth);
					currentRowWidth = 0;
					currentRow.add(o);
					currentRow = addRow(currentRow,currentRowWidth);
					currentRowWidth = 0;
				} else { 
					int w = wcalc.getWidth(o);
					if (w + currentRowWidth > maxWidth) { // break row
						currentRow = addRow(currentRow,currentRowWidth);
						currentRowWidth = 0;
					}
					currentRow.add(o);
					currentRowWidth += w;
				}
			}
		}
		addRow(currentRow,currentRowWidth);
	}
	
	/** Constructor.
	 * 
	 * @param i iterator of collection
	 * @param p predicate which evaluates whether item should be included
	 * @param ownRow predicate which evaluates whether item should have its own row
	 * @param maxColumns maximum columns per row
	 */
	public TableLayoutHelper(Iterator i, Predicate p, Predicate ownRow, int maxColumns) {
		this(i,p,ownRow,one,maxColumns);
		
	}
	
	/** Constructor.
	 * 
	 * @param i iterator over collection
	 * @param p predicate which evaluates whether item should be included
	 * @param maxColumns maximum columns per row
	 */
	public TableLayoutHelper(Iterator i, Predicate p, int maxColumns) {
		this(i,p,FalsePredicate.getInstance(),maxColumns);
	}
	
	/** Constructor.
	 * 
	 * @param i iterator over collection
	 * @param maxColumns maximum columns per row
	 */
	public TableLayoutHelper(Iterator i, int maxColumns) {
		this(i,TruePredicate.getInstance(),FalsePredicate.getInstance(),maxColumns);
	}
	
	/** Constructor.
	 * 
	 * @param i iterator of collection
	 * @param p predicate which evaluates whether item should be included
	 * @param wcalc a functor that calculates the width of an object
	 * @param maxWidth maximum width per row
	 */
	public TableLayoutHelper(Iterator i, Predicate p, WidthCalculator wcalc, int maxWidth) {
		this(i,p,FalsePredicate.getInstance(),wcalc,maxWidth);
	}
	
	/** Constructor.
	 * 
	 * @param i iterator of collection
	 * @param wcalc a functor that calculates the width of an object
	 * @param maxWidth maximum width per row
	 */
	public TableLayoutHelper(Iterator i, WidthCalculator wcalc, int maxWidth) {
		this(i,TruePredicate.getInstance(),FalsePredicate.getInstance(),wcalc,maxWidth);
	}
	
	/** Get rows.
	 * 
	 * @return rows (List<ArrayList>)
	 */
	public List getRows() {
		return rows;
	}
	
	/** Get element.
	 * 
	 * @param i row index
	 * @param j column index
	 * @return element in ith row and jth column
	 */
	public Object getElement(int i, int j) {
		return ((List)rows.get(i)).get(j);
	}
	
	/** Get a particular row.
	 * @param i row index
	 * @return requested rows
	 */
	public List getRow(int i) {
		return (List)rows.get(i);
	}
	
	/** Get width of row.
	 * Same as {@link #getColumnCount(int)} for tables rows are broken by column count.
	 * @param i row index 
	 * @return number of rows.
	 */
	public int getRowWidth(int i) {
		return ((Integer)rowSizes.get(i)).intValue();
	}
	
	/** Get column count..
	 * Same as {@link #getRowWidth(int)} for tables rows are broken by column count.
	 * @param i row index
	 * @return column count
	 */
	public int getColumnCount(int i) {
		return ((List)rows.get(i)).size();
	}
}
