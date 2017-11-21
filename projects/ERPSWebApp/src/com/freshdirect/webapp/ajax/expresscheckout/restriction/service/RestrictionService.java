package com.freshdirect.webapp.ajax.expresscheckout.restriction.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.erp.EnumStateCodes;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.RestrictionUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ItemCount;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormRestriction;
import com.freshdirect.webapp.util.MediaUtils;

public class RestrictionService {

    public static RestrictionService defaultService() {
        return INSTANCE;
    }

    private static final String RESTRICTION_TYPE_ADDRESS = "address";
    private static final String RESTRICTION_TYPE_PICKUP = "pickup";
    private static final String RESTRICTION_TYPE_OUTSIDE_NY_STATE = "outside_ny";
    private static final String RESTRICTION_TYPE_TIMESLOT = "timeslot";
    private static final String RESTRICTION_TYPE_EBT_PAYMENT = "ebt";
    private static final String AGE_VERIFICATION_TYPE = "ageverification";
    private static final String NON_EBT_LINES_JSON_KEY = "nonEBTExclusiveLines";

    private static final RestrictionService INSTANCE = new RestrictionService();

    private static final Logger LOG = LoggerFactory.getInstance(RestrictionService.class);

    private RestrictionService() {
    }

    public void applyAgeVerificationForAlcohol(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        cart.setAgeVerified(true);
    }

    public FormRestriction verifyEbtPaymentRestriction(FDUserI user) {
        FormRestriction restriction = new FormRestriction();
        List<FDCartLineI> nonEBTCartlines = getNonEBTExclusiveCartLine(user);
        if (!nonEBTCartlines.isEmpty()) {
            restriction.setPassed(false);
            restriction.setType(RESTRICTION_TYPE_EBT_PAYMENT);
            restriction.getData().put(NON_EBT_LINES_JSON_KEY, populateNonEBTCartDataItems(user, nonEBTCartlines));
        }
        return restriction;
    }

    private List<CartData.Item> populateNonEBTCartDataItems(FDUserI user, List<FDCartLineI> ebtCartlines) {
        List<CartData.Item> populatedCartLines = new ArrayList<CartData.Item>();
        for (FDCartLineI cartLine : ebtCartlines) {
            ProductModel productNode = cartLine.lookupProduct();
            if (productNode == null) {
                LOG.error("Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                continue;
            }
            FDProduct fdProduct = cartLine.lookupFDProduct();
            if (fdProduct == null) {
                LOG.error("Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                continue;
            }
            CartData.Item item = CartDataService.defaultService().populateCartDataItem(cartLine, fdProduct, new ItemCount(), user.getShoppingCart(),
                    Collections.<Integer> emptySet(), productNode, user);
            populatedCartLines.add(item);
        }
        return populatedCartLines;
    }

    public FormRestriction verifyRestriction(FDUserI user) throws FDResourceException, IOException, TemplateException {
        FormRestriction restriction = null;
        FDCartModel cart = user.getShoppingCart();
        if (cart.containsAlcohol() && cart.getDeliveryAddress() != null) {
            FormRestriction alcoholDeliverableToAddressRestriction = RestrictionService.defaultService().verifyAlcoholDeliveryAddressRestriction(user);
            if (alcoholDeliverableToAddressRestriction.isPassed()) {
                FormRestriction alcoholOutsideNyStateRestriction = RestrictionService.defaultService().verifyAlcoholOutsideNyStateRestriction(user);
                if (alcoholOutsideNyStateRestriction.isPassed()) {
                    FormRestriction alcoholPickupRestriction = RestrictionService.defaultService().verifyAlcoholPickupRestriction(user);
                    if (alcoholPickupRestriction.isPassed()) {
                        if (cart.getDeliveryReservation() != null) {
                            FormRestriction alcoholTimeslotRestriction = RestrictionService.defaultService().verifyAlcoholTimeslotRestriction(user);
                            if (alcoholTimeslotRestriction.isPassed()) {
                                FormRestriction ageVerificationForAlcoholRestriction = RestrictionService.defaultService().populateAgeVerificationForAlcohol(user);
                                if (!ageVerificationForAlcoholRestriction.isPassed()) {
                                    restriction = ageVerificationForAlcoholRestriction;
                                }
                            } else {
                                restriction = alcoholTimeslotRestriction;
                            }
                        }
                    } else {
                        restriction = alcoholPickupRestriction;
                    }
                } else {
                    restriction = alcoholOutsideNyStateRestriction;
                }
            } else {
                restriction = alcoholDeliverableToAddressRestriction;
            }
        }
        return restriction;
    }

    private boolean checkAgeVerification(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        return !cart.isAgeVerified();
    }

    private boolean checkAlcoholInCartAndAddressIsOutOfNYState(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
        String state = deliveryAddress.getState();
        return !EnumStateCodes.ENUM_STATE_NY.getId().equalsIgnoreCase(state);
    }

    private boolean checkAlcoholInCartAndPickupAddressIsSelected(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
        return EnumServiceType.PICKUP.equals(deliveryAddress.getServiceType()) && cart.containsWineAndSpirit();
    }

    private boolean checkAlcoholInCartAndSelectedTimeslotHasRestrictions(FDUserI user) throws FDResourceException {
        FDCartModel cart = user.getShoppingCart();
        ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
        FDReservation reservation = cart.getDeliveryReservation();
        List<RestrictionI> alcoholRestrictions = new ArrayList<RestrictionI>();
        DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
        DateRange baseRange = new DateRange(reservation.getStartTime(), reservation.getEndTime());
        List<RestrictionI> deliveryRestrictions = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, getAlcoholRestrictionReasons(cart), baseRange);
        final String county = FDDeliveryManager.getInstance().getCounty(deliveryAddress);
        alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(deliveryAddress.getState(), county, deliveryRestrictions);
        return !alcoholRestrictions.isEmpty();
    }

    private boolean checkAlcoholIsDeliverableToAddress(FDUserI user) throws FDResourceException {
        FDCartModel cart = user.getShoppingCart();
        ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
        return FDDeliveryManager.getInstance().checkForAlcoholDelivery(deliveryAddress);
    }

    private List<FDCartLineI> getNonEBTExclusiveCartLine(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        List<FDCartLineI> ebtIneligibleOrderLines = new ArrayList<FDCartLineI>();
        if(null !=cart && null !=cart.getPaymentMethod()){
	        if (EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())) {
	            for (FDCartLineI cartLine : cart.getOrderLines()) {
	                if (cartLine.getProductRef().lookupProductModel().isExcludedForEBTPayment()) {
	                    ebtIneligibleOrderLines.add(cartLine);
	                }
	            }
	            cart.setEbtIneligibleOrderLines(ebtIneligibleOrderLines);
	        }
        }
        return ebtIneligibleOrderLines;
    }

    @SuppressWarnings("unchecked")
    private Set<EnumDlvRestrictionReason> getAlcoholRestrictionReasons(FDCartModel cart) {
        Set<EnumDlvRestrictionReason> alcoholReasons = new HashSet<EnumDlvRestrictionReason>();
        for (Iterator<EnumDlvRestrictionReason> i = EnumDlvRestrictionReason.iterator(); i.hasNext();) {
            EnumDlvRestrictionReason reason = i.next();
            if (("WIN".equals(reason.getName()) || "BER".equals(reason.getName()) || "ACL".equals(reason.getName()))
                    && (cart != null && cart.getApplicableRestrictions().contains(reason))) {
                alcoholReasons.add(reason);
            }
        }
        return alcoholReasons;
    }

    private FormRestriction populateAgeVerificationForAlcohol(FDUserI user) {
        FormRestriction data = new FormRestriction();
        if (checkAgeVerification(user)) {
            data.setPassed(false);
            data.setType(AGE_VERIFICATION_TYPE);
        }
        return data;
    }

    private FormRestriction verifyAlcoholDeliveryAddressRestriction(FDUserI user) throws FDResourceException, IOException, TemplateException {
        FormRestriction restriction = new FormRestriction();
        if (!checkAlcoholIsDeliverableToAddress(user)) {
            restriction.setPassed(false);
            restriction.setType(RESTRICTION_TYPE_ADDRESS);
            restriction.setMedia(loadAlcoholLearnMoreMedia());
        }
        return restriction;
    }

    private FormRestriction verifyAlcoholOutsideNyStateRestriction(FDUserI user) throws FDResourceException, IOException, TemplateException {
        FormRestriction restriction = new FormRestriction();
        if (checkAlcoholInCartAndAddressIsOutOfNYState(user)) {
            restriction.setPassed(false);
            restriction.setType(RESTRICTION_TYPE_OUTSIDE_NY_STATE);
            restriction.setMedia(loadAlcoholLearnMoreMedia());
        }
        return restriction;
    }

    private FormRestriction verifyAlcoholPickupRestriction(FDUserI user) throws FDResourceException, IOException, TemplateException {
        FormRestriction restriction = new FormRestriction();
        if (checkAlcoholInCartAndPickupAddressIsSelected(user)) {
            restriction.setPassed(false);
            restriction.setType(RESTRICTION_TYPE_PICKUP);
            restriction.setMedia(loadAlcoholLearnMoreMedia());
        }
        return restriction;
    }

    private FormRestriction verifyAlcoholTimeslotRestriction(FDUserI user) throws FDResourceException, IOException, TemplateException {
        FormRestriction restriction = new FormRestriction();
        if (checkAlcoholInCartAndSelectedTimeslotHasRestrictions(user)) {
            restriction.setPassed(false);
            restriction.setType(RESTRICTION_TYPE_TIMESLOT);
            restriction.setMedia(loadAlcoholLearnMoreMedia());
        }
        return restriction;
    }

    private String loadAlcoholLearnMoreMedia() throws IOException, TemplateException {
        StringWriter out = new StringWriter();
        String result = null;
        try {
            MediaUtils.render("/media/editorial/timeslots/alcohol_learnmore_popup.html", out, null, null);
            result = out.toString();
        } finally {
            out.close();
        }
        return result;
    }
}
