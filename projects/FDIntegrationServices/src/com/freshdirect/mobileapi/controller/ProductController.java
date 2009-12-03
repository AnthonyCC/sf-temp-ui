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
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.ProductMoreInfo;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.Price;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SalesUnit;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.Sku;
import com.freshdirect.mobileapi.model.Product.ImageType;
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
            } else if (WHATS_GOOD_PRESIDEN_PICKS_ACTION.equals(action)) {
                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.PRESIDEN_PICKS);
            } else if (WHATS_GOOD_DEALS_ACTION.equals(action)) {
                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.BRAND_NAME_DEALS);
            } else if (WHATS_GOOD_BUTCHERS_ACTION.equals(action)) {
                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.BUTCHERS_BLOCK);
            } else if (WHATS_GOOD_PRODUCE_ACTION.equals(action)) {
                model = getWhatsGoodProductList(model, request, response, user, WhatsGoodType.PEAK_PRODUCE);
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

        Product data;
        try {
            data = new Product(product);
            if (null != data.getProductTerms()) {
                data.setProductTerms(getProductWrappedTerms(data.getProductTerms()));
            }
        } catch (ModelException e) {
            throw new FDException(e);
        } catch (IOException e) {
            throw new FDException(e);
        } catch (TemplateException e) {
            throw new FDException(e);
        }
        setResponseMessage(model, data, user);
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

            String mediaPath = FDStoreProperties.getMediaPath().substring(0, FDStoreProperties.getMediaPath().indexOf("/media"));

            model.addObject("moreInfo", productMoreInfo);
            model.addObject("product", product);
            model.addObject("productImage", product.getImage(ImageType.ZOOM));
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

    private ModelAndView getWhatsGoodProductList(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
            SessionUser user, WhatsGoodType type) throws NoSessionException, JsonException, FDException {
        SearchResult data = new SearchResult();

        // Retrieving any possible payload
        String postData = getPostData(request, response);
        int page = 1;
        int resultMax = 25;

        LOGGER.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
        }

        List<com.freshdirect.mobileapi.model.Product> products = null;

        try {
            switch (type) {
            case BRAND_NAME_DEALS:
                products = com.freshdirect.mobileapi.model.Product.getBrandNameDealsProductList(getUserFromSession(request, response));
                break;
            case BUTCHERS_BLOCK:
                products = com.freshdirect.mobileapi.model.Product.getButchersBlockProductList(getUserFromSession(request, response));
                break;
            case PEAK_PRODUCE:
                products = com.freshdirect.mobileapi.model.Product.getPeakProduceProductList(user);
                break;
            case PRESIDEN_PICKS:
                products = com.freshdirect.mobileapi.model.Product.getPresidentsPickProductList(getUserFromSession(request, response));
                break;
            }
        } catch (ModelException e) {
            throw new FDException(e);
        }
        ListPaginator<com.freshdirect.mobileapi.model.Product> paginator = new ListPaginator<com.freshdirect.mobileapi.model.Product>(
                products, resultMax);

        data.setProductsFromModel(paginator.getPage(page));
        data.setTotalResultCount(products.size());
        setResponseMessage(model, data, user);

        return model;
    }
}
