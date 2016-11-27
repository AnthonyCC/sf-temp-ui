<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.mail.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>

<fd:CheckLoginStatus guestAllowed='false' />

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
<%
	TellAFriend taf = (TellAFriend)session.getAttribute(SessionName.TELL_A_FRIEND);
	String productId = "";
	String categoryId = "";
	String productName = "";
    String recipeId    = "";
	boolean isProduct  = false;

	String tafLinkParams = "";
		
	if (taf instanceof TellAFriendProduct){ 
		TellAFriendProduct tafp = (TellAFriendProduct) taf;
		
		productId = tafp.getProductId();
		categoryId = tafp.getCategoryId();
		
		productName = tafp.getProductTitle();
		
		tafLinkParams = "productId=" + productId + "&catId="+ categoryId;
		isProduct = true;
	} else if (taf instanceof TellAFriendRecipe) {
		TellAFriendRecipe tafr = (TellAFriendRecipe) taf;
		
		recipeId = tafr.getRecipeId();

		tafLinkParams = "recipeId=" + recipeId;
    }

	String successPage = "/tell_a_friend/step_3_confirmation.jsp?"+tafLinkParams;
%>

<fd:SiteEmailController actionName="sendEmail" result="result" successPage="<%=successPage%>">
<fd:SiteEmailPreviewTag id="emailContent" >
	<table width="520" cellpadding="0" cellspacing="0" border="0">
		<form method="post">
		<tr>
			<td colspan="3"><img src="/media_stat/images/template/tell_a_friend/tell_friend.gif" width="132" height="17"></td>
			<td colspan="3" align="right"><img src="/media_stat/images/template/tell_a_friend/step_2_preview.gif" width="173" height="14"></td>
		</tr>
		<tr>
			<td colspan="6" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
				Here is a preview of the email you are going to send to your friend.<br>If it looks good, just click Send E-mail. <br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
				<div align="center"><a href="/tell_a_friend/step_1_compose.jsp?<%= tafLinkParams %>"><img src="/media_stat/images/template/tell_a_friend/make_changes.gif" width="94" height="16" border="0" alt="Make Changes"></a>&nbsp;&nbsp;<input type="image" name="send_email" src="/media_stat/images/template/tell_a_friend/send_email.gif" width="80" height="15" border="0" alt="Send E-mail"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></div>
			</td>
		</tr>
	
		<tr>
			<td colspan="6" bgcolor="#cccccc" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
	
		<tr>
			<td rowspan="3" bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="11" height="12"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="248" height="12"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="248" height="12"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="11" height="12"></td>
			<td rowspan="3" bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="2" class="text11" valign="top">
				<table cellpadding="0" cellspacing="0" border="0">
					<tr valign="top">
						<td align="right">TO:&nbsp;&nbsp;</td>
						<td><%=taf.getFriendName()%> (<%=taf.getFriendEmail()%>)</td>
					</tr>
					<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
					<tr valign="top">
						<td align="right">FROM:&nbsp;&nbsp;</td>
						<td><%=taf.getCustomerFirstName()%> <%=taf.getCustomerLastName()%> (<%=taf.getCustomerEmail()%>)</td>
					</tr>
					<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
					<tr valign="top">
						<td align="right">SUBJECT:&nbsp;&nbsp;</td>
						<td>Your friend <%=taf.getCustomerFirstName()%> <%if (isProduct){%>has sent you <%=productName%>!<% } else { %>thought you'd like to know about FreshDirect<% } %></td>
					</tr>
					<tr>
						<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td>
					</tr>
					<tr>
						<td colspan="2"><br><%=emailContent%><br></td>
					</tr>
					<tr>
						<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="16"></td>
					</tr>
				</table>
			</td>
			<td>&nbsp;</td>
		</tr>
	
		<tr>
			<td colspan="4" bgcolor="#CCCCCC" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>
		<tr>
			<td colspan="6" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
				<div align="center"><a href="/tell_a_friend/step_1_compose.jsp?<%= tafLinkParams %>"><img src="/media_stat/images/template/tell_a_friend/make_changes.gif" width="94" height="16" border="0" alt="Make Changes"></a>&nbsp;&nbsp;<input type="image" name="send_email" src="/media_stat/images/template/tell_a_friend/send_email.gif" width="80" height="15" border="0" alt="Send E-mail"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"></div>
			</td>
		</tr>
		</form>
	</table>
</fd:SiteEmailPreviewTag>
</fd:SiteEmailController>

	</tmpl:put>
</tmpl:insert>
