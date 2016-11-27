package com.freshdirect.security.ticket;

public class MasqueradePurposeBuilder {
	private static final String DELIMITER = "|";
	private static final int MAX_LENGTH = 80; //cust.securityticket.purpose field
	
	@Deprecated
	public static String buildPurpose(String userId, boolean forceOrderAvailable, String makeGoodFromOrderId, boolean autoApproveAuthorized, String autoApprovalLimit, String shopFromOrderId, String modifyOrderId){
		StringBuilder sb = new StringBuilder();
		sb.append(userId);
		
		sb.append(DELIMITER);
		sb.append(forceOrderAvailable ? "T" : "F");
		
		if (makeGoodFromOrderId!=null){
			sb.append(DELIMITER);
			sb.append(makeGoodFromOrderId);
		}

		sb.append(DELIMITER);
		sb.append(autoApproveAuthorized ? "T" : "F");

		if (autoApprovalLimit!=null){
			sb.append(DELIMITER);
			sb.append(autoApprovalLimit);
		}

		if (shopFromOrderId!=null){
			sb.append(DELIMITER);
			sb.append(shopFromOrderId);
		}
		
		if (modifyOrderId!=null){
			sb.append(DELIMITER);
			sb.append(modifyOrderId);
		}

		String purpose = sb.toString();
		return purpose.length() > MAX_LENGTH ? purpose.substring(0, MAX_LENGTH) : purpose;
	}

	@Deprecated
	public static String buildPurpose(String userId, boolean forceOrderAvailable, String makeGoodFromOrderId, boolean autoApproveAuthorized, String autoApprovalLimit){
		return buildPurpose(userId, forceOrderAvailable, makeGoodFromOrderId, autoApproveAuthorized, autoApprovalLimit, null, null);
	}

	public static String buildPurpose(MasqueradeParams params) {
		return Integer.toHexString(params.hashCode());
		// return buildPurpose(params.userId, params.forceOrderAvailable, params.makeGoodFromOrderId, params.autoApproveAuthorized, params.autoApprovalLimit, params.shopFromOrderId, params.modifyOrderId);
	}
}
