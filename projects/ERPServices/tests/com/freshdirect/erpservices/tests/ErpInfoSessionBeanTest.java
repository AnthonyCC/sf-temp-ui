package com.freshdirect.erpservices.tests;

import java.util.Arrays;

import javax.ejb.ObjectNotFoundException;

import java.rmi.RemoteException;
import java.util.*;

import org.junit.Test;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;

import junit.framework.TestCase;

public class ErpInfoSessionBeanTest extends TestCase {
	private ErpInfoSB sb;
	private String[] sampleSkuCodes = new String[] { "CAT3720336", "CAT3720337", "CHE2400107", "DAI2002513",
			"DAI2002587", "DAI2003521", "DAI2003527", "DAI2003535", "DAI2004024", "DAI2004025", "DAI2004026",
			"DAI2004027", "DAI2004028", "DAI2004029", "DAI2004030", "DAI2004031", "DAI2004032", "DAI2004033",
			"DAI2004034", "DAI2004035", "DAI2004036", "DAI2004037", "DAI2004038", "DAI2004039", "DAI2004040",
			"DAI2004041", "DAI2004042", "DAI2004043", "DAI2004044", "DAI2004045", "DAI2004046", "DAI2004047",
			"DAI2004048", "DAI2004049", "DAI2004050", "DAI2004051", "DAI2004052", "DAI2004053", "DAI2004054",
			"DAI2004055", "DAI2004056", "DAI2004057", "DAI2004058", "DAI2004059", "DAI2004060", "DAI2004061",
			"DAI2004062", "DAI2004063", "DAI2004064", "DAI2004065", "DAI2004066", "DAI2004067", "DAI2004068",
			"DAI2004069", "DAI2004070", "DAI2004071", "DAI2004072", "DAI2004073", "DAI2004074", "DAI2004075",
			"DAI2004076", "DAI2004077", "DAI2004078", "DAI2004079", "DAI2004080", "DAI2004081", "DAI2004082",
			"DAI2004083", "DAI2004084", "DAI2004085", "DAI2004086", "DAI2004087", "DAI2004088", "DAI2004089",
			"DAI2004090", "DAI2004091", "DAI2004092", "DAI2004093", "DAI2004094", "DAI2004095", "DAI2004096",
			"DAI2004097", "DAI2004098", "DAI2004099", "DAI2004100", "DAI2004101", "DAI2004102", "DAI2004103",
			"DAI2004104", "DAI2004105", "DAI2004106", "DAI2004107", "DAI2004108", "DAI2004109", "DAI2004110",
			"DAI2004111", "DAI2004112", "DAI2004113", "DAI2004114", "DAI2004115" };

	public ErpInfoSessionBeanTest() {
		sb = ERPServiceLocator.getInstance().getErpInfoSessionBean();
	}

	@Test
	public void testFindProductsBySku_One() {
		try {
			Collection<ErpProductInfoModel> products = sb.findProductsBySku(new String[] { "DAI0076247" });

			assertEquals(1, products.size());

			testProductInfo(products);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindProductsBySku_Two() {
		try {
			Collection<ErpProductInfoModel> products = sb
					.findProductsBySku(new String[] { "DAI0076247", "HMR3750603" });

			assertEquals(2, products.size());

			testProductInfo(products);
		} catch (Exception e) {
			fail();
		}

	}

	@Test
	public void testFindProductsBySku_Ten() {
		try {
			Collection<ErpProductInfoModel> products = sb
					.findProductsBySku(new String[] { "DAI2004041", "DAI2004042", "DAI2004043", "DAI2004044",
							"DAI2004045", "DAI2004046", "DAI2004047", "DAI2004048", "DAI2004049", "DAI2004050" });
			assertEquals(10, products.size());

			testProductInfo(products);
		} catch (Exception e) {
			fail();
		}

	}

	@Test
	public void testFindProductsBySku_InConditionLimit_Even_Size() {
		try {
			Collection<ErpProductInfoModel> products = sb.findProductsBySku(sampleSkuCodes);

			// if size is not the same, make sure the product info is really not
			// available
			if (sampleSkuCodes.length != products.size()) {
				List<String> nonExistSkuCodes = new ArrayList<String>(Arrays.asList(sampleSkuCodes));
				for (ErpProductInfoModel product : products) {
					nonExistSkuCodes.remove(nonExistSkuCodes.indexOf(product.getSkuCode()));
				}
				for (String nonExistSkuCode : nonExistSkuCodes) {
					try {
						sb.findProductBySku(nonExistSkuCode);
						fail("expect exception");
					} catch (ObjectNotFoundException e) {

					}
				}
			}

			testProductInfo(products);
		} catch (Exception e) {
			fail();
		}

	}

	@Test
	public void testFindProductsBySku_InConditionLimit_Odd_Size() {
		try {
			List<String> temp = new ArrayList<String>(Arrays.asList(sampleSkuCodes));
			temp.add("HMR3750603");
			String[] sampleSkuCodes = temp.toArray(new String[0]);
			Collection<ErpProductInfoModel> products = sb.findProductsBySku(sampleSkuCodes);
			// if size is not the same, make sure the product info is really not
			// available
			if (sampleSkuCodes.length != products.size()) {
				List<String> nonExistSkuCodes = new ArrayList<String>(Arrays.asList(sampleSkuCodes));
				for (ErpProductInfoModel product : products) {
					nonExistSkuCodes.remove(nonExistSkuCodes.indexOf(product.getSkuCode()));
				}
				for (String nonExistSkuCode : nonExistSkuCodes) {
					try {
						sb.findProductBySku(nonExistSkuCode);
						fail("expect exception");
					} catch (ObjectNotFoundException e) {

					}
				}
			}

			testProductInfo(products);
		} catch (Exception e) {
			fail();
		}

	}

	@Test
	public void testGetModifiedSkus_CurrentTime() {
		try {
			Set<String> skus = sb.getModifiedSkus(System.currentTimeMillis());
			assertEquals(0, skus.size());
		} catch (RemoteException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testGetModifiedSkus_Past() {
		try {
			Set<String> skusT = sb.getModifiedSkus(1164949200000L);
			System.out.println("skusT size is " + skusT.size());
			Set<String> skusD = sb.getModifiedSkus(1515611832396L);
			assertEquals(49494, skusT.size() - skusD.size());
		} catch (RemoteException e) {
			fail(e.getMessage());
		}

	}

	private void testProductInfo(Collection<ErpProductInfoModel> products)
			throws RemoteException, ObjectNotFoundException {
		for (ErpProductInfoModel product : products) {
			testProductInfo(product);
		}
	}

	private void testProductInfo(ErpProductInfoModel product) throws RemoteException, ObjectNotFoundException {
		ErpProductInfoModel expectedProduct = sb.findProductBySku(product.getSkuCode());
		assertTrue(Arrays.equals(expectedProduct.getMaterialPlants(), product.getMaterialPlants()));
		List<ErpMaterialSalesAreaInfo> expectedSaleAreaInfos = Arrays.asList(expectedProduct.getSalesAreas());
		for (ErpMaterialSalesAreaInfo sai : product.getSalesAreas()) {
			expectedSaleAreaInfos.contains(sai);
		}
		assertEquals(expectedProduct.getDescription(), product.getDescription());
		assertEquals(expectedProduct.getUpc(), product.getUpc());
		assertEquals(expectedProduct.getVersion(), product.getVersion());
		assertEquals(expectedProduct.getAlcoholicType(), product.getAlcoholicType());
		assertEquals(expectedProduct.getSalesAreas().length, product.getSalesAreas().length);
		Set<String> salesAreaMap = new HashSet<String>();
		for (int i = 0; i < expectedProduct.getSalesAreas().length; i++) {
			salesAreaMap.add(expectedProduct.getSalesAreas()[i].getSalesAreaInfo().toString());
		}
		for (int i = 0; i < product.getSalesAreas().length; i++) {
			salesAreaMap.remove(product.getSalesAreas()[i].getSalesAreaInfo().toString());
		}
		assertEquals(0, salesAreaMap.size());
		assertEquals(expectedProduct.getSalesAreas().length, product.getSalesAreas().length);

		assertTrue(Arrays.equals(expectedProduct.getMaterialPrices(), product.getMaterialPrices()));
		assertTrue(Arrays.equals(expectedProduct.getMaterialSapIds(), product.getMaterialSapIds()));
	}
}