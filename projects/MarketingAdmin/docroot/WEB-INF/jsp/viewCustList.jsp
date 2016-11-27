<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring1" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c1" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt1" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.mktAdmin.model.ReferralAdminModel' %>


<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
				<table>
				<% int cnt = 2; String style ="odd"; int fcnt=0;%>
				<c1:forEach var="cust" items="${custList}">       
					<tr class="<%= style %>" >
						<td><c1:out value="${cust}" /></td>						
					</tr>
					<% if (cnt % 2 == 0) {
						style="even";
					} else {
						style = "odd";
					}
					cnt++;
					fcnt++;
					%>
				</c1:forEach>
			</table>
			
			<% if(fcnt == 0) { %>
				<br/><br/><br/><br/>
				<b>No customer list uploaded to this campaign at this time.</b>
				<br/><br/><br/>
			<% } %>
		 
</tmpl:put>
</tmpl:insert>

