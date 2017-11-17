package com.freshdirect.cms.mediaassociation.domain;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;

public enum MediaAssociationRule {

    PRODUCT_IMAGE(null, "c", ContentType.Product, ContentTypes.Product.PROD_IMAGE),
    PRODUCT_IMAGE_CONFIRM(null, "cx", ContentType.Product, ContentTypes.Product.PROD_IMAGE_CONFIRM),
    PRODUCT_IMAGE_DETAIL(null, "p", ContentType.Product, ContentTypes.Product.PROD_IMAGE_DETAIL),
    PRODUCT_IMAGE_FEATURE(null, "f", ContentType.Product, ContentTypes.Product.PROD_IMAGE_FEATURE),
    PRODUCT_IMAGE_ROLLOVER(null, "cr", ContentType.Product, ContentTypes.Product.PROD_IMAGE_ROLLOVER),
    PRODUCT_IMAGE_ZOOM(null, "z", ContentType.Product, ContentTypes.Product.PROD_IMAGE_ZOOM),
    PRODUCT_DESCR(null, "desc", ContentType.Product, ContentTypes.Product.PROD_DESCR),
    PRODUCT_IMAGE_PACKAGE(null, "b", ContentType.Product, ContentTypes.Product.PROD_IMAGE_PACKAGE),
    PRODUCT_ALTERNATE_IMAGE(null, "a", ContentType.Product, ContentTypes.Product.ALTERNATE_IMAGE),
    PRODUCT_DESCRIPTIVE_IMAGE(null, "d", ContentType.Product, ContentTypes.Product.DESCRIPTIVE_IMAGE),
    PRODUCT_DESCRIPTION_NOTE(null, "note", ContentType.Product, ContentTypes.Product.PROD_DESCRIPTION_NOTE),
    PRODUCT_ABOUT(null, "abt", ContentType.Product, ContentTypes.Product.PRODUCT_ABOUT),
    PRODUCT_IMAGE_JUMBO(null, "j", ContentType.Product, ContentTypes.Product.PROD_IMAGE_JUMBO),
    PRODUCT_IMAGE_ITEM(null, "i", ContentType.Product, ContentTypes.Product.PROD_IMAGE_ITEM),
    PRODUCT_IMAGE_EXTRA(null, "e", ContentType.Product, ContentTypes.Product.PROD_IMAGE_EXTRA),

    BRAND_LOGO("bd", "l", ContentType.Brand, ContentTypes.Brand.BRAND_LOGO),
    BRAND_LOGO_MEDIUM("bd", "m", ContentType.Brand, ContentTypes.Brand.BRAND_LOGO_MEDIUM),
    BRAND_LOGO_SMALL("bd", "s", ContentType.Brand, ContentTypes.Brand.BRAND_LOGO_SMALL),

    RECIPTE_TITLE_IMAGE("rec", "c", ContentType.Recipe, ContentTypes.Recipe.titleImage),
    RECIPE_PHOTO("rec", "p", ContentType.Recipe, ContentTypes.Recipe.photo),
    RECIPE_DESCRIPTION("rec", "hn", ContentType.Recipe, ContentTypes.Recipe.description),
    RECIPE_INGREDIENTS_MEDIA("rec", "in", ContentType.Recipe, ContentTypes.Recipe.ingredientsMedia),
    RECIPE_PREPARATION_MEDIA("rec", "pr", ContentType.Recipe, ContentTypes.Recipe.preparationMedia),
    RECIPE_COPYRIGHT_MEDIA("rec", "cr", ContentType.Recipe, ContentTypes.Recipe.copyrightMedia),

    RECIPESOURCE_IMAGE("cbk", "c", ContentType.RecipeSource, ContentTypes.RecipeSource.image),
    RECIPESOURCE_ZOOM_IMAGE("cbk", "p", ContentType.RecipeSource, ContentTypes.RecipeSource.zoomImage),
    RECIPESOURCE_LEFT_CONTENT("cbk", "cl", ContentType.RecipeSource, ContentTypes.RecipeSource.leftContent),
    RECIPESOURCE_TOP_CONTENT("cbk", "ct", ContentType.RecipeSource, ContentTypes.RecipeSource.topContent),
    RECIPESOURCE_BOTTOM_CONTENT("cbk", "cb", ContentType.RecipeSource, ContentTypes.RecipeSource.bottomContent);

    private String prefix;
    private String suffix;
    private ContentType contentType;
    private Attribute relationship;

    MediaAssociationRule(String prefix, String suffix, ContentType contentType, Attribute relationship) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.contentType = contentType;
        this.relationship = relationship;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Attribute getRelationship() {
        return relationship;
    }
}
