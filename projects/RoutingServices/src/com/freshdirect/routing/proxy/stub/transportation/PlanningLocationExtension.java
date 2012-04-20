
/**
 * PlanningLocationExtension.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.transportation;
            

            /**
            *  PlanningLocationExtension bean class
            */
        
        public  class PlanningLocationExtension
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = PlanningLocationExtension
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
                        * field for SessionIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity localSessionIdentity ;
                                

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity getSessionIdentity(){
                               return localSessionIdentity;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SessionIdentity
                               */
                               public void setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity param){
                            
                                            this.localSessionIdentity=param;
                                    

                               }
                            

                        /**
                        * field for LocationIdentity
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.LocationIdentity localLocationIdentity ;
                                

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
                            
                                            this.localLocationIdentity=param;
                                    

                               }
                            

                        /**
                        * field for TerritoryID
                        */

                        
                                    protected java.lang.String localTerritoryID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTerritoryIDTracker = false ;
                           

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
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localTerritoryIDTracker = true;
                                       } else {
                                          localTerritoryIDTracker = true;
                                              
                                       }
                                   
                                            this.localTerritoryID=param;
                                    

                               }
                            

                        /**
                        * field for OriginID
                        */

                        
                                    protected java.lang.String localOriginID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOriginIDTracker = false ;
                           

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
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOriginIDTracker = true;
                                       } else {
                                          localOriginIDTracker = true;
                                              
                                       }
                                   
                                            this.localOriginID=param;
                                    

                               }
                            

                        /**
                        * field for OriginType
                        */

                        
                                    protected java.lang.String localOriginType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOriginTypeTracker = false ;
                           

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
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOriginTypeTracker = true;
                                       } else {
                                          localOriginTypeTracker = true;
                                              
                                       }
                                   
                                            this.localOriginType=param;
                                    

                               }
                            

                        /**
                        * field for SrvcPatternSet
                        */

                        
                                    protected java.lang.String localSrvcPatternSet ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSrvcPatternSetTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSrvcPatternSet(){
                               return localSrvcPatternSet;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SrvcPatternSet
                               */
                               public void setSrvcPatternSet(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSrvcPatternSetTracker = true;
                                       } else {
                                          localSrvcPatternSetTracker = true;
                                              
                                       }
                                   
                                            this.localSrvcPatternSet=param;
                                    

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
                        * field for ServicePattern
                        */

                        
                                    protected java.lang.String localServicePattern ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localServicePatternTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getServicePattern(){
                               return localServicePattern;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ServicePattern
                               */
                               public void setServicePattern(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localServicePatternTracker = true;
                                       } else {
                                          localServicePatternTracker = true;
                                              
                                       }
                                   
                                            this.localServicePattern=param;
                                    

                               }
                            

                        /**
                        * field for ActiveWeeks
                        * This was an Array!
                        */

                        
                                    protected int[] localActiveWeeks ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActiveWeeksTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int[]
                           */
                           public  int[] getActiveWeeks(){
                               return localActiveWeeks;
                           }

                           
                        


                               
                              /**
                               * validate the array for ActiveWeeks
                               */
                              protected void validateActiveWeeks(int[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param ActiveWeeks
                              */
                              public void setActiveWeeks(int[] param){
                              
                                   validateActiveWeeks(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localActiveWeeksTracker = true;
                                          } else {
                                             localActiveWeeksTracker = false;
                                                 
                                          }
                                      
                                      this.localActiveWeeks=param;
                              }

                               
                             

                        /**
                        * field for AssignDaysFlag
                        */

                        
                                    protected boolean localAssignDaysFlag ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAssignDaysFlagTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getAssignDaysFlag(){
                               return localAssignDaysFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AssignDaysFlag
                               */
                               public void setAssignDaysFlag(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localAssignDaysFlagTracker = true;
                                              
                                       } else {
                                          localAssignDaysFlagTracker = true;
                                       }
                                   
                                            this.localAssignDaysFlag=param;
                                    

                               }
                            

                        /**
                        * field for DayString
                        */

                        
                                    protected java.lang.String localDayString ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDayStringTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDayString(){
                               return localDayString;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DayString
                               */
                               public void setDayString(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDayStringTracker = true;
                                       } else {
                                          localDayStringTracker = true;
                                              
                                       }
                                   
                                            this.localDayString=param;
                                    

                               }
                            

                        /**
                        * field for DayShift
                        */

                        
                                    protected int localDayShift ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDayShiftTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDayShift(){
                               return localDayShift;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DayShift
                               */
                               public void setDayShift(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localDayShiftTracker = true;
                                              
                                       } else {
                                          localDayShiftTracker = true;
                                       }
                                   
                                            this.localDayShift=param;
                                    

                               }
                            

                        /**
                        * field for PrevOriginID
                        */

                        
                                    protected java.lang.String localPrevOriginID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPrevOriginIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPrevOriginID(){
                               return localPrevOriginID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PrevOriginID
                               */
                               public void setPrevOriginID(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPrevOriginIDTracker = true;
                                       } else {
                                          localPrevOriginIDTracker = true;
                                              
                                       }
                                   
                                            this.localPrevOriginID=param;
                                    

                               }
                            

                        /**
                        * field for PrevTerritoryID
                        */

                        
                                    protected java.lang.String localPrevTerritoryID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPrevTerritoryIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPrevTerritoryID(){
                               return localPrevTerritoryID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PrevTerritoryID
                               */
                               public void setPrevTerritoryID(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPrevTerritoryIDTracker = true;
                                       } else {
                                          localPrevTerritoryIDTracker = true;
                                              
                                       }
                                   
                                            this.localPrevTerritoryID=param;
                                    

                               }
                            

                        /**
                        * field for PrevWeeks
                        * This was an Array!
                        */

                        
                                    protected int[] localPrevWeeks ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPrevWeeksTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int[]
                           */
                           public  int[] getPrevWeeks(){
                               return localPrevWeeks;
                           }

                           
                        


                               
                              /**
                               * validate the array for PrevWeeks
                               */
                              protected void validatePrevWeeks(int[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param PrevWeeks
                              */
                              public void setPrevWeeks(int[] param){
                              
                                   validatePrevWeeks(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localPrevWeeksTracker = true;
                                          } else {
                                             localPrevWeeksTracker = false;
                                                 
                                          }
                                      
                                      this.localPrevWeeks=param;
                              }

                               
                             

                        /**
                        * field for PrevDays
                        */

                        
                                    protected java.lang.String localPrevDays ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPrevDaysTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPrevDays(){
                               return localPrevDays;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PrevDays
                               */
                               public void setPrevDays(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPrevDaysTracker = true;
                                       } else {
                                          localPrevDaysTracker = true;
                                              
                                       }
                                   
                                            this.localPrevDays=param;
                                    

                               }
                            

                        /**
                        * field for SrvcTimeTypeOverride
                        */

                        
                                    protected java.lang.String localSrvcTimeTypeOverride ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSrvcTimeTypeOverrideTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getSrvcTimeTypeOverride(){
                               return localSrvcTimeTypeOverride;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SrvcTimeTypeOverride
                               */
                               public void setSrvcTimeTypeOverride(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localSrvcTimeTypeOverrideTracker = true;
                                       } else {
                                          localSrvcTimeTypeOverrideTracker = true;
                                              
                                       }
                                   
                                            this.localSrvcTimeTypeOverride=param;
                                    

                               }
                            

                        /**
                        * field for AnchorDaysRange
                        */

                        
                                    protected int localAnchorDaysRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorDaysRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getAnchorDaysRange(){
                               return localAnchorDaysRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorDaysRange
                               */
                               public void setAnchorDaysRange(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localAnchorDaysRangeTracker = true;
                                              
                                       } else {
                                          localAnchorDaysRangeTracker = true;
                                       }
                                   
                                            this.localAnchorDaysRange=param;
                                    

                               }
                            

                        /**
                        * field for AnchorAnyDayFlag
                        */

                        
                                    protected boolean localAnchorAnyDayFlag ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorAnyDayFlagTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getAnchorAnyDayFlag(){
                               return localAnchorAnyDayFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorAnyDayFlag
                               */
                               public void setAnchorAnyDayFlag(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localAnchorAnyDayFlagTracker = true;
                                              
                                       } else {
                                          localAnchorAnyDayFlagTracker = true;
                                       }
                                   
                                            this.localAnchorAnyDayFlag=param;
                                    

                               }
                            

                        /**
                        * field for AnchorDaysWrapFlag
                        */

                        
                                    protected boolean localAnchorDaysWrapFlag ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorDaysWrapFlagTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getAnchorDaysWrapFlag(){
                               return localAnchorDaysWrapFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorDaysWrapFlag
                               */
                               public void setAnchorDaysWrapFlag(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localAnchorDaysWrapFlagTracker = true;
                                              
                                       } else {
                                          localAnchorDaysWrapFlagTracker = true;
                                       }
                                   
                                            this.localAnchorDaysWrapFlag=param;
                                    

                               }
                            

                        /**
                        * field for AnchorWeeksRange
                        */

                        
                                    protected int localAnchorWeeksRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorWeeksRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getAnchorWeeksRange(){
                               return localAnchorWeeksRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorWeeksRange
                               */
                               public void setAnchorWeeksRange(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localAnchorWeeksRangeTracker = true;
                                              
                                       } else {
                                          localAnchorWeeksRangeTracker = true;
                                       }
                                   
                                            this.localAnchorWeeksRange=param;
                                    

                               }
                            

                        /**
                        * field for AnchorAnyWeekFlag
                        */

                        
                                    protected boolean localAnchorAnyWeekFlag ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorAnyWeekFlagTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getAnchorAnyWeekFlag(){
                               return localAnchorAnyWeekFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorAnyWeekFlag
                               */
                               public void setAnchorAnyWeekFlag(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localAnchorAnyWeekFlagTracker = true;
                                              
                                       } else {
                                          localAnchorAnyWeekFlagTracker = true;
                                       }
                                   
                                            this.localAnchorAnyWeekFlag=param;
                                    

                               }
                            

                        /**
                        * field for AnchorWeeksWrapFlag
                        */

                        
                                    protected boolean localAnchorWeeksWrapFlag ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAnchorWeeksWrapFlagTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getAnchorWeeksWrapFlag(){
                               return localAnchorWeeksWrapFlag;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AnchorWeeksWrapFlag
                               */
                               public void setAnchorWeeksWrapFlag(boolean param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (false) {
                                           localAnchorWeeksWrapFlagTracker = true;
                                              
                                       } else {
                                          localAnchorWeeksWrapFlagTracker = true;
                                       }
                                   
                                            this.localAnchorWeeksWrapFlag=param;
                                    

                               }
                            

                        /**
                        * field for UserDefinedFields
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[] localUserDefinedFields ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserDefinedFieldsTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[]
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[] getUserDefinedFields(){
                               return localUserDefinedFields;
                           }

                           
                        


                               
                              /**
                               * validate the array for UserDefinedFields
                               */
                              protected void validateUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param UserDefinedFields
                              */
                              public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[] param){
                              
                                   validateUserDefinedFields(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localUserDefinedFieldsTracker = true;
                                          } else {
                                             localUserDefinedFieldsTracker = false;
                                                 
                                          }
                                      
                                      this.localUserDefinedFields=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField
                             */
                             public void addUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField param){
                                   if (localUserDefinedFields == null){
                                   localUserDefinedFields = new com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[]{};
                                   }

                            
                                 //update the setting tracker
                                localUserDefinedFieldsTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localUserDefinedFields);
                               list.add(param);
                               this.localUserDefinedFields =
                             (com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[list.size()]);

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
                        * field for RejectCode
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionRejectCode localRejectCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRejectCodeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionRejectCode
                           */
                           public  com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionRejectCode getRejectCode(){
                               return localRejectCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RejectCode
                               */
                               public void setRejectCode(com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionRejectCode param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRejectCodeTracker = true;
                                       } else {
                                          localRejectCodeTracker = true;
                                              
                                       }
                                   
                                            this.localRejectCode=param;
                                    

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
                       PlanningLocationExtension.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":PlanningLocationExtension",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "PlanningLocationExtension",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localSessionIdentity==null){
                                                 throw new org.apache.axis2.databinding.ADBException("sessionIdentity cannot be null!!");
                                            }
                                           localSessionIdentity.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","sessionIdentity"),
                                               factory,xmlWriter);
                                        
                                            if (localLocationIdentity==null){
                                                 throw new org.apache.axis2.databinding.ADBException("locationIdentity cannot be null!!");
                                            }
                                           localLocationIdentity.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","locationIdentity"),
                                               factory,xmlWriter);
                                         if (localTerritoryIDTracker){
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
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localTerritoryID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOriginIDTracker){
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
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOriginID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOriginTypeTracker){
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
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOriginType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSrvcPatternSetTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"srvcPatternSet", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"srvcPatternSet");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("srvcPatternSet");
                                    }
                                

                                          if (localSrvcPatternSet==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSrvcPatternSet);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localQuantitiesTracker){
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
                                } if (localServicePatternTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"servicePattern", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"servicePattern");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("servicePattern");
                                    }
                                

                                          if (localServicePattern==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localServicePattern);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localActiveWeeksTracker){
                             if (localActiveWeeks!=null) {
                                   namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                   boolean emptyNamespace = namespace == null || namespace.length() == 0;
                                   prefix =  emptyNamespace ? null : xmlWriter.getPrefix(namespace);
                                   for (int i = 0;i < localActiveWeeks.length;i++){
                                        
                                                   if (localActiveWeeks[i]!=java.lang.Integer.MIN_VALUE) {
                                               
                                                if (!emptyNamespace) {
                                                    if (prefix == null) {
                                                        java.lang.String prefix2 = generatePrefix(namespace);

                                                        xmlWriter.writeStartElement(prefix2,"activeWeeks", namespace);
                                                        xmlWriter.writeNamespace(prefix2, namespace);
                                                        xmlWriter.setPrefix(prefix2, namespace);

                                                    } else {
                                                        xmlWriter.writeStartElement(namespace,"activeWeeks");
                                                    }

                                                } else {
                                                    xmlWriter.writeStartElement("activeWeeks");
                                                }

                                            
                                                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActiveWeeks[i]));
                                                xmlWriter.writeEndElement();
                                            
                                                } else {
                                                   
                                                           // we have to do nothing since minOccurs is zero
                                                       
                                                }

                                   }
                             } else {
                                 
                                         throw new org.apache.axis2.databinding.ADBException("activeWeeks cannot be null!!");
                                    
                             }

                        } if (localAssignDaysFlagTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"assignDaysFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"assignDaysFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("assignDaysFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAssignDaysFlag));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDayStringTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"dayString", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"dayString");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("dayString");
                                    }
                                

                                          if (localDayString==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDayString);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDayShiftTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"dayShift", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"dayShift");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("dayShift");
                                    }
                                
                                               if (localDayShift==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDayShift));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPrevOriginIDTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"prevOriginID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"prevOriginID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("prevOriginID");
                                    }
                                

                                          if (localPrevOriginID==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPrevOriginID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPrevTerritoryIDTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"prevTerritoryID", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"prevTerritoryID");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("prevTerritoryID");
                                    }
                                

                                          if (localPrevTerritoryID==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPrevTerritoryID);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPrevWeeksTracker){
                             if (localPrevWeeks!=null) {
                                   namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                   boolean emptyNamespace = namespace == null || namespace.length() == 0;
                                   prefix =  emptyNamespace ? null : xmlWriter.getPrefix(namespace);
                                   for (int i = 0;i < localPrevWeeks.length;i++){
                                        
                                                   if (localPrevWeeks[i]!=java.lang.Integer.MIN_VALUE) {
                                               
                                                if (!emptyNamespace) {
                                                    if (prefix == null) {
                                                        java.lang.String prefix2 = generatePrefix(namespace);

                                                        xmlWriter.writeStartElement(prefix2,"prevWeeks", namespace);
                                                        xmlWriter.writeNamespace(prefix2, namespace);
                                                        xmlWriter.setPrefix(prefix2, namespace);

                                                    } else {
                                                        xmlWriter.writeStartElement(namespace,"prevWeeks");
                                                    }

                                                } else {
                                                    xmlWriter.writeStartElement("prevWeeks");
                                                }

                                            
                                                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrevWeeks[i]));
                                                xmlWriter.writeEndElement();
                                            
                                                } else {
                                                   
                                                           // we have to do nothing since minOccurs is zero
                                                       
                                                }

                                   }
                             } else {
                                 
                                         throw new org.apache.axis2.databinding.ADBException("prevWeeks cannot be null!!");
                                    
                             }

                        } if (localPrevDaysTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"prevDays", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"prevDays");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("prevDays");
                                    }
                                

                                          if (localPrevDays==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPrevDays);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localSrvcTimeTypeOverrideTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"srvcTimeTypeOverride", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"srvcTimeTypeOverride");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("srvcTimeTypeOverride");
                                    }
                                

                                          if (localSrvcTimeTypeOverride==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localSrvcTimeTypeOverride);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorDaysRangeTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorDaysRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorDaysRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorDaysRange");
                                    }
                                
                                               if (localAnchorDaysRange==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorDaysRange));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorAnyDayFlagTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorAnyDayFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorAnyDayFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorAnyDayFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorAnyDayFlag));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorDaysWrapFlagTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorDaysWrapFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorDaysWrapFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorDaysWrapFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorDaysWrapFlag));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorWeeksRangeTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorWeeksRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorWeeksRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorWeeksRange");
                                    }
                                
                                               if (localAnchorWeeksRange==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorWeeksRange));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorAnyWeekFlagTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorAnyWeekFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorAnyWeekFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorAnyWeekFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorAnyWeekFlag));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localAnchorWeeksWrapFlagTracker){
                                    namespace = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"anchorWeeksWrapFlag", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"anchorWeeksWrapFlag");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("anchorWeeksWrapFlag");
                                    }
                                
                                               if (false) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorWeeksWrapFlag));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localUserDefinedFieldsTracker){
                                       if (localUserDefinedFields!=null){
                                            for (int i = 0;i < localUserDefinedFields.length;i++){
                                                if (localUserDefinedFields[i] != null){
                                                 localUserDefinedFields[i].serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","userDefinedFields"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("userDefinedFields cannot be null!!");
                                        
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
                                 } if (localRejectCodeTracker){
                                    if (localRejectCode==null){

                                            java.lang.String namespace2 = "http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService";

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
                                     localRejectCode.serialize(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","rejectCode"),
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
                                                                      "sessionIdentity"));
                            
                            
                                    if (localSessionIdentity==null){
                                         throw new org.apache.axis2.databinding.ADBException("sessionIdentity cannot be null!!");
                                    }
                                    elementList.add(localSessionIdentity);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "locationIdentity"));
                            
                            
                                    if (localLocationIdentity==null){
                                         throw new org.apache.axis2.databinding.ADBException("locationIdentity cannot be null!!");
                                    }
                                    elementList.add(localLocationIdentity);
                                 if (localTerritoryIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "territoryID"));
                                 
                                         elementList.add(localTerritoryID==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTerritoryID));
                                    } if (localOriginIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "originID"));
                                 
                                         elementList.add(localOriginID==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginID));
                                    } if (localOriginTypeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "originType"));
                                 
                                         elementList.add(localOriginType==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOriginType));
                                    } if (localSrvcPatternSetTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "srvcPatternSet"));
                                 
                                         elementList.add(localSrvcPatternSet==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSrvcPatternSet));
                                    } if (localQuantitiesTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "quantities"));
                            
                            
                                    elementList.add(localQuantities==null?null:
                                    localQuantities);
                                } if (localServicePatternTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "servicePattern"));
                                 
                                         elementList.add(localServicePattern==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServicePattern));
                                    } if (localActiveWeeksTracker){
                            if (localActiveWeeks!=null){
                                  for (int i = 0;i < localActiveWeeks.length;i++){
                                      
                                          elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                                                                       "activeWeeks"));
                                          elementList.add(
                                          org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActiveWeeks[i]));

                                      

                                  }
                            } else {
                              
                                    throw new org.apache.axis2.databinding.ADBException("activeWeeks cannot be null!!");
                                
                            }

                        } if (localAssignDaysFlagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "assignDaysFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAssignDaysFlag));
                            } if (localDayStringTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "dayString"));
                                 
                                         elementList.add(localDayString==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDayString));
                                    } if (localDayShiftTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "dayShift"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDayShift));
                            } if (localPrevOriginIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "prevOriginID"));
                                 
                                         elementList.add(localPrevOriginID==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrevOriginID));
                                    } if (localPrevTerritoryIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "prevTerritoryID"));
                                 
                                         elementList.add(localPrevTerritoryID==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrevTerritoryID));
                                    } if (localPrevWeeksTracker){
                            if (localPrevWeeks!=null){
                                  for (int i = 0;i < localPrevWeeks.length;i++){
                                      
                                          elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                                                                       "prevWeeks"));
                                          elementList.add(
                                          org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrevWeeks[i]));

                                      

                                  }
                            } else {
                              
                                    throw new org.apache.axis2.databinding.ADBException("prevWeeks cannot be null!!");
                                
                            }

                        } if (localPrevDaysTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "prevDays"));
                                 
                                         elementList.add(localPrevDays==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrevDays));
                                    } if (localSrvcTimeTypeOverrideTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "srvcTimeTypeOverride"));
                                 
                                         elementList.add(localSrvcTimeTypeOverride==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSrvcTimeTypeOverride));
                                    } if (localAnchorDaysRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorDaysRange"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorDaysRange));
                            } if (localAnchorAnyDayFlagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorAnyDayFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorAnyDayFlag));
                            } if (localAnchorDaysWrapFlagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorDaysWrapFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorDaysWrapFlag));
                            } if (localAnchorWeeksRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorWeeksRange"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorWeeksRange));
                            } if (localAnchorAnyWeekFlagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorAnyWeekFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorAnyWeekFlag));
                            } if (localAnchorWeeksWrapFlagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "anchorWeeksWrapFlag"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnchorWeeksWrapFlag));
                            } if (localUserDefinedFieldsTracker){
                             if (localUserDefinedFields!=null) {
                                 for (int i = 0;i < localUserDefinedFields.length;i++){

                                    if (localUserDefinedFields[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                          "userDefinedFields"));
                                         elementList.add(localUserDefinedFields[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("userDefinedFields cannot be null!!");
                                    
                             }

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

                        } if (localRejectCodeTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService",
                                                                      "rejectCode"));
                            
                            
                                    elementList.add(localRejectCode==null?null:
                                    localRejectCode);
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
        public static PlanningLocationExtension parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            PlanningLocationExtension object =
                new PlanningLocationExtension();

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
                    
                            if (!"PlanningLocationExtension".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (PlanningLocationExtension)com.freshdirect.routing.proxy.stub.transportation.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list9 = new java.util.ArrayList();
                    
                        java.util.ArrayList list15 = new java.util.ArrayList();
                    
                        java.util.ArrayList list24 = new java.util.ArrayList();
                    
                        java.util.ArrayList list25 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","sessionIdentity").equals(reader.getName())){
                                
                                                object.setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","locationIdentity").equals(reader.getName())){
                                
                                                object.setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","territoryID").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTerritoryID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","originID").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","originType").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOriginType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","srvcPatternSet").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSrvcPatternSet(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","servicePattern").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setServicePattern(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","activeWeeks").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list9.add(reader.getElementText());
                                            
                                            //loop until we find a start element that is not part of this array
                                            boolean loopDone9 = false;
                                            while(!loopDone9){
                                                // Ensure we are at the EndElement
                                                while (!reader.isEndElement()){
                                                    reader.next();
                                                }
                                                // Step out of this element
                                                reader.next();
                                                // Step to next element event.
                                                while (!reader.isStartElement() && !reader.isEndElement())
                                                    reader.next();
                                                if (reader.isEndElement()){
                                                    //two continuous end elements means we are exiting the xml structure
                                                    loopDone9 = true;
                                                } else {
                                                    if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","activeWeeks").equals(reader.getName())){
                                                         list9.add(reader.getElementText());
                                                        
                                                    }else{
                                                        loopDone9 = true;
                                                    }
                                                }
                                            }
                                            // call the converter utility  to convert and set the array
                                            
                                            object.setActiveWeeks((int[])
                                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                            int.class,list9));
                                                
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","assignDaysFlag").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAssignDaysFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","dayString").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDayString(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","dayShift").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDayShift(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setDayShift(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setDayShift(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","prevOriginID").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPrevOriginID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","prevTerritoryID").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPrevTerritoryID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","prevWeeks").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list15.add(reader.getElementText());
                                            
                                            //loop until we find a start element that is not part of this array
                                            boolean loopDone15 = false;
                                            while(!loopDone15){
                                                // Ensure we are at the EndElement
                                                while (!reader.isEndElement()){
                                                    reader.next();
                                                }
                                                // Step out of this element
                                                reader.next();
                                                // Step to next element event.
                                                while (!reader.isStartElement() && !reader.isEndElement())
                                                    reader.next();
                                                if (reader.isEndElement()){
                                                    //two continuous end elements means we are exiting the xml structure
                                                    loopDone15 = true;
                                                } else {
                                                    if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","prevWeeks").equals(reader.getName())){
                                                         list15.add(reader.getElementText());
                                                        
                                                    }else{
                                                        loopDone15 = true;
                                                    }
                                                }
                                            }
                                            // call the converter utility  to convert and set the array
                                            
                                            object.setPrevWeeks((int[])
                                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                            int.class,list15));
                                                
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","prevDays").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPrevDays(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","srvcTimeTypeOverride").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSrvcTimeTypeOverride(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorDaysRange").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorDaysRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setAnchorDaysRange(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setAnchorDaysRange(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorAnyDayFlag").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorAnyDayFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorDaysWrapFlag").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorDaysWrapFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorWeeksRange").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorWeeksRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setAnchorWeeksRange(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setAnchorWeeksRange(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorAnyWeekFlag").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorAnyWeekFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","anchorWeeksWrapFlag").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAnchorWeeksWrapFlag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","userDefinedFields").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list24.add(com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField.Factory.parse(reader));
                                                                
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone24 = false;
                                                        while(!loopDone24){
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
                                                                loopDone24 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","userDefinedFields").equals(reader.getName())){
                                                                    list24.add(com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone24 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setUserDefinedFields((com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedField.class,
                                                                list24));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","stops").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list25.add(com.freshdirect.routing.proxy.stub.transportation.PlanningStop.Factory.parse(reader));
                                                                
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
                                                                    list25.add(com.freshdirect.routing.proxy.stub.transportation.PlanningStop.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone25 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setStops((com.freshdirect.routing.proxy.stub.transportation.PlanningStop[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.transportation.PlanningStop.class,
                                                                list25));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.roadnet.com/RTS/TransportationSuite/TransportationWebService","rejectCode").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRejectCode(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setRejectCode(com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionRejectCode.Factory.parse(reader));
                                              
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
           
          