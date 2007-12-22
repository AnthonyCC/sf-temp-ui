<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>DOM Onloaded Test</title>
    <meta name="generator" content="TextMate http://macromates.com/">
    <meta name="author" content="Sebestyén Gábor">
    <script  type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
    <!-- Date: 2007-12-21 -->
    <script type="text/javascript">
        document.onDocumentLoaded = function() {
            document.getElementById('before-id').innerHTML = (document.getElementById('after-id') != null ? "<i>after-id</i> element recognized!" : "<i>after-id</i> element NOT recognized!");
        }
    </script>
</head>
<body><%
int N=20000;
%>  <br/>
	<b>RESULT: </b><span id="before-id" style="border: 2px solid red;">Wait here for result!</span>
	<h1>Generating <%= N %> tables...</h1>
	    <div style="height: 200px; overflow: auto; border: 2px dotted #ccc">
        <center><%
for (int k=0; k<N; ++k) {
%>
			<table border="2" cellspacing="5" cellpadding="5">
			    <tr><th>Header <%= k %></th></tr>
			    <tr><td>Data <%= k %></td></tr>
			</table>
			<br/>
<% }
%>
		</center>
	</div>
	<span id="after-id">END of SAMPLE</span>
</body>
</html>
