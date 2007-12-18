
package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumComponentGroupLayout implements Serializable {
    
	public final static EnumComponentGroupLayout HORIZONTAL   = new EnumComponentGroupLayout("Horizontal Style", 0);
	public final static EnumComponentGroupLayout VERTICAL = new EnumComponentGroupLayout("Vertical Style", 1); 
	public final static EnumComponentGroupLayout POPUP_ONLY = new EnumComponentGroupLayout("Popup Only Style", 2); 
	private static List styles = null;
	
	static {
		ArrayList t = new ArrayList();
		t.add(VERTICAL);
		t.add(HORIZONTAL);
		t.add(POPUP_ONLY);
		styles = Collections.unmodifiableList(t);
	}

	private EnumComponentGroupLayout(String n, int i) {
		this.name = n;
		this.id = i;
	}

	public static List getDropDownStyles() {
		return styles;
	}

	public static EnumComponentGroupLayout getDrowDownStyle(int sid) {
	for (int i=0;i < styles.size();i++) {
		EnumComponentGroupLayout ls = (EnumComponentGroupLayout) styles.get(i);
			if (ls.getId() == sid)
				return ls;
		}
		return null;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumComponentGroupLayout) {
			if (((EnumComponentGroupLayout)o).getId() == this.id)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	public int getId(){
			return this.id;
		}

	public String getName(){
			return name;
		}

	public String toString() {
		return "EnumOptionsDropDownStyle:[ Name: "+ this.getName()+ " id: " + this.getId();
	}
	
	private int id;
	private String name;
}
