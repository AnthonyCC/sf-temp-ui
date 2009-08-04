/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import java.text.*;

import com.freshdirect.framework.webapp.*;
import com.freshdirect.webapp.taglib.*;
import com.freshdirect.webapp.taglib.fdstore.*;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.content.meal.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class HolidayMealControllerTag extends AbstractControllerTag {
    
    private static Category LOGGER = LoggerFactory.getInstance( HolidayMealControllerTag.class );
    
    private static SimpleDateFormat startFormatter = new SimpleDateFormat("EEEEEEEE, MMMMMMMM d, yyyy h:mm a");
    //private static SimpleDateFormat endFormatter = new SimpleDateFormat("h:mm a");
    private static NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
    
    private final static double DELIVERY_FEE = 4.95;
    
    private final static String VALENTINE_NAME = "Valentines Fondue";
    
    private final static double ORDER_MINIMUM = 39.99;
    
    private String id;
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    private final static String CURRENT_MEAL = "CURRENT_MEAL";
    
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            boolean requestedMeal = false;
            for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
                String pName = (String) en.nextElement();
                if ("mealId".equalsIgnoreCase(pName)) {
                    requestedMeal = true;
                    break;
                }
            }
            if (requestedMeal && (request.getParameter("mealId") != null)) {
                try {
                    List meals = CallCenterServices.getHolidayMeals(getUser().getIdentity());
                    for (Iterator iter = meals.iterator(); iter.hasNext(); ) {
                        MealModel m = (MealModel) iter.next();
                        if (m.getPK().getId().equals(request.getParameter("mealId"))) {
                            setMeal(m);
                        }
                    }
                } catch (FDResourceException fdre) {
                    LOGGER.debug("Couldn't look up holiday meals", fdre);
                    throw new JspException(fdre.getMessage());
                }
            } else if (!requestedMeal) {
                setMeal(null);
            }
        }
        
        int retVal = super.doStartTag();
        
        //pageContext.setAttribute("appetizers", apps);
        //pageContext.setAttribute("salads", salads);
        //pageContext.setAttribute("entrees", entrees);
        pageContext.setAttribute("fondues", fondues);
        
        pageContext.setAttribute("deliverySlots", deliverySlots);
        pageContext.setAttribute(id, getMeal());
        
        return retVal;
        
    }
    
    protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
        String action = this.getActionName();
        try {
            if ("saveHolidayMeal".equalsIgnoreCase(action)) {
                performSaveHolidayMeal(request, result);
            }
        } catch (FDResourceException ex) {
            LOGGER.error("Error performing action "+action, ex);
            result.addError(new ActionError("technical_difficulty", "Technical problems"));
        }
        
        return true;
    }
    
    private FDSessionUser getUser() {
        HttpSession session = pageContext.getSession();
        return (FDSessionUser) session.getAttribute(SessionName.USER);
    }
    
    
    private CallcenterUser getCallCenterUser() {
        HttpSession session = pageContext.getSession();
        return (CallcenterUser) session.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
    }
    
    
    protected void performSaveHolidayMeal(HttpServletRequest request, ActionResult result) throws FDResourceException {
        processForm(request, result);
        validateForm(result);
        if (result.isSuccess()) {
            MealModel m = CallCenterServices.saveHolidayMeal(getUser().getIdentity(), getCallCenterUser().getId(), getMeal());
            setMeal(m);
        }
    }
    
    
    private MealModel getMeal() {
        HttpSession session = pageContext.getSession();
        MealModel m = (MealModel) session.getAttribute(CURRENT_MEAL);
        if (m == null) {
            m = new MealModel();
            session.setAttribute(CURRENT_MEAL, m);
        }
        return m;
    }
    
    private void setMeal(MealModel m) {
        HttpSession session = pageContext.getSession();
        if (m != null) {
            session.setAttribute(CURRENT_MEAL, m);
        } else {
            session.removeAttribute(CURRENT_MEAL);
        }
    }
    
    private void processForm(HttpServletRequest request, ActionResult result) {
        
        getMeal().setStatus(EnumMealStatus.getType(request.getParameter("status")));
        
        if (request.getParameter("delivery") != null) {
            try {
                getMeal().setDelivery(startFormatter.parse(request.getParameter("delivery")));
            } catch (java.text.ParseException pe) {
                result.addError(new ActionError("technical_difficulty", "Technical problems with delivery date"));
            }
        }
        
        
        //processMenuList(request, apps, "appetizer_");
        
        //processMenuList(request, salads, "salad_");
        
        //processMenuList(request, entrees, "entree_");
        
        //if (getMealSize() > 0) getMeal().setName(SUPERBOWL_NAME);
        //int size =  getMealSize();
        
        processMenuList(request, fondues, "fondue_");
        
        getMeal().setName(VALENTINE_NAME);
        
        /*
        for (Iterator iter = getMeal().getItems(EnumMealItemType.ENTR).iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            if (mi.getQuantity() > 0) {
                getMeal().setName(mi.getName());
                break;
            }
        }
        */
    }
    
    /*
    private int getMealSize() {
        int size = 0;
        for (Iterator iter = getMeal().getItems().iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            size += mi.getQuantity();
        }
        return size;
    }
    
    private void processMenuSelection(HttpServletRequest request, List subMenu, String name) {
        String which = request.getParameter(name);
        int idx = Integer.parseInt(which);
        for (int i=0; i<subMenu.size(); i++) {
            int quantity = 0;
            if (idx == i) quantity = 1;
            MealItemModel menuItem = (MealItemModel) subMenu.get(i);
            boolean found = false;
            for (Iterator iter = getMeal().getItems(menuItem.getType()).iterator(); iter.hasNext(); ) {
                MealItemModel mi = (MealItemModel) iter.next();
                if (mi.getName().equalsIgnoreCase(menuItem.getName())) {
                    mi.setQuantity(quantity);
                    mi.setUnitPrice(menuItem.getUnitPrice());
                    found = true;
                    break;
                }
            }
            if (!found) {
                MealItemModel mi = new MealItemModel();
                mi.setType(menuItem.getType());
                mi.setName(menuItem.getName());
                mi.setQuantity(quantity);
                mi.setUnitPrice(menuItem.getUnitPrice());
                getMeal().addItem(mi);
            }
        }
        
    }
    */
    
    private void processMenuList(HttpServletRequest request, List subMenu, String prefix) {
        
        for (int i=0; i<subMenu.size(); i++) {
            MealItemModel menuItem = (MealItemModel) subMenu.get(i);
            String qString = request.getParameter(prefix + i);
            if (qString != null) qString = qString.trim();
            if ((qString == null) || "".equals(qString)) qString = "0";
            int quantity = 0;
            try {
                quantity = Integer.parseInt(qString.trim());
            } catch (NumberFormatException nfe) { }
            boolean found = false;
            for (Iterator iter = getMeal().getItems(menuItem.getType()).iterator(); iter.hasNext(); ) {
                MealItemModel mi = (MealItemModel) iter.next();
                if (mi.getName().equalsIgnoreCase(menuItem.getName())) {
                    mi.setQuantity(quantity);
                    mi.setUnitPrice(menuItem.getUnitPrice());
                    found = true;
                    break;
                }
            }
            if (!found) {
                MealItemModel mi = new MealItemModel();
                mi.setType(menuItem.getType());
                mi.setName(menuItem.getName());
                mi.setQuantity(quantity);
                mi.setUnitPrice(menuItem.getUnitPrice());
                getMeal().addItem(mi);
            }
        }
        
    }
    
    
    private void validateForm(ActionResult result) {
        
        //
        // must have selected a delivery timeslot
        //
        if (getMeal().getDelivery() == null) {
            result.addError(new ActionError("delivery", "You must select a delivery date."));
        }
        /*
        //
        // must have selected an entree
        //
        int entreeQuant = 0;
        for (Iterator iter = getMeal().getItems(EnumMealItemType.ENTR).iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            entreeQuant += mi.getQuantity();
        }
        if (entreeQuant != 1) {
            result.addError(new ActionError("entree", "You must select an entree."));
        }
        
        //
        // which entree was selected?
        //
        MealItemModel entr = null;
        for (Iterator iter = getMeal().getItems(EnumMealItemType.ENTR).iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            if (mi.getQuantity() == 1) {
                for (Iterator iter2 = entrees.iterator(); iter2.hasNext(); ) {
                    MealItemModel mi2 = (MealItemModel) iter2.next();
                    if (mi2.getName().equalsIgnoreCase(mi.getName())) {
                        entr = mi2;
                    }
                }
            }
        }
        
        //
        // must have selected correct number of sides
        //
        int sideQuant = 0;
        for (Iterator iter = getMeal().getItems(EnumMealItemType.SIDE).iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            sideQuant += mi.getQuantity();
        }
        if (entr != null) {
            int numberOfSides = ((Menu)menuOptions.get(entr)).getNumberOfSides();
            if (sideQuant != numberOfSides) {
                result.addError(new ActionError("sides", "You must select " + numberOfSides + " side dishes."));
            }
        }
        
        //
        // must have selected correct number of desserts
        //
        int dessertQuant = 0;
        for (Iterator iter = getMeal().getItems(EnumMealItemType.DESS).iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            dessertQuant += mi.getQuantity();
        }
        if (entr != null) {
            int numberOfDesserts = ((Menu)menuOptions.get(entr)).getNumberOfDesserts();
            if (dessertQuant != numberOfDesserts) {
                result.addError(new ActionError("desserts", "You must select " + numberOfDesserts + " desserts."));
            }
        }
        */
        //
        // price the order
        //
        MealModel m = getMeal();
        double newPrice = 0.0;
        for (Iterator iter = m.getItems().iterator(); iter.hasNext(); ) {
            MealItemModel mi = (MealItemModel) iter.next();
            newPrice += (mi.getQuantity() * mi.getUnitPrice());
        }
        m.setPrice(newPrice);
        
        //
        // order total must be >= $60 (before adding the delivery fee)
        //
        if (m.getPrice() < ORDER_MINIMUM) {
            //result.addError(new ActionError("entrees", "Order must total at least " + currencyFormatter.format(ORDER_MINIMUM) + ".  Current total is " + currencyFormatter.format(m.getPrice())));
            //result.addError(new ActionError("salads", "Order must total at least " + currencyFormatter.format(ORDER_MINIMUM) + ".  Current total is " + currencyFormatter.format(m.getPrice())));
            //result.addError(new ActionError("appetizers", "Order must total at least " + currencyFormatter.format(ORDER_MINIMUM) + ".  Current total is " + currencyFormatter.format(m.getPrice())));
            result.addError(new ActionError("fondues", "Order must total at least " + currencyFormatter.format(ORDER_MINIMUM) + ".  Current total is " + currencyFormatter.format(m.getPrice())));
        }
        
        //
        // finally add the delivery fee to the price
        //
        m.setPrice(m.getPrice() + DELIVERY_FEE);
        setMeal(m);
        
    }
    
    
    public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] {
                new VariableInfo(
                data.getAttributeString("result"),
                "com.freshdirect.framework.webapp.ActionResult",
                true,
                VariableInfo.NESTED),
                new VariableInfo(
                data.getAttributeString("id"),
                "com.freshdirect.fdstore.content.meal.MealModel",
                true,
                VariableInfo.NESTED),
                //new VariableInfo("entrees",     "java.util.List", true, VariableInfo.NESTED),
                //new VariableInfo("salads",       "java.util.List", true, VariableInfo.NESTED),
                //new VariableInfo("appetizers",  "java.util.List", true, VariableInfo.NESTED),
                new VariableInfo("deliverySlots","java.util.List", true, VariableInfo.NESTED),
                new VariableInfo("fondues","java.util.List", true, VariableInfo.NESTED)
            };
        }
    }
    
    public static class Timeslot {
        
        Date startTime;
        Date endTime;
        Date cutoffTime;
        
        public Timeslot(Date startTime, Date endTime, Date cutoffTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.cutoffTime = cutoffTime;
        }
        
        public Date getStartTime() {
            return this.startTime;
        }
        
        public Date getEndTime() {
            return this.endTime;
        }
        
        public Date getCutoffTime() {
            return this.cutoffTime;
        }
        
    }
    
    private final static ArrayList deliverySlots = new ArrayList();
    static {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar cutoffCal = Calendar.getInstance();
        
        cutoffCal.set(2003, 1, 11, 18, 0, 0);
        
        startCal.set(2003, 1, 13, 16, 0, 0);
          endCal.set(2003, 1, 13, 18, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 13, 18, 0, 0);
          endCal.set(2003, 1, 13, 20, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 13, 20, 0, 0);
          endCal.set(2003, 1, 13, 22, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 13, 22, 0, 0);
          endCal.set(2003, 1, 13, 23, 30, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        
        startCal.set(2003, 1, 14, 16, 0, 0);
          endCal.set(2003, 1, 14, 18, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 14, 18, 0, 0);
          endCal.set(2003, 1, 14, 20, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 14, 20, 0, 0);
          endCal.set(2003, 1, 14, 22, 0, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));
        startCal.set(2003, 1, 14, 22, 0, 0);
          endCal.set(2003, 1, 14, 23, 30, 0);
        deliverySlots.add(new Timeslot(startCal.getTime(), endCal.getTime(), cutoffCal.getTime()));


    }
    /*
    private static class Menu {
        
        int numberOfSides;
        int numberOfDesserts;
        
        public Menu(int numberOfSides, int numberOfDesserts) {
            this.numberOfSides = numberOfSides;
            this.numberOfDesserts = numberOfDesserts;
        }
        
        public int getNumberOfSides() {
            return this.numberOfSides;
        }
        
        public int getNumberOfDesserts() {
            return this.numberOfDesserts;
        }
        
    }
    */
    //private final static HashMap menuOptions = new HashMap();
    /*
    private final static ArrayList entrees = new ArrayList();
    static {
        entrees.add(new MealItemModel(EnumMealItemType.ENTR, "All-American Super Hero (3 feet)", 49.99));
        entrees.add(new MealItemModel(EnumMealItemType.ENTR, "Italian Stallion Super Hero (3 feet)", 52.99));
        entrees.add(new MealItemModel(EnumMealItemType.ENTR, "Lasagna Bolognaise with Tomato Sauce", 35.99));
        entrees.add(new MealItemModel(EnumMealItemType.ENTR, "Southern Fried Chicken, light and dark meat (20 pieces)", 21.99));
        entrees.add(new MealItemModel(EnumMealItemType.ENTR, "Mean Mike's Chili (2 pounds)", 8.99));
    }
    
    private final static ArrayList salads = new ArrayList();
    static {
        salads.add(new MealItemModel(EnumMealItemType.SALD, "Creamy Cole Slaw (1 quart)", 5.99));
        salads.add(new MealItemModel(EnumMealItemType.SALD, "Potato Salad (1 quart)", 5.99));
        salads.add(new MealItemModel(EnumMealItemType.SALD, "Macaroni Salad (1 quart)", 7.99));
    }
    
    private final static ArrayList apps = new ArrayList();
    static {
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Red Hot Buffalo Wings with Blue Cheese Dip, Carrots and Celery (about 50 pieces)", 19.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Barbecue Chicken Wings with Blue Cheese Dip, Carrots and Celery (about 50 pieces)", 19.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Chicken Fingers with Honey Mustard Dipping Sauce (about 60 pieces)", 29.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Mini Lump Crab and Salmon Cakes with Tartar Sauce (24 pieces)", 29.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Stuffed Mushroom Caps (24 pieces)", 14.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Warm Crab Dip (2 pounds)", 17.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Warm Artichoke and Spinach Dip (2 pounds)", 13.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Potato Skins with Cheddar, Bacon and Sour Cream (15 pieces)", 15.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Mini Spring Rolls with Dipping Sauce (18 pieces)", 19.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Fresh Guacamole and Salsa Combo with Tortilla Chips (1 pint of each)", 11.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Jumbo Shrimp Cocktail Platter (36 pieces)", 34.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Kansas City Barbecued Ribs (2 slabs)", 22.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Grilled Thin-Crust Cheese Pizza, Frozen (about 12\")", 5.99));
        apps.add(new MealItemModel(EnumMealItemType.APPE, "Grilled Thin-Crust Meatball Pizza, Frozen (about 12\")", 6.99));
    }
    */
    private final static ArrayList fondues = new ArrayList();
    static {
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Classic Cheese Fondue for Two (includes fondue pot, forks and heating element)", 64.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Classic Cheese Fondue for Two (food only)", 39.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Seafood Fondue for Two, Bouillabaisse Broth (includes fondue pot, forks and heating element)", 84.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Seafood Fondue for Two, Traditional Oil (includes fondue pot, forks and heating element)", 84.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Seafood Fondue for Two, Bouillabaise Broth (food only)", 59.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Seafood Fondue for Two, Traditional Oil (food only)", 59.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Meat Fondue for Two, Beef Broth (includes fondue pot, forks and heating element)", 94.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Meat Fondue for Two, Traditional Oil (includes fondue pot, forks and heating element)", 94.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Meat Fondue for Two, Beef Broth (food only)", 69.99));
        fondues.add(new MealItemModel(EnumMealItemType.FOND, "Meat Fondue for Two, Traditional Oil (food only)", 69.99));
    }
    
    
    
}