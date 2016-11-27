package com.freshdirect.deliverypass;

/**
 * @author skrishnasamy
 */

public class DeliveryPassException extends Exception {
	
	private static final long	serialVersionUID	= -5628685652868356390L;
	
	private String customerPk;
		
	public String getCustomerPk() {
		return customerPk;
	}

	public DeliveryPassException() {
		super();
	}

	public DeliveryPassException(String arg0) {
		super(arg0);
	}

	public DeliveryPassException(Throwable arg0) {
		super(arg0);
	}

	public DeliveryPassException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public DeliveryPassException(String msg, String customerPk) {
		super(msg);
		this.customerPk = customerPk;
	}

	public DeliveryPassException(String msg, String customerPk, Throwable t) {
		super(msg, t);
		this.customerPk = customerPk;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getMessage());
		sb.append("Customer Id : ");
		sb.append(getCustomerPk());
		return sb.toString();
	}
	
}
