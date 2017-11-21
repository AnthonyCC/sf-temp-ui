package com.freshdirect.cms.core.domain;

import static com.freshdirect.cms.core.domain.ContentType.Anchor;
import static com.freshdirect.cms.core.domain.ContentType.Banner;
import static com.freshdirect.cms.core.domain.ContentType.BookRetailer;
import static com.freshdirect.cms.core.domain.ContentType.Brand;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.CategorySection;
import static com.freshdirect.cms.core.domain.ContentType.ComponentGroup;
import static com.freshdirect.cms.core.domain.ContentType.ConfiguredProduct;
import static com.freshdirect.cms.core.domain.ContentType.ConfiguredProductGroup;
import static com.freshdirect.cms.core.domain.ContentType.DarkStore;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Domain;
import static com.freshdirect.cms.core.domain.ContentType.DomainValue;
import static com.freshdirect.cms.core.domain.ContentType.DonationOrganization;
import static com.freshdirect.cms.core.domain.ContentType.ErpCharacteristic;
import static com.freshdirect.cms.core.domain.ContentType.ErpMaterial;
import static com.freshdirect.cms.core.domain.ContentType.FAQ;
import static com.freshdirect.cms.core.domain.ContentType.FDFolder;
import static com.freshdirect.cms.core.domain.ContentType.FavoriteList;
import static com.freshdirect.cms.core.domain.ContentType.GlobalMenuItem;
import static com.freshdirect.cms.core.domain.ContentType.GlobalMenuSection;
import static com.freshdirect.cms.core.domain.ContentType.GlobalNavigation;
import static com.freshdirect.cms.core.domain.ContentType.HolidayGreeting;
import static com.freshdirect.cms.core.domain.ContentType.Html;
import static com.freshdirect.cms.core.domain.ContentType.Image;
import static com.freshdirect.cms.core.domain.ContentType.ImageBanner;
import static com.freshdirect.cms.core.domain.ContentType.MediaFolder;
import static com.freshdirect.cms.core.domain.ContentType.Module;
import static com.freshdirect.cms.core.domain.ContentType.ModuleContainer;
import static com.freshdirect.cms.core.domain.ContentType.ModuleGroup;
import static com.freshdirect.cms.core.domain.ContentType.MyFD;
import static com.freshdirect.cms.core.domain.ContentType.Page;
import static com.freshdirect.cms.core.domain.ContentType.PickList;
import static com.freshdirect.cms.core.domain.ContentType.PickListItem;
import static com.freshdirect.cms.core.domain.ContentType.Producer;
import static com.freshdirect.cms.core.domain.ContentType.ProducerType;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.ContentType.ProductFilter;
import static com.freshdirect.cms.core.domain.ContentType.ProductFilterGroup;
import static com.freshdirect.cms.core.domain.ContentType.ProductFilterMultiGroup;
import static com.freshdirect.cms.core.domain.ContentType.ProductGrabber;
import static com.freshdirect.cms.core.domain.ContentType.Recipe;
import static com.freshdirect.cms.core.domain.ContentType.RecipeAuthor;
import static com.freshdirect.cms.core.domain.ContentType.RecipeCategory;
import static com.freshdirect.cms.core.domain.ContentType.RecipeDepartment;
import static com.freshdirect.cms.core.domain.ContentType.RecipeSearchCriteria;
import static com.freshdirect.cms.core.domain.ContentType.RecipeSearchPage;
import static com.freshdirect.cms.core.domain.ContentType.RecipeSection;
import static com.freshdirect.cms.core.domain.ContentType.RecipeSource;
import static com.freshdirect.cms.core.domain.ContentType.RecipeSubcategory;
import static com.freshdirect.cms.core.domain.ContentType.RecipeTag;
import static com.freshdirect.cms.core.domain.ContentType.RecipeVariant;
import static com.freshdirect.cms.core.domain.ContentType.Recommender;
import static com.freshdirect.cms.core.domain.ContentType.RecommenderStrategy;
import static com.freshdirect.cms.core.domain.ContentType.Schedule;
import static com.freshdirect.cms.core.domain.ContentType.SearchRelevancyHint;
import static com.freshdirect.cms.core.domain.ContentType.SearchRelevancyList;
import static com.freshdirect.cms.core.domain.ContentType.SearchSuggestionGroup;
import static com.freshdirect.cms.core.domain.ContentType.Section;
import static com.freshdirect.cms.core.domain.ContentType.Sku;
import static com.freshdirect.cms.core.domain.ContentType.SortOption;
import static com.freshdirect.cms.core.domain.ContentType.SpellingSynonym;
import static com.freshdirect.cms.core.domain.ContentType.StarterList;
import static com.freshdirect.cms.core.domain.ContentType.SuperDepartment;
import static com.freshdirect.cms.core.domain.ContentType.Synonym;
import static com.freshdirect.cms.core.domain.ContentType.Tag;
import static com.freshdirect.cms.core.domain.ContentType.TextComponent;
import static com.freshdirect.cms.core.domain.ContentType.Tile;
import static com.freshdirect.cms.core.domain.ContentType.TileList;
import static com.freshdirect.cms.core.domain.ContentType.WebPage;
import static com.freshdirect.cms.core.domain.ContentType.WordStemmingException;
import static com.freshdirect.cms.core.domain.ContentType.YmalSet;
import static com.freshdirect.cms.core.domain.ContentType.YoutubeVideo;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.attribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.booleanAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.dateAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.doubleAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.integerAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.integerEnum;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.linkManyOf;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.linkOneOf;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.stringAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.stringEnum;

public final class ContentTypes {
    public static final class FDFolder {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute children = linkManyOf(Anchor, Banner, BookRetailer, Brand, ConfiguredProduct, ConfiguredProductGroup, DarkStore, Domain, DonationOrganization,
                FAQ, FDFolder, FavoriteList, GlobalMenuItem, ImageBanner, Module, ModuleContainer, ModuleGroup, PickList, PickListItem, Producer, ProducerType, ProductFilter,
                ProductFilterGroup, ProductFilterMultiGroup, Recipe, RecipeAuthor, RecipeDepartment, RecipeSearchPage, RecipeSource, RecipeTag, Recommender, RecommenderStrategy,
                Schedule, SearchRelevancyList, SearchSuggestionGroup, Section, SortOption, SpellingSynonym, StarterList, Synonym, Tag, TextComponent, Tile, TileList, WebPage,
                WordStemmingException, YmalSet, YoutubeVideo).toName("children")
            .navigable()
            .build();
    }

    public static final class DonationOrganization {
        public static final Attribute ORGANIZATION_NAME = stringAttribute("ORGANIZATION_NAME")
            .build();
        public static final Attribute EMAIL = stringAttribute("EMAIL")
            .build();
        public static final Attribute CONTACT_INFO = stringAttribute("CONTACT_INFO")
            .build();
        public static final Attribute ORGANIZATION_LOGO_SMALL = linkOneOf(Image).toName("ORGANIZATION_LOGO_SMALL")
            .build();
        public static final Attribute ORGANIZATION_LOGO = linkOneOf(Image).toName("ORGANIZATION_LOGO")
            .build();
        public static final Attribute ORGANIZATION_RECIEPT_LOGO = linkOneOf(Image).toName("ORGANIZATION_RECIEPT_LOGO")
            .build();
        public static final Attribute GIFTCARD_TYPE = linkManyOf(DomainValue).toName("GIFTCARD_TYPE")
            .build();
        public static final Attribute EDITORIAL_MAIN = linkOneOf(Html).toName("EDITORIAL_MAIN")
            .build();
        public static final Attribute EDITORIAL_HEADER_MEDIA = linkOneOf(Html).toName("EDITORIAL_HEADER_MEDIA")
            .build();
        public static final Attribute EDITORIAL_DETAIL = linkOneOf(Html).toName("EDITORIAL_DETAIL")
            .build();
        public static final Attribute EDITORIAL_RECEIPT_MEDIA = linkOneOf(Html).toName("EDITORIAL_RECEIPT_MEDIA")
            .build();
    }

    public static final class FAQ {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute QUESTION = stringAttribute("QUESTION")
            .build();
        public static final Attribute ANSWER = stringAttribute("ANSWER")
            .build();
        public static final Attribute KEYWORDS = stringAttribute("KEYWORDS")
            .build();
        public static final Attribute PRIORITY_LIST = stringAttribute("PRIORITY_LIST")
            .build();
    }

    public static final class Store {
        public static final Attribute NAME = stringAttribute("NAME")
            .build();
        public static final Attribute PREVIEW_HOST_NAME = stringAttribute("PREVIEW_HOST_NAME")
            .build();
        public static final Attribute folders = linkManyOf(FDFolder).toName("folders")
            .navigable()
            .build();
        public static final Attribute departments = linkManyOf(Department).toName("departments")
            .navigable()
            .build();
        public static final Attribute HOME_FEATURED_PRODUCTS = linkManyOf(Product).toName("HOME_FEATURED_PRODUCTS")
            .build();
        public static final Attribute myFD = linkManyOf(MyFD).toName("myFD")
            .navigable()
            .build();
        public static final Attribute EDITORIAL = linkOneOf(Html).toName("EDITORIAL")
            .build();
        public static final Attribute pages = linkManyOf(Page).toName("pages")
            .navigable()
            .build();
        public static final Attribute superDepartments = linkManyOf(SuperDepartment).toName("superDepartments")
            .navigable()
            .build();
        public static final Attribute globalNavigations = linkManyOf(GlobalNavigation).toName("globalNavigations")
            .navigable()
            .build();
        public static final Attribute searchPageSortOptions = linkManyOf(SortOption).toName("searchPageSortOptions")
            .build();
        public static final Attribute newProductsPageSortOptions = linkManyOf(SortOption).toName("newProductsPageSortOptions")
            .build();
        public static final Attribute presidentsPicksPageSortOptions = linkManyOf(SortOption).toName("presidentsPicksPageSortOptions")
            .build();
        public static final Attribute staffPicksPageSortOptions = linkManyOf(SortOption).toName("staffPicksPageSortOptions")
            .build();
        public static final Attribute eCouponsPageSortOptions = linkManyOf(SortOption).toName("eCouponsPageSortOptions")
            .build();
        public static final Attribute eCouponsPageTopMediaBanner = linkOneOf(Html).toName("eCouponsPageTopMediaBanner")
            .build();
        public static final Attribute searchPageTopMediaBanner = linkOneOf(Html).toName("searchPageTopMediaBanner")
            .build();
        public static final Attribute newProductsPageTopMediaBanner = linkOneOf(Html).toName("newProductsPageTopMediaBanner")
            .build();
        public static final Attribute presPicksPageTopMediaBanner = linkOneOf(Html).toName("presPicksPageTopMediaBanner")
            .build();
        public static final Attribute staffPicksPageTopMediaBanner = linkOneOf(Html).toName("staffPicksPageTopMediaBanner")
            .build();
        public static final Attribute tabletFeaturedCategories = linkManyOf(Category).toName("tabletFeaturedCategories")
            .build();
        public static final Attribute tabletSearchSuggestionGroups = linkManyOf(SearchSuggestionGroup).toName("tabletSearchSuggestionGroups")
            .build();
        public static final Attribute tabletIdeasBanner = linkOneOf(Banner).toName("tabletIdeasBanner")
            .build();
        public static final Attribute tabletIdeasFeaturedPicksLists = linkManyOf(Category).toName("tabletIdeasFeaturedPicksLists")
            .build();
        public static final Attribute iPhoneHomePagePicksLists = linkManyOf(Category).toName("iPhoneHomePagePicksLists")
            .build();
        public static final Attribute iPhoneHomePageImageBanners = linkManyOf(ImageBanner).toName("iPhoneHomePageImageBanners")
            .build();
        public static final Attribute tabletIdeasRecipeTags = linkManyOf(RecipeTag).toName("tabletIdeasRecipeTags")
            .build();
        public static final Attribute tabletHomeScreenPopUpShopBanners = linkManyOf(Banner).toName("tabletHomeScreenPopUpShopBanners")
            .build();
        public static final Attribute tabletIdeasBrands = linkManyOf(Brand).toName("tabletIdeasBrands")
            .build();
        public static final Attribute tabletIdeasRecipes = linkManyOf(Recipe, RecipeTag).toName("tabletIdeasRecipes")
            .build();
        public static final Attribute expressCheckoutReceiptHeader = linkOneOf(Html).toName("expressCheckoutReceiptHeader")
            .build();
        public static final Attribute expressCheckoutReceiptEditorial = linkOneOf(Html).toName("expressCheckoutReceiptEditorial")
            .build();
        public static final Attribute expressCheckoutTextMessageAlertHeader = linkOneOf(Html).toName("expressCheckoutTextMessageAlertHeader")
            .build();
        public static final Attribute welcomeCarouselImageBanners = linkManyOf(ImageBanner).toName("welcomeCarouselImageBanners")
            .build();
    }

    public static final class SuperDepartment {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute browseName = stringAttribute("browseName")
            .build();
        public static final Attribute sdFeaturedRecommenderTitle = stringAttribute("sdFeaturedRecommenderTitle")
            .build();
        public static final Attribute sdFeaturedRecommenderRandomizeProducts = booleanAttribute("sdFeaturedRecommenderRandomizeProducts")
            .build();
        public static final Attribute sdFeaturedRecommenderSiteFeature = stringAttribute("sdFeaturedRecommenderSiteFeature")
            .build();
        public static final Attribute sdMerchantRecommenderTitle = stringAttribute("sdMerchantRecommenderTitle")
            .build();
        public static final Attribute sdMerchantRecommenderRandomizeProducts = booleanAttribute("sdMerchantRecommenderRandomizeProducts")
            .build();
        public static final Attribute hideGlobalNavDropDown = booleanAttribute("hideGlobalNavDropDown")
            .build();
        public static final Attribute SEO_META_DESC = stringAttribute("SEO_META_DESC")
            .build();
        public static final Attribute PAGE_TITLE = stringAttribute("PAGE_TITLE")
            .build();
        public static final Attribute SEO_META_DESC_FDX = stringAttribute("SEO_META_DESC_FDX")
            .build();
        public static final Attribute PAGE_TITLE_FDX = stringAttribute("PAGE_TITLE_FDX")
            .build();
        public static final Attribute SKIP_SITEMAP = booleanAttribute("SKIP_SITEMAP")
                .build();
        public static final Attribute bannerLocation = stringEnum("bannerLocation")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute carouselPosition = stringEnum("carouselPosition")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute carouselRatio = stringEnum("carouselRatio")
            .withValues("FULL_WIDTH", "TWO_TWO", "THREE_ONE")
            .inheritable()
            .build();
        public static final Attribute departments = linkManyOf(Department).toName("departments")
            .build();
        public static final Attribute titleBar = linkOneOf(Image).toName("titleBar")
            .build();
        public static final Attribute superDepartmentBanner = linkOneOf(Html).toName("superDepartmentBanner")
            .build();
        public static final Attribute middleMedia = linkOneOf(Html).toName("middleMedia")
            .build();
        public static final Attribute sdFeaturedRecommenderSourceCategory = linkOneOf(Category).toName("sdFeaturedRecommenderSourceCategory")
            .build();
        public static final Attribute sdMerchantRecommenderProducts = linkManyOf(Product).toName("sdMerchantRecommenderProducts")
            .build();
        public static final Attribute catalog = stringEnum("catalog")
            .withValues("ALL", "RESIDENTAL", "CORPORATE")
            .required()
            .build();
    }

    public static final class GlobalNavigation {
        public static final Attribute items = linkManyOf(Department, SuperDepartment).toName("items")
            .required()
            .build();
        public static final Attribute media = linkOneOf(Html).toName("media")
            .required()
            .build();
    }

    public static final class CategorySection {
        public static final Attribute headline = stringAttribute("headline")
            .build();
        public static final Attribute insertColumnBreak = booleanAttribute("insertColumnBreak")
            .build();
        public static final Attribute selectedCategories = linkManyOf(Category).toName("selectedCategories")
            .required()
            .build();
    }

    public static final class Domain {
        public static final Attribute NAME = stringAttribute("NAME")
            .build();
        public static final Attribute Label = stringAttribute("Label")
            .build();
        public static final Attribute domainValues = linkManyOf(DomainValue).toName("domainValues")
            .navigable()
            .build();
    }

    public static final class DomainValue {
        public static final Attribute Label = stringAttribute("Label")
            .build();
        public static final Attribute VALUE = stringAttribute("VALUE")
            .build();
    }

    public static final class Department {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute ALT_TEXT = stringAttribute("ALT_TEXT")
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute MAX_ROWCOUNT = integerAttribute("MAX_ROWCOUNT")
            .build();
        public static final Attribute PRIORITY = integerAttribute("PRIORITY")
            .build();
        public static final Attribute USE_ALTERNATE_IMAGES = booleanAttribute("USE_ALTERNATE_IMAGES")
            .inheritable()
            .build();
        public static final Attribute HIDE_IN_QUICKSHOP = booleanAttribute("HIDE_IN_QUICKSHOP")
            .build();
        public static final Attribute DEPARTMENT_TEMPLATE_PATH = stringAttribute("DEPARTMENT_TEMPLATE_PATH")
            .build();
        public static final Attribute DEPARTMENT_ALT_TEMPLATE_PATH = stringAttribute("DEPARTMENT_ALT_TEMPLATE_PATH")
            .build();
        public static final Attribute GLOBAL_MENU_TITLE_LABEL = stringAttribute("GLOBAL_MENU_TITLE_LABEL")
            .build();
        public static final Attribute GLOBAL_MENU_LINK_LABEL = stringAttribute("GLOBAL_MENU_LINK_LABEL")
            .build();
        public static final Attribute noGroupingByCategory = booleanAttribute("noGroupingByCategory")
            .inheritable()
            .build();
        public static final Attribute showAllByDefault = booleanAttribute("showAllByDefault")
            .inheritable()
            .build();
        public static final Attribute expand2ndLowestNavigationBox = booleanAttribute("expand2ndLowestNavigationBox")
            .inheritable()
            .build();
        public static final Attribute disableAtpFailureRecommendation = booleanAttribute("disableAtpFailureRecommendation")
            .inheritable()
            .build();
        public static final Attribute featuredRecommenderTitle = stringAttribute("featuredRecommenderTitle")
            .build();
        public static final Attribute featuredRecommenderRandomizeProducts = booleanAttribute("featuredRecommenderRandomizeProducts")
            .build();
        public static final Attribute featuredRecommenderSiteFeature = stringAttribute("featuredRecommenderSiteFeature")
            .build();
        public static final Attribute merchantRecommenderTitle = stringAttribute("merchantRecommenderTitle")
            .build();
        public static final Attribute merchantRecommenderRandomizeProducts = booleanAttribute("merchantRecommenderRandomizeProducts")
            .build();
        public static final Attribute showPopularCategories = booleanAttribute("showPopularCategories")
            .build();
        public static final Attribute globalNavShopAllText = stringAttribute("globalNavShopAllText")
            .build();
        public static final Attribute regularCategoriesNavHeader = stringAttribute("regularCategoriesNavHeader")
            .build();
        public static final Attribute preferenceCategoriesNavHeader = stringAttribute("preferenceCategoriesNavHeader")
            .build();
        public static final Attribute regularCategoriesLeftNavBoxHeader = stringAttribute("regularCategoriesLeftNavBoxHeader")
            .build();
        public static final Attribute preferenceCategoriesLeftNavBoxHeader = stringAttribute("preferenceCategoriesLeftNavBoxHeader")
            .build();
        public static final Attribute maxItemsPerColumn = integerAttribute("maxItemsPerColumn")
            .build();
        public static final Attribute showCatSectionHeaders = booleanAttribute("showCatSectionHeaders")
            .build();
        public static final Attribute globalNavName = stringAttribute("globalNavName")
            .build();
        public static final Attribute hideGlobalNavDropDown = booleanAttribute("hideGlobalNavDropDown")
            .build();
        public static final Attribute disableCategoryYmalRecommender = booleanAttribute("disableCategoryYmalRecommender")
            .inheritable()
            .build();
        public static final Attribute SEO_META_DESC = stringAttribute("SEO_META_DESC")
            .build();
        public static final Attribute PAGE_TITLE = stringAttribute("PAGE_TITLE")
            .build();
        public static final Attribute SEO_META_DESC_FDX = stringAttribute("SEO_META_DESC_FDX")
            .build();
        public static final Attribute PAGE_TITLE_FDX = stringAttribute("PAGE_TITLE_FDX")
            .build();
        public static final Attribute SKIP_SITEMAP = booleanAttribute("SKIP_SITEMAP")
            .build();
        public static final Attribute BOTTOM_RIGHT_DISPLAY = integerEnum("BOTTOM_RIGHT_DISPLAY")
            .withValues(1, 2, 26)
            .build();
        public static final Attribute browseRecommenderType = stringEnum("browseRecommenderType")
            .withValues("NONE", "PDP_XSELL", "PDP_UPSELL")
            .inheritable()
            .build();
        public static final Attribute carouselPosition = stringEnum("carouselPosition")
            .withValues("TOP", "BOTTOM")
            .build();
        public static final Attribute carouselRatio = stringEnum("carouselRatio")
            .withValues("FULL_WIDTH", "TWO_TWO", "THREE_ONE")
            .build();
        public static final Attribute bannerLocation = stringEnum("bannerLocation")
            .withValues("TOP", "BOTTOM")
            .build();
        public static final Attribute categories = linkManyOf(Category).toName("categories")
            .navigable()
            .build();
        public static final Attribute FEATURED_CATEGORIES = linkManyOf(Category).toName("FEATURED_CATEGORIES")
            .build();
        public static final Attribute FEATURED_PRODUCTS = linkManyOf(ConfiguredProduct, Product).toName("FEATURED_PRODUCTS")
            .build();
        public static final Attribute DEPT_NAV = linkManyOf(Category).toName("DEPT_NAV")
            .build();
        public static final Attribute DEPT_MGR_NONAME = linkOneOf(Image).toName("DEPT_MGR_NONAME")
            .build();
        public static final Attribute DEPT_PHOTO = linkOneOf(Image).toName("DEPT_PHOTO")
            .build();
        public static final Attribute DEPT_PHOTO_SMALL = linkOneOf(Image).toName("DEPT_PHOTO_SMALL")
            .build();
        public static final Attribute DEPT_TITLE = linkOneOf(Image).toName("DEPT_TITLE")
            .build();
        public static final Attribute DEPARTMENT_MIDDLE_MEDIA = linkManyOf(Html).toName("DEPARTMENT_MIDDLE_MEDIA")
            .build();
        public static final Attribute DEPT_NAVBAR_ROLLOVER = linkOneOf(Image).toName("DEPT_NAVBAR_ROLLOVER")
            .build();
        public static final Attribute DEPT_NAVBAR = linkOneOf(Image).toName("DEPT_NAVBAR")
            .build();
        public static final Attribute EDITORIAL = linkOneOf(Html).toName("EDITORIAL")
            .build();
        public static final Attribute DEPT_MGR_BIO = linkOneOf(Html).toName("DEPT_MGR_BIO")
            .build();
        public static final Attribute DEPT_STORAGE_GUIDE_MEDIA = linkOneOf(Html).toName("DEPT_STORAGE_GUIDE_MEDIA")
            .build();
        public static final Attribute ASSOC_EDITORIAL = linkManyOf(Html).toName("ASSOC_EDITORIAL")
            .build();
        public static final Attribute DEPARTMENT_BOTTOM = linkManyOf(Html).toName("DEPARTMENT_BOTTOM")
            .build();
        public static final Attribute tile_list = linkManyOf(TileList).toName("tile_list")
            .build();
        public static final Attribute productFilterGroups = linkManyOf(ProductFilterGroup, ProductFilterMultiGroup).toName("productFilterGroups")
            .inheritable()
            .build();
        public static final Attribute popularCategories = linkManyOf(Category).toName("popularCategories")
            .build();
        public static final Attribute titleBar = linkOneOf(Image).toName("titleBar")
            .build();
        public static final Attribute productTags = linkManyOf(Tag).toName("productTags")
            .inheritable()
            .build();
        public static final Attribute categoryBanner = linkOneOf(Html).toName("categoryBanner")
            .inheritable()
            .build();
        public static final Attribute departmentBanner = linkOneOf(Html).toName("departmentBanner")
            .build();
        public static final Attribute middleMedia = linkOneOf(Html).toName("middleMedia")
            .build();
        public static final Attribute featuredRecommenderSourceCategory = linkOneOf(Category).toName("featuredRecommenderSourceCategory")
            .build();
        public static final Attribute merchantRecommenderProducts = linkManyOf(Product).toName("merchantRecommenderProducts")
            .build();
        public static final Attribute globalNavFeaturedCategory = linkOneOf(Category).toName("globalNavFeaturedCategory")
            .build();
        public static final Attribute globalNavFeaturedPreferenceCategory = linkOneOf(Category).toName("globalNavFeaturedPreferenceCategory")
            .build();
        public static final Attribute heroImage = linkOneOf(Image).toName("heroImage")
            .build();
        public static final Attribute seasonalMedia = linkOneOf(Html).toName("seasonalMedia")
            .build();
        public static final Attribute categorySections = linkManyOf(CategorySection).toName("categorySections")
            .navigable()
            .build();
        public static final Attribute tabletCallToActionBanner = linkOneOf(Banner).toName("tabletCallToActionBanner")
            .inheritable()
            .build();
        public static final Attribute tabletNoPurchaseSuggestions = linkManyOf(Banner).toName("tabletNoPurchaseSuggestions")
            .build();
        public static final Attribute tabletIcon = linkOneOf(Image).toName("tabletIcon")
            .build();
        public static final Attribute tabletHeaderBanner = linkOneOf(Banner).toName("tabletHeaderBanner")
            .build();
        public static final Attribute heroCarousel = linkManyOf(ImageBanner).toName("heroCarousel")
            .build();
        public static final Attribute catalog = stringEnum("catalog")
            .withValues("ALL", "RESIDENTAL", "CORPORATE")
            .required()
            .build();
    }

    public static final class Category {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute ALT_TEXT = stringAttribute("ALT_TEXT")
            .build();
        public static final Attribute KEYWORDS = stringAttribute("KEYWORDS")
            .build();
        public static final Attribute EDITORIAL_TITLE = stringAttribute("EDITORIAL_TITLE")
            .inheritable()
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute CONTAINS_BEER = booleanAttribute("CONTAINS_BEER")
            .build();
        public static final Attribute FILTER_LIST = stringAttribute("FILTER_LIST")
            .build();
        public static final Attribute FAVORITE_ALL_SHOW_PRICE = booleanAttribute("FAVORITE_ALL_SHOW_PRICE")
            .inheritable()
            .build();
        public static final Attribute noGroupingByCategory = booleanAttribute("noGroupingByCategory")
            .inheritable()
            .build();
        public static final Attribute showAllByDefault = booleanAttribute("showAllByDefault")
            .inheritable()
            .build();
        public static final Attribute expand2ndLowestNavigationBox = booleanAttribute("expand2ndLowestNavigationBox")
            .inheritable()
            .build();
        public static final Attribute disableAtpFailureRecommendation = booleanAttribute("disableAtpFailureRecommendation")
            .inheritable()
            .build();
        public static final Attribute TEMPLATE_TYPE = integerAttribute("TEMPLATE_TYPE")
            .inheritable()
            .build();
        public static final Attribute COLUMN_NUM = integerAttribute("COLUMN_NUM")
            .inheritable()
            .build();
        public static final Attribute COLUMN_SPAN = integerAttribute("COLUMN_SPAN")
            .build();
        public static final Attribute USE_ALTERNATE_IMAGES = booleanAttribute("USE_ALTERNATE_IMAGES")
            .inheritable()
            .build();
        public static final Attribute FAKE_ALL_FOLDER = booleanAttribute("FAKE_ALL_FOLDER")
            .build();
        public static final Attribute SHOW_SIDE_NAV = booleanAttribute("SHOW_SIDE_NAV")
            .inheritable()
            .build();
        public static final Attribute HIDE_INACTIVE_SIDE_NAV = booleanAttribute("HIDE_INACTIVE_SIDE_NAV")
            .inheritable()
            .build();
        public static final Attribute TREAT_AS_PRODUCT = booleanAttribute("TREAT_AS_PRODUCT")
            .inheritable()
            .build();
        public static final Attribute NUTRITION_SORT = booleanAttribute("NUTRITION_SORT")
            .inheritable()
            .build();
        public static final Attribute SIDENAV_BOLD = booleanAttribute("SIDENAV_BOLD")
            .build();
        public static final Attribute SIDENAV_LINK = booleanAttribute("SIDENAV_LINK")
            .inheritable()
            .build();
        public static final Attribute SIDENAV_PRIORITY = integerAttribute("SIDENAV_PRIORITY")
            .build();
        public static final Attribute SIDENAV_SHOWSELF = booleanAttribute("SIDENAV_SHOWSELF")
            .inheritable()
            .build();
        public static final Attribute SHOWSELF = booleanAttribute("SHOWSELF")
            .inheritable()
            .build();
        public static final Attribute PRIORITY = integerAttribute("PRIORITY")
            .build();
        public static final Attribute FEATURED = booleanAttribute("FEATURED")
            .build();
        public static final Attribute SECONDARY_CATEGORY = booleanAttribute("SECONDARY_CATEGORY")
            .build();
        public static final Attribute MATERIAL_CHARACTERISTIC = stringAttribute("MATERIAL_CHARACTERISTIC")
            .build();
        public static final Attribute TEMPLATE_PATH = stringAttribute("TEMPLATE_PATH")
            .build();
        public static final Attribute RATING_BREAK_ON_SUBFOLDERS = booleanAttribute("RATING_BREAK_ON_SUBFOLDERS")
            .inheritable()
            .build();
        public static final Attribute RATING_CHECK_SUBFOLDERS = booleanAttribute("RATING_CHECK_SUBFOLDERS")
            .inheritable()
            .build();
        public static final Attribute RATING_GROUP_NAMES = stringAttribute("RATING_GROUP_NAMES")
            .inheritable()
            .build();
        public static final Attribute RG_MORE_USAGE_LABEL = stringAttribute("RG_MORE_USAGE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_POP_USAGE_LABEL = stringAttribute("RG_POP_USAGE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_PRICE_LABEL = stringAttribute("RG_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_SIZE_PRICE_LABEL = stringAttribute("RG_SIZE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_PRICE_LABEL = stringAttribute("RG_TASTE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_TYPE_PRICE_LABEL = stringAttribute("RG_TASTE_TYPE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_USE_PRICE_LABEL = stringAttribute("RG_TASTE_USE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_TEXTURE_PRICE_LABEL = stringAttribute("RG_TEXTURE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_TYPE_PRICE_LABEL = stringAttribute("RG_TYPE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_USAGE_LABEL = stringAttribute("RG_USAGE_LABEL")
            .inheritable()
            .build();
        public static final Attribute RG_USAGE_PRICE_LABEL = stringAttribute("RG_USAGE_PRICE_LABEL")
            .inheritable()
            .build();
        public static final Attribute SHOW_RATING_RELATED_IMAGE = booleanAttribute("SHOW_RATING_RELATED_IMAGE")
            .inheritable()
            .build();
        public static final Attribute topText = stringAttribute("topText")
            .build();
        public static final Attribute bottomText = stringAttribute("bottomText")
            .build();
        public static final Attribute SS_LEVEL_AGGREGATION = booleanAttribute("SS_LEVEL_AGGREGATION")
            .build();
        public static final Attribute MANUAL_SELECTION_SLOTS = integerAttribute("MANUAL_SELECTION_SLOTS")
            .build();
        public static final Attribute SHOW_EMPTY_CATEGORY = booleanAttribute("SHOW_EMPTY_CATEGORY")
            .inheritable()
            .build();
        public static final Attribute HIDE_WINE_RATING = booleanAttribute("HIDE_WINE_RATING")
            .inheritable()
            .build();
        public static final Attribute GLOBAL_MENU_TITLE_LABEL = stringAttribute("GLOBAL_MENU_TITLE_LABEL")
            .build();
        public static final Attribute GLOBAL_MENU_LINK_LABEL = stringAttribute("GLOBAL_MENU_LINK_LABEL")
            .build();
        public static final Attribute HIDE_FEATURED_ITEMS = booleanAttribute("HIDE_FEATURED_ITEMS")
            .inheritable()
            .build();
        public static final Attribute preferenceCategory = booleanAttribute("preferenceCategory")
            .build();
        public static final Attribute catMerchantRecommenderTitle = stringAttribute("catMerchantRecommenderTitle")
            .inheritable()
            .build();
        public static final Attribute catMerchantRecommenderRandomizeProducts = booleanAttribute("catMerchantRecommenderRandomizeProducts")
            .inheritable()
            .build();
        public static final Attribute hideIfFilteringIsSupported = booleanAttribute("hideIfFilteringIsSupported")
            .build();
        public static final Attribute showPopularCategories = booleanAttribute("showPopularCategories")
            .build();
        public static final Attribute disableCategoryYmalRecommender = booleanAttribute("disableCategoryYmalRecommender")
            .inheritable()
            .build();
        public static final Attribute SEO_META_DESC = stringAttribute("SEO_META_DESC")
            .build();
        public static final Attribute PAGE_TITLE = stringAttribute("PAGE_TITLE")
            .build();
        public static final Attribute SEO_META_DESC_FDX = stringAttribute("SEO_META_DESC_FDX")
            .build();
        public static final Attribute PAGE_TITLE_FDX = stringAttribute("PAGE_TITLE_FDX")
            .build();
        public static final Attribute SKIP_SITEMAP = booleanAttribute("SKIP_SITEMAP")
            .build();
        public static final Attribute PrimaryText = stringAttribute("PrimaryText")
            .build();
        public static final Attribute SecondaryText = stringAttribute("SecondaryText")
            .build();
        public static final Attribute featuredRecommenderTitle = stringAttribute("featuredRecommenderTitle")
            .build();
        public static final Attribute featuredRecommenderRandomizeProducts = booleanAttribute("featuredRecommenderRandomizeProducts")
            .build();
        public static final Attribute featuredRecommenderSiteFeature = stringAttribute("featuredRecommenderSiteFeature")
            .build();
        public static final Attribute LIST_AS = stringEnum("LIST_AS")
            .withValues("full", "glance", "nav", "no_nav")
            .inheritable()
            .build();
        public static final Attribute GROCERY_DEFAULT_SORT = stringEnum("GROCERY_DEFAULT_SORT")
            .withValues("name", "price", "popularity", "sale")
            .inheritable()
            .build();
        public static final Attribute SIDENAV_SHOWCHILDREN = integerEnum("SIDENAV_SHOWCHILDREN")
            .withValues(0, 1, 2, 3)
            .inheritable()
            .build();
        public static final Attribute SHOWCHILDREN = integerEnum("SHOWCHILDREN")
            .withValues(0, 1, 2)
            .build();
        public static final Attribute PRODUCT_PROMOTION_TYPE = stringEnum("PRODUCT_PROMOTION_TYPE")
            .withValues("NONE", "PRESIDENTS_PICKS", "E_COUPONS", "PRODUCTS_ASSORTMENTS")
            .build();
        public static final Attribute browseRecommenderType = stringEnum("browseRecommenderType")
            .withValues("NONE", "PDP_XSELL", "PDP_UPSELL")
            .inheritable()
            .build();
        public static final Attribute bannerLocationCLP = stringEnum("bannerLocationCLP")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute bannerLocationPLP = stringEnum("bannerLocationPLP")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute carouselPositionPLP = stringEnum("carouselPositionPLP")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute carouselRatioPLP = stringEnum("carouselRatioPLP")
            .withValues("FULL_WIDTH", "TWO_TWO", "THREE_ONE")
            .inheritable()
            .build();
        public static final Attribute carouselPositionCLP = stringEnum("carouselPositionCLP")
            .withValues("TOP", "BOTTOM")
            .inheritable()
            .build();
        public static final Attribute carouselRatioCLP = stringEnum("carouselRatioCLP")
            .withValues("FULL_WIDTH", "TWO_TWO", "THREE_ONE")
            .inheritable()
            .build();
        public static final Attribute brandFilterLocation = stringEnum("brandFilterLocation")
            .withValues("BELOW_DEPARTMENT", "BELOW_LOWEST_LEVEL_CATEGROY", "OFF", "ORIGINAL")
            .inheritable()
            .build();
        public static final Attribute ALTERNATE_CONTENT = linkOneOf(Html).toName("ALTERNATE_CONTENT")
            .build();
        public static final Attribute CATEGORY_MIDDLE_MEDIA = linkManyOf(Html).toName("CATEGORY_MIDDLE_MEDIA")
            .build();
        public static final Attribute SEPARATOR_MEDIA = linkOneOf(Html).toName("SEPARATOR_MEDIA")
            .build();
        public static final Attribute ALIAS = linkOneOf(Category).toName("ALIAS")
            .build();
        public static final Attribute subcategories = linkManyOf(Category).toName("subcategories")
            .navigable()
            .build();
        public static final Attribute CANDIDATE_LIST = linkManyOf(Category, Product).toName("CANDIDATE_LIST")
            .build();
        public static final Attribute VIRTUAL_GROUP = linkManyOf(Category).toName("VIRTUAL_GROUP")
            .build();
        public static final Attribute recommender = linkOneOf(Recommender).toName("recommender")
            .build();
        public static final Attribute products = linkManyOf(ConfiguredProduct, Product).toName("products")
            .navigable()
            .build();
        public static final Attribute FEATURED_PRODUCTS = linkManyOf(ConfiguredProduct, Product).toName("FEATURED_PRODUCTS")
            .build();
        public static final Attribute FEATURED_BRANDS = linkManyOf(Brand).toName("FEATURED_BRANDS")
            .build();
        public static final Attribute FEATURED_NEW_PRODBRANDS = linkManyOf(Brand, ConfiguredProduct, Product).toName("FEATURED_NEW_PRODBRANDS")
            .build();
        public static final Attribute HOWTOCOOKIT_USAGE = linkManyOf(Domain).toName("HOWTOCOOKIT_USAGE")
            .build();
        public static final Attribute WINE_FILTER = linkManyOf(Domain).toName("WINE_FILTER")
            .inheritable()
            .build();
        public static final Attribute WINE_FILTER_VALUE = linkOneOf(DomainValue).toName("WINE_FILTER_VALUE")
            .build();
        public static final Attribute SIDE_NAV_FULL_LIST = linkManyOf(Domain).toName("SIDE_NAV_FULL_LIST")
            .build();
        public static final Attribute WINE_SORTING = linkManyOf(DomainValue).toName("WINE_SORTING")
            .inheritable()
            .build();
        public static final Attribute SIDE_NAV_SECTIONS = linkManyOf(DomainValue).toName("SIDE_NAV_SECTIONS")
            .build();
        public static final Attribute HOW_TO_COOK_IT_PRODUCTS = linkManyOf(ConfiguredProduct, Product).toName("HOW_TO_COOK_IT_PRODUCTS")
            .build();
        public static final Attribute RATING = linkManyOf(Domain).toName("RATING")
            .inheritable()
            .build();
        public static final Attribute RATING_HOME = linkOneOf(Category).toName("RATING_HOME")
            .inheritable()
            .build();
        public static final Attribute RG_MORE_USAGE = linkManyOf(Domain).toName("RG_MORE_USAGE")
            .inheritable()
            .build();
        public static final Attribute RG_POP_USAGE = linkManyOf(Domain).toName("RG_POP_USAGE")
            .inheritable()
            .build();
        public static final Attribute RG_SIZE_PRICE = linkManyOf(Domain).toName("RG_SIZE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_PRICE = linkManyOf(Domain).toName("RG_TASTE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_TYPE_PRICE = linkManyOf(Domain).toName("RG_TASTE_TYPE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_TASTE_USE_PRICE = linkManyOf(Domain).toName("RG_TASTE_USE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_TEXTURE_PRICE = linkManyOf(Domain).toName("RG_TEXTURE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_TYPE_PRICE = linkManyOf(Domain).toName("RG_TYPE_PRICE")
            .inheritable()
            .build();
        public static final Attribute RG_USAGE = linkManyOf(Domain).toName("RG_USAGE")
            .inheritable()
            .build();
        public static final Attribute RG_USAGE_PRICE = linkManyOf(Domain).toName("RG_USAGE_PRICE")
            .inheritable()
            .build();
        public static final Attribute CAT_LABEL = linkOneOf(Image).toName("CAT_LABEL")
            .build();
        public static final Attribute CAT_PHOTO = linkOneOf(Image).toName("CAT_PHOTO")
            .inheritable()
            .build();
        public static final Attribute CAT_TITLE = linkOneOf(Image).toName("CAT_TITLE")
            .inheritable()
            .build();
        public static final Attribute CATEGORY_DETAIL_IMAGE = linkOneOf(Image).toName("CATEGORY_DETAIL_IMAGE")
            .build();
        public static final Attribute DEPT_MGR = linkOneOf(Image).toName("DEPT_MGR")
            .inheritable()
            .build();
        public static final Attribute SIDENAV_IMAGE = linkOneOf(Image).toName("SIDENAV_IMAGE")
            .inheritable()
            .build();
        public static final Attribute CATEGORY_NAVBAR = linkOneOf(Image).toName("CATEGORY_NAVBAR")
            .build();
        public static final Attribute EDITORIAL = linkOneOf(Html).toName("EDITORIAL")
            .build();
        public static final Attribute ARTICLES = linkManyOf(Html).toName("ARTICLES")
            .build();
        public static final Attribute CATEGORY_BOTTOM_MEDIA = linkManyOf(Html).toName("CATEGORY_BOTTOM_MEDIA")
            .build();
        public static final Attribute CATEGORY_TOP_MEDIA = linkManyOf(Html).toName("CATEGORY_TOP_MEDIA")
            .build();
        public static final Attribute CAT_STORAGE_GUIDE_MEDIA = linkOneOf(Html).toName("CAT_STORAGE_GUIDE_MEDIA")
            .build();
        public static final Attribute CATEGORY_PREVIEW_MEDIA = linkOneOf(Html).toName("CATEGORY_PREVIEW_MEDIA")
            .build();
        public static final Attribute productGrabbers = linkManyOf(ProductGrabber).toName("productGrabbers")
            .navigable()
            .build();
        public static final Attribute productFilterGroups = linkManyOf(ProductFilterGroup, ProductFilterMultiGroup).toName("productFilterGroups")
            .inheritable()
            .build();
        public static final Attribute sortOptions = linkManyOf(SortOption).toName("sortOptions")
            .inheritable()
            .build();
        public static final Attribute productTags = linkManyOf(Tag).toName("productTags")
            .inheritable()
            .build();
        public static final Attribute nameImage = linkOneOf(Image).toName("nameImage")
            .build();
        public static final Attribute description = linkOneOf(Html).toName("description")
            .build();
        public static final Attribute categoryBanner = linkOneOf(Html).toName("categoryBanner")
            .inheritable()
            .build();
        public static final Attribute middleMedia = linkOneOf(Html).toName("middleMedia")
            .build();
        public static final Attribute catMerchantRecommenderProducts = linkManyOf(Product).toName("catMerchantRecommenderProducts")
            .inheritable()
            .build();
        public static final Attribute globalNavIcon = linkOneOf(Image).toName("globalNavIcon")
            .build();
        public static final Attribute globalNavPostNameImage = linkOneOf(Image).toName("globalNavPostNameImage")
            .build();
        public static final Attribute popularCategories = linkManyOf(Category).toName("popularCategories")
            .build();
        public static final Attribute tabletCallToActionBanner = linkOneOf(Banner).toName("tabletCallToActionBanner")
            .inheritable()
            .build();
        public static final Attribute tabletThumbnailImage = linkOneOf(Image).toName("tabletThumbnailImage")
            .build();
        public static final Attribute heroCarousel = linkManyOf(Image).toName("heroCarousel")
            .build();
        public static final Attribute featuredRecommenderSourceCategory = linkOneOf(Category).toName("featuredRecommenderSourceCategory")
            .build();
    }

    public static final class Product {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute GLANCE_NAME = stringAttribute("GLANCE_NAME")
            .build();
        public static final Attribute NAV_NAME = stringAttribute("NAV_NAME")
            .build();
        public static final Attribute ALT_TEXT = stringAttribute("ALT_TEXT")
            .build();
        public static final Attribute KEYWORDS = stringAttribute("KEYWORDS")
            .build();
        public static final Attribute AKA = stringAttribute("AKA")
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute SUBTITLE = stringAttribute("SUBTITLE")
            .build();
        public static final Attribute HIDE_URL = stringAttribute("HIDE_URL")
            .inheritable()
            .build();
        public static final Attribute ALSO_SOLD_AS_NAME = stringAttribute("ALSO_SOLD_AS_NAME")
            .build();
        public static final Attribute REDIRECT_URL = stringAttribute("REDIRECT_URL")
            .inheritable()
            .build();
        public static final Attribute SALES_UNIT_LABEL = stringAttribute("SALES_UNIT_LABEL")
            .inheritable()
            .build();
        public static final Attribute QUANTITY_TEXT = stringAttribute("QUANTITY_TEXT")
            .inheritable()
            .build();
        public static final Attribute QUANTITY_TEXT_SECONDARY = stringAttribute("QUANTITY_TEXT_SECONDARY")
            .build();
        public static final Attribute SERVING_SUGGESTION = stringAttribute("SERVING_SUGGESTION")
            .build();
        public static final Attribute PACKAGE_DESCRIPTION = stringAttribute("PACKAGE_DESCRIPTION")
            .build();
        public static final Attribute SEASON_TEXT = stringAttribute("SEASON_TEXT")
            .inheritable()
            .build();
        public static final Attribute WINE_FYI = stringAttribute("WINE_FYI")
            .build();
        public static final Attribute WINE_REGION = stringAttribute("WINE_REGION")
            .build();
        public static final Attribute WINE_TYPE = stringAttribute("WINE_TYPE")
            .build();
        public static final Attribute SEAFOOD_ORIGIN = stringAttribute("SEAFOOD_ORIGIN")
            .build();
        public static final Attribute RELATED_PRODUCTS_HEADER = stringAttribute("RELATED_PRODUCTS_HEADER")
            .inheritable()
            .build();
        public static final Attribute SHOW_SALES_UNIT_IMAGE = booleanAttribute("SHOW_SALES_UNIT_IMAGE")
            .build();
        public static final Attribute NUTRITION_MULTIPLE = booleanAttribute("NUTRITION_MULTIPLE")
            .build();
        public static final Attribute SHOW_TOP_TEN_IMAGE = booleanAttribute("SHOW_TOP_TEN_IMAGE")
            .build();
        public static final Attribute NOT_SEARCHABLE = booleanAttribute("NOT_SEARCHABLE")
            .inheritable()
            .build();
        public static final Attribute CONTAINER_WEIGHT_HALF_PINT = doubleAttribute("CONTAINER_WEIGHT_HALF_PINT")
            .build();
        public static final Attribute CONTAINER_WEIGHT_PINT = doubleAttribute("CONTAINER_WEIGHT_PINT")
            .build();
        public static final Attribute CONTAINER_WEIGHT_QUART = doubleAttribute("CONTAINER_WEIGHT_QUART")
            .build();
        public static final Attribute INCREMENT_MAX_ENFORCE = booleanAttribute("INCREMENT_MAX_ENFORCE")
            .inheritable()
            .build();
        public static final Attribute QUANTITY_MINIMUM = doubleAttribute("QUANTITY_MINIMUM")
            .inheritable()
            .build();
        public static final Attribute QUANTITY_MAXIMUM = doubleAttribute("QUANTITY_MAXIMUM")
            .inheritable()
            .build();
        public static final Attribute QUANTITY_INCREMENT = doubleAttribute("QUANTITY_INCREMENT")
            .inheritable()
            .build();
        public static final Attribute INVISIBLE = booleanAttribute("INVISIBLE")
            .build();
        public static final Attribute PERISHABLE = booleanAttribute("PERISHABLE")
            .build();
        public static final Attribute FROZEN = booleanAttribute("FROZEN")
            .build();
        public static final Attribute GROCERY = booleanAttribute("GROCERY")
            .build();
        public static final Attribute PROD_PAGE_RATINGS = stringAttribute("PROD_PAGE_RATINGS")
            .inheritable()
            .build();
        public static final Attribute PROD_PAGE_TEXT_RATINGS = stringAttribute("PROD_PAGE_TEXT_RATINGS")
            .inheritable()
            .build();
        public static final Attribute RATING_PROD_NAME = stringAttribute("RATING_PROD_NAME")
            .inheritable()
            .build();
        public static final Attribute DEFAULT_SUSTAINABILITY_RATING = booleanAttribute("DEFAULT_SUSTAINABILITY_RATING")
            .inheritable()
            .build();
        public static final Attribute EXCLUDED_EBT_PAYMENT = booleanAttribute("EXCLUDED_EBT_PAYMENT")
            .inheritable()
            .build();
        public static final Attribute HIDE_IPHONE = booleanAttribute("HIDE_IPHONE")
            .inheritable()
            .build();
        public static final Attribute WINE_CLASSIFICATION = stringAttribute("WINE_CLASSIFICATION")
            .build();
        public static final Attribute WINE_IMPORTER = stringAttribute("WINE_IMPORTER")
            .build();
        public static final Attribute WINE_ALCH_CONTENT = stringAttribute("WINE_ALCH_CONTENT")
            .build();
        public static final Attribute WINE_AGING = stringAttribute("WINE_AGING")
            .build();
        public static final Attribute WINE_CITY = stringAttribute("WINE_CITY")
            .build();
        public static final Attribute EXCLUDED_RECOMMENDATION = booleanAttribute("EXCLUDED_RECOMMENDATION")
            .inheritable()
            .build();
        public static final Attribute DISABLED_RECOMMENDATION = booleanAttribute("DISABLED_RECOMMENDATION")
            .inheritable()
            .build();
        public static final Attribute disableAtpFailureRecommendation = booleanAttribute("disableAtpFailureRecommendation")
            .inheritable()
            .build();
        public static final Attribute retainOriginalSkuOrder = booleanAttribute("retainOriginalSkuOrder")
            .inheritable()
            .build();
        public static final Attribute SEO_META_DESC = stringAttribute("SEO_META_DESC")
            .build();
        public static final Attribute PAGE_TITLE = stringAttribute("PAGE_TITLE")
            .build();
        public static final Attribute SEO_META_DESC_FDX = stringAttribute("SEO_META_DESC_FDX")
            .build();
        public static final Attribute PAGE_TITLE_FDX = stringAttribute("PAGE_TITLE_FDX")
            .build();
        public static final Attribute SKIP_SITEMAP = booleanAttribute("SKIP_SITEMAP")
            .build();
        public static final Attribute PAIR_IT_HEADING = stringAttribute("PAIR_IT_HEADING")
            .build();
        public static final Attribute PAIR_IT_TEXT = stringAttribute("PAIR_IT_TEXT")
            .build();
        public static final Attribute TIME_TO_COMPLETE = integerAttribute("TIME_TO_COMPLETE")
            .build();
        public static final Attribute PRODUCT_LAYOUT = integerEnum("PRODUCT_LAYOUT")
            .withValues(1, 3, 5, 6, 7, 8, 10, 11)
            .inheritable()
            .build();
        public static final Attribute LAYOUT = integerEnum("LAYOUT")
            .withValues(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 18, 19, 20, 21, 24, 25, 29, 31, 40, 41, 42, 97, 100, 101, 102, 103, 104, 111, 200, 201, 202, 301)
            .inheritable()
            .build();
        public static final Attribute TEMPLATE_TYPE = integerEnum("TEMPLATE_TYPE")
            .withValues(1, 2, 3)
            .inheritable()
            .build();
        public static final Attribute SELL_BY_SALESUNIT = stringEnum("SELL_BY_SALESUNIT")
            .withValues("BOTH", "BOTH_SEPARATE", "QUANTITY", "SALES_UNIT")
            .inheritable()
            .build();
        public static final Attribute SS_EXPERT_WEIGHTING = integerEnum("SS_EXPERT_WEIGHTING")
            .withValues(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5)
            .inheritable()
            .build();
        public static final Attribute browseRecommenderType = stringEnum("browseRecommenderType")
            .withValues("NONE", "PDP_XSELL", "PDP_UPSELL")
            .inheritable()
            .build();
        public static final Attribute HEAT_RATING = integerEnum("HEAT_RATING")
            .withValues(-1, 0, 1, 2, 3, 4, 5)
            .build();
        public static final Attribute skus = linkManyOf(Sku).toName("skus")
            .navigable()
            .build();
        public static final Attribute brands = linkManyOf(Brand).toName("brands")
            .build();
        public static final Attribute youMightAlsoLike = linkManyOf(Category, Product).toName("youMightAlsoLike")
            .build();
        public static final Attribute WE_RECOMMEND_TEXT = linkManyOf(Product).toName("WE_RECOMMEND_TEXT")
            .build();
        public static final Attribute WE_RECOMMEND_IMAGE = linkManyOf(Product).toName("WE_RECOMMEND_IMAGE")
            .build();
        public static final Attribute RELATED_PRODUCTS = linkManyOf(Category, ConfiguredProduct, Product, Recipe).toName("RELATED_PRODUCTS")
            .inheritable()
            .build();
        public static final Attribute RECOMMENDED_ALTERNATIVES = linkManyOf(ConfiguredProduct, ConfiguredProductGroup, Product, Sku).toName("RECOMMENDED_ALTERNATIVES")
            .inheritable()
            .build();
        public static final Attribute RELATED_RECIPES = linkManyOf(Recipe).toName("RELATED_RECIPES")
            .inheritable()
            .build();
        public static final Attribute ymalSets = linkManyOf(YmalSet).toName("ymalSets")
            .inheritable()
            .build();
        public static final Attribute PRODUCT_BUNDLE = linkManyOf(ConfiguredProduct, Product).toName("PRODUCT_BUNDLE")
            .build();
        public static final Attribute ALSO_SOLD_AS = linkManyOf(ConfiguredProduct, Product).toName("ALSO_SOLD_AS")
            .build();
        public static final Attribute HOWTOCOOKIT_FOLDERS = linkManyOf(Category).toName("HOWTOCOOKIT_FOLDERS")
            .build();
        public static final Attribute PRIMARY_HOME = linkManyOf(Category).toName("PRIMARY_HOME")
            .build();
        public static final Attribute PERFECT_PAIR = linkOneOf(Category).toName("PERFECT_PAIR")
            .build();
        public static final Attribute PREFERRED_SKU = linkOneOf(Sku).toName("PREFERRED_SKU")
            .build();
        public static final Attribute RATING = linkManyOf(DomainValue).toName("RATING")
            .build();
        public static final Attribute WINE_NEW_TYPE = linkManyOf(DomainValue).toName("WINE_NEW_TYPE")
            .build();
        public static final Attribute WINE_VINTAGE = linkManyOf(DomainValue).toName("WINE_VINTAGE")
            .build();
        public static final Attribute WINE_NEW_REGION = linkManyOf(DomainValue).toName("WINE_NEW_REGION")
            .build();
        public static final Attribute WINE_VARIETAL = linkManyOf(DomainValue).toName("WINE_VARIETAL")
            .build();
        public static final Attribute WINE_RATING1 = linkManyOf(DomainValue).toName("WINE_RATING1")
            .build();
        public static final Attribute WINE_RATING2 = linkManyOf(DomainValue).toName("WINE_RATING2")
            .build();
        public static final Attribute WINE_RATING3 = linkManyOf(DomainValue).toName("WINE_RATING3")
            .build();
        public static final Attribute USAGE_LIST = linkManyOf(Domain).toName("USAGE_LIST")
            .build();
        public static final Attribute UNIT_OF_MEASURE = linkOneOf(DomainValue).toName("UNIT_OF_MEASURE")
            .build();
        public static final Attribute VARIATION_MATRIX = linkManyOf(Domain).toName("VARIATION_MATRIX")
            .build();
        public static final Attribute VARIATION_OPTIONS = linkManyOf(Domain).toName("VARIATION_OPTIONS")
            .build();
        public static final Attribute WINE_COUNTRY = linkOneOf(DomainValue).toName("WINE_COUNTRY").inheritable()
            .build();
        public static final Attribute PROD_IMAGE = linkOneOf(Image).toName("PROD_IMAGE")
            .inheritable()
            .build();
        public static final Attribute PROD_IMAGE_CONFIRM = linkOneOf(Image).toName("PROD_IMAGE_CONFIRM")
            .build();
        public static final Attribute PROD_IMAGE_DETAIL = linkOneOf(Image).toName("PROD_IMAGE_DETAIL")
            .inheritable()
            .build();
        public static final Attribute PROD_IMAGE_FEATURE = linkOneOf(Image).toName("PROD_IMAGE_FEATURE")
            .build();
        public static final Attribute PROD_IMAGE_ZOOM = linkOneOf(Image).toName("PROD_IMAGE_ZOOM")
            .build();
        public static final Attribute RATING_RELATED_IMAGE = linkOneOf(Image).toName("RATING_RELATED_IMAGE")
            .build();
        public static final Attribute ALTERNATE_IMAGE = linkOneOf(Image).toName("ALTERNATE_IMAGE")
            .build();
        public static final Attribute DESCRIPTIVE_IMAGE = linkOneOf(Image).toName("DESCRIPTIVE_IMAGE")
            .build();
        public static final Attribute PROD_IMAGE_ROLLOVER = linkOneOf(Image).toName("PROD_IMAGE_ROLLOVER")
            .build();
        public static final Attribute PROD_IMAGE_PACKAGE = linkOneOf(Image).toName("PROD_IMAGE_PACKAGE")
            .build();
        public static final Attribute PROD_IMAGE_JUMBO = linkOneOf(Image).toName("PROD_IMAGE_JUMBO")
            .build();
        public static final Attribute PROD_IMAGE_ITEM = linkOneOf(Image).toName("PROD_IMAGE_ITEM")
            .build();
        public static final Attribute PROD_IMAGE_EXTRA = linkOneOf(Image).toName("PROD_IMAGE_EXTRA")
            .build();
        public static final Attribute ALTERNATE_PROD_IMAGE = linkOneOf(Image).toName("ALTERNATE_PROD_IMAGE")
            .build();
        public static final Attribute PRODUCT_ABOUT = linkOneOf(Html).toName("PRODUCT_ABOUT")
            .build();
        public static final Attribute PROD_DESCR = linkOneOf(Html).toName("PROD_DESCR")
            .inheritable()
            .build();
        public static final Attribute RECOMMEND_TABLE = linkOneOf(Html).toName("RECOMMEND_TABLE")
            .build();
        public static final Attribute PRODUCT_QUALITY_NOTE = linkOneOf(Html).toName("PRODUCT_QUALITY_NOTE")
            .inheritable()
            .build();
        public static final Attribute PROD_DESCRIPTION_NOTE = linkOneOf(Html).toName("PROD_DESCRIPTION_NOTE")
            .inheritable()
            .build();
        public static final Attribute FRESH_TIPS = linkManyOf(Html).toName("FRESH_TIPS")
            .inheritable()
            .build();
        public static final Attribute DONENESS_GUIDE = linkManyOf(Html).toName("DONENESS_GUIDE")
            .build();
        public static final Attribute FDDEF_FRENCHING = linkOneOf(Html).toName("FDDEF_FRENCHING")
            .build();
        public static final Attribute FDDEF_GRADE = linkOneOf(Html).toName("FDDEF_GRADE")
            .build();
        public static final Attribute FDDEF_RIPENESS = linkOneOf(Html).toName("FDDEF_RIPENESS")
            .build();
        public static final Attribute FDDEF_SOURCE = linkOneOf(Html).toName("FDDEF_SOURCE")
            .build();
        public static final Attribute SALES_UNIT_DESCRIPTION = linkOneOf(Html).toName("SALES_UNIT_DESCRIPTION")
            .build();
        public static final Attribute PARTIALLY_FROZEN = linkOneOf(Html).toName("PARTIALLY_FROZEN")
            .inheritable()
            .build();
        public static final Attribute COMPONENT_GROUPS = linkManyOf(ComponentGroup).toName("COMPONENT_GROUPS")
            .navigable()
            .build();
        public static final Attribute PRODUCT_TERMS_MEDIA = linkOneOf(Html).toName("PRODUCT_TERMS_MEDIA")
            .build();
        public static final Attribute MEDIA_CONTENT = linkOneOf(Html).toName("MEDIA_CONTENT")
            .inheritable()
            .build();
        public static final Attribute WINE_REVIEW1 = linkOneOf(Html).toName("WINE_REVIEW1")
            .build();
        public static final Attribute WINE_REVIEW2 = linkOneOf(Html).toName("WINE_REVIEW2")
            .build();
        public static final Attribute WINE_REVIEW3 = linkOneOf(Html).toName("WINE_REVIEW3")
            .build();
        public static final Attribute PRODUCT_BOTTOM_MEDIA = linkOneOf(Html).toName("PRODUCT_BOTTOM_MEDIA")
            .build();
        public static final Attribute GIFTCARD_TYPE = linkManyOf(DomainValue).toName("GIFTCARD_TYPE")
            .build();
        public static final Attribute SCARAB_YMAL_INCLUDE = linkManyOf(Product).toName("SCARAB_YMAL_INCLUDE")
            .inheritable()
            .build();
        public static final Attribute SCARAB_YMAL_EXCLUDE = linkManyOf(Category, Department, Product).toName("SCARAB_YMAL_EXCLUDE")
            .inheritable()
            .build();
        public static final Attribute SCARAB_YMAL_PROMOTE = linkManyOf(Category, Department, Product).toName("SCARAB_YMAL_PROMOTE")
            .inheritable()
            .build();
        public static final Attribute SCARAB_YMAL_DEMOTE = linkManyOf(Category, Department, Product).toName("SCARAB_YMAL_DEMOTE")
            .inheritable()
            .build();
        public static final Attribute tags = linkManyOf(Tag).toName("tags")
            .build();
        public static final Attribute PDP_UPSELL = linkManyOf(ConfiguredProduct, Product).toName("PDP_UPSELL")
            .build();
        public static final Attribute PDP_XSELL = linkManyOf(ConfiguredProduct, Product).toName("PDP_XSELL")
            .build();
        public static final Attribute completeTheMeal = linkManyOf(ConfiguredProduct, Product).toName("completeTheMeal")
            .build();
        public static final Attribute MEAL_INCLUDE_PRODUCTS = linkManyOf(Product).toName("MEAL_INCLUDE_PRODUCTS")
            .build();
    }

    public static final class Sku {
        // deprecated attribute - soon to be deleted
        public static final Attribute ORGANIC = booleanAttribute("ORGANIC")
            .readOnly()
            .build();
        // deprecated attribute - soon to be deleted
        public static final Attribute KOSHER = booleanAttribute("KOSHER")
            .readOnly()
            .build();
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute GLANCE_NAME = stringAttribute("GLANCE_NAME")
            .build();
        public static final Attribute NAV_NAME = stringAttribute("NAV_NAME")
            .build();
        public static final Attribute VARIATION_MATRIX = linkManyOf(DomainValue).toName("VARIATION_MATRIX")
            .build();
        public static final Attribute VARIATION_OPTIONS = linkManyOf(DomainValue).toName("VARIATION_OPTIONS")
            .build();
        public static final Attribute brands = linkManyOf(Brand).toName("brands")
            .build();
        public static final Attribute materials = linkManyOf(ErpMaterial).toName("materials")
            .readOnly()
            .build();
    }

    public static final class ConfiguredProduct {
        public static final Attribute SALES_UNIT = stringAttribute("SALES_UNIT")
            .build();
        public static final Attribute QUANTITY = doubleAttribute("QUANTITY")
            .build();
        public static final Attribute OPTIONS = stringAttribute("OPTIONS")
            .build();
        public static final Attribute REQUIRED = booleanAttribute("REQUIRED")
            .build();
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute GLANCE_NAME = stringAttribute("GLANCE_NAME")
            .build();
        public static final Attribute NAV_NAME = stringAttribute("NAV_NAME")
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute ALSO_SOLD_AS_NAME = stringAttribute("ALSO_SOLD_AS_NAME")
            .build();
        public static final Attribute SUBTITLE = stringAttribute("SUBTITLE")
            .build();
        public static final Attribute INVISIBLE = booleanAttribute("INVISIBLE")
            .build();
        public static final Attribute PACKAGE_DESCRIPTION = stringAttribute("PACKAGE_DESCRIPTION")
            .build();
        public static final Attribute SKU = linkOneOf(Sku).toName("SKU")
            .build();
        public static final Attribute PROD_DESCR = linkOneOf(Html).toName("PROD_DESCR")
            .build();
        public static final Attribute PROD_DESCRIPTION_NOTE = linkOneOf(Html).toName("PROD_DESCRIPTION_NOTE")
            .build();
        public static final Attribute PRODUCT_QUALITY_NOTE = linkOneOf(Html).toName("PRODUCT_QUALITY_NOTE")
            .build();
        public static final Attribute PROD_IMAGE = linkOneOf(Image).toName("PROD_IMAGE")
            .build();
        public static final Attribute PROD_IMAGE_CONFIRM = linkOneOf(Image).toName("PROD_IMAGE_CONFIRM")
            .build();
        public static final Attribute PROD_IMAGE_DETAIL = linkOneOf(Image).toName("PROD_IMAGE_DETAIL")
            .build();
        public static final Attribute PROD_IMAGE_FEATURE = linkOneOf(Image).toName("PROD_IMAGE_FEATURE")
            .build();
        public static final Attribute PROD_IMAGE_ZOOM = linkOneOf(Image).toName("PROD_IMAGE_ZOOM")
            .build();
        public static final Attribute ALTERNATE_IMAGE = linkOneOf(Image).toName("ALTERNATE_IMAGE")
            .build();
        public static final Attribute DESCRIPTIVE_IMAGE = linkOneOf(Image).toName("DESCRIPTIVE_IMAGE")
            .build();
        public static final Attribute WINE_COUNTRY = linkOneOf(DomainValue).toName("WINE_COUNTRY")
            .inheritable()
            .build();
        public static final Attribute ALSO_SOLD_AS = linkManyOf(ConfiguredProduct, Product).toName("ALSO_SOLD_AS")
            .build();
        public static final Attribute RELATED_PRODUCTS = linkManyOf(Category, ConfiguredProduct, Product, Recipe).toName("RELATED_PRODUCTS")
            .build();
        public static final Attribute ymalSets = linkManyOf(YmalSet).toName("ymalSets")
            .inheritable()
            .build();
        public static final Attribute PROD_IMAGE_JUMBO = linkOneOf(Image).toName("PROD_IMAGE_JUMBO")
            .build();
        public static final Attribute PROD_IMAGE_ITEM = linkOneOf(Image).toName("PROD_IMAGE_ITEM")
            .build();
        public static final Attribute PROD_IMAGE_EXTRA = linkOneOf(Image).toName("PROD_IMAGE_EXTRA")
            .build();
        public static final Attribute PDP_UPSELL = linkManyOf(ConfiguredProduct, Product).toName("PDP_UPSELL")
            .build();
        public static final Attribute PDP_XSELL = linkManyOf(ConfiguredProduct, Product).toName("PDP_XSELL")
            .build();
    }

    public static final class ConfiguredProductGroup {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute unavailabilityMessage = stringAttribute("unavailabilityMessage")
            .build();
        public static final Attribute required = booleanAttribute("required")
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute items = linkManyOf(ConfiguredProduct).toName("items")
            .navigable()
            .build();
    }

    public static final class ComponentGroup {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute SHOW_OPTIONS = booleanAttribute("SHOW_OPTIONS")
            .build();
        public static final Attribute COMPONENTGROUP_LAYOUT = integerEnum("COMPONENTGROUP_LAYOUT")
            .withValues(0, 1, 2)
            .build();
        public static final Attribute HEADER_IMAGE = linkOneOf(Image).toName("HEADER_IMAGE")
            .build();
        public static final Attribute EDITORIAL = linkOneOf(Html).toName("EDITORIAL")
            .build();
        public static final Attribute CHARACTERISTICS = linkManyOf(ErpCharacteristic).toName("CHARACTERISTICS")
            .build();
        public static final Attribute OPTIONAL_PRODUCTS = linkManyOf(Product).toName("OPTIONAL_PRODUCTS")
            .build();
        public static final Attribute CHEFS_PICKS = linkManyOf(Product).toName("CHEFS_PICKS")
            .build();
    }

    public static final class ErpCharacteristic {
    }

    public static final class Brand {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute GLANCE_NAME = stringAttribute("GLANCE_NAME")
            .build();
        public static final Attribute NAV_NAME = stringAttribute("NAV_NAME")
            .build();
        public static final Attribute ALT_TEXT = stringAttribute("ALT_TEXT")
            .build();
        public static final Attribute KEYWORDS = stringAttribute("KEYWORDS")
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute CHEF_NAME = stringAttribute("CHEF_NAME")
            .build();
        public static final Attribute CHEF_BLURB = stringAttribute("CHEF_BLURB")
            .build();
        public static final Attribute tabletCopyright = stringAttribute("tabletCopyright")
            .build();
        public static final Attribute FEATURED_PRODUCTS = linkManyOf(Product).toName("FEATURED_PRODUCTS")
            .inheritable()
            .build();
        public static final Attribute FEATURED_CATEGORIES = linkManyOf(Category).toName("FEATURED_CATEGORIES")
            .build();
        public static final Attribute BRAND_LOGO = linkOneOf(Image).toName("BRAND_LOGO")
            .build();
        public static final Attribute BRAND_LOGO_SMALL = linkOneOf(Image).toName("BRAND_LOGO_SMALL")
            .build();
        public static final Attribute BRAND_LOGO_MEDIUM = linkOneOf(Image).toName("BRAND_LOGO_MEDIUM")
            .build();
        public static final Attribute CHEF_IMAGE = linkOneOf(Image).toName("CHEF_IMAGE")
            .build();
        public static final Attribute BRAND_POPUP_CONTENT = linkOneOf(Html).toName("BRAND_POPUP_CONTENT")
            .build();
        public static final Attribute tabletHeader = linkOneOf(Image).toName("tabletHeader")
            .build();
        public static final Attribute tabletImages = linkManyOf(Image).toName("tabletImages")
            .build();
        public static final Attribute tabletAboutTextShort = linkOneOf(Html).toName("tabletAboutTextShort")
            .build();
        public static final Attribute tabletAboutTextLong = linkOneOf(Html).toName("tabletAboutTextLong")
            .build();
        public static final Attribute tabletThumbnailImage = linkOneOf(Image).toName("tabletThumbnailImage")
            .build();
        public static final Attribute producer = linkOneOf(Producer).toName("producer")
            .readOnly()
            .build();
    }

    public static final class RecipeDepartment {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute categories = linkManyOf(RecipeCategory).toName("categories")
            .navigable()
            .build();
        public static final Attribute departmentNav = linkManyOf(RecipeCategory).toName("departmentNav")
            .build();
        public static final Attribute featuredSource = linkManyOf(RecipeSource).toName("featuredSource")
            .build();
        public static final Attribute featuredRecipeCategory = linkOneOf(RecipeSubcategory).toName("featuredRecipeCategory")
            .build();
        public static final Attribute featuredProductCategory = linkOneOf(Category).toName("featuredProductCategory")
            .build();
        public static final Attribute editorial = linkOneOf(Html).toName("editorial")
            .build();
    }

    public static final class RecipeCategory {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute SECONDARY_CATEGORY = booleanAttribute("SECONDARY_CATEGORY")
            .build();
        public static final Attribute subcategories = linkManyOf(RecipeSubcategory).toName("subcategories")
            .navigable()
            .build();
        public static final Attribute classification = linkOneOf(Domain).toName("classification")
            .build();
        public static final Attribute featuredSource = linkOneOf(RecipeSource).toName("featuredSource")
            .build();
        public static final Attribute label = linkOneOf(Image).toName("label")
            .build();
        public static final Attribute zoomLabel = linkOneOf(Image).toName("zoomLabel")
            .build();
        public static final Attribute editorial = linkOneOf(Html).toName("editorial")
            .build();
        public static final Attribute photo = linkOneOf(Image).toName("photo")
            .build();
    }

    public static final class RecipeSubcategory {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute REDIRECT_URL = stringAttribute("REDIRECT_URL")
            .inheritable()
            .build();
        public static final Attribute classification = linkOneOf(DomainValue).toName("classification")
            .build();
        public static final Attribute groupBy = linkManyOf(DomainValue).toName("groupBy")
            .build();
        public static final Attribute filterBy = linkManyOf(DomainValue).toName("filterBy")
            .build();
        public static final Attribute featuredRecipes = linkManyOf(Recipe).toName("featuredRecipes")
            .inheritable()
            .build();
        public static final Attribute featuredProducts = linkManyOf(Product).toName("featuredProducts")
            .inheritable()
            .build();
        public static final Attribute label = linkOneOf(Image).toName("label")
            .build();
        public static final Attribute editorial = linkOneOf(Html).toName("editorial")
            .build();
    }

    public static final class Recipe {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute label = stringAttribute("label")
            .build();
        public static final Attribute keywords = stringAttribute("keywords")
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute startDate = attribute().name("startDate").type(java.util.Date.class)
            .build();
        public static final Attribute endDate = attribute().name("endDate").type(java.util.Date.class)
            .build();
        public static final Attribute theme_color = stringEnum("theme_color")
            .withValues("DEFAULT", "6699CC")
            .build();
        public static final Attribute productionStatus = stringEnum("productionStatus")
            .withValues("PENDING", "ACTIVE", "LIMITED", "COMPLETED")
            .build();
        public static final Attribute source = linkOneOf(RecipeSource).toName("source")
            .build();
        public static final Attribute authors = linkManyOf(RecipeAuthor).toName("authors")
            .build();
        public static final Attribute variants = linkManyOf(RecipeVariant).toName("variants")
            .navigable()
            .build();
        public static final Attribute classifications = linkManyOf(DomainValue).toName("classifications")
            .build();
        public static final Attribute RELATED_PRODUCTS = linkManyOf(Category, ConfiguredProduct, Product, Recipe).toName("RELATED_PRODUCTS")
            .inheritable()
            .build();
        public static final Attribute ymalSets = linkManyOf(YmalSet).toName("ymalSets")
            .inheritable()
            .build();
        public static final Attribute description = linkOneOf(Html).toName("description")
            .build();
        public static final Attribute ingredientsMedia = linkOneOf(Html).toName("ingredientsMedia")
            .build();
        public static final Attribute preparationMedia = linkOneOf(Html).toName("preparationMedia")
            .build();
        public static final Attribute titleImage = linkOneOf(Image).toName("titleImage")
            .build();
        public static final Attribute logo = linkOneOf(Image).toName("logo")
            .build();
        public static final Attribute photo = linkOneOf(Image).toName("photo")
            .build();
        public static final Attribute copyrightMedia = linkOneOf(Html).toName("copyrightMedia")
            .build();
        public static final Attribute tabletThumbnailImage = linkOneOf(Image).toName("tabletThumbnailImage")
            .build();
    }

    public static final class RecipeVariant {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute sections = linkManyOf(RecipeSection).toName("sections")
            .navigable()
            .build();
    }

    public static final class RecipeSection {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute SHOW_QUANTITY = booleanAttribute("SHOW_QUANTITY")
            .build();
        public static final Attribute ingredients = linkManyOf(ConfiguredProduct, ConfiguredProductGroup).toName("ingredients")
            .navigable()
            .build();
    }

    public static final class RecipeSource {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute ISBN = stringAttribute("ISBN")
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute productionStatus = stringEnum("productionStatus")
            .withValues("PENDING", "ACTIVE", "LIMITED", "COMPLETED")
            .build();
        public static final Attribute authors = linkManyOf(RecipeAuthor).toName("authors")
            .build();
        public static final Attribute featuredRecipes = linkManyOf(Recipe).toName("featuredRecipes")
            .build();
        public static final Attribute featuredProducts = linkManyOf(Product).toName("featuredProducts")
            .build();
        public static final Attribute leftContent = linkOneOf(Html).toName("leftContent")
            .build();
        public static final Attribute topContent = linkOneOf(Html).toName("topContent")
            .build();
        public static final Attribute bottomContent = linkOneOf(Html).toName("bottomContent")
            .build();
        public static final Attribute emailContent = linkOneOf(Html).toName("emailContent")
            .build();
        public static final Attribute image = linkOneOf(Image).toName("image")
            .build();
        public static final Attribute zoomImage = linkOneOf(Image).toName("zoomImage")
            .build();
        public static final Attribute bookRetailers = linkManyOf(BookRetailer).toName("bookRetailers")
            .build();
    }

    public static final class RecipeAuthor {
        public static final Attribute firstName = stringAttribute("firstName")
            .build();
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
    }

    public static final class YmalSet {
        public static final Attribute title = stringAttribute("title")
            .build();
        public static final Attribute startDate = attribute().name("startDate").type(java.util.Date.class)
            .build();
        public static final Attribute endDate = attribute().name("endDate").type(java.util.Date.class)
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute transactional = booleanAttribute("transactional")
            .build();
        public static final Attribute productsHeader = stringAttribute("productsHeader")
            .build();
        public static final Attribute workflowStatus = stringEnum("workflowStatus")
            .withValues("PENDING_APPROVAL", "ACTIVE", "COMPLETED")
            .build();
        public static final Attribute ymals = linkManyOf(Category, ConfiguredProduct, ConfiguredProductGroup, Product, Recipe).toName("ymals")
            .build();
        public static final Attribute recommenders = linkManyOf(Recommender).toName("recommenders")
            .navigable()
            .build();
    }

    public static final class StarterList {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute BLURB = stringAttribute("BLURB")
            .build();
        public static final Attribute startDate = attribute().name("startDate").type(java.util.Date.class)
            .build();
        public static final Attribute endDate = attribute().name("endDate").type(java.util.Date.class)
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute productionStatus = stringEnum("productionStatus")
            .withValues("PENDING", "ACTIVE", "COMPLETED", "LIMITED")
            .build();
        public static final Attribute listContents = linkManyOf(ConfiguredProduct, ConfiguredProductGroup).toName("listContents")
            .navigable()
            .build();
    }

    public static final class BookRetailer {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute notes = stringAttribute("notes")
            .build();
        public static final Attribute isbnLink = stringAttribute("isbnLink")
            .build();
        public static final Attribute logo = linkOneOf(Image).toName("logo")
            .build();
    }

    public static final class RecipeSearchPage {
        public static final Attribute criteria = linkManyOf(RecipeSearchCriteria).toName("criteria")
            .navigable()
            .build();
        public static final Attribute filterByDomains = linkManyOf(Domain).toName("filterByDomains")
            .build();
    }

    public static final class RecipeSearchCriteria {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute selectionType = stringEnum("selectionType")
            .withValues("ONE", "MANY")
            .required()
            .build();
        public static final Attribute logicalOperation = stringEnum("logicalOperation")
            .withValues("AND", "OR")
            .required()
            .build();
        public static final Attribute criteriaDomainValues = linkManyOf(DomainValue).toName("criteriaDomainValues")
            .build();
    }

    public static final class Synonym {
        public static final Attribute word = stringAttribute("word")
            .required()
            .build();
        public static final Attribute synonymValue = stringAttribute("synonymValue")
            .required()
            .build();
        public static final Attribute bidirectional = booleanAttribute("bidirectional")
            .build();
    }

    public static final class SpellingSynonym {
        public static final Attribute word = stringAttribute("word")
            .required()
            .build();
        public static final Attribute synonymValue = stringAttribute("synonymValue")
            .required()
            .build();
    }

    public static final class SearchRelevancyList {
        public static final Attribute Keywords = stringAttribute("Keywords")
            .required()
            .build();
        public static final Attribute categoryHints = linkManyOf(SearchRelevancyHint).toName("categoryHints")
            .navigable()
            .build();
    }

    public static final class SearchRelevancyHint {
        public static final Attribute score = integerAttribute("score")
            .build();
        public static final Attribute category = linkOneOf(Category, Department).toName("category")
            .required()
            .build();
    }

    public static final class WordStemmingException {
        public static final Attribute word = stringAttribute("word")
            .required()
            .build();
    }

    public static final class FavoriteList {
        public static final Attribute full_name = stringAttribute("full_name")
            .build();
        public static final Attribute favoriteItems = linkManyOf(ConfiguredProduct, Product).toName("favoriteItems")
            .build();
    }

    public static final class Recommender {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute strategy = linkOneOf(RecommenderStrategy).toName("strategy")
            .required()
            .build();
        public static final Attribute scope = linkManyOf(Brand, Category, ConfiguredProduct, ConfiguredProductGroup, Department, FavoriteList, Product).toName("scope")
            .build();
    }

    public static final class RecommenderStrategy {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute generator = stringAttribute("generator")
            .required()
            .build();
        public static final Attribute scoring = stringAttribute("scoring")
            .build();
        public static final Attribute top_n = integerAttribute("top_n")
            .build();
        public static final Attribute top_percent = doubleAttribute("top_percent")
            .build();
        public static final Attribute exponent = doubleAttribute("exponent")
            .build();
        public static final Attribute show_temp_unavailable = booleanAttribute("show_temp_unavailable")
            .build();
        public static final Attribute brand_uniq_sort = booleanAttribute("brand_uniq_sort")
            .build();
        public static final Attribute sampling = stringEnum("sampling")
            .withValues("deterministic", "uniform", "linear", "quadratic", "cubic", "harmonic", "sqrt", "power", "complicated")
            .build();
    }

    public static final class Producer {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute ADDRESS = stringAttribute("ADDRESS")
            .build();
        public static final Attribute GMAPS_LOCATION = stringAttribute("GMAPS_LOCATION")
            .build();
        public static final Attribute bubble_content = linkOneOf(Html).toName("bubble_content")
            .build();
        public static final Attribute icon = linkOneOf(Image).toName("icon")
            .build();
        public static final Attribute icon_shadow = linkOneOf(Image).toName("icon_shadow")
            .build();
        public static final Attribute producer_type = linkOneOf(ProducerType).toName("producer_type")
            .build();
        public static final Attribute brand_category = linkOneOf(Category).toName("brand_category")
            .build();
        public static final Attribute brand = linkOneOf(Brand).toName("brand")
            .build();
    }

    public static final class ProducerType {
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute icon = linkOneOf(Image).toName("icon")
            .build();
        public static final Attribute icon_shadow = linkOneOf(Image).toName("icon_shadow")
            .build();
    }

    public static final class TileList {
        public static final Attribute filter = linkManyOf(Category, Department, DomainValue, Product).toName("filter")
            .build();
        public static final Attribute tiles = linkManyOf(Tile).toName("tiles")
            .build();
    }

    public static final class Tile {
        public static final Attribute GGF_TYPE = booleanAttribute("GGF_TYPE")
            .build();
        public static final Attribute media = linkOneOf(Html, Image).toName("media")
            .build();
        public static final Attribute quick_buy = linkManyOf(Category, ConfiguredProduct, ConfiguredProductGroup, Product).toName("quick_buy")
            .build();
    }

    public static final class HolidayGreeting {
        public static final Attribute CODE = stringAttribute("CODE")
            .required()
            .build();
        public static final Attribute FULL_NAME = stringAttribute("FULL_NAME")
            .build();
        public static final Attribute startDate = attribute().name("startDate").type(java.util.Date.class)
            .required()
            .build();
        public static final Attribute endDate = attribute().name("endDate").type(java.util.Date.class)
            .build();
        public static final Attribute GREETING_TEXT = stringAttribute("GREETING_TEXT")
            .required()
            .build();
    }

    public static final class MyFD {
        public static final Attribute BLOG_URL = stringAttribute("BLOG_URL")
            .build();
        public static final Attribute BLOG_ENTRY_COUNT = integerAttribute("BLOG_ENTRY_COUNT")
            .build();
        public static final Attribute POLL_DADDY_API_KEY = stringAttribute("POLL_DADDY_API_KEY")
            .build();
        public static final Attribute HEADER = linkManyOf(Image).toName("HEADER")
            .build();
        public static final Attribute HOLIDAY_GREETINGS = linkManyOf(HolidayGreeting).toName("HOLIDAY_GREETINGS")
            .navigable()
            .build();
        public static final Attribute EDITORIAL_MAIN = linkManyOf(Html, Image).toName("EDITORIAL_MAIN")
            .build();
        public static final Attribute EDITORIAL_SIDE = linkManyOf(Html, Image).toName("EDITORIAL_SIDE")
            .build();
    }

    public static final class GlobalMenuItem {
        public static final Attribute TITLE_LABEL = stringAttribute("TITLE_LABEL")
            .build();
        public static final Attribute LAYOUT = integerEnum("LAYOUT")
            .withValues(0, 1, 2, 3)
            .inheritable()
            .build();
        public static final Attribute subSections = linkManyOf(GlobalMenuSection).toName("subSections")
            .build();
        public static final Attribute editorial = linkOneOf(Html).toName("editorial")
            .build();
    }

    public static final class GlobalMenuSection {
        public static final Attribute SHOW_ALL_SUB_CATEGORIES = booleanAttribute("SHOW_ALL_SUB_CATEGORIES")
            .build();
        public static final Attribute linkedProductContainer = linkOneOf(Category, Department).toName("linkedProductContainer")
            .build();
        public static final Attribute subCategoryItems = linkManyOf(Category).toName("subCategoryItems")
            .build();
        public static final Attribute editorial = linkOneOf(Html).toName("editorial")
            .build();
    }

    public static final class YoutubeVideo {
        public static final Attribute YOUTUBE_VIDEO_ID = stringAttribute("YOUTUBE_VIDEO_ID")
            .required()
            .build();
        public static final Attribute TITLE = stringAttribute("TITLE")
            .build();
        public static final Attribute CONTENT = linkOneOf(Html).toName("CONTENT")
            .build();
    }

    public static final class Page {
        public static final Attribute title = stringAttribute("title")
            .build();
        public static final Attribute showSideNav = booleanAttribute("showSideNav")
            .inheritable()
            .build();
        public static final Attribute media = linkManyOf(Html).toName("media")
            .build();
        public static final Attribute subPages = linkManyOf(Page).toName("subPages")
            .navigable()
            .build();
    }

    public static final class ProductGrabber {
        public static final Attribute productFilters = linkManyOf(ProductFilter).toName("productFilters")
            .build();
        public static final Attribute scope = linkManyOf(Category, Department).toName("scope")
            .build();
    }

    public static final class ProductFilterGroup {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute allSelectedLabel = stringAttribute("allSelectedLabel")
            .build();
        public static final Attribute displayOnCategoryListingPage = booleanAttribute("displayOnCategoryListingPage")
            .build();
        public static final Attribute type = stringEnum("type")
            .withValues("SINGLE", "POPUP", "MULTI")
            .required()
            .build();
        public static final Attribute productFilters = linkManyOf(ProductFilter).toName("productFilters")
            .required()
            .navigable()
            .build();
    }

    public static final class ProductFilter {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute invert = booleanAttribute("invert")
            .build();
        public static final Attribute fromValue = doubleAttribute("fromValue")
            .build();
        public static final Attribute toValue = doubleAttribute("toValue")
            .build();
        public static final Attribute nutritionCode = stringAttribute("nutritionCode")
            .build();
        public static final Attribute erpsyFlagCode = stringAttribute("erpsyFlagCode")
            .build();
        public static final Attribute type = stringEnum("type")
            .withValues("AND", "OR", "ALLERGEN", "BACK_IN_STOCK", "BRAND", "CLAIM", "CUSTOMER_RATING", "DOMAIN_VALUE", "EXPERT_RATING",
                        "FRESHNESS", "KOSHER", "NEW", "NUTRITION", "ON_SALE", "ORGANIC", "PRICE", "SUSTAINABILITY_RATING", "TAG")
            .required()
            .build();
        public static final Attribute domainValue = linkOneOf(DomainValue).toName("domainValue")
            .build();
        public static final Attribute tag = linkOneOf(Tag).toName("tag")
            .build();
        public static final Attribute brand = linkOneOf(Brand).toName("brand")
            .build();
        public static final Attribute filters = linkManyOf(ProductFilter).toName("filters")
            .build();
    }

    public static final class Tag {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute children = linkManyOf(Tag).toName("children")
            .navigable()
            .build();
    }

    public static final class ProductFilterMultiGroup {
        public static final Attribute level1Name = stringAttribute("level1Name")
            .required()
            .build();
        public static final Attribute level1AllSelectedLabel = stringAttribute("level1AllSelectedLabel")
            .build();
        public static final Attribute level2Name = stringAttribute("level2Name")
            .build();
        public static final Attribute level2AllSelectedLabel = stringAttribute("level2AllSelectedLabel")
            .build();
        public static final Attribute level1Type = stringEnum("level1Type")
            .withValues("SINGLE", "POPUP")
            .required()
            .build();
        public static final Attribute level2Type = stringEnum("level2Type")
            .withValues("SINGLE", "POPUP")
            .build();
        public static final Attribute rootTag = linkOneOf(Tag).toName("rootTag")
            .required()
            .build();
    }

    public static final class SortOption {
        public static final Attribute label = stringAttribute("label")
            .required()
            .build();
        public static final Attribute selectedLabel = stringAttribute("selectedLabel")
            .build();
        public static final Attribute selectedLabelReverseOrder = stringAttribute("selectedLabelReverseOrder")
            .build();

        public static final Attribute strategy = stringEnum("strategy")
            .withValues("CUSTOMER_RATING", "EXPERT_RATING", "NAME", "POPULARITY", "PRICE", "SALE", "SUSTAINABILITY_RATING", "DEPARTMENT", "E_COUPON_DOLLAR_DISCOUNT",
                        "E_COUPON_EXPIRATION_DATE", "E_COUPON_PERCENT_DISCOUNT", "E_COUPON_POPULARITY", "E_COUPON_START_DATE", "RECENCY", "SEARCH_RELEVANCY", "FAVS_FIRST")
            .required()
            .build();
    }

    public static final class Banner {
        public static final Attribute location = stringEnum("location")
            .withValues("EMPTY", "CHECKOUT", "HOMEPAGE")
            .build();
        public static final Attribute image = linkOneOf(Image).toName("image")
            .build();
        public static final Attribute link = linkOneOf(Brand, Category, Department, Html, Product, Recipe, SuperDepartment).toName("link")
            .build();
    }

    public static final class SearchSuggestionGroup {
        public static final Attribute searchTerms = stringAttribute("searchTerms")
            .required()
            .build();
        public static final Attribute tabletImage = linkOneOf(Image).toName("tabletImage")
            .build();
    }

    public static final class RecipeTag {
        public static final Attribute tagId = stringAttribute("tagId")
            .required()
            .build();
        public static final Attribute tabletImage = linkOneOf(Image).toName("tabletImage")
            .build();
    }

    public static final class WebPage {
        public static final Attribute PAGE_TITLE = stringAttribute("PAGE_TITLE")
            .build();
        public static final Attribute SEO_META_DESC = stringAttribute("SEO_META_DESC")
            .build();
        public static final Attribute PAGE_TITLE_FDX = stringAttribute("PAGE_TITLE_FDX")
            .build();
        public static final Attribute SEO_META_DESC_FDX = stringAttribute("SEO_META_DESC_FDX")
            .build();
        public static final Attribute SKIP_SITEMAP = booleanAttribute("SKIP_SITEMAP").build();
        public static final Attribute URL = stringAttribute("URL")
            .build();
        public static final Attribute WebPageType = stringEnum("WebPageType")
            .withValues("Feed", "TodaysPick")
            .build();
        public static final Attribute WebPageSection = linkManyOf(Section).toName("WebPageSection")
            .build();
        public static final Attribute WebPageSchedule = linkManyOf(Schedule).toName("WebPageSchedule")
            .build();
        public static final Attribute WebPageDarkStore = linkManyOf(DarkStore).toName("WebPageDarkStore")
            .build();
    }

    public static final class Schedule {
        public static final Attribute EndTime = attribute().name("EndTime").type(Time.class)
            .build();
        public static final Attribute StartDate = attribute().name("StartDate").type(java.util.Date.class)
            .build();
        public static final Attribute EndDate = attribute().name("EndDate").type(java.util.Date.class)
            .build();
        public static final Attribute StartTime = attribute().name("StartTime").type(Time.class)
            .build();
        public static final Attribute Day = stringEnum("Day")
            .withValues("AllDay", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            .build();
    }

    public static final class Section {
        public static final Attribute name = stringAttribute("name")
            .build();
        public static final Attribute captionText = stringAttribute("captionText")
            .build();
        public static final Attribute headlineText = stringAttribute("headlineText")
            .build();
        public static final Attribute bodyText = stringAttribute("bodyText")
            .build();
        public static final Attribute linkText = stringAttribute("linkText")
            .build();
        public static final Attribute linkURL = stringAttribute("linkURL")
            .build();
        public static final Attribute linkType = stringEnum("linkType")
            .withValues("Link", "Button")
            .build();
        public static final Attribute displayType = stringEnum("displayType")
            .withValues("Greeting", "HorizontalPickList", "VerticalPickList", "ShortBanner")
            .build();
        public static final Attribute drawer = booleanAttribute("drawer")
            .build();
        public static final Attribute imageBanner = linkOneOf(ImageBanner).toName("imageBanner")
            .build();
        public static final Attribute product = linkManyOf(Product).toName("product")
            .build();
        public static final Attribute mustHaveProduct = linkManyOf(Product).toName("mustHaveProduct")
            .build();
        public static final Attribute pickList = linkManyOf(PickList).toName("pickList")
            .build();
        public static final Attribute category = linkManyOf(Category).toName("category")
            .build();
        public static final Attribute linkTarget = linkOneOf(Category, Department, PickList, Product).toName("linkTarget")
            .build();
        public static final Attribute SectionSchedule = linkManyOf(Schedule).toName("SectionSchedule")
            .build();
        public static final Attribute SectionDarkStore = linkManyOf(DarkStore).toName("SectionDarkStore")
            .build();
    }

    public static final class Anchor {
        public static final Attribute Url = stringAttribute("Url")
            .build();
        public static final Attribute Text = stringAttribute("Text")
            .build();
        public static final Attribute Type = stringEnum("Type")
            .withValues("Button", "Link")
            .build();
        public static final Attribute Target = linkOneOf(Category, Department, PickList, Product).toName("Target")
            .build();
    }

    public static final class TextComponent {
        public static final Attribute Text = stringAttribute("Text")
            .build();
        public static final Attribute Type = stringAttribute("Type")
            .build();
        public static final Attribute Default = booleanAttribute("Default")
            .build();
    }

    public static final class ImageBanner {
        public static final Attribute Name = stringAttribute("Name")
            .build();
        public static final Attribute Type = stringAttribute("Type")
            .build();
        public static final Attribute Description = stringAttribute("Description")
            .build();
        public static final Attribute FlagText = stringAttribute("FlagText")
            .build();
        public static final Attribute FlagColor = stringAttribute("FlagColor")
            .build();
        public static final Attribute Price = stringAttribute("Price")
            .build();
        public static final Attribute linkOneText = stringAttribute("linkOneText")
            .build();
        public static final Attribute linkOneURL = stringAttribute("linkOneURL")
            .build();
        public static final Attribute linkOneType = stringAttribute("linkOneType")
            .build();
        public static final Attribute linkTwoText = stringAttribute("linkTwoText")
            .build();
        public static final Attribute linkTwoURL = stringAttribute("linkTwoURL")
            .build();
        public static final Attribute linkTwoType = stringAttribute("linkTwoType")
            .build();
        public static final Attribute bannerURL = stringAttribute("bannerURL")
            .build();
        public static final Attribute ImageBannerImage = linkOneOf(Image).toName("ImageBannerImage")
            .build();
        public static final Attribute ImageBannerLink = linkManyOf(Anchor).toName("ImageBannerLink")
            .build();
        public static final Attribute ImageBannerBurst = linkManyOf(Image).toName("ImageBannerBurst")
            .build();
        public static final Attribute ImageBannerProduct = linkOneOf(Product).toName("ImageBannerProduct")
            .build();
        public static final Attribute Target = linkOneOf(Category, Department, PickList, Product).toName("Target")
            .build();
        public static final Attribute linkOneTarget = linkOneOf(Category, Department, PickList, Product).toName("linkOneTarget")
            .build();
        public static final Attribute linkTwoTarget = linkOneOf(Category, Department, PickList, Product).toName("linkTwoTarget")
            .build();
    }

    public static final class PickList {
        public static final Attribute Name = stringAttribute("Name")
            .build();
        public static final Attribute PricingZone = stringAttribute("PricingZone")
            .build();
        public static final Attribute DistributionChannel = stringAttribute("DistributionChannel")
            .build();
        public static final Attribute SalesOrganization = stringAttribute("SalesOrganization")
            .build();
        public static final Attribute DisplayName = stringAttribute("DisplayName")
            .build();
        public static final Attribute Type = stringAttribute("Type")
            .build();
        public static final Attribute Description = stringAttribute("Description")
            .build();
        public static final Attribute PickListPickListItem = linkManyOf(PickListItem).toName("PickListPickListItem")
            .build();
        public static final Attribute PickListMedia = linkOneOf(ImageBanner).toName("PickListMedia")
            .build();
        public static final Attribute PickListSchedule = linkManyOf(Schedule).toName("PickListSchedule")
            .build();
        public static final Attribute PickListPickList = linkManyOf(PickList).toName("PickListPickList")
            .build();
        public static final Attribute PickListProduct = linkManyOf(Product).toName("PickListProduct")
            .build();
        public static final Attribute PickListCategory = linkManyOf(Category).toName("PickListCategory")
            .build();
    }

    public static final class PickListItem {
        public static final Attribute Default = booleanAttribute("Default")
            .build();
        public static final Attribute PickListItemProduct = linkOneOf(Product).toName("PickListItemProduct")
            .build();
    }

    public static final class DarkStore {
        public static final Attribute value = stringEnum("value").withValues("1300", "1310")
            .build();
        public static final Attribute name = stringAttribute("name")
            .build();
    }

    public static final class ModuleContainer {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute modulesAndGroups = linkManyOf(Module, ModuleGroup).toName("modulesAndGroups")
            .build();
    }

    public static final class ModuleGroup {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute moduleGroupTitle = stringAttribute("moduleGroupTitle")
            .build();
        public static final Attribute moduleGroupTitleTextBanner = stringAttribute("moduleGroupTitleTextBanner")
            .build();
        public static final Attribute viewAllButtonURL = stringAttribute("viewAllButtonURL")
            .build();
        public static final Attribute hideViewAllButton = booleanAttribute("hideViewAllButton")
            .build();
        public static final Attribute modules = linkManyOf(Module).toName("modules")
            .build();
        public static final Attribute viewAllSourceNode = linkOneOf(Category, Department).toName("viewAllSourceNode")
            .build();
    }

    public static final class Module {
        public static final Attribute name = stringAttribute("name")
            .required()
            .build();
        public static final Attribute moduleTitle = stringAttribute("moduleTitle")
            .build();
        public static final Attribute moduleTitleTextBanner = stringAttribute("moduleTitleTextBanner")
            .build();
        public static final Attribute viewAllButtonLink = stringAttribute("viewAllButtonLink")
            .build();
        public static final Attribute contentTitle = stringAttribute("contentTitle")
            .build();
        public static final Attribute contentTitleTextBanner = stringAttribute("contentTitleTextBanner")
            .build();
        public static final Attribute heroTitle = stringAttribute("heroTitle")
            .build();
        public static final Attribute heroSubtitle = stringAttribute("heroSubtitle")
            .build();
        public static final Attribute headerTitle = stringAttribute("headerTitle")
            .build();
        public static final Attribute headerSubtitle = stringAttribute("headerSubtitle")
            .build();
        public static final Attribute hideViewAllButton = booleanAttribute("hideViewAllButton")
            .build();
        public static final Attribute hideProductName = booleanAttribute("hideProductName")
            .build();
        public static final Attribute hideProductPrice = booleanAttribute("hideProductPrice")
            .build();
        public static final Attribute hideProductBadge = booleanAttribute("hideProductBadge")
            .build();
        public static final Attribute useViewAllPopup = booleanAttribute("useViewAllPopup")
            .build();
        public static final Attribute showViewAllOverlayOnImages = booleanAttribute("showViewAllOverlayOnImages")
            .build();
        public static final Attribute productSourceType = stringEnum("productSourceType")
            .withValues("GENERIC", "BROWSE", "TOP_ITEMS", "PRES_PICKS", "FEATURED_RECOMMENDER", "MOST_POPULAR_PRODUCTS", "BRAND_FEATURED_PRODUCTS", "STAFF_PICKS", "CRITEO")
            .required()
            .build();
        public static final Attribute displayType = stringEnum("displayType")
            .withValues("PRODUCT_CAROUSEL_MODULE", "PRODUCT_LIST_MODULE", "IMAGEGRID_MODULE", "ICON_CAROUSEL_MODULE", "OPENHTML_MODULE", "EDITORIAL_MODULE")
            .required()
            .build();
        public static final Attribute productListRowMax = stringEnum("productListRowMax")
            .withValues("1", "2", "3")
            .build();
        public static final Attribute imageGrid = linkManyOf(ImageBanner).toName("imageGrid")
            .build();
        public static final Attribute openHTML = linkOneOf(Html).toName("openHTML")
            .build();
        public static final Attribute iconList = linkManyOf(ImageBanner).toName("iconList")
            .build();
        public static final Attribute heroGraphic = linkOneOf(Image).toName("heroGraphic")
            .build();
        public static final Attribute headerGraphic = linkOneOf(Image).toName("headerGraphic")
            .build();
        public static final Attribute editorialContent = linkOneOf(Html).toName("editorialContent")
            .build();
        public static final Attribute sourceNode = linkOneOf(Brand, Category, Department).toName("sourceNode")
            .build();
    }

    public static final class Image {
        public static final Attribute height = integerAttribute("height")
            .readOnly()
            .build();
        public static final Attribute width = integerAttribute("width")
            .readOnly()
            .build();
        public static final Attribute path = stringAttribute("path")
            .readOnly()
            .build();
        public static final Attribute lastmodified = dateAttribute("lastmodified")
            .readOnly()
            .build();
    }

    public static final class Html {
        public static final Attribute title = stringAttribute("title")
            .build();
        public static final Attribute popupSize = stringAttribute("popupSize")
            .build();
        public static final Attribute path = stringAttribute("path")
            .readOnly()
            .build();
        public static final Attribute lastmodified = dateAttribute("lastmodified")
            .readOnly()
            .build();
    }

    public static final class Template {
        public static final Attribute path = stringAttribute("path")
            .readOnly()
            .build();
        public static final Attribute lastmodified = stringAttribute("lastmodified")
            .readOnly()
            .build();
    }

    public static final class MediaFolder {
        public static final Attribute name = stringAttribute("name")
            .readOnly()
            .build();
        public static final Attribute path = stringAttribute("path")
            .readOnly()
            .build();
        public static final Attribute lastmodified = stringAttribute("lastmodified")
            .readOnly()
            .build();
        public static final Attribute subFolders = linkManyOf(MediaFolder).toName("subFolders")
            .readOnly()
            .build();
        public static final Attribute files = linkManyOf(Html, Image).toName("files")
            .readOnly()
            .build();
    }
}
