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
<%@ taglib uri='oscache' prefix='oscache' %>

<% FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );  


//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");
request.setAttribute("listPos", "SystemMessage,CategoryNote");

%>

<fd:CheckLoginStatus />
<tmpl:insert template='/common/template/right_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - New Products</tmpl:put>
<tmpl:put name='banner' direct='true'><a href="javascript:pop('/request_product.jsp',400,585);"><img src="/media_stat/images/template/newproduct/suggestaproduct.gif" width="148" height="100" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></tmpl:put>
<tmpl:put name='banner2' direct='true'><tr>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
<td colspan="4" align="center"><img src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41"></td>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr></tmpl:put>
<tmpl:put name='content' direct='true'>

<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	final static int DAYS_1 = 7;
	final static int DAYS_2 = 14;
	final static int DAYS_3 = 21;
	final static int DEFAULT_DAYS = DAYS_2;
	final static Map DAYS = new HashMap();
	static {
		DAYS.put( "1", new Integer(DAYS_1) );
		DAYS.put( "2", new Integer(DAYS_2) );
		DAYS.put( "3", new Integer(DAYS_3) );
	}
%>
<table width="550" cellpadding="0" cellspacing="0" border="0">
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<%
Integer daysInt = (Integer)DAYS.get( request.getParameter("days") );
int days = daysInt==null ? DEFAULT_DAYS : daysInt.intValue();
%>
<oscache:cache time="3600" key='<%= "newprod/" + days %>'>
<%
try {
	boolean noNewProduct = false;
	boolean noBackStock = false;
%>
<fd:GetNewProducts id="products" days='<%=days%>'>

<tr><td>
<table cellpadding="0" cellspacing="0" border="0"><tr>
<td align="top"><img src="/media_stat/images/template/newproduct/new_products.gif" width="174" height="20" border="0"></td>
<td valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;See last: 
<%if (DAYS_1==days){%><b><%= DAYS_1 %> days</b><%}else{%><a href="/newproducts.jsp?days=1"><%= DAYS_1 %> days</a><%}%> | 
<%if (DAYS_2==days){%><b><%= DAYS_2 %> days</b><%}else{%><a href="/newproducts.jsp?days=2"><%= DAYS_2 %> days</a><%}%> | 
<%if (DAYS_3==days){%><b><%= DAYS_3 %> days</b><%}else{%><a href="/newproducts.jsp?days=3"><%= DAYS_3 %> days</a><%}%>
</td>
</tr></table>
</td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>

<tr><td>
	 <SCRIPT LANGUAGE=JavaScript>
		<!--
		OAS_AD('CategoryNote');
		//-->
	</SCRIPT>
</td></tr>

<%
String trkCode="&trk=newp";
if (products.size()!=0){
%>
<tr><td>
<%
	if (products.size()>10) { 
		// group by department
		String deptImageSuffix="_np";
	%>
	<%@ include file="/includes/layouts/newproductlist_layout.jspf" %>
	<%
	} else { %>
	<%@ include file="/includes/layouts/basic_layout.jspf" %>
	<%
	}
%>
</td></tr>

<%
} else noNewProduct = true;
%>
</fd:GetNewProducts>

<fd:GetReintroducedProducts id="products" days='<%=days%>'>
<%
String trkCode="&trk=back";
if (products.size()!=0){
%>
<% if (noNewProduct) {%>
<tr><td><br><b>No products within last <%=days%> days.</b><br></td></tr>
<% } %>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<tr><td>
<A NAME="back"></A>
<table cellpadding="0" cellspacing="0" border="0">
<tr><td><img src="/media_stat/images/template/newproduct/back_in_season.gif" width="271" height="25" border="0"></td>
<td valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;See last: 
<%if (DAYS_1==days){%><b><%= DAYS_1 %> days</b><%}else{%><a href="/newproducts.jsp?days=1#back"><%= DAYS_1 %> days</a><%}%> | 
<%if (DAYS_2==days){%><b><%= DAYS_2 %> days</b><%}else{%><a href="/newproducts.jsp?days=2#back"><%= DAYS_2 %> days</a><%}%> | 
<%if (DAYS_3==days){%><b><%= DAYS_3 %> days</b><%}else{%><a href="/newproducts.jsp?days=3#back"><%= DAYS_3 %> days</a><%}%>
</td></tr></table>
</td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>

<tr><td>
<%	if (products.size()>10) { 
		// group by department
		String deptImageSuffix="_cart";
%>
	<%@ include file="/includes/layouts/newproductlist_layout.jspf" %>
<%	} else { %>
	<%@ include file="/includes/layouts/basic_layout.jspf" %>
<%
	}
%>
</td></tr>
<%
} else { noBackStock = true; 
	
	if (!noNewProduct) {
%>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
<tr><td>
<A NAME="back"></A>
<table cellpadding="0" cellspacing="0" border="0">
<tr><td><img src="/media_stat/images/template/newproduct/back_in_season.gif" width="271" height="25" border="0"></td>
<td valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;See last: 
<%if (DAYS_1==days){%><b><%= DAYS_1 %> days</b><%}else{%><a href="/newproducts.jsp?days=1#back"><%= DAYS_1 %> days</a><%}%> | 
<%if (DAYS_2==days){%><b><%= DAYS_2 %> days</b><%}else{%><a href="/newproducts.jsp?days=2#back"><%= DAYS_2 %> days</a><%}%> | 
<%if (DAYS_3==days){%><b><%= DAYS_3 %> days</b><%}else{%><a href="/newproducts.jsp?days=3#back"><%= DAYS_3 %> days</a><%}%>
</td></tr></table>
</td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
<tr><td><br><b>No products within last <%=days%> days.</b></td></tr>
<% 	} 
} %>
</fd:GetReintroducedProducts>

<% if (noNewProduct && noBackStock) {%>
<tr><td>
<br><b>No products within last <%=days%> days.</b></td>
</td></tr>
<% } %>

<% } catch (Exception ex) {
		ex.printStackTrace();
%>
<oscache:usecached />
<% } %>
</oscache:cache>
</table>
</tmpl:put>

</tmpl:insert>
