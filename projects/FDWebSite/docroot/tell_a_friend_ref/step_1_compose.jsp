<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import=" com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.fdstore.referral.ReferralProgram"%>
<%@ page import="com.freshdirect.fdstore.referral.ReferralCampaign"%>

<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/tell_a_friend_ref/step_1_compose.jsp";
if (request.getQueryString()!=null) {
	successPage += URLEncoder.encode( "?" + request.getQueryString() );
}
String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");

ReferralProgram referralProgram = FDReferralManager.loadLastestActiveReferralProgram();
String campaignCode = (referralProgram != null && referralProgram.getCampaign() != null) ? referralProgram.getCampaign().getName().toLowerCase() : "REF_PROG";
String refProgramInclude = "/media/editorial/tell_a_friend/" +  campaignCode +".html";
String refProgramFooterInclude = "/media/editorial/tell_a_friend/ref_legal_1.html";
%>
<script language="JavaScript">



<!--

function textCounter(field, maxlimit) {
if (field.value.length > maxlimit) // if too long...trim it!
{
  field.value = field.value.substring(0, maxlimit);
}

}

function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
-->
</script>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Tell a Friend</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:SiteEmailController actionName="sendEmailNoPreview" result="result" successPage="/tell_a_friend_ref/step_2_confirmation.jsp">
<fd:TellAFriendGetterTag id="taf" >
	<table width="520" cellpadding="0" cellspacing="0" border="0">
		<form method="post">
		<fd:IncludeMedia name="<%=refProgramInclude%>" />
			
            
        <tr valign="top">
			<td>&nbsp;</td>
			<td class="text12" colspan="4"><fd:ErrorHandler result='<%=result%>' name='SYSTEM_ERROR' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></b></td> 						
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
			<td colspan="2"><input type="text" name="friend_email" class="text11" size="28" value="<%=taf.getFriendEmail()%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='friend_email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
			<fd:IncludeMedia name="/media/editorial/tell_a_friend/submit_note.html" />
			</td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="4" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b>MESSAGE:</b> <span class="text9">(you may add to or edit this text)</span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
		</tr>
	
		<tr>
			<td>&nbsp;</td>
			<td colspan="3"><textarea name="email_text" rows="5" cols="80" style="width: 440px" class="text11"  onKeyDown="textCounter(this.form.email_text,999);" onKeyUp="textCounter(this.form.email_text,999);"   ><%=taf.getEmailText()%></textarea><fd:ErrorHandler result='<%=result%>' name='email_text' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br><br></td>
			<td>&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="5" bgcolor="#cccccc" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		</tr>

		<tr>
			<td colspan="7" align="left"><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br><input type="image" name="send_email" src="/media_stat/images/template/tell_a_friend/send_email.gif" width="80" height="15" border="0" alt="Send E-mail"></td>
		</tr>

		</form>
	</table><br><br>
		<fd:IncludeMedia name="<%=refProgramFooterInclude%>" />
</fd:TellAFriendGetterTag>
</fd:SiteEmailController>

	</tmpl:put>
</tmpl:insert>