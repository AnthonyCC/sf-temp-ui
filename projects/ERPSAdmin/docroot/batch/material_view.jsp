<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='material in batch page' direct='true'/>

    <tmpl:put name='content' direct='true'>

        <fd:Batch id="batch"> 
    
        <fd:Material id='material' sapId='<%= request.getParameter("sapId") %>'>
            
            <table width="600" cellspacing=2 cellpadding=0>
                <tr>
                    <td align=left>View Material in Batch <%= batch.getBatchNumber() %></td>
                    <td align=right><a href="batch_view.jsp">Back to Batch <%= batch.getBatchNumber() %></a></td>
                </tr>
                <tr><td colspan=2 align=left class="section_title">Material Data</td></tr>
            </table>
            <!-- material info -->
            <table>
                <tr><td class="field_title">SAP ID</td><td class="field"><%= material.getSapId() %></td></tr>
                <tr><td class="field_title">SAP Description</td><td class="field"><%= material.getDescription() %></td></tr>
                <tr><td class="field_title">Base Unit</td><td class="field"><%= material.getBaseUnit() %></td></tr>
                <tr><td class="field_title">ATP Rule</td><td class="field"><%= material.getATPRule().getDisplayName() %></td></tr>
            </table>
            <br>
            
            <!-- material prices -->
            <table cellspacing=2 cellpadding=2>
                <tr><th>Pricing Unit</th><th>Price</th><th>Scale Quantity</th><th>Scale Unit</th></tr>
                <logic:iterate id="materialPrice" collection="<%= material.getPrices() %>" type="com.freshdirect.erp.model.ErpMaterialPriceModel">
                <tr>
                    <td><%= materialPrice.getPricingUnit() %></td>
                    <td><%= materialPrice.getPrice() %></td>
                    <td><%= materialPrice.getScaleQuantity() %></td>
                    <td><%= materialPrice.getScaleUnit() %></td>
                </tr>
                </logic:iterate>
            </table>

            <!-- sales units -->
            <table cellspacing=2 cellpadding=2>
                <tr><th>AUM</th><th>Ratio</th><th>SUM</th><th>SAP Description</th><th>Web Description</th><th>Selected</th></tr>
                <logic:iterate id="salesUnit" collection="<%= material.getSalesUnits() %>" type="com.freshdirect.erp.model.ErpSalesUnitModel">
                <tr>
                    <td><%= salesUnit.getAlternativeUnit() %></td>
                    <td><%= salesUnit.getNumerator() %> / <%= salesUnit.getDenominator() %></td>
                    <td><%= salesUnit.getBaseUnit() %></td>
                    <td><%= salesUnit.getDescription() %></td>
                    <td><%= salesUnit.getAttributes().getDescription() %></td>
                    <td align=center><%= (salesUnit.getAttributes().isSelected())?"X":"" %></td>
                </tr>
                </logic:iterate>
            </table>

            <table width="600" cellspacing=2 cellpadding=0>
                <tr><td align="left" class="section_title">Characteristic Data</td></tr>
            </table>
            <table>
                <tr><td class="field_title">Sales Unit Characteristic</td><td class="field"><%= material.getSalesUnitCharacteristic() %></td></tr>
                <tr><td class="field_title">Quantity Characteristic</td><td class="field"><%= material.getQuantityCharacteristic() %></td></tr>
            </table>
			<hr width="600" size="2" color="#000000">

            <!--  characteristics -->
            <%  
                // sort characteristic values by priority
                List chs = new ArrayList(material.getCharacteristics());
                Collections.sort(chs, AttributeComparator.PRIORITY);
            %>
            <logic:iterate id="charac" collection="<%= chs %>" type="com.freshdirect.erp.model.ErpCharacteristicModel">
            <% ErpsAttributes ea = charac.getAttributes(); %>
                <table>
                    <!-- characteristic attributes -->
                    <tr><td class="field_title">Characteristic</td><td class="field"><%= charac.getName() %></td></tr>
                    <tr><td class="field_title">Description</td><td class="field"><%= ea.getDescription("") %></td></tr>
                    <tr><td class="field_title">Under Label</td><td class="field"><%= ea.getUnderLabel("") %></td></tr>
                    <tr><td class="field_title">Priority</td><td class="field"><%= ea.getPriority() %></td></tr>
                    <tr><td class="field_title">Optional</td><td class="field"><%= ea.isOptional()?"yes":"no" %></td></tr>
                    <tr><td class="field_title">Display Format</td><td class="field"><%= ea.getDisplayFormat() %></td></tr>
                    <tr><td colspan=2>
                    <!-- characteristic values -->
                    <table cellspacing=2 cellpadding=2>
                        <tr><th>Value</th><th>Description</th><th>Price</th><th>Web Description</th><th>Selected</th><th>Label Value</th><th>Priority</th></tr>
                        <%  
                            // sort characteristic values by priority
                            List cvs = new ArrayList(charac.getCharacteristicValues());
                            Collections.sort(cvs, AttributeComparator.PRIORITY);
                            // number formatter for char value prices
                            java.text.DecimalFormat dformat = new java.text.DecimalFormat("0.00");
                        %>
                        <logic:iterate id="charValue" collection="<%= cvs %>" type="com.freshdirect.erp.model.ErpCharacteristicValueModel">
                            <fd:CharValuePrice id="charValuePrice" material="<%= material %>" charValue="<%= charValue %>">
                            <% ErpsAttributes charValueAttr = charValue.getAttributes(); %>
                            <tr>
                                <td><%= charValue.getName() %></td>
                                <td><%= charValue.getDescription() %></td>
                                <td><% out.print(dformat.format(charValuePrice.getPrice())); if (0.0 != charValuePrice.getPrice()) out.print(" per " + (!"".equals(charValuePrice.getPricingUnit())?charValuePrice.getPricingUnit():"each")); %></td>
                                <td><%= charValueAttr.getDescription("") %></td>
                                <td align=center><%= charValueAttr.isSelected()?"X":"" %></td>
                                <td align=center><%= charValueAttr.isLabelValue()?"X":"" %></td>
                                <td align=center><% int p = charValueAttr.getPriority(); if (p != 0) out.print(p); %></td>
                            </tr>
                             </fd:CharValuePrice>
                        </logic:iterate>
                    </table>
                    </td></tr>
                </table>
                <hr width="600" size="2" color="#000000">
            </logic:iterate>

        </fd:Material>

        </fd:Batch>

    </tmpl:put>

</tmpl:insert>