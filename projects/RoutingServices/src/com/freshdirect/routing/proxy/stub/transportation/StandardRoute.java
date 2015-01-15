
/**
 * StandardRoute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  StandardRoute bean class
            */
        
        public  class StandardRoute
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = StandardRoute
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

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity localRouteIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity getRouteIdentity(){
                               return localRouteIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteIdentity
                               */
                               public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRouteIdentityTracker = true;
                                       } else {
                                          localRouteIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localRouteIdentity=param;
                                    

                               }
                            

                        /**
                        * field for Description
                        */

                        
                                    protected java.lang.String localDescription ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDescriptionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDescription(){
                               return localDescription;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Description
                               */
                               public void setDescription(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDescriptionTracker = true;
                                       } else {
                                          localDescriptionTracker = false;
                                              
                                       }
                                   
                                            this.localDescription=param;
                                    

                               }
                            

                        /**
                        * field for Origin
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.LocationIdentity localOrigin ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOriginTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.LocationIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getOrigin(){
                               return localOrigin;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Origin
                               */
                               public void setOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity param){
                            
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

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.LocationIdentity localDestination ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDestinationTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.LocationIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getDestination(){
                               return localDestination;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Destination
                               */
                               public void setDestination(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDestinationTracker = true;
                                       } else {
                                          localDestinationTracker = true;
                                              
                                       }
                                   
                                            this.localDestination=param;
                                    

                               }
                            

                        /**
                        * field for Helper
                        */

                        
                                    protected boolean localHelper ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getHelper(){
                               return localHelper;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Helper
                               */
                               public void setHelper(boolean param){
                            
                                            this.localHelper=param;
                                    

                               }
                            

                        /**
                        * field for Color
                        */

                        
                                    protected int localColor ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getColor(){
                               return localColor;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Color
                               */
                               public void setColor(int param){
                            
                                            this.localColor=param;
                                    

                               }
                            

                        /**
                        * field for EquipmentTypeID
                        */

                        
                                    protected java.lang.String localEquipmentTypeID ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getEquipmentTypeID(){
                               return localEquipmentTypeID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EquipmentTypeID
                               */
                               public void setEquipmentTypeID(java.lang.String param){
                            
                                            this.localEquipmentTypeID=param;
                                    

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
                        * field for RouteEquipment
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[] localRouteEquipment ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteEquipmentTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[] getRouteEquipment(){
                               return localRouteEquipment;
                           }

                           
                        


                               
                              /**
                               * validate the array for RouteEquipment
                               */
                              protected void validateRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param RouteEquipment
                              */
                              public void setRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[] param){
                              
                                   validateRouteEquipment(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localRouteEquipmentTracker = true;
                                          } else {
                                             localRouteEquipmentTracker = false;
                                                 
                                          }
                                      
                                      this.localRouteEquipment=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity
                             */
                             public void addRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity param){
                                   if (localRouteEquipment == null){
                                   localRouteEquipment = new com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[]{};
                                   }

                            
                                 //update the setting tracker
                                localRouteEquipmentTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localRouteEquipment);
                               list.add(param);
                               this.localRouteEquipment =
                             (com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[list.size()]);

                             }
                             

                        /**
                        * field for Days
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[] localDays ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDaysTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[] getDays(){
                               return localDays;
                           }

                           
                        


                               
                              /**
                               * validate the array for Days
                               */
                              protected void validateDays(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Days
                              */
                              public void setDays(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[] param){
                              
                                   validateDays(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localDaysTracker = true;
                                          } else {
                                             localDaysTracker = false;
                                                 
                                          }
                                      
                                      this.localDays=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.DayOfWeek
                             */
                             public void addDays(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek param){
                                   if (localDays == null){
                                   localDays = new com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[]{};
                                   }

                            
                                 //update the setting tracker
                                localDaysTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localDays);
                               list.add(param);
                               this.localDays =
                             (com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[list.size()]);

                             }
                             

                        /**
                        * field for TeamSplit
                        */

                        
                                    protected boolean localTeamSplit ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getTeamSplit(){
                               return localTeamSplit;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TeamSplit
                               */
                               public void setTeamSplit(boolean param){
                            
                                            this.localTeamSplit=param;
                                    

                               }
                            

                        /**
                        * field for ModelID
                        */

                        
                                    protected java.lang.String localModelID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localModelIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getModelID(){
                               return localModelID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ModelID
                               */
                               public void setModelID(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localModelIDTracker = true;
                                       } else {
                                          localModelIDTracker = false;
                                              
                                       }
                                   
                                            this.localModelID=param;
                                    

                               }
                            

                        /**
                        * field for LoadPriority
                        */

                        
                                    protected int localLoadPriority ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLoadPriority(){
                               return localLoadPriority;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoadPriority
                               */
                               public void setLoadPriority(int param){
                            
                                            this.localLoadPriority=param;
                                    

                               }
                            

                        /**
                        * field for StartTime
                        */

                        
                                    protected java.util.Calendar localStartTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStartTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getStartTime(){
                               return localStartTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StartTime
                               */
                               public void setStartTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localStartTimeTracker = true;
                                       } else {
                                          localStartTimeTracker = false;
                                              
                                       }
                                   
                                            this.localStartTime=param;
                                    

                               }
                            

                        /**
                        * field for MaximumTime
                        */

                        
                                    protected int localMaximumTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getMaximumTime(){
                               return localMaximumTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaximumTime
                               */
                               public void setMaximumTime(int param){
                            
                                            this.localMaximumTime=param;
                                    

                               }
                            

                        /**
                        * field for MaximumTravelTime
                        */

                        
                                    protected int localMaximumTravelTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getMaximumTravelTime(){
                               return localMaximumTravelTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaximumTravelTime
                               */
                               public void setMaximumTravelTime(int param){
                            
                                            this.localMaximumTravelTime=param;
                                    

                               }
                            

                        /**
                        * field for PreferredTime
                        */

                        
                                    protected int localPreferredTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPreferredTime(){
                               return localPreferredTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreferredTime
                               */
                               public void setPreferredTime(int param){
                            
                                            this.localPreferredTime=param;
                                    

                               }
                            

                        /**
                        * field for RegularTime
                        */

                        
                                    protected int localRegularTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getRegularTime(){
                               return localRegularTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RegularTime
                               */
                               public void setRegularTime(int param){
                            
                                            this.localRegularTime=param;
                                    

                               }
                            

                        /**
                        * field for LastStopIsDestination
                        */

                        
                                    protected boolean localLastStopIsDestination ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getLastStopIsDestination(){
                               return localLastStopIsDestination;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LastStopIsDestination
                               */
                               public void setLastStopIsDestination(boolean param){
                            
                                            this.localLastStopIsDestination=param;
                                    

                               }
                            

                        /**
                        * field for OriginIsDestination
                        */

                        
                                    protected boolean localOriginIsDestination ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getOriginIsDestination(){
                               return localOriginIsDestination;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OriginIsDestination
                               */
                               public void setOriginIsDestination(boolean param){
                            
                                            this.localOriginIsDestination=param;
                                    

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
                        * field for StartingLoadType
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType localStartingLoadType ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType getStartingLoadType(){
                               return localStartingLoadType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StartingLoadType
                               */
                               public void setStartingLoadType(com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType param){
                            
                                            this.localStartingLoadType=param;
                                    

                               }
                            

                        /**
                        * field for DispatcherUserIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.UserIdentity localDispatcherUserIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDispatcherUserIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.UserIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.UserIdentity getDispatcherUserIdentity(){
                               return localDispatcherUserIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DispatcherUserIdentity
                               */
                               public void setDispatcherUserIdentity(com.freshdirect.routing.proxy.stub.transportation.UserIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDispatcherUserIdentityTracker = true;
                                       } else {
                                          localDispatcherUserIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localDispatcherUserIdentity=param;
                                    

                               }
                            

                        /**
                        * field for Stops
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.StandardStop[] localStops ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStopsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.StandardStop[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.StandardStop[] getStops(){
                               return localStops;
                           }

                           
                        


                               
                              /**
                               * validate the array for Stops
                               */
                              protected void validateStops(com.freshdirect.routing.proxy.stub.transportation.StandardStop[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Stops
                              */
                              public void setStops(com.freshdirect.routing.proxy.stub.transportation.StandardStop[] param){
                              
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
                             * @param param com.freshdirect.routing.proxy.stub.transportation.StandardStop
                             */
                             public void addStops(com.freshdirect.routing.proxy.stub.transportation.StandardStop param){
                                   if (localStops == null){
                                   localStops = new com.freshdirect.routing.proxy.stub.transportation.StandardStop[]{};
                                   }

                            
                                 //update the setting tracker
                                localStopsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localStops);
                               list.add(param);
                               this.localStops =
                             (com.freshdirect.routing.proxy.stub.transportation.StandardStop[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.StandardStop[list.size()]);

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
                       StandardRoute.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":StandardRoute",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "StandardRoute",
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
                                } if (localDescriptionTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"description", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"description");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("description");
                                    }
                                

                                          if (localDescription==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("description cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDescription);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOriginTracker){
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

                                            xmlWriter.writeStartElement(prefix,"helper", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"helper");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("helper");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("helper cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHelper));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"color", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"color");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("color");
                                    }
                                
                                               if (localColor==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("color cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localColor));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"equipmentTypeID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"equipmentTypeID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("equipmentTypeID");
                                    }
                                

                                          if (localEquipmentTypeID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("equipmentTypeID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localEquipmentTypeID);
                                            
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
                                 } if (localRouteEquipmentTracker){
                                       if (localRouteEquipment!=null){
                                            for (int i = 0;i < localRouteEquipment.length;i++){
                                                if (localRouteEquipment[i] != null){
                                                 localRouteEquipment[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeEquipment"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("routeEquipment cannot be null!!");
                                        
                                    }
                                 } if (localDaysTracker){
                                       if (localDays!=null){
                                            for (int i = 0;i < localDays.length;i++){
                                                if (localDays[i] != null){
                                                 localDays[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","days"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("days cannot be null!!");
                                        
                                    }
                                 }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"teamSplit", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"teamSplit");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("teamSplit");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("teamSplit cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTeamSplit));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localModelIDTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"modelID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"modelID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("modelID");
                                    }
                                

                                          if (localModelID==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("modelID cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localModelID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"loadPriority", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"loadPriority");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("loadPriority");
                                    }
                                
                                               if (localLoadPriority==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("loadPriority cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoadPriority));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localStartTimeTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"startTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"startTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("startTime");
                                    }
                                

                                          if (localStartTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("startTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStartTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"maximumTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"maximumTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("maximumTime");
                                    }
                                
                                               if (localMaximumTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("maximumTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaximumTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"maximumTravelTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"maximumTravelTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("maximumTravelTime");
                                    }
                                
                                               if (localMaximumTravelTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("maximumTravelTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaximumTravelTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"preferredTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"preferredTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("preferredTime");
                                    }
                                
                                               if (localPreferredTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("preferredTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreferredTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"regularTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"regularTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("regularTime");
                                    }
                                
                                               if (localRegularTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("regularTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegularTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"lastStopIsDestination", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"lastStopIsDestination");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("lastStopIsDestination");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("lastStopIsDestination cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastStopIsDestination));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"originIsDestination", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"originIsDestination");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("originIsDestination");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("originIsDestination cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginIsDestination));
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
                             
                                            if (localStartingLoadType==null){
                                                 throw new org.apache.axis2.databinding.ADBException("startingLoadType cannot be null!!");
                                            }
                                           localStartingLoadType.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","startingLoadType"),
                                               factory,xmlWriter);
                                         if (localDispatcherUserIdentityTracker){
                                    if (localDispatcherUserIdentity==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"dispatcherUserIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"dispatcherUserIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("dispatcherUserIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localDispatcherUserIdentity.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","dispatcherUserIdentity"),
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
                                } if (localDescriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "description"));
                                 
                                        if (localDescription != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDescription));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("description cannot be null!!");
                                        }
                                    } if (localOriginTracker){
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
                                                                      "helper"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHelper));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "color"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localColor));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "equipmentTypeID"));
                                 
                                        if (localEquipmentTypeID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEquipmentTypeID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("equipmentTypeID cannot be null!!");
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

                        } if (localRouteEquipmentTracker){
                             if (localRouteEquipment!=null) {
                                 for (int i = 0;i < localRouteEquipment.length;i++){

                                    if (localRouteEquipment[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "routeEquipment"));
                                         elementList.add(localRouteEquipment[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("routeEquipment cannot be null!!");
                                    
                             }

                        } if (localDaysTracker){
                             if (localDays!=null) {
                                 for (int i = 0;i < localDays.length;i++){

                                    if (localDays[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "days"));
                                         elementList.add(localDays[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("days cannot be null!!");
                                    
                             }

                        }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "teamSplit"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTeamSplit));
                             if (localModelIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "modelID"));
                                 
                                        if (localModelID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localModelID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("modelID cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "loadPriority"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoadPriority));
                             if (localStartTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "startTime"));
                                 
                                        if (localStartTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStartTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("startTime cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "maximumTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaximumTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "maximumTravelTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaximumTravelTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "preferredTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreferredTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "regularTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegularTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "lastStopIsDestination"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastStopIsDestination));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "originIsDestination"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginIsDestination));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "preRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreRouteTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "postRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostRouteTime));
                            
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "startingLoadType"));
                            
                            
                                    if (localStartingLoadType==null){
                                         throw new org.apache.axis2.databinding.ADBException("startingLoadType cannot be null!!");
                                    }
                                    elementList.add(localStartingLoadType);
                                 if (localDispatcherUserIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "dispatcherUserIdentity"));
                            
                            
                                    elementList.add(localDispatcherUserIdentity==null?null:
                                    localDispatcherUserIdentity);
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
        public static StandardRoute parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            StandardRoute object =
                new StandardRoute();

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
                    
                            if (!"StandardRoute".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (StandardRoute)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list8 = new java.util.ArrayList();
                    
                        java.util.ArrayList list9 = new java.util.ArrayList();
                    
                        java.util.ArrayList list10 = new java.util.ArrayList();
                    
                        java.util.ArrayList list25 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRouteIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","description").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","origin").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setOrigin(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity.Factory.parse(reader));
                                              
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
                                    
                                                object.setDestination(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","helper").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setHelper(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","color").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setColor(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","equipmentTypeID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEquipmentTypeID(
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
                                    list8.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                
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
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","drivers").equals(reader.getName())){
                                                                    list8.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone8 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setDrivers((com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.class,
                                                                list8));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeEquipment").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list9.add(com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone9 = false;
                                                        while(!loopDone9){
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
                                                                loopDone9 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeEquipment").equals(reader.getName())){
                                                                    list9.add(com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone9 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setRouteEquipment((com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.StandardRouteEquipmentIdentity.class,
                                                                list9));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","days").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list10.add(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone10 = false;
                                                        while(!loopDone10){
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
                                                                loopDone10 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","days").equals(reader.getName())){
                                                                    list10.add(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone10 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setDays((com.freshdirect.routing.proxy.stub.transportation.DayOfWeek[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.DayOfWeek.class,
                                                                list10));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","teamSplit").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTeamSplit(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","modelID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setModelID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","loadPriority").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoadPriority(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","startTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStartTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","maximumTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMaximumTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","maximumTravelTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMaximumTravelTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","preferredTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPreferredTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","regularTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegularTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","lastStopIsDestination").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLastStopIsDestination(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","originIsDestination").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginIsDestination(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","startingLoadType").equals(reader.getName())){
                                
                                                object.setStartingLoadType(com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","dispatcherUserIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setDispatcherUserIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setDispatcherUserIdentity(com.freshdirect.routing.proxy.stub.transportation.UserIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list25.add(com.freshdirect.routing.proxy.stub.transportation.StandardStop.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone25 = false;
                                                        while(!loopDone25){
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
                                                                loopDone25 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                                                    list25.add(com.freshdirect.routing.proxy.stub.transportation.StandardStop.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone25 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setStops((com.freshdirect.routing.proxy.stub.transportation.StandardStop[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.StandardStop.class,
                                                                list25));
                                                            
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
           
          