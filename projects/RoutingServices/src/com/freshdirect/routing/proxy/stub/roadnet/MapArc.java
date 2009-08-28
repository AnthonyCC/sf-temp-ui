
/**
 * MapArc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.roadnet;
            

            /**
            *  MapArc bean class
            */
        
        public  class MapArc extends com.freshdirect.routing.proxy.stub.roadnet.Arc
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = MapArc
                Namespace URI = http://www.upslogisticstech.com/UPSLT/RouteNetWebService
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.upslogisticstech.com/UPSLT/RouteNetWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for DefaultName
                        */

                        
                                    protected java.lang.String localDefaultName ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDefaultNameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getDefaultName(){
                               return localDefaultName;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param DefaultName
                               */
                               public void setDefaultName(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localDefaultNameTracker = true;
                                       } else {
                                          localDefaultNameTracker = false;
                                              
                                       }
                                   
                                            this.localDefaultName=param;
                                    

                               }
                            

                        /**
                        * field for StreetType
                        */

                        
                                    protected java.lang.String localStreetType ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStreetTypeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getStreetType(){
                               return localStreetType;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StreetType
                               */
                               public void setStreetType(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localStreetTypeTracker = true;
                                       } else {
                                          localStreetTypeTracker = false;
                                              
                                       }
                                   
                                            this.localStreetType=param;
                                    

                               }
                            

                        /**
                        * field for PreDirection
                        */

                        
                                    protected java.lang.String localPreDirection ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPreDirectionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPreDirection(){
                               return localPreDirection;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PreDirection
                               */
                               public void setPreDirection(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPreDirectionTracker = true;
                                       } else {
                                          localPreDirectionTracker = false;
                                              
                                       }
                                   
                                            this.localPreDirection=param;
                                    

                               }
                            

                        /**
                        * field for PostDirection
                        */

                        
                                    protected java.lang.String localPostDirection ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPostDirectionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPostDirection(){
                               return localPostDirection;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PostDirection
                               */
                               public void setPostDirection(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPostDirectionTracker = true;
                                       } else {
                                          localPostDirectionTracker = false;
                                              
                                       }
                                   
                                            this.localPostDirection=param;
                                    

                               }
                            

                        /**
                        * field for LeftLowRange
                        */

                        
                                    protected java.lang.String localLeftLowRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLeftLowRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLeftLowRange(){
                               return localLeftLowRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LeftLowRange
                               */
                               public void setLeftLowRange(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLeftLowRangeTracker = true;
                                       } else {
                                          localLeftLowRangeTracker = false;
                                              
                                       }
                                   
                                            this.localLeftLowRange=param;
                                    

                               }
                            

                        /**
                        * field for LeftHighRange
                        */

                        
                                    protected java.lang.String localLeftHighRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLeftHighRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getLeftHighRange(){
                               return localLeftHighRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param LeftHighRange
                               */
                               public void setLeftHighRange(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localLeftHighRangeTracker = true;
                                       } else {
                                          localLeftHighRangeTracker = false;
                                              
                                       }
                                   
                                            this.localLeftHighRange=param;
                                    

                               }
                            

                        /**
                        * field for RightLowRange
                        */

                        
                                    protected java.lang.String localRightLowRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRightLowRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRightLowRange(){
                               return localRightLowRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RightLowRange
                               */
                               public void setRightLowRange(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRightLowRangeTracker = true;
                                       } else {
                                          localRightLowRangeTracker = false;
                                              
                                       }
                                   
                                            this.localRightLowRange=param;
                                    

                               }
                            

                        /**
                        * field for RightHighRange
                        */

                        
                                    protected java.lang.String localRightHighRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRightHighRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRightHighRange(){
                               return localRightHighRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RightHighRange
                               */
                               public void setRightHighRange(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRightHighRangeTracker = true;
                                       } else {
                                          localRightHighRangeTracker = false;
                                              
                                       }
                                   
                                            this.localRightHighRange=param;
                                    

                               }
                            

                        /**
                        * field for Region1
                        */

                        
                                    protected java.lang.String localRegion1 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRegion1Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRegion1(){
                               return localRegion1;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Region1
                               */
                               public void setRegion1(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRegion1Tracker = true;
                                       } else {
                                          localRegion1Tracker = false;
                                              
                                       }
                                   
                                            this.localRegion1=param;
                                    

                               }
                            

                        /**
                        * field for Region2
                        */

                        
                                    protected java.lang.String localRegion2 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRegion2Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRegion2(){
                               return localRegion2;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Region2
                               */
                               public void setRegion2(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRegion2Tracker = true;
                                       } else {
                                          localRegion2Tracker = false;
                                              
                                       }
                                   
                                            this.localRegion2=param;
                                    

                               }
                            

                        /**
                        * field for Region3
                        */

                        
                                    protected java.lang.String localRegion3 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRegion3Tracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getRegion3(){
                               return localRegion3;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Region3
                               */
                               public void setRegion3(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localRegion3Tracker = true;
                                       } else {
                                          localRegion3Tracker = false;
                                              
                                       }
                                   
                                            this.localRegion3=param;
                                    

                               }
                            

                        /**
                        * field for PostalCode
                        */

                        
                                    protected java.lang.String localPostalCode ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPostalCodeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getPostalCode(){
                               return localPostalCode;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param PostalCode
                               */
                               public void setPostalCode(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localPostalCodeTracker = true;
                                       } else {
                                          localPostalCodeTracker = false;
                                              
                                       }
                                   
                                            this.localPostalCode=param;
                                    

                               }
                            

                        /**
                        * field for Country
                        */

                        
                                    protected java.lang.String localCountry ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCountryTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getCountry(){
                               return localCountry;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Country
                               */
                               public void setCountry(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localCountryTracker = true;
                                       } else {
                                          localCountryTracker = false;
                                              
                                       }
                                   
                                            this.localCountry=param;
                                    

                               }
                            

                        /**
                        * field for FormattedRange
                        */

                        
                                    protected java.lang.String localFormattedRange ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFormattedRangeTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFormattedRange(){
                               return localFormattedRange;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FormattedRange
                               */
                               public void setFormattedRange(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFormattedRangeTracker = true;
                                       } else {
                                          localFormattedRangeTracker = false;
                                              
                                       }
                                   
                                            this.localFormattedRange=param;
                                    

                               }
                            

                        /**
                        * field for FormattedName
                        */

                        
                                    protected java.lang.String localFormattedName ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFormattedNameTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFormattedName(){
                               return localFormattedName;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FormattedName
                               */
                               public void setFormattedName(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFormattedNameTracker = true;
                                       } else {
                                          localFormattedNameTracker = false;
                                              
                                       }
                                   
                                            this.localFormattedName=param;
                                    

                               }
                            

                        /**
                        * field for FormattedRegion
                        */

                        
                                    protected java.lang.String localFormattedRegion ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFormattedRegionTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  java.lang.String getFormattedRegion(){
                               return localFormattedRegion;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FormattedRegion
                               */
                               public void setFormattedRegion(java.lang.String param){
                            
                                       if (param != null){
                                          //update the setting tracker
                                          localFormattedRegionTracker = true;
                                       } else {
                                          localFormattedRegionTracker = false;
                                              
                                       }
                                   
                                            this.localFormattedRegion=param;
                                    

                               }
                            

                        /**
                        * field for Vertices
                        * This was an Array!
                        */

                        
                                    protected com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] localVertices ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localVerticesTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return com.freshdirect.routing.proxy.stub.roadnet.MapPoint[]
                           */
                           public  com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] getVertices(){
                               return localVertices;
                           }

                           
                        


                               
                              /**
                               * validate the array for Vertices
                               */
                              protected void validateVertices(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] param){
                             
                              }


                             /**
                              * Auto generated setter method
                              * @param param Vertices
                              */
                              public void setVertices(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] param){
                              
                                   validateVertices(param);

                               
                                          if (param != null){
                                             //update the setting tracker
                                             localVerticesTracker = true;
                                          } else {
                                             localVerticesTracker = false;
                                                 
                                          }
                                      
                                      this.localVertices=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param com.freshdirect.routing.proxy.stub.roadnet.MapPoint
                             */
                             public void addVertices(com.freshdirect.routing.proxy.stub.roadnet.MapPoint param){
                                   if (localVertices == null){
                                   localVertices = new com.freshdirect.routing.proxy.stub.roadnet.MapPoint[]{};
                                   }

                            
                                 //update the setting tracker
                                localVerticesTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localVertices);
                               list.add(param);
                               this.localVertices =
                             (com.freshdirect.routing.proxy.stub.roadnet.MapPoint[])list.toArray(
                            new com.freshdirect.routing.proxy.stub.roadnet.MapPoint[list.size()]);

                             }
                             

                        /**
                        * field for Weight
                        */

                        
                                    protected int localWeight ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getWeight(){
                               return localWeight;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Weight
                               */
                               public void setWeight(int param){
                            
                                            this.localWeight=param;
                                    

                               }
                            

                        /**
                        * field for Height
                        */

                        
                                    protected int localHeight ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getHeight(){
                               return localHeight;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Height
                               */
                               public void setHeight(int param){
                            
                                            this.localHeight=param;
                                    

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
                       MapArc.this.serialize(parentQName,factory,xmlWriter);
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
                

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.upslogisticstech.com/UPSLT/RouteNetWebService");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":MapArc",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "MapArc",
                           xmlWriter);
                   }

               
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
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
                              if (localArcIdentityTracker){
                                    if (localArcIdentity==null){

                                            java.lang.String namespace2 = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";

                                        if (! namespace2.equals("")) {
                                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                                            if (prefix2 == null) {
                                                prefix2 = generatePrefix(namespace2);

                                                xmlWriter.writeStartElement(prefix2,"arcIdentity", namespace2);
                                                xmlWriter.writeNamespace(prefix2, namespace2);
                                                xmlWriter.setPrefix(prefix2, namespace2);

                                            } else {
                                                xmlWriter.writeStartElement(namespace2,"arcIdentity");
                                            }

                                        } else {
                                            xmlWriter.writeStartElement("arcIdentity");
                                        }


                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localArcIdentity.serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","arcIdentity"),
                                        factory,xmlWriter);
                                    }
                                }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"lineWidth", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"lineWidth");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("lineWidth");
                                    }
                                
                                               if (localLineWidth==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("lineWidth cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLineWidth));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localDefaultNameTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"defaultName", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"defaultName");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("defaultName");
                                    }
                                

                                          if (localDefaultName==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("defaultName cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localDefaultName);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localStreetTypeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"streetType", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"streetType");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("streetType");
                                    }
                                

                                          if (localStreetType==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("streetType cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localStreetType);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPreDirectionTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"preDirection", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"preDirection");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("preDirection");
                                    }
                                

                                          if (localPreDirection==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("preDirection cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPreDirection);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPostDirectionTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"postDirection", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"postDirection");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("postDirection");
                                    }
                                

                                          if (localPostDirection==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("postDirection cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPostDirection);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLeftLowRangeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"leftLowRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"leftLowRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("leftLowRange");
                                    }
                                

                                          if (localLeftLowRange==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("leftLowRange cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLeftLowRange);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localLeftHighRangeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"leftHighRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"leftHighRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("leftHighRange");
                                    }
                                

                                          if (localLeftHighRange==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("leftHighRange cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localLeftHighRange);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRightLowRangeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"rightLowRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"rightLowRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("rightLowRange");
                                    }
                                

                                          if (localRightLowRange==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("rightLowRange cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRightLowRange);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRightHighRangeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"rightHighRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"rightHighRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("rightHighRange");
                                    }
                                

                                          if (localRightHighRange==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("rightHighRange cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRightHighRange);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRegion1Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"region1", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"region1");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("region1");
                                    }
                                

                                          if (localRegion1==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("region1 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRegion1);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRegion2Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"region2", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"region2");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("region2");
                                    }
                                

                                          if (localRegion2==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("region2 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRegion2);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localRegion3Tracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"region3", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"region3");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("region3");
                                    }
                                

                                          if (localRegion3==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("region3 cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localRegion3);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localPostalCodeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"postalCode", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"postalCode");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("postalCode");
                                    }
                                

                                          if (localPostalCode==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("postalCode cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localPostalCode);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localCountryTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"country", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"country");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("country");
                                    }
                                

                                          if (localCountry==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("country cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localCountry);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFormattedRangeTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"formattedRange", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"formattedRange");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("formattedRange");
                                    }
                                

                                          if (localFormattedRange==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("formattedRange cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFormattedRange);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFormattedNameTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"formattedName", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"formattedName");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("formattedName");
                                    }
                                

                                          if (localFormattedName==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("formattedName cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFormattedName);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localFormattedRegionTracker){
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"formattedRegion", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"formattedRegion");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("formattedRegion");
                                    }
                                

                                          if (localFormattedRegion==null){
                                              // write the nil attribute
                                              
                                                     throw new org.apache.axis2.databinding.ADBException("formattedRegion cannot be null!!");
                                                  
                                          }else{

                                        
                                                   xmlWriter.writeCharacters(localFormattedRegion);
                                            
                                          }
                                    
                                   xmlWriter.writeEndElement();
                             } if (localVerticesTracker){
                                       if (localVertices!=null){
                                            for (int i = 0;i < localVertices.length;i++){
                                                if (localVertices[i] != null){
                                                 localVertices[i].serialize(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","vertices"),
                                                           factory,xmlWriter);
                                                } else {
                                                   
                                                        // we don't have to do any thing since minOccures is zero
                                                    
                                                }

                                            }
                                     } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("vertices cannot be null!!");
                                        
                                    }
                                 }
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"weight", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"weight");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("weight");
                                    }
                                
                                               if (localWeight==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("weight cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeight));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "http://www.upslogisticstech.com/UPSLT/RouteNetWebService";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"height", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"height");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("height");
                                    }
                                
                                               if (localHeight==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("height cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));
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

                
                    attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance","type"));
                    attribList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","MapArc"));
                
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "color"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localColor));
                             if (localArcIdentityTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "arcIdentity"));
                            
                            
                                    elementList.add(localArcIdentity==null?null:
                                    localArcIdentity);
                                }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "lineWidth"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLineWidth));
                             if (localDefaultNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "defaultName"));
                                 
                                        if (localDefaultName != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultName));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("defaultName cannot be null!!");
                                        }
                                    } if (localStreetTypeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "streetType"));
                                 
                                        if (localStreetType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreetType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("streetType cannot be null!!");
                                        }
                                    } if (localPreDirectionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "preDirection"));
                                 
                                        if (localPreDirection != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPreDirection));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("preDirection cannot be null!!");
                                        }
                                    } if (localPostDirectionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "postDirection"));
                                 
                                        if (localPostDirection != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostDirection));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("postDirection cannot be null!!");
                                        }
                                    } if (localLeftLowRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "leftLowRange"));
                                 
                                        if (localLeftLowRange != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLeftLowRange));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("leftLowRange cannot be null!!");
                                        }
                                    } if (localLeftHighRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "leftHighRange"));
                                 
                                        if (localLeftHighRange != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLeftHighRange));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("leftHighRange cannot be null!!");
                                        }
                                    } if (localRightLowRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "rightLowRange"));
                                 
                                        if (localRightLowRange != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRightLowRange));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("rightLowRange cannot be null!!");
                                        }
                                    } if (localRightHighRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "rightHighRange"));
                                 
                                        if (localRightHighRange != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRightHighRange));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("rightHighRange cannot be null!!");
                                        }
                                    } if (localRegion1Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "region1"));
                                 
                                        if (localRegion1 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegion1));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("region1 cannot be null!!");
                                        }
                                    } if (localRegion2Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "region2"));
                                 
                                        if (localRegion2 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegion2));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("region2 cannot be null!!");
                                        }
                                    } if (localRegion3Tracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "region3"));
                                 
                                        if (localRegion3 != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRegion3));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("region3 cannot be null!!");
                                        }
                                    } if (localPostalCodeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "postalCode"));
                                 
                                        if (localPostalCode != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPostalCode));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("postalCode cannot be null!!");
                                        }
                                    } if (localCountryTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "country"));
                                 
                                        if (localCountry != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCountry));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("country cannot be null!!");
                                        }
                                    } if (localFormattedRangeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "formattedRange"));
                                 
                                        if (localFormattedRange != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFormattedRange));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("formattedRange cannot be null!!");
                                        }
                                    } if (localFormattedNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "formattedName"));
                                 
                                        if (localFormattedName != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFormattedName));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("formattedName cannot be null!!");
                                        }
                                    } if (localFormattedRegionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "formattedRegion"));
                                 
                                        if (localFormattedRegion != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFormattedRegion));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("formattedRegion cannot be null!!");
                                        }
                                    } if (localVerticesTracker){
                             if (localVertices!=null) {
                                 for (int i = 0;i < localVertices.length;i++){

                                    if (localVertices[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                          "vertices"));
                                         elementList.add(localVertices[i]);
                                    } else {
                                        
                                                // nothing to do
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("vertices cannot be null!!");
                                    
                             }

                        }
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "weight"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeight));
                            
                                      elementList.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService",
                                                                      "height"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));
                            

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
        public static MapArc parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            MapArc object =
                new MapArc();

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
                    
                            if (!"MapArc".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (MapArc)com.freshdirect.routing.proxy.stub.roadnet.ExtensionMapper.getTypeObject(
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","color").equals(reader.getName())){
                                
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
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","arcIdentity").equals(reader.getName())){
                                
                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setArcIdentity(null);
                                          reader.next();
                                            
                                            reader.next();
                                          
                                      }else{
                                    
                                                object.setArcIdentity(com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity.Factory.parse(reader));
                                              
                                        reader.next();
                                    }
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","lineWidth").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLineWidth(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","defaultName").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setDefaultName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","streetType").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setStreetType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","preDirection").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPreDirection(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","postDirection").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPostDirection(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","leftLowRange").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLeftLowRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","leftHighRange").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setLeftHighRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","rightLowRange").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRightLowRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","rightHighRange").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRightHighRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","region1").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegion1(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","region2").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegion2(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","region3").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setRegion3(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","postalCode").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setPostalCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","country").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setCountry(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","formattedRange").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFormattedRange(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","formattedName").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFormattedName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","formattedRegion").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setFormattedRegion(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","vertices").equals(reader.getName())){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list20.add(com.freshdirect.routing.proxy.stub.roadnet.MapPoint.Factory.parse(reader));
                                                                
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
                                                                if (new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","vertices").equals(reader.getName())){
                                                                    list20.add(com.freshdirect.routing.proxy.stub.roadnet.MapPoint.Factory.parse(reader));
                                                                        
                                                                }else{
                                                                    loopDone20 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        
                                                        object.setVertices((com.freshdirect.routing.proxy.stub.roadnet.MapPoint[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                com.freshdirect.routing.proxy.stub.roadnet.MapPoint.class,
                                                                list20));
                                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","weight").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setWeight(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService","height").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setHeight(
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
           
          