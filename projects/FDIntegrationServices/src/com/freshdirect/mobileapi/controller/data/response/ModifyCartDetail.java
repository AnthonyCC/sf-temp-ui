package com.freshdirect.mobileapi.controller.data.response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Rob
 *
 */
public class ModifyCartDetail extends CartDetail {

    private String orderNumber;

    private Date reservationCutoff;

    private Date priceLockCutoff;

    private DateFormat dateFormatter = new SimpleDateFormat(com.freshdirect.mobileapi.controller.data.DateFormat.STANDARDIZED_DATE_FORMAT);

    public String getPriceLockCutoff() {
        return dateFormatter.format(priceLockCutoff);
    }

    public void setPriceLockCutoff(String priceLockCutoff) throws ParseException {
        this.priceLockCutoff = dateFormatter.parse(priceLockCutoff);
    }

    public void setPriceLockCutoff(Date priceLockCutoff) {
        this.priceLockCutoff = priceLockCutoff;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReservationCutoff() {
        return dateFormatter.format(reservationCutoff);
    }

    public void setReservationCutoff(Date reservationCutoff) {
        this.reservationCutoff = reservationCutoff;
    }

    public void setReservationCutoff(String reservationCutoff) throws ParseException {
        this.reservationCutoff = dateFormatter.parse(reservationCutoff);
    }
}
