<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.erp.model.*" %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='batch manager' direct='true'/>
    
    <tmpl:put name='content' direct='true'>

        <fd:Batch id="batch" batchNumber='<%= request.getParameter("batchNumber") %>'>
        
            <table width="600" cellspacing=2 cellpadding=0>
            <tr><td class="section_title">Batch Data</td></tr>
            </table>
            <!-- batch -->
            <table>
            <tr><td>Batch</td><td><%= batch.getBatchNumber() %></td></tr>
            <tr><td>Created:</td><td><%= batch.getDateCreated() %></td></tr>
            <tr><td>Status</td><td><%= batch.getStatus().getDescription() %></td></tr>
            </table>

            <table width="600" cellspacing=2 cellpadding=0>
            <tr><td class="section_title">Materials In Batch</td></tr>
            </table>
            <!-- materials -->
            <fd:MaterialSearch results="materials" searchtype="BATCH" searchterm='<%= request.getParameter("batchNumber") %>'>
                <table>
                <logic:iterate id="material" collection="<%= materials %>" type="com.freshdirect.erp.model.ErpMaterialInfoModel">
                    <tr><td><a href="material_view.jsp?sapId=<%= material.getSapId() %>"><%= material.getSapId() %></a></td><td><%= material.getDescription() %></td></tr>
                </logic:iterate>
                </table>
            </fd:MaterialSearch>

        </fd:Batch>
	    
       
    </tmpl:put>



</tmpl:insert>