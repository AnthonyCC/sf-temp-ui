<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
    java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<%-- SIVACHANDAR 07/03/2007
     Check if FDSESSION USER EXISTS ELSE REDIRECT TO LOGIN PAGE
--%>
<fd:CheckLoginStatus />
<%-- ISTVAN 14/03/2007
     Check if cart has been emptied the item added in the meantime (obviously by someone manipulating it from another window)
--%>
<fd:FDShoppingCart id='cart' result='result'>
<%
   if (cart.getRecentOrderLines().size() == 0) {
%>
   <jsp:forward page="/checkout/view_cart.jsp" />
<%
   }
%>
</fd:FDShoppingCart>



<%    ProductModel productNode=null;   
      List orderLineItems = new ArrayList();
      String jspTemplate=null;
      String catIdParam       = request.getParameter("catId");
	  boolean isWineProductAdded = false;

Recipe recipe = null;

%>

<fd:FDShoppingCart id='cart'  result='result'  successPage='/checkout/view_cart.jsp'>
<%
   FDCartLineI recentOrdLine = (FDCartLineI)cart.getRecentOrderLines().get(0);

	if (recentOrdLine.getRecipeSourceId() != null) {
		recipe = (Recipe) ContentFactory.getInstance().getContentNode(recentOrdLine.getRecipeSourceId());
	}
%>

<fd:ProductGroup id='prodNode' categoryId='<%= recentOrdLine.getCategoryName() %>' productId='<%= recentOrdLine.getProductName() %>'>
<%
   orderLineItems.add((FDCartLineI)cart.getRecentOrderLines().get(0));
    int templateType=prodNode.getTemplateType(1);
    if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
       jspTemplate = "/common/template/usq_sidenav.jsp";
	   isWineProductAdded = true;
    } else { //assuming the default (Generic) Template
        jspTemplate = "/common/template/both_dnav.jsp";
    }

    if (catIdParam!=null && !"".equals(catIdParam)) {
        ContentNodeModel catNode = null;
        catNode = ContentFactory.getInstance().getContentNode(catIdParam);
	if (catNode instanceof RecipeCategory) {
		jspTemplate ="/common/template/recipe_DLRnavs.jsp" ;
	} 
    }


    if (prodNode.getProductLayout().canAddMultipleToCart()) {
      for (int olIdx = 1; olIdx < cart.getRecentOrderLines().size();olIdx++) {
          orderLineItems.add((FDCartLineI)cart.getRecentOrderLines().get(olIdx));
      }
   }
   productNode = prodNode;
%>
</fd:ProductGroup>

<%
    //--------OAS Page Variables-----------------------
    request.setAttribute("sitePage", productNode.getPath());
    request.setAttribute("listPos", "LittleRandy,SystemMessage,SideCartBottom");
    FDUserI userd = (FDUserI)session.getAttribute(SessionName.USER);
%>

<tmpl:insert template='<%=jspTemplate%>'>
  <tmpl:put name='title' direct='true'>FreshDirect - Confirmation</tmpl:put>
 <tmpl:put name='content' direct='true'>
            <table cellpadding="0" cellspacing="0" border="0" width="400">
                <tr>
                <td colspan="2">
					<%  if (isWineProductAdded) { %>
						<img src="/media_stat/images/layout/clear.gif" width="1" height="15"><br>
					<% } %>
					<img src="/media_stat/images/template/confirmation/you_have_just_added.gif" width="233" height="13" border="0" alt="YOU HAVE JUST ADDED TO YOUR CART:">
                    <br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td>
                </tr>

<%
   ProductModel prdNode=null;
   int itemShown = 0;
   for (int olIdx  = 0; olIdx < orderLineItems.size(); olIdx++) {  
    FDCartLineI orderLine = (FDCartLineI)orderLineItems.get(olIdx);    %>

<fd:ProductGroup id='prodNode' categoryId='<%= orderLine.getCategoryName() %>' productId='<%= orderLine.getProductName() %>'>  
<%  prdNode = prodNode;  %>
</fd:ProductGroup>
<%
    String earliestAvailability = prdNode.getSku(orderLine.getSkuCode()).getEarliestAvailabilityMessage();

    FDProduct defaultProduct = prdNode.getDefaultSku().getProduct();
	boolean displayShortTermUnavailability = defaultProduct.getMaterial().getBlockedDays().isEmpty();
	
    boolean isPricedByLB = ("LB".equalsIgnoreCase((defaultProduct.getPricing().getZonePrice(userd.getPricingContext().getZoneId()).getMaterialPrices()[0]).getPricingUnit()));
    boolean isSoldByLB = isPricedByLB && ("LB".equalsIgnoreCase((defaultProduct.getSalesUnits()[0]).getName()));
    ContentFactory contentFactory = ContentFactory.getInstance();
    Image confirmImage = prdNode.getConfirmImage();
%>
    <tr valign="top">
     <td><font class="title18"><%= orderLine.getDescription() %></font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/product_modify.jsp?cartLine=<%= orderLine.getRandomId() %>&trk=conf"><img src="/media_stat/images/buttons/edit_product.gif" width="32" height="14" alt="Edit" border="0"></a>
     <br><font class="space4pix"><br></font>
     Quantity: <span class="text10bold"><display:OrderLineQuantity product="<%= prdNode %>" orderline="<%= orderLine %>" customer="<%= userd %>"/></span>
	    <br><font class="space2pix"><br></font>
	       Options: <font class="text10bold"><%= orderLine.getConfigurationDesc() %></font>
	    <br><font class="space2pix"><BR></font>
	    Est. Price: <font class="text10bold"><%= currencyFormatter.format(orderLine.getPrice()) %></font><br><br>
	    <fd:CCLCheck>
			<!-- Add to Shopping List  -->
			<a href="/unsupported.jsp" onclick="CCL.add_recent_cart_items(); return false;"><img src="/media_stat/ccl/lists_link_with_icon_dfgs.gif" style="border: 0;"/></a><span style="padding-left: 15px"><fd:CCLNew/></span>
			<div><fd:CCLNew template="/common/template/includes/ccl_moreabout.jspf"/></div>
        </fd:CCLCheck>
     </td>
     <td valign="bottom" align="right">
<%   if (itemShown < 1) {   %>  
       <img src="<%= confirmImage.getPath() %>" border="0" width="<%= confirmImage.getWidth() %>" height="<%= confirmImage.getHeight() %>">
<%   } else {  %>
       <img src="/media_stat/images/layout/clear.gif" width="30" height="1" border="0">
<%   }  %>
    </td></tr>
<%  if(displayShortTermUnavailability && earliestAvailability != null) {%>
        <tr><td colspan="2"><br><font class="text11rbold">Reminder: Earliest Delivery <%=earliestAvailability%><br></font></td></tr>
<%  }
            itemShown++;
  }
  
  int level = userd.getLevel();
  if(level == FDUserI.GUEST && !userd.isInZone() && !userd.isCorporateUser()){%>
    <tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
    <tr><td class="text12gr" colspan="2"><b>Please note:</b> We are not yet in your area and cannot complete your delivery.</td></tr>
<%}   %>

    <%-- spacers --%>
    <tr>
      <td><img src="/media_stat/images/layout/clear.gif" width="320" height="10"></td>
      <td><img src="/media_stat/images/layout/clear.gif" width="80" height="10"></td>
    </tr>
    <tr>
      <td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    </tr>
    </table>
    <%@ include file="/includes/i_cart_confirm_bottom.jspf"%>
</tmpl:put>
</tmpl:insert>
</fd:FDShoppingCart>

