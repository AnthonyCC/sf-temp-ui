
/**
 * TimeZoneValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:47 EDT)
 */
            
                package com.freshdirect.routing.proxy.stub.report;
            

            /**
            *  TimeZoneValue bean class
            */
        
        public  class TimeZoneValue
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService",
                "TimeZoneValue",
                "ns1");

            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for TimeZoneValue
                        */

                        
                                    protected java.lang.String localTimeZoneValue ;
                                
                            private static java.util.HashMap _table_ = new java.util.HashMap();

                            // Constructor
                            
                                protected TimeZoneValue(java.lang.String value, boolean isRegisterValue) {
                                    localTimeZoneValue = value;
                                    if (isRegisterValue){
                                        
                                               _table_.put(localTimeZoneValue, this);
                                           
                                    }

                                }
                            
                                    public static final java.lang.String _tmzNone =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzNone");
                                
                                    public static final java.lang.String _tmzAzoresCapeVerdeIs =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAzoresCapeVerdeIs");
                                
                                    public static final java.lang.String _tmzMidAtlantic =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMidAtlantic");
                                
                                    public static final java.lang.String _tmzBrasilia =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBrasilia");
                                
                                    public static final java.lang.String _tmzBuenosAiresGeorgetown =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBuenosAiresGeorgetown");
                                
                                    public static final java.lang.String _tmzNewfoundland =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzNewfoundland");
                                
                                    public static final java.lang.String _tmzAtlanticTimeCanada =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAtlanticTimeCanada");
                                
                                    public static final java.lang.String _tmzLaPaz =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzLaPaz");
                                
                                    public static final java.lang.String _tmzBogotaLima =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBogotaLima");
                                
                                    public static final java.lang.String _tmzEasternTimeUSCanada =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzEasternTimeUSCanada");
                                
                                    public static final java.lang.String _tmzIndianaEast =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzIndianaEast");
                                
                                    public static final java.lang.String _tmzCentralTimeUSCanada =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzCentralTimeUSCanada");
                                
                                    public static final java.lang.String _tmzMexicoCityTegucigalpa =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMexicoCityTegucigalpa");
                                
                                    public static final java.lang.String _tmzSaskatchewan =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzSaskatchewan");
                                
                                    public static final java.lang.String _tmzArizona =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzArizona");
                                
                                    public static final java.lang.String _tmzMountainTimeUSCanada =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMountainTimeUSCanada");
                                
                                    public static final java.lang.String _tmzPacificTimeUSCanada =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzPacificTimeUSCanada");
                                
                                    public static final java.lang.String _tmzAlaska =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAlaska");
                                
                                    public static final java.lang.String _tmzHawaii =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzHawaii");
                                
                                    public static final java.lang.String _tmzMidwayIslandSamoa =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMidwayIslandSamoa");
                                
                                    public static final java.lang.String _tmzEniwetokKwajalein =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzEniwetokKwajalein");
                                
                                    public static final java.lang.String _tmzDublinEdinburghLondonLisbon =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzDublinEdinburghLondonLisbon");
                                
                                    public static final java.lang.String _tmzMonroviaCasablanca =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMonroviaCasablanca");
                                
                                    public static final java.lang.String _tmzBerlinStockholmRomeBernBrusselsVienna =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBerlinStockholmRomeBernBrusselsVienna");
                                
                                    public static final java.lang.String _tmzParisMadridAmsterdam =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzParisMadridAmsterdam");
                                
                                    public static final java.lang.String _tmzPragueWarsawBudapest =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzPragueWarsawBudapest");
                                
                                    public static final java.lang.String _tmzAthensHelsinkiIstanbul =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAthensHelsinkiIstanbul");
                                
                                    public static final java.lang.String _tmzCairo =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzCairo");
                                
                                    public static final java.lang.String _tmzEasternEurope =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzEasternEurope");
                                
                                    public static final java.lang.String _tmzHararePretoria =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzHararePretoria");
                                
                                    public static final java.lang.String _tmzIsrael =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzIsrael");
                                
                                    public static final java.lang.String _tmzBaghdadKuwaitNairobiRiyadh =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBaghdadKuwaitNairobiRiyadh");
                                
                                    public static final java.lang.String _tmzMoscowStPetersburgKazanVolgograd =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMoscowStPetersburgKazanVolgograd");
                                
                                    public static final java.lang.String _tmzTehran =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzTehran");
                                
                                    public static final java.lang.String _tmzAbuDhabiMuscatTbilisi =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAbuDhabiMuscatTbilisi");
                                
                                    public static final java.lang.String _tmzKabul =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzKabul");
                                
                                    public static final java.lang.String _tmzIslamabadKarachiEkaterinburgTashkent =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzIslamabadKarachiEkaterinburgTashkent");
                                
                                    public static final java.lang.String _tmzBombayCalcuttaMadrasNewDelhiColombo =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBombayCalcuttaMadrasNewDelhiColombo");
                                
                                    public static final java.lang.String _tmzAlmatyDhaka =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAlmatyDhaka");
                                
                                    public static final java.lang.String _tmzBangkokJakartaHanoi =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBangkokJakartaHanoi");
                                
                                    public static final java.lang.String _tmzBeijingChongqingUrumqi =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBeijingChongqingUrumqi");
                                
                                    public static final java.lang.String _tmzHongKongPerthSingaporeTaipei =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzHongKongPerthSingaporeTaipei");
                                
                                    public static final java.lang.String _tmzTokyoOsakaSapporoSeoulYakutsk =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzTokyoOsakaSapporoSeoulYakutsk");
                                
                                    public static final java.lang.String _tmzAdelaide =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzAdelaide");
                                
                                    public static final java.lang.String _tmzDarwin =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzDarwin");
                                
                                    public static final java.lang.String _tmzMelbourneSydney =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMelbourneSydney");
                                
                                    public static final java.lang.String _tmzGuamPortMoresbyVladivostok =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzGuamPortMoresbyVladivostok");
                                
                                    public static final java.lang.String _tmzHobart =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzHobart");
                                
                                    public static final java.lang.String _tmzMagadanSolomonIsNewCaledonia =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzMagadanSolomonIsNewCaledonia");
                                
                                    public static final java.lang.String _tmzFijiKamchatkaMarshallIs =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzFijiKamchatkaMarshallIs");
                                
                                    public static final java.lang.String _tmzWellingtonAuckland =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzWellingtonAuckland");
                                
                                    public static final java.lang.String _tmzTijuanaBaja =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzTijuanaBaja");
                                
                                    public static final java.lang.String _tmzBrisbane =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzBrisbane");
                                
                                    public static final java.lang.String _tmzCaracas =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("tmzCaracas");
                                
                                public static final TimeZoneValue tmzNone =
                                    new TimeZoneValue(_tmzNone,true);
                            
                                public static final TimeZoneValue tmzAzoresCapeVerdeIs =
                                    new TimeZoneValue(_tmzAzoresCapeVerdeIs,true);
                            
                                public static final TimeZoneValue tmzMidAtlantic =
                                    new TimeZoneValue(_tmzMidAtlantic,true);
                            
                                public static final TimeZoneValue tmzBrasilia =
                                    new TimeZoneValue(_tmzBrasilia,true);
                            
                                public static final TimeZoneValue tmzBuenosAiresGeorgetown =
                                    new TimeZoneValue(_tmzBuenosAiresGeorgetown,true);
                            
                                public static final TimeZoneValue tmzNewfoundland =
                                    new TimeZoneValue(_tmzNewfoundland,true);
                            
                                public static final TimeZoneValue tmzAtlanticTimeCanada =
                                    new TimeZoneValue(_tmzAtlanticTimeCanada,true);
                            
                                public static final TimeZoneValue tmzLaPaz =
                                    new TimeZoneValue(_tmzLaPaz,true);
                            
                                public static final TimeZoneValue tmzBogotaLima =
                                    new TimeZoneValue(_tmzBogotaLima,true);
                            
                                public static final TimeZoneValue tmzEasternTimeUSCanada =
                                    new TimeZoneValue(_tmzEasternTimeUSCanada,true);
                            
                                public static final TimeZoneValue tmzIndianaEast =
                                    new TimeZoneValue(_tmzIndianaEast,true);
                            
                                public static final TimeZoneValue tmzCentralTimeUSCanada =
                                    new TimeZoneValue(_tmzCentralTimeUSCanada,true);
                            
                                public static final TimeZoneValue tmzMexicoCityTegucigalpa =
                                    new TimeZoneValue(_tmzMexicoCityTegucigalpa,true);
                            
                                public static final TimeZoneValue tmzSaskatchewan =
                                    new TimeZoneValue(_tmzSaskatchewan,true);
                            
                                public static final TimeZoneValue tmzArizona =
                                    new TimeZoneValue(_tmzArizona,true);
                            
                                public static final TimeZoneValue tmzMountainTimeUSCanada =
                                    new TimeZoneValue(_tmzMountainTimeUSCanada,true);
                            
                                public static final TimeZoneValue tmzPacificTimeUSCanada =
                                    new TimeZoneValue(_tmzPacificTimeUSCanada,true);
                            
                                public static final TimeZoneValue tmzAlaska =
                                    new TimeZoneValue(_tmzAlaska,true);
                            
                                public static final TimeZoneValue tmzHawaii =
                                    new TimeZoneValue(_tmzHawaii,true);
                            
                                public static final TimeZoneValue tmzMidwayIslandSamoa =
                                    new TimeZoneValue(_tmzMidwayIslandSamoa,true);
                            
                                public static final TimeZoneValue tmzEniwetokKwajalein =
                                    new TimeZoneValue(_tmzEniwetokKwajalein,true);
                            
                                public static final TimeZoneValue tmzDublinEdinburghLondonLisbon =
                                    new TimeZoneValue(_tmzDublinEdinburghLondonLisbon,true);
                            
                                public static final TimeZoneValue tmzMonroviaCasablanca =
                                    new TimeZoneValue(_tmzMonroviaCasablanca,true);
                            
                                public static final TimeZoneValue tmzBerlinStockholmRomeBernBrusselsVienna =
                                    new TimeZoneValue(_tmzBerlinStockholmRomeBernBrusselsVienna,true);
                            
                                public static final TimeZoneValue tmzParisMadridAmsterdam =
                                    new TimeZoneValue(_tmzParisMadridAmsterdam,true);
                            
                                public static final TimeZoneValue tmzPragueWarsawBudapest =
                                    new TimeZoneValue(_tmzPragueWarsawBudapest,true);
                            
                                public static final TimeZoneValue tmzAthensHelsinkiIstanbul =
                                    new TimeZoneValue(_tmzAthensHelsinkiIstanbul,true);
                            
                                public static final TimeZoneValue tmzCairo =
                                    new TimeZoneValue(_tmzCairo,true);
                            
                                public static final TimeZoneValue tmzEasternEurope =
                                    new TimeZoneValue(_tmzEasternEurope,true);
                            
                                public static final TimeZoneValue tmzHararePretoria =
                                    new TimeZoneValue(_tmzHararePretoria,true);
                            
                                public static final TimeZoneValue tmzIsrael =
                                    new TimeZoneValue(_tmzIsrael,true);
                            
                                public static final TimeZoneValue tmzBaghdadKuwaitNairobiRiyadh =
                                    new TimeZoneValue(_tmzBaghdadKuwaitNairobiRiyadh,true);
                            
                                public static final TimeZoneValue tmzMoscowStPetersburgKazanVolgograd =
                                    new TimeZoneValue(_tmzMoscowStPetersburgKazanVolgograd,true);
                            
                                public static final TimeZoneValue tmzTehran =
                                    new TimeZoneValue(_tmzTehran,true);
                            
                                public static final TimeZoneValue tmzAbuDhabiMuscatTbilisi =
                                    new TimeZoneValue(_tmzAbuDhabiMuscatTbilisi,true);
                            
                                public static final TimeZoneValue tmzKabul =
                                    new TimeZoneValue(_tmzKabul,true);
                            
                                public static final TimeZoneValue tmzIslamabadKarachiEkaterinburgTashkent =
                                    new TimeZoneValue(_tmzIslamabadKarachiEkaterinburgTashkent,true);
                            
                                public static final TimeZoneValue tmzBombayCalcuttaMadrasNewDelhiColombo =
                                    new TimeZoneValue(_tmzBombayCalcuttaMadrasNewDelhiColombo,true);
                            
                                public static final TimeZoneValue tmzAlmatyDhaka =
                                    new TimeZoneValue(_tmzAlmatyDhaka,true);
                            
                                public static final TimeZoneValue tmzBangkokJakartaHanoi =
                                    new TimeZoneValue(_tmzBangkokJakartaHanoi,true);
                            
                                public static final TimeZoneValue tmzBeijingChongqingUrumqi =
                                    new TimeZoneValue(_tmzBeijingChongqingUrumqi,true);
                            
                                public static final TimeZoneValue tmzHongKongPerthSingaporeTaipei =
                                    new TimeZoneValue(_tmzHongKongPerthSingaporeTaipei,true);
                            
                                public static final TimeZoneValue tmzTokyoOsakaSapporoSeoulYakutsk =
                                    new TimeZoneValue(_tmzTokyoOsakaSapporoSeoulYakutsk,true);
                            
                                public static final TimeZoneValue tmzAdelaide =
                                    new TimeZoneValue(_tmzAdelaide,true);
                            
                                public static final TimeZoneValue tmzDarwin =
                                    new TimeZoneValue(_tmzDarwin,true);
                            
                                public static final TimeZoneValue tmzMelbourneSydney =
                                    new TimeZoneValue(_tmzMelbourneSydney,true);
                            
                                public static final TimeZoneValue tmzGuamPortMoresbyVladivostok =
                                    new TimeZoneValue(_tmzGuamPortMoresbyVladivostok,true);
                            
                                public static final TimeZoneValue tmzHobart =
                                    new TimeZoneValue(_tmzHobart,true);
                            
                                public static final TimeZoneValue tmzMagadanSolomonIsNewCaledonia =
                                    new TimeZoneValue(_tmzMagadanSolomonIsNewCaledonia,true);
                            
                                public static final TimeZoneValue tmzFijiKamchatkaMarshallIs =
                                    new TimeZoneValue(_tmzFijiKamchatkaMarshallIs,true);
                            
                                public static final TimeZoneValue tmzWellingtonAuckland =
                                    new TimeZoneValue(_tmzWellingtonAuckland,true);
                            
                                public static final TimeZoneValue tmzTijuanaBaja =
                                    new TimeZoneValue(_tmzTijuanaBaja,true);
                            
                                public static final TimeZoneValue tmzBrisbane =
                                    new TimeZoneValue(_tmzBrisbane,true);
                            
                                public static final TimeZoneValue tmzCaracas =
                                    new TimeZoneValue(_tmzCaracas,true);
                            

                                public java.lang.String getValue() { return localTimeZoneValue;}

                                public boolean equals(java.lang.Object obj) {return (obj == this);}
                                public int hashCode() { return toString().hashCode();}
                                public java.lang.String toString() {
                                
                                        return localTimeZoneValue.toString();
                                    

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
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME){

                 public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                       TimeZoneValue.this.serialize(MY_QNAME,factory,xmlWriter);
                 }
               };
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               MY_QNAME,factory,dataSource);
            
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
            
                
                //We can safely assume an element has only one type associated with it
                
                            java.lang.String namespace = parentQName.getNamespaceURI();
                            java.lang.String localName = parentQName.getLocalPart();
                        
                            if (! namespace.equals("")) {
                                java.lang.String prefix = xmlWriter.getPrefix(namespace);

                                if (prefix == null) {
                                    prefix = generatePrefix(namespace);

                                    xmlWriter.writeStartElement(prefix, localName, namespace);
                                    xmlWriter.writeNamespace(prefix, namespace);
                                    xmlWriter.setPrefix(prefix, namespace);

                                } else {
                                    xmlWriter.writeStartElement(namespace, localName);
                                }

                            } else {
                                xmlWriter.writeStartElement(localName);
                            }

                            // add the type details if this is used in a simple type
                               if (serializeType){
                                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService");
                                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           namespacePrefix+":TimeZoneValue",
                                           xmlWriter);
                                   } else {
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           "TimeZoneValue",
                                           xmlWriter);
                                   }
                               }
                            
                                          if (localTimeZoneValue==null){
                                            
                                                     throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");
                                                
                                         }else{
                                        
                                                       xmlWriter.writeCharacters(localTimeZoneValue);
                                            
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


        
                
                //We can safely assume an element has only one type associated with it
                 return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME,
                            new java.lang.Object[]{
                            org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                            org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimeZoneValue)
                            },
                            null);

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        
                public static TimeZoneValue fromValue(java.lang.String value)
                      throws java.lang.IllegalArgumentException {
                    TimeZoneValue enumeration = (TimeZoneValue)
                       
                               _table_.get(value);
                           

                    if (enumeration==null) throw new java.lang.IllegalArgumentException();
                    return enumeration;
                }
                public static TimeZoneValue fromString(java.lang.String value,java.lang.String namespaceURI)
                      throws java.lang.IllegalArgumentException {
                    try {
                       
                                       return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));
                                   

                    } catch (java.lang.Exception e) {
                        throw new java.lang.IllegalArgumentException();
                    }
                }

                public static TimeZoneValue fromString(javax.xml.stream.XMLStreamReader xmlStreamReader,
                                                                    java.lang.String content) {
                    if (content.indexOf(":") > -1){
                        java.lang.String prefix = content.substring(0,content.indexOf(":"));
                        java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                        return TimeZoneValue.Factory.fromString(content,namespaceUri);
                    } else {
                       return TimeZoneValue.Factory.fromString(content,"");
                    }
                }
            

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static TimeZoneValue parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            TimeZoneValue object = null;
                // initialize a hash map to keep values
                java.util.Map attributeMap = new java.util.HashMap();
                java.util.List extraAttributeList = new java.util.ArrayList();
            

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                    
                while(!reader.isEndElement()) {
                    if (reader.isStartElement()  || reader.hasText()){
                
                                    java.lang.String content = reader.getElementText();
                                    
                                        if (content.indexOf(":") > 0) {
                                            // this seems to be a Qname so find the namespace and send
                                            prefix = content.substring(0, content.indexOf(":"));
                                            namespaceuri = reader.getNamespaceURI(prefix);
                                            object = TimeZoneValue.Factory.fromString(content,namespaceuri);
                                        } else {
                                            // this seems to be not a qname send and empty namespace incase of it is
                                            // check is done in fromString method
                                            object = TimeZoneValue.Factory.fromString(content,"");
                                        }
                                        
                                        
                             } else {
                                reader.next();
                             }  
                           }  // end of while loop
                        



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
          