package com.freshdirect.fdstore.content;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentNodeModelUtil {

	private final static Category LOGGER = LoggerFactory.getInstance(ContentNodeModelUtil.class);

	static HashMap CONTENT_TO_TYPE_MAP = new HashMap();

	static {
		CONTENT_TO_TYPE_MAP.put("Store", ContentNodeI.TYPE_STORE);
		CONTENT_TO_TYPE_MAP.put("Template", ContentNodeI.TYPE_TEMPLATE);
		CONTENT_TO_TYPE_MAP.put("Department", ContentNodeI.TYPE_DEPARTMENT);
		CONTENT_TO_TYPE_MAP.put("Category", ContentNodeI.TYPE_CATEGORY);
		CONTENT_TO_TYPE_MAP.put("Product", ContentNodeI.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("Sku", ContentNodeI.TYPE_SKU);
		CONTENT_TO_TYPE_MAP.put("Brand", ContentNodeI.TYPE_BRAND);
		CONTENT_TO_TYPE_MAP.put("Domain", ContentNodeI.TYPE_DOMAIN);
		CONTENT_TO_TYPE_MAP.put("DomainValue", ContentNodeI.TYPE_DOMAINVALUE);
		CONTENT_TO_TYPE_MAP.put("ConfiguredProduct", ContentNodeI.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("ConfiguredProductGroup", ContentNodeI.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("ComponentGroup", ContentNodeI.TYPE_COMPONENTGROUP);
		CONTENT_TO_TYPE_MAP.put("RecipeDepartment", ContentNodeI.TYPE_RECIPE_DEPARTMENT);
		CONTENT_TO_TYPE_MAP.put("RecipeCategory", ContentNodeI.TYPE_RECIPE_CATEGORY);
		CONTENT_TO_TYPE_MAP.put("RecipeSubcategory", ContentNodeI.TYPE_RECIPE_SUBCATEGORY);
		CONTENT_TO_TYPE_MAP.put("Recipe", ContentNodeI.TYPE_RECIPE);
		CONTENT_TO_TYPE_MAP.put("RecipeVariant", ContentNodeI.TYPE_RECIPE_VARIANT);
		CONTENT_TO_TYPE_MAP.put("RecipeSection", ContentNodeI.TYPE_RECIPE_SECTION);
		CONTENT_TO_TYPE_MAP.put("RecipeSource", ContentNodeI.TYPE_RECIPE_SOURCE);
		CONTENT_TO_TYPE_MAP.put("RecipeAuthor", ContentNodeI.TYPE_RECIPE_AUTHOR);
		CONTENT_TO_TYPE_MAP.put("FDFolder", ContentNodeI.TYPE_FD_FOLDER);
		CONTENT_TO_TYPE_MAP.put("BookRetailer", ContentNodeI.TYPE_BOOK_RETAILER);
		CONTENT_TO_TYPE_MAP.put("RecipeSearchPage", ContentNodeI.TYPE_RECIPE_SEARCH_PAGE);
		CONTENT_TO_TYPE_MAP.put("RecipeSearchCriteria", ContentNodeI.TYPE_RECIPE_SEARCH_CRITERIA);
		CONTENT_TO_TYPE_MAP.put("YmalSet", ContentNodeI.TYPE_YMAL_SET);
		CONTENT_TO_TYPE_MAP.put("StarterList", ContentNodeI.TYPE_STARTER_LIST);
	}

	public static LinkedHashMap TYPE_MODEL_MAP = new LinkedHashMap();

	static {
		TYPE_MODEL_MAP.put("Sku", SkuModel.class);
		TYPE_MODEL_MAP.put("Product", ProductModelImpl.class);
		TYPE_MODEL_MAP.put("Category", CategoryModel.class);
		TYPE_MODEL_MAP.put("Brand", BrandModel.class);
		TYPE_MODEL_MAP.put("DomainValue", DomainValue.class);
		TYPE_MODEL_MAP.put("Domain", Domain.class);
		TYPE_MODEL_MAP.put("Department", DepartmentModel.class);
		TYPE_MODEL_MAP.put("Store", StoreModel.class);
		TYPE_MODEL_MAP.put("Template", Template.class);
		TYPE_MODEL_MAP.put("ConfiguredProduct", ConfiguredProduct.class);
		TYPE_MODEL_MAP.put("ConfiguredProductGroup", ConfiguredProductGroup.class);
		TYPE_MODEL_MAP.put("ComponentGroup", ComponentGroupModel.class);
		TYPE_MODEL_MAP.put("RecipeVariant", RecipeVariant.class);
		TYPE_MODEL_MAP.put("RecipeSection", RecipeSection.class);
		TYPE_MODEL_MAP.put("Recipe", Recipe.class);
		TYPE_MODEL_MAP.put("RecipeSubcategory", RecipeSubcategory.class);
		TYPE_MODEL_MAP.put("RecipeCategory", RecipeCategory.class);
		TYPE_MODEL_MAP.put("RecipeDepartment", RecipeDepartment.class);
		TYPE_MODEL_MAP.put("RecipeSource", RecipeSource.class);
		TYPE_MODEL_MAP.put("RecipeAuthor", RecipeAuthor.class);
		TYPE_MODEL_MAP.put("FDFolder", FDFolder.class);
		TYPE_MODEL_MAP.put("BookRetailer", BookRetailer.class);
		TYPE_MODEL_MAP.put("RecipeSearchPage", RecipeSearchPage.class);
		TYPE_MODEL_MAP.put("RecipeSearchCriteria", RecipeSearchCriteria.class);
		TYPE_MODEL_MAP.put("YmalSet", YmalSet.class);
		TYPE_MODEL_MAP.put("StarterList", StarterList.class);
	}

	public static ContentNodeModel constructModel(ContentKey key, boolean cache) {
		try {
			Class c = (Class) TYPE_MODEL_MAP.get(key.getType().getName());
			if (c == null)
				return null;
			Constructor constructor = c.getConstructor(new Class[] {ContentKey.class});
			ContentNodeModel returnModel = (ContentNodeModel) constructor.newInstance(new Object[] {key});

			// cache it
			if (cache) {
				ContentFactory.getInstance().registerContentNode(returnModel);
			}

			return returnModel;

		} catch (Exception ex) {
			throw new CmsRuntimeException("Error creating model for " + key, ex);
		}
	}

	public static ContentNodeModel findDefaultParent(ContentKey key) {
		if (FDContentTypes.STORE.equals(key.getType())) {
			return null;
		}

		ContentKey parentKey = null;

		if (FDContentTypes.PRODUCT.equals(key.getType())) {
			com.freshdirect.cms.ContentNodeI node = CmsManager.getInstance().getContentNode(key);
			parentKey = (ContentKey) node.getAttribute("PRIMARY_HOME").getValue();
		}

		if (parentKey == null) {
			Set keys = CmsManager.getInstance().getParentKeys(key);
			if (keys.size() == 0) {
				return null;
			}

			Iterator i = keys.iterator();
			parentKey = (ContentKey) i.next();
		}

		return ContentFactory.getInstance().getContentNode(parentKey.getId());
	}

	public static boolean refreshModels(ContentNodeModelImpl refModel, String refNodeAttr, List childModels, boolean setParent) {
		return refreshModels(refModel, refNodeAttr, childModels, setParent, false);
	}
	
	public static boolean refreshModels(ContentNodeModelImpl refModel, String refNodeAttr, List childModels, boolean setParent, boolean inheritedAttrs) {

		synchronized(childModels) {
			AttributeI attr;

			if (!inheritedAttrs) {
				attr = refModel.getCMSNode().getAttribute(refNodeAttr);
			} else {
				attr = refModel.getCmsAttribute(refNodeAttr);
			}
			
		    List newKeys = attr == null
            			 ? null
            			 : (List) attr.getValue();

			if (newKeys == null) {
				newKeys = new ArrayList();
			}
		
			boolean equal = compareKeys(newKeys, childModels);
		
			if (equal)
				return false; // didn't need to refresh
		
			childModels.clear();
			for (int i = 0; i < newKeys.size(); i++) {
				ContentKey key = (ContentKey) newKeys.get(i);
		
				// cache instances in navigable relationships
				boolean cache = setParent;
		
				// except: for products, do not cache instances outside primary home 
				if (cache && FDContentTypes.PRODUCT.equals(key.getType())) {
					com.freshdirect.cms.ContentNodeI node = CmsManager.getInstance().getContentNode(key);
					ContentKey priHome = (ContentKey) node.getAttribute("PRIMARY_HOME").getValue();
					cache = refModel.getContentKey().equals(priHome);
				}
				CmsContentNodeAdapter  m;
				if (setParent) {
					m = (CmsContentNodeAdapter) constructModel(key, cache);
					if (m==null) continue;
					m.setPriority(i);
					m.setParentNode(refModel);
						
				} else {
					m = (CmsContentNodeAdapter) ContentFactory.getInstance().getContentNodeByKey(key);
					
				}
				
				
				childModels.add(m);
			}
		
			return true;
		}
	}

	static boolean compareKeys(List keys, List models) {
		if (keys.size() != models.size())
			return false;

		for (int i = 0; i < keys.size(); i++) {
			ContentKey key = (ContentKey) keys.get(i);
			ContentNodeModel model = (ContentNodeModel) models.get(i);
			if (key.getId().equals(model.getContentName()))
				continue;
			else
				return false;
		}
		return true;
	}

	/**
	 * Recursively check existence of catKey
	 */
	/*
	private static boolean categoryExists(List categories, ContentKey catKey) {
		boolean foundCategory = false;
		for (int i = 0; i < categories.size(); i++) {
			CategoryModel cur = (CategoryModel) categories.get(i);
			if (cur.getContentKey().equals(catKey)) {
				return true;
			}
			foundCategory = categoryExists(cur.getSubcategories(), catKey);
		}
		return foundCategory;
	}
	*/

	public static CategoryModel findParentCategory(List categories, ProductModel m) {
		CategoryModel foundCategory = null;
		for (int i = 0; i < categories.size(); i++) {
			CategoryModel cur = (CategoryModel) categories.get(i);

			ProductModel foundProduct = cur.getPrivateProductByName(m.getContentName());
			if (foundProduct != null) {
				foundCategory = cur;
			}

			if (foundCategory == null) {
				foundCategory = findParentCategory(cur.getSubcategories(), m);
			}

			if (foundCategory != null) {
				com.freshdirect.fdstore.attributes.Attribute hidden = foundCategory.getAttribute("HIDE_URL");

				if ((hidden == null) || (hidden.getValue() == null)) {
					break;
				}

				if (hidden.getValue() != null) {
					String str = (String) hidden.getValue();
					if ("".equals(str)) {
						break;
					} else {
						foundCategory = null;
					}
				}
			}
		}

		return foundCategory;
	}

	private static ProductModel setNearestParentForProduct(ContentNodeModel context, ProductModel product) {
		CategoryModel foundCategory = null;
		DepartmentModel dept = null;

		List cats = Collections.EMPTY_LIST;
		if (context instanceof CategoryModel) {
			CategoryModel cat = (CategoryModel) context;
			cats = cat.getSubcategories();
			dept = cat.getDepartment();

			if (cat.getPrivateProductByName(product.getContentName()) != null) {
				foundCategory = cat;
			}

			if (foundCategory == null) {
				foundCategory = ContentNodeModelUtil.findParentCategory(cats, product);
			}
		} else if (context instanceof DepartmentModel) {
			dept = (DepartmentModel) context;
		}

		if ((foundCategory == null) && (dept != null)) {
			cats = dept.getCategories();

			foundCategory = ContentNodeModelUtil.findParentCategory(cats, product);
		}
		
		if (foundCategory == null) {
			AttributeI attr = product.getCmsAttribute("PRIMARY_HOME");
			ContentKey primaryHomeKey = (ContentKey) attr.getValue();
			foundCategory = (CategoryModel) ContentFactory.getInstance().getContentNode(primaryHomeKey.getId());
		}

		if (foundCategory == product.getParentNode()) {
			return null;
		}
		
		LOGGER.debug("Setting nearest parent for product " + product + " to "
				+ foundCategory + " (was " + product.getParentNode()
				+ "), in context " + context);
		
		CmsContentNodeAdapter a = (CmsContentNodeAdapter) product.clone();
		a.setParentNode(foundCategory);
		return (ProductModel) a;
	}

	public static void setNearestParentForProducts(ContentNodeModel context, List products) {
		for (ListIterator li = products.listIterator(); li.hasNext(); ) {
			ProductModel p = (ProductModel) li.next();
			ProductModel clone = setNearestParentForProduct(context, p);
			if (clone!=null) {
				li.set(clone);
			}
		}
	}
	
	public static Set getAllParentKeys(ContentKey key) {
		Set allParents = new HashSet();
		loadAllParentKeys(key, allParents);
		return Collections.unmodifiableSet(allParents);
	}
	
	private static void loadAllParentKeys(ContentKey key, Set allParents) {
		Set s = ContentFactory.getInstance().getParentKeys(key);
		if(s != null && !s.isEmpty()){
			
			Iterator i = s.iterator();
			while(i.hasNext()){
				ContentKey nextKey = (ContentKey) i.next();
				loadAllParentKeys(nextKey, allParents);
				ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(nextKey);
				//Remove the parent nodes that are hidden.
				if(!cn.isHidden()){
					allParents.add(nextKey);
				}
			}
		}
	}

	public static ContentKey getContentKey(String type, String contentId) {
			return new ContentKey(ContentType.get(type), contentId);
	}
	
	/**
	 * This method returns the reference content key if the parameter contentId is
	 * a ALIAS category else returns null.
	 * @param key
	 * @return ContentKey
	 */
	public static ContentKey getAliasCategoryRef(String type, String contentId){
		ContentKey refKey = null;
		ContentKey key = new ContentKey(ContentType.get(type), contentId);
		ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(key);
		//Make sure the category is not hidden.
		if(cn != null && !cn.isHidden() && cn instanceof CategoryModel){
			CategoryModel cm = (CategoryModel)cn;
			refKey = cm.getAliasAttributeValue();
		}
		return refKey;
	}
	
	public static Set findVirtualCategories(Set contentKeys){
		Set s = new HashSet();
		for (Iterator i = contentKeys.iterator(); i.hasNext(); ) {
			ContentKey key = (ContentKey) i.next();
			ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(key);
			//Make sure the category is not hidden.
			if(cn != null && cn instanceof CategoryModel && !cn.isHidden()){
				CategoryModel cm = (CategoryModel)cn;
				if(cm.getAttribute("VIRTUAL_GROUP", (Object)null) != null){
					s.add(cm);
				}
			}
		}
		return s;
	}

	public static boolean isProductInVirtualCategories(Set virtualCats, ProductModel prod) {
		boolean found = false;
		for (Iterator i = virtualCats.iterator(); i.hasNext(); ) {
			CategoryModel virtualCategory = (CategoryModel)i.next();
			List allProducts = virtualCategory.getProducts();
			found = allProducts.contains(prod);
			if(found){
				break;
			}
		}
		return found;
	}
}
