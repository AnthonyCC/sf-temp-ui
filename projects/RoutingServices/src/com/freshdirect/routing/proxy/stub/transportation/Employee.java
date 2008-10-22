/**
 * Employee.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Employee  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity;

    private java.lang.String employeeType;

    private boolean active;

    private java.lang.String employeeStatus;

    private java.lang.String lastName;

    private java.lang.String firstName;

    private java.lang.String middleName;

    private java.lang.String homePhoneNumber;

    private java.lang.String workPhoneNumber;

    private java.util.Date dateOfBirth;

    private java.util.Date dateStarted;

    private java.util.Date dateTerminated;

    private java.lang.String ADPFileNumber;

    private java.lang.String password;

    private com.freshdirect.routing.proxy.stub.transportation.Address address;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity;

    public Employee() {
    }

    public Employee(
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity,
           java.lang.String employeeType,
           boolean active,
           java.lang.String employeeStatus,
           java.lang.String lastName,
           java.lang.String firstName,
           java.lang.String middleName,
           java.lang.String homePhoneNumber,
           java.lang.String workPhoneNumber,
           java.util.Date dateOfBirth,
           java.util.Date dateStarted,
           java.util.Date dateTerminated,
           java.lang.String ADPFileNumber,
           java.lang.String password,
           com.freshdirect.routing.proxy.stub.transportation.Address address,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity) {
           this.employeeIdentity = employeeIdentity;
           this.employeeType = employeeType;
           this.active = active;
           this.employeeStatus = employeeStatus;
           this.lastName = lastName;
           this.firstName = firstName;
           this.middleName = middleName;
           this.homePhoneNumber = homePhoneNumber;
           this.workPhoneNumber = workPhoneNumber;
           this.dateOfBirth = dateOfBirth;
           this.dateStarted = dateStarted;
           this.dateTerminated = dateTerminated;
           this.ADPFileNumber = ADPFileNumber;
           this.password = password;
           this.address = address;
           this.currentRouteIdentity = currentRouteIdentity;
    }


    /**
     * Gets the employeeIdentity value for this Employee.
     * 
     * @return employeeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getEmployeeIdentity() {
        return employeeIdentity;
    }


    /**
     * Sets the employeeIdentity value for this Employee.
     * 
     * @param employeeIdentity
     */
    public void setEmployeeIdentity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity) {
        this.employeeIdentity = employeeIdentity;
    }


    /**
     * Gets the employeeType value for this Employee.
     * 
     * @return employeeType
     */
    public java.lang.String getEmployeeType() {
        return employeeType;
    }


    /**
     * Sets the employeeType value for this Employee.
     * 
     * @param employeeType
     */
    public void setEmployeeType(java.lang.String employeeType) {
        this.employeeType = employeeType;
    }


    /**
     * Gets the active value for this Employee.
     * 
     * @return active
     */
    public boolean isActive() {
        return active;
    }


    /**
     * Sets the active value for this Employee.
     * 
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Gets the employeeStatus value for this Employee.
     * 
     * @return employeeStatus
     */
    public java.lang.String getEmployeeStatus() {
        return employeeStatus;
    }


    /**
     * Sets the employeeStatus value for this Employee.
     * 
     * @param employeeStatus
     */
    public void setEmployeeStatus(java.lang.String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }


    /**
     * Gets the lastName value for this Employee.
     * 
     * @return lastName
     */
    public java.lang.String getLastName() {
        return lastName;
    }


    /**
     * Sets the lastName value for this Employee.
     * 
     * @param lastName
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }


    /**
     * Gets the firstName value for this Employee.
     * 
     * @return firstName
     */
    public java.lang.String getFirstName() {
        return firstName;
    }


    /**
     * Sets the firstName value for this Employee.
     * 
     * @param firstName
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }


    /**
     * Gets the middleName value for this Employee.
     * 
     * @return middleName
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }


    /**
     * Sets the middleName value for this Employee.
     * 
     * @param middleName
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }


    /**
     * Gets the homePhoneNumber value for this Employee.
     * 
     * @return homePhoneNumber
     */
    public java.lang.String getHomePhoneNumber() {
        return homePhoneNumber;
    }


    /**
     * Sets the homePhoneNumber value for this Employee.
     * 
     * @param homePhoneNumber
     */
    public void setHomePhoneNumber(java.lang.String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }


    /**
     * Gets the workPhoneNumber value for this Employee.
     * 
     * @return workPhoneNumber
     */
    public java.lang.String getWorkPhoneNumber() {
        return workPhoneNumber;
    }


    /**
     * Sets the workPhoneNumber value for this Employee.
     * 
     * @param workPhoneNumber
     */
    public void setWorkPhoneNumber(java.lang.String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }


    /**
     * Gets the dateOfBirth value for this Employee.
     * 
     * @return dateOfBirth
     */
    public java.util.Date getDateOfBirth() {
        return dateOfBirth;
    }


    /**
     * Sets the dateOfBirth value for this Employee.
     * 
     * @param dateOfBirth
     */
    public void setDateOfBirth(java.util.Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    /**
     * Gets the dateStarted value for this Employee.
     * 
     * @return dateStarted
     */
    public java.util.Date getDateStarted() {
        return dateStarted;
    }


    /**
     * Sets the dateStarted value for this Employee.
     * 
     * @param dateStarted
     */
    public void setDateStarted(java.util.Date dateStarted) {
        this.dateStarted = dateStarted;
    }


    /**
     * Gets the dateTerminated value for this Employee.
     * 
     * @return dateTerminated
     */
    public java.util.Date getDateTerminated() {
        return dateTerminated;
    }


    /**
     * Sets the dateTerminated value for this Employee.
     * 
     * @param dateTerminated
     */
    public void setDateTerminated(java.util.Date dateTerminated) {
        this.dateTerminated = dateTerminated;
    }


    /**
     * Gets the ADPFileNumber value for this Employee.
     * 
     * @return ADPFileNumber
     */
    public java.lang.String getADPFileNumber() {
        return ADPFileNumber;
    }


    /**
     * Sets the ADPFileNumber value for this Employee.
     * 
     * @param ADPFileNumber
     */
    public void setADPFileNumber(java.lang.String ADPFileNumber) {
        this.ADPFileNumber = ADPFileNumber;
    }


    /**
     * Gets the password value for this Employee.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this Employee.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the address value for this Employee.
     * 
     * @return address
     */
    public com.freshdirect.routing.proxy.stub.transportation.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this Employee.
     * 
     * @param address
     */
    public void setAddress(com.freshdirect.routing.proxy.stub.transportation.Address address) {
        this.address = address;
    }


    /**
     * Gets the currentRouteIdentity value for this Employee.
     * 
     * @return currentRouteIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getCurrentRouteIdentity() {
        return currentRouteIdentity;
    }


    /**
     * Sets the currentRouteIdentity value for this Employee.
     * 
     * @param currentRouteIdentity
     */
    public void setCurrentRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity) {
        this.currentRouteIdentity = currentRouteIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Employee)) return false;
        Employee other = (Employee) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.employeeIdentity==null && other.getEmployeeIdentity()==null) || 
             (this.employeeIdentity!=null &&
              this.employeeIdentity.equals(other.getEmployeeIdentity()))) &&
            ((this.employeeType==null && other.getEmployeeType()==null) || 
             (this.employeeType!=null &&
              this.employeeType.equals(other.getEmployeeType()))) &&
            this.active == other.isActive() &&
            ((this.employeeStatus==null && other.getEmployeeStatus()==null) || 
             (this.employeeStatus!=null &&
              this.employeeStatus.equals(other.getEmployeeStatus()))) &&
            ((this.lastName==null && other.getLastName()==null) || 
             (this.lastName!=null &&
              this.lastName.equals(other.getLastName()))) &&
            ((this.firstName==null && other.getFirstName()==null) || 
             (this.firstName!=null &&
              this.firstName.equals(other.getFirstName()))) &&
            ((this.middleName==null && other.getMiddleName()==null) || 
             (this.middleName!=null &&
              this.middleName.equals(other.getMiddleName()))) &&
            ((this.homePhoneNumber==null && other.getHomePhoneNumber()==null) || 
             (this.homePhoneNumber!=null &&
              this.homePhoneNumber.equals(other.getHomePhoneNumber()))) &&
            ((this.workPhoneNumber==null && other.getWorkPhoneNumber()==null) || 
             (this.workPhoneNumber!=null &&
              this.workPhoneNumber.equals(other.getWorkPhoneNumber()))) &&
            ((this.dateOfBirth==null && other.getDateOfBirth()==null) || 
             (this.dateOfBirth!=null &&
              this.dateOfBirth.equals(other.getDateOfBirth()))) &&
            ((this.dateStarted==null && other.getDateStarted()==null) || 
             (this.dateStarted!=null &&
              this.dateStarted.equals(other.getDateStarted()))) &&
            ((this.dateTerminated==null && other.getDateTerminated()==null) || 
             (this.dateTerminated!=null &&
              this.dateTerminated.equals(other.getDateTerminated()))) &&
            ((this.ADPFileNumber==null && other.getADPFileNumber()==null) || 
             (this.ADPFileNumber!=null &&
              this.ADPFileNumber.equals(other.getADPFileNumber()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.currentRouteIdentity==null && other.getCurrentRouteIdentity()==null) || 
             (this.currentRouteIdentity!=null &&
              this.currentRouteIdentity.equals(other.getCurrentRouteIdentity())));
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
        if (getEmployeeIdentity() != null) {
            _hashCode += getEmployeeIdentity().hashCode();
        }
        if (getEmployeeType() != null) {
            _hashCode += getEmployeeType().hashCode();
        }
        _hashCode += (isActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEmployeeStatus() != null) {
            _hashCode += getEmployeeStatus().hashCode();
        }
        if (getLastName() != null) {
            _hashCode += getLastName().hashCode();
        }
        if (getFirstName() != null) {
            _hashCode += getFirstName().hashCode();
        }
        if (getMiddleName() != null) {
            _hashCode += getMiddleName().hashCode();
        }
        if (getHomePhoneNumber() != null) {
            _hashCode += getHomePhoneNumber().hashCode();
        }
        if (getWorkPhoneNumber() != null) {
            _hashCode += getWorkPhoneNumber().hashCode();
        }
        if (getDateOfBirth() != null) {
            _hashCode += getDateOfBirth().hashCode();
        }
        if (getDateStarted() != null) {
            _hashCode += getDateStarted().hashCode();
        }
        if (getDateTerminated() != null) {
            _hashCode += getDateTerminated().hashCode();
        }
        if (getADPFileNumber() != null) {
            _hashCode += getADPFileNumber().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getCurrentRouteIdentity() != null) {
            _hashCode += getCurrentRouteIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Employee.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Employee"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "employeeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "employeeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("active");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "active"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("employeeStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "employeeStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "firstName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("middleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "middleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("homePhoneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "homePhoneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workPhoneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "workPhoneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOfBirth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateOfBirth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateStarted");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateStarted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTerminated");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateTerminated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ADPFileNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ADPFileNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("currentRouteIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "currentRouteIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
