<%// this jsp will redirect to the appropriate product page based on either the productPage Layout or some other logic %>

<%@ page import='com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@page import="java.util.Locale"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<fd:CheckLoginStatus />
<%!
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%
// Handle no-product case
if (productNode==null) {
    throw new JspException("Product not found in Content Management System");
} else if (productNode.isDiscontinued()) {
    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
}


%>
<fd:ClickThru product="<%= productNode %>"/>
<%
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", productNode.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,ProductNote,SideCartBottom");

// [redirection] if the previewmode is true then do not honor the hide-URL settting
if (!ContentFactory.getInstance().getPreviewMode() && productNode.isHidden()) {
    String redirectURL = response.encodeRedirectURL(productNode.getHideUrl());
    if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
        StringBuffer buf = new StringBuffer(redirectURL);
		FDURLUtil.appendCommonParameters(buf, request.getParameterMap()); // append tracking codes, etc.
    	response.sendRedirect(buf.toString());
    	return;
    }
}

// [redirection] if there is a redirect_url setting.. then go to that regardless of the preview mode
String redir = productNode.getRedirectUrl();
if (!"".equals(redir) && !"nm".equalsIgnoreCase(redir)) {
    String redirectURL = response.encodeRedirectURL(redir);
	if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
        StringBuffer buf = new StringBuffer(redirectURL);
		FDURLUtil.appendCommonParameters(buf, request.getParameterMap()); // append tracking codes, etc.
    	response.sendRedirect(buf.toString());
        return;
    }
}

// [redirection] if it is a grocery product, then jump to the grocery_product layout within the category page
// get the category layout type
if (productNode.getLayout().isGroceryLayout()) {
    response.sendRedirect( FDURLUtil.getCategoryURI(request, productNode) );
    out.close();
    return;
}


// [redirection] Alcohol alert -> redirect health warning page
FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
if( ((CategoryModel)productNode.getParentNode()).isHavingBeer() && !yser.isHealthWarningAcknowledged()){
    String redirectURL = "/health_warning.jsp?successPage=/product.jsp"+URLEncoder.encode("?"+request.getQueryString());
    response.sendRedirect(response.encodeRedirectURL(redirectURL));
    return;
}



String tgAction = request.getParameter("action")!=null ? request.getParameter("action") :  "addToCart";

EnumProductLayout prodPageLayout = productNode.getProductLayout();

if ( prodPageLayout.canAddMultipleToCart()  ) {
	tgAction="addMultipleToCart";
}
if ("true".equals(request.getParameter("ccl"))) {
	tgAction = "CCL";
}


//** values for the shopping cart controller
String sPage = FDURLUtil.getCartConfirmPageURI(request, productNode);

String jspTemplate;
if (EnumTemplateType.WINE.equals( productNode.getTemplateType() )) {
	jspTemplate = "/common/template/usq_sidenav.jsp";
} else { //assuming the default (Generic) Template
	jspTemplate = "/common/template/both_dnav.jsp";
}

%>
<tmpl:insert template='<%=jspTemplate%>'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>
    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>
<tmpl:put name='content' direct='true'>

<%if (FDStoreProperties.isAdServerEnabled()) {%>

	<script type="text/javascript">
		OAS_AD('ProductNote');
	</script>

<%} else {%>
    <%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
<%}%>
<fd:FDShoppingCart id='cart' result='actionResult' action='<%= tgAction %>' successPage='<%= sPage %>' source='<%= request.getParameter("fdsc.source")%>' >
<%  //hand the action results off to the dynamic include
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String cartMode = CartName.ADD_TO_CART;
	FDCartLineI templateLine = null ;
	
	request.setAttribute("actionResult", actionResult);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);
%>
<%@ include file="/includes/product/cutoff_notice.jspf" %>
<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
<jsp:include page="<%= prodPageLayout.getLayoutPath() %>" flush="false"/>

</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
</fd:ProductGroup>
