<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);%>
<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

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

<%
request.setAttribute("listPos", "SystemMessage,LittleRandy,ProductNote,SideCartBottom");
String catIdParam       = request.getParameter("catId");
String jspTemplate = "/common/template/both_dnav.jsp";

if (catIdParam!=null && !"".equals(catIdParam)) {
  ContentNodeModel catNode = null;
  catNode = ContentFactory.getInstance().getContentNode(catIdParam);
  if (catNode instanceof RecipeCategory) {
        jspTemplate ="/common/template/recipe_DLRnavs.jsp" ;
  }


    int templateType=catNode instanceof HasTemplateType ? ((HasTemplateType)catNode).getTemplateType(1) : 1;
    if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
       jspTemplate = "/common/template/usq_sidenav.jsp";
    } else { //assuming the default (Generic) Template
        jspTemplate = "/common/template/both_dnav.jsp";
    }


}
Recipe recipe = null;

%>

<tmpl:insert template='<%=jspTemplate%>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Cart Confirmation"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Cart Confirmation</tmpl:put> --%>

    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>



<tmpl:put name='content' direct='true'>

  <div id='oas_ProductNote'>
  	<SCRIPT LANGUAGE=JavaScript>
  		<!--
  		OAS_AD('ProductNote');
  		//-->
  	</SCRIPT>
  </div>

	<!-- Includes Bottom of Items added to Cart -->
	<fd:FDShoppingCart id='cart'  result='result'  successPage='/checkout/view_cart.jsp'>
<%
	  FDCartLineModel orderLine = (FDCartLineModel)cart.getRecentOrderLines().get(0);

	  if (orderLine.getRecipeSourceId() != null) {
		recipe = (Recipe) ContentFactory.getInstance().getContentNode(orderLine.getRecipeSourceId());
	  }

      boolean groupByDepartments = false;
%>
	<%@ include file="/includes/i_add_to_cart_confirmation.jspf" %>

	<%
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
		</fd:ProductGroup>

	</fd:FDShoppingCart>
	<!-- End Includes Bottom of Items added to Cart -->

</tmpl:put>
</tmpl:insert>
