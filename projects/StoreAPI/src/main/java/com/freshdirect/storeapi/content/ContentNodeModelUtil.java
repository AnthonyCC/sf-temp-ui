package com.freshdirect.storeapi.content;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.AttributeI;
import com.freshdirect.storeapi.ContentNode;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;

public class ContentNodeModelUtil {

    private static final Logger LOGGER = LoggerFactory.getInstance(ContentNodeModelUtil.class);

    private static final boolean STRICT_MODE = false;

    private static final Map<ContentType, String> CONTENT_TO_TYPE_MAP = new HashMap<ContentType, String>();

    static {
        CONTENT_TO_TYPE_MAP.put(ContentType.Store, ContentNodeModel.TYPE_STORE);
        CONTENT_TO_TYPE_MAP.put(ContentType.Template, ContentNodeModel.TYPE_TEMPLATE);
        CONTENT_TO_TYPE_MAP.put(ContentType.Department, ContentNodeModel.TYPE_DEPARTMENT);
        CONTENT_TO_TYPE_MAP.put(ContentType.Category, ContentNodeModel.TYPE_CATEGORY);
        CONTENT_TO_TYPE_MAP.put(ContentType.Product, ContentNodeModel.TYPE_PRODUCT);
        CONTENT_TO_TYPE_MAP.put(ContentType.Sku, ContentNodeModel.TYPE_SKU);
        CONTENT_TO_TYPE_MAP.put(ContentType.Brand, ContentNodeModel.TYPE_BRAND);
        CONTENT_TO_TYPE_MAP.put(ContentType.Domain, ContentNodeModel.TYPE_DOMAIN);
        CONTENT_TO_TYPE_MAP.put(ContentType.DomainValue, ContentNodeModel.TYPE_DOMAINVALUE);
        CONTENT_TO_TYPE_MAP.put(ContentType.ConfiguredProduct, ContentNodeModel.TYPE_PRODUCT);
        CONTENT_TO_TYPE_MAP.put(ContentType.ConfiguredProductGroup, ContentNodeModel.TYPE_PRODUCT);
        CONTENT_TO_TYPE_MAP.put(ContentType.ComponentGroup, ContentNodeModel.TYPE_COMPONENTGROUP);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeDepartment, ContentNodeModel.TYPE_RECIPE_DEPARTMENT);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeCategory, ContentNodeModel.TYPE_RECIPE_CATEGORY);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeSubcategory, ContentNodeModel.TYPE_RECIPE_SUBCATEGORY);
        CONTENT_TO_TYPE_MAP.put(ContentType.Recipe, ContentNodeModel.TYPE_RECIPE);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeVariant, ContentNodeModel.TYPE_RECIPE_VARIANT);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeSection, ContentNodeModel.TYPE_RECIPE_SECTION);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeSource, ContentNodeModel.TYPE_RECIPE_SOURCE);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeAuthor, ContentNodeModel.TYPE_RECIPE_AUTHOR);
        CONTENT_TO_TYPE_MAP.put(ContentType.FDFolder, ContentNodeModel.TYPE_FD_FOLDER);
        CONTENT_TO_TYPE_MAP.put(ContentType.BookRetailer, ContentNodeModel.TYPE_BOOK_RETAILER);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeSearchPage, ContentNodeModel.TYPE_RECIPE_SEARCH_PAGE);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecipeSearchCriteria, ContentNodeModel.TYPE_RECIPE_SEARCH_CRITERIA);
        CONTENT_TO_TYPE_MAP.put(ContentType.YmalSet, ContentNodeModel.TYPE_YMAL_SET);
        CONTENT_TO_TYPE_MAP.put(ContentType.StarterList, ContentNodeModel.TYPE_STARTER_LIST);
        CONTENT_TO_TYPE_MAP.put(ContentType.FavoriteList, ContentNodeModel.TYPE_FAVORITE_LIST);
        CONTENT_TO_TYPE_MAP.put(ContentType.Recommender, ContentNodeModel.TYPE_RECOMMENDER);
        CONTENT_TO_TYPE_MAP.put(ContentType.RecommenderStrategy, ContentNodeModel.TYPE_RECOMMENDER_STRATEGY);
        CONTENT_TO_TYPE_MAP.put(ContentType.FAQ, ContentNodeModel.TYPE_FAQ);
        CONTENT_TO_TYPE_MAP.put(ContentType.Page, ContentNodeModel.TYPE_PAGE);
        CONTENT_TO_TYPE_MAP.put(ContentType.SuperDepartment, ContentNodeModel.TYPE_SUPERDEPARTMENT);
        CONTENT_TO_TYPE_MAP.put(ContentType.CategorySection, ContentNodeModel.TYPE_CATEGORY_SECTION);
        CONTENT_TO_TYPE_MAP.put(ContentType.GlobalNavigation, ContentNodeModel.TYPE_GLOBAL_NAVIGATINO);
    }

    private static final LinkedHashMap<ContentType, Class<?>> TYPE_MODEL_MAP = new LinkedHashMap<ContentType, Class<?>>();

    static {
        TYPE_MODEL_MAP.put(ContentType.Sku, SkuModel.class);
        TYPE_MODEL_MAP.put(ContentType.Product, ProductModelImpl.class);
        TYPE_MODEL_MAP.put(ContentType.Category, CategoryModel.class);
        TYPE_MODEL_MAP.put(ContentType.Brand, BrandModel.class);
        TYPE_MODEL_MAP.put(ContentType.DomainValue, DomainValue.class);
        TYPE_MODEL_MAP.put(ContentType.Domain, Domain.class);
        TYPE_MODEL_MAP.put(ContentType.Department, DepartmentModel.class);
        TYPE_MODEL_MAP.put(ContentType.Store, StoreModel.class);
        TYPE_MODEL_MAP.put(ContentType.Template, Template.class);
        TYPE_MODEL_MAP.put(ContentType.ConfiguredProduct, ConfiguredProduct.class);
        TYPE_MODEL_MAP.put(ContentType.ConfiguredProductGroup, ConfiguredProductGroup.class);
        TYPE_MODEL_MAP.put(ContentType.ComponentGroup, ComponentGroupModel.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeVariant, RecipeVariant.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeSection, RecipeSection.class);
        TYPE_MODEL_MAP.put(ContentType.Recipe, Recipe.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeSubcategory, RecipeSubcategory.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeCategory, RecipeCategory.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeDepartment, RecipeDepartment.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeSource, RecipeSource.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeAuthor, RecipeAuthor.class);
        TYPE_MODEL_MAP.put(ContentType.FDFolder, FDFolder.class);
        TYPE_MODEL_MAP.put(ContentType.BookRetailer, BookRetailer.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeSearchPage, RecipeSearchPage.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeSearchCriteria, RecipeSearchCriteria.class);
        TYPE_MODEL_MAP.put(ContentType.YmalSet, YmalSet.class);
        TYPE_MODEL_MAP.put(ContentType.StarterList, StarterList.class);
        TYPE_MODEL_MAP.put(ContentType.FavoriteList, FavoriteList.class);
        TYPE_MODEL_MAP.put(ContentType.Recommender, Recommender.class);
        TYPE_MODEL_MAP.put(ContentType.RecommenderStrategy, RecommenderStrategy.class);
        TYPE_MODEL_MAP.put(ContentType.Tile, Tile.class);
        TYPE_MODEL_MAP.put(ContentType.TileList, TileList.class);
        TYPE_MODEL_MAP.put(ContentType.FAQ, Faq.class);
        TYPE_MODEL_MAP.put(ContentType.Producer, ProducerModel.class);
        TYPE_MODEL_MAP.put(ContentType.ProducerType, ProducerTypeModel.class);
        TYPE_MODEL_MAP.put(ContentType.MyFD, MyFD.class);
        TYPE_MODEL_MAP.put(ContentType.HolidayGreeting, HolidayGreeting.class);
        TYPE_MODEL_MAP.put(ContentType.GlobalMenuItem, GlobalMenuItemModel.class);
        TYPE_MODEL_MAP.put(ContentType.GlobalMenuSection, GlobalMenuSectionModel.class);
        TYPE_MODEL_MAP.put(ContentType.DonationOrganization, DonationOrganization.class);
        TYPE_MODEL_MAP.put(ContentType.YoutubeVideo, YoutubeVideoModel.class);
        TYPE_MODEL_MAP.put(ContentType.Page, PageModel.class);
        TYPE_MODEL_MAP.put(ContentType.ProductFilter, ProductFilterModel.class);
        TYPE_MODEL_MAP.put(ContentType.ProductFilterGroup, ProductFilterGroupModel.class);
        TYPE_MODEL_MAP.put(ContentType.ProductGrabber, ProductGrabberModel.class);
        TYPE_MODEL_MAP.put(ContentType.Tag, TagModel.class);
        TYPE_MODEL_MAP.put(ContentType.ProductFilterMultiGroup, ProductFilterMultiGroupModel.class);
        TYPE_MODEL_MAP.put(ContentType.SortOption, SortOptionModel.class);
        TYPE_MODEL_MAP.put(ContentType.SuperDepartment, SuperDepartmentModel.class);
        TYPE_MODEL_MAP.put(ContentType.GlobalNavigation, GlobalNavigationModel.class);
        TYPE_MODEL_MAP.put(ContentType.CategorySection, CategorySectionModel.class);
        TYPE_MODEL_MAP.put(ContentType.Banner, BannerModel.class);
        TYPE_MODEL_MAP.put(ContentType.SearchSuggestionGroup, SearchSuggestionGroupModel.class);
        TYPE_MODEL_MAP.put(ContentType.RecipeTag, RecipeTagModel.class);
        TYPE_MODEL_MAP.put(ContentType.ImageBanner, ImageBanner.class);
    }

    public static String getCachedContentType(ContentType type) {
        return CONTENT_TO_TYPE_MAP.get(type);
    }

    /**
     * Should only be invoked by ContentFactory.
     */
    public static ContentNodeModel constructModel(ContentKey key, boolean shouldKeyCached) {
        try {
            Class<?> c = TYPE_MODEL_MAP.get(key.type);
            if (c == null || !CmsManager.getInstance().containsContentKey(key)) {
                return null;
            }
            Constructor<?> constructor = c.getConstructor(new Class[] { ContentKey.class });
            ContentNodeModel model = (ContentNodeModel) constructor.newInstance(new Object[] { key });

            // cache it
            if (model != null && shouldKeyCached) {
                ContentFactory.getInstance().updateContentNodeCaches(model.getContentName(), model);
            }

            return model;

        } catch (Exception ex) {
            throw new RuntimeException("Error creating model for " + key, ex);
        }
    }

    public static ContentNodeModel constructModel(String contentId, boolean shouldIdCached) {
        ContentNodeModel model = null;
        for (ContentType type : TYPE_MODEL_MAP.keySet()) {
            ContentKey key = ContentKeyFactory.get(type, contentId);

            model = constructModel(key, shouldIdCached);
            if (model != null) {
                break;
            }

            // ContentNodeI node = ContentFactory.getInstance().getContentNode(key);
            // if (node != null) {
            // break;
            // }
        }
        return model;
    }

    public static boolean hasWineDepartment(ContentKey key) {
        Set<ContentKey> keys = ContentFactory.getInstance().getParentKeys(key);
        for (ContentKey currentKey : keys) {
            if (ContentType.Department == currentKey.type && WineUtil.getWineAssociateId().equalsIgnoreCase(currentKey.id)) {
                return true;
            } else {
                if (hasWineDepartment(currentKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean refreshModels(ContentNodeModelImpl refModel, String refNodeAttr, List<? extends ContentNodeModel> childModels, boolean setParent) {
        return refreshModels(refModel, refNodeAttr, childModels, setParent, false);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean refreshModels(ContentNodeModelImpl refModel, String refNodeAttr, List<? extends ContentNodeModel> childModels, boolean setParent,
            boolean inheritedAttrs) {
        Collection<? extends ContentNodeModel> updatedChildModels = new ArrayList<ContentNodeModel>();

        List<ContentKey> recentKeys = grabFreshChildKeys(refModel.getContentKey(), refNodeAttr, refModel.getParentKey());

        final boolean updateChildModels = !CmsManager.getInstance().isReadOnlyContent() || !compareKeys(recentKeys, childModels);
        if (updateChildModels) {
            for (int i = 0; i < recentKeys.size(); i++) {
                ContentKey key = recentKeys.get(i);
                ContentNodeModel newModel = buildChildContentNode(refModel, key, setParent, i);
                if (newModel != null) {
                    ((List) updatedChildModels).add(newModel);
                }
            }

            // update child models
            synchronized (childModels) {
                childModels.clear();
                ((List) childModels).addAll(updatedChildModels);
            }
        }

        return updateChildModels;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    private static List<ContentKey> grabFreshChildKeys(ContentKey contentKey, String attributeName, ContentKey parentKey) {
        ContentNodeI cmsNode = CmsManager.getInstance().getContentNode(contentKey);
        AttributeI cmsAttribute = ((ContentNode) cmsNode).getAttribute(attributeName);

        List<ContentKey> recentKeys = null;
        if (cmsAttribute != null) {
            final Attribute attributeDefinition = cmsAttribute.getDefinition();
            recentKeys = attributeDefinition.getFlags().isInheritable() ? getInheritedChildKeys(contentKey, attributeDefinition, parentKey)
                    : (List<ContentKey>) cmsAttribute.getValue();
        }
        return recentKeys != null ? recentKeys : Collections.<ContentKey> emptyList();
    }

    @SuppressWarnings("unchecked")
    private static List<ContentKey> getInheritedChildKeys(ContentKey contentKey, Attribute attribute, ContentKey parentKey) {
        List<ContentKey> childKeys = null;

        Map<Attribute, Object> values = CmsManager.getInstance().getInheritedValuesOf(contentKey, parentKey);
        childKeys = (List<ContentKey>) values.get(attribute);

        return childKeys != null ? childKeys : Collections.<ContentKey> emptyList();
    }

    private static ContentNodeModelImpl buildChildContentNode(ContentNodeModelImpl ownerModel, ContentKey childKey, boolean setParent, int ordinal) {
        // cache instances in navigable relationships
        boolean shouldCacheChildModel = setParent;

        // except: for products, do not cache instances outside primary home
        if (shouldCacheChildModel && ContentType.Product == childKey.type) {
            ContentKey primaryParentKey = CmsManager.getInstance().getPrimaryHomeKey(childKey);
            // is refModel equals to primary home cat? -> cache
            shouldCacheChildModel = ownerModel.getContentKey().equals(primaryParentKey);
        }
        if (shouldCacheChildModel) {
            ContentNodeModelImpl cachedContentNode = (ContentNodeModelImpl) ContentFactory.getInstance().getContentNodeByKey(childKey);
            if (cachedContentNode != null) {
                ContentNodeModel currentParentModel = cachedContentNode.getParentNode();
                if (currentParentModel == null) {
                    // no parent set yet -> set it and return model
                    cachedContentNode.setParentNode(ownerModel);
                    cachedContentNode.setPriority(ordinal);
                    return cachedContentNode;
                } else if (!currentParentModel.equals(ownerModel)) {
                    // different parent found
                    if (currentParentModel.getContentKey().type == ContentType.Product && ownerModel.getContentKey().equals(currentParentModel.getContentKey())) {
                        // if the parent is a product, it is possible that we have to construct child objects for not the primary product node.
                        // in that chase construct an object, but do not cache it
                        if (ownerModel instanceof ProductModelImpl && !((ProductModelImpl) ownerModel).isInPrimaryHome()) {
                            CategoryModel primaryHome = ((ProductModelImpl) ownerModel).getPrimaryHome();
                            LOGGER.debug("trying to construct child object of a product " + ownerModel.getContentKey() + ", which is in "
                                    + ownerModel.getParentNode().getContentKey() + " instead of the primary home:" + (primaryHome != null ? primaryHome.getContentKey() : null));
                        }
                        shouldCacheChildModel = false;
                    } else if (childKey.type == ContentType.ConfiguredProduct) {
                        LOGGER.warn("Configured product " + childKey + " already exists at " + currentParentModel.getContentKey() + ", instead of the expected "
                                + ownerModel.getContentKey());
                        shouldCacheChildModel = false;
                    } else if (childKey.type == ContentType.RecipeVariant && currentParentModel.getContentKey().type == ContentType.Recipe) {
                        LOGGER.warn(buildErrorMessage(ownerModel, childKey, cachedContentNode, currentParentModel));
                        shouldCacheChildModel = false;
                    } else {
                        // in strict mode we cannot accept tolerate this type of errors, temporally we have to relax this statement.
                        if (STRICT_MODE) {
                            throw new RuntimeException(buildErrorMessage(ownerModel, childKey, cachedContentNode, currentParentModel));
                        }
                        LOGGER.warn(buildErrorMessage(ownerModel, childKey, cachedContentNode, currentParentModel));
                    }
                } else {
                    if (setParent) {
                        cachedContentNode.setPriority(ordinal);
                    }
                    return cachedContentNode;
                }
            }
        }
        ContentNodeModelImpl nodeModel = null;
        if (setParent) {
            nodeModel = (ContentNodeModelImpl) constructModel(childKey, shouldCacheChildModel);
            if (nodeModel != null) {
                nodeModel.setPriority(ordinal);
                nodeModel.setParentNode(ownerModel);
            }
        } else {
            nodeModel = (ContentNodeModelImpl) ContentFactory.getInstance().getContentNodeByKey(childKey);
        }
        return nodeModel;
    }

    /**
     * @param refModel
     * @param key
     * @param cachedContentNodeByKey
     * @param parentNode
     * @return
     */
    private static String buildErrorMessage(ContentNodeModelImpl refModel, ContentKey key, ContentNodeModelImpl cachedContentNodeByKey, ContentNodeModel parentNode) {
        return "Content node already exists for key:" + key + ", node:" + cachedContentNodeByKey + ", hash:" + System.identityHashCode(cachedContentNodeByKey)
                + " but with different parent : " + parentNode.getContentKey() + '(' + System.identityHashCode(parentNode) + ", parents : "
                + ContentFactory.getInstance().getParentKeys(parentNode.getContentKey()) + ") \n\tinstead of the expected " + refModel.getContentKey() + "("
                + System.identityHashCode(refModel) + ") which parent are: " + ContentFactory.getInstance().getParentKeys(refModel.getContentKey());
    }

    private static boolean compareKeys(List<ContentKey> keys, List<? extends ContentNodeModel> models) {
        if (keys.size() != models.size())
            return false;

        for (int i = 0; i < keys.size(); i++) {
            ContentKey key = keys.get(i);
            ContentNodeModel model = models.get(i);
            if (key == null || model == null || !key.id.equals(model.getContentName()))
                return false;
        }
        return true;
    }

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

                if (hidden == null || "".equals(hidden)) {
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
            foundCategory = product.getPrimaryHome();
        }

        if (foundCategory == product.getParentNode()) {
            return null;
        }

        LOGGER.debug("Setting nearest parent for product " + product + " to " + foundCategory + " (was " + product.getParentNode() + "), in context " + context);

        ContentNodeModelImpl a = (ContentNodeModelImpl) product.clone();
        a.setParentNode(foundCategory);
        return (ProductModel) a;
    }

    public static void setNearestParentForProducts(ContentNodeModel context, List<ProductModel> products) {
        for (ListIterator<ProductModel> li = products.listIterator(); li.hasNext();) {
            ProductModel p = li.next();
            ProductModel clone = setNearestParentForProduct(context, p);
            if (clone != null) {
                li.set(clone);
            }
        }
    }

    public static Set<ContentKey> getAllParentKeys(ContentKey key) {
        return getAllParentKeys(key, false);
    }

    public static Set<ContentKey> getAllParentKeys(ContentKey key, boolean excludeNotSearchable) {
        Set<ContentKey> allParents = new HashSet<ContentKey>();
        loadAllParentKeys(key, allParents, excludeNotSearchable);
        return Collections.unmodifiableSet(allParents);
    }

    private static void loadAllParentKeys(ContentKey key, Set<ContentKey> allParents, boolean excludeNotSearchable) {
        Set<ContentKey> parentKeys = ContentFactory.getInstance().getParentKeys(key);
        if (parentKeys != null && !parentKeys.isEmpty()) {
            for (ContentKey parentKey : parentKeys) {
                ContentNodeModel parentNode = ContentFactory.getInstance().getContentNodeByKey(parentKey);
                if (!parentNode.isHidden() && !(excludeNotSearchable && !parentNode.isSearchable())) {
                    loadAllParentKeys(parentKey, allParents, excludeNotSearchable);
                    // Remove the parent nodes that are hidden.
                    allParents.add(parentKey);
                }
            }
        }
    }

    public static ContentKey getContentKey(String type, String contentId) {
        return ContentKeyFactory.get(ContentType.valueOf(type), contentId);
    }

    /**
     * This method returns the reference content key if the parameter contentId is a ALIAS category else returns null.
     *
     * @param key
     * @return ContentKey
     */
    public static ContentKey getAliasCategoryRef(String type, String contentId) {
        ContentKey refKey = null;
        ContentKey key = getContentKey(type, contentId);
        ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(key);
        // Make sure the category is not hidden.
        if (cn != null && !cn.isHidden() && cn instanceof CategoryModel) {
            CategoryModel cm = (CategoryModel) cn;
            refKey = cm.getAliasAttributeValue();
        }
        return refKey;
    }

    /**
     * Filters a set of contentkeys by checking if they have virtual categories assigned. Returns the actual categorymodels. NOTE: Does not return the virtual categories, only the
     * categories which have one assigned.
     *
     * @param contentKeys
     *            Set of ContentKeys to check for virtual categories
     * @return filtered Set of CategoryModels which have virtual categories
     */
    public static Set<CategoryModel> findVirtualCategories(Set<ContentKey> contentKeys, boolean loopEnabled) {
        Set<CategoryModel> s = new HashSet<CategoryModel>();
        for (ContentKey key : contentKeys) {
            ContentNodeModel cn = ContentFactory.getInstance().getContentNodeByKey(key);
            // Make sure the category is not hidden.
            if (cn != null && cn instanceof CategoryModel && !cn.isHidden()) {
                CategoryModel cm = (CategoryModel) cn;
                if (cm.getVirtualGroupRefs().size() > 0) {
                    s.add(cm);
                }
                // Check whether this category has recommender's then add to the list.
                Recommender rec = cm.getRecommender();
                if (rec != null) {
                    // This category has a recommender
                    LOGGER.debug("Found the recommender for contentkey:" + cn.getContentName() + "|recs:" + rec.getFullName());
                    s.add(cm);
                }

                // Based on the boolean flag to check children, get children of this category and check for each childrean whether they have recommenders and if so, add to the
                // list.
                if (loopEnabled) {
                    // getChildren
                    Set<ContentKey> childKeys = cm.getAllChildProductKeys();
                    findVirtualCategories(childKeys, true);
                }
            }
        }
        return s;
    }

    public static boolean isProductInVirtualCategories(Set<CategoryModel> virtualCats, ProductModel prod) {
        boolean found = false;
        for (CategoryModel virtualCategory : virtualCats) {
            List<ProductModel> allProducts = virtualCategory.getProducts();
            found = allProducts.contains(prod);
            if (found) {
                break;
            }
        }
        return found;
    }

    /**
     * Return true, if the given parameter is null or has 0 length.
     *
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

    /**
     * Find department node by climbing up the parent chain
     *
     * @param model
     * @return
     */
    public static ContentNodeModel findDepartment(ContentNodeModel model) {
        while (model != null) {
            if (ContentType.Department == model.getContentKey().type) {
                return model;
            }

            model = model.getParentNode();
        }
        return null;
    }
}
