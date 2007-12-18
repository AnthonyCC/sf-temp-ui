/*
 * DepartmentRef.java
 *
 * Created on March 19, 2002, 10:47 AM
 */

package com.freshdirect.fdstore.content;
/**
 *
 * @author  rgayle
 * @version 
 */
public class DepartmentRef extends ContentRef{

    /** Creates new DepartmentRef */
    public DepartmentRef(String refName)  throws NullPointerException {
            super(ContentNodeI.TYPE_DEPARTMENT);
            if (refName==null) throw new NullPointerException("Department Id cannot be null");
            this.refName = refName;
    }

    public String getDepartmentName() {
            return this.refName;
    }
    
    public DepartmentModel getDepartment() {
        return (DepartmentModel)ContentFactory.getInstance().getContentNodeByName(this.refName);
    }

}
