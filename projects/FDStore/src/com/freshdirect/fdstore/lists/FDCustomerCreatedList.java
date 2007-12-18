package com.freshdirect.fdstore.lists;

import java.util.Comparator;

import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;



public class FDCustomerCreatedList extends FDCustomerProductList {
	
	private static final long serialVersionUID = -584907125438726272L;
	
	/** Maximum allowed length in characters for a list name. */
	public static final int MAX_NAME_LENGTH = 35;
	
	public FDCustomerCreatedList() {
	}
	
	
	private static class CompareByModificationDate implements Comparator {
		 public int compare(Object o1, Object o2) {
			 if (!(o1 instanceof FDCustomerCreatedList) || !(o2 instanceof FDCustomerCreatedList)) return 0;
			 
			 FDCustomerCreatedList l1 = (FDCustomerCreatedList)o1;
			 FDCustomerCreatedList l2 = (FDCustomerCreatedList)o2;
			 
			 if (l1.getModificationDate() == null) {
				 return l2.getModificationDate() == null ? 0 : 1;
			 }
			 if (l2.getModificationDate() == null) {
				 return -1;
			 }
			 
			 return - l1.getModificationDate().compareTo(l2.getModificationDate());
		 }
		 
		 public boolean equals(Object o) {
			 // there is only one static instance
			 return o == this;
		 }
	}
	
	private static class CompareByName implements Comparator {
		 public int compare(Object o1, Object o2) {
			 if (!(o1 instanceof FDCustomerCreatedList) || !(o2 instanceof FDCustomerCreatedList)) return 0;
			 
			 FDCustomerCreatedList l1 = (FDCustomerCreatedList)o1;
			 FDCustomerCreatedList l2 = (FDCustomerCreatedList)o2;
			 
			 // should not return 0, since YoYo and yoyo than would be treated equal
			 return l1.getName().compareToIgnoreCase(l2.getName()) < 0 ? -1 : 1;
		 }
		 
		 public boolean equals(Object o) {
			 // there is only one static instance
			 return o == this;
		 }
	}
	
	private static class CompareByItemCount implements Comparator {
		
		public int compare(Object o1, Object o2) {
			 if (!(o1 instanceof FDCustomerCreatedList) || !(o2 instanceof FDCustomerCreatedList)) return 0;
			 
			 
			 if (o1.equals(o2)) return 0;
			 
			 int c1 = ((FDCustomerCreatedList)o1).getCount();
			 int c2 = ((FDCustomerCreatedList)o2).getCount();
			 
			 
			 return  c1 < c2 ? 1 : -1;
	     }
		 
		 public boolean equals(Object o) {
			 // there is only one static instance
			 return o == this;
		 }
	}
	
	private static Comparator compareByModificationDate = new CompareByModificationDate();
	private static Comparator compareByName = new CompareByName();
	private static Comparator compareByItemCount = new CompareByItemCount();
	
	public static Comparator getModificationDateComparator() { return compareByModificationDate; }
	public static Comparator getNameComparator() { return compareByName; }
	public static Comparator getItemCountComparator() { return compareByItemCount; }
	
	public void mergeSelection(FDProductSelectionI selection, boolean modifying) {
		super.mergeSelection(selection, modifying);
		// the above call will mark this list as modified
	}
	
	public void addLineItem(FDCustomerProductListLineItem item) {
		getLineItems().add(item);
		markAsModified();
	}

	/**
	 *  Return the type of list this implementation handles.
	 *  
	 *  @return the list type corresponding to customer created lists
	 *  @see EnumCustomerListType#CC_LIST
	 */
	public EnumCustomerListType getType() {
		return EnumCustomerListType.CC_LIST; 
	}

	public int getCount() {
		return getLineItems().size();
	}
}
