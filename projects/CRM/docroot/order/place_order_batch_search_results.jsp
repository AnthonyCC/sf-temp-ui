<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.attributes.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%  //
    // Get index of current search term
    //
    int idx = 0;
    if (request.getParameter("searchIndex") != null) {
        idx = Integer.parseInt( request.getParameter("searchIndex") );
    }
    String searchIndex = String.valueOf(idx);
    String rawList = request.getParameter("search_pad");
    //
    // Get subset of results to display
    //
    int pageCount = 0;
    if (request.getParameter("page") != null) {
        pageCount = Integer.parseInt( request.getParameter("page") );
    }
%>

<fd:ParseSearchTerms searchParams="<%= rawList %>" isBulkSearch="true" searchFor="criteria" searchList="searchTerms">
<fd:Search searchFor="<%= criteria %>" searchResults="searchResults" errorPage="/order/place_order_build.jsp">

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Search Results</tmpl:put>

<%! SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<% 
List products = new ArrayList();
List recipes =  searchResults.getRecipes();
if (searchResults != null && (recipes.size() >0 || searchResults.getExactProducts().size() > 0 || searchResults.getFuzzyProducts().size() > 0) ) { 
        String offSet = "" + (pageCount * 75);
        
        // remove items that do not have a default sku
        for (Iterator pi = searchResults.getExactProducts().iterator();pi.hasNext();) {
            ProductModel pm = (ProductModel)pi.next();
            if (pm.getDefaultSku()!=null) products.add(pm);
        }
        for (Iterator pi = searchResults.getFuzzyProducts().iterator();pi.hasNext();) {
            ProductModel pm = (ProductModel)pi.next();
            if (pm.getDefaultSku()!=null)   products.add(pm);
        }
}

if (searchTerms.size() > 0) { %>
    <%-- =============== BEGIN BATCH SEARCH ITEMS SECTION ============ --%>
            <table width="100%" cellpadding="2" cellspacing="0" border="0">
                <tr valign="top">
                    <td>
<%  int termCounter = 0; %>
                        <logic:iterate id="term" collection="<%= searchTerms %>" type="java.lang.String">
<%  if ( term.equalsIgnoreCase(criteria) ) { 
        searchIndex=""+(termCounter);
%>
                        &nbsp;<b><%= term %></b>&nbsp;
<%  } else {  %>
                        &nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>"><%= term %></a>&nbsp;
<%  } 
    termCounter++; %>
                        </logic:iterate>
                    </td>
                </tr>
            </table>
            <%-- =============== END BATCH SEARCH ITEMS SECTION ============ --%>
<% } %>
<table width="100%" cellpadding="2" cellspacing="0" border="0" align="CENTER" class="order">
    <form name="build_list" method="post">
    <tr valign="top">
        <td width="60%">
            <%-- ~~~~~~~~~~~~~ BEGIN RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
            <FONT CLASS="space4pix"><BR></FONT>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr><td width="100%" BGCOLOR="#CCCCCC"></td>
                </tr>
            </table>
            <FONT CLASS="space4pix"><BR></FONT>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
                <tr>
                    <td width="50%">FOUND item <%= searchIndex+1%> of <%= searchTerms.size() %>: &nbsp; <FONT CLASS="text8blackbold"><%= criteria %></FONT> (<%= products.size() %> matches)<BR></td>
                    <td width="50%" align="RIGHT">
                        <B>page 
<%  for (int numPages = 0; numPages * 75 <= (recipes.size() + products.size()); numPages++) {
        if (numPages == pageCount) { %>
                        <%= (numPages+1) %> .
<%      } else { %>
                        <A HREF="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  searchIndex + "&page=" + numPages) %>"><%= (numPages+1) %></a> .
<%      }
    } %>
                        </B><BR>
                        </td>
                </tr>
            </table><FONT CLASS="space4pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT>
            <%-- ~~~~~~~~~~~~~ END RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>

            <%-- ~~~~~~~~~~~~~ BEGIN SEARCH RESULTS SECTION ~~~~~~~~~~~~~ --%>
<%  if ( (recipes.size() + products.size()) >0)  { 
        String offSet = "" + (pageCount * 75);
       
%>
        <%@ include file="/includes/i_search_results.jspf"%>
<%  } else { %>
        <table width="100%" cellpadding="2" cellspacing="0" border="0" class="order">
            <tr>
                <td width="30%"></td>
                <td width="70%"><% if ( !criteria.equalsIgnoreCase("fedele") ) { %>Your search for <span class="text7grbold"><%= criteria %></span> produced no results.<% } else { %><span class="text7grbold"><%= criteria%></span> runs the place. You can't buy him or sell him.<% } %></td>
            </tr>
        </table>
<%  } %>
            <%-- ~~~~~~~~~~~~~ END SEARCH RESULTS SECTION ~~~~~~~~~~~~~ --%>
            
            <%-- ~~~~~~~~~~~~~ BEGIN RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
            <FONT CLASS="space4pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr><td width="100%" BGCOLOR="#CCCCCC"></td>
                </tr>
            </table>
            <FONT CLASS="space4pix"><BR></FONT>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
                <tr>
                    <td width="50%">FOUND item <%=searchIndex %> of <%= searchTerms.size() %>: &nbsp; <FONT CLASS="text8blackbold"><%= criteria %></FONT> (<%= products.size() %> matches)<BR></td>
                    <td width="50%" align="RIGHT">
                        <B>page 
<%  for (int numPages = 0; numPages * 75 <=(recipes.size()+ products.size()); numPages++) {
        if (numPages == pageCount) { %>
                        <%= (numPages+1) %> .
<%      } else { %>
                        <A HREF="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  searchIndex + "&page=" + numPages) %>"><%= (numPages+1) %></a> .
<%      }
    } %>
                        </B><BR>
                        </td>
                </tr>
            </table><FONT CLASS="space4pix"><BR></FONT>
            <%-- ~~~~~~~~~~~~~ END RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
            </form>
            
            <BR>
            <BR>
        </td>
    </tr>
</table>
</div>

<div class="order_list">
    <%@ include file="/includes/cart_header.jspf"%>
</div>

</tmpl:put>

</tmpl:insert>
</fd:Search>
</fd:ParseSearchTerms>