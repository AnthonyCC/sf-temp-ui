
/**
 * NotificationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  NotificationType bean class
            */
        
        public  class NotificationType
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                "NotificationType",
                "ns1");

            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for NotificationType
                        */

                        
                                    protected java.lang.String localNotificationType ;
                                
                            private static java.util.HashMap _table_ = new java.util.HashMap();

                            // Constructor
                            
                                protected NotificationType(java.lang.String value, boolean isRegisterValue) {
                                    localNotificationType = value;
                                    if (isRegisterValue){
                                        
                                               _table_.put(localNotificationType, this);
                                           
                                    }

                                }
                            
                                    public static final java.lang.String _ntRouteStart =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntRouteStart");
                                
                                    public static final java.lang.String _ntOriginDepart =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntOriginDepart");
                                
                                    public static final java.lang.String _ntDestinationArrive =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntDestinationArrive");
                                
                                    public static final java.lang.String _ntRouteComplete =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntRouteComplete");
                                
                                    public static final java.lang.String _ntRouteChange =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntRouteChange");
                                
                                    public static final java.lang.String _ntStopSequence =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntStopSequence");
                                
                                    public static final java.lang.String _ntStopArrive =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntStopArrive");
                                
                                    public static final java.lang.String _ntStopDepart =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntStopDepart");
                                
                                    public static final java.lang.String _ntStopCancel =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntStopCancel");
                                
                                    public static final java.lang.String _ntGPS =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntGPS");
                                
                                    public static final java.lang.String _ntDeliveryDetail =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntDeliveryDetail");
                                
                                    public static final java.lang.String _ntRoutePathDeviationException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntRoutePathDeviationException");
                                
                                    public static final java.lang.String _ntRPDExitPointException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntRPDExitPointException");
                                
                                    public static final java.lang.String _ntUnplannedStopException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntUnplannedStopException");
                                
                                    public static final java.lang.String _ntEarlyArrivalException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntEarlyArrivalException");
                                
                                    public static final java.lang.String _ntLateDepartureException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntLateDepartureException");
                                
                                    public static final java.lang.String _ntServiceTimeException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntServiceTimeException");
                                
                                    public static final java.lang.String _ntGpsGapException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntGpsGapException");
                                
                                    public static final java.lang.String _ntLateGpsException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntLateGpsException");
                                
                                    public static final java.lang.String _ntNoGpsException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntNoGpsException");
                                
                                    public static final java.lang.String _ntTimeOffPlannedException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntTimeOffPlannedException");
                                
                                    public static final java.lang.String _ntOutOfSequenceException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntOutOfSequenceException");
                                
                                    public static final java.lang.String _ntOSDMonitoringException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntOSDMonitoringException");
                                
                                    public static final java.lang.String _ntDriverCancelException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntDriverCancelException");
                                
                                    public static final java.lang.String _ntUndeliverableStopException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntUndeliverableStopException");
                                
                                    public static final java.lang.String _ntOutOfContactException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntOutOfContactException");
                                
                                    public static final java.lang.String _ntUserException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntUserException");
                                
                                    public static final java.lang.String _ntSpeedViolationException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntSpeedViolationException");
                                
                                    public static final java.lang.String _ntEventReceiptException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntEventReceiptException");
                                
                                    public static final java.lang.String _ntStartCompleteOutOfBoundsException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntStartCompleteOutOfBoundsException");
                                
                                    public static final java.lang.String _ntDutyPeriodViolationException =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntDutyPeriodViolationException");
                                
                                    public static final java.lang.String _ntOrdersSent =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntOrdersSent");
                                
                                    public static final java.lang.String _ntTextMessage =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntTextMessage");
                                
                                    public static final java.lang.String _ntSchedulerSuccessfulOptimize =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntSchedulerSuccessfulOptimize");
                                
                                    public static final java.lang.String _ntSchedulerUpdateOrderFailed =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntSchedulerUpdateOrderFailed");
                                
                                    public static final java.lang.String _ntSchedulerOrderCanceled =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ntSchedulerOrderCanceled");
                                
                                public static final NotificationType ntRouteStart =
                                    new NotificationType(_ntRouteStart,true);
                            
                                public static final NotificationType ntOriginDepart =
                                    new NotificationType(_ntOriginDepart,true);
                            
                                public static final NotificationType ntDestinationArrive =
                                    new NotificationType(_ntDestinationArrive,true);
                            
                                public static final NotificationType ntRouteComplete =
                                    new NotificationType(_ntRouteComplete,true);
                            
                                public static final NotificationType ntRouteChange =
                                    new NotificationType(_ntRouteChange,true);
                            
                                public static final NotificationType ntStopSequence =
                                    new NotificationType(_ntStopSequence,true);
                            
                                public static final NotificationType ntStopArrive =
                                    new NotificationType(_ntStopArrive,true);
                            
                                public static final NotificationType ntStopDepart =
                                    new NotificationType(_ntStopDepart,true);
                            
                                public static final NotificationType ntStopCancel =
                                    new NotificationType(_ntStopCancel,true);
                            
                                public static final NotificationType ntGPS =
                                    new NotificationType(_ntGPS,true);
                            
                                public static final NotificationType ntDeliveryDetail =
                                    new NotificationType(_ntDeliveryDetail,true);
                            
                                public static final NotificationType ntRoutePathDeviationException =
                                    new NotificationType(_ntRoutePathDeviationException,true);
                            
                                public static final NotificationType ntRPDExitPointException =
                                    new NotificationType(_ntRPDExitPointException,true);
                            
                                public static final NotificationType ntUnplannedStopException =
                                    new NotificationType(_ntUnplannedStopException,true);
                            
                                public static final NotificationType ntEarlyArrivalException =
                                    new NotificationType(_ntEarlyArrivalException,true);
                            
                                public static final NotificationType ntLateDepartureException =
                                    new NotificationType(_ntLateDepartureException,true);
                            
                                public static final NotificationType ntServiceTimeException =
                                    new NotificationType(_ntServiceTimeException,true);
                            
                                public static final NotificationType ntGpsGapException =
                                    new NotificationType(_ntGpsGapException,true);
                            
                                public static final NotificationType ntLateGpsException =
                                    new NotificationType(_ntLateGpsException,true);
                            
                                public static final NotificationType ntNoGpsException =
                                    new NotificationType(_ntNoGpsException,true);
                            
                                public static final NotificationType ntTimeOffPlannedException =
                                    new NotificationType(_ntTimeOffPlannedException,true);
                            
                                public static final NotificationType ntOutOfSequenceException =
                                    new NotificationType(_ntOutOfSequenceException,true);
                            
                                public static final NotificationType ntOSDMonitoringException =
                                    new NotificationType(_ntOSDMonitoringException,true);
                            
                                public static final NotificationType ntDriverCancelException =
                                    new NotificationType(_ntDriverCancelException,true);
                            
                                public static final NotificationType ntUndeliverableStopException =
                                    new NotificationType(_ntUndeliverableStopException,true);
                            
                                public static final NotificationType ntOutOfContactException =
                                    new NotificationType(_ntOutOfContactException,true);
                            
                                public static final NotificationType ntUserException =
                                    new NotificationType(_ntUserException,true);
                            
                                public static final NotificationType ntSpeedViolationException =
                                    new NotificationType(_ntSpeedViolationException,true);
                            
                                public static final NotificationType ntEventReceiptException =
                                    new NotificationType(_ntEventReceiptException,true);
                            
                                public static final NotificationType ntStartCompleteOutOfBoundsException =
                                    new NotificationType(_ntStartCompleteOutOfBoundsException,true);
                            
                                public static final NotificationType ntDutyPeriodViolationException =
                                    new NotificationType(_ntDutyPeriodViolationException,true);
                            
                                public static final NotificationType ntOrdersSent =
                                    new NotificationType(_ntOrdersSent,true);
                            
                                public static final NotificationType ntTextMessage =
                                    new NotificationType(_ntTextMessage,true);
                            
                                public static final NotificationType ntSchedulerSuccessfulOptimize =
                                    new NotificationType(_ntSchedulerSuccessfulOptimize,true);
                            
                                public static final NotificationType ntSchedulerUpdateOrderFailed =
                                    new NotificationType(_ntSchedulerUpdateOrderFailed,true);
                            
                                public static final NotificationType ntSchedulerOrderCanceled =
                                    new NotificationType(_ntSchedulerOrderCanceled,true);
                            

                                public java.lang.String getValue() { return localNotificationType;}

                                public boolean equals(java.lang.Object obj) {return (obj == this);}
                                public int hashCode() { return toString().hashCode();}
                                public java.lang.String toString() {
                                
                                        return localNotificationType.toString();
                                    

                                }

                        

     /**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
   public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
        boolean isReaderMTOMAware = false;
        
        try{
          isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }catch(java.lang.IllegalArgumentException e){
          isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
   }
     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
                org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME){

                 public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                       NotificationType.this.serialize(MY_QNAME,factory,xmlWriter);
                 }
               };
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               MY_QNAME,factory,dataSource);
            
       }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       final org.apache.axiom.om.OMFactory factory,
                                       org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,factory,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               final org.apache.axiom.om.OMFactory factory,
                               org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                
                //We can safely assume an element has only one type associated with it
                
                            java.lang.String namespace = parentQName.getNamespaceURI();
                            java.lang.String localName = parentQName.getLocalPart();
                        
                            if (! namespace.equals("")) {
                                java.lang.String prefix = xmlWriter.getPrefix(namespace);

                                if (prefix == null) {
                                    prefix = generatePrefix(namespace);

                                    xmlWriter.writeStartElement(prefix, localName, namespace);
                                    xmlWriter.writeNamespace(prefix, namespace);
                                    xmlWriter.setPrefix(prefix, namespace);

                                } else {
                                    xmlWriter.writeStartElement(namespace, localName);
                                }

                            } else {
                                xmlWriter.writeStartElement(localName);
                            }

                            // add the type details if this is used in a simple type
                               if (serializeType){
                                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService");
                                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           namespacePrefix+":NotificationType",
                                           xmlWriter);
                                   } else {
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           "NotificationType",
                                           xmlWriter);
                                   }
                               }
                            
                                          if (localNotificationType==null){
                                            
                                                     throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");
                                                
                                         }else{
                                        
                                                       xmlWriter.writeCharacters(localNotificationType);
                                            
                                         }
                                    
                            xmlWriter.writeEndElement();

                    

        }

         /**
          * Util method to write an attribute with the ns prefix
          */
          private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
              if (xmlWriter.getPrefix(namespace) == null) {
                       xmlWriter.writeNamespace(prefix, namespace);
                       xmlWriter.setPrefix(prefix, namespace);

              }

              xmlWriter.writeAttribute(namespace,attName,attValue);

         }

        /**
          * Util method to write an attribute without the ns prefix
          */
          private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
                if (namespace.equals(""))
              {
                  xmlWriter.writeAttribute(attName,attValue);
              }
              else
              {
                  registerPrefix(xmlWriter, namespace);
                  xmlWriter.writeAttribute(namespace,attName,attValue);
              }
          }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


         /**
         * Register a namespace prefix
         */
         private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
                java.lang.String prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                        prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                    }

                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }

                return prefix;
            }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                
                //We can safely assume an element has only one type associated with it
                 return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME,
                            new java.lang.Object[]{
                            org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                            org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNotificationType)
                            },
                            null);

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        
                public static NotificationType fromValue(java.lang.String value)
                      throws java.lang.IllegalArgumentException {
                    NotificationType enumeration = (NotificationType)
                       
                               _table_.get(value);
                           

                    if (enumeration==null) throw new java.lang.IllegalArgumentException();
                    return enumeration;
                }
                public static NotificationType fromString(java.lang.String value,java.lang.String namespaceURI)
                      throws java.lang.IllegalArgumentException {
                    try {
                       
                                       return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));
                                   

                    } catch (java.lang.Exception e) {
                        throw new java.lang.IllegalArgumentException();
                    }
                }

                public static NotificationType fromString(javax.xml.stream.XMLStreamReader xmlStreamReader,
                                                                    java.lang.String content) {
                    if (content.indexOf(":") > -1){
                        java.lang.String prefix = content.substring(0,content.indexOf(":"));
                        java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                        return NotificationType.Factory.fromString(content,namespaceUri);
                    } else {
                       return NotificationType.Factory.fromString(content,"");
                    }
                }
            

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static NotificationType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            NotificationType object = null;
                // initialize a hash map to keep values
                java.util.Map attributeMap = new java.util.HashMap();
                java.util.List extraAttributeList = new java.util.ArrayList();
            

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                    
                while(!reader.isEndElement()) {
                    if (reader.isStartElement()  || reader.hasText()){
                
                                    java.lang.String content = reader.getElementText();
                                    
                                        if (content.indexOf(":") > 0) {
                                            // this seems to be a Qname so find the namespace and send
                                            prefix = content.substring(0, content.indexOf(":"));
                                            namespaceuri = reader.getNamespaceURI(prefix);
                                            object = NotificationType.Factory.fromString(content,namespaceuri);
                                        } else {
                                            // this seems to be not a qname send and empty namespace incase of it is
                                            // check is done in fromString method
                                            object = NotificationType.Factory.fromString(content,"");
                                        }
                                        
                                        
                             } else {
                                reader.next();
                             }  
                           }  // end of while loop
                        



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
          