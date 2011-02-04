<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<script type="text/javascript" language="javascript" src="/ccassets/javascript/callcenter_javascript.js"></script>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : Login /</title>
	
	<style type="text/css">
	<!--
	body {
	margin: 6px;
	margin-top: 0px;
	height: 100%;
	
	background-color: #E7E7D6;
	
	font-family:  Trebuchet MS, Arial, Verdana, sans-serif;
	color: #000000;
	font-size: 10pt;
	scrollbar-base-color: #FFFFFF; 
	scrollbar-face-color: #FFFFFF; 
	scrollbar-track-color: #FFFFFF; 
	scrollbar-arrow-color: #666666;
	scrollbar-highlight-color: #CCCCCC; 
	scrollbar-3dlight-color: #999999; 
	scrollbar-shadow-color: #666666;
	scrollbar-darkshadow-color: #666666;
	}
	
	.main_nav {
	position: relative;
	width: 60%;
	height: auto;
	left: 0;
	top: 0;
	/*background:#FFFFFF;*/
	}
	
	/* LOGIN */
	
	.login_header {
	font-size: 18pt;
	color: #000000;
	}
	
	.login_field {
	font-size: 10pt;
	color: #000000;
	}
	
	/* LOGIN */
	
	.black1px{
	height: 1px;
	color: #000000;
	}
	
	.submit {
	background-color: #000000;
	font-size: 8pt;
	color: #FFFFFF;
	border: 1px #999999 solid;
	padding: 2px;
	padding-left: 0px;
	padding-right: 0px;
	margin-left: 5px;
	margin-right: 5px;
	}
	
	.input_text{
	background-color:#FFFFFF;
	font-size: 10pt;
	color: #000000;
	font-family:  Trebuchet MS, Arial, Verdana, sans-serif;
	}

	.error {
		font-size: 10pt; font-weight: bold; color: #CC0000;
	}
	
	.error_detail {
		font-size: 9pt; font-weight: bold; color: #CC0000;
	}
	
	.copyright {
	font-size: 8pt;
	color: #555555;
	}
	.login_form_title {
	font-size: 22px;
	font-weight: bold;
	}
	.login_fresh { color: #f93; font-size: 24px;}
	.login_direct { color: #693; font-size: 24px;}
	-->
	</style>
    
    <script language="javascript">
    function iframeBreaker(){
        if(top.location != location){
            top.location.href=document.location.href;
        }
    } 
    iframeBreaker();    
    </script>
</head>
<%
if(null !=CrmSession.getSessionStatus(session)){
	CrmSession.getSessionStatus(session).clear(true);
	session.invalidate();
}

%>
<body scroll="no">
    <br /><br />
	<div align="center">
		<div class="main_nav" style="background: #FFFFFF; border-bottom: 2px #000000 solid;">
	    	<div class="login_header" style="padding: 12px; border-bottom: 1px #000000 solid;"><table align="center" cellpadding="0" cellspacing="0"><tr valign="bottom">
	    	<td class="login_form_title">	    	<b><span class="login_direct">Fresh</span><span class="login_fresh">Direct&nbsp;</span>Customer Relationship Management</b></td></tr></table></div>
		<br />
    	
			<table align="center" cellpadding="6" cellspacing="0" class="login_field">
		    	<form name="login" method="POST" action="j_security_check">
				<tr>
					<td align="right">Username:</td>
					<td>
						<input type="text" class="input_text" style="width: 150px;" tabindex="1" name="j_username" value="<%=request.getParameter("j_username")%>">
						
					</td>
				</tr>
				<tr>
					<td align="right">Password:</td>
					<td>
						<input type="password" class="input_text" style="width: 150px;" tabindex="2" name="j_password">
						
					</td>
				</tr>

				
				
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="LOGIN" class="submit" name="login" style="width: 220px;" tabindex="3">
						<br /><%--br><hr class="black1px"--%>
					</td>
				</tr>
				<script>
					document.forms[0].j_username.focus();
					document.forms[0].j_username.select();
				</script>
				</form>
				<tr></tr>
				<tr><td colspan="2" align="center"><a href="javascript:popup('<%= FDStoreProperties.getCrmForgotPasswordUrl() %>','large');" >Forgot password?</a></td></tr>
				<%--form method="POST">
				<tr>
				<input type="hidden" name="userId" value="guest">
				<input type="hidden" name="password" value="guest">
					<td colspan="2" valign="top" align="center"><input type="submit" value="ENTER AS GUEST" class="submit" name="guest_login" style="width: 200px;" tabindex="4"><br /><br /><hr class="black1px">Please log into the system or choose to enter as Guest
					</td>
				</tr>
				</form--%>
			</table><br />
		
		</div>
		<br /><span class="copyright">&copy; 2002 - 2011 FreshDirect. All Rights Reserved.</span><br /><br />
	</div>
</body>
</html>