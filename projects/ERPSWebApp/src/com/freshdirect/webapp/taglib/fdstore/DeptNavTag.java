/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.webapp.taglib.*;
import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.attributes.*;
import com.freshdirect.fdstore.FDResourceException;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DeptNavTag extends AbstractGetterTag {

	private String categoryId = null;
	private String categoryList = null;

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryList(String categoryList) {
		this.categoryList = categoryList;
	}

	protected Object getResult() throws FDResourceException {
		// find the department based on category

		// find the relevant category
		CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNodeByName( this.categoryId );
		if (category==null) {
			throw new FDResourceException("Category "+this.categoryId+" not found");
		}
		DepartmentModel dept = category.getDepartment();

		Attribute deptNavAttrib = dept.getAttribute("DEPT_NAV");

		List catList = null;			
		if (deptNavAttrib==null) {
			// !!! filter off stuff based on hide flags?
			catList = dept.getCategories();
		} else {
			List refList = (List)deptNavAttrib.getValue();
			catList = new ArrayList( refList.size() );
			for (Iterator i=refList.iterator(); i.hasNext(); ) {
									ContentRef ref = (ContentRef)i.next();
									if (!(ref instanceof CategoryRef)) continue;
				CategoryModel cat = ((CategoryRef)ref).getCategory();
				if (cat!=null) {
					catList.add(cat);
				}
			}
		}
		
		pageContext.setAttribute(this.categoryList, catList);

		return dept;
	}

	public static class TagEI extends TagExtraInfo {
	
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
								 "com.freshdirect.fdstore.content.DepartmentModel",
								 true,
								 VariableInfo.NESTED),
				new VariableInfo(data.getAttributeString("categoryList"),
								 "java.util.List",
								 true,
								 VariableInfo.NESTED)
			};
		}
	}

}