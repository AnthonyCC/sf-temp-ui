package com.freshdirect.fdstore.promotion.management.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;


import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewDAO;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;

public class FDPromotionManagerDAOTestCase extends DbTestCaseSupport {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public FDPromotionManagerDAOTestCase(String name) {
		super(name);
	}

	@Override
	protected String getSchema() {
		return "CUST";
	}
	
	@Override
	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CAMPAIGN",				
			"CUST.CUSTOMER",
			"CUST.PROMOTION_NEW",
			"CUST.PROMO_CUSTOMER"
			};
	}
	public void testGetPromotions()  throws Exception {
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");		
		List promoList = FDPromotionManagerNewDAO.getPromotions(conn);
		assertEquals(3, promoList.size());
	}
	
	public void testCreatePromotion() throws Exception {
		
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");

		FDPromotionNewModel p1 = new FDPromotionNewModel();
		
		p1.setTmpAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		Date expDate = new Date();
		p1.setExpirationDate(DateUtil.truncate(expDate));
		p1.setId("");
		p1.setMaxAmount("25");
		p1.setPercentOff("");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test namec");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test codec");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ3");
//		p1.setRefProgCampaignCode("REF_PROG_1");
//		p1.setIsMaxUsagePerCustomer(true);
		p1.setRollingExpirationDays(new Integer(3));
		Date startDate = new Date();
		p1.setStartDate(DateUtil.truncate(startDate));
//		p1.setUniqueUse(true);
		
		// execute
		PrimaryKey pk = FDPromotionManagerNewDAO.createPromotion(conn, p1);

		p1.setId(pk.getId());
		
		// verify
		assertNotNull(pk);
		assertTrue("Primary key is blank", !"".equals(pk.getId()));
		
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
		
//		assertEquals(p1.getTmpAssignedCustomerUserIds(), p2.getAssignedCustomerUserIds());
		assertEquals(p1.getCategoryName(), p2.getCategoryName());
		assertEquals(p1.getDescription(), p2.getDescription());
//		assertEquals(p1.getExcludeBrands(), p2.getExcludeBrands());
//		assertEquals(p1.getExcludeSkuPrefix(), p2.getExcludeSkuPrefix());
		assertEquals(p1.getExpirationDate(), DateUtil.truncate(p2.getExpirationDate()));
		assertEquals(p1.getId(), p2.getId());
		assertEquals(Double.valueOf(p1.getMaxAmount()), Double.valueOf(p2.getMaxAmount()));
//		assertEquals(p1.isOrderTypeCorporateAllowed(), p2.isOrderTypeCorporateAllowed());
//		assertEquals(p1.isOrderTypeDepotAllowed(), p2.isOrderTypeDepotAllowed());
//		assertEquals(p1.isOrderTypeHomeAllowed(), p2.isOrderTypeHomeAllowed());
//		assertEquals(p1.isOrderTypePickupAllowed(), p2.isOrderTypePickupAllowed());
		assertEquals(Integer.valueOf(p1.getMaxUsage()), Integer.valueOf(p2.getMaxUsage()));
		assertEquals(Double.valueOf(p1.getMinSubtotal()), Double.valueOf(p2.getMinSubtotal()));
		assertEquals(p1.getName(), p2.getName());
//		assertEquals(p1.getNeedBrands(), p2.getNeedBrands());
//		assertEquals(p1.getNeedItemsFrom(), p2.getNeedItemsFrom());
//		assertEquals(p1.getNeedDryGoods(), p2.getNeedDryGoods());
//		assertEquals(Integer.valueOf(p1.getOrderCount()), Integer.valueOf(p2.getOrderCount()));
		assertEquals(p1.getPromotionCode(), p2.getPromotionCode());
		assertEquals(p1.getPromotionType(), p2.getPromotionType());
		assertEquals(p1.getRedemptionCode(), p2.getRedemptionCode());
		assertEquals(p1.getStartDate(), DateUtil.truncate(p2.getStartDate()));
//		assertEquals(p1.getRefProgCampaignCode(), p2.getRefProgCampaignCode());
//		assertEquals(p1.isMaxUsagePerCustomer(),p2.isMaxUsagePerCustomer()); 
		assertEquals(p1.getRollingExpirationDays().intValue(),p2.getRollingExpirationDays().intValue()); 
//		assertEquals(p1.isUniqueUse(), p2.isUniqueUse());
		
	}

	public void testGetPromotion() throws Exception {
		
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
		FDPromotionNewModel p1 = new FDPromotionNewModel();
		
		p1.setAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		p1.setExpirationDate(DF.parse("2010-01-24"));
		p1.setId("p1");
		p1.setMaxAmount("25");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test name1");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test code1");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ");
		p1.setStartDate(DF.parse("2010-01-01"));
//		p1.setRefProgCampaignCode("REF_PROG_1");
		p1.setRollingExpirationDays(new Integer(4));
//		p1.setIsMaxUsagePerCustomer(true);
		Map assignedCustomerParams = new HashMap();
		AssignedCustomerParam param = new AssignedCustomerParam(new Integer(2), DF.parse("2005-08-01")); 
		assignedCustomerParams.put("CU1", param);
		p1.setAssignedCustomerParams(assignedCustomerParams);
		
		// execute
		//PrimaryKey pk = new PrimaryKey("p1");
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
		
		// verify
		assertEquals(p1.getAssignedCustomerUserIds(), p2.getAssignedCustomerUserIds());
		assertEquals(p1.getCategoryName(), p2.getCategoryName());
		assertEquals(p1.getDescription(), p2.getDescription());
//		assertEquals(p1.getExcludeBrands(), p2.getExcludeBrands());
//		assertEquals(p1.getExcludeSkuPrefix(), p2.getExcludeSkuPrefix());
		assertEquals(DateUtil.truncate(p1.getExpirationDate()), DateUtil.truncate(p2.getExpirationDate()));
		assertEquals(p1.getId(), p2.getId());
		assertEquals(Double.valueOf(p1.getMaxAmount()), Double.valueOf(p2.getMaxAmount()));
//		assertEquals(p1.isOrderTypeCorporateAllowed(), p2.isOrderTypeCorporateAllowed());
//		assertEquals(p1.isOrderTypeDepotAllowed(), p2.isOrderTypeDepotAllowed());
//		assertEquals(p1.isOrderTypeHomeAllowed(), p2.isOrderTypeHomeAllowed());
//		assertEquals(p1.isOrderTypePickupAllowed(), p2.isOrderTypePickupAllowed());
		assertEquals(Integer.valueOf(p1.getMaxUsage()), Integer.valueOf(p2.getMaxUsage()));
		assertEquals(Double.valueOf(p1.getMinSubtotal()), Double.valueOf(p2.getMinSubtotal()));
		assertEquals(p1.getName(), p2.getName());
//		assertEquals(p1.getNeedBrands(), p2.getNeedBrands());
//		assertEquals(p1.getNeedItemsFrom(), p2.getNeedItemsFrom());
//		assertEquals(p1.getNeedDryGoods(), p2.getNeedDryGoods());
//		assertEquals(Integer.valueOf(p1.getOrderCount()), Integer.valueOf(p2.getOrderCount()));
		assertEquals(p1.getPromotionCode(), p2.getPromotionCode());
		assertEquals(p1.getPromotionType(), p2.getPromotionType());
		assertEquals(p1.getRedemptionCode(), p2.getRedemptionCode());
		assertEquals(DateUtil.truncate(p1.getStartDate()), DateUtil.truncate(p2.getStartDate()));
//		assertEquals(p1.getRefProgCampaignCode(), p2.getRefProgCampaignCode());
//		assertEquals(p1.isMaxUsagePerCustomer(),p2.isMaxUsagePerCustomer()); 
		assertEquals(p1.getRollingExpirationDays().intValue(),p2.getRollingExpirationDays().intValue());
		/*for (Iterator iter = p1.getAssignedCustomerParams().keySet().iterator(); iter.hasNext();) {
			String customerId = (String)iter.next();
			AssignedCustomerParam param1 = (AssignedCustomerParam)p1.getAssignedCustomerParams().get(customerId);
			AssignedCustomerParam param2 = (AssignedCustomerParam)p2.getAssignedCustomerParams().get(customerId);
			assertEquals(param1.getUsageCount(), param2.getUsageCount());
			assertEquals(param1.getExpirationDate(), param2.getExpirationDate());
		}*/		
	}

	public void testStorePromotion() throws Exception {
		
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
		FDPromotionNewModel p1 = new FDPromotionNewModel(new PrimaryKey("p2"));
		
		p1.setAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		p1.setExpirationDate(DF.parse("2010-01-24"));
		p1.setId("p2");
		p1.setMaxAmount("25");
		p1.setPercentOff("");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test name2");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test code2");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ4");
		p1.setStartDate(DF.parse("2010-01-01"));
//		p1.setRefProgCampaignCode("REF_PROG_5");
		p1.setRollingExpirationDays(new Integer(5));
//		p1.setIsMaxUsagePerCustomer(true);
//		p1.setUniqueUse(true);
		
		// execute
		FDPromotionManagerNewDAO.storePromotion(conn, p1);

		// verify
		//PrimaryKey pk = new PrimaryKey("p2");
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
		
		assertEquals(p1.getPK().getId(), p2.getPK().getId());
		assertEquals(p1.getAssignedCustomerUserIds(), p2.getAssignedCustomerUserIds());
		assertEquals(p1.getCategoryName(), p2.getCategoryName());
		assertEquals(p1.getDescription(), p2.getDescription());
//		assertEquals(p1.getExcludeBrands(), p2.getExcludeBrands());
//		assertEquals(p1.getExcludeSkuPrefix(), p2.getExcludeSkuPrefix());
		assertEquals(DateUtil.truncate(p1.getExpirationDate()), DateUtil.truncate(p2.getExpirationDate()));
		assertEquals(p1.getId(), p2.getId());
		assertEquals(Double.valueOf(p1.getMaxAmount()), Double.valueOf(p2.getMaxAmount()));
//		assertEquals(p1.isOrderTypeCorporateAllowed(), p2.isOrderTypeCorporateAllowed());
//		assertEquals(p1.isOrderTypeDepotAllowed(), p2.isOrderTypeDepotAllowed());
//		assertEquals(p1.isOrderTypeHomeAllowed(), p2.isOrderTypeHomeAllowed());
//		assertEquals(p1.isOrderTypePickupAllowed(), p2.isOrderTypePickupAllowed());
		assertEquals(Integer.valueOf(p1.getMaxUsage()), Integer.valueOf(p2.getMaxUsage()));
		assertEquals(Double.valueOf(p1.getMinSubtotal()), Double.valueOf(p2.getMinSubtotal()));
		assertEquals(p1.getName(), p2.getName());
//		assertEquals(p1.getNeedBrands(), p2.getNeedBrands());
//		assertEquals(p1.getNeedItemsFrom(), p2.getNeedItemsFrom());
//		assertEquals(p1.getNeedDryGoods(), p2.getNeedDryGoods());
//		assertEquals(Integer.valueOf(p1.getOrderCount()), Integer.valueOf(p2.getOrderCount()));
		assertEquals(p1.getPromotionCode(), p2.getPromotionCode());
		assertEquals(p1.getPromotionType(), p2.getPromotionType());
		assertEquals(p1.getRedemptionCode(), p2.getRedemptionCode());
//		assertEquals(p1.getRefProgCampaignCode(), p2.getRefProgCampaignCode());
//		assertEquals(p1.isMaxUsagePerCustomer(),p2.isMaxUsagePerCustomer()); 
//		assertEquals(p1.isUniqueUse(),p2.isUniqueUse()); 
		assertEquals(p1.getRollingExpirationDays().intValue(),p2.getRollingExpirationDays().intValue()); 
		assertEquals(DateUtil.truncate(p1.getStartDate()), DateUtil.truncate(p2.getStartDate()));

		/*Map assignedCustomerParams = new HashMap();
		AssignedCustomerParam param = new AssignedCustomerParam(new Integer(4), DF.parse("2005-07-01")); 
		assignedCustomerParams.put("CU1", param);
		p1.setAssignedCustomerParams(assignedCustomerParams);
		for (Iterator iter = p1.getAssignedCustomerParams().keySet().iterator(); iter.hasNext();) {
			String customerId = (String)iter.next();
			AssignedCustomerParam param1 = (AssignedCustomerParam)p1.getAssignedCustomerParams().get(customerId);
			AssignedCustomerParam param2 = (AssignedCustomerParam)p2.getAssignedCustomerParams().get(customerId);
			assertEquals(param1.getUsageCount(), param2.getUsageCount());
			assertEquals(param1.getExpirationDate(), param2.getExpirationDate());
		}*/		
		
	}

	/*public void testRemovePromotion() throws Exception {
		
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
	
		// execute
		PrimaryKey pk = new PrimaryKey("p3");
		FDPromotionManagerNewDAO.removePromotion(conn, pk);

		// verify
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, "P_THREE");
				
		assertNull("Promotion not removed", p2);
		
	}*/
	
}
