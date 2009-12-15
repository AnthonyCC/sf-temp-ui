package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.ProductLabeling;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer.SmartStoreRecommendationType;
import com.freshdirect.mobileapi.model.tagwrapper.RequestParamName;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.mobileapi.model.tagwrapper.UtilsWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.UtilsWrapper.UtilsExecutionWrapper;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.smartstore.Impression;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;
import com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy;

public class SmartStore {

    public static final String RECOMMENDATION = "RECOMMENDATION";

    private final static Category LOG = LoggerFactory.getInstance(SmartStore.class);

    private SessionUser user;

    private static final int MAX_RECOMMENDATION = 25;

    public SmartStore(SessionUser user) {
        this.user = user;
    }

    public ResultBundle getFavoritesRecommendations(RequestData requestData, final Recommendations previousRecommendations,
            final String previousImpression, final String page) {
        ResultBundle resultBundle = getRecommendations(EnumSiteFeature.DYF, requestData, previousRecommendations, previousImpression, page);
        SmartStoreRecommendationContainer ssResult = (SmartStoreRecommendationContainer) resultBundle
                .getExtraData(SmartStore.RECOMMENDATION);
        if (ssResult != null && ssResult.getProducts().size() == 0)
            resultBundle = getRecommendations(EnumSiteFeature.FAVORITES, requestData, previousRecommendations, previousImpression, page);
        return resultBundle;
    }

    public ResultBundle getRecommendations(final EnumSiteFeature siteFeature, RequestData requestData,
            final Recommendations previousRecommendations, final String previousImpression, final String page) {
        Recommendations recommendations;
        ResultBundle resultBundle = new ResultBundle();
        resultBundle.setActionResult(new ActionResult());
        Map<String, String> prd2recommendation = null;

        UtilsWrapper wrapper = new UtilsWrapper();
        wrapper.addExpectedSessionValues(new String[] { SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS,
                SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION }, new String[] {
                SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS, SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION });
        wrapper.addExpectedRequestValues(new String[] { RequestParamName.REQ_PARAM_YMAL_RESET,
                RequestParamName.REQ_PARAM_SMART_STORE_IMPRESSION }, new String[] { RequestParamName.REQ_PARAM_YMAL_RESET,
                RequestParamName.REQ_PARAM_SMART_STORE_IMPRESSION });

        // https://info.schematic.com/jira/browse/FDIPONE-869
        // YMAL should be refreshed everytime.  pagination is done on client side.
        // in the current implementation we will keep recommendations until a request for a new site feature arrives
        //        if (previousRecommendations != null && previousRecommendations.getVariant().getSiteFeature().equals(siteFeature)) {
        //            // use previous
        //            wrapper.setUtilsExecutionWrapper(new UtilsExecutionWrapper() {
        //                @Override
        //                public Object executeUtils(PageContext pageContext) {
        //                    // page alignment must happen before logging
        //                    alignPage(previousRecommendations, page);
        //
        //                    // logging
        //                    String impression = logImpressions(previousImpression, pageContext, previousRecommendations);
        //                    pageContext.getSession().setAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION, impression);
        //
        //                    return null;
        //                }
        //            });
        //            recommendations = previousRecommendations;
        //        } else {
        // construct new
        wrapper.setUtilsExecutionWrapper(new UtilsExecutionWrapper() {
            @Override
            public Object executeUtils(PageContext pageContext) {
                // we do not use the GenericRecommendationsTag because it does not fit our need
                final SessionInput input = new SessionInput(user.getFDSessionUser());
                // FDStoreRecommender.initYmalSource(input, user.getFDSessionUser(), pageContext.getRequest());
                // input.setCurrentNode(input.getYmalSource());
                input.setYmalSource(YmalUtil.resolveYmalSource(user.getFDSessionUser(), null, pageContext.getRequest()));
                input.setCurrentNode(YmalUtil.getSelectedCartLine(user.getFDSessionUser()).lookupProduct());
                
                input.setMaxRecommendations(MAX_RECOMMENDATION);
                Recommendations recs;
                try {
                    recs = FDStoreRecommender.getInstance().getRecommendations(siteFeature, user.getFDSessionUser(), input);

                    // page alignment must happen before logging
                    alignPage(recs, page);

                    // logging
                    String impression = logImpressions(null, pageContext, recs);
                    pageContext.getSession().setAttribute(SessionParamName.SESSION_PARAM_PREVIOUS_IMPRESSION, impression);

                    return recs;
                } catch (FDResourceException e) {
                    // this is a nasty way to return error as I cannot handle
                    // the exception in this call back function properly
                    return e;
                }
            }
        });
        Object o = wrapper.executeUtil();
        if (o instanceof FDResourceException) {
            FDResourceException e = (FDResourceException) o;
            LOG.error("failed to retrieve recommendations", e);
            resultBundle.getActionResult().addError(
                    new ActionError("smartStore", "failed to retrieve recommendations: " + e.getMessage() + ", see log for details"));
            return resultBundle;
        } else {
            recommendations = (Recommendations) o;
            // TODO known bug (APPREQ-748) - this mapping should be cached along with the recommendations
            // take thread locals for local use
            prd2recommendation = (Map<String, String>) AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.get();
        }
        
        //        }
        // wrapper is used to save recommendations in the session
        wrapper.addSessionValue(SessionParamName.SESSION_PARAM_PREVIOUS_RECOMMENDATIONS, recommendations);
        resultBundle.setWrapper(wrapper);

        List<ProductModel> rProds = recommendations.getProducts();
        Variant variant = recommendations.getVariant();

        SmartStoreRecommendationContainer smartStoreRecommendationContainer = new SmartStoreRecommendationContainer();
        smartStoreRecommendationContainer.setLast(recommendations.isLastPage());

        SmartStoreRecommendationType type = null;
        if (EnumSiteFeature.DYF.equals(siteFeature))
            type = SmartStoreRecommendationType.YOUR_FAVORITE;
        else if (EnumSiteFeature.FAVORITES.equals(siteFeature))
            type = SmartStoreRecommendationType.FRESHDIRECT_FAVORITE;
        else if (EnumSiteFeature.YMAL.equals(siteFeature))
            type = SmartStoreRecommendationType.YOU_MIGHT_ALSO_LIKE;
        smartStoreRecommendationContainer.setType(type);

        ConfigurationContext confContext = new ConfigurationContext();
        user.setFDUserOnConfigurationContext(confContext);
        ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();
        List<ProductImpression> impressions = new ArrayList<ProductImpression>();
        for (ProductModel rProd : rProds) {
            ProductImpression pi = cUtil.configure(rProd, confContext);
            impressions.add(pi);
        }

        int rank = 1;
        for (ProductImpression pi : impressions) {

            ProductModel product = pi.getProductModel();
            SmartStoreRecommendationProduct resultItem = new SmartStoreRecommendationProduct();
            try {

                if (product.getParentNode() instanceof ConfiguredProductGroup) {
                    System.out.println("ConfiguredProductGroup");
                }
                resultItem.setProduct(Product.wrap(product, user.getFDSessionUser().getUser()));

                ProductLabeling pl = new ProductLabeling(user.getFDSessionUser(), product, variant.getHideBursts());
                // added this using the thread local values
                String trkd = prd2recommendation != null ? prd2recommendation.get(product.getContentKey().getId()) : pl.getTrackingCode();
                resultItem.setTrackingCodeEx(trkd);
                resultItem.setParameterBundle(StringEscapeUtils.unescapeHtml(FDURLUtil.getProductURI(product, recommendations.getVariant()
                        .getId(), recommendations.getVariant().getSiteFeature().getName().toLowerCase(), trkd, rank, recommendations
                        .getImpressionId(product))));

                if (pi instanceof TransactionalProductImpression) {

                    //resultItem.setTrackingCode(variant.getSiteFeature().getName().toLowerCase());
                    //resultItem.setVariant(variant.getId());
                    //resultItem.setRecProductIds(StringEscapeUtils.escapeHtml(Recommendations.getSerializedProducts(rProds))); TODO: Not needed?
                    //                    resultItem.setRecCurrentNode(recommendations != null && recommendations.getSessionInput() != null
                    //                            && recommendations.getSessionInput().getCurrentNode() != null ? recommendations.getSessionInput()
                    //                            .getCurrentNode().getContentKey().getId() : "(null)");
                    //                    resultItem.setRecYmalSource(recommendations != null && recommendations.getSessionInput() != null
                    //                            && recommendations.getSessionInput().getCurrentNode() != null ? recommendations.getSessionInput()
                    //                            .getCurrentNode().getContentKey().getId() : "(null)");

                    /*
                     * DUP: com.freshdirect.webapp.taglib.fdstore.TxProductControlTag
                     * LAST UPDATED ON: 11/2/2009
                     * LAST UPDATED WITH SVN#: 6677
                     * WHY: The following logic was duplicate because it was specified in a Tag class that could not be wrapped effectively. 
                     * WHAT: The original tag generates hidden input files to appear on the form.  Those hidden input values are submitted
                     *   when items have been added to the cart.  This duplicated code sets the values in a Java class so that it could be passed
                     *   onto the client as data.  At that point, it's the client's responsibility to send these data back when associated product
                     *   has been added to the cart. 
                     */
                    FDConfigurableI configuration = ((TransactionalProductImpression) pi).getConfiguration();
                    for (Iterator iit = configuration.getOptions().entrySet().iterator(); iit.hasNext();) {
                        Map.Entry entry = (Map.Entry) iit.next();
                        resultItem.addOption(entry.getKey().toString(), entry.getValue().toString());
                    }
                }

                // you should add the product model to the result only if it's generated
                // otherwise the product property will be null
                smartStoreRecommendationContainer.addProduct(resultItem);
            } catch (ModelException e) {
                LOG.error("ModelException encountered while trying to wrap sku=" + pi.getSku().getSkuCode(), e);
                continue;
            }
            rank++;
        }

        // clean up thread locals after use
        // TODO fix required, see above (APPREQ-748)
        AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.set(null);
        AbstractRecommendationService.RECOMMENDER_STRATEGY_SERVICE_AUDIT.set(null);

        resultBundle.addExtraData(RECOMMENDATION, smartStoreRecommendationContainer);

        resultBundle.setActionResult(new ActionResult());

        return resultBundle;
    }

    private void alignPage(Recommendations recommendations, String page) {
        Integer n = null;
        try {
            n = Integer.parseInt(page);
        } catch (NumberFormatException exc) {
        }

        if (n != null) {
            recommendations.setOffset(n.intValue());
        } else if ("next".equalsIgnoreCase(page)) {
            recommendations.pageForward();
        } else if ("prev".equalsIgnoreCase(page)) {
            recommendations.pageBackward();
        }
    }

    private String logImpressions(String previousImpression, PageContext pageContext, Recommendations recs) {
        if (recs.getProducts().size() > 0) {
            user.getFDSessionUser().logImpression(recs.getVariant().getId(), recs.getProducts().size());
        }

        for (ProductModel p : recs.getProducts()) {
            FDEventUtil.logRecommendationImpression(recs.getVariant().getId(), p.getContentKey());
        }

        if (ImpressionLogger.isGlobalEnabled()) {
            Impression imp = Impression.get(user.getFDSessionUser(), (HttpServletRequest) pageContext.getRequest(), "iPhone"); //TODO: Make this externalized
            // the third parameter will become the "facility" for which impressions are logged (e.g. cart, ymal, iPhone, etc.)
            // Impression imp = Impression.get(user.getFDSessionUser(), (HttpServletRequest) pageContext.getRequest(), "iPhone");

            int rank = 1;
            Map<ContentKey, String> map = new HashMap<ContentKey, String>();
            String impression;
            if (previousImpression != null)
                impression = previousImpression;
            else
                impression = imp.logFeatureImpression(null, null, recs);
            //r.getRecommenderStrategyIdForProduct(p.getContentName())
            for (ProductModel p : recs.getProducts()) {
                String imp_id = imp.logProduct(impression, rank, p.getContentKey(), recs.getRecommenderIdForProduct(p.getContentName()),
                        recs.getRecommenderStrategyIdForProduct(p.getContentName()));
                map.put(p.getContentKey(), imp_id);
                rank++;
            }
            recs.addImpressionIds(map);
            return impression;
        }
        return null;
    }

    public static class SmartStoreRecommendationProduct {

        private Map<String, String> options = new HashMap<String, String>();

        //This is the query string w/ all tracking parameters needed to smartstore metric tracking.
        private String parameterBundle;

        //        private String trackingCode;

        public String getParameterBundle() {
            return parameterBundle;
        }

        public void setParameterBundle(String parameterBundle) {
            this.parameterBundle = parameterBundle;
        }

        private String trackingCodeEx;

        private String impressionId;

        private Product product;

        //        private String variant;

        private String siteFeature;

        //        private String recProductIds;
        //
        //        private String recCurrentNode;
        //
        //        private String recYmalSource;

        private String recRefreshable;

        private String recSmartSavings;

        public Map<String, String> getOptions() {
            return options;
        }

        public void setOptions(Map<String, String> options) {
            this.options = options;
        }

        public void addOption(String key, String value) {
            this.options.put(key, value);
        }

        //        public String getVariant() {
        //            return variant;
        //        }
        //
        //        public void setVariant(String variant) {
        //            this.variant = variant;
        //        }

        public String getSiteFeature() {
            return siteFeature;
        }

        public void setSiteFeature(String siteFeature) {
            this.siteFeature = siteFeature;
        }

        //        public String getRecProductIds() {
        //            return recProductIds;
        //        }
        //
        //        public void setRecProductIds(String recProductIds) {
        //            this.recProductIds = recProductIds;
        //        }

        //        public String getRecCurrentNode() {
        //            return recCurrentNode;
        //        }
        //
        //        public void setRecCurrentNode(String recCurrentNode) {
        //            this.recCurrentNode = recCurrentNode;
        //        }

        //        public String getRecYmalSource() {
        //            return recYmalSource;
        //        }
        //
        //        public void setRecYmalSource(String recYmalSource) {
        //            this.recYmalSource = recYmalSource;
        //        }

        public String getRecRefreshable() {
            return recRefreshable;
        }

        public void setRecRefreshable(String recRefreshable) {
            this.recRefreshable = recRefreshable;
        }

        public String getRecSmartSavings() {
            return recSmartSavings;
        }

        public void setRecSmartSavings(String recSmartSavings) {
            this.recSmartSavings = recSmartSavings;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        //        public String getTrackingCode() {
        //            return trackingCode;
        //        }
        //
        //        public void setTrackingCode(String trackingCode) {
        //            this.trackingCode = trackingCode;
        //        }

        public String getTrackingCodeEx() {
            return trackingCodeEx;
        }

        public void setTrackingCodeEx(String trackingCodeEx) {
            this.trackingCodeEx = trackingCodeEx;
        }

        public String getImpressionId() {
            return impressionId;
        }

        public void setImpressionId(String impressionId) {
            this.impressionId = impressionId;
        }

    }

    public static class SmartStoreRecommendationContainer {

        public enum SmartStoreRecommendationType {
            FRESHDIRECT_FAVORITE, YOUR_FAVORITE, YOU_MIGHT_ALSO_LIKE
        }

        private SmartStoreRecommendationType type;

        public SmartStoreRecommendationType getType() {
            return type;
        }

        public void setType(SmartStoreRecommendationType type) {
            this.type = type;
        }

        private boolean last;

        public List<SmartStoreRecommendationProduct> getProducts() {
            return products;
        }

        public String getYmalSetId() {
            return ymalSetId;
        }

        public void setYmalSetId(String ymalSetId) {
            this.ymalSetId = ymalSetId;
        }

        public String getOriginatingProductId() {
            return originatingProductId;
        }

        public void setOriginatingProductId(String originatingProductId) {
            this.originatingProductId = originatingProductId;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getRecProductIds() {
            return recProductIds;
        }

        public void setRecProductIds(String recProductIds) {
            this.recProductIds = recProductIds;
        }

        public String getRecCurrentNode() {
            return recCurrentNode;
        }

        public void setRecCurrentNode(String recCurrentNode) {
            this.recCurrentNode = recCurrentNode;
        }

        public String getRecYmalSource() {
            return recYmalSource;
        }

        public void setRecYmalSource(String recYmalSource) {
            this.recYmalSource = recYmalSource;
        }

        private String ymalSetId;

        private String originatingProductId;

        private String baseUrl;

        private String recProductIds;

        private String recCurrentNode;

        private String recYmalSource;

        private List<SmartStoreRecommendationProduct> products = new ArrayList<SmartStoreRecommendationProduct>();

        public void addProduct(SmartStoreRecommendationProduct product) {
            this.products.add(product);
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

    }
}
