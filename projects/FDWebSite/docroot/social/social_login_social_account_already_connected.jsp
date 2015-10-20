
<%@ taglib uri="freshdirect" prefix="fd" %>



<html>

<head>
	<fd:css href="/assets/css/social_login.css" />	
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" >

	<center>
	<div class="container" style="width:222px; margin-left :30px;margin-top:25px; margin-bottom: 30;">


			<%		    
				String alreadyConnectedSocialAccount = (String)session.getAttribute("AlreadyConnectedSocialAccount");				
				if(alreadyConnectedSocialAccount != null){
					session.removeAttribute("AlreadyConnectedSocialAccount");
				}
				
				System.out.println(" in jsp, alreadyConnectedSocialAccount = " + alreadyConnectedSocialAccount);
			%>
	
	    		
			<div class="signup-style" style="height:150px;"> 
					
					<span style="font-size:13px;font-family:Verdana, Arial, sans-serif;margin-top:20px;margin-right:30px;margin-left:50px;float:left;text-align:left;"> 						
						<center>
							<b>Whoops</b>								
							<div class="clear"></div>
							<div class="clear"></div>								
							Looks like this social account is already linked to <br>
							<bold> <%= alreadyConnectedSocialAccount %> </bold>														
						</center>								
					</span>	
					<br>
					
					<div>				
							
							<form>
						
								<table border="0" cellpadding="5" cellspacing="8">												
									<tr>
										<td style="padding-top: 10px;">														
	
											<a onclick="window.close();" href="#" class="butText" style="font-weight:bold;font-size:14px;">
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

