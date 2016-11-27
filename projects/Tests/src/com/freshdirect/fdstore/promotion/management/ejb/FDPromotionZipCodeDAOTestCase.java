package com.freshdirect.fdstore.promotion.management.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;


import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewDAO;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;

/*
 * This class Tests 
 *   1.  Insertion of promotion, zipcode Restrictions
 *   2.  Inserting/Adding/Modifying ZipRestrictions
 *   3.  Checks if the modified Date and Modified By fields change 
 *   4.  Checks adding text to notes field in Promotion Table
 */
public class FDPromotionZipCodeDAOTestCase extends DbTestCaseSupport {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	public FDPromotionZipCodeDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CUSTOMER",
			"CUST.CAMPAIGN",
			
					
			"CUST.PROMOTION_NEW",
			"CUST.PROMO_CUSTOMER",
			"CUST.PROMO_GEOGRAPHY_NEW",
			"CUST.PROMO_GEOGRAPHY_DATA_NEW"
				
			};
	}
	
	public void testInsertPromotion() throws Exception {
		//setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
		FDPromotionNewModel p1 = new FDPromotionNewModel();
//		p1.setTmpAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		Date expDate = new Date();
		p1.setExpirationDate(DateUtil.truncate(expDate));
		p1.setMaxAmount("25");
		p1.setPercentOff("");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test zipcode values1");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test zipcode1");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ3");
//		p1.setRefProgCampaignCode("REF_PROG_1");
//		p1.setIsMaxUsagePerCustomer(true);
		p1.setRollingExpirationDays(new Integer(3));
		Date startDate = new Date();
		p1.setStartDate(DateUtil.truncate(startDate));
//		p1.setUniqueUse(true);
//		p1.setActive(true);
//		p1.setNotes("Some useful test note");
		p1.setModifiedBy("yuva");
		// execute

		PrimaryKey pk = FDPromotionManagerNewDAO.createPromotion(conn, p1);
		
		p1.setId(pk.getId());
		
		// verify
		assertNotNull(pk);
		assertTrue("Primary key is blank", !"".equals(pk.getId()));
		
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
		//assertTrue("No Zip Code Restrictions", !p2.getZipRestrictions().isEmpty());

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
//		assertEquals(p1.getNotes(), p2.getNotes());
		
	}
	
	public void testInsertZipCodeRestriction() throws Exception {
//		 setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
		FDPromotionNewModel p1 = new FDPromotionNewModel();
//		p1.setTmpAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		Date expDate = new Date();
		p1.setExpirationDate(DateUtil.truncate(expDate));
		p1.setMaxAmount("25");
		p1.setPercentOff("");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test zipcode values1");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test zipcode1");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ4");
//		p1.setRefProgCampaignCode("REF_PROG_1");
//		p1.setIsMaxUsagePerCustomer(true);
		p1.setRollingExpirationDays(new Integer(3));
		Date startDate = new Date();
		p1.setStartDate(DateUtil.truncate(startDate));
//		p1.setUniqueUse(true);
//		p1.setActive(true);
//		p1.setNotes("Some useful test note");
		p1.setModifiedBy("yuva");
		// execute

		PrimaryKey pk = FDPromotionManagerNewDAO.createPromotion(conn, p1);
		
		p1.setId(pk.getId());
		p1.setPK(pk);
		p1.setModifiedBy("dkee");
		
		FDPromoZipRestriction zp = new FDPromoZipRestriction();
		zp.setType("EXCEPT");
		zp.setZipCodes("11023,11024");
		zp.setStartDate(DF.parse("2006-04-27"));
		
		FDPromoZipRestriction zp1 = new FDPromoZipRestriction();
		zp1.setType("ONLY");
		zp1.setZipCodes("ALL");
		zp1.setStartDate(DF.parse("2006-05-15"));
		
		TreeMap zipRestrictions = new TreeMap();
		zipRestrictions.put(DF.parse("2006-04-27"), zp);
		zipRestrictions.put(DF.parse("2006-05-15"), zp1);
		p1.setZipRestrictions(zipRestrictions);
		
		FDPromotionManagerNewDAO.storePromotion(conn, p1);
		
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
//		assertEquals(p1.getTmpAssignedCustomerUserIds(), p2.getAssignedCustomerUserIds());
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
//		assertEquals(p1.isUniqueUse(), p2.isUniqueUse());
//		assertEquals(p1.getNotes(), p2.getNotes());
		assertNotSame(p1.getModifiedBy(), p2.getModifiedBy());
		TreeMap p1ZipRestrictions = p1.getZipRestrictions();
		TreeMap p2ZipRestrictions = p2.getZipRestrictions();
		for(Iterator i1 = p1ZipRestrictions.keySet().iterator(); i1.hasNext(); ){
			Date key = (Date)i1.next();
			FDPromoZipRestriction zr1= (FDPromoZipRestriction)p1ZipRestrictions.get(key);
			FDPromoZipRestriction zr2 = (FDPromoZipRestriction)p2ZipRestrictions.get(key);
			assertEquals(zr1.getType(), zr2.getType());
			assertEquals(zr1.getZipCodes(), zr2.getZipCodes());
			assertEquals(zr1.getStartDate(), zr2.getStartDate());
		}		
		
	}
	
	public void testModifyZipCodeRestriction() throws Exception {
		// setup
		this.setUpDataSet("FDPromotionManagerDAO-init.xml");
		
		FDPromotionNewModel p1 = new FDPromotionNewModel();
//		p1.setTmpAssignedCustomerUserIds("test@test.com");
		p1.setCategoryName("abc");
		p1.setDescription("Promotion test description");
//		p1.setExcludeBrands("1000,1001,1002");
//		p1.setExcludeSkuPrefix("80*");
		Date expDate = new Date();
		p1.setExpirationDate(DateUtil.truncate(expDate));
		p1.setMaxAmount("25");
		p1.setPercentOff("");
//		p1.setIsOrderTypeCorporateAllowed(true);
//		p1.setIsOrderTypeDepotAllowed(true);
//		p1.setIsOrderTypeHomeAllowed(true);
//		p1.setIsOrderTypePickupAllowed(true);
		p1.setMaxUsage("1");
		p1.setMinSubtotal("40");
		p1.setName("Test zipcode values1");
//		p1.setNeedBrands("XXX,YYY");
//		p1.setNeedItemsFrom("2000,2001");
//		p1.setNeedsDryGoods(true);
//		p1.setOrderCount("3");
		p1.setPromotionCode("Test zipcode1");
		p1.setPromotionType("REDEMPTION");
		p1.setRedemptionCode("XYZ5");
//		p1.setRefProgCampaignCode("REF_PROG_1");
//		p1.setIsMaxUsagePerCustomer(true);
		p1.setRollingExpirationDays(new Integer(3));
		Date startDate = new Date();
		p1.setStartDate(DateUtil.truncate(startDate));
//		p1.setUniqueUse(true);
//		p1.setActive(true);
//		p1.setNotes("Some useful test note");
		p1.setModifiedBy("yuva");
		// execute
		PrimaryKey pk = FDPromotionManagerNewDAO.createPromotion(conn, p1);
		
		p1.setId(pk.getId());

		p1.setPK(pk);
		
		FDPromoZipRestriction zp = new FDPromoZipRestriction();
		zp.setType("EXCEPT");
		zp.setZipCodes("11023,11024");
		zp.setStartDate(DF.parse("2006-04-27"));
		
		FDPromoZipRestriction zp1 = new FDPromoZipRestriction();
		zp1.setType("ONLY");
		zp1.setZipCodes("ALL");
		zp1.setStartDate(DF.parse("2006-05-15"));
		
		TreeMap zipRestrictions = new TreeMap();
		zipRestrictions.put(DF.parse("2006-04-27"), zp);
		zipRestrictions.put(DF.parse("2006-05-15"), zp1);
		p1.setZipRestrictions(zipRestrictions);
		
		FDPromotionManagerNewDAO.storePromotion(conn, p1);
		
		TreeMap preZrMap = (TreeMap) p1.getZipRestrictions();
		
		FDPromoZipRestriction preZr1 = (FDPromoZipRestriction)preZrMap.get(DF.parse("2006-04-27"));
		FDPromoZipRestriction preZr2 = (FDPromoZipRestriction)preZrMap.get(DF.parse("2006-05-15"));
		
		preZrMap.remove(DF.parse("2006-05-15"));
		
		preZr2.setType("EXCEPT");
		preZr2.setZipCodes("ALL");
		preZr2.setStartDate(DF.parse("2006-05-30"));
		
		preZrMap.put(DF.parse("2006-05-30"), preZr2);
		p1.setZipRestrictions(preZrMap);
		FDPromotionManagerNewDAO.storePromotion(conn, p1);
		
		FDPromotionNewModel p2 = FDPromotionManagerNewDAO.getPromotion(conn, p1.getPromotionCode());
//		assertEquals(p1.getTmpAssignedCustomerUserIds(), p2.getAssignedCustomerUserIds());
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
//		assertEquals(p1.isUniqueUse(), p2.isUniqueUse());
//		assertEquals(p1.getNotes(), p2.getNotes());
		assertNotSame(p1.getModifiedBy(), p2.getModifiedBy());
		TreeMap p2ZipRestrictions = p2.getZipRestrictions();
		FDPromoZipRestriction postZr1 = (FDPromoZipRestriction) p2ZipRestrictions.get(DF.parse("2006-04-27"));
		FDPromoZipRestriction postZr2 = (FDPromoZipRestriction) p2ZipRestrictions.get(DF.parse("2006-05-30"));
		
		FDPromoZipRestriction checkOldZr = (FDPromoZipRestriction) p2ZipRestrictions.get(DF.parse("2006-05-15"));
		assertTrue("Old Zip Restriction still exists in database even after removing", checkOldZr==null);
		
		assertEquals(preZr1.getType(), postZr1.getType());
		assertEquals(preZr1.getZipCodes(), postZr1.getZipCodes());
		assertEquals(preZr1.getStartDate(), preZr1.getStartDate());
		assertEquals(postZr2.getType(), preZr2.getType());
		assertEquals(postZr2.getZipCodes(), preZr2.getZipCodes());
		assertEquals(postZr2.getStartDate(), preZr2.getStartDate());	
		
	}
	
}
