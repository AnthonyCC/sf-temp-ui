<html>
<body bgcolor=#ffffff>

<h2>Debug pages</h2>

<ul>
<li><a href="debugger.jsp">Session debugger</a></li>
<li><a href="snoop.jsp">Snoop request</a></li>
<li>Refresh Promotion</li>
<form method="post" action="/test/debug/refresh_promotion.jsp">
    Enter Promotion Code: <input type="text" name="promoCode" value=""/>&nbsp;&nbsp;<input type="submit" name="refresh" value="Refresh"/>
</form>
<li><a href="refresh_fdstore_properties.jsp">Reload FDStore.properties</a></li>
</ul>
</body>
</html>