/*
 * ErpReversalModel.java
 *
 * Created on January 14, 2002, 8:30 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
public class ErpReversalModel extends ErpPaymentMethodTransactionModel{

	/** Creates new ErpReversalModel */
    public ErpReversalModel() {
		super(EnumTransactionType.REVERSAL);
    }

}
