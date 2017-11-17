<%@ page import='com.freshdirect.webapp.util.*'
%><%@ page import='com.freshdirect.fdstore.*'
%><%@ page import='com.freshdirect.fdstore.customer.*'
%><%@ page import='com.freshdirect.storeapi.content.*'
%><%@ page import="com.freshdirect.common.pricing.*"
%><%@ page import='java.util.*'
%><%@ page import='java.text.*'
%><%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='bean' prefix='bean'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd' %><fd:CheckLoginStatus/><%!

java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");

%><%--
	ISTVAN 14/03/2007
    Check if cart has been emptied the item added in the meantime (obviously by someone manipulating it from another window)
--%><fd:FDShoppingCart id='cart' result='result'><%
   if (cart.getRecentOrderLines().size() == 0) {
%><jsp:forward page="/checkout/view_cart.jsp" /><%
   } else if (cart.getRecentOrderLines().size() == 1) {
	   // [APPREQ-425] jump to single item cart confirm page if only one item is added
	   FDCartLineI l = cart.getRecentOrderLines().get(0);
	   
	   String redir = "/cart_confirm.jsp?catId="+ request.getParameter("catId") + "&productId=" + l.getProductRef().getProductId();
%><jsp:forward page="<%= redir %>" /><%
   }
%></fd:FDShoppingCart><%

request.setAttribute("isCartConfirmPage", "true");

// OAS AD
request.setAttribute("listPos", "SystemMessage,LittleRandy,ProductNote,SideCartBottom");


String catIdParam       = request.getParameter("catId");
String productId=request.getParameter("productId");
String jspTemplate = "/common/template/both_dnav.jsp";

if (catIdParam!=null && !"".equals(catIdParam)) {
  ContentNodeModel catNode = null;
  catNode = ContentFactory.getInstance().getContentNode(catIdParam);
  if (catNode instanceof RecipeCategory) {
        jspTemplate ="/common/template/recipe_DLRnavs.jsp" ;
  } 
}

if (productId!=null && !"".equals(productId)) {

  ContentNodeModel _prodNode = null;
  _prodNode = ContentFactory.getInstance().getContentNode(productId);
  int templateType=_prodNode instanceof HasTemplateType ? ((HasTemplateType)_prodNode).getTemplateType(1) : 1;  
  if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
       jspTemplate = "/common/template/usq_sidenav.jsp";
       
  } 
  
}
Recipe recipe = null;

%><tmpl:insert template='<%=jspTemplate%>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Cart Confirmation"/>
    </tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Cart Confirmation</tmpl:put>

    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>
	

    
<tmpl:put name='content' direct='true'>

<% if (request.getAttribute("CCLLoaded") == null) { %>
<%@ include file="/shared/template/includes/ccl.jspf" %>
<% } %>

<script type="text/javascript">
	OAS_AD('ProductNote');
</script>

<%-- 

	<!-- Facebook Conversion Code for Add_to_Cart -->
	<script>
		!function(f,b,e,v,n,t,s){if(f.fbq)return;n=f.fbq=function(){n.callMethod?
		n.callMethod.apply(n,arguments):n.queue.push(arguments)};if(!f._fbq)f._fbq=n;
		n.push=n;n.loaded=!0;n.version='2.0';n.queue=[];t=b.createElement(e);t.async=!0;
		t.src=v;s=b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t,s)}(window,
		document,'script','//connect.facebook.net/en_US/fbevents.js');
		// Insert Your Facebook Pixel ID below. 
		fbq('init', '1634670833479183');
		fbq('track', 'AddToCart', {'value':'0.00','currency':'USD',
			'content_name': $jq.QueryString["productId"] !== undefined ? "pdp": $jq.QueryString["pageType"]||"DEFAULT" ,
			'content_ids':'['+<%= "atc_"+orderLine.getCategoryName()+"_"+orderLine.getSkuCode()+"_"+orderLine.getProductName() %>+']',
			'content_type':'product'});
		//fbq('track', 'AddToCart', {'value':'0.00','currency':'USD'});
	</script>
 --%>
<%-- Includes Bottom of Items added to Cart --%>
<fd:FDShoppingCart id='cart'  result='result'  successPage='/checkout/view_cart.jsp'>
<%	
	  FDCartLineModel orderLine = (FDCartLineModel)cart.getRecentOrderLines().get(0);

	  if (orderLine.getRecipeSourceId() != null) {
		recipe = (Recipe) ContentFactory.getInstance().getContentNode(orderLine.getRecipeSourceId());
	  }

      boolean groupByDepartments = false;

%><%@ include file="/includes/i_add_to_cart_confirmation.jspf" %><%

	ContentNodeModel context;
	if (orderLine.getRecipeSourceId() != null) {
		context = ContentFactory.getInstance().getContentNode( orderLine.getRecipeSourceId() );
	} else {
		context = orderLine.getProductRef().lookupProductModel();
	}
	request.setAttribute("sitePage", context.getPath());
	%>
		<fd:ProductGroup id='productNode' categoryId='<%= orderLine.getCategoryName() %>' productId='<%= orderLine.getProductName() %>'>
			<%@include file="/includes/i_cart_confirm_bottom.jspf"%>
			<!-- Facebook Conversion Code for Add_to_Cart -->
			<script>
				!function(f,b,e,v,n,t,s){if(f.fbq)return;n=f.fbq=function(){n.callMethod?
				n.callMethod.apply(n,arguments):n.queue.push(arguments)};if(!f._fbq)f._fbq=n;
				n.push=n;n.loaded=!0;n.version='2.0';n.queue=[];t=b.createElement(e);t.async=!0;
				t.src=v;s=b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t,s)}(window,
				document,'script','//connect.facebook.net/en_US/fbevents.js');
				// Insert Your Facebook Pixel ID below. 
				fbq('init', '1634670833479183');
				<%-- should match atcInfo itemId format: "atc_cat_pid_86_HMR0065413_mls_main_chicken" --%>
				fbq('track', 'AddToCart', {'value':'0.00','currency':'USD',
				'content_name': $jq.QueryString["productId"] !== undefined ? "pdp": $jq.QueryString["pageType"]||"DEFAULT" ,
				'content_ids':['<%= "atc_"+orderLine.getCategoryName()+"_"+orderLine.getSkuCode()+"_"+orderLine.getProductName() %>'],
				'content_type':'product'});
							
				/* Pinterest Code for Add_to_Cart */
		        !function(e){if(!window.pintrk){window.pintrk=function(){window.pintrk.queue.push(Array.prototype.slice.call(arguments))};var
		  		n=window.pintrk;n.queue=[],n.version="3.0";var
		  		t=document.createElement("script");t.async=!0,t.src=e;var
		  		r=document.getElementsByTagName("script")[0];r.parentNode.insertBefore(t,r)}}("https://s.pinimg.com/ct/core.js");
				// Insert Your Pinterest Pixel ID below.		
				pintrk('load','2618512501957');
				<%-- should match atcInfo itemId format: "atc_cat_pid_86_HMR0065413_mls_main_chicken" --%>
				pintrk('track', 'addtocart', {'value':'0.00','currency':'USD',
				'search_query': $jq.QueryString["productId"] !== undefined ? "pdp": $jq.QueryString["pageType"]||"DEFAULT" ,
				'line_items':['<%= "atc_"+orderLine.getCategoryName()+"_"+orderLine.getSkuCode()+"_"+orderLine.getProductName() %>'],
				});
				
		</script>
		</fd:ProductGroup>
	
	</fd:FDShoppingCart>
	<!-- End Includes Bottom of Items added to Cart -->

</tmpl:put>
</tmpl:insert>
