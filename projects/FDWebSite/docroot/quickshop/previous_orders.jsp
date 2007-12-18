<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' />
<fd:QuickShopController id="quickCart" orderId="" action="">
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<% boolean showDetails = true; %>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); 
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>
<%@ include file="/quickshop/includes/order_nav.jspf" %>
	<tmpl:insert template='/common/template/quick_shop.jsp'>
	    <tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from Previous Orders</tmpl:put>
	    <%--tmpl:put name='banner' direct='true'><a href="/newproducts.jsp"><img src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif" width="140" height="108" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br></tmpl:put--%>
			<tmpl:put name='content' direct='true'>
			
			<div align="center">
				<br>
				<font class="title18"><b>Shop from your previous orders!</b><br><font class="space4pix"><br></font>
				<font class="text12bold" color="#FF9933">CHOOSE AN ORDER:</font><br><font class="space8pix"><br></font>
				<%= orderNav.toString() %>
				<br><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" vspace="6">
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
			</div>
			
			</tmpl:put>

	</tmpl:insert>
</fd:OrderHistoryInfo>
</fd:QuickShopController>























