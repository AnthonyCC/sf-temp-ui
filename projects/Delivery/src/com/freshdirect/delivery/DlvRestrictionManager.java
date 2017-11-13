package com.freshdirect.delivery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.ejb.DlvRestrictionManagerHome;
import com.freshdirect.delivery.ejb.DlvRestrictionManagerSB;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.ecommerce.data.delivery.AbstractRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AddressAndRestrictedAdressData;
import com.freshdirect.ecommerce.data.delivery.AddressRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.DateRangeData;
import com.freshdirect.ecommerce.data.delivery.OneTimeRestrictionData;
import com.freshdirect.ecommerce.data.delivery.OneTimeReverseRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RecurringRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.ecommerce.data.delivery.TimeOfDayData;
import com.freshdirect.ecommerce.data.delivery.TimeOfDayRangeData;
import com.freshdirect.ecommerce.data.dlv.AddressInfoData;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;
import com.freshdirect.payment.service.FDECommerceService;

public class DlvRestrictionManager {
	
	private static DlvRestrictionManagerHome dlvRestrictionHome = null;
	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");
	
	protected static void lookupManagerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			dlvRestrictionHome = (DlvRestrictionManagerHome) ctx.lookup(FDStoreProperties.getDlvRestrictionManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}
	
	public static RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				 return buildRestriction(FDECommerceService.getInstance().getDlvRestriction(restrictionId));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.getDlvRestriction(restrictionId);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	private static RestrictionI buildRestriction(Object dlvRestriction) {
		if (dlvRestriction == null) {
			return null;
		}
		if (dlvRestriction instanceof RecurringRestrictionData) {
			RecurringRestrictionData recurringRestrictionData = (RecurringRestrictionData) dlvRestriction;
			TimeOfDay startDate = new TimeOfDay(recurringRestrictionData.getTimeOfDayRange().getStartDate().getNormalDate());
			TimeOfDay endDate = new TimeOfDay(recurringRestrictionData.getTimeOfDayRange().getEndDate().getNormalDate());
			if (JUST_BEFORE_MIDNIGHT.equals(endDate)) {
				endDate = TimeOfDay.NEXT_MIDNIGHT;
			}
			RecurringRestriction recurringRestriction = new RecurringRestriction(recurringRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(recurringRestrictionData.getCriterion()),
					EnumDlvRestrictionReason.getEnum(recurringRestrictionData.getReason()), recurringRestrictionData.getName(), recurringRestrictionData.getMessage(),  recurringRestrictionData.getDayOfWeek(),  
					startDate, endDate, recurringRestrictionData.getPath());
			return recurringRestriction;
		} else if (dlvRestriction instanceof OneTimeReverseRestrictionData) {
			OneTimeReverseRestrictionData oneTimeReverseRestrictionData = (OneTimeReverseRestrictionData) dlvRestriction;
			OneTimeReverseRestriction oneTimeReverseRestriction = new OneTimeReverseRestriction(oneTimeReverseRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(oneTimeReverseRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(oneTimeReverseRestrictionData.getReason()),oneTimeReverseRestrictionData.getName(), oneTimeReverseRestrictionData.getMessage(),
					oneTimeReverseRestrictionData.getRange().getStartdate(), oneTimeReverseRestrictionData.getRange().getEndDate(), oneTimeReverseRestrictionData.getPath());
			return oneTimeReverseRestriction;
		} else if (dlvRestriction instanceof OneTimeRestrictionData) {
			OneTimeRestrictionData oneTimeRestrictionData = (OneTimeRestrictionData) dlvRestriction;
			OneTimeRestriction oneTimeRestriction = new OneTimeRestriction(oneTimeRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(oneTimeRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(oneTimeRestrictionData.getReason()),oneTimeRestrictionData.getName(), oneTimeRestrictionData.getMessage(),
					oneTimeRestrictionData.getRange().getStartdate(), oneTimeRestrictionData.getRange().getEndDate(), oneTimeRestrictionData.getPath());
			return oneTimeRestriction;

		} else if (dlvRestriction instanceof AlcoholRestrictionData) {
			AlcoholRestrictionData alcoholRestrictionData = (AlcoholRestrictionData) dlvRestriction;
			AlcoholRestriction alcoholRestriction = new AlcoholRestriction(alcoholRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(alcoholRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(alcoholRestrictionData.getReason()),alcoholRestrictionData.getName(), alcoholRestrictionData.getMessage(),
					alcoholRestrictionData.getDateRange().getStartdate(), alcoholRestrictionData.getDateRange().getEndDate(), EnumDlvRestrictionType.getEnum(alcoholRestrictionData.getType()),
					alcoholRestrictionData.getPath(), alcoholRestrictionData.getState(), alcoholRestrictionData.getCounty(),
					alcoholRestrictionData.getCity(), alcoholRestrictionData.getMunicipalityId(), alcoholRestrictionData.isAlcoholRestricted());
			return alcoholRestriction;
			}
		return null;
	}

	public static AlcoholRestriction getAlcoholRestriction(String restrictionId, String municipalityId) throws FDResourceException	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				 return buildAlcoholRestriction(FDECommerceService.getInstance().getAlcoholRestriction(restrictionId, municipalityId));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.getAlcoholRestriction(restrictionId, municipalityId);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	private static AlcoholRestriction buildAlcoholRestriction(AlcoholRestrictionData alcoholRestrictionData) {
		AlcoholRestriction alcoholRestriction = new AlcoholRestriction(alcoholRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(alcoholRestrictionData.getCriterion()), 
				EnumDlvRestrictionReason.getEnum(alcoholRestrictionData.getReason()),alcoholRestrictionData.getName(), alcoholRestrictionData.getMessage(),
				alcoholRestrictionData.getDateRange().getStartdate(), alcoholRestrictionData.getDateRange().getEndDate(), EnumDlvRestrictionType.getEnum(alcoholRestrictionData.getType()),
				alcoholRestrictionData.getPath(), alcoholRestrictionData.getState(), alcoholRestrictionData.getCounty(),
				alcoholRestrictionData.getCity(), alcoholRestrictionData.getMunicipalityId(), alcoholRestrictionData.isAlcoholRestricted());
		alcoholRestriction.setTimeRangeMap(buildTimeRangeMap(alcoholRestrictionData.getTimeRangeMap()));
		return alcoholRestriction;
	}

	private static Map<Integer, List<TimeOfDayRange>> buildTimeRangeMap(
			Map<Integer, List<TimeOfDayRangeData>> timeRangeMap) {
		Map<Integer, List<TimeOfDayRange>> timeRangeMaptemp = new HashMap<Integer, List<TimeOfDayRange>>();
		for (Map.Entry<Integer, List<TimeOfDayRangeData>> pair : timeRangeMap.entrySet()) {
			Integer tempInt = pair.getKey();
			List<TimeOfDayRangeData> tODRangeData = pair.getValue();
			List<TimeOfDayRange> tODRange = new ArrayList<TimeOfDayRange>();
			for(TimeOfDayRangeData data: tODRangeData){
				tODRange.add(new TimeOfDayRange(new TimeOfDay(data.getStartDate().getNormalDate()), new TimeOfDay(data.getEndDate().getNormalDate())));
			}
			timeRangeMaptemp.put(tempInt, tODRange);
		}
		
		return timeRangeMaptemp;
	}

	public static RestrictedAddressModel getAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				 return buildRestrictedAddressModel(FDECommerceService.getInstance().getAddressRestriction(address1, apartment, zipCode));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.getAddressRestriction(address1,apartment,zipCode);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	private static RestrictedAddressModel buildRestrictedAddressModel(RestrictedAddressModelData addressRestriction) {
		RestrictedAddressModel restrictedAddress = new RestrictedAddressModel();
		if(addressRestriction==null)
			return null;
		restrictedAddress.setLastModified(addressRestriction.getLastModified());
		restrictedAddress.setModifiedBy(addressRestriction.getModifiedBy());
		restrictedAddress.setReason(EnumRestrictedAddressReason.getRestrictionReason(addressRestriction.getReason()));
		restrictedAddress.setServiceType(EnumServiceType.getEnum(addressRestriction.getServiceType()));
		restrictedAddress.setCompanyName(addressRestriction.getCompanyName());
		restrictedAddress.setAddress1(addressRestriction.getAddress1());
		restrictedAddress.setAddress2(addressRestriction.getAddress2());
		restrictedAddress.setApartment(addressRestriction.getApartment());
		restrictedAddress.setCity(addressRestriction.getCity());
		restrictedAddress.setState(addressRestriction.getState());
		restrictedAddress.setZipCode(addressRestriction.getZipCode());
		AddressInfo addressinfo = new AddressInfo(addressRestriction.getAddressInfoData().getZoneCode(), addressRestriction.getAddressInfoData().getLongitude(),
				addressRestriction.getAddressInfoData().getLatitude(), addressRestriction.getAddressInfoData().getScrubbedStreet(), EnumAddressType.getEnum(addressRestriction.getAddressInfoData().getAddressType()), addressRestriction.getAddressInfoData().getCounty(),
				addressRestriction.getAddressInfoData().getBuildingId(), addressRestriction.getAddressInfoData().getLocationId());
		addressinfo.setZoneId(addressRestriction.getAddressInfoData().getZoneId());
		addressinfo.setGeocodeException(addressRestriction.getAddressInfoData().isGeocodeException());
		addressinfo.setSsScrubbedAddress(addressRestriction.getAddressInfoData().getSsScrubbedAddress());
		restrictedAddress.setAddressInfo(addressinfo);
		return restrictedAddress;
	}

	public static List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				 return buildRestriction(FDECommerceService.getInstance().getDlvRestrictions(dlvReason, dlvType, dlvCriterion));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.getDlvRestrictions(dlvReason,dlvType,dlvCriterion);
			}
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}
	
//	public static RestrictedAddressModel getAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
//	{
//		if (dlvRestrictionHome == null) {
//			lookupManagerHome();
//		}
//		try {
//			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
//			return sb.getAddressRestriction(address1,apartment,zipCode);
//		} catch (CreateException ce) {
//			dlvRestrictionHome = null;
//			throw new FDResourceException(ce, "Error creating session bean");
//		} catch (RemoteException re) {
//			dlvRestrictionHome = null;
//			throw new FDResourceException(re, "Error talking to session bean");
//		}
//		
//	}

	

	private static List buildRestriction(List dlvRestrictions) {
		List restriction = new ArrayList();
		for (Object object : dlvRestrictions) {
			restriction.add(buildRestriction(object));
		}
		return restriction;
	}


	public static void addDlvRestriction(RestrictionI restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				 FDECommerceService.getInstance().addDlvRestriction(buildRestrictionData(restriction));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.addDlvRestriction(restriction);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static String addAlcoholRestriction(AlcoholRestriction restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				return FDECommerceService.getInstance().addAlcoholRestriction(buildAlcoholRestrictionData(restriction));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.addAlcoholRestriction(restriction);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	
	public static void addAddressRestriction(RestrictedAddressModel restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().addAddressRestriction(buildRestrictedModelData(restriction));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.addAddressRestriction(restriction);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
			
	
	public static void storeDlvRestriction(RestrictionI restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().storeDlvRestriction(buildRestrictionData(restriction));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.storeDlvRestriction(restriction);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	

	private static RestrictionData buildRestrictionData(RestrictionI restriction) {
		if (restriction == null) {
			return null;
		}
		RestrictionData restrictionData = new RestrictionData();
		if (restriction instanceof RecurringRestriction) {
			RecurringRestrictionData recurringRestrictionData = new RecurringRestrictionData();
			buildAbstractRestrictionData(recurringRestrictionData, restriction);
			RecurringRestriction recurringRestriction = (RecurringRestriction) restriction;
			recurringRestrictionData.setDayOfWeek(recurringRestriction.getDayOfWeek());
			TimeOfDayRangeData timeOfDayRangeData = new TimeOfDayRangeData();
			TimeOfDayData endDate = new TimeOfDayData();
			endDate.setNormalDate(recurringRestriction.getTimeRange().getEndTime().getNormalDate());
			timeOfDayRangeData.setEndDate(endDate);
			TimeOfDayData startDate = new TimeOfDayData();
			startDate.setNormalDate(recurringRestriction.getTimeRange().getStartTime().getNormalDate());
			timeOfDayRangeData.setStartDate(startDate);
			recurringRestrictionData.setTimeOfDayRange(timeOfDayRangeData);
			restrictionData.setClassType(RecurringRestrictionData.class.getName());
			restrictionData.setRecurringRestrictionData(recurringRestrictionData);
		} else if (restriction instanceof OneTimeReverseRestriction) {
			OneTimeReverseRestrictionData oneTimeReverseRestrictionData = new OneTimeReverseRestrictionData();
			buildAbstractRestrictionData(oneTimeReverseRestrictionData, restriction);
			OneTimeReverseRestriction oneTimeReverseRestriction = (OneTimeReverseRestriction) restriction;
			DateRangeData range = new DateRangeData();
			range.setEndDate(oneTimeReverseRestriction.getDateRange().getEndDate());
			range.setStartdate(oneTimeReverseRestriction.getDateRange().getStartDate());
			oneTimeReverseRestrictionData.setRange(range);
			restrictionData.setClassType(OneTimeReverseRestrictionData.class.getName());
			restrictionData.setOneTimeReverseRestrictionData(oneTimeReverseRestrictionData);
		} else if (restriction instanceof OneTimeRestriction) {
			OneTimeRestrictionData oneTimeRestrictionData = new OneTimeRestrictionData();
			buildAbstractRestrictionData(oneTimeRestrictionData, restriction);
			OneTimeRestriction oneTimeRestriction = (OneTimeRestriction) restriction;
			DateRangeData range = new DateRangeData();
			range.setEndDate(oneTimeRestriction.getDateRange().getEndDate());
			range.setStartdate(oneTimeRestriction.getDateRange().getStartDate());
			oneTimeRestrictionData.setRange(range);
			restrictionData.setClassType(OneTimeRestrictionData.class.getName());
			restrictionData.setOneTimeRestrictionData(oneTimeRestrictionData);

		} else if (restriction instanceof AlcoholRestriction) {
			AlcoholRestrictionData alcoholRestrictionData = new AlcoholRestrictionData();
			buildAbstractRestrictionData(alcoholRestrictionData, restriction);
			AlcoholRestriction alcoholRestriction = (AlcoholRestriction) restriction;
			DateRangeData range = new DateRangeData();
			range.setEndDate(alcoholRestriction.getDateRange().getEndDate());
			range.setStartdate(alcoholRestriction.getDateRange().getStartDate());
			alcoholRestrictionData.setDateRange(range);
			alcoholRestrictionData.setType(alcoholRestriction.getType().getName());
			alcoholRestrictionData.setState(alcoholRestriction.getState());
			alcoholRestrictionData.setCounty(alcoholRestriction.getCounty());
			alcoholRestrictionData.setCity(alcoholRestriction.getCity());
			alcoholRestrictionData.setMunicipalityId(alcoholRestriction.getMunicipalityId());
			alcoholRestrictionData.setAlcoholRestricted(alcoholRestriction.isAlcoholRestricted());
			Map<Integer, List<TimeOfDayRangeData>> timeRangeMapData = new HashMap<Integer, List<TimeOfDayRangeData>>();
			Map<Integer, List<TimeOfDayRange>> timeRangeMap = alcoholRestriction.getTimeRangeMap();
			for (Integer key : timeRangeMap.keySet()) {
				List<TimeOfDayRangeData> timeOfDayRangeDataList = new ArrayList<TimeOfDayRangeData>();
				List<TimeOfDayRange> timeOfDayRangeList = timeRangeMap.get(key);
				for (TimeOfDayRange timeOfDayRange : timeOfDayRangeList) {
					TimeOfDayRangeData timeOfDayRangeData = new TimeOfDayRangeData();
					TimeOfDayData startDate = new TimeOfDayData();
					startDate.setNormalDate(timeOfDayRange.getStartTime().getNormalDate());
					TimeOfDayData endDate = new TimeOfDayData();
					endDate.setNormalDate(timeOfDayRange.getEndTime().getNormalDate());
					timeOfDayRangeData.setEndDate(endDate);
					timeOfDayRangeData.setStartDate(startDate);
					timeOfDayRangeDataList.add(timeOfDayRangeData);
				}
				timeRangeMapData.put(key, timeOfDayRangeDataList);
				
			}
			 restrictionData.setAlcoholRestrictionData(alcoholRestrictionData);
			 restrictionData.setClassType(AlcoholRestriction.class.getName());
		}
		return restrictionData;
	}
	private static AbstractRestrictionData buildAbstractRestrictionData(AbstractRestrictionData abstractRestrictionData, RestrictionI source) {
		abstractRestrictionData.setCriterion(source.getCriterion().getName());
		abstractRestrictionData.setMessage(source.getMessage());
		abstractRestrictionData.setName(source.getName());
		abstractRestrictionData.setPath(source.getPath());
		abstractRestrictionData.setReason(source.getReason().getName());
		abstractRestrictionData.setId(source.getId());
		return abstractRestrictionData;
	}

	public static void storeAlcoholRestriction(AlcoholRestriction restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().storeAlcoholRestriction(buildAlcoholRestrictionData(restriction));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.storeAlcoholRestriction(restriction);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	
	private static AlcoholRestrictionData buildAlcoholRestrictionData(AlcoholRestriction restriction) {
		AlcoholRestrictionData alcoholRestrictionData = new AlcoholRestrictionData();
		buildAbstractRestrictionData(alcoholRestrictionData, restriction);
		AlcoholRestriction alcoholRestriction = (AlcoholRestriction) restriction;
		DateRangeData range = new DateRangeData();
		range.setEndDate(alcoholRestriction.getDateRange().getEndDate());
		range.setStartdate(alcoholRestriction.getDateRange().getStartDate());
		alcoholRestrictionData.setDateRange(range);
		alcoholRestrictionData.setType(alcoholRestriction.getType().getName());
		alcoholRestrictionData.setState(alcoholRestriction.getState());
		alcoholRestrictionData.setCounty(alcoholRestriction.getCounty());
		alcoholRestrictionData.setCity(alcoholRestriction.getCity());
		alcoholRestrictionData.setMunicipalityId(alcoholRestriction.getMunicipalityId());
		alcoholRestrictionData.setAlcoholRestricted(alcoholRestriction.isAlcoholRestricted());
		Map<Integer, List<TimeOfDayRangeData>> timeRangeMapData = new HashMap<Integer, List<TimeOfDayRangeData>>();
		Map<Integer, List<TimeOfDayRange>> timeRangeMap = alcoholRestriction.getTimeRangeMap();
		for (Integer key : timeRangeMap.keySet()) {
			List<TimeOfDayRangeData> timeOfDayRangeDataList = new ArrayList<TimeOfDayRangeData>();
			List<TimeOfDayRange> timeOfDayRangeList = timeRangeMap.get(key);
			for (TimeOfDayRange timeOfDayRange : timeOfDayRangeList) {
				TimeOfDayRangeData timeOfDayRangeData = new TimeOfDayRangeData();
				TimeOfDayData startDate = new TimeOfDayData();
				startDate.setNormalDate(timeOfDayRange.getStartTime().getNormalDate());
				TimeOfDayData endDate = new TimeOfDayData();
				endDate.setNormalDate(timeOfDayRange.getEndTime().getNormalDate());
				timeOfDayRangeData.setEndDate(endDate);
				timeOfDayRangeData.setStartDate(startDate);
				timeOfDayRangeDataList.add(timeOfDayRangeData);
			}
			timeRangeMapData.put(key, timeOfDayRangeDataList);
		}
		return alcoholRestrictionData;
	}

	public static void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().storeAddressRestriction(buildRestrictedAndAddressModelData(restriction,address1,apartment,zipCode));
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.storeAddressRestriction(restriction,address1,apartment,zipCode);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	
	private static AddressAndRestrictedAdressData buildRestrictedAndAddressModelData(RestrictedAddressModel restriction, String address1,
			String apartment, String zipCode) {
		AddressAndRestrictedAdressData request = new AddressAndRestrictedAdressData();
		request.setRestrictedAddressModelData(buildRestrictedModelData(restriction));
		AddressRestrictionData addressRestrictionData = new AddressRestrictionData();
		addressRestrictionData.setAddress(address1);
		addressRestrictionData.setApartment(apartment);
		addressRestrictionData.setZipcode(zipCode);
		request.setAddressRestrictionData(addressRestrictionData);
		return request;
	}


	private static RestrictedAddressModelData buildRestrictedModelData(RestrictedAddressModel restriction) {
		RestrictedAddressModelData restrictedAddressData = new RestrictedAddressModelData();
		restrictedAddressData.setLastModified(restriction.getLastModified());
		restrictedAddressData.setModifiedBy(restriction.getModifiedBy());
		restrictedAddressData.setReason(restriction.getReason().getCode());
		restrictedAddressData.setServiceType(restriction.getServiceType().getName());
		restrictedAddressData.setCompanyName(restriction.getCompanyName());
		restrictedAddressData.setAddress1(restriction.getAddress1());
		restrictedAddressData.setAddress2(restriction.getAddress2());
		restrictedAddressData.setApartment(restriction.getApartment());
		restrictedAddressData.setCity(restriction.getCity());
		restrictedAddressData.setState(restriction.getState());
		restrictedAddressData.setZipCode(restriction.getZipCode());
		AddressInfoData addressInfoData = new AddressInfoData();
		if(restriction.getAddressInfo() != null){
			addressInfoData.setZoneId(restriction.getAddressInfo().getZoneId());
			addressInfoData.setZoneCode(restriction.getAddressInfo().getZoneCode());
			addressInfoData.setLongitude(restriction.getAddressInfo().getLongitude());
			addressInfoData.setLatitude(restriction.getAddressInfo().getLatitude());
			addressInfoData.setScrubbedStreet(restriction.getAddressInfo().getScrubbedStreet());
			addressInfoData.setAddressType(restriction.getAddressInfo().getAddressType()==null?null:restriction.getAddressInfo().getAddressType().getName());
			addressInfoData.setCounty(restriction.getAddressInfo().getCounty());
			addressInfoData.setGeocodeException(restriction.getAddressInfo().isGeocodeException());
			addressInfoData.setBuildingId(restriction.getAddressInfo().getBuildingId());
			addressInfoData.setLocationId(restriction.getAddressInfo().getLocationId());
			addressInfoData.setSsScrubbedAddress(restriction.getAddressInfo().getSsScrubbedAddress());
			}
		restrictedAddressData.setAddressInfoData(addressInfoData);
		return restrictedAddressData;
	}

	public static void deleteDlvRestriction(String restrictionId) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().deleteDlvRestriction(restrictionId);
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.deleteDlvRestriction(restrictionId);
			}
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void deleteAlcoholRestriction(String restrictionId) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().deleteAlcoholRestriction(restrictionId);
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.deleteAlcoholRestriction(restrictionId);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void deleteAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().deleteAddressRestriction(address1, apartment, zipCode);
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.deleteAddressRestriction(address1,apartment,zipCode);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void setAlcoholRestrictedFlag(String municipalityId, boolean restricted)  throws FDResourceException {
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				FDECommerceService.getInstance().setAlcoholRestrictedFlag(municipalityId, restricted);
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				sb.setAlcoholRestrictedFlag(municipalityId, restricted);
			}
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}		
	}
	
	public static Map<String, List<String>> getMunicipalityStateCounties()throws FDResourceException {
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvRestrictionManagerSB")){
				return FDECommerceService.getInstance().getMunicipalityStateCounties();
			}
			else{
				DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
				return sb.getMunicipalityStateCounties();
			}
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
