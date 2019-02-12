<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Place auto_renew Order</tmpl:put>
<% 
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); 
    
%>
<crm:GetCurrentAgent id="currentAgent">
<crm:GetErpCustomer id="customer" user="<%= user %>">

<tmpl:put name='content' direct='true'>
<% String returnParam = "returnPage=" + "/main/place_auto_renew_order.jsp";
%>

<br clear="all">
<table width="100%" class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td width="40%"><span class="cust_module_header_text"> 
</span></b></span></td><td width="60%"><input type="submit" class="submit" style="width: 250px;" value="Continue" onClick="javascript:document.forms['place_order'].submit();"></td></tr></table></div>


</tmpl:put>

</crm:GetErpCustomer>
</crm:GetCurrentAgent>
</tmpl:insert>