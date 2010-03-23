<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
%>
<tmpl:insert template='/common/template/right_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - New Products</tmpl:put>
<tmpl:put name='banner' direct='true'><a href="javascript:pop('/request_product.jsp',400,585);"><img src="/media_stat/images/template/newproduct/suggestaproduct.gif" width="148" height="100" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></tmpl:put>
<tmpl:put name='banner2' direct='true'><tr>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
<td colspan="4" align="center"><img src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41"></td>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr></tmpl:put>
<tmpl:put name='content' direct='true'>


<%@ include file="/includes/i_header_new.jspf" %>

<%@ include file="/includes/i_featured_new.jspf" %>


</tmpl:put>

</tmpl:insert>
