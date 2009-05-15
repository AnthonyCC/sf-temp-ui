/**
 * RoutingImportOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingImportOrder  implements java.io.Serializable {
    private java.lang.String orderNumber;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities;

    private java.lang.String specialInstructions;

    private java.lang.String preferredRouteIDOverride;

    private java.lang.Integer additionalServiceTime;

    private boolean pickup;

    private java.util.Date deliveryDate;

    private java.util.Calendar dateAdded;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity;

    private java.lang.String takenBy;

    private java.lang.String orderSelector;

    private org.apache.axis.types.Time tw1Open;

    private org.apache.axis.types.Time tw1Close;

    private boolean isVoid;

    private org.apache.axis.types.Time openTime;

    private org.apache.axis.types.Time closeTime;

    private org.apache.axis.types.Time tw2Open;

    private org.apache.axis.types.Time tw2Close;

    private boolean forceBulkServiceTime;

    private com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities dailyQuantityIncrease;

    private java.util.Date beginDate;

    private java.util.Date endDate;

    private int urgency;

    private int dailyUrgencyIncrease;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOriginIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode rejectCode;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] lineItems;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] orderActivities;

    public RoutingImportOrder() {
    }

    public RoutingImportOrder(
           java.lang.String orderNumber,
           com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity,
           com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities,
           java.lang.String specialInstructions,
           java.lang.String preferredRouteIDOverride,
           java.lang.Integer additionalServiceTime,
           boolean pickup,
           java.util.Date deliveryDate,
           java.util.Calendar dateAdded,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity,
           java.lang.String takenBy,
           java.lang.String orderSelector,
           org.apache.axis.types.Time tw1Open,
           org.apache.axis.types.Time tw1Close,
           boolean isVoid,
           org.apache.axis.types.Time openTime,
           org.apache.axis.types.Time closeTime,
           org.apache.axis.types.Time tw2Open,
           org.apache.axis.types.Time tw2Close,
           boolean forceBulkServiceTime,
           com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields,
           com.freshdirect.routing.proxy.stub.transportation.Quantities dailyQuantityIncrease,
           java.util.Date beginDate,
           java.util.Date endDate,
           int urgency,
           int dailyUrgencyIncrease,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOriginIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode rejectCode,
           com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] lineItems,
           com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] orderActivities) {
           this.orderNumber = orderNumber;
           this.sessionIdentity = sessionIdentity;
           this.quantities = quantities;
           this.specialInstructions = specialInstructions;
           this.preferredRouteIDOverride = preferredRouteIDOverride;
           this.additionalServiceTime = additionalServiceTime;
           this.pickup = pickup;
           this.deliveryDate = deliveryDate;
           this.dateAdded = dateAdded;
           this.locationIdentity = locationIdentity;
           this.takenBy = takenBy;
           this.orderSelector = orderSelector;
           this.tw1Open = tw1Open;
           this.tw1Close = tw1Close;
           this.isVoid = isVoid;
           this.openTime = openTime;
           this.closeTime = closeTime;
           this.tw2Open = tw2Open;
           this.tw2Close = tw2Close;
           this.forceBulkServiceTime = forceBulkServiceTime;
           this.userDefinedFields = userDefinedFields;
           this.dailyQuantityIncrease = dailyQuantityIncrease;
           this.beginDate = beginDate;
           this.endDate = endDate;
           this.urgency = urgency;
           this.dailyUrgencyIncrease = dailyUrgencyIncrease;
           this.preferredOriginIdentity = preferredOriginIdentity;
           this.rejectCode = rejectCode;
           this.lineItems = lineItems;
           this.orderActivities = orderActivities;
    }


    /**
     * Gets the orderNumber value for this RoutingImportOrder.
     * 
     * @return orderNumber
     */
    public java.lang.String getOrderNumber() {
        return orderNumber;
    }


    /**
     * Sets the orderNumber value for this RoutingImportOrder.
     * 
     * @param orderNumber
     */
    public void setOrderNumber(java.lang.String orderNumber) {
        this.orderNumber = orderNumber;
    }


    /**
     * Gets the sessionIdentity value for this RoutingImportOrder.
     * 
     * @return sessionIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity getSessionIdentity() {
        return sessionIdentity;
    }


    /**
     * Sets the sessionIdentity value for this RoutingImportOrder.
     * 
     * @param sessionIdentity
     */
    public void setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }


    /**
     * Gets the quantities value for this RoutingImportOrder.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this RoutingImportOrder.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the specialInstructions value for this RoutingImportOrder.
     * 
     * @return specialInstructions
     */
    public java.lang.String getSpecialInstructions() {
        return specialInstructions;
    }


    /**
     * Sets the specialInstructions value for this RoutingImportOrder.
     * 
     * @param specialInstructions
     */
    public void setSpecialInstructions(java.lang.String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }


    /**
     * Gets the preferredRouteIDOverride value for this RoutingImportOrder.
     * 
     * @return preferredRouteIDOverride
     */
    public java.lang.String getPreferredRouteIDOverride() {
        return preferredRouteIDOverride;
    }


    /**
     * Sets the preferredRouteIDOverride value for this RoutingImportOrder.
     * 
     * @param preferredRouteIDOverride
     */
    public void setPreferredRouteIDOverride(java.lang.String preferredRouteIDOverride) {
        this.preferredRouteIDOverride = preferredRouteIDOverride;
    }


    /**
     * Gets the additionalServiceTime value for this RoutingImportOrder.
     * 
     * @return additionalServiceTime
     */
    public java.lang.Integer getAdditionalServiceTime() {
        return additionalServiceTime;
    }


    /**
     * Sets the additionalServiceTime value for this RoutingImportOrder.
     * 
     * @param additionalServiceTime
     */
    public void setAdditionalServiceTime(java.lang.Integer additionalServiceTime) {
        this.additionalServiceTime = additionalServiceTime;
    }


    /**
     * Gets the pickup value for this RoutingImportOrder.
     * 
     * @return pickup
     */
    public boolean isPickup() {
        return pickup;
    }


    /**
     * Sets the pickup value for this RoutingImportOrder.
     * 
     * @param pickup
     */
    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }


    /**
     * Gets the deliveryDate value for this RoutingImportOrder.
     * 
     * @return deliveryDate
     */
    public java.util.Date getDeliveryDate() {
        return deliveryDate;
    }


    /**
     * Sets the deliveryDate value for this RoutingImportOrder.
     * 
     * @param deliveryDate
     */
    public void setDeliveryDate(java.util.Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    /**
     * Gets the dateAdded value for this RoutingImportOrder.
     * 
     * @return dateAdded
     */
    public java.util.Calendar getDateAdded() {
        return dateAdded;
    }


    /**
     * Sets the dateAdded value for this RoutingImportOrder.
     * 
     * @param dateAdded
     */
    public void setDateAdded(java.util.Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }


    /**
     * Gets the locationIdentity value for this RoutingImportOrder.
     * 
     * @return locationIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getLocationIdentity() {
        return locationIdentity;
    }


    /**
     * Sets the locationIdentity value for this RoutingImportOrder.
     * 
     * @param locationIdentity
     */
    public void setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity) {
        this.locationIdentity = locationIdentity;
    }


    /**
     * Gets the takenBy value for this RoutingImportOrder.
     * 
     * @return takenBy
     */
    public java.lang.String getTakenBy() {
        return takenBy;
    }


    /**
     * Sets the takenBy value for this RoutingImportOrder.
     * 
     * @param takenBy
     */
    public void setTakenBy(java.lang.String takenBy) {
        this.takenBy = takenBy;
    }


    /**
     * Gets the orderSelector value for this RoutingImportOrder.
     * 
     * @return orderSelector
     */
    public java.lang.String getOrderSelector() {
        return orderSelector;
    }


    /**
     * Sets the orderSelector value for this RoutingImportOrder.
     * 
     * @param orderSelector
     */
    public void setOrderSelector(java.lang.String orderSelector) {
        this.orderSelector = orderSelector;
    }


    /**
     * Gets the tw1Open value for this RoutingImportOrder.
     * 
     * @return tw1Open
     */
    public org.apache.axis.types.Time getTw1Open() {
        return tw1Open;
    }


    /**
     * Sets the tw1Open value for this RoutingImportOrder.
     * 
     * @param tw1Open
     */
    public void setTw1Open(org.apache.axis.types.Time tw1Open) {
        this.tw1Open = tw1Open;
    }


    /**
     * Gets the tw1Close value for this RoutingImportOrder.
     * 
     * @return tw1Close
     */
    public org.apache.axis.types.Time getTw1Close() {
        return tw1Close;
    }


    /**
     * Sets the tw1Close value for this RoutingImportOrder.
     * 
     * @param tw1Close
     */
    public void setTw1Close(org.apache.axis.types.Time tw1Close) {
        this.tw1Close = tw1Close;
    }


    /**
     * Gets the isVoid value for this RoutingImportOrder.
     * 
     * @return isVoid
     */
    public boolean isIsVoid() {
        return isVoid;
    }


    /**
     * Sets the isVoid value for this RoutingImportOrder.
     * 
     * @param isVoid
     */
    public void setIsVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }


    /**
     * Gets the openTime value for this RoutingImportOrder.
     * 
     * @return openTime
     */
    public org.apache.axis.types.Time getOpenTime() {
        return openTime;
    }


    /**
     * Sets the openTime value for this RoutingImportOrder.
     * 
     * @param openTime
     */
    public void setOpenTime(org.apache.axis.types.Time openTime) {
        this.openTime = openTime;
    }


    /**
     * Gets the closeTime value for this RoutingImportOrder.
     * 
     * @return closeTime
     */
    public org.apache.axis.types.Time getCloseTime() {
        return closeTime;
    }


    /**
     * Sets the closeTime value for this RoutingImportOrder.
     * 
     * @param closeTime
     */
    public void setCloseTime(org.apache.axis.types.Time closeTime) {
        this.closeTime = closeTime;
    }


    /**
     * Gets the tw2Open value for this RoutingImportOrder.
     * 
     * @return tw2Open
     */
    public org.apache.axis.types.Time getTw2Open() {
        return tw2Open;
    }


    /**
     * Sets the tw2Open value for this RoutingImportOrder.
     * 
     * @param tw2Open
     */
    public void setTw2Open(org.apache.axis.types.Time tw2Open) {
        this.tw2Open = tw2Open;
    }


    /**
     * Gets the tw2Close value for this RoutingImportOrder.
     * 
     * @return tw2Close
     */
    public org.apache.axis.types.Time getTw2Close() {
        return tw2Close;
    }


    /**
     * Sets the tw2Close value for this RoutingImportOrder.
     * 
     * @param tw2Close
     */
    public void setTw2Close(org.apache.axis.types.Time tw2Close) {
        this.tw2Close = tw2Close;
    }


    /**
     * Gets the forceBulkServiceTime value for this RoutingImportOrder.
     * 
     * @return forceBulkServiceTime
     */
    public boolean isForceBulkServiceTime() {
        return forceBulkServiceTime;
    }


    /**
     * Sets the forceBulkServiceTime value for this RoutingImportOrder.
     * 
     * @param forceBulkServiceTime
     */
    public void setForceBulkServiceTime(boolean forceBulkServiceTime) {
        this.forceBulkServiceTime = forceBulkServiceTime;
    }


    /**
     * Gets the userDefinedFields value for this RoutingImportOrder.
     * 
     * @return userDefinedFields
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }


    /**
     * Sets the userDefinedFields value for this RoutingImportOrder.
     * 
     * @param userDefinedFields
     */
    public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }


    /**
     * Gets the dailyQuantityIncrease value for this RoutingImportOrder.
     * 
     * @return dailyQuantityIncrease
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getDailyQuantityIncrease() {
        return dailyQuantityIncrease;
    }


    /**
     * Sets the dailyQuantityIncrease value for this RoutingImportOrder.
     * 
     * @param dailyQuantityIncrease
     */
    public void setDailyQuantityIncrease(com.freshdirect.routing.proxy.stub.transportation.Quantities dailyQuantityIncrease) {
        this.dailyQuantityIncrease = dailyQuantityIncrease;
    }


    /**
     * Gets the beginDate value for this RoutingImportOrder.
     * 
     * @return beginDate
     */
    public java.util.Date getBeginDate() {
        return beginDate;
    }


    /**
     * Sets the beginDate value for this RoutingImportOrder.
     * 
     * @param beginDate
     */
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }


    /**
     * Gets the endDate value for this RoutingImportOrder.
     * 
     * @return endDate
     */
    public java.util.Date getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this RoutingImportOrder.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the urgency value for this RoutingImportOrder.
     * 
     * @return urgency
     */
    public int getUrgency() {
        return urgency;
    }


    /**
     * Sets the urgency value for this RoutingImportOrder.
     * 
     * @param urgency
     */
    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }


    /**
     * Gets the dailyUrgencyIncrease value for this RoutingImportOrder.
     * 
     * @return dailyUrgencyIncrease
     */
    public int getDailyUrgencyIncrease() {
        return dailyUrgencyIncrease;
    }


    /**
     * Sets the dailyUrgencyIncrease value for this RoutingImportOrder.
     * 
     * @param dailyUrgencyIncrease
     */
    public void setDailyUrgencyIncrease(int dailyUrgencyIncrease) {
        this.dailyUrgencyIncrease = dailyUrgencyIncrease;
    }


    /**
     * Gets the preferredOriginIdentity value for this RoutingImportOrder.
     * 
     * @return preferredOriginIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getPreferredOriginIdentity() {
        return preferredOriginIdentity;
    }


    /**
     * Sets the preferredOriginIdentity value for this RoutingImportOrder.
     * 
     * @param preferredOriginIdentity
     */
    public void setPreferredOriginIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOriginIdentity) {
        this.preferredOriginIdentity = preferredOriginIdentity;
    }


    /**
     * Gets the rejectCode value for this RoutingImportOrder.
     * 
     * @return rejectCode
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode getRejectCode() {
        return rejectCode;
    }


    /**
     * Sets the rejectCode value for this RoutingImportOrder.
     * 
     * @param rejectCode
     */
    public void setRejectCode(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode rejectCode) {
        this.rejectCode = rejectCode;
    }


    /**
     * Gets the lineItems value for this RoutingImportOrder.
     * 
     * @return lineItems
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] getLineItems() {
        return lineItems;
    }


    /**
     * Sets the lineItems value for this RoutingImportOrder.
     * 
     * @param lineItems
     */
    public void setLineItems(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] lineItems) {
        this.lineItems = lineItems;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem getLineItems(int i) {
        return this.lineItems[i];
    }

    public void setLineItems(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem _value) {
        this.lineItems[i] = _value;
    }


    /**
     * Gets the orderActivities value for this RoutingImportOrder.
     * 
     * @return orderActivities
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] getOrderActivities() {
        return orderActivities;
    }


    /**
     * Sets the orderActivities value for this RoutingImportOrder.
     * 
     * @param orderActivities
     */
    public void setOrderActivities(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] orderActivities) {
        this.orderActivities = orderActivities;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity getOrderActivities(int i) {
        return this.orderActivities[i];
    }

    public void setOrderActivities(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity _value) {
        this.orderActivities[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingImportOrder)) return false;
        RoutingImportOrder other = (RoutingImportOrder) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orderNumber==null && other.getOrderNumber()==null) || 
             (this.orderNumber!=null &&
              this.orderNumber.equals(other.getOrderNumber()))) &&
            ((this.sessionIdentity==null && other.getSessionIdentity()==null) || 
             (this.sessionIdentity!=null &&
              this.sessionIdentity.equals(other.getSessionIdentity()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.specialInstructions==null && other.getSpecialInstructions()==null) || 
             (this.specialInstructions!=null &&
              this.specialInstructions.equals(other.getSpecialInstructions()))) &&
            ((this.preferredRouteIDOverride==null && other.getPreferredRouteIDOverride()==null) || 
             (this.preferredRouteIDOverride!=null &&
              this.preferredRouteIDOverride.equals(other.getPreferredRouteIDOverride()))) &&
            ((this.additionalServiceTime==null && other.getAdditionalServiceTime()==null) || 
             (this.additionalServiceTime!=null &&
              this.additionalServiceTime.equals(other.getAdditionalServiceTime()))) &&
            this.pickup == other.isPickup() &&
            ((this.deliveryDate==null && other.getDeliveryDate()==null) || 
             (this.deliveryDate!=null &&
              this.deliveryDate.equals(other.getDeliveryDate()))) &&
            ((this.dateAdded==null && other.getDateAdded()==null) || 
             (this.dateAdded!=null &&
              this.dateAdded.equals(other.getDateAdded()))) &&
            ((this.locationIdentity==null && other.getLocationIdentity()==null) || 
             (this.locationIdentity!=null &&
              this.locationIdentity.equals(other.getLocationIdentity()))) &&
            ((this.takenBy==null && other.getTakenBy()==null) || 
             (this.takenBy!=null &&
              this.takenBy.equals(other.getTakenBy()))) &&
            ((this.orderSelector==null && other.getOrderSelector()==null) || 
             (this.orderSelector!=null &&
              this.orderSelector.equals(other.getOrderSelector()))) &&
            ((this.tw1Open==null && other.getTw1Open()==null) || 
             (this.tw1Open!=null &&
              this.tw1Open.equals(other.getTw1Open()))) &&
            ((this.tw1Close==null && other.getTw1Close()==null) || 
             (this.tw1Close!=null &&
              this.tw1Close.equals(other.getTw1Close()))) &&
            this.isVoid == other.isIsVoid() &&
            ((this.openTime==null && other.getOpenTime()==null) || 
             (this.openTime!=null &&
              this.openTime.equals(other.getOpenTime()))) &&
            ((this.closeTime==null && other.getCloseTime()==null) || 
             (this.closeTime!=null &&
              this.closeTime.equals(other.getCloseTime()))) &&
            ((this.tw2Open==null && other.getTw2Open()==null) || 
             (this.tw2Open!=null &&
              this.tw2Open.equals(other.getTw2Open()))) &&
            ((this.tw2Close==null && other.getTw2Close()==null) || 
             (this.tw2Close!=null &&
              this.tw2Close.equals(other.getTw2Close()))) &&
            this.forceBulkServiceTime == other.isForceBulkServiceTime() &&
            ((this.userDefinedFields==null && other.getUserDefinedFields()==null) || 
             (this.userDefinedFields!=null &&
              this.userDefinedFields.equals(other.getUserDefinedFields()))) &&
            ((this.dailyQuantityIncrease==null && other.getDailyQuantityIncrease()==null) || 
             (this.dailyQuantityIncrease!=null &&
              this.dailyQuantityIncrease.equals(other.getDailyQuantityIncrease()))) &&
            ((this.beginDate==null && other.getBeginDate()==null) || 
             (this.beginDate!=null &&
              this.beginDate.equals(other.getBeginDate()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            this.urgency == other.getUrgency() &&
            this.dailyUrgencyIncrease == other.getDailyUrgencyIncrease() &&
            ((this.preferredOriginIdentity==null && other.getPreferredOriginIdentity()==null) || 
             (this.preferredOriginIdentity!=null &&
              this.preferredOriginIdentity.equals(other.getPreferredOriginIdentity()))) &&
            ((this.rejectCode==null && other.getRejectCode()==null) || 
             (this.rejectCode!=null &&
              this.rejectCode.equals(other.getRejectCode()))) &&
            ((this.lineItems==null && other.getLineItems()==null) || 
             (this.lineItems!=null &&
              java.util.Arrays.equals(this.lineItems, other.getLineItems()))) &&
            ((this.orderActivities==null && other.getOrderActivities()==null) || 
             (this.orderActivities!=null &&
              java.util.Arrays.equals(this.orderActivities, other.getOrderActivities())));
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
        if (getOrderNumber() != null) {
            _hashCode += getOrderNumber().hashCode();
        }
        if (getSessionIdentity() != null) {
            _hashCode += getSessionIdentity().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getSpecialInstructions() != null) {
            _hashCode += getSpecialInstructions().hashCode();
        }
        if (getPreferredRouteIDOverride() != null) {
            _hashCode += getPreferredRouteIDOverride().hashCode();
        }
        if (getAdditionalServiceTime() != null) {
            _hashCode += getAdditionalServiceTime().hashCode();
        }
        _hashCode += (isPickup() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDeliveryDate() != null) {
            _hashCode += getDeliveryDate().hashCode();
        }
        if (getDateAdded() != null) {
            _hashCode += getDateAdded().hashCode();
        }
        if (getLocationIdentity() != null) {
            _hashCode += getLocationIdentity().hashCode();
        }
        if (getTakenBy() != null) {
            _hashCode += getTakenBy().hashCode();
        }
        if (getOrderSelector() != null) {
            _hashCode += getOrderSelector().hashCode();
        }
        if (getTw1Open() != null) {
            _hashCode += getTw1Open().hashCode();
        }
        if (getTw1Close() != null) {
            _hashCode += getTw1Close().hashCode();
        }
        _hashCode += (isIsVoid() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOpenTime() != null) {
            _hashCode += getOpenTime().hashCode();
        }
        if (getCloseTime() != null) {
            _hashCode += getCloseTime().hashCode();
        }
        if (getTw2Open() != null) {
            _hashCode += getTw2Open().hashCode();
        }
        if (getTw2Close() != null) {
            _hashCode += getTw2Close().hashCode();
        }
        _hashCode += (isForceBulkServiceTime() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUserDefinedFields() != null) {
            _hashCode += getUserDefinedFields().hashCode();
        }
        if (getDailyQuantityIncrease() != null) {
            _hashCode += getDailyQuantityIncrease().hashCode();
        }
        if (getBeginDate() != null) {
            _hashCode += getBeginDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        _hashCode += getUrgency();
        _hashCode += getDailyUrgencyIncrease();
        if (getPreferredOriginIdentity() != null) {
            _hashCode += getPreferredOriginIdentity().hashCode();
        }
        if (getRejectCode() != null) {
            _hashCode += getRejectCode().hashCode();
        }
        if (getLineItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLineItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLineItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOrderActivities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrderActivities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrderActivities(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingImportOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSessionIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CategoryQuantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialInstructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "specialInstructions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredRouteIDOverride");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredRouteIDOverride"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "additionalServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pickup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "pickup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAdded");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateAdded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takenBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "takenBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderSelector");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderSelector"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw1Open");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw1Open"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw1Close");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw1Close"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isVoid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "isVoid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "openTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("closeTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "closeTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw2Open");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw2Open"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw2Close");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw2Close"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forceBulkServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "forceBulkServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UserDefinedFields"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dailyQuantityIncrease");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dailyQuantityIncrease"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "beginDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urgency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "urgency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dailyUrgencyIncrease");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dailyUrgencyIncrease"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredOriginIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredOriginIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rejectCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "rejectCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportOrderRejectCode"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportLineItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderActivities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderActivities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportOrderActivity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
