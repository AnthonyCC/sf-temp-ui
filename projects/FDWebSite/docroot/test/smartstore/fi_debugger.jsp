<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='logic' prefix='logic' %>
<fd:CheckLoginStatus />
<%
	Iterator it;
	URLGenerator urlG = new URLGenerator(request);
	String origURL = urlG.build();

	/* No of Items */
	String defaultNoOfItems = "5";

	String noOfItems = urlG.get("noOfItems");
	if (noOfItems == null || noOfItems.length() == 0
			|| !NumberUtils.isNumber(noOfItems)) {
		noOfItems = defaultNoOfItems;
	}
	if (defaultNoOfItems.equals(noOfItems)) {
		urlG.remove("noOfItems");
	} else {
		urlG.set("noOfItems", noOfItems);
	}
	int i_noOfItems = 5;
	try {
		i_noOfItems = Integer.parseInt(noOfItems);
	} catch (NumberFormatException e) {
	}

	/* category Id */
	String defaultCategoryId = "";

	String categoryId = urlG.get("categoryId");
	if (categoryId == null || categoryId.length() == 0) {
		categoryId = defaultCategoryId;
	}
	if (defaultCategoryId.equals(categoryId)) {
		urlG.remove("categoryId");
	} else {
		urlG.set("categoryId", categoryId);
	}

	/* category */
	ContentNodeModel categoryNode = defaultCategoryId.equals(categoryId) ? null
			: ContentFactory.getInstance().getContentNode(categoryId);
	CategoryModel category = null;
	if (categoryNode != null) {
		if ((categoryNode instanceof CategoryModel)) {
			category = (CategoryModel) categoryNode;
		}
	}

	/* category name */
	String categoryName = null;
	if (category != null) {
		categoryName = category.getFullName();
	}

	/* user name */
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	String userId = user.getUserId();
	
	/* redirect */
	String newURL = urlG.build();

	// debug
	System.err.println("orig URI: " + origURL);
	System.err.println("generated URI: " + newURL);
	System.err.println("# of items to display: " + noOfItems);
	System.err.println("category Id: " + categoryId);
	System.err.println("category name: " + categoryName);

	if (!origURL.equals(newURL)) {
		response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	}
%>

<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.cms.ContentNodeI"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>FEATURED ITEMS DEBUGGER - <%=categoryName%></title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:right;}
.test-page .rec-options{border:1px solid black;font-weight:bold;}
.test-page .rec-options .view-label{text-transform:capitalize;}
.test-page .rec-options table {width:auto;}
.test-page .rec-options table td{vertical-align:top;padding:5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
.test-page .var-comparator{margin-top:60px;}
.test-page .var-comparator td{width:50%;text-align:center;vertical-align:top;}
.test-page .var-comparator .left{padding-right:20px;}
.test-page .var-comparator .right{padding-left:20px;}
.test-page .var-comparator .var-cmp-head{margin-bottom:20px;}
.test-page .var-comparator .var-cmp-head .title14 {margin-bottom:5px;}
.test-page .prod-items td{border:1px solid black;width:auto;padding:5px;}
.test-page .prod-items td.pic{width:100px;}
.test-page .prod-items td.info{text-align:left;vertical-align:top;}
.test-page .prod-items .taxonomy{font-style:italic;}
.test-page .prod-items td.info div{position:relative;height:80px;}
.test-page .prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.test-page .prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.test-page .prod-items .positive{color:#006600;}
.test-page .prod-items .negative{color:#990000;}
.test-page .prod-items .unknown{color:#FF9900;}
.n_weight{font-weight:normal;}
.not-found{color:red;}
p.error{color:red !important;margin:20px 0px;}
p.fi{margin:20px 0px;}
	</style>
</head>
<body class="test-page">
	<form method="get" action="<%=request.getRequestURI()%>">
    <div class="rec-chooser title14">
    	<% if (user != null && user.getIdentity() != null) { %>
    	<span>Logged in as: <%= userId %></span>
    	<% } else { %>
    	<span>Not logged in</span>
    	<% } %>
    </div>
    <div class="rec-options" class="rec-chooser title14">
    	<table>
    		<tr>
    			<td class="text12">
    				<p class="label">
    					<span
    							title="The algorithm return the maximun # of elements you specify here"
    							># of items to display</span>
    				</p>
    				<p>
    					<input type="text" name="noOfItems" value="<%= noOfItems %>"
    							onfocus="this.select();"
    							onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
    							title="Press &lt;Enter&gt; to activate the entered value">
    				</p>
    			</td>
    			<td>
    				<p class="label">
    					<span title="You have to specify a category for Featured Items selection">Category id</span>
    				</p>
    				<p>
    					<input type="text" name="categoryId" value="<%= categoryId %>"
    							onfocus="this.select();"
    							onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
    							title="Press &lt;Enter&gt; to activate the entered category">
    				</p>
    				<%
    					if (categoryName != null) {
    				%>
    				<p class="result">
    					<span title="The name of the category found">Category name: <span
    							class="n_weight"><%= categoryName %> (<%= category.getContentKey().getId() %>)</span></span>
    				</p>
    				<p class="result">
    					<span title="The number of selection slots is used to display items from Favorite Products list">Manual selections slots: <span
    							class="n_weight"><%= category.getManualSelectionSlots() %></span></span>
    				</p>
    				<p class="result">
    					<span title="The list of Featured Products of the given Category">Featured products:</span>
    				</p>
    				<%
    						if (category.getFeaturedProducts() != null && category.getFeaturedProducts().size() > 0) {
    				%>
    				<p class="result label n_weight"
    						title="The list of Featured Products of the given Category">
    				<%
    							List featuredProducts = category.getFeaturedProducts();
    							for (int i = 0; i < featuredProducts.size(); i++) {
    								ProductModel product = (ProductModel) featuredProducts.get(i);
    								if (i != 0) {
    				%>
    					<br>
    				<%
    								}
    				%>
    					&bull; <%= product.getFullName() %> (<%= product.getContentKey().getId() %>)
    				<%
    							}
    				%>
    				</p>
    				<%
    						} else {
    				%>
    				<p class="result label n_weight"
    						title="The list of Featured Products of the given Category">
    					&lt;empty&gt;
    				</p>
    				<%
    						}
    				%>
    				<p class="result">
    					<span title="The list of Candidate Categories used to select Candidate Products">Candidate list:</span>
    				</p>
    				<%
    						if (category.getCandidateList() != null && category.getCandidateList().size() > 0) {
    				%>
    				<p class="result n_weight"
    						title="The list of Candidate Categories used to select Candidate Products">
    				<%
    							List candidateList = category.getCandidateList();
    							for (int i = 0; i < candidateList.size(); i++) {
    								CategoryModel cat = (CategoryModel) candidateList.get(i);
    								if (i != 0) {
    				%>
    					<br>
    				<%
    								}
    				%>
    					&bull; <%= cat.getFullName() %> (<%= cat.getContentKey().getId() %>)
    				<%
    							}
    				%>
    				</p>
    				<%
    						} else {
    				%>
    				<p class="result n_weight"
    						title="The list of Candidate Categories used to select Candidate Products">
    					&lt;empty&gt;
    				</p>
    				<%
    						}
    					}
    				%>
    			</td>
    			<td>
    				<p class="label">
    					<span title="The items in the Cart of the current User. Items listed here cannot appear among the Featured Items"
    							>Cart items:</span>
    				</p>
    				<p class="n_weight"
    						title="The items in the Cart of the current User. Items listed here cannot appear among the Featured Items">
    					<% 	
    						boolean notFirst = false;
    						if (user != null && user.getShoppingCart() != null) {
    							List orderlines = user.getShoppingCart().getOrderLines();
    							for(Iterator i = orderlines.iterator(); i.hasNext();) {
    								FDCartLineI cartLine = (FDCartLineI) i.next();
    								ContentNodeI prod = SmartStoreUtil.getProductContentKey(cartLine.getSkuCode()).getContentNode();
    								if (notFirst) {
    					%>
    					<br>
    					<%
    								}
    					%>
    					&bull; <span><%= prod.getLabel() %>  (<%= prod.getKey().getId() %>)</span>
    					<%
    								notFirst = true;
    							}
    						}
    					%>
    				</p>
    			</td>
    			<td>
    				<p>
    					<span title="The Cohort the User belongs to">User cohort:</span>
    				</p>
    				<p class="result n_weight label"
    						title="The Cohort the User belongs to">
    					<%
    						String cohortName = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
    						if (cohortName != null) {
    					%>
    					<span><%= cohortName %></span>
    					<%
    						} else {
    					%>
    					<span>&lt;unknown&gt;</span>
    					<%
    						}
    					%>
    				</p>
    				<p class="label">
    					<span title="The Algorithm (Variant) which is assigned to the Cohort the User belongs to">Applied algorithm (variant):</span>
    				</p>
    				<p class="n_weight"
    						title="The Algorithm (Variant) which is assigned to the Cohort the User belongs to">
    					<%
    						RecommendationService rs = SmartStoreUtil.getRecommendationService(user, EnumSiteFeature.FEATURED_ITEMS, null);
    						if (rs != null) {
    					%>
    					<span><%= rs.getVariant().getId() %></span>
    					<%
    						} else {
    					%>
    					<span>&lt;unknown&gt;</span>
    					<%
    						}
    					%>
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
	</form>
	<% if (category != null) { %>
	<p class="fi">
	<fd:FeaturedItemsRecommendations id="recommendations"
			currentNode="<%= category %>" itemCount="<%= i_noOfItems %>">
		<%
			if (recommendations.getContentNodes().size() > 0) {
				request.setAttribute("recommendationsRendered", "true");
	
				List products = recommendations.getContentNodes();
		%>
		<table cellpadding="0" cellspacing="0" width="550" border="0">
			<tr>
				<td class="title14" colspan="<%= products.size() * 2 %>">
					Popular Items
					<br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="8">
				</td>
			</tr>
			<tr valign="bottom">
				<logic:iterate id='contentNode' collection="<%=products%>"
						type="com.freshdirect.fdstore.content.ProductModel">
					<%
						ProductModel productNode = contentNode;
						String fiRating = "";
						boolean fiIsDeal = false; // is deal?
						int fiDeal = 0; // deal percentage
					%>
					<fd:ProduceRatingCheck>
					<%
						fiRating = JspMethods.getProductRating(productNode);
					%>
					</fd:ProduceRatingCheck>
					<fd:FDProductInfo id="productInfo"
							skuCode="<%=productNode.getDefaultSku().getSkuCode()%>">
					<%
						fiIsDeal = productInfo.isDeal();
						if (fiIsDeal) {
							fiDeal = productInfo.getDealPercentage();
						}
					%>
					</fd:FDProductInfo>
					<%
						String actionURI = FDURLUtil.getProductURI(productNode,
								"feat", recommendations.getVariant().getId());
					%>
					<%-- display a product --%>
					<td align="center" width="105" valign="bottom">
					<%
						if (fiIsDeal) {
					%>
					<div id="prod_container"
							style="height: 90px; width: 100px; text-align: left;">
						<fd:ProductImage product="<%=productNode%>" action="<%=actionURI%>" />
					</div>
					<div style="position: absolute;" id="sale_star">
						<a href="<%=actionURI%>">
							<img alt="SAVE <%=fiDeal%>%"
									src="/media_stat/images/deals/brst_sm_<%=fiDeal%>.gif"
									style="BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px">
						</a>
					</div>
					<%
						} else {
					%> 
					<fd:ProductImage product="<%=productNode%>" action="<%=actionURI%>" />
					<%
					 	}
					%>
					</td>
					<td width="10">
						<img src="/media/images/layout/clear.gif" width="8" height="1">
					</td>
				</logic:iterate>
			</tr>
			<tr valign="top">
				<logic:iterate id='contentNode' collection="<%=products%>"
						type="com.freshdirect.fdstore.content.ProductModel">
					<%
						ProductModel productNode = contentNode;
						String fiRating = "";
						String fiProdPrice = null;
						String fiProdBasePrice = null;
						boolean fiIsDeal = false; // is deal?
						int fiDeal = 0; // deal percentage
					%>
					<fd:ProduceRatingCheck>
					<%
						fiRating = JspMethods.getProductRating(productNode);
					%>
					</fd:ProduceRatingCheck>
					<fd:FDProductInfo id="productInfo" skuCode="<%=productNode.getDefaultSku().getSkuCode()%>">
					<%
						fiProdPrice = JspMethods.currencyFormatter
								.format(productInfo.getDefaultPrice())
								/** +"/"+productInfo.getDisplayableDefaultPriceUnit().toLowerCase() **/
								;
	
						fiIsDeal = productInfo.isDeal();
						if (fiIsDeal) {
							fiProdBasePrice = JspMethods.currencyFormatter
								.format(productInfo.getBasePrice()); //+"/"+ productInfo.getBasePriceUnit().toLowerCase();
							fiDeal = productInfo.getDealPercentage();
						}
					%>
					</fd:FDProductInfo>
					<%
						String actionURI = FDURLUtil.getProductURI(productNode,
								"feat", recommendations.getVariant().getId());
					%>
					<%-- display a product --%>
					<td align="center" WIDTH="105">
					<%
						if (productNode.isDisplayable()) {
					%>
					<div>
						<a href="<%=actionURI%>"><%@
							include file="/includes/product/i_prd_name.jspf"
						%></a>
					</div>
					<%
						} else {
					%>
					<div>
						<a style="color: #999999" href="<%=actionURI%>"><%@
							include file="/includes/product/i_prd_name.jspf"
						%></a>
					</div>
					<%
						}
						if (fiRating != null && fiRating.trim().length() > 0) {
					%>
					<div class="center">
						<img name="<%=fiRating%>" src="/media_stat/images/ratings/<%=fiRating%>.gif" width="59"
								height="11" alt="" border="0">
					</div>
					<%
						}
						if (fiIsDeal) {
					%>
					<div style="FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00"><%=fiProdPrice%></div>
					<div style="FONT-SIZE: 7pt; COLOR: #888">(was <%=fiProdBasePrice%>)</div>
					<%
						} else {
					%>
					<div class="favoritePrice"><%=fiProdPrice%></div>
					<%
						}
					%>
					</td>
					<td width="10">
						<img src="/media/images/layout/clear.gif" width="8" height="1">
					</td>
				</logic:iterate>
			</tr>
		</table>
		<%
			}
		%>
	</fd:FeaturedItemsRecommendations>
	</p>
	<% } else { %>
	<p class="error">
		<span class="title18">You have to set category to be able to display recommendations!</span>
	</p>
	<% } %>
</body>
</html>