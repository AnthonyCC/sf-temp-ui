
/**
 * RoutingImportOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  RoutingImportOrder bean class
            */
        
        public  class RoutingImportOrder
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = RoutingImportOrder
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
                        * field for OrderNumber
                        */

                        
                                    protected java.lang.String localOrderNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOrderNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOrderNumber(){
                               return localOrderNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OrderNumber
                               */
                               public void setOrderNumber(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOrderNumberTracker = true;
                                       } else {
                                          localOrderNumberTracker = false;
                                              
                                       }
                                   
                                            this.localOrderNumber=param;
                                    

                               }
                            

                        /**
                        * field for InternalOrderID
                        */

                        
                                    protected int localInternalOrderID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInternalOrderIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getInternalOrderID(){
                               return localInternalOrderID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InternalOrderID
                               */
                               public void setInternalOrderID(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localInternalOrderIDTracker = false;
                                              
                                       } else {
                                          localInternalOrderIDTracker = true;
                                       }
                                   
                                            this.localInternalOrderID=param;
                                    

                               }
                            

                        /**
                        * field for SessionIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity localSessionIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSessionIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity getSessionIdentity(){
                               return localSessionIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SessionIdentity
                               */
                               public void setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSessionIdentityTracker = true;
                                       } else {
                                          localSessionIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localSessionIdentity=param;
                                    

                               }
                            

                        /**
                        * field for Quantities
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities localQuantities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQuantitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities getQuantities(){
                               return localQuantities;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Quantities
                               */
                               public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localQuantitiesTracker = true;
                                       } else {
                                          localQuantitiesTracker = true;
                                              
                                       }
                                   
                                            this.localQuantities=param;
                                    

                               }
                            

                        /**
                        * field for SpecialInstructions
                        */

                        
                                    protected java.lang.String localSpecialInstructions ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSpecialInstructionsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSpecialInstructions(){
                               return localSpecialInstructions;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SpecialInstructions
                               */
                               public void setSpecialInstructions(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSpecialInstructionsTracker = true;
                                       } else {
                                          localSpecialInstructionsTracker = false;
                                              
                                       }
                                   
                                            this.localSpecialInstructions=param;
                                    

                               }
                            

                        /**
                        * field for PreferredRouteIDOverride
                        */

                        
                                    protected java.lang.String localPreferredRouteIDOverride ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPreferredRouteIDOverrideTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPreferredRouteIDOverride(){
                               return localPreferredRouteIDOverride;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreferredRouteIDOverride
                               */
                               public void setPreferredRouteIDOverride(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPreferredRouteIDOverrideTracker = true;
                                       } else {
                                          localPreferredRouteIDOverrideTracker = false;
                                              
                                       }
                                   
                                            this.localPreferredRouteIDOverride=param;
                                    

                               }
                            

                        /**
                        * field for AdditionalServiceTime
                        */

                        
                                    protected int localAdditionalServiceTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAdditionalServiceTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getAdditionalServiceTime(){
                               return localAdditionalServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AdditionalServiceTime
                               */
                               public void setAdditionalServiceTime(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localAdditionalServiceTimeTracker = false;
                                              
                                       } else {
                                          localAdditionalServiceTimeTracker = true;
                                       }
                                   
                                            this.localAdditionalServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for Pickup
                        */

                        
                                    protected boolean localPickup =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getPickup(){
                               return localPickup;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Pickup
                               */
                               public void setPickup(boolean param){
                            
                                            this.localPickup=param;
                                    

                               }
                            

                        /**
                        * field for DeliveryDate
                        */

                        
                                    protected java.util.Date localDeliveryDate ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDeliveryDateTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Date
                           */
                           public  java.util.Date getDeliveryDate(){
                               return localDeliveryDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DeliveryDate
                               */
                               public void setDeliveryDate(java.util.Date param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDeliveryDateTracker = true;
                                       } else {
                                          localDeliveryDateTracker = false;
                                              
                                       }
                                   
                                            this.localDeliveryDate=param;
                                    

                               }
                            

                        /**
                        * field for DateAdded
                        */

                        
                                    protected java.util.Calendar localDateAdded ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDateAddedTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getDateAdded(){
                               return localDateAdded;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DateAdded
                               */
                               public void setDateAdded(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDateAddedTracker = true;
                                       } else {
                                          localDateAddedTracker = false;
                                              
                                       }
                                   
                                            this.localDateAdded=param;
                                    

                               }
                            

                        /**
                        * field for LocationIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.LocationIdentity localLocationIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocationIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.LocationIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getLocationIdentity(){
                               return localLocationIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationIdentity
                               */
                               public void setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLocationIdentityTracker = true;
                                       } else {
                                          localLocationIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localLocationIdentity=param;
                                    

                               }
                            

                        /**
                        * field for TakenBy
                        */

                        
                                    protected java.lang.String localTakenBy ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTakenByTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getTakenBy(){
                               return localTakenBy;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TakenBy
                               */
                               public void setTakenBy(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTakenByTracker = true;
                                       } else {
                                          localTakenByTracker = false;
                                              
                                       }
                                   
                                            this.localTakenBy=param;
                                    

                               }
                            

                        /**
                        * field for OrderSelector
                        */

                        
                                    protected java.lang.String localOrderSelector ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOrderSelectorTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOrderSelector(){
                               return localOrderSelector;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OrderSelector
                               */
                               public void setOrderSelector(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOrderSelectorTracker = true;
                                       } else {
                                          localOrderSelectorTracker = false;
                                              
                                       }
                                   
                                            this.localOrderSelector=param;
                                    

                               }
                            

                        /**
                        * field for Tw1Open
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localTw1Open ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw1OpenTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getTw1Open(){
                               return localTw1Open;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw1Open
                               */
                               public void setTw1Open(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw1OpenTracker = true;
                                       } else {
                                          localTw1OpenTracker = true;
                                              
                                       }
                                   
                                            this.localTw1Open=param;
                                    

                               }
                            

                        /**
                        * field for Tw1Close
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localTw1Close ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw1CloseTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getTw1Close(){
                               return localTw1Close;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw1Close
                               */
                               public void setTw1Close(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw1CloseTracker = true;
                                       } else {
                                          localTw1CloseTracker = true;
                                              
                                       }
                                   
                                            this.localTw1Close=param;
                                    

                               }
                            

                        /**
                        * field for IsVoid
                        */

                        
                                    protected boolean localIsVoid =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getIsVoid(){
                               return localIsVoid;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param IsVoid
                               */
                               public void setIsVoid(boolean param){
                            
                                            this.localIsVoid=param;
                                    

                               }
                            

                        /**
                        * field for OpenTime
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localOpenTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOpenTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getOpenTime(){
                               return localOpenTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OpenTime
                               */
                               public void setOpenTime(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOpenTimeTracker = true;
                                       } else {
                                          localOpenTimeTracker = true;
                                              
                                       }
                                   
                                            this.localOpenTime=param;
                                    

                               }
                            

                        /**
                        * field for CloseTime
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localCloseTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCloseTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getCloseTime(){
                               return localCloseTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CloseTime
                               */
                               public void setCloseTime(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCloseTimeTracker = true;
                                       } else {
                                          localCloseTimeTracker = true;
                                              
                                       }
                                   
                                            this.localCloseTime=param;
                                    

                               }
                            

                        /**
                        * field for Tw2Open
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localTw2Open ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw2OpenTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getTw2Open(){
                               return localTw2Open;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw2Open
                               */
                               public void setTw2Open(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw2OpenTracker = true;
                                       } else {
                                          localTw2OpenTracker = true;
                                              
                                       }
                                   
                                            this.localTw2Open=param;
                                    

                               }
                            

                        /**
                        * field for Tw2Close
                        */

                        
                                    protected org.apache.axis2.databinding.types.Time localTw2Close ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw2CloseTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.apache.axis2.databinding.types.Time
                           */
                           public  org.apache.axis2.databinding.types.Time getTw2Close(){
                               return localTw2Close;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw2Close
                               */
                               public void setTw2Close(org.apache.axis2.databinding.types.Time param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw2CloseTracker = true;
                                       } else {
                                          localTw2CloseTracker = true;
                                              
                                       }
                                   
                                            this.localTw2Close=param;
                                    

                               }
                            

                        /**
                        * field for ForceBulkServiceTime
                        */

                        
                                    protected boolean localForceBulkServiceTime =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getForceBulkServiceTime(){
                               return localForceBulkServiceTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ForceBulkServiceTime
                               */
                               public void setForceBulkServiceTime(boolean param){
                            
                                            this.localForceBulkServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for UserDefinedFields
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields localUserDefinedFields ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserDefinedFieldsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields getUserDefinedFields(){
                               return localUserDefinedFields;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserDefinedFields
                               */
                               public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUserDefinedFieldsTracker = true;
                                       } else {
                                          localUserDefinedFieldsTracker = true;
                                              
                                       }
                                   
                                            this.localUserDefinedFields=param;
                                    

                               }
                            

                        /**
                        * field for DailyQuantityIncrease
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Quantities localDailyQuantityIncrease ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDailyQuantityIncreaseTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Quantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Quantities getDailyQuantityIncrease(){
                               return localDailyQuantityIncrease;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DailyQuantityIncrease
                               */
                               public void setDailyQuantityIncrease(com.freshdirect.routing.proxy.stub.transportation.Quantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDailyQuantityIncreaseTracker = true;
                                       } else {
                                          localDailyQuantityIncreaseTracker = true;
                                              
                                       }
                                   
                                            this.localDailyQuantityIncrease=param;
                                    

                               }
                            

                        /**
                        * field for BeginDate
                        */

                        
                                    protected java.util.Date localBeginDate ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localBeginDateTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Date
                           */
                           public  java.util.Date getBeginDate(){
                               return localBeginDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param BeginDate
                               */
                               public void setBeginDate(java.util.Date param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localBeginDateTracker = true;
                                       } else {
                                          localBeginDateTracker = false;
                                              
                                       }
                                   
                                            this.localBeginDate=param;
                                    

                               }
                            

                        /**
                        * field for EndDate
                        */

                        
                                    protected java.util.Date localEndDate ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEndDateTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Date
                           */
                           public  java.util.Date getEndDate(){
                               return localEndDate;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EndDate
                               */
                               public void setEndDate(java.util.Date param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEndDateTracker = true;
                                       } else {
                                          localEndDateTracker = false;
                                              
                                       }
                                   
                                            this.localEndDate=param;
                                    

                               }
                            

                        /**
                        * field for Urgency
                        */

                        
                                    protected int localUrgency =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt("0");
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getUrgency(){
                               return localUrgency;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Urgency
                               */
                               public void setUrgency(int param){
                            
                                            this.localUrgency=param;
                                    

                               }
                            

                        /**
                        * field for DailyUrgencyIncrease
                        */

                        
                                    protected int localDailyUrgencyIncrease =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt("0");
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDailyUrgencyIncrease(){
                               return localDailyUrgencyIncrease;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DailyUrgencyIncrease
                               */
                               public void setDailyUrgencyIncrease(int param){
                            
                                            this.localDailyUrgencyIncrease=param;
                                    

                               }
                            

                        /**
                        * field for PreferredOriginIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.LocationIdentity localPreferredOriginIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPreferredOriginIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.LocationIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getPreferredOriginIdentity(){
                               return localPreferredOriginIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreferredOriginIdentity
                               */
                               public void setPreferredOriginIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPreferredOriginIdentityTracker = true;
                                       } else {
                                          localPreferredOriginIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localPreferredOriginIdentity=param;
                                    

                               }
                            

                        /**
                        * field for RejectCode
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode localRejectCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRejectCodeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode getRejectCode(){
                               return localRejectCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RejectCode
                               */
                               public void setRejectCode(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRejectCodeTracker = true;
                                       } else {
                                          localRejectCodeTracker = true;
                                              
                                       }
                                   
                                            this.localRejectCode=param;
                                    

                               }
                            

                        /**
                        * field for LineItems
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] localLineItems ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLineItemsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] getLineItems(){
                               return localLineItems;
                           }

                           
                        


                               
                              /**
                               * validate the array for LineItems
                               */
                              protected void validateLineItems(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param LineItems
                              */
                              public void setLineItems(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[] param){
                              
                                   validateLineItems(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localLineItemsTracker = true;
                                          } else {
                                             localLineItemsTracker = false;
                                                 
                                          }
                                      
                                      this.localLineItems=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem
                             */
                             public void addLineItems(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem param){
                                   if (localLineItems == null){
                                   localLineItems = new com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[]{};
                                   }

                            
                                 //update the setting tracker
                                localLineItemsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localLineItems);
                               list.add(param);
                               this.localLineItems =
                             (com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[list.size()]);

                             }
                             

                        /**
                        * field for OrderActivities
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] localOrderActivities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOrderActivitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] getOrderActivities(){
                               return localOrderActivities;
                           }

                           
                        


                               
                              /**
                               * validate the array for OrderActivities
                               */
                              protected void validateOrderActivities(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param OrderActivities
                              */
                              public void setOrderActivities(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[] param){
                              
                                   validateOrderActivities(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localOrderActivitiesTracker = true;
                                          } else {
                                             localOrderActivitiesTracker = false;
                                                 
                                          }
                                      
                                      this.localOrderActivities=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity
                             */
                             public void addOrderActivities(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity param){
                                   if (localOrderActivities == null){
                                   localOrderActivities = new com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[]{};
                                   }

                            
                                 //update the setting tracker
                                localOrderActivitiesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localOrderActivities);
                               list.add(param);
                               this.localOrderActivities =
                             (com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[list.size()]);

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
                       RoutingImportOrder.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":RoutingImportOrder",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "RoutingImportOrder",
                           xmlWriter);
                   }

               
                   }
                if (localOrderNumberTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"orderNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"orderNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("orderNumber");
                                    }
                                

                                          if (localOrderNumber==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("orderNumber cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOrderNumber);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localInternalOrderIDTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"internalOrderID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"internalOrderID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("internalOrderID");
                                    }
                                
                                               if (localInternalOrderID==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("internalOrderID cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInternalOrderID));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSessionIdentityTracker){
                                    if (localSessionIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"sessionIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"sessionIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("sessionIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localSessionIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","sessionIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localQuantitiesTracker){
                                    if (localQuantities==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

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
                                     localQuantities.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","quantities"),
                                        factory,xmlWriter);
                                    }
                                } if (localSpecialInstructionsTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"specialInstructions", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"specialInstructions");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("specialInstructions");
                                    }
                                

                                          if (localSpecialInstructions==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("specialInstructions cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSpecialInstructions);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPreferredRouteIDOverrideTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"preferredRouteIDOverride", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"preferredRouteIDOverride");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("preferredRouteIDOverride");
                                    }
                                

                                          if (localPreferredRouteIDOverride==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("preferredRouteIDOverride cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPreferredRouteIDOverride);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAdditionalServiceTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"additionalServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"additionalServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("additionalServiceTime");
                                    }
                                
                                               if (localAdditionalServiceTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("additionalServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAdditionalServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"pickup", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"pickup");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("pickup");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("pickup cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPickup));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localDeliveryDateTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"deliveryDate", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"deliveryDate");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("deliveryDate");
                                    }
                                

                                          if (localDeliveryDate==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("deliveryDate cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryDate));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDateAddedTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"dateAdded", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"dateAdded");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("dateAdded");
                                    }
                                

                                          if (localDateAdded==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("dateAdded cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDateAdded));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLocationIdentityTracker){
                                    if (localLocationIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"locationIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"locationIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("locationIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localLocationIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localTakenByTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"takenBy", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"takenBy");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("takenBy");
                                    }
                                

                                          if (localTakenBy==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("takenBy cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localTakenBy);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOrderSelectorTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"orderSelector", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"orderSelector");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("orderSelector");
                                    }
                                

                                          if (localOrderSelector==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("orderSelector cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOrderSelector);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw1OpenTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw1Open", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw1Open");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw1Open");
                                    }
                                

                                          if (localTw1Open==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1Open));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw1CloseTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw1Close", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw1Close");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw1Close");
                                    }
                                

                                          if (localTw1Close==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1Close));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"isVoid", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"isVoid");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("isVoid");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("isVoid cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsVoid));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localOpenTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"openTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"openTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("openTime");
                                    }
                                

                                          if (localOpenTime==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOpenTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localCloseTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"closeTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"closeTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("closeTime");
                                    }
                                

                                          if (localCloseTime==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCloseTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw2OpenTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw2Open", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw2Open");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw2Open");
                                    }
                                

                                          if (localTw2Open==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2Open));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw2CloseTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw2Close", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw2Close");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw2Close");
                                    }
                                

                                          if (localTw2Close==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2Close));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"forceBulkServiceTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"forceBulkServiceTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("forceBulkServiceTime");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("forceBulkServiceTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForceBulkServiceTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localUserDefinedFieldsTracker){
                                    if (localUserDefinedFields==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"userDefinedFields", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"userDefinedFields");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("userDefinedFields");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localUserDefinedFields.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","userDefinedFields"),
                                        factory,xmlWriter);
                                    }
                                } if (localDailyQuantityIncreaseTracker){
                                    if (localDailyQuantityIncrease==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"dailyQuantityIncrease", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"dailyQuantityIncrease");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("dailyQuantityIncrease");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localDailyQuantityIncrease.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dailyQuantityIncrease"),
                                        factory,xmlWriter);
                                    }
                                } if (localBeginDateTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"beginDate", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"beginDate");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("beginDate");
                                    }
                                

                                          if (localBeginDate==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("beginDate cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBeginDate));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEndDateTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"endDate", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"endDate");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("endDate");
                                    }
                                

                                          if (localEndDate==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("endDate cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEndDate));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"urgency", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"urgency");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("urgency");
                                    }
                                
                                               if (localUrgency==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("urgency cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUrgency));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"dailyUrgencyIncrease", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"dailyUrgencyIncrease");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("dailyUrgencyIncrease");
                                    }
                                
                                               if (localDailyUrgencyIncrease==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("dailyUrgencyIncrease cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDailyUrgencyIncrease));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localPreferredOriginIdentityTracker){
                                    if (localPreferredOriginIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"preferredOriginIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"preferredOriginIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("preferredOriginIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localPreferredOriginIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preferredOriginIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localRejectCodeTracker){
                                    if (localRejectCode==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"rejectCode", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"rejectCode");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("rejectCode");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localRejectCode.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","rejectCode"),
                                        factory,xmlWriter);
                                    }
                                } if (localLineItemsTracker){
                                       if (localLineItems!=null){
                                            for (int i = 0;i < localLineItems.length;i++){
                                                if (localLineItems[i] != null){
                                                 localLineItems[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","lineItems"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("lineItems cannot be null!!");
                                        
                                    }
                                 } if (localOrderActivitiesTracker){
                                       if (localOrderActivities!=null){
                                            for (int i = 0;i < localOrderActivities.length;i++){
                                                if (localOrderActivities[i] != null){
                                                 localOrderActivities[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orderActivities"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("orderActivities cannot be null!!");
                                        
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

                 if (localOrderNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "orderNumber"));
                                 
                                        if (localOrderNumber != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderNumber));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("orderNumber cannot be null!!");
                                        }
                                    } if (localInternalOrderIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "internalOrderID"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInternalOrderID));
                            } if (localSessionIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "sessionIdentity"));
                            
                            
                                    elementList.add(localSessionIdentity==null?null:
                                    localSessionIdentity);
                                } if (localQuantitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "quantities"));
                            
                            
                                    elementList.add(localQuantities==null?null:
                                    localQuantities);
                                } if (localSpecialInstructionsTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "specialInstructions"));
                                 
                                        if (localSpecialInstructions != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSpecialInstructions));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("specialInstructions cannot be null!!");
                                        }
                                    } if (localPreferredRouteIDOverrideTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "preferredRouteIDOverride"));
                                 
                                        if (localPreferredRouteIDOverride != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreferredRouteIDOverride));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("preferredRouteIDOverride cannot be null!!");
                                        }
                                    } if (localAdditionalServiceTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "additionalServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAdditionalServiceTime));
                            }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "pickup"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPickup));
                             if (localDeliveryDateTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "deliveryDate"));
                                 
                                        if (localDeliveryDate != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeliveryDate));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("deliveryDate cannot be null!!");
                                        }
                                    } if (localDateAddedTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "dateAdded"));
                                 
                                        if (localDateAdded != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDateAdded));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("dateAdded cannot be null!!");
                                        }
                                    } if (localLocationIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationIdentity"));
                            
                            
                                    elementList.add(localLocationIdentity==null?null:
                                    localLocationIdentity);
                                } if (localTakenByTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "takenBy"));
                                 
                                        if (localTakenBy != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTakenBy));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("takenBy cannot be null!!");
                                        }
                                    } if (localOrderSelectorTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "orderSelector"));
                                 
                                        if (localOrderSelector != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderSelector));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("orderSelector cannot be null!!");
                                        }
                                    } if (localTw1OpenTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw1Open"));
                                 
                                         elementList.add(localTw1Open==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1Open));
                                    } if (localTw1CloseTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw1Close"));
                                 
                                         elementList.add(localTw1Close==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1Close));
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "isVoid"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsVoid));
                             if (localOpenTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "openTime"));
                                 
                                         elementList.add(localOpenTime==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOpenTime));
                                    } if (localCloseTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "closeTime"));
                                 
                                         elementList.add(localCloseTime==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCloseTime));
                                    } if (localTw2OpenTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw2Open"));
                                 
                                         elementList.add(localTw2Open==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2Open));
                                    } if (localTw2CloseTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw2Close"));
                                 
                                         elementList.add(localTw2Close==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2Close));
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "forceBulkServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForceBulkServiceTime));
                             if (localUserDefinedFieldsTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "userDefinedFields"));
                            
                            
                                    elementList.add(localUserDefinedFields==null?null:
                                    localUserDefinedFields);
                                } if (localDailyQuantityIncreaseTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "dailyQuantityIncrease"));
                            
                            
                                    elementList.add(localDailyQuantityIncrease==null?null:
                                    localDailyQuantityIncrease);
                                } if (localBeginDateTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "beginDate"));
                                 
                                        if (localBeginDate != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBeginDate));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("beginDate cannot be null!!");
                                        }
                                    } if (localEndDateTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "endDate"));
                                 
                                        if (localEndDate != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEndDate));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("endDate cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "urgency"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUrgency));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "dailyUrgencyIncrease"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDailyUrgencyIncrease));
                             if (localPreferredOriginIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "preferredOriginIdentity"));
                            
                            
                                    elementList.add(localPreferredOriginIdentity==null?null:
                                    localPreferredOriginIdentity);
                                } if (localRejectCodeTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "rejectCode"));
                            
                            
                                    elementList.add(localRejectCode==null?null:
                                    localRejectCode);
                                } if (localLineItemsTracker){
                             if (localLineItems!=null) {
                                 for (int i = 0;i < localLineItems.length;i++){

                                    if (localLineItems[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "lineItems"));
                                         elementList.add(localLineItems[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("lineItems cannot be null!!");
                                    
                             }

                        } if (localOrderActivitiesTracker){
                             if (localOrderActivities!=null) {
                                 for (int i = 0;i < localOrderActivities.length;i++){

                                    if (localOrderActivities[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "orderActivities"));
                                         elementList.add(localOrderActivities[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("orderActivities cannot be null!!");
                                    
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
        public static RoutingImportOrder parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            RoutingImportOrder object =
                new RoutingImportOrder();

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
                    
                            if (!"RoutingImportOrder".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RoutingImportOrder)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list30 = new java.util.ArrayList();
                    
                        java.util.ArrayList list31 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orderNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOrderNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","internalOrderID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInternalOrderID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setInternalOrderID(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","sessionIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setSessionIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","quantities").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setQuantities(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setQuantities(com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","specialInstructions").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSpecialInstructions(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preferredRouteIDOverride").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPreferredRouteIDOverride(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","additionalServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAdditionalServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setAdditionalServiceTime(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","pickup").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPickup(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","deliveryDate").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDeliveryDate(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDate(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dateAdded").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDateAdded(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setLocationIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","takenBy").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTakenBy(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orderSelector").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOrderSelector(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw1Open").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw1Open(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw1Close").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw1Close(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","isVoid").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setIsVoid(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","openTime").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOpenTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","closeTime").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCloseTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw2Open").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw2Open(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw2Close").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw2Close(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","forceBulkServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setForceBulkServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","userDefinedFields").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setUserDefinedFields(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dailyQuantityIncrease").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setDailyQuantityIncrease(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setDailyQuantityIncrease(com.freshdirect.routing.proxy.stub.transportation.Quantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","beginDate").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setBeginDate(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDate(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","endDate").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEndDate(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDate(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","urgency").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUrgency(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","dailyUrgencyIncrease").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDailyUrgencyIncrease(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","preferredOriginIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setPreferredOriginIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setPreferredOriginIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","rejectCode").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRejectCode(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRejectCode(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderRejectCode.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","lineItems").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list30.add(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone30 = false;
                                                        while(!loopDone30){
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
                                                                loopDone30 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","lineItems").equals(reader.getName())){
                                                                    list30.add(com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone30 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setLineItems((com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.RoutingImportLineItem.class,
                                                                list30));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orderActivities").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list31.add(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone31 = false;
                                                        while(!loopDone31){
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
                                                                loopDone31 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orderActivities").equals(reader.getName())){
                                                                    list31.add(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone31 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setOrderActivities((com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderActivity.class,
                                                                list31));
                                                            
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
           
          