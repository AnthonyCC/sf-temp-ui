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

<fd:css href="/assets/css/social_login.css" />

	
  <!--[if IE]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/footer.ie.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->
  <!--[if lte IE 6]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.ie6.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->



<!-- ********************************************************* -->	
<!-- submit button disable function -->																
<script type="text/javascript" language="javascript">
	$jq(function(){
		// disable the submit button on page load
		$jq('#signupbtn').attr('disabled', true);
		// on keyup we will check every time the form is valid or not
		$jq('#lost_password').bind('change keyup', function() {
		    if($jq(this).validate().checkForm()) { // form is valid
        		$jq('#signupbtn').removeClass('button_disabled').attr('disabled', false);
    		} else { // form is invalid
        		$jq('#signupbtn').addClass('button_disabled').attr('disabled', true);
    		}
		});
	});
</script >
<!-- ********************************************************* -->	

<%!
//System Message fields
final List FIELD_NAMES = new ArrayList();
{
FIELD_NAMES.add(0, "email");
}
%>
</head>

<body>
	<fd:ForgotPasswordController results="result" successPage='/social/forgot_password_confirm.jsp' password="password">	
	<center>
	
	<% String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"}; %>
		
	<fd:ErrorHandler result='<%=result%>' field='<%=checkReminderForm%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>	
	</fd:ErrorHandler>

	<div id="sulCont" class="signup-style-social signin-social-forgot-pass">

			<div class="form-side-social-header">Forgot your password? No Problem.</div>
			<div class="signin-social-forgot-pass-header-message">
				Enter your email address and we'll send you a link to reset your password:
			</div>
			
		
		<div id="form_feilds">

			<form id="lost_password" name="lost_password" method="post" >
				
				<table border="0" cellpadding="5" cellspacing="8">
					
								
					<tr>
						<td>
							<input class="padding-input-box text11ref inputDef" type="email" maxlength="128" size="21" name="email"  id="email" placeholder="E-mail">						
							<input type="hidden" name="passStep" value="sendUrl">
							<input type="hidden" name="isSocialLogin" value="true">   	
						</td>
					</tr>
					<!-- 
					<tr>
						<td style="padding-top: 12px;">
							<input type="checkbox" id="altEmail" name="altEmail" value="true">
							<span class="input-email-send-message">Send to all emails on file.</span>
						</td>
					</tr>
					-->
					<tr>
						<td style="padding-top: 15px;">
							<a onclick="document.lost_password.submit();" href="#" class="butText" style="font-weight:bold;font-size:14px;">
								<input type="submit" id="signupbtn" maxlength="25" size="21" value="Submit"> 
							</a>
						</td>
					</tr>
				</table>
								
			</form>

			</div>

		 <script type="text/javascript" language="javascript">
		 
		 $jq.validator.addMethod("customemail", 
				 function validateEmail(email) {
			    	var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
			    	return re.test(email);
				} 
		 );
			
		 $jq('#lost_password').validate({ 	
			errorElement: "div",
			rules:{
		    	email:{
		        	required:true,
					email:true,
					customemail:true
				},          
		 	},
		 	messages:{
				email:{
					required:"Required",
					email:"Incomplete e-mail Address",
					customemail:"Incomplete e-mail Address"
				},
			},
	        onkeyup: false,
			errorPlacement: function(error, element) {
				error.insertBefore(element);
			},  
		   
		 });
		</script>
		</div>
	</center>
	</fd:ForgotPasswordController>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>