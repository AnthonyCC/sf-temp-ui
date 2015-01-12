package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.request.SimpleRequest;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.controller.data.response.ModifiedOrder;
import com.freshdirect.mobileapi.controller.data.response.QuickShop;
import com.freshdirect.mobileapi.controller.data.response.QuickShopLists;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Order;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.comparator.FilterOptionLabelComparator;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopListRequestObject;

/**
 * @author Rob
 *
 */
public class OrderController extends BaseController {
    private static final Category LOGGER = LoggerFactory.getInstance(OrderController.class);

    private static final String PARAM_ORDER_ID = "orderId";

    private static final String ACTION_GET_ORDER = "getorderdetail";

    private static final String ACTION_GET_QUICK_SHOP_ORDER_LIST = "getquickshoporders";

    private static final String ACTION_CANCEL_ORDER = "cancelorder";

    private static final String ACTION_LOAD_ORDER_TO_CART = "modifyorder";

    private static final String ACTION_CANCEL_ORDER_MODIFY = "cancelmodify";

    private static final String ACTION_QUICK_SHOP = "quickshop";
    
    private static final String ACTION_GET_QUICK_SHOP_EVERYITEM = "geteveryitemfordept";
    
    private static final String ACTION_GET_QUICK_SHOP_EVERYITEM_DEPT = "getdeptsforeveryitem";
    
    private static final String ACTION_GET_QUICK_SHOP_EVERYITEMEVERORDERED = "geteveryitemeverordered";
    
    
    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_GET_ORDER.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = getOrderDetail(model, user, orderId);
        } else if (ACTION_GET_QUICK_SHOP_ORDER_LIST.equals(action)) {
            model = getQuickshopOrders(model, user, request, response);
        } else if (ACTION_CANCEL_ORDER.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = cancelOrder(model, user, orderId, request);
        } else if (ACTION_LOAD_ORDER_TO_CART.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = loadOrder(model, user, orderId, request);
        } else if (ACTION_QUICK_SHOP.equals(action)) {
            String orderId = request.getParameter("orderId");
            model = getProductsFromOrder(model, user, orderId);
        } else if (ACTION_CANCEL_ORDER_MODIFY.equals(action)) {
            model = cancelOrderModify(model, user, request);
        } else if (ACTION_GET_QUICK_SHOP_EVERYITEM.equals(action)) {  
        	String orderId = request.getParameter("orderId");
        	String deptId = request.getParameter("qsDeptId");
        	String noOfOrderDays = request.getParameter("qsNoOfFilterDays");
        	Integer noOfOrderFilterDays = null;
        	if(noOfOrderDays != null && noOfOrderDays.trim().length() > 0 
        			&& !"none".equalsIgnoreCase(noOfOrderDays)) {
        		noOfOrderFilterDays =  new Integer(noOfOrderDays);
        	}
        	String sortBy = request.getParameter("qsSortBy");
        	
        	// Retrieving any possible payload
            String postData = getPostData(request, response);
            
        	System.out.println("PostData received: [" + postData + "]");
        	SearchQuery requestMessage = new SearchQuery();
            if (StringUtils.isNotEmpty(postData)) {
                requestMessage = parseRequestObject(request, response, SearchQuery.class);
            }
            model = getProductsFromOrderDept(model, user, orderId
            										, (deptId != null && deptId.trim().length() > 0 
            												&& !"all".equalsIgnoreCase(deptId)) ? deptId : null
            												, noOfOrderFilterDays
            												, sortBy
            												, requestMessage, request.getSession());
        } else if (ACTION_GET_QUICK_SHOP_EVERYITEM_DEPT.equals(action)) {
        	String orderId = request.getParameter("orderId");
        	String noOfOrderDays = request.getParameter("qsNoOfFilterDays");
        	Integer noOfOrderFilterDays = null;
        	if(noOfOrderDays != null && noOfOrderDays.trim().length() > 0 
        			&& !"none".equalsIgnoreCase(noOfOrderDays)) {
        		noOfOrderFilterDays =  new Integer(noOfOrderDays);
        	}
        	model = getDeptForQuickshopEveryItem(model, user, orderId, noOfOrderFilterDays);
        }  else if (ACTION_GET_QUICK_SHOP_EVERYITEMEVERORDERED.equals(action)) {  
        	//Every item ever ordered similar to storefront quickshop every item ever ordered
        	// Retrieving any possible payload
            String postData = getPostData(request, response);
            
        	System.out.println("PostData received: [" + postData + "]");
        	SearchQuery requestMessage = new SearchQuery();
            if (StringUtils.isNotEmpty(postData)) {
                requestMessage = parseRequestObject(request, response, SearchQuery.class);
            }
            model = getEveryItemEverOrdered(model, user, requestMessage, request.getSession());
        }

        return model;
    }

    /**
     * 
     * @param user
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView cancelOrderModify(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException,
            JsonException {
        Message responseMessage;
		try {
			ResultBundle resultBundle = Order.cancelModify(user);
			ActionResult result = resultBundle.getActionResult();
			propogateSetSessionValues(request.getSession(), resultBundle);

			responseMessage = null;
			if (result.isSuccess()) {
			    responseMessage = Message.createSuccessMessage("Order modification was successfully canceled");
			} else {
			    responseMessage = getErrorMessage(result, request);
			}
			responseMessage.addWarningMessages(result.getWarnings());
		} catch (Exception e) {
			responseMessage = new Message();
			responseMessage.addErrorMessage(traceFor(e));
//			responseMessage.setStatus(ERR_SYSTEM);
		}
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView loadOrder(ModelAndView model, SessionUser user, String orderId, HttpServletRequest request) throws FDException,
            JsonException {
    	com.freshdirect.mobileapi.controller.data.response.Order order = user.getOrder(orderId).getOrderDetail(user);
        ResultBundle resultBundle = Order.loadOrderToCartForUpdate(orderId, user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            Cart cart = user.getShoppingCart();
            CartDetail cartDetail = cart.getCartDetail(user, null);
            responseMessage = new ModifiedOrder();
            ((ModifiedOrder) responseMessage).setCartDetail(cartDetail);
            ((ModifiedOrder) responseMessage).setModificationCutoffTime(cart.getModificationCutoffTime());
            ((ModifiedOrder) responseMessage).setDeliveryAddress(order.getDeliveryAddress());
            ((ModifiedOrder) responseMessage).setPaymentMethod(order.getPaymentMethod());
            ((ModifiedOrder) responseMessage).setReservationDateTime(order.getReservationDateTime());
            ((ModifiedOrder) responseMessage).setReservationTimeRange(order.getReservationTimeRange());
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView cancelOrder(ModelAndView model, SessionUser user, String orderId, HttpServletRequest request) throws FDException,
            JsonException {
        ResultBundle resultBundle = Order.cancelOrder(orderId, user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Order was successfully canceled");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView getOrderDetail(ModelAndView model, SessionUser user, String orderId) throws FDException, JsonException {
        Order order = user.getOrder(orderId);
        Message responseMessage = order.getOrderDetail(user);
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @return
     * @throws FDException
     * @throws JsonException  
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView getQuickshopOrders(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response)
            throws FDException, JsonException {
        List<OrderInfo> infos = OrderHistory.getCompletedOrderInfos(user.getCompleteOrderHistory());

        String postData = getPostData(request, response);
        int page = 1;
        int resultMax = MobileApiProperties.getQuickshopListMax();
        LOGGER.debug("PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
        }
        ListPaginator<OrderInfo> paginator = new ListPaginator<OrderInfo>(infos, resultMax);
        QuickShopLists responseMessage = QuickShopLists.initWithOrder(paginator.getPage(page));
        responseMessage.setTotalResultCount(infos.size());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getProductsFromOrder(ModelAndView model, SessionUser user, String orderId) throws FDException, JsonException {
        Order order = user.getOrder(orderId);

        List<ProductConfiguration> products;
        try {
            products = order.getOrderProducts(orderId, user);
        } catch (ModelException e) {
            throw new FDException(e);
        }
        QuickShop quickShop = new QuickShop();
        quickShop.setProducts(products);
        setResponseMessage(model, quickShop, user);
        return model;
    }
    
    private ModelAndView getProductsFromOrderDept(ModelAndView model, SessionUser user
    													, String orderId, String deptId
    													, Integer filterOrderDays, String sortBy
    													, SearchQuery query, HttpSession session) throws FDException, JsonException {
        Order order = new Order();
        
        List<ProductConfiguration> products;
        List<ProductConfiguration> productPage = null;
        
        try {
            products = order.getOrderProductsForDept(orderId, deptId, filterOrderDays, sortBy, user);            
            if(products != null) {
            	int start = (query.getPage() - 1) * query.getMax();
            	if(start >=0 && start <= products.size()) {
            		productPage = products.subList(start, Math.min(start + query.getMax(), products.size()));
            	}
            }
        } catch (ModelException e) {
            throw new FDException(e);
        }
        QuickShop quickShop = new QuickShop();
        quickShop.setProducts(productPage);
        quickShop.setTotalResultCount(products != null ? products.size() : 0);
        setResponseMessage(model, quickShop, user);
        return model;
    }
    
	private ModelAndView getEveryItemEverOrdered(ModelAndView model, SessionUser user, SearchQuery query,
			HttpSession session) throws FDException, JsonException {
		
		List<ProductConfiguration> products;
		List<ProductConfiguration> productPage = null;

		try {
			products = loadProductsWithQuickShopFilter(user, session, query);
			if (products != null) {
				int start = (query.getPage() - 1) * query.getMax();
				if (start >= 0 && start <= products.size()) {
					productPage = products.subList(start,
							Math.min(start + query.getMax(), products.size()));
				}
			}
		} catch (ModelException e) {
			throw new FDException(e);
		}
		QuickShop quickShop = new QuickShop();
		quickShop.setProducts(productPage);
		quickShop.setTotalResultCount(products != null ? products.size() : 0);
		setResponseMessage(model, quickShop, user);
		return model;
	}
    
    private List<ProductConfiguration> loadProductsWithQuickShopFilter(SessionUser user, HttpSession session, SearchQuery query) throws ModelException {
        FDUserI fdUser = user.getFDSessionUser();//.getUser();
        
        QuickShopListRequestObject requestData = new QuickShopListRequestObject();
       
        requestData.setTimeFrame("timeFrameAll");
        requestData.setSortId("freq");
        requestData.setSearchTerm(query.getQuery());
        try {
        	FilteringFlowResult<QuickShopLineItemWrapper> result = QuickShopHelper.getQuickShopPastOrderItems(fdUser, session, requestData, requestData.convertToFilteringNavigator());
            return convertToSkuList(result, fdUser);
        } catch (FDResourceException e) {
            throw new ModelException(e);
        }
    }

    private List<ProductConfiguration> convertToSkuList(FilteringFlowResult<QuickShopLineItemWrapper> result, FDUserI fdUser) throws ModelException {
        List<ProductConfiguration> productsWithSkus = new ArrayList<ProductConfiguration>();
        if(result != null) {
        	List<FilteringSortingItem<QuickShopLineItemWrapper>> items =  result.getItems();
        	
	        for (FilteringSortingItem<QuickShopLineItemWrapper> wrapper : items) {
	        	QuickShopLineItem line = wrapper.getNode().getItem();
	        	final ProductModel productModel = ContentFactory.getInstance().getProductByName(line.getCatId(), line.getProductId());
	        	if(productModel != null) {
	                ProductConfiguration configuration = new ProductConfiguration();
	                configuration.populateProductWithModel(Product.wrap(productModel, fdUser), line.getSkuCode());
	                productsWithSkus.add(configuration);
	        	}
	        }
        }
        return productsWithSkus;
    }
    
    private ModelAndView getDeptForQuickshopEveryItem(ModelAndView model, SessionUser user, String orderId, Integer filterOrderDays) throws FDException, JsonException {
    	Order order = new Order();

    	List<Department> departments;
    	    	
    	List<FilterOption> qCartDepartments = new ArrayList<FilterOption>();
    	        
        List<FilterOption> departmentList = new ArrayList<FilterOption>();
    	try {
    		departments = order.getDeptForQuickshopEveryItem(orderId,filterOrderDays, user);

    		if(departments != null) {
    			if(departments.size() > 1) {
	    			FilterOption allDepartmentFilter =  new FilterOption();
	    	    	allDepartmentFilter.setId("all");
	    	    	allDepartmentFilter.setLabel("ALL DEPARTMENTS");
	    	        qCartDepartments.add(allDepartmentFilter);
    			}
    	        
	    		FilterOptionLabelComparator filterComparator = new FilterOptionLabelComparator();
	 		
	    		Iterator<Department> dit = departments.iterator();
	    		while (dit.hasNext()) {
	    			Department department = dit.next();
	    			FilterOption option = new FilterOption();
	    			option.setId(department.getId());
	    			option.setLabel(department.getName());
	    			option.setImages(department.getImages());
	    			departmentList.add(option);
	    		}
	
	    		Collections.sort(departmentList, filterComparator); 
	    		qCartDepartments.addAll(departmentList);
    		}
    	} catch (ModelException e) {
    		throw new FDException(e);
    	}

    	QuickShop quickShop = new QuickShop();
    	quickShop.setDepartments(qCartDepartments);
    	setResponseMessage(model, quickShop, user);
    	return model;
    }

}
