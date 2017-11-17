package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SAPProductFamilyLoaderSessionBean extends SessionBeanSupport {

	 private static Category LOGGER = LoggerFactory.getInstance( SAPProductFamilyLoaderSessionBean.class );

	public SAPProductFamilyLoaderSessionBean() {
		super();
		// TODO Auto-generated constructor stub
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
    
    public void loadData(List productFamilyList) throws RemoteException, LoaderException{
    	
    LOGGER.debug("\nBeginning SAPLoaderSessionBean loadData\n");
    
    try {
 
        this.initCtx =  new InitialContext();
        Connection conn = null;
        try {

            beforeBatch();
            
            UserTransaction utx = getSessionContext().getUserTransaction();

            utx.setTransactionTimeout(30000);
            
            try {
                utx.begin();

                conn=getConnection();
               LOGGER.debug("Batch Number is ##### :"+this.batchNumber);
                LOGGER.debug("productfamilyLIst######"+productFamilyList.size());
                SAPProductFamilyLoaderDAO.createProductFamilyMasterInfo(conn,this.batchNumber,productFamilyList);
                LOGGER.debug("data successfully inserted in erp.product_family_master");
              // batchComplete();                    
                try {
                    //
                    // try to commit all the changes together
                    //
                    utx.commit();
                    LOGGER.debug("\nCompleted SAPProductFamilyLoaderSessionBean loadData\n");
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
                    LOGGER.error("\nUnexpected runtime exception in SAPProductFamilyLoaderSessionBean loadData", rune);
                    throw new LoaderException(rune, "Unexpected runtime exception");
                }
                
            } catch (LoaderException le) {
                utx.setRollbackOnly();
                LOGGER.error("\n\nAborting SAPProductFamilyLoaderSessionBean loadData\n", le);
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
  
    public void updateCacheWithProdFly(List<String >familyIds) throws RemoteException, LoaderException{

    	
        LOGGER.debug("\nBeginning SAPProductFamilyLoaderSessionBean updateCache\n");
        
        try {
 
            this.initCtx =  new InitialContext();
            Connection conn = null;
            try {

                
                UserTransaction utx = getSessionContext().getUserTransaction();

                utx.setTransactionTimeout(30000);
                
                try {
                    utx.begin();
       
                    
                    conn=getConnection();
                    for(String familyId: familyIds){
                    List<String> skuCodes = SAPProductFamilyLoaderDAO.fetchProductFamilyMasterInfo(conn,familyId);
                        CmsServiceLocator.ehCacheUtil().putListToCache(CmsCaches.FD_FAMILY_PRODUCT_CACHE.cacheName, familyId, skuCodes);
                    }
                   
                    try {
 
                        utx.commit();
                        LOGGER.debug("\nCompleted SAPProductFamilyLoaderSessionBean updateCache\n");
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
                        LOGGER.error("\nUnexpected runtime exception in SAPProductFamilyLoaderSessionBean updateCache", rune);
                        throw new LoaderException(rune, "Unexpected runtime exception");
                    }
                    
                } catch (LoaderException le) {
                    utx.setRollbackOnly();
                    LOGGER.error("\n\nAborting SAPProductFamilyLoaderSessionBean updateCache\n", le);
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
                LOGGER.debug("connecton obj $$$$$$$$"+conn);
                  this.batchNumber = SAPProductFamilyLoaderDAO.getNextBatchNumber(conn);
                  SAPProductFamilyLoaderDAO.createHistoryData(conn, batchTimestamp, batchNumber);                
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
	 
    public void batchComplete() throws LoaderException {
        
        Connection conn = null;
        String desc="Batch completed successfully";
        try {
            //
            // get connection
            //
            conn = getConnection();

            SAPProductFamilyLoaderDAO.updateHistoryData(conn, batchNumber, EnumApprovalStatus.NEW, desc);
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
    
    public void afterBatchFailed(Exception ex) throws LoaderException {
        //
        // an error occurred while doing the load and the transaction was rolled back
        // make a note in the history table so there don't appear to be any gaps in the
        // version numbers
        //
        // do this step in a separate transaction
        //
        UserTransaction utx = getSessionContext().getUserTransaction();
        
        //System.out.println("Batch version is " + batchNumber);
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
                SAPProductFamilyLoaderDAO.updateHistoryData(conn, batchNumber, EnumApprovalStatus.REJECTED, desc);                
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
