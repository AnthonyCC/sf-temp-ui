
	<%			
		String NewlyLinkedSocialNetworkProvider = request.getParameter("NewlyLinkedSocialNetworkProvider");			
		session.setAttribute("NewlyLinkedSocialNetworkProvider", NewlyLinkedSocialNetworkProvider);		
	%>
	
	
	<script>
		window.opener.location.href='/your_account/signin_information.jsp';
		window.close();
	</script>


