<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
    String parent    = request.getParameter("parent");
    String catId     = request.getParameter("catId");
    String title     = request.getParameter("title");
    String level     = request.getParameter("level");
    
    ContentFactory cf = ContentFactory.getInstance();    
    ProductModel product = cf.getProduct(parent);
    ContentNodeModel category = cf.getContentNode(catId);
    
    List<ProductModel> productBundle = product.getProductBundle();

    ComponentPopupView cpv = new ComponentPopupView(category, parent, productBundle, title, level);
    request.setAttribute("componentView", cpv);
%>
    <jsp:include page="/includes/popups/component_view.jsp" flush="false"/>
    
    