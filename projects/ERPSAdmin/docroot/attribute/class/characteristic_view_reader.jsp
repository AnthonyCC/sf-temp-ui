<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.erp.model.*' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='characteristic view page' direct='true'/>
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

            <fd:Class id="erpClass">
                
                <table width="600"><tr>
                    <td align=left><%= erpClass.getSapId() %></td>
                    <td align=right><a href="class_view.jsp">Back to Class</a></td>
                </tr></table>
                
                <!--  characteristic -->
                <fd:Characteristic erpClass='<%= erpClass %>' sapId='<%= request.getParameter("characteristic") %>' id='characteristic'>

                    <fd:AttributeController erpObject="<%= erpClass %>" userMessage="feedback" />

                    <table width="600">
                        <tr>
                            <td align=left>View Characteristic</td>
                            <td align=right><%= feedback %></td>
                        </tr>
                        <tr><td bgcolor='#DDDDDD' colspan=2>Characteristic Data</td></tr>
                    </table>

                    <form action="characteristic_view.jsp" method="post">
                    <input type=hidden name=action value=save>

                    <table>
                        <tr>
                            <td>Characteristic</td><td><%= characteristic.getName() %></td>
                        </tr>
                        <!-- characteristic attributes -->
                            <tr>
                                <td>Description</td>
                                <td><%= characteristic.getAttribute(EnumAttributeName.DESCRIPTION) %></td>
                            </tr>
                            <tr>
                                <td>Under Label</td>
                                <td><%= characteristic.getAttribute(EnumAttributeName.UNDER_LABEL) %></td>
                            </tr>
                            <tr>
                                <td>Priority</td>
                                <td><%= characteristic.getAttribute(EnumAttributeName.PRIORITY.getName(), "") %></td>
                            </tr>
                            <tr>
                                <td>Optional</td>
                                <td>
                                    <%= (true == characteristic.getAttributeBoolean(EnumAttributeName.OPTIONAL))?"YES":"NO" %>
                                </td>
                            </tr>
                            <tr>
                                <td>Display Format</td>
                                <td>
 <%= ("dropdown".equalsIgnoreCase(characteristic.getAttribute(EnumAttributeName.DISPLAY_FORMAT)))?"DropDown":"Checkbox" %>
                                </td>
                            </tr>
                        <tr><td colspan=2>
                        <!-- characteristic values -->
                        <table cellspacing=2 cellpadding=2>
                            <tr><th>Value</th><th>Description</th><th>Web Description</th><th>Selected</th><th>Label Value</th><th>Priority</th><th>SkuCode</th></tr>
                            <%  
                                // sort characteristic values by priority
                                List cvs = new ArrayList(characteristic.getCharacteristicValues());
                                Collections.sort(cvs, AttributeComparator.PRIORITY);
                                boolean selected = false;
                                // number formatter for char value prices
                                java.text.DecimalFormat dformat = new java.text.DecimalFormat("0.00");
                            %>
                            <logic:iterate id="charValue" collection="<%= cvs %>" type="com.freshdirect.erp.model.ErpCharacteristicValueModel">
                                <tr>
                                    <td><%= charValue.getName() %></td>
                                    <td><%= charValue.getDescription() %></td>
                                    <td><%= charValue.getAttribute(EnumAttributeName.DESCRIPTION) %></td>
                                    <td align=center><%= charValue.getAttributeBoolean(EnumAttributeName.SELECTED)?" Yes":"No" %></td>
                                    <td align=center><%= charValue.getAttributeBoolean(EnumAttributeName.LABEL_VALUE)?" Yes":"No" %></td>
                                    <td align=center><%= charValue.getAttributeInt(EnumAttributeName.PRIORITY)%>
                                    </select></td>
                                    <td><%= charValue.getAttribute(EnumAttributeName.SKUCODE) %></td>
                                    </tr>
                            </logic:iterate>
                            <tr><td colspan=2></td><td align=right>No default selection</td><td align=center><%= (!selected)?" Yes":"No" %></td><td colspan=2></td></tr>
                        </table>
                        </td></tr>
                        
                    </table>
                </fd:Characteristic>
                </form>

                <fd:MaterialSearch results="classMaterials" searchtype="CLASS" searchterm="<%= erpClass.getSapId() %>">
                    <table width="600" cellspacing=2 cellpadding=0>
                    <tr><td class="section_title">Materials that share this Characteristic</td></tr>
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
