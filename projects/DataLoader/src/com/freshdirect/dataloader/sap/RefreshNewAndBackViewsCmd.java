package com.freshdirect.dataloader.sap;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.framework.util.log.LoggerFactory;


@SuppressWarnings("javadoc")
public class RefreshNewAndBackViewsCmd
{
	private static final Logger LOGGER = LoggerFactory.getInstance(RefreshNewAndBackViewsCmd.class);

	public static void main(String[] args)
	{
		Context ctx = null;
		try
		{
			ctx = ErpServicesProperties.getInitialContext();

			ErpInfoHome infoHome = (ErpInfoHome) ctx.lookup("freshdirect.erp.Info");

			ErpInfoSB infoSB = infoHome.create();

			try
			{
				LOGGER.info("----- refreshing new and back-in-stock products materialized views -----");
				
				infoSB.refreshNewAndBackViews();
				
				LOGGER.info("----- refreshing new and back-in-stock products materialized views completed successfully -----");
			}
			catch (RemoteException e)
			{
				LOGGER.error("Unable to refresh new and back-in-stock products materialized views", e);
			}			
		}
		catch (NamingException e)
		{
			LOGGER.warn("Error opening naming context", e);
		}
		catch (RemoteException e1)
		{
			LOGGER.error("Unexpected system level exception while trying to create an ErpInfo", e1);
		}
		catch (CreateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				if (ctx != null)
					ctx.close();
			}
			catch (NamingException e)
			{
				LOGGER.warn("Error closing naming context", e);
			}
		}
	}
}
