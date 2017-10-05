package com.freshdirect.mobileapi.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.ProductList;
import com.freshdirect.mobileapi.controller.data.ProductMoreInfo;
import com.freshdirect.mobileapi.controller.data.SearchResult;
import com.freshdirect.mobileapi.controller.data.request.MultipleRequest;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.Price;
import com.freshdirect.mobileapi.controller.data.response.WhatsGoodCategories;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.PairItProductModel;
import com.freshdirect.mobileapi.model.Product.ImageType;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SalesUnit;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.Sku;
import com.freshdirect.mobileapi.model.WhatsGood;
import com.freshdirect.mobileapi.model.data.WhatsGoodCategory;
import com.freshdirect.mobileapi.model.tagwrapper.SmartSearchTagWrapper;
import com.freshdirect.mobileapi.service.ProductServiceImpl;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.util.NutritionInfoPanelRendererUtil;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.RequestUtil;

import freemarker.template.TemplateException;

public class ProductController extends BaseController {
    private static Category LOGGER = LoggerFactory.getInstance(ProductController.class);
    private static String cssString=null;

    public enum WhatsGoodType {
        PRESIDEN_PICKS, BRAND_NAME_DEALS, BUTCHERS_BLOCK, PEAK_PRODUCE
    }

    public static final String MORE_INFO_ACTION = "moreInfo";
    
    public static final String GET_PRODUCT_DETAIL = "getproductdetail";
    
    public static final String MULTIPLE_PRODUCT_DETAIL = "multipleproductdetail";

    public static final String GET_PRICE_ACTION = "getprice";

    public static final String GET_WHATS_GOOD_CATEGORY_PRODUCTS_ACTION = "getwhatsgoodcateory";

    public static final String GET_WHATS_GOOD_CATEGORIES_ACTION = "getwhatsgoodcategories";

    public static final String ACKNOWLEDGE_HEALTH_WARNING = "acknowledgehealthwarning";

    public static final String WHATS_GOOD_PRESIDEN_PICKS_ACTION = "wsgpresidentspick";

    public static final String WHATS_GOOD_DEALS_ACTION = "wsgdeals";

    public static final String WHATS_GOOD_BUTCHERS_ACTION = "wsgbutchers";

    public static final String WHATS_GOOD_PRODUCE_ACTION = "wsgproduce";

    private static final String PRODUCT_MORE_INFO_TEMPLATE = "product-more-info";
    
    public static final String GET_REALTED_PRODUCTS_ACTION = "getrelatedproducts";
    
    public static final String GET_RECOMMENDED_PRODUCTS_ACTION = "recommended";

    @Override
    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException {

    	if (user == null) {
    		user = fakeUser(request.getSession());
    	}

        try {
            if (MORE_INFO_ACTION.equalsIgnoreCase(action)) {
                model = getProductMoreInfo(model, request, response, user);
            } else if (GET_PRICE_ACTION.equalsIgnoreCase(action)) {
                model = getPrice(model, request, response, user);
            } else if (ACKNOWLEDGE_HEALTH_WARNING.equals(action)) {
                model = getAcknowledgeHealthWarning(model, request, response, user);
            } else if (GET_WHATS_GOOD_CATEGORIES_ACTION.equals(action)) {
                model = getWhatsGoodCategories(model, request, response, user);
            } else if (GET_WHATS_GOOD_CATEGORY_PRODUCTS_ACTION.equals(action)) {
                String categoryId = request.getParameter("categoryId");
                model = getWhatsGoodProducts(model, request, response, user, categoryId);
            } else if (GET_REALTED_PRODUCTS_ACTION.equals(action)) {
                model = getRelatedProducts(model, request, response, user);
            } else if(GET_RECOMMENDED_PRODUCTS_ACTION.equals(action)){
            	model = getRecommendedProducts(model, request, response, user);
            } else if(MULTIPLE_PRODUCT_DETAIL.equals(action)){
            	MultipleRequest reqestMessage = parseRequestObject(request, response, MultipleRequest.class);
            	if (isExtraResponseRequested(request)) {
            	    model = getMultipleProductDetailPotatoes(model, reqestMessage, response, user);
            	} else {
            	    model = getMultipleProductDetail(model, reqestMessage, response, user);
            	}
            }else {
                if (isExtraResponseRequested(request)) {
                    model = getPotatoProduct(model, request, response, user);
                } else {
                    model = getProduct(model, request, response, user, GET_PRODUCT_DETAIL.equalsIgnoreCase(action));
                }
            }
        } catch (ModelException e) {
            throw new ServiceException(e);
        }

        return model;
    }

    private ModelAndView getRecommendedProducts(ModelAndView model,
			HttpServletRequest request, HttpServletResponse response,
			SessionUser user) throws JsonException {
    	 String categoryId = request.getParameter("categoryId");
         String productId = request.getParameter("productId");
         com.freshdirect.mobileapi.controller.data.PairItProductModel result = new com.freshdirect.mobileapi.controller.data.PairItProductModel();
         ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);
         if (product == null) {
             product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey("Product:" + productId));
         }
         if(product!=null){
        	 com.freshdirect.mobileapi.model.PairItProductModel p = new PairItProductModel();
        	 p.setCompleteMeal(product);
        	 if(p.getPairItProductIds()!=null && p.getPairItProductIds().size() > 0) {
        	 result.setHeading(p.getHeading());
        	 result.setText(p.getText());
        	 result.setPairItProductIds(p.getPairItProductIds());
        	 } else {
        		 result.addDebugMessage("No pair it product is available for this product");
        	 }
         } else {
        	 result.addDebugMessage("Requested product not available");
         }
          setResponseMessage(model, result, user);
		return model;
	}

	private ModelAndView getRelatedProducts(ModelAndView model,
            HttpServletRequest request, HttpServletResponse response,
            SessionUser user) throws JsonException {
        String categoryId = request.getParameter("categoryId");
        String productId = request.getParameter("productId");
		SearchResult result = new SearchResult();
        ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);

        List<com.freshdirect.mobileapi.model.Product> products = new ArrayList<com.freshdirect.mobileapi.model.Product>();

        boolean restoreAllowAnon = com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper.AllowAnonymousUsers;
        com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper.AllowAnonymousUsers = true;
        
        
		try {
            // upsell first
			//APPDEV-4236: Call to getrelatedproducts failing for Alcohol
			if(product != null){
	            List<ProductModel> upsellProducts = ProductRecommenderUtil.getUpsellProducts(product);
	            for (ProductModel productModel : upsellProducts) {
	                try {
	                    com.freshdirect.mobileapi.model.Product p = com.freshdirect.mobileapi.model.Product.wrap(productModel,user.getFDSessionUser().getUser());//4236 - added user parameter
	                    products.add(p);
	                } catch (ModelException e) {
	                    result.addDebugMessage("NOPRODUCTFOR_" + productModel.getContentName(), this.traceFor(e));
	                }
	            }
			}
        } catch (Exception e) {
            result.addDebugMessage("ERROR_LOADING_UPSELL_ITEMS", this.traceFor(e));
        }

		try {
            // crosssell next
			//APPDEV-4236: Call to getrelatedproducts failing for Alcohol
			if(product != null){
	            List<ProductData> crossSellProducts = ProductRecommenderUtil.getCrossSellProducts(product , user.getFDSessionUser().getUser());
	            for (ProductData productData : crossSellProducts) {
	            	try {
	                    com.freshdirect.mobileapi.model.Product p = com.freshdirect.mobileapi.model.Product.getProduct(productData.getProductId(), productData.getCatId(), null, user);
	                    products.add(p);
	                } catch (ServiceException e) {
	                    result.addDebugMessage("NOPRODUCTFOR_" + productData.getProductId(), this.traceFor(e));
	                }
	            }
			}
        } catch (Exception e) {
            result.addDebugMessage("ERROR_LOADING_CROSSSELL_ITEMS", this.traceFor(e));
        }

		// no recommendations - will search for something instead
		if (products.size() == 0) {
	        ProductServiceImpl productService = new ProductServiceImpl();
			try {
				SmartSearchTagWrapper wrapper = new SmartSearchTagWrapper(user);
		        wrapper.setRequestUrl(RequestUtil.getFullRequestUrl(request));
		        setContextHeaders(request, wrapper);
	            List<com.freshdirect.mobileapi.model.Product> searchResults = productService.search("", null, 0, 10, SortType.RELEVANCY, null, null, null,
	                    user, request, wrapper);
	            products.addAll(searchResults);
            } catch (ServiceException e) {
                result.addDebugMessage("ERROR_LOADING_SEARCH_ITEMS", this.traceFor(e));
            }

		}
		
		result.setProductsFromModel(products);
		result.setTotalResultCount(products.size());
		setResponseMessage(model, result, user);
		
        com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper.AllowAnonymousUsers = restoreAllowAnon;

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
        if(productConf != null && productConf.getProduct() != null && productConf.getProduct().getSku() != null) {
	        Sku sku = product.getSkyByCode(productConf.getProduct().getSku().getCode());
	        if(productConf.getSalesUnit() != null) {
	            
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
	        }
        } else {
        	LOGGER.debug("getPrice_failed:" + (productConf != null ? productConf.toString() : "NONE"));
        }
        return model;

    }

    
    /**
     * Produces and returns a detailed product info
     * Works similarly to {@link #getProduct(ModelAndView, HttpServletRequest, HttpServletResponse, SessionUser)})
     *  
     * @param model
     * @param request
     * @param response
     * @param user
     * @return
     * @throws JsonException
     */
    private ModelAndView getPotatoProduct(ModelAndView model, HttpServletRequest request, HttpServletResponse response, SessionUser user) throws JsonException {
        // JSON response body
        class ProductPotatoMessage extends Message {
            public final ProductPotatoData product;
            public String terms = null;
            
            public ProductPotatoMessage(ProductPotatoData product) {
                this.product = product;
            }
        }


        // final String categoryId = request.getParameter("categoryId");
        final String productId = request.getParameter("productId");
        if (/*StringUtils.isEmpty(categoryId) ||*/ StringUtils.isEmpty(productId)) {
            Message responseMessage = Message.createFailureMessage("Missing parameter(s)");
            setResponseMessage(model, responseMessage, user);
            return model;
        }
        // enable product incomplete true for pdp
        final ProductPotatoData data = ProductPotatoUtil.getProductPotato(productId, null, user.getFDSessionUser(), true, true);
        if (data == null) {
            Message responseMessage = Message.createFailureMessage("Product not found");
            setResponseMessage(model, responseMessage, user);
            return model;
        }

		// Temp fix for FKWeb-973
        if(data.getProductData()!=null && data.getProductData().getProductJumboImage()!=null && 
        		data.getProductData().getProductJumboImage().contains("/media_stat/images/layout/clear.gif")){
            	data.getProductData().setProductJumboImage(null);
        }
        
        if(data.getProductData()!=null && data.getProductData().getProductImagePackage()!=null && 
        		data.getProductData().getProductImagePackage().contains("/media_stat/images/layout/clear.gif")){
            	data.getProductData().setProductImagePackage(null);
        }
        
        if(data.getProductData()!=null && data.getProductData().getProductAlternateImage()!=null && 
        		data.getProductData().getProductAlternateImage().contains("/media_stat/images/layout/clear.gif")){
            	data.getProductData().setProductAlternateImage(null);
        }

        // setup response
        
        final Message responseMessage = new ProductPotatoMessage(data);
        if (!user.isHealthWarningAcknowledged() && data.getProductData().isAlcoholic()) {
            
            //APPDEV-4300 - Alcohol Products - No Details Returned if User Not Logged In 

            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage.addErrorMessage(ERR_HEALTH_WARNING, MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
        }
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
     * @throws JsonGenerationException 
     * @throws FDException 
     * @throws JsonException 
     * @throws NoSessionException 
     * @throws ModelException 
     * @throws Exception
     */
	private ModelAndView getProduct(ModelAndView model, HttpServletRequest request, HttpServletResponse response,
			SessionUser user, boolean isNewVersionForProductDetail) throws ServiceException, FDException, JsonException, NoSessionException, ModelException {
		test1();
		com.freshdirect.mobileapi.model.Product product = getProduct(request, response);

		Message responseMessage;
		if (product == null) {
			responseMessage = Message.createFailureMessage("Could not find product");
			setResponseMessage(model, responseMessage, user);
			return model;
		}

		if (!user.isHealthWarningAcknowledged() && product.isAlcoholProduct()) {
			responseMessage = new Message();

			// APPDEV-4300 - Alcohol Products - No Details Returned if User Not
			// Logged In
			responseMessage = new Product(product);

			// HtmlResponse data;

		

			if (null != ((Product) responseMessage).getProductTerms()) {
				try {
					((Product) responseMessage)
							.setProductTerms(getProductWrappedTerms(((Product) responseMessage).getProductTerms()));
				} catch (IOException e) {
					throw new FDException(e);
				} catch (TemplateException e) {
					throw new FDException(e);
				}
			}
			responseMessage.setStatus(Message.STATUS_FAILED);
			responseMessage.addErrorMessage(ERR_HEALTH_WARNING,
					MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
		} else {
			try {
				responseMessage = new Product(product);
				if (null != ((Product) responseMessage).getProductTerms()) {
					((Product) responseMessage)
							.setProductTerms(getProductWrappedTerms(((Product) responseMessage).getProductTerms()));
				}
			} catch (ModelException e) {
				throw new FDException(e);
			} catch (IOException e) {
				throw new FDException(e);
			} catch (TemplateException e) {
				throw new FDException(e);
			}
		}
		
		// STORYappdev-6259 im setting the nutrition information from the
		// same place as the website, the soy templates.
		if (isNewVersionForProductDetail) {
			ProductModel productModel = ContentFactory.getInstance().getProductByName(product.getCategoryId(),
					product.getProductId());
			((Product) responseMessage).setNutrition(NutritionInfoPanelRendererUtil.getSkuNutritionHtmlwithSoy(
					user.getFDSessionUser(), product.getDefaultSku().getOriginalSku().getProduct(), productModel,
					getServletContext(), NutritionInfoPanelRendererUtil.PanelNameEnum.PANELMOBIL_API_PRODUCT_DETAIL, 
					 ""));
		}
		setResponseMessage(model, responseMessage, user);
		return model;

	}

    /**
     * 
     * @param model
     * @param request
     * @param response
     * @param user
     * @return
     * @throws JsonException 
     */
    private ModelAndView getMultipleProductDetailPotatoes(ModelAndView model, MultipleRequest request, HttpServletResponse response, SessionUser user) throws JsonException {

        class Payload extends Message {
            public final List<ProductPotatoData> products;
            
            public Payload(List<ProductPotatoData> products) {
                this.products = products;
            }
        }

        final List<ProductPotatoData> potatoes = new ArrayList<ProductPotatoData>();
        final FDUserI uzer = user.getFDSessionUser();

        // process product keys
        for (final String productId : request.getIds()) {
            
            final ProductPotatoData data = ProductPotatoUtil.getProductPotato(productId, null, uzer, false);
            if (data != null) {
				// Temp fix for FKWeb-973
            	if(data.getProductData()!=null && data.getProductData().getProductJumboImage()!=null && 
            		data.getProductData().getProductJumboImage().contains("/media_stat/images/layout/clear.gif")){
                	data.getProductData().setProductJumboImage(null);
                }
                potatoes.add(data);
            }
        }

        // set response payload
        setResponseMessage(model, new Payload(potatoes) , user);
        
        return model; 
    }
    
    private ModelAndView getMultipleProductDetail(ModelAndView model, MultipleRequest request, HttpServletResponse response, SessionUser user)
            throws ServiceException, FDException, JsonException, NoSessionException, ModelException {
    	
    	List<String> idlist = request.getIds();
    	List<com.freshdirect.mobileapi.controller.data.Product> productList = new ArrayList<com.freshdirect.mobileapi.controller.data.Product>();
    	List<String> errorlist = new ArrayList<String>();
    	
    	for (String productid : idlist)
    	{ 
    		try{
    			com.freshdirect.mobileapi.model.Product product = com.freshdirect.mobileapi.model.Product.getProduct(productid, "xxx", null, user);
    		
    			if (product == null){
    				errorlist.add("Could not find product - " + productid);
    				continue;
    			} 
    		
    			Message responseMessage;
    			responseMessage = new Product(product);
    			if (!user.isHealthWarningAcknowledged() && product.isAlcoholProduct()) {
    				if (null != ((Product) responseMessage).getProductTerms()) {
                        ((Product) responseMessage).setProductTerms(getProductWrappedTerms(((Product) responseMessage).getProductTerms()));
                    }
    				responseMessage.setStatus(Message.STATUS_FAILED);
    				responseMessage.addErrorMessage(ERR_HEALTH_WARNING, MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
    			} else {
    				if (null != ((Product) responseMessage).getProductTerms()) {
                        ((Product) responseMessage).setProductTerms(getProductWrappedTerms(((Product) responseMessage).getProductTerms()));
                    }
    			}
    			productList.add((Product)responseMessage);
    		} catch (Exception e) {
				errorlist.add("Could not get details - " + productid + ". Reason - " + e.toString());
            	continue;
			}
    	}
    	
    	ProductList pl = new ProductList();
    	pl.setNoOfProducts(productList.size());
    	pl.setProducts(productList);
    	for(int count = 0; count < errorlist.size(); count++)
    	{
    		pl.addErrorMessage(errorlist.get(count));
    	}
    	setResponseMessage(model, pl, user);
    	return model;
    }
    
    private void test1(){
    	//getResourceAsStream("all_nutrition_display.css")
    	System.err.println("test1");
    	String foo = getServletContext().getRealPath("/");
    	 System.out.println(  "SERVLET CONTEXT PATH SERVLET CONTEXT PAT SERVLET CONTEXT PAT SERVLET CONTEXT PAT " + foo);
      	
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
    private ModelAndView getProductMoreInfo(ModelAndView model, HttpServletRequest request, HttpServletResponse response,  SessionUser user)
            throws ServiceException, FDException, JsonException, NoSessionException, ModelException {
        com.freshdirect.mobileapi.model.Product product = getProduct(request, response);
        //HtmlResponse data;

        ProductModel productModel = ContentFactory.getInstance().getProductByName(product.getCategoryId(), product.getProductId() );
        final FDUserI uzer = user.getFDSessionUser();
        model = getModelAndView(PRODUCT_MORE_INFO_TEMPLATE);
        try {
            //data = new HtmlResponse();
            response.setContentType("text/html");
            ServletContext context = getServletContext();
           // ProductMoreInfo productMoreInfo = new ProductMoreInfo(product, productModel, uzer, context );
            ProductMoreInfo productMoreInfo = new ProductMoreInfo(product);

            model.addObject("moreInfo", productMoreInfo);
            model.addObject("product", product);
            model.addObject("productImage", product.getImage(ImageType.DETAIL));
            model.addObject("mediaPath", MobileApiProperties.getMediaPath());
            model.addObject("request", request);
           // model.addObject("cssSource", getCSSasString() ) ;
            model.addObject("isFromMobile","true");
            

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
            result = com.freshdirect.mobileapi.model.Product.getProductWithAnalyticsEventSend(productId, categoryId, null, getUserFromSession(request, response), request);
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
