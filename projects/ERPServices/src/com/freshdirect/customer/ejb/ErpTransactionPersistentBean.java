/* Generated by Together */

package com.freshdirect.customer.ejb;

import com.freshdirect.customer.*;
import java.util.Date;

/**
 * Abstract superclass for read-only ERP PBeans.
 * 
 * @version    $Revision:4$
 * @author     $Author:Viktor Szathmary$
 * @stereotype fd-persistent
 */
abstract class ErpTransactionPersistentBean extends ErpReadOnlyPersistentBean implements ErpTransactionI {

    protected Date transactionDate;
    protected EnumTransactionSource transactionSource;
    protected String transactionInitiator;
	/** Default constructor. */
	public ErpTransactionPersistentBean() {
		super();
	}

    public void decorateModel(ErpTransactionModel model) {
        super.decorateModel(model);
		model.setTransactionSource( this.transactionSource );
        model.setTransactionDate( this.transactionDate );
        model.setTransactionInitiator(this.transactionInitiator);
    }
    
    public Date getTransactionDate(){
    	return this.transactionDate;
    }

    public String getTransactionInitiator() {
    	return this.transactionInitiator;
    }
}
