<%@ page import="com.freshdirect.fdstore.content.YoutubeVideoModel"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<%
String videoId=(String)request.getParameter("id");
String title=(String)request.getParameter("title");
String videoWidth=(String)request.getParameter("width");
String videoHeight=(String)request.getParameter("height");
String container=(String)request.getParameter("container");
String isPopup=(String)request.getParameter("ispopup");

if(videoId == null) videoId="ytv";
if(container == null) container="video-container";
if(videoWidth == null) videoWidth="100%";
if(videoHeight == null) videoHeight="90%";

%>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" lang="en-US"/>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
	<%if(isPopup !=null) { %>
		<fd:AnalyticsInit/>
		<!-- Load the swfobject library-->
		<fd:javascript src="/assets/javascript/swfobject.js" />
		<!-- Load the tracking js -->
		<fd:javascript src="/assets/javascript/youtube-tracking.js"/>
	<%} %>
	
	<fd:YoutubeVideoTag attrName='<%= videoId %>' title='<%=title%>'/>
	
	<%
	if(pageContext.getAttribute("error")!=null){
		%>
		</head>
		<body>
			<p><b><%= pageContext.getAttribute("error")%></b></p>
		</body>
	<%
	} else {
		YoutubeVideoModel videoModel=(YoutubeVideoModel)pageContext.getAttribute(videoId);
	%>

	<script type="text/javascript"> 
	  if(VIDEOS==null){
	  	var VIDEOS = {};
	  }
	  var videoUrl = '<%=videoModel.getYouTubeVideoId()%>';
	  
	  if(videoUrl.indexOf("youtube") != -1) {
		  var firstPos = videoUrl.indexOf("=") + 1;
		  var lastPos = videoUrl.indexOf("&", firstPos) != -1 ? videoUrl.indexOf("&", firstPos) : videoUrl.indexOf("=", firstPos);
		  if(lastPos == -1){
			  lastPos == videoUrl.length;
		  }
		  videoUrl=videoUrl.substring(firstPos, lastPos);
	  }
	  
	  VIDEOS.<%= videoId %> = {id:'<%= videoId %>', 
			  				   title:'<%=videoModel.getTitle()%>', 
			  				   ytId: videoUrl, 
			  				   container:'<%=container%>', 
			  				   playStarted:false,
			  				   trackInterval: 10,
			  				   timer: -1,
			  				   trackCheckpoint: -1,
			  				   timerIsOn: 0,
			  				   viewed: 0};
	  
	  var params = { allowScriptAccess: "always" };		
	  var atts = { id: '<%= videoId %>' };
	  var videoLocn;
	  if('PL' == videoUrl.substring(0,2)){
	  	videoLocn = ["http://www.youtube.com/v/videoseries?version=3&listType=playList&list=", VIDEOS.<%= videoId %>.ytId, "&enablejsapi=1&rel=0&playerapiid=",'<%= videoId %>'].join('');		  
	  }else{
		videoLocn = ["http://www.youtube.com/v/", VIDEOS.<%= videoId %>.ytId, "?version=3&enablejsapi=1&rel=0&playerapiid=",'<%= videoId %>'].join('');
	  }
	  swfobject.embedSWF(videoLocn, VIDEOS.<%= videoId %>.container, '<%= videoWidth %>', '<%= videoHeight %>', "8", null, null, params, atts);

	</script>

  </head>

<body scrolling="no" style="margin:0;padding:0;overflow:hidden">
<div id="<%= container %>">You need Flash player 8+ and JavaScript enabled to view this video.</div>
<%if(videoModel.getContent()!=null) {%>
<fd:IncludeMedia name="<%= videoModel.getContent().getPath() %>"/>
<% } %>
</body>
<% } %>
</html>
