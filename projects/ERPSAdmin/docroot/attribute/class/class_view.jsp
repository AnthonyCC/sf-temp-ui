<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='class view page' direct='true'/>

    <tmpl:put name='leftnav' direct='true'>
        <div id="leftnav">
			<!-- SAP class tree goes here -->
			<fd:ClassTree id="classTree">
				<logic:iterate id="erpClass" collection="<%= classTree %>" type="com.freshdirect.erp.model.ErpClassModel">
					<a href="class_view.jsp?sapId=<%= erpClass.getSapId() %>"><%= erpClass.getSapId() %></a><br>
				</logic:iterate>
			</fd:ClassTree>
		</div>
    </tmpl:put>


    <tmpl:put name='content' direct='true'>

            <fd:Class id="erpClass" sapId='<%= request.getParameter("sapId") %>'>

            <form action="class_view.jsp" method="post">
            <input type=hidden name=action value=save>

                <fd:AttributeController erpObject="<%= erpClass %>" userMessage="feedback" />
                
                <table width="600">
                    <tr>
                        <td align=left>View/Edit Class</td>
                        <td align=right><%= feedback %></td>
                    </tr>
                    <tr><td colspan=2 bgcolor='#DDDDDD' align=left>Class Data</td></tr>
                </table>
                <table>
                    <tr><td>Class</td><td><%= erpClass.getSapId() %></td></tr>
                <hr>
                <!-- characteristics -->
                <table>
                    <tr><th>Characteristic</th><th>Priority</th><th></th></tr>
                    <%  
                        // sort characteristics by priority
                        List chs = new ArrayList(erpClass.getCharacteristics());
                        Collections.sort(chs, AttributeComparator.PRIORITY);
                    %>
                <logic:iterate id="charac" collection="<%= chs %>" type="com.freshdirect.erp.model.ErpCharacteristicModel">
                    <tr>
                        <td><%= charac.getName() %></td>
                        <td><select name="<%= FormElementNameHelper.getFormElementName(charac, EnumAttributeName.PRIORITY.getName()) %>">
                            <option>
                            <% for (int i=1;i<erpClass.numberOfCharacteristics()+1;i++) {
                                out.print("<option");
                                if (i == charac.getAttributeInt(EnumAttributeName.PRIORITY)) out.print(" SELECTED");
                                    out.println(">" + i);
                            } %>
                        </select></td>
                        <td><a href="characteristic_view.jsp?characteristic=<%= charac.getName() %>">View/Edit</a></td>
                    </tr>
                </logic:iterate>
                    <tr>
                        <td colspan=3 align=center>
                            <table cellpadding=2>
                                <tr><td align=right><input type=button value=cancel></td><td align=left><input type=submit value="save changes"></td></tr>
                            </table>
                        </td>
                    </tr>
                 </table>

            </form>

            <fd:MaterialSearch results="classMaterials" searchtype="CLASS" searchterm="<%= erpClass.getSapId() %>">
                <table width="600" cellspacing=2 cellpadding=0>
                <tr><td class="section_title">Materials in Class</td></tr>
                </table>
                <!-- search results -->
                    <%= classMaterials.size() %> Materials found
                    <table>
                        <tr><th>SAP ID</th><th>Description</th></tr>
                    <logic:iterate id="material" collection="<%= classMaterials %>" type="com.freshdirect.erp.model.ErpMaterialInfoModel">
                        <tr><td><%= material.getSapId() %></a></td><td><%= material.getDescription() %></td></tr>
                    </logic:iterate>
                    </table>
            </fd:MaterialSearch>

            </fd:Class>
        
    </tmpl:put>




</tmpl:insert>