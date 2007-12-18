<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true'/>
<%
	request.setAttribute("quickshop.level","index");
	
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>	
<tmpl:insert template='/common/template/quick_shop.jsp'>
	<tmpl:put name='title' direct='true'>Quick Shop</tmpl:put>
		<tmpl:put name='content' direct='true'>
<TABLE WIDTH="535" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<tr>
	<td align="center">
	<img src="/media_stat/images/template/quickshop/quickshop_header.gif" width="535" height="75" border="0">
	</td>
</tr>
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td>
</tr>
<tr>
	<td height="1" bgcolor="#996699"><img src="/media_stat/images/layout/996699.gif" width="535" height="1" alt="" border="0"></td>
</tr>
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td>
</tr>
<tr>
	<td>

		<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
		<tr>
			<td colspan="2" align="center" class="bodyCopy">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt="" border="0"><br>
			<span class="title18"><b>We store your orders here, details<br>and all, so you can reorder fast.</b></span><br>
			<br>
			<a href="/index.jsp"><b>Click here to continue shopping from our home page!</b></a><br><br>
			<img src="/media_stat/images/template/quickshop/9managers_s.jpg" width="500" height="122" vspace="4" alt="Department Managers" border="0">
			<br><br>
			</td>
		</tr>

		<tr><td colspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		
		<tr><td colspan="2" align="center" class="text13"><b>If you've already placed an order with us log in:</b></td></tr>		
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>				
		<tr>	
			<td><img src="/media_stat/images/layout/clear.gif" width="150" height="1" alt="" border="0"></td>
			<td>
				<%@ include file="/includes/i_login_field.jspf" %>		
			</td>
		</tr>	
		<tr><td colspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		
		<tr>
		<td colspan="2" align="center">
		<table width="100%" cellpadding="2" cellspacing="0" border="0">
			<tr>
				<td width="50%" align="right" class="text13"><b>Forgot your password?</b>&nbsp;</td>
				<td class="text13"><a href="/login/forget_password<%= request.getRequestURI().indexOf("main") > -1? "_main":""%>.jsp">Click here for help</a>.</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr class="text13">
				<td align="right" class="text13"><b>New customer?</b>&nbsp;</td>
				<td class="text13"><a href="/registration/signup.jsp">Sign up now</a>.</td>
			</tr>
		</table>
			</td>
		</tr>		

		</table>
		
	</td>
</tr>

</TABLE>
<br><br>

		</tmpl:put>
</tmpl:insert>