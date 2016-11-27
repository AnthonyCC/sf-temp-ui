package com.freshdirect.framework.util;

public interface CounterMBean {

    public String getName();

    public long getAllTime();

    public int getCallCount();

    public long getMaxTime();

    public double getAverageTime();

    public double getAverageWithoutMaximum();
    
    public long getFailed();


    public void reset();
}
