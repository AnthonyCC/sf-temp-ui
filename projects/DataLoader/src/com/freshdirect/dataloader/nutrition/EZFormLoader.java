/*
 * EZFormLoader.java
 *
 * Created on August 22, 2001, 4:07 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class EZFormLoader {
    
    String serverUrl = "t3://localhost:80";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        EZFormLoader s = new EZFormLoader();
        s.go();
    }
    
    EZFormNutritionParser nutriParser = null;
    EZFormIngredientParser ingrediParser = null;
    
    /** Creates new slurp */
    public EZFormLoader() {
        nutriParser = new EZFormNutritionParser();
        ingrediParser = new EZFormIngredientParser();
    }
    
    public void go() {
        
        nutriParser.parseFile("D:/nutrition/Nfpdata.txt");
        List exceps = nutriParser.getExceptions();
        if (exceps.size() > 0) {
            Iterator excepIter = exceps.iterator();
            while (excepIter.hasNext()) {
                Exception ex = (Exception) excepIter.next();
                System.out.println(ex);
            }
        }
        
        
        ingrediParser.parseFile("D:/nutrition/Ingstats.txt");
        exceps = ingrediParser.getExceptions();
        if (exceps.size() > 0) {
            Iterator excepIter = exceps.iterator();
            while (excepIter.hasNext()) {
                Exception ex = (Exception) excepIter.next();
                System.out.println(ex);
            }
        }
        
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
        
        //
        // combine ingredients with nutrition info in a single ErpNutritionModel
        //
        Map<String, ErpNutritionModel> nutrition = nutriParser.getNutrition();
        Map<String, String> ingredients = ingrediParser.getIngredients();
        for (String skuCode : ingredients.keySet()) {
            if (nutrition.containsKey(skuCode)) {
                ErpNutritionModel enm = nutrition.get(skuCode);
                enm.setIngredients(ingredients.get(skuCode));
            } else {
                ErpNutritionModel enm = new ErpNutritionModel();
                enm.setSkuCode(skuCode);
                enm.setIngredients(ingredients.get(skuCode));
                nutrition.put(skuCode, enm);
            }
        }
        
        Context ctx = null;
        try {
            ctx = getInitialContext();
            ErpNutritionHome home = (ErpNutritionHome) ctx.lookup("freshdirect.content.Nutrition");
            
            ErpNutritionSB sb = home.create();
            for (String skuCode : nutrition.keySet()) {
                System.out.println("Loading nutrition for " + skuCode);
                ErpNutritionModel enm = nutrition.get(skuCode);
				ErpNutritionModel oldEnm = sb.getNutrition(enm.getSkuCode());
				
				if(enm.getHeatingInstructions().equals(""))
					enm.setHeatingInstructions(oldEnm.getHeatingInstructions());
				
				if(enm.getIngredients().equals(""))
					enm.setIngredients(oldEnm.getIngredients());

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
