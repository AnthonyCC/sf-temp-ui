/*
 * InvoiceLoader.java
 *
 * Created on October 3, 2001, 12:58 PM
 */

package com.freshdirect.dataloader.payment;

/**
 *
 * @author  knadeem
 * @version
 */
import java.util.*;

import javax.ejb.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import javax.naming.*;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.*;
import com.freshdirect.dataloader.*;
import com.freshdirect.dataloader.payment.ejb.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;


public class InvoiceLoader implements ConsumerI {
    
    private final static Category LOGGER = LoggerFactory.getInstance( InvoiceLoader.class );
	
	private final String INVOICE = "F2";
	private final String RETURN = "RE";
	private List exceptionList = null;
	private InvoiceParser invoiceParser = null;
	private HashMap invoiceMap = null;
	private HashMap returnsMap = null;
	private HashMap shippingInfoMap = null;
	private InvoiceLoaderHome invoiceLoaderHome = null;
	
	
	/** Creates new InvoiceLoader */
	public InvoiceLoader() {
		this.exceptionList = new LinkedList();
		invoiceParser = new InvoiceParser();
		invoiceMap = new HashMap();
		returnsMap = new HashMap();
		shippingInfoMap = new HashMap();
	}
	
	public void processInvoiceBatch(String fileName) throws LoaderException {
		try {
			load(fileName);
			if (!this.parseSuccessful()) {
                this.logExceptions(this.exceptionList);
                String msg = this.createErrorMessage(this.exceptionList);
                throw new LoaderException(msg);
			} else {
				List eList = this.doLoad();
				if(!eList.isEmpty()){
					this.logExceptions(eList);
					String msg = this.createErrorMessage(eList);
					throw new LoaderException(msg);
				}
			}
		} catch (BadDataException bde) {
			throw new LoaderException(bde);
		}
	}
	
	private void logExceptions(List eList){
		for(Iterator i = eList.iterator(); i.hasNext(); ){
			Exception e = (Exception) i.next();
			LOGGER.warn("Error occured", e);
		}
	}
	
	private String createErrorMessage(List eList){
		StringBuffer msg = new StringBuffer();
		msg.append("Invoice batch contained errors:\n");
		for(Iterator i = eList.iterator(); i.hasNext(); ){
			Exception e = (Exception) i.next();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			msg.append(sw.getBuffer());
			msg.append("\n");
			msg.append(e.getMessage()).append("\n");
		}
		return msg.toString();
	}
		
	
	public static void main(String[] args){
		InvoiceLoader loader = new InvoiceLoader();
		if(args.length  <= 0){
			System.out.println("Usage: java InvoiceLoader fully/qualified/filename");
			System.exit(0);
		}
		String fileName = args[0];
		try{
			loader.processInvoiceBatch(fileName);
		}catch(LoaderException e){
			LOGGER.error("LoaderException: ", e);
		} 
	}
	
	public boolean parseSuccessful() {
		return this.exceptionList.isEmpty();
	}
	
	public void load(String fileName) throws BadDataException {
		invoiceParser.setConsumer(this);
		this.invoiceParser.parseFile(fileName);
        this.exceptionList.addAll(this.invoiceParser.getExceptions());
		
	}
	
	public List doLoad() throws LoaderException{
		List eList = new ArrayList();
		if(this.invoiceLoaderHome == null){
			this.lookupInvoiceLoaderHome();
		}
		InvoiceLoaderSB sb;
		try{
			sb = this.invoiceLoaderHome.create();
		}catch(CreateException ce){
			throw new LoaderException(ce);
		}catch(RemoteException re){
			throw new LoaderException(re);
		}

		try{
			for(Iterator i = this.invoiceMap.keySet().iterator(); i.hasNext(); ){ 
				String saleId = (String)i.next();
				ErpShippingInfo shippingInfo = (ErpShippingInfo)this.shippingInfoMap.get(saleId);
				sb.addAndReconcileInvoice(saleId, (ErpInvoiceModel)this.invoiceMap.get(saleId), shippingInfo);
			}
		}catch(ErpTransactionException e){
			eList.add(new LoaderException(e, e.getMessage()));
		}catch(RemoteException e){
			eList.add(new LoaderException(e, e.getMessage()));
		}
		return eList;
	}
	
	public void consume(Object o, String webOrderNum, String billingType, ErpShippingInfo shippingInfo) {
		if(INVOICE.equalsIgnoreCase(billingType)){
			if(this.invoiceMap.containsKey(webOrderNum)){
				this.exceptionList.add(new BadDataException("more than one invoice found for webOrderNum: "+webOrderNum));
			}else{
				this.invoiceMap.put(webOrderNum, o);
				this.shippingInfoMap.put(webOrderNum, shippingInfo);
			}
		}
		if(RETURN.equalsIgnoreCase(billingType)) {
			if(this.returnsMap.containsKey(webOrderNum)){
				this.exceptionList.add(new BadDataException("more than one return found for webOrderNum: "+webOrderNum));
			}else{
				this.returnsMap.put(webOrderNum, o);
			}
		}
    }
	
	private void lookupInvoiceLoaderHome() throws EJBException {
		Context ctx = null;
		try {
			Hashtable h = new Hashtable();
			h.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
			h.put( Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
			ctx = new InitialContext(h);
			this.invoiceLoaderHome = (InvoiceLoaderHome) ctx.lookup("freshdirect.dataloader.InvoiceLoader");
		} catch (NamingException ex) {
            LOGGER.debug(ex);
			throw new EJBException(ex);
		} finally {
			try {
				if(ctx != null){
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
                LOGGER.debug(ne);
            }
		}
	} 
}
