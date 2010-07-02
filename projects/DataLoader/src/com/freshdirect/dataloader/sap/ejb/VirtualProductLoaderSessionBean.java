/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.VirtualProductParser;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpProductEB;
import com.freshdirect.erp.ejb.ErpProductHome;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A session bean that takes a set of anonymous models representing erp objects to be created
 * and processes them in the correct order together in a single batch.
 *
 * @version $Revision$
 * @author $Author$
 */
public class VirtualProductLoaderSessionBean extends SessionBeanSupport {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( VirtualProductLoaderSessionBean.class );
    
    /** Creates new VirtualProductLoaderSessionBean */
    public VirtualProductLoaderSessionBean() {
        super();
    }
    
    /** naming context for locating remote objects
     */
    Context initCtx = null;
    
    /** database connection
     */
    private transient Connection conn = null;
    
    /**
     * performs the batch load.  processes each of the objects in the correct order.
     *
     * @param products the collection of virtual products
     * @throws LoaderException any problems encountered while creating or updating objects in the system
     */
    public void loadData(Map<String, Map<String, Object>> products) throws LoaderException {
        
        LOGGER.debug("\nBeginning VirtualProductLoaderSessionBean::loadData\n");
        
        try {
            //
            // get the naming context
            //
            this.initCtx =  new InitialContext();
            
            try {
                //
                // get the Connection
                //
                this.conn = getConnection();
                //
                // run the batch steps
                //
                processProducts(products);
                
                this.conn.close();

                LOGGER.debug("\nCompleted VirtualProductLoaderSessionBean::loadData\n");

            } catch (RuntimeException rune) {
                getSessionContext().setRollbackOnly();
                LOGGER.error("\nUnexpected runtime exception", rune);
                try {
                    this.conn.close();
                } catch (SQLException sqle2) {
                    // do nothing and finish the rollback
                }
                throw new LoaderException(rune, "Unexpected runtime exception");
            } catch (SQLException sqle) {
                getSessionContext().setRollbackOnly();
                LOGGER.error("\nUnable to obtain or close a Connection from a DataSource", sqle);
                try {
                    this.conn.close();
                } catch (SQLException sqle2) {
                    // do nothing and finish the rollback
                }
                throw new LoaderException(sqle, "Unable to obtain a connection.");
            } catch (LoaderException le) {
                getSessionContext().setRollbackOnly();
                LOGGER.error("\n\nAborting VirtualProductLoaderSessionBean loadData\n", le);
                try {
                    this.conn.close();
                } catch (SQLException sqle) {
                    // do nothing and finish the rollback
                }
                throw(le);
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
     * @param activeMaterials
     * @throws LoaderException  */
    private void processProducts(Map<String, Map<String, Object>> products) throws LoaderException {
        try {
            LOGGER.info("\nStarting to process Virtual Products\n");
            ErpProductHome productHome = (ErpProductHome) initCtx.lookup("java:comp/env/ejb/ErpProduct");
            
            Iterator vpIter = products.keySet().iterator();
            while (vpIter.hasNext()) {
                //
                // first get the sku code for the virtual product we're going to create
                // and the extra info about which product this is based on and
                // how this virtual product is different from the original
                //
                String virtualSku = (String) vpIter.next();
                HashMap extraInfo = (HashMap) products.get(virtualSku);
                String  originalSku = (String) extraInfo.get(VirtualProductParser.ORIGINAL_SKU);
                List hiddenSalesUnits = (List) extraInfo.get(VirtualProductParser.SALES_UNITS);
                List hiddenCharVals = (List) extraInfo.get(VirtualProductParser.CHAR_VALUES);
                //
                // now get the original product and grab its model
                //
                ErpProductEB erpProduct = productHome.findBySkuCode(originalSku);
                ErpProductModel origPrdModel = (ErpProductModel) erpProduct.getModel();
                //
                // create a new anonymous product model
                //
                ErpProductModel virtPrdModel = new ErpProductModel();
                System.out.println("Creating virtual product : " + virtualSku);
                System.out.println("Based on : " + originalSku);
                virtPrdModel.setSkuCode(virtualSku);
                virtPrdModel.setUnavailabilityDate(origPrdModel.getUnavailabilityDate());
                virtPrdModel.setUnavailabilityReason(origPrdModel.getUnavailabilityReason());
                virtPrdModel.setUnavailabilityStatus(origPrdModel.getUnavailabilityStatus());

                //
                // create a new anonymous material proxy
                //
				virtPrdModel.setProxiedMaterial(origPrdModel.getProxiedMaterial());

                //
                // list of hidden sales unit PKs
                //
                ArrayList<PrimaryKey> hiddenSuPKs = new ArrayList<PrimaryKey>();
                Iterator origSuIter = origPrdModel.getSalesUnits().iterator();
                while (origSuIter.hasNext()) {
                    ErpSalesUnitModel origSu = (ErpSalesUnitModel) origSuIter.next();
                    Iterator virtSuIter = hiddenSalesUnits.iterator();
                    while (virtSuIter.hasNext()) {
                        String virtSuName = (String) virtSuIter.next();
                        if (origSu.getAlternativeUnit().toUpperCase().equals(virtSuName.toUpperCase())) {
                            hiddenSuPKs.add(origSu.getPK());
                            System.out.println("Hiding sales unit  : " + virtSuName);
                        }
                    }
                }
				virtPrdModel.setHiddenSalesUnitPKs(hiddenSuPKs.toArray(new VersionedPrimaryKey[0]));
                //
                // list of hidden characteristic values
                //
                ArrayList<PrimaryKey> hiddenCvPKs = new ArrayList<PrimaryKey>();
                Iterator clsIter = origPrdModel.getProxiedMaterial().getClasses().iterator();
                while (clsIter.hasNext()) {
                    ErpClassModel cls = (ErpClassModel) clsIter.next();
                    Iterator<ErpCharacteristicModel> charIter = cls.getCharacteristics().iterator();
                    while(charIter.hasNext()) {
                        ErpCharacteristicModel charac = charIter.next();
                        Iterator<ErpCharacteristicValueModel> cvIter = charac.getCharacteristicValues().iterator();
                        while (cvIter.hasNext()) {
                            ErpCharacteristicValueModel charVal = cvIter.next();
                            Iterator hiddenPairs = hiddenCharVals.iterator();
                            while (hiddenPairs.hasNext()) {
                                HashMap cvPair = (HashMap) hiddenPairs.next();
                                String hiddenCharName = ((String) cvPair.keySet().iterator().next());
                                String hiddenCVName = (String) cvPair.get(hiddenCharName);
                                if (charac.getName().toUpperCase().equals(hiddenCharName.toUpperCase()) && charVal.getName().toUpperCase().equals(hiddenCVName.toUpperCase())) {
                                    hiddenCvPKs.add(charVal.getPK());
                                    System.out.println("Hiding characteristic value : " + hiddenCharName + ":" + hiddenCVName);
                                }
                            }
                        }
                    }
                }
				virtPrdModel.setHiddenCharacteristicValuePKs(hiddenCvPKs.toArray(new VersionedPrimaryKey[0]));
                
                //
                // ask the pricing factory to figure out what the default price should be
                // for a product consisting of only one material
                //
                Pricing pr = PricingFactory.getPricing(virtPrdModel.getProxiedMaterial(), new ErpCharacteristicValuePriceModel[0] );
                //
                // find the sales unit with the lowest ratio
                //
                List units = new ArrayList(virtPrdModel.getSalesUnits());
                Collections.sort(units, salesUnitComparator);
                ErpSalesUnitModel lowestRatio = (ErpSalesUnitModel) units.get(0);
                //
                // perform the pricing using the pricing engine
                //
                double defaultPrice = 0.0;
                String defaultPriceUnit = "";
                try {
					FDConfiguration prConf = new FDConfiguration( 1.0, lowestRatio.getAlternativeUnit() );
                    
                    MaterialPrice pricingCondition = PricingEngine.getConfiguredPrice( pr, prConf, new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE)) .getPricingCondition();
                    
                    defaultPrice = pricingCondition.getPrice();
                    defaultPriceUnit = pricingCondition.getPricingUnit();
                    
                } catch (PricingException pe) {
                    String message = "Unable to perform pricing for product " + virtualSku + " from erps.material " + origPrdModel.getProxiedMaterial().getSapId();
                    LOGGER.error(message, pe);
                    throw new LoaderException(pe, message);
                }
                //
                // add default price to virtual product
                //
                
                //virtPrdModel.setDefaultPrice(defaultPrice);
                //virtPrdModel.setDefaultPriceUnit(defaultPriceUnit);
                
                
                //
                // create the new product
                //
                ErpProductEB erpProductEB = null;
                int batchNumber = ((VersionedPrimaryKey)origPrdModel.getPK()).getVersion();
                LOGGER.info("\nCreating new Virtual Product " + virtPrdModel.getSkuCode() + "\n");
                try {
                    erpProductEB = productHome.create(batchNumber, virtPrdModel);
                } catch (CreateException ce) {

                    if (ce.getMessage().indexOf("unique constraint") > -1) {
						LOGGER.warn("Skipping product, it already exists " + virtPrdModel.getSkuCode() + " / "+batchNumber);
						/*               	
                        //
                        // check to see if the exception was caused by a unique constraint violation
                        // if so, it means we really want to replace a product that had been deactivated
                        // in the deactivateDeletedProducts step, remove and then re-create
                        //
                        try {
                            erpProductEB  = productHome.findBySkuCodeAndVersion(virtPrdModel.getSkuCode(), batchNumber);
                            erpProductEB.remove();
                            erpProductEB = productHome.create(batchNumber, virtPrdModel);
                        } catch (ObjectNotFoundException onfe) {
                            //
                            // why would this happen?  if we previously violated the unique constraint,
                            // we ought to be able to locate the item that caused the exception
                            //
                            LOGGER.warn("\nInteresting inconsistency while trying to update SKU " + virtPrdModel.getSkuCode(), onfe);
                            throw ce;
                        }
                       */
                    } else {
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
                    ErpProductModel erpProductModel = (ErpProductModel) erpProductEB.getModel();
                    LOGGER.info("\nSuccessfully created Product " + erpProductModel.getSkuCode() + ", id= " + erpProductModel.getPK().getId() + "\n");
                }
            }
            LOGGER.info("\nCompleted processing Virtual Products\n");
        } catch (NamingException ne) {
            throw new LoaderException(ne, "Unable to find home for ErpProduct");
        } catch (CreateException ce) {
            throw new LoaderException(ce, "Unable to create a new version of an ErpProduct");
        } catch (FinderException fe) {
            throw new LoaderException(fe, "Unable to locate an ErpProduct to replace");
//        } catch (RemoveException re) {
			//throw new LoaderException(re, "Unable to remove an ErpProduct before re-creation");
        } catch (RemoteException re) {
            throw new LoaderException(re, "Unexpected system level exception while trying to create an ErpProduct");
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
            if (ratio1 < ratio2)
                return -1;
            else if (ratio2 < ratio1)
                return 1;
            else
                return 0;
        }
    };
    
    
}
