
<%@page import="com.freshdirect.event.ImpressionEventAggregator"%>
<%@page import="com.freshdirect.event.ClickThroughEventAggregator"%>

<script type="text/javascript">

function toggle() {
	var start = document.getElementById("start");
	var timeInput = document.getElementById("time");
	if (start.checked) {
		timeInput.innerHTML = "<li>time period (sec): <input type=\"text\" name=\"secs\" value=\"300\"/></li>";
	} else {
		timeInput.innerHTML = "";
	}
}
</script>


<%

String action = request.getParameter("action");
if ("start".equals(action)) {
	int millis = 1000 * Integer.parseInt(request.getParameter("secs"));
%>
	<ul>
		<li>Impressions: <%= ImpressionEventAggregator.getInstance().startTimedFlushes(millis) %></li>
		<li>Click-throughs: <%= ClickThroughEventAggregator.getInstance().startTimedFlushes(millis) %></li>
	</ul>

<%
} else if ("stop".equals(action)) {
%>
	<ul>
		<li><%= ImpressionEventAggregator.getInstance().stopTimedFlushes() %></li>
		<li><%= ClickThroughEventAggregator.getInstance().stopTimedFlushes() %></li>
	</ul>
<%
} 
%>

<form>

	Manage timed fluses:  
	<ul>
		<li>start <input id="start" type="radio" name="action" onclick="toggle();" value="start"></li>
		<div id="time"></div>
		<li>stop <input id="stop" type="radio" name="action" onclick="toggle();" value="stop" checked></li>
		
	</ul>
	<input type="submit">

</form>
