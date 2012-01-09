
/**
 * AssetSessionDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core;
            

            /**
            *  AssetSessionDto bean class
            */
        
        public  class AssetSessionDto
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = AssetSessionDto
                Namespace URI = http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core
                Namespace Prefix = ns4
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core")){
                return "ns4";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for AssetID
                        */

                        
                                    protected com.microsoft.schemas._2003._10.serialization.Guid localAssetID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAssetIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.microsoft.schemas._2003._10.serialization.Guid
                           */
                           public  com.microsoft.schemas._2003._10.serialization.Guid getAssetID(){
                               return localAssetID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AssetID
                               */
                               public void setAssetID(com.microsoft.schemas._2003._10.serialization.Guid param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localAssetIDTracker = true;
                                       } else {
                                          localAssetIDTracker = false;
                                              
                                       }
                                   
                                            this.localAssetID=param;
                                    

                               }
                            

                        /**
                        * field for Distance
                        */

                        
                                    protected double localDistance ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDistanceTracker = false ;
                           

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
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (java.lang.Double.isNaN(param)) {
                                           localDistanceTracker = true;
                                              
                                       } else {
                                          localDistanceTracker = true;
                                       }
                                   
                                            this.localDistance=param;
                                    

                               }
                            

                        /**
                        * field for DistanceUnit
                        */

                        
                                    protected java.lang.String localDistanceUnit ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDistanceUnitTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDistanceUnit(){
                               return localDistanceUnit;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DistanceUnit
                               */
                               public void setDistanceUnit(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDistanceUnitTracker = true;
                                       } else {
                                          localDistanceUnitTracker = true;
                                              
                                       }
                                   
                                            this.localDistanceUnit=param;
                                    

                               }
                            

                        /**
                        * field for DriverName
                        */

                        
                                    protected java.lang.String localDriverName ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriverNameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDriverName(){
                               return localDriverName;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverName
                               */
                               public void setDriverName(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDriverNameTracker = true;
                                       } else {
                                          localDriverNameTracker = true;
                                              
                                       }
                                   
                                            this.localDriverName=param;
                                    

                               }
                            

                        /**
                        * field for DriverScore
                        */

                        
                                    protected int localDriverScore ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriverScoreTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getDriverScore(){
                               return localDriverScore;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DriverScore
                               */
                               public void setDriverScore(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localDriverScoreTracker = true;
                                              
                                       } else {
                                          localDriverScoreTracker = true;
                                       }
                                   
                                            this.localDriverScore=param;
                                    

                               }
                            

                        /**
                        * field for DrivingTimeTicks
                        */

                        
                                    protected long localDrivingTimeTicks ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDrivingTimeTicksTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getDrivingTimeTicks(){
                               return localDrivingTimeTicks;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DrivingTimeTicks
                               */
                               public void setDrivingTimeTicks(long param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Long.MIN_VALUE) {
                                           localDrivingTimeTicksTracker = false;
                                              
                                       } else {
                                          localDrivingTimeTicksTracker = true;
                                       }
                                   
                                            this.localDrivingTimeTicks=param;
                                    

                               }
                            

                        /**
                        * field for ID
                        */

                        
                                    protected com.microsoft.schemas._2003._10.serialization.Guid localID ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localIDTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.microsoft.schemas._2003._10.serialization.Guid
                           */
                           public  com.microsoft.schemas._2003._10.serialization.Guid getID(){
                               return localID;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ID
                               */
                               public void setID(com.microsoft.schemas._2003._10.serialization.Guid param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localIDTracker = true;
                                       } else {
                                          localIDTracker = false;
                                              
                                       }
                                   
                                            this.localID=param;
                                    

                               }
                            

                        /**
                        * field for LoginLocationDescription
                        */

                        
                                    protected java.lang.String localLoginLocationDescription ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginLocationDescriptionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLoginLocationDescription(){
                               return localLoginLocationDescription;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginLocationDescription
                               */
                               public void setLoginLocationDescription(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLoginLocationDescriptionTracker = true;
                                       } else {
                                          localLoginLocationDescriptionTracker = true;
                                              
                                       }
                                   
                                            this.localLoginLocationDescription=param;
                                    

                               }
                            

                        /**
                        * field for LoginOdometerValue
                        */

                        
                                    protected int localLoginOdometerValue ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginOdometerValueTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLoginOdometerValue(){
                               return localLoginOdometerValue;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginOdometerValue
                               */
                               public void setLoginOdometerValue(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localLoginOdometerValueTracker = true;
                                              
                                       } else {
                                          localLoginOdometerValueTracker = true;
                                       }
                                   
                                            this.localLoginOdometerValue=param;
                                    

                               }
                            

                        /**
                        * field for LoginTime
                        */

                        
                                    protected java.util.Calendar localLoginTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLoginTime(){
                               return localLoginTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginTime
                               */
                               public void setLoginTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLoginTimeTracker = true;
                                       } else {
                                          localLoginTimeTracker = true;
                                              
                                       }
                                   
                                            this.localLoginTime=param;
                                    

                               }
                            

                        /**
                        * field for LogoutLocationDescription
                        */

                        
                                    protected java.lang.String localLogoutLocationDescription ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutLocationDescriptionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLogoutLocationDescription(){
                               return localLogoutLocationDescription;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutLocationDescription
                               */
                               public void setLogoutLocationDescription(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLogoutLocationDescriptionTracker = true;
                                       } else {
                                          localLogoutLocationDescriptionTracker = true;
                                              
                                       }
                                   
                                            this.localLogoutLocationDescription=param;
                                    

                               }
                            

                        /**
                        * field for LogoutOdometerValue
                        */

                        
                                    protected int localLogoutOdometerValue ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutOdometerValueTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getLogoutOdometerValue(){
                               return localLogoutOdometerValue;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutOdometerValue
                               */
                               public void setLogoutOdometerValue(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localLogoutOdometerValueTracker = true;
                                              
                                       } else {
                                          localLogoutOdometerValueTracker = true;
                                       }
                                   
                                            this.localLogoutOdometerValue=param;
                                    

                               }
                            

                        /**
                        * field for LogoutTime
                        */

                        
                                    protected java.util.Calendar localLogoutTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLogoutTime(){
                               return localLogoutTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutTime
                               */
                               public void setLogoutTime(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLogoutTimeTracker = true;
                                       } else {
                                          localLogoutTimeTracker = true;
                                              
                                       }
                                   
                                            this.localLogoutTime=param;
                                    

                               }
                            

                        /**
                        * field for MaxSpeed
                        */

                        
                                    protected double localMaxSpeed ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMaxSpeedTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getMaxSpeed(){
                               return localMaxSpeed;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaxSpeed
                               */
                               public void setMaxSpeed(double param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (java.lang.Double.isNaN(param)) {
                                           localMaxSpeedTracker = true;
                                              
                                       } else {
                                          localMaxSpeedTracker = true;
                                       }
                                   
                                            this.localMaxSpeed=param;
                                    

                               }
                            

                        /**
                        * field for MaxSpeedUnit
                        */

                        
                                    protected java.lang.String localMaxSpeedUnit ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMaxSpeedUnitTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getMaxSpeedUnit(){
                               return localMaxSpeedUnit;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaxSpeedUnit
                               */
                               public void setMaxSpeedUnit(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localMaxSpeedUnitTracker = true;
                                       } else {
                                          localMaxSpeedUnitTracker = true;
                                              
                                       }
                                   
                                            this.localMaxSpeedUnit=param;
                                    

                               }
                            

                        /**
                        * field for OdometerValueUnit
                        */

                        
                                    protected java.lang.String localOdometerValueUnit ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOdometerValueUnitTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getOdometerValueUnit(){
                               return localOdometerValueUnit;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param OdometerValueUnit
                               */
                               public void setOdometerValueUnit(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localOdometerValueUnitTracker = true;
                                       } else {
                                          localOdometerValueUnitTracker = true;
                                              
                                       }
                                   
                                            this.localOdometerValueUnit=param;
                                    

                               }
                            

                        /**
                        * field for TotalTripTimeTicks
                        */

                        
                                    protected long localTotalTripTimeTicks ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTotalTripTimeTicksTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getTotalTripTimeTicks(){
                               return localTotalTripTimeTicks;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TotalTripTimeTicks
                               */
                               public void setTotalTripTimeTicks(long param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Long.MIN_VALUE) {
                                           localTotalTripTimeTicksTracker = false;
                                              
                                       } else {
                                          localTotalTripTimeTicksTracker = true;
                                       }
                                   
                                            this.localTotalTripTimeTicks=param;
                                    

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
                       AssetSessionDto.this.serialize(parentQName,factory,xmlWriter);
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
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":AssetSessionDto",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "AssetSessionDto",
                           xmlWriter);
                   }

               
                   }
                if (localAssetIDTracker){
                                            if (localAssetID==null){
                                                 throw new org.apache.axis2.databinding.ADBException("AssetID cannot be null!!");
                                            }
                                           localAssetID.serialize(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","AssetID"),
                                               factory,xmlWriter);
                                        } if (localDistanceTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"Distance", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"Distance");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("Distance");
                                    }
                                
                                               if (java.lang.Double.isNaN(localDistance)) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistance));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDistanceUnitTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"DistanceUnit", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"DistanceUnit");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("DistanceUnit");
                                    }
                                

                                          if (localDistanceUnit==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDistanceUnit);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDriverNameTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"DriverName", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"DriverName");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("DriverName");
                                    }
                                

                                          if (localDriverName==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDriverName);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDriverScoreTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"DriverScore", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"DriverScore");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("DriverScore");
                                    }
                                
                                               if (localDriverScore==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverScore));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDrivingTimeTicksTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"DrivingTimeTicks", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"DrivingTimeTicks");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("DrivingTimeTicks");
                                    }
                                
                                               if (localDrivingTimeTicks==java.lang.Long.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("DrivingTimeTicks cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDrivingTimeTicks));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localIDTracker){
                                            if (localID==null){
                                                 throw new org.apache.axis2.databinding.ADBException("ID cannot be null!!");
                                            }
                                           localID.serialize(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","ID"),
                                               factory,xmlWriter);
                                        } if (localLoginLocationDescriptionTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginLocationDescription", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginLocationDescription");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginLocationDescription");
                                    }
                                

                                          if (localLoginLocationDescription==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLoginLocationDescription);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLoginOdometerValueTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginOdometerValue", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginOdometerValue");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginOdometerValue");
                                    }
                                
                                               if (localLoginOdometerValue==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginOdometerValue));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLoginTimeTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginTime");
                                    }
                                

                                          if (localLoginTime==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutLocationDescriptionTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutLocationDescription", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutLocationDescription");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutLocationDescription");
                                    }
                                

                                          if (localLogoutLocationDescription==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLogoutLocationDescription);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutOdometerValueTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutOdometerValue", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutOdometerValue");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutOdometerValue");
                                    }
                                
                                               if (localLogoutOdometerValue==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutOdometerValue));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutTimeTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutTime");
                                    }
                                

                                          if (localLogoutTime==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTime));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localMaxSpeedTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"MaxSpeed", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"MaxSpeed");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("MaxSpeed");
                                    }
                                
                                               if (java.lang.Double.isNaN(localMaxSpeed)) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxSpeed));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localMaxSpeedUnitTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"MaxSpeedUnit", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"MaxSpeedUnit");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("MaxSpeedUnit");
                                    }
                                

                                          if (localMaxSpeedUnit==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localMaxSpeedUnit);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localOdometerValueUnitTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"OdometerValueUnit", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"OdometerValueUnit");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("OdometerValueUnit");
                                    }
                                

                                          if (localOdometerValueUnit==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localOdometerValueUnit);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localTotalTripTimeTicksTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"TotalTripTimeTicks", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"TotalTripTimeTicks");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("TotalTripTimeTicks");
                                    }
                                
                                               if (localTotalTripTimeTicks==java.lang.Long.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("TotalTripTimeTicks cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalTripTimeTicks));
                                               }
                                    
                                   xmlWriter.writeEndElement();
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

                 if (localAssetIDTracker){
                            elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "AssetID"));
                            
                            
                                    if (localAssetID==null){
                                         throw new org.apache.axis2.databinding.ADBException("AssetID cannot be null!!");
                                    }
                                    elementList.add(localAssetID);
                                } if (localDistanceTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "Distance"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistance));
                            } if (localDistanceUnitTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "DistanceUnit"));
                                 
                                         elementList.add(localDistanceUnit==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDistanceUnit));
                                    } if (localDriverNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "DriverName"));
                                 
                                         elementList.add(localDriverName==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverName));
                                    } if (localDriverScoreTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "DriverScore"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriverScore));
                            } if (localDrivingTimeTicksTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "DrivingTimeTicks"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDrivingTimeTicks));
                            } if (localIDTracker){
                            elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "ID"));
                            
                            
                                    if (localID==null){
                                         throw new org.apache.axis2.databinding.ADBException("ID cannot be null!!");
                                    }
                                    elementList.add(localID);
                                } if (localLoginLocationDescriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LoginLocationDescription"));
                                 
                                         elementList.add(localLoginLocationDescription==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginLocationDescription));
                                    } if (localLoginOdometerValueTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LoginOdometerValue"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginOdometerValue));
                            } if (localLoginTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LoginTime"));
                                 
                                         elementList.add(localLoginTime==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTime));
                                    } if (localLogoutLocationDescriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LogoutLocationDescription"));
                                 
                                         elementList.add(localLogoutLocationDescription==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutLocationDescription));
                                    } if (localLogoutOdometerValueTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LogoutOdometerValue"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutOdometerValue));
                            } if (localLogoutTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "LogoutTime"));
                                 
                                         elementList.add(localLogoutTime==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTime));
                                    } if (localMaxSpeedTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "MaxSpeed"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxSpeed));
                            } if (localMaxSpeedUnitTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "MaxSpeedUnit"));
                                 
                                         elementList.add(localMaxSpeedUnit==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxSpeedUnit));
                                    } if (localOdometerValueUnitTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "OdometerValueUnit"));
                                 
                                         elementList.add(localOdometerValueUnit==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOdometerValueUnit));
                                    } if (localTotalTripTimeTicksTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "TotalTripTimeTicks"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalTripTimeTicks));
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
        public static AssetSessionDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            AssetSessionDto object =
                new AssetSessionDto();

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
                    
                            if (!"AssetSessionDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AssetSessionDto)com.telargo.tfc.gs.coreservice.v3.imports.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","AssetID").equals(reader.getName())){
                                
                                                object.setAssetID(com.microsoft.schemas._2003._10.serialization.Guid.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","Distance").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDistance(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setDistance(java.lang.Double.NaN);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setDistance(java.lang.Double.NaN);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","DistanceUnit").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDistanceUnit(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","DriverName").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","DriverScore").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriverScore(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setDriverScore(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setDriverScore(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","DrivingTimeTicks").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDrivingTimeTicks(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setDrivingTimeTicks(java.lang.Long.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","ID").equals(reader.getName())){
                                
                                                object.setID(com.microsoft.schemas._2003._10.serialization.Guid.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LoginLocationDescription").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginLocationDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LoginOdometerValue").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginOdometerValue(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setLoginOdometerValue(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setLoginOdometerValue(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LoginTime").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LogoutLocationDescription").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutLocationDescription(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LogoutOdometerValue").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutOdometerValue(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setLogoutOdometerValue(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setLogoutOdometerValue(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","LogoutTime").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","MaxSpeed").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMaxSpeed(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setMaxSpeed(java.lang.Double.NaN);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setMaxSpeed(java.lang.Double.NaN);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","MaxSpeedUnit").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMaxSpeedUnit(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","OdometerValueUnit").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setOdometerValueUnit(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","TotalTripTimeTicks").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTotalTripTimeTicks(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setTotalTripTimeTicks(java.lang.Long.MIN_VALUE);
                                           
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
           
          