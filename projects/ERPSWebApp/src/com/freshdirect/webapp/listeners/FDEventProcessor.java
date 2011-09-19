package com.freshdirect.webapp.listeners;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.analytics.CEPService;
import com.freshdirect.analytics.TimeslotEventDAO;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDEventProcessor implements FDEventProcessorI {

	private static final Category LOGGER = LoggerFactory.getInstance(FDEventProcessor.class);

	public void process(FDUser user, HttpSession session) {
		Connection conn = null;

		try
		{
			if(user!=null && user.getIdentity()!=null && user.getIdentity().getErpCustomerPK()!=null)
			{
				String customerId = user.getIdentity().getErpCustomerPK();
				List timeslots = TimeslotEventDAO.getEvents(conn, customerId, session.getCreationTime());
				CEPService.insert(timeslots);
			}
		}
		catch(Exception e)
		{
			LOGGER.info(e.getMessage());
		}
	}


}
