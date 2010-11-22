
/**
 * RoutingRoute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  RoutingRoute bean class
            */
        
        public  class RoutingRoute
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = RoutingRoute
                Namespace URI = http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for RouteIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity localRouteIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getRouteIdentity(){
                               return localRouteIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteIdentity
                               */
                               public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRouteIdentityTracker = true;
                                       } else {
                                          localRouteIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localRouteIdentity=param;
                                    

                               }
                            

                        /**
                        * field for RouteNumber
                        */

                        
                                    protected int localRouteNumber ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getRouteNumber(){
                               return localRouteNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteNumber
                               */
                               public void setRouteNumber(int param){
                            
                                            this.localRouteNumber=param;
                                    

                               }
                            

                        /**
                        * field for RouteID
                        */

                        
                                    protected java.lang.String localRouteID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteIDTracker = false ;
                           

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
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRouteIDTracker = true;
                                       } else {
                                          localRouteIDTracker = false;
                                              
                                       }
                                   
                                            this.localRouteID=param;
                                    

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
                        * field for CompleteTime
                        */

                        
                                    protected java.util.Calendar localCompleteTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCompleteTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getCompleteTime(){
                               return localCompleteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CompleteTime
                               */
                               public void setCompleteTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCompleteTimeTracker = true;
                                       } else {
                                          localCompleteTimeTracker = false;
                                              
                                       }
                                   
                                            this.localCompleteTime=param;
                                    

                               }
                            

                        /**
                        * field for Capacity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localCapacity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCapacityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getCapacity(){
                               return localCapacity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Capacity
                               */
                               public void setCapacity(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCapacityTracker = true;
                                       } else {
                                          localCapacityTracker = true;
                                              
                                       }
                                   
                                            this.localCapacity=param;
                                    

                               }
                            

                        /**
                        * field for MaxRunningCapacity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localMaxRunningCapacity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMaxRunningCapacityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getMaxRunningCapacity(){
                               return localMaxRunningCapacity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaxRunningCapacity
                               */
                               public void setMaxRunningCapacity(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localMaxRunningCapacityTracker = true;
                                       } else {
                                          localMaxRunningCapacityTracker = true;
                                              
                                       }
                                   
                                            this.localMaxRunningCapacity=param;
                                    

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
                        * field for Distance
                        */

                        
                                    protected double localDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getDistance(){
                               return localDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Distance
                               */
                               public void setDistance(double param){
                            
                                            this.localDistance=param;
                                    

                               }
                            

                        /**
                        * field for StartingQuantity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localStartingQuantity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStartingQuantityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getStartingQuantity(){
                               return localStartingQuantity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StartingQuantity
                               */
                               public void setStartingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localStartingQuantityTracker = true;
                                       } else {
                                          localStartingQuantityTracker = true;
                                              
                                       }
                                   
                                            this.localStartingQuantity=param;
                                    

                               }
                            

                        /**
                        * field for EndingQuantity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localEndingQuantity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEndingQuantityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getEndingQuantity(){
                               return localEndingQuantity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EndingQuantity
                               */
                               public void setEndingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEndingQuantityTracker = true;
                                       } else {
                                          localEndingQuantityTracker = true;
                                              
                                       }
                                   
                                            this.localEndingQuantity=param;
                                    

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

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] localRouteEquipment ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRouteEquipmentTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] getRouteEquipment(){
                               return localRouteEquipment;
                           }

                           
                        


                               
                              /**
                               * validate the array for RouteEquipment
                               */
                              protected void validateRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param RouteEquipment
                              */
                              public void setRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] param){
                              
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
                             * @param param com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity
                             */
                             public void addRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity param){
                                   if (localRouteEquipment == null){
                                   localRouteEquipment = new com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[]{};
                                   }

                            
                                 //update the setting tracker
                                localRouteEquipmentTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localRouteEquipment);
                               list.add(param);
                               this.localRouteEquipment =
                             (com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[list.size()]);

                             }
                             

                        /**
                        * field for Status
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus localStatus ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus getStatus(){
                               return localStatus;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Status
                               */
                               public void setStatus(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus param){
                            
                                            this.localStatus=param;
                                    

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
                        * field for TravelTime
                        */

                        
                                    protected int localTravelTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getTravelTime(){
                               return localTravelTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TravelTime
                               */
                               public void setTravelTime(int param){
                            
                                            this.localTravelTime=param;
                                    

                               }
                            

                        /**
                        * field for ServiceTime
                        */

                        
                                    protected int localServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getServiceTime(){
                               return localServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServiceTime
                               */
                               public void setServiceTime(int param){
                            
                                            this.localServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for TotalTime
                        */

                        
                                    protected int localTotalTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getTotalTime(){
                               return localTotalTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TotalTime
                               */
                               public void setTotalTime(int param){
                            
                                            this.localTotalTime=param;
                                    

                               }
                            

                        /**
                        * field for PaidTime
                        */

                        
                                    protected int localPaidTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPaidTime(){
                               return localPaidTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PaidTime
                               */
                               public void setPaidTime(int param){
                            
                                            this.localPaidTime=param;
                                    

                               }
                            

                        /**
                        * field for NonPaidTime
                        */

                        
                                    protected int localNonPaidTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getNonPaidTime(){
                               return localNonPaidTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param NonPaidTime
                               */
                               public void setNonPaidTime(int param){
                            
                                            this.localNonPaidTime=param;
                                    

                               }
                            

                        /**
                        * field for BreakTime
                        */

                        
                                    protected int localBreakTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getBreakTime(){
                               return localBreakTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param BreakTime
                               */
                               public void setBreakTime(int param){
                            
                                            this.localBreakTime=param;
                                    

                               }
                            

                        /**
                        * field for PaidBreakTime
                        */

                        
                                    protected int localPaidBreakTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPaidBreakTime(){
                               return localPaidBreakTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PaidBreakTime
                               */
                               public void setPaidBreakTime(int param){
                            
                                            this.localPaidBreakTime=param;
                                    

                               }
                            

                        /**
                        * field for WaitTime
                        */

                        
                                    protected int localWaitTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getWaitTime(){
                               return localWaitTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WaitTime
                               */
                               public void setWaitTime(int param){
                            
                                            this.localWaitTime=param;
                                    

                               }
                            

                        /**
                        * field for PaidWaitTime
                        */

                        
                                    protected int localPaidWaitTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPaidWaitTime(){
                               return localPaidWaitTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PaidWaitTime
                               */
                               public void setPaidWaitTime(int param){
                            
                                            this.localPaidWaitTime=param;
                                    

                               }
                            

                        /**
                        * field for LayoverTime
                        */

                        
                                    protected int localLayoverTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLayoverTime(){
                               return localLayoverTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LayoverTime
                               */
                               public void setLayoverTime(int param){
                            
                                            this.localLayoverTime=param;
                                    

                               }
                            

                        /**
                        * field for PaidLayoverTime
                        */

                        
                                    protected int localPaidLayoverTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPaidLayoverTime(){
                               return localPaidLayoverTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PaidLayoverTime
                               */
                               public void setPaidLayoverTime(int param){
                            
                                            this.localPaidLayoverTime=param;
                                    

                               }
                            

                        /**
                        * field for DriverRegularTimeCost
                        */

                        
                                    protected int localDriverRegularTimeCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverRegularTimeCost(){
                               return localDriverRegularTimeCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverRegularTimeCost
                               */
                               public void setDriverRegularTimeCost(int param){
                            
                                            this.localDriverRegularTimeCost=param;
                                    

                               }
                            

                        /**
                        * field for DriverOverTimeCost
                        */

                        
                                    protected int localDriverOverTimeCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverOverTimeCost(){
                               return localDriverOverTimeCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverOverTimeCost
                               */
                               public void setDriverOverTimeCost(int param){
                            
                                            this.localDriverOverTimeCost=param;
                                    

                               }
                            

                        /**
                        * field for DriverStopCost
                        */

                        
                                    protected int localDriverStopCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverStopCost(){
                               return localDriverStopCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverStopCost
                               */
                               public void setDriverStopCost(int param){
                            
                                            this.localDriverStopCost=param;
                                    

                               }
                            

                        /**
                        * field for DriverDistanceCost
                        */

                        
                                    protected int localDriverDistanceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverDistanceCost(){
                               return localDriverDistanceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverDistanceCost
                               */
                               public void setDriverDistanceCost(int param){
                            
                                            this.localDriverDistanceCost=param;
                                    

                               }
                            

                        /**
                        * field for DriverPieceCost
                        */

                        
                                    protected int localDriverPieceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverPieceCost(){
                               return localDriverPieceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverPieceCost
                               */
                               public void setDriverPieceCost(int param){
                            
                                            this.localDriverPieceCost=param;
                                    

                               }
                            

                        /**
                        * field for DriverFixedCost
                        */

                        
                                    protected int localDriverFixedCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverFixedCost(){
                               return localDriverFixedCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverFixedCost
                               */
                               public void setDriverFixedCost(int param){
                            
                                            this.localDriverFixedCost=param;
                                    

                               }
                            

                        /**
                        * field for EquipmentFixedCost
                        */

                        
                                    protected int localEquipmentFixedCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getEquipmentFixedCost(){
                               return localEquipmentFixedCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EquipmentFixedCost
                               */
                               public void setEquipmentFixedCost(int param){
                            
                                            this.localEquipmentFixedCost=param;
                                    

                               }
                            

                        /**
                        * field for EquipmentDistanceCost
                        */

                        
                                    protected int localEquipmentDistanceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getEquipmentDistanceCost(){
                               return localEquipmentDistanceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EquipmentDistanceCost
                               */
                               public void setEquipmentDistanceCost(int param){
                            
                                            this.localEquipmentDistanceCost=param;
                                    

                               }
                            

                        /**
                        * field for ReloadTime
                        */

                        
                                    protected int localReloadTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getReloadTime(){
                               return localReloadTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReloadTime
                               */
                               public void setReloadTime(int param){
                            
                                            this.localReloadTime=param;
                                    

                               }
                            

                        /**
                        * field for CreationMethod
                        */

                        
                                    protected java.lang.String localCreationMethod ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCreationMethodTracker = false ;
                           

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
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCreationMethodTracker = true;
                                       } else {
                                          localCreationMethodTracker = false;
                                              
                                       }
                                   
                                            this.localCreationMethod=param;
                                    

                               }
                            

                        /**
                        * field for Week
                        */

                        
                                    protected int localWeek ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getWeek(){
                               return localWeek;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Week
                               */
                               public void setWeek(int param){
                            
                                            this.localWeek=param;
                                    

                               }
                            

                        /**
                        * field for Fees
                        */

                        
                                    protected int localFees ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getFees(){
                               return localFees;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Fees
                               */
                               public void setFees(int param){
                            
                                            this.localFees=param;
                                    

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
                        * field for DepotServiceTime
                        */

                        
                                    protected int localDepotServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDepotServiceTime(){
                               return localDepotServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DepotServiceTime
                               */
                               public void setDepotServiceTime(int param){
                            
                                            this.localDepotServiceTime=param;
                                    

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
                        * field for CalculateReloadTime
                        */

                        
                                    protected boolean localCalculateReloadTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getCalculateReloadTime(){
                               return localCalculateReloadTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CalculateReloadTime
                               */
                               public void setCalculateReloadTime(boolean param){
                            
                                            this.localCalculateReloadTime=param;
                                    

                               }
                            

                        /**
                        * field for ShuttleTime
                        */

                        
                                    protected int localShuttleTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getShuttleTime(){
                               return localShuttleTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ShuttleTime
                               */
                               public void setShuttleTime(int param){
                            
                                            this.localShuttleTime=param;
                                    

                               }
                            

                        /**
                        * field for ShuttleDistance
                        */

                        
                                    protected double localShuttleDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getShuttleDistance(){
                               return localShuttleDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ShuttleDistance
                               */
                               public void setShuttleDistance(double param){
                            
                                            this.localShuttleDistance=param;
                                    

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
                        * field for StemDistanceOut
                        */

                        
                                    protected double localStemDistanceOut ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getStemDistanceOut(){
                               return localStemDistanceOut;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StemDistanceOut
                               */
                               public void setStemDistanceOut(double param){
                            
                                            this.localStemDistanceOut=param;
                                    

                               }
                            

                        /**
                        * field for StemDistanceIn
                        */

                        
                                    protected double localStemDistanceIn ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getStemDistanceIn(){
                               return localStemDistanceIn;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StemDistanceIn
                               */
                               public void setStemDistanceIn(double param){
                            
                                            this.localStemDistanceIn=param;
                                    

                               }
                            

                        /**
                        * field for StemTimeOut
                        */

                        
                                    protected int localStemTimeOut ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getStemTimeOut(){
                               return localStemTimeOut;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StemTimeOut
                               */
                               public void setStemTimeOut(int param){
                            
                                            this.localStemTimeOut=param;
                                    

                               }
                            

                        /**
                        * field for StemTimeIn
                        */

                        
                                    protected int localStemTimeIn ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getStemTimeIn(){
                               return localStemTimeIn;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StemTimeIn
                               */
                               public void setStemTimeIn(int param){
                            
                                            this.localStemTimeIn=param;
                                    

                               }
                            

                        /**
                        * field for PreviousReloadRouteIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity localPreviousReloadRouteIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPreviousReloadRouteIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getPreviousReloadRouteIdentity(){
                               return localPreviousReloadRouteIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreviousReloadRouteIdentity
                               */
                               public void setPreviousReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPreviousReloadRouteIdentityTracker = true;
                                       } else {
                                          localPreviousReloadRouteIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localPreviousReloadRouteIdentity=param;
                                    

                               }
                            

                        /**
                        * field for NextReloadRouteIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity localNextReloadRouteIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localNextReloadRouteIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getNextReloadRouteIdentity(){
                               return localNextReloadRouteIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param NextReloadRouteIdentity
                               */
                               public void setNextReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localNextReloadRouteIdentityTracker = true;
                                       } else {
                                          localNextReloadRouteIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localNextReloadRouteIdentity=param;
                                    

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
                        * field for PreferedSkillSet
                        */

                        
                                    protected java.lang.String localPreferedSkillSet ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPreferedSkillSetTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPreferedSkillSet(){
                               return localPreferedSkillSet;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreferedSkillSet
                               */
                               public void setPreferedSkillSet(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPreferedSkillSetTracker = true;
                                       } else {
                                          localPreferedSkillSetTracker = false;
                                              
                                       }
                                   
                                            this.localPreferedSkillSet=param;
                                    

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

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] localStops ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStopsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingStop[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] getStops(){
                               return localStops;
                           }

                           
                        


                               
                              /**
                               * validate the array for Stops
                               */
                              protected void validateStops(com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Stops
                              */
                              public void setStops(com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] param){
                              
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
                             * @param param com.freshdirect.routing.proxy.stub.transportation.RoutingStop
                             */
                             public void addStops(com.freshdirect.routing.proxy.stub.transportation.RoutingStop param){
                                   if (localStops == null){
                                   localStops = new com.freshdirect.routing.proxy.stub.transportation.RoutingStop[]{};
                                   }

                            
                                 //update the setting tracker
                                localStopsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localStops);
                               list.add(param);
                               this.localStops =
                             (com.freshdirect.routing.proxy.stub.transportation.RoutingStop[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.RoutingStop[list.size()]);

                             }
                             

                        /**
                        * field for ActualDriver1Key
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity localActualDriver1Key ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualDriver1KeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getActualDriver1Key(){
                               return localActualDriver1Key;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriver1Key
                               */
                               public void setActualDriver1Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualDriver1KeyTracker = true;
                                       } else {
                                          localActualDriver1KeyTracker = true;
                                              
                                       }
                                   
                                            this.localActualDriver1Key=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriver2Key
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity localActualDriver2Key ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualDriver2KeyTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getActualDriver2Key(){
                               return localActualDriver2Key;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriver2Key
                               */
                               public void setActualDriver2Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualDriver2KeyTracker = true;
                                       } else {
                                          localActualDriver2KeyTracker = true;
                                              
                                       }
                                   
                                            this.localActualDriver2Key=param;
                                    

                               }
                            

                        /**
                        * field for ActualSizes
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localActualSizes ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualSizesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getActualSizes(){
                               return localActualSizes;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualSizes
                               */
                               public void setActualSizes(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualSizesTracker = true;
                                       } else {
                                          localActualSizesTracker = true;
                                              
                                       }
                                   
                                            this.localActualSizes=param;
                                    

                               }
                            

                        /**
                        * field for ActualStartTime
                        */

                        
                                    protected java.util.Calendar localActualStartTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualStartTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualStartTime(){
                               return localActualStartTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualStartTime
                               */
                               public void setActualStartTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualStartTimeTracker = true;
                                       } else {
                                          localActualStartTimeTracker = false;
                                              
                                       }
                                   
                                            this.localActualStartTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualCompleteTime
                        */

                        
                                    protected java.util.Calendar localActualCompleteTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualCompleteTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualCompleteTime(){
                               return localActualCompleteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualCompleteTime
                               */
                               public void setActualCompleteTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualCompleteTimeTracker = true;
                                       } else {
                                          localActualCompleteTimeTracker = false;
                                              
                                       }
                                   
                                            this.localActualCompleteTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualStops
                        */

                        
                                    protected int localActualStops ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualStops(){
                               return localActualStops;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualStops
                               */
                               public void setActualStops(int param){
                            
                                            this.localActualStops=param;
                                    

                               }
                            

                        /**
                        * field for ActualDistance
                        */

                        
                                    protected int localActualDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDistance(){
                               return localActualDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDistance
                               */
                               public void setActualDistance(int param){
                            
                                            this.localActualDistance=param;
                                    

                               }
                            

                        /**
                        * field for OdometerStart
                        */

                        
                                    protected int localOdometerStart ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOdometerStart(){
                               return localOdometerStart;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OdometerStart
                               */
                               public void setOdometerStart(int param){
                            
                                            this.localOdometerStart=param;
                                    

                               }
                            

                        /**
                        * field for OdometerFinish
                        */

                        
                                    protected int localOdometerFinish ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOdometerFinish(){
                               return localOdometerFinish;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OdometerFinish
                               */
                               public void setOdometerFinish(int param){
                            
                                            this.localOdometerFinish=param;
                                    

                               }
                            

                        /**
                        * field for ActualHelper
                        */

                        
                                    protected int localActualHelper ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualHelper(){
                               return localActualHelper;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualHelper
                               */
                               public void setActualHelper(int param){
                            
                                            this.localActualHelper=param;
                                    

                               }
                            

                        /**
                        * field for Comments
                        */

                        
                                    protected java.lang.String localComments ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCommentsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getComments(){
                               return localComments;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Comments
                               */
                               public void setComments(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCommentsTracker = true;
                                       } else {
                                          localCommentsTracker = false;
                                              
                                       }
                                   
                                            this.localComments=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverRegularTimeCost
                        */

                        
                                    protected int localActualDriverRegularTimeCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverRegularTimeCost(){
                               return localActualDriverRegularTimeCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverRegularTimeCost
                               */
                               public void setActualDriverRegularTimeCost(int param){
                            
                                            this.localActualDriverRegularTimeCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverOvertimeCost
                        */

                        
                                    protected int localActualDriverOvertimeCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverOvertimeCost(){
                               return localActualDriverOvertimeCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverOvertimeCost
                               */
                               public void setActualDriverOvertimeCost(int param){
                            
                                            this.localActualDriverOvertimeCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverStopCost
                        */

                        
                                    protected int localActualDriverStopCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverStopCost(){
                               return localActualDriverStopCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverStopCost
                               */
                               public void setActualDriverStopCost(int param){
                            
                                            this.localActualDriverStopCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverDistanceCost
                        */

                        
                                    protected int localActualDriverDistanceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverDistanceCost(){
                               return localActualDriverDistanceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverDistanceCost
                               */
                               public void setActualDriverDistanceCost(int param){
                            
                                            this.localActualDriverDistanceCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverPieceCost
                        */

                        
                                    protected int localActualDriverPieceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverPieceCost(){
                               return localActualDriverPieceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverPieceCost
                               */
                               public void setActualDriverPieceCost(int param){
                            
                                            this.localActualDriverPieceCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDriverFixedCost
                        */

                        
                                    protected int localActualDriverFixedCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualDriverFixedCost(){
                               return localActualDriverFixedCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDriverFixedCost
                               */
                               public void setActualDriverFixedCost(int param){
                            
                                            this.localActualDriverFixedCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualEquipmentFixedCost
                        */

                        
                                    protected int localActualEquipmentFixedCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualEquipmentFixedCost(){
                               return localActualEquipmentFixedCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualEquipmentFixedCost
                               */
                               public void setActualEquipmentFixedCost(int param){
                            
                                            this.localActualEquipmentFixedCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualEquipmentDistanceCost
                        */

                        
                                    protected int localActualEquipmentDistanceCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualEquipmentDistanceCost(){
                               return localActualEquipmentDistanceCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualEquipmentDistanceCost
                               */
                               public void setActualEquipmentDistanceCost(int param){
                            
                                            this.localActualEquipmentDistanceCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualFees
                        */

                        
                                    protected int localActualFees ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualFees(){
                               return localActualFees;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualFees
                               */
                               public void setActualFees(int param){
                            
                                            this.localActualFees=param;
                                    

                               }
                            

                        /**
                        * field for ActualServiceTime
                        */

                        
                                    protected int localActualServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualServiceTime(){
                               return localActualServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualServiceTime
                               */
                               public void setActualServiceTime(int param){
                            
                                            this.localActualServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualTravelTime
                        */

                        
                                    protected int localActualTravelTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualTravelTime(){
                               return localActualTravelTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualTravelTime
                               */
                               public void setActualTravelTime(int param){
                            
                                            this.localActualTravelTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualBreakTime
                        */

                        
                                    protected int localActualBreakTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualBreakTime(){
                               return localActualBreakTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualBreakTime
                               */
                               public void setActualBreakTime(int param){
                            
                                            this.localActualBreakTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualPaidBreakTime
                        */

                        
                                    protected int localActualPaidBreakTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualPaidBreakTime(){
                               return localActualPaidBreakTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualPaidBreakTime
                               */
                               public void setActualPaidBreakTime(int param){
                            
                                            this.localActualPaidBreakTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualWaitTime
                        */

                        
                                    protected int localActualWaitTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualWaitTime(){
                               return localActualWaitTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualWaitTime
                               */
                               public void setActualWaitTime(int param){
                            
                                            this.localActualWaitTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualPaidWaitTime
                        */

                        
                                    protected int localActualPaidWaitTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualPaidWaitTime(){
                               return localActualPaidWaitTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualPaidWaitTime
                               */
                               public void setActualPaidWaitTime(int param){
                            
                                            this.localActualPaidWaitTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualLayoverTime
                        */

                        
                                    protected int localActualLayoverTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualLayoverTime(){
                               return localActualLayoverTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualLayoverTime
                               */
                               public void setActualLayoverTime(int param){
                            
                                            this.localActualLayoverTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualPaidLayoverTime
                        */

                        
                                    protected int localActualPaidLayoverTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualPaidLayoverTime(){
                               return localActualPaidLayoverTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualPaidLayoverTime
                               */
                               public void setActualPaidLayoverTime(int param){
                            
                                            this.localActualPaidLayoverTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualPreRouteTime
                        */

                        
                                    protected int localActualPreRouteTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualPreRouteTime(){
                               return localActualPreRouteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualPreRouteTime
                               */
                               public void setActualPreRouteTime(int param){
                            
                                            this.localActualPreRouteTime=param;
                                    

                               }
                            

                        /**
                        * field for ActualPostRouteTime
                        */

                        
                                    protected int localActualPostRouteTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualPostRouteTime(){
                               return localActualPostRouteTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualPostRouteTime
                               */
                               public void setActualPostRouteTime(int param){
                            
                                            this.localActualPostRouteTime=param;
                                    

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
                       RoutingRoute.this.serialize(parentQName,factory,xmlWriter);
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
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":RoutingRoute",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "RoutingRoute",
                           xmlWriter);
                   }

               
                   }
                if (localRouteIdentityTracker){
                                    if (localRouteIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

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
                                     localRouteIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeIdentity"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"routeNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"routeNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("routeNumber");
                                    }
                                
                                               if (localRouteNumber==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("routeNumber cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteNumber));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localRouteIDTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             } if (localDescriptionTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

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
                                     localOrigin.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","origin"),
                                        factory,xmlWriter);
                                    }
                                } if (localDestinationTracker){
                                    if (localDestination==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

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
                                     localDestination.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","destination"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                              if (localStartTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             } if (localCompleteTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"completeTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"completeTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("completeTime");
                                    }
                                

                                          if (localCompleteTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("completeTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompleteTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localCapacityTracker){
                                    if (localCapacity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"capacity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"capacity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("capacity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localCapacity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","capacity"),
                                        factory,xmlWriter);
                                    }
                                } if (localMaxRunningCapacityTracker){
                                    if (localMaxRunningCapacity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"maxRunningCapacity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"maxRunningCapacity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("maxRunningCapacity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localMaxRunningCapacity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","maxRunningCapacity"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"distance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"distance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("distance");
                                    }
                                
                                               if (java.lang.Double.isNaN(localDistance)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("distance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localStartingQuantityTracker){
                                    if (localStartingQuantity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"startingQuantity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"startingQuantity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("startingQuantity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localStartingQuantity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","startingQuantity"),
                                        factory,xmlWriter);
                                    }
                                } if (localEndingQuantityTracker){
                                    if (localEndingQuantity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"endingQuantity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"endingQuantity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("endingQuantity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localEndingQuantity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","endingQuantity"),
                                        factory,xmlWriter);
                                    }
                                } if (localDriversTracker){
                                       if (localDrivers!=null){
                                            for (int i = 0;i < localDrivers.length;i++){
                                                if (localDrivers[i] != null){
                                                 localDrivers[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","drivers"),
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
                                                 localRouteEquipment[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeEquipment"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("routeEquipment cannot be null!!");
                                        
                                    }
                                 }
                                            if (localStatus==null){
                                                 throw new org.apache.axis2.databinding.ADBException("status cannot be null!!");
                                            }
                                           localStatus.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","status"),
                                               factory,xmlWriter);
                                        
                                            if (localDay==null){
                                                 throw new org.apache.axis2.databinding.ADBException("day cannot be null!!");
                                            }
                                           localDay.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","day"),
                                               factory,xmlWriter);
                                        
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"travelTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"travelTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("travelTime");
                                    }
                                
                                               if (localTravelTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("travelTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTravelTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"serviceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"serviceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("serviceTime");
                                    }
                                
                                               if (localServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("serviceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"totalTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"totalTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("totalTime");
                                    }
                                
                                               if (localTotalTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("totalTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"paidTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"paidTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("paidTime");
                                    }
                                
                                               if (localPaidTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("paidTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"nonPaidTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"nonPaidTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("nonPaidTime");
                                    }
                                
                                               if (localNonPaidTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("nonPaidTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNonPaidTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"breakTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"breakTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("breakTime");
                                    }
                                
                                               if (localBreakTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("breakTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBreakTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"paidBreakTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"paidBreakTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("paidBreakTime");
                                    }
                                
                                               if (localPaidBreakTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("paidBreakTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidBreakTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"waitTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"waitTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("waitTime");
                                    }
                                
                                               if (localWaitTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("waitTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWaitTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"paidWaitTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"paidWaitTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("paidWaitTime");
                                    }
                                
                                               if (localPaidWaitTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("paidWaitTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidWaitTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"layoverTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"layoverTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("layoverTime");
                                    }
                                
                                               if (localLayoverTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("layoverTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLayoverTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"paidLayoverTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"paidLayoverTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("paidLayoverTime");
                                    }
                                
                                               if (localPaidLayoverTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("paidLayoverTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidLayoverTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverRegularTimeCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverRegularTimeCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverRegularTimeCost");
                                    }
                                
                                               if (localDriverRegularTimeCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverRegularTimeCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverRegularTimeCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverOverTimeCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverOverTimeCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverOverTimeCost");
                                    }
                                
                                               if (localDriverOverTimeCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverOverTimeCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverOverTimeCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverStopCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverStopCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverStopCost");
                                    }
                                
                                               if (localDriverStopCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverStopCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverStopCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverDistanceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverDistanceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverDistanceCost");
                                    }
                                
                                               if (localDriverDistanceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverDistanceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverDistanceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverPieceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverPieceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverPieceCost");
                                    }
                                
                                               if (localDriverPieceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverPieceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverPieceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driverFixedCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driverFixedCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driverFixedCost");
                                    }
                                
                                               if (localDriverFixedCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("driverFixedCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverFixedCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"equipmentFixedCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"equipmentFixedCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("equipmentFixedCost");
                                    }
                                
                                               if (localEquipmentFixedCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("equipmentFixedCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEquipmentFixedCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"equipmentDistanceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"equipmentDistanceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("equipmentDistanceCost");
                                    }
                                
                                               if (localEquipmentDistanceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("equipmentDistanceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEquipmentDistanceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"reloadTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"reloadTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("reloadTime");
                                    }
                                
                                               if (localReloadTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("reloadTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReloadTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localCreationMethodTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"week", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"week");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("week");
                                    }
                                
                                               if (localWeek==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("week cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeek));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"fees", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"fees");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("fees");
                                    }
                                
                                               if (localFees==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("fees cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFees));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"depotServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"depotServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("depotServiceTime");
                                    }
                                
                                               if (localDepotServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("depotServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDepotServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"calculateReloadTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"calculateReloadTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("calculateReloadTime");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("calculateReloadTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculateReloadTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"shuttleTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"shuttleTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("shuttleTime");
                                    }
                                
                                               if (localShuttleTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("shuttleTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShuttleTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"shuttleDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"shuttleDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("shuttleDistance");
                                    }
                                
                                               if (java.lang.Double.isNaN(localShuttleDistance)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("shuttleDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShuttleDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stemDistanceOut", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stemDistanceOut");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stemDistanceOut");
                                    }
                                
                                               if (java.lang.Double.isNaN(localStemDistanceOut)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stemDistanceOut cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemDistanceOut));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stemDistanceIn", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stemDistanceIn");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stemDistanceIn");
                                    }
                                
                                               if (java.lang.Double.isNaN(localStemDistanceIn)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stemDistanceIn cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemDistanceIn));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stemTimeOut", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stemTimeOut");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stemTimeOut");
                                    }
                                
                                               if (localStemTimeOut==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stemTimeOut cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemTimeOut));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stemTimeIn", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stemTimeIn");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stemTimeIn");
                                    }
                                
                                               if (localStemTimeIn==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stemTimeIn cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemTimeIn));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localPreviousReloadRouteIdentityTracker){
                                    if (localPreviousReloadRouteIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"previousReloadRouteIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"previousReloadRouteIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("previousReloadRouteIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localPreviousReloadRouteIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","previousReloadRouteIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localNextReloadRouteIdentityTracker){
                                    if (localNextReloadRouteIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"nextReloadRouteIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"nextReloadRouteIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("nextReloadRouteIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localNextReloadRouteIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","nextReloadRouteIdentity"),
                                        factory,xmlWriter);
                                    }
                                }
                                            if (localStartingLoadType==null){
                                                 throw new org.apache.axis2.databinding.ADBException("startingLoadType cannot be null!!");
                                            }
                                           localStartingLoadType.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","startingLoadType"),
                                               factory,xmlWriter);
                                         if (localPreferedSkillSetTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"preferedSkillSet", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"preferedSkillSet");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("preferedSkillSet");
                                    }
                                

                                          if (localPreferedSkillSet==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("preferedSkillSet cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPreferedSkillSet);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDispatcherUserIdentityTracker){
                                    if (localDispatcherUserIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

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
                                     localDispatcherUserIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dispatcherUserIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localStopsTracker){
                                       if (localStops!=null){
                                            for (int i = 0;i < localStops.length;i++){
                                                if (localStops[i] != null){
                                                 localStops[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stops"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("stops cannot be null!!");
                                        
                                    }
                                 } if (localActualDriver1KeyTracker){
                                    if (localActualDriver1Key==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"actualDriver1Key", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"actualDriver1Key");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("actualDriver1Key");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localActualDriver1Key.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriver1Key"),
                                        factory,xmlWriter);
                                    }
                                } if (localActualDriver2KeyTracker){
                                    if (localActualDriver2Key==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"actualDriver2Key", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"actualDriver2Key");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("actualDriver2Key");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localActualDriver2Key.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriver2Key"),
                                        factory,xmlWriter);
                                    }
                                } if (localActualSizesTracker){
                                    if (localActualSizes==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"actualSizes", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"actualSizes");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("actualSizes");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localActualSizes.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualSizes"),
                                        factory,xmlWriter);
                                    }
                                } if (localActualStartTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualStartTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualStartTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualStartTime");
                                    }
                                

                                          if (localActualStartTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualStartTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStartTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localActualCompleteTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualCompleteTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualCompleteTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualCompleteTime");
                                    }
                                

                                          if (localActualCompleteTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualCompleteTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualCompleteTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualStops", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualStops");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualStops");
                                    }
                                
                                               if (localActualStops==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualStops cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStops));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDistance");
                                    }
                                
                                               if (localActualDistance==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"odometerStart", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"odometerStart");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("odometerStart");
                                    }
                                
                                               if (localOdometerStart==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("odometerStart cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOdometerStart));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"odometerFinish", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"odometerFinish");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("odometerFinish");
                                    }
                                
                                               if (localOdometerFinish==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("odometerFinish cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOdometerFinish));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualHelper", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualHelper");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualHelper");
                                    }
                                
                                               if (localActualHelper==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualHelper cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualHelper));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localCommentsTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"comments", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"comments");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("comments");
                                    }
                                

                                          if (localComments==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("comments cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localComments);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverRegularTimeCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverRegularTimeCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverRegularTimeCost");
                                    }
                                
                                               if (localActualDriverRegularTimeCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverRegularTimeCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverRegularTimeCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverOvertimeCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverOvertimeCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverOvertimeCost");
                                    }
                                
                                               if (localActualDriverOvertimeCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverOvertimeCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverOvertimeCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverStopCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverStopCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverStopCost");
                                    }
                                
                                               if (localActualDriverStopCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverStopCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverStopCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverDistanceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverDistanceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverDistanceCost");
                                    }
                                
                                               if (localActualDriverDistanceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverDistanceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverDistanceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverPieceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverPieceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverPieceCost");
                                    }
                                
                                               if (localActualDriverPieceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverPieceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverPieceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDriverFixedCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDriverFixedCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDriverFixedCost");
                                    }
                                
                                               if (localActualDriverFixedCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDriverFixedCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverFixedCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualEquipmentFixedCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualEquipmentFixedCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualEquipmentFixedCost");
                                    }
                                
                                               if (localActualEquipmentFixedCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualEquipmentFixedCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualEquipmentFixedCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualEquipmentDistanceCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualEquipmentDistanceCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualEquipmentDistanceCost");
                                    }
                                
                                               if (localActualEquipmentDistanceCost==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualEquipmentDistanceCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualEquipmentDistanceCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualFees", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualFees");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualFees");
                                    }
                                
                                               if (localActualFees==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualFees cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualFees));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualServiceTime");
                                    }
                                
                                               if (localActualServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualTravelTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualTravelTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualTravelTime");
                                    }
                                
                                               if (localActualTravelTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualTravelTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualTravelTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualBreakTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualBreakTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualBreakTime");
                                    }
                                
                                               if (localActualBreakTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualBreakTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualBreakTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualPaidBreakTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualPaidBreakTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualPaidBreakTime");
                                    }
                                
                                               if (localActualPaidBreakTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualPaidBreakTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidBreakTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualWaitTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualWaitTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualWaitTime");
                                    }
                                
                                               if (localActualWaitTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualWaitTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualWaitTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualPaidWaitTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualPaidWaitTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualPaidWaitTime");
                                    }
                                
                                               if (localActualPaidWaitTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualPaidWaitTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidWaitTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualLayoverTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualLayoverTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualLayoverTime");
                                    }
                                
                                               if (localActualLayoverTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualLayoverTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualLayoverTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualPaidLayoverTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualPaidLayoverTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualPaidLayoverTime");
                                    }
                                
                                               if (localActualPaidLayoverTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualPaidLayoverTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidLayoverTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualPreRouteTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualPreRouteTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualPreRouteTime");
                                    }
                                
                                               if (localActualPreRouteTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualPreRouteTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPreRouteTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualPostRouteTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualPostRouteTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualPostRouteTime");
                                    }
                                
                                               if (localActualPostRouteTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualPostRouteTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPostRouteTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
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
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "routeIdentity"));
                            
                            
                                    elementList.add(localRouteIdentity==null?null:
                                    localRouteIdentity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "routeNumber"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteNumber));
                             if (localRouteIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "routeID"));
                                 
                                        if (localRouteID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("routeID cannot be null!!");
                                        }
                                    } if (localDescriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "description"));
                                 
                                        if (localDescription != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDescription));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("description cannot be null!!");
                                        }
                                    } if (localOriginTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "origin"));
                            
                            
                                    elementList.add(localOrigin==null?null:
                                    localOrigin);
                                } if (localDestinationTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "destination"));
                            
                            
                                    elementList.add(localDestination==null?null:
                                    localDestination);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "helper"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHelper));
                             if (localStartTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "startTime"));
                                 
                                        if (localStartTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStartTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("startTime cannot be null!!");
                                        }
                                    } if (localCompleteTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "completeTime"));
                                 
                                        if (localCompleteTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompleteTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("completeTime cannot be null!!");
                                        }
                                    } if (localCapacityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "capacity"));
                            
                            
                                    elementList.add(localCapacity==null?null:
                                    localCapacity);
                                } if (localMaxRunningCapacityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "maxRunningCapacity"));
                            
                            
                                    elementList.add(localMaxRunningCapacity==null?null:
                                    localMaxRunningCapacity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "color"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localColor));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "distance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistance));
                             if (localStartingQuantityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "startingQuantity"));
                            
                            
                                    elementList.add(localStartingQuantity==null?null:
                                    localStartingQuantity);
                                } if (localEndingQuantityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "endingQuantity"));
                            
                            
                                    elementList.add(localEndingQuantity==null?null:
                                    localEndingQuantity);
                                } if (localDriversTracker){
                             if (localDrivers!=null) {
                                 for (int i = 0;i < localDrivers.length;i++){

                                    if (localDrivers[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
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
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "routeEquipment"));
                                         elementList.add(localRouteEquipment[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("routeEquipment cannot be null!!");
                                    
                             }

                        }
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "status"));
                            
                            
                                    if (localStatus==null){
                                         throw new org.apache.axis2.databinding.ADBException("status cannot be null!!");
                                    }
                                    elementList.add(localStatus);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "day"));
                            
                            
                                    if (localDay==null){
                                         throw new org.apache.axis2.databinding.ADBException("day cannot be null!!");
                                    }
                                    elementList.add(localDay);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "teamSplit"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTeamSplit));
                             if (localModelIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "modelID"));
                                 
                                        if (localModelID != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localModelID));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("modelID cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "loadPriority"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoadPriority));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "maximumTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaximumTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "preferredTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreferredTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "regularTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegularTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "travelTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTravelTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "serviceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "totalTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "paidTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "nonPaidTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNonPaidTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "breakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBreakTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "paidBreakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidBreakTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "waitTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWaitTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "paidWaitTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidWaitTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "layoverTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLayoverTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "paidLayoverTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidLayoverTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverRegularTimeCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverRegularTimeCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverOverTimeCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverOverTimeCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverStopCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverStopCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverDistanceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverDistanceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverPieceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverPieceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driverFixedCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverFixedCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "equipmentFixedCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEquipmentFixedCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "equipmentDistanceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEquipmentDistanceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "reloadTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReloadTime));
                             if (localCreationMethodTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "creationMethod"));
                                 
                                        if (localCreationMethod != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreationMethod));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("creationMethod cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "week"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeek));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "fees"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFees));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "lastStopIsDestination"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLastStopIsDestination));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "depotServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDepotServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "originIsDestination"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginIsDestination));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "calculateReloadTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculateReloadTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "shuttleTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShuttleTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "shuttleDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShuttleDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "preRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreRouteTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "postRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostRouteTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stemDistanceOut"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemDistanceOut));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stemDistanceIn"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemDistanceIn));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stemTimeOut"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemTimeOut));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stemTimeIn"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStemTimeIn));
                             if (localPreviousReloadRouteIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "previousReloadRouteIdentity"));
                            
                            
                                    elementList.add(localPreviousReloadRouteIdentity==null?null:
                                    localPreviousReloadRouteIdentity);
                                } if (localNextReloadRouteIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "nextReloadRouteIdentity"));
                            
                            
                                    elementList.add(localNextReloadRouteIdentity==null?null:
                                    localNextReloadRouteIdentity);
                                }
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "startingLoadType"));
                            
                            
                                    if (localStartingLoadType==null){
                                         throw new org.apache.axis2.databinding.ADBException("startingLoadType cannot be null!!");
                                    }
                                    elementList.add(localStartingLoadType);
                                 if (localPreferedSkillSetTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "preferedSkillSet"));
                                 
                                        if (localPreferedSkillSet != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreferedSkillSet));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("preferedSkillSet cannot be null!!");
                                        }
                                    } if (localDispatcherUserIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "dispatcherUserIdentity"));
                            
                            
                                    elementList.add(localDispatcherUserIdentity==null?null:
                                    localDispatcherUserIdentity);
                                } if (localStopsTracker){
                             if (localStops!=null) {
                                 for (int i = 0;i < localStops.length;i++){

                                    if (localStops[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "stops"));
                                         elementList.add(localStops[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("stops cannot be null!!");
                                    
                             }

                        } if (localActualDriver1KeyTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriver1Key"));
                            
                            
                                    elementList.add(localActualDriver1Key==null?null:
                                    localActualDriver1Key);
                                } if (localActualDriver2KeyTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriver2Key"));
                            
                            
                                    elementList.add(localActualDriver2Key==null?null:
                                    localActualDriver2Key);
                                } if (localActualSizesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualSizes"));
                            
                            
                                    elementList.add(localActualSizes==null?null:
                                    localActualSizes);
                                } if (localActualStartTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualStartTime"));
                                 
                                        if (localActualStartTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStartTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualStartTime cannot be null!!");
                                        }
                                    } if (localActualCompleteTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualCompleteTime"));
                                 
                                        if (localActualCompleteTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualCompleteTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualCompleteTime cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualStops"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualStops));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "odometerStart"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOdometerStart));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "odometerFinish"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOdometerFinish));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualHelper"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualHelper));
                             if (localCommentsTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "comments"));
                                 
                                        if (localComments != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localComments));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("comments cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverRegularTimeCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverRegularTimeCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverOvertimeCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverOvertimeCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverStopCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverStopCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverDistanceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverDistanceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverPieceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverPieceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDriverFixedCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDriverFixedCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualEquipmentFixedCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualEquipmentFixedCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualEquipmentDistanceCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualEquipmentDistanceCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualFees"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualFees));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualTravelTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualTravelTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualBreakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualBreakTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualPaidBreakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidBreakTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualWaitTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualWaitTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualPaidWaitTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidWaitTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualLayoverTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualLayoverTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualPaidLayoverTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPaidLayoverTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualPreRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPreRouteTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualPostRouteTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualPostRouteTime));
                            

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
        public static RoutingRoute parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            RoutingRoute object =
                new RoutingRoute();

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
                    
                            if (!"RoutingRoute".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RoutingRoute)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list16 = new java.util.ArrayList();
                    
                        java.util.ArrayList list17 = new java.util.ArrayList();
                    
                        java.util.ArrayList list66 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRouteIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","description").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","origin").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","destination").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","helper").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","startTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStartTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","completeTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCompleteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","capacity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setCapacity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setCapacity(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","maxRunningCapacity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setMaxRunningCapacity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setMaxRunningCapacity(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","color").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","distance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","startingQuantity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setStartingQuantity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setStartingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","endingQuantity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setEndingQuantity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setEndingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","drivers").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list16.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone16 = false;
                                                        while(!loopDone16){
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
                                                                loopDone16 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","drivers").equals(reader.getName())){
                                                                    list16.add(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone16 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setDrivers((com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.class,
                                                                list16));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeEquipment").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list17.add(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.Factory.parse(reader));
                                                                
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
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeEquipment").equals(reader.getName())){
                                                                    list17.add(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone17 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setRouteEquipment((com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity.class,
                                                                list17));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","status").equals(reader.getName())){
                                
                                                object.setStatus(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","day").equals(reader.getName())){
                                
                                                object.setDay(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","teamSplit").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","modelID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setModelID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","loadPriority").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","maximumTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preferredTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","regularTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","travelTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTravelTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","serviceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","totalTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTotalTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","paidTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPaidTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","nonPaidTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setNonPaidTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","breakTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setBreakTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","paidBreakTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPaidBreakTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","waitTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWaitTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","paidWaitTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPaidWaitTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","layoverTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLayoverTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","paidLayoverTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPaidLayoverTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverRegularTimeCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverRegularTimeCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverOverTimeCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverOverTimeCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverStopCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverStopCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverDistanceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverDistanceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverPieceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverPieceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverFixedCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverFixedCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","equipmentFixedCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEquipmentFixedCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","equipmentDistanceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEquipmentDistanceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","reloadTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setReloadTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","creationMethod").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCreationMethod(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","week").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWeek(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","fees").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFees(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","lastStopIsDestination").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","depotServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDepotServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","originIsDestination").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","calculateReloadTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCalculateReloadTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","shuttleTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setShuttleTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","shuttleDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setShuttleDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preRouteTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","postRouteTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stemDistanceOut").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStemDistanceOut(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stemDistanceIn").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStemDistanceIn(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stemTimeOut").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStemTimeOut(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stemTimeIn").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStemTimeIn(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","previousReloadRouteIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setPreviousReloadRouteIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setPreviousReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","nextReloadRouteIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setNextReloadRouteIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setNextReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","startingLoadType").equals(reader.getName())){
                                
                                                object.setStartingLoadType(com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preferedSkillSet").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPreferedSkillSet(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dispatcherUserIdentity").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list66.add(com.freshdirect.routing.proxy.stub.transportation.RoutingStop.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone66 = false;
                                                        while(!loopDone66){
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
                                                                loopDone66 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                                                    list66.add(com.freshdirect.routing.proxy.stub.transportation.RoutingStop.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone66 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setStops((com.freshdirect.routing.proxy.stub.transportation.RoutingStop[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.RoutingStop.class,
                                                                list66));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriver1Key").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setActualDriver1Key(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setActualDriver1Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriver2Key").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setActualDriver2Key(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setActualDriver2Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualSizes").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setActualSizes(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setActualSizes(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualStartTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualStartTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualCompleteTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualCompleteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualStops").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualStops(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","odometerStart").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOdometerStart(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","odometerFinish").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOdometerFinish(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualHelper").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualHelper(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","comments").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setComments(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverRegularTimeCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverRegularTimeCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverOvertimeCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverOvertimeCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverStopCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverStopCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverDistanceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverDistanceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverPieceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverPieceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDriverFixedCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDriverFixedCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualEquipmentFixedCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualEquipmentFixedCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualEquipmentDistanceCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualEquipmentDistanceCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualFees").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualFees(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualTravelTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualTravelTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualBreakTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualBreakTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualPaidBreakTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualPaidBreakTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualWaitTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualWaitTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualPaidWaitTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualPaidWaitTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualLayoverTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualLayoverTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualPaidLayoverTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualPaidLayoverTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualPreRouteTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualPreRouteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualPostRouteTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualPostRouteTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
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
           
          