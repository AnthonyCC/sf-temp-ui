package com.freshdirect.fdstore.coremetrics;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.logistics.framework.util.LoggerFactory;

/**
 * CoreMetrics context
 * 
 * @author segabor
 *
 */
public class CmContext implements Serializable {
	private static final long serialVersionUID = -2813839304782457210L;

	private static final Logger LOGGER = LoggerFactory.getInstance(CmContext.class);
	
	
	private static CmContext sharedInstance = null;

	/**
	 * Enterprise ID := FD client ID in CoreMetrics system
	 */
	public static final String ENTERPRISE_ID = "51640000";
	
	
	/**
	 * If enabled, CM sends events from storefront
	 */
	private boolean isEnabled = true;
	
	
	/**
	 * CoreMetrics instance
	 */
	private CmInstance instance = CmInstance.FDW;


	/**
	 * CoreMetrics Compound Client ID
	 * 
	 * Compound ID consists of ENTERPRISE ID and Client ID, separated by a pipe symbol
	 * 
	 * @see CmInstance
	 */
	private String compoundId;

	/**
	 * Context running with test account
	 */
	private boolean testAccount;
	
	protected void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	protected void setInstance(CmInstance instance) {
		this.instance = instance;
	}
	
	public CmInstance getInstance() {
		return instance;
	}
	
	protected void setCompoundId(String compoundId) {
		this.compoundId = compoundId;
	}
	
	public String getCompoundId() {
		return compoundId;
	}
	
	protected void setTestAccount(boolean testAccount) {
		this.testAccount = testAccount;
	}
	
	public boolean isTestAccount() {
		return testAccount;
	}


	/**
	 * Return CM Client ID
	 * @return
	 */
	public String getClientId() {
		if (CmInstance.GLOBAL == instance) {
			return ENTERPRISE_ID;
		}

		return instance != null && CmInstance.UNKNOWN != instance
				? instance.getClientId(testAccount)
				: null
		;
	}

	
	/**
	 * Default context getter
	 * @return
	 */
	public static CmContext getContext() {
		if (sharedInstance == null) {
			synchronized(CmContext.class) {
				if (sharedInstance == null) {
					sharedInstance = new Builder()
						.setDefaultClientId()
						.setCmInstance()
						.setEnabled()
						.build();
					
					LOGGER.info("CmContext " + sharedInstance + " is created");
				}
			}
		}

		return sharedInstance;
	}



	/**
	 * Build specific context based on parameters
	 * 
	 * @param eStore
	 * @param facade
	 * @param test
	 * @return
	 */
	public static CmContext createContextFor(EnumEStoreId eStore, CmFacade facade, boolean test) {
		CmContext ctx = new Builder()
			.setEStoreId(eStore)
			.setFacade(facade)
			.setTestAcc(test)
			.setCmInstance()
			.build();
		
		LOGGER.info("CmContext " + ctx + " is created");

		return ctx;
	}
	
	
	
	public static CmContext createGlobalContext() {
		CmContext ctx = new Builder()
			.setGlobal()
			.build();
	
		LOGGER.info("CmContext " + ctx + " is created");
	
		return ctx;
	}
	
	
	/**
	 * Use {@link CmContext#getContext()} instead of using constructor.
	 */
	public CmContext() {
	}



	/**
	 * Return category ID prefixed with CM instance name
	 * 
	 * @param categoryId
	 * @return
	 */
	public String prefixedCategoryId(String categoryId) {
		return categoryId != null
				? instance != null
					? instance.name() + "_" + categoryId
					: categoryId
				: null
		;
	}

	@Override
	public String toString() {

		if (instance == null || CmInstance.UNKNOWN == instance) {
			return "UNKNOWN/UNDEFINED";
		} else {
			StringBuilder s = new StringBuilder();

			
			s.append("{")
				.append(instance.name())
				.append(",store:" + instance.getEStoreId())
				.append(",facade:"+ instance.getFacade())
				.append(",test:"+ testAccount)
				.append("}")
			;
			
			return s.toString();
		}
	}
	
	

	/**
	 * CoreMetrics context builder
	 * 
	 * @author segabor
	 *
	 */
	public static class Builder {
		// group #1
		private String clientId = null;
		private boolean global = false; // retained for global configuration
		
		// group #2
		private EnumEStoreId eStoreId = null;
		private CmFacade facade = null;
		
		// calculated
		private boolean isEnabled;
		// calculated
		private CmInstance instance = CmInstance.FDW;
		// calculated / group #2
		private boolean testAcc = true;
		// by default, compound client id consists of FD ID and FDW client ID (test)
		// calculated
        private String compoundId = ENTERPRISE_ID + "|" + FDStoreProperties.getCoremetricsClientId();
		
		
		// --- group #1 setters ---
		
		public Builder setDefaultClientId() {
			this.clientId = FDStoreProperties.getCoremetricsClientId(); return this;
		}
		
		public Builder setClientId(String clientId) {
			this.clientId = clientId; return this;
		}

		public Builder setGlobal() {
			this.global = true; return this;
		}

		// --- group #2 setters ---
		
		public Builder setEStoreId(EnumEStoreId eStoreId) {
			this.eStoreId = eStoreId; return this;
		}
		
		public Builder setFacade(CmFacade facade) {
			this.facade = facade; return this;
		}
		
		public Builder setTestAcc(boolean testAcc) {
			this.testAcc = testAcc; return this;
		}
		
		
		// --- configurators ---
		
		/**
		 * Determine CoreMetrics instance from CM Client ID
		 * 
		 * @return
		 */
		public Builder setCmInstance() {
			if (global) {
				// Not necessary to call this explicitly as builder will take care of
				// setting proper instance when global flag is turned on
				this.instance = CmInstance.GLOBAL;
				return this;
			}

			if (clientId != null) {
				CmInstance result = CmInstance.lookupByClientId(clientId);
				if (result != null) {
					this.instance = result; this.testAcc = false;
					LOGGER.debug("Found production instance " + result);
				} else {
					result = CmInstance.lookupByTestClientId(clientId);
					if (result != null) {
						this.instance = result; this.testAcc = true;
						LOGGER.debug("Found test instance " + result);
					} else {
						throw new IllegalStateException("Builder was not able to determine CM instance from client ID " + clientId);
					}
				}
			} else if ( eStoreId != null && facade != null ) {
				for (CmInstance i : CmInstance.values()) {
					if (CmInstance.UNKNOWN == i)
						continue;
					
					if (eStoreId == i.getEStoreId() && facade == i.getFacade()) {
						this.instance = i;
						break;
					}
				}
			} else {
				throw new IllegalStateException("Error configuring instance!");
			}
			return this;
		}


		/**
		 * Determines whether CM reporting is enabled for an instance
		 * 
		 * May not be called before {@link CmContext.Builder#setCmInstance()
		 */
		public Builder setEnabled() {
			// CM events are turned on by default
			this.isEnabled = !( CmInstance.SDSW == instance ); return this;
		}

		public Builder setCompoundId() {
			// compose client id from central ID and client ID
			this.compoundId = ENTERPRISE_ID + "|" + instance.getClientId(this.testAcc); return this;
		}


		public CmContext build() {
			CmContext ctx = new CmContext();

			if (global) {
				// Do not send events as global ctx is abstract
				ctx.setEnabled(false);
				ctx.setInstance(CmInstance.GLOBAL);
				// TO BE CONFIRMED
				ctx.setCompoundId(ENTERPRISE_ID);
			} else {
				ctx.setEnabled(isEnabled);
				ctx.setInstance(instance);
				ctx.setTestAccount(testAcc);
				ctx.setCompoundId(compoundId);
			}
			

			return ctx;
		}
	}
}
