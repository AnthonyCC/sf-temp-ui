/* Generated by Together */

package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Layout types marked as deprecated, which are not used in production, in March 29, 2011. 
 *
 */
public enum EnumLayoutType implements Serializable {
    
    GENERIC              ("Generic", 0),             //(CATEGORY AND DEPARTMENT LAYOUT)
    HORIZONTAL           ("Horizontal", 1),          //(CATEGORY AND DEPARTMENT LAYOUT)
    VERTICAL             ("Vertical", 2),            //(CATEGORY AND DEPARTMENT LAYOUT)
    FEATURED_ALL         ("Featured All", 3),        //SEAFOOD, CHEESE AND MEAT CATEGORIES
    FEATURED_CATEGORY    ("Featured Category", 4),   //MEAT CATEGORIES
    COFFEE_BY_REGION     ("Coffee By Region", 5),    //COFFEE REGION AND ROAST
    PRODUCT_SORT         ("Product Sort", 6),        //TEA CATEGORIES, COFFEE CATEGORIES
    PRODUCT_FOLDER_LIST  ("Product Folder List", 7), //BULK BEEF
    COFFEE_DEPARTMENT    ("Coffee Department", 8),   //( DEPARTMENT LAYOUT)
    GROCERY_DEPARTMENT   ("Grocery Department", 9),  //( Grocery/Frozen/Dair/Specialty DEPARTMENT LAYOUT)
    GROCERY_CATEGORY     ("Grocery Category", 10),   //(Grocery/Frozen/Dair/Specialty cATEGORY page)
    GROCERY_PRODUCT      ("Grocery Product", 11),    //( Grocery/Frozen/Dair/Specialty Product page)
    BULK_MEAT_PRODUCT    ("Bulk Meat Product", 12),    //( Bulk meat category/Product page)
    HOW_TO_COOK_IT	 ("How To Cook It", 13),    //( How to Cook It layout)
    @Deprecated // not used
    MEALS_STYLE	    	 ("Meals Style", 14),    //( Meals department layout)  
    MULTI_CATEGORY	 ("Multi Category Layout", 15),    //( Multi Category Layout)
    TRANSAC_MULTI_CATEGORY	       ("Transactional Multi Category", 16),    //( Transactional Multi Category)
    @Deprecated // not used
    MULTI_CATEGORY_PROD_REDIRECT       ("Multi Category Product Redirect", 17),    //( Transactional Multi Category)
    TRANSAC_GROUPED_ITEMS	       ("Transactional Grouped Items", 18),    //( Transactional grouped Items)
    HOLIDAY_MENU	               ("Holiday Menu", 19),    //( menu layout)
    THANKSGIVING_CATEGORY              ("Thanksgiving layout", 20),    //( Composite transaction 1)
    HOLIDAY_MEAL_BUNDLE_CATEGORY      ("Holiday meal bundle layout", 21),
    @Deprecated  // used by hidden categories
    GENERIC_WITH_PAGING                ("Generic with Paging",22),
    @Deprecated // used by hidden categories
    VALENTINES_CATEGORY                ("Valentine's Category",23),
    PARTY_PLATTER_CATEGORY             ("Party Platter Category",24),
    PICKS_PROMO                        ("Our Picks",25), //Our picks layout
    @Deprecated // not used
    VERTICAL_LABELED_CATEGORY          ("Vertical Labeled Category", 26), //(DEPARTMENT LAYOUT, Local Foods) 
    @Deprecated // used by hidden categories
    FEATURED_MENU                      ("Featured Menu", 27), //(CATEGORY LAYOUT, Easy Meals)
    @Deprecated // not used
    TOP_TEN                            ("Top Ten", 28), //(Top Ten Picks)

    MEAT_DEPT                          ("Meat Dept", 29), //(Meat Dept Redesign)
    @Deprecated // not used
    MEAT_DEALS                         ("Meat Deals", 30), //(Best Meat Deals and EDLP)
    MEAT_CATEGORY                      ("Meat Category", 31), //(Meat Categories to display Deals and EDLP)
	
    MEDIA_INCLUDE                      ("Media Include", 97), //[APPREQ-77]
	
    WINE_CATEGORY                      ("Wine Category", 100), // Wine Layout
    TRANSAC_MULTI_PAIRED_ITEMS	       ("Transactional Multi Paired Items", 101), // Wine Layout
    TEMPLATE_LAYOUT	               ("Template Layout", 102), // Wine Layout
	
    // this is never rendered by layout manager
    @Deprecated // used by hidden categories
    MULTI_ITEM_MEAL_OPTION_HORZ        ("Multi Item Meal Layout Option 1", 98), //(Optional category layout 1, Salad Meal)
    @Deprecated // not used
    MULTI_ITEM_MEAL_OPTION_VERT        ("Multi Item Meal Layout Option 2", 99), //(Optional category layout 2, Salad Meal)  --- NOT USED
    
    // 4mm specific layouts    
    FOURMM_DEPARTMENT	("4minute-meals landing page", 40),
    FOURMM_CATEGORY		("4minute-meals restaurant page",41),

    // Bakery department
    BAKERY_DEPARTMENT ("Bakery department", 42),

    // Wine dept specific layouts
    WINE_DEALS							("Wine Deals", 103),
    WINE_EXPERTS_FAVS					("Wine Expert's Favs", 104),

    // Multi Category Layout variants
    MULTI_CATEGORY_QUICKBUY				("Multi Category Layout (with Quickbuy)", 111),


    //promo page layouts
    PRESIDENTS_PICKS					("President's Picks", 200),
    E_COUPONS					("E-Coupons", 201),
    PRODUCTS_ASSORTMENTS					("Products Assortments", 202);
    
    public static List<EnumLayoutType> getLayoutTypes() {
        return Arrays.asList(values());
    }

    public static EnumLayoutType getLayoutType(int lid) {
        for (EnumLayoutType ls : values()) {
            if (ls.getId() == lid) {
                return ls;
            }
        }
        return null;
    }

    private EnumLayoutType(String n, int i) {
        this.name = n;
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int id;
    private String name;

    /**
     * @return Returns true if layout is a kind of grocery
     */
    public boolean isGroceryLayout() {
        return (GROCERY_DEPARTMENT.equals(this) || GROCERY_CATEGORY.equals(this) || GROCERY_PRODUCT.equals(this));
    }
    
    /**
     * @return Returns true if layout is four minute meals related.
     */
    public boolean is4mmLayout() {
    	return ( FOURMM_DEPARTMENT.equals( this ) || FOURMM_CATEGORY.equals( this ) );
    }
}
