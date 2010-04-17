package com.freshdirect.giftcard;


public class ErpRecipentModel extends RecipientModel {

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSale_id() {
		return sale_id;
	}

	public void setSale_id(String sale_id) {
		this.sale_id = sale_id;
	}

	private String customerId=null;
	private String sale_id=null;
	private String orderLineId=null;
	

	public String getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

		

	public ErpRecipentModel(){	
		super();
	}
	public void toModel(RecipientModel model){
        this.setAmount(model.getAmount());
        this.setDeliveryMode(model.getDeliveryMode());
        this.setRecipientEmail(model.getRecipientEmail());
        this.setRecipientName(model.getRecipientName());
        this.setPersonalMessage(model.getPersonalMessage());
        this.setSenderEmail(model.getSenderEmail());
        this.setSenderName(model.getSenderName());
        this.setTemplateId(model.getTemplateId());            
  }
}
