<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<c:if test="${not empty messages}">
		<br/>
       <c:forEach var="msg" items="${messages}">            
            <c:out value="${msg}" escapeXml="false"/><br />
       </c:forEach>    
    <c:remove var="messages" scope="session"/>
</c:if>