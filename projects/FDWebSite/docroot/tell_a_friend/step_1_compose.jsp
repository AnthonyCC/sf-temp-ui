<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>

<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/tell_a_friend/step_1_compose.jsp";
if (request.getQueryString()!=null) {
	successPage += URLEncoder.encode( "?" + request.getQueryString() );
}
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
-->
</script>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Tell a Friend</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:SiteEmailController actionName="previewEmail" result="result" successPage="/tell_a_friend/step_2_preview.jsp">
<fd:TellAFriendGetterTag id="taf" >
	<table width="520" cellpadding="0" cellspacing="0" border="0">
		<form method="post">
		<tr>
			<td colspan="4"><img src="/media_stat/images/template/tell_a_friend/tell_friend.gif" width="132" height="17"></td>
			<td colspan="3" align="right"><img src="/media_stat/images/template/tell_a_friend/step_1_compose.gif" width="185" height="15"></td>
		</tr>
		<tr>
			<td colspan="7" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
				To tell a friend about this product, just fill out the fields below,<br>Preview your e-mail, and then send it.
				<div align="right" class="text10">* Required Information<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></div>
			</td>
		</tr>
	
		<tr>
			<td colspan="7" bgcolor="#cccccc" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
	
		<tr>
			<td rowspan="8" bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="39" height="16"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="60" height="16"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="120" height="16"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="260" height="16"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="39" height="16"></td>
			<td rowspan="8" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
	
		<tr valign="top">
			<td>&nbsp;</td>
			<td class="text12"><b>FROM:</b></td> 
			<td>&nbsp;</td>
			<td colspan="2" class="text12"><%=taf.getCustomerFirstName()%> <%=taf.getCustomerLastName()%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><%=taf.getCustomerEmail()%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td>
			</td>
		</tr>
	
		<tr valign="top">
			<td>&nbsp;</td>
			<td class="text12"><b>TO:</b></td>
			<td class="text12" align="right"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>* Friend's Name&nbsp;&nbsp;</td>
			<td colspan="2">
				<input type="text" name="friend_name" class="text11" size="28" maxlength="30" value="<%=taf.getFriendName()%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='friend_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
			</td>
		</tr>
	
		<tr valign="top">
			<td>&nbsp;</td>
			<td colspan="2" class="text12" align="right"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>* Friend's E-mail Address&nbsp;&nbsp;</td>
			<td colspan="2"><input type="text" name="friend_email" class="text11" size="28" value="<%=taf.getFriendEmail()%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='friend_email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><span class="text9"><b>Note:</b> We will not pester your friend with 'spam.'<br>View our <a href="javascript:linkTo('/help/privacy_policy.jsp')">Privacy Policy</a>.</span></td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="4" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b>MESSAGE:</b> <span class="text9">(you may add to or edit this text)</span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="3"><textarea name="email_text" rows="5" cols="80" style="width: 440px" class="text11" maxlength="1000"><%=taf.getEmailText()%></textarea><br><br></td>
			<td>&nbsp;</td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="3" class="text11">
				<blockquote>Order online at www.FreshDirect.com and choose from <b>over 3,000 irresistibly fresh items</b>, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, <b>with 100% satisfaction guaranteed.</b></blockquote>
			</td>
			<td>&nbsp;</td>
		</tr>
	
		<tr>
			<td colspan="5" bgcolor="#cccccc" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>

		<tr>
			<td colspan="7" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br><input type="image" name="preview_email" src="/media_stat/images/template/tell_a_friend/preview_email.gif" width="117" height="18" border="0" alt="Preview E-mail"></td>
		</tr>

		</form>
	</table><br><br>
</fd:TellAFriendGetterTag>
</fd:SiteEmailController>

	</tmpl:put>
</tmpl:insert>
