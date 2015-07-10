package com.freshdirect.webapp.ajax.expresscheckout.location.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.DeliveryLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.LocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.OpeningHourConstants;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.PickupLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationConstants;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationDataService;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class DeliveryAddressService {

	private static final String DEFAULT_DELIVERY_ADDRESS_ID = "addressId";
	private static final String HAMPTON_DEPOT_CODE = "HAM";
	private static final String PICKUP_DELIVERY_POPUP = "/delivery_popup.jsp?depotCode=%s&locaId=%s";

	private static Category LOGGER = LoggerFactory.getInstance(DeliveryAddressService.class);

	private static final DeliveryAddressService INSTANCE = new DeliveryAddressService();

	private DeliveryAddressService() {
	}

	public static DeliveryAddressService defaultService() {
		return INSTANCE;
	}

	public List<ValidationError> addDeliveryAddressMethod(FormDataRequest deliveryAddressRequest, HttpSession session, FDUserI user) throws FDResourceException {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(DeliveryAddressValidationDataService.defaultService().prepareAndValidate(deliveryAddressRequest));
		if (validationErrors.isEmpty()) {
			AddressModel addressModel = parseDeliveryAddressForm(deliveryAddressRequest);
			ErpAddressModel erpAddressModel = parseErpDeliveryAddressForm(deliveryAddressRequest);
			String action = FormDataService.defaultService().get(deliveryAddressRequest, "action");
			ActionResult actionResult = new ActionResult();
			DeliveryAddressManipulator.performAddAndSetDeliveryAddress((FDSessionUser) user, actionResult, session, user.getShoppingCart(), action, erpAddressModel, addressModel);
			processErrors(validationErrors, actionResult);
		}
		return validationErrors;
	}

	public List<ValidationError> editDeliveryAddressMethod(FormDataRequest deliveryAddressRequest, HttpSession session, FDUserI user) throws FDResourceException {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(DeliveryAddressValidationDataService.defaultService().prepareAndValidate(deliveryAddressRequest));
		if (validationErrors.isEmpty()) {
			AddressModel addressModel = parseDeliveryAddressForm(deliveryAddressRequest);
			ErpAddressModel erpAddressModel = parseErpDeliveryAddressForm(deliveryAddressRequest);
			String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
			String action = FormDataService.defaultService().get(deliveryAddressRequest, "action");
			ActionResult actionResult = new ActionResult();
			TimeslotEventModel event = TimeslotService.defaultService().createTimeslotEventModel(user);
			DeliveryAddressManipulator.performEditDeliveryAddress(event, (FDSessionUser) user, actionResult, session, user.getShoppingCart(), action, erpAddressModel, addressModel, deliveryAddressId);
			processErrors(validationErrors, actionResult);
		}
		return validationErrors;
	}

	public void deleteDeliveryAddressMethod(FormDataRequest deliveryAddressRequest, HttpSession session, FDUserI user) throws FDResourceException {
		String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
		ActionResult actionResult = new ActionResult();
		TimeslotEventModel event = TimeslotService.defaultService().createTimeslotEventModel(user);
		DeliveryAddressManipulator.performDeleteDeliveryAddress(user, session, deliveryAddressId, actionResult, event);
	}

	public List<ValidationError> selectDeliveryAddressMethod(String deliveryAddressId, String contactNumber, String actionName, HttpSession session, FDUserI user) throws FDResourceException,
			JspException, RedirectToPage {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		ErpAddressModel deliveryAddress = user.getShoppingCart().getDeliveryAddress();
		if (deliveryAddress == null || deliveryAddress.getId() == null || user.getShoppingCart().getZoneInfo() == null || !deliveryAddress.getId().equals(deliveryAddressId)) {
			ActionResult actionResult = new ActionResult();
			DeliveryAddressManipulator.performSetDeliveryAddress(session, user, deliveryAddressId, contactNumber, null, actionName, true, actionResult, null, null, null, null, null, null);
			TimeslotService.defaultService().releaseTimeslot(user);
			processErrors(validationErrors, actionResult);
		}
		return validationErrors;
	}

	private void processErrors(List<ValidationError> validationErrors, ActionResult actionResult) {
		for (ActionError error : actionResult.getErrors()) {
			validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
		}
	}

	public List<LocationData> loadDeliveryAddressById(FDUserI user, String addressId) throws FDResourceException {
		final List<LocationData> locationDatas = new ArrayList<LocationData>();

		DlvDepotModel depotModel = FDDepotManager.getInstance().getDepotByLocationId(addressId);
		if (depotModel != null) {
			DlvLocationModel locationModel = depotModel.getLocation(addressId);

			if (depotModel.isPickup()) {
				final ErpCustomerInfoModel customerInfoModel = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());
				locationDatas.add(convertPickupLocationModelToLocationData(locationModel, depotModel, customerInfoModel));
			} else {
				locationDatas.add(convertDepotLocationModelToLocationData(locationModel));
			}
		} else {
			ErpAddressModel shippingAddress = FDCustomerManager.getAddress(user.getIdentity(), addressId);
			locationDatas.add(convertDeliveryAddressModelToLocationData(addressId, shippingAddress, shippingAddress.getServiceType(), isDeliveryZoneUnattended(shippingAddress)));
		}

		return locationDatas;
	}

	public List<LocationData> loadDeliveryAddress(final FDUserI user, HttpSession session) throws FDResourceException, JspException, RedirectToPage {
		List<LocationData> addresses = new ArrayList<LocationData>();

		if (EnumCheckoutMode.NORMAL.equals(user.getCheckoutMode())) {

			String selectedDeliveryAddressId = null;
			if ((user.isDepotUser() || user.isPickupUser())) {
				if (user.getShoppingCart().getDeliveryAddress() == null) {
					selectedDeliveryAddressId = FDCustomerManager.getDefaultDepotLocationPK(user.getIdentity());
					if (selectedDeliveryAddressId != null) {
						selectDeliveryAddressMethod(selectedDeliveryAddressId, "", "selectDeliveryAddressMethod", session, user);
					}
				} else {
					ErpAddressModel deliveryAddress = user.getShoppingCart().getDeliveryAddress();
					if (deliveryAddress instanceof ErpDepotAddressModel) {
						selectedDeliveryAddressId = user.getShoppingCart().getDeliveryAddress().getLocationId();
						selectDeliveryAddressMethod(selectedDeliveryAddressId, "", "selectDeliveryAddressMethod", session, user);
					}
				}
			}

			if (selectedDeliveryAddressId == null) {
				if (user.getShoppingCart().getDeliveryAddress() == null) {
					selectedDeliveryAddressId = FDCustomerManager.getDefaultShipToAddressPK(user.getIdentity());
					if (selectedDeliveryAddressId != null) {
						selectDeliveryAddressMethod(selectedDeliveryAddressId, "", "selectDeliveryAddressMethod", session, user);
					}
				} else {
					PrimaryKey deliveryAddressMethodPrimaryKey = user.getShoppingCart().getDeliveryAddress().getPK();
					if (deliveryAddressMethodPrimaryKey != null) {
						selectedDeliveryAddressId = deliveryAddressMethodPrimaryKey.getId();
					}
				}
			}

			if (user.isDepotUser()) {
				for (Object deliveryLocationModel : FDDepotManager.getInstance().getDepot(user.getDepotCode()).getLocations()) {
					if (deliveryLocationModel instanceof DlvLocationModel) {
						final DlvLocationModel pickupDeliveryLocationModel = (DlvLocationModel) deliveryLocationModel;
						LocationData depoAddress = convertDepotLocationModelToLocationData(pickupDeliveryLocationModel);
						if (depoAddress.getId().equals(selectedDeliveryAddressId)) {
							depoAddress.setSelected(true);
						}
						addresses.add(depoAddress);
					}
				}
			}

			List<ErpAddressModel> shippingAddresses = new ArrayList<ErpAddressModel>(FDCustomerManager.getShipToAddresses(user.getIdentity()));
			sortDeliveryAddress(user, shippingAddresses);

			for (ErpAddressModel shippingAddress : shippingAddresses) {
				EnumServiceType serviceType = shippingAddress.getServiceType();
				if ((EnumServiceType.HOME.equals(serviceType) || EnumServiceType.CORPORATE.equals(serviceType))) {
					String deliveryAddressId = NVL.apply(shippingAddress.getId(), DEFAULT_DELIVERY_ADDRESS_ID);
					LocationData deliveryAddress = convertDeliveryAddressModelToLocationData(deliveryAddressId, shippingAddress, serviceType, isDeliveryZoneUnattended(shippingAddress));
					if (deliveryAddress.getId().equals(selectedDeliveryAddressId)) {
						deliveryAddress.setSelected(true);
					}
					addresses.add(deliveryAddress);
				}
			}

			if (addresses.size() < 2) {
				disableDeleteActionOnAddresses(addresses);
			}

			if (selectedDeliveryAddressId == null && !addresses.isEmpty()) {
				selectDeliveryAddressMethod(addresses.get(0).getId(), "", "selectDeliveryAddressMethod", session, user);
				addresses.get(0).setSelected(true);
			}

			if (user.isPickupUser()) {
				final ErpCustomerInfoModel customerInfoModel = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());
				for (final DlvDepotModel pickupDeliveryDepotModel : loadFilteredPickupDepotModel()) {
					for (Object deliveryLocationModel : pickupDeliveryDepotModel.getLocations()) {
						if (deliveryLocationModel instanceof DlvLocationModel) {
							final DlvLocationModel pickupDeliveryLocationModel = (DlvLocationModel) deliveryLocationModel;
							PickupLocationData pickupAddress = convertPickupLocationModelToLocationData(pickupDeliveryLocationModel, pickupDeliveryDepotModel, customerInfoModel);
							if (pickupAddress.getId().equals(selectedDeliveryAddressId)) {
								pickupAddress.setSelected(true);
							}
							addresses.add(pickupAddress);
						}
					}
				}
			}

		}

		return addresses;
	}

	private void disableDeleteActionOnAddresses(List<LocationData> addresses) {
		for (LocationData locationData : addresses) {
			locationData.setCanDelete(false);
		}
	}

	public List<LocationData> loadSuccessLocations(FDCartI cart, FDUserI user) throws FDResourceException {
		final List<LocationData> depotLocationDatas = new ArrayList<LocationData>();
		FDIdentity identity = user.getIdentity();
		ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
		ErpCustomerInfoModel customerInfoModel = FDCustomerFactory.getErpCustomerInfo(identity);

		if (deliveryAddress instanceof ErpDepotAddressModel) {
			ErpDepotAddressModel depotAddress = (ErpDepotAddressModel) deliveryAddress;
			if (depotAddress.isPickup()) {
				DlvDepotModel depotModel = com.freshdirect.fdstore.FDDepotManager.getInstance().getDepotByLocationId(depotAddress.getLocationId());
				if (depotModel != null) {
					for (Object deliveryLocationModel : depotModel.getLocations()) {
						if (deliveryLocationModel instanceof DlvLocationModel) {
							final DlvLocationModel pickupDeliveryLocationModel = (DlvLocationModel) deliveryLocationModel;
							LocationData deliveryLocationData = convertPickupLocationModelToLocationData(pickupDeliveryLocationModel, depotModel, customerInfoModel);
							deliveryLocationData.setSelected(true);
							depotLocationDatas.add(deliveryLocationData);
							break;
						}
					}
				}
			}
		} else {
			EnumServiceType selectedServiceType = user.getSelectedServiceType();
			if (EnumServiceType.HOME.equals(selectedServiceType) || EnumServiceType.CORPORATE.equals(selectedServiceType)) {
				String deliveryAddressId = NVL.apply(deliveryAddress.getId(), DEFAULT_DELIVERY_ADDRESS_ID);
				LocationData deliveryLocationData = convertDeliveryAddressModelToLocationData(deliveryAddressId, deliveryAddress, selectedServiceType, isDeliveryAddressUnattended(deliveryAddress));
				deliveryLocationData.setSelected(true);
				depotLocationDatas.add(deliveryLocationData);
			}
		}

		return depotLocationDatas;
	}

	public void sortDeliveryAddress(FDUserI user, List<ErpAddressModel> deliveryAddressMethods) throws FDResourceException {
		FDOrderI lastOrder = FDCustomerManager.getLastOrder(user.getIdentity());
		ErpAddressModel lastUsedOrderAddress = null;
		if (lastOrder != null) {
			lastUsedOrderAddress = lastOrder.getDeliveryAddress();
			if (deliveryAddressMethods.contains(lastUsedOrderAddress)) {
				deliveryAddressMethods.remove(lastUsedOrderAddress);
			} else {
				lastUsedOrderAddress = null;
			}
		}
		Collections.sort(deliveryAddressMethods, DELIVERY_ADDRESS_COMPARATOR_BY_ID_REVERSED);
		if (lastUsedOrderAddress != null) {
			deliveryAddressMethods.add(0, lastUsedOrderAddress);
		}
	}

	private static final Comparator<ErpAddressModel> DELIVERY_ADDRESS_COMPARATOR_BY_ID = new Comparator<ErpAddressModel>() {

		@Override
		public int compare(ErpAddressModel o1, ErpAddressModel o2) {
			Long id1 = Long.parseLong(o1.getPK().getId());
			Long id2 = Long.parseLong(o2.getPK().getId());
			return id1.compareTo(id2);
		}

	};

	private static final Comparator<ErpAddressModel> DELIVERY_ADDRESS_COMPARATOR_BY_ID_REVERSED = ComparatorChain.<ErpAddressModel> reverseOrder(ComparatorChain
			.create(DELIVERY_ADDRESS_COMPARATOR_BY_ID));

	private boolean isDeliveryZoneUnattended(ErpAddressModel deliveryAddress) throws FDResourceException {
		boolean isUnatteded = false;

		try {
			DlvZoneInfoModel deliveryZoneInfo = FDDeliveryManager.getInstance().getZoneInfo(deliveryAddress, new Date(), null, null);
			isUnatteded = deliveryZoneInfo.isUnattended() && isDeliveryAddressUnattended(deliveryAddress);
		} catch (FDInvalidAddressException e) {
			LOGGER.error("Can not find zone info of delivery address.", e);
		}
		return isUnatteded;
	}

	private boolean isDeliveryAddressUnattended(ErpAddressModel deliveryAddress) {
		return EnumUnattendedDeliveryFlag.OPT_IN.equals(deliveryAddress.getUnattendedDeliveryFlag());
	}

	private LocationData convertDeliveryAddressModelToLocationData(String deliveryAddressId, ErpAddressModel deliveryAddress, EnumServiceType serviceType, boolean isBackupDeliveryUnattended) {
		final DeliveryLocationData deliveryLocationData = new DeliveryLocationData();
		deliveryLocationData.setId(deliveryAddressId);
		deliveryLocationData.setSelected(false);
		deliveryLocationData.setServiceType(serviceType.toString().toLowerCase());
		deliveryLocationData.setFirstName(deliveryAddress.getFirstName());
		deliveryLocationData.setLastName(deliveryAddress.getLastName());
		deliveryLocationData.setAddress1(deliveryAddress.getAddress1());
		deliveryLocationData.setAddress2(deliveryAddress.getAddress2());
		deliveryLocationData.setApartment(deliveryAddress.getApartment());
		deliveryLocationData.setCity(deliveryAddress.getCity());
		deliveryLocationData.setState(deliveryAddress.getState());
		deliveryLocationData.setZip(deliveryAddress.getZipCode());

		if (EnumServiceType.HOME.equals(serviceType)) {
			deliveryLocationData.setNickName(deliveryAddress.getCompanyName());
		} else {
			deliveryLocationData.setCompanyName(deliveryAddress.getCompanyName());
		}

		if (deliveryAddress.getPhone() != null && !"".equals(deliveryAddress.getPhone())) {
			deliveryLocationData.setPhone(deliveryAddress.getPhone().getPhone());
			deliveryLocationData.setPhoneExtension(deliveryAddress.getPhone().getExtension());
			deliveryLocationData.setPhoneType(deliveryAddress.getPhone().getType());
		}

		if (deliveryAddress.getAltContactPhone() != null && !"".equals(deliveryAddress.getAltContactPhone())) {
			deliveryLocationData.setAlternativePhone(deliveryAddress.getAltContactPhone().getPhone());
			deliveryLocationData.setAlternativePhoneExtension(deliveryAddress.getAltContactPhone().getExtension());
			deliveryLocationData.setAlternativePhoneType(deliveryAddress.getAltContactPhone().getType());
		}

		if (deliveryAddress.getInstructions() != null && !"".equals(deliveryAddress.getInstructions()) && !"NONE".equalsIgnoreCase(deliveryAddress.getInstructions())) {
			deliveryLocationData.setInstructions(deliveryAddress.getInstructions());
		}

		final EnumDeliverySetting backupDelivery = deliveryAddress.getAltDelivery();
		if (backupDelivery != null && backupDelivery.getId() > 0) {
			if (EnumDeliverySetting.NONE.equals(backupDelivery)) {
				deliveryLocationData.setBackupDeliveryAuthenticate(DeliveryAddressValidationConstants.BACKUP_DELIVERY_NONE);
			} else if (EnumDeliverySetting.DOORMAN.equals(backupDelivery)) {
				deliveryLocationData.setBackupDeliveryAuthenticate(DeliveryAddressValidationConstants.BACKUP_DELIVERY_DOORMAN);
			} else if (EnumDeliverySetting.NEIGHBOR.equals(backupDelivery)) {
				deliveryLocationData.setBackupDeliveryAuthenticate(DeliveryAddressValidationConstants.BACKUP_DELIVERY_NEIGHBOR);
				deliveryLocationData.setBackupDeliveryFirstName(deliveryAddress.getAltFirstName());
				deliveryLocationData.setBackupDeliveryLastName(deliveryAddress.getAltLastName());
				if (deliveryAddress.getAlternateAddress() != null) {
					deliveryLocationData.setBackupDeliveryAddress(deliveryAddress.getAlternateAddress().getAddress1());
					deliveryLocationData.setBackupDeliveryApartment(deliveryAddress.getAlternateAddress().getApartment());
				}
				if (deliveryAddress.getAltPhone() != null) {
					deliveryLocationData.setBackupDeliveryPhone(deliveryAddress.getAltPhone().getPhone());
					deliveryLocationData.setBackupDeliveryPhoneExtension(deliveryAddress.getAltPhone().getExtension());
				}
			}
		}

		if (isBackupDeliveryUnattended) {
			deliveryLocationData.setBackupDeliveryAuthenticate(DeliveryAddressValidationConstants.BACKUP_DELIVERY_UNATTANDED);
			deliveryLocationData.setBackupDeliveryInstructions(deliveryAddress.getUnattendedDeliveryInstructions());
		}

		return deliveryLocationData;
	}

	private PickupLocationData convertPickupLocationModelToLocationData(DlvLocationModel pickupLocationModel, DlvDepotModel deliveryDepotModel, ErpCustomerInfoModel customerInfoModel) {
		final PickupLocationData pickupDepotLocationData = new PickupLocationData();
		String addressId = pickupLocationModel.getId();
		pickupDepotLocationData.setId(addressId);
		pickupDepotLocationData.setServiceType(EnumServiceType.PICKUP.toString().toLowerCase());
		pickupDepotLocationData.setSelected(false);
		pickupDepotLocationData.setName(deliveryDepotModel.getName());
		pickupDepotLocationData.setAddress1(pickupLocationModel.getAddress().getAddress1());
		pickupDepotLocationData.setAddress2(pickupLocationModel.getAddress().getAddress2());
		pickupDepotLocationData.setCity(pickupLocationModel.getAddress().getCity());
		pickupDepotLocationData.setState(pickupLocationModel.getAddress().getState());
		pickupDepotLocationData.setZip(pickupLocationModel.getAddress().getZipCode());
		pickupDepotLocationData.setPopupUrl(preparePickupPopupUrl(deliveryDepotModel.getDepotCode(), addressId));
		pickupDepotLocationData.setOpeningHours(OpeningHourConstants.getOpeningHours());

		if (customerInfoModel.getOtherPhone() != null && !"".equals(customerInfoModel.getOtherPhone())) {
			pickupDepotLocationData.setPhone(customerInfoModel.getOtherPhone().getPhone());
		}

		return pickupDepotLocationData;
	}

	private LocationData convertDepotLocationModelToLocationData(DlvLocationModel deliveryLocationModel) {
		final LocationData depotLocationData = new LocationData();
		depotLocationData.setId(deliveryLocationModel.getId());
		depotLocationData.setSelected(false);
		depotLocationData.setServiceType(EnumServiceType.DEPOT.toString().toLowerCase());
		depotLocationData.setName(deliveryLocationModel.getFacility());
		depotLocationData.setAddress1(deliveryLocationModel.getAddress().getAddress1());
		depotLocationData.setAddress2(deliveryLocationModel.getAddress().getAddress2());
		depotLocationData.setApartment(deliveryLocationModel.getAddress().getApartment());
		depotLocationData.setCity(deliveryLocationModel.getAddress().getCity());
		depotLocationData.setState(deliveryLocationModel.getAddress().getState());
		depotLocationData.setZip(deliveryLocationModel.getAddress().getZipCode());
		depotLocationData.setInstructions(deliveryLocationModel.getInstructions());
		return depotLocationData;
	}

	private String preparePickupPopupUrl(String depotCode, String addressId) {
		return String.format(PICKUP_DELIVERY_POPUP, depotCode, addressId);
	}

	private List<DlvDepotModel> loadFilteredPickupDepotModel() throws FDResourceException {
		final List<DlvDepotModel> pickupDepotModels = new ArrayList<DlvDepotModel>();
		for (Object pickupDepotObj : FDDepotManager.getInstance().getPickupDepots()) {
			final DlvDepotModel pickupDepotModel = (DlvDepotModel) pickupDepotObj;
			if (!(HAMPTON_DEPOT_CODE.equalsIgnoreCase(pickupDepotModel.getDepotCode()) || pickupDepotModel.isDeactivated()))
				pickupDepotModels.add(pickupDepotModel);
		}
		return pickupDepotModels;
	}

	private AddressModel parseDeliveryAddressForm(FormDataRequest addressRequestData) {
		AddressModel deliveryAddressModel = new AddressModel();
		return decorateAddressModel(deliveryAddressModel, addressRequestData);
	}

	private ErpAddressModel parseErpDeliveryAddressForm(FormDataRequest addressRequestData) {
		Map<String, String> formData = FormDataService.defaultService().getSimpleMap(addressRequestData);

		ErpAddressModel erpAddressModel = new ErpAddressModel();

		decorateAddressModel(erpAddressModel, addressRequestData);

		erpAddressModel.setFirstName(formData.get(DeliveryAddressValidationConstants.FIRST_NAME));
		erpAddressModel.setLastName(formData.get(DeliveryAddressValidationConstants.LAST_NAME));

		String phone = formData.get(DeliveryAddressValidationConstants.PHONE);
		String phoneExt = formData.get(DeliveryAddressValidationConstants.PHONE_EXTENSION);
		String phoneType = formData.get(DeliveryAddressValidationConstants.PHONE_TYPE);
		erpAddressModel.setPhone(new PhoneNumber(phone, phoneExt, phoneType));

		String alternativePhone = formData.get(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE);
		String alternativePhoneExt = formData.get(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE_EXTENSION);
		String alternativePhoneType = formData.get(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE_TYPE);
		erpAddressModel.setAltContactPhone(new PhoneNumber(alternativePhone, alternativePhoneExt, alternativePhoneType));

		erpAddressModel.setInstructions(formData.get(DeliveryAddressValidationConstants.INSTRUCTIONS));

		String backupDeliverySetting = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION);
		String backupDeliveryLastName = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_LAST_NAME);
		String backupDeliveryFirstName = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_FIRST_NAME);
		String backupDeliveryPhone = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_PHONE);
		String backupDeliveryExt = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_PHONE_EXTENSION);
		String backupDeliveryApartment = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_ADDRESS);

		erpAddressModel.setAltDelivery(EnumDeliverySetting.getDeliverySetting(backupDeliverySetting));

		if (EnumDeliverySetting.NEIGHBOR.getDeliveryCode().equalsIgnoreCase(backupDeliverySetting)) {
			if ((backupDeliveryFirstName != null) && (!backupDeliveryFirstName.equals(""))) {
				erpAddressModel.setAltFirstName(backupDeliveryFirstName);
			}
			if ((backupDeliveryLastName != null) && (!backupDeliveryLastName.equals(""))) {
				erpAddressModel.setAltLastName(backupDeliveryLastName);
			}
			if ((backupDeliveryApartment != null) && (!backupDeliveryApartment.equals(""))) {
				erpAddressModel.setAltApartment(backupDeliveryApartment);
			}
			if ((backupDeliveryPhone != null) && (!backupDeliveryPhone.equals(""))) {
				erpAddressModel.setAltPhone(new PhoneNumber(backupDeliveryPhone, backupDeliveryExt));
			}
		}

		String unattendedDeliveryInstructions = "";
		EnumUnattendedDeliveryFlag unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.NOT_SEEN;
		if (DeliveryAddressValidationConstants.BACKUP_DELIVERY_UNATTANDED.equalsIgnoreCase(backupDeliverySetting)) {
			unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.OPT_IN;
			unattendedDeliveryInstructions = formData.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_INSTRUCTIONS);
		}
		erpAddressModel.setUnattendedDeliveryFlag(unattendedDeliveryFlag);
		erpAddressModel.setUnattendedDeliveryInstructions(unattendedDeliveryInstructions);

		return erpAddressModel;
	}

	private AddressModel decorateAddressModel(AddressModel addressModel, FormDataRequest addressRequestData) {
		Map<String, String> formData = FormDataService.defaultService().getSimpleMap(addressRequestData);

		if (DeliveryAddressValidationDataService.ADD_ADDRESS_HOME.equals(addressRequestData.getFormId())) {
			addressModel.setCompanyName(formData.get(DeliveryAddressValidationConstants.NICK_NAME));
		} else {
			addressModel.setCompanyName(formData.get(DeliveryAddressValidationConstants.COMPANY_NAME));
		}

		addressModel.setAddress1(formData.get(DeliveryAddressValidationConstants.STREET));
		addressModel.setAddress2(formData.get(DeliveryAddressValidationConstants.STREET2));
		addressModel.setApartment(formData.get(DeliveryAddressValidationConstants.APARTMENT));
		addressModel.setCity(formData.get(DeliveryAddressValidationConstants.CITY));
		addressModel.setState(formData.get(DeliveryAddressValidationConstants.STATE));
		addressModel.setZipCode(formData.get(DeliveryAddressValidationConstants.ZIP));
		String serviceType = formData.get(DeliveryAddressValidationConstants.SERVICE_TYPE);
		addressModel.setServiceType(EnumServiceType.getEnum(serviceType.toUpperCase()));

		return addressModel;
	}
}
