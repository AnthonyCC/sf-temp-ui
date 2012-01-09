
/**
 * CurrentStatusRoutesFilter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core;
            

            /**
            *  CurrentStatusRoutesFilter bean class
            */
        
        public  class CurrentStatusRoutesFilter extends org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters.FilterBase
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = CurrentStatusRoutesFilter
                Namespace URI = http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core
                Namespace Prefix = ns6
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core")){
                return "ns6";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for Driver
                        */

                        
                                    protected java.lang.String localDriver ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDriverTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDriver(){
                               return localDriver;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Driver
                               */
                               public void setDriver(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDriverTracker = true;
                                       } else {
                                          localDriverTracker = true;
                                              
                                       }
                                   
                                            this.localDriver=param;
                                    

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
                                          localIDTracker = true;
                                              
                                       }
                                   
                                            this.localID=param;
                                    

                               }
                            

                        /**
                        * field for InternalNumber
                        */

                        
                                    protected java.lang.String localInternalNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localInternalNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getInternalNumber(){
                               return localInternalNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param InternalNumber
                               */
                               public void setInternalNumber(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localInternalNumberTracker = true;
                                       } else {
                                          localInternalNumberTracker = true;
                                              
                                       }
                                   
                                            this.localInternalNumber=param;
                                    

                               }
                            

                        /**
                        * field for LoginLocation
                        */

                        
                                    protected java.lang.String localLoginLocation ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginLocationTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLoginLocation(){
                               return localLoginLocation;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginLocation
                               */
                               public void setLoginLocation(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLoginLocationTracker = true;
                                       } else {
                                          localLoginLocationTracker = true;
                                              
                                       }
                                   
                                            this.localLoginLocation=param;
                                    

                               }
                            

                        /**
                        * field for LoginTimeFrom
                        */

                        
                                    protected java.util.Calendar localLoginTimeFrom ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginTimeFromTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLoginTimeFrom(){
                               return localLoginTimeFrom;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginTimeFrom
                               */
                               public void setLoginTimeFrom(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLoginTimeFromTracker = true;
                                       } else {
                                          localLoginTimeFromTracker = true;
                                              
                                       }
                                   
                                            this.localLoginTimeFrom=param;
                                    

                               }
                            

                        /**
                        * field for LoginTimeTo
                        */

                        
                                    protected java.util.Calendar localLoginTimeTo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLoginTimeToTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLoginTimeTo(){
                               return localLoginTimeTo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LoginTimeTo
                               */
                               public void setLoginTimeTo(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLoginTimeToTracker = true;
                                       } else {
                                          localLoginTimeToTracker = true;
                                              
                                       }
                                   
                                            this.localLoginTimeTo=param;
                                    

                               }
                            

                        /**
                        * field for LogoutLocation
                        */

                        
                                    protected java.lang.String localLogoutLocation ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutLocationTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLogoutLocation(){
                               return localLogoutLocation;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutLocation
                               */
                               public void setLogoutLocation(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLogoutLocationTracker = true;
                                       } else {
                                          localLogoutLocationTracker = true;
                                              
                                       }
                                   
                                            this.localLogoutLocation=param;
                                    

                               }
                            

                        /**
                        * field for LogoutTimeFrom
                        */

                        
                                    protected java.util.Calendar localLogoutTimeFrom ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutTimeFromTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLogoutTimeFrom(){
                               return localLogoutTimeFrom;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutTimeFrom
                               */
                               public void setLogoutTimeFrom(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLogoutTimeFromTracker = true;
                                       } else {
                                          localLogoutTimeFromTracker = true;
                                              
                                       }
                                   
                                            this.localLogoutTimeFrom=param;
                                    

                               }
                            

                        /**
                        * field for LogoutTimeTo
                        */

                        
                                    protected java.util.Calendar localLogoutTimeTo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLogoutTimeToTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.util.Calendar
                           */
                           public  java.util.Calendar getLogoutTimeTo(){
                               return localLogoutTimeTo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LogoutTimeTo
                               */
                               public void setLogoutTimeTo(java.util.Calendar param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLogoutTimeToTracker = true;
                                       } else {
                                          localLogoutTimeToTracker = true;
                                              
                                       }
                                   
                                            this.localLogoutTimeTo=param;
                                    

                               }
                            

                        /**
                        * field for RegistrationNumber
                        */

                        
                                    protected java.lang.String localRegistrationNumber ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRegistrationNumberTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRegistrationNumber(){
                               return localRegistrationNumber;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RegistrationNumber
                               */
                               public void setRegistrationNumber(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRegistrationNumberTracker = true;
                                       } else {
                                          localRegistrationNumberTracker = true;
                                              
                                       }
                                   
                                            this.localRegistrationNumber=param;
                                    

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
                       CurrentStatusRoutesFilter.this.serialize(parentQName,factory,xmlWriter);
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
                

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":CurrentStatusRoutesFilter",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "CurrentStatusRoutesFilter",
                           xmlWriter);
                   }

                if (localLimitTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"Limit", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"Limit");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("Limit");
                                    }
                                
                                               if (localLimit==java.lang.Integer.MIN_VALUE) {
                                           
                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLimit));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localDriverTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"Driver", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"Driver");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("Driver");
                                    }
                                

                                          if (localDriver==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDriver);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localIDTracker){
                                    if (localID==null){

                                            java.lang.String namespace2 = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"ID", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"ID");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("ID");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localID.serialize(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","ID"),
                                        factory,xmlWriter);
                                    }
                                } if (localInternalNumberTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"InternalNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"InternalNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("InternalNumber");
                                    }
                                

                                          if (localInternalNumber==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localInternalNumber);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLoginLocationTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginLocation", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginLocation");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginLocation");
                                    }
                                

                                          if (localLoginLocation==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLoginLocation);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLoginTimeFromTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginTimeFrom", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginTimeFrom");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginTimeFrom");
                                    }
                                

                                          if (localLoginTimeFrom==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTimeFrom));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLoginTimeToTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LoginTimeTo", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LoginTimeTo");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LoginTimeTo");
                                    }
                                

                                          if (localLoginTimeTo==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTimeTo));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutLocationTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutLocation", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutLocation");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutLocation");
                                    }
                                

                                          if (localLogoutLocation==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLogoutLocation);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutTimeFromTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutTimeFrom", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutTimeFrom");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutTimeFrom");
                                    }
                                

                                          if (localLogoutTimeFrom==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTimeFrom));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLogoutTimeToTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"LogoutTimeTo", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"LogoutTimeTo");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("LogoutTimeTo");
                                    }
                                

                                          if (localLogoutTimeTo==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTimeTo));
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRegistrationNumberTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"RegistrationNumber", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"RegistrationNumber");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("RegistrationNumber");
                                    }
                                

                                          if (localRegistrationNumber==null){
                                              // write the nil attribute
                                              
                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRegistrationNumber);
                                            
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

                
                    attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance","type"));
                    attribList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","CurrentStatusRoutesFilter"));
                 if (localLimitTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters",
                                                                      "Limit"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLimit));
                            } if (localDriverTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "Driver"));
                                 
                                         elementList.add(localDriver==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDriver));
                                    } if (localIDTracker){
                            elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "ID"));
                            
                            
                                    elementList.add(localID==null?null:
                                    localID);
                                } if (localInternalNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "InternalNumber"));
                                 
                                         elementList.add(localInternalNumber==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInternalNumber));
                                    } if (localLoginLocationTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LoginLocation"));
                                 
                                         elementList.add(localLoginLocation==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginLocation));
                                    } if (localLoginTimeFromTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LoginTimeFrom"));
                                 
                                         elementList.add(localLoginTimeFrom==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTimeFrom));
                                    } if (localLoginTimeToTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LoginTimeTo"));
                                 
                                         elementList.add(localLoginTimeTo==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLoginTimeTo));
                                    } if (localLogoutLocationTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LogoutLocation"));
                                 
                                         elementList.add(localLogoutLocation==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutLocation));
                                    } if (localLogoutTimeFromTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LogoutTimeFrom"));
                                 
                                         elementList.add(localLogoutTimeFrom==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTimeFrom));
                                    } if (localLogoutTimeToTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "LogoutTimeTo"));
                                 
                                         elementList.add(localLogoutTimeTo==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLogoutTimeTo));
                                    } if (localRegistrationNumberTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core",
                                                                      "RegistrationNumber"));
                                 
                                         elementList.add(localRegistrationNumber==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegistrationNumber));
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
        public static CurrentStatusRoutesFilter parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            CurrentStatusRoutesFilter object =
                new CurrentStatusRoutesFilter();

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
                    
                            if (!"CurrentStatusRoutesFilter".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (CurrentStatusRoutesFilter)com.telargo.tfc.gs.coreservice.v3.imports.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters","Limit").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLimit(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                            
                                       } else {
                                           
                                           
                                                   object.setLimit(java.lang.Integer.MIN_VALUE);
                                               
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setLimit(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","Driver").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDriver(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","ID").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setID(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setID(com.microsoft.schemas._2003._10.serialization.Guid.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","InternalNumber").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setInternalNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LoginLocation").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginLocation(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LoginTimeFrom").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginTimeFrom(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LoginTimeTo").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLoginTimeTo(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LogoutLocation").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutLocation(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LogoutTimeFrom").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutTimeFrom(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","LogoutTimeTo").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLogoutTimeTo(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Filters.Core","RegistrationNumber").equals(reader.getName())){
                                
                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){
                                    
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegistrationNumber(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                            
                                       } else {
                                           
                                           
                                           reader.getElementText(); // throw away text nodes if any.
                                       }
                                      
                                        reader.next();
                                    
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
           
          