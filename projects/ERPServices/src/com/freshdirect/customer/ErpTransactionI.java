package com.freshdirect.customer;

import java.util.Comparator;
import java.util.Date;

public interface ErpTransactionI {

	public final static Comparator TX_DATE_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			ErpTransactionI tx1 = (ErpTransactionI) o1;
			ErpTransactionI tx2 = (ErpTransactionI) o2;
			return tx1.getTransactionDate().compareTo(tx2.getTransactionDate());
		}

	};

	public abstract Date getTransactionDate();
}