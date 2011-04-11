            <fd:ErpProduct id="product" skuCode='<%= skuCode %>'>
            <%
                if ((skuCode == null) && (product != null)) skuCode = product.getSkuCode();
                if (product != null) {
            %>

                <fd:AttributeController erpObject="<%= product %>" userMessage="feedback" />
				<div id="feeback" style="color:red; font-size:12px;font-weight:bold;"><%=feedback%></div>

                <table width="600" cellspacing=2 cellpadding=0>
                    <tr><th align="left" class="section_title">PRODUCT:</th></tr>
                    <tr><td><%= (product.getSkuCode() != null) ? product.getSkuCode() : "" %></td></tr>
                    <tr><td><%= product.getProxiedMaterial().getDescription() %></td></tr>
                    <% if (product.getSkuCode() == null) { %>
						<tr><td><b>There is no such product in ERPServices with skucode : <%= skuCode %></b></td></tr>
					<% } else {
                            if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("DISC"))) { %>
                        <tr><td><b>This product is discontinued</b></td></tr>
                    <%      } else if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("UNAV"))) { %>
                        <tr><td><b>This product is temporarily unavailable</b></td></tr>
                     <%     } else if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("SEAS"))) { %>
                        <tr><td><b>This product is out of season</b></td></tr>
                     <%     } else { %>
                        <tr><td><b>This product is currently available for sale</b></td></tr>
                     <%     }
                        } %>
                </table>
				
				
                <%  if (product.getSkuCode() != null) { %>
                <form action="product_view.jsp" method="post">
                <input type=hidden name=action value=save>
				<input type=hidden name="sku_code" value="<%= skuCode %>">
                <table width="600" cellspacing="2" cellpadding="0">
                    <tr><th align="left">Default Pricing Unit Description</th></tr>
                    <tr>                    
                    <td align="left"><%= product.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION) %></td></tr>                    
                </table>
                </form>
                <table width="600" cellspacing="2" cellpadding="0">
                <tr><th align="left">Zone Id</th><th align="left">Price</th></tr>
                <% 
                
                      java.util.List prices=product.getProxiedMaterial().getPrices();
                      for(int i=0;i<prices.size();i++)
                      {
                    	  ErpMaterialPriceModel model=(ErpMaterialPriceModel)prices.get(i);   
                      
                %>
                
                
                    
                    <tr>                    
                    <td align="left"><%=model.getSapId() %></td>
                    <td align="left"><%=model.getPrice() %></td></tr>                                
                <%
                      }
                 %></table>     
                                                                                     
                  <%  }   %>
                <table width="600" cellspacing=2 cellpadding=0>
                    <tr><th align="left" class="section_title" colspan=3>SAP Material</td></tr>
					<% if (product.getSkuCode() == null) { %>
						<tr><td align="center" colspan="3"><b>There is no such product in ERPServices with skucode : <%= skuCode %></b></td></tr>
					<%
						} else {
							com.freshdirect.erp.model.ErpMaterialModel material = product.getProxiedMaterial();
					%> 
						<tr><td><%= material.getDescription() %></td><td><%= material.getSapId() %></td><td><%= material.getUPC() %></td></tr>
					<% } %>
                </table>

                <%  if (product.getSkuCode() != null) { %>
                <form action="product_view.jsp" method="post">
                <input type=hidden name=action value=save>
                <table width="600" cellspacing="2" cellpadding="0">
                    <tr><th align="left" class="section_title">New / Back-in-Stock Manual Override:</th></tr>
                    <tr><td align="left">Date of becoming new <%= product.getAttribute(EnumAttributeName.NEW_PRODUCT_DATE) %>
                    </td></tr>
                    <tr><td align="left">Back-in-stock date: <%= product.getAttribute(EnumAttributeName.BACK_IN_STOCK_DATE) %>
                    </td></tr>                    
                </table>
                </form>
                <% } %>
                <br>
                
            <%  } %>
            </fd:ErpProduct>

