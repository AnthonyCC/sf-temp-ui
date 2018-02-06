package com.freshdirect.webapp.ajax.expresscheckout.timeslot.service;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.FDDeliveryTimeslots;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdlogistics.model.FDTimeslotList;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionHelper;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.util.DlvTimeslotStats;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.fdstore.util.RestrictionUtil;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.logistics.delivery.model.EnumOrderAction;
import com.freshdirect.logistics.delivery.model.EnumOrderType;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.webapp.action.fdstore.ChooseTimeslotAction;
import com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service.CoremetricsService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotsData;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.Result;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.CCFormatter;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class TimeslotService {

    private static final Logger LOGGER = LoggerFactory.getInstance(TimeslotService.class);
    private static final String DEFAULT_TIMESLOT_ID = "timeslotId";
    private static final TimeslotService INSTANCE = new TimeslotService();

    private TimeslotService() {
    }

    public static TimeslotService defaultService() {
        return INSTANCE;
    }

    /**
     * Load cart timeslot
     * 
     * @param user
     *            Customer object
     * @param cart
     *            Cart content
     */
    public FormTimeslotData loadCartTimeslot(FDUserI user, final FDCartI cart) {
        FormTimeslotData timeslotData = new FormTimeslotData();
        FDReservation reservation = cart.getDeliveryReservation();
        ErpAddressModel addressModel=null;
        addressModel=cart.getDeliveryAddress();
        ErpAddressModel originalAddress = (cart.getDeliveryReservation()!=null)?cart.getDeliveryReservation().getAddress():null;
        if(StandingOrderHelper.isSO3StandingOrder(user)){
        	StandingOrderHelper.loadSO3CartTimeSlot(timeslotData,user.getCurrentStandingOrder());
        } else if (reservation != null) {
            if (!(cart instanceof FDOrderI)) {
                // Deal with case when timeslot zone does not match
                // the zone of selected address assigned to cart

                // pick timeslot address
                String timeslotAddressId = reservation.getAddressId();
                // pick delivery address
                String dlvAddressId = null;
                if (cart != null && addressModel != null) {
                    dlvAddressId = addressModel.getId();
                }

               if(TimeslotLogic.isAddressChange(originalAddress, addressModel, timeslotAddressId, dlvAddressId)){
                         // timeslot has different address than the selected
                    LOGGER.warn("Delivery address does not match timeslot address. Discard selected timeslot (ID=" + reservation.getTimeslotId() + ")");

                         return timeslotData;
                    
                }
                		
            }
           
            Date startTime = reservation.getStartTime();
            Calendar startTimeCalendar = DateUtil.toCalendar(startTime);
            String dayNames[] = new DateFormatSymbols().getWeekdays();
            timeslotData.setId(NVL.apply(reservation.getTimeslotId(), DEFAULT_TIMESLOT_ID));
            timeslotData.setYear(String.valueOf(startTimeCalendar.get(Calendar.YEAR)));
            timeslotData.setMonth(String.valueOf(startTimeCalendar.get(Calendar.MONTH) + 1));
            timeslotData.setDayOfMonth(String.valueOf(startTimeCalendar.get(Calendar.DAY_OF_MONTH)));
            timeslotData.setDayOfWeek(dayNames[startTimeCalendar.get(Calendar.DAY_OF_WEEK)]);
            timeslotData.setTimePeriod(format(startTime, reservation.getEndTime()));
            timeslotData.setStartDate(startTime);
            timeslotData.setEndDate(reservation.getEndTime());
        }
        timeslotData.setOnOpenCoremetrics(CoremetricsService.defaultService().getCoremetricsData("timeslot"));
        timeslotData.setShowForceOrder(user.getMasqueradeContext() != null && user.getMasqueradeContext().isForceOrderAvailable() && !user.getMasqueradeContext().isAddOnOrderEnabled());
        timeslotData.setForceOrderEnabled(user.getMasqueradeContext() != null && user.getMasqueradeContext().isForceOrderEnabled());
        
        return timeslotData;
    }

    public FormTimeslotsData loadTimeslotsData(FDUserI user, FDDeliveryTimeslotModel timeslotModel) throws FDResourceException {
        FormTimeslotsData timeslotData = new FormTimeslotsData();

        if (user.getShoppingCart().getDeliveryReservation() != null) {
            timeslotData.setSelectedTimeslotId(user.getShoppingCart().getDeliveryReservation().getTimeslotId());
        }

        if (user.getReservation() != null) {
            timeslotData.setReservedTimeslotId(user.getReservation().getTimeslotId());
        }

        timeslotData.setZonePromoAmount(CCFormatter.formatQuantity(timeslotModel.getZonePromoAmount()));

        for (FDTimeslotUtil timeslotUtil : timeslotModel.getTimeslotList()) {
            List<FormTimeslotData> timeslotDatas = new ArrayList<FormTimeslotData>();
            List<FDTimeslot> timeslots = timeslotUtil.getTimeslotsFlat();
            sortTimeslotByStartTime(timeslots);
            for (FDTimeslot fdTimeslot : timeslots) {
                timeslotDatas.add(loadTimeslotData(user, fdTimeslot));
            }
            timeslotData.addTimeSlots(timeslotDatas);
        }

        return timeslotData;
    }

    public void sortTimeslotByStartTime(List<FDTimeslot> timeslots) {
        Collections.sort(timeslots, new Comparator<FDTimeslot>() {

            @Override
            public int compare(FDTimeslot t1, FDTimeslot t2) {
                return t1.getStartTime().compareTo(t2.getStartTime());
            }

        });
    }

    public FormTimeslotData loadTimeslotData(FDUserI user, FDTimeslot slot) throws FDResourceException {
        FormTimeslotData timeslotData = new FormTimeslotData();
        Date startTime = slot.getStartDateTime();
        Calendar startTimeCalendar = DateUtil.toCalendar(startTime);
        String dayNames[] = new DateFormatSymbols().getWeekdays();
        timeslotData.setId(NVL.apply(slot.getId(), DEFAULT_TIMESLOT_ID));
        timeslotData.setYear(String.valueOf(startTimeCalendar.get(Calendar.YEAR)));
        timeslotData.setMonth(String.valueOf(startTimeCalendar.get(Calendar.MONTH) + 1));
        timeslotData.setDayOfMonth(String.valueOf(startTimeCalendar.get(Calendar.DAY_OF_MONTH)));
        timeslotData.setDayOfWeek(dayNames[startTimeCalendar.get(Calendar.DAY_OF_WEEK)]);
        timeslotData.setTimePeriod(format(startTime, slot.getEndTime()));
        timeslotData.setStartDate(startTime);
        timeslotData.setEndDate(slot.getEndDateTime());
        timeslotData.setFull((user.isChefsTable() && !slot.hasAvailCTCapacity()) || (!user.isChefsTable() && !slot.hasNormalAvailCapacity()));
        timeslotData.setCutoffDate(slot.getCutoffDateTime());
        timeslotData.setCutoffTime(format(slot.getCutoffDateTime()) + " " + dayNames[DateUtil.toCalendar(slot.getCutoffDateTime()).get(Calendar.DAY_OF_WEEK)]);
        timeslotData.setChefsTable(!slot.hasNormalAvailCapacity() && slot.hasAvailCTCapacity());
        timeslotData.setSteeringDiscount(slot.getSteeringDiscount());
        timeslotData.setEcoFriendly(slot.isEcoFriendly());
        timeslotData.setDepot(slot.isDepot());
        timeslotData.setAlcoholRestricted(slot.isAlcoholRestricted());
        timeslotData.setPremiumAmount(slot.getPremiumAmount());
        timeslotData.setPremiumSlot(slot.isPremiumSlot());
        timeslotData.setUnavailable(slot.isUnavailable());
        timeslotData.setMinOrderMsg(slot.getMinOrderMsg());
        timeslotData.setMinOrderAmt(slot.getMinOrderAmt());
        timeslotData.setMinOrderMet(slot.isMinOrderMet());
        timeslotData.setIsEarlyAM(slot.isEarlyAM());
        timeslotData.setDeliveryFee(slot.getDeliveryFee());
        timeslotData.setPromoDeliveryFee(slot.getPromoDeliveryFee());
        return timeslotData;
    }

    public List<ValidationError> reserveDeliveryTimeSlot(FormDataRequest timeslotRequestData, FDUserI user, HttpSession session) throws FDResourceException {
        String deliveryTimeSlotId = FormDataService.defaultService().get(timeslotRequestData, "deliveryTimeslotId");  
        String soFirstDate = FormDataService.defaultService().get(timeslotRequestData, "soFirstDate");        
        try {
            return reserveDeliveryTimeslot(deliveryTimeSlotId, session, soFirstDate);
        } catch (ReservationException e) {
            LOGGER.error(MessageFormat.format("Failed to reserve timeslot for timeslot id[{0}]:", deliveryTimeSlotId), e);
            throw new FDResourceException(e);
        }
    }

    public void applyPreReservedDeliveryTimeslot(HttpSession session) throws FDResourceException {
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        FDCartModel cart = user.getShoppingCart();
        if (cart.getDeliveryReservation() == null) {
            FDReservation reservation = user.getReservation();
            if (reservation != null && cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getId().equals(reservation.getAddressId())) {
                String timeslotId = reservation.getTimeslotId();
                try {
                    reserveDeliveryTimeslot(timeslotId, session);
                } catch (ReservationException e) {
                    LOGGER.error(MessageFormat.format("Failed to reserve timeslot for timeslot id[{0}]:", timeslotId), e);
                    throw new FDResourceException(e);
                }
            }
        }
    }

    public List<ValidationError> reserveDeliveryTimeslot(String deliveryTimeSlotId, HttpSession session, String soNextDeliveryDate) throws FDResourceException, ReservationException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        ActionResult actionResult = new ActionResult();
        ChooseTimeslotAction.reserveDeliveryTimeSlot(session, deliveryTimeSlotId, null, actionResult, soNextDeliveryDate);
        for (ActionError error : actionResult.getErrors()) {
            validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
        }
        return validationErrors;
    }
    
    public List<ValidationError> reserveDeliveryTimeslot(String deliveryTimeSlotId, HttpSession session) throws FDResourceException, ReservationException {
        return reserveDeliveryTimeslot(deliveryTimeSlotId, session, null);
    }

    public void releaseTimeslot(FDUserI user) throws FDResourceException {
        FDCartModel cart = user.getShoppingCart();
        FDReservation timeslotReservation = cart.getDeliveryReservation();
        if (timeslotReservation != null) {
            String rsvId = timeslotReservation.getPK().getId();
            ErpAddressModel erpAddress = cart.getDeliveryAddress();

            TimeslotEvent event = new TimeslotEvent((user.getApplication() != null) ? user.getApplication().getCode() : "", cart.isDlvPassApplied(), cart.getDeliverySurcharge(),
                    cart.isDeliveryChargeWaived(), (cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
            LOGGER.info("releaseReservation by ID: " + rsvId);
            FDDeliveryManager.getInstance().releaseReservation(rsvId, erpAddress, event, true);
            cart.setDeliveryReservation(null);
        }
    }

    public boolean isTimeslotSelected(final FDUserI user) {
        FDReservation deliveryReservation = user.getShoppingCart().getDeliveryReservation();
        return (deliveryReservation != null && deliveryReservation.getTimeslotId() != null);
    }

    public TimeslotEvent createTimeslotEventModel(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        boolean isCtActive = (cart != null && cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false;
        String transactionSource = (user.getApplication() != null) ? user.getApplication().getCode() : "";
        boolean dlvPassApplied = (cart != null) ? cart.isDlvPassApplied() : false;
        double deliveryCharge = (cart != null) ? cart.getDeliverySurcharge() : 0.00;
        boolean deliveryChargeWaived = (cart != null) ? cart.isDeliveryChargeWaived() : false;

        return new TimeslotEvent(transactionSource, dlvPassApplied, deliveryCharge, deliveryChargeWaived, isCtActive, user.getPrimaryKey(), EnumCompanyCode.fd.name());
    }

    private String format(Date startDate, Date endDate) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(format(startDate)).append(" - ").append(format(endDate));
        return sb.toString();
    }

    private String format(Date date) {
    	StringBuffer sb = new StringBuffer();
    	Calendar cal = DateUtil.toCalendar(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int marker = cal.get(Calendar.AM_PM);

        if (hour > 12) {
            sb.append(hour - 12);
        } else {
            sb.append(hour);
        }

        if (minute != 0) {
            sb.append(':').append(minute);
        }

        if (marker == Calendar.AM) {
            sb.append("AM");
        } else {
            sb.append("PM");
        }
        return sb.toString();
    }

    public Result getGenericTimeslots(ErpAddressModel address, FDUserI user, TimeslotContext timeSlotContext, boolean isEventLogged)
            throws FDResourceException {
        final FDCartModel cart = StandingOrderHelper.isSO3StandingOrder(user) ? user.getSoTemplateCart() : user.getShoppingCart();
        final FDStandingOrder so = user.getCurrentStandingOrder();

        final Result result = new Result();
        final FDDeliveryTimeslotModel deliveryModel = new FDDeliveryTimeslotModel();
        DateRange range = createDateRange(1, 8);
        if (user.isNewSO3Enabled()) {
            range = createDateRange(2, 9); // For SO 3.0 - fetch timeslots from 2 days ahead of current date.
        }
        List<DateRange> ranges = new ArrayList<DateRange>();
        ranges.add(range);

        // collect all restrictions
        DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
        // narrow down restrictions
        LOGGER.debug("All Restrictions (before OTR filter): " + restrictions.size());
        restrictions.keepRestrictionsForGenericTimeslots();
        LOGGER.debug("All Restrictions: " + restrictions.size());

        FDTimeslotUtil tsu; // we want only a single set of time slots (see: multiple sets in case of advance orders)
        List<FDTimeslotUtil> singleTSset = new ArrayList<FDTimeslotUtil>();
        FDDeliveryTimeslots deliveryTimeslots = null;
        {
            String applicationId = (user.getApplication() != null) ? user.getApplication().getCode() : "";
            if (user.getMasqueradeContext() != null) {
                applicationId = "CSR";
            }
            TimeslotEvent event = new TimeslotEvent(applicationId, cart.isDlvPassApplied(), cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(),
                    (cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false, user.getPrimaryKey(), EnumCompanyCode.fd.name());

            event.setLogged(isEventLogged);

            // Fetch time slots
            try {
                deliveryTimeslots = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(ranges, event, TimeslotLogic.encodeCustomer(address, user),
                        TimeslotLogic.getOrderContext(EnumOrderAction.CREATE, user.getIdentity().getErpCustomerPK(), EnumOrderType.SO_TEMPLATE), timeSlotContext,
                        so.getUser().isNewSO3Enabled());
            } catch (FDAuthenticationException e) {
                LOGGER.error(e);
            }

            FDTimeslotList tsList = deliveryTimeslots.getTimeslotList().get(0);

            tsu = new FDTimeslotUtil(tsList.getTimeslots(), range.getStartDate(), range.getEndDate(), restrictions, tsList.getResponseTime(), range.isAdvanced(), null);
            singleTSset.add(tsu);
        }

        List<RestrictionI> alcoholRestrictions;
        {
            List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, getAlcoholRestrictionReasons(cart), range);
            // Filter Alcohol restrictions by current State and county.
            final String county = FDDeliveryManager.getInstance().getCounty(address);
            alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(address.getState(), county, r);

            LOGGER.debug("Alcohol restrictions" + alcoholRestrictions);
        }

        DlvTimeslotStats stats = new DlvTimeslotStats();

        stats.apply(deliveryTimeslots);

        TimeslotLogic.filterDeliveryTimeSlots(user, restrictions, singleTSset, Collections.<String> emptySet(), deliveryModel, alcoholRestrictions, false, address, true,
                stats);
        // Post-op: remove unnecessary timeslots
        TimeslotLogic.purge(singleTSset);

        deliveryModel.setTimeslotList(singleTSset);

        stats.apply(deliveryModel);
        // update chefs table stats in user
        stats.apply(user);

        // unflag variable min order timeslots for deliveryinfo timeslots
        // TimeslotLogic.clearVariableMinimum(user, singleTSset);
        // deliveryModel.setMinOrderReqd(false);

        // fill in delivery model
        deliveryModel.setShoppingCart(cart);
        deliveryModel.setCurrentStandingOrder(user.getCurrentStandingOrder()); // <== ??? does it matter here?
        deliveryModel.setZoneId(cart.getDeliveryZone());
        deliveryModel.setZonePromoAmount(PromotionHelper.getDiscount(user, deliveryModel.getZoneId()));

        // set selected timeslot ID according to next delivery date of standing order template
        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            deliveryModel.setTimeSlotId("");

        } else {
            deliveryModel.setTimeSlotId(StandingOrderHelper.findMatchingSlot(so, tsu));
        }
        deliveryModel.setPreReserved(false);
        deliveryModel.setPreReserveSlotId(null);
        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            calculateSOFirstDeilveryDate(so, user, deliveryModel);

        }
        // cheat the capacity
        deliveryModel.setHasCapacity(true);
        result.setDeliveryTimeslotModel(deliveryModel);
        return result;
    }

    public Result getTimeslot(HttpSession session, ErpAddressModel address, FDUserI user, TimeslotContext timeSlotContext, boolean isEventLogged, String timeSlotId,
            boolean forceOrder, boolean deliveryInfo, boolean returnSameDaySlots) throws FDResourceException, ReservationException {
        FDDeliveryTimeslotModel deliveryModel = new FDDeliveryTimeslotModel();

        final FDCartModel cart = user.getShoppingCart();
        Result result = new Result();
        // check if preReservedSlotId exits
        checkForPreReservedSlotId(deliveryModel, cart, user, timeSlotId, address);

        // getRestriction reasons
        getRestrictionReason(cart, deliveryModel);

        boolean showPremiumSlots = false;

        DateRange baseRange = createDateRange(1, 8);

        if (EnumEStoreId.FDX.name().equals(user.getUserContext().getStoreContext().getEStoreId().name())) {
            baseRange = createDateRange(0, 7);
        }

        DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

        // set standing order
        if (user.getCheckoutMode().isModifyStandingOrder()) {
            deliveryModel.setCurrentStandingOrder(user.getCurrentStandingOrder());
        }

        if (user != null && user.getIdentity() != null && !StringUtil.isEmpty(user.getIdentity().getErpCustomerPK())) {
            address.setCustomerId(user.getIdentity().getErpCustomerPK());
        }

        String applicationId = (user.getApplication() != null) ? user.getApplication().getCode() : "";
        if (user.getMasqueradeContext() != null) {
            applicationId = "CSR";
        }
        TimeslotEvent event = new TimeslotEvent(applicationId, cart.isDlvPassApplied(), cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(),
                (cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false, user.getPrimaryKey(), EnumCompanyCode.fd.name());

        event.setLogged(isEventLogged);

        DlvTimeslotStats stats = new DlvTimeslotStats();

        List<DateRange> dateRanges = new ArrayList<DateRange>();
        dateRanges.add(baseRange);
        List<FDTimeslotUtil> timeslotList = getFDTimeslotListForDateRange(restrictions, dateRanges, result, address, user, deliveryModel, event, stats, forceOrder, deliveryInfo,
                timeSlotContext);

        if (!returnSameDaySlots || (timeSlotContext != null && !timeSlotContext.equals(TimeslotContext.CHECKOUT_TIMESLOTS))) {
            TimeslotLogic.purgeSDSlots(timeslotList);
        }

        // Start:: Add On Order

        if (user.getMasqueradeContext() != null && user.getMasqueradeContext().getParentOrderId() != null) {
            String parentOrderId = user.getMasqueradeContext().getParentOrderId();
            FDOrderI order = FDCustomerManager.getOrder(parentOrderId);
            timeslotList = this.addOnOrderTimeSlot(timeslotList, order);
            if (timeslotList == null || timeslotList.isEmpty())
                try {
                    throw new ReservationException();
                } catch (ReservationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            deliveryModel.setPreReserveSlotId(order.getDeliveryReservation().getTimeslot().getId());
            deliveryModel.setPreReserved(true);
            TimeslotService.defaultService().reserveDeliveryTimeslot(order.getDeliveryReservation().getTimeslot().getId(), session);

        }
        // End:: Add On Order

        showPremiumSlots = TimeslotLogic.hasPremiumSlots(timeslotList);

        deliveryModel.setShowPremiumSlots(showPremiumSlots);

        if (!timeslotList.isEmpty()) {
        baseRange = new DateRange(timeslotList.get(0).getStartDate(), DateUtil.addDays(timeslotList.get(0).getEndDate(), 1));

        EnumDlvRestrictionReason specialHoliday = getNextHoliday(restrictions, baseRange, FDStoreProperties.getHolidayLookaheadDays());

        LOGGER.debug("specialHoliday :" + specialHoliday);

        final boolean containsSpecialHoliday = cart.getApplicableRestrictions().contains(specialHoliday);
        final boolean containsAdvanceOrderItem = cart.hasAdvanceOrderItem();

        LOGGER.debug("containsSpecialHoliday :" + containsSpecialHoliday + " :containsAdvanceOrderItem:" + containsAdvanceOrderItem);

        dateRanges = getDateRanges(baseRange, (containsSpecialHoliday && !deliveryInfo), restrictions, specialHoliday, containsAdvanceOrderItem);

        Collections.sort(dateRanges, new DateRangeComparator());

        /* Holiday & specialItems restrictions */
        getHolidayRestrictions(restrictions, dateRanges, deliveryModel);

        dateRanges.remove(0);
        // saving the reservation in the timeslot event

        timeslotList.addAll(getFDTimeslotListForDateRange(restrictions, dateRanges, result, address, user, deliveryModel, event, stats, forceOrder, deliveryInfo, timeSlotContext));

        // list of timeslots that must be shown regardless of capacity
        Set<String> retainTimeslotIds = new HashSet<String>();
        if (user.getReservation() != null) {
            // make sure current reservation is shown
            retainTimeslotIds.add(user.getReservation().getTimeslotId());
        }
        if (cart instanceof FDModifyCartModel) {
            // make sure original reservation still shown
            String tsId = ((FDModifyCartModel) cart).getOriginalReservationId();
            retainTimeslotIds.add(tsId);
        }

        List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, getAlcoholRestrictionReasons(cart), baseRange);
        // Filter Alcohol restrictions by current State and county.
        final String county = FDDeliveryManager.getInstance().getCounty(address);
        List<RestrictionI> alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(address.getState(), county, r);

        LOGGER.debug("AlcoholRestrictions :" + alcoholRestrictions.size());

        /* setDiscounts & apply Geo-restrictions to timeslots */

        TimeslotLogic.filterDeliveryTimeSlots(user, restrictions, timeslotList, retainTimeslotIds, deliveryModel, alcoholRestrictions, forceOrder, address, false, stats);

        // TimeSlot event specific block
        try {
            if (deliveryModel.getRsv() != null && deliveryModel.isPreReserved())
                event.setReservationId(deliveryModel.getRsv().getId());

            if (cart instanceof FDModifyCartModel && deliveryModel.getRsv() != null
                    && (address.getPK() != null && address.getPK().getId() != null && address.getPK().getId().equals(deliveryModel.getRsv().getAddressId())))
                event.setReservationId(deliveryModel.getRsv().getId());

            event.setSameDay(deliveryModel.isShowPremiumSlots() ? "X" : "");
            // build session event
            if (FDStoreProperties.isSessionLoggingEnabled() && result.isSuccess() && isEventLogged) {
                TimeslotLogic.logTimeslotSessionInfo(user, address, deliveryInfo, timeslotList, event);
            }

        } catch (Exception e) {
            // eat it
            LOGGER.info("reservation capture in timeslot event failed");
        }

        // Post-op: remove unnecessary timeslots
        TimeslotLogic.purge(timeslotList);

        // unflag variable min order timeslots for deliveryinfo timeslots
        if (timeSlotContext != null && !timeSlotContext.equals(TimeslotContext.CHECKOUT_TIMESLOTS)) {
            TimeslotLogic.clearVariableMinimum(user, timeslotList);
            deliveryModel.setMinOrderReqd(false);
        }

        }
        deliveryModel.setTimeslotList(timeslotList);

        stats.apply(deliveryModel);
        // update chefs table stats in user
        stats.apply(user);

        user.applyOrderMinimum();

        deliveryModel.setZoneId(cart.getDeliveryZone());

        // get zone Promotion amount
        deliveryModel.setZonePromoAmount(PromotionHelper.getDiscount(user, deliveryModel.getZoneId()));
        // set cart to model
        deliveryModel.setShoppingCart(cart);

        deliveryModel.setSameDayCutoff(stats.getSameDayCutoff());
        deliveryModel.setSameDayCutoffUTC(stats.getSameDayCutoffUTC());

        result.setDeliveryTimeslotModel(deliveryModel);

        return result;
    }

    public List<FDTimeslotUtil> getFDTimeslotListForDateRange(DlvRestrictionsList restrictions, List<DateRange> dateRanges, ActionResult result, ErpAddressModel address,
            FDUserI user, FDDeliveryTimeslotModel deliveryModel, TimeslotEvent event, DlvTimeslotStats stats, boolean forceOrder, boolean deliveryInfo,
            TimeslotContext timeSlotContext) throws FDResourceException {

        List<FDTimeslotUtil> tsuList = new ArrayList<FDTimeslotUtil>();
        int responseTime;

        FDDeliveryTimeslots deliveryTimeslots = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(dateRanges, event, TimeslotLogic.encodeCustomer(address, user),
                forceOrder, deliveryInfo, TimeslotLogic.getOrderContext(user), timeSlotContext, false);

        for (FDTimeslotList timeslotList : deliveryTimeslots.getTimeslotList()) {

            if (timeslotList == null || timeslotList.getError() != null) {
                result.addError(new ActionError("deliveryTime",
                        "We are sorry. Our system is temporarily experiencing a problem "
                                + "displaying the available timeslots. Please try to refresh this page in about three minutes. "
                                + "If you continue to experience difficulties loading this page, " + "please call our customer service department"
                                + (user != null ? " at " + user.getCustomerServiceContact() : "")));
            }

            List<FDTimeslot> timeslots = timeslotList.getTimeslots();
            responseTime = timeslotList.getResponseTime();

            for (DateRange range : dateRanges) {
                if (timeslotList.getRange().overlaps(range)) {
                    timeslotList.setAdvanced(range.isAdvanced());
                }
            }

            tsuList.add(new FDTimeslotUtil(timeslots, timeslotList.getRange().getStartDate(), timeslotList.getRange().getEndDate(), restrictions, responseTime,
                    timeslotList.isAdvanced(), timeslotList.getEventPk()));

        }

        stats.apply(deliveryTimeslots);
        deliveryModel.apply(deliveryTimeslots);
        return tsuList;
    }

    public DateRange createDateRange(int begin, int end) {
        Calendar begCal = Calendar.getInstance();
        begCal.add(Calendar.DATE, begin);
        begCal = DateUtil.truncate(begCal);

        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.DATE, end);
        endCal = DateUtil.truncate(endCal);

        return new DateRange(begCal.getTime(), endCal.getTime());
    }

    @SuppressWarnings("unchecked")
    public Set<EnumDlvRestrictionReason> getAlcoholRestrictionReasons(FDCartModel cart) {
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

    private void calculateSOFirstDeilveryDate(FDStandingOrder so, FDUserI user, FDDeliveryTimeslotModel deliveryModel) throws FDResourceException {

        if (user != null) {
            boolean flg = false;
            FDOrderHistory h = (FDOrderHistory) user.getOrderHistory();
            if (null != h.getErpSaleInfos() && !h.getErpSaleInfos().isEmpty()) {
				for (ErpSaleInfo fdOrderInfo : h.getErpSaleInfos()) {
					// to prevent from already settled order to pass trough to manipulating dates on SO template		//COS17-51
					if (so.getId().equalsIgnoreCase(fdOrderInfo.getStandingOrderId())
							&& !fdOrderInfo.getStatus().isCanceled()
							&& fdOrderInfo.getDeliveryCutoffTime().after(new Date())
							&& FDStandingOrdersManager.getInstance().isModifiable(fdOrderInfo)
							&& so.getFrequency() > 0) {
									flg = true;
									break;
					}
				}
            }

            if (flg) {
                for (FDTimeslotUtil fdTimeslotUtil : deliveryModel.getTimeslotList()) {
                	Date aheadDays = new Date();
                	Calendar cal = Calendar.getInstance();
                	cal.add(Calendar.DAY_OF_MONTH, 9);			// as we are getting timeslots for SO3-user from(2, 9) date ranges 
                	cal.set(Calendar.HOUR_OF_DAY, 0);
                	cal.set(Calendar.MINUTE, 0);
                	aheadDays = cal.getTime();
                    for (FDTimeslot fdTimeslot : fdTimeslotUtil.getTimeslotsFlat()) {
                    	// preventing manipulating of timeslots if firstDeliveryDate from logistics falls out from date ranges(2,9)	   //COS17-51
                    	if (null != fdTimeslot.getSoFirstDeliveryDate() && null != fdTimeslot.getDeliveryDate() && fdTimeslot.getSoFirstDeliveryDate().before(aheadDays)) {
                            fdTimeslot.setSoFirstDeliveryDate(getSubsequentDeliveryDate(fdTimeslot.getDeliveryDate(), so.getFrequency()));
                        }
                    }
                }
            }
        }
    }

    public Date getSubsequentDeliveryDate(Date baseDate, int frequency) {
        // calculate next delivery

        Calendar cl = Calendar.getInstance();
        cl.setTime(baseDate);

        cl.add(Calendar.DATE, 7 * frequency);

        cl.set(Calendar.HOUR, 0);
        cl.set(Calendar.MINUTE, 0);

        return cl.getTime();
    }

    private void checkForPreReservedSlotId(FDDeliveryTimeslotModel deliverymodel, FDCartModel cart, FDUserI user, String timeSlotId, ErpAddressModel address)
            throws FDResourceException {
        FDReservation rsv = null;
        boolean hasPreReserved = false;
        String preReserveSlotId = "";
        ErpDepotAddressModel depotAddress = null;

        EnumCheckoutMode checkOutMode = user.getCheckoutMode();
        if (EnumCheckoutMode.NORMAL == checkOutMode || EnumCheckoutMode.CREATE_SO == checkOutMode || EnumCheckoutMode.MODIFY_SO_MSOI == checkOutMode) {
            rsv = user.getReservation();
            if (rsv != null) {
                preReserveSlotId = rsv.getTimeslotId();
                hasPreReserved = address.getPK() != null
                        && !TimeslotLogic.isAddressChange(rsv.getAddress(), cart.getDeliveryAddress(), rsv.getAddressId(), address.getPK().getId());
            }
            if (cart.getDeliveryReservation() != null) {
                rsv = cart.getDeliveryReservation();
            }
            if (timeSlotId == null) {
                if (address != null && address instanceof ErpDepotAddressModel) {
                    depotAddress = (ErpDepotAddressModel) address;
                    if (depotAddress.isPickup() && rsv != null && cart.getDeliveryReservation() != null)
                        timeSlotId = rsv.getTimeslotId();
                    else
                        timeSlotId = "";
                } else {
                    if (rsv != null
                            && (address != null && address.getPK() != null && address.getPK().getId() != null
                                    && !TimeslotLogic.isAddressChange(rsv.getAddress(), cart.getDeliveryAddress(), rsv.getAddressId(), address.getPK().getId()))
                            && cart.getDeliveryReservation() != null) {
                        timeSlotId = rsv.getTimeslotId();
                    } else {
                        timeSlotId = "";
                    }
                }
            }

            if (cart instanceof FDModifyCartModel && rsv != null && !EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType()) && "".equals(preReserveSlotId)
                    && !hasPreReserved) {
                preReserveSlotId = rsv.getTimeslotId();
                hasPreReserved = address.getPK() != null
                        && !TimeslotLogic.isAddressChange(rsv.getAddress(), cart.getDeliveryAddress(), rsv.getAddressId(), address.getPK().getId());
            }

        } else {
            timeSlotId = "";
        }

        deliverymodel.setTimeSlotId(timeSlotId); // this is the ID of selected timeslot
        deliverymodel.setPreReserved(hasPreReserved);
        deliverymodel.setPreReserveSlotId(preReserveSlotId);
        deliverymodel.setRsv(rsv);
    }

    /**
     * Set restrictions based on various cart items
     * 
     * @param cart
     * @param deliveryModel
     */
    private void getRestrictionReason(FDCartModel cart, FDDeliveryTimeslotModel deliveryModel) {
        for (Iterator<EnumDlvRestrictionReason> i = cart.getApplicableRestrictions().iterator(); i.hasNext();) {
            EnumDlvRestrictionReason reason = i.next();
            if (EnumDlvRestrictionReason.THANKSGIVING.equals(reason)) {
                deliveryModel.setThxgivingRestriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.THANKSGIVING_MEALS.equals(reason)) {
                deliveryModel.setThxgiving_meal_Restriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.EASTER.equals(reason)) {
                deliveryModel.setEasterRestriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.EASTER_MEALS.equals(reason)) {
                deliveryModel.setEasterMealRestriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.ALCOHOL.equals(reason)) {
                deliveryModel.setAlcoholRestriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.KOSHER.equals(reason)) {
                deliveryModel.setKosherRestriction(true);
                continue;
            }
            if (EnumDlvRestrictionReason.VALENTINES.equals(reason)) {
                deliveryModel.setValentineRestriction(true);
                continue;
            }
        }
    }

    private List<FDTimeslotUtil> addOnOrderTimeSlot(List<FDTimeslotUtil> timeslotList, FDOrderI order) {
        for (FDTimeslotUtil list : timeslotList) {
            for (Collection<FDTimeslot> col : list.getTimeslots()) {
                for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext();) {
                    FDTimeslot timeslot = k.next();
                    if (!timeslot.getId().equals(order.getDeliveryReservation().getTimeslot().getId()))
                        k.remove();
                }
            }
        }
        return timeslotList;
    }

    public EnumDlvRestrictionReason getNextHoliday(DlvRestrictionsList restrictions, DateRange baseRange, int lookahead) {
        Set<EnumDlvRestrictionReason> holidays = new HashSet<EnumDlvRestrictionReason>();
        for (@SuppressWarnings("unchecked")
        Iterator<EnumDlvRestrictionReason> i = EnumDlvRestrictionReason.iterator(); i.hasNext();) {
            EnumDlvRestrictionReason reason = i.next();
            if (reason.isSpecialHoliday()) {
                holidays.add(reason);
            }
        }

        DateRange holidayRange = new DateRange(baseRange.getStartDate(), DateUtil.addDays(baseRange.getEndDate(), lookahead));
        for (Iterator<RestrictionI> i = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, holidays).iterator(); i.hasNext();) {
            RestrictionI r = i.next();
            if (r instanceof OneTimeReverseRestriction) {
                OneTimeReverseRestriction rev = (OneTimeReverseRestriction) r;
                if (holidayRange.contains(rev.getDateRange().getStartDate())) {
                    return r.getReason();
                }
            }
        }
        return null;
    }

    public List<DateRange> getDateRanges(DateRange period, boolean lookahead, DlvRestrictionsList restrictions, EnumDlvRestrictionReason specialRestriction) {
        return getDateRanges(period, lookahead, restrictions, specialRestriction, false);
    }

    public List<DateRange> getDateRanges(DateRange period, boolean lookahead, DlvRestrictionsList restrictions, EnumDlvRestrictionReason specialRestriction,
            boolean useAdvanceOrderDates) {
        int daysInAdvance = FDStoreProperties.getHolidayLookaheadDays();

        Calendar restrictionEndCal = Calendar.getInstance();
        restrictionEndCal.setTime(period.getStartDate());
        restrictionEndCal.add(Calendar.DATE, daysInAdvance);

        List<RestrictionI> specialRestrictions = Collections.<RestrictionI> emptyList();
        if (specialRestriction != null) {
            specialRestrictions = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, specialRestriction, null,
                    new DateRange(period.getStartDate(), restrictionEndCal.getTime()));

        }

        DateRange restrictionRange = null;
        for (Iterator<RestrictionI> i = specialRestrictions.iterator(); i.hasNext();) {
            RestrictionI o = i.next();
            if (o instanceof OneTimeRestriction) {
                restrictionRange = ((OneTimeRestriction) o).getDateRange();
            } else if (o instanceof OneTimeReverseRestriction && useAdvanceOrderDates) {
                restrictionRange = ((OneTimeReverseRestriction) o).getDateRange();
            }
        }

        DateRange advOrdDateRange = FDStoreProperties.getAdvanceOrderRange();
        boolean isAdvOrderGap = FDStoreProperties.IsAdvanceOrderGap();
        DateRange advOrdNewDateRange = FDStoreProperties.getAdvanceOrderNewRange();
        DateRange restrictionRangeNew = null;

        LOGGER.debug("advOrdDateRange :" + advOrdDateRange);
        LOGGER.debug("advOrdNewDateRange :" + advOrdNewDateRange);

        if (useAdvanceOrderDates && advOrdDateRange.overlaps(new DateRange(period.getStartDate(), DateUtil.addDays(period.getStartDate(), daysInAdvance)))) {
            // get the advanced Dlv date range and factor them into the date range

            // adjust the end and start range
            if (restrictionRange == null) {
                restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate(), true);
            } else {
                if (advOrdDateRange.getEndDate().after(restrictionRange.getEndDate())) {
                    restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate(), true);
                } else if (daysInAdvance < (restrictionRange.getStartDate().getTime() - period.getStartDate().getTime()) / DateUtil.DAY) {
                    // if the holiday starts after advance range and holiday is not in the lookahead range then use advance order end date
                    if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate())) {
                        restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate(), true);
                    } else {
                        restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate(), true);
                    }
                }

                if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate())) {
                    restrictionRange = new DateRange(advOrdDateRange.getStartDate(), restrictionRange.getEndDate(), true);
                }
            }
        }

        // Get the second advance order range for gaps
        if (useAdvanceOrderDates && advOrdNewDateRange.overlaps(new DateRange(period.getStartDate(), DateUtil.addDays(period.getStartDate(), daysInAdvance)))) {
            // adjust the end and start range
            restrictionRangeNew = new DateRange(advOrdNewDateRange.getStartDate(), advOrdNewDateRange.getEndDate(), true);
        } // Advance order range for gaps

        // shrink the date range if it is more than 7
        if (restrictionRange != null && !isAdvOrderGap
                && (restrictionRange.getEndDate().getTime() - restrictionRange.getStartDate().getTime()) / DateUtil.DAY > ErpServicesProperties.getHorizonDays()) {
            restrictionRange = new DateRange(restrictionRange.getStartDate(), DateUtil.addDays(restrictionRange.getStartDate(), ErpServicesProperties.getHorizonDays()), true);
        } else if ((restrictionRange != null && isAdvOrderGap
                && (restrictionRange.getEndDate().getTime() - restrictionRange.getStartDate().getTime()) / DateUtil.DAY >= ErpServicesProperties.getHorizonDays())
                || restrictionRangeNew != null && isAdvOrderGap
                        && (restrictionRangeNew.getEndDate().getTime() - restrictionRangeNew.getStartDate().getTime()) / DateUtil.DAY >= ErpServicesProperties.getHorizonDays()) {
            // if there is advance order timeslots with a gap then read the second date range from properties
            restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate(), true);
            restrictionRangeNew = new DateRange(advOrdNewDateRange.getStartDate(), advOrdNewDateRange.getEndDate(), true);

        }

        LOGGER.debug("restrictionRange :" + restrictionRange);
        LOGGER.debug("restrictionRangeNew  :" + restrictionRangeNew);

        List<DateRange> lst = new ArrayList<DateRange>();
        int dayDiff = 0;
        if (restrictionRange != null) {
            dayDiff = (int) ((restrictionRange.getStartDate().getTime() - period.getStartDate().getTime()) / DateUtil.DAY);
        }

        int remainingDays = restrictionRange == null ? 0 : (int) ((restrictionRange.getEndDate().getTime() - period.getEndDate().getTime()) / DateUtil.DAY);

        // If there is advance order with gap read the second date range along with first range
        if (isAdvOrderGap) {
            int dayDiffNew = 0;
            if (restrictionRangeNew != null) {
                dayDiffNew = (int) ((restrictionRangeNew.getStartDate().getTime() - period.getStartDate().getTime()) / DateUtil.DAY);
            }
            int remainingDaysNew = restrictionRangeNew == null ? 0 : (int) ((restrictionRangeNew.getEndDate().getTime() - period.getEndDate().getTime()) / DateUtil.DAY);

            // read the first advance order date range
            if (dayDiff > daysInAdvance || dayDiff < 6 || (!lookahead && !useAdvanceOrderDates)) {
                if (dayDiff < 6 && remainingDays > 0) {
                    lst.add(new DateRange(period.getEndDate(), restrictionRange.getEndDate(), true));
                } else {

                }

            } else if (dayDiff > 6) {
                lst.add(restrictionRange);
            } else {
                restrictionRange = new DateRange(DateUtil.addDays(restrictionRange.getStartDate(), 1), restrictionRange.getEndDate(), true);
                lst.add(restrictionRange);
            }

            // read the second advance order date range
            if (dayDiffNew > daysInAdvance || dayDiffNew < 6 || (!lookahead && !useAdvanceOrderDates)) {
                if (dayDiffNew < 6 && remainingDaysNew > 0) {
                    lst.add(new DateRange(period.getEndDate(), restrictionRangeNew.getEndDate(), true));
                }

            } else if (dayDiffNew > 6) {
                lst.add(restrictionRangeNew);
            } else {
                restrictionRangeNew = new DateRange(DateUtil.addDays(restrictionRangeNew.getStartDate(), 1), restrictionRangeNew.getEndDate(), true);
                lst.add(restrictionRangeNew);
            }
            lst.add(period);
        } else if (dayDiff > daysInAdvance || dayDiff < 6 || (!lookahead && !useAdvanceOrderDates)) {
            if (dayDiff < 6 && remainingDays > 0) {
                lst.add(new DateRange(period.getEndDate(), restrictionRange.getEndDate(), true));
            }
            lst.add(period);
        } else if (dayDiff > 6) {
            lst.add(restrictionRange);
            lst.add(period);
        } else {
            restrictionRange = new DateRange(DateUtil.addDays(restrictionRange.getStartDate(), 1), restrictionRange.getEndDate(), true);
            lst.add(restrictionRange);
            lst.add(period);
        }

        return lst;
    }

    public List<RestrictionI> getHolidayRestrictions(DlvRestrictionsList restrictions, List<DateRange> dateRanges, FDDeliveryTimeslotModel deliveryModel) {
        List<RestrictionI> holidayRes = new ArrayList<RestrictionI>();
        for (Iterator<DateRange> i = dateRanges.iterator(); i.hasNext();) {
            DateRange validRange = i.next();

            // DateRange validRange = new DateRange(baseRange.getStartDate(), baseRange.getEndDate());

            for (Iterator<RestrictionI> itr = restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, validRange).iterator(); itr.hasNext();) {
                RestrictionI r = itr.next();
                holidayRes.add(r);
            }
        }
        deliveryModel.setHolidayRestrictions(holidayRes);
        return holidayRes;
    }

    public boolean isTimeslotAlcoholRestricted(List<RestrictionI> alcoholRestrictions, FDTimeslot slot) {
        if (alcoholRestrictions.size() > 0 && slot != null) {
            DateRange slotRange = new DateRange(slot.getStartDateTime(), slot.getEndDateTime());
            for (Iterator<RestrictionI> i = alcoholRestrictions.iterator(); i.hasNext();) {
                RestrictionI r = i.next();

                if (r instanceof AlcoholRestriction) {
                    AlcoholRestriction ar = (AlcoholRestriction) r;
                    if (ar.overlaps(slotRange))
                        return true;
                }
            }
        }
        return false;
    }

    private class DateRangeComparator implements Comparator<DateRange> {

        @Override
        public int compare(DateRange obj1, DateRange obj2) {
            if (obj1.getStartDate() != null && obj2.getStartDate() != null) {
                return -(obj2.getStartDate().compareTo(obj1.getStartDate()));
            }
            return 0;
        }
    }

}
