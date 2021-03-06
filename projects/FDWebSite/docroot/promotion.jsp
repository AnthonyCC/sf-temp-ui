<%-- internal promo details --%>

<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_PROMOTION_TOTAL = 970;
final int W_PROMOTION_LEFT = 200;
final int W_PROMOTION_RIGHT = 100;
%>
<fd:CheckLoginStatus/>
<%
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        //String referrer = request.getHeader("referer"); 
       
        boolean hasDepartment = false;
        
        String continueShoppingLink = "";
        String deptLink = "/department.jsp?deptId=";
        String deptId = request.getParameter("dept");
        String catId = request.getParameter("cat");
        String from = request.getParameter("fr");
        String deptName = "";
        
        if (deptId != null) {
                hasDepartment = true;
				continueShoppingLink = response.encodeURL(deptLink+deptId+"&");
			if ("kosher_temp".equalsIgnoreCase(deptId)) {
				deptName = "Kosher";
			} else {
                ContentNodeModel currentNode = PopulatorUtil.getContentNode(deptId);
                deptName = ((DepartmentModel)currentNode).getFullName();
            }    
        }
        else if (catId != null) {
                hasDepartment = true;
                ContentNodeModel currentNode = PopulatorUtil.getContentNode(catId);
				deptId = ((CategoryModel)currentNode).getDepartment().getContentName();
                continueShoppingLink = response.encodeURL(deptLink+deptId+"&");
                deptName = ((CategoryModel)currentNode).getDepartment().getFullName();
        } else if ("qs".equalsIgnoreCase(from)) {
                continueShoppingLink = "quickshop/index.jsp?";
        } else {
                continueShoppingLink = "index.jsp?";
        }
        
        continueShoppingLink += "trk=promo";
      
        boolean isPopup = false;
%>

<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Promotion"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Promotion</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
    <table width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td width="<%= W_PROMOTION_LEFT %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_PROMOTION_LEFT %>" height="1"></td>
      <td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
      <td width="<%= W_PROMOTION_RIGHT %>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_PROMOTION_RIGHT %>" height="1"></td>
    </tr>
    <tr>
    <td>&nbsp;</td>
    <td align="right">
            <table cellpadding="0" cellspacing="4" border="0">
            <tr valign="middle">
            <td align="right" style="padding-right: 5px;">
				<a href="<%=continueShoppingLink%>"><img src="/media_stat/images/buttons/continue_shopping_text.gif" width="117" height="13" border="0" alt="CONTINUE SHOPPING" /></a>
				<a href="<%=continueShoppingLink%>"><%= hasDepartment?("<br />in the <b>"+deptName+" Department</b>"):""%></a></td>
            <td><a href="<%=continueShoppingLink%>"><img src="/media_stat/images/buttons/arrow_green_right.gif" width="28" height="28" border="0" alt="CONTINUE SHOPPING"></a></td>
            </tr>
            </table>
    </td>
    <td rowspan="3">&nbsp;</td>
    </tr>
    
    <tr>
    <td colspan="2">
		<%@ include file="/includes/promotions/signup.jspf" %>
    </td>
    </tr>
    
    <tr>
    <td>&nbsp;</td>
    <td align="right">
            <table cellpadding="0" cellspacing="4" border="0">
            <tr valign="middle">
            <td align="right" style="padding-right: 5px;">
				<a href="<%=continueShoppingLink%>"><img src="/media_stat/images/buttons/continue_shopping_text.gif" width="117" height="13" border="0" alt="CONTINUE SHOPPING" /></a>
				<a href="<%=continueShoppingLink%>"><%= hasDepartment?("<br>in the <b>"+deptName+" Department</b>"):""%></a>
			</td>
            <td><a href="<%=continueShoppingLink%>"><img src="/media_stat/images/buttons/arrow_green_right.gif" width="28" height="28" border="0" alt="CONTINUE SHOPPING"></a></td>
            </tr>
            </table>
    </td>
    </tr>
    </table>
</tmpl:put>
</tmpl:insert>


					
