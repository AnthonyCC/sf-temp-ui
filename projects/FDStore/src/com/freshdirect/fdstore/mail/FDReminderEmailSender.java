package com.freshdirect.fdstore.mail;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDReminderEmailSender {

	private static Category LOGGER = LoggerFactory.getInstance(FDReminderEmailSender.class);

	public static void main(String[] args) {
		try {
			List custIds = FDCustomerManager.getReminderListForToday();
			for (Iterator i = custIds.iterator(); i.hasNext();) {
				String id = (String) i.next();
				FDCustomerManager.sendReminderEmail(id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Problems sending Reminder Emails", e);
		}
	}
}
