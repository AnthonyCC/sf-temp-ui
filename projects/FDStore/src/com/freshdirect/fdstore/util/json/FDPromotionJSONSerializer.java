package com.freshdirect.fdstore.util.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Category;
import org.json.JSONArray;
import org.json.JSONObject;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.delivery.EnumComparisionType;
import com.freshdirect.delivery.EnumDeliveryOption;
import com.freshdirect.delivery.EnumPromoFDXTierType;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.fdstore.promotion.ActiveInactiveStrategy;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.AssignedCustomerStrategy;
import com.freshdirect.fdstore.promotion.AudienceStrategy;
import com.freshdirect.fdstore.promotion.CartStrategy;
import com.freshdirect.fdstore.promotion.CompositeStrategy;
import com.freshdirect.fdstore.promotion.CustomerStrategy;
import com.freshdirect.fdstore.promotion.DCPDLineItemStrategy;
import com.freshdirect.fdstore.promotion.DCPDiscountApplicator;
import com.freshdirect.fdstore.promotion.DCPDiscountRule;
import com.freshdirect.fdstore.promotion.DateRangeStrategy;
import com.freshdirect.fdstore.promotion.DlvZoneStrategy;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumOrderType;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.FDMinDCPDTotalPromoData;
import com.freshdirect.fdstore.promotion.FraudStrategy;
import com.freshdirect.fdstore.promotion.GeographyStrategy;
import com.freshdirect.fdstore.promotion.HeaderDiscountApplicator;
import com.freshdirect.fdstore.promotion.HeaderDiscountRule;
import com.freshdirect.fdstore.promotion.LimitedUseStrategy;
import com.freshdirect.fdstore.promotion.LineItemDiscountApplicator;
import com.freshdirect.fdstore.promotion.LineItemStrategyI;
import com.freshdirect.fdstore.promotion.MaxLineItemCountStrategy;
import com.freshdirect.fdstore.promotion.MaxRedemptionStrategy;
import com.freshdirect.fdstore.promotion.MinimumSubtotalStrategy;
import com.freshdirect.fdstore.promotion.OrderTypeStrategy;
import com.freshdirect.fdstore.promotion.PercentOffApplicator;
import com.freshdirect.fdstore.promotion.PerishableLineItemStrategy;
import com.freshdirect.fdstore.promotion.ProductSampleApplicator;
import com.freshdirect.fdstore.promotion.ProfileAttributeStrategy;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionContextI;
import com.freshdirect.fdstore.promotion.PromotionDlvDate;
import com.freshdirect.fdstore.promotion.PromotionDlvDay;
import com.freshdirect.fdstore.promotion.PromotionDlvTimeSlot;
import com.freshdirect.fdstore.promotion.PromotionGeography;
import com.freshdirect.fdstore.promotion.PromotionStrategyI;
import com.freshdirect.fdstore.promotion.RecommendationStrategy;
import com.freshdirect.fdstore.promotion.RecommendedLineItemStrategy;
import com.freshdirect.fdstore.promotion.RedemptionCodeStrategy;
import com.freshdirect.fdstore.promotion.ReferAFriendStrategy;
import com.freshdirect.fdstore.promotion.RuleBasedPromotionStrategy;
import com.freshdirect.fdstore.promotion.SampleLineApplicator;
import com.freshdirect.fdstore.promotion.SampleStrategy;
import com.freshdirect.fdstore.promotion.SignupDiscountApplicator;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.promotion.SignupStrategy;
import com.freshdirect.fdstore.promotion.SkuLimitStrategy;
import com.freshdirect.fdstore.promotion.StateCountyStrategy;
import com.freshdirect.fdstore.promotion.UniqueUseStrategy;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDollarDiscount;
import com.freshdirect.fdstore.promotion.management.FDPromoPaymentStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoStateCountyRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.WSAdminInfo;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductReference;
import com.metaparadigm.jsonrpc.AbstractSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.ObjectMatch;
import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class FDPromotionJSONSerializer extends AbstractSerializer {
	private static final long serialVersionUID = 4602538095592746033L;

	private static Category		LOGGER				= LoggerFactory.getInstance( FDPromotionJSONSerializer.class );

	private static Class<?>[] _serializableClasses = new Class[] {
		FDPromoContentModel.class, FDPromoCustStrategyModel.class,
		FDPromoPaymentStrategyModel.class, FDPromotionNewModel.class,
		FDPromoChangeModel.class, FDPromoChangeDetailModel.class,
		FDPromoDlvZoneStrategyModel.class, FDPromoDlvTimeSlotModel.class, WSPromotionInfo.class,WSAdminInfo.class, EnumPromotionStatus.class,
		FDPromoDollarDiscount.class,FDPromoStateCountyRestriction.class,EnumDeliveryOption.class,
		Promotion.class,
		EnumPromotionType.class,
		PromotionStrategyI.class,
		ActiveInactiveStrategy.class,
		AssignedCustomerStrategy.class,
		AudienceStrategy.class,
		CartStrategy.class,
		CompositeStrategy.class,
		CustomerStrategy.class,
		DateRangeStrategy.class,
		DlvZoneStrategy.class,
		FraudStrategy.class,
		GeographyStrategy.class,
		LimitedUseStrategy.class,
		MaxRedemptionStrategy.class,
		MinimumSubtotalStrategy.class,
		OrderTypeStrategy.class,
		ProfileAttributeStrategy.class,
		RecommendationStrategy.class,
		RedemptionCodeStrategy.class,
		ReferAFriendStrategy.class,
		RuleBasedPromotionStrategy.class,
		SampleStrategy.class,
		SignupStrategy.class,
		StateCountyStrategy.class,
		UniqueUseStrategy.class,
		PromotionApplicatorI.class,
		DCPDiscountApplicator.class,
		ExtendDeliveryPassApplicator.class,
		HeaderDiscountApplicator.class,
		LineItemDiscountApplicator.class,
		PercentOffApplicator.class,
		ProductSampleApplicator.class,
		SampleLineApplicator.class,
		SignupDiscountApplicator.class,
		WaiveChargeApplicator.class,
		AssignedCustomerParam.class,//For AssignedCustomerStrategy.java
		EnumDCPDContentType.class,//For CartStrategy.java
		FDMinDCPDTotalPromoData.class,//For CartStrategy.java
		EnumCardType.class,//For CustomerStrategy.java
		EnumOrderType.class,//For CustomerStrategy.java, OrderTypeStrategy.java
		EnumComparisionType.class,//For CustomerStrategy.java
		EnumDeliveryType.class,//For CustomerStrategy.java
		PromotionDlvTimeSlot.class,//For DlvZoneStrategy.java
		PromotionDlvDate.class,//For DlvZoneStrategy.java
		PromotionDlvDay.class,//For DlvZoneStrategy.java
		EnumPromoFDXTierType.class,	//For DlvZoneStrategy.java
		PromotionGeography.class,//For GeographyStrategy.java
		PromotionContextI.class,//For PromotionApplicatorI.java
		DCPDiscountRule.class,//For DCPDiscountApplicator.java
		HeaderDiscountRule.class,//For HeaderDiscountApplicator.java
		FDPromoDollarDiscount.class,//For HeaderDiscountRule.java
		ProductReference.class,//For ProductSampleApplicator.java
		SignupDiscountRule.class,//For SignupDiscountApplicator.java
		EnumChargeType.class,//For WaiveChargeApplicator.java
		LineItemStrategyI.class,//For LineItemDiscountApplicator.java
		DCPDLineItemStrategy.class,
		MaxLineItemCountStrategy.class,
		PerishableLineItemStrategy.class,
		RecommendedLineItemStrategy.class,
		SkuLimitStrategy.class,
		ContentKey.class,//For DCPDLineItemStrategy.java
		PromotionContextAdapter.class,
		EnumOfferType.class
		

	};

	private static Class<?>[] _JSONClasses = new Class[] { JSONObject.class };

	
	private static Serializer instance = new FDPromotionJSONSerializer();

	private boolean appendClassInfo = true;
	
	/** Use getInstance() */
	private FDPromotionJSONSerializer() {}
	
	/** Get a reusable instance of this serializer.
	 * 
	 * @return serializer instance
	 */
	public static Serializer getInstance() { return instance; }

	public void setAppendClassInfo(boolean appendClassInfo) {
		this.appendClassInfo = appendClassInfo;
	}

	public boolean isAppendClassInfo() {
		return appendClassInfo;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	@Override
	public Object marshall(SerializerState state, Object obj)
			throws MarshallException {
		return serializeRightHandSide(obj);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz, Object obj) throws UnmarshallException {
		unmarshall(state, clazz, obj);
		return ObjectMatch.OKAY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unmarshall(SerializerState state, Class clazz, Object jsonObj)
			throws UnmarshallException {

		return deserializeRightHandSize(jsonObj);
	}


	/**
	 * @param obj
	 */
	Map<String, Method> collectProperties(Class<?> klass) {
		Map<String,Method> props = new HashMap<String,Method>();

		final boolean isModel = ModelSupport.class.isAssignableFrom(klass);
		
		// find getters
		for (Method m : klass.getMethods()) {
			if (m.getDeclaringClass().isAssignableFrom(ModelSupport.class))
				continue;
			
			if (m.getName().startsWith("get")) {
				if (isModel && (m.getName().equals("getId") || m.getName().equals("getPK") )) {
					continue;
				} else if (m.getName().equals("getClass")) {
					continue;
				} else if (m.getParameterTypes().length > 0) {
					continue;
				}

				final String prop = java.beans.Introspector.decapitalize(m.getName().substring(3));
				
				if (getSetter(klass, prop, m.getReturnType()) != null) {
					props.put(prop, m);
				} else {
					LOGGER.debug(klass+" [collectProperties] Prop '" + prop + "' skipped, no setter");
				}
			} else if (m.getName().startsWith("is")) {
				// boolean type
				final String prop = java.beans.Introspector.decapitalize(m.getName().substring(2));
				if (getSetter(klass, prop, m.getReturnType()) != null) {
					props.put(prop, m);
				} else {
					LOGGER.debug(klass+" [collectProperties] Prop '" + prop + "' skipped, no setter");
				}
			}
		}
		
		return props;
	}
	
	protected Method getSetter(Class<?> klazz, String prop, Class<?> valueType) {
		char chars[] = prop.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		final String setterName = "set"+new String(chars);

		Method setter = null;
		try {
			 setter = klazz.getMethod(setterName, valueType);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}

		return void.class.equals(setter.getReturnType()) ? setter : null;		
	}
	
	public void doSerialize(Object obj, Map<String, Method> props, JSONObject target) {
		List<String> sortedPropKeys = new ArrayList<String>(props.keySet());
		Collections.sort(sortedPropKeys, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (String propName : sortedPropKeys) {
			final Method m = props.get(propName);

			try {
				Object val = m.invoke(obj);
				if (val != null) {
					serializeKeyValuePair(target, propName, val);
				}
			} catch (IllegalArgumentException e) {
				LOGGER.error("Failed to serialize property " + propName, e);
			} catch (IllegalAccessException e) {
				LOGGER.error("Failed to serialize property " + propName, e);
			} catch (InvocationTargetException e) {
				LOGGER.error("Failed to serialize property " + propName, e);
			}
		}
	}
	
	public void serializeKeyValuePair(JSONObject target, Object key, Object val) {
		if (key == null || val == null)
			return;

		final String serKey = getSerializedValue(key).toString();
		
		target.put(serKey, serializeRightHandSide(val));
	}


	protected Object serializeRightHandSide(Object val) {
		final String klassName = val.getClass().getName();
		if (val.getClass().isArray()) {
			JSONArray jarr = new JSONArray();
			serializeArray(jarr, Arrays.asList((Object[])val) );
			return jarr;
		} else if (val instanceof Iterable<?>) {
			JSONArray jarr = new JSONArray();
			serializeArray(jarr, (Iterable<?>)val);
			return jarr;
		} else if (val instanceof Map<?,?>) {
			JSONObject jarr = new JSONObject();
			serializeMap(jarr, (Map<?,?>)val);
			return jarr;
		} else if (klassName.startsWith("java.lang") ||
				klassName.startsWith("java.util") ||
				klassName.startsWith("java.sql") ||
				val instanceof org.apache.commons.lang.enums.Enum ||
				val instanceof java.lang.Enum<?>) {
			return getSerializedValue(val);
		} else {
			JSONObject jsonObj = new JSONObject();
			serializeObject(jsonObj, val);				
			return jsonObj;
		}
	}
	
	
	/**
	 * Serializes a map
	 * 
	 * @param target
	 * @param map
	 */
	public void serializeMap(JSONObject target, Map<?,?> map) {
		for (Object key : map.keySet()) {
			serializeKeyValuePair(target, key, map.get(key));
		}
	}
	
	
	/**
	 * Serializes an iterable set to arr object
	 * @param arr Iterable object (Collection, Set, List, etc)
	 * @param arrval
	 */
	public void serializeArray(JSONArray arr, Iterable<?> arrval) {
		for (Object val : arrval) {
			arr.put(serializeRightHandSide(val));
		}
	}

	/**
	 * Serializes a non-primitive object by visiting its getters
	 * and serializing retrieved values
	 * @param target
	 * @param anObject
	 */
	public void serializeObject(JSONObject target, Object anObject) {
		Map<String,Method> props = collectProperties(anObject.getClass());
		doSerialize(anObject, props, target);
		
		if (appendClassInfo) {
			target.put("javaClass", anObject.getClass().getName());
		}
	}
	
	
	public Object getSerializedValue(Object val) {
		if (val instanceof String) {
			return val;
		} else if (val instanceof java.lang.Number) {
			return val;
		} else if (val instanceof org.apache.commons.lang.enums.Enum) {
			final Enum enum1 = (org.apache.commons.lang.enums.Enum)val;
			
			for (Field f : val.getClass().getFields()) {
				try {
					if (f.get(null).equals(enum1)) {
						return f.getName();
					}
				} catch (IllegalArgumentException e) {
					LOGGER.error("Failed to decode enum " + enum1, e);
				} catch (IllegalAccessException e) {
					LOGGER.error("Failed to decode enum " + enum1, e);
				}
			}
			
			LOGGER.debug("Serialize Apache enum " + val.getClass().getName() + " value " + val + " to " + enum1.getName() );
			return enum1.getName();
		} else if (val instanceof java.lang.Enum<?>) {
			java.lang.Enum<?> enm = (java.lang.Enum<?>) val;
			return enm.name();
		} else if (val instanceof java.sql.Date || val instanceof java.sql.Timestamp) {
			return Long.toString( ((java.util.Date)val).getTime() );
		} else if (val instanceof java.util.Date) {
			return DateFormatUtils.ISO_DATETIME_FORMAT.format((Date) val);
		} else {
			return val.toString();
		}
	}



	public Object deserializeRightHandSize(Object obj) {
		if (obj instanceof JSONObject) {
			JSONObject jsObject = (JSONObject) obj;
			if (jsObject.has("javaClass")) {
				try {
					return restoreObject(jsObject);
				} catch (NoSuchElementException e) {
					LOGGER.error("deserializeRightHandSize", e);
					return null;
				} catch (ClassNotFoundException e) {
					LOGGER.error("deserializeRightHandSize", e);
					return null;
				}
			} else {
				// MAP ??
			}
		} else if (obj instanceof JSONArray) {
			JSONArray arr = (JSONArray) obj;
			
			List l = new ArrayList();
			for (int i=0; i<arr.length(); i++) {
				Object v = arr.get(i);
				
				Object tv = deserializeRightHandSize(v);
				
				l.add(tv);
			}
			
			return l;
		}

		// return primitive type in original string format
		return obj;
	}
	
	public Object restoreObject(JSONObject jsObject) throws NoSuchElementException, ClassNotFoundException {
		Class<?> klass = Class.forName(jsObject.getString("javaClass"));
		
		// restore class
		try {
			Object obj = klass.newInstance();
			
			Map<String, Method> props = collectProperties(klass);
			for (String prop : props.keySet()) {
				Method getter = props.get(prop);
				
				// no data, skip property
				if (!jsObject.has(prop))
					continue;
				
				// skip if method has no return value
				Class<?> valueType = getter.getReturnType();
				if (valueType == null)
					continue;
				
				Method setter = getSetter(klass, prop, valueType);
				if (setter == null)
					continue;
				
				// deserialize value
				Object rhs = deserializeRightHandSize(jsObject.get(prop));
				
				if (valueType.isArray()) {
					//
					// ARRAY
					//
					if (rhs instanceof Collection) {
						Collection coll = (Collection) rhs;
						
		                Object vals = Array.newInstance(valueType.getComponentType(), coll.size());
						int k = 0;
						for (Object o : coll) {
		                    Array.set(vals, k++, getDeserializedValue(o, valueType.getComponentType()));
						}
						
						// set value
						silentInvoke(obj, setter, vals);
					} else {
						// Value is in not expected format, skip ...
						// System.err.println("BANG[1] rhs:" + rhs.getClass().getName() + "/ valueType: " + valueType);
						// LOGGER.debug("Unexpected rhs:" + rhs.getClass().getName() + "/ valueType: " + valueType));
						continue;
					}
				} else if (Iterable.class.isAssignableFrom(valueType)) {
					//
					// COLLECTION
					//
					
					Collection coll = (Collection) rhs;
					Collection valami;
					ParameterizedType rt =null;
					if(getter.getGenericReturnType() instanceof ParameterizedTypeImpl){
						try {
							rt = (ParameterizedType) getter.getGenericReturnType();
						} catch (Exception e1) {
							LOGGER.error("Exception in getter.getGenericReturnType():", e1);
						}
					}
					if (List.class.equals(valueType)) {
						if(null == rt){
							valami = new ArrayList();
						}else{
							valami = createListOfType((Class<?>)rt.getActualTypeArguments()[0]);
						}
					} else if (Set.class.equals(valueType)) {
						if(null == rt){
							valami = new HashSet();
						}else{
							valami = createSetOfType((Class<?>)rt.getActualTypeArguments()[0]);
						}
					} else {
						valami = (Collection) valueType.newInstance();
					}

					
					/*for (Object o : coll) {
						valami.add(deserializeRightHandSize(o));
					}*/
					
					if(null !=rt){
						Class classRt =(Class) rt.getActualTypeArguments()[0];
						if(org.apache.commons.lang.enums.Enum.class.equals(classRt.getSuperclass()) || org.apache.commons.lang.enums.Enum.class.equals(classRt) ||
								org.apache.commons.lang.enums.ValuedEnum.class.equals(classRt.getSuperclass()) || org.apache.commons.lang.enums.ValuedEnum.class.equals(classRt)){
							try {
								for (Object o : coll) {
									Field decl = classRt.getField(o.toString());
									Object fld = decl.get(valueType);
									valami.add(fld);
								}
							} catch (SecurityException e) {
								LOGGER.error("SecurityException:", e);
							} catch (IllegalArgumentException e) {
								LOGGER.error("IllegalArgumentException:", e);
							} catch (NoSuchFieldException e) {
								LOGGER.error("NoSuchFieldException:", e);
							}
						}else{
							for (Object o : coll) {
								valami.add(deserializeRightHandSize(o));
							}	
						}
					}else{
						for (Object o : coll) {
							valami.add(deserializeRightHandSize(o));
						}
					}
					
					silentInvoke(obj, setter, valami);
				} else if (Map.class.isAssignableFrom(valueType)) {
					ParameterizedType rt = (ParameterizedType) getter.getGenericReturnType();
					Class<?> keyType = null;
					Class<?> valType = null;
					try {
						keyType = (Class<?>) rt.getActualTypeArguments()[0];
						if(rt.getActualTypeArguments()[1] instanceof ParameterizedTypeImpl){
							valType =((ParameterizedTypeImpl)rt.getActualTypeArguments()[1]).getRawType();
						}else{
							valType = (Class<?>) rt.getActualTypeArguments()[1];
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Map map = (Map) valueType.newInstance();
					Map map;
					if (Map.class.equals(valueType)) {
						map = new HashMap();
					} else {
						map = (Map) valueType.newInstance();
					}
					
					for (Iterator it = ((JSONObject)rhs).keys(); it.hasNext(); ) {
						String key = (String) it.next();
						Object val = ((JSONObject)rhs).get(key);
						
						Object tKey = getDeserializedValue(key, keyType);
						Object tVal = deserializeRightHandSize(val);
						if (Set.class.equals(valType) && null !=tVal && tVal instanceof List){
							tVal = new HashSet((List)tVal);
						}
						map.put(tKey, tVal);
					}
					
					silentInvoke(obj, setter, map);
				} else {
					silentInvoke(obj, setter, getDeserializedValue(rhs, valueType));
				}
				
			}
			
			return obj;
		} catch (InstantiationException e) {
			LOGGER.error("restoreObject", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("restoreObject", e);
		} catch (ClassCastException e){
			LOGGER.error("restoreObject", e);
		}
		
		return null;
	}

	
	/**
	 * @param obj
	 * @param setter
	 * @param arg
	 * @throws IllegalAccessException
	 */
	private void silentInvoke(Object obj, Method setter, Object arg)
			throws IllegalAccessException {
		try {
			setter.invoke(obj, arg);
		} catch (IllegalArgumentException e) {
			LOGGER.error("silentIvoke crashed. Target: " + obj.getClass() + " / Setter: "+setter.getName() + " / Arg: " + arg + " / Arg Class: " + arg.getClass(), e);
		} catch (InvocationTargetException e) {
			LOGGER.error("silentIvoke crashed. Target: " + obj.getClass() + " / Setter: "+setter.getName() + " / Arg: " + arg + " / Arg Class: " + arg.getClass(), e);
		}
	}
	

	public Object getDeserializedValue(Object rhs, Class<?> valueType) {
		// Debug
		LOGGER.debug("Decode '" + rhs + "' of type "+ valueType.getName());
		
		if (java.sql.Date.class.isAssignableFrom(valueType)) {
			return new java.sql.Date(Long.parseLong(rhs.toString()));
		} else if (java.sql.Timestamp.class.isAssignableFrom(valueType)) {
			return new java.sql.Timestamp(Long.parseLong(rhs.toString()));
		} else if (java.util.Date.class.isAssignableFrom(valueType)) {
			// Date type
			java.util.Date d = null;
			/*try {
				d = DateUtils.parseIso8601DateTime(rhs.toString());
			} catch (ParseException e) {
			}
			
			if (d == null) {
				d = new Date(Long.parseLong(rhs.toString()));
			}*/
			
			try {
				d = new Date(Long.parseLong(rhs.toString()));
			} catch (NumberFormatException e) {
				LOGGER.error("Failed to decode String:"+rhs.toString()+"to java.util.Date " + valueType, e);
			}
			if (d != null)
				return d;
		} else if (org.apache.commons.lang.enums.Enum.class.isAssignableFrom(valueType)) {
			try {
				Field decl = valueType.getField(rhs.toString());
				Object fld = decl.get(valueType);
				return fld;
			} catch (IllegalAccessException e) {
				LOGGER.error("Failed to decode Apache enum " + valueType, e);
			} catch (SecurityException e) {
				LOGGER.error("Failed to decode Apache enum " + valueType, e);
			} catch (NoSuchFieldException e) {
				LOGGER.error("Failed to decode Apache enum " + valueType, e);
			}
		} else if (java.lang.Double.class.isAssignableFrom(valueType)) {
			return Double.parseDouble((String.valueOf(rhs)));
		} else if (java.lang.Float.class.isAssignableFrom(valueType)) {
			return Float.parseFloat(String.valueOf(rhs));
		} else if (boolean.class.isAssignableFrom(valueType) || java.lang.Boolean.class.isAssignableFrom(valueType)) {
			return Boolean.parseBoolean(String.valueOf(rhs));
		} else {
			LOGGER.debug("[getDeserializedValue] Type: " + valueType + " <- " + rhs);
			
			return rhs;
		}
		
		LOGGER.warn("Failed to decode value '" + rhs + "' with type " + valueType.getName());
		
		return null;
	}
	
	public static <T> Set<T> createSetOfType(Class<T> type) {
	    return new HashSet<T>();
	}
	
	public static <T> List<T> createListOfType(Class<T> type) {
	    return new ArrayList<T>();
	}
}
