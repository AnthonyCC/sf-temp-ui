<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
</head>
<body>
<%try{%>
    <%-- header on top and store tree in the leftnav --%>
    
    <div class="main_nav">
        <jsp:include page="/includes/main_nav.jsp"/>
    </div>
	
	<jsp:include page='/includes/customer_header.jsp'/>
	
	<jsp:include page='/includes/case_header.jsp'/>
	
	<div class="side_nav">
		
	</div>
	
	<div>
        <tmpl:get name="content"/>
    </div>

	<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
<%}catch (Exception ex){
	ex.printStackTrace();
	throw ex;
}
%>
</body>
</html>