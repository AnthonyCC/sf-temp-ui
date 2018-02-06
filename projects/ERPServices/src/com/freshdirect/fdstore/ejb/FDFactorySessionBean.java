package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.ejb.ErpGrpInfoHome;
import com.freshdirect.erp.ejb.ErpGrpInfoSB;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.ejb.ErpMaterialEB;
import com.freshdirect.erp.ejb.ErpMaterialHome;
import com.freshdirect.erp.ejb.ErpProductFamilyHome;
import com.freshdirect.erp.ejb.ErpProductFamilySB;
import com.freshdirect.erp.ejb.ErpProductHome;
import com.freshdirect.erp.ejb.ErpZoneInfoHome;
import com.freshdirect.erp.ejb.ErpZoneInfoSB;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.framework.core.SessionBeanSupport;

/**
 * Factory session bean implementation for FD objects.
 */
/**
 *@deprecated Please use the FDFactoryController and FDFactoryServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class FDFactorySessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 8471002847721814732L;

	private transient ErpProductHome productHome = null;
	private transient ErpZoneInfoHome zoneHome = null;
	private transient ErpGrpInfoHome grpHome = null;

	private transient ErpMaterialHome materialHome = null;

	private transient ErpProductFamilyHome familyHome = null;

	
	
	private FDProductHelper productHelper = new FDProductHelper();

	protected ErpInfoSB getErpInfoSB() {
	    return ERPServiceLocator.getInstance().getErpInfoSessionBean();
	}
	
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
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();
	
			// find ErpProductInfo by SKU, and latest version
			ErpProductInfoModel productInfo;
			try {
				productInfo = infoSB.findProductBySku(sku);
			} catch (ObjectNotFoundException ex) {
				throw new FDSkuNotFoundException(ex, "SKU "+sku+" not found");
			}
			
			// create FDProductInfo
			return this.productHelper.getFDProductInfoNew(productInfo);//::FDX::

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
    
	
	public Collection getFilteredSkus(List skuList) throws RemoteException, FDResourceException{
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		try {
			// find ErpProduct by sku & version		
			ErpGrpInfoSB infoSB = grpHome.create();
		    return infoSB.getFilteredSkus(skuList);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
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
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();
	
			// find ErpProductInfo by SKU, and latest version
			ErpProductInfoModel productInfo;
			try {
				productInfo = infoSB.findProductBySku(sku, version);
			} catch (ObjectNotFoundException ex) {
				throw new FDSkuNotFoundException(ex, "SKU "+sku+", version " +version+" not found");
			}
			
			// create FDProductInfo
			return this.productHelper.getFDProductInfoNew(productInfo);//::FDX::

		} catch (RemoteException re) {
			throw new FDResourceException(re);
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
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();
			List productInfos = new ArrayList(skus.length);

			Collection erpInfos = infoSB.findProductsBySku(skus);
			for (Iterator i=erpInfos.iterator(); i.hasNext(); ) {
				ErpProductInfoModel productInfo = (ErpProductInfoModel)i.next();
				// create FDProductInfo
				productInfos.add( this.productHelper.getFDProductInfoNew(productInfo));//::FDX::

			}
			return productInfos;

		} catch (RemoteException re) {
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
	/*public FDProduct getProduct(final String sku, final int version) throws FDSkuNotFoundException, FDResourceException {
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
    }*/
	
	public FDProduct getProduct(final String sku, final int version) throws FDSkuNotFoundException, FDResourceException {
		if (this.materialHome==null) {
			this.lookupMaterialHome();
		}
		try {
			// find ErpProduct by sku & version
			ErpMaterialEB materialEB;
			try {
				materialEB = materialHome.findBySkuCodeAndVersion(sku, version);
			} catch (ObjectNotFoundException fe) {
				throw new FDSkuNotFoundException(fe, "SKU "+sku+" not found");
			}

			return this.productHelper.getFDProduct( (ErpMaterialModel)materialEB.getModel() );

		} catch (FinderException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }

    public Collection getNewSkuCodes(int days) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.findNewSkuCodes(days);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
    
    
	public Collection getSkuCodes(String sapId) throws FDResourceException{
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.findSkusBySapId(sapId);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

    
    
    public Map<String, Integer> getSkusOldness() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getSkusOldness();

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
    
    public Collection getReintroducedSkuCodes(int days) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.findReintroducedSkuCodes(days);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Collection getOutOfStockSkuCodes() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.findOutOfStockSkuCodes();
		} catch (RemoteException re) {
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
	public ErpZoneMasterInfo getZoneInfo(String zoneId) throws FDResourceException {
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
	public Collection getZoneInfos(String zoneIds[]) throws FDResourceException {
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
	public GroupScalePricing getGrpInfo(FDGroup group) throws FDGroupNotFoundException, FDResourceException {
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		try {
			// find ErpProduct by sku & version
					
			ErpGrpInfoSB infoSB = grpHome.create();		
			return infoSB.findGrpInfoMaster(group);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
    
	public ErpProductFamilyModel getFamilyInfo(String familyId) throws FDGroupNotFoundException, FDResourceException {
		if (this.familyHome==null) {
			this.lookupFamilyInfoHome();
		}
		try {
			// find ErpProduct by sku & version
					
			ErpProductFamilySB infoSB = familyHome.create();		
			return infoSB.findFamilyInfo(familyId);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
	
	public ErpProductFamilyModel getSkuFamilyInfo(String materialId) throws FDGroupNotFoundException, FDResourceException {
		if (this.familyHome==null) {
			this.lookupFamilyInfoHome();
		}
		try {
			// find ErpProduct by sku & version
					
			ErpProductFamilySB infoSB = familyHome.create();		
			return infoSB.findSkyFamilyInfo(materialId);

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
	public Collection<GroupScalePricing> getGrpInfos(FDGroup grpIds[]) throws FDResourceException {
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		try {
			// find ErpProduct by sku & version		
			ErpGrpInfoSB infoSB = grpHome.create();
		    return infoSB.findGrpInfoMaster(grpIds);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
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
    
    
    private  void lookupGrpInfoHome() throws FDResourceException {
		if (grpHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			grpHome = (ErpGrpInfoHome) ctx.lookup("freshdirect.erp.GrpInfoManager");
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
	

    private void lookupMaterialHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			this.materialHome = (ErpMaterialHome) ctx.lookup(ErpServicesProperties.getMaterialHome());
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {}
		}
	}
    

    private  void lookupFamilyInfoHome() throws FDResourceException {
		if (familyHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			familyHome = (ErpProductFamilyHome) ctx.lookup("freshdirect.erp.ErpProductFamily");
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

	@Override
    public void ejbCreate() throws CreateException {
		// nothing required
	}
	
	@Override
    public void ejbRemove() {
		// nothing required
	}
	
	public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes)throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	
	public List findPeakProduceSKUsByDepartment(List skuPrefixes)throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();
			return infoSB.findPeakProduceSKUsByDepartment(skuPrefixes);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String, Map<String,Date>> getNewSkus(/*String salesOrg, String distributionChannel*/) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getNewSkus(/*salesOrg,distributionChannel*/);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String, Map<String,Date>> getBackInStockSkus() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getBackInStockSkus();

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getOverriddenNewSkus();

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getOverriddenBackInStockSkus();

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getSkuAvailabilityHistory(skuCode);

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}		
	}

	public void refreshNewAndBackViews() throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			infoSB.refreshNewAndBackViews();

		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}		
	}
	
	public FDGroup  getLatestActiveGroup(String groupId) throws FDGroupNotFoundException, FDResourceException {
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		try {
			// find ErpProduct by sku & version
					
			ErpGrpInfoSB infoSB = grpHome.create();		
			return infoSB.getLatestActiveGroup(groupId);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
    }
	
	public FDProductInfo getProductInfo(ErpProductInfoModel erpProdInfo) throws FDResourceException {
		// create FDProductInfo
		return this.productHelper.getFDProductInfoNew(erpProdInfo);//::FDX::
	}
	
	public  Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws FDResourceException {
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		
		try {								
			ErpGrpInfoSB infoSB = grpHome.create();		
			return infoSB.getGroupIdentityForMaterial(matId);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
	}
	
	public Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws FDResourceException{
		if (this.grpHome==null) {
			this.lookupGrpInfoHome();
		}
		
		try {								
			ErpGrpInfoSB infoSB = grpHome.create();		
			return infoSB.getModifiedOnlyGroups(lastModified);

		} catch (CreateException fe) {
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			this.productHome=null;
			throw new FDResourceException(re);
		}
	}
}
