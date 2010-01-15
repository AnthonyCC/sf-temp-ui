/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import javax.ejb.*;
import javax.transaction.*;
import java.rmi.RemoteException;
import javax.naming.*;
import java.sql.*;
import java.util.*;

import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.framework.core.*;
import com.freshdirect.dataloader.*;
import com.freshdirect.dataloader.sap.*;

import com.freshdirect.erp.*;
import com.freshdirect.erp.model.*;
import com.freshdirect.erp.ejb.*;

import com.freshdirect.common.pricing.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 * A session bean that takes a set of anonymous models representing erp objects to be created
 * and processes them in the correct order together in a single batch.
 *
 * @version $Revision$
 * @author $Author$
 */
public class SAPLoaderSessionBean extends SessionBeanSupport {
    
    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( SAPLoaderSessionBean.class );
    
    /** Creates new SAPLoaderSessionBean */
    public SAPLoaderSessionBean() {
        super();
    }
    
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.dataloader.sap.ejb.SAPLoaderHome";
    }
    
    /** naming context for locating remote objects
     */
    Context initCtx = null;
    
    /** the batch number currently begin worked on
     */
    private transient int batchNumber = -1;
    /** the date and time processing started on the current batch
     */
    private transient Timestamp batchTimestamp = null;
    
    /** a cache of models of classes created during this batch
     */
    private transient HashMap createdClasses = null;
    /** a cache of materials created during this batch
     */
    private transient HashMap createdMaterials = null;
    
    
    /**
     * performs the batch load.  processes each of the objects in the correct order.
     *
     * @param deletedMaterials the collection of materials to discontinue
     * @param classes the collection of classes to create or update in this batch
     * @param activeMaterials the collection of materials to create of update in this batch
     * @param characteristicValuePrices the collection of characteristic value prices to create or update in this batch
     * @throws LoaderException any problems encountered while creating or updating objects in the system
     */
    public void loadData(HashMap classes, HashMap materials, HashMap characteristicValuePrices) throws LoaderException {
        
        LOGGER.debug("\nBeginning SAPLoaderSessionBean loadData\n");
        
        try {
            //
            // get the naming context
            //
            this.initCtx =  new InitialContext();
            
            try {
                //
                // do batch setup
                //
                beforeBatch();
                
                UserTransaction utx = getSessionContext().getUserTransaction();
                //
                // set a timeout period for this transaction (in seconds)
                //
                utx.setTransactionTimeout(30000);
                try {
                    utx.begin();
                    //
                    // run the batch steps
                    //
                    processClasses(classes);
                    processMaterials(materials);
                    processCharacteristicValuePrices(characteristicValuePrices);
                    processProducts(materials);
                    batchComplete();
                    
                    try {
                        //
                        // try to commit all the changes together
                        //
                        utx.commit();
                        LOGGER.debug("\nCompleted SAPLoaderSessionBean loadData\n");
                    } catch (RollbackException re) {
                        utx.setRollbackOnly();
                        LOGGER.error("\nUnable to update ERPS objects.  UserTransaction had already rolled back before attempt to commit.", re);
                        throw new LoaderException(re, "Unable to update ERPS objects.  UserTransaction had already rolled back before attempt to commit.");
                    } catch (HeuristicMixedException hme) {
                        utx.setRollbackOnly();
                        LOGGER.error("\nUnable to update ERPS objects.  TransactionManager aborted due to mixed heuristics.", hme);
                        throw new LoaderException(hme, "Unable to update ERPS objects.  TransactionManager aborted due to mixed heuristics.");
                    } catch (HeuristicRollbackException hre) {
                        utx.setRollbackOnly();
                        LOGGER.error("\nUnable to update ERPS objects.  TransactionManager heuristically rolled back transaction.", hre);
                        throw new LoaderException(hre, "Unable to update ERPS objects.  TransactionManager heuristically rolled back transaction.");
                    } catch (RuntimeException rune) {
                        utx.setRollbackOnly();
                        LOGGER.error("\nUnexpected runtime exception in SAPLoaderSessionBean loadData", rune);
                        throw new LoaderException(rune, "Unexpected runtime exception");
                    }
                    
                } catch (LoaderException le) {
                    utx.setRollbackOnly();
                    LOGGER.error("\n\nAborting SAPLoaderSessionBean loadData\n", le);
                    utx.rollback();
                    afterBatchFailed(le);
                    throw(le);
                }
                
            } catch (NotSupportedException nse) {
                LOGGER.error("\nUnable to update ERPS objects.  Unable to begin a UserTransaction.", nse);
                throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
            } catch (SystemException se) {
                LOGGER.error("\nUnable to update ERPS objects.  Unable to begin a UserTransaction.", se);
                throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
            } finally {
                //
                // close the naming context
                //
                try {
                    this.initCtx.close();
                } catch (NamingException ne) {
                    //
                    // don't need to rethrow this since the transaction has already completed or failed
                    //
                    LOGGER.warn("Had difficulty closing naming context after transaction had completed.  " + ne.getMessage());
                }
            }
            
        } catch (NamingException ne) {
            LOGGER.error("\nUnable to get naming context to locate components required by the loader.", ne);
            throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
        }
    }
    
    /**
     * does set up at the beginning of a batch load
     * @throws EJBException any problems during setup
     */
    public void beforeBatch() throws LoaderException {
        //
        // set the creation time for this batch
        //
        this.batchTimestamp = new Timestamp(new java.util.Date().getTime());
        //
        // get the user transaction
        //
        UserTransaction utx = getSessionContext().getUserTransaction();
        
        try {
            utx.begin();
            //
            // set a timeout period for this transaction (in seconds)
            //
            utx.setTransactionTimeout(3000);
            //
            // get the next batch number from the batch sequence
            //
            Connection conn = null;
            try {
                conn = getConnection();
                //
                // get a new batch number
                //
                PreparedStatement ps = conn.prepareStatement("select erps.batch_seq.nextval from dual");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    this.batchNumber = rs.getInt(1);
                } else {
                    LOGGER.error("Unable to begin new batch.  Didn't get a new batch number.");
                    throw new LoaderException("Unable to begin new batch.  Didn't get a new batch number.");
                }
                rs.close();
                ps.close();
                //
                // make an entry in the history table
                //
                ps = conn.prepareStatement("insert into erps.history (version, date_created, created_by, approval_status) values (?,?,?,?)");
                ps.setInt(1, this.batchNumber);
                ps.setTimestamp(2, this.batchTimestamp);
                ps.setString(3, "Loader");
                ps.setString(4, EnumApprovalStatus.LOADING.getStatusCode());
                int rowsaffected = ps.executeUpdate();
                if (rowsaffected != 1) {
                    throw new LoaderException("Unable to begin new batch.  Couldn't update loader history table.");
                }
                rs.close();
                ps.close();
                
                conn.close();
                
                try {
                    utx.commit();
                } catch (RollbackException re) {
                    LOGGER.error("\nUnable to start a batch.  UserTransaction had already rolled back before attempt to commit.", re);
                    throw new LoaderException(re, "Unable to start a batch.  UserTransaction had already rolled back before attempt to commit.");
                } catch (HeuristicMixedException hme) {
                    LOGGER.error("\nUnable to start a batch.  TransactionManager aborted due to mixed heuristics.", hme);
                    throw new LoaderException(hme, "Unable to start a batch.  TransactionManager aborted due to mixed heuristics.");
                } catch (HeuristicRollbackException hre) {
                    LOGGER.error("\nUnable to start a batch.  TransactionManager heuristically rolled back transaction.", hre);
                    throw new LoaderException(hre, "Unable to start a batch.  TransactionManager heuristically rolled back transaction.");
                }
                
            } catch (SQLException sqle) {
                LOGGER.error("Unable to begin new batch.", sqle);
                utx.setRollbackOnly();
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException sqle2) {
                        sqle2.printStackTrace();
                        LOGGER.error("Unable to begin new batch.", sqle2);
                        throw new LoaderException("Unable to begin new batch.  " + sqle2);
                    }
                }
                utx.rollback();
                throw new LoaderException("Unable to begin a new batch.  " + sqle.getMessage());
            }
            
        } catch (NotSupportedException nse) {
            LOGGER.error("\nUnable to complete a failed batch.  Unable to begin a UserTransaction.", nse);
            throw new LoaderException(nse, "Unable to start batch.  Unable to begin a UserTransaction.");
        } catch (SystemException se) {
            LOGGER.error("\nUnable to complete a failed batch.  Unable to begin a UserTransaction.", se);
            throw new LoaderException(se, "Unable to start batch.  Unable to begin a UserTransaction.");
        }
    }
    
    /**
     * @param classes
     * @throws LoaderException  */
    private void processClasses(HashMap classes) throws LoaderException {
        //
        // create a new HashMap of models of classes so that subsequent loader steps can
        // form the proper foreign key relationships in the database
        //
        createdClasses = new HashMap();
        try {
            LOGGER.info("\nStarting to process Classes for batch number " + this.batchNumber + "\n");
            LOGGER.info("\nProcessing " + classes.size() + " classes\n");
            ErpClassHome classHome = (ErpClassHome) initCtx.lookup("java:comp/env/ejb/ErpClass");
            
            Iterator classKeyIter = classes.keySet().iterator();
            while (classKeyIter.hasNext()) {
                ErpClassModel erpClsModel = (ErpClassModel) classes.get(classKeyIter.next());
                //
                // create the new class
                //
                LOGGER.info("\nCreating Class " + erpClsModel.getSapId() + "\n");
                ErpClassEB erpClsEB = classHome.create(this.batchNumber, erpClsModel);
                //
                // read back the model as a sanity check
                //
                erpClsModel = (ErpClassModel) erpClsEB.getModel();
                LOGGER.info("\nSuccessfully created Class " + erpClsModel.getSapId() + ", id= " + erpClsModel.getPK().getId() + "\n");
                //
                // add this model to the createdClasses HashMap, keyed by Name
                //
                createdClasses.put(erpClsModel.getSapId(), erpClsModel);
                
            }
            LOGGER.info("\nCompleted processing Classes for batch number " + this.batchNumber + "\n");
        } catch (NamingException ne) {
            throw new LoaderException(ne, "Unable to find home for ErpClass");
        } catch (CreateException ce) {
            throw new LoaderException(ce, "Unable to create a new version of an ErpClass");
        } catch (RemoteException re) {
            throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpClass");
        }
    }
    
    /**
     * @param activeMaterials
     * @throws LoaderException  */
    private void processMaterials(HashMap activeMaterials) throws LoaderException {
        //
        // create a new HashMap of models of materials so that subsequent loader steps can
        // form the proper foreign key relationships in the database
        //
        createdMaterials = new HashMap();
        try {
            LOGGER.info("\nStarting to process Materials for batch number " + this.batchNumber + "\n");
            LOGGER.info("\nProcessing " + activeMaterials.size() + " materials\n");
            ErpMaterialHome matlHome = (ErpMaterialHome) initCtx.lookup("java:comp/env/ejb/ErpMaterial");
            
            Iterator matlIter = activeMaterials.keySet().iterator();
            while (matlIter.hasNext()) {
                ErpMaterialModel erpMatlModel = (ErpMaterialModel) matlIter.next();
                HashMap extraInfo = (HashMap) activeMaterials.get(erpMatlModel);
                //
                // set some additional properties on the material model that couldn't have been set
                // until the classes had been created first
                //
                HashSet classNames = (HashSet) extraInfo.get(SAPConstants.CLASS);
                if (classNames != null) {
                    LinkedList matlClasses = new LinkedList();
                    Iterator matlClassIter = classNames.iterator();
                    while (matlClassIter.hasNext()) {
                        String className = (String) matlClassIter.next();
                        ErpClassModel erpClsModel = (ErpClassModel) createdClasses.get(className);
                        if (erpClsModel == null) {
                            throw new LoaderException("Unable to form an association between Material number " + erpMatlModel.getSapId() + " and Class " + className);
                        }
                        matlClasses.add(erpClsModel);
                    }
                    erpMatlModel.setClasses(matlClasses);
                }
                //
                // create the new material
                //
                LOGGER.info("\nCreating new Material " + erpMatlModel.getDescription() + "\n");
                ErpMaterialEB erpMatlEB = matlHome.create(this.batchNumber, erpMatlModel);
                //
                // read back the model as a sanity check
                //
                erpMatlModel = (ErpMaterialModel) erpMatlEB.getModel();
                LOGGER.info("\nSuccessfully created Material " + erpMatlModel.getDescription() + ", id= " + erpMatlModel.getPK().getId() + "\n");
                //
                // add this model to the createdMaterials HashMap, keyed by SapID (material number)
                //
                createdMaterials.put(erpMatlModel.getSapId(), erpMatlModel);
                
            }
            LOGGER.info("\nCompleted processing Materials for batch number " + this.batchNumber + "\n");
        } catch (NamingException ne) {
            throw new LoaderException(ne, "Unable to find home for ErpMaterial");
        } catch (CreateException ce) {
            throw new LoaderException(ce, "Unable to create a new version of an ErpMaterial");
        } catch (RemoteException re) {
            throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpMaterial");
        }
    }
    
    /**
     * @param characteristicValuePrices
     * @throws LoaderException  */
    private void processCharacteristicValuePrices(HashMap characteristicValuePrices) throws LoaderException {
        try {
            LOGGER.info("\nStarting to process CharacteristicValuePrices for batch number " + this.batchNumber + "\n");
            LOGGER.info("\nProcessing " + characteristicValuePrices.size() + " characteristic value prices\n");
            ErpCharacteristicValuePriceHome cvpHome = (ErpCharacteristicValuePriceHome) initCtx.lookup("java:comp/env/ejb/ErpCharacteristicValuePrice");
            
            Iterator cvpKeyIter = characteristicValuePrices.keySet().iterator();
            while (cvpKeyIter.hasNext()) {
                ErpCharacteristicValuePriceModel erpCvpModel = (ErpCharacteristicValuePriceModel) cvpKeyIter.next();
                HashMap extraInfo = (HashMap) characteristicValuePrices.get(erpCvpModel);
                //
                // set some additional properties on the characteristic value price model that couldn't have been set
                // until the classes and materials had been created first
                //
                //  find the material for this characteristic value price
                //
                String matlNumber = (String) extraInfo.get(SAPConstants.MATERIAL_NUMBER);
                ErpMaterialModel erpMatl = (ErpMaterialModel) createdMaterials.get(matlNumber);
                //
                // if erpMatl wasn't in the list of created materials, this
                // characteristic value price was exported as part of a discontinued material.
                // don't insert this characteristic value price.
                //
                if (erpMatl != null) {
                    erpCvpModel.setMaterialId(erpMatl.getPK().getId());
                    //
                    // find the characteristic value for this characteristic value price
                    //
                    String charValueName = (String) extraInfo.get(SAPConstants.CHARACTERISTIC_VALUE);
                    try {
                        String clsName = (String) extraInfo.get(SAPConstants.CLASS);
                        ErpClassModel erpClass = (ErpClassModel) createdClasses.get(clsName);
                        String characName = (String) extraInfo.get(SAPConstants.CHARACTERISTIC_NAME);
                        ErpCharacteristicModel erpCharac = erpClass.getCharacteristic(characName);
                        ErpCharacteristicValueModel erpCharVal = erpCharac.getCharacteristicValue(charValueName);
                        erpCvpModel.setCharacteristicValueId(erpCharVal.getPK().getId());
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                        throw new LoaderException("Unable to form an association between CharacteristicValuePrice " + erpCvpModel.getSapId() + " and CharacteristicValue " + charValueName);
                    }
                    //
                    // create the new characteristic value price
                    //
                    LOGGER.info("\nCreating new CharacteristicValuePrice " + erpCvpModel.getSapId() + "\n");
                    ErpCharacteristicValuePriceEB erpCvpEB = cvpHome.create(this.batchNumber, erpCvpModel);
                    //
                    // read back the model as a sanity check
                    //
                    erpCvpModel = (ErpCharacteristicValuePriceModel) erpCvpEB.getModel();
                    LOGGER.info("\nSuccessfully created CharacteristicValuePrice " + erpCvpModel.getSapId() + ", id= " + erpCvpModel.getPK().getId() + "\n");
                }
            }
            LOGGER.info("\nCompleted processing CharacteristicValuePrices for batch number " + this.batchNumber + "\n");
        } catch (NamingException ne) {
            throw new LoaderException(ne, "Unable to find home for ErpCharacteristicValuePrice");
        } catch (CreateException ce) {
            throw new LoaderException(ce, "Unable to create a new version of an ErpCharacteristicValuePrice");
        } catch (RemoteException re) {
            throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpCharacteristicValuePrice");
        }
    }
    
	//
	// this query finds all current products that relate to a specific material
	//
	private final static String QUERY =
		"select p.sku_code from erps.product p, erps.materialproxy mpx, erps.material m " +
		"where m.sap_id=? and p.id=mpx.product_id and mpx.mat_id=m.id " +
		"and m.version=(select max(version) from erps.material where sap_id=m.sap_id and version<>?)";
	    
    /**
     * @param activeMaterials
     * @throws LoaderException  */
    private void processProducts(HashMap activeMaterials) throws LoaderException {
        
        Connection conn = null;

        
        try {
            LOGGER.info("\nStarting to process default Products for batch number " + this.batchNumber + "\n");
            LOGGER.info("\nProcessing " + createdMaterials.size() + " products\n");
            ErpProductHome productHome = (ErpProductHome) initCtx.lookup("java:comp/env/ejb/ErpProduct");
            
            conn = getConnection();
            
            Iterator matlIter = activeMaterials.keySet().iterator();
            while (matlIter.hasNext()) {
                //
                // list of all products that are affected by activating this material
                //
                HashSet affectedSkus = new HashSet();
                //
                // first get the anonymous material model so we can get the SKU of the default product
                // from its extra info
                //
                ErpMaterialModel anonMatlModel = (ErpMaterialModel) matlIter.next();
                HashMap extraInfo = (HashMap) activeMaterials.get(anonMatlModel);
                affectedSkus.add(extraInfo.get(SAPConstants.SKU));
                String unavailStatus = (String) extraInfo.get("UNAVAILABILITY_STATUS");
                java.util.Date unavailDate = (java.util.Date) extraInfo.get("UNAVAILABILITY_DATE");
                String unavailReason = (String) extraInfo.get("UNAVAILABILITY_REASON");
                String rating = (String) extraInfo.get("RATING");
                String days_fresh = (String) extraInfo.get("DAYS_FRESH");
                String days_in_house = (String) extraInfo.get("DAYS_IN_HOUSE");
                //
                // now get the real material model that we created previously to we can create the
                // material proxy and product objects that relate to a material
                //
                ErpMaterialModel erpMatlModel = (ErpMaterialModel) createdMaterials.get(anonMatlModel.getSapId());
                //
                // find the sku codes of the other products affected by this material
                //
                try {
                    PreparedStatement ps = conn.prepareStatement(QUERY);
                    ps.setString(1, anonMatlModel.getSapId());
                    ps.setInt(2, this.batchNumber);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        affectedSkus.add(rs.getString(1));
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException sqle) {
                    LOGGER.error("Unable to find virtual products to create for material " + anonMatlModel.getSapId(), sqle);
                    throw new LoaderException("Unable to find virtual products to create for material " + anonMatlModel.getSapId() + "\n" + sqle.getMessage());
                }
                Iterator skuIter = affectedSkus.iterator();
                while (skuIter.hasNext()) {
                    String skuCode = (String) skuIter.next();

					ErpProductModel erpProductModel = new ErpProductModel();
					erpProductModel.setProxiedMaterial(erpMatlModel);

                    //
                    // if this product is a new version of a previously existing product, make sure to carry forward
                    // any hidden sales units or characteristic values
                    //
                    try {
                        ErpProductEB origPrdEB = (ErpProductEB) productHome.findBySkuCode(skuCode);
                        ErpProductModel origPrdModel = (ErpProductModel) origPrdEB.getModel();

                        // keep track of used to be hidden
                        List origHiddenSalesUnits = new ArrayList();
                        List origHiddenCharVals = new ArrayList();

                        ///if (origPrdModel.getMaterialProxies().size() > 0) {
                        {
                            // if there is a material proxy, figure out what should be hidden

                            VersionedPrimaryKey[] hiddenSuPKs = origPrdModel.getHiddenSalesUnitPKs();
                            for (int i=0; i<hiddenSuPKs.length; i++) {
                                for (Iterator suIter = origPrdModel.getProxiedMaterial().getSalesUnits().iterator(); suIter.hasNext();) {
                                    ErpSalesUnitModel origSu = (ErpSalesUnitModel) suIter.next();
                                    if (hiddenSuPKs[i].getId().equals(origSu.getPK().getId())) {
                                        origHiddenSalesUnits.add(origSu);
                                    }
                                }
                            }
                            VersionedPrimaryKey[] hiddenCvPKs = origPrdModel.getHiddenCharacteristicValuePKs();
                            for (int j=0; j<hiddenCvPKs.length; j++) {
                                for (Iterator chIter = origPrdModel.getProxiedMaterial().getCharacteristics().iterator(); chIter.hasNext();) {
                                    ErpCharacteristicModel origChar = (ErpCharacteristicModel) chIter.next();
                                    for (Iterator cvIter = origChar.getCharacteristicValues().iterator(); cvIter.hasNext();) {
                                        ErpCharacteristicValueModel origCharVal = (ErpCharacteristicValueModel) cvIter.next();
                                        if (hiddenCvPKs[j].getId().equals(origCharVal.getPK().getId())) {
                                            origHiddenCharVals.add(origCharVal);
                                        }
                                    }
                                }
                            }
                        }
                        //
                        // find hidden sales unit PKs in new version
                        //
                        ArrayList hiddenSuPKs = new ArrayList();
                        Iterator newSuIter = erpMatlModel.getSalesUnits().iterator();
                        while (newSuIter.hasNext()) {
                            ErpSalesUnitModel newSu = (ErpSalesUnitModel) newSuIter.next();
                            Iterator hiddenSuIter = origHiddenSalesUnits.iterator();
                            while (hiddenSuIter.hasNext()) {
                                ErpSalesUnitModel hiddenSu = (ErpSalesUnitModel) hiddenSuIter.next();
                                if (newSu.getAlternativeUnit().equalsIgnoreCase(hiddenSu.getAlternativeUnit())) {
                                    hiddenSuPKs.add(newSu.getPK());
                                    System.out.println("hiding sales unit " + newSu.getAlternativeUnit());
                                }
                            }
                        }
						erpProductModel.setHiddenSalesUnitPKs((VersionedPrimaryKey[]) hiddenSuPKs.toArray(new VersionedPrimaryKey[0]));
                        
                        //if (origPxyModel != null) {
                       	{
                            //
                            // find hidden characteristic value PKs in new version
                            //
                            ArrayList hiddenCvPKs = new ArrayList();
                            Iterator newCharIter = erpMatlModel.getCharacteristics().iterator();
                            while(newCharIter.hasNext()) {
                                ErpCharacteristicModel newCharac = (ErpCharacteristicModel) newCharIter.next();
                                Iterator newCvIter = newCharac.getCharacteristicValues().iterator();
                                while (newCvIter.hasNext()) {
                                    ErpCharacteristicValueModel newCharVal = (ErpCharacteristicValueModel) newCvIter.next();
                                    
                                    Iterator origCharIter = origPrdModel.getProxiedMaterial().getCharacteristics().iterator();
                                    while (origCharIter.hasNext()) {
                                        ErpCharacteristicModel origCharModel = (ErpCharacteristicModel) origCharIter.next();
                                        Iterator origCvIter = origCharModel.getCharacteristicValues().iterator();
                                        while (origCvIter.hasNext()) {
                                            ErpCharacteristicValueModel origCvModel = (ErpCharacteristicValueModel) origCvIter.next();
                                            
                                            Iterator origHiddenCharValsIter = origHiddenCharVals.iterator();
                                            while (origHiddenCharValsIter.hasNext()) {
                                                ErpCharacteristicValueModel origHiddenCv = (ErpCharacteristicValueModel) origHiddenCharValsIter.next();
                                                
                                                if ( newCharac.getName().equalsIgnoreCase(origCharModel.getName())
                                                && newCharVal.getName().equalsIgnoreCase(origCvModel.getName())
                                                && origCvModel.getName().equalsIgnoreCase(origHiddenCv.getName())
                                                ) {
                                                    hiddenCvPKs.add(newCharVal.getPK());
                                                    System.out.println("hiding char value " + newCharac.getName() + ":" + newCharVal.getName());
                                                }
                                            }
                                        }
                                    }
                                    
                                }
                            }
							erpProductModel.setHiddenCharacteristicValuePKs((VersionedPrimaryKey[]) hiddenCvPKs.toArray(new VersionedPrimaryKey[0]));
                        }
                    } catch (ObjectNotFoundException onfe) {
                        // no such sku, no big deal
                        // this will happen when we are creating the default version
                        // of a product for the first time
                    }
                    //
                    // ask the pricing factory to figure out what the default price should be
                    // for a product consisting of only one material
                    //
                    Pricing pr = PricingFactory.getPricing(erpMatlModel, new ErpCharacteristicValuePriceModel[0] );
                    double defaultPrice = 0.0;
                    String defaultPriceUnit = " ";
                    //
                    // if we have valid sales units for the material, use the pricing engine
                    // otherwise, just use nil pricing (0 price, no price unit)
                    //
                    if (erpProductModel.getSalesUnits().size() > 0) {
                        //
                        // find the sales unit with the lowest ratio
                        //
                        List units = new ArrayList(erpProductModel.getSalesUnits());
                        Collections.sort(units, salesUnitComparator);
                        ErpSalesUnitModel lowestRatio = (ErpSalesUnitModel) units.get(0);
                        //
                        // perform the pricing using the pricing engine
                        //
                        try {
							FDConfiguration prConf = new FDConfiguration( 1.0, lowestRatio.getAlternativeUnit() );
                            
							MaterialPrice pricingCondition = PricingEngine.getConfiguredPrice(pr, prConf).getPricingCondition();
                            
                            defaultPrice = pricingCondition.getPrice();
                            defaultPriceUnit = pricingCondition.getPricingUnit();
                            
                        } catch (PricingException pe) {
                            String message = "Unable to perform pricing for product " + skuCode + " from erps.material " + erpMatlModel.getSapId();
                            LOGGER.error(message, pe);
                            throw new LoaderException(pe, message);
                        }
                    } else {
                        throw new LoaderException("There are no sales units for material " + erpMatlModel.getSapId() + " in product " + skuCode + ".  Perhaps they have all been hidden?");
                    }
                    //
                    // creating product
                    //
					erpProductModel.setSkuCode(skuCode);
					erpProductModel.setDefaultPrice(defaultPrice);
					erpProductModel.setDefaultPriceUnit(defaultPriceUnit);
					erpProductModel.setUnavailabilityStatus(unavailStatus);
					erpProductModel.setUnavailabilityDate(unavailDate);
					erpProductModel.setUnavailabilityReason(unavailReason);
					erpProductModel.setRating(rating);
					erpProductModel.setDaysFresh(days_fresh);
					erpProductModel.setDaysInHouse(days_in_house);
					erpProductModel.setBasePrice(anonMatlModel.getBasePrice());
					erpProductModel.setBasePriceUnit(anonMatlModel.getBasePricingUnit());

                    
                    //
                    // if the default price ends up being zero, this is carried over from an old-style discontinued product
                    //
                    if (defaultPrice == 0.0) {
                        LOGGER.error("\nSomehow price ended up being 0.  What happened?");
                        erpProductModel.setUnavailabilityDate(new java.util.Date());
                        erpProductModel.setUnavailabilityStatus("DISC");
                        erpProductModel.setUnavailabilityReason("Discontinued by SAP");
                    }
                    //
                    // create the new product
                    //
                    ErpProductEB erpProductEB = null;
                    LOGGER.info("\nCreating new Product " + erpProductModel.getSkuCode() + "\n");
                    try {
                        erpProductEB = productHome.create(this.batchNumber, erpProductModel);
                    } catch (CreateException ce) {
                        if (ce.getMessage().indexOf("unique constraint") > -1) {
                            //
                            // check to see if the exception was caused by a unique constraint violation
                            // if so, it means we really want to replace a product that had been deactivated
                            // in the deactivateDeletedProducts step, remove and then re-create
                            //
                            try {
                                erpProductEB  = productHome.findBySkuCodeAndVersion(skuCode, this.batchNumber);
                                erpProductEB.remove();
                                erpProductEB = productHome.create(this.batchNumber, erpProductModel);
                            } catch (ObjectNotFoundException onfe) {
                                //
                                // why would this happen?  if we previously violated the unique constraint,
                                // we ought to be able to locate the item that caused the exception
                                //
                                LOGGER.warn("\nInteresting inconsistency while trying to update SKU " + skuCode, onfe);
                                throw ce;
                            }
                        } else {
                            LOGGER.error("\nUnusual CreateException occurred while trying to update SKU " + skuCode, ce);
                            //
                            // if not, then some other problem occurred, rethrow the exception
                            //
                            throw ce;
                        }
                    }
                    //
                    // read back the model as a sanity check if an instance of an ErpProductEntityBean is available
                    //
                    if (erpProductEB != null) {
                        erpProductModel = (ErpProductModel) erpProductEB.getModel();
                        LOGGER.info("\nSuccessfully created Product " + erpProductModel.getSkuCode() + ", id= " + erpProductModel.getPK().getId() + "\n");
                    }
                }
            }
            LOGGER.info("\nCompleted processing Products for batch number " + this.batchNumber + "\n");
        } catch (NamingException ne) {
            throw new LoaderException(ne, "Unable to find home for ErpProduct");
        } catch (CreateException ce) {
            throw new LoaderException(ce, "Unable to create a new version of an ErpProduct");
        } catch (FinderException fe) {
            throw new LoaderException(fe, "Unable to locate an ErpProduct");
        } catch (RemoveException re) {
            throw new LoaderException(re, "Unable to remove an ErpProduct before re-creation");
        } catch (RemoteException re) {
            throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpProduct");
        } catch (SQLException sqle) {
            throw new LoaderException(sqle, "Unexpected SQL exception while trying to create an ErpProduct");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    // eat it
                }
            }
        }
    }
    
    /**
     *
     * a convenience object that can compare sales units by their ratio (numerator / denominator)
     *
     */
    private static Comparator salesUnitComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            ErpSalesUnitModel su1 = (ErpSalesUnitModel) o1;
            ErpSalesUnitModel su2 = (ErpSalesUnitModel) o2;
            double ratio1 = su1.getNumerator()/su1.getDenominator();
            double ratio2 = su2.getNumerator()/su2.getDenominator();
            if (ratio1 < ratio2){
            	if(!su1.isDisplayInd() ||(su1.isDisplayInd()==su2.isDisplayInd())){
            		return -1;
            	}else{
            		return 1;
            	}
            }                
            else if (ratio2 < ratio1){
            	if(!su2.isDisplayInd() ||(su1.isDisplayInd()==su2.isDisplayInd())){
            		return 1;
            	}else{
            		return -1;
            	}
            }
            else
                return 0;
        }
    };
    
    /**
     * makes an entry in the history table indicating that a batch succeeded and is ready for review
     *
     * @throws LoaderException any unexpected errors updating the history table
     */
    public void batchComplete() throws LoaderException {
        
        Connection conn = null;
        try {
            //
            // get connection
            //
            conn = getConnection();

            PreparedStatement ps = conn.prepareStatement("update erps.history set approval_status=? where version=?");
            ps.setString(1, EnumApprovalStatus.NEW.getStatusCode());
            ps.setInt(2, batchNumber);
            int rowsaffected = ps.executeUpdate();
            if (rowsaffected != 1) {
                throw new LoaderException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.");
            }
            ps.close();
            ps = null;
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            LOGGER.error("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.", sqle);
            throw new LoaderException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  " + sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    sqle2.printStackTrace();
                    LOGGER.error("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.", sqle2);
                    throw new LoaderException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  " + sqle2);
                }
            }
        }
        
    }
    
    /**
     * makes an entry in the history table indicating that a batch failed and was rejected automatically
     *
     * @throws LoaderException any unexpected errors updating the history table
     */
    public void afterBatchFailed(Exception ex) throws LoaderException {
        //
        // an error occurred while doing the load and the transaction was rolled back
        // make a note in the history table so there don't appear to be any gaps in the
        // version numbers
        //
        // do this step in a separate transaction
        //
        UserTransaction utx = getSessionContext().getUserTransaction();
        
        System.out.println("Batch version is " + batchNumber);
        
        Connection conn = null;
        try {
            //
            // start the transaction
            //
            utx.begin();
            try {
                //
                // get connection
                //
                conn = getConnection();
                
                PreparedStatement ps = conn.prepareStatement("update erps.history set approval_status=?, description=? where version=?");
                ps.setString(1, EnumApprovalStatus.REJECTED.getStatusCode());
                ps.setString(2, "This batch failed and was automatically rejected by the loader.  " + ex.getMessage());
                ps.setInt(3, batchNumber);
                int rowsaffected = ps.executeUpdate();
                if (rowsaffected != 1) {
                    throw new LoaderException("Unable to complete a failed batch.  Couldn't update loader history table to mark a failed batch as rejected.");
                }
                ps.close();
                ps = null;
                
                conn.close();
                conn = null;
                
                try {
                    //
                    // commit
                    //
                    utx.commit();
                } catch (RollbackException re) {
                    LOGGER.error("\nUnable to complete a failed batch.  UserTransaction had already rolled back before attempt to commit.", re);
                    throw new LoaderException(re, "Unable to complete a failed batch.  UserTransaction had already rolled back before attempt to commit.");
                } catch (HeuristicMixedException hme) {
                    LOGGER.error("\nUnable to complete a failed batch.  TransactionManager aborted due to mixed heuristics.", hme);
                    throw new LoaderException(hme, "Unable to complete a failed batch.  TransactionManager aborted due to mixed heuristics.");
                } catch (HeuristicRollbackException hre) {
                    LOGGER.error("\nUnable to complete a failed batch.  TransactionManager heuristically rolled back transaction.", hre);
                    throw new LoaderException(hre, "Unable to complete a failed batch.  TransactionManager heuristically rolled back transaction.");
                }
            } catch (SQLException sqle) {
                utx.setRollbackOnly();
                //
                // rollback
                //
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException sqle2) {
                        // problem closing connection, rollback anyway
                    }
                }
                utx.rollback();
                sqle.printStackTrace();
                LOGGER.error("Unable to complete a failed batch.  Couldn't update loader history table to mark a failed batch as rejected.", sqle);
                throw new LoaderException("Unable to complete a failed batch.  Couldn't update loader history table to mark a failed batch as rejected.  " + sqle);
            }
        } catch (NotSupportedException nse) {
            LOGGER.error("\nUnable to complete a failed batch.  Unable to begin a UserTransaction.", nse);
            throw new LoaderException(nse, "Unable to complete a failed batch.  Unable to begin a UserTransaction.");
        } catch (SystemException se) {
            LOGGER.error("\nUnable to complete a failed batch.  Unable to begin a UserTransaction.", se);
            throw new LoaderException(se, "Unable to complete a failed batch.  Unable to begin a UserTransaction.");
        }
        
    }
    
}
