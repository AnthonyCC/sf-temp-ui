package com.freshdirect.webapp.ajax.expresscheckout.timeslot.data;

import java.util.List;

public class FormTimeslotData {

    private String id;
    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String year;
    private String timePeriod;
    private List<String> onOpenCoremetrics;
    private boolean showForceOrder;
    private boolean forceOrderEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<String> getOnOpenCoremetrics() {
        return onOpenCoremetrics;
    }

    public void setOnOpenCoremetrics(List<String> onOpenCoremetrics) {
        this.onOpenCoremetrics = onOpenCoremetrics;
    }

    public boolean isShowForceOrder() {
        return showForceOrder;
    }

    public void setShowForceOrder(boolean showForceOrder) {
        this.showForceOrder = showForceOrder;
    }

    
    public boolean isForceOrderEnabled() {
        return forceOrderEnabled;
    }

    
    public void setForceOrderEnabled(boolean forceOrderEnabled) {
        this.forceOrderEnabled = forceOrderEnabled;
    }
}
