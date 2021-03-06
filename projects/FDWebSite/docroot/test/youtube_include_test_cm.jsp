<%@ taglib uri='freshdirect' prefix='fd' %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="en-US"/>

	<title>Youtube IBM Coremetrics Test Page</title>
	<!-- Load the swfobject library-->
	<fd:javascript src="/assets/javascript/swfobject.js" />
	
	<jsp:include page="/common/template/includes/youtube_video_player_cm.jsp">
		<jsp:param name="id" value="yt1" />
		<jsp:param name="title" value="Youtube Video Test" />
		<jsp:param name="width" value="750" />
		<jsp:param name="height" value="350" />
		<jsp:param name="container" value="video1-container" />
	</jsp:include>

		
	<jsp:include page="/common/template/includes/youtube_video_player_cm.jsp">
		<jsp:param name="id" value="yt2" />
		<jsp:param name="title" value="Youtube Video Test" />
		<jsp:param name="width" value="450" />
		<jsp:param name="height" value="350" />
		<jsp:param name="container" value="video2-container" />
	</jsp:include>
	

</head>

<body>
</body>
</html>