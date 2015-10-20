<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>





<html>

<head>
	<fd:css href="/assets/css/social_login.css" />	
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>   <!--     added to enable JQuery ********************************************** -->
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" style="width: 400px; height: 168px;">

	<center>
	<div class="container" id="social_login_popup_container">

				<%
					HashMap socialUser = (HashMap)session.getAttribute(SessionName.SOCIAL_USER);
					String provider = (String)socialUser.get("provider");										
				 %>
    	
    		
				<div class="signup-style" style="height:150px;"> 
						
						<span style="font-size:13px;font-family:Verdana, Arial, sans-serif;margin-top:20px;margin-right:30px;margin-left:50px;float:left;text-align:left;"> 						
							<center>
								<b>Oops!</b>								
								<div class="clear"></div>
								<div class="clear"></div>								
								Your account is not connected to <%= provider %>. <br>
								Sign in to connect your accounts.
							</center>								
						</span>	
						<br>
						
						<div>				
								<div>
								<form id="litesignup" name="litesignup" method="post" action="/social/signup_lite_social.jsp" >

									<table border="0" cellpadding="5" cellspacing="8">												
										<tr>
											<td style="padding-top: 10px;">														
													<table>
														<tr>
															<td> 	
																<a 	onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp?triedToConnect=<%= provider %>', width: 518, height: 518}) "
																	href="#" class="butText" style="font-weight:bold;font-size:14px;">
																	<input type="socialButton" id="signinbtn" value="Sign In"> 
																</a>																																		
															</td>
															<td>&nbsp;</td>
															<td>&nbsp;</td>
															<td>&nbsp;</td>
															<td>
																<a onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', width: 518, height: 518}) " href="#" class="butText" style="font-weight:bold;font-size:14px;">
																	<input type="socialButton" id="signupbtn" width="120" value="Create Account"> 
																</a>																																		
															</td>
														</tr>															
													</table>
											</td>
										</tr>					
									</table>								
								</form>
								</div> 					
						</div>																			
				</div>
	
	</div> <!-- container ends here -->	
	</center>	


	<script type="text/javascript">
		jQuery.noConflict();
		
		//window.top.alert("popup windows pop up");   === NOT Working
		//window.top.alert("popup windows pop up");
		
		jQuery(document).ready(function() {			
			
			// resize the social_login popup window to its content
			/*
		    var myElement = $('#social_login_popup_container');
		    var sWidth = myElement.width();
		    var sHeight = myElement.height();

		    if((sWidth == null) || (sHeight == null)){
		    	sWidth = 398;
		    	sHeight = 168;
		    }
		    */
		    
		    //window.resizeTo(sWidth, sHeight);
		     //$jq('#ifrPopup iframe').contentWindow.resizeTo(sWidth, sHeight);
		     
		    //$jq('#ifrPopup iframe').contentWindow.resizeTo('398', '168');   // NOT Working
		    
			//$jq('#ifrPopup iframe').contentWindow.resizeTo('width: 398', 'height: 168');   // NOT Working
			
			console.log($jq(window.top['FreshDirect'].components.ifrPopup.popup.$el[0].childNodes[1]).find('iframe:first'));
			$jq(window.top['FreshDirect'].components.ifrPopup.popup.$el[0].childNodes[1]).find('iframe:first').css({'height': 169, 'overflow-y': 'hidden'});
			
			
			//$jq('#ifrPopup iframe').css('height', 168); // NOT Working
			//$jq('#ifrPopup iframe').css('width', 399); // NOT Working
			
			//$jq('#ifrPopup iframe').width(398); // NOT Working
			//$jq('#ifrPopup iframe').height(168); // NOT Working
			
			
			//$jq('#ifrPopup iframepopup-body').width(398);
			//$jq('#ifrPopup iframepopup-body').height(168);
			
			//$jq('#ifrPopup iframepopup-body').css('width', 398);
			//$jq('#ifrPopup iframepopup-body').css('height', 168);
		});
	</script>	
	
	
</body>
</html>

