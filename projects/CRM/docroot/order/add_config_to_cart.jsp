<%@ page import="java.util.*" %>

<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<%
        String queryString = request.getQueryString();
	
	// Get the customer's shopping cart
	//
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDCartModel cart = user.getShoppingCart();
	if (cart == null) {
    	//
        // user doesn't have a cart yet, create one for them and put it in the session
        //
    	user.setShoppingCart( new FDCartModel() );
		session.setAttribute(SessionName.USER, user);
    }
	//
	// Get relevant product data
	//
	ProductModel productModel = ContentFactory.getInstance().getProductByName(request.getParameter("catId"), request.getParameter("productId"));
	
	DepartmentModel dept = productModel.getDepartment();
	
	try {
    	FDProduct product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo( request.getParameter("skuCode") ));
		String confDescr = "";
		
		String requestedUnit = request.getParameter("salesUnit");
		FDSalesUnit salesUnit;
		if (product.getSalesUnits().length == 1) {
			// get the default sales unit
			salesUnit = product.getSalesUnits()[0];
		} else {
			salesUnit = product.getSalesUnit(requestedUnit);
		}

		if (salesUnit == null) {
			throw new JspException("Invalid sales unit: " + requestedUnit);
		}
		
		//
		// Construct the options map
		//
		HashMap options = new HashMap();
		StringTokenizer namesTokenizer = new StringTokenizer(request.getParameter("vcNames"), ",");
		StringTokenizer valuesTokenizer = new StringTokenizer(request.getParameter("vcValues"), ",");
		while ( namesTokenizer.hasMoreTokens() ) {
			options.put(namesTokenizer.nextToken(), valuesTokenizer.nextToken());
		}
		
		
		FDConfiguration configuration = new FDConfiguration(Double.parseDouble( request.getParameter("quantity") ), salesUnit.getName(), options);		
		FDCartLineModel cartLine = new FDCartLineModel(new FDSku(product), productModel.getProductRef(), configuration, null);
	
		cart.addOrderLine(cartLine);
		//Log that an item has been added.
		FDEventUtil.logAddToCartEvent(cartLine, request);

			try {
				cart.refreshAll();
			} catch (FDException e) {
				throw new JspException(e.getMessage());
			}

			user.updateUserState();
			cart.sortOrderLines();
			
	} catch (FDResourceException ex) {
    	ex.printStackTrace();
		throw new JspException();
	}

        /* need to get the redirectUrl parameter, but the getRequest may ignore portion that contains the & character.
          so we've got to do it ourselves */
        String redirectURL = null;
        StringBuffer destinationBuf = new StringBuffer(80);
        if (queryString!=null) {
            int redirURLPos = queryString.indexOf("redirectUrl=");
            if (redirURLPos!=-1 && redirURLPos+12<queryString.length()) {
                redirectURL = queryString.substring(redirURLPos+12);
            }
        }
        if (redirectURL!=null && redirectURL.length()>0 ) {
            destinationBuf.append(redirectURL);
        } 

        if (destinationBuf.toString().indexOf("searchIndex")==-1) {
            if (destinationBuf.length()>0) {
                destinationBuf.append("?"); 
            } else destinationBuf.append("&");
            destinationBuf.append("searchIndex=");
            destinationBuf.append( request.getParameter("searchIndex") );
        }
	response.sendRedirect( destinationBuf.toString() );
return;
%>