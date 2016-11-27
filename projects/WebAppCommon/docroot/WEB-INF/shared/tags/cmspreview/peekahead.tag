<%@ tag body-content="tagdependent" description="Greetings Tag" %>

<%@ tag import="com.freshdirect.fdstore.content.CMSComponentType"%>
<%@ tag import="com.freshdirect.fdstore.content.CMSSectionModel"%>
<%@ attribute name="section" type="com.freshdirect.fdstore.content.CMSSectionModel" required="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<div class="modulePeekAhead">
<c:forEach var="component" items="${section.components}">
<c:if test="${component.componentType eq 'BANNER'}">
	<div class="ModuleBanner">
		<img src="${component.image.path}" width="${component.image.width}" height="${component.image.height}">
		<h6 style="color:green">${component.flagText}</h6>
		<h2 style="color:green">${component.name}</h2>
	</div>
</c:if>
<c:if test="${component.componentType eq 'BUTTON'}">
	<div class="blueButton">${component.text}</div>
</c:if>
</c:forEach>
</div>