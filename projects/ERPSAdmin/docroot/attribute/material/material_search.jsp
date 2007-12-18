<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='material search page' direct='true'/>
    
    <tmpl:put name='content' direct='true'>

        <fd:MaterialSearch results='searchResults' searchtype='<%= request.getParameter("searchtype") %>' searchterm='<%= request.getParameter("searchterm") %>'>
            <form action="material_search.jsp" method="post">
                <table width="600" cellspacing=2 cellpadding=0>
                	<tr><td align="left" class="section_title">Search Materials</td></tr>
                	<tr><td>
                		<input name=searchterm type=text size=30 value="<%= searchterm %>">
                		<input type=submit value="FIND">
                	</td></tr>
                    <tr><td>
                            <input type=radio name=searchtype value="SAPID" <%= ("SAPID".equals(searchtype)||"".equals(searchtype))?"CHECKED":"" %>> SAP ID
                            <input type=radio name=searchtype value="WEBID" <%= "WEBID".equals(searchtype)?"CHECKED":"" %>> Web ID
                            <input type=radio name=searchtype value="DESCR" <%= "DESCR".equals(searchtype)?"CHECKED":"" %>> SAP Description
                    </td></tr>
                </table>
            </form>
        
        	<table width="600" cellspacing=2 cellpadding=0>
        	<tr><td class="section_title">Search Results</td></tr>
        	</table>
	        <!-- search results -->
	        <% if (searchResults.size() > 0) { %>
	            <%= searchResults.size() %> Materials found
	            <table>
	                <tr><th>SAP ID</th><th>Description</th></tr>
	            <logic:iterate id="material" collection="<%= searchResults %>" type="com.freshdirect.erp.model.ErpMaterialInfoModel">
	                <tr><td><a href="material_view.jsp?sapId=<%= material.getSapId() %>"><%= material.getSapId() %></a></td><td><%= material.getDescription() %></td></tr>
	            </logic:iterate>
	            </table>
	        <% } %>
        </fd:MaterialSearch>
        
    </tmpl:put>



</tmpl:insert>