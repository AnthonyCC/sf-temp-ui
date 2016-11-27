<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body>
<%  
	String successPage = request.getParameter("successPage");

	if(successPage != null)
	{
		%>
<div class="social-login-spinner">
	<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />  
</div>
			<script language="javascript">
				window.top.location='/<%=successPage%>';
			</script>
		
		<% 
	}


%>
</body>
</html>