<%@ page contentType="application/vnd.ms-excel" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='java.util.*' %>

<%
String contentType = "text/html";
if("true".equalsIgnoreCase(request.getParameter("xls"))) contentType = "application/vnd.ms-excel";

response.setContentType(contentType);
%>


<%@page import="com.freshdirect.webapp.util.JspMethods"%><html>
<head><title>Feature Report</title>

<!-- css for PC IE -->
<STYLE TYPE="text/css">
<!-- 
BODY 	     { font-size: 10px;  font-family: Verdana, Arial, sans-serif; }
td 	     { font-size: 9px;  font-family: Verdana, Arial, sans-serif; border-color: #CCCCCC; border-style: solid}
Table 	     { font-size: 9px;  font-family: Verdana, Arial, sans-serif; border-color: #CCCCCC; border-style: solid; }


/* A { text-decoration: none; } */

.pulldown 		{ font-size: 13px; font-family: Arial, sans-serif;}


.titleNB		{ font-size: 13px;  line-height: 18px;  font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.textNB 	    { color: #338800;  font-size: 9px; font-family: Verdana, Arial, sans-serif;}
.space2pix      { font-size: 2px; }


.text8 	         { font-size: 8px; font-family: Verdana, Arial, sans-serif;}
.text8w	         { font-size: 8px; color: #FFFFFF; font-family: Verdana, Arial, sans-serif;}
.text9 	         { font-size: 9px; font-family: Verdana, Arial, sans-serif;}
.text9w	         { font-size: 9px; color: #FFFFFF; font-family: Verdana, Arial, sans-serif;}
.text9bold       { font-size: 9px;  font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text9_lh12	     { font-size: 9px; line-height: 12px; font-family: Verdana, Arial, sans-serif;}

.text10 		 { font-size: 9px; font-family: Verdana, Arial, sans-serif;}
.text10bold      { font-size: 9px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text10italics 	 { font-size: 9px; font-style: italic; font-family: Verdana, Arial, sans-serif;}
.text10trebuchet { font-size: 10px; font-family: Trebuchet MS, sans-serif; font-family: Verdana, Arial, sans-serif;}
.text10_lh16	 { font-size: 9px; line-height: 16px; font-family: Verdana, Arial, sans-serif;}
.text10_lh14	 { font-size: 9px; line-height: 14px; font-family: Verdana, Arial, sans-serif;}

.text10or	     { font-size: 9px; color: #FF9900; font-family: Verdana, Arial, sans-serif;}
.text10w	     { font-size: 9px; color: #FFFFFF; font-family: Verdana, Arial, sans-serif;}
.text10wbold     { font-size: 9px; color: #FFFFFF; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text10gbold      { font-size: 9px; color: #336600; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.text10r 		 { font-size: 9px; color: #CC0000; font-family: Verdana, Arial, sans-serif;}
.text10rbold	 { font-size: 9px; font-weight: bold; color: #CC3300; font-family: Verdana, Arial, sans-serif;}
.text10o 		 { font-size: 9px; color: #2FB472; font-family: Verdana, Arial, sans-serif;}
.text10gr 		 { font-size: 9px; color: #666666; font-family: Verdana, Arial, sans-serif; font-weight: normal;}
.text10blbold 		 { font-size: 9px; color: #6699CC; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text10bl		 { font-size: 9px; color: #6699CC; font-family: Verdana, Arial, sans-serif;}

.title10 		{ font-size: 9px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.text11		     { font-size: 10px; font-family: Verdana, Arial, sans-serif;}
.text11bold      { font-size: 10px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.title11 		{ font-size: 10px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text11wbold     { font-size: 10px; color: #FFFFFF;  font-weight: bold;font-family: Verdana, Arial, sans-serif;}
.text11rbold	 { font-size: 10px; font-weight: bold; color: #CC0000; font-family: Verdana, Arial, sans-serif;}
.text11orbold	 { font-size: 10px; font-weight: bold; color: #FF9933; font-family: Verdana, Arial, sans-serif;}
.text11pbold	 { font-size: 10px; font-weight: bold; color: #CC6666; font-family: Verdana, Arial, sans-serif;}

.text12 		{ font-size: 11px; font-family: Verdana, Arial, sans-serif;}
.text12bold 	{ font-size: 11px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.text12wbold     { font-size: 11px; color: #FFFFFF;  font-weight: bold;font-family: Verdana, Arial, sans-serif;}
.text12gbold     { font-size: 11px; color: #336600;  font-weight: bold;font-family: Verdana, Arial, sans-serif;}
.text12rbold	 { font-size: 11px; font-weight: bold; color: #CC3300; font-family: Verdana, Arial, sans-serif;}

.title12 		{ font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.text13		    { font-size: 12px; font-family: Verdana, Arial, sans-serif;}
.text13bold		{ font-size: 12px; font-weight: bold;  font-family: Verdana, Arial, sans-serif;}
.text13rbold	 { font-size: 12px; font-weight: bold; color: #CC3300; font-family: Verdana, Arial, sans-serif;}
.text13orbold	 { font-size: 12px; font-weight: bold; color: #FF9933; font-family: Verdana, Arial, sans-serif;}


.title13 		{ font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.text14		    { font-size: 13px; font-family: Verdana, Arial, sans-serif;}

.title14		{ font-size: 13px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.titleor14		{ font-size: 13px; color: #FF9933; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.text15	        { font-size: 14px; font-family: Verdana, Arial, sans-serif;}

.title15 	    { font-size: 14px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.title16 	    { font-size: 15px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}
.title16or 	    { font-size: 15px; font-weight: bold; color: #FF9933; font-family: Verdana, Arial, sans-serif;}

.title17 	    { font-size: 16px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.title18 	    { font-size: 17px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.title20 	    { font-size: 19px; font-weight: bold; font-family: Verdana, Arial, sans-serif;}

.search         {width:100px; font-size: 10px; font-family: Verdana, Arial, sans-serif;}

.space1pix      { font-size: 1px; }
.space2pix      { font-size: 2px; }
.space4pix      { font-size: 4px; }
.space10pix     { font-size: 10px; }

.gbold          { color: #336600;  font-weight: bold; }
.FFFFFF	        { color: #FFFFFF; }
.EEEEEE	        { color: #EEEEEE; }
input.radio     { background : #FFFFFE; color: #000000;}
A.list		    { line-height: 12px; }

.noBorder		{ border-style: solid; border-width: 0px; }
	
.submit {
background-color: #000000;
font-size: 8pt;
color: #FFFFFF;
border: 1px #999999 solid;
padding: 2px;
padding-left: 0px;
padding-right: 0px;
}
-->
</STYLE>



</head>
<body>
<%
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    String deptName = request.getParameter("deptName");
    String deptId = request.getParameter("deptId");
	StoreModel store = getStore();
%>
<%if(contentType.equalsIgnoreCase("text/html")){%>
    <table border=01 BGCOLOR="#FFFFFF" CELLPADDING="02" CELLSPACING="0">
        <tr>
    <%  int count = 0;
        for (Iterator dIter = store.getDepartments().iterator(); dIter.hasNext(); count++) { 
            DepartmentModel dept = (DepartmentModel) dIter.next();
            if(count >= 17){
                count = 0;
                %></tr><tr><%
            }
    %>
            <td>
                    <font class="text12">
                    <%if(!dept.getContentKey().getId().equals(deptId)){%>
                    <a href="featured_report.jsp?deptId=<%= dept.getContentKey().getId() %>&deptName=<%= dept.getFullName() %>"><%= dept.getFullName() %>
                    <%}
                            else{%>
                            <b><%= dept.getFullName() %></b>
                            <%}%>
                    </font>	
            </td>
            <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <%}%>
    </tr>
    </table>
    <br>
<%}%>
<FONT CLASS="title14">Featured Products Report:  <%=deptName==null?"":deptName.toUpperCase()%></FONT><BR>
<%GregorianCalendar myDate = new GregorianCalendar();%>
<%=myDate.get(Calendar.MONTH)+1%>/<%=myDate.get(Calendar.DATE)%>/<%=myDate.get(Calendar.YEAR)%>  <%if(myDate.get(Calendar.HOUR)==0){%>12<%}else{%><%=myDate.get(Calendar.HOUR)%><%}%>:<%=myDate.get(Calendar.MINUTE)%> <%=myDate.get(Calendar.AM_PM)==1?" pm":" am"%><br>
<%if(deptId != null && contentType.equalsIgnoreCase("text/html")){%>
<table CLASS="noBorder"><tr>
    <form method="POST" action="<%=request.getRequestURI() + "?" + request.getQueryString() + "&xls=true"%>"><td CLASS="noBorder"><input class="submit" type='submit' value="Convert to excel"></td></form>
</tr></table>
<%}%>
**Italicized items are unavailable
<br>


 

<br>
<br>

<%
	if(deptId != null){
	%>


    <table border=01 BGCOLOR="#FFFFFF" CELLPADDING="02" CELLSPACING="0">
<%  
        List nodes = descendStore(deptId);
        int maxDepth = ((NodeHolder)Collections.max(nodes, nodeHolderDepthComparator)).getDepth();
%>
        <tr>
            <td colspan="<%= maxDepth+1 %>" align="center" valign=top><FONT CLASS="text12bold">Store Location</FONT></td>
            <td align="center" valign=top>
                <FONT CLASS="text12bold">Our Favorites<br></FONT>
                Used to feature products within a category<BR>
                top of some category pages (i.e. steaks)<br>
                <FONT CLASS="text10">first 4 available displayed</FONT>
            </td>
			<td align="center" valign=top>
                <FONT CLASS="text12bold">Promotions</FONT>
            </td>
        </tr>	
		
<%
        for (Iterator iter = nodes.iterator(); iter.hasNext(); ) { 
            int numOfFavorites = 1;
            NodeHolder nh = (NodeHolder) iter.next();
            
            String fontColor = "black";
            if(nh.getNode() instanceof CategoryModel && !((CategoryModel)nh.getNode()).getShowSelf())  fontColor="red";
%>
        <tr>
            <%if (nh.getDepth() > 0) {%><td colspan="<%= nh.getDepth() %>">&nbsp;</td><% } %>
            <td valign='top' style='color:<%=fontColor%>'>
				<%if (nh.getDepth() == 0 || nh.getDepth() == 1){%>
				<b><%= noBreak(nh.getNode().getFullName()) %></b>
				<%}
				else{%>
				<%= noBreak(nh.getNode().getFullName()) %>
				<%}%>
				
				
			</td>
            <% if (nh.getDepth() < maxDepth) { %><td colspan="<%= maxDepth-nh.getDepth() %>">&nbsp;</td><% } %>
            <td valign=top>
            <%-- begin Our Favorites --%>
<% 
                EnumLayoutType layout = nh.getNode().getLayout();
                if (!(layout.equals(EnumLayoutType.FEATURED_ALL) || layout.equals(EnumLayoutType.GROCERY_CATEGORY) || layout.equals(EnumLayoutType.MULTI_CATEGORY))) { %>
                n/a
<%              } else {
                    List<ProductModel> faves =  nh.getNode().getFeaturedProducts();
                    if (faves != null) {
                        int c = 0; %>
                        <table CLASS="noBorder">
<%                       for (ProductModel prd : faves) {
                            if (prd != null) { 
                                SkuModel defaultSku = prd.getDefaultSku();
                                if (defaultSku != null) {
                                    FDProductInfo prdInfo = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
                                    boolean unavail = prd.isUnavailable();
%>
                            <tr><td CLASS="noBorder"><%= ++c %></td><td CLASS="noBorder">&nbsp;&nbsp;</td><td CLASS="noBorder"><%= unavail?"<i>":"" %><%= noBreak(prd.getFullName()) %><%= unavail?"</i>":"" %></td><td CLASS="noBorder">&nbsp;&nbsp;</td><td align=right CLASS="noBorder"><%= JspMethods.formatPrice(prdInfo, user.getPricingContext()) %></td></tr>
<%                               }
                            }
                        }
                        numOfFavorites = c;
%>                      </table>
<%                  }
                }
            %>
            <%-- end Our Favorites --%>
            </td><td valign=top align=left>
            <%-- begin Promotion --%>
            <%-- end Promotion --%>
            </td>
        </tr>
<%      } %>
    </table>
<%   } %>

</body>
</html>
<%!

    ContentFactory contentFactory = ContentFactory.getInstance();
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

    private class NodeHolder {
    
        private ProductContainer node;
        private int depth;

        public NodeHolder(ProductContainer cn, int d) {
            node = cn;
            depth = d;
        }

        public ProductContainer getNode() {
            return node;
        }

        public int getDepth() {
            return depth;
        }

    }

    private Comparator nodeHolderDepthComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            NodeHolder nh1 = (NodeHolder) o1;
            NodeHolder nh2 = (NodeHolder) o2;
            return (nh1.getDepth() - nh2.getDepth());
        }
    };

    public StoreModel getStore() throws FDResourceException {
        return contentFactory.getStore();
    }

	public List descendStore(String deptId) throws FDResourceException {
        List reportNodes = new ArrayList();
		List depts = contentFactory.getStore().getDepartments();
		for(Iterator i = depts.iterator();i.hasNext(); ){
            DepartmentModel dept = (DepartmentModel) i.next();
            if (dept.getContentKey().getId().equals(deptId)) {
                descendDepartment(reportNodes, dept);
            }
		}
        return reportNodes;
	}

    public List descendStore() throws FDResourceException {
        List reportNodes = new ArrayList();
		List depts = contentFactory.getStore().getDepartments();
		for(Iterator i = depts.iterator();i.hasNext(); ){
            descendDepartment(reportNodes, (DepartmentModel) i.next());
		}
        return reportNodes;
	}

	public void descendDepartment(List nodeList, DepartmentModel dept) throws FDResourceException {
        int depth = 0;
        nodeList.add(new NodeHolder(dept, depth));
		List cats = dept.getCategories();
		for(Iterator i = cats.iterator(); i.hasNext(); ){
			descendCategory(nodeList, (CategoryModel)i.next(), depth);
		}
	}

	public void descendCategory(List nodeList, CategoryModel cat, int depth) throws FDResourceException {
        if(cat.getShowSelf()){
            ++depth;
            nodeList.add(new NodeHolder(cat, depth));
        }
            List sub = cat.getSubcategories();
            for (Iterator i = sub.iterator(); i.hasNext(); ) {
                    descendCategory(nodeList, (CategoryModel)i.next(), depth);
            }
	}

    public String noBreak(String b) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<b.length(); i++) {
            char c = b.charAt(i);
            if (c == ' ') {
                sb.append("&nbsp;");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

%>

