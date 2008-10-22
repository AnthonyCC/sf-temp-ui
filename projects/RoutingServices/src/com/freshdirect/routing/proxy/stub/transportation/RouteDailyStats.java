/**
 * RouteDailyStats.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RouteDailyStats  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsIdentity routeDailyStatsIdentity;

    private int routeCount;

    private int totalPlannedServiceTime;

    private int minPlannedServiceTime;

    private int maxPlannedServiceTime;

    private int avgPlannedServiceTime;

    private int totalActualServiceTime;

    private int minActualServiceTime;

    private int maxActualServiceTime;

    private int avgActualServiceTime;

    private int totalPlannedTravelTime;

    private int minPlannedTravelTime;

    private int maxPlannedTravelTime;

    private int avgPlannedTravelTime;

    private int totalActualTravelTime;

    private int minActualTravelTime;

    private int maxActualTravelTime;

    private int avgActualTravelTime;

    private double totalPlannedDistance;

    private double minPlannedDistance;

    private double maxPlannedDistance;

    private double avgPlannedDistance;

    private double totalActualDistance;

    private double minActualDistance;

    private double maxActualDistance;

    private double avgActualDistance;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities totalPlannedQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities minPlannedQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities maxPlannedQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities avgPlannedQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities totalActualQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities minActualQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities maxActualQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities avgActualQuantity;

    private int totalPlannedStopCount;

    private int minPlannedStopCount;

    private int maxPlannedStopCount;

    private int avgPlannedStopCount;

    private int totalActualStopCount;

    private int minActualStopCount;

    private int maxActualStopCount;

    private int avgActualStopCount;

    private int totalPlannedCost;

    private int minPlannedCost;

    private int maxPlannedCost;

    private int avgPlannedCost;

    private int totalActualCost;

    private int minActualCost;

    private int maxActualCost;

    private int avgActualCost;

    public RouteDailyStats() {
    }

    public RouteDailyStats(
           com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsIdentity routeDailyStatsIdentity,
           int routeCount,
           int totalPlannedServiceTime,
           int minPlannedServiceTime,
           int maxPlannedServiceTime,
           int avgPlannedServiceTime,
           int totalActualServiceTime,
           int minActualServiceTime,
           int maxActualServiceTime,
           int avgActualServiceTime,
           int totalPlannedTravelTime,
           int minPlannedTravelTime,
           int maxPlannedTravelTime,
           int avgPlannedTravelTime,
           int totalActualTravelTime,
           int minActualTravelTime,
           int maxActualTravelTime,
           int avgActualTravelTime,
           double totalPlannedDistance,
           double minPlannedDistance,
           double maxPlannedDistance,
           double avgPlannedDistance,
           double totalActualDistance,
           double minActualDistance,
           double maxActualDistance,
           double avgActualDistance,
           com.freshdirect.routing.proxy.stub.transportation.Quantities totalPlannedQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities minPlannedQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities maxPlannedQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities avgPlannedQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities totalActualQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities minActualQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities maxActualQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities avgActualQuantity,
           int totalPlannedStopCount,
           int minPlannedStopCount,
           int maxPlannedStopCount,
           int avgPlannedStopCount,
           int totalActualStopCount,
           int minActualStopCount,
           int maxActualStopCount,
           int avgActualStopCount,
           int totalPlannedCost,
           int minPlannedCost,
           int maxPlannedCost,
           int avgPlannedCost,
           int totalActualCost,
           int minActualCost,
           int maxActualCost,
           int avgActualCost) {
           this.routeDailyStatsIdentity = routeDailyStatsIdentity;
           this.routeCount = routeCount;
           this.totalPlannedServiceTime = totalPlannedServiceTime;
           this.minPlannedServiceTime = minPlannedServiceTime;
           this.maxPlannedServiceTime = maxPlannedServiceTime;
           this.avgPlannedServiceTime = avgPlannedServiceTime;
           this.totalActualServiceTime = totalActualServiceTime;
           this.minActualServiceTime = minActualServiceTime;
           this.maxActualServiceTime = maxActualServiceTime;
           this.avgActualServiceTime = avgActualServiceTime;
           this.totalPlannedTravelTime = totalPlannedTravelTime;
           this.minPlannedTravelTime = minPlannedTravelTime;
           this.maxPlannedTravelTime = maxPlannedTravelTime;
           this.avgPlannedTravelTime = avgPlannedTravelTime;
           this.totalActualTravelTime = totalActualTravelTime;
           this.minActualTravelTime = minActualTravelTime;
           this.maxActualTravelTime = maxActualTravelTime;
           this.avgActualTravelTime = avgActualTravelTime;
           this.totalPlannedDistance = totalPlannedDistance;
           this.minPlannedDistance = minPlannedDistance;
           this.maxPlannedDistance = maxPlannedDistance;
           this.avgPlannedDistance = avgPlannedDistance;
           this.totalActualDistance = totalActualDistance;
           this.minActualDistance = minActualDistance;
           this.maxActualDistance = maxActualDistance;
           this.avgActualDistance = avgActualDistance;
           this.totalPlannedQuantity = totalPlannedQuantity;
           this.minPlannedQuantity = minPlannedQuantity;
           this.maxPlannedQuantity = maxPlannedQuantity;
           this.avgPlannedQuantity = avgPlannedQuantity;
           this.totalActualQuantity = totalActualQuantity;
           this.minActualQuantity = minActualQuantity;
           this.maxActualQuantity = maxActualQuantity;
           this.avgActualQuantity = avgActualQuantity;
           this.totalPlannedStopCount = totalPlannedStopCount;
           this.minPlannedStopCount = minPlannedStopCount;
           this.maxPlannedStopCount = maxPlannedStopCount;
           this.avgPlannedStopCount = avgPlannedStopCount;
           this.totalActualStopCount = totalActualStopCount;
           this.minActualStopCount = minActualStopCount;
           this.maxActualStopCount = maxActualStopCount;
           this.avgActualStopCount = avgActualStopCount;
           this.totalPlannedCost = totalPlannedCost;
           this.minPlannedCost = minPlannedCost;
           this.maxPlannedCost = maxPlannedCost;
           this.avgPlannedCost = avgPlannedCost;
           this.totalActualCost = totalActualCost;
           this.minActualCost = minActualCost;
           this.maxActualCost = maxActualCost;
           this.avgActualCost = avgActualCost;
    }


    /**
     * Gets the routeDailyStatsIdentity value for this RouteDailyStats.
     * 
     * @return routeDailyStatsIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsIdentity getRouteDailyStatsIdentity() {
        return routeDailyStatsIdentity;
    }


    /**
     * Sets the routeDailyStatsIdentity value for this RouteDailyStats.
     * 
     * @param routeDailyStatsIdentity
     */
    public void setRouteDailyStatsIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsIdentity routeDailyStatsIdentity) {
        this.routeDailyStatsIdentity = routeDailyStatsIdentity;
    }


    /**
     * Gets the routeCount value for this RouteDailyStats.
     * 
     * @return routeCount
     */
    public int getRouteCount() {
        return routeCount;
    }


    /**
     * Sets the routeCount value for this RouteDailyStats.
     * 
     * @param routeCount
     */
    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }


    /**
     * Gets the totalPlannedServiceTime value for this RouteDailyStats.
     * 
     * @return totalPlannedServiceTime
     */
    public int getTotalPlannedServiceTime() {
        return totalPlannedServiceTime;
    }


    /**
     * Sets the totalPlannedServiceTime value for this RouteDailyStats.
     * 
     * @param totalPlannedServiceTime
     */
    public void setTotalPlannedServiceTime(int totalPlannedServiceTime) {
        this.totalPlannedServiceTime = totalPlannedServiceTime;
    }


    /**
     * Gets the minPlannedServiceTime value for this RouteDailyStats.
     * 
     * @return minPlannedServiceTime
     */
    public int getMinPlannedServiceTime() {
        return minPlannedServiceTime;
    }


    /**
     * Sets the minPlannedServiceTime value for this RouteDailyStats.
     * 
     * @param minPlannedServiceTime
     */
    public void setMinPlannedServiceTime(int minPlannedServiceTime) {
        this.minPlannedServiceTime = minPlannedServiceTime;
    }


    /**
     * Gets the maxPlannedServiceTime value for this RouteDailyStats.
     * 
     * @return maxPlannedServiceTime
     */
    public int getMaxPlannedServiceTime() {
        return maxPlannedServiceTime;
    }


    /**
     * Sets the maxPlannedServiceTime value for this RouteDailyStats.
     * 
     * @param maxPlannedServiceTime
     */
    public void setMaxPlannedServiceTime(int maxPlannedServiceTime) {
        this.maxPlannedServiceTime = maxPlannedServiceTime;
    }


    /**
     * Gets the avgPlannedServiceTime value for this RouteDailyStats.
     * 
     * @return avgPlannedServiceTime
     */
    public int getAvgPlannedServiceTime() {
        return avgPlannedServiceTime;
    }


    /**
     * Sets the avgPlannedServiceTime value for this RouteDailyStats.
     * 
     * @param avgPlannedServiceTime
     */
    public void setAvgPlannedServiceTime(int avgPlannedServiceTime) {
        this.avgPlannedServiceTime = avgPlannedServiceTime;
    }


    /**
     * Gets the totalActualServiceTime value for this RouteDailyStats.
     * 
     * @return totalActualServiceTime
     */
    public int getTotalActualServiceTime() {
        return totalActualServiceTime;
    }


    /**
     * Sets the totalActualServiceTime value for this RouteDailyStats.
     * 
     * @param totalActualServiceTime
     */
    public void setTotalActualServiceTime(int totalActualServiceTime) {
        this.totalActualServiceTime = totalActualServiceTime;
    }


    /**
     * Gets the minActualServiceTime value for this RouteDailyStats.
     * 
     * @return minActualServiceTime
     */
    public int getMinActualServiceTime() {
        return minActualServiceTime;
    }


    /**
     * Sets the minActualServiceTime value for this RouteDailyStats.
     * 
     * @param minActualServiceTime
     */
    public void setMinActualServiceTime(int minActualServiceTime) {
        this.minActualServiceTime = minActualServiceTime;
    }


    /**
     * Gets the maxActualServiceTime value for this RouteDailyStats.
     * 
     * @return maxActualServiceTime
     */
    public int getMaxActualServiceTime() {
        return maxActualServiceTime;
    }


    /**
     * Sets the maxActualServiceTime value for this RouteDailyStats.
     * 
     * @param maxActualServiceTime
     */
    public void setMaxActualServiceTime(int maxActualServiceTime) {
        this.maxActualServiceTime = maxActualServiceTime;
    }


    /**
     * Gets the avgActualServiceTime value for this RouteDailyStats.
     * 
     * @return avgActualServiceTime
     */
    public int getAvgActualServiceTime() {
        return avgActualServiceTime;
    }


    /**
     * Sets the avgActualServiceTime value for this RouteDailyStats.
     * 
     * @param avgActualServiceTime
     */
    public void setAvgActualServiceTime(int avgActualServiceTime) {
        this.avgActualServiceTime = avgActualServiceTime;
    }


    /**
     * Gets the totalPlannedTravelTime value for this RouteDailyStats.
     * 
     * @return totalPlannedTravelTime
     */
    public int getTotalPlannedTravelTime() {
        return totalPlannedTravelTime;
    }


    /**
     * Sets the totalPlannedTravelTime value for this RouteDailyStats.
     * 
     * @param totalPlannedTravelTime
     */
    public void setTotalPlannedTravelTime(int totalPlannedTravelTime) {
        this.totalPlannedTravelTime = totalPlannedTravelTime;
    }


    /**
     * Gets the minPlannedTravelTime value for this RouteDailyStats.
     * 
     * @return minPlannedTravelTime
     */
    public int getMinPlannedTravelTime() {
        return minPlannedTravelTime;
    }


    /**
     * Sets the minPlannedTravelTime value for this RouteDailyStats.
     * 
     * @param minPlannedTravelTime
     */
    public void setMinPlannedTravelTime(int minPlannedTravelTime) {
        this.minPlannedTravelTime = minPlannedTravelTime;
    }


    /**
     * Gets the maxPlannedTravelTime value for this RouteDailyStats.
     * 
     * @return maxPlannedTravelTime
     */
    public int getMaxPlannedTravelTime() {
        return maxPlannedTravelTime;
    }


    /**
     * Sets the maxPlannedTravelTime value for this RouteDailyStats.
     * 
     * @param maxPlannedTravelTime
     */
    public void setMaxPlannedTravelTime(int maxPlannedTravelTime) {
        this.maxPlannedTravelTime = maxPlannedTravelTime;
    }


    /**
     * Gets the avgPlannedTravelTime value for this RouteDailyStats.
     * 
     * @return avgPlannedTravelTime
     */
    public int getAvgPlannedTravelTime() {
        return avgPlannedTravelTime;
    }


    /**
     * Sets the avgPlannedTravelTime value for this RouteDailyStats.
     * 
     * @param avgPlannedTravelTime
     */
    public void setAvgPlannedTravelTime(int avgPlannedTravelTime) {
        this.avgPlannedTravelTime = avgPlannedTravelTime;
    }


    /**
     * Gets the totalActualTravelTime value for this RouteDailyStats.
     * 
     * @return totalActualTravelTime
     */
    public int getTotalActualTravelTime() {
        return totalActualTravelTime;
    }


    /**
     * Sets the totalActualTravelTime value for this RouteDailyStats.
     * 
     * @param totalActualTravelTime
     */
    public void setTotalActualTravelTime(int totalActualTravelTime) {
        this.totalActualTravelTime = totalActualTravelTime;
    }


    /**
     * Gets the minActualTravelTime value for this RouteDailyStats.
     * 
     * @return minActualTravelTime
     */
    public int getMinActualTravelTime() {
        return minActualTravelTime;
    }


    /**
     * Sets the minActualTravelTime value for this RouteDailyStats.
     * 
     * @param minActualTravelTime
     */
    public void setMinActualTravelTime(int minActualTravelTime) {
        this.minActualTravelTime = minActualTravelTime;
    }


    /**
     * Gets the maxActualTravelTime value for this RouteDailyStats.
     * 
     * @return maxActualTravelTime
     */
    public int getMaxActualTravelTime() {
        return maxActualTravelTime;
    }


    /**
     * Sets the maxActualTravelTime value for this RouteDailyStats.
     * 
     * @param maxActualTravelTime
     */
    public void setMaxActualTravelTime(int maxActualTravelTime) {
        this.maxActualTravelTime = maxActualTravelTime;
    }


    /**
     * Gets the avgActualTravelTime value for this RouteDailyStats.
     * 
     * @return avgActualTravelTime
     */
    public int getAvgActualTravelTime() {
        return avgActualTravelTime;
    }


    /**
     * Sets the avgActualTravelTime value for this RouteDailyStats.
     * 
     * @param avgActualTravelTime
     */
    public void setAvgActualTravelTime(int avgActualTravelTime) {
        this.avgActualTravelTime = avgActualTravelTime;
    }


    /**
     * Gets the totalPlannedDistance value for this RouteDailyStats.
     * 
     * @return totalPlannedDistance
     */
    public double getTotalPlannedDistance() {
        return totalPlannedDistance;
    }


    /**
     * Sets the totalPlannedDistance value for this RouteDailyStats.
     * 
     * @param totalPlannedDistance
     */
    public void setTotalPlannedDistance(double totalPlannedDistance) {
        this.totalPlannedDistance = totalPlannedDistance;
    }


    /**
     * Gets the minPlannedDistance value for this RouteDailyStats.
     * 
     * @return minPlannedDistance
     */
    public double getMinPlannedDistance() {
        return minPlannedDistance;
    }


    /**
     * Sets the minPlannedDistance value for this RouteDailyStats.
     * 
     * @param minPlannedDistance
     */
    public void setMinPlannedDistance(double minPlannedDistance) {
        this.minPlannedDistance = minPlannedDistance;
    }


    /**
     * Gets the maxPlannedDistance value for this RouteDailyStats.
     * 
     * @return maxPlannedDistance
     */
    public double getMaxPlannedDistance() {
        return maxPlannedDistance;
    }


    /**
     * Sets the maxPlannedDistance value for this RouteDailyStats.
     * 
     * @param maxPlannedDistance
     */
    public void setMaxPlannedDistance(double maxPlannedDistance) {
        this.maxPlannedDistance = maxPlannedDistance;
    }


    /**
     * Gets the avgPlannedDistance value for this RouteDailyStats.
     * 
     * @return avgPlannedDistance
     */
    public double getAvgPlannedDistance() {
        return avgPlannedDistance;
    }


    /**
     * Sets the avgPlannedDistance value for this RouteDailyStats.
     * 
     * @param avgPlannedDistance
     */
    public void setAvgPlannedDistance(double avgPlannedDistance) {
        this.avgPlannedDistance = avgPlannedDistance;
    }


    /**
     * Gets the totalActualDistance value for this RouteDailyStats.
     * 
     * @return totalActualDistance
     */
    public double getTotalActualDistance() {
        return totalActualDistance;
    }


    /**
     * Sets the totalActualDistance value for this RouteDailyStats.
     * 
     * @param totalActualDistance
     */
    public void setTotalActualDistance(double totalActualDistance) {
        this.totalActualDistance = totalActualDistance;
    }


    /**
     * Gets the minActualDistance value for this RouteDailyStats.
     * 
     * @return minActualDistance
     */
    public double getMinActualDistance() {
        return minActualDistance;
    }


    /**
     * Sets the minActualDistance value for this RouteDailyStats.
     * 
     * @param minActualDistance
     */
    public void setMinActualDistance(double minActualDistance) {
        this.minActualDistance = minActualDistance;
    }


    /**
     * Gets the maxActualDistance value for this RouteDailyStats.
     * 
     * @return maxActualDistance
     */
    public double getMaxActualDistance() {
        return maxActualDistance;
    }


    /**
     * Sets the maxActualDistance value for this RouteDailyStats.
     * 
     * @param maxActualDistance
     */
    public void setMaxActualDistance(double maxActualDistance) {
        this.maxActualDistance = maxActualDistance;
    }


    /**
     * Gets the avgActualDistance value for this RouteDailyStats.
     * 
     * @return avgActualDistance
     */
    public double getAvgActualDistance() {
        return avgActualDistance;
    }


    /**
     * Sets the avgActualDistance value for this RouteDailyStats.
     * 
     * @param avgActualDistance
     */
    public void setAvgActualDistance(double avgActualDistance) {
        this.avgActualDistance = avgActualDistance;
    }


    /**
     * Gets the totalPlannedQuantity value for this RouteDailyStats.
     * 
     * @return totalPlannedQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getTotalPlannedQuantity() {
        return totalPlannedQuantity;
    }


    /**
     * Sets the totalPlannedQuantity value for this RouteDailyStats.
     * 
     * @param totalPlannedQuantity
     */
    public void setTotalPlannedQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities totalPlannedQuantity) {
        this.totalPlannedQuantity = totalPlannedQuantity;
    }


    /**
     * Gets the minPlannedQuantity value for this RouteDailyStats.
     * 
     * @return minPlannedQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getMinPlannedQuantity() {
        return minPlannedQuantity;
    }


    /**
     * Sets the minPlannedQuantity value for this RouteDailyStats.
     * 
     * @param minPlannedQuantity
     */
    public void setMinPlannedQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities minPlannedQuantity) {
        this.minPlannedQuantity = minPlannedQuantity;
    }


    /**
     * Gets the maxPlannedQuantity value for this RouteDailyStats.
     * 
     * @return maxPlannedQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getMaxPlannedQuantity() {
        return maxPlannedQuantity;
    }


    /**
     * Sets the maxPlannedQuantity value for this RouteDailyStats.
     * 
     * @param maxPlannedQuantity
     */
    public void setMaxPlannedQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities maxPlannedQuantity) {
        this.maxPlannedQuantity = maxPlannedQuantity;
    }


    /**
     * Gets the avgPlannedQuantity value for this RouteDailyStats.
     * 
     * @return avgPlannedQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getAvgPlannedQuantity() {
        return avgPlannedQuantity;
    }


    /**
     * Sets the avgPlannedQuantity value for this RouteDailyStats.
     * 
     * @param avgPlannedQuantity
     */
    public void setAvgPlannedQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities avgPlannedQuantity) {
        this.avgPlannedQuantity = avgPlannedQuantity;
    }


    /**
     * Gets the totalActualQuantity value for this RouteDailyStats.
     * 
     * @return totalActualQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getTotalActualQuantity() {
        return totalActualQuantity;
    }


    /**
     * Sets the totalActualQuantity value for this RouteDailyStats.
     * 
     * @param totalActualQuantity
     */
    public void setTotalActualQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities totalActualQuantity) {
        this.totalActualQuantity = totalActualQuantity;
    }


    /**
     * Gets the minActualQuantity value for this RouteDailyStats.
     * 
     * @return minActualQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getMinActualQuantity() {
        return minActualQuantity;
    }


    /**
     * Sets the minActualQuantity value for this RouteDailyStats.
     * 
     * @param minActualQuantity
     */
    public void setMinActualQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities minActualQuantity) {
        this.minActualQuantity = minActualQuantity;
    }


    /**
     * Gets the maxActualQuantity value for this RouteDailyStats.
     * 
     * @return maxActualQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getMaxActualQuantity() {
        return maxActualQuantity;
    }


    /**
     * Sets the maxActualQuantity value for this RouteDailyStats.
     * 
     * @param maxActualQuantity
     */
    public void setMaxActualQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities maxActualQuantity) {
        this.maxActualQuantity = maxActualQuantity;
    }


    /**
     * Gets the avgActualQuantity value for this RouteDailyStats.
     * 
     * @return avgActualQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getAvgActualQuantity() {
        return avgActualQuantity;
    }


    /**
     * Sets the avgActualQuantity value for this RouteDailyStats.
     * 
     * @param avgActualQuantity
     */
    public void setAvgActualQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities avgActualQuantity) {
        this.avgActualQuantity = avgActualQuantity;
    }


    /**
     * Gets the totalPlannedStopCount value for this RouteDailyStats.
     * 
     * @return totalPlannedStopCount
     */
    public int getTotalPlannedStopCount() {
        return totalPlannedStopCount;
    }


    /**
     * Sets the totalPlannedStopCount value for this RouteDailyStats.
     * 
     * @param totalPlannedStopCount
     */
    public void setTotalPlannedStopCount(int totalPlannedStopCount) {
        this.totalPlannedStopCount = totalPlannedStopCount;
    }


    /**
     * Gets the minPlannedStopCount value for this RouteDailyStats.
     * 
     * @return minPlannedStopCount
     */
    public int getMinPlannedStopCount() {
        return minPlannedStopCount;
    }


    /**
     * Sets the minPlannedStopCount value for this RouteDailyStats.
     * 
     * @param minPlannedStopCount
     */
    public void setMinPlannedStopCount(int minPlannedStopCount) {
        this.minPlannedStopCount = minPlannedStopCount;
    }


    /**
     * Gets the maxPlannedStopCount value for this RouteDailyStats.
     * 
     * @return maxPlannedStopCount
     */
    public int getMaxPlannedStopCount() {
        return maxPlannedStopCount;
    }


    /**
     * Sets the maxPlannedStopCount value for this RouteDailyStats.
     * 
     * @param maxPlannedStopCount
     */
    public void setMaxPlannedStopCount(int maxPlannedStopCount) {
        this.maxPlannedStopCount = maxPlannedStopCount;
    }


    /**
     * Gets the avgPlannedStopCount value for this RouteDailyStats.
     * 
     * @return avgPlannedStopCount
     */
    public int getAvgPlannedStopCount() {
        return avgPlannedStopCount;
    }


    /**
     * Sets the avgPlannedStopCount value for this RouteDailyStats.
     * 
     * @param avgPlannedStopCount
     */
    public void setAvgPlannedStopCount(int avgPlannedStopCount) {
        this.avgPlannedStopCount = avgPlannedStopCount;
    }


    /**
     * Gets the totalActualStopCount value for this RouteDailyStats.
     * 
     * @return totalActualStopCount
     */
    public int getTotalActualStopCount() {
        return totalActualStopCount;
    }


    /**
     * Sets the totalActualStopCount value for this RouteDailyStats.
     * 
     * @param totalActualStopCount
     */
    public void setTotalActualStopCount(int totalActualStopCount) {
        this.totalActualStopCount = totalActualStopCount;
    }


    /**
     * Gets the minActualStopCount value for this RouteDailyStats.
     * 
     * @return minActualStopCount
     */
    public int getMinActualStopCount() {
        return minActualStopCount;
    }


    /**
     * Sets the minActualStopCount value for this RouteDailyStats.
     * 
     * @param minActualStopCount
     */
    public void setMinActualStopCount(int minActualStopCount) {
        this.minActualStopCount = minActualStopCount;
    }


    /**
     * Gets the maxActualStopCount value for this RouteDailyStats.
     * 
     * @return maxActualStopCount
     */
    public int getMaxActualStopCount() {
        return maxActualStopCount;
    }


    /**
     * Sets the maxActualStopCount value for this RouteDailyStats.
     * 
     * @param maxActualStopCount
     */
    public void setMaxActualStopCount(int maxActualStopCount) {
        this.maxActualStopCount = maxActualStopCount;
    }


    /**
     * Gets the avgActualStopCount value for this RouteDailyStats.
     * 
     * @return avgActualStopCount
     */
    public int getAvgActualStopCount() {
        return avgActualStopCount;
    }


    /**
     * Sets the avgActualStopCount value for this RouteDailyStats.
     * 
     * @param avgActualStopCount
     */
    public void setAvgActualStopCount(int avgActualStopCount) {
        this.avgActualStopCount = avgActualStopCount;
    }


    /**
     * Gets the totalPlannedCost value for this RouteDailyStats.
     * 
     * @return totalPlannedCost
     */
    public int getTotalPlannedCost() {
        return totalPlannedCost;
    }


    /**
     * Sets the totalPlannedCost value for this RouteDailyStats.
     * 
     * @param totalPlannedCost
     */
    public void setTotalPlannedCost(int totalPlannedCost) {
        this.totalPlannedCost = totalPlannedCost;
    }


    /**
     * Gets the minPlannedCost value for this RouteDailyStats.
     * 
     * @return minPlannedCost
     */
    public int getMinPlannedCost() {
        return minPlannedCost;
    }


    /**
     * Sets the minPlannedCost value for this RouteDailyStats.
     * 
     * @param minPlannedCost
     */
    public void setMinPlannedCost(int minPlannedCost) {
        this.minPlannedCost = minPlannedCost;
    }


    /**
     * Gets the maxPlannedCost value for this RouteDailyStats.
     * 
     * @return maxPlannedCost
     */
    public int getMaxPlannedCost() {
        return maxPlannedCost;
    }


    /**
     * Sets the maxPlannedCost value for this RouteDailyStats.
     * 
     * @param maxPlannedCost
     */
    public void setMaxPlannedCost(int maxPlannedCost) {
        this.maxPlannedCost = maxPlannedCost;
    }


    /**
     * Gets the avgPlannedCost value for this RouteDailyStats.
     * 
     * @return avgPlannedCost
     */
    public int getAvgPlannedCost() {
        return avgPlannedCost;
    }


    /**
     * Sets the avgPlannedCost value for this RouteDailyStats.
     * 
     * @param avgPlannedCost
     */
    public void setAvgPlannedCost(int avgPlannedCost) {
        this.avgPlannedCost = avgPlannedCost;
    }


    /**
     * Gets the totalActualCost value for this RouteDailyStats.
     * 
     * @return totalActualCost
     */
    public int getTotalActualCost() {
        return totalActualCost;
    }


    /**
     * Sets the totalActualCost value for this RouteDailyStats.
     * 
     * @param totalActualCost
     */
    public void setTotalActualCost(int totalActualCost) {
        this.totalActualCost = totalActualCost;
    }


    /**
     * Gets the minActualCost value for this RouteDailyStats.
     * 
     * @return minActualCost
     */
    public int getMinActualCost() {
        return minActualCost;
    }


    /**
     * Sets the minActualCost value for this RouteDailyStats.
     * 
     * @param minActualCost
     */
    public void setMinActualCost(int minActualCost) {
        this.minActualCost = minActualCost;
    }


    /**
     * Gets the maxActualCost value for this RouteDailyStats.
     * 
     * @return maxActualCost
     */
    public int getMaxActualCost() {
        return maxActualCost;
    }


    /**
     * Sets the maxActualCost value for this RouteDailyStats.
     * 
     * @param maxActualCost
     */
    public void setMaxActualCost(int maxActualCost) {
        this.maxActualCost = maxActualCost;
    }


    /**
     * Gets the avgActualCost value for this RouteDailyStats.
     * 
     * @return avgActualCost
     */
    public int getAvgActualCost() {
        return avgActualCost;
    }


    /**
     * Sets the avgActualCost value for this RouteDailyStats.
     * 
     * @param avgActualCost
     */
    public void setAvgActualCost(int avgActualCost) {
        this.avgActualCost = avgActualCost;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RouteDailyStats)) return false;
        RouteDailyStats other = (RouteDailyStats) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.routeDailyStatsIdentity==null && other.getRouteDailyStatsIdentity()==null) || 
             (this.routeDailyStatsIdentity!=null &&
              this.routeDailyStatsIdentity.equals(other.getRouteDailyStatsIdentity()))) &&
            this.routeCount == other.getRouteCount() &&
            this.totalPlannedServiceTime == other.getTotalPlannedServiceTime() &&
            this.minPlannedServiceTime == other.getMinPlannedServiceTime() &&
            this.maxPlannedServiceTime == other.getMaxPlannedServiceTime() &&
            this.avgPlannedServiceTime == other.getAvgPlannedServiceTime() &&
            this.totalActualServiceTime == other.getTotalActualServiceTime() &&
            this.minActualServiceTime == other.getMinActualServiceTime() &&
            this.maxActualServiceTime == other.getMaxActualServiceTime() &&
            this.avgActualServiceTime == other.getAvgActualServiceTime() &&
            this.totalPlannedTravelTime == other.getTotalPlannedTravelTime() &&
            this.minPlannedTravelTime == other.getMinPlannedTravelTime() &&
            this.maxPlannedTravelTime == other.getMaxPlannedTravelTime() &&
            this.avgPlannedTravelTime == other.getAvgPlannedTravelTime() &&
            this.totalActualTravelTime == other.getTotalActualTravelTime() &&
            this.minActualTravelTime == other.getMinActualTravelTime() &&
            this.maxActualTravelTime == other.getMaxActualTravelTime() &&
            this.avgActualTravelTime == other.getAvgActualTravelTime() &&
            this.totalPlannedDistance == other.getTotalPlannedDistance() &&
            this.minPlannedDistance == other.getMinPlannedDistance() &&
            this.maxPlannedDistance == other.getMaxPlannedDistance() &&
            this.avgPlannedDistance == other.getAvgPlannedDistance() &&
            this.totalActualDistance == other.getTotalActualDistance() &&
            this.minActualDistance == other.getMinActualDistance() &&
            this.maxActualDistance == other.getMaxActualDistance() &&
            this.avgActualDistance == other.getAvgActualDistance() &&
            ((this.totalPlannedQuantity==null && other.getTotalPlannedQuantity()==null) || 
             (this.totalPlannedQuantity!=null &&
              this.totalPlannedQuantity.equals(other.getTotalPlannedQuantity()))) &&
            ((this.minPlannedQuantity==null && other.getMinPlannedQuantity()==null) || 
             (this.minPlannedQuantity!=null &&
              this.minPlannedQuantity.equals(other.getMinPlannedQuantity()))) &&
            ((this.maxPlannedQuantity==null && other.getMaxPlannedQuantity()==null) || 
             (this.maxPlannedQuantity!=null &&
              this.maxPlannedQuantity.equals(other.getMaxPlannedQuantity()))) &&
            ((this.avgPlannedQuantity==null && other.getAvgPlannedQuantity()==null) || 
             (this.avgPlannedQuantity!=null &&
              this.avgPlannedQuantity.equals(other.getAvgPlannedQuantity()))) &&
            ((this.totalActualQuantity==null && other.getTotalActualQuantity()==null) || 
             (this.totalActualQuantity!=null &&
              this.totalActualQuantity.equals(other.getTotalActualQuantity()))) &&
            ((this.minActualQuantity==null && other.getMinActualQuantity()==null) || 
             (this.minActualQuantity!=null &&
              this.minActualQuantity.equals(other.getMinActualQuantity()))) &&
            ((this.maxActualQuantity==null && other.getMaxActualQuantity()==null) || 
             (this.maxActualQuantity!=null &&
              this.maxActualQuantity.equals(other.getMaxActualQuantity()))) &&
            ((this.avgActualQuantity==null && other.getAvgActualQuantity()==null) || 
             (this.avgActualQuantity!=null &&
              this.avgActualQuantity.equals(other.getAvgActualQuantity()))) &&
            this.totalPlannedStopCount == other.getTotalPlannedStopCount() &&
            this.minPlannedStopCount == other.getMinPlannedStopCount() &&
            this.maxPlannedStopCount == other.getMaxPlannedStopCount() &&
            this.avgPlannedStopCount == other.getAvgPlannedStopCount() &&
            this.totalActualStopCount == other.getTotalActualStopCount() &&
            this.minActualStopCount == other.getMinActualStopCount() &&
            this.maxActualStopCount == other.getMaxActualStopCount() &&
            this.avgActualStopCount == other.getAvgActualStopCount() &&
            this.totalPlannedCost == other.getTotalPlannedCost() &&
            this.minPlannedCost == other.getMinPlannedCost() &&
            this.maxPlannedCost == other.getMaxPlannedCost() &&
            this.avgPlannedCost == other.getAvgPlannedCost() &&
            this.totalActualCost == other.getTotalActualCost() &&
            this.minActualCost == other.getMinActualCost() &&
            this.maxActualCost == other.getMaxActualCost() &&
            this.avgActualCost == other.getAvgActualCost();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRouteDailyStatsIdentity() != null) {
            _hashCode += getRouteDailyStatsIdentity().hashCode();
        }
        _hashCode += getRouteCount();
        _hashCode += getTotalPlannedServiceTime();
        _hashCode += getMinPlannedServiceTime();
        _hashCode += getMaxPlannedServiceTime();
        _hashCode += getAvgPlannedServiceTime();
        _hashCode += getTotalActualServiceTime();
        _hashCode += getMinActualServiceTime();
        _hashCode += getMaxActualServiceTime();
        _hashCode += getAvgActualServiceTime();
        _hashCode += getTotalPlannedTravelTime();
        _hashCode += getMinPlannedTravelTime();
        _hashCode += getMaxPlannedTravelTime();
        _hashCode += getAvgPlannedTravelTime();
        _hashCode += getTotalActualTravelTime();
        _hashCode += getMinActualTravelTime();
        _hashCode += getMaxActualTravelTime();
        _hashCode += getAvgActualTravelTime();
        _hashCode += new Double(getTotalPlannedDistance()).hashCode();
        _hashCode += new Double(getMinPlannedDistance()).hashCode();
        _hashCode += new Double(getMaxPlannedDistance()).hashCode();
        _hashCode += new Double(getAvgPlannedDistance()).hashCode();
        _hashCode += new Double(getTotalActualDistance()).hashCode();
        _hashCode += new Double(getMinActualDistance()).hashCode();
        _hashCode += new Double(getMaxActualDistance()).hashCode();
        _hashCode += new Double(getAvgActualDistance()).hashCode();
        if (getTotalPlannedQuantity() != null) {
            _hashCode += getTotalPlannedQuantity().hashCode();
        }
        if (getMinPlannedQuantity() != null) {
            _hashCode += getMinPlannedQuantity().hashCode();
        }
        if (getMaxPlannedQuantity() != null) {
            _hashCode += getMaxPlannedQuantity().hashCode();
        }
        if (getAvgPlannedQuantity() != null) {
            _hashCode += getAvgPlannedQuantity().hashCode();
        }
        if (getTotalActualQuantity() != null) {
            _hashCode += getTotalActualQuantity().hashCode();
        }
        if (getMinActualQuantity() != null) {
            _hashCode += getMinActualQuantity().hashCode();
        }
        if (getMaxActualQuantity() != null) {
            _hashCode += getMaxActualQuantity().hashCode();
        }
        if (getAvgActualQuantity() != null) {
            _hashCode += getAvgActualQuantity().hashCode();
        }
        _hashCode += getTotalPlannedStopCount();
        _hashCode += getMinPlannedStopCount();
        _hashCode += getMaxPlannedStopCount();
        _hashCode += getAvgPlannedStopCount();
        _hashCode += getTotalActualStopCount();
        _hashCode += getMinActualStopCount();
        _hashCode += getMaxActualStopCount();
        _hashCode += getAvgActualStopCount();
        _hashCode += getTotalPlannedCost();
        _hashCode += getMinPlannedCost();
        _hashCode += getMaxPlannedCost();
        _hashCode += getAvgPlannedCost();
        _hashCode += getTotalActualCost();
        _hashCode += getMinActualCost();
        _hashCode += getMaxActualCost();
        _hashCode += getAvgActualCost();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RouteDailyStats.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteDailyStats"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDailyStatsIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDailyStatsIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteDailyStatsIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualStopCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualStopCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPlannedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPlannedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minPlannedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minPlannedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxPlannedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxPlannedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgPlannedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgPlannedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalActualCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalActualCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minActualCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "minActualCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxActualCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxActualCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avgActualCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "avgActualCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
