<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<body>
	
	<%			
		String socialnetwork = request.getParameter("socialnetwork");
		String nextPage = session.getAttribute("nextSuccesspage") != null? (String) session.getAttribute("nextSuccesspage") : "";
	%>
	
	<br/><br/>
	
	<h4 align="center">
		You have an existing FD account.<br>
		It is now linked to your <%=socialnetwork%> account.
		<input id ="next-page" type="hidden" value="<%=nextPage %>" />
	</h4>
	
	<br/><br/>
	
	<center>
		<button onclick="close_window()" style="width: 200px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center; border-radius: 5px; margin-left: 20px;">
			<%=nextPage.startsWith("/oauth/")? "Continue" : "Begin Shopping" %>
		</button>
	</center>
	
	
	<script>
		function close_window()
		{
			var nextPage = document.getElementById('next-page').value;
			window.top.location= nextPage.indexOf('/oauth/') === 0? nextPage : '/login/index.jsp';
			if (window.top['FreshDirect'] && window.top['FreshDirect'].components && window.top['FreshDirect'].components.ifrPopup)
				window.top['FreshDirect'].components.ifrPopup.close();
		} 
	</script>

</body>
</html>


