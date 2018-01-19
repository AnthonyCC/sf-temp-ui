package com.freshdirect.cms.ui.editor;


import static com.freshdirect.cms.ui.editor.AttributeLabelKey.keyOf;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.ui.editor.domain.VirtualAttributes;


@SuppressWarnings("serial")
public final class AttributeLabels {
    public static final Map<AttributeLabelKey, String> ATTRIBUTE_LABELS = new HashMap<AttributeLabelKey, String>();

    public static final Map<AttributeLabelKey, Map<String, String>> ENUM_LABELS = new HashMap<AttributeLabelKey, Map<String, String>>();


    static {
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FDFolder, ContentTypes.FDFolder.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FDFolder, ContentTypes.FDFolder.children), "Children");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.ORGANIZATION_NAME), "Organization Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.EMAIL), "Recipient Email");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.CONTACT_INFO), "Contact Info");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL), "Organization logo Small");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.ORGANIZATION_LOGO), "Organization logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.ORGANIZATION_RECIEPT_LOGO), "Organization Reciept Page logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.GIFTCARD_TYPE), "Gift Card Types");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.EDITORIAL_MAIN), "Editorial for Main Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.EDITORIAL_HEADER_MEDIA), "Editorial for Header Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.EDITORIAL_DETAIL), "Editorial for Detail Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DonationOrganization, ContentTypes.DonationOrganization.EDITORIAL_RECEIPT_MEDIA), "Editorial for Receipt Content");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.FAQ, ContentTypes.FAQ.FULL_NAME), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FAQ, ContentTypes.FAQ.QUESTION), "Question");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FAQ, ContentTypes.FAQ.ANSWER), "Answer");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FAQ, ContentTypes.FAQ.KEYWORDS), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FAQ, ContentTypes.FAQ.PRIORITY_LIST), "Priority list");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.NAME), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.PREVIEW_HOST_NAME), "Preview Host Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.folders), "Folders");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.departments), "Departments");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.HOME_FEATURED_PRODUCTS), "Home Featured Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.myFD), "My Fd");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.EDITORIAL), "Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.pages), "Media pages");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.superDepartments), "Super Departments");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.globalNavigations), "Global Navigations");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.searchPageSortOptions), "Search Page Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.newProductsPageSortOptions), "New Products Page Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.presidentsPicksPageSortOptions), "Fresh Deals Page Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.staffPicksPageSortOptions), "Staff's Picks Page Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.eCouponsPageSortOptions), "e-Coupons Page Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.eCouponsPageTopMediaBanner), "e-Coupons Page Top Media Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.searchPageTopMediaBanner), "Search Page Top Media Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.newProductsPageTopMediaBanner), "New Products Page Top Media Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.presPicksPageTopMediaBanner), "Fresh Deals Page Top Media Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.staffPicksPageTopMediaBanner), "Staff Picks Page Top Media Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletFeaturedCategories), "Tablet Featured Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletSearchSuggestionGroups), "Tablet Search Suggestion Groups");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletIdeasBanner), "Tablet Ideas Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletIdeasFeaturedPicksLists), "Tablet Ideas Featured Picks Lists");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.iPhoneHomePagePicksLists), "iPhone Home Page Picks Lists");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.iPhoneHomePageImageBanners), "iPhone Homepage Image Banners");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletIdeasRecipeTags), "Tablet Ideas Recipe Tags (Theme Page)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletHomeScreenPopUpShopBanners), "Tablet Home Screen Pop-up Shop Banners");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletIdeasBrands), "Tablet Ideas Brands");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.tabletIdeasRecipes), "Tablet Ideas Recipes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.expressCheckoutReceiptHeader), "Express Checkout Receipt Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.expressCheckoutReceiptEditorial), "Express Checkout Receipt Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.expressCheckoutTextMessageAlertHeader), "Express Checkout Text Message Alert Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Store, ContentTypes.Store.welcomeCarouselImageBanners), "Welcome Carousel Image Banners");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.browseName), "Browse Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdFeaturedRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdFeaturedRecommenderRandomizeProducts), "Randomize Products (only if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdFeaturedRecommenderSiteFeature), "Site Feature (ignored if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdMerchantRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdMerchantRecommenderRandomizeProducts), "Randomize Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.hideGlobalNavDropDown), "Hide Global Nav Drop Down");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.SEO_META_DESC), "SEO Meta Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.PAGE_TITLE), "Page Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.SEO_META_DESC_FDX), "SEO Meta Description FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.PAGE_TITLE_FDX), "Page Title FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.SKIP_SITEMAP), "Skip Sitemap");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.bannerLocation), "Banner Location");
        ENUM_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.bannerLocation), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.carouselPosition), "Carousel Position");
        ENUM_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.carouselPosition), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.carouselRatio), "Carousel Ratio");
        ENUM_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.carouselRatio), new HashMap<String, String>() {{
            put("FULL_WIDTH", "FULL WIDTH");
            put("TWO_TWO", "2/2");
            put("THREE_ONE", "3/1");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.departments), "Departments");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.titleBar), "Title Bar");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.superDepartmentBanner), "Super Department Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.middleMedia), "SuperDepartment Middle Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdFeaturedRecommenderSourceCategory), "Source Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.SuperDepartment.sdMerchantRecommenderProducts), "Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.Department.catalog), "Catalog");
        ENUM_LABELS.put(keyOf(ContentType.SuperDepartment, ContentTypes.Department.catalog), new HashMap<String, String>() {{
            put("ALL", "All");
            put("RESIDENTAL", "Residential");
            put("CORPORATE", "Corporate");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalNavigation, ContentTypes.GlobalNavigation.items), "Dropdown Data Source (DDS)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalNavigation, ContentTypes.GlobalNavigation.media), "Media");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.CategorySection, ContentTypes.CategorySection.headline), "Headline");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.CategorySection, ContentTypes.CategorySection.insertColumnBreak), "Break Following Categories Into A New Column");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.CategorySection, ContentTypes.CategorySection.selectedCategories), "Selected Categories");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Domain, ContentTypes.Domain.NAME), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Domain, ContentTypes.Domain.Label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Domain, ContentTypes.Domain.domainValues), "Domain Values");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.DomainValue, ContentTypes.DomainValue.Label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DomainValue, ContentTypes.DomainValue.VALUE), "Value");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.ALT_TEXT), "Alt Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.MAX_ROWCOUNT), "Max Rowcount");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.PRIORITY), "Priority");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.USE_ALTERNATE_IMAGES), "Use Alternate Images");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.HIDE_IN_QUICKSHOP), "Hide in Quickshop");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPARTMENT_TEMPLATE_PATH), "Department Template Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPARTMENT_ALT_TEMPLATE_PATH), "Department Cohort Match Template Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.GLOBAL_MENU_TITLE_LABEL), "Global Menu Title Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.GLOBAL_MENU_LINK_LABEL), "Global Menu Link Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.noGroupingByCategory), "Disable Product Grouping By Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.showAllByDefault), "Show All By Default");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.expand2ndLowestNavigationBox), "Expand Second Lowest Navigation Box");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.disableAtpFailureRecommendation), "Disable ATP Failure Recommendation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.featuredRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.featuredRecommenderRandomizeProducts), "Randomize Products (only if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.featuredRecommenderSiteFeature), "Site Feature (ignored if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.merchantRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.merchantRecommenderRandomizeProducts), "Randomize Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.showPopularCategories), "Show Popular Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.globalNavShopAllText), "Global Nav Shop All Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.regularCategoriesNavHeader), "Regular Categories Nav Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.preferenceCategoriesNavHeader), "Preference Categories Nav Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.regularCategoriesLeftNavBoxHeader), "Regular Categories Left Nav Box Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.preferenceCategoriesLeftNavBoxHeader), "Preference Categories Left Nav Box Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.maxItemsPerColumn), "Preference Categories Max Items In Column In Flyout");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.showCatSectionHeaders), "Show Category Section Headers");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.globalNavName), "Global Nav Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.hideGlobalNavDropDown), "Hide Global Nav Drop Down");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.disableCategoryYmalRecommender), "Disable Category YMAL Recommender");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.SEO_META_DESC), "SEO Meta Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.PAGE_TITLE), "Page Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.SEO_META_DESC_FDX), "SEO Meta Description FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.PAGE_TITLE_FDX), "Page Title FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.SKIP_SITEMAP), "Skip Sitemap");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.BOTTOM_RIGHT_DISPLAY), "Bottom Right Display");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.BOTTOM_RIGHT_DISPLAY), new HashMap<String, String>() {{
            put("1", "SECONDARY_FOLDERS");
            put("2", "BLURB_AND_IMAGE");
            put("26", "Vertical Categories");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.browseRecommenderType), "Browse Recommender Type");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.browseRecommenderType), new HashMap<String, String>() {{
            put("NONE", "None");
            put("PDP_XSELL", "Cross-sell (first item)");
            put("PDP_UPSELL", "Upsell (first item)");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.carouselPosition), "Carousel Position");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.carouselPosition), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.carouselRatio), "Carousel Ratio");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.carouselRatio), new HashMap<String, String>() {{
            put("FULL_WIDTH", "FULL WIDTH");
            put("TWO_TWO", "2/2");
            put("THREE_ONE", "3/1");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.bannerLocation), "Department Banner Location");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.bannerLocation), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.categories), "Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.FEATURED_CATEGORIES), "Featured Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.FEATURED_PRODUCTS), "Featured Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_NAV), "Dept Nav");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_MGR_NONAME), "Dept Mgr Noname");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_PHOTO), "Dept Photo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_PHOTO_SMALL), "Dept Photo Small");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_TITLE), "Dept Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPARTMENT_MIDDLE_MEDIA), "Dept Middle Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_NAVBAR_ROLLOVER), "Dept Navbar Rollover");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_NAVBAR), "Dept Navbar");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.EDITORIAL), "Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_MGR_BIO), "Dept Mgr Bio");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPT_STORAGE_GUIDE_MEDIA), "Dept Storage Guide Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.ASSOC_EDITORIAL), "Assoc Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.DEPARTMENT_BOTTOM), "Dept Bottom");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.tile_list), "Assigned Tiles");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.productFilterGroups), "Product Filter Groups");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.popularCategories), "Popular Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.titleBar), "Title Bar");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.productTags), "Tags For Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.categoryBanner), "Category Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.departmentBanner), "Department Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.middleMedia), "Middle Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.featuredRecommenderSourceCategory), "Source Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.merchantRecommenderProducts), "Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.globalNavFeaturedCategory), "Global Nav Featured Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.globalNavFeaturedPreferenceCategory), "Global Nav Featured Preference Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.heroImage), "Hero Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.seasonalMedia), "Seasonal Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.categorySections), "Category Sections");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.tabletCallToActionBanner), "Call To Action Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.tabletNoPurchaseSuggestions), "No Purchase Suggestions");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.tabletIcon), "Icon");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.tabletHeaderBanner), "Tablet Header Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.heroCarousel), "Hero Carousel");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.catalog), "Catalog");
        ENUM_LABELS.put(keyOf(ContentType.Department, ContentTypes.Department.catalog), new HashMap<String, String>() {{
            put("ALL", "All");
            put("RESIDENTAL", "Residential");
            put("CORPORATE", "Corporate");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.ALT_TEXT), "Alt Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.KEYWORDS), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.EDITORIAL_TITLE), "Editorial Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CONTAINS_BEER), "Contains Beer");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FILTER_LIST), "Filter List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FAVORITE_ALL_SHOW_PRICE), "Favorite All Show Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.noGroupingByCategory), "Disable Product Grouping By Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.showAllByDefault), "Show All By Default");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.expand2ndLowestNavigationBox), "Expand Second Lowest Navigation Box");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.disableAtpFailureRecommendation), "Disable ATP Failure Recommendation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.TEMPLATE_TYPE), "Template Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.COLUMN_NUM), "Column Number");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.COLUMN_SPAN), "Column Span");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.USE_ALTERNATE_IMAGES), "Use Alternate Images");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FAKE_ALL_FOLDER), "Fake All Folder");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOW_SIDE_NAV), "Show Side Nav");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.HIDE_INACTIVE_SIDE_NAV), "Hide Inactive Category in Side Nav");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.TREAT_AS_PRODUCT), "Treat as Product");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.NUTRITION_SORT), "Nutrition Sort");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_BOLD), "Side Nav Bold");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_LINK), "Side Nav Link");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_PRIORITY), "Side Nav Priority");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_SHOWSELF), "Side Nav Show Self");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOWSELF), "Show Self");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PRIORITY), "Priority");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FEATURED), "Featured");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SECONDARY_CATEGORY), "Secondary Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.MATERIAL_CHARACTERISTIC), "Material Characteristic");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.TEMPLATE_PATH), "Content Template Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RATING_BREAK_ON_SUBFOLDERS), "Rating Break on Subfolders");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RATING_CHECK_SUBFOLDERS), "Rating Check Subfolders");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RATING_GROUP_NAMES), "Rating Group Names");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_MORE_USAGE_LABEL), "Label for More Usage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_POP_USAGE_LABEL), "Label for Popular Usage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_PRICE_LABEL), "Label for Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_SIZE_PRICE_LABEL), "Label for Popular Usage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_PRICE_LABEL), "Label for Taste & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_TYPE_PRICE_LABEL), "Label for Taste & Type & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_USE_PRICE_LABEL), "Label for Taste & Use & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TEXTURE_PRICE_LABEL), "Label for Texture & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TYPE_PRICE_LABEL), "Label for Type & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_USAGE_LABEL), "Label for Usage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_USAGE_PRICE_LABEL), "Label for Usage & Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOW_RATING_RELATED_IMAGE), "Show Rating Related Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.topText), "Top Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.bottomText), "Bottom Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SS_LEVEL_AGGREGATION), "Level Aggregation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.MANUAL_SELECTION_SLOTS), "Manual Selection Slots");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOW_EMPTY_CATEGORY), "Show Empty Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.HIDE_WINE_RATING), "Hide Wine Rating & Pricing (Use Regular Display Instead)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.GLOBAL_MENU_TITLE_LABEL), "Global Menu Title Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.GLOBAL_MENU_LINK_LABEL), "Global Menu Link Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.HIDE_FEATURED_ITEMS), "Hide featured items");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.preferenceCategory), "Preference Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.catMerchantRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.catMerchantRecommenderRandomizeProducts), "Randomize Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.hideIfFilteringIsSupported), "Hide If Filtering Is Supported");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.showPopularCategories), "Show Popular Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.disableCategoryYmalRecommender), "Disable Category YMAL Recommender");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SEO_META_DESC), "SEO Meta Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PAGE_TITLE), "Page Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SEO_META_DESC_FDX), "SEO Meta Description FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PAGE_TITLE_FDX), "Page Title FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SKIP_SITEMAP), "Skip Sitemap");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PrimaryText), "Primary Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SecondaryText), "Secondary Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.LIST_AS), "List As Name");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.LIST_AS), new HashMap<String, String>() {{
            put("full", "Full Name");
            put("glance", "Glance Name");
            put("nav", "Nav Name");
            put("no_nav", "Nav Name");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.GROCERY_DEFAULT_SORT), "Default sorting for grocery");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.GROCERY_DEFAULT_SORT), new HashMap<String, String>() {{
            put("name", "Sort by Name");
            put("price", "Sort by Price");
            put("popularity", "Sort by Popularity");
            put("sale", "Sort by Sale");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_SHOWCHILDREN), "Side Nav Show Children");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_SHOWCHILDREN), new HashMap<String, String>() {{
            put("0", "IF_IN_BROWSE_PATH");
            put("1", "ALWAYS");
            put("2", "NEVER");
            put("3", "ALWAYS_FOLDERS");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOWCHILDREN), "Show Children");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SHOWCHILDREN), new HashMap<String, String>() {{
            put("0", "FALSE");
            put("1", "TRUE");
            put("2", "never");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PRODUCT_PROMOTION_TYPE), "Product Promotion Type");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.PRODUCT_PROMOTION_TYPE), new HashMap<String, String>() {{
            put("NONE", "None");
            put("PRESIDENTS_PICKS", "Fresh Deals");
            put("E_COUPONS", "E-Coupons");
            put("PRODUCTS_ASSORTMENTS", "Products Assortments");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.browseRecommenderType), "Browse Recommender Type");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.browseRecommenderType), new HashMap<String, String>() {{
            put("NONE", "None");
            put("PDP_XSELL", "Cross-sell (first item)");
            put("PDP_UPSELL", "Upsell (first item)");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.bannerLocationCLP), "Category Banner Location CLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.bannerLocationCLP), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.bannerLocationPLP), "Category Banner Location PLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.bannerLocationPLP), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselPositionPLP), "Carousel Position PLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselPositionPLP), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselRatioPLP), "Carousel Ratio PLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselRatioPLP), new HashMap<String, String>() {{
            put("FULL_WIDTH", "FULL WIDTH");
            put("TWO_TWO", "2/2");
            put("THREE_ONE", "3/1");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselPositionCLP), "Carousel Position CLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselPositionCLP), new HashMap<String, String>() {{
            put("TOP", "TOP");
            put("BOTTOM", "BOTTOM");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselRatioCLP), "Carousel Ratio CLP");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.carouselRatioCLP), new HashMap<String, String>() {{
            put("FULL_WIDTH", "FULL WIDTH");
            put("TWO_TWO", "2/2");
            put("THREE_ONE", "3/1");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.brandFilterLocation), "Brand Filter Location");
        ENUM_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.brandFilterLocation), new HashMap<String, String>() {{
            put("BELOW_DEPARTMENT", "On/Appearing below depratment");
            put("BELOW_LOWEST_LEVEL_CATEGROY", "On/Appearing below lowest level category");
            put("OFF", "Turned off");
            put("ORIGINAL", "On/Appearing in original order");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.ALTERNATE_CONTENT), "Alternate Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_MIDDLE_MEDIA), "Cat Middle Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SEPARATOR_MEDIA), "Separator Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.ALIAS), "Alias");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.subcategories), "Subcategories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CANDIDATE_LIST), "Candidate List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.VIRTUAL_GROUP), "Virtual Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.recommender), "Recommender");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.products), "Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FEATURED_PRODUCTS), "Featured Prods");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FEATURED_BRANDS), "Featured Brands");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.FEATURED_NEW_PRODBRANDS), "Featured New Prods/Brands");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.HOWTOCOOKIT_USAGE), "HTCI Usage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.WINE_FILTER), "Wine Filter Criteria");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.WINE_FILTER_VALUE), "Wine Filter Value");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDE_NAV_FULL_LIST), "Side Nav Full List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.WINE_SORTING), "Wine Sort Criteria");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDE_NAV_SECTIONS), "Side Nav Sections");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.HOW_TO_COOK_IT_PRODUCTS), "HTCI Prods");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RATING), "Rating");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RATING_HOME), "Rating Home Folder");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_MORE_USAGE), "More Usage Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_POP_USAGE), "Popular Usage Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_SIZE_PRICE), "Size & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_PRICE), "Taste & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_TYPE_PRICE), "Taste & Type & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TASTE_USE_PRICE), "Taste & Use & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TEXTURE_PRICE), "Texture & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_TYPE_PRICE), "Type & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_USAGE), "Usage Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.RG_USAGE_PRICE), "Usage & Price Rating Group");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CAT_LABEL), "Cat Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CAT_PHOTO), "Cat Photo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CAT_TITLE), "Cat Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_DETAIL_IMAGE), "Cat Detail Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.DEPT_MGR), "Dept Mgr");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.SIDENAV_IMAGE), "Side Nav Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_NAVBAR), "Category Navbar");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.EDITORIAL), "Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.ARTICLES), "Articles");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_BOTTOM_MEDIA), "Category Bottom Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_TOP_MEDIA), "Category Top Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CAT_STORAGE_GUIDE_MEDIA), "Cat Storage Guide Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.CATEGORY_PREVIEW_MEDIA), "Category Preview Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.productGrabbers), "Product Grabbers");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.productFilterGroups), "Product Filter Groups");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.sortOptions), "Sort Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.productTags), "Tags for Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.nameImage), "Name Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.description), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.categoryBanner), "Category Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.middleMedia), "Middle Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.catMerchantRecommenderProducts), "Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.globalNavIcon), "Global Nav Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.globalNavPostNameImage), "Global Nav Post Name Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.popularCategories), "Popular Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.tabletCallToActionBanner), "Call To Action Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.tabletThumbnailImage), "Tablet Thumbnail Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.heroCarousel), "Hero Carousel");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.featuredRecommenderTitle), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.featuredRecommenderRandomizeProducts), "Randomize Products (only if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.featuredRecommenderSiteFeature), "Site Feature (ignored if Source Category is set)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Category, ContentTypes.Category.featuredRecommenderSourceCategory), "Source Category");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.GLANCE_NAME), "Glance Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.NAV_NAME), "Nav Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ALT_TEXT), "Alt text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.KEYWORDS), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.AKA), "AKA Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SUBTITLE), "Subtitle");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.HIDE_URL), "Hide URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ALSO_SOLD_AS_NAME), "Also Sold As Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.REDIRECT_URL), "Redirect URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SALES_UNIT_LABEL), "Sales Unit Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.QUANTITY_TEXT), "Quantity Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.QUANTITY_TEXT_SECONDARY), "Secondary Quantity Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SERVING_SUGGESTION), "Serving Suggestion");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PACKAGE_DESCRIPTION), "Package Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SEASON_TEXT), "Season Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_FYI), "Wine FYI");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_REGION), "Wine Region");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_TYPE), "Wine Grape");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SEAFOOD_ORIGIN), "Seafood Origin");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RELATED_PRODUCTS_HEADER), "Related Products Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SHOW_SALES_UNIT_IMAGE), "Show Sales Unit Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.NUTRITION_MULTIPLE), "Nutrition Multiple");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SHOW_TOP_TEN_IMAGE), "Show Top Ten Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.NOT_SEARCHABLE), "Not Searchable");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.CONTAINER_WEIGHT_HALF_PINT), "Container Weight Half Pint");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.CONTAINER_WEIGHT_PINT), "Container Weight Pint");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.CONTAINER_WEIGHT_QUART), "Container Weight Quart");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.INCREMENT_MAX_ENFORCE), "Increment Max Enforce");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.QUANTITY_MINIMUM), "Quantity Minimum");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.QUANTITY_MAXIMUM), "Quantity Maximum");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.QUANTITY_INCREMENT), "Quantity Increment");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.INVISIBLE), "Invisible");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PERISHABLE), "Perishable");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FROZEN), "Frozen");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.GROCERY), "Grocery");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_PAGE_RATINGS), "Prod Page Ratings");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_PAGE_TEXT_RATINGS), "Prod Page Text Ratings");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RATING_PROD_NAME), "Prod Page Rating Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.DEFAULT_SUSTAINABILITY_RATING), "Show Default Sustainability Rating");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.EXCLUDED_EBT_PAYMENT), "Exclude for EBT Payment");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.HIDE_IPHONE), "Hide from IPhone");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_CLASSIFICATION), "Wine Classification Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_IMPORTER), "Wine Importer");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_ALCH_CONTENT), "Wine Alchohol Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_AGING), "Wine Aging Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_CITY), "Wine City");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.EXCLUDED_RECOMMENDATION), "Excluded Recommendation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.DISABLED_RECOMMENDATION), "Disabled Recommendation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.disableAtpFailureRecommendation), "Disable ATP Failure Recommendation");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.retainOriginalSkuOrder), "Retain Original SKU Order");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SEO_META_DESC), "SEO Meta Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PAGE_TITLE), "Page Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SEO_META_DESC_FDX), "SEO Meta Description FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PAGE_TITLE_FDX), "Page Title FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SKIP_SITEMAP), "Skip Sitemap");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PAIR_IT_HEADING), "PAIR IT - HEADING");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PAIR_IT_TEXT), "PAIR IT - TEXT");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.TIME_TO_COMPLETE), "Time To Complete");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_LAYOUT), "Prod Layout");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_LAYOUT), new HashMap<String, String>() {{
            put("1", "Perishable Product");
            put("3", "Wine Product");
            put("5", "Party Platter");
            put("6", "Multi Menu Layout");
            put("7", "Component Group Meal");
            put("8", "New Wine Product");
            put("10", "Holiday Meal Bundle");
            put("11", "Recipe Mealkit");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.LAYOUT), "Layout");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.LAYOUT), new HashMap<String, String>() {{
                put("0", "Generic");
                put("1", "Horizontal");
                put("2", "Vertical");
                put("3", "Featured_All");
                put("4", "Featured_Category");
                put("5", "By Region");
                put("6", "Product Sort");
                put("7", "Product_Folder_ List");
                put("8", "Coffee_Cheese Department");
                put("9", "Grocery_Style_Department");
                put("10", "Grocery_Style_Category");
                put("11", "Grocery _Style_Product");
                put("12", "Bulk_Meat");
                put("13", "How_To_Cook_It");
                put("15", "Multi_Category_Layout");
                put("16", "Transactional Multi Category");
                put("18", "Transactional_Paired_Items");
                put("19", "Holiday Menu");
                put("20", "Composite Category 1");
                put("21", "ThanksGiving Category");
                put("24", "Package Layout 1");
                put("25", "All Static Media");
                put("29", "Meat Dept");
                put("31", "Meat Category");
                put("97", "Media Include");
                put("111", "Multi Category Layout (with Quickbuy)");
                put("201", "E-Coupons");
                put("202", "Products Assortments");
                put("101", "Transactional_multi_Paired_Items");
                put("102", "Template_Layout");
                put("40", "4minute-meals landing page");
                put("41", "4minute-meals restaurant page");
                put("103", "Wine Deals");
                put("104", "Wine Expert's Favs");
                put("42", "Bakery department");
                put("100", "Wine_Category");
                put("200", "President's Picks");
                put("301", "Recipe Mealkit Category");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.TEMPLATE_TYPE), "Template Type");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.TEMPLATE_TYPE), new HashMap<String, String>() {{
            put("1", "Fresh Direct Default Template");
            put("2", "FreshDirect Best Cellars Template");
            put("3", "Media");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SELL_BY_SALESUNIT), "Sell by Sales Unit");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SELL_BY_SALESUNIT), new HashMap<String, String>() {{
            put("BOTH", "Both");
            put("BOTH_SEPARATE", "Both Separate");
            put("QUANTITY", "Quantity");
            put("SALES_UNIT", "Sales Unit");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SS_EXPERT_WEIGHTING), "Expert Weighting");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SS_EXPERT_WEIGHTING), new HashMap<String, String>() {{
            put("-5", "-5");
            put("-4", "-4");
            put("-3", "-3");
            put("-2", "-2");
            put("-1", "-1");
            put("0", "0");
            put("1", "1");
            put("2", "2");
            put("3", "3");
            put("4", "4");
            put("5", "5");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.browseRecommenderType), "Browse Recommender Type");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.browseRecommenderType), new HashMap<String, String>() {{
            put("NONE", "None");
            put("PDP_XSELL", "Cross-sell (first item)");
            put("PDP_UPSELL", "Upsell (first item)");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.HEAT_RATING), "Heat Rating");
        ENUM_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.HEAT_RATING), new HashMap<String, String>() {{
            put("-1", "N/A");
            put("0", "No Heat");
            put("1", "Minor Heat");
            put("2", "Subtle Heat");
            put("3", "Medium Heat");
            put("4", "Strong Heat");
            put("5", "Fiery Heat");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.skus), "Skus");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.brands), "Brands");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.youMightAlsoLike), "You Might Also Like");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WE_RECOMMEND_TEXT), "We Recommend Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WE_RECOMMEND_IMAGE), "We Recommend Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RELATED_PRODUCTS), "Related Prods");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RECOMMENDED_ALTERNATIVES), "Recommended Alternatives");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RELATED_RECIPES), "Related Recipes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ymalSets), "YMAL sets");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_BUNDLE), "Prod Bundle");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ALSO_SOLD_AS), "Also Sold As");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.HOWTOCOOKIT_FOLDERS), "HTCI Folders");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRIMARY_HOME), "Primary Home");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PERFECT_PAIR), "Perfect Pair");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PREFERRED_SKU), "Preferred SKU");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RATING), "Ratings");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_NEW_TYPE), "Wine Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_VINTAGE), "Wine Vintage");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_NEW_REGION), "Wine Region");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_VARIETAL), "Wine Varietal");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_RATING1), "Wine Rating 1");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_RATING2), "Wine Rating 2");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_RATING3), "Wine Rating 3");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.USAGE_LIST), "Usage List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.UNIT_OF_MEASURE), "Unit of Measure");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.VARIATION_MATRIX), "Variation Matrix");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.VARIATION_OPTIONS), "Variation Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_COUNTRY), "Wine Country");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE), "Prod Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_CONFIRM), "Prod Image Confirm");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_DETAIL), "Prod Image Detail");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_FEATURE), "Prod Image Feature");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_ZOOM), "Prod Image Zoom");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RATING_RELATED_IMAGE), "Rating Related Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ALTERNATE_IMAGE), "Alternate Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.DESCRIPTIVE_IMAGE), "Descriptive Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_ROLLOVER), "Prod Image Rollover");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_PACKAGE), "Product Image Package");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_JUMBO), "Prod Image Jumbo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_ITEM), "Prod Image Item");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_IMAGE_EXTRA), "Prod Image Extra");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.ALTERNATE_PROD_IMAGE), "Alternate Product Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_ABOUT), "Prod About");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_DESCR), "Prod Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.RECOMMEND_TABLE), "Recommend Table");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_QUALITY_NOTE), "Prod Quality Note");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PROD_DESCRIPTION_NOTE), "Prod Description Note");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FRESH_TIPS), "Fresh Tips");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.DONENESS_GUIDE), "Doneness Guide");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FDDEF_FRENCHING), "FD Def Frenching");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FDDEF_GRADE), "FD Def Grade");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FDDEF_RIPENESS), "FD Def Ripeness");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.FDDEF_SOURCE), "FD Def Source");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SALES_UNIT_DESCRIPTION), "Sales Unit Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PARTIALLY_FROZEN), "Partially Frozen");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.COMPONENT_GROUPS), "Component Groups");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_TERMS_MEDIA), "Product Confirmation Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.MEDIA_CONTENT), "Media Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_REVIEW1), "Wine Review 1");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_REVIEW2), "Wine Review 2");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.WINE_REVIEW3), "Wine Review 3");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PRODUCT_BOTTOM_MEDIA), "Bottom Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.GIFTCARD_TYPE), "Gift Card Types");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SCARAB_YMAL_INCLUDE), "Include Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SCARAB_YMAL_EXCLUDE), "Exclude Products or Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SCARAB_YMAL_PROMOTE), "Promote Products or Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.SCARAB_YMAL_DEMOTE), "Demote Products or Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.tags), "Tags");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PDP_UPSELL), "Upsell");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.PDP_XSELL), "Cross-Sell");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.completeTheMeal), "Complete The Meal");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Product, ContentTypes.Product.MEAL_INCLUDE_PRODUCTS), "Meal Include Products");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.ORGANIC), "Organic");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.KOSHER), "Kosher");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.GLANCE_NAME), "Glance Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.NAV_NAME), "Nav Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.VARIATION_MATRIX), "Variation Matrix");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.VARIATION_OPTIONS), "Variation Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.brands), "Brands");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, ContentTypes.Sku.materials), "Materials");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, VirtualAttributes.Sku.materialVersion), "Version");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, VirtualAttributes.Sku.promoPrice), "Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Sku, VirtualAttributes.Sku.UNAVAILABILITY_STATUS), "Unavailability Status");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.SALES_UNIT), "Sales Unit");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.QUANTITY), "Quantity");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.OPTIONS), "Configuration Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.REQUIRED), "Required product");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.GLANCE_NAME), "Glance Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.NAV_NAME), "Nav Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.ALSO_SOLD_AS_NAME), "Also Sold As Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.SUBTITLE), "Subtitle");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.INVISIBLE), "Invisible");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PACKAGE_DESCRIPTION), "Package Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.SKU), "Sku");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_DESCR), "Prod Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_DESCRIPTION_NOTE), "Prod Description Note");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PRODUCT_QUALITY_NOTE), "Prod Quality Note");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE), "Prod Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_CONFIRM), "Prod Image Confirm");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_DETAIL), "Prod Image Detail");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_FEATURE), "Prod Image Feature");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_ZOOM), "Prod Image Zoom");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.ALTERNATE_IMAGE), "Alternate Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.DESCRIPTIVE_IMAGE), "Descriptive Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.WINE_COUNTRY), "Wine Country");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.ALSO_SOLD_AS), "Also Sold As");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.RELATED_PRODUCTS), "Related Prods");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.ymalSets), "YMAL sets");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_JUMBO), "Prod Image Jumbo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_ITEM), "Prod Image Item");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PROD_IMAGE_EXTRA), "Prod Image Extra");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PDP_UPSELL), "Upsell");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProduct, ContentTypes.ConfiguredProduct.PDP_XSELL), "Cross-Sell");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.unavailabilityMessage), "Unavailability Message");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.required), "Required");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ConfiguredProductGroup, ContentTypes.ConfiguredProductGroup.items), "Items");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.SHOW_OPTIONS), "Show Options");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.COMPONENTGROUP_LAYOUT), "Component Group Layout");
        ENUM_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.COMPONENTGROUP_LAYOUT), new HashMap<String, String>() {{
            put("0", "Horizontal");
            put("1", "Vertical");
            put("2", "Show in Popup Only");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.HEADER_IMAGE), "Header Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.EDITORIAL), "Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.CHARACTERISTICS), "Characteristics");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.OPTIONAL_PRODUCTS), "Optional Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ComponentGroup, ContentTypes.ComponentGroup.CHEFS_PICKS), "Chefs Picks");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.alcoholicContent), "Alcoholic Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.atpRule), "ATP Rule");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.blockedDays), "Blocked Days");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.DESCRIPTION), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.kosher), "Kosher Production");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.leadTime), "Lead Time");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.platter), "Platter");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.taxable), "Taxable");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.UPC), "UPC");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.NAME), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpMaterial, VirtualAttributes.ErpMaterial.salesUnits), "Sales Units");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpCharacteristic, VirtualAttributes.ErpCharacteristic.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpCharacteristic, VirtualAttributes.ErpCharacteristic.values), "Characteristic Values");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpCharacteristic, VirtualAttributes.ErpCharacteristicValue.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ErpCharacteristic, VirtualAttributes.ErpCharacteristicValue.name), "Name");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.GLANCE_NAME), "Glance Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.NAV_NAME), "Nav Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.ALT_TEXT), "Alt Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.KEYWORDS), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.CHEF_NAME), "Chef name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.CHEF_BLURB), "Chef blurb text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletCopyright), "Tablet Copyright");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.FEATURED_PRODUCTS), "Featured Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.FEATURED_CATEGORIES), "Featured Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.BRAND_LOGO), "Large Brand Logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.BRAND_LOGO_SMALL), "Small Brand Logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.BRAND_LOGO_MEDIUM), "Medium Brand Logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.CHEF_IMAGE), "Chef image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.BRAND_POPUP_CONTENT), "Brand Popup Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletHeader), "Tablet Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletImages), "Tablet Images");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletAboutTextShort), "Tablet About Text Short");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletAboutTextLong), "Tablet About Text Long");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.tabletThumbnailImage), "Tablet Thumbnail Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Brand, ContentTypes.Brand.producer), "Brand Producer");


        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.categories), "Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.departmentNav), "Department Nav");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.featuredSource), "Featured Source");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.featuredRecipeCategory), "Featured Recipe Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.featuredProductCategory), "Featured Product Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeDepartment, ContentTypes.RecipeDepartment.editorial), "Editorial");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.SECONDARY_CATEGORY), "Secondary Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.subcategories), "Subcategories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.classification), "Classification");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.featuredSource), "Featured Source");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.zoomLabel), "Zoom Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.editorial), "Editorial");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeCategory, ContentTypes.RecipeCategory.photo), "Photo");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.REDIRECT_URL), "Redirect URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.classification), "Classification");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.groupBy), "Group By");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.filterBy), "Filter By");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.featuredRecipes), "Featured Recipes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.featuredProducts), "Featured Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSubcategory, ContentTypes.RecipeSubcategory.editorial), "Editorial");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.keywords), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.startDate), "Start Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.endDate), "End Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.theme_color), "Theme");
        ENUM_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.theme_color), new HashMap<String, String>() {{
            put("DEFAULT", "Default");
            put("6699CC", "Real Simple");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.productionStatus), "Production Status");
        ENUM_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.productionStatus), new HashMap<String, String>() {{
            put("PENDING", "Pending Approval");
            put("ACTIVE", "Active");
            put("LIMITED", "Limited");
            put("COMPLETED", "Completed");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.source), "Source");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.authors), "Authors");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.variants), "Variants");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.classifications), "Classifications");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.RELATED_PRODUCTS), "Related Prods");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.ymalSets), "YMAL sets");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.description), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.ingredientsMedia), "Ingredients Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.preparationMedia), "Preparation Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.titleImage), "Title Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.logo), "Logo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.photo), "Photo");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.copyrightMedia), "Copyright Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recipe, ContentTypes.Recipe.tabletThumbnailImage), "Tablet Thumbnail Image");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeVariant, ContentTypes.RecipeVariant.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeVariant, ContentTypes.RecipeVariant.sections), "Sections");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSection, ContentTypes.RecipeSection.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSection, ContentTypes.RecipeSection.SHOW_QUANTITY), "Show Quantity");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSection, ContentTypes.RecipeSection.ingredients), "Ingredients");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.ISBN), "Isbn");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.productionStatus), "Production Status");
        ENUM_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.productionStatus), new HashMap<String, String>() {{
            put("PENDING", "Pending Approval");
            put("ACTIVE", "Active");
            put("LIMITED", "Limited");
            put("COMPLETED", "Completed");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.authors), "Authors");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.featuredRecipes), "Featured Recipes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.featuredProducts), "Featured Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.leftContent), "Left Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.topContent), "Top Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.bottomContent), "Bottom Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.emailContent), "Email Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.image), "Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.zoomImage), "Zoom Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSource, ContentTypes.RecipeSource.bookRetailers), "Book Retailers");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeAuthor, ContentTypes.RecipeAuthor.firstName), "First Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeAuthor, ContentTypes.RecipeAuthor.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeAuthor, ContentTypes.RecipeAuthor.notes), "Notes");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.title), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.startDate), "Start Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.endDate), "End Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.transactional), "Transactional");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.productsHeader), "Products Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.workflowStatus), "Workflow Status");
        ENUM_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.workflowStatus), new HashMap<String, String>() {{
            put("PENDING_APPROVAL", "Pending Approval");
            put("ACTIVE", "Active");
            put("COMPLETED", "Completed");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.ymals), "Ymals");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YmalSet, ContentTypes.YmalSet.recommenders), "Recommenders");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.FULL_NAME), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.BLURB), "Blurb");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.startDate), "Start Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.endDate), "End Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.productionStatus), "Production Status");
        ENUM_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.productionStatus), new HashMap<String, String>() {{
            put("PENDING", "Pending Approval");
            put("ACTIVE", "Active");
            put("COMPLETED", "Completed");
            put("LIMITED", "Limited");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.StarterList, ContentTypes.StarterList.listContents), "List Contents");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.BookRetailer, ContentTypes.BookRetailer.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.BookRetailer, ContentTypes.BookRetailer.notes), "Notes");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.BookRetailer, ContentTypes.BookRetailer.isbnLink), "Isbn Link");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.BookRetailer, ContentTypes.BookRetailer.logo), "Logo");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchPage, ContentTypes.RecipeSearchPage.criteria), "Search Criteria");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchPage, ContentTypes.RecipeSearchPage.filterByDomains), "Filter By Domains");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.selectionType), "Selection Type");
        ENUM_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.selectionType), new HashMap<String, String>() {{
            put("ONE", "Single");
            put("MANY", "Multiple");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.logicalOperation), "Logical Operation");
        ENUM_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.logicalOperation), new HashMap<String, String>() {{
            put("AND", "AND");
            put("OR", "OR");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeSearchCriteria, ContentTypes.RecipeSearchCriteria.criteriaDomainValues), "Domain Values");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Synonym, ContentTypes.Synonym.word), "Word(s)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Synonym, ContentTypes.Synonym.synonymValue), "Equivalent Substitute(s)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Synonym, ContentTypes.Synonym.bidirectional), "Bidirectional?");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SpellingSynonym, ContentTypes.SpellingSynonym.word), "Word");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SpellingSynonym, ContentTypes.SpellingSynonym.synonymValue), "Synonym To");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchRelevancyList, ContentTypes.SearchRelevancyList.Keywords), "Keywords");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchRelevancyList, ContentTypes.SearchRelevancyList.categoryHints), "Categories");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchRelevancyHint, ContentTypes.SearchRelevancyHint.score), "Score");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchRelevancyHint, ContentTypes.SearchRelevancyHint.category), "Category");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.WordStemmingException, ContentTypes.WordStemmingException.word), "Bad Singular Form");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.FavoriteList, ContentTypes.FavoriteList.full_name), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.FavoriteList, ContentTypes.FavoriteList.favoriteItems), "Favorite Items");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recommender, ContentTypes.Recommender.FULL_NAME), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recommender, ContentTypes.Recommender.strategy), "Recommender Strategy");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Recommender, ContentTypes.Recommender.scope), "Scope");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.FULL_NAME), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.generator), "Generator expression");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.scoring), "Scoring (ranking) expression");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.top_n), "Top N");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.top_percent), "Top percent");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.exponent), "Exponent (for power sampling)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.show_temp_unavailable), "Show Temporary Unavailable");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.brand_uniq_sort), "Brand Uniqueness Sorting");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.sampling), "Sampling strategy");
        ENUM_LABELS.put(keyOf(ContentType.RecommenderStrategy, ContentTypes.RecommenderStrategy.sampling), new HashMap<String, String>() {{
            put("deterministic", "Deterministic");
            put("uniform", "Uniform");
            put("linear", "Linear");
            put("quadratic", "Quadratic");
            put("cubic", "Cubic");
            put("harmonic", "Harmonic");
            put("sqrt", "Square root");
            put("power", "Power CDF");
            put("complicated", "Complicated");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.FULL_NAME), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.ADDRESS), "Address");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.GMAPS_LOCATION), "Google Maps Location");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.bubble_content), "Bubble Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.icon), "Icon");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.icon_shadow), "Icon Shadow");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.producer_type), "Producer Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.brand_category), "Brand Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Producer, ContentTypes.Producer.brand), "Brand");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProducerType, ContentTypes.ProducerType.FULL_NAME), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProducerType, ContentTypes.ProducerType.icon), "Icon");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProducerType, ContentTypes.ProducerType.icon_shadow), "Icon Shadow");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.TileList, ContentTypes.TileList.filter), "Filter");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.TileList, ContentTypes.TileList.tiles), "Tiles");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Tile, ContentTypes.Tile.GGF_TYPE), "Goes Great With");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Tile, ContentTypes.Tile.media), "Media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Tile, ContentTypes.Tile.quick_buy), "Quick Buy");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.HolidayGreeting, ContentTypes.HolidayGreeting.CODE), "Event/Occasion Code in Profile Survey");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.HolidayGreeting, ContentTypes.HolidayGreeting.FULL_NAME), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.HolidayGreeting, ContentTypes.HolidayGreeting.startDate), "Start Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.HolidayGreeting, ContentTypes.HolidayGreeting.endDate), "End Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.HolidayGreeting, ContentTypes.HolidayGreeting.GREETING_TEXT), "Greeting Text");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.BLOG_URL), "Base URL of the Blog");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.BLOG_ENTRY_COUNT), "No. of Blog Entries to Display");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.POLL_DADDY_API_KEY), "PollDaddy API Key");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.HEADER), "MyFD Header");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.HOLIDAY_GREETINGS), "Holiday Greetings");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.EDITORIAL_MAIN), "Editorial for Main Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MyFD, ContentTypes.MyFD.EDITORIAL_SIDE), "Editorial for Side Section");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuItem, ContentTypes.GlobalMenuItem.TITLE_LABEL), "Global Menu Title Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuItem, ContentTypes.GlobalMenuItem.LAYOUT), "Layout");
        ENUM_LABELS.put(keyOf(ContentType.GlobalMenuItem, ContentTypes.GlobalMenuItem.LAYOUT), new HashMap<String, String>() {{
            put("0", "Editorial");
            put("1", "Single Section Layout");
            put("2", "Two Section Layout");
            put("3", "Four Section Layout");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuItem, ContentTypes.GlobalMenuItem.subSections), "Sub Menu Sections");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuItem, ContentTypes.GlobalMenuItem.editorial), "Editorial");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuSection, ContentTypes.GlobalMenuSection.SHOW_ALL_SUB_CATEGORIES), "Show All Sub Categories");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuSection, ContentTypes.GlobalMenuSection.linkedProductContainer), "Linked Product Container");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuSection, ContentTypes.GlobalMenuSection.subCategoryItems), "Sub Category Items");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.GlobalMenuSection, ContentTypes.GlobalMenuSection.editorial), "Editorial");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.YoutubeVideo, ContentTypes.YoutubeVideo.YOUTUBE_VIDEO_ID), "YouTube Video Id");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YoutubeVideo, ContentTypes.YoutubeVideo.TITLE), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.YoutubeVideo, ContentTypes.YoutubeVideo.CONTENT), "Content");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Page, ContentTypes.Page.title), "Page title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Page, ContentTypes.Page.showSideNav), "Show Left Side Nav Bar");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Page, ContentTypes.Page.media), "Media list");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Page, ContentTypes.Page.subPages), "Subpages");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductGrabber, ContentTypes.ProductGrabber.productFilters), "ProductFilters");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductGrabber, ContentTypes.ProductGrabber.scope), "Scope");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.allSelectedLabel), "All Selected Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.displayOnCategoryListingPage), "Display On Category Listing Page");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.type), "Type");
        ENUM_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.type), new HashMap<String, String>() {{
            put("SINGLE", "Single Select");
            put("POPUP", "Popup");
            put("MULTI", "Multi-select (additive)");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterGroup, ContentTypes.ProductFilterGroup.productFilters), "Product Filters");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.invert), "Invert");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.fromValue), "From Value");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.toValue), "To Value");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.nutritionCode), "Nutrition Code (only for Nutrition filter)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.erpsyFlagCode), "ERPSy Flag Code");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.type), "Type");
        ENUM_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.type), new HashMap<String, String>() {{
            put("AND", "AND (Filter Combination)");
            put("OR", "OR (Filter Combination)");
            put("ALLERGEN", "Allergen (ERPSy Flag)");
            put("BACK_IN_STOCK", "Back In Stock");
            put("GOING_OUT_OF_STOCK", "Going Out Of Stock");
            put("BRAND", "Brand");
            put("CLAIM", "Claim (ERPSy Flag)");
            put("CUSTOMER_RATING", "Customer Rating (Range)");
            put("DOMAIN_VALUE", "Domain Value");
            put("EXPERT_RATING", "Expert Rating (Range)");
            put("FRESHNESS", "Freshness (Range)");
            put("KOSHER", "Kosher");
            put("NEW", "New");
            put("NUTRITION", "Nutrition (Range)");
            put("ON_SALE", "On Sale");
            put("ORGANIC", "Organic (ERPSy Flag)");
            put("PRICE", "Price (Range)");
            put("SUSTAINABILITY_RATING", "Sustainability Rating (Range)");
            put("TAG", "Tag");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.domainValue), "Domain Value");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.tag), "Tag");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.brand), "Brand");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilter, ContentTypes.ProductFilter.filters), "Filters");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Tag, ContentTypes.Tag.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Tag, ContentTypes.Tag.children), "Children");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level1Name), "Level 1 Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level1AllSelectedLabel), "Level 1 All Selected Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level2Name), "Level 2 Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level2AllSelectedLabel), "Level 2 All Selected Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level1Type), "Level 1 Type");
        ENUM_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level1Type), new HashMap<String, String>() {{
            put("SINGLE", "Single Select");
            put("POPUP", "Popup");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level2Type), "Level 2 Type");
        ENUM_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.level2Type), new HashMap<String, String>() {{
            put("SINGLE", "Single Select");
            put("POPUP", "Popup");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ProductFilterMultiGroup, ContentTypes.ProductFilterMultiGroup.rootTag), "Root Tag");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SortOption, ContentTypes.SortOption.label), "Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SortOption, ContentTypes.SortOption.selectedLabel), "Selected Label");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SortOption, ContentTypes.SortOption.selectedLabelReverseOrder), "Selected Label Reverse Order");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SortOption, ContentTypes.SortOption.strategy), "Strategy");
        ENUM_LABELS.put(keyOf(ContentType.SortOption, ContentTypes.SortOption.strategy), new HashMap<String, String>() {{
            put("CUSTOMER_RATING", "Customer Rating");
            put("EXPERT_RATING", "Expert Rating");
            put("NAME", "Name");
            put("POPULARITY", "Popularity");
            put("PRICE", "Price");
            put("SALE", "Sale");
            put("SUSTAINABILITY_RATING", "Sustainability Rating");
            put("DEPARTMENT", "Department");
            put("E_COUPON_DOLLAR_DISCOUNT", "e-Coupon Dollar Discount");
            put("E_COUPON_EXPIRATION_DATE", "e-Coupon Expiration Date");
            put("E_COUPON_PERCENT_DISCOUNT", "e-Coupon Percent Discount");
            put("E_COUPON_POPULARITY", "e-Coupon Popularity");
            put("E_COUPON_START_DATE", "e-Coupon Start Date");
            put("RECENCY", "Recency");
            put("SEARCH_RELEVANCY", "Search Relevancy");
            put("FAVS_FIRST", "Favs First");
            put("CUSTOMER_POPULARITY", "Customer Popularity");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Banner, ContentTypes.Banner.location), "Location");
        ENUM_LABELS.put(keyOf(ContentType.Banner, ContentTypes.Banner.location), new HashMap<String, String>() {{
            put("EMPTY", "Empty");
            put("CHECKOUT", "Checkout");
            put("HOMEPAGE", "Homepage");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Banner, ContentTypes.Banner.image), "Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Banner, ContentTypes.Banner.link), "Link");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchSuggestionGroup, ContentTypes.SearchSuggestionGroup.searchTerms), "Search Terms (separated by semicolons)");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.SearchSuggestionGroup, ContentTypes.SearchSuggestionGroup.tabletImage), "Tablet Image");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeTag, ContentTypes.RecipeTag.tagId), "Tag ID");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.RecipeTag, ContentTypes.RecipeTag.tabletImage), "Tablet Image");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.PAGE_TITLE), "Page Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.SEO_META_DESC), "SEO Meta Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.PAGE_TITLE_FDX), "Page Title FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.SEO_META_DESC_FDX), "SEO Meta Description FDX");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.URL), "URL of the  page");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.SKIP_SITEMAP), "Skip Sitemap");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.WebPageType), "WebPage Type");
        ENUM_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.WebPageType), new HashMap<String, String>() {{
                put("Feed", "Feed");
                put("TodaysPick", "TodaysPick");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.WebPageSection), "Sections");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.WebPageSchedule), "Schedule");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.WebPage, ContentTypes.WebPage.WebPageDarkStore), "Dark Store");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.EndTime), "End Time");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.StartDate), "Start Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.EndDate), "End Date");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.StartTime), "Start Time");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.Day), "Day");
        ENUM_LABELS.put(keyOf(ContentType.Schedule, ContentTypes.Schedule.Day), new HashMap<String, String>() {{
            put("AllDay", "All Day");
            put("Sunday", "Sunday");
            put("Monday", "Monday");
            put("Tuesday", "Tuesday");
            put("Wednesday", "Wednesday");
            put("Thursday", "Thursday");
            put("Friday", "Friday");
            put("Saturday", "Saturday");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.name), "Full Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.captionText), "Caption Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.headlineText), "Headline Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.bodyText), "Body Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.linkText), "Link Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.linkURL), "Link URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.linkType), "Link Type");
        ENUM_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.linkType), new HashMap<String, String>() {{
            put("Link", "Link type in Mobile Feed");
            put("Button", "Button type in Mobile Feed");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.displayType), "Display Type");
        ENUM_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.displayType), new HashMap<String, String>() {{
            put("Greeting", "Greeting display type in Mobile Feed");
            put("HorizontalPickList", "Featured Product display type in Mobile Feed");
            put("VerticalPickList", "Vertical display type in Mobile Feed");
            put("ShortBanner", "ShortBanner display type in Mobile Feed");
        }});

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.drawer), "Drawer?");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.imageBanner), "Image Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.product), "Product");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.mustHaveProduct), "Must Have Product");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.pickList), "Picks List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.category), "Category");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.linkTarget), "Link Target");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.SectionSchedule), "Schedule");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Section, ContentTypes.Section.SectionDarkStore), "Dark Store");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Anchor, ContentTypes.Anchor.Url), "Url");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Anchor, ContentTypes.Anchor.Text), "Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Anchor, ContentTypes.Anchor.Type), "Anchor Type");
        ENUM_LABELS.put(keyOf(ContentType.Anchor, ContentTypes.Anchor.Type), new HashMap<String, String>() {{
            put("Button", "Button");
            put("Link", "Link");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Anchor, ContentTypes.Anchor.Target), "Target");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.TextComponent, ContentTypes.TextComponent.Text), "Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.TextComponent, ContentTypes.TextComponent.Type), "Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.TextComponent, ContentTypes.TextComponent.Default), "Default");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.Name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.Type), "Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.Description), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.FlagText), "Flag Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.FlagColor), "Flag Color");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.Price), "Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkOneText), "Link 1 Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkOneURL), "Link 1 URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkOneType), "Link 1 Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkTwoText), "Link 2 Text");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkTwoURL), "Link 2 URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkTwoType), "Link 2 Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.bannerURL), "Banner URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.ImageBannerImage), "Image");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.ImageBannerLink), "Links");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.ImageBannerBurst), "Burst");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.ImageBannerProduct), "Product");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.Target), "Target");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkOneTarget), "Link 1 Target");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ImageBanner, ContentTypes.ImageBanner.linkTwoTarget), "Link 2 Target");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.Name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PricingZone), "Pricing Zone");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.DistributionChannel), "Distribution Channel");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.SalesOrganization), "Sales Organization");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.DisplayName), "Display Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.Type), "Type");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.Description), "Description");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListPickListItem), "PickList Items");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListMedia), "Image Banners");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListSchedule), "Schedule");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListPickList), "Pick List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListProduct), "Products");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickList, ContentTypes.PickList.PickListCategory), "Category");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickListItem, ContentTypes.PickListItem.Default), "Default");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.PickListItem, ContentTypes.PickListItem.PickListItemProduct), "Product");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.DarkStore, ContentTypes.DarkStore.value), "Select Dark Store");
        ENUM_LABELS.put(keyOf(ContentType.DarkStore, ContentTypes.DarkStore.value), new HashMap<String, String>() {

            {
                put("1300", "1300-NYC-Brooklyn");
                put("1310", "1310-NYC-Manhattan");
            }
        });
        ATTRIBUTE_LABELS.put(keyOf(ContentType.DarkStore, ContentTypes.DarkStore.name), "Name");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleContainer, ContentTypes.ModuleContainer.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleContainer, ContentTypes.ModuleContainer.modulesAndGroups), "Modules and Groups");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.moduleGroupTitle), "Module Group Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.moduleGroupTitleTextBanner), "Module Group Title Text Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.viewAllButtonURL), "View All Button URL");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.hideViewAllButton), "Hide View All Button");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.modules), "Modules");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.ModuleGroup, ContentTypes.ModuleGroup.viewAllSourceNode), "View All Source Node");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.moduleTitle), "Module Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.moduleTitleTextBanner), "Module Title Text Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.viewAllButtonLink), "View All Button Link");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.contentTitle), "Content Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.contentTitleTextBanner), "Content Title Text Banner");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.heroTitle), "Hero Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.heroSubtitle), "Hero Subtitle");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.headerTitle), "Header Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.headerSubtitle), "Header Subtitle");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.hideViewAllButton), "Hide View All Button");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.hideProductName), "Hide Product Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.hideProductPrice), "Hide Product Price");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.hideProductBadge), "Hide Product Badge");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.useViewAllPopup), "Show View all Overlay");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.showViewAllOverlayOnImages), "Show View all Overlay On Images");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.productSourceType), "Product Source Type");
        ENUM_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.productSourceType), new HashMap<String, String>() {{
            put("GENERIC", "GENERIC");
            put("BROWSE", "BROWSE");
            put("TOP_ITEMS", "TOP_ITEMS");
            put("PRES_PICKS", "PRES_PICKS");
            put("FEATURED_RECOMMENDER", "FEATURED_RECOMMENDER");
            put("MOST_POPULAR_PRODUCTS", "MOST_POPULAR_PRODUCTS");
            put("BRAND_FEATURED_PRODUCTS", "BRAND_FEATURED_PRODUCTS");
            put("STAFF_PICKS", "STAFF_PICKS");
            put("CRITEO", "CRITEO");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.displayType), "Display Type");
        ENUM_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.displayType), new HashMap<String, String>() {{
            put("PRODUCT_CAROUSEL_MODULE", "PRODUCT_CAROUSEL_MODULE");
            put("PRODUCT_LIST_MODULE", "PRODUCT_LIST_MODULE");
            put("IMAGEGRID_MODULE", "IMAGEGRID_MODULE");
            put("ICON_CAROUSEL_MODULE", "ICON_CAROUSEL_MODULE");
            put("OPENHTML_MODULE", "OPENHTML_MODULE");
            put("EDITORIAL_MODULE", "EDITORIAL_MODULE");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.productListRowMax), "Maximum Row number of ProductList");
        ENUM_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.productListRowMax), new HashMap<String, String>() {{
            put("1", "1");
            put("2", "2");
            put("3", "3");
        }});
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.imageGrid), "Image Grid");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.openHTML), "Open HTML media");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.iconList), "Icon List");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.heroGraphic), "Hero Graphic");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.headerGraphic), "Header Graphic");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.editorialContent), "Editorial Content");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Module, ContentTypes.Module.sourceNode), "Source Node");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Image, ContentTypes.Image.height), "Height");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Image, ContentTypes.Image.width), "Width");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Image, ContentTypes.Image.path), "Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Image, ContentTypes.Image.lastmodified), "Last Modified");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Html, ContentTypes.Html.title), "Title");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Html, ContentTypes.Html.popupSize), "Popup Size");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Html, ContentTypes.Html.path), "Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Html, ContentTypes.Html.lastmodified), "Last Modified");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.Template, ContentTypes.Template.path), "Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.Template, ContentTypes.Template.lastmodified), "Last Modified");

        ATTRIBUTE_LABELS.put(keyOf(ContentType.MediaFolder, ContentTypes.MediaFolder.name), "Name");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MediaFolder, ContentTypes.MediaFolder.path), "Path");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MediaFolder, ContentTypes.MediaFolder.lastmodified), "Last Modified");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MediaFolder, ContentTypes.MediaFolder.subFolders), "Sub Folders");
        ATTRIBUTE_LABELS.put(keyOf(ContentType.MediaFolder, ContentTypes.MediaFolder.files), "Files");

    }
}

