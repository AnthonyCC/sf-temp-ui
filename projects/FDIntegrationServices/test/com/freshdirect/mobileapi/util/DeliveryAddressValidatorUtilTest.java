package com.freshdirect.mobileapi.util;

import java.text.MessageFormat;

import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;

import junit.framework.TestCase;

public class DeliveryAddressValidatorUtilTest extends TestCase {

    private static final String VALID_APARTMENT = "3";
    private static final String INVALID_APARTMENT = "$%^3";
    private static final String VALID_ZIP_CODE = "07016";
    private static final String INVALID_ZIP_CODE = "7016";
    private static final String VALID_STATE = "NJ";
    private static final String INVALID_STATE = "NJX";
    private static final String VALID_CITY = "Cranford";
    private static final String INVALID_CITY = "/Cra$nford#";
    private static final String VALID_ADDRESS = "16 LINCOLN AVE E";
    private static final String INVALID_ADDRESS = "16 & LINCOLN AVE E";
    private static final String VALID_PHONE_NUMBER = "0123456789";
    private static final String INVALID_PHONE_NUMBER = "0123";
    private static final String INVALID_MESSAGE_PATTERN = "Invalid value - {0}: {1}";
    private static final String PHONE_NUMBER_KEY = "dlvhomephone";
    private static final String PRIMARY_ADDRESS_KEY = "address1";
    private static final String SECONDARY_ADDRESS_KEY = "address2";
    private static final String CITY_KEY = "city";
    private static final String APARTMENT_KEY = "apartment";
    private static final String ZIPCODE_KEY = "zipcode";
    private static final String STATE_KEY = "state";

    public void testValidDeliveryHomePhoneContainsTenDigits() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setDlvhomephone(VALID_PHONE_NUMBER);

        ActionResult result = DeliveryAddressValidatorUtil.validateDeliveryAddress(address);

        TestCase.assertTrue("Delivery home phone does not contain ten digits", result.isSuccess());
    }

    public void testInvalidDeliveryHomePhoneDoesNotContainsTenDigits() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setDlvhomephone(INVALID_PHONE_NUMBER);

        ActionResult result = DeliveryAddressValidatorUtil.validateDeliveryAddress(address);

        TestCase.assertTrue(result.isFailure());
        TestCase.assertEquals("error type is not matched", PHONE_NUMBER_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, PHONE_NUMBER_KEY, INVALID_PHONE_NUMBER),
                result.getFirstError().getDescription());
    }

    public void testValidUnattandedDeliveryAddressWithMandatoryProperties() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("Not all mandatory properties are setup correctly to validate unattanded delivery", result.isSuccess());
    }

    public void testValidUnattandedDeliveryAddressWithMandatoryAndOptionalProperties() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);
        address.setAddress2(VALID_ADDRESS);
        address.setApartment(VALID_APARTMENT);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("Not all mandatory properties are setup correctly to validate unattanded delivery", result.isSuccess());
    }

    public void testInvalidUnattandedDeliveryAddressWithPrimaryAddressContainsSpecialChars() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(INVALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", PRIMARY_ADDRESS_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, PRIMARY_ADDRESS_KEY, INVALID_ADDRESS),
                result.getFirstError().getDescription());
    }

    public void testInvalidUnattandedDeliveryAddressWithCityContainsSpecialChars() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(INVALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", CITY_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, CITY_KEY, INVALID_CITY), result.getFirstError().getDescription());
    }

    public void testInvalidUnattandedDeliveryAddressWithStateIsNotContainedPredefinedList() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(INVALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", STATE_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, STATE_KEY, INVALID_STATE), result.getFirstError().getDescription());
    }

    public void testInvalidUnattandedDeliveryAddressWithZipCodeDoesNotContainsFiveDigits() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(INVALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", ZIPCODE_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, ZIPCODE_KEY, INVALID_ZIP_CODE),
                result.getFirstError().getDescription());
    }

    public void testValidUnattandedDeliveryAddressWithOptionalEmptySecondaryAddress() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setAddress2("");
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("Delivery secondary address line is not valid", result.isSuccess());
    }

    public void testInvalidUnattandedDeliveryAddressWithOptionalSecondaryAddressContainsSpecialChars() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setAddress2(INVALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", SECONDARY_ADDRESS_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, SECONDARY_ADDRESS_KEY, INVALID_ADDRESS),
                result.getFirstError().getDescription());
    }

    public void testValidUnattandedDeliveryAddressWithOptionalEmptyApartment() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);
        address.setApartment("");

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("Delivery apartment is not valid", result.isSuccess());
    }

    public void testInvalidUnattandedDeliveryAddressWithOptionalApartmentContainsSpecialChars() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(VALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(VALID_ZIP_CODE);
        address.setApartment(INVALID_APARTMENT);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", APARTMENT_KEY, result.getFirstError().getType());
        TestCase.assertEquals("error description is not matched", MessageFormat.format(INVALID_MESSAGE_PATTERN, APARTMENT_KEY, INVALID_APARTMENT),
                result.getFirstError().getDescription());
    }

    public void testInvalidUnattandedDeliveryAddressMultipleIssues() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setAddress1(INVALID_ADDRESS);
        address.setCity(VALID_CITY);
        address.setState(VALID_STATE);
        address.setZipcode(INVALID_ZIP_CODE);

        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);

        TestCase.assertTrue("there is no error", result.isFailure());
        TestCase.assertEquals("error type is not matched", 2, result.getErrors().size());
    }

}
