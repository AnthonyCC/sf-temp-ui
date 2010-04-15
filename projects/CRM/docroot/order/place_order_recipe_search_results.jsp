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

<fd:FindRecipes id="recipes">
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>New Order > Search Results</tmpl:put>
<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<% 
List products = new ArrayList();
//List recipes =  searchResults.getRecipes();
String recipeSource = request.getParameter("recipeSource");
String recipeAuthor = request.getParameter("recipeAuthor");
String recipe = "";
StringBuffer buf = new StringBuffer();
 if (recipeSource!=null && !"".equals(recipeSource.trim()) ) {
    buf.append("Recipe Source : ").append(recipeSource);
    RecipeSource rs = (RecipeSource)ContentFactory.getInstance().getContentNode(recipeSource);
    recipe = rs.getName();
 }else if (recipeAuthor!=null && !"".equals(recipeAuthor.trim()) ) {
    buf.append("Recipe Author : ").append(recipeAuthor);
    RecipeAuthor ra = (RecipeAuthor)ContentFactory.getInstance().getContentNode(recipeAuthor);
    recipe = ra.getName();
 }

if (recipes.size() > 0 ) { 
        String offSet = "" + (pageCount * 75);

}

%>
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
                    <td width="50%">FOUND Recipes from <FONT CLASS="text8blackbold"><%= recipe %></FONT> (<%= recipes.size() %> matches)<BR></td>
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
			No matching recipes found
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
                    <td width="50%">FOUND Recipes from <FONT CLASS="text8blackbold"><%= recipe %></FONT> (<%= recipes.size() %> matches)<BR></td>
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
</fd:FindRecipes>