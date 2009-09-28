<%
	/*
		passing in one var "rhShareInfo" as string (boolean)
	*/
	boolean rhShareInfo = false;
%>

<% 
	if ( request.getParameter("rhShareInfo") != null ) {
		if ( "true".equals((String)request.getParameter("rhShareInfo")) ) {
			rhShareInfo = true;
		}
	}

	//do saving

	if (rhShareInfo) {
		%>Saved as Yes<%
	}else{
		%>Saved as No<%
	}
%>