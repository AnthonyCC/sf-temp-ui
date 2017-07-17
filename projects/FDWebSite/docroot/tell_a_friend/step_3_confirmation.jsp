<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>

<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
	String tafLinkParams = "";
	String productId = request.getParameter("productId");
	String catId = request.getParameter("catId");
	if(productId != null && catId != null){
		tafLinkParams = "productId="+productId+"&catId="+catId;
	}
%>
<tmpl:insert template='/common/template/large_pop.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Tell a Friend"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Tell a Friend</tmpl:put>
		<tmpl:put name='content' direct='true'>
	<table width="520" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="center" class="text12"><br><br><br>
			<div style="font-size: 48px; font-face:Arial,Verdana,Helvetica;"><b>THANK YOU.</b></div>
			<div class="text15">Your e-mail has been sent<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14"><br>
			<a href="/tell_a_friend/step_1_compose.jsp?<%=tafLinkParams%>"><img src="/media_stat/images/template/tell_a_friend/tell_another.gif" width="127" height="15" border="0" alt="Tell another friend"></a><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"><br><a href="javascript:window.reallyClose();"><img src="/media_stat/images/template/tell_a_friend/close_window.gif" width="129" height="16" border="0" alt="Close this window"></a><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14"><br><img src="/media_stat/images/template/tell_a_friend/confirm_berry.jpg" alt="" width="70" height="70"></div><br><br><br><br></td></tr>
	</table>
	</tmpl:put>
</tmpl:insert>
