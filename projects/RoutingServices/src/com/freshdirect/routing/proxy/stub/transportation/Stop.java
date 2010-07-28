
/**
 * Stop.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  Stop bean class
            */
        
        public  class Stop
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = Stop
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
                        * field for StopIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.StopIdentity localStopIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStopIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.StopIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.StopIdentity getStopIdentity(){
                               return localStopIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StopIdentity
                               */
                               public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localStopIdentityTracker = true;
                                       } else {
                                          localStopIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localStopIdentity=param;
                                    

                               }
                            

                        /**
                        * field for StopIndex
                        */

                        
                                    protected int localStopIndex ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getStopIndex(){
                               return localStopIndex;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StopIndex
                               */
                               public void setStopIndex(int param){
                            
                                            this.localStopIndex=param;
                                    

                               }
                            

                        /**
                        * field for PlannedSequenceNum
                        */

                        
                                    protected int localPlannedSequenceNum ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPlannedSequenceNum(){
                               return localPlannedSequenceNum;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedSequenceNum
                               */
                               public void setPlannedSequenceNum(int param){
                            
                                            this.localPlannedSequenceNum=param;
                                    

                               }
                            

                        /**
                        * field for ActualSequenceNum
                        */

                        
                                    protected int localActualSequenceNum ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getActualSequenceNum(){
                               return localActualSequenceNum;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualSequenceNum
                               */
                               public void setActualSequenceNum(int param){
                            
                                            this.localActualSequenceNum=param;
                                    

                               }
                            

                        /**
                        * field for StopType
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.StopType localStopType ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.StopType
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.StopType getStopType(){
                               return localStopType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StopType
                               */
                               public void setStopType(com.freshdirect.routing.proxy.stub.transportation.StopType param){
                            
                                            this.localStopType=param;
                                    

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
                        * field for BuildingDeliverySequence
                        */

                        
                                    protected int localBuildingDeliverySequence ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getBuildingDeliverySequence(){
                               return localBuildingDeliverySequence;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param BuildingDeliverySequence
                               */
                               public void setBuildingDeliverySequence(int param){
                            
                                            this.localBuildingDeliverySequence=param;
                                    

                               }
                            

                        /**
                        * field for Consignee
                        */

                        
                                    protected java.lang.String localConsignee ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localConsigneeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getConsignee(){
                               return localConsignee;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Consignee
                               */
                               public void setConsignee(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localConsigneeTracker = true;
                                       } else {
                                          localConsigneeTracker = false;
                                              
                                       }
                                   
                                            this.localConsignee=param;
                                    

                               }
                            

                        /**
                        * field for Shipper
                        */

                        
                                    protected java.lang.String localShipper ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localShipperTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getShipper(){
                               return localShipper;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Shipper
                               */
                               public void setShipper(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localShipperTracker = true;
                                       } else {
                                          localShipperTracker = false;
                                              
                                       }
                                   
                                            this.localShipper=param;
                                    

                               }
                            

                        /**
                        * field for PlannedArrival
                        */

                        
                                    protected java.util.Calendar localPlannedArrival ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPlannedArrivalTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedArrival(){
                               return localPlannedArrival;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedArrival
                               */
                               public void setPlannedArrival(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPlannedArrivalTracker = true;
                                       } else {
                                          localPlannedArrivalTracker = false;
                                              
                                       }
                                   
                                            this.localPlannedArrival=param;
                                    

                               }
                            

                        /**
                        * field for ProjectedArrival
                        */

                        
                                    protected java.util.Calendar localProjectedArrival ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localProjectedArrivalTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getProjectedArrival(){
                               return localProjectedArrival;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ProjectedArrival
                               */
                               public void setProjectedArrival(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localProjectedArrivalTracker = true;
                                       } else {
                                          localProjectedArrivalTracker = false;
                                              
                                       }
                                   
                                            this.localProjectedArrival=param;
                                    

                               }
                            

                        /**
                        * field for ActualArrival
                        */

                        
                                    protected java.util.Calendar localActualArrival ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualArrivalTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualArrival(){
                               return localActualArrival;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualArrival
                               */
                               public void setActualArrival(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualArrivalTracker = true;
                                       } else {
                                          localActualArrivalTracker = false;
                                              
                                       }
                                   
                                            this.localActualArrival=param;
                                    

                               }
                            

                        /**
                        * field for PlannedDeparture
                        */

                        
                                    protected java.util.Calendar localPlannedDeparture ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPlannedDepartureTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getPlannedDeparture(){
                               return localPlannedDeparture;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedDeparture
                               */
                               public void setPlannedDeparture(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPlannedDepartureTracker = true;
                                       } else {
                                          localPlannedDepartureTracker = false;
                                              
                                       }
                                   
                                            this.localPlannedDeparture=param;
                                    

                               }
                            

                        /**
                        * field for ProjectedDeparture
                        */

                        
                                    protected java.util.Calendar localProjectedDeparture ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localProjectedDepartureTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getProjectedDeparture(){
                               return localProjectedDeparture;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ProjectedDeparture
                               */
                               public void setProjectedDeparture(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localProjectedDepartureTracker = true;
                                       } else {
                                          localProjectedDepartureTracker = false;
                                              
                                       }
                                   
                                            this.localProjectedDeparture=param;
                                    

                               }
                            

                        /**
                        * field for ActualDeparture
                        */

                        
                                    protected java.util.Calendar localActualDeparture ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActualDepartureTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getActualDeparture(){
                               return localActualDeparture;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDeparture
                               */
                               public void setActualDeparture(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localActualDepartureTracker = true;
                                       } else {
                                          localActualDepartureTracker = false;
                                              
                                       }
                                   
                                            this.localActualDeparture=param;
                                    

                               }
                            

                        /**
                        * field for Cancelled
                        */

                        
                                    protected boolean localCancelled ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getCancelled(){
                               return localCancelled;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Cancelled
                               */
                               public void setCancelled(boolean param){
                            
                                            this.localCancelled=param;
                                    

                               }
                            

                        /**
                        * field for PlannedDistance
                        */

                        
                                    protected double localPlannedDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getPlannedDistance(){
                               return localPlannedDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedDistance
                               */
                               public void setPlannedDistance(double param){
                            
                                            this.localPlannedDistance=param;
                                    

                               }
                            

                        /**
                        * field for ProjectedDistance
                        */

                        
                                    protected double localProjectedDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getProjectedDistance(){
                               return localProjectedDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ProjectedDistance
                               */
                               public void setProjectedDistance(double param){
                            
                                            this.localProjectedDistance=param;
                                    

                               }
                            

                        /**
                        * field for ActualDistance
                        */

                        
                                    protected double localActualDistance ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getActualDistance(){
                               return localActualDistance;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDistance
                               */
                               public void setActualDistance(double param){
                            
                                            this.localActualDistance=param;
                                    

                               }
                            

                        /**
                        * field for OpenTime
                        */

                        
                                    protected java.util.Calendar localOpenTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOpenTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getOpenTime(){
                               return localOpenTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OpenTime
                               */
                               public void setOpenTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOpenTimeTracker = true;
                                       } else {
                                          localOpenTimeTracker = false;
                                              
                                       }
                                   
                                            this.localOpenTime=param;
                                    

                               }
                            

                        /**
                        * field for CloseTime
                        */

                        
                                    protected java.util.Calendar localCloseTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCloseTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getCloseTime(){
                               return localCloseTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CloseTime
                               */
                               public void setCloseTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCloseTimeTracker = true;
                                       } else {
                                          localCloseTimeTracker = false;
                                              
                                       }
                                   
                                            this.localCloseTime=param;
                                    

                               }
                            

                        /**
                        * field for Tw1OpenTime
                        */

                        
                                    protected java.util.Calendar localTw1OpenTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw1OpenTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getTw1OpenTime(){
                               return localTw1OpenTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw1OpenTime
                               */
                               public void setTw1OpenTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw1OpenTimeTracker = true;
                                       } else {
                                          localTw1OpenTimeTracker = false;
                                              
                                       }
                                   
                                            this.localTw1OpenTime=param;
                                    

                               }
                            

                        /**
                        * field for Tw1CloseTime
                        */

                        
                                    protected java.util.Calendar localTw1CloseTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw1CloseTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getTw1CloseTime(){
                               return localTw1CloseTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw1CloseTime
                               */
                               public void setTw1CloseTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw1CloseTimeTracker = true;
                                       } else {
                                          localTw1CloseTimeTracker = false;
                                              
                                       }
                                   
                                            this.localTw1CloseTime=param;
                                    

                               }
                            

                        /**
                        * field for Tw2OpenTime
                        */

                        
                                    protected java.util.Calendar localTw2OpenTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw2OpenTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getTw2OpenTime(){
                               return localTw2OpenTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw2OpenTime
                               */
                               public void setTw2OpenTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw2OpenTimeTracker = true;
                                       } else {
                                          localTw2OpenTimeTracker = false;
                                              
                                       }
                                   
                                            this.localTw2OpenTime=param;
                                    

                               }
                            

                        /**
                        * field for Tw2CloseTime
                        */

                        
                                    protected java.util.Calendar localTw2CloseTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTw2CloseTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getTw2CloseTime(){
                               return localTw2CloseTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Tw2CloseTime
                               */
                               public void setTw2CloseTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTw2CloseTimeTracker = true;
                                       } else {
                                          localTw2CloseTimeTracker = false;
                                              
                                       }
                                   
                                            this.localTw2CloseTime=param;
                                    

                               }
                            

                        /**
                        * field for DelayType
                        */

                        
                                    protected java.lang.String localDelayType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDelayTypeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDelayType(){
                               return localDelayType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DelayType
                               */
                               public void setDelayType(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDelayTypeTracker = true;
                                       } else {
                                          localDelayTypeTracker = false;
                                              
                                       }
                                   
                                            this.localDelayType=param;
                                    

                               }
                            

                        /**
                        * field for DelayMinutes
                        */

                        
                                    protected int localDelayMinutes ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDelayMinutes(){
                               return localDelayMinutes;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DelayMinutes
                               */
                               public void setDelayMinutes(int param){
                            
                                            this.localDelayMinutes=param;
                                    

                               }
                            

                        /**
                        * field for Seal
                        */

                        
                                    protected java.lang.String localSeal ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSealTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSeal(){
                               return localSeal;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Seal
                               */
                               public void setSeal(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSealTracker = true;
                                       } else {
                                          localSealTracker = false;
                                              
                                       }
                                   
                                            this.localSeal=param;
                                    

                               }
                            

                        /**
                        * field for UserDefinedField1
                        */

                        
                                    protected java.lang.String localUserDefinedField1 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserDefinedField1Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUserDefinedField1(){
                               return localUserDefinedField1;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserDefinedField1
                               */
                               public void setUserDefinedField1(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUserDefinedField1Tracker = true;
                                       } else {
                                          localUserDefinedField1Tracker = false;
                                              
                                       }
                                   
                                            this.localUserDefinedField1=param;
                                    

                               }
                            

                        /**
                        * field for UserDefinedField2
                        */

                        
                                    protected java.lang.String localUserDefinedField2 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserDefinedField2Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUserDefinedField2(){
                               return localUserDefinedField2;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserDefinedField2
                               */
                               public void setUserDefinedField2(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUserDefinedField2Tracker = true;
                                       } else {
                                          localUserDefinedField2Tracker = false;
                                              
                                       }
                                   
                                            this.localUserDefinedField2=param;
                                    

                               }
                            

                        /**
                        * field for UserDefinedField3
                        */

                        
                                    protected java.lang.String localUserDefinedField3 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserDefinedField3Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUserDefinedField3(){
                               return localUserDefinedField3;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserDefinedField3
                               */
                               public void setUserDefinedField3(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUserDefinedField3Tracker = true;
                                       } else {
                                          localUserDefinedField3Tracker = false;
                                              
                                       }
                                   
                                            this.localUserDefinedField3=param;
                                    

                               }
                            

                        /**
                        * field for LocationUserDefinedField1
                        */

                        
                                    protected java.lang.String localLocationUserDefinedField1 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocationUserDefinedField1Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationUserDefinedField1(){
                               return localLocationUserDefinedField1;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationUserDefinedField1
                               */
                               public void setLocationUserDefinedField1(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLocationUserDefinedField1Tracker = true;
                                       } else {
                                          localLocationUserDefinedField1Tracker = false;
                                              
                                       }
                                   
                                            this.localLocationUserDefinedField1=param;
                                    

                               }
                            

                        /**
                        * field for LocationUserDefinedField2
                        */

                        
                                    protected java.lang.String localLocationUserDefinedField2 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocationUserDefinedField2Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationUserDefinedField2(){
                               return localLocationUserDefinedField2;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationUserDefinedField2
                               */
                               public void setLocationUserDefinedField2(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLocationUserDefinedField2Tracker = true;
                                       } else {
                                          localLocationUserDefinedField2Tracker = false;
                                              
                                       }
                                   
                                            this.localLocationUserDefinedField2=param;
                                    

                               }
                            

                        /**
                        * field for LocationUserDefinedField3
                        */

                        
                                    protected java.lang.String localLocationUserDefinedField3 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocationUserDefinedField3Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationUserDefinedField3(){
                               return localLocationUserDefinedField3;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationUserDefinedField3
                               */
                               public void setLocationUserDefinedField3(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLocationUserDefinedField3Tracker = true;
                                       } else {
                                          localLocationUserDefinedField3Tracker = false;
                                              
                                       }
                                   
                                            this.localLocationUserDefinedField3=param;
                                    

                               }
                            

                        /**
                        * field for TimeZone
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue localTimeZone ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getTimeZone(){
                               return localTimeZone;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TimeZone
                               */
                               public void setTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue param){
                            
                                            this.localTimeZone=param;
                                    

                               }
                            

                        /**
                        * field for Instructions
                        */

                        
                                    protected java.lang.String localInstructions ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInstructionsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getInstructions(){
                               return localInstructions;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Instructions
                               */
                               public void setInstructions(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localInstructionsTracker = true;
                                       } else {
                                          localInstructionsTracker = false;
                                              
                                       }
                                   
                                            this.localInstructions=param;
                                    

                               }
                            

                        /**
                        * field for RedeliveryID
                        */

                        
                                    protected int localRedeliveryID ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getRedeliveryID(){
                               return localRedeliveryID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RedeliveryID
                               */
                               public void setRedeliveryID(int param){
                            
                                            this.localRedeliveryID=param;
                                    

                               }
                            

                        /**
                        * field for CancelCode
                        */

                        
                                    protected java.lang.String localCancelCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCancelCodeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getCancelCode(){
                               return localCancelCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param CancelCode
                               */
                               public void setCancelCode(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCancelCodeTracker = true;
                                       } else {
                                          localCancelCodeTracker = false;
                                              
                                       }
                                   
                                            this.localCancelCode=param;
                                    

                               }
                            

                        /**
                        * field for UndeliverableCode
                        */

                        
                                    protected java.lang.String localUndeliverableCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUndeliverableCodeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getUndeliverableCode(){
                               return localUndeliverableCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UndeliverableCode
                               */
                               public void setUndeliverableCode(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localUndeliverableCodeTracker = true;
                                       } else {
                                          localUndeliverableCodeTracker = false;
                                              
                                       }
                                   
                                            this.localUndeliverableCode=param;
                                    

                               }
                            

                        /**
                        * field for Undeliverable
                        */

                        
                                    protected boolean localUndeliverable ;
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getUndeliverable(){
                               return localUndeliverable;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Undeliverable
                               */
                               public void setUndeliverable(boolean param){
                            
                                            this.localUndeliverable=param;
                                    

                               }
                            

                        /**
                        * field for AdditionalServiceTime
                        */

                        
                                    protected int localAdditionalServiceTime ;
                                

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
                            
                                            this.localAdditionalServiceTime=param;
                                    

                               }
                            

                        /**
                        * field for Quantities
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.ItemQuantities localQuantities ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQuantitiesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.ItemQuantities
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.ItemQuantities getQuantities(){
                               return localQuantities;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Quantities
                               */
                               public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.ItemQuantities param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localQuantitiesTracker = true;
                                       } else {
                                          localQuantitiesTracker = true;
                                              
                                       }
                                   
                                            this.localQuantities=param;
                                    

                               }
                            

                        /**
                        * field for ReasonCodes
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.ReasonCodes localReasonCodes ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localReasonCodesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.ReasonCodes
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.ReasonCodes getReasonCodes(){
                               return localReasonCodes;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ReasonCodes
                               */
                               public void setReasonCodes(com.freshdirect.routing.proxy.stub.transportation.ReasonCodes param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localReasonCodesTracker = true;
                                       } else {
                                          localReasonCodesTracker = true;
                                              
                                       }
                                   
                                            this.localReasonCodes=param;
                                    

                               }
                            

                        /**
                        * field for LocationName
                        */

                        
                                    protected java.lang.String localLocationName ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocationNameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLocationName(){
                               return localLocationName;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LocationName
                               */
                               public void setLocationName(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLocationNameTracker = true;
                                       } else {
                                          localLocationNameTracker = false;
                                              
                                       }
                                   
                                            this.localLocationName=param;
                                    

                               }
                            

                        /**
                        * field for Address
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Address localAddress ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAddressTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Address
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Address getAddress(){
                               return localAddress;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Address
                               */
                               public void setAddress(com.freshdirect.routing.proxy.stub.transportation.Address param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localAddressTracker = true;
                                       } else {
                                          localAddressTracker = true;
                                              
                                       }
                                   
                                            this.localAddress=param;
                                    

                               }
                            

                        /**
                        * field for PhoneNumber
                        */

                        
                                    protected java.lang.String localPhoneNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPhoneNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPhoneNumber(){
                               return localPhoneNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PhoneNumber
                               */
                               public void setPhoneNumber(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPhoneNumberTracker = true;
                                       } else {
                                          localPhoneNumberTracker = false;
                                              
                                       }
                                   
                                            this.localPhoneNumber=param;
                                    

                               }
                            

                        /**
                        * field for Driver1Name
                        */

                        
                                    protected java.lang.String localDriver1Name ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriver1NameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDriver1Name(){
                               return localDriver1Name;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Driver1Name
                               */
                               public void setDriver1Name(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDriver1NameTracker = true;
                                       } else {
                                          localDriver1NameTracker = false;
                                              
                                       }
                                   
                                            this.localDriver1Name=param;
                                    

                               }
                            

                        /**
                        * field for Driver2Name
                        */

                        
                                    protected java.lang.String localDriver2Name ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriver2NameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDriver2Name(){
                               return localDriver2Name;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Driver2Name
                               */
                               public void setDriver2Name(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDriver2NameTracker = true;
                                       } else {
                                          localDriver2NameTracker = false;
                                              
                                       }
                                   
                                            this.localDriver2Name=param;
                                    

                               }
                            

                        /**
                        * field for RouteComplete
                        */

                        
                                    protected boolean localRouteComplete =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getRouteComplete(){
                               return localRouteComplete;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteComplete
                               */
                               public void setRouteComplete(boolean param){
                            
                                            this.localRouteComplete=param;
                                    

                               }
                            

                        /**
                        * field for RouteInProgress
                        */

                        
                                    protected boolean localRouteInProgress =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getRouteInProgress(){
                               return localRouteInProgress;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RouteInProgress
                               */
                               public void setRouteInProgress(boolean param){
                            
                                            this.localRouteInProgress=param;
                                    

                               }
                            

                        /**
                        * field for PlannedDeliveryCost
                        */

                        
                                    protected double localPlannedDeliveryCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getPlannedDeliveryCost(){
                               return localPlannedDeliveryCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PlannedDeliveryCost
                               */
                               public void setPlannedDeliveryCost(double param){
                            
                                            this.localPlannedDeliveryCost=param;
                                    

                               }
                            

                        /**
                        * field for ActualDeliveryCost
                        */

                        
                                    protected double localActualDeliveryCost ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getActualDeliveryCost(){
                               return localActualDeliveryCost;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ActualDeliveryCost
                               */
                               public void setActualDeliveryCost(double param){
                            
                                            this.localActualDeliveryCost=param;
                                    

                               }
                            

                        /**
                        * field for SurveyIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity localSurveyIdentity ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSurveyIdentityTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity getSurveyIdentity(){
                               return localSurveyIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SurveyIdentity
                               */
                               public void setSurveyIdentity(com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSurveyIdentityTracker = true;
                                       } else {
                                          localSurveyIdentityTracker = true;
                                              
                                       }
                                   
                                            this.localSurveyIdentity=param;
                                    

                               }
                            

                        /**
                        * field for DriverAlerts
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.DriverAlert[] localDriverAlerts ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriverAlertsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.DriverAlert[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.DriverAlert[] getDriverAlerts(){
                               return localDriverAlerts;
                           }

                           
                        


                               
                              /**
                               * validate the array for DriverAlerts
                               */
                              protected void validateDriverAlerts(com.freshdirect.routing.proxy.stub.transportation.DriverAlert[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param DriverAlerts
                              */
                              public void setDriverAlerts(com.freshdirect.routing.proxy.stub.transportation.DriverAlert[] param){
                              
                                   validateDriverAlerts(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localDriverAlertsTracker = true;
                                          } else {
                                             localDriverAlertsTracker = false;
                                                 
                                          }
                                      
                                      this.localDriverAlerts=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.DriverAlert
                             */
                             public void addDriverAlerts(com.freshdirect.routing.proxy.stub.transportation.DriverAlert param){
                                   if (localDriverAlerts == null){
                                   localDriverAlerts = new com.freshdirect.routing.proxy.stub.transportation.DriverAlert[]{};
                                   }

                            
                                 //update the setting tracker
                                localDriverAlertsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localDriverAlerts);
                               list.add(param);
                               this.localDriverAlerts =
                             (com.freshdirect.routing.proxy.stub.transportation.DriverAlert[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.DriverAlert[list.size()]);

                             }
                             

                        /**
                        * field for Orders
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.Order[] localOrders ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOrdersTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.Order[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.Order[] getOrders(){
                               return localOrders;
                           }

                           
                        


                               
                              /**
                               * validate the array for Orders
                               */
                              protected void validateOrders(com.freshdirect.routing.proxy.stub.transportation.Order[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Orders
                              */
                              public void setOrders(com.freshdirect.routing.proxy.stub.transportation.Order[] param){
                              
                                   validateOrders(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localOrdersTracker = true;
                                          } else {
                                             localOrdersTracker = false;
                                                 
                                          }
                                      
                                      this.localOrders=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.Order
                             */
                             public void addOrders(com.freshdirect.routing.proxy.stub.transportation.Order param){
                                   if (localOrders == null){
                                   localOrders = new com.freshdirect.routing.proxy.stub.transportation.Order[]{};
                                   }

                            
                                 //update the setting tracker
                                localOrdersTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localOrders);
                               list.add(param);
                               this.localOrders =
                             (com.freshdirect.routing.proxy.stub.transportation.Order[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.Order[list.size()]);

                             }
                             

                        /**
                        * field for RemoveFlag
                        */

                        
                                    protected boolean localRemoveFlag =
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean("false");
                                

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getRemoveFlag(){
                               return localRemoveFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RemoveFlag
                               */
                               public void setRemoveFlag(boolean param){
                            
                                            this.localRemoveFlag=param;
                                    

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
                       Stop.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":Stop",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "Stop",
                           xmlWriter);
                   }

               
                   }
                if (localStopIdentityTracker){
                                    if (localStopIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"stopIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"stopIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("stopIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localStopIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stopIdentity"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"stopIndex", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"stopIndex");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("stopIndex");
                                    }
                                
                                               if (localStopIndex==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("stopIndex cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStopIndex));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedSequenceNum", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedSequenceNum");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedSequenceNum");
                                    }
                                
                                               if (localPlannedSequenceNum==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("plannedSequenceNum cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedSequenceNum));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualSequenceNum", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualSequenceNum");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualSequenceNum");
                                    }
                                
                                               if (localActualSequenceNum==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualSequenceNum cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualSequenceNum));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                            if (localStopType==null){
                                                 throw new org.apache.axis2.databinding.ADBException("stopType cannot be null!!");
                                            }
                                           localStopType.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stopType"),
                                               factory,xmlWriter);
                                         if (localLocationIdentityTracker){
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
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
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
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"buildingDeliverySequence", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"buildingDeliverySequence");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("buildingDeliverySequence");
                                    }
                                
                                               if (localBuildingDeliverySequence==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("buildingDeliverySequence cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBuildingDeliverySequence));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localConsigneeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"consignee", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"consignee");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("consignee");
                                    }
                                

                                          if (localConsignee==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("consignee cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localConsignee);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localShipperTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"shipper", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"shipper");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("shipper");
                                    }
                                

                                          if (localShipper==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("shipper cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localShipper);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPlannedArrivalTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedArrival", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedArrival");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedArrival");
                                    }
                                

                                          if (localPlannedArrival==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedArrival cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedArrival));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localProjectedArrivalTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"projectedArrival", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"projectedArrival");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("projectedArrival");
                                    }
                                

                                          if (localProjectedArrival==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("projectedArrival cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedArrival));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localActualArrivalTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualArrival", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualArrival");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualArrival");
                                    }
                                

                                          if (localActualArrival==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualArrival cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualArrival));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPlannedDepartureTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedDeparture", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedDeparture");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedDeparture");
                                    }
                                

                                          if (localPlannedDeparture==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("plannedDeparture cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDeparture));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localProjectedDepartureTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"projectedDeparture", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"projectedDeparture");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("projectedDeparture");
                                    }
                                

                                          if (localProjectedDeparture==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("projectedDeparture cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedDeparture));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localActualDepartureTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDeparture", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDeparture");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDeparture");
                                    }
                                

                                          if (localActualDeparture==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("actualDeparture cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDeparture));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"cancelled", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"cancelled");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("cancelled");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("cancelled cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCancelled));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedDistance");
                                    }
                                
                                               if (java.lang.Double.isNaN(localPlannedDistance)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("plannedDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"projectedDistance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"projectedDistance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("projectedDistance");
                                    }
                                
                                               if (java.lang.Double.isNaN(localProjectedDistance)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("projectedDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedDistance));
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
                                
                                               if (java.lang.Double.isNaN(localActualDistance)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDistance cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDistance));
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
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("openTime cannot be null!!");
                                                  
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
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("closeTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCloseTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw1OpenTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw1OpenTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw1OpenTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw1OpenTime");
                                    }
                                

                                          if (localTw1OpenTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("tw1OpenTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1OpenTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw1CloseTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw1CloseTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw1CloseTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw1CloseTime");
                                    }
                                

                                          if (localTw1CloseTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("tw1CloseTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1CloseTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw2OpenTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw2OpenTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw2OpenTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw2OpenTime");
                                    }
                                

                                          if (localTw2OpenTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("tw2OpenTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2OpenTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTw2CloseTimeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"tw2CloseTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"tw2CloseTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("tw2CloseTime");
                                    }
                                

                                          if (localTw2CloseTime==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("tw2CloseTime cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2CloseTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDelayTypeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"delayType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"delayType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("delayType");
                                    }
                                

                                          if (localDelayType==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("delayType cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDelayType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"delayMinutes", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"delayMinutes");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("delayMinutes");
                                    }
                                
                                               if (localDelayMinutes==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("delayMinutes cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDelayMinutes));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localSealTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"seal", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"seal");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("seal");
                                    }
                                

                                          if (localSeal==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("seal cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSeal);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localUserDefinedField1Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"userDefinedField1", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"userDefinedField1");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("userDefinedField1");
                                    }
                                

                                          if (localUserDefinedField1==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("userDefinedField1 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUserDefinedField1);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localUserDefinedField2Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"userDefinedField2", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"userDefinedField2");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("userDefinedField2");
                                    }
                                

                                          if (localUserDefinedField2==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("userDefinedField2 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUserDefinedField2);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localUserDefinedField3Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"userDefinedField3", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"userDefinedField3");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("userDefinedField3");
                                    }
                                

                                          if (localUserDefinedField3==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("userDefinedField3 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUserDefinedField3);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLocationUserDefinedField1Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationUserDefinedField1", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationUserDefinedField1");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationUserDefinedField1");
                                    }
                                

                                          if (localLocationUserDefinedField1==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField1 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationUserDefinedField1);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLocationUserDefinedField2Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationUserDefinedField2", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationUserDefinedField2");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationUserDefinedField2");
                                    }
                                

                                          if (localLocationUserDefinedField2==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField2 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationUserDefinedField2);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLocationUserDefinedField3Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationUserDefinedField3", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationUserDefinedField3");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationUserDefinedField3");
                                    }
                                

                                          if (localLocationUserDefinedField3==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField3 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationUserDefinedField3);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                            if (localTimeZone==null){
                                                 throw new org.apache.axis2.databinding.ADBException("timeZone cannot be null!!");
                                            }
                                           localTimeZone.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","timeZone"),
                                               factory,xmlWriter);
                                         if (localInstructionsTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"instructions", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"instructions");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("instructions");
                                    }
                                

                                          if (localInstructions==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("instructions cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localInstructions);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"redeliveryID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"redeliveryID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("redeliveryID");
                                    }
                                
                                               if (localRedeliveryID==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("redeliveryID cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRedeliveryID));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localCancelCodeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"cancelCode", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"cancelCode");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("cancelCode");
                                    }
                                

                                          if (localCancelCode==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("cancelCode cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localCancelCode);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localUndeliverableCodeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"undeliverableCode", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"undeliverableCode");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("undeliverableCode");
                                    }
                                

                                          if (localUndeliverableCode==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("undeliverableCode cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localUndeliverableCode);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"undeliverable", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"undeliverable");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("undeliverable");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("undeliverable cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUndeliverable));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
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
                              if (localQuantitiesTracker){
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
                                } if (localReasonCodesTracker){
                                    if (localReasonCodes==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"reasonCodes", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"reasonCodes");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("reasonCodes");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localReasonCodes.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","reasonCodes"),
                                        factory,xmlWriter);
                                    }
                                } if (localLocationNameTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"locationName", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"locationName");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("locationName");
                                    }
                                

                                          if (localLocationName==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("locationName cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLocationName);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAddressTracker){
                                    if (localAddress==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"address", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"address");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("address");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localAddress.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","address"),
                                        factory,xmlWriter);
                                    }
                                } if (localPhoneNumberTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"phoneNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"phoneNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("phoneNumber");
                                    }
                                

                                          if (localPhoneNumber==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("phoneNumber cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPhoneNumber);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDriver1NameTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driver1Name", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driver1Name");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driver1Name");
                                    }
                                

                                          if (localDriver1Name==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("driver1Name cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDriver1Name);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDriver2NameTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"driver2Name", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"driver2Name");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("driver2Name");
                                    }
                                

                                          if (localDriver2Name==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("driver2Name cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDriver2Name);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"routeComplete", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"routeComplete");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("routeComplete");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("routeComplete cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteComplete));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"routeInProgress", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"routeInProgress");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("routeInProgress");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("routeInProgress cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteInProgress));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"plannedDeliveryCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"plannedDeliveryCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("plannedDeliveryCost");
                                    }
                                
                                               if (java.lang.Double.isNaN(localPlannedDeliveryCost)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("plannedDeliveryCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDeliveryCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"actualDeliveryCost", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"actualDeliveryCost");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("actualDeliveryCost");
                                    }
                                
                                               if (java.lang.Double.isNaN(localActualDeliveryCost)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("actualDeliveryCost cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDeliveryCost));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localSurveyIdentityTracker){
                                    if (localSurveyIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"surveyIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"surveyIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("surveyIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localSurveyIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","surveyIdentity"),
                                        factory,xmlWriter);
                                    }
                                } if (localDriverAlertsTracker){
                                       if (localDriverAlerts!=null){
                                            for (int i = 0;i < localDriverAlerts.length;i++){
                                                if (localDriverAlerts[i] != null){
                                                 localDriverAlerts[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverAlerts"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("driverAlerts cannot be null!!");
                                        
                                    }
                                 } if (localOrdersTracker){
                                       if (localOrders!=null){
                                            for (int i = 0;i < localOrders.length;i++){
                                                if (localOrders[i] != null){
                                                 localOrders[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orders"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("orders cannot be null!!");
                                        
                                    }
                                 }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"removeFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"removeFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("removeFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("removeFlag cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRemoveFlag));
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

                 if (localStopIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stopIdentity"));
                            
                            
                                    elementList.add(localStopIdentity==null?null:
                                    localStopIdentity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stopIndex"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStopIndex));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "plannedSequenceNum"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedSequenceNum));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualSequenceNum"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualSequenceNum));
                            
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "stopType"));
                            
                            
                                    if (localStopType==null){
                                         throw new org.apache.axis2.databinding.ADBException("stopType cannot be null!!");
                                    }
                                    elementList.add(localStopType);
                                 if (localLocationIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationIdentity"));
                            
                            
                                    elementList.add(localLocationIdentity==null?null:
                                    localLocationIdentity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "latitude"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLatitude));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "longitude"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLongitude));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "buildingDeliverySequence"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localBuildingDeliverySequence));
                             if (localConsigneeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "consignee"));
                                 
                                        if (localConsignee != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConsignee));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("consignee cannot be null!!");
                                        }
                                    } if (localShipperTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "shipper"));
                                 
                                        if (localShipper != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShipper));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("shipper cannot be null!!");
                                        }
                                    } if (localPlannedArrivalTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "plannedArrival"));
                                 
                                        if (localPlannedArrival != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedArrival));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedArrival cannot be null!!");
                                        }
                                    } if (localProjectedArrivalTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "projectedArrival"));
                                 
                                        if (localProjectedArrival != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedArrival));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("projectedArrival cannot be null!!");
                                        }
                                    } if (localActualArrivalTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualArrival"));
                                 
                                        if (localActualArrival != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualArrival));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualArrival cannot be null!!");
                                        }
                                    } if (localPlannedDepartureTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "plannedDeparture"));
                                 
                                        if (localPlannedDeparture != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDeparture));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("plannedDeparture cannot be null!!");
                                        }
                                    } if (localProjectedDepartureTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "projectedDeparture"));
                                 
                                        if (localProjectedDeparture != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedDeparture));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("projectedDeparture cannot be null!!");
                                        }
                                    } if (localActualDepartureTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDeparture"));
                                 
                                        if (localActualDeparture != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDeparture));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("actualDeparture cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "cancelled"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCancelled));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "plannedDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "projectedDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProjectedDistance));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDistance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDistance));
                             if (localOpenTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "openTime"));
                                 
                                        if (localOpenTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOpenTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("openTime cannot be null!!");
                                        }
                                    } if (localCloseTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "closeTime"));
                                 
                                        if (localCloseTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCloseTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("closeTime cannot be null!!");
                                        }
                                    } if (localTw1OpenTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw1OpenTime"));
                                 
                                        if (localTw1OpenTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1OpenTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("tw1OpenTime cannot be null!!");
                                        }
                                    } if (localTw1CloseTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw1CloseTime"));
                                 
                                        if (localTw1CloseTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw1CloseTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("tw1CloseTime cannot be null!!");
                                        }
                                    } if (localTw2OpenTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw2OpenTime"));
                                 
                                        if (localTw2OpenTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2OpenTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("tw2OpenTime cannot be null!!");
                                        }
                                    } if (localTw2CloseTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "tw2CloseTime"));
                                 
                                        if (localTw2CloseTime != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTw2CloseTime));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("tw2CloseTime cannot be null!!");
                                        }
                                    } if (localDelayTypeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "delayType"));
                                 
                                        if (localDelayType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDelayType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("delayType cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "delayMinutes"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDelayMinutes));
                             if (localSealTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "seal"));
                                 
                                        if (localSeal != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSeal));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("seal cannot be null!!");
                                        }
                                    } if (localUserDefinedField1Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "userDefinedField1"));
                                 
                                        if (localUserDefinedField1 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUserDefinedField1));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("userDefinedField1 cannot be null!!");
                                        }
                                    } if (localUserDefinedField2Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "userDefinedField2"));
                                 
                                        if (localUserDefinedField2 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUserDefinedField2));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("userDefinedField2 cannot be null!!");
                                        }
                                    } if (localUserDefinedField3Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "userDefinedField3"));
                                 
                                        if (localUserDefinedField3 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUserDefinedField3));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("userDefinedField3 cannot be null!!");
                                        }
                                    } if (localLocationUserDefinedField1Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationUserDefinedField1"));
                                 
                                        if (localLocationUserDefinedField1 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationUserDefinedField1));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField1 cannot be null!!");
                                        }
                                    } if (localLocationUserDefinedField2Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationUserDefinedField2"));
                                 
                                        if (localLocationUserDefinedField2 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationUserDefinedField2));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField2 cannot be null!!");
                                        }
                                    } if (localLocationUserDefinedField3Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationUserDefinedField3"));
                                 
                                        if (localLocationUserDefinedField3 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationUserDefinedField3));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationUserDefinedField3 cannot be null!!");
                                        }
                                    }
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "timeZone"));
                            
                            
                                    if (localTimeZone==null){
                                         throw new org.apache.axis2.databinding.ADBException("timeZone cannot be null!!");
                                    }
                                    elementList.add(localTimeZone);
                                 if (localInstructionsTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "instructions"));
                                 
                                        if (localInstructions != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInstructions));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("instructions cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "redeliveryID"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRedeliveryID));
                             if (localCancelCodeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "cancelCode"));
                                 
                                        if (localCancelCode != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCancelCode));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("cancelCode cannot be null!!");
                                        }
                                    } if (localUndeliverableCodeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "undeliverableCode"));
                                 
                                        if (localUndeliverableCode != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUndeliverableCode));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("undeliverableCode cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "undeliverable"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUndeliverable));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "additionalServiceTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAdditionalServiceTime));
                             if (localQuantitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "quantities"));
                            
                            
                                    elementList.add(localQuantities==null?null:
                                    localQuantities);
                                } if (localReasonCodesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "reasonCodes"));
                            
                            
                                    elementList.add(localReasonCodes==null?null:
                                    localReasonCodes);
                                } if (localLocationNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "locationName"));
                                 
                                        if (localLocationName != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocationName));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("locationName cannot be null!!");
                                        }
                                    } if (localAddressTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "address"));
                            
                            
                                    elementList.add(localAddress==null?null:
                                    localAddress);
                                } if (localPhoneNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "phoneNumber"));
                                 
                                        if (localPhoneNumber != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPhoneNumber));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("phoneNumber cannot be null!!");
                                        }
                                    } if (localDriver1NameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driver1Name"));
                                 
                                        if (localDriver1Name != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriver1Name));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("driver1Name cannot be null!!");
                                        }
                                    } if (localDriver2NameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "driver2Name"));
                                 
                                        if (localDriver2Name != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriver2Name));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("driver2Name cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "routeComplete"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteComplete));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "routeInProgress"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRouteInProgress));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "plannedDeliveryCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPlannedDeliveryCost));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "actualDeliveryCost"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActualDeliveryCost));
                             if (localSurveyIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "surveyIdentity"));
                            
                            
                                    elementList.add(localSurveyIdentity==null?null:
                                    localSurveyIdentity);
                                } if (localDriverAlertsTracker){
                             if (localDriverAlerts!=null) {
                                 for (int i = 0;i < localDriverAlerts.length;i++){

                                    if (localDriverAlerts[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "driverAlerts"));
                                         elementList.add(localDriverAlerts[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("driverAlerts cannot be null!!");
                                    
                             }

                        } if (localOrdersTracker){
                             if (localOrders!=null) {
                                 for (int i = 0;i < localOrders.length;i++){

                                    if (localOrders[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                          "orders"));
                                         elementList.add(localOrders[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("orders cannot be null!!");
                                    
                             }

                        }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService",
                                                                      "removeFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRemoveFlag));
                            

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
        public static Stop parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            Stop object =
                new Stop();

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
                    
                            if (!"Stop".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Stop)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list56 = new java.util.ArrayList();
                    
                        java.util.ArrayList list57 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stopIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setStopIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stopIndex").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStopIndex(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","plannedSequenceNum").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedSequenceNum(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualSequenceNum").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualSequenceNum(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","stopType").equals(reader.getName())){
                                
                                                object.setStopType(com.freshdirect.routing.proxy.stub.transportation.StopType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","latitude").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","longitude").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","buildingDeliverySequence").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setBuildingDeliverySequence(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","consignee").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setConsignee(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","shipper").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setShipper(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","plannedArrival").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedArrival(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","projectedArrival").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setProjectedArrival(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualArrival").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualArrival(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","plannedDeparture").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedDeparture(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","projectedDeparture").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setProjectedDeparture(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDeparture").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDeparture(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","cancelled").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCancelled(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","plannedDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","projectedDistance").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setProjectedDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
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
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","openTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOpenTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","closeTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCloseTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw1OpenTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw1OpenTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw1CloseTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw1CloseTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw2OpenTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw2OpenTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","tw2CloseTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTw2CloseTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","delayType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDelayType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","delayMinutes").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDelayMinutes(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","seal").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSeal(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","userDefinedField1").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUserDefinedField1(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","userDefinedField2").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUserDefinedField2(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","userDefinedField3").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUserDefinedField3(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationUserDefinedField1").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationUserDefinedField1(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationUserDefinedField2").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationUserDefinedField2(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationUserDefinedField3").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationUserDefinedField3(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","timeZone").equals(reader.getName())){
                                
                                                object.setTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","instructions").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInstructions(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","redeliveryID").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRedeliveryID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","cancelCode").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCancelCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","undeliverableCode").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUndeliverableCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","undeliverable").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUndeliverable(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","additionalServiceTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAdditionalServiceTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","quantities").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setQuantities(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setQuantities(com.freshdirect.routing.proxy.stub.transportation.ItemQuantities.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","reasonCodes").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setReasonCodes(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setReasonCodes(com.freshdirect.routing.proxy.stub.transportation.ReasonCodes.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","locationName").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLocationName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","address").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setAddress(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setAddress(com.freshdirect.routing.proxy.stub.transportation.Address.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","phoneNumber").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPhoneNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driver1Name").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriver1Name(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driver2Name").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriver2Name(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeComplete").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteComplete(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","routeInProgress").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRouteInProgress(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","plannedDeliveryCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPlannedDeliveryCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","actualDeliveryCost").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setActualDeliveryCost(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","surveyIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setSurveyIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setSurveyIdentity(com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverAlerts").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list56.add(com.freshdirect.routing.proxy.stub.transportation.DriverAlert.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone56 = false;
                                                        while(!loopDone56){
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
                                                                loopDone56 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","driverAlerts").equals(reader.getName())){
                                                                    list56.add(com.freshdirect.routing.proxy.stub.transportation.DriverAlert.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone56 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setDriverAlerts((com.freshdirect.routing.proxy.stub.transportation.DriverAlert[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.DriverAlert.class,
                                                                list56));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orders").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list57.add(com.freshdirect.routing.proxy.stub.transportation.Order.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone57 = false;
                                                        while(!loopDone57){
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
                                                                loopDone57 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","orders").equals(reader.getName())){
                                                                    list57.add(com.freshdirect.routing.proxy.stub.transportation.Order.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone57 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setOrders((com.freshdirect.routing.proxy.stub.transportation.Order[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.Order.class,
                                                                list57));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService","removeFlag").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRemoveFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                              
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
           
          