package com.freshdirect.webapp.unbxdanalytics.visitor;

public class VisitType {

    public final static String VISITOR_TYPE_VALUE_FIRST = "first_time";
    public final static String VISITOR_TYPE_VALUE_REPEAT = "repeat";

    public final boolean repeat;
    public final boolean visitorEventRequired;

    public VisitType(boolean repeat, boolean visitEventRequired) {
        this.repeat = repeat;
        this.visitorEventRequired = visitEventRequired;
    }

    @Override
    public String toString() {
        return repeat ? VISITOR_TYPE_VALUE_REPEAT : VISITOR_TYPE_VALUE_FIRST;
    }
}
