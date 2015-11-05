
<%@ taglib uri="freshdirect" prefix="fd" %>



<html>
<head>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>

<body>
	<center>
		<div id="sulCont" class="signup-style-social signin-social-login-social-account-already-connected">
			<%		    
				String alreadyConnectedSocialAccount = (String)session.getAttribute("AlreadyConnectedSocialAccount");				
				if(alreadyConnectedSocialAccount != null){
					session.removeAttribute("AlreadyConnectedSocialAccount");
				}
				
				System.out.println(" in jsp, alreadyConnectedSocialAccount = " + alreadyConnectedSocialAccount);
			%>
    			
    		<div class="form-side-social-header">Whoops</div>
			<div class="signin-social-forgot-pass-header-message">
				Looks like this social account is already linked to
				</br><bold><%= alreadyConnectedSocialAccount %></bold>						
			</div>
			<button class="social-login-green-button" onclick="window.close();">
				Continue
			</button>
		</div>
	</center>
	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>

</html>

