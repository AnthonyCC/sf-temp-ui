//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.25 at 11:23:41 AM CEST 
//


package com.freshdirect.fdstore.content.productfeed;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for images complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="images">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="image" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="imgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="imgUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "images", propOrder = {
    "image"
})
public class Images {

    protected List<Images.Image> image;

    /**
     * Gets the value of the image property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the image property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Images.Image }
     * 
     * 
     */
    public List<Images.Image> getImage() {
        if (image == null) {
            image = new ArrayList<Images.Image>();
        }
        return this.image;
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
     *       &lt;attribute name="imgType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="imgUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Image {

        @XmlAttribute(name = "imgType", required = true)
        protected String imgType;
        @XmlAttribute(name = "imgUrl", required = true)
        protected String imgUrl;

        /**
         * Gets the value of the imgType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImgType() {
            return imgType;
        }

        /**
         * Sets the value of the imgType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImgType(String value) {
            this.imgType = value;
        }

        /**
         * Gets the value of the imgUrl property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImgUrl() {
            return imgUrl;
        }

        /**
         * Sets the value of the imgUrl property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImgUrl(String value) {
            this.imgUrl = value;
        }

    }

}
