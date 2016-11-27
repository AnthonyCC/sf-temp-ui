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
String successPage = "/tell_a_friend/step_1_compose_recipe.jsp";
if (request.getQueryString()!=null) {
	successPage += URLEncoder.encode( "?" + request.getQueryString() );
}
String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");

String recipeId = (String) request.getParameter("recipeId");
Recipe recipe   = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);

%>
<script language="JavaScript">
<!--
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
-->
</script>

<tmpl:insert template='/common/template/minimal_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Tell a Friend</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:SiteEmailController actionName="sendEmailNoPreview" result="result" successPage='<%= "/tell_a_friend/step_2_confirmation_recipe.jsp?recipeId=" + recipeId %>'>
<fd:TellAFriendGetterTag id="taf" >
	<table width="520" cellpadding="0" cellspacing="0" border="0">
		<form method="post">
		<tr>
            <td colspan="7"><img src="/media_stat/images/template/tell_a_friend/rec_email_frnd_hdr.gif" widith="451" height="20" alt="share the love, send a recipe to a friend"/></td>
		</tr>
		<tr>
			<td colspan="7" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
				We've made it easy to share your favourite recipes with a friend. Simply fill out the form below - feel free to edit the standard message - and we'll email them all the details, including ingredient list and preparation instructions.
			</td>
		</tr>
	
        <tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan="7" bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
        <tr><td>&nbsp;</td></tr>

        <tr>
            <td class="text12" colspan="7"><b><%= recipe.getFullName() %></b></td>
        </tr>
        <tr>
            <td colspan="7"><%= recipe.getDisplayableSource() %></td>
        </tr>

        <tr><td>&nbsp;</td></tr>
	
		<tr valign="top">
			<td class="text12"><b>FROM:</b></td> 
			<td>&nbsp;</td>
			<td colspan="2" class="text12"><%=taf.getCustomerFirstName()%> <%=taf.getCustomerLastName()%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><%=taf.getCustomerEmail()%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td>
			</td>
		</tr>
	
		<tr valign="top">
			<td class="text12"><b>TO:</b></td>
			<td class="text12" align="right"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>* Friend's Name&nbsp;&nbsp;</td>
			<td colspan="2">
				<input type="text" name="friend_name" class="text11" size="28" maxlength="30" value="<%=taf.getFriendName()%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='friend_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
			</td>
		</tr>
	
		<tr valign="top">
			<td colspan="2" class="text12" align="right"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>* Friend's E-mail Address&nbsp;&nbsp;</td>
			<td colspan="2"><input type="text" name="friend_email" class="text11" size="28" value="<%=taf.getFriendEmail()%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='friend_email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><span class="text9"><b>Note:</b> We will not pester your friend with 'spam.'<br>View our <a href="javascript:linkTo('/help/privacy_policy.jsp')">Privacy Policy</a>.</span></td>
		</tr>
	
		<tr>
			<td colspan="4" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b>MESSAGE:</b> <span class="text9">(you may add to or edit this text)</span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
		</tr>
	
		<tr>
			<td colspan="3"><textarea name="email_text" rows="5" cols="80" style="width: 440px" class="text11" maxlength="1000"><%=taf.getEmailText()%></textarea><br><br></td>
		</tr>
	
		<tr>
			<td colspan="7" align="left"><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br><input type="image" name="send_email" src="/media_stat/images/template/tell_a_friend/send_email.gif" width="80" height="15" border="0" alt="Send E-mail"></td>
		</tr>

        <tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan="7" bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
        <tr><td>&nbsp;</td></tr>

		<tr>
			<td colspan="7" class="text11">
				This page is intended solely for use by FreshDirect customers to send recipes to family and friends. FreshDirect does not support spamming, and misuse of this program will be considered unacceptable use according to our Customer Agreement.
			</td>
		</tr>
	
		</form>
	</table><br><br>
</fd:TellAFriendGetterTag>
</fd:SiteEmailController>

	</tmpl:put>
</tmpl:insert>
