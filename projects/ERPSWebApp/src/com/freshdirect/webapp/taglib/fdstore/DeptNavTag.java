package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class DeptNavTag extends AbstractGetterTag<DepartmentModel> {

	private static final long	serialVersionUID	= -5456656217906642923L;

	Logger LOGGER = LoggerFactory.getInstance(DeptNavTag.class);
	
	private String	categoryId		= null;
	private String	deptId			= null;
	private String	categoryList	= null;

	public void setCategoryId( String categoryId ) {
		this.categoryId = categoryId;
	}
	public void setDeptId( String deptId ) {
		this.deptId = deptId;
	}
	public void setCategoryList( String categoryList ) {
		this.categoryList = categoryList;
	}

	protected DepartmentModel getResult() throws FDResourceException {
		// find the department based on category
		DepartmentModel dept = null;
		// find the relevant category
		if ( this.categoryId != null ) {
			CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNode( this.categoryId );
			if ( category == null ) {
				throw new FDResourceException( "Category " + this.categoryId + " not found" );
			}
			dept = category.getDepartment();
			
		} else if ( this.deptId != null ) {
			dept = (DepartmentModel)ContentFactory.getInstance().getContentNode( this.deptId );
		}

		List<CategoryModel> deptNavAttrib = dept == null ? null : dept.getDeptNav();

		List<CategoryModel> catList = null;
		if (deptNavAttrib == null || deptNavAttrib.size() == 0) {
			// !!! filter off stuff based on hide flags?
			catList = (dept == null) ? new ArrayList<CategoryModel>() : dept.getCategories();
		} else {
			catList = new ArrayList<CategoryModel>( deptNavAttrib.size() );
			catList.addAll(deptNavAttrib);
		}

		pageContext.setAttribute( this.categoryList, catList );

		return dept;
	}

	public static class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo( TagData data ) {
			return new VariableInfo[] {
					new VariableInfo( data.getAttributeString( "id" ),
									"com.freshdirect.storeapi.content.DepartmentModel", 
									true, 
									VariableInfo.NESTED ),
					new VariableInfo( data.getAttributeString( "categoryList" ), 
									"java.util.List<com.freshdirect.storeapi.content.CategoryModel>", 
									true,
									VariableInfo.NESTED ) 
				};
		}
	}
}
