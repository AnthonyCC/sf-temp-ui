package com.freshdirect.webapp.util.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.json.JSONObject;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoPaymentStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

public class FDPromotionJSONSerializerTest extends TestCase {
	private FDPromoContentModel ctnt;
	private FDPromoCustStrategyModel custStrategy;
	private FDPromoPaymentStrategyModel paymentStrategy;
	private FDPromotionNewModel promoNew;
	
	
	private JSONSerializer ser = new JSONSerializer();
	private FDPromotionJSONSerializer promoSer;

	@Override
	protected void setUp() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		final Date aDate = df.parse("1973.01.31");
		
		// FDPromoContentModel class 
		ctnt = new FDPromoContentModel();
		
		ctnt.setContentId("sample_id");
		ctnt.setContentType(EnumDCPDContentType.BRAND);
		ctnt.setPK(new PrimaryKey("sample_pk"));
		ctnt.setPromotionId("promo123");

		// FDPromoCustStrategyModel class
		custStrategy = new FDPromoCustStrategyModel(
				"PromoId1",
				1, 2,
				new String[]{"C1", "C2", "C3"},
				"dpStatus1",
				aDate, aDate, true, false, false,
				new EnumCardType[]{EnumCardType.AMEX, EnumCardType.GCP},
				"tejbetok");

		// FDPromoPaymentStrategyModel
		paymentStrategy = new FDPromoPaymentStrategyModel();
		paymentStrategy.setPromotionId("Promo123");

		paymentStrategy.setOrderTypeHome(true);
		paymentStrategy.setOrderTypePickup(false);
		paymentStrategy.setOrderTypePickup(false);
		
		paymentStrategy.setPaymentType(new EnumCardType[]{EnumCardType.AMEX, EnumCardType.GCP});
		paymentStrategy.setPriorEcheckUse("tejbetok");

		// FDPromotionNewModel class
		promoNew = new FDPromotionNewModel();
		
		Set<String> assignedCustomerUserIds = new HashSet<String>();
		assignedCustomerUserIds.add("user1");
		assignedCustomerUserIds.add("user2");
		assignedCustomerUserIds.add("user3");
		promoNew.setAssignedCustomerUserIds(assignedCustomerUserIds);
		
		TreeMap<Date,FDPromoZipRestriction> zrt = new TreeMap<Date,FDPromoZipRestriction>();
		for(int j=0; j<3; j++) {
			final Date zrDate = df.parse("2010.01."+j);

			FDPromoZipRestriction zr = new FDPromoZipRestriction();
			zr.setStartDate(aDate);
			zr.setType("type"+j);
			zr.setZipCodes("11101,11102,11103");
			zrt.put(zrDate,zr);
		}
		promoNew.setZipRestrictions(zrt);
		
		List<FDPromotionAttributeParam> attrList = new ArrayList<FDPromotionAttributeParam>();
		for (int j=0; j<3; j++) {
			FDPromotionAttributeParam attr = new FDPromotionAttributeParam();
			attr.setPK(new PrimaryKey("12300"+j));
			attr.setAttributeName("attr"+j);
			attr.setAttributeIndex("ix"+j);
			attr.setDesiredValue("val"+j);
			attrList.add(attr);
		}
		promoNew.setAttributeList(attrList);
		
		promoNew.setPromotionCode("promo123");
		promoNew.setName("PROMO123");
		promoNew.setDescription("Desc");
		promoNew.setRedemptionCode("RDC123");
		promoNew.setStartDate(aDate);
		promoNew.setStartYear("1973");
		promoNew.setStartMonth("1");
		promoNew.setStartDay("31");
		promoNew.setExpirationDate(aDate);
		promoNew.setExpirationYear("1973");
		promoNew.setExpirationMonth("1");
		promoNew.setExpirationDay("31");
		promoNew.setRollingExpirationDays(3);
		promoNew.setMaxUsage("3");
		promoNew.setPromotionType("PT1");
		promoNew.setMinSubtotal("10");
		promoNew.setMaxAmount("2");
		promoNew.setPercentOff("5");
		promoNew.setWaiveChargeType("dontknow");
		promoNew.setStatus(EnumPromotionStatus.CANCELLED);
		promoNew.setOfferDesc("offer desc 123");
		promoNew.setAudienceDesc("valami");
		promoNew.setTerms("nothing");
		promoNew.setRedeemCount(1);
		promoNew.setSkuQuantity(10);
		promoNew.setPerishable(false);
		promoNew.setTmpAssignedCustomerUserIds("111,112,113");
		
		List<FDPromoContentModel> dcpdData = new ArrayList<FDPromoContentModel>();
		{
			dcpdData.add(ctnt);
		}
		promoNew.setDcpdData(dcpdData);
		
		List<FDPromoContentModel> cartStrategies = new ArrayList<FDPromoContentModel>();
		{
			dcpdData.add(ctnt);
		}
		promoNew.setCartStrategies(cartStrategies);

		List<FDPromoCustStrategyModel> custStrategies = new ArrayList<FDPromoCustStrategyModel>();
		{
			FDPromoCustStrategyModel custStrategy = new FDPromoCustStrategyModel();
			custStrategy.setPromotionId("promo123");
			custStrategy.setCohorts(new String[]{"C1","C2","C3"});
			custStrategy.setDpStatus("dontknow");
			custStrategy.setDpExpStart(aDate);
			custStrategy.setDpExpEnd(aDate);
			custStrategies.add(custStrategy);
		}
		promoNew.setCustStrategies(custStrategies);
		
		List<FDPromoPaymentStrategyModel> dlvPaymentStrategies = new ArrayList<FDPromoPaymentStrategyModel>();
		{
			FDPromoPaymentStrategyModel strategy = new FDPromoPaymentStrategyModel();
			strategy.setPromotionId("promo123");
			strategy.setOrderTypeHome(true);
			strategy.setOrderTypePickup(false);
			strategy.setOrderTypeCorporate(false);
			strategy.setPriorEcheckUse("nope");
			
			dlvPaymentStrategies.add(strategy);
		}
		promoNew.setPaymentStrategies(dlvPaymentStrategies);
		
		
		List<FDPromoDlvZoneStrategyModel> dlvZoneStrategies = new ArrayList<FDPromoDlvZoneStrategyModel>();
		{
			FDPromoDlvZoneStrategyModel zoneStrat = new FDPromoDlvZoneStrategyModel();
			zoneStrat.setPromotionId("promo123");
			zoneStrat.setDlvDays("Mon,Tue,Wed");
			zoneStrat.setDlvZones(new String[]{"zone1", "zone2", "zone3"});
			
			List<FDPromoDlvTimeSlotModel> dlvTimeSlots = new ArrayList<FDPromoDlvTimeSlotModel>();
			FDPromoDlvTimeSlotModel dlvTimeSlot = new FDPromoDlvTimeSlotModel();
			{
				dlvTimeSlot.setDayId(0);
				dlvTimeSlot.setDlvTimeStart("8:00");
				dlvTimeSlot.setDlvTimeEnd("10:00");
				dlvTimeSlot.setPromoDlvZoneId("<nil>");
			}
			dlvTimeSlots.add(dlvTimeSlot);

			zoneStrat.setDlvTimeSlots(dlvTimeSlots);
		}
		promoNew.setDlvZoneStrategies(dlvZoneStrategies);
		
		List<FDPromoChangeModel> auditChanges = new ArrayList<FDPromoChangeModel>();
		{
			FDPromoChangeModel audit = new FDPromoChangeModel();
			audit.setPromotionId("promo123");
			audit.setUserId("fob@fob.com");
			audit.setActionDate(aDate);
			audit.setActionType(EnumPromoChangeType.APPROVE);
			
			FDPromoChangeDetailModel detail = new FDPromoChangeDetailModel();
			detail.setPromoChangeId("pchg1");
			detail.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
			detail.setChangeFieldOldValue("old");
			detail.setChangeFieldNewValue("new");
			
			List<FDPromoChangeDetailModel> changeDetails = new ArrayList<FDPromoChangeDetailModel>();
			changeDetails.add(detail);
			audit.setChangeDetails(changeDetails);
		}
		promoNew.setAuditChanges(auditChanges);
		
		promoNew.setNeedDryGoods(false);
		promoNew.setNeedCustomerList(false);



		promoSer = (FDPromotionJSONSerializer) FDPromotionJSONSerializer.getInstance();
		ser.registerDefaultSerializers();
		ser.registerSerializer(promoSer);
	}
	

	public void testNewPromoProperties() {
		final Map<Class<?>, String[]> klassProps = new HashMap<Class<?>, String[]>();
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromotionNewModel.class,
				new String[]{"applyFraud", "assignedCustomerParams", "assignedCustomerUserIds", "attributeList", "audienceDesc", "auditChanges", "cartBrands", "cartCats", "cartDepts", "cartSkus", "cartStrategies", "categoryName", "combineOffer", "createdBy", "createdDate", "custStrategies", "dcpdBrands", "dcpdCats", "dcpdData", "dcpdDepts", "dcpdRecps", "dcpdSkus", "description", "dlvDates", "dlvZoneStrategies", "expirationDate", "expirationDateStr", "expirationDay", "expirationMonth", "expirationTimeStr", "expirationYear", "extendDpDays", "favoritesOnly", "geoRestrictionType", "lastPublishedDate", "maxAmount", "maxItemCount", "maxUsage", "minSubtotal", "modifiedBy", "modifiedDate", "name", "needCustomerList", "needDryGoods", "offerDesc", "offerType", "onHold", "paymentStrategies", "percentOff", "perishable", "productName", "profileOperator", "promotionCode", "promotionType", "publishes", "redeemCount", "redemptionCode", "rollingExpirationDays", "ruleBased", "skuQuantity", "startDate", "startDateStr", "startDay", "startMonth", "startTimeStr", "startYear", "status", "subTotalExcludeSkus", "terms", "tmpAssignedCustomerUserIds", "waiveChargeType", "zipRestrictions"});
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromoChangeModel.class,
				new String[]{"actionDate", "actionType", "changeDetails", "promotionId", "userId"});
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel.class,
				new String[]{"changeFieldName", "changeFieldNewValue", "changeFieldOldValue", "changeSectionId", "promoChangeId"});
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel.class,
				new String[]{"dayId", "dlvTimeEnd", "dlvTimeStart", "promoDlvZoneId"});
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel.class,
				new String[]{"dlvDays", "dlvTimeSlots", "dlvZones", "promotionId"});
		klassProps.put(com.freshdirect.fdstore.promotion.management.FDPromoPaymentStrategyModel.class,
				new String[]{"orderTypeCorporate", "orderTypeHome", "orderTypePickup", "paymentType", "priorEcheckUse", "promotionId"});
		
		for (Class<?> klass : klassProps.keySet()) {
			Map<String, Method> props = promoSer.collectProperties(klass);
			List<String> sortedPropKeys = new ArrayList<String>(props.keySet());
			Collections.sort(sortedPropKeys, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});

			List<String> expectedProps = new ArrayList<String>( Arrays.asList(klassProps.get(klass)) );
			assertEquals(expectedProps, sortedPropKeys);
		}
	}

	
	/**
	 * If testNewPromoProperties test breaks run this method and replace klassProps.put .. lines
	 * in the test with the resulted ones
	 * 
	 */
	public void generateKlassPropRowsForNewPromotionProperties() {
		// Uncomment to generate new list of getters above
		Class<?>[] klasses = new Class[] {
			FDPromoContentModel.class,
			FDPromoCustStrategyModel.class,
			FDPromotionNewModel.class,
			FDPromoChangeModel.class,
			FDPromoChangeDetailModel.class,
			FDPromoDlvTimeSlotModel.class,
			FDPromoDlvZoneStrategyModel.class,
			FDPromoPaymentStrategyModel.class
		};

        for (Class<?> klass : klasses) {
			Map<String, Method> props = promoSer.collectProperties(klass);
			List<String> sortedPropKeys = new ArrayList<String>(props.keySet());
			Collections.sort(sortedPropKeys, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			StringBuffer buf = new StringBuffer();
			buf.append("klassProps.put("+klass.getCanonicalName()+".class,\t\t\t\t");
			buf.append("new String[]{");
			int k = props.keySet().size();
			for (String prop : sortedPropKeys) {
				buf.append("\""+prop+"\"");
				k--;
				buf.append(k > 0 ? ", " : "}");
			}
			buf.append(");");
			System.out.println(buf.toString());
		}
	}
	
	
	public void testSimpleTypes() throws MarshallException {
		Object testObj;
		
		promoSer.setAppendClassInfo(false);

		class T1 {
			int k=1;
			@SuppressWarnings("unused")
			public int getK() {return k;}
			public void setK(int k) {
				this.k = k;
			}
		};
		
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T1());
		// System.err.println(testObj.toString());
		assertEquals("{\"k\":1}", testObj.toString());

		class T2 {
			String str = "abc";
			@SuppressWarnings("unused")
			public String getStr() {return str;}
			public void setStr(String str) {
				this.str = str;
			}
		};
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T2());
		// System.err.println(testObj.toString());
		assertEquals("{\"str\":\"abc\"}", testObj.toString());

		class T3 {
			String[] str = {"a", "b", "c"};
			@SuppressWarnings("unused")
			public String[] getStr() {return str;}
			public void setStr(String[] str) {
				this.str = str;
			}
		}
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T3());
		// System.err.println(testObj.toString());
		assertEquals("{\"str\":[\"a\",\"b\",\"c\"]}", testObj.toString());

		class T4 {
			Object[] array = new Object[]{new Integer[]{1,2,3}, "b", "c"};
			@SuppressWarnings("unused")
			public Object[] getArray() {return array;}
			public void setArray(Object[] array) {
				this.array = array;
			}
		}
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T4());
		// System.err.println(testObj.toString());
		assertEquals("{\"array\":[[1,2,3],\"b\",\"c\"]}", testObj.toString());

		class T5 {
			Map<String,Object> map = new HashMap<String,Object>();
			
			public T5() {
				map.put("a", "b");
				map.put("b", new String[]{"alma", "korte","eper"});
				map.put("c", new T4());
			}
			
			@SuppressWarnings("unused")
			public Map<String, Object> getMap() {return map;}
			public void setMap(Map<String, Object> map) {
				this.map = map;
			}
		}
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T5());
		// System.err.println(testObj.toString());
		assertEquals("{\"map\":{\"b\":[\"alma\",\"korte\",\"eper\"],\"c\":{\"array\":[[1,2,3],\"b\",\"c\"]},\"a\":\"b\"}}", testObj.toString());

		class T6 {
			EnumCheckoutMode mode = EnumCheckoutMode.CREATE_SO;
			
			public EnumCheckoutMode getMode() {return mode;}
			public void setMode(EnumCheckoutMode mode) {
				this.mode = mode;
			}
		}
		
		testObj = (JSONObject) promoSer.marshall(new SerializerState(), new T6());
		// System.err.println(testObj);
		assertEquals("{\"mode\":\"CREATE_SO\"}", testObj.toString());
		
		List<String> l1 = new ArrayList<String>();
		l1.add("a1");
		l1.add("a2");
		l1.add("a3");
		
		testObj = promoSer.marshall(new SerializerState(), l1);
		// System.err.println(testObj.toString());
		assertEquals("[\"a1\",\"a2\",\"a3\"]", testObj.toString());
	}
	
	public void testSerialization() throws MarshallException, UnmarshallException {
		promoSer.setAppendClassInfo(true);
		
		assertTrue(isEqual(ctnt, ser.fromJSON(ser.toJSON(ctnt))));
		assertTrue(isEqual(custStrategy, ser.fromJSON(ser.toJSON(custStrategy))));
		assertTrue(isEqual(paymentStrategy, ser.fromJSON(ser.toJSON(paymentStrategy))));
		final FDPromotionNewModel promoNew2 = (FDPromotionNewModel) ser.fromJSON(ser.toJSON(promoNew));
		assertTrue(isEqual(promoNew, promoNew2));
	}




	
	protected boolean isEqual(Object o1, Object o2) {
		if (o1 == null || o2 == null)
			return false;
		
		if (o1.equals(o2))
			return true;

		if (!o1.getClass().equals(o2.getClass()))
			return false;
		
		Map<String, Method> props = promoSer.collectProperties(o1.getClass());
		List<String> sortedPropKeys = new ArrayList<String>(props.keySet());
		Collections.sort(sortedPropKeys, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (String propName : sortedPropKeys) {
			final Method m = props.get(propName);

			Object val1, val2;
			try {
				val1 = m.invoke(o1);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return false;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return false;
			}

			try {
				val2 = m.invoke(o2);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return false;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return false;
			}

			if (val1 == null && val2 == null)
				continue;
			
			if (val1 != null) {
				if (val2 == null) {
					System.err.println("[NEQ] Property '" + propName + "' not-null != null");
					return false;
				}
				
				try {
					if (val1.getClass().isArray()) {
						Object[] arr1 = (Object[]) val1;
						Object[] arr2 = (Object[]) val2;

						if (arr1.length != arr2.length) {
							System.err.println("[NEQ|1] Property '" + propName
									+ "' |" + arr1.length + "| != |"
									+ arr2.length + "|");
							return false;
						}

						for (int j = 1; j < arr1.length; j++) {
							if (!isEqual(arr1[j], arr2[j])) {
								System.err.println("[NEQ|2}@" + j + " " + arr1[j] + " != " + arr2[j]);
								return false;
							}
						}
					} else if (val2 == null || !isEqual(val1,val2)) {
						System.err.println("[NEQ|3] Property '" + propName + "' "
								+ val1 + " != " + val2);
						return false;
					}
				} catch (ClassCastException e) {
					System.err.println("BAANG " + propName + " / " + val1.getClass() + " / parent: " + o1.getClass());
				}
			} else {
				if (val2 != null) {
					System.err.println("[NEQ|4] val2 is NULL");
					return false;
				}
			}
		}
		
		return true;
	}
}
