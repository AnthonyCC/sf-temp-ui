package com.freshdirect.fdstore;

public enum EnumCheckoutMode {
	NORMAL,				// standard checkout process
	CREATE_SO,			// create standing order (new SO + CCL, new order)
	MODIFY_SO_CSOI,		// modify standing order (update SO + CCL, new order)
	MODIFY_SO_MSOI,		// modify standing order (update SO + CCL, edit order)
	MODIFY_SO_TMPL;		// modify standing order template (update SO + CCL, NO order created / edited)
	
	/**
	 * Returns true if original cart content is saved during checkout phase
	 * so cart content must be restored after canceling a pending SO action 
	 */
	public boolean isCartSaved() {
		return (this == EnumCheckoutMode.MODIFY_SO_CSOI ||
				this == EnumCheckoutMode.MODIFY_SO_MSOI ||
				this == EnumCheckoutMode.MODIFY_SO_TMPL
				);
	}
	
	public boolean isModifyStandingOrder() {
		return (this == EnumCheckoutMode.MODIFY_SO_CSOI ||
				this == EnumCheckoutMode.MODIFY_SO_MSOI ||
				this == EnumCheckoutMode.MODIFY_SO_TMPL
				);
	}

	public boolean isStandingOrderMode() {
		return (this == EnumCheckoutMode.CREATE_SO ||
				this == EnumCheckoutMode.MODIFY_SO_CSOI ||
				this == EnumCheckoutMode.MODIFY_SO_MSOI ||
				this == EnumCheckoutMode.MODIFY_SO_TMPL
				);
	}
}
