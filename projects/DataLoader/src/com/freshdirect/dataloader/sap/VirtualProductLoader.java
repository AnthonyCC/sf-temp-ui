/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.ejb.VirtualProductLoaderHome;
import com.freshdirect.dataloader.sap.ejb.VirtualProductLoaderSB;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class VirtualProductLoader {
    
    /** the url of the server that hosts the loader session bean
     */
    public String serverUrl = "t3://localhost:8080";
    
    //
    // list of exceptions
    //
    /** a list of exceptions that occurred during parsing
     */
    List exceptionList = null;
    
    //
    // parser for virtual product files
    //
    /** a tab-delimited file parser
     */
    VirtualProductParser parser = null;
    
    /** run me
     * @link dependency
     * @param args
     */
    
    public static void main(String[] args) {
        
        System.out.println("\n----- Loader Starting -----");
        
        VirtualProductLoader loader = new VirtualProductLoader();        
        String inputFile = "c:\\temp\\virt_skus.txt";
        
        if (args.length > 0) {
        	for (String arg : args) {
        		if (arg.startsWith("serverURL=")) {
        			loader.serverUrl = arg.substring("serverURL=".length());
        		}
        		else if (arg.startsWith("inputFile=")) {
        			inputFile = arg.substring("inputFile=".length());
        		}
        	}
        }
        try {
            //
            // parse the raw files
            //
            loader.parser.parseFile(inputFile);
            loader.exceptionList.addAll(loader.parser.getExceptions());
            if (!loader.parseSuccessful()) {
                loader.reportParsingExceptions();
            }
            //
            // bail if any problems were found with the data
            //
            if ((!loader.parseSuccessful()))
                return;
            //
            // upload the products
            //
            loader.doLoad();
            
        } catch (LoaderException le) {
            le.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        
        System.out.println("\n\n----- Loader Done -----");
        
    }
    
    
    /** default constructor
     */
    public VirtualProductLoader() {
        //
        // list of exceptions found parsing a file
        //
        this.exceptionList = new LinkedList();
        //
        // create parser
        //
        this.parser = new VirtualProductParser();
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
        
        debug();
        
        if (!doit) return;
        
        System.out.println("\n----- starting doLoad() -----");
        
        Map<String, Map<String, Object>> products = parser.getProducts();
        
        Context ctx = null;
        try {
            ctx = getInitialContext();
            VirtualProductLoaderHome home = (VirtualProductLoaderHome) ctx.lookup("freshdirect.dataloader.VirtualProductLoader");
            
            VirtualProductLoaderSB vpl = home.create();
            
            vpl.loadData(products);
            
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
    
    public void debug() {
        Map<String, Map<String, Object>> products = parser.getProducts();
        for (String vSku : products.keySet()) {
            System.out.println("virtual sku :  " + vSku);
            Map<String, Object> extraInfo = products.get(vSku);
            System.out.println("original sku : " + extraInfo.get(VirtualProductParser.ORIGINAL_SKU));
            System.out.println("hidden sales units : ");
            List units = (List) extraInfo.get(VirtualProductParser.SALES_UNITS);
            for (Iterator uIter = units.iterator(); uIter.hasNext();) {
                System.out.println("\t" + uIter.next());
            }
            System.out.println("hidden characteristics : ");
            List cvs = (List) extraInfo.get(VirtualProductParser.CHAR_VALUES);
            for (Iterator cIter = cvs.iterator(); cIter.hasNext();) {
                Map cvPair = (Map) cIter.next();
                for (Iterator cvIter = cvPair.keySet().iterator(); cvIter.hasNext();) {
                    Object charac = cvIter.next();
                    System.out.println("\t" + charac + " : " + cvPair.get(charac));
                }
            }
            System.out.println();
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
