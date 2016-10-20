package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import static com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationConstants.BACKUP_DELIVERY_DOORMAN;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.AccountNumberConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.Constraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.CreditCardConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.PhoneConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.PredefinedTextConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.RoutingNumberConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.StateConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.StreetTextConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.TextConstraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.ZipCodeConstraint;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;

@SuppressWarnings("serial")
public class ConstraintProviderService {

    private static final String BANK_ACCOUNT_TYPE_SAVING = "S";
    private static final String BANK_ACCOUNT_TYPE_CHECKING = "C";
    public static final String OFF = "off";
    public static final String ON = "on";

    private static final ConstraintProviderService INSTANCE = new ConstraintProviderService();

    private static final Set<String> PHONE_TYPE_TYPE = new HashSet<String>(Arrays.asList(DeliveryAddressValidationConstants.PHONE_TYPE_MOBILE,
            DeliveryAddressValidationConstants.PHONE_TYPE_HOME, DeliveryAddressValidationConstants.PHONE_TYPE_WORK));

    private static final Set<String> BACKUP_DELIVERY_TYPE = new HashSet<String>(Arrays.asList(DeliveryAddressValidationConstants.BACKUP_DELIVERY_NONE, BACKUP_DELIVERY_DOORMAN,
            DeliveryAddressValidationConstants.BACKUP_DELIVERY_NEIGHBOR, DeliveryAddressValidationConstants.BACKUP_DELIVERY_UNATTANDED));

    private static final Set<String> ACCOUNT_TYPE_TYPE = new HashSet<String>(Arrays.asList(BANK_ACCOUNT_TYPE_CHECKING, BANK_ACCOUNT_TYPE_SAVING));

    private static final Set<String> ON_OFF_TYPE = new HashSet<String>(Arrays.asList(ON, OFF));

    private static final Map<String, Constraint<String>> HOME_DELIVERY_ADDRESS_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(DeliveryAddressValidationConstants.NICK_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.FIRST_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.LAST_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.STREET, new StreetTextConstraint(false));
            put(DeliveryAddressValidationConstants.APARTMENT, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.CITY, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.STATE, new StateConstraint(false));
            put(DeliveryAddressValidationConstants.ZIP, new ZipCodeConstraint(false));
            put(DeliveryAddressValidationConstants.PHONE, new PhoneConstraint(false));
            put(DeliveryAddressValidationConstants.PHONE_TYPE, new PredefinedTextConstraint(true, PHONE_TYPE_TYPE));
            put(DeliveryAddressValidationConstants.PHONE_EXTENSION, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE, new PhoneConstraint(true));
            put(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE_TYPE, new PredefinedTextConstraint(true, PHONE_TYPE_TYPE));
            put(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE_EXTENSION, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.INSTRUCTIONS, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION, new PredefinedTextConstraint(true, BACKUP_DELIVERY_TYPE));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_FIRST_NAME, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_LAST_NAME, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_ADDRESS, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_PHONE, new PhoneConstraint(true));
            put(DeliveryAddressValidationConstants.BACKUP_DELIVERY_INSTRUCTIONS, new TextConstraint(true));
        }
    };

    private static final Map<String, Constraint<String>> OFFICE_DELIVERY_ADDRESS_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(DeliveryAddressValidationConstants.COMPANY_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.FIRST_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.LAST_NAME, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.STREET, new StreetTextConstraint(false));
            put(DeliveryAddressValidationConstants.APARTMENT, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.CITY, new TextConstraint(false));
            put(DeliveryAddressValidationConstants.STATE, new StateConstraint(false));
            put(DeliveryAddressValidationConstants.ZIP, new ZipCodeConstraint(false));
            put(DeliveryAddressValidationConstants.PHONE, new PhoneConstraint(false));
            put(DeliveryAddressValidationConstants.PHONE_TYPE, new PredefinedTextConstraint(true, PHONE_TYPE_TYPE));
            put(DeliveryAddressValidationConstants.PHONE_EXTENSION, new TextConstraint(true));
            put(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE, new PhoneConstraint(true));
            put(DeliveryAddressValidationConstants.ALTERNATIVE_PHONE_TYPE, new PredefinedTextConstraint(true, PHONE_TYPE_TYPE));
            put(DeliveryAddressValidationConstants.INSTRUCTIONS, new TextConstraint(true));
        }
    };

    private static final Map<String, Constraint<String>> CREDIT_CARD_PAYMENT_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(PaymentMethodName.ACCOUNT_HOLDER, new TextConstraint(false));
            put(PaymentMethodName.CARD_BRAND, new TextConstraint(false));
            put(PaymentMethodName.ACCOUNT_NUMBER, new CreditCardConstraint(false));
            put(PaymentMethodName.CARD_EXP_MONTH, new TextConstraint(false));
            put(PaymentMethodName.CARD_EXP_YEAR, new TextConstraint(false));
            put(EnumUserInfoName.BIL_COUNTRY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_ADDRESS_1.getCode(), new StreetTextConstraint(false));
            put(EnumUserInfoName.BIL_APARTMENT.getCode(), new TextConstraint(true));
            put(EnumUserInfoName.BIL_CITY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_STATE.getCode(), new StateConstraint(false));
            put(EnumUserInfoName.BIL_ZIPCODE.getCode(), new ZipCodeConstraint(false));
            put(PaymentMethodName.PHONE, new PhoneConstraint(false));
        }
    };

    private static final Map<String, Constraint<String>> BANK_PAYMENT_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(PaymentMethodName.BANK_ACCOUNT_TYPE, new PredefinedTextConstraint(false, ACCOUNT_TYPE_TYPE));
            put(PaymentMethodName.BANK_NAME, new TextConstraint(false));
            put(PaymentMethodName.ACCOUNT_NUMBER, new AccountNumberConstraint(false));
            put(PaymentMethodName.ACCOUNT_NUMBER_VERIFY, new AccountNumberConstraint(false));
            put(PaymentMethodName.ABA_ROUTE_NUMBER, new RoutingNumberConstraint(false));
            put(EnumUserInfoName.BIL_COUNTRY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_ADDRESS_1.getCode(), new StreetTextConstraint(false));
            put(EnumUserInfoName.BIL_APARTMENT.getCode(), new TextConstraint(true));
            put(EnumUserInfoName.BIL_CITY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_STATE.getCode(), new StateConstraint(false));
            put(EnumUserInfoName.BIL_ZIPCODE.getCode(), new ZipCodeConstraint(false));
            put(PaymentMethodName.PHONE, new PhoneConstraint(false));
            put(PaymentMethodName.TERMS, new PredefinedTextConstraint(false, ON_OFF_TYPE));
        }
    };

    private static final Map<String, Constraint<String>> EBT_PAYMENT_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(PaymentMethodName.ACCOUNT_HOLDER, new TextConstraint(false));
            put(PaymentMethodName.ACCOUNT_NUMBER, new CreditCardConstraint(false));
            put(EnumUserInfoName.BIL_COUNTRY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_ADDRESS_1.getCode(), new StreetTextConstraint(false));
            put(EnumUserInfoName.BIL_APARTMENT.getCode(), new TextConstraint(true));
            put(EnumUserInfoName.BIL_CITY.getCode(), new TextConstraint(false));
            put(EnumUserInfoName.BIL_STATE.getCode(), new StateConstraint(false));
            put(EnumUserInfoName.BIL_ZIPCODE.getCode(), new ZipCodeConstraint(false));
            put(PaymentMethodName.PHONE, new PhoneConstraint(false));
        }
    };

    private static final Map<String, Constraint<String>> SMS_ALERT_CONSTRAINTS = new HashMap<String, Constraint<String>>() {

        {
            put(AlertValidationConstraints.MOBILE, new PhoneConstraint(false));
            put(AlertValidationConstraints.NOTICES, new PredefinedTextConstraint(false, ON_OFF_TYPE));
            put(AlertValidationConstraints.ALERTS, new PredefinedTextConstraint(false, ON_OFF_TYPE));
            put(AlertValidationConstraints.PERKS, new PredefinedTextConstraint(false, ON_OFF_TYPE));
            put(AlertValidationConstraints.REMIND_ME, new PredefinedTextConstraint(false, ON_OFF_TYPE));
        }
    };

    private static final Map<String, Constraint<String>> UNMODIFIED_UNATTENDED_DELIVERY_ADDRESS_CONSTRAINTS = Collections
            .unmodifiableMap(new HashMap<String, Constraint<String>>() {

                {
                    put(DeliveryAddressValidationConstants.STREET, new StreetTextConstraint(false));
                    put(DeliveryAddressValidationConstants.APARTMENT, new TextConstraint(true));
                    put(DeliveryAddressValidationConstants.CITY, new TextConstraint(false));
                    put(DeliveryAddressValidationConstants.STATE, new StateConstraint(false));
                    put(DeliveryAddressValidationConstants.ZIP, new ZipCodeConstraint(false));
                }
            });

    private static final Map<String, Constraint<String>> UNMODIFIED_HOME_DELIVERY_ADDRESS_CONSTRAINTS = Collections.unmodifiableMap(HOME_DELIVERY_ADDRESS_CONSTRAINTS);

    private static final Map<String, Constraint<String>> UNMODIFIED_OFFICE_DELIVERY_ADDRESS_CONSTRAINTS = Collections.unmodifiableMap(OFFICE_DELIVERY_ADDRESS_CONSTRAINTS);

    private static final Map<String, Constraint<String>> UNMODIFIED_CREDIT_CARD_PAYMENT_CONSTRAINTS = Collections.unmodifiableMap(CREDIT_CARD_PAYMENT_CONSTRAINTS);

    private static final Map<String, Constraint<String>> UNMODIFIED_BANK_PAYMENT_CONSTRAINTS = Collections.unmodifiableMap(BANK_PAYMENT_CONSTRAINTS);

    private static final Map<String, Constraint<String>> UNMODIFIED_EBT_PAYMENT_CONSTRAINTS = Collections.unmodifiableMap(EBT_PAYMENT_CONSTRAINTS);

    private static final Map<String, Constraint<String>> UNMODIFIED_SMS_ALERT_CONSTRAINTS = Collections.unmodifiableMap(SMS_ALERT_CONSTRAINTS);

    private ConstraintProviderService() {
    }

    public static ConstraintProviderService defaultService() {
        return INSTANCE;
    }

    public Map<String, Constraint<String>> getHomeDeliveryAddressConstraints(Map<String, String> data) {
        return modifyHomeConstraintDependOnBackupDeliveryField(data, UNMODIFIED_HOME_DELIVERY_ADDRESS_CONSTRAINTS);
    }

    public Map<String, Constraint<String>> getOfficeDeliveryAddressConstraints() {
        return UNMODIFIED_OFFICE_DELIVERY_ADDRESS_CONSTRAINTS;
    }

    public Map<String, Constraint<String>> getCreditCardPaymentConstraints() {
        return UNMODIFIED_CREDIT_CARD_PAYMENT_CONSTRAINTS;
    }

    public Map<String, Constraint<String>> getBankPaymentConstraints() {
        return UNMODIFIED_BANK_PAYMENT_CONSTRAINTS;
    }

    public Map<String, Constraint<String>> getElectronicBenefitTransferPaymentConstraints() {
        return UNMODIFIED_EBT_PAYMENT_CONSTRAINTS;
    }

    public Map<String, Constraint<String>> getSmsAlertConstraints(Map<String, String> data) {
        return modifyAlertConstraint(data, UNMODIFIED_SMS_ALERT_CONSTRAINTS);
    }

    public Map<String, Constraint<String>> getUnattendedDeliveryAddressConstraints() {
        return UNMODIFIED_UNATTENDED_DELIVERY_ADDRESS_CONSTRAINTS;
    }

    private Map<String, Constraint<String>> modifyAlertConstraint(Map<String, String> datas, Map<String, Constraint<String>> constraints) {
        final String notices = datas.get(AlertValidationConstraints.NOTICES);
        final String alerts = datas.get(AlertValidationConstraints.ALERTS);
        final String perks = datas.get(AlertValidationConstraints.PERKS);

        if (!isAtLeastOneSmsAlertCheckboxOn(notices, alerts, perks)) {
            constraints.get(AlertValidationConstraints.NOTICES).setForceInValid(true);
            constraints.get(AlertValidationConstraints.ALERTS).setForceInValid(true);
            constraints.get(AlertValidationConstraints.PERKS).setForceInValid(true);
        } else {
            constraints.get(AlertValidationConstraints.NOTICES).setForceInValid(false);
            constraints.get(AlertValidationConstraints.ALERTS).setForceInValid(false);
            constraints.get(AlertValidationConstraints.PERKS).setForceInValid(false);
        }
        return constraints;
    }

    private boolean isAtLeastOneSmsAlertCheckboxOn(final String notices, final String alerts, final String perks) {
        return ConstraintProviderService.ON.equals(notices) || ConstraintProviderService.ON.equals(alerts) || ConstraintProviderService.ON.equals(perks);
    }

    private Map<String, Constraint<String>> modifyHomeConstraintDependOnBackupDeliveryField(Map<String, String> datas, Map<String, Constraint<String>> constraints) {
        final String backupDeliveryAuthorizationValue = datas.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION);

        if (DeliveryAddressValidationConstants.BACKUP_DELIVERY_DOORMAN.equals(backupDeliveryAuthorizationValue)
                || DeliveryAddressValidationConstants.BACKUP_DELIVERY_NONE.equals(backupDeliveryAuthorizationValue)) {
            setBackupDeliverySectionOptional(constraints);
        } else if (DeliveryAddressValidationConstants.BACKUP_DELIVERY_NEIGHBOR.equals(backupDeliveryAuthorizationValue)) {
            setBackupDeliverySectionOptional(constraints);
            setBackupDeliveryNeighborSectionRequired(constraints);
        } else if (DeliveryAddressValidationConstants.BACKUP_DELIVERY_UNATTANDED.equals(backupDeliveryAuthorizationValue)) {
            setBackupDeliverySectionOptional(constraints);
            setBackupDeliveryUnattandedSectionRequired(constraints);
        }
        return constraints;
    }

    private void setBackupDeliveryUnattandedSectionRequired(Map<String, Constraint<String>> constraints) {
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION).setOptional(false);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_INSTRUCTIONS).setOptional(false);
    }

    private void setBackupDeliveryNeighborSectionRequired(Map<String, Constraint<String>> constraints) {
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION).setOptional(false);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_FIRST_NAME).setOptional(false);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_LAST_NAME).setOptional(false);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_ADDRESS).setOptional(false);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_PHONE).setOptional(false);
    }

    private void setBackupDeliverySectionOptional(Map<String, Constraint<String>> constraints) {
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_AUTHORIZATION).setOptional(true);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_FIRST_NAME).setOptional(true);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_LAST_NAME).setOptional(true);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_ADDRESS).setOptional(true);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_PHONE).setOptional(true);
        constraints.get(DeliveryAddressValidationConstants.BACKUP_DELIVERY_INSTRUCTIONS).setOptional(true);
    }

}
