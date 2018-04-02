<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='product search page' direct='true'/>
    
    <tmpl:put name='content' direct='true'>

        <fd:ProductSearch results='searchResults' searchtype='<%= request.getParameter("searchtype") %>' searchterm='<%= request.getParameter("searchterm") %>'>
            <form action="product_search.jsp" method="post">
                <table width="600" cellspacing=2 cellpadding=0>
                	<tr><td align="left" class="section_title">Search Products</td></tr>
                	<tr><td>
                		<input name=searchterm type=text size=30 value="<%= searchterm %>">
                		<input type=submit value="FIND">
                	</td></tr>
                    <tr><td>
                            <input type=radio name=searchtype value="SAPID" <%= ("SAPID".equals(searchtype)||"".equals(searchtype))?"CHECKED":"" %>> SAP ID
                            <input type=radio name=searchtype value="WEBID" <%= "WEBID".equals(searchtype)?"CHECKED":"" %>> Web ID
                            <input type=radio name=searchtype value="UPC"   <%= "UPC".equals(searchtype)?"CHECKED":"" %>> UPC
                            <input type=radio name=searchtype value="DESCR" <%= "DESCR".equals(searchtype)?"CHECKED":"" %>> SAP Description
                    </td></tr>
                </table>
            </form>
        
        	<table width="600" cellspacing=2 cellpadding=0>
        	<tr><td class="section_title">Search Results</td></tr>
        	</table>
	        <!-- search results -->
	        <% if (searchResults.size() > 0) { 
                System.out.println("Found " + searchResults.size() + " products");
            %>
	            <%= searchResults.size() %> Products found
	            <table>
	                <tr><th>SKU</th><th>Description</th></tr>
	            <logic:iterate id="product" collection="<%= searchResults %>" type="com.freshdirect.erp.model.ErpProductInfoModel">
	                <tr><td><a href="product_view.jsp?skuCode=<%= product.getSkuCode() %>"><%= product.getSkuCode() %></a></td><td><%= product.getDescription() %></td></tr>
	            </logic:iterate>
	            </table>
	        <% } %>
        </fd:ProductSearch>
        
    </tmpl:put>



</tmpl:insert>