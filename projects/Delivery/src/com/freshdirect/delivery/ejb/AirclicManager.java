package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextTelAsset;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.SignatureVO;
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
		
		public SignatureVO getSignatureDetails(String order)
		{
			SignatureVO signatureVO = null;
			try
			{
			AirclicManagerSB sb = getAirclicManagerHome().create();
			signatureVO = sb.getSignatureDetails(order);
			}
		
			catch (CreateException e) {
				e.printStackTrace();
				} catch (RemoteException e) {
				e.printStackTrace();
			} 
			return signatureVO;

		}
		
		public byte[] getSignature(String order)
		{
			byte[] _image = null;
			try
			{
			AirclicManagerSB sb = getAirclicManagerHome().create();
			_image = sb.getSignature(order);
			}
		
			catch (CreateException e) {
				e.printStackTrace();
				} catch (RemoteException e) {
				e.printStackTrace();
			} 
			return _image;

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
		
		public Map<String, DispatchNextTelVO> getDispatchResourceNextTel(Date dispatchDate) throws FDResourceException {
			
			Map<String, DispatchNextTelVO> result = new HashMap<String, DispatchNextTelVO>();
			try {
				AirclicManagerSB sb = getAirclicManagerHome().create();
				result = sb.getDispatchResourceNextTel(dispatchDate); 
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			} catch (DlvResourceException e) {				
				e.printStackTrace();
			}
			return result;
		}
		
		public Map<String, AirclicNextelVO> getNXOutScan(Date scanDate) throws FDResourceException {
			
			Map<String, AirclicNextelVO> result = new HashMap<String, AirclicNextelVO>();
			try {
				AirclicManagerSB sb = getAirclicManagerHome().create();
				result = sb.getNXOutScan(scanDate); 
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			} catch (DlvResourceException e) {				
				e.printStackTrace();
			}
			return result;
		}
		
		public void updateEmployeeNexTelData(List<DispatchNextTelVO> employeeNextTels) throws FDResourceException {
			try {
				AirclicManagerSB sb = getAirclicManagerHome().create();
				sb.updateEmployeeNexTelData(employeeNextTels); 
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			} catch (DlvResourceException e) {				
				e.printStackTrace();
			}
		}
		
		public Map<String, String> getNextTelAssets() throws FDResourceException {
			
			Map<String, String> result = new HashMap<String, String>();
			try {
				AirclicManagerSB sb = getAirclicManagerHome().create();
				result = sb.getNextTelAssets(); 
			} catch (CreateException e) {
				throw new FDResourceException(e, "Cannot create SessionBean");
			} catch (RemoteException e) {
				throw new FDResourceException(e, "Cannot talk to the SessionBean");
			} catch (DlvResourceException e) {				
				e.printStackTrace();
			}
			return result;
		}
	}
