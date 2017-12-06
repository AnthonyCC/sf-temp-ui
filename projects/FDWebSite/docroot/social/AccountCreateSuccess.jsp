<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html>

<html lang="en-US" xml:lang="en-US">
<head>
	<%-- <title>FreshDirect</title> --%>
	<fd:SEOMetaTag title="FreshDirect"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
  	<%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  	<% request.getSession(false).removeAttribute("signupSuccess"); %>
</head>
<body>
	<div id="sulCont" class="signup-style-social signin-social-account-create-success">
		<div class="form-side-social-header">Congratulations!</div>
		<div class="signin-social-forgot-pass-header-message">Your account has been created!</div>
		<center>
			<button onclick="close_window_new_account()" class="social-login-green-button" id="social-login-green-button_begin-shopping">Begin Shopping</button>
		</center>
	</div>
	<script>
	function close_window(){
		window.top.location='/login/index.jsp';
		window.top['FreshDirect'].components.ifrPopup.close();
	}
	$jq( document ).ready(function() {
		FreshDirect.components.ifrPopup.reposition();
	});
	</script>
	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>	
</body>
</html>