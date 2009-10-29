package com.freshdirect.webapp.taglib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.NullAppender;

import com.freshdirect.TestUtils;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.customer.IDGenerator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.RecommendationEventLoggerMockup;
import com.freshdirect.smartstore.fdstore.RecommendationServiceTestBase;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SessionImpressionLog;
import com.freshdirect.smartstore.fdstore.SingleVariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;
import com.freshdirect.smartstore.service.VariantRegistry;
import com.freshdirect.webapp.taglib.smartstore.FeaturedItemsTag;
import com.freshdirect.webapp.util.FDEventUtil;
import com.mockrunner.mock.web.MockPageContext;

public class ImpressionTest extends RecommendationServiceTestBase {

    class MockAppender extends NullAppender {

        List events = new ArrayList();

        public void doAppend(LoggingEvent event) {
            events.add(event);
        }

        public List getEvents() {
            return events;
        }

        public void reset() {
            events.clear();
        }
    }

    class MockIDGenerator implements IDGenerator {
        int id;

        public synchronized String getNextId() {
            return "mock-id-" + (id++);
        }
    }

    MockAppender                               requestAppender = new MockAppender();
    MockAppender                               featureAppender = new MockAppender();
    MockAppender                               productAppender = new MockAppender();
    private FeaturedItemsRecommendationService firs;

    public void setUp() throws Exception {
        super.setUp();
        SessionImpressionLog.setIdGenerator(new MockIDGenerator());
        ImpressionLogger.setGlobalEnabled(true);
        ImpressionLogger.REQUEST.addAppender(requestAppender);
        ImpressionLogger.FEATURE.addAppender(featureAppender);
        ImpressionLogger.PRODUCT.addAppender(productAppender);
    }

    protected void tearDown() throws Exception {
        ImpressionLogger.setGlobalEnabled(false);
        featureAppender.reset();
        productAppender.reset();
    }

    RecommendationService getFeaturedItemsService() {
        if (firs == null) {
            firs = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS)), RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS), new java.util.HashMap()), false, false);
            firs.getVariant().setRecommender(firs);
            VariantRegistry.getInstance().addService(firs.getVariant());
        }
        return firs;
    }

    public void testProductImpression() {
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));

        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getFeaturedItemsService().getVariant()));

        FeaturedItemsTag fit = TestUtils.createFeaturedItemsTag(ctx, "spe_cooki_cooki");

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();

            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("recommendations", recomm);
            assertEquals("3 recommendation", 3, recomm.getProducts().size());

            List nodes = recomm.getProducts();
            assertNode("0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
            assertNode("2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");

            FDEventUtil.flushImpressions();

            assertEquals("event log size", 3, eventLogger.getCollectedEvents().size());

            assertRecommendationEventsAggregate("0", "Product:spe_madmoose_chc", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(0));
            assertRecommendationEventsAggregate("1", "Product:spe_moore_lemon", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(1));
            assertRecommendationEventsAggregate("2", "Product:spe_walkers_shortbre_02", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(2));

            assertEquals("request 1", 1, this.requestAppender.getEvents().size());
            assertEquals("feature impressions 1", 1, this.featureAppender.getEvents().size());
            assertEquals("feature event 1", "mock-id-0_f1,mock-id-0,,fi,spe_cooki_cooki,,,", getMessage(this.featureAppender, 0));
            assertEquals("product impressions 3", 3, this.productAppender.getEvents().size());
            assertEquals("product imp 1", "mock-id-0_p1,mock-id-0_f1,spe_madmoose_chc,1,,", getMessage(this.productAppender, 0));
            assertEquals("product imp 2", "mock-id-0_p2,mock-id-0_f1,spe_moore_lemon,2,,", getMessage(this.productAppender, 1));
            assertEquals("product imp 3", "mock-id-0_p3,mock-id-0_f1,spe_walkers_shortbre_02,3,,", getMessage(this.productAppender, 2));

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception " + e.getMessage());
        }
    }

    String getMessage(MockAppender appender, int position) {
        LoggingEvent event = (LoggingEvent) appender.getEvents().get(position);
        return (String) event.getMessage();
    }

}
