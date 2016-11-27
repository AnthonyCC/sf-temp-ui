/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.SynchronousParser;
import com.freshdirect.dataloader.SynchronousParserClient;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DetailLoader implements SynchronousParser, SynchronousParserClient {
    
    /** the url of the server that hosts the loader session bean
     */
    String serverUrl = "t3://localhost:8080";
    
    private SynchronousParserClient client = null;
    
    //
    // list of exceptions
    //
    /** a list of exceptions that occurred during parsing
     */
    List exceptionList = null;
    
    //
    // parser for reconcilation files from Chase
    //
    /** a tab-delimited file parser
     */
    DetailParser parser = null;
    
    public void parseFile(InputStream fileStream){
    	System.out.println("\n----- Loader Starting -----");
        
        //DetailLoader loader = new DetailLoader();
        
        try {
            //
            // parse the raw files
            //
            this.parser.parseFile(fileStream);
            this.exceptionList.addAll(this.parser.getExceptions());
            if (!this.parseSuccessful()) {
                this.reportParsingExceptions();
            }
            //
            // bail if any problems were found with the data
            //
            if ((!this.parseSuccessful()))
                return;
            //
            // upload the parsed objects
            //
            this.doLoad();
            
        } catch (LoaderException le) {
            le.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        
        System.out.println("\n\n----- Loader Done -----");
    	
    }
    
    /** run me
     * @link dependency
     * @param args
     */
    
    public static void main(String[] args) {
        
        System.out.println("\n----- Loader Starting -----");
        
        DetailLoader loader = new DetailLoader();
        
        try {
            //
            // parse the raw files
            //
            loader.parser.parseFile("d:/settlements/E012.txt");
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
            // upload the parsed objects
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
    public DetailLoader() {
        //
        // list of exceptions found parsing a file
        //
        this.exceptionList = new LinkedList();
        //
        // create parser
        //
        this.parser = new DetailParser();
        this.parser.setClient(this);
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
            System.out.println();
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
        
        if (!doit) return;
        
        System.out.println("\n----- starting doLoad() -----");
        
        /*
        
        Context ctx = null;
        try {
            ctx = getInitialContext();
            ReconciliationLoaderHome home = (ReconciliationLoaderHome) ctx.lookup("freshdirect.dataloader.ReconciliationLoader");
            
            ReconciliationLoaderSB vpl = home.create();
            
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
         *
        */
        
    }
    
    public void accept(Object o){
    	this.client.accept(o);
    	
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
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
