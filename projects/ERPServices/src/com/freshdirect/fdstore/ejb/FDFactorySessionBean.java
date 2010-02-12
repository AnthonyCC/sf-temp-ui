/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.ejb.ErpProductEB;
import com.freshdirect.erp.ejb.ErpProductHome;
import com.freshdirect.erp.ejb.ErpZoneInfoHome;
import com.freshdirect.erp.ejb.ErpZoneInfoSB;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.SessionBeanSupport;

/**
 * Factory session bean implementation for FD objects.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDFactorySessionBean extends SessionBeanSupport {

	private transient ErpInfoHome infoHome = null;
	private transient ErpProductHome productHome = null;
	private transient ErpZoneInfoHome zoneHome = null;
	
	
	private FDProductHelper productHelper = new FDProductHelper();

	/**
	 * Get current product information object for sku.
	 *
	 * @param sku SKU code
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 */
	public FDProductInfo getProductInfo(final String sku) throws FDSkuNotFoundException, FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();
	
			// find ErpProductInfo by SKU, and latest version
			ErpProductInfoModel productInfo;
			try {
				productInfo = infoSB.findProductBySku(sku);
			} catch (ObjectNotFoundException ex) {
				throw new FDSkuNotFoundException(ex, "SKU "+sku+" not found");
			}
			
			// create FDProductInfo
			return this.productHelper.getFDProductInfo(productInfo);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}
    
    /**
	 * Get current product information object for a specific version of a sku.
	 *
	 * @param sku SKU code
     * @param version requested version
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public FDProductInfo getProductInfo(final String sku, final int version) throws FDSkuNotFoundException, FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();
	
			// find ErpProductInfo by SKU, and latest version
			ErpProductInfoModel productInfo;
			try {
				productInfo = infoSB.findProductBySku(sku, version);
			} catch (ObjectNotFoundException ex) {
				throw new FDSkuNotFoundException(ex, "SKU "+sku+", version " +version+" not found");
			}
			
			// create FDProductInfo
			return this.productHelper.getFDProductInfo(productInfo);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects that were found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public Collection getProductInfos(final String[] skus) throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();
			List productInfos = new ArrayList(skus.length);

			Collection erpInfos = infoSB.findProductsBySku(skus);
			for (Iterator i=erpInfos.iterator(); i.hasNext(); ) {
				ErpProductInfoModel productInfo = (ErpProductInfoModel)i.next();
				// create FDProductInfo
				productInfos.add( this.productHelper.getFDProductInfo(productInfo));

			}
			return productInfos;

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}

	/**
	 * Get product with specified version. 
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 */
	public FDProduct getProduct(final String sku, final int version) throws FDSkuNotFoundException, FDResourceException {
		if (this.productHome==null) {
			this.lookupProductHome();
		}
		try {
			// find ErpProduct by sku & version
			ErpProductEB productEB;
			try {
				productEB = productHome.findBySkuCodeAndVersion(sku, version);
			} catch (ObjectNotFoundException fe) {
				throw new FDSkuNotFoundException(fe, "SKU "+sku+" not found");
			}

			return this.productHelper.getFDProduct( (ErpProductModel)productEB.getModel() );

		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
    
    public Collection getNewSkuCodes(int days) throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();

			return infoSB.findNewSkuCodes(days);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}
    
    public Map<String, Integer> getSkusOldness() throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();

			return infoSB.getSkusOldness();

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}
    
    public Collection getReintroducedSkuCodes(int days) throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();

			return infoSB.findReintroducedSkuCodes(days);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}

	public Collection getOutOfStockSkuCodes() throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();

			return infoSB.findOutOfStockSkuCodes();

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}
	
	
	
	
	
	
	/**
	 * Get product with specified version. 
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 */
	public ErpZoneMasterInfo getZoneInfo(String zoneId) throws FDSkuNotFoundException, FDResourceException {
		if (this.zoneHome==null) {
			this.lookupZoneInfoHome();
		}
		try {
			// find ErpProduct by sku & version
			
		
			ErpZoneInfoSB infoSB = zoneHome.create();
		
			return infoSB.findZoneInfoMaster(zoneId);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
    

	/**
	 * Get product with specified version. 
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 */
	public Collection getZoneInfos(String zoneIds[]) throws FDSkuNotFoundException, FDResourceException {
		if (this.zoneHome==null) {
			this.lookupZoneInfoHome();
		}
		try {
			// find ErpProduct by sku & version
		
			ErpZoneInfoSB infoSB = zoneHome.create();
		
			return infoSB.findZoneInfoMaster(zoneIds);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
	
	private void lookupInfoHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.infoHome = (ErpInfoHome) ctx.lookup("java:comp/env/ejb/ErpInfo");
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}

	private void lookupProductHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.productHome = (ErpProductHome) ctx.lookup("java:comp/env/ejb/ErpProduct");
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}

	
	   
    private  void lookupZoneInfoHome() throws FDResourceException {
		if (zoneHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			zoneHome = (ErpZoneInfoHome) ctx.lookup("freshdirect.erp.ZoneInfoManager");
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}
	
	public void ejbCreate() throws CreateException {
		// nothing required
	}
	
	public void ejbRemove() {
		// nothing required
	}
	
	public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes)throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();

			return infoSB.findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}
	
	
	public List findPeakProduceSKUsByDepartment(List skuPrefixes)throws FDResourceException {
		if (this.infoHome==null) {
			this.lookupInfoHome();	
		}
		try {
			ErpInfoSB infoSB = this.infoHome.create();
			return infoSB.findPeakProduceSKUsByDepartment(skuPrefixes);

		} catch (RemoteException re) {
			this.infoHome=null;
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			this.infoHome=null;
			throw new FDResourceException(ce);
		}
	}

}
