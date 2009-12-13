package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.ProductMoreInfo;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.Price;
import com.freshdirect.mobileapi.controller.data.response.WhatsGoodCategories;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SalesUnit;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.Sku;
import com.freshdirect.mobileapi.model.WhatsGood;
import com.freshdirect.mobileapi.model.Product.ImageType;
import com.freshdirect.mobileapi.model.data.WhatsGoodCategory;
import com.freshdirect.mobileapi.model.tagwrapper.HealthWarningControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.MobileApiProperties;

import freemarker.template.TemplateException;

public class ProductController extends BaseController {
    private static Category LOGGER = LoggerFactory.getInstance(ProductController.class);

    public enum WhatsGoodType {
        PRESIDEN_PICKS, BRAND_NAME_DEALS, BUTCHERS_BLOCK, PEAK_PRODUCE
    }

    public static final String MORE_INFO_ACTION = "moreInfo";

    public static final String GET_PRICE_ACTION = "getprice";

    public static final String GET_WHATS_GOOD_CATEGORY_PRODUCTS_ACTION = "getwhatsgoodcateory";

    public static final String GET_WHATS_GOOD_CATEGORIES_ACTION = "getwhatsgoodcategories";

    public static final String ACKNOWLEDGE_HEALTH_WARNING = "acknowledgehealthwarning";

    public static final String WHATS_GOOD_PRESIDEN_PICKS_ACTION = "wsgpresidentspick";

    public static final String WHATS_GOOD_DEALS_ACTION = "wsgdeals";

    public static final String WHATS_GOOD_BUTCHERS_ACTION = "wsgbutchers";

    public static final String WHATS_GOOD_PRODUCE_ACTION = "wsgproduce";

    private static final String PRODUCT_MORE_INFO_TEMPLATE = "product-more-info";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {

        try {
            if (MORE_INFO_ACTION.equalsIgnoreCase(action)) {
                model = getProductMoreInfo(model, request, response);
            } else if (GET_PRICE_ACTION.equalsIgnoreCase(action)) {
                model = getPrice(model, request, response, user);
            } else if (ACKNOWLEDGE_HEALTH_WARNING.equals(action)) {
                model = getAcknowledgeHealthWarning(model, request, response, user);
                //            } else if (WHATS_GOOD_PRESIDEN_PICKS_ACTION.equals(action)) {
                //                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.PRESIDEN_PICKS);
                //            } else if (WHATS_GOOD_DEALS_ACTION.equals(action)) {
                //                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.BRAND_NAME_DEALS);
                //            } else if (WHATS_GOOD_BUTCHERS_ACTION.equals(action)) {
                //                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.BUTCHERS_BLOCK);
                //            } else if (WHATS_GOOD_PRODUCE_ACTION.equals(action)) {
                //                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.PEAK_PRODUCE);
            } else if (GET_WHATS_GOOD_CATEGORIES_ACTION.equals(action)) {
                model = getWhatsGoodCategories(model, request, response, user);
            } else if (GET_WHATS_GOOD_CATEGORY_PRODUCTS_ACTION.equals(action)) {
                String categoryId = request.getParameter("categoryId");
                model = getWhatsGoodProducts(model, request, response, user, categoryId);
            } else {
                model = getProduct(model, request, response, user);
            }
        } catch (ModelException e) {
            throw new ServiceException(e);
        }

        return model;
    }

    /**
     * @param model
     * @param request
     * @param response
     * @param user
     * @return
     * @throws JsonException
     * @throws FDException
     */
    private ModelAndView getAcknowledgeHealthWarning(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
            SessionUser user) throws JsonException, FDException {

        Message responseMessage = null;
        ResultBundle resultBundle = user.acknowledgeHealthWarning();
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("User acknowledgment of health warning captured successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);

        return model;
    }

    /**
     * @param model
     * @param request
     * @param response
     * @return
     * @throws ServiceException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws JsonException 
     * @throws NoSessionException 
     * @throws ModelException 
     * @throws Exception
     */
    private ModelAndView getPrice(ModelAndView model, HttpServletRequest request, HttpServletResponse response, SessionUser user)
            throws ServiceException, JsonException, NoSessionException, ModelException {
        com.freshdirect.mobileapi.model.Product product = getProduct(request, response);

        ProductConfiguration productConf = parseRequestObject(request, response, ProductConfiguration.class);
        Sku sku = product.getSkyByCode(productConf.getProduct().getSku().getCode());
        SalesUnit su = product.getSalesUnitByName(productConf.getSalesUnit().getName());
        Price price = new Price();

        try {
            price.setPrice(product.getPrice(sku, su, productConf.getQuantity(), productConf.getOptions()));
            if (product.isDisplayEstimatedQuantity()) {
                price.setEstimatedQuantity(product.getEstimatedQuantity(sku, su, productConf.getQuantity()));
            }
        } catch (PricingException e) {
            LOGGER.error("PricingException encountered on " + productConf.toString(), e);
        }
        setResponseMessage(model, price, user);
        return model;

    }

    /**
     * @param model
     * @param request
     * @param response
     * @return
     * @throws ServiceException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     * @throws FDException 
     * @throws JsonException 
     * @throws NoSessionException 
     * @throws ModelException 
     * @throws Exception
     */
    private ModelAndView getProduct(ModelAndView model, HttpServletRequest request, HttpServletResponse response, SessionUser user)
            throws ServiceException, FDException, JsonException, NoSessionException, ModelException {

        com.freshdirect.mobileapi.model.Product product = getProduct(request, response);

        Message responseMessage;
        if (!user.isHealthWarningAcknowledged() && product.isAlcoholProduct()) {
            responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage.addErrorMessage(ERR_HEALTH_WARNING, MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
        } else {
            try {
                responseMessage = new Product(product);
                if (null != ((Product) responseMessage).getProductTerms()) {
                    ((Product) responseMessage).setProductTerms(getProductWrappedTerms(((Product) responseMessage).getProductTerms()));
                }
            } catch (ModelException e) {
                throw new FDException(e);
            } catch (IOException e) {
                throw new FDException(e);
            } catch (TemplateException e) {
                throw new FDException(e);
            }
        }
        setResponseMessage(model, responseMessage, user);
        return model;

    }

    /**
     * @param model
     * @param request
     * @param response
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     * @throws ServiceException 
     * @throws FDException 
     * @throws JsonException 
     * @throws NoSessionException 
     * @throws ModelException 
     * @throws TemplateException 
     * @throws IOException 
     * @throws Exception
     */
    private ModelAndView getProductMoreInfo(ModelAndView model, HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, FDException, JsonException, NoSessionException, ModelException {
        com.freshdirect.mobileapi.model.Product product = getProduct(request, response);
        //HtmlResponse data;

        model = getModelAndView(PRODUCT_MORE_INFO_TEMPLATE);
        try {
            //data = new HtmlResponse();
            response.setContentType("text/html");
            ProductMoreInfo productMoreInfo = new ProductMoreInfo(product);

            model.addObject("moreInfo", productMoreInfo);
            model.addObject("product", product);
            model.addObject("productImage", product.getImage(ImageType.DETAIL));
            model.addObject("mediaPath", MobileApiProperties.getMediaPath());
            model.addObject("request", request);

        } catch (ModelException e) {
            throw new FDException(e);
        }
        //setResponseMessage(model, data);
        return model;

    }

    /**
     * @param request
     * @return
     * @throws NoSessionException 
     * @throws ServiceException
     * @throws NoSessionException 
     * @throws ModelException 
     */
    private com.freshdirect.mobileapi.model.Product getProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, ModelException, NoSessionException {
        com.freshdirect.mobileapi.model.Product result = null;
        String categoryId = request.getParameter("categoryId");
        String productId = request.getParameter("productId");
        if (!(StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(productId))) {
            //ProductService productService = new ProductServiceImpl();
            //result = productService.getProduct(categoryId, productId);
            result = com.freshdirect.mobileapi.model.Product.getProduct(productId, categoryId, getUserFromSession(request, response));
        }
        return result;
    }

    /**
     * @param model
     * @param request
     * @param response
     * @param user
     * @param categoryId
     * @return
     * @throws NoSessionException
     * @throws JsonException
     * @throws FDException
     */
    private ModelAndView getWhatsGoodProducts(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
            SessionUser user, String categoryId) throws NoSessionException, JsonException, FDException {

        SearchResult data = new SearchResult();
        String postData = getPostData(request, response);
        int page = 1;
        int resultMax = 25;
        LOGGER.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
        }
        List<com.freshdirect.mobileapi.model.Product> products = WhatsGood.getProducts(categoryId, user);
        ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
                products, resultMax);

        data.setProductsFromModel(paginator.getPage(page));
        data.setTotalResultCount(products.size());
        setResponseMessage(model, data, user);

        return model;
    }

    //    private ModelAndView getWhatsGoodProductList(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
    //            SessionUser user, WhatsGoodType type) throws NoSessionException, JsonException, FDException {
    //        SearchResult data = new SearchResult();
    //
    //        // Retrieving any possible payload
    //        String postData = getPostData(request, response);
    //        int page = 1;
    //        int resultMax = 25;
    //
    //        LOGGER.debug("PostData received: [" + postData + "]");
    //        if (StringUtils.isNotEmpty(postData)) {
    //            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
    //            page = requestMessage.getPage();
    //            resultMax = requestMessage.getMax();
    //        }
    //
    //        List<com.freshdirect.mobileapi.model.Product> products = null;
    //
    //        try {
    //            switch (type) {
    //            case BRAND_NAME_DEALS:
    //                products = WhatsGood.getBrandNameDealsProductList(getUserFromSession(request, response));
    //                break;
    //            case BUTCHERS_BLOCK:
    //                products = WhatsGood.getButchersBlockProductList(getUserFromSession(request, response));
    //                break;
    //            case PEAK_PRODUCE:
    //                products = WhatsGood.getPeakProduceProductList(user);
    //                break;
    //            case PRESIDEN_PICKS:
    //                products = WhatsGood.getPresidentsPickProductList(getUserFromSession(request, response));
    //                break;
    //            }
    //        } catch (ModelException e) {
    //            throw new FDException(e);
    //        }
    //        ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
    //                products, resultMax);
    //
    //        data.setProductsFromModel(paginator.getPage(page));
    //        data.setTotalResultCount(products.size());
    //        setResponseMessage(model, data, user);
    //
    //        return model;
    //    }

    private String mediaServerUrl = MobileApiProperties.getMediaPath();

    private ModelAndView getWhatsGoodCategories(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
            SessionUser user) throws ServiceException, FDException, JsonException, NoSessionException, ModelException {
        WhatsGoodCategories whatsGoodCategories = new WhatsGoodCategories();
        List<WhatsGoodCategory> categories = WhatsGood.getWhatsGoodCategories();
        for (WhatsGoodCategory category : categories) {
            category.prependHeaderImageUrl(mediaServerUrl);
        }
        whatsGoodCategories.setCategories(categories);

        setResponseMessage(model, whatsGoodCategories, user);
        return model;
    }

}
