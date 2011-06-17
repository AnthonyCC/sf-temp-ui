<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'/>

<%
RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
%>

<% 	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
	
	String custFirstName = user.getFirstName();
	int firstNameLength = custFirstName.length();
	int firstNameLastIndex = firstNameLength - 1;
	String orderId = null; 
	String qsDeptId = request.getParameter("qsDeptId");
	boolean hasDeptId = qsDeptId != null && !"".equals(qsDeptId);
	String tmpl = hasDeptId ? "/common/template/quick_shop_nav.jsp" : "/common/template/quick_shop.jsp" ;
	boolean showLink = false;
	boolean selected = false;
	String qsMenuLink = "";
	String actionName = request.getParameter("fdAction");

	if (actionName == null) {
		actionName = "";
	}

%>
<%@ include file="/quickshop/includes/department_nav.jspf" %>
<fd:QuickShopController id="quickCart" orderId="every" action="<%= actionName %>">
<tmpl:insert template='<%= tmpl %>'>
    	<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from Every Item Ordered</tmpl:put>
     	<%--tmpl:put name='banner' direct='true'><a href="/newproducts.jsp"><img src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif" width="140" height="108" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br></tmpl:put--%>
		
		<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br></font><a href="/quickshop/every_item.jsp"><img src="/media_stat/images/template/quickshop/qs_every_item_catnav.gif" width="80" height="38" border="0"></a><br><font class="space4pix"><br></font><%= departmentNav.toString() %><br><br></tmpl:put>
		
		<tmpl:put name='content' direct='true'>
		

<script language="javascript" src="/assets/javascript/grocery_product.js"></script>
<script language="javascript">
function doSort(sortBy){
document.qs_cart.sortBy.value=sortBy;
document.qs_cart.fdAction.value='sort';
document.qs_cart.submit();
}

function getWinYOffset(){
	if (window.scrollY) return window.scrollY; // Mozilla
 	if (window.pageYOffset) return window.pageYOffset; // Opera, NN4
 	if (document.documentElement && document.documentElement.scrollTop) return document.documentElement.scrollTop; // IE
 	if (document.body && document.body.scrollTop) return document.body.scrollTop;
	return 0;
}

function scrollPosition() {
	var hash = window.location.hash;
	var pos = hash.substring(5, hash.length);
	if (pos != "") {
		scrollTo(0, pos);
	}
}

window.onload = function() {
	scrollPosition();
}

</script>

<%
	DateFormat dateFormatter = new SimpleDateFormat("EEE. MMM d, yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>		
<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

<%@ include file="/quickshop/includes/i_confirm_single.jspf" %>

<% if (hasDeptId) {
        int departmentProds;
        String departmentDisplay;
        if(qsDeptId.equalsIgnoreCase("all")){
            departmentProds = quickCart.getProducts().size();
            departmentDisplay = "All Departments";
        }else{
            departmentProds = quickCart.getProducts(qsDeptId).size();
            departmentDisplay = custFirstName.toUpperCase().charAt(0) + custFirstName.substring(1,firstNameLength).toLowerCase();
            if(custFirstName.toLowerCase().lastIndexOf("s") > -1 && custFirstName.toLowerCase().lastIndexOf("s") == firstNameLastIndex){
                departmentDisplay = departmentDisplay + "' " + currentDepartment;
            }else{
                departmentDisplay = departmentDisplay + "'s " + currentDepartment;
            }
        }
%>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="bodyCopy">
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
        <tr><td colspan="2" align="right"><b>Sort by:</b>
		<SELECT NAME="sort" onChange="javascript:doSort(this.options[this.selectedIndex].value)" CLASS="text10">
                <OPTION NAME="Ack" <%="".equalsIgnoreCase(request.getParameter("sortBy"))?"selected":""%> value="name">Name</option>
                <OPTION NAME="Ack" <%="recent_order".equalsIgnoreCase(request.getParameter("sortBy"))?"selected":""%> value="recent_order">Recently Ordered</option>
                <OPTION NAME="Ack" <%="frequency".equalsIgnoreCase(request.getParameter("sortBy"))?"selected":""%>  value="frequency">Frequently Ordered</option>
        </td></tr>
        <tr><td colspan="2" class="text12"><b><span class="title18"><%=departmentDisplay%></span> (<%= departmentProds %> item<%= departmentProds > 1 ? "s" : "" %>)</b>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr><td bgcolor="#996699" height="1" colspan="2"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></td></tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td></tr>
	<tr valign="top">
	    <td><li></li></td>
	    <td><b>Enter quantities in boxes on the left</b> (if you don't want an item, leave it blank).<br></td>
	</tr>
	<tr valign="top">
	    <td><li></li></td>
	    <td><b>Add the selected items to your cart</b> by clicking the orange button.<br></td>
	</tr>
	</table>
	<%  
		String qsPage = "every_item.jsp";
		final boolean isStandingOrderPage = false;
	%>
	<%@ include file="/shared/quickshop/includes/i_vieworder.jspf"%>
	
<% } else { %><br>
	<font class="title18"><b>Shop from everything you've ever ordered!</b><br><font class="space4pix"><br></font>
	<font class="text12bold" color="#FF9933">CHOOSE DEPARTMENT:</font><br><font class="space8pix"><br></font>
	<font class="text13"><b><%= departmentNav.toString() %></b></font>
	<br><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" vspace="4">
	<br>
        <%if(FDStoreProperties.isAdServerEnabled()) {%>
            <SCRIPT LANGUAGE=JavaScript>
            <!--
            OAS_AD('QSBottom');
            //-->
            </SCRIPT>
        <%}else{%>
            <%@ include file="/includes/home/i_banners.jspf" %>
        <%}%>
	<img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" vspace="4"><br><br>
	<a href="/index.jsp" class="bodyCopy"><b>Click here to continue shopping from our homepage!</b></a><br><br><br>
<% } %>
		</tmpl:put>
	
</tmpl:insert>
</fd:QuickShopController>

