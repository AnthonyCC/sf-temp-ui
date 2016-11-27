
/**
 * TelematicsRoute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  TelematicsRoute bean class
            */
        
        public  class TelematicsRoute
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = TelematicsRoute
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
                        * field for RouteIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RouteIdentity localRouteIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RouteIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity(){
                               return localRouteIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteIdentity
                               */
                               public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRouteIdentityTracker = true;
                                       } else {
                                          localRouteIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localRouteIdentity=param;
                                    

                               }
                            

                        /**
                        * field for InternalRouteId
                        */

                        
                                    protected int localInternalRouteId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getInternalRouteId(){
                               return localInternalRouteId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InternalRouteId
                               */
                               public void setInternalRouteId(int param){
                            
                                            this.localInternalRouteId=param;
                                    

                               }
                            

                        /**
                        * field for Origin
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation localOrigin ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOriginTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation getOrigin(){
                               return localOrigin;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Origin
                               */
                               public void setOrigin(com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOriginTracker = true;
                                       } else {
                                          localOriginTracker = true;
                                              
                                       }
                                   
                                            this.localOrigin=param;
                                    

                               }
                            

                        /**
                        * field for Destination
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation localDestination ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDestinationTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation getDestination(){
                               return localDestination;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Destination
                               */
                               public void setDestination(com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDestinationTracker = true;
                                       } else {
                                          localDestinationTracker = true;
                                              
                                       }
                                   
                                            this.localDestination=param;
                                    

                               }
                            

                        /**
                        * field for PlannedStart
                        */

                        
                                    protected java.util.Calendar localPlannedStart ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedStart(){
                               return localPlannedStart;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedStart
                               */
                               public void setPlannedStart(java.util.Calendar param){
                            
                                            this.localPlannedStart=param;
                                    

                               }
                            

                        /**
                        * field for ActualStart
                        */

                        
                                    protected java.util.Calendar localActualStart ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualStartTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualStart(){
                               return localActualStart;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualStart
                               */
                               public void setActualStart(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualStartTracker = true;
                                       } else {
                                          localActualStartTracker = false;
                                              
                                       }
                                   
                                            this.localActualStart=param;
                                    

                               }
                            

                        /**
                        * field for PlannedComplete
                        */

                        
                                    protected java.util.Calendar localPlannedComplete ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedComplete(){
                               return localPlannedComplete;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedComplete
                               */
                               public void setPlannedComplete(java.util.Calendar param){
                            
                                            this.localPlannedComplete=param;
                                    

                               }
                            

                        /**
                        * field for ActualComplete
                        */

                        
                                    protected java.util.Calendar localActualComplete ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualCompleteTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualComplete(){
                               return localActualComplete;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualComplete
                               */
                               public void setActualComplete(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualCompleteTracker = true;
                                       } else {
                                          localActualCompleteTracker = false;
                                              
                                       }
                                   
                                            this.localActualComplete=param;
                                    

                               }
                            

                        /**
                        * field for PlannedOriginDeparture
                        */

                        
                                    protected java.util.Calendar localPlannedOriginDeparture ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedOriginDeparture(){
                               return localPlannedOriginDeparture;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedOriginDeparture
                               */
                               public void setPlannedOriginDeparture(java.util.Calendar param){
                            
                                            this.localPlannedOriginDeparture=param;
                                    

                               }
                            

                        /**
                        * field for ActualOriginDeparture
                        */

                        
                                    protected java.util.Calendar localActualOriginDeparture ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualOriginDepartureTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualOriginDeparture(){
                               return localActualOriginDeparture;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualOriginDeparture
                               */
                               public void setActualOriginDeparture(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualOriginDepartureTracker = true;
                                       } else {
                                          localActualOriginDepartureTracker = false;
                                              
                                       }
                                   
                                            this.localActualOriginDeparture=param;
                                    

                               }
                            

                        /**
                        * field for PlannedDestinationArrival
                        */

                        
                                    protected java.util.Calendar localPlannedDestinationArrival ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedDestinationArrival(){
                               return localPlannedDestinationArrival;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedDestinationArrival
                               */
                               public void setPlannedDestinationArrival(java.util.Calendar param){
                            
                                            this.localPlannedDestinationArrival=param;
                                    

                               }
                            

                        /**
                        * field for ActualDestinationArrival
                        */

                        
                                    protected java.util.Calendar localActualDestinationArrival ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualDestinationArrivalTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualDestinationArrival(){
                               return localActualDestinationArrival;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDestinationArrival
                               */
                               public void setActualDestinationArrival(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualDestinationArrivalTracker = true;
                                       } else {
                                          localActualDestinationArrivalTracker = false;
                                              
                                       }
                                   
                                            this.localActualDestinationArrival=param;
                                    

                               }
                            

                        /**
                        * field for LastMessageId
                        */

                        
                                    protected int localLastMessageId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLastMessageId(){
                               return localLastMessageId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LastMessageId
                               */
                               public void setLastMessageId(int param){
                            
                                            this.localLastMessageId=param;
                                    

                               }
                            

                        /**
                        * field for PendingEventCount
                        */

                        
                                    protected int localPendingEventCount ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPendingEventCount(){
                               return localPendingEventCount;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PendingEventCount
                               */
                               public void setPendingEventCount(int param){
                            
                                            this.localPendingEventCount=param;
                                    

                               }
                            

                        /**
                        * field for LastCachedPosition
                        */

                        
                                    protected java.util.Calendar localLastCachedPosition ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLastCachedPositionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLastCachedPosition(){
                               return localLastCachedPosition;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LastCachedPosition
                               */
                               public void setLastCachedPosition(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLastCachedPositionTracker = true;
                                       } else {
                                          localLastCachedPositionTracker = false;
                                              
                                       }
                                   
                                            this.localLastCachedPosition=param;
                                    

                               }
                            

                        /**
                        * field for RouteEquipment
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.TelematicsRouteEquipment localRouteEquipment ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteEquipmentTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.TelematicsRouteEquipment
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.TelematicsRouteEquipment getRouteEquipment(){
                               return localRouteEquipment;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteEquipment
                               */
                               public void setRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.TelematicsRouteEquipment param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRouteEquipmentTracker = true;
                                       } else {
                                          localRouteEquipmentTracker = true;
                                              
                                       }
                                   
                                            this.localRouteEquipment=param;
                                    

                               }
                            

                        /**
                        * field for Stops
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[] localStops ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStopsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[] getStops(){
                               return localStops;
                           }

                           
                        


                               
                              /**
                               * validate the array for Stops
                               */
                              protected void validateStops(com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Stops
                              */
                              public void setStops(com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[] param){
                              
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
                             * @param param com.freshdirect.routing.proxy.stub.transportation.TelematicsStop
                             */
                             public void addStops(com.freshdirect.routing.proxy.stub.transportation.TelematicsStop param){
                                   if (localStops == null){
                                   localStops = new com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[]{};
                                   }

                            
                                 //update the setting tracker
                                localStopsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localStops);
                               list.add(param);
                               this.localStops =
                             (com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[list.size()]);

                             }
                             

                        /**
                        * field for PositionHistory
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] localPositionHistory ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPositionHistoryTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PositionHistory[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] getPositionHistory(){
                               return localPositionHistory;
                           }

                           
                        


                               
                              /**
                               * validate the array for PositionHistory
                               */
                              protected void validatePositionHistory(com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param PositionHistory
                              */
                              public void setPositionHistory(com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] param){
                              
                                   validatePositionHistory(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localPositionHistoryTracker = true;
                                          } else {
                                             localPositionHistoryTracker = false;
                                                 
                                          }
                                      
                                      this.localPositionHistory=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.PositionHistory
                             */
                             public void addPositionHistory(com.freshdirect.routing.proxy.stub.transportation.PositionHistory param){
                                   if (localPositionHistory == null){
                                   localPositionHistory = new com.freshdirect.routing.proxy.stub.transportation.PositionHistory[]{};
                                   }

                            
                                 //update the setting tracker
                                localPositionHistoryTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localPositionHistory);
                               list.add(param);
                               this.localPositionHistory =
                             (com.freshdirect.routing.proxy.stub.transportation.PositionHistory[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.PositionHistory[list.size()]);

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
                       TelematicsRoute.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":TelematicsRoute",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "TelematicsRoute",
                           xmlWriter);
                   }

               
                   }
                if (localRouteIdentityTracker){
                                    if (localRouteIdentity==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"routeIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"routeIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("routeIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localRouteIdentity.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeIdentity"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"internalRouteId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"internalRouteId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("internalRouteId");
                                    }
                                
                                               if (localInternalRouteId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("internalRouteId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInternalRouteId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localOriginTracker){
                                    if (localOrigin==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"origin", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"origin");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("origin");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localOrigin.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","origin"),
                                        factory,xmlWriter);
                                    }
                                } if (localDestinationTracker){
                                    if (localDestination==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"destination", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"destination");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("destination");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localDestination.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","destination"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedStart", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedStart");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedStart");
                                    }
                                

                                          if (localPlannedStart==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedStart cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedStart));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localActualStartTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualStart", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualStart");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualStart");
                                    }
                                

                                          if (localActualStart==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualStart cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStart));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedComplete", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedComplete");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedComplete");
                                    }
                                

                                          if (localPlannedComplete==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedComplete cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedComplete));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localActualCompleteTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualComplete", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualComplete");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualComplete");
                                    }
                                

                                          if (localActualComplete==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualComplete cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualComplete));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedOriginDeparture", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedOriginDeparture");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedOriginDeparture");
                                    }
                                

                                          if (localPlannedOriginDeparture==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedOriginDeparture cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedOriginDeparture));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localActualOriginDepartureTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualOriginDeparture", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualOriginDeparture");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualOriginDeparture");
                                    }
                                

                                          if (localActualOriginDeparture==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualOriginDeparture cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualOriginDeparture));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedDestinationArrival", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedDestinationArrival");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedDestinationArrival");
                                    }
                                

                                          if (localPlannedDestinationArrival==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedDestinationArrival cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDestinationArrival));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localActualDestinationArrivalTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDestinationArrival", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDestinationArrival");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDestinationArrival");
                                    }
                                

                                          if (localActualDestinationArrival==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualDestinationArrival cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDestinationArrival));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"lastMessageId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"lastMessageId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("lastMessageId");
                                    }
                                
                                               if (localLastMessageId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("lastMessageId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastMessageId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"pendingEventCount", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"pendingEventCount");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("pendingEventCount");
                                    }
                                
                                               if (localPendingEventCount==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("pendingEventCount cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPendingEventCount));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localLastCachedPositionTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"lastCachedPosition", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"lastCachedPosition");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("lastCachedPosition");
                                    }
                                

                                          if (localLastCachedPosition==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("lastCachedPosition cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastCachedPosition));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRouteEquipmentTracker){
                                    if (localRouteEquipment==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"routeEquipment", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"routeEquipment");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("routeEquipment");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localRouteEquipment.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeEquipment"),
                                        factory,xmlWriter);
                                    }
                                } if (localStopsTracker){
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
                                 } if (localPositionHistoryTracker){
                                       if (localPositionHistory!=null){
                                            for (int i = 0;i < localPositionHistory.length;i++){
                                                if (localPositionHistory[i] != null){
                                                 localPositionHistory[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","positionHistory"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("positionHistory cannot be null!!");
                                        
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

                 if (localRouteIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "routeIdentity"));
                            
                            
                                    elementList.add(localRouteIdentity==null?null:
                                    localRouteIdentity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "internalRouteId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInternalRouteId));
                             if (localOriginTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "origin"));
                            
                            
                                    elementList.add(localOrigin==null?null:
                                    localOrigin);
                                } if (localDestinationTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "destination"));
                            
                            
                                    elementList.add(localDestination==null?null:
                                    localDestination);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "plannedStart"));
                                 
                                        if (localPlannedStart != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedStart));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedStart cannot be null!!");
                                        }
                                     if (localActualStartTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "actualStart"));
                                 
                                        if (localActualStart != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStart));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualStart cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "plannedComplete"));
                                 
                                        if (localPlannedComplete != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedComplete));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedComplete cannot be null!!");
                                        }
                                     if (localActualCompleteTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "actualComplete"));
                                 
                                        if (localActualComplete != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualComplete));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualComplete cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "plannedOriginDeparture"));
                                 
                                        if (localPlannedOriginDeparture != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedOriginDeparture));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedOriginDeparture cannot be null!!");
                                        }
                                     if (localActualOriginDepartureTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "actualOriginDeparture"));
                                 
                                        if (localActualOriginDeparture != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualOriginDeparture));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualOriginDeparture cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "plannedDestinationArrival"));
                                 
                                        if (localPlannedDestinationArrival != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDestinationArrival));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedDestinationArrival cannot be null!!");
                                        }
                                     if (localActualDestinationArrivalTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "actualDestinationArrival"));
                                 
                                        if (localActualDestinationArrival != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDestinationArrival));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualDestinationArrival cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "lastMessageId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastMessageId));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "pendingEventCount"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPendingEventCount));
                             if (localLastCachedPositionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "lastCachedPosition"));
                                 
                                        if (localLastCachedPosition != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastCachedPosition));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("lastCachedPosition cannot be null!!");
                                        }
                                    } if (localRouteEquipmentTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "routeEquipment"));
                            
                            
                                    elementList.add(localRouteEquipment==null?null:
                                    localRouteEquipment);
                                } if (localStopsTracker){
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

                        } if (localPositionHistoryTracker){
                             if (localPositionHistory!=null) {
                                 for (int i = 0;i < localPositionHistory.length;i++){

                                    if (localPositionHistory[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "positionHistory"));
                                         elementList.add(localPositionHistory[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("positionHistory cannot be null!!");
                                    
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
        public static TelematicsRoute parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            TelematicsRoute object =
                new TelematicsRoute();

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
                    
                            if (!"TelematicsRoute".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (TelematicsRoute)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list17 = new java.util.ArrayList();
                    
                        java.util.ArrayList list18 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRouteIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","internalRouteId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInternalRouteId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","origin").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setOrigin(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setOrigin(com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","destination").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setDestination(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setDestination(com.freshdirect.routing.proxy.stub.transportation.TelematicsLocation.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","plannedStart").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedStart(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","actualStart").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualStart(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","plannedComplete").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedComplete(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","actualComplete").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualComplete(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","plannedOriginDeparture").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedOriginDeparture(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","actualOriginDeparture").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualOriginDeparture(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","plannedDestinationArrival").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedDestinationArrival(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","actualDestinationArrival").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDestinationArrival(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","lastMessageId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLastMessageId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","pendingEventCount").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPendingEventCount(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","lastCachedPosition").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLastCachedPosition(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeEquipment").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRouteEquipment(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.TelematicsRouteEquipment.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list17.add(com.freshdirect.routing.proxy.stub.transportation.TelematicsStop.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone17 = false;
                                                        while(!loopDone17){
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
                                                                loopDone17 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                                                    list17.add(com.freshdirect.routing.proxy.stub.transportation.TelematicsStop.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone17 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setStops((com.freshdirect.routing.proxy.stub.transportation.TelematicsStop[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.TelematicsStop.class,
                                                                list17));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","positionHistory").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list18.add(com.freshdirect.routing.proxy.stub.transportation.PositionHistory.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone18 = false;
                                                        while(!loopDone18){
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
                                                                loopDone18 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","positionHistory").equals(reader.getName())){
                                                                    list18.add(com.freshdirect.routing.proxy.stub.transportation.PositionHistory.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone18 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setPositionHistory((com.freshdirect.routing.proxy.stub.transportation.PositionHistory[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.PositionHistory.class,
                                                                list18));
                                                            
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
           
          