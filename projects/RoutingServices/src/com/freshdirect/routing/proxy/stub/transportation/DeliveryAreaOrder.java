
/**
 * DeliveryAreaOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  DeliveryAreaOrder bean class
            */
        
        public  class DeliveryAreaOrder
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = DeliveryAreaOrder
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
                        * field for Identity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity localIdentity ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity getIdentity(){
                               return localIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Identity
                               */
                               public void setIdentity(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity param){
                            
                                            this.localIdentity=param;
                                    

                               }
                            

                        /**
                        * field for OrderType
                        */

                        
                                    protected java.lang.String localOrderType ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOrderType(){
                               return localOrderType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OrderType
                               */
                               public void setOrderType(java.lang.String param){
                            
                                            this.localOrderType=param;
                                    

                               }
                            

                        /**
                        * field for Quantities
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localQuantities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQuantitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getQuantities(){
                               return localQuantities;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Quantities
                               */
                               public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localQuantitiesTracker = true;
                                       } else {
                                          localQuantitiesTracker = true;
                                              
                                       }
                                   
                                            this.localQuantities=param;
                                    

                               }
                            

                        /**
                        * field for PickupQuantities
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localPickupQuantities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPickupQuantitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getPickupQuantities(){
                               return localPickupQuantities;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PickupQuantities
                               */
                               public void setPickupQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPickupQuantitiesTracker = true;
                                       } else {
                                          localPickupQuantitiesTracker = true;
                                              
                                       }
                                   
                                            this.localPickupQuantities=param;
                                    

                               }
                            

                        /**
                        * field for Confirmed
                        */

                        
                                    protected boolean localConfirmed ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localConfirmedTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getConfirmed(){
                               return localConfirmed;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Confirmed
                               */
                               public void setConfirmed(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localConfirmedTracker = false;
                                              
                                       } else {
                                          localConfirmedTracker = true;
                                       }
                                   
                                            this.localConfirmed=param;
                                    

                               }
                            

                        /**
                        * field for ReservedTime
                        */

                        
                                    protected java.util.Calendar localReservedTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localReservedTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getReservedTime(){
                               return localReservedTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReservedTime
                               */
                               public void setReservedTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localReservedTimeTracker = true;
                                       } else {
                                          localReservedTimeTracker = false;
                                              
                                       }
                                   
                                            this.localReservedTime=param;
                                    

                               }
                            

                        /**
                        * field for DeliveryWindowStart
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localDeliveryWindowStart ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDeliveryWindowStartTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getDeliveryWindowStart(){
                               return localDeliveryWindowStart;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DeliveryWindowStart
                               */
                               public void setDeliveryWindowStart(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDeliveryWindowStartTracker = true;
                                       } else {
                                          localDeliveryWindowStartTracker = true;
                                              
                                       }
                                   
                                            this.localDeliveryWindowStart=param;
                                    

                               }
                            

                        /**
                        * field for DeliveryWindowEnd
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localDeliveryWindowEnd ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDeliveryWindowEndTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getDeliveryWindowEnd(){
                               return localDeliveryWindowEnd;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DeliveryWindowEnd
                               */
                               public void setDeliveryWindowEnd(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDeliveryWindowEndTracker = true;
                                       } else {
                                          localDeliveryWindowEndTracker = true;
                                              
                                       }
                                   
                                            this.localDeliveryWindowEnd=param;
                                    

                               }
                            

                        /**
                        * field for StopServiceTime
                        */

                        
                                    protected int localStopServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getStopServiceTime(){
                               return localStopServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StopServiceTime
                               */
                               public void setStopServiceTime(int param){
                            
                                            this.localStopServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for OrderServiceTime
                        */

                        
                                    protected int localOrderServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getOrderServiceTime(){
                               return localOrderServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OrderServiceTime
                               */
                               public void setOrderServiceTime(int param){
                            
                                            this.localOrderServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for IgnoreStopServiceTime
                        */

                        
                                    protected boolean localIgnoreStopServiceTime ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getIgnoreStopServiceTime(){
                               return localIgnoreStopServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param IgnoreStopServiceTime
                               */
                               public void setIgnoreStopServiceTime(boolean param){
                            
                                            this.localIgnoreStopServiceTime=param;
                                    

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
                        * field for Latitude
                        */

                        
                                    protected int localLatitude ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLatitude(){
                               return localLatitude;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Latitude
                               */
                               public void setLatitude(int param){
                            
                                            this.localLatitude=param;
                                    

                               }
                            

                        /**
                        * field for Longitude
                        */

                        
                                    protected int localLongitude ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLongitude(){
                               return localLongitude;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Longitude
                               */
                               public void setLongitude(int param){
                            
                                            this.localLongitude=param;
                                    

                               }
                            

                        /**
                        * field for LocationId
                        */

                        
                                    protected java.lang.String localLocationId ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationId(){
                               return localLocationId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationId
                               */
                               public void setLocationId(java.lang.String param){
                            
                                            this.localLocationId=param;
                                    

                               }
                            

                        /**
                        * field for LocationType
                        */

                        
                                    protected java.lang.String localLocationType ;
                                

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationType(){
                               return localLocationType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationType
                               */
                               public void setLocationType(java.lang.String param){
                            
                                            this.localLocationType=param;
                                    

                               }
                            

                        /**
                        * field for Description
                        */

                        
                                    protected java.lang.String localDescription ;
                                

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
                            
                                            this.localDescription=param;
                                    

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
                        * field for Activities
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] localActivities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActivitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] getActivities(){
                               return localActivities;
                           }

                           
                        


                               
                              /**
                               * validate the array for Activities
                               */
                              protected void validateActivities(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Activities
                              */
                              public void setActivities(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] param){
                              
                                   validateActivities(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localActivitiesTracker = true;
                                          } else {
                                             localActivitiesTracker = false;
                                                 
                                          }
                                      
                                      this.localActivities=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity
                             */
                             public void addActivities(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity param){
                                   if (localActivities == null){
                                   localActivities = new com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[]{};
                                   }

                            
                                 //update the setting tracker
                                localActivitiesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localActivities);
                               list.add(param);
                               this.localActivities =
                             (com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[list.size()]);

                             }
                             

                        /**
                        * field for Movable
                        */

                        
                                    protected java.util.Calendar localMovable ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMovableTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getMovable(){
                               return localMovable;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Movable
                               */
                               public void setMovable(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localMovableTracker = true;
                                       } else {
                                          localMovableTracker = false;
                                              
                                       }
                                   
                                            this.localMovable=param;
                                    

                               }
                            

                        /**
                        * field for RouteId
                        */

                        
                                    protected int localRouteId ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getRouteId(){
                               return localRouteId;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteId
                               */
                               public void setRouteId(int param){
                            
                                            this.localRouteId=param;
                                    

                               }
                            

                        /**
                        * field for SequenceNumber
                        */

                        
                                    protected int localSequenceNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSequenceNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getSequenceNumber(){
                               return localSequenceNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SequenceNumber
                               */
                               public void setSequenceNumber(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localSequenceNumberTracker = false;
                                              
                                       } else {
                                          localSequenceNumberTracker = true;
                                       }
                                   
                                            this.localSequenceNumber=param;
                                    

                               }
                            

                        /**
                        * field for WaitSeconds
                        */

                        
                                    protected int localWaitSeconds ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localWaitSecondsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getWaitSeconds(){
                               return localWaitSeconds;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param WaitSeconds
                               */
                               public void setWaitSeconds(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localWaitSecondsTracker = false;
                                              
                                       } else {
                                          localWaitSecondsTracker = true;
                                       }
                                   
                                            this.localWaitSeconds=param;
                                    

                               }
                            

                        /**
                        * field for PlannedArrivalTime
                        */

                        
                                    protected java.util.Calendar localPlannedArrivalTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPlannedArrivalTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedArrivalTime(){
                               return localPlannedArrivalTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedArrivalTime
                               */
                               public void setPlannedArrivalTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPlannedArrivalTimeTracker = true;
                                       } else {
                                          localPlannedArrivalTimeTracker = false;
                                              
                                       }
                                   
                                            this.localPlannedArrivalTime=param;
                                    

                               }
                            

                        /**
                        * field for DistanceTo
                        */

                        
                                    protected int localDistanceTo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDistanceToTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDistanceTo(){
                               return localDistanceTo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DistanceTo
                               */
                               public void setDistanceTo(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localDistanceToTracker = false;
                                              
                                       } else {
                                          localDistanceToTracker = true;
                                       }
                                   
                                            this.localDistanceTo=param;
                                    

                               }
                            

                        /**
                        * field for SecondsTo
                        */

                        
                                    protected int localSecondsTo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSecondsToTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getSecondsTo(){
                               return localSecondsTo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SecondsTo
                               */
                               public void setSecondsTo(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localSecondsToTracker = false;
                                              
                                       } else {
                                          localSecondsToTracker = true;
                                       }
                                   
                                            this.localSecondsTo=param;
                                    

                               }
                            

                        /**
                        * field for Sequenced
                        */

                        
                                    protected boolean localSequenced ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSequencedTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getSequenced(){
                               return localSequenced;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Sequenced
                               */
                               public void setSequenced(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localSequencedTracker = false;
                                              
                                       } else {
                                          localSequencedTracker = true;
                                       }
                                   
                                            this.localSequenced=param;
                                    

                               }
                            

                        /**
                        * field for ReferenceNumber
                        */

                        
                                    protected java.lang.String localReferenceNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localReferenceNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getReferenceNumber(){
                               return localReferenceNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReferenceNumber
                               */
                               public void setReferenceNumber(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localReferenceNumberTracker = true;
                                       } else {
                                          localReferenceNumberTracker = false;
                                              
                                       }
                                   
                                            this.localReferenceNumber=param;
                                    

                               }
                            

                        /**
                        * field for ResultType
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.ReserveResultType localResultType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localResultTypeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.ReserveResultType
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.ReserveResultType getResultType(){
                               return localResultType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ResultType
                               */
                               public void setResultType(com.freshdirect.routing.proxy.stub.transportation.ReserveResultType param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localResultTypeTracker = true;
                                       } else {
                                          localResultTypeTracker = true;
                                              
                                       }
                                   
                                            this.localResultType=param;
                                    

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
                       DeliveryAreaOrder.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":DeliveryAreaOrder",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "DeliveryAreaOrder",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localIdentity==null){
                                                 throw new org.apache.axis2.databinding.ADBException("identity cannot be null!!");
                                            }
                                           localIdentity.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","identity"),
                                               factory,xmlWriter);
                                        
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"orderType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"orderType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("orderType");
                                    }
                                

                                          if (localOrderType==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("orderType cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOrderType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                              if (localQuantitiesTracker){
                                    if (localQuantities==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"quantities", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"quantities");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("quantities");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localQuantities.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","quantities"),
                                        factory,xmlWriter);
                                    }
                                } if (localPickupQuantitiesTracker){
                                    if (localPickupQuantities==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"pickupQuantities", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"pickupQuantities");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("pickupQuantities");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localPickupQuantities.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","pickupQuantities"),
                                        factory,xmlWriter);
                                    }
                                } if (localConfirmedTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"confirmed", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"confirmed");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("confirmed");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("confirmed cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConfirmed));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localReservedTimeTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"reservedTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"reservedTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("reservedTime");
                                    }
                                

                                          if (localReservedTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("reservedTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReservedTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDeliveryWindowStartTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"deliveryWindowStart", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"deliveryWindowStart");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("deliveryWindowStart");
                                    }
                                

                                          if (localDeliveryWindowStart==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryWindowStart));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDeliveryWindowEndTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"deliveryWindowEnd", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"deliveryWindowEnd");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("deliveryWindowEnd");
                                    }
                                

                                          if (localDeliveryWindowEnd==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryWindowEnd));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stopServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stopServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stopServiceTime");
                                    }
                                
                                               if (localStopServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stopServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStopServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"orderServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"orderServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("orderServiceTime");
                                    }
                                
                                               if (localOrderServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("orderServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"ignoreStopServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"ignoreStopServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("ignoreStopServiceTime");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("ignoreStopServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIgnoreStopServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"latitude", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"latitude");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("latitude");
                                    }
                                
                                               if (localLatitude==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("latitude cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLatitude));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"longitude", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"longitude");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("longitude");
                                    }
                                
                                               if (localLongitude==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("longitude cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLongitude));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationId");
                                    }
                                

                                          if (localLocationId==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationId cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationId);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationType");
                                    }
                                

                                          if (localLocationType==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationType cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             
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
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
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
                              if (localActivitiesTracker){
                                       if (localActivities!=null){
                                            for (int i = 0;i < localActivities.length;i++){
                                                if (localActivities[i] != null){
                                                 localActivities[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","activities"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("activities cannot be null!!");
                                        
                                    }
                                 } if (localMovableTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"movable", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"movable");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("movable");
                                    }
                                

                                          if (localMovable==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("movable cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMovable));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"routeId", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"routeId");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("routeId");
                                    }
                                
                                               if (localRouteId==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("routeId cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteId));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localSequenceNumberTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"sequenceNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"sequenceNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("sequenceNumber");
                                    }
                                
                                               if (localSequenceNumber==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("sequenceNumber cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSequenceNumber));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localWaitSecondsTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"waitSeconds", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"waitSeconds");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("waitSeconds");
                                    }
                                
                                               if (localWaitSeconds==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("waitSeconds cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWaitSeconds));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPlannedArrivalTimeTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedArrivalTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedArrivalTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedArrivalTime");
                                    }
                                

                                          if (localPlannedArrivalTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedArrivalTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedArrivalTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDistanceToTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"distanceTo", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"distanceTo");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("distanceTo");
                                    }
                                
                                               if (localDistanceTo==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("distanceTo cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistanceTo));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSecondsToTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"secondsTo", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"secondsTo");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("secondsTo");
                                    }
                                
                                               if (localSecondsTo==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("secondsTo cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSecondsTo));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSequencedTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"sequenced", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"sequenced");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("sequenced");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("sequenced cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSequenced));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localReferenceNumberTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"referenceNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"referenceNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("referenceNumber");
                                    }
                                

                                          if (localReferenceNumber==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("referenceNumber cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localReferenceNumber);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localResultTypeTracker){
                                    if (localResultType==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"resultType", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"resultType");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("resultType");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localResultType.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","resultType"),
                                        factory,xmlWriter);
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
                                                                      "identity"));
                            
                            
                                    if (localIdentity==null){
                                         throw new org.apache.axis2.databinding.ADBException("identity cannot be null!!");
                                    }
                                    elementList.add(localIdentity);
                                
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "orderType"));
                                 
                                        if (localOrderType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("orderType cannot be null!!");
                                        }
                                     if (localQuantitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "quantities"));
                            
                            
                                    elementList.add(localQuantities==null?null:
                                    localQuantities);
                                } if (localPickupQuantitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "pickupQuantities"));
                            
                            
                                    elementList.add(localPickupQuantities==null?null:
                                    localPickupQuantities);
                                } if (localConfirmedTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "confirmed"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConfirmed));
                            } if (localReservedTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "reservedTime"));
                                 
                                        if (localReservedTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReservedTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("reservedTime cannot be null!!");
                                        }
                                    } if (localDeliveryWindowStartTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "deliveryWindowStart"));
                                 
                                         elementList.add(localDeliveryWindowStart==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryWindowStart));
                                    } if (localDeliveryWindowEndTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "deliveryWindowEnd"));
                                 
                                         elementList.add(localDeliveryWindowEnd==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryWindowEnd));
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "stopServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStopServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "orderServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "ignoreStopServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIgnoreStopServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "serviceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "latitude"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLatitude));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "longitude"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLongitude));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "locationId"));
                                 
                                        if (localLocationId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationId cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "locationType"));
                                 
                                        if (localLocationType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationType cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "description"));
                                 
                                        if (localDescription != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDescription));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("description cannot be null!!");
                                        }
                                    
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "breakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBreakTime));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "paidBreakTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPaidBreakTime));
                             if (localActivitiesTracker){
                             if (localActivities!=null) {
                                 for (int i = 0;i < localActivities.length;i++){

                                    if (localActivities[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "activities"));
                                         elementList.add(localActivities[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("activities cannot be null!!");
                                    
                             }

                        } if (localMovableTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "movable"));
                                 
                                        if (localMovable != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMovable));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("movable cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "routeId"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteId));
                             if (localSequenceNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "sequenceNumber"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSequenceNumber));
                            } if (localWaitSecondsTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "waitSeconds"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWaitSeconds));
                            } if (localPlannedArrivalTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "plannedArrivalTime"));
                                 
                                        if (localPlannedArrivalTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedArrivalTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedArrivalTime cannot be null!!");
                                        }
                                    } if (localDistanceToTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "distanceTo"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistanceTo));
                            } if (localSecondsToTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "secondsTo"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSecondsTo));
                            } if (localSequencedTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "sequenced"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSequenced));
                            } if (localReferenceNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "referenceNumber"));
                                 
                                        if (localReferenceNumber != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReferenceNumber));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("referenceNumber cannot be null!!");
                                        }
                                    } if (localResultTypeTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "resultType"));
                            
                            
                                    elementList.add(localResultType==null?null:
                                    localResultType);
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
        public static DeliveryAreaOrder parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            DeliveryAreaOrder object =
                new DeliveryAreaOrder();

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
                    
                            if (!"DeliveryAreaOrder".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (DeliveryAreaOrder)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list20 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","identity").equals(reader.getName())){
                                
                                                object.setIdentity(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","orderType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOrderType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","quantities").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setQuantities(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","pickupQuantities").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setPickupQuantities(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setPickupQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","confirmed").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setConfirmed(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","reservedTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setReservedTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","deliveryWindowStart").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDeliveryWindowStart(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","deliveryWindowEnd").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDeliveryWindowEnd(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stopServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStopServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","orderServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOrderServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","ignoreStopServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setIgnoreStopServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","serviceTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","latitude").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLatitude(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","longitude").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLongitude(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","locationId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","locationType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","description").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","breakTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","paidBreakTime").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","activities").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list20.add(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone20 = false;
                                                        while(!loopDone20){
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
                                                                loopDone20 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","activities").equals(reader.getName())){
                                                                    list20.add(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone20 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setActivities((com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity.class,
                                                                list20));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","movable").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMovable(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","routeId").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","sequenceNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSequenceNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setSequenceNumber(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","waitSeconds").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWaitSeconds(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setWaitSeconds(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","plannedArrivalTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedArrivalTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","distanceTo").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDistanceTo(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setDistanceTo(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","secondsTo").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSecondsTo(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setSecondsTo(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","sequenced").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSequenced(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","referenceNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setReferenceNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","resultType").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setResultType(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setResultType(com.freshdirect.routing.proxy.stub.transportation.ReserveResultType.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
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
           
          