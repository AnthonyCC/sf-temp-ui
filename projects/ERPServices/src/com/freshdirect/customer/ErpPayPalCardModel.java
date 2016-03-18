package com.freshdirect.customer;

import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * ErpCreditCard model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpPayPalCardModel extends ErpCreditCardModel {

	private static final long	serialVersionUID	= 4226856315306939606L;
	
	
	public EnumPaymentMethodType getPaymentMethodType() {
		return EnumPaymentMethodType.PAYPAL;
	}

}


