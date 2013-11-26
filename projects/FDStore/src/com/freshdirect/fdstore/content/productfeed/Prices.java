//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.25 at 03:39:42 PM EST 
//


package com.freshdirect.fdstore.content.productfeed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for prices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="prices">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="price" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="zoneCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                   &lt;element name="unitDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="unitWeight" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="scaleQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="salePrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *                   &lt;element name="saleStartTime" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="saleEndTime" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
@XmlType(name = "prices", propOrder = {
    "price"
})
public class Prices {

    protected List<Prices.Price> price;

    /**
     * Gets the value of the price property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the price property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Prices.Price }
     * 
     * 
     */
    public List<Prices.Price> getPrice() {
        if (price == null) {
            price = new ArrayList<Prices.Price>();
        }
        return this.price;
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
     *         &lt;element name="zoneCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *         &lt;element name="unitDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="unitWeight" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="scaleQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="salePrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
     *         &lt;element name="saleStartTime" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="saleEndTime" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
        "zoneCode",
        "unitPrice",
        "unitDescription",
        "unitWeight",
        "scaleQuantity",
        "salePrice",
        "saleStartTime",
        "saleEndTime"
    })
    public static class Price {

        @XmlElement(required = true)
        protected String zoneCode;
        @XmlElement(required = true)
        protected BigDecimal unitPrice;
        @XmlElement(required = true)
        protected String unitDescription;
        @XmlElement(required = true)
        protected String unitWeight;
        @XmlElement(required = true)
        protected String scaleQuantity;
        protected BigDecimal salePrice;
        @XmlElement(type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "date")
        protected Calendar saleStartTime;
        @XmlElement(type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "date")
        protected Calendar saleEndTime;

        /**
         * Gets the value of the zoneCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZoneCode() {
            return zoneCode;
        }

        /**
         * Sets the value of the zoneCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZoneCode(String value) {
            this.zoneCode = value;
        }

        /**
         * Gets the value of the unitPrice property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        /**
         * Sets the value of the unitPrice property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setUnitPrice(BigDecimal value) {
            this.unitPrice = value;
        }

        /**
         * Gets the value of the unitDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUnitDescription() {
            return unitDescription;
        }

        /**
         * Sets the value of the unitDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUnitDescription(String value) {
            this.unitDescription = value;
        }

        /**
         * Gets the value of the unitWeight property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUnitWeight() {
            return unitWeight;
        }

        /**
         * Sets the value of the unitWeight property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUnitWeight(String value) {
            this.unitWeight = value;
        }

        /**
         * Gets the value of the scaleQuantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScaleQuantity() {
            return scaleQuantity;
        }

        /**
         * Sets the value of the scaleQuantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScaleQuantity(String value) {
            this.scaleQuantity = value;
        }

        /**
         * Gets the value of the salePrice property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSalePrice() {
            return salePrice;
        }

        /**
         * Sets the value of the salePrice property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSalePrice(BigDecimal value) {
            this.salePrice = value;
        }

        /**
         * Gets the value of the saleStartTime property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Calendar getSaleStartTime() {
            return saleStartTime;
        }

        /**
         * Sets the value of the saleStartTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSaleStartTime(Calendar value) {
            this.saleStartTime = value;
        }

        /**
         * Gets the value of the saleEndTime property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Calendar getSaleEndTime() {
            return saleEndTime;
        }

        /**
         * Sets the value of the saleEndTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSaleEndTime(Calendar value) {
            this.saleEndTime = value;
        }

    }

}
