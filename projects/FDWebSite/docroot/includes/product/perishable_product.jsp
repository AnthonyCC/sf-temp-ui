<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% //expanded page dimensions
final int W_PERISHABLE_PRODUCT_LEFT = 218;
final int W_PERISHABLE_PRODUCT_CENTER_PADDING = 14;
final int W_PERISHABLE_PRODUCT_RIGHT = 369;
%>

<%@ include file="/shared/includes/product/i_product_methods.jspf" %>

<%
	List shelfLifeList = null;
	String leastShelfDays = null; // least number of shelf life days for multiple skus

//*** get needed vars from request attributes, they must exist or else we throw jsp error ***/
	FDUserI user = (FDUserI)request.getAttribute( "user" );
	String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
	ProductModel productNode = (ProductModel)request.getAttribute( "productNode" );
	String cartMode = (String)request.getAttribute( "cartMode" );
	FDCartLineI templateLine = (FDCartLineI)request.getAttribute( "templateLine" );
	ActionResult result = (ActionResult)request.getAttribute( "actionResult" );
	if ( result == null || productNode == null || cartMode == null || user == null ) {
		throw new JspException( " One or several required request attributes are missing. " );
	}

	//String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
	String prodPageRatingStuff = getProdPageRatings( productNode, response ); // get and format the product page ratings
	int templateType = productNode.getTemplateType(1);

	if ( productNode == null ) {
		throw new JspException( "Product not found in Content Management System" );
	} else if ( productNode.isDiscontinued() ) {
		throw new JspException( "Product Discontinued" );
	}

	String app = (String)session.getAttribute( SessionName.APPLICATION );
	boolean isWebApp = app == null || "WEB".equalsIgnoreCase( app );

	// ProductLabeling pl = new ProductLabeling((FDUserI) session.getAttribute(SessionName.USER), productNode);
    // String actionUrl = FDURLUtil.getProductURI( productNode, pl.getTrackingCode() );
    
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>


<div class="prodLayout-mainTable-cont">
		<% if (!mobWeb) { /* skip tables in mobWeb */ %><table border="0" cellspacing="0" cellpadding="0"><% } %>
			<% if (mobWeb) { /* at the top if mobWeb */ %>
				<div class="prodDetail-images"><%@ include file="/shared/includes/product/i_product_image.jspf" %></div>
			<% } %>
			<% if (!mobWeb) { /* skip tables in mobWeb */ %><tr valign="top"><% } %>
	
			<% if (!mobWeb) { /* skip tables in mobWeb */ %><td width="<%= W_PERISHABLE_PRODUCT_LEFT %>" align="right" class="text11"><% } %>
				<!-- Product include start -->
				<%@ include file="/shared/includes/product/i_product.jspf" %>
	<%-- **************************************** BEGIN Shelf Life ****************************************************************************** --%>
				<%
				shelfLifeList = (List)session.getAttribute("freshList");
				if(shelfLifeList == null) {
					shelfLifeList = new ArrayList();
				}
				ListIterator freshItr2 = shelfLifeList.listIterator();
				while(freshItr2.hasNext()) {
					String label = (String)freshItr2.next();
				}
	
				String shelfLife = JspMethods.getFreshnessGuaranteed(productNode);
				if(shelfLife != null && shelfLife.trim().length() > 0 ) {
						int labelCount=0;
						int valueCount=0;
						// sanity check to make sure we have 1 shelf life value per domain label
						if(shelfLifeList != null && !shelfLifeList.isEmpty()) {
							ListIterator shelfItr = shelfLifeList.listIterator();
							while(shelfItr.hasNext()) {
								String val = (String)shelfItr.next();
								//if the value is numeric it is a shelf life value
								//but don't count it if it is 0 or negative (it should not be 0 but this is an extra check)
								if(StringUtil.isNumeric(val)) {
									if(Integer.parseInt(val) > 0) {
										valueCount++;
									}
								} else {
									// if the value is not numeric it is a domain label.
									// need to make sure number of labels = number of values
									labelCount++;
								 }
							}
						}
						boolean showStackedShelfLife = labelCount == valueCount && valueCount > 0 && labelCount > 0;
						if(skuSize == 1 || showStackedShelfLife) {
					%>
							<img src="/media_stat/images/layout/clear.gif" width="<%=W_PERISHABLE_PRODUCT_CENTER_PADDING%>" height="10" border="0" hspace="0" vspace="0">
							<table border="0" cellspacing="0" cellpadding="0" style="clear: both;">
								<tr>
									<td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
									<td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="<%=W_PERISHABLE_PRODUCT_LEFT-12 %>" height="1"></td>
									<td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
								</tr>
								<tr>
									<td colspan="3" align="center" valign="top">
									<table width="0" border="0" cellspacing="0" cellpadding="5">
										<tr><td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
											<table border="0" cellspacing="0" cellpadding="0" width="<%=W_PERISHABLE_PRODUCT_LEFT-2%>">
											<%if(shelfLifeList.isEmpty() && skuSize == 1) { %>
												<tr valign="top">
													<td><img src="/media_stat/images/layout/clear.gif" width="9" height="1"></td>
													<td width="27"><img src="/media/images/site_pages/shelflife/days_<%=shelfLife%>.gif" width="27" height="27" border="0"></td>
													<td><img src="/media_stat/images/layout/clear.gif" width="9" height="1"></td>
													<td  valign="top"><img src="/media/images/site_pages/shelflife/guarant_fresh_hdr_lg.gif" width="129" height="10"><br />
													<span class="text12">at least </span><span class="title12"><%=shelfLife%> day<%= (Integer.parseInt(shelfLife) > 1) ? "s": ""%></span><span class="text12"><br/> from delivery</span></td>
													<td><img src="/media_stat/images/layout/clear.gif" width="9" height="1"></td>
												</tr>
											<% } else if(showStackedShelfLife) {
												// calculate lowest shelf life in stack sku group
												// sku with lowest shelf life value will display per domain label
												ListIterator lowLifeItr = shelfLifeList.listIterator();
												while(lowLifeItr.hasNext()) {
													String val = (String)lowLifeItr.next();
													//if the value is numeric it is a shelf life value
													if(StringUtil.isNumeric(val)) {
														if(leastShelfDays == null) {
															leastShelfDays = val;
														}
														if(Integer.parseInt(val) < Integer.parseInt(leastShelfDays)) {
															leastShelfDays = val;
														}
													}
												}
												ListIterator freshItr = shelfLifeList.listIterator();
												boolean printHeader = true;
												while(freshItr.hasNext()) {
													String label = (String)freshItr.next();
													if(freshItr.hasNext()) {
														String daysFresh = (String)freshItr.next();
												%>
														<tr valign="top">
														<% if(printHeader) { %>
															<td><img src="/media_stat/images/layout/clear.gif" width="5" height="0"></td>
															<td width="27"><img src="/media/images/site_pages/shelflife/days_<%=leastShelfDays%>.gif" width="27" height="27" border="0"></td>
															<td><img src="/media_stat/images/layout/clear.gif" width="9" height="0"></td>
															<td  valign="top"><img src="/media/images/site_pages/shelflife/guarant_fresh_hdr_lg.gif" width="129" height="10">
															<img src="/media_stat/images/layout/clear.gif" width="9" height="10">
														<%
															 printHeader = false;
														} else { %>
															<td colspan="3">&nbsp;</td>
															<td>
														<% } %>
															<table><tr><td>
															<span class="title12"><i><%=label%>:</i></span><br /><span class="text12">at least</span><span class="title12"> <%=daysFresh%> day<%= (Integer.parseInt(daysFresh) > 1) ? "s": ""%></span><span class="text12"> from delivery</span></td>
															<td><img src="/media_stat/images/layout/clear.gif" width="9" height="0"></td>
															</tr></table>
														</tr>
												<%
													}
												}%>
												<tr>
													<td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
												</tr>
											<%
											//reset list
											session.setAttribute("freshList", null);
											} %>
											</table>
										</td></tr>
									</table>
									</td>
								</tr>
								<tr>
									<td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
									<td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
									<td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
								</tr>
							</table>
							<table width="<%=W_PERISHABLE_PRODUCT_LEFT%>">
								<tr>
									<td align="right">
									<img src="/media_stat/images/layout/clear.gif" width="100%" height="6">
									<a href="javascript:pop('/shared/brandpop.jsp?brandId=bd_fd_fresh_guarantee',400,585)">Learn more about our Freshness Guarantee - CLICK&nbsp;HERE</a>
									</td>
								</tr>
							</table>
					<%
					}
				}
				//reset list
				session.setAttribute("freshList", null);
				%>
	<%-- **************************************** END Shelf Life ****************************************************************************** --%>
			<% if (!mobWeb) { /* skip tables in mobWeb */ %></td><% } %>
			<% if (!mobWeb) { /* skip tables in mobWeb */ %>
			<td width="<%=W_PERISHABLE_PRODUCT_CENTER_PADDING%>">
				<img src="/media_stat/images/layout/clear.gif" width="<%=W_PERISHABLE_PRODUCT_CENTER_PADDING%>" height="1" border="0" hspace="0" vspace="0">
			</td>
			<% } %>
	
			 <% if (!mobWeb) { /* skip tables in mobWeb */ %><td width="<%= W_PERISHABLE_PRODUCT_RIGHT %>" class="text12"><%} %>
	 	         <div style="height:4px;"></div>
	 	         <% if (!mobWeb) { /* further down if mobWeb */ %>
				 	<%@ include file="/includes/product/i_product_soc_buttons.jspf" %>
				 <% } %>
	
				<% if ( cartMode.equals( CartName.ADD_TO_CART ) && isWebApp ) { %>
			        <%@ include file="/shared/includes/product/i_product_right_column.jspf" %>
	
				<% } else {
					
					SkuModel dfltSku = (SkuModel)productNode.getSkus().get( 0 );
					FDProduct fdprod = dfltSku.getProduct();
					boolean hasNutriOrIngrd = ( fdprod.hasNutritionFacts() || fdprod.hasNutritionPanel() || fdprod.hasIngredients() );
					%>
					
					<% if (!mobWeb) { /* at the top if mobWeb */ %>
						<%@ include file="/shared/includes/product/i_product_image.jspf" %>
					<% } %>
					 
					<% Html productDesc = productNode.getProductDescription(); %>
	
					<% if ( productDesc != null ) { %>
			     		<b>About</b>
					<% } %>
					
					<%@ include file="/shared/includes/product/i_product_about.jspf" %>
	
					<% if ( hasNutriOrIngrd ) { %>
	                	<br/><a href="javascript:pop('/shared/nutrition_info.jsp?catId=<%=productNode.getParentNode().getContentName()%>&productId=<%=productNode.getContentName()%>',335,375)">Nutrition, Ingredients, and Allergens</a>
					<% } else { %>
						<%if(!EnumProductLayout.RECIPE_MEALKIT_PRODUCT.equals(productNode.getProductLayout())){ %>
						<br/>Please check product label for nutrition, ingredients, and allergens.
					<% }} %>
					<br/><br/>
	
		 	         <% if (mobWeb) { /* here for mobWeb */ %>
					 	<%@ include file="/includes/product/i_product_soc_buttons.jspf" %>
					 <% } %>
				<% } %>
				<!-- Content end -->
	<% if (!mobWeb) { /* skip tables in mobWeb */ %>
			</td>
		</tr>
	</table><% } %>
</div>

<%-- Bazaarvoice review download --%>
<% if (FDStoreProperties.isBazaarvoiceEnabled()) { /* why was this check not used? */ %>
	<script>
	  (function ($) {
	    if (window['$BV']) {<%-- corrected JS check --%>
	      $BV.ui('rr', 'show_reviews', { productId: '<%= ProductDisplayUtil.getRealProduct(productNode).getContentName() %>'});
	      
	      $(document.body).delegate("#BVRRSummaryContainer .bv-rating-label, .bv-popup-in .bv-read-review", 'click', function (e) {
	    	  var target = document.createElement('a');
	    	  target.href = window.location.href;
	    	  target.hash = "#BVRRContainer";
	    	  if(target.search.indexOf("product_view=") === -1) {
	        	  target.search += "&product_view=reviews";
	    	  } else {
	        	  target.search = target.search.replace(/product_view=[\w]+/,"product_view=reviews");
	    	  }
	    	  
	    	  window.location.href = target.href;
	      });
	    }
	  }(jQuery));
	</script>
<% } %>
