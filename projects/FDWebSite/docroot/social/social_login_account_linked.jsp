<!DOCTYPE html>
<html>
<body>
	
	<%			
		String socialnetwork = request.getParameter("socialnetwork");								
	%>
	
	<br/><br/>
	
	<h4 align="center">
		Your FD account is now linked to your <%=socialnetwork%> account.
	</h4>
	
	<br/><br/>
	
	<center>
		<button onclick="close_window()">
			Continue
		</button>
	</center>
	
	
	<script>
		function close_window()
		{
			//window.top.location='/your_account/signin_information.jsp';
			//window.parent.location='/your_account/signin_information.jsp';
			//window.parent.location.href='/your_account/signin_information.jsp';
			window.opener.location.href='/your_account/signin_information.jsp';
			window.close();
		} 
	</script>

</body>
</html>


