<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.content.util.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<%
String deptId = request.getParameter("deptId");
String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");
String attrib = request.getParameter("attrib");
String tmpl = request.getParameter("tmpl");
String spec = request.getParameter("spec");

ContentFactory cf = ContentFactory.getInstance();
MediaI mi = null;
DepartmentModel dep = null;
CategoryModel cat = null;
String catName = null;
ProductModel prod =  null;
ContentNodeModel dept = null;
String deptName = null;

String title = null;
String pathToMedia = null;
Image prodImg = null;
boolean special = false; //hardcoded media path
boolean notMediaFile = false;
boolean kosher = false;

if ("KOSHER".equalsIgnoreCase(attrib)) {
    special = true;
    kosher = true;
    pathToMedia = "/media/editorial/kosher/symbols/"+spec+".html";
    title = "Kosher Symbol";
} else if ("DEPT_STORAGE_GUIDE_MEDIA".equalsIgnoreCase(attrib) || "CAT_STORAGE_GUIDE_MEDIA".equalsIgnoreCase(attrib)) {
        special = true;
        notMediaFile = true; 
        if (deptId!=null){
                dep = (DepartmentModel)cf.getContentNodeByName(deptId);
                deptName = dep.getFullName();
                //pathToMedia = "/shared/includes/dept_storage_guide.jspf";  not media file included below
                title = deptName + " Storage Guide";
        } else {
                cat = (CategoryModel)cf.getContentNodeByName(catId);
                catName = cat.getFullName();
                //pathToMedia = "/shared/includes/cat_storage_guide.jspf";  not media file included below
                title = catName + " Storage Guide";
        }
} else if ("DEPT_MGR_BIO".equalsIgnoreCase(attrib) || "PARTIALLY_FROZEN".equalsIgnoreCase(attrib)) {
        dep = (DepartmentModel)cf.getContentNodeByName(deptId);
        deptName = dep.getFullName();
        special = true; 
                if ("DEPT_MGR_BIO".equalsIgnoreCase(attrib)) {
                        if (spec != null) {
                        pathToMedia = "/media/editorial/manager_bios/" + spec + ".html";
                        } else {
                        pathToMedia = ((MediaI)dep.getAttribute(attrib).getValue()).getPath();
                        }
                        
                        if (dep.getAttribute(attrib).getValue() instanceof TitledMedia) {
                                if (spec != null) {
                                        if ("craig_kominiak".equalsIgnoreCase(spec)) {
                                        title = "Craig Kominiak, Bakery";
                                        } else if ("fredy_ayala".equalsIgnoreCase(spec)) {
                                        title = "Fredy Ayala, Pastry";
                                        } else if ("david_mcInerney".equalsIgnoreCase(spec)) {
                                        title = "David McInerney, Executive Chef";
                                        } else if ("michael_stark".equalsIgnoreCase(spec)) {
                                        title = "Michael Stark, Chef de Cuisine";
                                        }
                                } else {
                                title = ((TitledMedia)dep.getAttribute(attrib).getValue()).getMediaTitle();
                                }
                        } else {
                                title = deptName + " - Department Manager";
                        }
                } else {
                title = deptName + " - ";
                if ("parbaked_promo".equalsIgnoreCase(spec)) {
                title += "Parbaked Breads";
                } else {
                title += "About Flash-Freezing";
                }
                pathToMedia = "/media/editorial/" + deptName.toLowerCase() +"/fd_defs/" + spec + ".html";
                }
} else {
        prod =  cf.getProductByName(catId,prodId);
        prodImg = prod.getCategoryImage();
        dept = prod.getDepartment();
        deptName = dept.getFullName();
        pathToMedia = ((MediaI)prod.getAttribute(attrib).getValue()).getPath();
        if (prod.getAttribute(attrib).getValue() instanceof TitledMedia) {
                title = ((TitledMedia)prod.getAttribute(attrib).getValue()).getMediaTitle();
        } else {
                cat = (CategoryModel)cf.getContentNodeByName(catId);
                catName = cat.getFullName();
                if ("FDDEF_FRENCHING".equalsIgnoreCase(attrib)) {
                        title = "About Frenching";
                } else if ("FDDEF_RIPENESS".equalsIgnoreCase(attrib)) {
                        title = "About Ripeness: " + catName;
                } else {
                        if ("SALES_UNIT_DESCRIPTION".equalsIgnoreCase(attrib)) {
                                title = deptName + " - How Much to Buy";
                        } else if ("FDDEF_GRADE".equalsIgnoreCase(attrib)) {
                                title = deptName + " - About "+ catName +" Grades";
                        } else if ("PRODUCT_ABOUT".equalsIgnoreCase(attrib)) {
                                title = deptName + " - About this " + deptName.toLowerCase() + " product";
                        }
                }
        }
}

boolean showImg = false;
if (!special && !"FDDEF_GRADE".equalsIgnoreCase(attrib) && !"FDDEF_SOURCE".equalsIgnoreCase(attrib) && !"FDDEF_FRENCHING".equalsIgnoreCase(attrib) && !"cheese".equalsIgnoreCase(deptName) ) {
        showImg = true;
}

String recTable = null;
if (!special && prod.hasAttribute("RECOMMEND_TABLE")) {
recTable = ((Html)prod.getAttribute("RECOMMEND_TABLE").getValue()).getPath();
}

boolean isWine = dept != null && "win".equalsIgnoreCase(dept.getContentName());

String tmplFile = "/common/template/" + tmpl + "_pop.jsp";

if (isWine) {
    tmplFile = "/common/template/bestcellars/" + tmpl + "_pop.jsp";
}

String prodPrice = null;

%>

<tmpl:insert template='<%=tmplFile%>'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%=title%></tmpl:put>
        <tmpl:put name='content' direct='true'>

                <% if ("SALES_UNIT_DESCRIPTION".equalsIgnoreCase(attrib) && recTable != null && !"".equals(recTable)) { //has rec table 
                        if ("large".equalsIgnoreCase(tmpl)) { //large popup %>             
                <table border="0" cellpadding="0" cellspacing="0" width="520">
            <tr valign="top">
            <td colspan="3" class="text11"><fd:IncludeMedia name="<%=pathToMedia%>" /></td>
            </tr>
                        <tr><td><img src="/media_stat/images/layout/clear.gif" width="85" height="12"></td><td><img src="/media_stat/images/layout/clear.gif" width="305" height="12"></td><td><img src="/media_stat/images/layout/clear.gif" width="130" height="12"></td></tr>
            <tr valign="top">
                <td rowspan="2"><img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" border="0" alt="<%= prod.getFullName() %>"></td>
                <td><font class="title14"><%=prod.getFullName()%></font></td>
                                <td align="right"><img src="/media_stat/images/layout/star.gif" width="6" height="6" hspace="2" vspace="4" border="0" alt="most popular"><font class="text9">Most popular thickness</font></td>
            </tr>
                        <tr><td colspan="2"><img src="/media_stat/images/layout/669933.gif" width="435" height="1" hspace="0" vspace="6"><br><fd:IncludeMedia name="<%=recTable%>" /></td></tr>
            </table>
                        <% } else { //small popup %>
                                <table border="0" cellpadding="0" cellspacing="0" width="315">
                                <tr valign="top">
                            <td class="text11"><fd:IncludeMedia name="<%=pathToMedia%>" /></td>
                                <td align="right"><img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" border="0" alt="<%= prod.getFullName() %>"></td>
                            <tr>
                                <tr><td><img src="/media_stat/images/layout/clear.gif" width="230" height="10"></td><td><img src="/media_stat/images/layout/clear.gif" width="85" height="10"></td></tr>
                                <tr valign="top">
                                <td colspan="2"><font class="title14"><%=prod.getFullName()%></font><br><img src="/media_stat/images/layout/669933.gif" width="315" height="1" hspace="0" vspace="3"><br><fd:IncludeMedia name="<%=recTable%>" /></td>
                                </tr>
                                </table>
                        <% 
                                } 
                     } else { //no rec table %>
                        <% if (showImg) { //img layout + specs for wine dept layout %>
                                <table border="0" cellpadding="0" cellspacing="0" width="<%= "small".equalsIgnoreCase(tmpl) ? "315" : "520"%>">
                                <tr><td colspan="2" class="text12">
                                <% if (isWine) {
                                    SkuModel sku = prod.getDefaultSku();
                    %>
                                <fd:FDProductInfo id="productInfo" skuCode="<%=  sku.getSkuCode() %>">
                    <%   
                                prodPrice = currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
                    %>                      
                                </fd:FDProductInfo>
                                <%
                                    String winId = catId.substring(4,7);
                                    String icon = "/media_stat/images/template/wine/" + winId +  "_icon.gif";
                                    String headerBg = "#";
                                        if ("fiz".equalsIgnoreCase(winId)) {
                                            headerBg += "92B1DC";
                                        } else if ("fre".equalsIgnoreCase(winId)) {
                                            headerBg += "BBD55D";
                                        } else if ("sof".equalsIgnoreCase(winId)) {
                                            headerBg += "FFE653";
                                        } else if ("lus".equalsIgnoreCase(winId)) {
                                            headerBg += "F0A500";
                                        } else if ("jui".equalsIgnoreCase(winId)) {
                                            headerBg += "D62E2E";
                                        } else if ("smo".equalsIgnoreCase(winId)) {
                                            headerBg += "AB005A";
                                        } else if ("big".equalsIgnoreCase(winId)) {
                                            headerBg += "894C6F";
                                        } else if ("swe".equalsIgnoreCase(winId)) {
                                            headerBg += "F2948F";
                                        } else if ("bey".equalsIgnoreCase(winId)) {
                                            headerBg += "8E343F";
                                            icon = "/media_stat/images/layout/clear.gif";
                                        } 
                                %>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td bgcolor="<%= headerBg %>"><img src="<%= icon %>" width="35" height="35"></td>
                                            <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
                                            <td width="100%" align="center" class="text12wbold" bgcolor="<%= headerBg %>"><span class="text15"><%=prod.getGlanceName().toUpperCase()%></span><br><%=prod.getNavName().toUpperCase()%></td>
                                            <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
                                            <td bgcolor="<%= headerBg %>"><img src="<%= icon %>" width="35" height="35"></td>
                                        </tr>
                                    </table>
                                <% } else { %>
                                    <b><%=title%></b>
                                <% } %>
                                </td></tr>
                                <tr><td><img src="/media_stat/images/layout/clear.gif" width="90" height="10"></td><td><img src="/media_stat/images/layout/clear.gif" width="225" height="10"></td></tr>
                                <tr valign="top">
                                <td <%= isWine ? "align=\"center\"" : ""%>><img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" border="0" alt="<%= prod.getFullName() %>"><% if (isWine) { %><br><font class="space4pix"><br></font><span class="text12"><b><%=prodPrice%></b></span><% } %></td>
                            <td class="text11"><fd:IncludeMedia name="<%=pathToMedia%>" /><% if (isWine) { %><br><br><br><%@ include file="/shared/includes/wine_copyright.jspf" %><% } %></td>
                            <tr>
                                </table>
                        <% } else { //no img layout | not media include %>
                                <table width="98%"><tr><td>
                                <% if (!notMediaFile) {
                                %>
                                <% if (kosher) {%><fd:IncludeMedia name="/media/editorial/kosher/symbols/kos_symbol_top.html" /><br><br><% } %>
                                <fd:IncludeMedia name="<%=pathToMedia%>" />
                                <% if (kosher) {%><fd:IncludeMedia name="/media/editorial/kosher/symbols/kos_symbol_bot.html" /><% } %>
                                <%} else {%>
                                        <% if ("DEPT_STORAGE_GUIDE_MEDIA".equalsIgnoreCase(attrib)) { %>
                                                <%@ include file="/shared/includes/dept_storage_guide.jspf" %>
                                        <% } else if ("CAT_STORAGE_GUIDE_MEDIA".equalsIgnoreCase(attrib)) { %>
                                                <%@ include file="/shared/includes/cat_storage_guide.jspf" %>
                                        <% } %>
                                <% }  %></td></tr>
                                </table>
                        <%
                        } 
                    }%>
    </tmpl:put>
</tmpl:insert>
<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>