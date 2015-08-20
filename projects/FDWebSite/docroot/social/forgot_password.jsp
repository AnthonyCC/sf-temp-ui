<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>FreshDirect</title>
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
<%@ include file="/shared/template/includes/i_body_start.jspf" %>	
	
  <!--[if IE]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/footer.ie.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->
  <!--[if lte IE 6]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.ie6.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->


		

<%!
//System Message fields
final List FIELD_NAMES = new ArrayList();
{
FIELD_NAMES.add(0, "email");
}
%>
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" >
	<fd:ForgotPasswordController results="result" successPage='/social/forgot_password_confirm.jsp' password="password">	
	<center>
	
	<% String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"}; %>
		
	<fd:ErrorHandler result='<%=result%>' field='<%=checkReminderForm%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>	
	</fd:ErrorHandler>

	<div id="sulCont" class="signup-style" style="margin-top:20px;">

			<span style="font-size:12px;font-weight:bold;font-family: Verdana, Arial, sans-serif;margin-right:170px">Forgot your password? No Problem.</span>
			<br><br>
			<font class="align"><span style="font-size:12px">Enter your email address and we'll send you a link to reset password:</span></FONT>
		
		<div id="form_feilds" style="margin-top:40px;">

			<form id="lost_password" name="lost_password" method="post" >
				
				<table border="0" cellpadding="5" cellspacing="8">
					
								
					<tr><td><input class="padding-input-box text11ref inputDef" type="email" maxlength="128" size="21" name="email"  id="email" placeholder="E-mail">
					<br/>
					<input type="hidden" name="passStep" value="sendUrl">
					

					<tr><td style="valign:center"><input type="checkbox" id="altEmail" name="altEmail" value="true"><span class="text12 bodyCopySULNote">Send to all emails on file.</span></td></tr>
					<tr>
						<td style="padding-top: 10px;"><a onclick="document.lost_password.submit();" href="#" class="butText" style="font-weight:bold;font-size:14px;">
						<input type="submit" id="submit" maxlength="25" size="21" value="Submit"> </a></td>
					</tr>
				</table>
								
			</form>
			
			<!-- jquery validations start here  -->

			</div> <!-- form_fields ends here -->		

	</div>
	</center>
	</fd:ForgotPasswordController>

</body>
</html>