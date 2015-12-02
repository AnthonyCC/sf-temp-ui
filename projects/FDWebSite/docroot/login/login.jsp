<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<% //expanded page dimensions
final int W_LOGIN_TOTAL = 970;
%>

<% 
String template = "/common/template/no_nav.jsp";
	//diff nav for popup login
	//if ("popup".equals( request.getParameter("type") ))
boolean isPopup = false;
String sPage = (request.getParameter("successPage")!=null)?request.getParameter("successPage").toLowerCase():null;
	if (sPage != null) {
		
	    // determine the preSuccessPage from previous workflow
	    session.setAttribute(SessionName.PREV_SUCCESS_PAGE, sPage); 		
		
		if (sPage.indexOf("type=popup") != -1){
			template = "/common/template/large_pop.jsp";
			isPopup = true;
		}else if ( sPage.indexOf("gift_card") > 0 && FDStoreProperties.isGiftCardEnabled() ) {
			template = "/common/template/giftcard.jsp";
		}else if ( sPage.indexOf("robin_hood") > 0 && FDStoreProperties.isRobinHoodEnabled() ) {
			template = "/common/template/robinhood.jsp";
		}
	}
%>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%=template%>'>
<tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put>
<tmpl:put name='content' direct='true'>
<table border="0" cellspacing="0" cellpadding="0" width="<%=W_LOGIN_TOTAL%>" align="center">
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt=""></td></tr>
        <tr>
            <td width="<%=W_LOGIN_TOTAL-200%>">
				<img src="/media_stat/images/navigation/current_cust_log_in_now.gif" width="222" height="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW"></td>
        	<td width="200" align="right"><font class="text9">* Required Information</font></td>
		</tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""></td></tr>
		<tr><td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt=""></td></tr>
		<tr><td colspan="2">
		<table border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td width="110"><img src="/media_stat/images/layout/clear.gif" width="110" height="1" border="0" alt=""><BR></td>
			<td class="text13">
				<%@ include file="/includes/i_login_field.jspf" %>
			</td>
		</tr>
		</table>
		</td>
		</tr>
</table>
</tmpl:put>
</tmpl:insert>
