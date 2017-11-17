<%@ tag body-content="tagdependent" description="Greetings Tag" %>

<%@ tag import="com.freshdirect.storeapi.content.CMSComponentType"%>
<%@ tag import="com.freshdirect.storeapi.content.CMSSectionModel"%>
<%@ attribute name="section" type="com.freshdirect.storeapi.content.CMSSectionModel" required="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<div class="moduleGreeting">
<%-- <c:forEach var="component" items="${section.components}">
	<div>
	<c:if test="${component.componentType eq 'TEXT'}">
		${component.text}	
	</c:if>
	<c:if test="${component.componentType eq 'ANCHOR'}">
		<a href="${component.url}">${component.text}</a>
	</c:if>
	</div>	
</c:forEach> --%>
<div>
	
		${section.captionText}	
		<br>
		${section.headlineText}
		<br>
		${section.bodyText}	
		<br>
		<a href="${section.linkURL}">${section.linkText}</a>
	
	</div>	
</div>