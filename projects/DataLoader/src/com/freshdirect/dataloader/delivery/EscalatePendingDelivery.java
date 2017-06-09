package com.freshdirect.dataloader.delivery;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.PendingOrder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class EscalatePendingDelivery {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(EscalatePendingDelivery.class);

	public static void main(String[] args) {
		
		EscalatePendingDelivery escalatePendingDelivery = new EscalatePendingDelivery();
		Map<String, List<PendingOrder>> pendingOrders = escalatePendingDelivery.getPendingDeliveries();
		
		List<PendingOrder> freshdirectOrders = pendingOrders.get("freshdirect");
		List<PendingOrder> fdxOrders = pendingOrders.get("freshdirect");
		if(null != freshdirectOrders && freshdirectOrders.size()> 0){
		escalatePendingDelivery.sendExceptionMail(freshdirectOrders,"FreshDirect");
		}
		
		if(null != fdxOrders && fdxOrders.size()> 0){
			escalatePendingDelivery.sendExceptionMail(fdxOrders, "Foodkick");
		}
	}

	private Map<String, List<PendingOrder>> getPendingDeliveries() {
		Map<String, List<PendingOrder>> pendingOrders = null;
		try {
			pendingOrders = FDCustomerManager.getPendingDeliveries();
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
		return pendingOrders;
	}
	
	private void sendExceptionMail(List<PendingOrder> pendingOrders, String eStore) {
		try {
			String subject= "Delivery confirmation needed-"+eStore+" orders";

			StringWriter sw = new StringWriter();

			sw.write("<html>");
			sw.write("<body>");	

			sw.write("Please delivery confirm the "+eStore+" orders.");
			sw.write("\n\n");
			sw.write("<table border='1'><tr>");
			sw.write("<td>");
			sw.write("Order Count");
			sw.write("</td>");
			sw.write("<td>");
			sw.write("Delivery Date");
			sw.write("</td>");
			sw.write("</tr>");
				for(PendingOrder order:pendingOrders){
					sw.write("<tr><td>");
					sw.write(order.getOrderCount());
					sw.write("</td><td>");
					sw.write(order.getDeliveryDate().substring(0,10));
					sw.write("</td></tr>");
				}
				sw.write("</table>");
				sw.write("\n\n\n");
				sw.write("</body>");
				sw.write("</html>");

			ErpMailSender mailer = new ErpMailSender();
			String ccList="";
			if(eStore.equals("FreshDirect")){
				ccList = ErpServicesProperties.getPendingDeliveryEscalationMailCcFD();
			}
			else{
				ccList = ErpServicesProperties.getPendingDeliveryEscalationMailCcFDX();
			}
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getPendingDeliveryEscalationMailTo(),ccList,
					subject, sw.getBuffer().toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.error("Error Sending Delivery confirmation escalation email: ", e);
		}
	}
}
