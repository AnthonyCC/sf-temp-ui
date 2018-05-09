package com.freshdirect.webapp.globalnav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentKeyFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.GlobalNavigationModel;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.RecipeCategory;
import com.freshdirect.storeapi.content.RecipeDepartment;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class NavigationHighlightTag extends SimpleTagSupport {
	private final static Category LOGGER = LoggerFactory.getInstance(NavigationHighlightTag.class);

	@Override
	public void doTag() throws JspException, IOException {

		PageContext ctx = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest)ctx.getRequest();
		FDSessionUser user = (FDSessionUser) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);

		try {

			String deptId = request.getParameter("deptId");
			String catId = request.getParameter("catId");
			String browseId = request.getParameter("id");
			String globalUri = request.getRequestURI();

			//fallback and check attributes on param fail
				if (deptId == null || "".equals(deptId)) {
					deptId = (request.getAttribute("deptId")!=null)?request.getAttribute("deptId").toString():"";
					if ("".equals(deptId.toString())) { deptId = null; }
				}
				if (catId == null || "".equals(catId)) {
					catId = (request.getAttribute("catId")!=null)?request.getAttribute("catId").toString():"";
					if ("".equals(catId.toString())) { catId = null; }
				}
				if (browseId == null || "".equals(browseId)) {
					browseId = (request.getAttribute("browseId")!=null)?request.getAttribute("browseId").toString():"";
					if ("".equals(browseId.toString())) { browseId = null; }
				}

			String thisDept = "";

			if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user)) {
				GlobalNavigationModel globalNavigationModel = GlobalNavContextUtil.getGlobalNavigationModel(user);
				HashMap<String, String> relatedDepartmentIds = new HashMap<String, String>();
				for (ContentNodeModel contentNode : globalNavigationModel.getItems()) {
					if (contentNode instanceof SuperDepartmentModel) {
						for (DepartmentModel dept : ((SuperDepartmentModel) contentNode).getDepartments()) {
							relatedDepartmentIds.put(dept.getContentName(), contentNode.getContentName());
						}
					}
				}

				String ppicksId = ContentKeyFactory.getPresidentsPicksCategoryKey().getId();

				List<String> bottomTopNavCategories = new ArrayList<String>(Arrays.asList(ppicksId, "wgd_produce", "wgd_seafood", "wgd_butchers", "wgd_kitchendeals", "wgd_deals"));

				if (browseId != null) {
                    ContentNodeModel thisObj = ContentFactory.getInstance().getContentNode(browseId);
					if (bottomTopNavCategories.contains(browseId)) {
							thisDept = browseId;

					} else if (thisObj instanceof SuperDepartmentModel) {
						thisDept = thisObj.getContentName();
					} else if (thisObj instanceof DepartmentModel) {
						String sdRelation = "";
						if (!"".equals(sdRelation = checkSuperDepartmentRelation(thisObj, relatedDepartmentIds))) {
							thisDept = sdRelation;
						} else {
							thisDept = thisObj.getContentName();
						}
					} else if (thisObj instanceof CategoryModel) {
						String sdRelation = "";
						thisObj = findParentOfCategory(browseId);
						if (bottomTopNavCategories.contains(thisObj.getParentNode().getContentName())) {
							thisDept = thisObj.getParentNode().getContentName();
						} else if (thisObj!= null && !"".equals(sdRelation = checkSuperDepartmentRelation(thisObj, relatedDepartmentIds))) {
							thisDept = sdRelation;
						} else {
							thisDept = thisObj.getContentName();
						}
					}
				} else if (catId != null){
                    ContentNodeModel thisObj = ContentFactory.getInstance().getContentNode(catId);
					ContentNodeModel thisDeptObj = findParentOfCategory (catId);
					String sdRelation = "";
					if (bottomTopNavCategories.contains(thisObj.getParentNode().getContentName())) {
						thisDept = thisObj.getParentNode().getContentName();
					} else if (thisDeptObj!= null && !"".equals(sdRelation = checkSuperDepartmentRelation(thisDeptObj, relatedDepartmentIds))) {
						thisDept = sdRelation;
                    } else if (thisDeptObj != null) {
                        thisDept = thisDeptObj.getContentName();
					}
				} else if (deptId != null) {
					if ("kosher_temp".equalsIgnoreCase(deptId)){
						thisDept = "kos";
					} else {
                        ContentNodeModel thisDeptObj = PopulatorUtil.getContentNode(deptId);
						String sdRelation = "";
						if (thisDeptObj!= null && !"".equals(sdRelation = checkSuperDepartmentRelation(thisDeptObj, relatedDepartmentIds))) {
							thisDept = sdRelation;
						} else {
							thisDept = thisDeptObj.getContentName();
						}
					}


				} else {
					//hmmm..if this url contains recipe%.jsp then assume the department is Recipe, since no cat or deptId specified
					if (globalUri.startsWith("/recipe_dept.jsp") ||
					    globalUri.startsWith("/recipe_cat.jsp") ||
					    globalUri.startsWith("/recipe_subcat.jsp") ||
					    globalUri.startsWith("/recipe.jsp") ||
					    globalUri.startsWith("/recipe_search.jsp") ) {
					  thisDept = RecipeDepartment.getDefault().getContentName();
					}
				}
			} else {
				if (catId != null){
					ContentNodeModel thisDeptObj = findParentOfCategory (catId);
					thisDept = thisDeptObj.getContentName();
				} else if (deptId != null) {
					if ("kosher_temp".equalsIgnoreCase(deptId)){
						thisDept = "kos";
					} else {
                        ContentNodeModel thisDeptObj = ContentFactory.getInstance().getContentNode(deptId);
						thisDept = thisDeptObj.getContentName();
					}


				} else {
					//hmmm..if this url contains recipe%.jsp then assume the department is Recipe, since no cat or deptId specified
					if (globalUri.startsWith("/recipe_dept.jsp") ||
					    globalUri.startsWith("/recipe_cat.jsp") ||
					    globalUri.startsWith("/recipe_subcat.jsp") ||
					    globalUri.startsWith("/recipe.jsp") ||
					    globalUri.startsWith("/recipe_search.jsp") ) {
					  thisDept = RecipeDepartment.getDefault().getContentName();
					}
				}

			}

			boolean isAtHome = true;
			if(	globalUri.indexOf("login")> -1 ||
				globalUri.indexOf("promotion")> -1 ||
				globalUri.indexOf("newproducts")> -1 ||
				globalUri.indexOf("quickshop")> -1 ||
				globalUri.indexOf("checkout")> -1 ||
				globalUri.indexOf("help")> -1 ||
				globalUri.indexOf("site_access")> -1 ||
				globalUri.indexOf("about")> -1  ||
				globalUri.indexOf("registration")> -1  ||
				globalUri.indexOf("your_account")> -1 ||
				globalUri.indexOf("search")> -1 ||
				globalUri.indexOf("survey")> -1 ||
				globalUri.indexOf("/department.jsp")> -1 ||
				globalUri.indexOf("/whatsgood.jsp")> -1 ||
				globalUri.indexOf("view_cart")> -1 ||
				globalUri.indexOf("myfd")> -1 ||
				globalUri.indexOf("health_warning")> -1 ||
				globalUri.indexOf("product_modify")> -1	) {
				isAtHome = false;
			}
				String navigation = isAtHome ? "home" : "off";
			if(!thisDept.equals("") && !thisDept.equalsIgnoreCase("about") && !thisDept.equalsIgnoreCase("cmty")){
				navigation = thisDept;
			}
			ctx.setAttribute("navigation", navigation);
        } catch (FDResourceException e) {
			LOGGER.error(e);
		}

	}

    private ContentNodeModel findParentOfCategory(String catId) throws FDNotFoundException {
        ContentNodeModel categoryNode = ContentFactory.getInstance().getContentNode(catId);
		ContentNodeModel dept = null;
		if (categoryNode instanceof RecipeCategory) {
			dept = RecipeDepartment.getDefault();
		} else {
		   dept= ((CategoryModel)categoryNode).getDepartment();
		}
		return dept;
	}

	private String checkSuperDepartmentRelation(ContentNodeModel thisDeptObj, Map<String, String> relatedDepartmentIds) {

		for (String id : relatedDepartmentIds.keySet()) {
			if (id.equals(thisDeptObj.getContentName())) {
				return relatedDepartmentIds.get(id);
			}
		}
		return "";
	}
}
