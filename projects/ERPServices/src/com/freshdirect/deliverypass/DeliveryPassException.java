/**
 * 
 */
package com.freshdirect.deliverypass;

/**
 * @author skrishnasamy
 *
 */
public class DeliveryPassException extends Exception {
	private String customerPk;
		
	public String getCustomerPk() {
		return customerPk;
	}

	/**
	 * 
	 */
	public DeliveryPassException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DeliveryPassException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DeliveryPassException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DeliveryPassException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param msg
	 * @param customerPk
	 */
	public DeliveryPassException(String msg, String customerPk) {
		super(msg);
		this.customerPk = customerPk;
	}

	/**
	 * 
	 * @param msg
	 * @param customerPk
	 * @param throwable
	 */
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
