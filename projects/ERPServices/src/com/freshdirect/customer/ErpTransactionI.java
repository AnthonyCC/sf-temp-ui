package com.freshdirect.customer;

import java.util.Comparator;
import java.util.Date;

public interface ErpTransactionI {

	public final static Comparator<ErpTransactionI> TX_DATE_COMPARATOR = new Comparator<ErpTransactionI>() {

		public int compare( ErpTransactionI tx1, ErpTransactionI tx2 ) {
			try {
				return tx1.getTransactionDate().compareTo( tx2.getTransactionDate() );
			} catch(Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	};

	public abstract Date getTransactionDate();
}