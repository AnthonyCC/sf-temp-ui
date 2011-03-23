package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class LayoutManager extends BodyTagSupport {
	
	private static final long	serialVersionUID	= -4387696427307262039L;
	
	//tag inputs
	private String resultName = null;
	private boolean isDepartment = false;
	private boolean isCategory = false;
	private ContentNodeModel currentNode = null;
	private String layoutSettingsName = null;
	
	//Work variables
	private int layoutType = -1;
	
	public void setIsDepartment(boolean isDept) {
		if (!isCategory) {
			this.isDepartment = isDept;
		}
	}

	public void setIsCategory(boolean isCategory) {
		if (!isDepartment) {
			this.isCategory = isCategory;
		}
	}

	public void setCurrentNode(ContentNodeModel currentNode) {
		this.currentNode = currentNode;
	}

	public void setResult(String resultName) {
		this.resultName = resultName;
	}

	public void setLayoutSettingsName(String layoutSettingsName) {
		this.layoutSettingsName = layoutSettingsName;
	}

	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionResult result = new ActionResult();
		result.addError((currentNode == null), "currentNode", "The currentNode parameter was not specified.");
		result.addError((!isDepartment && !isCategory), "nodeType", "The content node type was not specified ");
		if (!result.isFailure()) {
		    EnumLayoutType layout = ContentNodeModelUtil.getLayout(currentNode, null); 
		    layoutType = layout != null ? layout.getId() : -1;
		    
			Settings lms = setupLayout(request, session, result);
			pageContext.setAttribute(resultName, result);
			pageContext.setAttribute(layoutSettingsName, lms);
			return EVAL_BODY_BUFFERED;
		} else {
			pageContext.setAttribute(resultName, result);
			return EVAL_BODY_BUFFERED;
		}
	}

	private Settings setupLayout(HttpServletRequest request, HttpSession session, ActionResult result) {

		String sortBy = request.getParameter("sortBy");
		if (sortBy == null)
			sortBy = "name";

		String sortNameAttrib = currentNode instanceof ProductContainer ? ((ProductContainer)currentNode).getListAs("full") : "full";
		if (!sortNameAttrib.equalsIgnoreCase(SortStrategyElement.SORTNAME_GLANCE) && !sortNameAttrib.equalsIgnoreCase(SortStrategyElement.SORTNAME_NAV))
			sortNameAttrib = SortStrategyElement.SORTNAME_FULL;

		if (request.getParameter("groceryVirtual") != null) {
			//groceryVirtual = true;
			//alwasy force the layout to the GrocerProduct if the groceryVitrual is found in the queryStrng
			layoutType = EnumLayoutType.GROCERY_PRODUCT.getId();
		}
		
		boolean sortDescending = "true".equalsIgnoreCase(request.getParameter("sortDescending")) ? true : false;

		/* for testing only..override incoming layout with specified layout
		 if (layoutType== EnumLayoutType.FEATURED_ALL.getId()){
			layoutType = EnumLayoutType.HOLIDAY_MENU.getId();
		}*/
		//layoutType = EnumLayoutType.TOP_TEN.getId();
		//**** end of test block ****/
		
		Settings s = new Settings();

		// Default value for departments
		if (isDepartment) {
			s.setGrabberDepth(0);
		}

		if (layoutType == EnumLayoutType.GENERIC.getId()) {
			s.setLayoutFileName("/includes/layouts/generic_layout.jsp");
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.GENERIC_WITH_PAGING.getId()) {
			s.setLayoutFileName("/includes/layouts/generic_paging.jsp");
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.HORIZONTAL.getId()) {
			s.setLayoutFileName("/includes/layouts/horizontal_pattern.jsp");
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE,sortDescending));
		} else if (layoutType == EnumLayoutType.FEATURED_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/featured_category.jsp");
			s.setGrabberDepth(isDepartment?0:1);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.VERTICAL.getId()) {
			s.setLayoutFileName("/includes/layouts/vertical_layout.jsp");
			s.setGrabberDepth(1);
			s.setIgnoreShowChildren(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.FEATURED_ALL.getId()) {
			s.setLayoutFileName("/includes/layouts/featured_all.jsp");
			s.setIgnoreDuplicateProducts(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE,false));
		} else if (layoutType == EnumLayoutType.PRODUCT_SORT.getId()) {
			s.setLayoutFileName("/includes/layouts/product_sort.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.COFFEE_DEPARTMENT.getId()) {
			s.setLayoutFileName("/includes/layouts/coffee_dept_layout.jsp");
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.COFFEE_BY_REGION.getId()) {
			s.setLayoutFileName("/includes/layouts/coffee_by_region.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.GROCERY_DEPARTMENT.getId()) {
			s.setLayoutFileName("/includes/layouts/grocery_dept_layout_new.jsp");
			s.setGrabberDepth(1);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.GROCERY_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/grocery_category_layout.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(true);
			s.setIgnoreDuplicateProducts(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_NAME, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.GROCERY_PRODUCT.getId()) {
			s.setLayoutFileName("/includes/layouts/grocery_product.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(false);
			s.setIncludeUnavailable(false);
			s.setReturnSkus(true);
			if ("nutrition".equalsIgnoreCase(sortBy)) {
				String nutritionName = request.getParameter("nutritionName");
				
				if(nutritionName.equals(ErpNutritionType.SERVING_SIZE))
					nutritionName = ErpNutritionType.SERVING_WEIGHT;
				
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
				s.addSortStrategyElement(
					new SortStrategyElement(
						SortStrategyElement.PRODUCTS_BY_NUTRITION,
						ErpNutritionType.getType(nutritionName).getDisplayName(),
						sortDescending));
				s.addSortStrategyElement(
					new SortStrategyElement(
						SortStrategyElement.PRODUCTS_BY_NUTRITION,
						ErpNutritionType.getType(ErpNutritionType.SERVING_WEIGHT).getDisplayName(),
						sortDescending));
				s.addSortStrategyElement(
					new SortStrategyElement(
						SortStrategyElement.PRODUCTS_BY_NUTRITION,
						ErpNutritionType.getType(ErpNutritionType.TOTAL_CALORIES).getDisplayName(),
						sortDescending));
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortDescending));
			} else if ("kosher".equalsIgnoreCase(sortBy)) {
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_KOSHER, false));
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortDescending));
			} else if ("price".equalsIgnoreCase(sortBy)) {
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE, sortDescending));
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortDescending));
			} else if ("name".equalsIgnoreCase(sortBy)) {
				s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortDescending));
			}
		} else if (layoutType == EnumLayoutType.PRODUCT_FOLDER_LIST.getId()) {
			s.setLayoutFileName("/includes/layouts/product_folder_list.jsp");
			s.setIgnoreShowChildren(false);
			s.setReturnHiddenFolders(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			//s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_NAME, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.BULK_MEAT_PRODUCT.getId()) {
			s.setLayoutFileName("/includes/layouts/bulk_meat_product.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(false);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_NAME, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.HOW_TO_COOK_IT.getId()) {
			s.setLayoutFileName("/includes/layouts/how_to_cookit.jsp");
			s.setIgnoreShowChildren(true);
			s.setReturnHiddenFolders(true);
			s.setGrabberDepth(0);
			s.setReturnHiddenFolders(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.MEALS_STYLE.getId()) {
			s.setLayoutFileName("/includes/layouts/meals_style.jsp");
			s.setIgnoreShowChildren(false);
			s.setReturnHiddenFolders(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.MULTI_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/multi_category_layout.jsp");
			s.setIgnoreShowChildren(false);
			s.setReturnHiddenFolders(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.MULTI_CATEGORY_PROD_REDIRECT.getId()) {
			s.setLayoutFileName("/includes/layouts/multi_category_redir_layout.jsp");
			s.setIgnoreShowChildren(false);
			s.setReturnHiddenFolders(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/transac_multi_category.jsp");
			s.setIgnoreShowChildren(false);
			s.setReturnHiddenFolders(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()) {
			s.setLayoutFileName("/includes/layouts/transac_grouped_items.jsp");
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.HOLIDAY_MENU.getId()) {
			s.setLayoutFileName("/includes/layouts/holiday_menu.jsp");
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.THANKSGIVING_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/thanksgiving_category.jsp");
			s.setIgnoreShowChildren(false);
			s.setIncludeUnavailable(false);
			s.setReturnHiddenFolders(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.VALENTINES_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/holiday_category.jsp");
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			s.setReturnHiddenFolders(false);
			s.setIgnoreDuplicateProducts(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/party_platter_cat.jsp");
			s.setGrabberDepth(1);
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(true);
			s.setReturnHiddenFolders(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.PICKS_PROMO.getId()) {
			s.setLayoutFileName("/includes/layouts/picks_promo.jsp");
		} else if (layoutType == EnumLayoutType.VERTICAL_LABELED_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/vertical_layout.jsp");
			s.setGrabberDepth(1);
			s.setReturnHiddenFolders(true);
			s.setIgnoreShowChildren(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.FEATURED_MENU.getId()) {
			s.setLayoutFileName("/includes/layouts/featured_menu.jsp");
			s.setIgnoreShowChildren(true);
			s.setIgnoreDuplicateProducts(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));	
		} else if (layoutType == EnumLayoutType.TOP_TEN.getId()) {
			s.setLayoutFileName("/includes/layouts/top_ten.jsp");
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			s.setIgnoreDuplicateProducts(true);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));	
		} else if (layoutType == EnumLayoutType.MEDIA_INCLUDE.getId()) {
			// [APPREQ-77]
			s.setLayoutFileName("/includes/layouts/media_no_nav.jsp");
			// TODO: what more I need to do here?
		} else if (layoutType == EnumLayoutType.MEAT_DEPT.getId()) {
			// Meat Dept Redesign new layout
			s.setLayoutFileName("/includes/layouts/meat_dept.jsp");
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.MEAT_DEALS.getId()) {
			// Best Meat Deals sub-category new layout
			//s.setLayoutFileName("/includes/layouts/meat_deals.jsp");
			s.setLayoutFileName("/includes/multi_cat_dept.jsp");
			//s.setIgnoreShowChildren(true);
			//s.setIgnoreDuplicateProducts(true);
			s.setGrabberDepth(1);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.MEAT_CATEGORY.getId()) {
			// Meat sub-category new layout
			s.setLayoutFileName("/includes/layouts/meat_categories.jsp");
			//s.setIgnoreShowChildren(true);
			//s.setIgnoreDuplicateProducts(true);
			s.setGrabberDepth(1);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
			
		} else if (layoutType == EnumLayoutType.FOURMM_DEPARTMENT.getId()) {
			s.setLayoutFileName("/includes/layouts/4mm/landing_page_layout.jsp");
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE,sortDescending));
			s.setReturnHiddenFolders(false);
			s.setGrabberDepth(0);
			
		} else if (layoutType == EnumLayoutType.FOURMM_CATEGORY.getId()) {
			s.setLayoutFileName("/includes/layouts/4mm/restaurant_page_layout.jsp");
			s.setGrabberDepth(-1); // we don't need itemgrabber here, 4mm helper class has everything cached
			
		} else if (layoutType == EnumLayoutType.BAKERY_DEPARTMENT.getId()) {
			// TODO Bakery layout params
	        s.setLayoutFileName("/includes/layouts/bakerydpt.jsp");
	        s.addSortStrategyElement( new SortStrategyElement(SortStrategyElement.NO_SORT) );
	        s.setIgnoreShowChildren( true );
	        s.setGrabberDepth( 1 );	// depth of 1 means: two levels deep
	        
		} else {
			// default to the generic layout using the default settings for the ItemGrabber
			s.setLayoutFileName("/includes/layouts/generic_layout.jsp");
		}

		//*** we some categories/departments may need to alter the layout settings to itemgrabber
		//vegEtables  DEPARTMENT.
		if ((currentNode.getContentName().toUpperCase().indexOf("VEG") >= 0
		     || currentNode.getContentName().toUpperCase().indexOf("SEA") >= 0)
			 && isDepartment) {
			s.setReturnHiddenFolders(false);
			s.setIgnoreShowChildren(false);
			s.setFilterDiscontinued(false);
			s.setGrabberDepth(99);
		}

		//Kitchen (meals)/OUR_PICKS,  DEPARTMENT.
		if (("OUR_PICKS".indexOf(currentNode.getContentName().toUpperCase()) >= 0) && isDepartment) {
			s.setGrabberDepth(99);
		}
        // new wine store layouts
		if (layoutType == EnumLayoutType.WINE_CATEGORY.getId()) {
			if (currentNode instanceof CategoryModel && ContentFactory.getInstance().getDomainValueForWineCategory((CategoryModel) currentNode) != null) {
				s.setGrabberDepth(-1);
			}
			s.setFilterUnavailable(true);
			// [APPREQ-77]
			s.setLayoutFileName("/includes/layouts/wine_cat_details.jsp");
			//s.setGrabberDepth(1);
			s.setReturnHiddenFolders(true);
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			//Below Sort Strategy is handled by WineSortStrategy.
			//s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			//s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			//s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			//s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
			// TODO: what more I need to do here?
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
		} else if (layoutType == EnumLayoutType.WINE_DEALS.getId()) {
			s.setGrabberDepth(0);
			s.setFilterUnavailable(true);
			s.setLayoutFileName("/includes/layouts/wine_deals.jsp");
			s.setReturnHiddenFolders(false);
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.NO_SORT));
		} else if (layoutType == EnumLayoutType.WINE_EXPERTS_FAVS.getId()) {
			s.setGrabberDepth(0);
			s.setFilterUnavailable(true);
			s.setLayoutFileName("/includes/layouts/wine_experts_favs.jsp");
			s.setReturnHiddenFolders(false);
			s.setIgnoreShowChildren(true);
			s.setIncludeUnavailable(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.NO_SORT));
		} else if (layoutType == EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()) {				
			s.setLayoutFileName("/includes/layouts/trans_multi_paired_items.jsp");
			s.setGrabberDepth(1);
			s.setReturnHiddenFolders(true);
			s.setIgnoreShowChildren(false);
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_AVAILABILITY));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, sortDescending));
			s.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, sortNameAttrib, false));
			// TODO: what more I need to do here?
		} // top 10
		
		if (layoutType == EnumLayoutType.TEMPLATE_LAYOUT.getId()) {				
			s.setLayoutFileName("/includes/layouts/template_layouts.jsp");
			s.setIncludeUnavailable(false);
		}
		
		// ok.. return the layoutManager setting object
		return s;
	}

	
	public static class Settings {
		
		private int grabberDepth = 99;
		private boolean filterDiscontinued = true;
		private boolean filterUnavailable = false;
		private boolean ignoreShowChildren = false;
		private boolean returnHiddenFolders = false;
		private boolean returnSecondaryFolders = false;
		private boolean ignoreDuplicateProducts = false;
		private boolean includeUnavailable = true; //include the unavailable jspf file.
		private String layoutFileName = null;
		private List<SortStrategyElement> sortStrategy = new ArrayList<SortStrategyElement>();
		private boolean returnSkus = false;

		public boolean isFilterDiscontinued() {
			return filterDiscontinued;
		}

		public boolean isFilterUnavailable() {
			return filterUnavailable;
		}

		public int getGrabberDepth() {
			return grabberDepth;
		}

		public boolean isIgnoreDuplicateProducts() {
			return ignoreDuplicateProducts;
		}

		public boolean isIgnoreShowChildren() {
			return ignoreShowChildren;
		}

		public boolean isIncludeUnavailable() {
			return includeUnavailable;
		}

		public String getLayoutFileName() {
			return layoutFileName;
		}

		public boolean isReturnHiddenFolders() {
			return returnHiddenFolders;
		}

		public boolean isReturnSecondaryFolders() {
			return returnSecondaryFolders;
		}

		public List<SortStrategyElement> getSortStrategy() {
			return sortStrategy;
		}
		
		public boolean isReturnSkus() {
			return returnSkus;
		}
		
		public String toString() {
			return (
				" GrabberDepth ="
					+ grabberDepth
					+ "\n FilterDiscontinued ="
					+ filterDiscontinued
					+ "\n FilterUnavailable ="
					+ filterUnavailable
					+ "\n IgnoreShowChildren ="
					+ ignoreShowChildren
					+ "\n ReturnHiddenFolders ="
					+ returnHiddenFolders
					+ "\n FoldersByPriority ="
					+ returnSecondaryFolders
					+ "\n IgnoreDuplicateProducts ="
					+ ignoreDuplicateProducts
					+ "\n IncludeUnavailable ="
					+ includeUnavailable
					+ "\n ProductSortByFirst ="
					+ layoutFileName
					+ "\n returnSkus ="
					+ returnSkus);
		}
		
		public void setFilterDiscontinued(boolean b) {
			filterDiscontinued = b;
		}

		public void setFilterUnavailable(boolean filterUnavailable) {
			this.filterUnavailable = filterUnavailable;
		}

		public void setGrabberDepth(int i) {
			grabberDepth = i;
		}

		public void setIgnoreDuplicateProducts(boolean b) {
			ignoreDuplicateProducts = b;
		}

		public void setIgnoreShowChildren(boolean b) {
			ignoreShowChildren = b;
		}

		public void setIncludeUnavailable(boolean b) {
			includeUnavailable = b;
		}

		public void setLayoutFileName(String string) {
			layoutFileName = string;
		}

		public void setReturnHiddenFolders(boolean b) {
			returnHiddenFolders = b;
		}

		public void setReturnSecondaryFolders(boolean b) {
			returnSecondaryFolders = b;
		}

		public void setSortStrategy(List<SortStrategyElement> sortStrategy) {
			this.sortStrategy = sortStrategy;
		}

		public void addSortStrategyElement(SortStrategyElement strat) {
			this.sortStrategy.add(strat);
		}
		public void setReturnSkus(boolean flag) {
			this.returnSkus = flag;
		}
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
					data.getAttributeString("layoutSettingsName"),
					"com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings",
					true,
					VariableInfo.NESTED)};
		}
	}

}
