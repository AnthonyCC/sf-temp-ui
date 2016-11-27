
<%@ taglib uri="freshdirect" prefix="fd" %>


<html>

<head>
	<fd:css href="/assets/css/social_login.css" />	
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" >

	<center>
	<div class="container" style="width:222px; margin-left :30px;margin-top:25px; margin-bottom: 30;">

			<%			
				String socialnetwork = request.getParameter("socialnetwork");								
			%>


			<div class="signup-style" style="height:150px;"> 
					
					<span style="font-size:13px;font-family:Verdana, Arial, sans-serif;margin-top:20px;margin-right:30px;margin-left:50px;float:left;text-align:left;"> 						
							<b>
								You have an existing FD account.  ---1<br>
								It is now linked to your <%=socialnetwork%> account.							
							</b>								
													
					</span>	
					<br>

						<p style="font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-left:45px;margin-right:30px;text-align:left;">
						You have an existing FD account.  ---2<br>
						It is now linked to your <%=socialnetwork%> account.
						
						
						</p>
											
					<div>				
							
							<form>
						
								<table border="0" cellpadding="5" cellspacing="8">												
									<tr>
										<td style="padding-top: 10px;">														
	
											<a onclick="window.top.location='/login/index.jsp';" href="#" class="butText" style="font-weight:bold;font-size:14px;">
												<input type="socialButton" id="continuebtn" width="120" value="Continue"> 
											</a>																																		
	
										</td>
									</tr>					
								</table>								
							</form>
											
					</div>						
					

																		
			</div>
	
	</div> <!-- container ends here -->	
	</center>	

</body>
</html>
