<%@ taglib uri='template' prefix='tmpl' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><tmpl:get name='title'/></title>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" SCROLL="no">

    <%-- basic template with header on top and store tree in the leftnav --%>
    
    <DIV STYLE="position:relative;width:100%;left:0;top:0">
        <jsp:include page='/includes/header.jsp'/>
    </DIV>
    
    <DIV STYLE="position:relative;left:0%;width:15%;float:left;height:auto;overflow-y:scroll">
        <jsp:include page='/includes/storeTree.jsp'/>
    </DIV>
    
    <DIV STYLE="position:relative;width:100%;height:auto;overflow-y:scroll">
        <tmpl:get name='content'/>
    </DIV>
    
</BODY>
</HTML>