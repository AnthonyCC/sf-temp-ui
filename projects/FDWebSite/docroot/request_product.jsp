<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/request_product.jsp?"+request.getQueryString();
String department = request.getParameter("department");
if("wine".equalsIgnoreCase(department)) {
%>
<jsp:forward page='<%="/request_wine.jsp?"+request.getQueryString()%>' />
<%}

String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");


%>
<script language="JavaScript">
<!--
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
//-->
</script>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity customerIdentity = null;
ErpCustomerInfoModel customerInfo = null;
if (user!=null && user.getLevel() == 2){
customerIdentity = user.getIdentity();
customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
}
%>
<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Request a Product</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:RequestAProductTag actionName="sendEmail" result="result" successPage="request_product_conf.jsp">
	<table width="520" cellpadding="0" cellspacing="0" border="0">
	<form name="request_product" method="post">
	<tr>
	<td colspan="4" class="text12"><img src="/media_stat/images/template/newproduct/request_product.gif" width="202" height="17"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
We try to offer our customers the best selection of fresh foods as well as the most popular packaged brands. Your product requests will help us make FreshDirect a better place to shop!
<br> <br>
<b>Please describe the products you'd like in as much detail as possible including specific brands, sizes, and flavors. </b>
	</td>
	</tr>
	<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="60" height="1"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="280" height="1"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1"></td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td colspan="3" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>&nbsp;&nbsp;&nbsp;&nbsp;<fd:ErrorHandler result='<%=result%>' name='error' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
	</tr>
	<% if (user!=null && user.getLevel() == 2){%>
	<input type="hidden" name="user_email" value="<%=customerInfo.getEmail()%>">
	<input type="hidden" name="first_name" value="<%=customerInfo.getFirstName()%>">
	<input type="hidden" name="last_name" value="<%=customerInfo.getLastName()%>">
	<input type="hidden" name="department" value="<%=department%>">
	<%}%>
	<tr>
	<td></td>
	<td align="right"><b>Product #1&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
	<td align="left"><input name="product1" size="45">
	<td></td>
	</tr>
	<tr>
	<td><br></td>
	</tr>
	<tr>
	<td></td>
	<td align="right"><b>Product #2&nbsp;&nbsp;&nbsp;&nbsp;</b>
	<td align="left"><input name="product2" size="45"><br></td>
	<td></td>
	</tr>
	<tr>
	<td><br></td>
	</tr>
	<tr>
	<td></td>
	<td align="right"><b>Product #3&nbsp;&nbsp;&nbsp;&nbsp;</b>
	<td align="left"><input name="product3" size="45"><br></td>
	<td></td>
	</tr>
	<tr>
	<td><br></td>
	</tr>
	<script language="JavaScript"><!--
		document.request_product.product1.focus();
	//-->
	</script>
	<tr>
	<td></td>
	<td colspan="2" align="center"><a href="javascript:document.request_product.reset()"><img src="/media_stat/images/template/newproduct/b_clear.gif" width="47" height="17" border="0" alt="Clear"></a>&nbsp;&nbsp;<input type="image" name="send_email" src="/media_stat/images/template/newproduct/b_send.gif" width="45" height="15" vspace="1" border="0" alt="Send Request"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
</td>
	<td></td>
	</tr>
	<tr><td colspan="4" bgcolor="#999966" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
	</form>
	</table>
</fd:RequestAProductTag>
	</tmpl:put>
</tmpl:insert>
