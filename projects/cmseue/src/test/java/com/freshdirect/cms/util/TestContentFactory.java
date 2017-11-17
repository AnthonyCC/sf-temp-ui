package com.freshdirect.cms.util;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.*;
import static com.google.common.collect.Lists.newArrayList;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.google.common.collect.ImmutableMap;

public final class TestContentFactory {

    private TestContentFactory() {
    }

    public static Map<ContentKey, Map<Attribute, Object>> getTestContent() {
        Map<ContentKey, Map<Attribute, Object>> nodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        // store
        nodes.put(RootContentKey.STORE_FRESHDIRECT.contentKey, ImmutableMap.<Attribute, Object>of(
            ContentTypes.Store.departments, newArrayList(
                get(Department, "dept1"),
                get(Department, "dept2"),
                get(Department, "dept3")
            )
        ));

        // departments
        nodes.put(get(Department, "dept1"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Department.FULL_NAME, "Alpha",
            ContentTypes.Department.categories, newArrayList(
                get(Category, "cat_2"),
                get(Category, "cat_a_valid"),
                get(Category, "cat_a_invalid")
            )
        ));
        nodes.put(get(Department, "dept2"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Department.FULL_NAME, "Beta",
            ContentTypes.Department.categories, newArrayList(
                get(Category, "cat_hdn"),
                get(Category, "cat_nosearch"),
                get(Category, "cat_hdn_nosearch")
            )
        ));
        nodes.put(get(Department, "dept3"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Department.FULL_NAME, "Gamme",
            ContentTypes.Department.categories, newArrayList(
                get(Category, "cat_g_valid"),
                get(Category, "cat_g_invalid")
            )
        ));

        // categories
        nodes.put(get(Category, "cat_orphan"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Orphaned Category",
            ContentTypes.Category.subcategories, newArrayList(
                get(Category, "cat_1")
            )
        ));
        nodes.put(get(Category, "cat_1"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category #1",
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_1")
            )
        ));
        nodes.put(get(Category, "cat_2"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category #2",
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_1"),
                get(Product, "prd_2"),
                get(Product, "prd_3"),
                get(Product, "prd_4")
            )
        ));
        nodes.put(get(Category, "cat_hdn"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Hidden Category",
            ContentTypes.Product.HIDE_URL, "/index.jsp",
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_2")
            )
        ));
        nodes.put(get(Category, "cat_nosearch"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Not Searchable Category",
            ContentTypes.Product.NOT_SEARCHABLE, Boolean.TRUE,
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_3")
            )
        ));
        nodes.put(get(Category, "cat_hdn_nosearch"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Hidden And Not Searchable Category",
            ContentTypes.Product.HIDE_URL, "/index.jsp",
            ContentTypes.Product.NOT_SEARCHABLE, Boolean.TRUE,
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_3")
            )
        ));

        nodes.put(get(Category, "cat_a_valid"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category Alpha Valid (1)",
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_r")
            )
        ));
        nodes.put(get(Category, "cat_a_invalid"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category Alpha Invalid (1)",
            ContentTypes.Product.HIDE_URL, "/index.jsp",
            ContentTypes.Product.NOT_SEARCHABLE, Boolean.TRUE,
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_r")
            )
        ));

        nodes.put(get(Category, "cat_g_valid"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category Gamma Valid (1)",
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_r")
            )
        ));
        nodes.put(get(Category, "cat_g_invalid"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Category.FULL_NAME, "Category Gamma Invalid (1)",
            ContentTypes.Product.HIDE_URL, "/index.jsp",
            ContentTypes.Product.NOT_SEARCHABLE, Boolean.TRUE,
            ContentTypes.Category.products, newArrayList(
                get(Product, "prd_r")
            )
        ));

        // products
        nodes.put(get(Product, "prd_1"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Product #1",
            ContentTypes.Product.PRIMARY_HOME, newArrayList(
                get(Category, "cat_1")
            )
        ));
        nodes.put(get(Product, "new_prd"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "New Product"
        ));
        nodes.put(get(Product, "prd_2"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Product #2",
            ContentTypes.Product.PRIMARY_HOME, newArrayList(
                get(Category, "cat_hdn")
            )
        ));
        nodes.put(get(Product, "prd_3"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Product #3",
            ContentTypes.Product.PRIMARY_HOME, newArrayList(
                get(Category, "cat_nosearch")
            )
        ));
        nodes.put(get(Product, "prd_4"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Product #4",
            ContentTypes.Product.PRIMARY_HOME, newArrayList(
                get(Category, "cat_hdn_nosearch")
            )
        ));
        nodes.put(get(Product, "prd_r"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Product to test Ranking"
        ));

        // -- //
        nodes.put(get(FDFolder, "recipes"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Recipes",
            ContentTypes.FDFolder.children, newArrayList(
            get(RecipeDepartment, "rec")
            )
        ));

        nodes.put(get(RecipeDepartment, "rec"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Recipes",
            ContentTypes.RecipeDepartment.categories, newArrayList(get(RecipeCategory, "rec_now"))
        ));

        nodes.put(get(RecipeCategory, "rec_now"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "What's Cooking Now",
            ContentTypes.RecipeCategory.subcategories, newArrayList(
            get(RecipeSubcategory, "rec_now_season")
            )
        ));

        nodes.put(get(RecipeSubcategory, "rec_now_season"), ImmutableMap.<Attribute, Object>of(
            ContentTypes.Product.FULL_NAME, "Winter Recipes"
        ));

        return nodes;
    }
}
