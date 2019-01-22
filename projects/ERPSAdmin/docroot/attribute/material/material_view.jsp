<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='material search page' direct='true'/>

    <tmpl:put name='content' direct='true'>

        <fd:MaterialSearch results="searchResults">
            <form action="material_search.jsp" method="post">
                <table width="600" cellspacing=2 cellpadding=0>
                	<tr><td align="left" class="section_title">Search Materials</td></tr>
                	<tr><td>
                		<input name=searchterm type=text size=30 value="<%= searchterm %>">
                		<input type=submit value="FIND">
		        		<a href="material_search.jsp">Return to Search Results</a><br>
                	</td></tr>
                    <tr><td>
                            <input type=radio name=searchtype value="SAPID" <%= ("SAPID".equals(searchtype)||"".equals(searchtype))?"CHECKED":"" %>> SAP ID
                            <input type=radio name=searchtype value="WEBID" <%= "WEBID".equals(searchtype)?"CHECKED":"" %>> Web ID
                            <input type=radio name=searchtype value="DESCR" <%= "DESCR".equals(searchtype)?"CHECKED":"" %>> SAP Description
                    </td></tr>
                </table>
            </form>
        </fd:MaterialSearch>
    
		<% if(!SecurityManager.isUserAdmin(request)) {%>
			<%@ include file='/attribute/material/material_view_reader.jsp' %>
		<%} else { %>
    
        

		<%} %>

    </tmpl:put>

</tmpl:insert>
