//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.09 at 11:07:07 AM EST 
//


package com.freshdirect.fdstore.content.productfeed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for groupPrices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupPrices">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupPrice" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="zoneCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="groupMaterials" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                   &lt;element name="unitWeight" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="groupQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="groupId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="groupDesc" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "groupPrices", propOrder = {
    "groupPrice"
})
public class GroupPrices {

    protected List<GroupPrices.GroupPrice> groupPrice;

    /**
     * Gets the value of the groupPrice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupPrice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupPrice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupPrices.GroupPrice }
     * 
     * 
     */
    public List<GroupPrices.GroupPrice> getGroupPrice() {
        if (groupPrice == null) {
            groupPrice = new ArrayList<GroupPrices.GroupPrice>();
        }
        return this.groupPrice;
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
     *         &lt;element name="groupMaterials" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *         &lt;element name="unitWeight" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="groupQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="groupId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="groupDesc" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "groupMaterials",
        "unitPrice",
        "unitWeight",
        "groupQuantity"
    })
    public static class GroupPrice {

        @XmlElement(required = true)
        protected String zoneCode;
        @XmlElement(required = true)
        protected String groupMaterials;
        @XmlElement(required = true)
        protected BigDecimal unitPrice;
        @XmlElement(required = true)
        protected String unitWeight;
        @XmlElement(required = true)
        protected String groupQuantity;
        @XmlAttribute(required = true)
        protected String groupId;
        @XmlAttribute(required = true)
        protected String groupDesc;

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
         * Gets the value of the groupMaterials property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupMaterials() {
            return groupMaterials;
        }

        /**
         * Sets the value of the groupMaterials property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupMaterials(String value) {
            this.groupMaterials = value;
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
         * Gets the value of the groupQuantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupQuantity() {
            return groupQuantity;
        }

        /**
         * Sets the value of the groupQuantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupQuantity(String value) {
            this.groupQuantity = value;
        }

        /**
         * Gets the value of the groupId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupId() {
            return groupId;
        }

        /**
         * Sets the value of the groupId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupId(String value) {
            this.groupId = value;
        }

        /**
         * Gets the value of the groupDesc property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupDesc() {
            return groupDesc;
        }

        /**
         * Sets the value of the groupDesc property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupDesc(String value) {
            this.groupDesc = value;
        }

    }

}
