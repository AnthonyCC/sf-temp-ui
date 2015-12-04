package com.freshdirect.referral.extole;

public class ExtoleServiceException extends  Exception {
	
	private static final long serialVersionUID = -4404726882838158210L;
	private String details;
	
	public ExtoleServiceException() {
		super();
	}
	
	public ExtoleServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExtoleServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExtoleServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ExtoleServiceException(String message,String details) {
		super(message);
		this.details = details;
	}
	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExtoleServiceException [message="+this.getMessage()+". details=" + details + "]";
	}
	
}
