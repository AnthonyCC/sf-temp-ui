package com.freshdirect.routing.ejb;

public class OrderCancelCommand implements java.io.Serializable{
	
			private String saleId;
			
			
			public String getSaleId() {
				return saleId;
			}

			public void setSaleId(String saleId) {
				this.saleId = saleId;
			}

		
}
