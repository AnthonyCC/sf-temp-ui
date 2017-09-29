<%@page contentType="text/html"%>
<%@page import="com.freshdirect.fdstore.warmup.*" %>
<html lang="en-US" xml:lang="en-US">
<head><title>Inventory Check</title></head>
<body>

<%

    String dept = request.getParameter("dept");
    if (dept != null) {

        InventoryWarmupManager.inventorySync(dept);

    }

%>


DONE!!!

</body>
</html>
