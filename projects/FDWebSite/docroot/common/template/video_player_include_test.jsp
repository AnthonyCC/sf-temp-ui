<%@ taglib uri='freshdirect' prefix='fd' %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="en-US"/>

	<title>Youtube Google Analytics Test Page</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>

	<fd:AnalyticsInit/>
	<!-- Load the swfobject library-->
	<fd:javascript src="/assets/javascript/swfobject.js" />
	<!-- Load the tracking js -->
	<fd:javascript src="/assets/javascript/youtube-tracking.js"/>
	
	<jsp:include page="includes/youtube_video_player.jsp">
		<jsp:param name="id" value="yt1" />
		<jsp:param name="title" value="Youtube Video Test" />
		<jsp:param name="width" value="750" />
		<jsp:param name="height" value="350" />
		<jsp:param name="container" value="video1-container" />
	</jsp:include>

		
	<jsp:include page="includes/youtube_video_player.jsp">
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