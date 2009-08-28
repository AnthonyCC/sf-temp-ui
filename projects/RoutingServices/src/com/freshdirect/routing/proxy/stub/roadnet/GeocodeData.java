
/**
 * GeocodeData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.roadnet;
            

            /**
            *  GeocodeData bean class
            */
        
        public  class GeocodeData
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = GeocodeData
                Namespace URI = http://www.upslogisticstech.com/UPSLT/RouteNetWebService
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.upslogisticstech.com/UPSLT/RouteNetWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for Matched
                        */

                        
                                    protected boolean localMatched =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getMatched(){
                               return localMatched;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Matched
                               */
                               public void setMatched(boolean param){
                            
                                            this.localMatched=param;
                                    

                               }
                            

                        /**
                        * field for Coordinate
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.roadnet.MapPoint localCoordinate ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.roadnet.MapPoint
                           */
                           public  com.freshdirect.routing.proxy.stub.roadnet.MapPoint getCoordinate(){
                               return localCoordinate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Coordinate
                               */
                               public void setCoordinate(com.freshdirect.routing.proxy.stub.roadnet.MapPoint param){
                            
                                            this.localCoordinate=param;
                                    

                               }
                            

                        /**
                        * field for Quality
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult localQuality ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult
                           */
                           public  com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult getQuality(){
                               return localQuality;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Quality
                               */
                               public void setQuality(com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult param){
                            
                                            this.localQuality=param;
                                    

                               }
                            

                        /**
                        * field for Confidence
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence localConfidence ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence
                           */
                           public  com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence getConfidence(){
                               return localConfidence;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Confidence
                               */
                               public void setConfidence(com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence param){
                            
                                            this.localConfidence=param;
                                    

                               }
                            

                        /**
                        * field for QualityDescription
                        */

                        
                                    protected java.lang.String localQualityDescription ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQualityDescriptionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getQualityDescription(){
                               return localQualityDescription;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param QualityDescription
                               */
                               public void setQualityDescription(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localQualityDescriptionTracker = true;
                                       } else {
                                          localQualityDescriptionTracker = false;
                                              
                                       }
                                   
                                            this.localQualityDescription=param;
                                    

                               }
                            

                        /**
                        * field for Candidates
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.roadnet.MapArc[] localCandidates ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCandidatesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.roadnet.MapArc[]
                           */
                           public  com.freshdirect.routing.proxy.stub.roadnet.MapArc[] getCandidates(){
                               return localCandidates;
                           }

                           
                        


                               
                              /**
                               * validate the array for Candidates
                               */
                              protected void validateCandidates(com.freshdirect.routing.proxy.stub.roadnet.MapArc[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Candidates
                              */
                              public void setCandidates(com.freshdirect.routing.proxy.stub.roadnet.MapArc[] param){
                              
                                   validateCandidates(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localCandidatesTracker = true;
                                          } else {
                                             localCandidatesTracker = false;
                                                 
                                          }
                                      
                                      this.localCandidates=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.roadnet.MapArc
                             */
                             public void addCandidates(com.freshdirect.routing.proxy.stub.roadnet.MapArc param){
                                   if (localCandidates == null){
                                   localCandidates = new com.freshdirect.routing.proxy.stub.roadnet.MapArc[]{};
                                   }

                            
                                 //update the setting tracker
                                localCandidatesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localCandidates);
                               list.add(param);
                               this.localCandidates =
                             (com.freshdirect.routing.proxy.stub.roadnet.MapArc[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.roadnet.MapArc[list.size()]);

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
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName){

                 public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                       GeocodeData.this.serialize(parentQName,factory,xmlWriter);
                 }
               };
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               parentQName,factory,dataSource);
            
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
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();

                    if ((namespace != null) && (namespace.trim().length() > 0)) {
                        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
                        if (writerPrefix != null) {
                            xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
                        } else {
                            if (prefix == null) {
                                prefix = generatePrefix(namespace);
                            }

                            xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                            xmlWriter.writeNamespace(prefix, namespace);
                            xmlWriter.setPrefix(prefix, namespace);
                        }
                    } else {
                        xmlWriter.writeStartElement(parentQName.getLocalPart());
                    }
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.upslogisticstech.com/UPSLT/RouteNetWebService");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":GeocodeData",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "GeocodeData",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"matched", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"matched");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("matched");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("matched cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMatched));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                            if (localCoordinate==null){
                                                 throw new org.apache.axis2.databinding.ADBException("coordinate cannot be null!!");
                                            }
                                           localCoordinate.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","coordinate"),
                                               factory,xmlWriter);
                                        
                                            if (localQuality==null){
                                                 throw new org.apache.axis2.databinding.ADBException("quality cannot be null!!");
                                            }
                                           localQuality.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","quality"),
                                               factory,xmlWriter);
                                        
                                            if (localConfidence==null){
                                                 throw new org.apache.axis2.databinding.ADBException("confidence cannot be null!!");
                                            }
                                           localConfidence.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","confidence"),
                                               factory,xmlWriter);
                                         if (localQualityDescriptionTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"qualityDescription", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"qualityDescription");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("qualityDescription");
                                    }
                                

                                          if (localQualityDescription==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("qualityDescription cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localQualityDescription);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localCandidatesTracker){
                                       if (localCandidates!=null){
                                            for (int i = 0;i < localCandidates.length;i++){
                                                if (localCandidates[i] != null){
                                                 localCandidates[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","candidates"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("candidates cannot be null!!");
                                        
                                    }
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


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "matched"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMatched));
                            
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "coordinate"));
                            
                            
                                    if (localCoordinate==null){
                                         throw new org.apache.axis2.databinding.ADBException("coordinate cannot be null!!");
                                    }
                                    elementList.add(localCoordinate);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "quality"));
                            
                            
                                    if (localQuality==null){
                                         throw new org.apache.axis2.databinding.ADBException("quality cannot be null!!");
                                    }
                                    elementList.add(localQuality);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "confidence"));
                            
                            
                                    if (localConfidence==null){
                                         throw new org.apache.axis2.databinding.ADBException("confidence cannot be null!!");
                                    }
                                    elementList.add(localConfidence);
                                 if (localQualityDescriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "qualityDescription"));
                                 
                                        if (localQualityDescription != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQualityDescription));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("qualityDescription cannot be null!!");
                                        }
                                    } if (localCandidatesTracker){
                             if (localCandidates!=null) {
                                 for (int i = 0;i < localCandidates.length;i++){

                                    if (localCandidates[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                          "candidates"));
                                         elementList.add(localCandidates[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("candidates cannot be null!!");
                                    
                             }

                        }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static GeocodeData parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            GeocodeData object =
                new GeocodeData();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"GeocodeData".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GeocodeData)com.freshdirect.routing.proxy.stub.roadnet.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list6 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","matched").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMatched(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","coordinate").equals(reader.getName())){
                                
                                                object.setCoordinate(com.freshdirect.routing.proxy.stub.roadnet.MapPoint.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","quality").equals(reader.getName())){
                                
                                                object.setQuality(com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","confidence").equals(reader.getName())){
                                
                                                object.setConfidence(com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","qualityDescription").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setQualityDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","candidates").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list6.add(com.freshdirect.routing.proxy.stub.roadnet.MapArc.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone6 = false;
                                                        while(!loopDone6){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone6 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","candidates").equals(reader.getName())){
                                                                    list6.add(com.freshdirect.routing.proxy.stub.roadnet.MapArc.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone6 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setCandidates((com.freshdirect.routing.proxy.stub.roadnet.MapArc[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.roadnet.MapArc.class,
                                                                list6));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                  
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
          