package com.freshdirect.framework.util;


public class Counter implements Cloneable, CounterMBean {
    public final static boolean PROFILE = false;
    
    
    int callCount;
    long allTime;
    long maxTime;
    long failed;
    String name;

    public Counter() {
    }

    public Counter(String name) {
        this.name = name;
    }

    public synchronized void failedCall() {
        failed++;
    }

    public synchronized void call(long time) {
        maxTime = Math.max(maxTime, time);
        callCount++;
        allTime += time;
    }

    public long getAllTime() {
        return allTime;
    }

    public int getCallCount() {
        return callCount;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public double getAverageTime() {
        return callCount > 0 ? ((double) allTime / (double) callCount) : 0.0;
    }
    
    public double getAverageWithoutMaximum() {
        return callCount > 1 ? ((double) (allTime - maxTime) / (double) (callCount - 1)) : 0.0;
    }

    public long start() {
        return System.currentTimeMillis();
    }

    public long end(long startime) {
        long elapsed = System.currentTimeMillis() - startime;
        call(elapsed);
        return elapsed;
    }

    @Override
    public Counter clone() {
        try {
            return (Counter) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public long getFailed() {
        return failed;
    }

    public String getName() {
        return name;
    }

    public Counter register() {
        JMXUtil.registerToPlatformMBean(this, "com.freshdirect.framework.counter", "Counter", name);
        return this;
    }
    
    public synchronized void reset() {
        callCount = 0;
        allTime = 0;
        maxTime = 0;
        failed = 0;
                
    }
}
