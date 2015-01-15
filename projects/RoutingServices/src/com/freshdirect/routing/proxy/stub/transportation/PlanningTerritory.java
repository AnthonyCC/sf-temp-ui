
/**
 * PlanningTerritory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  PlanningTerritory bean class
            */
        
        public  class PlanningTerritory
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = PlanningTerritory
                Namespace URI = http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for TerritoryID
                        */

                        
                                    protected java.lang.String localTerritoryID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getTerritoryID(){
                               return localTerritoryID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TerritoryID
                               */
                               public void setTerritoryID(java.lang.String param){
                            
                                            this.localTerritoryID=param;
                                    

                               }
                            

                        /**
                        * field for TerritoryNumber
                        */

                        
                                    protected int localTerritoryNumber ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getTerritoryNumber(){
                               return localTerritoryNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TerritoryNumber
                               */
                               public void setTerritoryNumber(int param){
                            
                                            this.localTerritoryNumber=param;
                                    

                               }
                            

                        /**
                        * field for OriginID
                        */

                        
                                    protected java.lang.String localOriginID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOriginID(){
                               return localOriginID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OriginID
                               */
                               public void setOriginID(java.lang.String param){
                            
                                            this.localOriginID=param;
                                    

                               }
                            

                        /**
                        * field for OriginType
                        */

                        
                                    protected java.lang.String localOriginType ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOriginType(){
                               return localOriginType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OriginType
                               */
                               public void setOriginType(java.lang.String param){
                            
                                            this.localOriginType=param;
                                    

                               }
                            

                        /**
                        * field for CreationMethod
                        */

                        
                                    protected java.lang.String localCreationMethod ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getCreationMethod(){
                               return localCreationMethod;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CreationMethod
                               */
                               public void setCreationMethod(java.lang.String param){
                            
                                            this.localCreationMethod=param;
                                    

                               }
                            

                        /**
                        * field for Drivers
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] localDrivers ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriversTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] getDrivers(){
                               return localDrivers;
                           }

                           
                        


                               
                              /**
                               * validate the array for Drivers
                               */
                              protected void validateDrivers(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Drivers
                              */
                              public void setDrivers(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] param){
                              
                                   validateDrivers(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localDriversTracker = true;
                                          } else {
                                             localDriversTracker = false;
                                                 
                                          }
                                      
                                      this.localDrivers=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity
                             */
                             public void addDrivers(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity param){
                                   if (localDrivers == null){
                                   localDrivers = new com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[]{};
                                   }

                            
                                 //update the setting tracker
                                localDriversTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localDrivers);
                               list.add(param);
                               this.localDrivers =
                             (com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[list.size()]);

                             }
                             

                        /**
                        * field for TerritoryEquipment
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] localTerritoryEquipment ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTerritoryEquipmentTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] getTerritoryEquipment(){
                               return localTerritoryEquipment;
                           }

                           
                        


                               
                              /**
                               * validate the array for TerritoryEquipment
                               */
                              protected void validateTerritoryEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param TerritoryEquipment
                              */
                              public void setTerritoryEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] param){
                              
                                   validateTerritoryEquipment(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localTerritoryEquipmentTracker = true;
                                          } else {
                                             localTerritoryEquipmentTracker = false;
                                                 
                                          }
                                      
                                      this.localTerritoryEquipment=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity
                             */
                             public void addTerritoryEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity param){
                                   if (localTerritoryEquipment == null){
                                   localTerritoryEquipment = new com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[]{};
                                   }

                            
                                 //update the setting tracker
                                localTerritoryEquipmentTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localTerritoryEquipment);
                               list.add(param);
                               this.localTerritoryEquipment =
                             (com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[list.size()]);

                             }
                             

                        /**
                        * field for Routes
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[] localRoutes ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRoutesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[] getRoutes(){
                               return localRoutes;
                           }

                           
                        


                               
                              /**
                               * validate the array for Routes
                               */
                              protected void validateRoutes(com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Routes
                              */
                              public void setRoutes(com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[] param){
                              
                                   validateRoutes(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localRoutesTracker = true;
                                          } else {
                                             localRoutesTracker = false;
                                                 
                                          }
                                      
                                      this.localRoutes=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.PlanningRoute
                             */
                             public void addRoutes(com.freshdirect.routing.proxy.stub.transportation.PlanningRoute param){
                                   if (localRoutes == null){
                                   localRoutes = new com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[]{};
                                   }

                            
                                 //update the setting tracker
                                localRoutesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localRoutes);
                               list.add(param);
                               this.localRoutes =
                             (com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[list.size()]);

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
                       PlanningTerritory.this.serialize(parentQName,factory,xmlWriter);
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
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":PlanningTerritory",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "PlanningTerritory",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"territoryID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"territoryID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("territoryID");
                                    }
                                

                                          if (localTerritoryID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("territoryID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localTerritoryID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"territoryNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"territoryNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("territoryNumber");
                                    }
                                
                                               if (localTerritoryNumber==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("territoryNumber cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTerritoryNumber));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"originID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"originID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("originID");
                                    }
                                

                                          if (localOriginID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("originID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOriginID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"originType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"originType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("originType");
                                    }
                                

                                          if (localOriginType==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("originType cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOriginType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"creationMethod", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"creationMethod");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("creationMethod");
                                    }
                                

                                          if (localCreationMethod==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("creationMethod cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localCreationMethod);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localDriversTracker){
                                       if (localDrivers!=null){
                                            for (int i = 0;i < localDrivers.length;i++){
                                                if (localDrivers[i] != null){
                                                 localDrivers[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","drivers"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("drivers cannot be null!!");
                                        
                                    }
                                 } if (localTerritoryEquipmentTracker){
                                       if (localTerritoryEquipment!=null){
                                            for (int i = 0;i < localTerritoryEquipment.length;i++){
                                                if (localTerritoryEquipment[i] != null){
                                                 localTerritoryEquipment[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryEquipment"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("territoryEquipment cannot be null!!");
                                        
                                    }
                                 } if (localRoutesTracker){
                                       if (localRoutes!=null){
                                            for (int i = 0;i < localRoutes.length;i++){
                                                if (localRoutes[i] != null){
                                                 localRoutes[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routes"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("routes cannot be null!!");
                                        
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

                
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "territoryID"));
                                 
                                        if (localTerritoryID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTerritoryID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("territoryID cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "territoryNumber"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTerritoryNumber));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "originID"));
                                 
                                        if (localOriginID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("originID cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "originType"));
                                 
                                        if (localOriginType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("originType cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "creationMethod"));
                                 
                                        if (localCreationMethod != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreationMethod));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("creationMethod cannot be null!!");
                                        }
                                     if (localDriversTracker){
                             if (localDrivers!=null) {
                                 for (int i = 0;i < localDrivers.length;i++){

                                    if (localDrivers[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "drivers"));
                                         elementList.add(localDrivers[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("drivers cannot be null!!");
                                    
                             }

                        } if (localTerritoryEquipmentTracker){
                             if (localTerritoryEquipment!=null) {
                                 for (int i = 0;i < localTerritoryEquipment.length;i++){

                                    if (localTerritoryEquipment[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "territoryEquipment"));
                                         elementList.add(localTerritoryEquipment[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("territoryEquipment cannot be null!!");
                                    
                             }

                        } if (localRoutesTracker){
                             if (localRoutes!=null) {
                                 for (int i = 0;i < localRoutes.length;i++){

                                    if (localRoutes[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "routes"));
                                         elementList.add(localRoutes[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("routes cannot be null!!");
                                    
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
        public static PlanningTerritory parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            PlanningTerritory object =
                new PlanningTerritory();

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
                    
                            if (!"PlanningTerritory".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (PlanningTerritory)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list6 = new java.util.ArrayList();
                    
                        java.util.ArrayList list7 = new java.util.ArrayList();
                    
                        java.util.ArrayList list8 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTerritoryID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTerritoryNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","originID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","originType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","creationMethod").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCreationMethod(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","drivers").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list6.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                
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
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","drivers").equals(reader.getName())){
                                                                    list6.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone6 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setDrivers((com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.class,
                                                                list6));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryEquipment").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list7.add(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone7 = false;
                                                        while(!loopDone7){
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
                                                                loopDone7 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryEquipment").equals(reader.getName())){
                                                                    list7.add(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone7 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setTerritoryEquipment((com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.class,
                                                                list7));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routes").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list8.add(com.freshdirect.routing.proxy.stub.transportation.PlanningRoute.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone8 = false;
                                                        while(!loopDone8){
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
                                                                loopDone8 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routes").equals(reader.getName())){
                                                                    list8.add(com.freshdirect.routing.proxy.stub.transportation.PlanningRoute.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone8 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setRoutes((com.freshdirect.routing.proxy.stub.transportation.PlanningRoute[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.PlanningRoute.class,
                                                                list8));
                                                            
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
           
          