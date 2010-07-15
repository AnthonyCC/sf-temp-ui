/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ejb.ErpZoneInfoDAO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A session bean that takes a set of anonymous models representing erp objects to be created
 * and processes them in the correct order together in a single batch.
 *
 * @version $Revision$
 * @author $Author$
 */
public class SAPZoneInfoLoaderSessionBean extends SessionBeanSupport {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( SAPZoneInfoLoaderSessionBean.class );
    
    /** Creates new SAPLoaderSessionBean */
    public SAPZoneInfoLoaderSessionBean() {
        super();
    }
    
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    @Override
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
    public void loadData(List<ErpZoneMasterInfo> zoneInfoList) throws LoaderException {
        
        LOGGER.debug("\nBeginning SAPLoaderSessionBean loadData\n");
        
        try {
            //
            // get the naming context
            //
            this.initCtx =  new InitialContext();
            Connection conn = null;
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
                    
                    conn=getConnection();
                    System.out.println("Batch Number is :"+this.batchNumber);
                    SAPZoneInfoLoaderDAO.createZoneMasterInfo(conn,this.batchNumber,zoneInfoList);
                    createHirarchyForZoneInfo(zoneInfoList);
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
                }catch (SQLException sqle) {
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
                LOGGER.error("\nUnable to update ERPS objects.  Unable to begin a UserTransaction.", nse);
                throw new LoaderException(nse, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
            } catch (SystemException se) {
                LOGGER.error("\nUnable to update ERPS objects.  Unable to begin a UserTransaction.", se);
                throw new LoaderException(se, "Unable to update ERPS objects.  Unable to begin a UserTransaction.");
            } 
            catch (Exception se) {
                LOGGER.error("\nUnable to update ERPS objects.  Unable to store zone information.", se);
                throw new LoaderException(se, "Unable to update Zone INformation.  Unable to begin a UserTransaction.");
            } 
            finally {
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
            
        } catch (NamingException ne) {
            LOGGER.error("\nUnable to get naming context to locate components required by the loader.", ne);
            throw new LoaderException(ne, "Unable to get naming context to locate components required by the loader.");
        }
    }
    
    public static final String DEFAULT_ZONE_ID="0000100000";
    public static  Map<String, String>  zoneLevelMap=null;
    static{
    	zoneLevelMap=new HashMap<String, String>();
    	zoneLevelMap.put("00", "0000100000");
    	zoneLevelMap.put("01", "0000100001");
    	zoneLevelMap.put("02", "0000100002");
    }
    
   public void createHirarchyForZoneInfo(List zoneInfoList) throws LoaderException{
	   // identify the level of the data
	   // load parent level for the zone from db
	   	   	   	 
       	Connection conn = null;                      
          try {
               conn = getConnection();
               //
               // get a new batch number
               //                
            // update the zone with parent zone info
        	   for(int i=0;i<zoneInfoList.size();i++){
        		   ErpZoneMasterInfo zoneInfo=(ErpZoneMasterInfo)zoneInfoList.get(i);
        		   String sapId=zoneInfo.getSapId();
        		   if(!zoneLevelMap.containsValue(sapId)){
        			   String  enumServType=zoneInfo.getServiceType().getCode();
        			   String zoneId=zoneLevelMap.get(enumServType);        			  
        			   ErpZoneMasterInfo zInfo= ErpZoneInfoDAO.getZoneInfoDetails(conn, zoneId);
        			   zoneInfo.setParentZone(zInfo);        			  
        		   }else{
        			   if(!DEFAULT_ZONE_ID.equalsIgnoreCase(sapId)){
        				   ErpZoneMasterInfo zInfo= ErpZoneInfoDAO.getZoneInfoDetails(conn,DEFAULT_ZONE_ID);
        				   zoneInfo.setParentZone(zInfo);
        			   }
        		   }        			   
        	   }                               
                 SAPZoneInfoLoaderDAO.updateZoneParentInfo(conn, batchNumber, zoneInfoList);                
                 conn.close();
                 conn=null;              
               
           } catch (SQLException sqle) {
               LOGGER.error("Unable to begin new batch.", sqle);
               if (conn != null) {
                   try {
                       conn.close();
                   } catch (SQLException sqle2) {
                       sqle2.printStackTrace();
                       LOGGER.error("Unable to begin new batch.", sqle2);
                       throw new LoaderException("Unable to begin new batch.  " + sqle2);
                   }
               }
     
           }        
           catch (Exception se) {
               LOGGER.error("\nUnable to update ERPS objects.  Unable to store zone information.", se);
               throw new LoaderException(se, "Unable to update Zone INformation.  Unable to begin a UserTransaction.");
           } 
           finally{
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
        Connection conn = null;
        try {
            utx.begin();
            //
            // set a timeout period for this transaction (in seconds)
            //
            utx.setTransactionTimeout(3000);
            //
            // get the next batch number from the batch sequence
            //
           
            try {
                conn = getConnection();
                //
                // get a new batch number
                //
                
                  this.batchNumber = SAPZoneInfoLoaderDAO.getNextBatchNumber(conn);
                  SAPZoneInfoLoaderDAO.createHistoryData(conn, batchTimestamp, batchNumber);                
                  conn.close();
                  conn=null;
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
        finally{
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
     * makes an entry in the history table indicating that a batch succeeded and is ready for review
     *
     * @throws LoaderException any unexpected errors updating the history table
     */
    public void batchComplete() throws LoaderException {
        
        Connection conn = null;
        String desc="Batch completed successfully";
        try {
            //
            // get connection
            //
            conn = getConnection();

            SAPZoneInfoLoaderDAO.updateHistoryData(conn, batchNumber, EnumApprovalStatus.NEW, desc);
            conn.close();
            conn=null;
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
        String desc="";  
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
                desc="This batch failed and was automatically rejected by the loader.  " + ex.getMessage();
                SAPZoneInfoLoaderDAO.updateHistoryData(conn, batchNumber, EnumApprovalStatus.REJECTED, desc);                
                conn.close();               
                conn=null;
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
        finally{
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
    
}
