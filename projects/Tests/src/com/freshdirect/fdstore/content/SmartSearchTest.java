package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.AutoComplete;
import com.freshdirect.cms.search.LuceneSearchService;
import com.freshdirect.cms.search.LuceneSearchServiceTest;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.aspects.FDFactoryZoneInfoAspect;
import com.freshdirect.fdstore.aspects.ProductStatisticUserProviderAspect;
import com.freshdirect.fdstore.aspects.ScoreFactorGlobalNameAspect;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.util.SearchNavigator;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockPageContext;

public class SmartSearchTest extends TestCase {

    private final static Logger LOGGER = Logger.getLogger(SmartSearchTest.class);

    public SmartSearchTest(String name) {
        super(name);
    }

    boolean                alreadyInited = false;

    ContentServiceI        service;
    LuceneSearchService    searchService;

    MockPageContext        ctx;
    MockHttpServletRequest request;
    SmartSearchTag         sst;

    public void setUp() throws Exception {
        AutoComplete.setDisableAutocompleter(true);

        Context context = TestUtils.createContext();
        
        TestUtils.createMockContainer(context);

        TestUtils.createTransaction(context);

        AspectSystem aspectSystem = TestUtils.createAspectSystem();

        //aspectSystem.add(new ErpCustomerFinderAspect(null));
        

        LOGGER.info("CMS init");
        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");

        List indexes = new ArrayList();
        AttributeIndex index = new AttributeIndex();
        index.setContentType("Product");
        index.setAttributeName("FULL_NAME");
        indexes.add(index);

        searchService = LuceneSearchServiceTest.createSearchService(indexes, LuceneSearchServiceTest.createTempDir(SmartSearchTest.class.getName(), "tmp"));

        Map contentNodes = service.getContentNodes(service.getContentKeys());
        searchService.index(contentNodes.values());

        CmsManager.setInstance(new CmsManager(service, searchService));

        aspectSystem.add(new ScoreFactorGlobalNameAspect(Collections.singleton(ScoreProvider.GLOBAL_POPULARITY)));
        
        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("DAI0008813", 2.0).addAvailableSku("DAI0008812", 3.0).addAvailableSku("CAN0062899",
                4.0).addAvailableSku("DAI0059088", 5.0));

        aspectSystem.add(new FDFactoryZoneInfoAspect());
        /*aspectSystem.add(new ProductStatisticProviderAspect() {
            public Map getGlobalProductScores() {
                try {
                    Map map = new HashMap();
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "cfncndy_ash_mcrrd"), new Float(100));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_orgval_whlmilk_01"), new Float(90));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_organi_2_milk_02"), new Float(80));
                    return map;
                } catch (InvalidContentKeyException e) {
                    throw new RuntimeException(e);
                }
            }

        });*/
        ScoreProvider.setInstance(new ProductStatisticUserProviderAspect() {
             

            @Override
            protected double getPersonalizedScore(String userId, ContentKey key, int idx) {
                String id = key.getId();
                if ("dai_orgval_whlmilk_01".equals(id)) {
                    return 3;
                }
                if ("dai_organi_1_milk_01".equals(id)) {
                    return 2;
                }
                return 0;
            }
            
            @Override
            protected double getGlobalScore(ContentKey key, int idx) {
                String id = key.getId();
                if ("cfncndy_ash_mcrrd".equals(id)) {
                    return 100;
                }
                if ("dai_orgval_whlmilk_01".equals(id)) {
                    return 90;
                }
                if ("dai_organi_2_milk_02".equals(id)) {
                    return 80;
                }
             
                return 0;
            }
            
            
        }.addPersonalizedFactors(ScoreProvider.USER_FREQUENCY).addGlobalFactors(ScoreProvider.GLOBAL_POPULARITY));

        GlobalCompiler.getInstance().addVariable(ScoreProvider.GLOBAL_POPULARITY, Expression.RET_FLOAT);
        GlobalCompiler.getInstance().addVariable(ScoreProvider.USER_FREQUENCY, Expression.RET_FLOAT);
        initTag();
    }

    void initTag() {
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "my-test-user-id", "fdId"));
        request = (MockHttpServletRequest) ctx.getRequest();
        
        sst = new SmartSearchTag();
        sst.setPageContext(ctx);

        sst.setCategoryTree("categoryTree");
        sst.setFilteredCategoryTreeName("filteredCategoryTree");
        sst.setSearchResults("searchResults");
        sst.setBrandSet("brandSet");
        sst.setCategorySet("categorySet");
        sst.setProductList("productList");
        sst.setSelectedCategories("selectedCategories");
    }

    public void testBasicSearch() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertEquals("currentPage-0", 0, fres.getCurrentPage());
            assertEquals("pageSize", SearchNavigator.getDefaultForView(SearchNavigator.VIEW_DEFAULT).normalPageSize, fres.getPageSize());
            assertEquals("start", 0, fres.getStart());
            assertEquals("pageCount", 1, fres.getPageCount());

            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product 2", "Organic Valley 2% Milk", fres.getFilteredProducts().get(2));
            assertContentNodeModel("filtered product 3", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(3));

            LOGGER.info("test basic search: OK.");
        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testPaging() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            request.setupAddParameter("pageSize", "2");
            request.setupAddParameter("start", "0");
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertEquals("currentPage-0", 0, fres.getCurrentPage());
            assertEquals("pageSize", 2, fres.getPageSize());
            assertEquals("start", 0, fres.getStart());
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());

            assertEquals("pageCount", 2, fres.getPageCount());

            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product 2", "Organic Valley 2% Milk", fres.getFilteredProducts().get(2));
            assertContentNodeModel("filtered product 3", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(3));

            assertEquals("first page", 2, fres.getFilteredProductsListPage().size());
            assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProductsListPage().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", fres.getFilteredProductsListPage().get(1));

            request.setupAddParameter("start", "2");
            sst.doStartTag();

            fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertEquals("currentPage-0", 1, fres.getCurrentPage());
            assertEquals("pageSize", 2, fres.getPageSize());
            assertEquals("start", 2, fres.getStart());
            assertEquals("pageCount", 2, fres.getPageCount());

            assertEquals("second page", 2, fres.getFilteredProductsListPage().size());
            assertContentNodeModel("filtered product 2", "Organic Valley 2% Milk", fres.getFilteredProductsListPage().get(0));
            assertContentNodeModel("filtered product 3", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProductsListPage().get(1));

            LOGGER.info("test paging: OK.");
        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchByQ() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();
            assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product 2", "Organic Valley 2% Milk", fres.getFilteredProducts().get(2));
            assertContentNodeModel("filtered product 3", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(3));
            LOGGER.info("testSearchByQ: OK.");

        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchByBrand() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            request.setupAddParameter("brandValue", "bd_organic_valley");

            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 3, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Organic Valley 1% Milk", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 2% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product 2", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(2));

            LOGGER.info("testSearchByBrand: OK.");
        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchByCategories() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            request.setupAddParameter("catId", "gro_choc_fine");

            sst.doStartTag();
            assertSmartSearchTagResult();
            assertSelectedCategories(new String[] { "gro_choc_fine", "gro_candy", "gro" });

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertOneBrandInTheBrandSet("Asher's");

            assertEquals("filtered product list size", 1, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(0));

            initTag();

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            request.setupAddParameter("catId", "gro_candy_blkch");

            sst.doStartTag();
            assertSmartSearchTagResult();
            assertSelectedCategories(new String[] { "gro_candy_blkch", "gro_choc_fine", "gro_candy", "gro" });

            LOGGER.info("testSearchByCategories: OK.");
        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchByCategoriesAndBrands() {
        // orgnat_dai_milk
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_NAME.getLabel());
            request.setupAddParameter("brandValue", "bd_organic_valley");
            request.setupAddParameter("catId", "orgnat_dai_milk");
            request.setupAddParameter("start", "0");

            sst.doStartTag();

            assertSmartSearchTagResult();

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertOneBrandInTheBrandSet("Organic Valley");

            assertEquals("filtered product list size", 1, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(0));

            LOGGER.info("testSearchByCategoriesAndBrands: OK.");
        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchWithPriceOrdering() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_PRICE.getLabel());
            request.setupAddParameter("start", "0");
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product 0", "Organic Valley 2% Milk", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product 2", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(2));
            assertContentNodeModel("filtered product 3", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(3));

            LOGGER.info("testSearchWithPriceOrdering: OK.");

        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchWithPopularitySort() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_POPULARITY.getLabel());
            request.setupAddParameter("start", "0");
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModel("filtered product: pop score 100", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product: pop score  90", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product: pop score  80", "Organic Valley 2% Milk", fres.getFilteredProducts().get(2));
            assertContentNodeModel("filtered product: pop score   0", "Organic Valley 1% Milk", fres.getFilteredProducts().get(3));

            LOGGER.info("testSearchWithRelevancySort: OK.");

        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchWithRelevancySort() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "milk");
            request.setupAddParameter("sort", SearchSortType.BY_RELEVANCY.getLabel());
            request.setupAddParameter("start", "0");
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModelKey("filtered product: pop score   0, user score 2:", "dai_organi_1_milk_01", fres.getFilteredProducts().get(0));
            assertContentNodeModelKey("filtered product: pop score  90", "dai_organi_2_milk_02", fres.getFilteredProducts().get(1));
            assertContentNodeModelKey("filtered product: pop score  80, user score 3:", "dai_orgval_whlmilk_01", fres.getFilteredProducts().get(2));
            assertContentNodeModelKey("filtered product: pop score 100", "cfncndy_ash_mcrrd", fres.getFilteredProducts().get(3));

            assertContentNodeModel("filtered product: pop score   0, user score 2:", "Organic Valley 1% Milk", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product: pop score  90", "Organic Valley 2% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product: pop score  80, user score 3:", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts()
                    .get(2));
            assertContentNodeModel("filtered product: pop score 100", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(3));

            LOGGER.info("testSearchWithRelevancySort: OK.");

        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    public void testSearchWithRelevancySortMultiTerm() {
        // TestPageContext ctx = new TestPageContext();
        // MockServletContext
        try {

            request.setupAddParameter("searchParams", "chocolate milk dairy organic");
            request.setupAddParameter("sort", SearchSortType.BY_RELEVANCY.getLabel());
            request.setupAddParameter("start", "0");
            sst.doStartTag();

            assertSmartSearchTagResult();
            assertSelectedCategories(null);

            FilteredSearchResults fres = (FilteredSearchResults) ctx.getAttribute("searchResults");
            assertNotNull("has products", fres.getProducts());

            assertEquals("product list size", 4, fres.getProducts().size());
            assertCategoryTree();
            assertBrandSet();

            assertEquals("filtered product list size", 4, fres.getFilteredProducts().size());
            assertContentNodeModelKey("filtered product: pop score   0, user score 2:", "dai_organi_1_milk_01", fres.getFilteredProducts().get(0));
            assertContentNodeModelKey("filtered product: pop score  90", "dai_organi_2_milk_02", fres.getFilteredProducts().get(1));
            assertContentNodeModelKey("filtered product: pop score  80, user score 3:", "dai_orgval_whlmilk_01", fres.getFilteredProducts().get(2));
            assertContentNodeModelKey("filtered product: pop score 100", "cfncndy_ash_mcrrd", fres.getFilteredProducts().get(3));

            assertContentNodeModel("filtered product: pop score   0, user score 2:", "Organic Valley 1% Milk", fres.getFilteredProducts().get(0));
            assertContentNodeModel("filtered product: pop score  90", "Organic Valley 2% Milk", fres.getFilteredProducts().get(1));
            assertContentNodeModel("filtered product: pop score  80, user score 3:", "Organic Valley Ultra Pasteurized Whole Milk", fres.getFilteredProducts()
                    .get(2));
            assertContentNodeModel("filtered product: pop score 100", "Asher's Milk Chocolate Pecan Caramel Pattie", fres.getFilteredProducts().get(3));

            LOGGER.info("testSearchWithRelevancySort: OK.");

        } catch (JspException e) {
            e.printStackTrace();
            fail("Error :" + e.getMessage());
        }
    }

    private void assertContentNodeModelKey(String desc, String id, Object object) {
        assertEquals(desc, id, ((ContentNodeModel) object).getContentKey().getId());
    }

    private void assertCategoryTree() {
        CategoryNodeTree tree = (CategoryNodeTree) ctx.getAttribute("categoryTree");
        assertNotNull("tree roots", tree.getRoots());
        assertEquals("root nodes", 3, tree.getRoots().size());
        Iterator iter = tree.getRoots().iterator();

        assertTreeElement("root element 1", "Dairy", 2, 1, (TreeElement) iter.next());
        assertTreeElement("root element 2", "Organic & All-Natural", 1, 1, (TreeElement) iter.next());
        assertTreeElement("root element 3", "Grocery", 1, 1, (TreeElement) iter.next());
    }

    private void assertBrandSet() {
        Set brandSet = (Set) ctx.getAttribute("brandSet");
        assertEquals("brand set size", 2, brandSet.size());
        assertContentNodeWithNameInTheCollection("brandset", brandSet, "Asher's");
        assertContentNodeWithNameInTheCollection("brandset", brandSet, "Organic Valley");
    }

    private void assertOneBrandInTheBrandSet(String name) {
        Set brandSet = (Set) ctx.getAttribute("brandSet");
        assertEquals("brand set size", 1, brandSet.size());
        assertContentNodeWithNameInTheCollection("brandset", brandSet, name);
    }

    private void assertSelectedCategories(String[] categories) {
        Set selcat = (Set) ctx.getAttribute("selectedCategories");
        if (categories != null) {
            assertEquals("selected categories size", categories.length, selcat.size());
            for (int i = 0; i < categories.length; i++) {
                assertTrue("selected categories contains:" + categories[i], selcat.contains(categories[i]));
            }
        } else {
            assertEquals("selected categories is zerro", 0, selcat.size());
        }
    }

    private void assertContentNodeWithNameInTheCollection(String errorMsg, Collection collection, String fullName) {
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            ContentNodeModel cnm = (ContentNodeModel) iter.next();
            if (fullName.equals(cnm.getFullName())) {
                return;
            }
        }
        fail("Missing:" + fullName + ", error:" + errorMsg);
    }

    static void assertContentNodeModel(String desc, String fullName, Object object) {
        assertEquals(desc, fullName, ((ContentNodeModel) object).getFullName());
    }

    private void assertSmartSearchTagResult() {
        assertNotNull("categoryTree", ctx.getAttribute("categoryTree"));
        assertNotNull("filteredCategoryTree", ctx.getAttribute("filteredCategoryTree"));
        assertNotNull("searchResults", ctx.getAttribute("searchResults"));
        assertNotNull("brandSet", ctx.getAttribute("brandSet"));
        assertNotNull("categorySet", ctx.getAttribute("categorySet"));
        assertNotNull("selectedCategories", ctx.getAttribute("selectedCategories"));
        assertNotNull("productList", ctx.getAttribute("productList"));

        assertTrue("searchResults is FilteredSearchResults", ctx.getAttribute("searchResults") instanceof FilteredSearchResults);
        assertTrue("categoryTree is CategoryNodeTree", ctx.getAttribute("categoryTree") instanceof CategoryNodeTree);
        assertTrue("filteredCategoryTree is CategoryNodeTree", ctx.getAttribute("filteredCategoryTree") instanceof CategoryNodeTree);

        assertTrue("brandSet is Set", ctx.getAttribute("brandSet") instanceof Set);
        assertTrue("categorySet is Set", ctx.getAttribute("categorySet") instanceof Set);
        assertTrue("selectedCategories is Set", ctx.getAttribute("selectedCategories") instanceof Set);
        assertTrue("productList is List", ctx.getAttribute("productList") instanceof List);
    }

    private void assertTreeElement(String name, String modelName, int depthChildCount, int childCount, TreeElement element) {
        assertNotNull(name + " - element", element);
        assertEquals(name + " - fullname ", modelName, element.getModel().getFullName());
        assertEquals(name + " - depthChildCount ", depthChildCount, element.getChildCount());
        assertEquals(name + " - childCount ", childCount, element.getChildren().size());
    }

    protected String[] getAffectedTables() {
        return null;
    }

    protected String getSchema() {
        return null;
    }

}
