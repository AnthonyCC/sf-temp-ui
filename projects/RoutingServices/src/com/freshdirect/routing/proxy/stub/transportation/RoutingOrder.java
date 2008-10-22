/**
 * RoutingOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingOrder  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity;

    private java.lang.String orderNumber;

    private com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities;

    private int additionalServiceTime;

    private java.lang.String takenBy;

    private boolean printed;

    private com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities dailyIncrease;

    private java.util.Date beginDate;

    private java.util.Date endDate;

    private int urgency;

    private java.lang.String selector;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderType orderType;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType originalPlanType;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType lastPlanType;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingTriggerType triggerType;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderSource orderSource;

    private boolean voidOrder;

    private java.lang.String uploadSelector;

    private java.util.Calendar dateAdded;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferedOrigin;

    private boolean forceBulkServiceTime;

    private int dailyUrgencyIncrease;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities beginQuantity;

    private int beginUrgency;

    private java.lang.String specialInstructions;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem[] lineItems;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity[] activities;

    public RoutingOrder() {
    }

    public RoutingOrder(
           com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity,
           java.lang.String orderNumber,
           com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities,
           int additionalServiceTime,
           java.lang.String takenBy,
           boolean printed,
           com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields,
           com.freshdirect.routing.proxy.stub.transportation.Quantities dailyIncrease,
           java.util.Date beginDate,
           java.util.Date endDate,
           int urgency,
           java.lang.String selector,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderType orderType,
           com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType originalPlanType,
           com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType lastPlanType,
           com.freshdirect.routing.proxy.stub.transportation.RoutingTriggerType triggerType,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderSource orderSource,
           boolean voidOrder,
           java.lang.String uploadSelector,
           java.util.Calendar dateAdded,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferedOrigin,
           boolean forceBulkServiceTime,
           int dailyUrgencyIncrease,
           com.freshdirect.routing.proxy.stub.transportation.Quantities beginQuantity,
           int beginUrgency,
           java.lang.String specialInstructions,
           com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem[] lineItems,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity[] activities) {
           this.stopIdentity = stopIdentity;
           this.orderIdentity = orderIdentity;
           this.orderNumber = orderNumber;
           this.quantities = quantities;
           this.additionalServiceTime = additionalServiceTime;
           this.takenBy = takenBy;
           this.printed = printed;
           this.userDefinedFields = userDefinedFields;
           this.dailyIncrease = dailyIncrease;
           this.beginDate = beginDate;
           this.endDate = endDate;
           this.urgency = urgency;
           this.selector = selector;
           this.orderType = orderType;
           this.originalPlanType = originalPlanType;
           this.lastPlanType = lastPlanType;
           this.triggerType = triggerType;
           this.orderSource = orderSource;
           this.voidOrder = voidOrder;
           this.uploadSelector = uploadSelector;
           this.dateAdded = dateAdded;
           this.preferedOrigin = preferedOrigin;
           this.forceBulkServiceTime = forceBulkServiceTime;
           this.dailyUrgencyIncrease = dailyUrgencyIncrease;
           this.beginQuantity = beginQuantity;
           this.beginUrgency = beginUrgency;
           this.specialInstructions = specialInstructions;
           this.lineItems = lineItems;
           this.activities = activities;
    }


    /**
     * Gets the stopIdentity value for this RoutingOrder.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this RoutingOrder.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the orderIdentity value for this RoutingOrder.
     * 
     * @return orderIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity getOrderIdentity() {
        return orderIdentity;
    }


    /**
     * Sets the orderIdentity value for this RoutingOrder.
     * 
     * @param orderIdentity
     */
    public void setOrderIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity) {
        this.orderIdentity = orderIdentity;
    }


    /**
     * Gets the orderNumber value for this RoutingOrder.
     * 
     * @return orderNumber
     */
    public java.lang.String getOrderNumber() {
        return orderNumber;
    }


    /**
     * Sets the orderNumber value for this RoutingOrder.
     * 
     * @param orderNumber
     */
    public void setOrderNumber(java.lang.String orderNumber) {
        this.orderNumber = orderNumber;
    }


    /**
     * Gets the quantities value for this RoutingOrder.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this RoutingOrder.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the additionalServiceTime value for this RoutingOrder.
     * 
     * @return additionalServiceTime
     */
    public int getAdditionalServiceTime() {
        return additionalServiceTime;
    }


    /**
     * Sets the additionalServiceTime value for this RoutingOrder.
     * 
     * @param additionalServiceTime
     */
    public void setAdditionalServiceTime(int additionalServiceTime) {
        this.additionalServiceTime = additionalServiceTime;
    }


    /**
     * Gets the takenBy value for this RoutingOrder.
     * 
     * @return takenBy
     */
    public java.lang.String getTakenBy() {
        return takenBy;
    }


    /**
     * Sets the takenBy value for this RoutingOrder.
     * 
     * @param takenBy
     */
    public void setTakenBy(java.lang.String takenBy) {
        this.takenBy = takenBy;
    }


    /**
     * Gets the printed value for this RoutingOrder.
     * 
     * @return printed
     */
    public boolean isPrinted() {
        return printed;
    }


    /**
     * Sets the printed value for this RoutingOrder.
     * 
     * @param printed
     */
    public void setPrinted(boolean printed) {
        this.printed = printed;
    }


    /**
     * Gets the userDefinedFields value for this RoutingOrder.
     * 
     * @return userDefinedFields
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }


    /**
     * Sets the userDefinedFields value for this RoutingOrder.
     * 
     * @param userDefinedFields
     */
    public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }


    /**
     * Gets the dailyIncrease value for this RoutingOrder.
     * 
     * @return dailyIncrease
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getDailyIncrease() {
        return dailyIncrease;
    }


    /**
     * Sets the dailyIncrease value for this RoutingOrder.
     * 
     * @param dailyIncrease
     */
    public void setDailyIncrease(com.freshdirect.routing.proxy.stub.transportation.Quantities dailyIncrease) {
        this.dailyIncrease = dailyIncrease;
    }


    /**
     * Gets the beginDate value for this RoutingOrder.
     * 
     * @return beginDate
     */
    public java.util.Date getBeginDate() {
        return beginDate;
    }


    /**
     * Sets the beginDate value for this RoutingOrder.
     * 
     * @param beginDate
     */
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }


    /**
     * Gets the endDate value for this RoutingOrder.
     * 
     * @return endDate
     */
    public java.util.Date getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this RoutingOrder.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the urgency value for this RoutingOrder.
     * 
     * @return urgency
     */
    public int getUrgency() {
        return urgency;
    }


    /**
     * Sets the urgency value for this RoutingOrder.
     * 
     * @param urgency
     */
    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }


    /**
     * Gets the selector value for this RoutingOrder.
     * 
     * @return selector
     */
    public java.lang.String getSelector() {
        return selector;
    }


    /**
     * Sets the selector value for this RoutingOrder.
     * 
     * @param selector
     */
    public void setSelector(java.lang.String selector) {
        this.selector = selector;
    }


    /**
     * Gets the orderType value for this RoutingOrder.
     * 
     * @return orderType
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderType getOrderType() {
        return orderType;
    }


    /**
     * Sets the orderType value for this RoutingOrder.
     * 
     * @param orderType
     */
    public void setOrderType(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderType orderType) {
        this.orderType = orderType;
    }


    /**
     * Gets the originalPlanType value for this RoutingOrder.
     * 
     * @return originalPlanType
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType getOriginalPlanType() {
        return originalPlanType;
    }


    /**
     * Sets the originalPlanType value for this RoutingOrder.
     * 
     * @param originalPlanType
     */
    public void setOriginalPlanType(com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType originalPlanType) {
        this.originalPlanType = originalPlanType;
    }


    /**
     * Gets the lastPlanType value for this RoutingOrder.
     * 
     * @return lastPlanType
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType getLastPlanType() {
        return lastPlanType;
    }


    /**
     * Sets the lastPlanType value for this RoutingOrder.
     * 
     * @param lastPlanType
     */
    public void setLastPlanType(com.freshdirect.routing.proxy.stub.transportation.RoutingPlanType lastPlanType) {
        this.lastPlanType = lastPlanType;
    }


    /**
     * Gets the triggerType value for this RoutingOrder.
     * 
     * @return triggerType
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingTriggerType getTriggerType() {
        return triggerType;
    }


    /**
     * Sets the triggerType value for this RoutingOrder.
     * 
     * @param triggerType
     */
    public void setTriggerType(com.freshdirect.routing.proxy.stub.transportation.RoutingTriggerType triggerType) {
        this.triggerType = triggerType;
    }


    /**
     * Gets the orderSource value for this RoutingOrder.
     * 
     * @return orderSource
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderSource getOrderSource() {
        return orderSource;
    }


    /**
     * Sets the orderSource value for this RoutingOrder.
     * 
     * @param orderSource
     */
    public void setOrderSource(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderSource orderSource) {
        this.orderSource = orderSource;
    }


    /**
     * Gets the voidOrder value for this RoutingOrder.
     * 
     * @return voidOrder
     */
    public boolean isVoidOrder() {
        return voidOrder;
    }


    /**
     * Sets the voidOrder value for this RoutingOrder.
     * 
     * @param voidOrder
     */
    public void setVoidOrder(boolean voidOrder) {
        this.voidOrder = voidOrder;
    }


    /**
     * Gets the uploadSelector value for this RoutingOrder.
     * 
     * @return uploadSelector
     */
    public java.lang.String getUploadSelector() {
        return uploadSelector;
    }


    /**
     * Sets the uploadSelector value for this RoutingOrder.
     * 
     * @param uploadSelector
     */
    public void setUploadSelector(java.lang.String uploadSelector) {
        this.uploadSelector = uploadSelector;
    }


    /**
     * Gets the dateAdded value for this RoutingOrder.
     * 
     * @return dateAdded
     */
    public java.util.Calendar getDateAdded() {
        return dateAdded;
    }


    /**
     * Sets the dateAdded value for this RoutingOrder.
     * 
     * @param dateAdded
     */
    public void setDateAdded(java.util.Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }


    /**
     * Gets the preferedOrigin value for this RoutingOrder.
     * 
     * @return preferedOrigin
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getPreferedOrigin() {
        return preferedOrigin;
    }


    /**
     * Sets the preferedOrigin value for this RoutingOrder.
     * 
     * @param preferedOrigin
     */
    public void setPreferedOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferedOrigin) {
        this.preferedOrigin = preferedOrigin;
    }


    /**
     * Gets the forceBulkServiceTime value for this RoutingOrder.
     * 
     * @return forceBulkServiceTime
     */
    public boolean isForceBulkServiceTime() {
        return forceBulkServiceTime;
    }


    /**
     * Sets the forceBulkServiceTime value for this RoutingOrder.
     * 
     * @param forceBulkServiceTime
     */
    public void setForceBulkServiceTime(boolean forceBulkServiceTime) {
        this.forceBulkServiceTime = forceBulkServiceTime;
    }


    /**
     * Gets the dailyUrgencyIncrease value for this RoutingOrder.
     * 
     * @return dailyUrgencyIncrease
     */
    public int getDailyUrgencyIncrease() {
        return dailyUrgencyIncrease;
    }


    /**
     * Sets the dailyUrgencyIncrease value for this RoutingOrder.
     * 
     * @param dailyUrgencyIncrease
     */
    public void setDailyUrgencyIncrease(int dailyUrgencyIncrease) {
        this.dailyUrgencyIncrease = dailyUrgencyIncrease;
    }


    /**
     * Gets the beginQuantity value for this RoutingOrder.
     * 
     * @return beginQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getBeginQuantity() {
        return beginQuantity;
    }


    /**
     * Sets the beginQuantity value for this RoutingOrder.
     * 
     * @param beginQuantity
     */
    public void setBeginQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities beginQuantity) {
        this.beginQuantity = beginQuantity;
    }


    /**
     * Gets the beginUrgency value for this RoutingOrder.
     * 
     * @return beginUrgency
     */
    public int getBeginUrgency() {
        return beginUrgency;
    }


    /**
     * Sets the beginUrgency value for this RoutingOrder.
     * 
     * @param beginUrgency
     */
    public void setBeginUrgency(int beginUrgency) {
        this.beginUrgency = beginUrgency;
    }


    /**
     * Gets the specialInstructions value for this RoutingOrder.
     * 
     * @return specialInstructions
     */
    public java.lang.String getSpecialInstructions() {
        return specialInstructions;
    }


    /**
     * Sets the specialInstructions value for this RoutingOrder.
     * 
     * @param specialInstructions
     */
    public void setSpecialInstructions(java.lang.String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }


    /**
     * Gets the lineItems value for this RoutingOrder.
     * 
     * @return lineItems
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem[] getLineItems() {
        return lineItems;
    }


    /**
     * Sets the lineItems value for this RoutingOrder.
     * 
     * @param lineItems
     */
    public void setLineItems(com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem[] lineItems) {
        this.lineItems = lineItems;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem getLineItems(int i) {
        return this.lineItems[i];
    }

    public void setLineItems(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingLineItem _value) {
        this.lineItems[i] = _value;
    }


    /**
     * Gets the activities value for this RoutingOrder.
     * 
     * @return activities
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity[] getActivities() {
        return activities;
    }


    /**
     * Sets the activities value for this RoutingOrder.
     * 
     * @param activities
     */
    public void setActivities(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity[] activities) {
        this.activities = activities;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity getActivities(int i) {
        return this.activities[i];
    }

    public void setActivities(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivity _value) {
        this.activities[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingOrder)) return false;
        RoutingOrder other = (RoutingOrder) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity()))) &&
            ((this.orderIdentity==null && other.getOrderIdentity()==null) || 
             (this.orderIdentity!=null &&
              this.orderIdentity.equals(other.getOrderIdentity()))) &&
            ((this.orderNumber==null && other.getOrderNumber()==null) || 
             (this.orderNumber!=null &&
              this.orderNumber.equals(other.getOrderNumber()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            this.additionalServiceTime == other.getAdditionalServiceTime() &&
            ((this.takenBy==null && other.getTakenBy()==null) || 
             (this.takenBy!=null &&
              this.takenBy.equals(other.getTakenBy()))) &&
            this.printed == other.isPrinted() &&
            ((this.userDefinedFields==null && other.getUserDefinedFields()==null) || 
             (this.userDefinedFields!=null &&
              this.userDefinedFields.equals(other.getUserDefinedFields()))) &&
            ((this.dailyIncrease==null && other.getDailyIncrease()==null) || 
             (this.dailyIncrease!=null &&
              this.dailyIncrease.equals(other.getDailyIncrease()))) &&
            ((this.beginDate==null && other.getBeginDate()==null) || 
             (this.beginDate!=null &&
              this.beginDate.equals(other.getBeginDate()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            this.urgency == other.getUrgency() &&
            ((this.selector==null && other.getSelector()==null) || 
             (this.selector!=null &&
              this.selector.equals(other.getSelector()))) &&
            ((this.orderType==null && other.getOrderType()==null) || 
             (this.orderType!=null &&
              this.orderType.equals(other.getOrderType()))) &&
            ((this.originalPlanType==null && other.getOriginalPlanType()==null) || 
             (this.originalPlanType!=null &&
              this.originalPlanType.equals(other.getOriginalPlanType()))) &&
            ((this.lastPlanType==null && other.getLastPlanType()==null) || 
             (this.lastPlanType!=null &&
              this.lastPlanType.equals(other.getLastPlanType()))) &&
            ((this.triggerType==null && other.getTriggerType()==null) || 
             (this.triggerType!=null &&
              this.triggerType.equals(other.getTriggerType()))) &&
            ((this.orderSource==null && other.getOrderSource()==null) || 
             (this.orderSource!=null &&
              this.orderSource.equals(other.getOrderSource()))) &&
            this.voidOrder == other.isVoidOrder() &&
            ((this.uploadSelector==null && other.getUploadSelector()==null) || 
             (this.uploadSelector!=null &&
              this.uploadSelector.equals(other.getUploadSelector()))) &&
            ((this.dateAdded==null && other.getDateAdded()==null) || 
             (this.dateAdded!=null &&
              this.dateAdded.equals(other.getDateAdded()))) &&
            ((this.preferedOrigin==null && other.getPreferedOrigin()==null) || 
             (this.preferedOrigin!=null &&
              this.preferedOrigin.equals(other.getPreferedOrigin()))) &&
            this.forceBulkServiceTime == other.isForceBulkServiceTime() &&
            this.dailyUrgencyIncrease == other.getDailyUrgencyIncrease() &&
            ((this.beginQuantity==null && other.getBeginQuantity()==null) || 
             (this.beginQuantity!=null &&
              this.beginQuantity.equals(other.getBeginQuantity()))) &&
            this.beginUrgency == other.getBeginUrgency() &&
            ((this.specialInstructions==null && other.getSpecialInstructions()==null) || 
             (this.specialInstructions!=null &&
              this.specialInstructions.equals(other.getSpecialInstructions()))) &&
            ((this.lineItems==null && other.getLineItems()==null) || 
             (this.lineItems!=null &&
              java.util.Arrays.equals(this.lineItems, other.getLineItems()))) &&
            ((this.activities==null && other.getActivities()==null) || 
             (this.activities!=null &&
              java.util.Arrays.equals(this.activities, other.getActivities())));
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
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        if (getOrderIdentity() != null) {
            _hashCode += getOrderIdentity().hashCode();
        }
        if (getOrderNumber() != null) {
            _hashCode += getOrderNumber().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        _hashCode += getAdditionalServiceTime();
        if (getTakenBy() != null) {
            _hashCode += getTakenBy().hashCode();
        }
        _hashCode += (isPrinted() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUserDefinedFields() != null) {
            _hashCode += getUserDefinedFields().hashCode();
        }
        if (getDailyIncrease() != null) {
            _hashCode += getDailyIncrease().hashCode();
        }
        if (getBeginDate() != null) {
            _hashCode += getBeginDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        _hashCode += getUrgency();
        if (getSelector() != null) {
            _hashCode += getSelector().hashCode();
        }
        if (getOrderType() != null) {
            _hashCode += getOrderType().hashCode();
        }
        if (getOriginalPlanType() != null) {
            _hashCode += getOriginalPlanType().hashCode();
        }
        if (getLastPlanType() != null) {
            _hashCode += getLastPlanType().hashCode();
        }
        if (getTriggerType() != null) {
            _hashCode += getTriggerType().hashCode();
        }
        if (getOrderSource() != null) {
            _hashCode += getOrderSource().hashCode();
        }
        _hashCode += (isVoidOrder() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUploadSelector() != null) {
            _hashCode += getUploadSelector().hashCode();
        }
        if (getDateAdded() != null) {
            _hashCode += getDateAdded().hashCode();
        }
        if (getPreferedOrigin() != null) {
            _hashCode += getPreferedOrigin().hashCode();
        }
        _hashCode += (isForceBulkServiceTime() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDailyUrgencyIncrease();
        if (getBeginQuantity() != null) {
            _hashCode += getBeginQuantity().hashCode();
        }
        _hashCode += getBeginUrgency();
        if (getSpecialInstructions() != null) {
            _hashCode += getSpecialInstructions().hashCode();
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
        if (getActivities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivities(), i);
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
        new org.apache.axis.description.TypeDesc(RoutingOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingStopIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CategoryQuantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "additionalServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takenBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "takenBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("printed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "printed"));
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
        elemField.setFieldName("dailyIncrease");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dailyIncrease"));
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
        elemField.setFieldName("selector");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "selector"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalPlanType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "originalPlanType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingPlanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastPlanType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastPlanType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingPlanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("triggerType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "triggerType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingTriggerType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderSource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderSource"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voidOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "voidOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadSelector");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "uploadSelector"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("preferedOrigin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferedOrigin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
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
        elemField.setFieldName("dailyUrgencyIncrease");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dailyUrgencyIncrease"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "beginQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginUrgency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "beginUrgency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialInstructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "specialInstructions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingLineItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "activities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderActivity"));
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
