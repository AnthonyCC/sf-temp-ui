<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
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
    
        <fd:Material id='material' sapId='<%= request.getParameter("sapId") %>'>

            <fd:AttributeController erpObject="<%= material %>" userMessage="feedback" />
            
            <table width="600" cellspacing=2 cellpadding=0>
                <tr>
                    <td align=left>View/Edit Material</td>
                    <td align=right><%= feedback %></td>
                </tr>
                <tr><td colspan=2 align=left class="section_title">Material Data</td></tr>
            </table>
            
            <form action="material_view.jsp" method="post">
            <input type=hidden name=action value=save>

            <!-- material info -->
            <table>
                <tr><td class="field_title">SAP ID</td><td class="field"><%= material.getSapId() %></td></tr>
                <tr><td class="field_title">SAP Description</td><td class="field"><%= material.getDescription() %></td></tr>
                <tr><td class="field_title">Label Name</td><td><input type=text size=60 name='<%= FormElementNameHelper.getFormElementName(material, EnumAttributeName.LABEL_NAME.getName()) %>' value='<%= material.getAttribute(EnumAttributeName.LABEL_NAME) %>'></td></tr>
                <tr><td class="field_title">Base Unit</td><td class="field"><%= material.getBaseUnit() %></td></tr>
                <tr><td class="field_title">ATP Rule</td><td class="field"><%= material.getATPRule().getDisplayName() %></td></tr>
            </table>
            <br>
            
            <!-- taxable, promotional and kosher production status -->
            <table cellspacing=2 cellpadding=2>
                <tr><td class="field_title">Taxable</td><td><%= material.isTaxable()%></td><tr>
                <tr><td class="field_title">Kosher Production</td><td><%= material.isKosherProduction() %></td><tr>
                <tr><td class="field_title">Signup Promo</td><td><input type='checkbox' value='true' name='<%= FormElementNameHelper.getFormElementName(material, EnumAttributeName.CUST_PROMO.getName()) %>' <%= (true == material.getAttributeBoolean(EnumAttributeName.CUST_PROMO))?"CHECKED":"" %>></td><tr>
            </table>

            <!-- bottle deposit amount -->
            <table cellspacing=2 cellpadding=2>
                <tr><td class="field_title">Deposit amount</td><td><input size='3' name='<%= FormElementNameHelper.getFormElementName(material, EnumAttributeName.DEPOSIT_AMOUNT.getName()) %>' value='<%= material.getAttributeInt(EnumAttributeName.DEPOSIT_AMOUNT) %>'>
                (number of returnable bottles/cans in this package)
                </td><tr>
            </table>
            
			<table cellspacing=2 cellpadding=2>
                <tr><td class="field_title">Restrictions</td><td><input size='12' name='<%= FormElementNameHelper.getFormElementName(material, EnumAttributeName.RESTRICTIONS.getName()) %>' value='<%= material.getAttribute(EnumAttributeName.RESTRICTIONS) %>'>
                (comma separated list of additional delivery restriction reason codes, eg. "TKG,VAL,EAS").
                </td><tr>
            </table>
			

            <!-- sales units -->
            <table cellspacing=2 cellpadding=2>
                <tr><th>AUM</th><th>Ratio</th><th>SUM</th><th>Desc.</th><th>Web Description</th><th>Selected</th></tr>
                <logic:iterate id="salesUnit" indexId="idx" collection="<%= material.getSalesUnits() %>" type="com.freshdirect.erp.model.ErpSalesUnitModel">
                <tr>
                    <td><%= salesUnit.getAlternativeUnit() %></td>
                    <td><%= salesUnit.getNumerator() %> / <%= salesUnit.getDenominator() %></td>
                    <td><%= salesUnit.getBaseUnit() %></td>
                    <td><%= salesUnit.getDescription() %></td>
                    <td><input type=text size=40 name='<%= FormElementNameHelper.getFormElementName(salesUnit, EnumAttributeName.DESCRIPTION.getName()) %>' value='<%= salesUnit.getAttribute(EnumAttributeName.DESCRIPTION) %>'></td>
                    <td align=left><input type=radio name='selected' value='<%= FormElementNameHelper.getFormElementName(salesUnit, EnumAttributeName.SELECTED.getName()) %>' <%= (true == salesUnit.getAttributeBoolean(EnumAttributeName.SELECTED))?"CHECKED":"" %>>
                    <%
                     if (idx.intValue()==0) { %>
                        &nbsp;&nbsp;Clear Selected:<input type="radio" name="selected" value="clearSelected" onClick="clearSelected();">
                     <% } %>
                    </td>
                </tr>
                </logic:iterate>
                <tr><td colspan=5 align=center><table cellpadding=2><tr>
                    <td align=right><input type=button value=cancel></td><td align=left><input type=submit value="save changes"></td>
                </tr></td></table></tr>
            </table>
            

            
            
            </form>

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
                <table>
                    <tr><td colspan=2 align=center><a href="characteristic_view.jsp?characteristic=<%= charac.getName() %>">Edit</a></td></tr>
                    <!-- characteristic attributes -->
                    <tr><td class="field_title">Characteristic</td><td class="field"><%= charac.getName() %></td></tr>
                    <tr><td class="field_title">Description</td><td class="field"><%= charac.getAttribute(EnumAttributeName.DESCRIPTION) %></td></tr>
                    <tr><td class="field_title">Under Label</td><td class="field"><%= charac.getAttribute(EnumAttributeName.UNDER_LABEL) %></td></tr>
                    <tr><td class="field_title">Priority</td><td class="field"><%= charac.getAttribute(EnumAttributeName.PRIORITY.getName(), "") %></td></tr>
                    <tr><td class="field_title">Optional</td><td class="field"><%= charac.getAttributeBoolean(EnumAttributeName.OPTIONAL)?"yes":"no" %></td></tr>
                    <tr><td class="field_title">Display Format</td><td class="field"><%= charac.getAttribute(EnumAttributeName.DISPLAY_FORMAT) %></td></tr>
                    <tr><td colspan=2>
                    <!-- characteristic values -->
                    <table cellspacing=2 cellpadding=2>
                        <tr><th>Value</th><th>Description</th><th>Price</th><th>Web Description</th><th>Selected</th><th>Label Value</th><th>Priority</th><th>SkuCode</th></tr>
                        <%  
                            // sort characteristic values by priority
                            List cvs = new ArrayList(charac.getCharacteristicValues());
                            Collections.sort(cvs, AttributeComparator.PRIORITY);
                            // number formatter for char value prices
                            java.text.DecimalFormat dformat = new java.text.DecimalFormat("0.00");
                        %>
                        <logic:iterate id="charValue" collection="<%= cvs %>" type="com.freshdirect.erp.model.ErpCharacteristicValueModel">
                            <fd:CharValuePrice id="charValuePrice" material="<%= material %>" charValue="<%= charValue %>">
                            <tr>
                                <td><%= charValue.getName() %></td>
                                <td><%= charValue.getDescription() %></td>
                                <td><% out.print(dformat.format(charValuePrice.getPrice())); if (0.0 != charValuePrice.getPrice()) out.print(" per " + (!"".equals(charValuePrice.getPricingUnit())?charValuePrice.getPricingUnit():"each")); %></td>
                                <td><%= charValue.getAttribute(EnumAttributeName.DESCRIPTION) %></td>
                                <td align=center><%= charValue.getAttributeBoolean(EnumAttributeName.SELECTED)?"X":"" %></td>
                                <td align=center><%= charValue.getAttributeBoolean(EnumAttributeName.LABEL_VALUE)?"X":"" %></td>
                                <td align=center><% int p = charValue.getAttributeInt(EnumAttributeName.PRIORITY); if (p != 0) out.print(p); %></td>
                                <td><%= charValue.getAttribute(EnumAttributeName.SKUCODE) %></td>
                            </tr>
                             </fd:CharValuePrice>
                        </logic:iterate>
                    </table>
                    </td></tr>
                </table>
                <hr width="600" size="2" color="#000000">
            </logic:iterate>


                        <!-- material prices -->
            <table cellspacing=2 cellpadding=2>
                <tr><th>Pricing Unit</th><th>Price</th><th>Scale Quantity</th><th>Scale Unit</th><th>Zone Pricing</th></tr>
                <logic:iterate id="materialPrice" collection="<%= material.getPrices() %>" type="com.freshdirect.erp.model.ErpMaterialPriceModel">
                <tr>
                    <td><%= materialPrice.getPricingUnit() %></td>
                    <td><%= materialPrice.getPrice() %></td>
                    <td><%= materialPrice.getScaleQuantity() %></td>
                    <td><%= materialPrice.getScaleUnit() %></td>
                    <td><%= materialPrice.getSapZoneId() %></td>
                </tr>
                </logic:iterate>
            </table>



        </fd:Material>

    </tmpl:put>

</tmpl:insert>