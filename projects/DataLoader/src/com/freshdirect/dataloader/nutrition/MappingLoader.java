/*
 * MappingLoader.java
 *
 * Created on August 22, 2001, 12:30 PM
 */

package com.freshdirect.dataloader.nutrition;

/**
 *
 * @author  knadeem
 * @version
 */

import java.util.*;

import javax.ejb.*;
import java.rmi.RemoteException;
import javax.naming.*;

import com.freshdirect.dataloader.*;
import com.freshdirect.content.nutrition.ejb.*;

public class MappingLoader implements SynchronousParserClient {
    
    String serverUrl = "t3://127.0.0.1:7005";
    List exceptionList = null;
    SkuUpcParser mappingParser = null;
    Context ctx;
    ErpNutritionSB sb;
    
    /** Creates new MappingLoader */
    public MappingLoader() {
        mappingParser = new SkuUpcParser();
        exceptionList = new ArrayList();
    }
    
    public static void main(String[] args) {
        System.out.println("\n----- Loader Starting -----");
        
        MappingLoader loader = new MappingLoader();
        loader.load("D:\\Nutrition\\dairy.txt");
        loader.reportParsingExceptions();
        System.out.println("\n\n----- Loader Done -----");
        
    }
    
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
    public void load(String fileName) {
        try{
            ctx = getInitialContext();
            ErpNutritionHome home = (ErpNutritionHome) ctx.lookup("freshdirect.content.Nutrition");
            sb = home.create();
                        
            System.out.println("\n----- Load Mappings -----");
            mappingParser.setClient(this);
            this.mappingParser.parseFile(fileName);
            this.exceptionList.addAll(this.mappingParser.getExceptions());
            
            ctx.close();
            
        }catch (CreateException ce) {
            ce.printStackTrace();
        }catch (NamingException ne) {
            ne.printStackTrace();
        }catch (RemoteException re){
            re.printStackTrace();
        }
        
    }
    
    public void accept(Object o) {
        System.out.println("In accept");
        
        SkuUpcParser.SkuUpcMap map = (SkuUpcParser.SkuUpcMap)o;
        try{
            System.out.println("Trying to call the bean");
            sb.createUpcSkuMapping(map.getSkuCode(), map.getUpc());
            System.out.println("After Calling the  bean");
        }catch(EJBException ee){
            System.out.println("EJBException");
            ee.printStackTrace();
        }catch(RemoteException re){
            System.out.println("RemoteException");
            re.printStackTrace();
        }
    }
    
    /** helper method to find the naming context for locating objects on a server
     * @throws NamingException any problems encountered locating the remote server
     * @return the naming context to use to locate remote components on the server
     */
    protected Context getInitialContext() throws NamingException {
        
        Hashtable env = new Hashtable();
        env.put(Context.PROVIDER_URL, serverUrl);
        env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
        return new InitialContext(env);
        
    }
}
