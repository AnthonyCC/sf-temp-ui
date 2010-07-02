/*
 * ProductPriceFixer.java
 *
 * Created on August 29, 2001, 3:13 PM
 */

package com.freshdirect.dataloader.sap;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.erp.ejb.ErpMaterialEB;
import com.freshdirect.erp.ejb.ErpMaterialHome;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.FDConfiguration;


/**
 *
 * @author  mrose
 * @version
 */
public class ProductPriceFixer {
    
    public static void main(String[] args) {
        ProductPriceFixer ppf = new ProductPriceFixer();
        ppf.fix();
    }
    
    boolean worked = false;
    
    public void fix() {
        Connection conn = null;
        try {
            
            conn = getConnection();
            conn.setAutoCommit(false);
            
            //
            // get the material numbers
            //
            ArrayList<String> matlIds = new ArrayList<String>();
            PreparedStatement ps = conn.prepareStatement("select distinct sap_id from material");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                matlIds.add(rs.getString(1));
            
            rs.close();
            ps.close();
            
            //
            // get material home
            //
            Context ctx = getInitialContext();
            ErpMaterialHome matlHome = (ErpMaterialHome) ctx.lookup("freshdirect.erp.Material");
            ctx.close();
            //
            // create prepared statement to update a product
            //
            ps = conn.prepareStatement("update product set default_price=?, default_unit=? where sku_code=" + 
                            "(select distinct prd.SKU_CODE from product prd, materialproxy mpx, material mtl " +
                            "where prd.ID=mpx.PRODUCT_ID and mpx.MAT_ID=mtl.ID and mtl.sap_id=?)");
            //
            // loop through the materials
            //
            Iterator<String> iter = matlIds.iterator();
            //int count = 0;
            while (iter.hasNext()) {
                //if (++count > 25) break;
                //
                // get a material
                //
                String matlId = iter.next();
                ErpMaterialEB erpMatlEB = matlHome.findBySapId(matlId);
                ErpMaterialModel erpMatl = (ErpMaterialModel) erpMatlEB.getModel();
                
                Pricing pr = PricingFactory.getPricing(erpMatl, new ErpCharacteristicValuePriceModel[0] );
                List units = erpMatl.getSalesUnits();
                Collections.sort(units, new SalesUnitComparator());
                ErpSalesUnitModel lowestRatio = (ErpSalesUnitModel) units.get(0);
                System.out.println(erpMatl.getSapId() + " lowest ratio is " + lowestRatio.getNumerator() + " / " + lowestRatio.getDenominator());
                FDConfiguration prConf = new FDConfiguration( 1.0, lowestRatio.getAlternativeUnit() );
                
                try { 

                    MaterialPrice pricingCondition = PricingEngine.getConfiguredPrice( pr, prConf, PricingContext.DEFAULT).getPricingCondition();

                    double defaultPrice = pricingCondition.getPrice();
                    String defaultPriceUnit = pricingCondition.getPricingUnit();
                    
                    System.out.println(erpMatl.getSapId() + " " + defaultPrice + " " + defaultPriceUnit);

                    //
                    // update the product table 
                    //
                    ps.clearParameters();
                    ps.setDouble(1, defaultPrice);
                    ps.setString(2, defaultPriceUnit);
                    ps.setString(3, matlId);
                    ps.executeUpdate();
                } catch (PricingException pe) {
                    pe.printStackTrace();
                }
                
            }
            
            ps.close();
            conn.commit();
            worked = true;
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (EJBException ejbe) {
            ejbe.printStackTrace();
        } catch (FinderException fe) {
            fe.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    if (!worked) {
                        conn.rollback();
                    }
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        
    }
    
    public class SalesUnitComparator implements Comparator {
        
        public int compare(Object o1, Object o2) {
            ErpSalesUnitModel su1 = (ErpSalesUnitModel) o1;
            ErpSalesUnitModel su2 = (ErpSalesUnitModel) o2;
            double ratio1 = su1.getNumerator()/su1.getDenominator();
            double ratio2 = su2.getNumerator()/su2.getDenominator();
            if (ratio1 < ratio2)
                return -1;
            else if (ratio2 < ratio1)
                return 1;
            else
                return 0;
        }
        
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "ERPS_DEV", "ERPS_DEV");
    }
    
    protected Context getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.PROVIDER_URL, "t3://127.0.0.1:7005");
        env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
        return new InitialContext(env);
    }
    
}
