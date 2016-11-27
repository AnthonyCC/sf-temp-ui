/*
 * EZFormLoader.java
 *
 * Created on August 22, 2001, 4:07 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.dataloader.LoaderException;

/**
 * loads nutrition info from an EZForm export into ERPS
 *
 * @author  mrose
 * @version
 */
public class EshaLoader {
    
    String serverUrl = "t3://localhost:80";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        EshaLoader s = new EshaLoader();
        s.go();
    }
    
    EshaSpreadsheetParser parser = null;
    
    /** Creates new slurp */
    public EshaLoader() {
        parser = new EshaSpreadsheetParser();
    }
    
    public void go() {
        
        parser.parseFile("c:/product_data/nutrition/2004_04_06.xls");
        /*
        List exceps = parser.getExceptions();
        if (exceps.size() > 0) {
            Iterator excepIter = exceps.iterator();
            while (excepIter.hasNext()) {
                Exception ex = (Exception) excepIter.next();
                System.out.println(ex);
            }
        }
        */
        try {
            doLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
        
        ArrayList<ErpNutritionModel> nutrition = parser.getNutrition();
        
        Context ctx = null;
        try {
            ctx = getInitialContext();
            ErpNutritionHome home = (ErpNutritionHome) ctx.lookup("freshdirect.content.Nutrition");
            
            ErpNutritionSB sb = home.create();
            for (ErpNutritionModel enm : nutrition) {
                ErpNutritionModel oldEnm = sb.getNutrition(enm.getSkuCode());

                if(enm.getHeatingInstructions().equals(""))
                    enm.setHeatingInstructions(oldEnm.getHeatingInstructions());
                
                if(enm.getIngredients().equals(""))
                    enm.setIngredients(oldEnm.getIngredients());
                
                System.out.println("Loading nutrition for " + enm.getSkuCode());
                sb.updateNutrition(enm, "dataloader");
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
