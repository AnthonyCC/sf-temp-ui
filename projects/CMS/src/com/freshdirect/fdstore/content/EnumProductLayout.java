
package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumProductLayout implements Serializable {
    
	public final static EnumProductLayout PERISHABLE  = new EnumProductLayout("Perishable Product Layout", 1,"/includes/product/perishable_product.jsp",false);         //(default Freshdirect Templtes)
	public final static EnumProductLayout COMPOSITE     = new EnumProductLayout("Composite Product Layout", 2,"/includes/product/transac_composite.jsp",false);         //(Composite Template)
	public final static EnumProductLayout WINE     = new EnumProductLayout("Wine Product Layout", 3,"/includes/product/wine_product.jsp",false);                        //(Wine Template)
	public final static EnumProductLayout COMPOSITE_PLUS  = new EnumProductLayout("Compoiste-Plus Product Layout", 4,"/includes/product/transac_composite_plus.jsp",true);   //(Valentine Composite Template)
	public final static EnumProductLayout PARTY_PLATTER  = new EnumProductLayout("Party Platter Product Layout", 5,"/includes/product/party_platter.jsp",true);  //(Party Platter Template)
	public final static EnumProductLayout MULTI_ITEM_MEAL  = new EnumProductLayout("Multi Item Meal Option Layout", 6,"/includes/product/party_platter.jsp",true);  //(Multi Item Meal Option Template)
	public final static EnumProductLayout COMPONENTGROUP_MEAL  = new EnumProductLayout("Meal Layout using Component groups", 7,"/includes/product/componentGroupMeal.jsp",true);  //(Component group meal template)
	public final static EnumProductLayout CONFIGURED_PRODUCT =   new EnumProductLayout("Configured Product Layout",8,"/includes/product/configProduct.jsp",false);
	private static List types = null;
	
	static {
		ArrayList t = new ArrayList();
		t.add(PERISHABLE);
		t.add(COMPOSITE);
		t.add(WINE);
		t.add(COMPOSITE_PLUS);
		t.add(PARTY_PLATTER);
		t.add(MULTI_ITEM_MEAL);
		t.add(COMPONENTGROUP_MEAL);
		t.add(CONFIGURED_PRODUCT);
		
		types = Collections.unmodifiableList(t);
	}

	private EnumProductLayout(String n, int i, String layoutPath,boolean addMultipleToCart) {
		this.name = n;
		this.id = i;
		this.layoutPath = layoutPath;
		this.canAddMultipleToCart = addMultipleToCart;
	}

	public static List getLayoutTypes() {
		return types;
	}

	public static EnumProductLayout getLayoutType(int lid) {
	for (int i=0;i < types.size();i++) {
		EnumProductLayout ls = (EnumProductLayout) types.get(i);
			if (ls.getId() == lid)
				return ls;
		}
		return null;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumProductLayout) {
			return ((EnumProductLayout)o).getId() == this.id;
		}
		return false;
	}


	public int getId(){
			return id;
		}

	public String getName(){
			return name;
		}

	public String getLayoutPath() {
		return this.layoutPath;
	}
	
	public boolean canAddMultipleToCart() {
		return this.canAddMultipleToCart;
	}
	public String toString() {
		return "EnumProductLayout:[ Name: "+ this.getName()+ " id: " + this.getId()+" Path:"+getLayoutPath();
	}
	
	private int id;
	private String name;
	private String layoutPath;
	private boolean canAddMultipleToCart;
}
