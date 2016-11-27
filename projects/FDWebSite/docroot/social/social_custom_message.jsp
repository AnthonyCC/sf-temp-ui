<!DOCTYPE html>
<html>
<body>
	
	<%			
		String socialnetwork = request.getParameter("socialnetwork");								
	%>
	
	<br/><br/>
	
	<h4 align="center">
		We are unable to connect your social account with FreshDirect. Please <a href="mailto:service@freshdirect.com">contact customer service</a> if you need further assistance.
	</h4>
	
	<br/><br/>
	
	<center>
		<button onclick="close_window()" style="width: 200px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center; border-radius: 5px; margin-left: 20px;">
			Continue
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


