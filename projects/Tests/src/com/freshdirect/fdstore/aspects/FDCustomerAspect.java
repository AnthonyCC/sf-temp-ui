package com.freshdirect.fdstore.aspects;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;

import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEB;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.ModelProducerI;
import com.freshdirect.framework.core.PrimaryKey;

public abstract class FDCustomerAspect extends BaseAspect {

    public FDCustomerAspect() {
        super(new MethodPatternPointcut("FDCustomerHome\\.findByPrimaryKey"));
    }

    @Override
    public void intercept(InvocationContext arg0) throws Exception {
        PrimaryKey pk = (PrimaryKey) arg0.getParamVals()[0];
        arg0.setReturnObject(getCustomer(pk));
    }

    public ModelProducerI getCustomer(final PrimaryKey pk) {
        return new FDCustomerEB() {
            
            @Override
            public void setErpCustomerPK(String erpCustomerPK) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setDefaultShipToAddressPK(String addressPK) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setDefaultPaymentMethodPK(String pmPK) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setDefaultDepotLocationPK(String locationId) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void incrementLoginCount() throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public int getLoginCount() throws RemoteException {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Date getLastLogin() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getErpCustomerPK() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getDefaultShipToAddressPK() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getDefaultPaymentMethodPK() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getDefaultDepotLocationPK() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void remove() throws RemoteException, RemoveException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isIdentical(EJBObject arg0) throws RemoteException {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public Object getPrimaryKey() throws RemoteException {
                return pk;
            }
            
            @Override
            public Handle getHandle() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public EJBHome getEJBHome() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void setFromModel(ModelI model) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public PrimaryKey getPK() throws RemoteException {
                return pk;
            }
            
            @Override
            public void invalidate() throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void updatePasswordHint(String s) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setProfileAttribute(String name, String value) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setDepotCode(String depotCode) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void removeProfileAttribute(String name) throws RemoteException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public int incrementPasswordRequestAttempts() throws RemoteException {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Date getPasswordRequestExpiration() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getDepotCode() throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String generatePasswordRequest(Date expiration) throws RemoteException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void erasePasswordRequest() throws RemoteException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public ModelI getModel() throws RemoteException {
                return getCustomerModel(pk);
            }

			@Override
			public int getPymtVerifyAttempts() throws RemoteException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int incrementPymtVerifyAttempts() throws RemoteException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void resetPymtVerifyAttempts() throws RemoteException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setPymtVerifyAttempts(int pymtVerifyAttempts)
					throws RemoteException {
				// TODO Auto-generated method stub
				
			}
        };
    }

    public abstract FDCustomerModel getCustomerModel(PrimaryKey pk);

}
