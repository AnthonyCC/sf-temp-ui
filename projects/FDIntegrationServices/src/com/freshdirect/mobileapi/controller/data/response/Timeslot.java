package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.DateFormat;

public class Timeslot implements DateFormat {

    private boolean full = false;

    private String id;

    private Date start;

    private Date end;

    private Date cutoffDate;

    private Boolean isChefsTable;

    private final SimpleDateFormat formatter = new SimpleDateFormat(STANDARDIZED_DATE_FORMAT);

    public static List<Timeslot> initWithList(List<com.freshdirect.mobileapi.model.Timeslot> slots) {
        List<Timeslot> newInstances = new ArrayList<Timeslot>();
        for (com.freshdirect.mobileapi.model.Timeslot slot : slots) {
            newInstances.add(new Timeslot(slot));
        }
        return newInstances;
    }

    public Timeslot(com.freshdirect.mobileapi.model.Timeslot slot) {
        this.id = slot.getTimeslotId();
        this.start = slot.getBegDateTime();
        this.end = slot.getEndDateTime();
        this.cutoffDate = slot.getCutoffDateTime();
        this.isChefsTable = slot.isChefsTable();
        this.full = slot.isFull();
    }

    public Timeslot(Date rangeStart, Date rangeEnd, Date cutoff) {
        this.start = rangeStart;
        this.end = rangeEnd;
        this.cutoffDate = cutoff;
    }

    public String getStart() {
        return formatter.format(this.start);
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setStart(String start) throws ParseException {
        this.start = formatter.parse(start);
    }

    public String getEnd() {
        return formatter.format(this.end);
    }

    public void setEnd(String end) throws ParseException {
        this.end = formatter.parse(end);
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getCutoffDate() {
        return formatter.format(this.cutoffDate);
    }

    public void setCutoffDate(Date cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public Boolean isChefsTable() {
        return isChefsTable;
    }

    public void setChefsTable(Boolean isChefsTable) {
        this.isChefsTable = isChefsTable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFull() {
        return full;
    }

}
