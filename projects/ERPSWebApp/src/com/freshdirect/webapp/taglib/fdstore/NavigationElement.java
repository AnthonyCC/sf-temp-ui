/*
 * $Workfile:NavigationElement.java$
 *
 * $Date:4/15/03 12:51:29 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeI;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.framework.util.NVL;

public abstract class NavigationElement {
	private final ContentNodeModel node;
	private final CategoryModel parentCategory;
	private final int depth;

	private String sortString = null;
        
        protected NavigationElement(int depth, ContentNodeModel node) {
            this(depth,node,false);
        }
	protected NavigationElement(int depth, ContentNodeModel node, boolean isParent) {
		if (node==null) throw new NullPointerException("ContentNode cannot be null");

		this.depth = depth;
		this.node = node;

                if (isParent) {  // for items that are not nodes, we pass the node that is the owner of the item (i.e:Articles)
                    if(node instanceof CategoryModel) {
                        this.parentCategory = (CategoryModel)node; 
                    } else this.parentCategory = null;
                } else {
                    // find closest displayable parent category
                    CategoryModel foundCat = null;
                    ContentNodeI tempNode = node.getParentNode();
                    while (!(tempNode instanceof DepartmentModel)) {
                            CategoryModel cat = (CategoryModel)tempNode;
                            if ( cat.getSideNavShowSelf() ) {
                                    // displayable, we're done
                                    foundCat = cat;
                                    break;
                            }
                            tempNode = tempNode.getParentNode();
                    }
                    this.parentCategory = foundCat;
                }
	}

	public CategoryModel getParentCategory() {
		return this.parentCategory;
	}
	
	public int getDepth() {
		return this.depth;
	}

	public String getDisplayString() {
		String navName = this.node.getNavName();
		return navName!=null ? navName : this.node.getFullName();
	}

	public String getContentName() {
		String contentName = this.node.getContentName();
		return contentName!=null ? contentName : "";
	}


	public String getSortString() {
		if (this.sortString==null) {
                        if (this.isArticle()){
                            this.sortString = constructSortString(this.parentCategory) + ":" + PAD_ZEROS.format( this.getPriority() ) + ":" + this.getArticleSortString();
                        } else {
                            this.sortString = constructSortString(this.parentCategory) + ":" + PAD_ZEROS.format( this.getPriority() ) + ":" + this.getDisplayString();
                        }
		}
		return this.sortString;
	}

	public String toString() {
		return this.getDisplayString();
	}
	
	
	public boolean hasSideNavImage() {
		return (this.node.getAttribute("SIDENAV_IMAGE")!=null);
	}

	public String getAltText(){
		return this.node.getAltText();
	}
	
	public Image getSideNavImage() {
		if (hasSideNavImage()) {
			return (Image) this.node.getAttribute("SIDENAV_IMAGE").getValue();
		}
		return null;
	}

	/**
 	 * Recursive method to create a sort string with all the parent paths
 	 */
	private static String constructSortString(CategoryModel f) {
                if (f==null) return "";
		ContentNodeModel parent = f.getParentNode();
		if (parent==null || parent instanceof DepartmentModel) return "";
		CategoryModel parentCat = (CategoryModel)parent;
                if (!f.getSideNavShowSelf()){
                    return constructSortString(parentCat);
                }
		return constructSortString(parentCat) + ":" + PAD_ZEROS.format(f.getSideNavPriority()) + ":" + NVL.apply(f.getNavName(), f.getFullName());
	}

        public boolean isArticle() {return false;}
        public String getArticleSortString() {return "";}
        
	/*
	protected static final int BREAK_NEVER = 0;
	protected static final int BREAK_ALWAYS_BEFORE = 1;
	protected static final int BREAK_ALWAYS_AFTER = 2;
	protected static final int BREAK_ALWAYS_BOTH = 3;
	protected static final int BREAK_IF_OPEN_BEFORE = 4;
	protected static final int BREAK_IF_OPEN_AFTER = 5;
	protected static final int BREAK_IF_OPEN_BOTH = 6;
	*/
        protected static SimpleDateFormat dateFmtDisplay = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
        protected static SimpleDateFormat dateFmtSort = new SimpleDateFormat("yyyy:MM:dd",Locale.US);

	protected static final NumberFormat PAD_ZEROS = new DecimalFormat("000");

	public abstract int getPriority();
	public abstract String getURL();

	public abstract boolean isAvailable();
	public abstract boolean isProduct();
	public abstract boolean isBold();
	public abstract boolean showLink();
	public abstract boolean breakBefore();
	public abstract boolean breakAfter();

}// end of abstract class
