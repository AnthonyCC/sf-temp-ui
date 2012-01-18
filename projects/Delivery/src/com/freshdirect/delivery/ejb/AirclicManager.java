package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;

public class AirclicManager {
	private final ServiceLocator serviceLocator;
	
	private static AirclicManager instance;
	private long REFRESH_PERIOD = 1000 * 60 * 10; // 10 minutes
	private long lastRefresh = 0;
	private List<AirclicMessageVO> messages;
	
		private AirclicManagerHome getAirclicManagerHome() {
			try {
				return (AirclicManagerHome) serviceLocator.getRemoteHome(FDStoreProperties.getAirclicManagerHome());
			} catch (NamingException ne) {
				throw new EJBException(ne);
			}
		}
		private AirclicManager() throws NamingException
		{
			this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
		}
		
		public byte[] getSignature(String order)
		{
			byte[] signBytes = null;
			try
			{
			AirclicManagerSB sb = getAirclicManagerHome().create();
			signBytes = sb.getSignature(order);
			}
		
			catch (CreateException e) {
				e.printStackTrace();
				} catch (RemoteException e) {
				e.printStackTrace();
			} 
			return signBytes;

		}

		public static AirclicManager getInstance()
		{
			if(instance==null)
			{
				try {
					instance=new AirclicManager();
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return instance;
		}
		
		public String sendMessage(String[] data)
		{
			try {
				DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
				Date deliveryDate = df.parse(data[0]);
				String result = "";
				int stop = 0;
				if(data[2]!=null) stop = Integer.parseInt(data[2]);
			AirclicTextMessageVO textMessage = new AirclicTextMessageVO(deliveryDate, data[1], stop ,
					data[3], data[4], data[5], data[6]);
			
			AirclicManagerSB sb = getAirclicManagerHome().create();

			return sb.saveMessage(textMessage);
			}
			catch (CreateException e) {
				e.printStackTrace();
				} catch (RemoteException e) {
				e.printStackTrace();
			} catch (DlvResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "Sorry, we're experiencing technical difficulties. Please try again later.";
		}
		public synchronized List<AirclicMessageVO> getMessages() throws FDResourceException {
			if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
				try {
					AirclicManagerSB sb = getAirclicManagerHome().create();

					messages = sb.getAirclicMessages();
					

					lastRefresh = System.currentTimeMillis();

				} catch (CreateException e) {
					throw new FDResourceException(e, "Cannot create SessionBean");
				} catch (RemoteException e) {
					throw new FDResourceException(e, "Cannot talk to the SessionBean");
				} catch (DlvResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return messages;
		}
	}
