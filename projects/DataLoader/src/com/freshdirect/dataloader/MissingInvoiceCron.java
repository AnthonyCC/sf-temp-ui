package com.freshdirect.dataloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDOrderSearchCriteria;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/**@author ekracoff on May 12, 2004*/
public class MissingInvoiceCron extends DBReportCreator {
	private final static Category LOGGER = LoggerFactory.getInstance(MissingInvoiceCron.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Missing Invoice Report Started");
		Context ctx = null;
		try {
			ctx = getInitialContext();
			FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
			FDCustomerManagerSB sb = home.create();

			FDOrderSearchCriteria criteria = new FDOrderSearchCriteria();
			criteria.setDeliveryDate(Calendar.getInstance().getTime());
			
			List orders = sb.locateOrders(criteria);
			List missingOrders = new ArrayList();

			int processed = 0;
			int invoiced = 0;
			for (Iterator i = orders.iterator(); i.hasNext();) {

				FDCustomerOrderInfo order = (FDCustomerOrderInfo) i.next();
				EnumSaleStatus status = order.getOrderStatus();

				if (EnumSaleStatus.INPROCESS.equals(status)) {
					processed++;
					missingOrders.add(order.getSaleId());
				} else if (
					EnumSaleStatus.SETTLED.equals(status)
						|| EnumSaleStatus.ENROUTE.equals(status)
						|| EnumSaleStatus.PAYMENT_PENDING.equals(status)
						|| EnumSaleStatus.REFUSED_ORDER.equals(status)
						|| EnumSaleStatus.RETURNED.equals(status)
						|| EnumSaleStatus.CAPTURE_PENDING.equals(status)) {
					processed++;
					invoiced++;
				}
			}
			
			StringBuffer buff = new StringBuffer();
			
			buff.append("---------------------------------------------\n");
			buff.append("Daily order report ( ").append(new SimpleDateFormat().format(new Date())).append("):\n");
			buff.append("---------------------------------------------\n");
			buff.append("Orders Processed: ").append(processed).append("\n");
			buff.append("Orders Invoiced: ").append(invoiced).append("\n");
			
			buff.append("Missing: ").append(missingOrders.size()).append("\n");
			int count = 1;
			for(Iterator ri = missingOrders.iterator(); ri.hasNext(); count++){
				String id = (String) ri.next();
				buff.append("\t").append(count).append(".  ").append(id);
			}
			buff.append("---------------------------------------------\n");

			ErpMailSender mailer = new ErpMailSender();
			System.out.println(ErpServicesProperties.getSapMailFrom() + "  " + ErpServicesProperties.getSapMailTo() + "  " + ErpServicesProperties.getSapMailCC());
			mailer.sendMail(ErpServicesProperties.getSapMailFrom(), ErpServicesProperties.getSapMailTo(), ErpServicesProperties.getSapMailCC(), "Invoice Report for " + new Date(), buff.toString());
			
		} catch (Exception e) {
			LOGGER.warn("Exception during missing invoice report", e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
			}
		}
		LOGGER.warn("Missing Invoice Report Complete");
	}

	public void createMissingInvoiceReport() {

	}

	static public Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

}
