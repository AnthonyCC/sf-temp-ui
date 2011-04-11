<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='material search page' direct='true'/>

    <tmpl:put name='content' direct='true'>
        
        <fd:Material id="material">
                
                <table width="600"><tr>
                    <td align=left><%= material.getDescription() %></td>
                    <td align=right><a href="material_view.jsp">Back to Material</a></td>
                </tr></table>

                <!--  characteristic -->
                <fd:Characteristic material='<%= material %>' sapId='<%= request.getParameter("characteristic") %>' id='characteristic'>

                    <fd:AttributeController erpObject="<%= material %>" userMessage="feedback" />

                    <table width="600">
                        <tr>
                            <td align=left>View/Edit Characteristic</td>
                            <td align=right><%= feedback %></td>
                        </tr>
                        <tr><td colspan=2 class="section_title">Characteristic Data</td></tr>
                    </table>

                    <form action="characteristic_view.jsp" method="post">
                    <input type=hidden name=action value=save>
					<input type=hidden name=sapId value="<%= material.getSapId() %>">

                    <table>
                        <tr>
                            <td>Characteristic</td><td><%= characteristic.getName() %></td>
                        </tr>
                        <!-- characteristic attributes -->
                            <tr>
                                <td>Description</td>
                                <td><input type=text size=40 name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.DESCRIPTION.getName()) %>' value='<%= characteristic.getAttribute(EnumAttributeName.DESCRIPTION) %>'></td>
                            </tr>
                            <tr>
                                <td>Under Label</td>
                                <td><input type=text size=40 name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.UNDER_LABEL.getName()) %>' value='<%= characteristic.getAttribute(EnumAttributeName.UNDER_LABEL) %>'></td>
                            </tr>
                            <tr>
                                <td>Priority</td>
                                <td><%= characteristic.getAttribute(EnumAttributeName.PRIORITY.getName(), "") %></td>
                            </tr>
                            <tr>
                                <td>Optional</td>
                                <td>
                                    <input type=radio name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.OPTIONAL.getName()) %>' value='true' <%= characteristic.getAttributeBoolean(EnumAttributeName.OPTIONAL)?"CHECKED":"" %>> yes
                                    <input type=radio name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.OPTIONAL.getName()) %>' value='false'  <%= !characteristic.getAttributeBoolean(EnumAttributeName.OPTIONAL)?"CHECKED":"" %>> no
                                </td>
                            </tr>
                            <tr>
                                <td>Display Format</td>
                                <td>
                                    <input type=radio name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.DISPLAY_FORMAT.getName()) %>' value='dropdown' <%= ("dropdown".equalsIgnoreCase(characteristic.getAttribute(EnumAttributeName.DISPLAY_FORMAT)))?"CHECKED":"" %>> Dropdown
                                    <input type=radio name='<%= FormElementNameHelper.getFormElementName(characteristic, EnumAttributeName.DISPLAY_FORMAT.getName()) %>' value='checkbox'  <%= ("checkbox".equalsIgnoreCase(characteristic.getAttribute(EnumAttributeName.DISPLAY_FORMAT)))?"CHECKED":"" %>> Checkbox
                                </td>
                            </tr>
                        <tr><td colspan=2>
                        <!-- characteristic values -->
                        <table cellspacing=2 cellpadding=2>
                            <tr><th>Value</th><th>Description</th><th>Price</th><th>Web Description</th><th>Selected</th><th>Label Value</th><th>Priority</th><th>SkuCode</th></tr>
                            <%  
                                // sort characteristic values by priority
                                List cvs = new ArrayList(characteristic.getCharacteristicValues());
                                Collections.sort(cvs, AttributeComparator.PRIORITY);
                                boolean selected = false;
                                // number formatter for char value prices
                                java.text.DecimalFormat dformat = new java.text.DecimalFormat("0.00");
                            %>
                            <logic:iterate id="charValue" collection="<%= cvs %>" type="com.freshdirect.erp.model.ErpCharacteristicValueModel">
                                <fd:CharValuePrice id="charValuePrice" material="<%= material %>" charValue="<%= charValue %>">
                                <tr>
                                    <td><%= charValue.getName() %></td>
                                    <td><%= charValue.getDescription() %></td>
                                    <td><% out.print(dformat.format(charValuePrice.getPrice())); if (0.0 != charValuePrice.getPrice()) out.print(" per " + (!"".equals(charValuePrice.getPricingUnit())?charValuePrice.getPricingUnit():"each")); %></td>
                                    <td><input type=text size=40 name='<%= FormElementNameHelper.getFormElementName(charValue, EnumAttributeName.DESCRIPTION.getName()) %>' value='<%= charValue.getAttribute(EnumAttributeName.DESCRIPTION) %>'></td>
                                    <td align=center><input type=radio name='<%= EnumAttributeName.SELECTED.getName() %>' value='<%= FormElementNameHelper.getFormElementName(charValue, EnumAttributeName.SELECTED.getName()) %>'<%= charValue.getAttributeBoolean(EnumAttributeName.SELECTED)?" CHECKED":"" %>></td>
                                    <td align=center><input type=radio name='<%= EnumAttributeName.LABEL_VALUE.getName() %>' value='<%= FormElementNameHelper.getFormElementName(charValue, EnumAttributeName.LABEL_VALUE.getName()) %>'<%= charValue.getAttributeBoolean(EnumAttributeName.LABEL_VALUE)?" CHECKED":"" %>></td>
                                    <td align=center><select name="<%= FormElementNameHelper.getFormElementName(charValue, EnumAttributeName.PRIORITY.getName()) %>">
                                        <option>
                                        <%  if (charValue.getAttributeBoolean(EnumAttributeName.SELECTED)) selected = true;
                                            for (int i=1;i<characteristic.numberOfCharacteristicValues()+1;i++) {
                                                out.print("<option");
                                                if (i == charValue.getAttributeInt(EnumAttributeName.PRIORITY)) { out.print(" SELECTED"); selected=true; }
                                                out.println(">" + i);
                                            } %>
                                    </select></td>
                                    <td><input type=text size=12 name='<%= FormElementNameHelper.getFormElementName(charValue, EnumAttributeName.SKUCODE.getName()) %>' value='<%= charValue.getAttribute(EnumAttributeName.SKUCODE) %>'></td>
                                    </tr>
                                </fd:CharValuePrice>
                            </logic:iterate>
                            <tr><td colspan=3></td><td align=right>No default selection</td><td align=center><input type=radio name='selected' value='none'<%= (!selected)?" CHECKED":"" %>></td><td colspan=2></td></tr>
                        </table>
                        </td></tr>
                        <tr><td colspan=2 align=center><table cellpadding=2><tr>
                        <td align=right><input type=button value=cancel></td><td align=left><input type=submit value="save changes"></td>
                    </tr></td></table></tr>
                    </table>
                </fd:Characteristic>
                </form>

            </fd:Material>

    </tmpl:put>

</tmpl:insert>