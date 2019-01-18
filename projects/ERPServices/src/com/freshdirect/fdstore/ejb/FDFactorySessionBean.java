package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
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
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Factory session bean implementation for FD objects.
 */
/**
 *@deprecated Please use the FDFactoryController and FDFactoryServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
@Deprecated
public class FDFactorySessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 8471002847721814732L;

	private static final Logger LOGGER = LoggerFactory.getInstance(FDFactorySessionBean.class);

	private transient ErpProductHome productHome = null;
	private transient ErpZoneInfoHome zoneHome = null;

	private transient ErpMaterialHome materialHome = null;

	private transient ErpProductFamilyHome familyHome = null;



	private FDProductHelper productHelper = new FDProductHelper();

	protected ErpInfoSB getErpInfoSB() {
	    return ERPServiceLocator.getInstance().getErpInfoSessionBean();
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
	public Collection<FDProductInfo> getProductInfos(final String[] skus) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();
			List<FDProductInfo> productInfos = new ArrayList<FDProductInfo>(skus.length);

			Collection<ErpProductInfoModel> erpInfos = infoSB.findProductsBySku(skus);
			for (ErpProductInfoModel productInfo: erpInfos) {
				// create FDProductInfo
				try {
					productInfos.add( this.productHelper.getFDProductInfoNew(productInfo));//::FDX::
				} catch (FDResourceException exc) {
					LOGGER.debug("Failed to transform ErpProductInfoModel into FDProductInfo; SKU=" + productInfo.getSkuCode(), exc);
					throw exc;
				}
			}
			return productInfos;
		} catch (RemoteException re) {
			LOGGER.debug("Exception occurred while calling findProductsBySku("+Arrays.toString(skus)+")", re);
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

	public FDProductInfo getProductInfo(ErpProductInfoModel erpProdInfo) throws FDResourceException {
		// create FDProductInfo
		return this.productHelper.getFDProductInfoNew(erpProdInfo);//::FDX::
	}

	
	public Set<String> getModifiedSkus(long lastModified) throws FDResourceException {
		try {
			ErpInfoSB infoSB = this.getErpInfoSB();

			return infoSB.getModifiedSkus(lastModified);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

}
