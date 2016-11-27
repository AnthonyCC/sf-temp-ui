/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.orderbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;

/**
 * Test case for placing orders
 *
 * @version $Revision$
 * @author $Author$
 */
public class SimBot {

    protected long oneSecond = 1000;
    protected long oneHour = 60*60*oneSecond;
    protected long oneDay = 24*oneHour;

    public static void main(String[] args) {
        SimBot robot = new SimBot();
        System.out.println("Starting FunkyRobot");
        robot.go();
        System.out.println("Finished FunkyRobot");
    }

    public SimBot() {
        super();
    }

    public void go() {

        FDIdentity ident = null;
        try {
            ident = FDCustomerManager.login("meattest@freshdirect.com", "meattest");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String skuSetName = "more_test_skus";
        List skuList = loadSkuList("d:/robotresults/" + skuSetName + ".txt");

        //
        // this is the request date and delivery start time
        //
        Calendar cal = Calendar.getInstance();
        // year, month, day, hour, minute, second
        cal.set(2002, Calendar.JANUARY, 13, 18, 0, 0);
        java.util.Date deliveryDate = cal.getTime();

        //
        // order from a list of skus for the customer
        //
        orderSkusForCustomer(skuList, ident, deliveryDate, skuSetName);

    }

    public void orderSkusForCustomer(List skuList, FDIdentity identity, java.util.Date deliveryDate, String skuSetName) {

        int orderCounter = 0;

        int addressCounter = 0;

        int success = 0;
        int failure = 0;

        try {

            PrintWriter resFile = new PrintWriter(new FileWriter("d:/robotresults/" + skuSetName + "_" + String.valueOf(System.currentTimeMillis()) + ".txt"), true);

            Iterator skuIter = skuList.iterator();
            while (skuIter.hasNext()) {

                orderCounter++;

                try {

                    FDCartModel cart = makeCart(identity, deliveryDate);

                    resFile.println("Starting an order");

                    //
                    // fill up cart with at least 1 order lines, if possible
                    //
                    while ((cart.numberOfOrderLines() < 1) && (skuIter.hasNext())) {

                        String sku = (String) skuIter.next();
                        System.out.println("Adding to cart SKU : " + sku);
                        resFile.println("Adding to cart SKU : " + sku);

                        try {
                            cart.addOrderLines(makeOrderLines(sku).subList(0,1));
                        } catch (FDSkuNotFoundException fdsnfe) {
                            System.out.println("Sku not found: " + sku);
                            resFile.println("Sku not found: " + sku);
                        }

                    }

                    //
                    // write the contents of the the cart to the log
                    //
                    resFile.println("Attempting to place order for cart containing:");
                    int lineCount = 0;
                    Iterator cartIter = cart.getOrderLines().iterator();
                    while (cartIter.hasNext()) {
                        FDCartLineModel cartLine = (FDCartLineModel) cartIter.next();
                        lineCount++;
                        resFile.println("Line " + lineCount*100);
                        resFile.println("\tSKU : " + cartLine.getSkuCode());
                        resFile.println("\tQuantity : " + cartLine.getQuantity());
                        resFile.println("\tSales Unit : " + cartLine.getSalesUnit());
                        resFile.println("\tConfiguration");
                        Map config = cartLine.getConfiguration();
                        if (config != null) {
                            Iterator keyIter = config.keySet().iterator();
                            while (keyIter.hasNext()) {
                                Object key = keyIter.next();
                                Object value = config.get(key);
                                resFile.println("\t\t" + key + " : " + value);
                            }
                        }
                    }

                    FDCustomerManager.placeOrder(identity, cart, EnumTransactionSource.SYSTEM, false);
                    resFile.println("Successfully placed order");
                    success++;

                } catch (FDResourceException fdre) {

                    resFile.println("FDResourceException : " + fdre.getMessage());
                    System.out.println("FDResourceException : " + fdre.getMessage());
                    resFile.println("BAPI call failed");
                    failure++;
                }catch (ErpAuthorizationException aex) {

                    resFile.println("ErpAuthorizationException : " + aex.getMessage());
                    System.out.println("ErpAuthorizationException : " + aex.getMessage());
                    resFile.println("Credit Card Authorization failed");
                    failure++;
                }catch (ErpFraudException aex) {

                    resFile.println("ErpFraudException : " + aex.getMessage());
                    System.out.println("ErpFraudException : " + aex.getMessage());
                    resFile.println("Fraud check rules blocked the order.");
                    failure++;
				}catch (ReservationException ex) {

					resFile.println("ReservationException : " + ex.getMessage());
					System.out.println("ReservationException : " + ex.getMessage());
					resFile.println("ReservationException blocked the order.");
					failure++;
				}

                System.out.println("Success : " + success);
                resFile.println("Success : " + success);
                System.out.println("Failure : " + failure);
                resFile.println("Failure : " + failure);
                System.out.println("Total orders attempted : " + orderCounter);
                resFile.println("Total orders attempted : " + orderCounter);

                resFile.println("\n\n");

            }

            resFile.println("\n\n");
            resFile.println("Success : " + success);
            resFile.println("Failure : " + failure);
            resFile.println("Total orders attempted : " + orderCounter);

            resFile.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    protected FDCartModel makeCart(FDIdentity ident, java.util.Date requestDate) throws FDResourceException {
        // requestDate is date and time of delivery start

        FDCartModel cart = new FDCartModel();

        // set delivery stuff
        cart.setDeliveryStartTime(requestDate);
        cart.setDeliveryEndTime(new java.util.Date(requestDate.getTime() + (2*oneHour) - oneSecond));

        DlvZoneInfoModel zoneInfo = new DlvZoneInfoModel();
        zoneInfo.setZoneCode("003");
        zoneInfo.setDeliveryCharges(3.99);
        cart.setZoneInfo(zoneInfo);

        Collection addrs = FDCustomerManager.getShipToAddresses(ident);
        cart.setDeliveryAddress((ErpAddressModel) addrs.iterator().next());

        Collection pays = FDCustomerManager.getPaymentMethods(ident);
        cart.setPaymentMethod((ErpPaymentMethodI) pays.iterator().next());

        return cart;

    }


    public List loadSkuList(String filename) {

        ArrayList skus = new ArrayList();

        try {
            BufferedReader lines = new BufferedReader(new FileReader(filename));
            String line = null;
            while (null != (line = lines.readLine())) {
                if ("".equals(line.trim()) || line.startsWith("#"))
                    continue;
                skus.add(line);
            }
            //Collections.shuffle(skus);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            return skus;
        }
    }

    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }



    protected void addToCart(FDCartModel cart, List lines) {

        Iterator iter = lines.iterator();
        while (iter.hasNext()) {
            FDCartLineModel cartLine = (FDCartLineModel) iter.next();
            cart.addOrderLine(cartLine);
        }

    }

    protected List makeOrderLines(String skuCode) throws FDResourceException, FDSkuNotFoundException {

        ArrayList lines = new ArrayList();
        FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
        FDProduct product = FDCachedFactory.getProduct(productInfo);

        // creates the minimum number of configured products to exercise
        // all of the options of all variations and all of the sales units
        // how many to make?
        // find the maximum of the # of sales units and the number of options in each varaition
        int max = product.getSalesUnits().length;
        FDVariation[] variations = product.getVariations();
        for (int i=0; i<variations.length; i++) {
            FDVariation variation = variations[i];
            max = Math.max(max, variation.getVariationOptions().length);
        }

        for (int n=0; n<max; n++) {
            // pick a sales unit
            FDSalesUnit[] units = product.getSalesUnits();
            FDSalesUnit salesUnit = units[n % units.length];
            String confDescr = "";
            // make a variation map
            HashMap optionMap = new HashMap();
            for (int i=0; i<variations.length; i++) {
                FDVariation variation = variations[i];
                FDVariationOption[] options = variation.getVariationOptions();
                FDVariationOption option = options[n % options.length];
                optionMap.put(variation.getName(), option.getName());
                if (!option.getDescription().equalsIgnoreCase("None")) {
                    if ((i != 0) && (confDescr.length() > 0)) confDescr += ", ";
                    confDescr += option.getDescription();
                }
            }
            FDCartLineModel cartLine = new FDCartLineModel(productInfo.getSkuCode(), productInfo.getVersion());

            //
            // pick a random quantity between 5 and 10, except for...
            //
            int quantity = 5 + (int) (5.0 * Math.random());
            if (skuCode.startsWith("MEA")) {
                //
                // MEAT
                // quantity 1 -> 3 for meat
                //
                quantity = 1 + (int) (3.0 * Math.random());
            }
            else if (skuCode.startsWith("TEA") || skuCode.startsWith("COF")) {
                //
                // COFFEE & TEA
                // quantity 1
                //
                quantity = 1;
            } else if (skuCode.startsWith("SEA")) {
                //
                // SEAFOOD
                // quantity 1 -> 5
                //
                quantity = 1 + (int) (5.0 * Math.random());
            } else if (skuCode.startsWith("DEL")) {
                //
                // DELI
                // quantity 1
                //
                quantity = 1;
            } else if (skuCode.startsWith("FRU") || skuCode.startsWith("YEL")) {
                //
                // FRUIT
                // quantity 3
                //
                quantity = 3;
            } else if (skuCode.startsWith("VEG")) {
                //
                // VEGGIES
                // quantity 1 -> 5
                //
                quantity = 1 + (int) (5.0 * Math.random());
            } else if (skuCode.startsWith("CHE")) {
                //
                // CHEESE
                // quantity 1
                //
                quantity = 1;
            }
            cartLine.setQuantity(quantity);
            cartLine.setSalesUnit(product.getSalesUnits()[0].getName());
            cartLine.setConfiguration(optionMap);
            cartLine.setConfigurationDesc(confDescr);
            cartLine.setDepartmentDesc("MEAT");
            cartLine.setDescription("Robot "+skuCode);

            if (skuCode.startsWith("DEL") || skuCode.startsWith("CHE")) {
                //
                // only order line per sku for deli and cheese
                //
                if (lines.size() > 0) continue;
            }

            lines.add(cartLine);
        }

        return lines;

    }

}
