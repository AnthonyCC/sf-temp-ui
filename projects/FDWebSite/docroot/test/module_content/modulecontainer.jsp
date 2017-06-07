<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %><%

String moduleContainerId = "ModuleContainer:"+request.getParameter("moduleContainerId");

%><fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />
<fd:CheckDraftContextTag/>


<html>
	<head>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>		
</head>

<body>
	<%if (request.getParameter("moduleContainerId") == null){%>
	Module container Id was not provided.
	<%}
	else{%>
		<potato:modulehandling name="moduleContainerPotato" moduleContainerId="<%=moduleContainerId%>" />
		
		<c:if test="${empty moduleContainerPotato.config}">
			Module container was not found with the following Id:  <%=request.getParameter("moduleContainerId") %>
		</c:if>
		<c:if test="${not empty moduleContainerPotato.config}">
			<div style="width:970px;margin: auto;">
				<soy:render template="common.contentModules" data="${moduleContainerPotato}" />
			</div>
		</c:if>
	
	
	
	<%} %>
	
<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.moduleContainer = window.FreshDirect.moduleContainer || {};
    
    window.FreshDirect.user = window.FreshDirect.user || {};
    window.FreshDirect.user.isZipPopupUsed = true;

    window.FreshDirect.moduleContainer.data = <fd:ToJSON object="${moduleContainerPotato}" noHeaders="true"/>
    console.log(window.FreshDirect.moduleContainer.data);
    </script>
    <jwr:script src="/fdlibs.js" useRandomParam="false" />
        <script>
      var $jq = FreshDirect.libs.$;
    </script>
    <soy:import packageName="common"/>
     <jwr:script src="/fdmodules.js"  useRandomParam="false" />
    <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
    <jwr:script src="/fdmisc.js" useRandomParam="false" />

   

   
    
    
  </body>
</html>
