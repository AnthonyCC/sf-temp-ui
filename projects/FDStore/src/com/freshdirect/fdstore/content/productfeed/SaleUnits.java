//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.29 at 01:15:09 PM EDT 
//


package com.freshdirect.fdstore.content.productfeed;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saleUnits complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saleUnits">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="saleUnit" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="baseUnit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="estimatedWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saleUnits", propOrder = {
    "saleUnit"
})
public class SaleUnits {

    protected List<SaleUnits.SaleUnit> saleUnit;

    /**
     * Gets the value of the saleUnit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the saleUnit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSaleUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SaleUnits.SaleUnit }
     * 
     * 
     */
    public List<SaleUnits.SaleUnit> getSaleUnit() {
        if (saleUnit == null) {
            saleUnit = new ArrayList<SaleUnits.SaleUnit>();
        }
        return this.saleUnit;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="baseUnit" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="estimatedWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "baseUnit",
        "estimatedWeight",
        "description"
    })
    public static class SaleUnit {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String baseUnit;
        protected String estimatedWeight;
        @XmlElement(required = true)
        protected String description;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the baseUnit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBaseUnit() {
            return baseUnit;
        }

        /**
         * Sets the value of the baseUnit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBaseUnit(String value) {
            this.baseUnit = value;
        }

        /**
         * Gets the value of the estimatedWeight property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEstimatedWeight() {
            return estimatedWeight;
        }

        /**
         * Sets the value of the estimatedWeight property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEstimatedWeight(String value) {
            this.estimatedWeight = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

    }

}
