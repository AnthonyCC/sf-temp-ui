/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.sap.helper.BasePriceInfo;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @author  mrose
 * @version
 */
public class TreeBuilder {

	private static Logger LOGGER = LoggerFactory.getInstance( TreeBuilder.class );    
	    
    private MaterialParser materialParser = null;
    private MaterialPriceParser materialPriceParser = null;
    private SalesUnitParser salesUnitParser = null;
    private VariantParser variantParser = null;
    private VariantPriceParser variantPriceParser = null;
    
    private LinkedList<BadDataException> exceptionList = null;
    
    /**
     * The collection of all classes in a batch
     * contains ErpClassModels keyed by their name
     */
    private HashMap<String, ErpClassModel> classes = null;
    
    /**
     * The collection of all materials in a batch
     * contains ErpMaterialModels keyed by their material number
     */
    private HashMap<String, ErpMaterialModel> materials = null;
    
    //
    // some comparators for sorting stuff
    //
    private Comparator<ErpCharacteristicModel> charComparator = new Comparator<ErpCharacteristicModel>() {
        public int compare(ErpCharacteristicModel char1, ErpCharacteristicModel char2) {
            return char1.getName().compareTo(char2.getName());
        }
    };
    
    private Comparator<ErpCharacteristicValueModel> charValueComparator = new Comparator<ErpCharacteristicValueModel>() {
        public int compare(ErpCharacteristicValueModel val1, ErpCharacteristicValueModel val2) {
            return val1.getName().compareTo(val2.getName());
        }
    };
    
    private Comparator<ErpMaterialPriceModel> matlPriceComparator = new Comparator<ErpMaterialPriceModel>() {
        public int compare(ErpMaterialPriceModel price1, ErpMaterialPriceModel price2) {
            if (price1.getScaleQuantity() == price2.getScaleQuantity()) return 0;
            else if (price1.getScaleQuantity() < price2.getScaleQuantity()) return -1;
            else return 1;
        }
    };
    
    private Comparator<ErpSalesUnitModel> salesUnitComparator = new Comparator<ErpSalesUnitModel>() {
        public int compare(ErpSalesUnitModel unit1, ErpSalesUnitModel unit2) {
            return unit1.getAlternativeUnit().compareTo(unit2.getAlternativeUnit());
        }
    };
    
    /** Creates new TreeBuilder */
    public TreeBuilder() {
        classes = new HashMap<String, ErpClassModel>();
        materials = new HashMap<String, ErpMaterialModel>();
        exceptionList = new LinkedList<BadDataException>();
    }
    
    public MaterialParser getMaterialParser(){ return materialParser; }
    
    public void setMaterialParser(MaterialParser parser) {
        materialParser = parser;
    }
    
    public MaterialPriceParser getMaterialPriceParser(){ return materialPriceParser; }
    
    public void setMaterialPriceParser(MaterialPriceParser parser) {
        materialPriceParser = parser;
    }
    
    public SalesUnitParser getSalesUnitParser(){ return salesUnitParser; }
    
    public void setSalesUnitParser(SalesUnitParser parser) {
        salesUnitParser = parser;
    }
    
    public VariantParser getVariantParser(){ return variantParser; }
    
    public void setVariantParser(VariantParser parser) {
        variantParser = parser;
    }
    
    public VariantPriceParser getVariantPriceParser(){ return variantPriceParser; }
    
    public void setVariantPriceParser(VariantPriceParser parser) {
        variantPriceParser = parser;
    }
    
    /**
     * indicates whether any build errors were found during a run
     *
     * @return true if no exceptions occurred during build
     */
    public boolean buildSuccessful() {
        return (this.exceptionList.size() == 0);
    }
    
    /** @return list of BadDataExceptions */
    public List<BadDataException> getBuildExceptions() {
    	return this.exceptionList;
    }
    
    public void build() throws BadDataException {
        build(false);
    }
    
    public void build(boolean verbose) throws BadDataException {
        //
        // build the class tree
        //
        buildClassTree();
        //
        // build the material tree
        //
        buildMaterialTree();
        //
        // some items from the class tree really belong in the material tree
        //
        pruneClassTree();
        //
        // print a report if requested
        //
        if (verbose) {
            printClassTree();
            printMaterialTree();
        }
    }
    
    public Map<String, ErpClassModel> getClasses() {
        return classes;
    }
    
    public Map<String, ErpMaterialModel> getMaterials() {
        return materials;
    }
    
    public Map<ErpMaterialModel, Map<String, Object>> getActiveMaterials() {
        return materialParser.getActiveMaterials();
    }
    
    public Map<ErpCharacteristicValuePriceModel, Map<String, String>> getCharacteristicValuePrices() {
        return variantPriceParser.getCharacteristicValuePrices();
    }
    
    private void buildClassTree() throws BadDataException {
        LOGGER.info("----- Building Class Tree -----");
        //
        // walk through all of the collected characteristics
        // and add them to their respective classes, creating
        // the classes along the way as necessary
        //
        Map<ErpCharacteristicModel, Map<String, String>> characs = variantParser.getCharacteristics();
        Iterator<ErpCharacteristicModel> keys = characs.keySet().iterator();
        while (keys.hasNext()) {
            ErpCharacteristicModel charac = keys.next();
            Map<String, String> extraInfo = characs.get(charac);
            //
            // find the class for each characteristic
            // create the class and add it the list of all
            // classes in this batch it if it wasn't found
            //
            String className = extraInfo.get(SAPConstants.CLASS);
            ErpClassModel erpClass = null;
            if (!classes.containsKey(className)) {
                erpClass = new ErpClassModel();
                erpClass.setSapId(className);
                classes.put(className, erpClass);
            } else {
                erpClass = classes.get(className);
            }
            //
            // add the characteristic to the class if it hasn't been added yet
            //
            if (!erpClass.hasCharacteristic(charac))
                erpClass.addCharacteristic(charac);
        }
        //
        // walk through all of the collected characteristic values
        // and add them to their characteristics
        //
        Map<ErpCharacteristicValueModel, Map<String, String>> characValues = variantPriceParser.getCharacteristicValues();
        Iterator<ErpCharacteristicValueModel> keys2 = characValues.keySet().iterator();
        while (keys2.hasNext()) {
            ErpCharacteristicValueModel characValue = keys2.next();
            Map<String, String> extraInfo = characValues.get(characValue);
            //
            // find the characteristic for each characteristic value
            //
            String className = extraInfo.get(SAPConstants.CLASS);
            String charName = extraInfo.get(SAPConstants.CHARACTERISTIC_NAME);
            ErpClassModel erpClass = classes.get(className);
            if (erpClass == null) {
                //
                // throw an exception and bail if the class for a characteristic value
                // hasn't been previously created
                //
                exceptionList.add(new BadDataException("Unable to find a matching Class \"" + className +"\" for CharacteristicValue \"" + characValue + "\"."));
            }
            ErpCharacteristicModel erpChar = erpClass.getCharacteristic(charName);
            if (erpChar == null) {
                //
                // throw an exception and bail if the characteristic for a characteristic value
                // hasn't been previously created
                //
                exceptionList.add(new BadDataException("Unable to find a matching Characteristic \"" + charName +"\" for CharacteristicValue \"" + characValue + "\"."));
            }
            //
            // add the characteristic value to the characteristic
            // if it hasn't been added yet
            //
            if (!erpChar.hasCharacteristicValue(characValue))
                erpChar.addCharacteristicValue(characValue);
        }
        //
        // go back and sort everything in the class tree
        //
        Iterator<ErpClassModel> classIter = classes.values().iterator();
        while (classIter.hasNext()) {
            ErpClassModel erpClass = classIter.next();
            List<ErpCharacteristicModel> chars = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics());
            Collections.sort(chars, charComparator);
            erpClass.setCharacteristics(chars);
            Iterator<ErpCharacteristicModel> charIter = erpClass.getCharacteristics().iterator();
            while (charIter.hasNext()) {
                ErpCharacteristicModel erpChar = charIter.next();
                List<ErpCharacteristicValueModel> charVals = new ArrayList<ErpCharacteristicValueModel>(erpChar.getCharacteristicValues());
                Collections.sort(charVals, charValueComparator);
                erpChar.setCharacteristicValues(charVals);
            }
        }
    }
    
    private void printClassTree() {
        LOGGER.info("----- Printing Class Tree -----");
        //
        // classes
        //
        Iterator<String> keys = classes.keySet().iterator();
        while (keys.hasNext()) {
            String className = keys.next();
            ErpClassModel erpClass = classes.get(className);
            LOGGER.debug(className);
            //
            // characteristics for class
            //
            Iterator<ErpCharacteristicModel> charIter = erpClass.getCharacteristics().iterator();
            while (charIter.hasNext()) {
                ErpCharacteristicModel erpChar = charIter.next();
                LOGGER.debug(erpChar.getName());
                //
                // characteristic values for characteristic
                //
                Iterator<ErpCharacteristicValueModel> charValIter = erpChar.getCharacteristicValues().iterator();
                while (charValIter.hasNext()) {
                    ErpCharacteristicValueModel erpCharVal = charValIter.next();
                    LOGGER.debug(erpCharVal.getName() + " : " + erpCharVal.getDescription());
                }
            }
        }
    }
    
    private void buildMaterialTree() throws BadDataException {
        LOGGER.info("----- Building Material Tree -----");
        //
        // get the material to class mappings
        //
        Map<String, Set<String>> materialClasses = variantParser.getMaterialClasses();
        //
        // get all of the material prices
        //
        Map<String, Set<ErpMaterialPriceModel>> materialPrices = materialPriceParser.getMaterialPrices();
        
        
        
        
        Map materialBasePrices=materialPriceParser.getMaterialBasePrices();
        //
        // get all of the sales units
        //
        HashMap<String, HashSet<ErpSalesUnitModel>> salesUnits = salesUnitParser.getSalesUnits();
        //
        // walk through all the active materials and add their
        // material prices and sales units
        //
        Map<ErpMaterialModel, Map<String, Object>> activeMaterials = materialParser.getActiveMaterials();
        Iterator<ErpMaterialModel> keys = activeMaterials.keySet().iterator();
        while (keys.hasNext()) {
            ErpMaterialModel material = keys.next();
            //
            // add the names of this material's classes to its extra info
            //
            Map<String, Object> extraInfo = activeMaterials.get(material);
            extraInfo.put(SAPConstants.CLASS, materialClasses.get(material.getSapId()));
            //
            // find the prices for this material
            //
            HashSet<ErpMaterialPriceModel> prices = (HashSet<ErpMaterialPriceModel>) materialPrices.get(material.getSapId());
            if (prices == null) {
                //
                // throw an exception and bail if no prices are found for a material
                // (not necessarily true.  maybe just log a warning?)
                //
                //exceptionList.add(new BadDataException("No prices were found for Material " + material.getSapId()));
                //
                material.setPrices(new LinkedList<ErpMaterialPriceModel>());
            } else {
                //
                // set the prices for the material, sorted by scale quantity
                //
                ArrayList<ErpMaterialPriceModel> priceList = new ArrayList<ErpMaterialPriceModel>(prices);
                boolean isDefaultZoneExists=false;
                for(int i=0;i<priceList.size();i++){
                	
                	ErpMaterialPriceModel pModel=priceList.get(i);
                	if(ErpServicesProperties.getMasterDefaultZoneId().equalsIgnoreCase(pModel.getSapZoneId())){
                		isDefaultZoneExists=true;
                	}
                }
                
                if(!isDefaultZoneExists){
                	exceptionList.add(new BadDataException("No Master Default Zone Id found for Material " + material.getSapId()));
                }
                
                if(materialBasePrices.containsKey(material.getSapId())) {
                	HashSet basePriceSet=(HashSet)materialBasePrices.get(material.getSapId());
                	if(basePriceSet!=null && basePriceSet.size()>0){
                		Iterator iterator=basePriceSet.iterator();
                	
	                	while(iterator.hasNext())
	                	{
	                	    BasePriceInfo bpInfo=(BasePriceInfo)iterator.next();
	                	    String zoneId=bpInfo.getZoneId();
	                	    if(prices!=null && zoneId!=null){
	                    		Iterator<ErpMaterialPriceModel> matIterator=prices.iterator();
	                    		ErpMaterialPriceModel unitPriceModel=null;
	                    		double minquantity=0;
	                        	while(matIterator.hasNext())
	                        	{                        		
	                        		ErpMaterialPriceModel priceModel=matIterator.next();
	                        		if(zoneId.equalsIgnoreCase(priceModel.getSapZoneId())){
	                        			 if(priceModel.getScaleQuantity()<=minquantity){
	                        			    unitPriceModel=priceModel;	 
	                        			 }
	                        			 
	                        		}
	                        	} 
	                        	if(unitPriceModel!=null){
	                        		LOGGER.info("setting the promo price for "+unitPriceModel.getSapZoneId());
	                        		unitPriceModel.setPromoPrice(bpInfo.getPrice());
	                        	}
	                	    }                	    
	                	    
	                	 //material.setBasePrice(bpInfo.getPrice());
	                	 //material.setBasePricingUnit(bpInfo.getUnit());
	                	}
                	}	
                }
                Collections.sort(priceList, matlPriceComparator);
                material.setPrices(priceList);
            }
            //
            // find the sales units for this material
            //
            HashSet<ErpSalesUnitModel> units = salesUnits.get(material.getSapId());
            if (units == null) {
                //
                // throw an exception and bail if no sales units are found for a material
                //
                exceptionList.add(new BadDataException("No sales units were found for Material " + material.getSapId()));
            } else {
                //
                // set the sales units for the material, sorted by alternative unit (roughly in numerical order)
                //
                ArrayList<ErpSalesUnitModel> unitList = new ArrayList<ErpSalesUnitModel>(units);
                Collections.sort(unitList, salesUnitComparator);
                material.setSalesUnits(unitList);
            }
            //
            // check to each material price and make sure at least one sales unit references it
            // in either the base unit or alternative unit
            //
            Iterator priceIter = material.getPrices().iterator();
            while (priceIter.hasNext()) {
                ErpMaterialPriceModel mPrice = (ErpMaterialPriceModel) priceIter.next();
                boolean unitMatch = false;
                Iterator unitIter = material.getSalesUnits().iterator();
                while (unitIter.hasNext()) {
                    ErpSalesUnitModel sUnit = (ErpSalesUnitModel) unitIter.next();
                    if ((mPrice.getPricingUnit().equals(sUnit.getBaseUnit())) || (mPrice.getPricingUnit().equals(sUnit.getAlternativeUnit())))
                        unitMatch = true;
                }
                if (unitMatch == false) {
                    //
                    // no matches, this is an exception
                    //
                    exceptionList.add(new BadDataException("There were no sales units for Material " + material.getSapId() + " that matched its " + mPrice.getPricingUnit() + " pricing unit."));
                }
            }
            //
            // add to the list of all materials
            //
            materials.put(material.getSapId(), material);
        }
        
    }
    
    private void printMaterialTree() {
        LOGGER.info("----- Printing Material Tree -----");
        //
        // materials
        //
        Map<ErpMaterialModel, Map<String, Object>> actMatls = materialParser.getActiveMaterials();
        Iterator<ErpMaterialModel> matlIter = actMatls.keySet().iterator();
        while (matlIter.hasNext()) {
            ErpMaterialModel material = matlIter.next();
            HashMap extraInfo = (HashMap) actMatls.get(material);
            LOGGER.debug(material.getDescription());
            LOGGER.debug("SAP id : " + material.getSapId());
            LOGGER.debug("SKU : " + (String) extraInfo.get(SAPConstants.SKU));
            LOGGER.debug("UPC : " +  ((material.getUPC()!=null)?material.getUPC():""));
            LOGGER.debug("Quantity Characteristic : " + material.getQuantityCharacteristic());
            LOGGER.debug("SalesUnit Characteristic : " + material.getSalesUnitCharacteristic());
            LOGGER.debug("ATPRule : " + material.getATPRule());
            LOGGER.debug("LeadTime : " + material.getLeadTime());
            LOGGER.debug("Availability : " + (String) extraInfo.get("UNAVAILABILITY_REASON"));
            //
            // material prices
            //
            LOGGER.debug("Prices");
            Iterator matlPriceIter = material.getPrices().iterator();
            while (matlPriceIter.hasNext()) {
                ErpMaterialPriceModel materialPrice = (ErpMaterialPriceModel) matlPriceIter.next();
                LOGGER.debug(materialPrice.getScaleQuantity() + " : " + materialPrice.getPricingUnit() + " : "  + materialPrice.getPrice());
            }
            //
            // sales units
            //
            LOGGER.debug("Sales Units");
            Iterator salesUnitIter = material.getSalesUnits().iterator();
            while (salesUnitIter.hasNext()) {
                ErpSalesUnitModel salesUnit = (ErpSalesUnitModel) salesUnitIter.next();
                LOGGER.debug(salesUnit.getDescription() + " : " + salesUnit.getBaseUnit() + " : " + salesUnit.getAlternativeUnit());
            }
            //
            // characteristics and characteristic values
            //
            LOGGER.debug("Characteristics");
            HashSet classNameSet = (HashSet)extraInfo.get(SAPConstants.CLASS);
            if (classNameSet != null) {
                Iterator classNameIter = classNameSet.iterator();
                while (classNameIter.hasNext()) {
                    String className = (String) classNameIter.next();
                    ErpClassModel erpClass = classes.get(className);
                    //
                    // characteristics
                    //
                    Iterator<ErpCharacteristicModel> charIter = erpClass.getCharacteristics().iterator();
                    while (charIter.hasNext()) {
                        ErpCharacteristicModel erpChar = charIter.next();
                        LOGGER.debug(erpChar.getName());
                        //
                        // characteristic values
                        //
                        Iterator<ErpCharacteristicValueModel> charValIter = erpChar.getCharacteristicValues().iterator();
                        while (charValIter.hasNext()) {
                            ErpCharacteristicValueModel erpCharVal = charValIter.next();
                            LOGGER.debug(erpCharVal.getName() + " : " + erpCharVal.getDescription());
                        }
                    }
                }
            }
        }
    }
    
    private void pruneClassTree() throws BadDataException {
        LOGGER.info("----- Reeconciling Class Tree and Material Tree -----");
        //
        // there are properties of materials that SAP exports as characteristics.
        // after the class tree and material tree are built, we need to go back
        // and find these properties from the class tree
        //
        // get all the characteristic values along with their extra info
        // from the variant price parser
        //
        Map<ErpCharacteristicValueModel, Map<String, String>> characteristicValues = variantPriceParser.getCharacteristicValues();
        //
        // walk through all the classes
        //
        Iterator<ErpClassModel> classIter = classes.values().iterator();
        while (classIter.hasNext()) {
            ErpClassModel erpClass = classIter.next();
            //
            // first remove the quantity characteristics from each class and
            // add the the name of the removed characteristic to the material
            // as its quantityCharacteristic property
            //
            // for some reason known only to the SAP folks, some materials
            // will have this property and some won't, so don't fret if
            // there ends up being materials with a NULL quantityCharacteristic property
            //
            // find the characteristics with a name that ends with "_QTY"
            //
            List<ErpCharacteristicModel> charList = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics());
            ListIterator<ErpCharacteristicModel> charListIter = charList.listIterator();
            int found = 0;
            while (charListIter.hasNext()) {
                ErpCharacteristicModel erpCharac = charListIter.next();
                if (erpCharac.getName().endsWith("_QTY")) {
                    if (++found > 1) {
                        //
                        // throw an exception and bail if more than one quantity characteristic was found
                        //
                        exceptionList.add(new BadDataException("Duplicate quantity characteristics were found for Class \"" + erpClass.getSapId() + "\""));
                    }
                    //
                    // this is the one, remove it
                    // and set the modified list back on the class it came from
                    //
                    charListIter.remove();
                    erpClass.setCharacteristics(charList);
                    //
                    // figure out which materials it belongs to
                    // and set all of their quantityCharacteristic properties
                    //
                    Iterator<ErpCharacteristicValueModel> charValueIter = characteristicValues.keySet().iterator();
                    while (charValueIter.hasNext()) {
                        ErpCharacteristicValueModel erpCharValue = charValueIter.next();
                        HashMap extraInfo = (HashMap) characteristicValues.get(erpCharValue);
                        String characName = (String) extraInfo.get(SAPConstants.CHARACTERISTIC_NAME);
                        if (characName.equals(erpCharac.getName())) {
                            String materialNumber = (String) extraInfo.get(SAPConstants.MATERIAL_NUMBER);
                            ErpMaterialModel material = materials.get(materialNumber);
                            //
                            // add the quantity characteristic
                            //
                            if (material != null)
                                material.setQuantityCharacteristic(erpCharac.getName());
                        }
                    }
                }
            }
            //
            // next, find the characteristic that has a materials's sales units
            // as its characteristic values and set the name of that characteristic
            // as the material's salesUnitCharacteristic property
            //
            charListIter = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics()).listIterator();
            while (charListIter.hasNext()) {
                ErpCharacteristicModel erpCharac = charListIter.next();
                //
                // walk through all the materials and note if there were any matches
                //
                Iterator<ErpMaterialModel> matlIter = materials.values().iterator();
                boolean isSalesUnit = false;
                boolean matchError = false;
                while (matlIter.hasNext()) {
                    ErpMaterialModel erpMaterial = matlIter.next();
                    //
                    // skip materials that aren't in this class
                    //
                    boolean matlInClass = false;
                    HashSet matlClasses = (HashSet)variantParser.getMaterialClasses().get(erpMaterial.getSapId());
                    //
                    // some materials don't belong to a class
                    //
                    if (matlClasses == null) continue;
                    Iterator matlClassIter = matlClasses.iterator();
                    while (matlClassIter.hasNext()) {
                        String matlClass = (String) matlClassIter.next();
                        if (matlClass.equals(erpClass.getSapId()))
                            matlInClass = true;
                    }
                    if (!matlInClass) continue;
                    //
                    // try to find a match between characteristic value names
                    // and sales unit names
                    //
                    int matchCount = 0;
                    Iterator<ErpCharacteristicValueModel> charValIter = erpCharac.getCharacteristicValues().iterator();
                    while (charValIter.hasNext()) {
                        ErpCharacteristicValueModel erpCharValue = charValIter.next();
                        Iterator salesUnitIter = erpMaterial.getSalesUnits().iterator();
                        while (salesUnitIter.hasNext()) {
                            ErpSalesUnitModel erpSalesUnit = (ErpSalesUnitModel) salesUnitIter.next();
                            if (erpCharValue.getName().equals(erpSalesUnit.getAlternativeUnit())) {
                                //
                                // found a match
                                //
                                ++matchCount;
                            }
                        }
                    }
                    //
                    // see if matches were found between the characteristic values and the sales units
                    //
                    if ((matchCount != 0) && (matchCount == erpMaterial.numberOfSalesUnits()) && (matchCount == erpCharac.numberOfCharacteristicValues()))  {
                        //
                        // if the match count is equal to the number of sales units a material has and the number of values
                        // of the matched characteristic, then the characterisitic is a sales unit
                        //
                        erpMaterial.setSalesUnitCharacteristic(erpCharac.getName());
                        isSalesUnit = true;
                    } else if ((matchCount != 0) && ((matchCount != erpMaterial.numberOfSalesUnits()) || (matchCount != erpCharac.numberOfCharacteristicValues()))) {
                        //
                        // if some matches were found, but the number of matches isn't equal to the number of
                        // sales units for the material or the number of values for the matched characteristic
                        // this probably means that there was some data entry error on the SAP side and we should flag this as an exception
                        //
                        exceptionList.add(new BadDataException("There was a mismatch between the sales units for Material " + erpMaterial.getSapId() + " and the values of Characteristic " + erpCharac.getName() + " in Class " + erpClass.getSapId()));
                        matchError = true;
                    }
                }
                //
                // if the characteristic was really a sales unit and no erroneous partial matches were found
                // remove the characteristic from the class
                //
                if (isSalesUnit && !matchError) {
                    //
                    // get the list of characteristic from the class
                    //
                    List<ErpCharacteristicModel> characs = new ArrayList<ErpCharacteristicModel>(erpClass.getCharacteristics());
                    ListIterator<ErpCharacteristicModel> clIter = characs.listIterator();
                    while (clIter.hasNext()) {
                        if (clIter.next().getName().equals(erpCharac.getName())) {
                            //
                            // remove the characteristic that matched the sales units from the list
                            //
                            clIter.remove();
                            break;
                        }
                    }
                    //
                    // set the list of characteristics back on the class
                    //
                    erpClass.setCharacteristics(characs);
                }
            }
            //
            // finished matching all of a classes characteristics against all materials
            //
        }
        //
        // finshed with all classes
        //
    }
    
    
}
