<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.content.util.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
    String catId     = request.getParameter("catId");
    String title     = request.getParameter("title");
    
    ContentFactory cf = ContentFactory.getInstance();
    ContentNodeModel category = cf.getContentNodeByName(catId);
    ComponentPopupView cpv = null;
    String listAs=null;
    
    if (category!=null) {
      listAs = category.getAttribute("LIST_AS","full");
    }
    
    String productNameSort=null;
    if ("nav".equals(listAs)) {
        productNameSort=SortStrategyElement.SORTNAME_NAV;
    } else if ("glance".equalsIgnoreCase(listAs)) {
        productNameSort=SortStrategyElement.SORTNAME_GLANCE;
    } else {
        productNameSort= SortStrategyElement.SORTNAME_FULL;
    }


%>
    <fd:ItemGrabber category='<%= category %>' id='itemCollection'  
     depth='10'    ignoreShowChildren='true'  returnHiddenFolders='false'>
<%
    List sortStrategy = new ArrayList();
    sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, productNameSort,false));
%>
<fd:ItemSorter nodes='<%=(List)itemCollection %>' strategy='<%=sortStrategy%>'/>

<%
        cpv = new ComponentPopupView(category, (ArrayList)itemCollection, title, request.getParameter("level"));%>
 </fd:ItemGrabber>
<%
    request.setAttribute("componentView", cpv);
%>

    <jsp:include page="/includes/popups/component_view.jsp" flush="false"/>
    
