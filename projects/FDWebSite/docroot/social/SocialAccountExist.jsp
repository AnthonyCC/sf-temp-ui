<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
   <%--  <title>FreshDirect</title> --%>
      <fd:SEOMetaTag title="FreshDirect"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body>
	<center>
		<div id="sulCont" class="signup-style-social signup-social-account-exist">
			<div class="form-side-social-header">Create Account:</div>
			<div class="signin-social-forgot-pass-header-message">An account with the referenced social network already exists.</div>
			<div class="signin-social-forgot-pass-header-message">You are now signed in.</div>
			
			<button onclick="close_window()" class="social-login-green-button">Continue</button>
			
			<script>
				function close_window(){
					window.top['FreshDirect'].components.ifrPopup.close();
				};
				$jq( document ).ready(function() {
					FreshDirect.components.ifrPopup.reposition();
				});
			</script>
		
		</div>
	</center>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>