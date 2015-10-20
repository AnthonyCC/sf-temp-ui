
<%    	
    String forwardPage  = (String) request.getParameter("forwardPage"); 
	String callback_uri  = (String) request.getParameter("callback_uri"); 
	String service  = (String) request.getParameter("service"); 		
%>



<script type="text/javascript">			
	window.location = "<%=forwardPage%>?service=<%=service%>&callback_uri=<%=callback_uri%>";    
</script>




  






  

  
  