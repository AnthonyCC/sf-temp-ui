/* Generated by Together */

package com.freshdirect.storeapi.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumTemplateType implements Serializable {
    
    public final static EnumTemplateType GENERIC  = new EnumTemplateType("Generic Template", 1);             //(default Freshdirect Templtes)
    public final static EnumTemplateType WINE     = new EnumTemplateType("Wine Template", 2);          //(Wine Template)

    private static List types = null;

    static {
        ArrayList t = new ArrayList();
        t.add(GENERIC);
        t.add(WINE);
        types = Collections.unmodifiableList(t);
    }

	private EnumTemplateType(String n, int i) {
		this.name = n;
		this.id = i;
	}

    public static List getTemplateTypes() {
        return types;
    }

    public static EnumTemplateType getTemplateType(int lid) {
	for (int i=0;i < types.size();i++) {
		EnumTemplateType ls = (EnumTemplateType) types.get(i);
            if (ls.getId() == lid)
                return ls;
        }
        return null;
    }

    public boolean equals(Object o) {
        if (o instanceof EnumTemplateType) {
			if (((EnumTemplateType)o).getId() == this.id)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }


    public int getId(){
            return id;
        }

    public String getName(){
            return name;
        }

    private int id;
    private String name;
}
