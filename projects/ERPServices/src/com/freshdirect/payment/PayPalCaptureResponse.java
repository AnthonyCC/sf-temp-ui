package com.freshdirect.payment;

public class PayPalCaptureResponse {

	public static enum Processor {

		SETTLED("4000"), SETTLEMENT_PENDING("4002"), SETTLEMENT_DECLINED("4001"), ALREADY_CAPTURED(
				"4003"), ALREADY_REFUNDED("4004"), RISK_REJECTED("4005"), AMOUNT_EXCEEDS_ALLOWABLE_LIMIT("4006");

		private final String responseCode;

		Processor(String responseCode) {
			this.responseCode = responseCode;
		}

		/**
		 * @return the responseCode
		 */
		public String getResponseCode() {
			return responseCode;
		}

	}

}
