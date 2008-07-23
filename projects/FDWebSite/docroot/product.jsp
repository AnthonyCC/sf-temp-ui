<%// this jsp will redirect to the appropriate product page based on eitherthe  productPage Layout or some other logic %>

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
<fd:ClickThru product="<%= productNode %>"/>
<%
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", productNode.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,ProductNote,SideCartBottom");
%>

<%
Attribute attrib=productNode.getAttribute("HIDE_URL");
//if the previewmode is true then do not honor the hide-URL settting
if (!ContentFactory.getInstance().getPreviewMode()) {
    if (attrib!=null) {
        String redirectURL = response.encodeRedirectURL((String)attrib.getValue());
	   if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
    }
}
//if there is a redirect_url setting.. then go to that regardless of the preview mode
attrib=productNode.getAttribute("REDIRECT_URL");
if (attrib!=null && !"nm".equalsIgnoreCase((String)attrib.getValue())  && !"".equals(attrib.getValue())) {
    String redirectURL = response.encodeRedirectURL((String)attrib.getValue());
	if (redirectURL.toUpperCase().indexOf("/PRODUCT.JSP?")==-1) {
    	response.sendRedirect(redirectURL);
        return;
    }
}

if (productNode==null) {
    throw new JspException("Product not found in Content Management System");
} else if (productNode.isDiscontinued()) {
    throw new JspException("Product Discontinued :"+request.getParameter("productId"));
}

// if it is a grocery product, then jump to the grocery_product layout within the category page
// get the category layout type
Attribute layoutAttrib = productNode.getAttribute("LAYOUT");
boolean isCallCenterApp = "callCenter".equalsIgnoreCase((String)session.getAttribute(SessionName.APPLICATION));
if (layoutAttrib!=null) {
    EnumLayoutType layoutType = EnumLayoutType.getLayoutType(((Integer)layoutAttrib.getValue()).intValue());
    if (layoutType!=null && layoutType.getName().toUpperCase().startsWith("GROCERY") ) {
        String trk = request.getParameter("trk");
        String redirectURL = response.encodeRedirectURL("/category.jsp?catId="+request.getParameter("catId")+"&prodCatId="+request.getParameter("catId")+"&productId="+productNode+"&trk=");
        if (trk !=null){
	        redirectURL += trk;
	        // SmartStore tracking (trk=dyf&variant=<variant ID>)
	        //   include variant parameter as well
	        if (request.getParameter("variant") != null) {
	        	redirectURL += "&variant=" + URLEncoder.encode(request.getParameter("variant"), "UTF-8");
	        }
		if (request.getParameter("fdsc.source") != null) {
			redirectURL += "&fdsc.source=" + URLEncoder.encode(request.getParameter("fdsc.source"));
		}
        } else {
        	redirectURL += "prod";
        }
        response.sendRedirect(redirectURL);
        out.close();
        return;
    }
}

Attribute hasBeerAttrib=productNode.getParentNode().getAttribute("CONTAINS_BEER");
FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
if(hasBeerAttrib != null && Boolean.TRUE.equals(hasBeerAttrib.getValue()) && !yser.isHealthWarningAcknowledged()){
    String redirectURL = "/health_warning.jsp?successPage=/product.jsp"+URLEncoder.encode("?"+request.getQueryString());
    response.sendRedirect(response.encodeRedirectURL(redirectURL));
}


String productPage; 
String tgAction = request.getParameter("action")!=null ? request.getParameter("action") :  "addToCart";
EnumProductLayout prodPageLayout = productNode.getProductLayout();
productPage = prodPageLayout.getLayoutPath();

if ( prodPageLayout.canAddMultipleToCart()  ) {
        tgAction="addMultipleToCart";
} 


//** values for the shopping cart controller
String ptrk = request.getParameter("trk") != null ? request.getParameter("trk") : "";
String sPage = "/cart_confirm.jsp?catId="+productNode.getParentNode().getContentName()+"&productId="+productNode.getContentName()+"&trk="+ptrk;
if (request.getParameter("variant") != null) {
	// SmartStore variant tracking, add variant to the URL of the confirmation page
	sPage += "&variant=" + URLEncoder.encode(request.getParameter("variant"), "UTF-8");
}

int templateType = productNode.getAttribute("TEMPLATE_TYPE", 1);
String jspTemplate;
if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
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
    <SCRIPT LANGUAGE=JavaScript>
    <!--
    OAS_AD('ProductNote');
    //-->
    </SCRIPT>
<%} else {%>
    <%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
<%}%>


<%-- CCL --%>
<%
   if ("true".equals(request.getParameter("ccl"))) {
	   tgAction = "CCL";
   }
%>

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
<jsp:include page="<%=productPage%>" flush="false"/>

</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
</fd:ProductGroup>
