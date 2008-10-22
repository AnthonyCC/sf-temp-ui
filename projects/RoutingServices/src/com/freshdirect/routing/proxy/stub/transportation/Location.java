/**
 * Location.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Location  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity;

    private java.lang.String description;

    private java.lang.String accountType;

    private java.lang.String serviceTimeType;

    private java.lang.String timeWindowType;

    private int timeWindowFactor;

    private com.freshdirect.routing.proxy.stub.transportation.Address address;

    private int latitude;

    private int longitude;

    private int buildingDeliverySequence;

    private com.freshdirect.routing.proxy.stub.transportation.GeocodeResult locquality;

    private com.freshdirect.routing.proxy.stub.transportation.GeocodeConfidence locConfidence;

    private java.lang.String contact;

    private java.lang.String alternateContact;

    private java.lang.String faxNumber;

    private java.lang.String phoneNumber;

    private java.lang.String standardInstructions;

    private java.lang.String preferredRouteID;

    private java.lang.String storeNumber;

    private java.lang.String userDefinedField1;

    private java.lang.String userDefinedField2;

    private java.lang.String userDefinedField3;

    private int priority;

    private java.lang.String zoneID;

    private java.util.Date dateAdded;

    private java.lang.String deliveryDays;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities bulkThreashold;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] employees;

    private double fixedFee;

    private double variableFee;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone;

    private java.lang.Boolean markAsVoid;

    private com.freshdirect.routing.proxy.stub.transportation.LocationRejectCode rejectCode;

    private com.freshdirect.routing.proxy.stub.transportation.Consignee[] consigneeHistory;

    public Location() {
    }

    public Location(
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity,
           java.lang.String description,
           java.lang.String accountType,
           java.lang.String serviceTimeType,
           java.lang.String timeWindowType,
           int timeWindowFactor,
           com.freshdirect.routing.proxy.stub.transportation.Address address,
           int latitude,
           int longitude,
           int buildingDeliverySequence,
           com.freshdirect.routing.proxy.stub.transportation.GeocodeResult locquality,
           com.freshdirect.routing.proxy.stub.transportation.GeocodeConfidence locConfidence,
           java.lang.String contact,
           java.lang.String alternateContact,
           java.lang.String faxNumber,
           java.lang.String phoneNumber,
           java.lang.String standardInstructions,
           java.lang.String preferredRouteID,
           java.lang.String storeNumber,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           int priority,
           java.lang.String zoneID,
           java.util.Date dateAdded,
           java.lang.String deliveryDays,
           com.freshdirect.routing.proxy.stub.transportation.Quantities bulkThreashold,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] employees,
           double fixedFee,
           double variableFee,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone,
           java.lang.Boolean markAsVoid,
           com.freshdirect.routing.proxy.stub.transportation.LocationRejectCode rejectCode,
           com.freshdirect.routing.proxy.stub.transportation.Consignee[] consigneeHistory) {
           this.locationIdentity = locationIdentity;
           this.description = description;
           this.accountType = accountType;
           this.serviceTimeType = serviceTimeType;
           this.timeWindowType = timeWindowType;
           this.timeWindowFactor = timeWindowFactor;
           this.address = address;
           this.latitude = latitude;
           this.longitude = longitude;
           this.buildingDeliverySequence = buildingDeliverySequence;
           this.locquality = locquality;
           this.locConfidence = locConfidence;
           this.contact = contact;
           this.alternateContact = alternateContact;
           this.faxNumber = faxNumber;
           this.phoneNumber = phoneNumber;
           this.standardInstructions = standardInstructions;
           this.preferredRouteID = preferredRouteID;
           this.storeNumber = storeNumber;
           this.userDefinedField1 = userDefinedField1;
           this.userDefinedField2 = userDefinedField2;
           this.userDefinedField3 = userDefinedField3;
           this.priority = priority;
           this.zoneID = zoneID;
           this.dateAdded = dateAdded;
           this.deliveryDays = deliveryDays;
           this.bulkThreashold = bulkThreashold;
           this.employees = employees;
           this.fixedFee = fixedFee;
           this.variableFee = variableFee;
           this.timeZone = timeZone;
           this.markAsVoid = markAsVoid;
           this.rejectCode = rejectCode;
           this.consigneeHistory = consigneeHistory;
    }


    /**
     * Gets the locationIdentity value for this Location.
     * 
     * @return locationIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getLocationIdentity() {
        return locationIdentity;
    }


    /**
     * Sets the locationIdentity value for this Location.
     * 
     * @param locationIdentity
     */
    public void setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity) {
        this.locationIdentity = locationIdentity;
    }


    /**
     * Gets the description value for this Location.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Location.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the accountType value for this Location.
     * 
     * @return accountType
     */
    public java.lang.String getAccountType() {
        return accountType;
    }


    /**
     * Sets the accountType value for this Location.
     * 
     * @param accountType
     */
    public void setAccountType(java.lang.String accountType) {
        this.accountType = accountType;
    }


    /**
     * Gets the serviceTimeType value for this Location.
     * 
     * @return serviceTimeType
     */
    public java.lang.String getServiceTimeType() {
        return serviceTimeType;
    }


    /**
     * Sets the serviceTimeType value for this Location.
     * 
     * @param serviceTimeType
     */
    public void setServiceTimeType(java.lang.String serviceTimeType) {
        this.serviceTimeType = serviceTimeType;
    }


    /**
     * Gets the timeWindowType value for this Location.
     * 
     * @return timeWindowType
     */
    public java.lang.String getTimeWindowType() {
        return timeWindowType;
    }


    /**
     * Sets the timeWindowType value for this Location.
     * 
     * @param timeWindowType
     */
    public void setTimeWindowType(java.lang.String timeWindowType) {
        this.timeWindowType = timeWindowType;
    }


    /**
     * Gets the timeWindowFactor value for this Location.
     * 
     * @return timeWindowFactor
     */
    public int getTimeWindowFactor() {
        return timeWindowFactor;
    }


    /**
     * Sets the timeWindowFactor value for this Location.
     * 
     * @param timeWindowFactor
     */
    public void setTimeWindowFactor(int timeWindowFactor) {
        this.timeWindowFactor = timeWindowFactor;
    }


    /**
     * Gets the address value for this Location.
     * 
     * @return address
     */
    public com.freshdirect.routing.proxy.stub.transportation.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this Location.
     * 
     * @param address
     */
    public void setAddress(com.freshdirect.routing.proxy.stub.transportation.Address address) {
        this.address = address;
    }


    /**
     * Gets the latitude value for this Location.
     * 
     * @return latitude
     */
    public int getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this Location.
     * 
     * @param latitude
     */
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this Location.
     * 
     * @return longitude
     */
    public int getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this Location.
     * 
     * @param longitude
     */
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the buildingDeliverySequence value for this Location.
     * 
     * @return buildingDeliverySequence
     */
    public int getBuildingDeliverySequence() {
        return buildingDeliverySequence;
    }


    /**
     * Sets the buildingDeliverySequence value for this Location.
     * 
     * @param buildingDeliverySequence
     */
    public void setBuildingDeliverySequence(int buildingDeliverySequence) {
        this.buildingDeliverySequence = buildingDeliverySequence;
    }


    /**
     * Gets the locquality value for this Location.
     * 
     * @return locquality
     */
    public com.freshdirect.routing.proxy.stub.transportation.GeocodeResult getLocquality() {
        return locquality;
    }


    /**
     * Sets the locquality value for this Location.
     * 
     * @param locquality
     */
    public void setLocquality(com.freshdirect.routing.proxy.stub.transportation.GeocodeResult locquality) {
        this.locquality = locquality;
    }


    /**
     * Gets the locConfidence value for this Location.
     * 
     * @return locConfidence
     */
    public com.freshdirect.routing.proxy.stub.transportation.GeocodeConfidence getLocConfidence() {
        return locConfidence;
    }


    /**
     * Sets the locConfidence value for this Location.
     * 
     * @param locConfidence
     */
    public void setLocConfidence(com.freshdirect.routing.proxy.stub.transportation.GeocodeConfidence locConfidence) {
        this.locConfidence = locConfidence;
    }


    /**
     * Gets the contact value for this Location.
     * 
     * @return contact
     */
    public java.lang.String getContact() {
        return contact;
    }


    /**
     * Sets the contact value for this Location.
     * 
     * @param contact
     */
    public void setContact(java.lang.String contact) {
        this.contact = contact;
    }


    /**
     * Gets the alternateContact value for this Location.
     * 
     * @return alternateContact
     */
    public java.lang.String getAlternateContact() {
        return alternateContact;
    }


    /**
     * Sets the alternateContact value for this Location.
     * 
     * @param alternateContact
     */
    public void setAlternateContact(java.lang.String alternateContact) {
        this.alternateContact = alternateContact;
    }


    /**
     * Gets the faxNumber value for this Location.
     * 
     * @return faxNumber
     */
    public java.lang.String getFaxNumber() {
        return faxNumber;
    }


    /**
     * Sets the faxNumber value for this Location.
     * 
     * @param faxNumber
     */
    public void setFaxNumber(java.lang.String faxNumber) {
        this.faxNumber = faxNumber;
    }


    /**
     * Gets the phoneNumber value for this Location.
     * 
     * @return phoneNumber
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Sets the phoneNumber value for this Location.
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Gets the standardInstructions value for this Location.
     * 
     * @return standardInstructions
     */
    public java.lang.String getStandardInstructions() {
        return standardInstructions;
    }


    /**
     * Sets the standardInstructions value for this Location.
     * 
     * @param standardInstructions
     */
    public void setStandardInstructions(java.lang.String standardInstructions) {
        this.standardInstructions = standardInstructions;
    }


    /**
     * Gets the preferredRouteID value for this Location.
     * 
     * @return preferredRouteID
     */
    public java.lang.String getPreferredRouteID() {
        return preferredRouteID;
    }


    /**
     * Sets the preferredRouteID value for this Location.
     * 
     * @param preferredRouteID
     */
    public void setPreferredRouteID(java.lang.String preferredRouteID) {
        this.preferredRouteID = preferredRouteID;
    }


    /**
     * Gets the storeNumber value for this Location.
     * 
     * @return storeNumber
     */
    public java.lang.String getStoreNumber() {
        return storeNumber;
    }


    /**
     * Sets the storeNumber value for this Location.
     * 
     * @param storeNumber
     */
    public void setStoreNumber(java.lang.String storeNumber) {
        this.storeNumber = storeNumber;
    }


    /**
     * Gets the userDefinedField1 value for this Location.
     * 
     * @return userDefinedField1
     */
    public java.lang.String getUserDefinedField1() {
        return userDefinedField1;
    }


    /**
     * Sets the userDefinedField1 value for this Location.
     * 
     * @param userDefinedField1
     */
    public void setUserDefinedField1(java.lang.String userDefinedField1) {
        this.userDefinedField1 = userDefinedField1;
    }


    /**
     * Gets the userDefinedField2 value for this Location.
     * 
     * @return userDefinedField2
     */
    public java.lang.String getUserDefinedField2() {
        return userDefinedField2;
    }


    /**
     * Sets the userDefinedField2 value for this Location.
     * 
     * @param userDefinedField2
     */
    public void setUserDefinedField2(java.lang.String userDefinedField2) {
        this.userDefinedField2 = userDefinedField2;
    }


    /**
     * Gets the userDefinedField3 value for this Location.
     * 
     * @return userDefinedField3
     */
    public java.lang.String getUserDefinedField3() {
        return userDefinedField3;
    }


    /**
     * Sets the userDefinedField3 value for this Location.
     * 
     * @param userDefinedField3
     */
    public void setUserDefinedField3(java.lang.String userDefinedField3) {
        this.userDefinedField3 = userDefinedField3;
    }


    /**
     * Gets the priority value for this Location.
     * 
     * @return priority
     */
    public int getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this Location.
     * 
     * @param priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }


    /**
     * Gets the zoneID value for this Location.
     * 
     * @return zoneID
     */
    public java.lang.String getZoneID() {
        return zoneID;
    }


    /**
     * Sets the zoneID value for this Location.
     * 
     * @param zoneID
     */
    public void setZoneID(java.lang.String zoneID) {
        this.zoneID = zoneID;
    }


    /**
     * Gets the dateAdded value for this Location.
     * 
     * @return dateAdded
     */
    public java.util.Date getDateAdded() {
        return dateAdded;
    }


    /**
     * Sets the dateAdded value for this Location.
     * 
     * @param dateAdded
     */
    public void setDateAdded(java.util.Date dateAdded) {
        this.dateAdded = dateAdded;
    }


    /**
     * Gets the deliveryDays value for this Location.
     * 
     * @return deliveryDays
     */
    public java.lang.String getDeliveryDays() {
        return deliveryDays;
    }


    /**
     * Sets the deliveryDays value for this Location.
     * 
     * @param deliveryDays
     */
    public void setDeliveryDays(java.lang.String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }


    /**
     * Gets the bulkThreashold value for this Location.
     * 
     * @return bulkThreashold
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getBulkThreashold() {
        return bulkThreashold;
    }


    /**
     * Sets the bulkThreashold value for this Location.
     * 
     * @param bulkThreashold
     */
    public void setBulkThreashold(com.freshdirect.routing.proxy.stub.transportation.Quantities bulkThreashold) {
        this.bulkThreashold = bulkThreashold;
    }


    /**
     * Gets the employees value for this Location.
     * 
     * @return employees
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] getEmployees() {
        return employees;
    }


    /**
     * Sets the employees value for this Location.
     * 
     * @param employees
     */
    public void setEmployees(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] employees) {
        this.employees = employees;
    }

    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getEmployees(int i) {
        return this.employees[i];
    }

    public void setEmployees(int i, com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity _value) {
        this.employees[i] = _value;
    }


    /**
     * Gets the fixedFee value for this Location.
     * 
     * @return fixedFee
     */
    public double getFixedFee() {
        return fixedFee;
    }


    /**
     * Sets the fixedFee value for this Location.
     * 
     * @param fixedFee
     */
    public void setFixedFee(double fixedFee) {
        this.fixedFee = fixedFee;
    }


    /**
     * Gets the variableFee value for this Location.
     * 
     * @return variableFee
     */
    public double getVariableFee() {
        return variableFee;
    }


    /**
     * Sets the variableFee value for this Location.
     * 
     * @param variableFee
     */
    public void setVariableFee(double variableFee) {
        this.variableFee = variableFee;
    }


    /**
     * Gets the timeZone value for this Location.
     * 
     * @return timeZone
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getTimeZone() {
        return timeZone;
    }


    /**
     * Sets the timeZone value for this Location.
     * 
     * @param timeZone
     */
    public void setTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone) {
        this.timeZone = timeZone;
    }


    /**
     * Gets the markAsVoid value for this Location.
     * 
     * @return markAsVoid
     */
    public java.lang.Boolean getMarkAsVoid() {
        return markAsVoid;
    }


    /**
     * Sets the markAsVoid value for this Location.
     * 
     * @param markAsVoid
     */
    public void setMarkAsVoid(java.lang.Boolean markAsVoid) {
        this.markAsVoid = markAsVoid;
    }


    /**
     * Gets the rejectCode value for this Location.
     * 
     * @return rejectCode
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationRejectCode getRejectCode() {
        return rejectCode;
    }


    /**
     * Sets the rejectCode value for this Location.
     * 
     * @param rejectCode
     */
    public void setRejectCode(com.freshdirect.routing.proxy.stub.transportation.LocationRejectCode rejectCode) {
        this.rejectCode = rejectCode;
    }


    /**
     * Gets the consigneeHistory value for this Location.
     * 
     * @return consigneeHistory
     */
    public com.freshdirect.routing.proxy.stub.transportation.Consignee[] getConsigneeHistory() {
        return consigneeHistory;
    }


    /**
     * Sets the consigneeHistory value for this Location.
     * 
     * @param consigneeHistory
     */
    public void setConsigneeHistory(com.freshdirect.routing.proxy.stub.transportation.Consignee[] consigneeHistory) {
        this.consigneeHistory = consigneeHistory;
    }

    public com.freshdirect.routing.proxy.stub.transportation.Consignee getConsigneeHistory(int i) {
        return this.consigneeHistory[i];
    }

    public void setConsigneeHistory(int i, com.freshdirect.routing.proxy.stub.transportation.Consignee _value) {
        this.consigneeHistory[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Location)) return false;
        Location other = (Location) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.locationIdentity==null && other.getLocationIdentity()==null) || 
             (this.locationIdentity!=null &&
              this.locationIdentity.equals(other.getLocationIdentity()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.accountType==null && other.getAccountType()==null) || 
             (this.accountType!=null &&
              this.accountType.equals(other.getAccountType()))) &&
            ((this.serviceTimeType==null && other.getServiceTimeType()==null) || 
             (this.serviceTimeType!=null &&
              this.serviceTimeType.equals(other.getServiceTimeType()))) &&
            ((this.timeWindowType==null && other.getTimeWindowType()==null) || 
             (this.timeWindowType!=null &&
              this.timeWindowType.equals(other.getTimeWindowType()))) &&
            this.timeWindowFactor == other.getTimeWindowFactor() &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            this.latitude == other.getLatitude() &&
            this.longitude == other.getLongitude() &&
            this.buildingDeliverySequence == other.getBuildingDeliverySequence() &&
            ((this.locquality==null && other.getLocquality()==null) || 
             (this.locquality!=null &&
              this.locquality.equals(other.getLocquality()))) &&
            ((this.locConfidence==null && other.getLocConfidence()==null) || 
             (this.locConfidence!=null &&
              this.locConfidence.equals(other.getLocConfidence()))) &&
            ((this.contact==null && other.getContact()==null) || 
             (this.contact!=null &&
              this.contact.equals(other.getContact()))) &&
            ((this.alternateContact==null && other.getAlternateContact()==null) || 
             (this.alternateContact!=null &&
              this.alternateContact.equals(other.getAlternateContact()))) &&
            ((this.faxNumber==null && other.getFaxNumber()==null) || 
             (this.faxNumber!=null &&
              this.faxNumber.equals(other.getFaxNumber()))) &&
            ((this.phoneNumber==null && other.getPhoneNumber()==null) || 
             (this.phoneNumber!=null &&
              this.phoneNumber.equals(other.getPhoneNumber()))) &&
            ((this.standardInstructions==null && other.getStandardInstructions()==null) || 
             (this.standardInstructions!=null &&
              this.standardInstructions.equals(other.getStandardInstructions()))) &&
            ((this.preferredRouteID==null && other.getPreferredRouteID()==null) || 
             (this.preferredRouteID!=null &&
              this.preferredRouteID.equals(other.getPreferredRouteID()))) &&
            ((this.storeNumber==null && other.getStoreNumber()==null) || 
             (this.storeNumber!=null &&
              this.storeNumber.equals(other.getStoreNumber()))) &&
            ((this.userDefinedField1==null && other.getUserDefinedField1()==null) || 
             (this.userDefinedField1!=null &&
              this.userDefinedField1.equals(other.getUserDefinedField1()))) &&
            ((this.userDefinedField2==null && other.getUserDefinedField2()==null) || 
             (this.userDefinedField2!=null &&
              this.userDefinedField2.equals(other.getUserDefinedField2()))) &&
            ((this.userDefinedField3==null && other.getUserDefinedField3()==null) || 
             (this.userDefinedField3!=null &&
              this.userDefinedField3.equals(other.getUserDefinedField3()))) &&
            this.priority == other.getPriority() &&
            ((this.zoneID==null && other.getZoneID()==null) || 
             (this.zoneID!=null &&
              this.zoneID.equals(other.getZoneID()))) &&
            ((this.dateAdded==null && other.getDateAdded()==null) || 
             (this.dateAdded!=null &&
              this.dateAdded.equals(other.getDateAdded()))) &&
            ((this.deliveryDays==null && other.getDeliveryDays()==null) || 
             (this.deliveryDays!=null &&
              this.deliveryDays.equals(other.getDeliveryDays()))) &&
            ((this.bulkThreashold==null && other.getBulkThreashold()==null) || 
             (this.bulkThreashold!=null &&
              this.bulkThreashold.equals(other.getBulkThreashold()))) &&
            ((this.employees==null && other.getEmployees()==null) || 
             (this.employees!=null &&
              java.util.Arrays.equals(this.employees, other.getEmployees()))) &&
            this.fixedFee == other.getFixedFee() &&
            this.variableFee == other.getVariableFee() &&
            ((this.timeZone==null && other.getTimeZone()==null) || 
             (this.timeZone!=null &&
              this.timeZone.equals(other.getTimeZone()))) &&
            ((this.markAsVoid==null && other.getMarkAsVoid()==null) || 
             (this.markAsVoid!=null &&
              this.markAsVoid.equals(other.getMarkAsVoid()))) &&
            ((this.rejectCode==null && other.getRejectCode()==null) || 
             (this.rejectCode!=null &&
              this.rejectCode.equals(other.getRejectCode()))) &&
            ((this.consigneeHistory==null && other.getConsigneeHistory()==null) || 
             (this.consigneeHistory!=null &&
              java.util.Arrays.equals(this.consigneeHistory, other.getConsigneeHistory())));
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
        if (getLocationIdentity() != null) {
            _hashCode += getLocationIdentity().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getAccountType() != null) {
            _hashCode += getAccountType().hashCode();
        }
        if (getServiceTimeType() != null) {
            _hashCode += getServiceTimeType().hashCode();
        }
        if (getTimeWindowType() != null) {
            _hashCode += getTimeWindowType().hashCode();
        }
        _hashCode += getTimeWindowFactor();
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        _hashCode += getLatitude();
        _hashCode += getLongitude();
        _hashCode += getBuildingDeliverySequence();
        if (getLocquality() != null) {
            _hashCode += getLocquality().hashCode();
        }
        if (getLocConfidence() != null) {
            _hashCode += getLocConfidence().hashCode();
        }
        if (getContact() != null) {
            _hashCode += getContact().hashCode();
        }
        if (getAlternateContact() != null) {
            _hashCode += getAlternateContact().hashCode();
        }
        if (getFaxNumber() != null) {
            _hashCode += getFaxNumber().hashCode();
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getStandardInstructions() != null) {
            _hashCode += getStandardInstructions().hashCode();
        }
        if (getPreferredRouteID() != null) {
            _hashCode += getPreferredRouteID().hashCode();
        }
        if (getStoreNumber() != null) {
            _hashCode += getStoreNumber().hashCode();
        }
        if (getUserDefinedField1() != null) {
            _hashCode += getUserDefinedField1().hashCode();
        }
        if (getUserDefinedField2() != null) {
            _hashCode += getUserDefinedField2().hashCode();
        }
        if (getUserDefinedField3() != null) {
            _hashCode += getUserDefinedField3().hashCode();
        }
        _hashCode += getPriority();
        if (getZoneID() != null) {
            _hashCode += getZoneID().hashCode();
        }
        if (getDateAdded() != null) {
            _hashCode += getDateAdded().hashCode();
        }
        if (getDeliveryDays() != null) {
            _hashCode += getDeliveryDays().hashCode();
        }
        if (getBulkThreashold() != null) {
            _hashCode += getBulkThreashold().hashCode();
        }
        if (getEmployees() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEmployees());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEmployees(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += new Double(getFixedFee()).hashCode();
        _hashCode += new Double(getVariableFee()).hashCode();
        if (getTimeZone() != null) {
            _hashCode += getTimeZone().hashCode();
        }
        if (getMarkAsVoid() != null) {
            _hashCode += getMarkAsVoid().hashCode();
        }
        if (getRejectCode() != null) {
            _hashCode += getRejectCode().hashCode();
        }
        if (getConsigneeHistory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConsigneeHistory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConsigneeHistory(), i);
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
        new org.apache.axis.description.TypeDesc(Location.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Location"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "accountType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceTimeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceTimeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeWindowType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeWindowType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeWindowFactor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeWindowFactor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buildingDeliverySequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "buildingDeliverySequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locquality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locquality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "GeocodeResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locConfidence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locConfidence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "GeocodeConfidence"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "contact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternateContact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "alternateContact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faxNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "faxNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "phoneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standardInstructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "standardInstructions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredRouteID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredRouteID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storeNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "storeNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zoneID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "zoneID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAdded");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateAdded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryDays");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryDays"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bulkThreashold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "bulkThreashold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employees");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "employees"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fixedFee");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "fixedFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variableFee");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "variableFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneValue"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markAsVoid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "markAsVoid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rejectCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "rejectCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationRejectCode"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consigneeHistory");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "consigneeHistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Consignee"));
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
