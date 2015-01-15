
/**
 * PlanningRoute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  PlanningRoute bean class
            */
        
        public  class PlanningRoute
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = PlanningRoute
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
                        * field for WeekNumber
                        */

                        
                                    protected int localWeekNumber ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getWeekNumber(){
                               return localWeekNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WeekNumber
                               */
                               public void setWeekNumber(int param){
                            
                                            this.localWeekNumber=param;
                                    

                               }
                            

                        /**
                        * field for Day
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.DayOfWeek localDay ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.DayOfWeek
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.DayOfWeek getDay(){
                               return localDay;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Day
                               */
                               public void setDay(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek param){
                            
                                            this.localDay=param;
                                    

                               }
                            

                        /**
                        * field for RouteID
                        */

                        
                                    protected java.lang.String localRouteID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRouteID(){
                               return localRouteID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteID
                               */
                               public void setRouteID(java.lang.String param){
                            
                                            this.localRouteID=param;
                                    

                               }
                            

                        /**
                        * field for Helper
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.HelperType localHelper ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.HelperType
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.HelperType getHelper(){
                               return localHelper;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Helper
                               */
                               public void setHelper(com.freshdirect.routing.proxy.stub.transportation.HelperType param){
                            
                                            this.localHelper=param;
                                    

                               }
                            

                        /**
                        * field for OutStemDistance
                        */

                        
                                    protected int localOutStemDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOutStemDistance(){
                               return localOutStemDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OutStemDistance
                               */
                               public void setOutStemDistance(int param){
                            
                                            this.localOutStemDistance=param;
                                    

                               }
                            

                        /**
                        * field for InStemDistance
                        */

                        
                                    protected int localInStemDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getInStemDistance(){
                               return localInStemDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InStemDistance
                               */
                               public void setInStemDistance(int param){
                            
                                            this.localInStemDistance=param;
                                    

                               }
                            

                        /**
                        * field for OutStemTime
                        */

                        
                                    protected int localOutStemTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOutStemTime(){
                               return localOutStemTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OutStemTime
                               */
                               public void setOutStemTime(int param){
                            
                                            this.localOutStemTime=param;
                                    

                               }
                            

                        /**
                        * field for InStemTime
                        */

                        
                                    protected int localInStemTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getInStemTime(){
                               return localInStemTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InStemTime
                               */
                               public void setInStemTime(int param){
                            
                                            this.localInStemTime=param;
                                    

                               }
                            

                        /**
                        * field for PreRouteTime
                        */

                        
                                    protected int localPreRouteTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPreRouteTime(){
                               return localPreRouteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreRouteTime
                               */
                               public void setPreRouteTime(int param){
                            
                                            this.localPreRouteTime=param;
                                    

                               }
                            

                        /**
                        * field for PostRouteTime
                        */

                        
                                    protected int localPostRouteTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPostRouteTime(){
                               return localPostRouteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PostRouteTime
                               */
                               public void setPostRouteTime(int param){
                            
                                            this.localPostRouteTime=param;
                                    

                               }
                            

                        /**
                        * field for Stops
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PlanningStop[] localStops ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStopsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PlanningStop[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PlanningStop[] getStops(){
                               return localStops;
                           }

                           
                        


                               
                              /**
                               * validate the array for Stops
                               */
                              protected void validateStops(com.freshdirect.routing.proxy.stub.transportation.PlanningStop[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Stops
                              */
                              public void setStops(com.freshdirect.routing.proxy.stub.transportation.PlanningStop[] param){
                              
                                   validateStops(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localStopsTracker = true;
                                          } else {
                                             localStopsTracker = false;
                                                 
                                          }
                                      
                                      this.localStops=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.PlanningStop
                             */
                             public void addStops(com.freshdirect.routing.proxy.stub.transportation.PlanningStop param){
                                   if (localStops == null){
                                   localStops = new com.freshdirect.routing.proxy.stub.transportation.PlanningStop[]{};
                                   }

                            
                                 //update the setting tracker
                                localStopsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localStops);
                               list.add(param);
                               this.localStops =
                             (com.freshdirect.routing.proxy.stub.transportation.PlanningStop[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.PlanningStop[list.size()]);

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
                       PlanningRoute.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":PlanningRoute",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "PlanningRoute",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"weekNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"weekNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("weekNumber");
                                    }
                                
                                               if (localWeekNumber==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("weekNumber cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeekNumber));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                            if (localDay==null){
                                                 throw new org.apache.axis2.databinding.ADBException("day cannot be null!!");
                                            }
                                           localDay.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","day"),
                                               factory,xmlWriter);
                                        
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"routeID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"routeID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("routeID");
                                    }
                                

                                          if (localRouteID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("routeID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRouteID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                            if (localHelper==null){
                                                 throw new org.apache.axis2.databinding.ADBException("helper cannot be null!!");
                                            }
                                           localHelper.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","helper"),
                                               factory,xmlWriter);
                                        
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"outStemDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"outStemDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("outStemDistance");
                                    }
                                
                                               if (localOutStemDistance==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("outStemDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutStemDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"inStemDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"inStemDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("inStemDistance");
                                    }
                                
                                               if (localInStemDistance==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("inStemDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInStemDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"outStemTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"outStemTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("outStemTime");
                                    }
                                
                                               if (localOutStemTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("outStemTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutStemTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"inStemTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"inStemTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("inStemTime");
                                    }
                                
                                               if (localInStemTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("inStemTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInStemTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"preRouteTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"preRouteTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("preRouteTime");
                                    }
                                
                                               if (localPreRouteTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("preRouteTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreRouteTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"postRouteTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"postRouteTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("postRouteTime");
                                    }
                                
                                               if (localPostRouteTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("postRouteTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostRouteTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localStopsTracker){
                                       if (localStops!=null){
                                            for (int i = 0;i < localStops.length;i++){
                                                if (localStops[i] != null){
                                                 localStops[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("stops cannot be null!!");
                                        
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
                                                                      "weekNumber"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeekNumber));
                            
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "day"));
                            
                            
                                    if (localDay==null){
                                         throw new org.apache.axis2.databinding.ADBException("day cannot be null!!");
                                    }
                                    elementList.add(localDay);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "routeID"));
                                 
                                        if (localRouteID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("routeID cannot be null!!");
                                        }
                                    
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "helper"));
                            
                            
                                    if (localHelper==null){
                                         throw new org.apache.axis2.databinding.ADBException("helper cannot be null!!");
                                    }
                                    elementList.add(localHelper);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "outStemDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutStemDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "inStemDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInStemDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "outStemTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOutStemTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "inStemTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInStemTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "preRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreRouteTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "postRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostRouteTime));
                             if (localStopsTracker){
                             if (localStops!=null) {
                                 for (int i = 0;i < localStops.length;i++){

                                    if (localStops[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "stops"));
                                         elementList.add(localStops[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("stops cannot be null!!");
                                    
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
        public static PlanningRoute parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            PlanningRoute object =
                new PlanningRoute();

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
                    
                            if (!"PlanningRoute".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (PlanningRoute)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list11 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","weekNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWeekNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","day").equals(reader.getName())){
                                
                                                object.setDay(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","helper").equals(reader.getName())){
                                
                                                object.setHelper(com.freshdirect.routing.proxy.stub.transportation.HelperType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","outStemDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOutStemDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","inStemDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInStemDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","outStemTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOutStemTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","inStemTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInStemTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","preRouteTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPreRouteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","postRouteTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPostRouteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list11.add(com.freshdirect.routing.proxy.stub.transportation.PlanningStop.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone11 = false;
                                                        while(!loopDone11){
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
                                                                loopDone11 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                                                    list11.add(com.freshdirect.routing.proxy.stub.transportation.PlanningStop.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone11 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setStops((com.freshdirect.routing.proxy.stub.transportation.PlanningStop[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.PlanningStop.class,
                                                                list11));
                                                            
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
           
          