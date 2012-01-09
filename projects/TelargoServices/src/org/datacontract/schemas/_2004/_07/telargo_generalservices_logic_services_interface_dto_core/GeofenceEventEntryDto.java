
/**
 * GeofenceEventEntryDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core;
            

            /**
            *  GeofenceEventEntryDto bean class
            */
        
        public  class GeofenceEventEntryDto
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = GeofenceEventEntryDto
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
                        * field for Direction
                        */

                        
                                    protected org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.GeofenceDirectionEnumDto localDirection ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDirectionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.GeofenceDirectionEnumDto
                           */
                           public  org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.GeofenceDirectionEnumDto getDirection(){
                               return localDirection;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Direction
                               */
                               public void setDirection(org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.GeofenceDirectionEnumDto param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDirectionTracker = true;
                                       } else {
                                          localDirectionTracker = false;
                                              
                                       }
                                   
                                            this.localDirection=param;
                                    

                               }
                            

                        /**
                        * field for EnterLeaveOdo
                        */

                        
                                    protected int localEnterLeaveOdo ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEnterLeaveOdoTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getEnterLeaveOdo(){
                               return localEnterLeaveOdo;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EnterLeaveOdo
                               */
                               public void setEnterLeaveOdo(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localEnterLeaveOdoTracker = false;
                                              
                                       } else {
                                          localEnterLeaveOdoTracker = true;
                                       }
                                   
                                            this.localEnterLeaveOdo=param;
                                    

                               }
                            

                        /**
                        * field for EnterLeaveTime
                        */

                        
                                    protected int localEnterLeaveTime ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEnterLeaveTimeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getEnterLeaveTime(){
                               return localEnterLeaveTime;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EnterLeaveTime
                               */
                               public void setEnterLeaveTime(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localEnterLeaveTimeTracker = false;
                                              
                                       } else {
                                          localEnterLeaveTimeTracker = true;
                                       }
                                   
                                            this.localEnterLeaveTime=param;
                                    

                               }
                            

                        /**
                        * field for EventEntry
                        */

                        
                                    protected org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetEventEntryDto localEventEntry ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEventEntryTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetEventEntryDto
                           */
                           public  org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetEventEntryDto getEventEntry(){
                               return localEventEntry;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EventEntry
                               */
                               public void setEventEntry(org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetEventEntryDto param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localEventEntryTracker = true;
                                       } else {
                                          localEventEntryTracker = true;
                                              
                                       }
                                   
                                            this.localEventEntry=param;
                                    

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
                       GeofenceEventEntryDto.this.serialize(parentQName,factory,xmlWriter);
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
                           namespacePrefix+":GeofenceEventEntryDto",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "GeofenceEventEntryDto",
                           xmlWriter);
                   }

               
                   }
                if (localDirectionTracker){
                                            if (localDirection==null){
                                                 throw new org.apache.axis2.databinding.ADBException("Direction cannot be null!!");
                                            }
                                           localDirection.serialize(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","Direction"),
                                               factory,xmlWriter);
                                        } if (localEnterLeaveOdoTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"EnterLeaveOdo", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"EnterLeaveOdo");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("EnterLeaveOdo");
                                    }
                                
                                               if (localEnterLeaveOdo==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("EnterLeaveOdo cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnterLeaveOdo));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEnterLeaveTimeTracker){
                                    namespace = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"EnterLeaveTime", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"EnterLeaveTime");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("EnterLeaveTime");
                                    }
                                
                                               if (localEnterLeaveTime==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("EnterLeaveTime cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnterLeaveTime));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localEventEntryTracker){
                                    if (localEventEntry==null){

                                            java.lang.String namespace2 = "http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"EventEntry", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"EventEntry");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("EventEntry");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localEventEntry.serialize(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","EventEntry"),
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

                 if (localDirectionTracker){
                            elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "Direction"));
                            
                            
                                    if (localDirection==null){
                                         throw new org.apache.axis2.databinding.ADBException("Direction cannot be null!!");
                                    }
                                    elementList.add(localDirection);
                                } if (localEnterLeaveOdoTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "EnterLeaveOdo"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnterLeaveOdo));
                            } if (localEnterLeaveTimeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "EnterLeaveTime"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnterLeaveTime));
                            } if (localEventEntryTracker){
                            elementList.add(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core",
                                                                      "EventEntry"));
                            
                            
                                    elementList.add(localEventEntry==null?null:
                                    localEventEntry);
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
        public static GeofenceEventEntryDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            GeofenceEventEntryDto object =
                new GeofenceEventEntryDto();

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
                    
                            if (!"GeofenceEventEntryDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GeofenceEventEntryDto)com.telargo.tfc.gs.coreservice.v3.imports.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","Direction").equals(reader.getName())){
                                
                                                object.setDirection(org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.GeofenceDirectionEnumDto.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","EnterLeaveOdo").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEnterLeaveOdo(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setEnterLeaveOdo(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","EnterLeaveTime").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setEnterLeaveTime(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setEnterLeaveTime(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Telargo.GeneralServices.Logic.Services.Interface.Dto.Core","EventEntry").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setEventEntry(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setEventEntry(org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetEventEntryDto.Factory.parse(reader));
                                              
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
           
          