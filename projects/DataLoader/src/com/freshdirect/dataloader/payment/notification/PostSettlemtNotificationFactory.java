package com.freshdirect.dataloader.payment.notification;

public class PostSettlemtNotificationFactory {

	public IPostSettlementNotification getSettlementProcessor(String processor){
		if("AVALARA".equals(processor)){
			return new PostSettlementAvalaraNotificationProcessor();
		}
		return null;
	}
	
}
