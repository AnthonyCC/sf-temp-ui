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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentNodeModelUtil {

	private final static Logger LOGGER = LoggerFactory.getInstance(ContentNodeModelUtil.class);
	
	private final static boolean STRICT_MODE = false;

	static Map<String,String> CONTENT_TO_TYPE_MAP = new HashMap<String,String>();

	static {
		CONTENT_TO_TYPE_MAP.put("Store", ContentNodeModel.TYPE_STORE);
		CONTENT_TO_TYPE_MAP.put("Template", ContentNodeModel.TYPE_TEMPLATE);
		CONTENT_TO_TYPE_MAP.put("Department", ContentNodeModel.TYPE_DEPARTMENT);
		CONTENT_TO_TYPE_MAP.put("Category", ContentNodeModel.TYPE_CATEGORY);
		CONTENT_TO_TYPE_MAP.put("Product", ContentNodeModel.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("Sku", ContentNodeModel.TYPE_SKU);
		CONTENT_TO_TYPE_MAP.put("Brand", ContentNodeModel.TYPE_BRAND);
		CONTENT_TO_TYPE_MAP.put("Domain", ContentNodeModel.TYPE_DOMAIN);
		CONTENT_TO_TYPE_MAP.put("DomainValue", ContentNodeModel.TYPE_DOMAINVALUE);
		CONTENT_TO_TYPE_MAP.put("ConfiguredProduct", ContentNodeModel.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("ConfiguredProductGroup", ContentNodeModel.TYPE_PRODUCT);
		CONTENT_TO_TYPE_MAP.put("ComponentGroup", ContentNodeModel.TYPE_COMPONENTGROUP);
		CONTENT_TO_TYPE_MAP.put("RecipeDepartment", ContentNodeModel.TYPE_RECIPE_DEPARTMENT);
		CONTENT_TO_TYPE_MAP.put("RecipeCategory", ContentNodeModel.TYPE_RECIPE_CATEGORY);
		CONTENT_TO_TYPE_MAP.put("RecipeSubcategory", ContentNodeModel.TYPE_RECIPE_SUBCATEGORY);
		CONTENT_TO_TYPE_MAP.put("Recipe", ContentNodeModel.TYPE_RECIPE);
		CONTENT_TO_TYPE_MAP.put("RecipeVariant", ContentNodeModel.TYPE_RECIPE_VARIANT);
		CONTENT_TO_TYPE_MAP.put("RecipeSection", ContentNodeModel.TYPE_RECIPE_SECTION);
		CONTENT_TO_TYPE_MAP.put("RecipeSource", ContentNodeModel.TYPE_RECIPE_SOURCE);
		CONTENT_TO_TYPE_MAP.put("RecipeAuthor", ContentNodeModel.TYPE_RECIPE_AUTHOR);
		CONTENT_TO_TYPE_MAP.put("FDFolder", ContentNodeModel.TYPE_FD_FOLDER);
		CONTENT_TO_TYPE_MAP.put("BookRetailer", ContentNodeModel.TYPE_BOOK_RETAILER);
		CONTENT_TO_TYPE_MAP.put("RecipeSearchPage", ContentNodeModel.TYPE_RECIPE_SEARCH_PAGE);
		CONTENT_TO_TYPE_MAP.put("RecipeSearchCriteria", ContentNodeModel.TYPE_RECIPE_SEARCH_CRITERIA);
		CONTENT_TO_TYPE_MAP.put("YmalSet", ContentNodeModel.TYPE_YMAL_SET);
		CONTENT_TO_TYPE_MAP.put("StarterList", ContentNodeModel.TYPE_STARTER_LIST);
		CONTENT_TO_TYPE_MAP.put("FavoriteList", ContentNodeModel.TYPE_FAVORITE_LIST);
		CONTENT_TO_TYPE_MAP.put("Recommender", ContentNodeModel.TYPE_RECOMMENDER);
		CONTENT_TO_TYPE_MAP.put("RecommenderStrategy", ContentNodeModel.TYPE_RECOMMENDER_STRATEGY);
		CONTENT_TO_TYPE_MAP.put("FAQ", ContentNodeModel.TYPE_FAQ);
	}

	public static LinkedHashMap<String,Class<?>> TYPE_MODEL_MAP = new LinkedHashMap<String,Class<?>>();

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
		TYPE_MODEL_MAP.put("FavoriteList", FavoriteList.class);
		TYPE_MODEL_MAP.put("Recommender", Recommender.class);
		TYPE_MODEL_MAP.put("RecommenderStrategy", RecommenderStrategy.class);
                TYPE_MODEL_MAP.put("Tile", Tile.class);
                TYPE_MODEL_MAP.put("TileList", TileList.class);
		TYPE_MODEL_MAP.put("FAQ", Faq.class);
		TYPE_MODEL_MAP.put("Producer", ProducerModel.class);
        TYPE_MODEL_MAP.put("ProducerType", ProducerTypeModel.class);
        TYPE_MODEL_MAP.put("MyFD", MyFD.class);
        TYPE_MODEL_MAP.put("HolidayGreeting", HolidayGreeting.class);
	}

	/**
	 * Should only be invoked by ContentFactory.
	 */
	public static ContentNodeModel constructModel(ContentKey key, boolean cache) {
		try {
			Class<?> c = TYPE_MODEL_MAP.get(key.getType().getName());
			if (c == null)
				return null;
			Constructor<?> constructor = c.getConstructor(new Class[] {ContentKey.class});
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
			parentKey = (ContentKey) node.getAttributeValue("PRIMARY_HOME");
		}

		if (parentKey == null) {
			Set<ContentKey> keys = CmsManager.getInstance().getParentKeys(key);
			if (keys.size() == 0) {
				return null;
			}

			Iterator<ContentKey> i = keys.iterator();
			parentKey = i.next();
		}

		return ContentFactory.getInstance().getContentNodeByKey(parentKey);
	}

	public static boolean refreshModels(ContentNodeModelImpl refModel, String refNodeAttr, List childModels, boolean setParent) {
		return refreshModels(refModel, refNodeAttr, childModels, setParent, false);
	}
	
	public static boolean refreshModels( ContentNodeModelImpl refModel, String refNodeAttr, List childModels, boolean setParent, boolean inheritedAttrs ) {

		synchronized ( childModels ) {
			Object value;

			if ( !inheritedAttrs ) {
				value = (refModel.getCMSNode() != null) ? refModel.getCMSNode().getAttributeValue( refNodeAttr ) : null;
			} else {
				value = refModel.getCmsAttributeValue( refNodeAttr );
			}

			List<ContentKey> newKeys = (List<ContentKey>) value;

			if (newKeys == null) {
				newKeys = new ArrayList<ContentKey>();
			}

			boolean equal = compareKeys( newKeys, childModels );

			if ( equal )
				return false; // didn't need to refresh

			childModels.clear();
			for (int i = 0; i < newKeys.size(); i++) {
				ContentKey key = (ContentKey) newKeys.get(i);
		
				ContentNodeModelImpl m = buildChildContentNode(refModel, key, setParent, i);
                                
				if (m!=null) {
					childModels.add(m);
				}

			}

			return true;
		}
	}

    private static ContentNodeModelImpl buildChildContentNode(ContentNodeModelImpl refModel, ContentKey key, boolean setParent, int i) {
        // cache instances in navigable relationships
        boolean cache = setParent;

        // except: for products, do not cache instances outside primary home 
        if (cache && FDContentTypes.PRODUCT.equals(key.getType())) {
        	com.freshdirect.cms.ContentNodeI node = CmsManager.getInstance().getContentNode(key);
        	ContentKey priHome = (ContentKey) node.getAttributeValue("PRIMARY_HOME");
        	cache = refModel.getContentKey().equals(priHome);
        }
        if (cache) {
            ContentNodeModelImpl cachedContentNodeByKey = (ContentNodeModelImpl) ContentFactory.getInstance().getCachedContentNodeByKey(key);
            if (cachedContentNodeByKey != null) {
                ContentNodeModel parentNode = cachedContentNodeByKey.getParentNode();
                if (parentNode == null) {
                    cachedContentNodeByKey.setParentNode(refModel);
                    cachedContentNodeByKey.setPriority(i);
                    return cachedContentNodeByKey;
                } else if (parentNode != refModel ) {
                    if (parentNode.getContentKey().getType() == FDContentTypes.PRODUCT && refModel.getContentKey().equals(parentNode.getContentKey())) {
                        // if the parent is a product, it is possible that we have to construct child objects for not the primary product node.
                        // in that chase construct an object, but do not cache it
                        if (refModel instanceof ProductModelImpl && !((ProductModelImpl)refModel).isInPrimaryHome()) {
                            CategoryModel primaryHome = ((ProductModelImpl) refModel).getPrimaryHome();
                            LOGGER.debug("trying to construct child object of a product " + refModel.getContentKey() + ", which is in "
                                    + refModel.getParentNode().getContentKey() + " instead of the primary home:"
                                    + (primaryHome != null ? primaryHome.getContentKey() : null));
                        }
                        cache = false;
                    } else if (key.getType() == FDContentTypes.CONFIGURED_PRODUCT) {
                        LOGGER.warn("Configured product "+key+" already exists at "+parentNode.getContentKey()+", instead of the expected "+refModel.getContentKey());
                        cache = false;
                    } else if (key.getType() == FDContentTypes.RECIPE_VARIANT && parentNode.getContentKey().getType() == FDContentTypes.RECIPE) {
                        LOGGER.warn(buildErrorMessage(refModel, key, cachedContentNodeByKey, parentNode));
                        cache = false;
                    } else {
                        // in strict mode we cannot accept tolerate this type of errors, temporaly we have to relax this statement.
                        if (STRICT_MODE) {
                            throw new RuntimeException(buildErrorMessage(refModel, key, cachedContentNodeByKey, parentNode));
                        }
						LOGGER.warn(buildErrorMessage(refModel, key, cachedContentNodeByKey, parentNode));
                    }
                } else {
                    if (setParent) {
                        cachedContentNodeByKey.setPriority(i);
                    }
                    return cachedContentNodeByKey;
                }
            }
        }
        ContentNodeModelImpl m = null;
        if (setParent) {
        	m = (ContentNodeModelImpl) constructModel(key, cache);
        	if (m!=null) {
        	    m.setPriority(i);
        	    m.setParentNode(refModel);
        	}
        } else {
        	m = (ContentNodeModelImpl) ContentFactory.getInstance().getContentNodeByKey(key);
        }
        return m;
    }

    /**
     * @param refModel
     * @param key
     * @param cachedContentNodeByKey
     * @param parentNode
     * @return
     */
    private static String buildErrorMessage(ContentNodeModelImpl refModel, ContentKey key, ContentNodeModelImpl cachedContentNodeByKey,
            ContentNodeModel parentNode) {
        return "Content node already exists for key:" + key + ", node:" + cachedContentNodeByKey + ", hash:"
                + System.identityHashCode(cachedContentNodeByKey) + " but with different parent : " + parentNode.getContentKey() + '('
                + System.identityHashCode(parentNode) + ") instead of the expected " + refModel.getContentKey() + "(" + System.identityHashCode(refModel) + ')';
    }

	static boolean compareKeys(List<ContentKey> keys, List<? extends ContentNodeModel> models) {
		if (keys.size() != models.size())
			return false;

		for (int i = 0; i < keys.size(); i++) {
			ContentKey key = keys.get(i);
			ContentNodeModel model = models.get(i);
			if ( key == null || model == null || !key.getId().equals(model.getContentName()) ) 
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

    public static CategoryModel findParentCategory(List<CategoryModel> categories, ProductModel m) {
        CategoryModel foundCategory = null;
        for (int i = 0; i < categories.size(); i++) {
            CategoryModel cur = categories.get(i);

            ProductModel foundProduct = cur.getPrivateProductByName(m.getContentName());
            if (foundProduct != null) {
                foundCategory = cur;
            }

            if (foundCategory == null) {
                foundCategory = findParentCategory(cur.getSubcategories(), m);
            }

            if (foundCategory != null) {
                String hidden = foundCategory.getHideUrl();

                if ( hidden == null || "".equals(hidden) ) {
                    break;
                }
                foundCategory = null;
            }
        }

        return foundCategory;
    }

	private static ProductModel setNearestParentForProduct(ContentNodeModel context, ProductModel product) {
		CategoryModel foundCategory = null;
		DepartmentModel dept = null;

		List<CategoryModel> cats = Collections.emptyList();
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
			Object attr = product.getCmsAttributeValue("PRIMARY_HOME");
			ContentKey primaryHomeKey = (ContentKey) attr;
			foundCategory = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(primaryHomeKey);
		}

		if (foundCategory == product.getParentNode()) {
			return null;
		}
		
		LOGGER.debug("Setting nearest parent for product " + product + " to "
				+ foundCategory + " (was " + product.getParentNode()
				+ "), in context " + context);
		
		ContentNodeModelImpl a = (ContentNodeModelImpl) product.clone();
		a.setParentNode(foundCategory);
		return (ProductModel) a;
	}

	public static void setNearestParentForProducts(ContentNodeModel context, List<ProductModel> products) {
		for (ListIterator<ProductModel> li = products.listIterator(); li.hasNext(); ) {
			ProductModel p = li.next();
			ProductModel clone = setNearestParentForProduct(context, p);
			if (clone!=null) {
				li.set(clone);
			}
		}
	}
	
	public static Set<ContentKey> getAllParentKeys(ContentKey key) {
		Set<ContentKey> allParents = new HashSet<ContentKey>();
		loadAllParentKeys(key, allParents);
		return Collections.unmodifiableSet(allParents);
	}
	
	private static void loadAllParentKeys(ContentKey key, Set<ContentKey> allParents) {
		Set<ContentKey> s = ContentFactory.getInstance().getParentKeys(key);
		if ( s != null && !s.isEmpty() ) {
			for ( ContentKey nextKey : s ) {
				loadAllParentKeys(nextKey, allParents);
				ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(nextKey);
				//Remove the parent nodes that are hidden.
				if(!cn.isHidden()){
					allParents.add(nextKey);
				}
			}
		}
	}

	public static ContentKey getContentKey( String type, String contentId ) {
		return new ContentKey( ContentType.get( type ), contentId );
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
	
	/**
	 * Filters a set of contentkeys by checking if they have virtual categories assigned.
	 * Returns the actual categorymodels.
	 * NOTE: Does not return the virtual categories, only the categories which have one assigned.
	 * 
	 * @param contentKeys Set of ContentKeys to check for virtual categories 
	 * @return filtered Set of CategoryModels which have virtual categories
	 */
	public static Set<CategoryModel> findVirtualCategories(Set<ContentKey> contentKeys){
		Set<CategoryModel> s = new HashSet<CategoryModel>();
		for ( ContentKey key : contentKeys ) {
			ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(key);
			//Make sure the category is not hidden.
			if(cn != null && cn instanceof CategoryModel && !cn.isHidden()){
				CategoryModel cm = (CategoryModel)cn;
				if(cm.getVirtualGroupRefs().size() > 0){
				    s.add(cm);
				}
			}
		}
		return s;
	}

	public static boolean isProductInVirtualCategories(Set<CategoryModel> virtualCats, ProductModel prod) {
		boolean found = false;
		for ( CategoryModel virtualCategory : virtualCats ) {
			List<ProductModel> allProducts = virtualCategory.getProducts();
			found = allProducts.contains(prod);
			if ( found ) {
				break;
			}
		}
		return found;
	}

	/**
	 * Return true, if the given parameter is null or has 0 length.
	 * @param str
	 * @return
	 */
	public static boolean empty(String str) {
	    return str == null || str.length() == 0;
	}
	
	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return the value if it's not null or empty, otherwise the default value
	 */
	public static String nullValue(String value, String defaultValue) {
	    return empty(value) ? defaultValue : value;
	}
	
    public static EnumLayoutType getLayout(ContentNodeModel node, EnumLayoutType defValue) {
        if (node instanceof ProductModel) {
            return ((ProductModel) node).getLayout();
        }
        if (node instanceof ProductContainer) {
            return ((ProductContainer) node).getLayout(defValue);
        }
        return defValue;
    }
	
}
