package com.freshdirect.security.ticket;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;


/**
 * Simple POJO to keep track of parameters of a masqueraded request
 * 
 * Note, always update hashCode() function after changing parameters!
 * 
 * @author segabor
 *
 */
public class MasqueradeParams implements Serializable {
	private static final long serialVersionUID = 3672938250361543309L;

	public String userId;					// aka customerId
	
	public boolean hasCustomerCase;

	public boolean forceOrderAvailable;		// source : crm security
	public boolean autoApproveAuthorized;	// source : crm security
	public String autoApprovalLimit;		// source : menu manager

	public String makeGoodFromOrderId;		// source : request
	public String shopFromOrderId;			// source : request
	public String modifyOrderId;			// source : request
	
	public String destination;

	
	public MasqueradeParams() {}



	/**
	 * this method is used to capture parameters entirely from request
	 * in storefront side
	 * 
	 * @param request
	 */
	public MasqueradeParams(HttpServletRequest request) {
		userId = request.getParameter("customerId");

		hasCustomerCase = Boolean.parseBoolean(request.getParameter("case"));
		
		forceOrderAvailable = Boolean.parseBoolean(request.getParameter("forceOrderAvailable"));
		makeGoodFromOrderId = request.getParameter("makeGoodFromOrderId");
		autoApproveAuthorized = Boolean.parseBoolean(request.getParameter("autoApproveAuthorized"));
		autoApprovalLimit = request.getParameter("autoApprovalLimit");
		shopFromOrderId = request.getParameter("shopFromOrderId");
		modifyOrderId = request.getParameter("modifyOrderId");

		destination = request.getParameter("destination");
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((autoApprovalLimit == null) ? 0 : autoApprovalLimit
						.hashCode());
		result = prime * result + (autoApproveAuthorized ? 1231 : 1237);
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + (forceOrderAvailable ? 1231 : 1237);
		result = prime * result + (hasCustomerCase ? 1231 : 1237);
		result = prime
				* result
				+ ((makeGoodFromOrderId == null) ? 0 : makeGoodFromOrderId
						.hashCode());
		result = prime * result
				+ ((modifyOrderId == null) ? 0 : modifyOrderId.hashCode());
		result = prime * result
				+ ((shopFromOrderId == null) ? 0 : shopFromOrderId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MasqueradeParams other = (MasqueradeParams) obj;
		if (autoApprovalLimit == null) {
			if (other.autoApprovalLimit != null)
				return false;
		} else if (!autoApprovalLimit.equals(other.autoApprovalLimit))
			return false;
		if (autoApproveAuthorized != other.autoApproveAuthorized)
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (forceOrderAvailable != other.forceOrderAvailable)
			return false;
		if (hasCustomerCase != other.hasCustomerCase)
			return false;
		if (makeGoodFromOrderId == null) {
			if (other.makeGoodFromOrderId != null)
				return false;
		} else if (!makeGoodFromOrderId.equals(other.makeGoodFromOrderId))
			return false;
		if (modifyOrderId == null) {
			if (other.modifyOrderId != null)
				return false;
		} else if (!modifyOrderId.equals(other.modifyOrderId))
			return false;
		if (shopFromOrderId == null) {
			if (other.shopFromOrderId != null)
				return false;
		} else if (!shopFromOrderId.equals(other.shopFromOrderId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	
}
