<!DOCTYPE html>

<html>
<head>
	<title></title>
	
  	<%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body>
	<div id="sulCont" class="signup-style-social signin-social-account-create-success">
		<div class="form-side-social-header">Congratulations!</div>
		<div class="signin-social-forgot-pass-header-message">Your account has been created!</div>
		<center>
			<button onclick="close_window()" class="social-login-green-button">Begin Shopping</button>
		</center>
	</div>
	<script>
		function close_window(){
			window.top.location='/login/index.jsp';
				window.top['FreshDirect'].components.ifrPopup.close();
			};
		$jq( document ).ready(function() {
			FreshDirect.components.ifrPopup.reposition();
		});
	</script>
	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>	
</body>
</html>