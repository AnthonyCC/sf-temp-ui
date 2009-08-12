<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.attributes.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
List getSearchList(String params, int rix, String val2replace) {
	List list = new ArrayList();
	if (params != null && !"".equals(params.trim())) {
		StringTokenizer tokenizer = new StringTokenizer(params, ",\r\n\f\"\'");
		int k=0;
		while (tokenizer.hasMoreTokens()) {
			String currentToken = tokenizer.nextToken().trim();
			
			// replace current value with suggested one
			if (k==rix)
				currentToken = val2replace;
			
			if ( !"".equals(currentToken) ) {
				list.add(currentToken);
			}
			
			k++;
		}
	}
	return list;
}


// this function replaces term in 'at' position with the suggested one
String transform(HttpSession s, String suggestion, int at) {
	// get the actual list from session
	List a = getSearchList((String) s.getAttribute(SessionName.LIST_SEARCH_RAW), at, suggestion);
	
	// serialize word list back
	StringBuffer paramsBuf = new StringBuffer();
	for (Iterator it = a.iterator(); it.hasNext(); ) {
		paramsBuf.append( (String) it.next() );
		if ( it.hasNext() ) { paramsBuf.append(","); }
	}

	return paramsBuf.toString();
}

%>
<%  //
    // Get index of current search term
    //
    int idx = 0;
    if (request.getParameter("searchIndex") != null) {
        idx = Integer.parseInt( request.getParameter("searchIndex") );
    }
    int searchIndex = idx /* String.valueOf(idx) */;
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
<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<% 
List products = new ArrayList();
List recipes =  searchResults.getRecipes();
if (searchResults != null && (recipes.size() > 0 || searchResults.getProducts().size() > 0) ) { 
        String offSet = "" + (pageCount * 75);
        
        // remove items that do not have a default sku
        for (Iterator pi = searchResults.getProducts().iterator();pi.hasNext();) {
            ProductModel pm = (ProductModel)pi.next();
            if (pm.getDefaultSku()!=null) products.add(pm);
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
        searchIndex = termCounter /* ""+(termCounter) */;
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
    <form name="build_list" method="post">
<table width="100%" cellpadding="2" cellspacing="0" border="0" align="CENTER" class="order">
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
                    <td width="50%">FOUND item #<%= searchIndex+1%> of <%= searchTerms.size() %>: &nbsp; <FONT CLASS="text8blackbold"><%= criteria %></FONT> (<%= products.size() %> matches)<BR></td>
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
		<div style="width: 100%; padding: 2px; margin-left: 30%" class="order">
<%
	if (searchResults.getSpellingSuggestion() != null) { 
		String suggestion = searchResults.getSpellingSuggestion();
%>
			<div class="text15" style="line-height: 4em">
			Did you mean <a style="font-weight: bold;" href="?searchIndex=<%= searchIndex %>&search_pad=<%= transform(session, suggestion, searchIndex) %>"><%=suggestion%></a>?
			</div>
<%
	} else {
%>			Your search for <span class="text7grbold"><%= criteria %></span> produced no results.
<%
	}
%>
		</div>
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
                    <td width="50%">FOUND item #<%= searchIndex+1 %> of <%= searchTerms.size() %>: &nbsp; <FONT CLASS="text8blackbold"><%= criteria %></FONT> (<%= products.size() %> matches)<BR></td>
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
            
            <BR>
            <BR>
        </td>
    </tr>
</table>
            </form>
</div>

<div class="order_list">
    <%@ include file="/includes/cart_header.jspf"%>
</div>

</tmpl:put>

</tmpl:insert>
</fd:Search>
</fd:ParseSearchTerms>