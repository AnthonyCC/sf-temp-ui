package com.freshdirect.webapp.ajax.expresscheckout.timeslot.service;

import java.util.Date;

import org.jmock.Expectations;

import com.freshdirect.Fixture;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;

public class TimeslotServiceTest extends Fixture {

    private FDUserI user;
    private FDCartI order;
    private FDReservation reservation;
    private TimeslotService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        user = context.mock(FDUserI.class, "user");
        order = context.mock(FDOrderI.class, "order");
        reservation = context.mock(FDReservation.class, "reservation");

        service = TimeslotService.defaultService();
    }

    public void testLoadCartTimeslotSuccessfully() {
        final Date startTime = new Date(1431381600000L);
        final Date endTime = new Date(1431424800000L);
        final String timeslotId = "timeslotId";
        String year = "2015";
        String month = "5";
        String dayOfMonth = "12";
        String dayOfWeek = "Tuesday";
        String timePeriod = "0AM - 12PM";

        context.checking(new Expectations() {

            {
                oneOf(order).getDeliveryReservation();
                will(returnValue(reservation));

                oneOf(reservation).getStartTime();
                will(returnValue(startTime));

                oneOf(reservation).getTimeslotId();
                will(returnValue(timeslotId));

                oneOf(reservation).getEndTime();
                will(returnValue(endTime));
                
                exactly(2).of(user).getMasqueradeContext();
                will(returnValue(null));
            }
        });

        FormTimeslotData timeslot = service.loadCartTimeslot(user, order);

        assertEquals(timeslotId, timeslot.getId());
        assertEquals(year, timeslot.getYear());
        assertEquals(month, timeslot.getMonth());
        assertEquals(dayOfMonth, timeslot.getDayOfMonth());
        assertEquals(dayOfWeek, timeslot.getDayOfWeek());
        assertEquals(timePeriod, timeslot.getTimePeriod());
    }

}
