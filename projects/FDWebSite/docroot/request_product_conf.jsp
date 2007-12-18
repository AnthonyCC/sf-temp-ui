<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>

<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>

<% boolean formSubmit = false; %>
<script language="JavaScript">
<!--
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
-->
</script>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Request a Product</tmpl:put>
		<tmpl:put name='content' direct='true'>
	<table width="520" cellpadding="0" cellspacing="0" border="0">
	<tr><td align="center" class="text12"><br><br><br><div style="font-size: 48px; font-face:Arial,Verdana,Helvetica;"><b>THANK YOU.</b></div><br><div class="text12">We will do our best to add to our selection based on your requests.<br>To continue shopping <a href="javascript:window.close();">close this window</a> or <a href="javascript:linkTo('/newproducts.jsp');" onClick="javascript:linkTo('/newproducts.jsp');javascript:window.close();">click here to see our New Products!</a><br><br><br><img src="/media_stat/images/template/newproduct/confirm_berry.jpg" width="70" height="70"></div><br><br><br><br><br></td></tr>
	</table>
	</tmpl:put>
</tmpl:insert>
