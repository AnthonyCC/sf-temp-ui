<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="config.css" />
	<title>Relevancy Configuration</title>
</head>
<%@ page
	import='
	com.freshdirect.cms.fdstore.*,
	com.freshdirect.framework.webapp.*,
	com.freshdirect.smartstore.service.SearchScoringRegistry'%>
<body>
<%
	SearchScoringRegistry sr = SearchScoringRegistry.getInstance();
	String userRelevancy = request.getParameter("userRelevancy");
	boolean userRelChanged = false;
	if (userRelevancy != null && userRelevancy.trim().length() > 0 && !userRelevancy.trim().equals(sr.getUserScoringAlgorithm().toString().trim())) {
		sr.setUserScoringAlgorithm(userRelevancy);
		userRelChanged = true;
	}

	String globalRelevancy = request.getParameter("globalRelevancy");
	boolean globalRelChanged = false;
	if (globalRelevancy != null && globalRelevancy.trim().length() > 0 && !globalRelevancy.trim().equals(sr.getGlobalScoringAlgorithm().toString().trim())) {
		sr.setGlobalScoringAlgorithm(globalRelevancy);
		globalRelChanged = true;
	}

	if (globalRelChanged || userRelChanged || request.getParameter("reloadFactors") != null) {
	    sr.load();
	}
%>
<div class="high">
	<form method="get">
		<ul>
			<li>
				<label for="userRelevancy">User relevancy:</label> 
				<input type="text" id="userRelevancy" name="userRelevancy"
					value="<%= sr.getUserScoringAlgorithm().toString()%>"></input><% if (userRelChanged) {  %><span style="font-color:red;">CHANGED!</span><% }  %>
			</li>
			<li>
				<label for="globalRelevancy">Global relevancy:</label> 
				<input type="text" id="globalRelevancy" name="globalRelevancy"
					value="<%= sr.getGlobalScoringAlgorithm().toString()%>"></input><% if (globalRelChanged) {  %><span style="font-color:red;">CHANGED!</span><% }  %>
			</li>
			<li>
				<input type="submit" value="Change!"> <a href="?reloadFactors=1">reload factors</a>
			</li>
		</ul>
	</form>
</div>

</body>
</html>
