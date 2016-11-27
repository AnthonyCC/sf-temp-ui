/*
 * KwikeeLoader.java
 *
 * Created on August 21, 2001, 6:38 PM
 */

package com.freshdirect.dataloader.nutrition;

/**
 *
 * @author  knadeem
 * @version
 */

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;

public class KwikeeLoader {
    
    String serverUrl = "t3://127.0.0.1:7005";
    List exceptionList = null;
    KwikeeParser kwikeeParser = null;
    
    /** run me
     * @link dependency
     * @param args
     */
    /*#SAPLoaderSB lnkSAPLoaderSB;*/
    
    public static void main(String[] args) {
        System.out.println("\n----- Loader Starting -----");
        
        KwikeeLoader loader = new KwikeeLoader();
        
        try {
            loader.load("D:\\Nutrition\\kwikee.txt");
            if (loader.parseSuccessful()) {
                loader.doLoad();
            } else {
                loader.reportParsingExceptions();
            }
        } catch (BadDataException bde) {
            bde.printStackTrace();
        } catch (LoaderException le) {
            le.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        
        System.out.println("\n\n----- Loader Done -----");
        
    }
    
    
    /** defautl constructor
     */
    public KwikeeLoader() {
        //
        // list of exceptions found parsing a file
        //
        this.exceptionList = new LinkedList();
        kwikeeParser = new KwikeeParser();
        //
        // create builder and parsers
        //
        
    }
    
    /**
     * indicates whether any parsing errors were found during a run
     *
     * @return true if no exceptions occurred during loading
     */
    public boolean parseSuccessful() {
        return (this.exceptionList.size() == 0);
    }
    
    /**
     * prints all the exceptions found during parsing
     *
     */
    public void reportParsingExceptions() {
        Iterator exIter = exceptionList.iterator();
        while (exIter.hasNext()) {
            BadDataException bde = (BadDataException) exIter.next();
            System.out.println(bde);
        }
    }
    
    /** loads and parses all of the information from a set of files in a single batch
     * @param batchRepository the path to the directory containing batch export folders
     * @param batchFolder the directory within the batchRepository that contains a set of export files
     * @param department the prefix for the names of the files within a batch
     * @throws BadDataException any unrecoverable errors encountered during loading
     */
    public void load(String fileName) throws BadDataException {
        
        System.out.println("\n----- Load Kwikee Data -----");
        this.kwikeeParser.parseFile(fileName);
        this.exceptionList.addAll(this.kwikeeParser.getExceptions());
        
        
    }
    
    
    /** send the results of the parsing and building to the session bean on the server
     * that performs the updates
     * @throws LoaderException any problems encountered by the session bean as it modifies the system
     * @throws RemoteException any system level problems encountered communicating with the session bean
     */
    public void doLoad() throws LoaderException, RemoteException {
        doLoad(true);
    }
    
    /** send the results of the parsing and building to the session bean on the server
     * that performs the updates.  optionally, can be told not to actually perform the load
     * @param doit if false, the load is not actually performed
     * @throws LoaderException any problems encountered by the session bean as it modifies the system
     * @throws RemoteException any system level problems encountered communicating with the session bean
     */
    public void doLoad(boolean doit) throws LoaderException, RemoteException {
        
        if (!doit) return;
        
        System.out.println("\n----- starting doLoad() -----");
        
        ArrayList<ErpNutritionModel> nutritionModels = kwikeeParser.getNutritionModels();
        ErpNutritionModel model;
        
        Context ctx = null;
        try {
            ctx = getInitialContext();
            ErpNutritionHome home = (ErpNutritionHome) ctx.lookup("freshdirect.content.Nutrition");
            
            ErpNutritionSB sb = home.create();
            for(int i = 0, size = nutritionModels.size(); i < size; i++){
                model = nutritionModels.get(i);
                //System.out.println(model.getUpc());
                try{
                    String skuCode = sb.getSkuCodeForUpc(model.getUpc());
                    model.setSkuCode(skuCode);
                    if(skuCode != null && (!skuCode.equals(""))){
                        model.setSkuCode(skuCode);
                        sb.createNutrition(model);
                    }
                }catch(FinderException fe){
                    System.out.println(fe.getMessage());
                }
                
                
            }
            
            System.out.println("\n----- normally exiting doLoad() -----");
            
        } catch (CreateException ce) {
            ce.printStackTrace();
        } catch (NamingException ne) {
            ne.printStackTrace();
        } finally {
            try {
                if (ctx != null)
                    ctx.close();
            } catch (NamingException ne) {
                ne.printStackTrace();
            }
        }
        
    }
    
    /** helper method to find the naming context for locating objects on a server
     * @throws NamingException any problems encountered locating the remote server
     * @return the naming context to use to locate remote components on the server
     */
    protected Context getInitialContext() throws NamingException {
        
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.PROVIDER_URL, serverUrl);
        env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
        return new InitialContext(env);
        
    }
    
    
    
}
