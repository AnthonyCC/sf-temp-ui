<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<body>
	
	<%			
		String socialnetwork = request.getParameter("socialnetwork");								
	%>
	
	<br/><br/>
	
	<h4 align="center">
		You have an existing FD account.<br>
		It is now linked to your <%=socialnetwork%> account.
	</h4>
	
	<br/><br/>
	
	<center>
		<button onclick="close_window()" style="width: 200px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center; border-radius: 5px; margin-left: 20px;">
			Begin Shopping
		</button>
	</center>
	
	
	<script>
		function close_window()
		{
			window.top.location='/login/index.jsp';
			window.top['FreshDirect'].components.ifrPopup.close();
		} 
	</script>

</body>
</html>


