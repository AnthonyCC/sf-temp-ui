package com.freshdirect.routing.ejb;

public class OrderCreateCommand implements java.io.Serializable{
	
		
		private String reservationId;
		
		private String sapOrderNumber;
		
		private Double tip;
		
		private String saleId;
		
		private String parentOrderId;
		
		private String deliveryInstructions;
		
		private String serviceType;
		
		private String unattendedInstr;
		
		private String orderMobileNumber;
		
		
		public String getReservationId() {
			return reservationId;
		}

		public void setReservationId(String reservationId) {
			this.reservationId = reservationId;
		}

		public String getSapOrderNumber() {
			return sapOrderNumber;
		}

		public void setSapOrderNumber(String sapOrderNumber) {
			this.sapOrderNumber = sapOrderNumber;
		}

		public Double getTip() {
			return tip;
		}

		public void setTip(Double tip) {
			this.tip = tip;
		}

		public String getSaleId() {
			return saleId;
		}

		public void setSaleId(String saleId) {
			this.saleId = saleId;
		}

		public String getParentOrderId() {
			return parentOrderId;
		}

		public void setParentOrderId(String parentOrderId) {
			this.parentOrderId = parentOrderId;
		}

		public String getOrderMobileNumber() {
			return orderMobileNumber;
		}

		public void setOrderMobileNumber(String orderMobileNumber) {
			this.orderMobileNumber = orderMobileNumber;
		}

		public String getDeliveryInstructions() {
			return deliveryInstructions;
		}

		public void setDeliveryInstructions(String deliveryInstructions) {
			this.deliveryInstructions = deliveryInstructions;
		}

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		public String getUnattendedInstr() {
			return unattendedInstr;
		}

		public void setUnattendedInstr(String unattendedInstr) {
			this.unattendedInstr = unattendedInstr;
		}
		
		
	}



