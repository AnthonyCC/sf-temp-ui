<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.erp.model.*" %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='batch manager' direct='true'/>
    
    <tmpl:put name='content' direct='true'>

        <fd:BatchSearch results="batches">
        
        <table width="600" cellspacing=2 cellpadding=0>
        <tr><td class="section_title">Search Results</td></tr>
        </table>
	    <!-- search results -->
	    <%  java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yy hh:mm aa");
            if (batches.size() > 0) { %>
            <%= batches.size() %> Batches found
	        <table>
	            <tr><th>Batch Number</th><th>Created</th><th>Description</th><th>Status</th><th></th></tr>
	            <logic:iterate id="batch" collection="<%= batches %>" type="com.freshdirect.erp.model.BatchModel">
	            <tr>
                    <td><%= batch.getBatchNumber() %></td>
                    <td><%= df.format(batch.getDateCreated()) %></td>
                    <td><%= batch.getDescription() %></td>
                    <td><%= batch.getStatus().getDescription() %></td>
                    <td><a href="batch_view.jsp?batchNumber=<%= batch.getBatchNumber() %>">Review</a></td>
                </tr>
	            </logic:iterate>
	        </table>
	    <% } %>

        </fd:BatchSearch>
       
    </tmpl:put>



</tmpl:insert>