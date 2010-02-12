<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@page import="org.xml.sax.*"%>
<%@page import="javax.xml.transform.*"%>
<%@page import="javax.xml.transform.stream.*"%>

<%@ page import='java.util.*' %>
<%@ page errorPage='product_error.jsp' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
public DepartmentModel findDepartment (String deptId) throws FDResourceException {
		return (DepartmentModel)ContentFactory.getInstance().getContentNode(deptId);
}

public String findParentOfCategory (String catId) throws FDResourceException {
		CategoryModel categoryNode = (CategoryModel)ContentFactory.getInstance().getContentNode(catId);
		DepartmentModel dept = categoryNode.getDepartment();
		return dept.getContentName();
}

public String getProdPageRatings(ProductModel _productNode, HttpServletResponse _response)  throws FDResourceException {
  WebProductRating webProductRating = RatingUtil.getRatings(_productNode);
  StringBuffer rtnString = new StringBuffer(200);

    if (webProductRating!=null) {
        StringBuffer ratingLines  = new StringBuffer();
        String ratingLabel = "<br><b>"+webProductRating.getRatingLabel()+"</b>";
        List ratings = webProductRating.getRatings();
        List textRatings = webProductRating.getTextRatings();

        // add rating heading and rating lines
        if (ratings.size() > 0 ) {
            if (webProductRating.getRatingLabel()!=null) {
                rtnString.append("<br><b>");
                rtnString.append(webProductRating.getRatingLabel());
                rtnString.append("</b>");
            }

            for (Iterator itrRatings = ratings.iterator();itrRatings.hasNext();) {
               ProductRating prodRating = (ProductRating)itrRatings.next();
               rtnString.append("<br><img width=\"63\" height=\"8\" src=\"/media_stat/images/template/rating3_05_0");
               rtnString.append(prodRating.getRating());
               rtnString.append(".gif\" alt=\"");
               rtnString.append(prodRating.getRating());
               rtnString.append(" (out of 5)\">&nbsp;<font class=\"text9\">");
               rtnString.append(prodRating.getRatingName().toUpperCase());
               rtnString.append("</font>");
            }
        }
        //Dont add the Compare by link and Laabel  while in CallCener ** *
        if (webProductRating.getCompareByLabel()!=null && webProductRating.getLinkParams()!=null) {
            rtnString.append("<br><a href=\"");
            rtnString.append(_response.encodeURL("/order/build_order_browse.jsp?"+webProductRating.getLinkParams()) );
            rtnString.append("\">");
            // get the label for this comparison folder.
            rtnString.append(webProductRating.getCompareByLabel());
            rtnString.append("</a><br>");
        }
        //add the text ratings
        if (textRatings.size() > 0 ) {
            rtnString.append("<br><img src=\"/media_stat/images/layout/cccccc.gif\" vspace=\"6\" width=\"200\" height=\"1\">");
            for (Iterator itrRatings = textRatings.iterator();itrRatings.hasNext();) {
               ProductRating prodRating = (ProductRating)itrRatings.next();
               rtnString.append("<br><b>");
               rtnString.append(prodRating.getRatingName());
               rtnString.append(":</b>&nbsp;");
               rtnString.append(prodRating.getRating());
            }
            rtnString.append("<br><img src=\"/media_stat/images/layout/cccccc.gif\" vspace=\"6\" width=\"200\" height=\"1\"><br>");
        }
    }
    return rtnString.toString();
}
%>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
	String index = String.valueOf(searchIdx);
	String rawList = request.getParameter("search_pad");

    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

	FDCartLineI templateLine = null;
	
	//
	// Check request for a default config
	//
	String hideUrl =productNode.getHideUrl();
	if (hideUrl!=null) {
		String redirectURL = response.encodeRedirectURL((String)hideUrl);
		//response.sendRedirect(redirectURL);
	//	return;
	}
	if (productNode==null) {
		throw new JspException("Product not found in Content Management System");
	} else if (productNode.isDiscontinued()) {
		throw new JspException("Product Discontinued");
	}

	boolean qualifies = productNode.isQualifiedForPromotions() && user.getMaxSignupPromotion()>0;
	double promotionValue = 0.0;
	if (qualifies) {
		promotionValue = user.getMaxSignupPromotion();
	}
	String prefix = String.valueOf(promotionValue);
	prefix = prefix.substring(0, prefix.indexOf('.'));
%>
<fd:ParseSearchTerms searchParams="<%= rawList %>" isBulkSearch="true" searchFor="criteria" searchList="searchTerms">

<% request.setAttribute("needsCCL","true"); %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Product</tmpl:put>

<tmpl:put name='content' direct='true'>
                                        
<jsp:include page='/includes/order_header.jsp'/>

<div class="content" style="position: relative; float: left; width: 60%; height: 70%;">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order">
	<TR VALIGN="TOP">
		<TD width="1%">&nbsp;</TD>
                <TD>
                <%
                ContentNodeModel requestNode = productNode; %>
                <%@ include file="/includes/i_bread_crumbs.jspf"%>
                </TD>
        </TR>
</TABLE>
<!-- batch search items section -->
<% if (searchTerms.size() > 0) { %>
<div class="content_fixed">
		<TABLE width="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0">
			<TR VALIGN="TOP">
				<TD width="100%">
<%	int termCounter = 0; %>
					<logic:iterate id="term" collection="<%= searchTerms %>" type="java.lang.String">
<%	if ( term.equalsIgnoreCase(criteria) ) { %>
					&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>" class="text8"><b><%= term %></b></a>&nbsp;
<%	} else {  %>
					&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>"><%= term %></a>&nbsp;
<% 	}
termCounter++; %>
					</logic:iterate>
					<hr class="gray1px">
				</TD>
			</TR>
		</TABLE>
</div>
<% } %>
<div class="content_scroll" style="height: 94%; padding-top: 0px;">
<%= searchTerms.size() == 0 ? "<br>":""%>
<TABLE width="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="TOP">
		<TD width="1%">&nbsp;</TD>
		<TD width="59%">
			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" width="550" ALIGN="CENTER">
				<TR VALIGN="TOP">
					<TD>
					<!-- product page content goes here -->
						<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" width="400" ALIGN="CENTER">
						    <TR VALIGN="TOP">
							    <TD width="180" ALIGN="RIGHT" CLASS="text11">
									<!-- Product include start -->

                                                                                          
<%
    EnumProductLayout prodPageLayout = productNode.getProductLayout();
	String cartMode = ( "true".equals( request.getParameter("ignoreAction") ) ) ? CartName.IGNORE_ACTION : CartName.ADD_TO_CART;
    String tgAction = request.getParameter("action")!=null ? request.getParameter("action") :  
       prodPageLayout.canAddMultipleToCart() ? "addMultipleToCart" :  "addToCart";
	String successPage;
	successPage = "/order/product.jsp?" + request.getQueryString();

    if (request.getParameter("template") != null) {
        FDProduct defaultProduct = FDCachedFactory.getProduct( FDCachedFactory.getProductInfo(request.getParameter("skuCode")) );
    	StringTokenizer namesTokenizer = new StringTokenizer(request.getParameter("vcNames"), ",");
    	StringTokenizer valuesTokenizer = new StringTokenizer(request.getParameter("vcValues"), ",");
    	HashMap options = new HashMap();
    	while ( namesTokenizer.hasMoreTokens() ) {
    		options.put(namesTokenizer.nextToken(), valuesTokenizer.nextToken());
    	}
		FDConfiguration configuration = new FDConfiguration(Double.parseDouble( request.getParameter("qty") ), request.getParameter("salesUnit"), options);
		String variantId = request.getParameter("variant");
    	templateLine = new FDCartLineModel( new FDSku(defaultProduct), 
    	     (productNode.isPreconfigured() ? ((ConfiguredProduct)productNode).getProduct() : productNode), 
    	      configuration, variantId, user.getPricingContext().getZoneId());
    }
%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tgAction %>' successPage='<%= successPage %>'>
<%
  //hand the action results off to the dynamic include
	request.setAttribute("actionResult", result);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);
%>
   <jsp:include page="<%=prodPageLayout.getLayoutPath()%>" flush="false"/>
</fd:FDShoppingCart>
							    </TD>
							</tr>
						</table>
						<!-- end product page content -->
					</TD>
				</TR>
			</TABLE>
			<BR>
			<BR>
		</TD>
	</TR>
</TABLE>
</div>
</div>

<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>
</fd:ParseSearchTerms>
</fd:ProductGroup>
