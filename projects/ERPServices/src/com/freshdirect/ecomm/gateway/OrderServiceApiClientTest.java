/*
 * @author tbalumuri
 */
package com.freshdirect.ecomm.gateway;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;

public class OrderServiceApiClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrderHistory() throws FDResourceException, RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		ErpOrderHistory o = new ErpOrderHistory(service.getOrderHistory("2202100934"));
		System.out.println(o.getFirstOrderDate());
	
	}

	/**
	 * Test get last order id string.
	 *
	 * @throws RemoteException the remote exception
	 */
	@Test
	public void testGetLastOrderIdString() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		String o = service.getLastOrderId("2202100934");
		assertTrue(StringUtils.isNotBlank(o));
	
	}

	@Test
	public void testGetLastOrderIdStringEnumEStoreId() throws RemoteException {
		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		String o = service.getLastOrderId("2202100934", EnumEStoreId.FD);
		assertTrue(StringUtils.isNotBlank(o));
	}

	@Test
	public void testGetDlvSaleInfo() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		DlvSaleInfo o = service.getDlvSaleInfo("9476749307");
		assertTrue(o.getSaleId().equalsIgnoreCase("9476749307"));
	
	}

	@Test
	public void testGetOrdersByDlvPassId() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		List<ErpSaleInfo> o = service.getOrdersByDlvPassId("2202100934", "10002299394");
		assertTrue(o.size()>=0);
	
	}

	@Test
	public void testGetRecentOrdersByDlvPassId() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		List<DlvPassUsageLine> o = service.getRecentOrdersByDlvPassId("2202100934", "10002299394", 100);
		assertTrue(o.size()>=0);
	
	}

	@Test
	public void testGetDlvPassesUsageInfo() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		Map<String, DlvPassUsageInfo> o = service.getDlvPassesUsageInfo("2202100934");
		assertTrue(o.size()>=0);
	
	}

	@Test
	public void testGetValidOrderCount() throws RemoteException {


		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		int o = service.getValidOrderCount("2202100934");
		assertTrue(o>0);
	
	
	}

	@Test
	public void testIsOrderBelongsToUser() throws RemoteException {

		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		boolean o = service.isOrderBelongsToUser("2202100934", "10088133295");
		assertTrue(o);
	
	
	
	}

	@Test
	public void testGetWebOrderHistory() throws FDResourceException, RemoteException {


		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
		List<ErpSaleInfo> o = service.getWebOrderHistory("2202100934");
		assertTrue(o.size()>0);
	
	
	
	
	}

}
