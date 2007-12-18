/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.orderbot;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.bmproduct.CategoryModel;
import com.freshdirect.bmproduct.ProductModel;
import com.freshdirect.bmproduct.SkuModel;
import com.freshdirect.bmproduct.ejb.ProductTreeHome;
import com.freshdirect.bmproduct.ejb.ProductTreeSB;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;


/**
 * finds sku mismatches between BM and ERPServices
 *
 * @version $Revision$
 * @author $Author$
 */
public class DoctorSku {
    
    protected long oneSecond = 1000;
    protected long oneHour = 60*60*oneSecond;
    protected long oneDay = 24*oneHour;
    
    public static void main(String[] args) {
        DoctorSku robot = new DoctorSku();
        System.out.println("Starting CleanRobot");
        robot.go();
        System.out.println("Finished CleanRobot");
    }
    
    public DoctorSku() {
        super();
    }
    
    CategoryModel productTree = null;
    
    List bmSkus = null;
    List fdSkus = null;
    
    public void go() {
        
        productTree = getProductTree();
        if (productTree == null) {
            System.out.println("couldn't get BM product tree");
            return;
        }
        
        fdSkus = getFDSkus();
        
        findFDSkusNotInBM();
        
        findBMSkusNotInFD();
        
    }
    
    protected void findFDSkusNotInBM() {
        ErpInfoSB infoBean = getErpInfoSB();
        Iterator skuIter = fdSkus.iterator();
        while (skuIter.hasNext()) {
            String skucode = (String) skuIter.next();
            ProductModel p = getBMProduct(skucode);
            if (p == null) {
                if (infoBean == null) {
                    System.out.println(skucode + " not found in BM");
                } else {
                    try {
                        ErpProductInfoModel pInfo = infoBean.findProductBySku(skucode);
                        System.out.println(skucode + " for FD product " + pInfo.getDescription() + " with SAP status of " +
                            ((pInfo.getUnavailabilityReason()!=null)?pInfo.getUnavailabilityReason():"Available") + " not found in BlueMartini");
                    } catch (RemoteException re) {
                        System.out.println(skucode + " not found in BM");
                        re.printStackTrace();
                    } catch (ObjectNotFoundException onfe) {
                        System.out.println(skucode + " not found in BM");
                        onfe.printStackTrace();
                    }
                }
            }
        }
    }
    
    protected void findBMSkusNotInFD() {
        findBMSkusNotInFD(productTree);
    }
    
    protected void findBMSkusNotInFD(CategoryModel category) {
        Iterator prodIter = category.getProducts().iterator();
        while (prodIter.hasNext()) {
            ProductModel product = (ProductModel) prodIter.next();
            Iterator skuIter = product.getSkus().iterator();
            while (skuIter.hasNext()) {
                SkuModel sku = (SkuModel) skuIter.next();
                String skucode = sku.getSkuCode();
                try {
                    FDProduct p = getFDProduct(skucode);
                } catch (FDSkuNotFoundException fdsnfe) {
                    System.out.println(skucode + " for BM product " + product.getDescription() + " not found in ERPServices");
                } catch (FDResourceException fdre) {
                    fdre.printStackTrace();
                }
            }
        }
        Iterator catIter = category.getCategories().iterator();
        while (catIter.hasNext()) {
            CategoryModel nextCat = (CategoryModel) catIter.next();
            findBMSkusNotInFD(nextCat);
        }
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }  
    
    protected FDProduct getFDProduct(String sku) throws FDResourceException, FDSkuNotFoundException {
        FDProductInfo productInfo = FDCachedFactory.getProductInfo(sku);
        return FDCachedFactory.getProduct(sku, productInfo.getVersion());
    }
    
    public ProductModel getBMProduct(String skuCode) {
        return getBMProduct(productTree, skuCode);
    }
    
    public ProductModel getBMProduct(CategoryModel category, String skuCode) {
        if (category.numberOfProducts() > 0) {
            Iterator prodIter = category.getProducts().iterator();
            while (prodIter.hasNext()) {
                ProductModel product = (ProductModel) prodIter.next();
                Iterator skuIter = product.getSkus().iterator();
                while (skuIter.hasNext()) {
                    SkuModel sku = (SkuModel) skuIter.next();
                    if (sku.getSkuCode().equalsIgnoreCase(skuCode)) {
                        return product;
                    }
                }
            }
        }
        Iterator catIter = category.getCategories().iterator();
        while (catIter.hasNext()) {
            ProductModel prod = getBMProduct((CategoryModel) catIter.next(), skuCode);
            if (prod != null)
                return prod;
        }
        return null;
    }
    
    private static final String activeSkuQuery = "select prd.sku_code from product prd " +
        "where prd.version=(select max(version) from product where sku_code=prd.sku_code) " +
        "order by prd.sku_code";
    
    protected List getFDSkus() {
        Connection conn = null;
        List skus = new ArrayList();
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@ems1.nyc1.freshdirect.com:1521:DBEMS01", "ERPS_CMS", "ERPS_CMS");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(activeSkuQuery);
            while (rs.next()) {
                skus.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) { }
            }
        }
        return skus;
    }
    
    
    public CategoryModel getProductTree() {
        
        try {
            Context ctx = ErpServicesProperties.getInitialContext();
            ProductTreeHome ptHome = (ProductTreeHome) ctx.lookup("freshdirect.erp.ProductTree");
            ProductTreeSB ptree = ptHome.create();
            ctx.close();
            return ptree.getProductTree();
            
        } catch (NamingException ne) {
            ne.printStackTrace();
            return null;
        } catch (CreateException ce) {
            ce.printStackTrace();
            return null;
        } catch (RemoteException re) {
            re.printStackTrace();
            return null;
        }
        
    }
    
    
    public ErpInfoSB getErpInfoSB() {
        try {
            Context ctx = ErpServicesProperties.getInitialContext();
            ErpInfoHome home = (ErpInfoHome) ctx.lookup("freshdirect.erp.Info");
            ErpInfoSB bean = home.create();
            ctx.close();
            return bean;
            
        } catch (NamingException ne) {
            ne.printStackTrace();
            return null;
        } catch (CreateException ce) {
            ce.printStackTrace();
            return null;
        } catch (RemoteException re) {
            re.printStackTrace();
            return null;
        }
    }
}
