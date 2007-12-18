<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>
<%!
  SimpleDateFormat modByFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");
%>

<%
String servletContext = request.getContextPath();

String params = request.getQueryString();

boolean isProd = request.getParameter("prodId")!=null;
String action = request.getParameter("action")==null ? "" : request.getParameter("action");
String nodeId = request.getParameter("nodeId");

String cancelHref = "product_info.jsp?"+request.getQueryString();
String saveHref = "javascript:productDetailsForm.submit();";
System.out.println(" ################### just before tag: request = "+request.getParameter("action"));
%>
<sa:ProductController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='pm'>
<%
// *************************  debug stuff ************************

String submitButtonText="add";

String actionValue = pm.getPK()!=null ? "update" : "create";

String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");

String param = "catId=" + catId + "&prodId=" + prodId;
String attrib = "attrib=";
String prop = "prop=";
String type = "type=";
String add = "ADD";
String notSet = "<font class='notice'>Not set</font>";
String attribF = "attribute from: ";
String valF = " | value from: ";
String info = "&nbsp;<b>i</b>&nbsp;";

String rowSpace = "<tr><td colspan=\"4\" bgcolor=\"#E9E8D6\" style=\"padding:0px;\"><img src=\"" + servletContext + "/images/clear.gif\" width=\"1\" height=\"1\"></td></tr>";

String scrollHeight = "74%";
%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
	<form name="productDetailsForm" method="post" action="product_info.jsp">
	<input type="hidden" name="catId" value="<%=catId%>">
	<input name="action" type="hidden" value="<%=actionValue%>">
<%
Collection myErrors = result.getErrors();
if (myErrors.size() > 0 ){ %>
  <table>
<%  for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
      ActionError ae = (ActionError)itE.next();
      System.out.println(ae.getType()+"-->"+ae.getDescription());  %>
      <tr><td class="error"><%=ae.getDescription()%></td></tr>
<% }   %>
  </table>
<%
}
%>

	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td colspan="2" class="breadcrumb"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
		<span class="path">Current Path: <%@ include file="includes/breadcrumb.jspf"%> <%=pm.getPK()!=null ? pm.getFullName() : " <b>Adding new product</b>"%>
	    </td>
	</tr>
	<tr>
	<td valign="bottom">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
			<tr>
				<td class="tab_p">&nbsp;&nbsp;PRODUCT INFO&nbsp;&nbsp;</td>
				<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
				<td class="tab_off">
		<%      if (pm.getPK()!=null) {   %>
		           <a href="<%= servletContext %>/web_store/hierarchy/sku_info.jsp" class="tab">&nbsp;&nbsp;SKU INFO&nbsp;&nbsp;</a>
		<%      } else {   %>
		           &nbsp;&nbsp;SKU INFO&nbsp;&nbsp;
		<%      }   %>
			</tr>
		</table>
	</td>
	<td align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
			<tr align="center" valign="middle">
	<%      if(pm.getPK()!=null) {	%>
				<td class="preview"><a href="javascript:preview('/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
		<%      } else {      %>
				<td class="button">&nbsp;PREVIEW&nbsp;</td>
		<%      }  %>
				<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
				<td class="cancel"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
				<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
				<td class="save"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
				<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		</table>
	</td>
	</tr>
	<tr>
		<td colspan="2" class="tab_p">
			<table width="100%" cellpadding="2">
			<tr>
			<td align="right" class="tabDetails">Last Modified: <%=pm.getLastModified()!=null ? modByFormat.format(pm.getLastModified()) : ""%> By: <%=pm.getLastModifiedBy()%></td>
			</tr>
			</table>
		</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>

	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section" style="background-color:#FDFCEA;"">
	<tr bgcolor="#000000" class="sectionHeader"><th width="2%"><a href="#" onclick="toggleDisplay('properties'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th colspan="4">View/Edit Product Properties</th></tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Product ID</td>
	<td width="45%">
    <%
    if (pm.getPK()==null) { %>
    <input name="productId" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getContentName() %>">
    <%} else {  %>
	<input name="productId" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getContentName() %>">
    <%--input type="hidden" name="productId" value="<%= pm.getContentName() %>"><%=pm.getContentName() %--%>
    <%  }%>
    </td>
	<% Image catImage = (Image)pm.getCategoryImage(); %>
	<td width="20%" rowspan="2" class="notice"><% if (catImage != null) {%><img src="<%=catImage.getPath()%>" width="<%=catImage.getWidth()%>" height="<%=catImage.getHeight()%>" border="1"><%
	if (catImage.getHeight() > 100) {
		scrollHeight = "62%";
	}
	}else{%><i>no category image available</i><%}%></td>
	<td rowspan="2"><img src="<%= servletContext %>/images/clear.gif" width="8" height="1"></td>
	</tr>
	</table>

	<div style="position:relative; width:100%; height:<%=scrollHeight%>; overflow-y:scroll;">
	<div id="properties" style="display: none; background-color:#FDFCEA;">
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
			<td width="2%">&nbsp;&nbsp;&nbsp;</td>
			<td width="30%">CONTENT_NODE_ID</td>
			<td width="45%"><%= pm.getPK()!=null ? pm.getPK().getId() :"" %></td>
			<td width="20%"></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>ALT_TEXT</td>
			<td><input name="altText" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getAltText() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>FULL_NAME</td>
			<td><input name="fullName" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getFullName() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>GLANCE_NAME</td>
			<td><input name="glanceName" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getGlanceName() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>NAV_NAME</td>
			<td><input name="navName" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getNavName() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>KEYWORDS</td>
			<td><input name="keywords" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getKeywords() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>BLURB</td>
			<td><input name="blurb" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getBlurb() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>BLURB_TITLE</td>
			<td><input name="blurbTitle" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getBlurbTitle() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>EDITORIAL_TITLE</td>
			<td><input name="editorialTitle" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getEditorialTitle() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>AKA</td>
			<td><input name="aka" type="text" style="width:250px;" size="25" class="textbox2" value="<%= pm.getAka() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>QUANTITY_MIN</td>
			<td><input name="quantityMin" type="text" style="width:40px;" size="4" class="textbox2" value="<%= pm.getQuantityMinimum() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>QUANTITY_MAX</td>
			<td><input name="quantityMax" type="text" style="width:40px;" size="4" class="textbox2" value="<%= pm.getQuantityMaximum() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>QUANTITY_INCR</td>
			<td><input name="quantityInc" type="text" style="width:40px;" size="4" class="textbox2" value="<%= pm.getQuantityIncrement() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>INVISIBLE</td>
			<td><select name="isInvisible" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (pm.isInvisible()) {%>selected<%}%>>TRUE</option>
			<option <% if (!pm.isInvisible()) {%>selected<%}%>>FALSE</option>
			</select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>PERISHABLE</td>
			<td><select name="isPerishable" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (pm.isPerishable()) {%>selected<%}%>>TRUE</option>
			<option <% if (!pm.isPerishable()) {%>selected<%}%>>FALSE</option>
			</select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>FROZEN</td>
			<td><select name="isFrozen" style="width:80px;" class="pulldown1"><option></option>
			<option <% if (pm.isFrozen()) {%>selected<%}%>>TRUE</option>
			<option <% if (!pm.isFrozen()) {%>selected<%}%>>FALSE</option>
			</select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>GROCERY</td>
			<td><select name="isGrocery" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (pm.isGrocery()) {%>selected<%}%>>TRUE</option>
			<option <% if (!pm.isGrocery()) {%>selected<%}%>>FALSE</option>
			</select></td>
			<td></td>
		</tr>
	</table>
	</div>

	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
	<tr bgcolor="#000000" class="sectionHeader"><th width="2%"><a href="#" onclick="toggleDisplay('attributes'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th>View/Edit Product Attributes</th></tr>
	</table>

	<div id="attributes" style="display= show; background-color:#FDFCEA;">
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes1'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">PROMOS</td>
	</tr>
	</table>
	<div id="attributes1" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
			<td width="2%">&nbsp;&nbsp;&nbsp;</td>
			<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RECOMMEND_TABLE','l');">RECOMMEND_TABLE</a></td>
			<td width="45%">
	                <% if (pm.hasAttribute("RECOMMEND_TABLE")) {
	                                String thisMedia = ((Html)pm.getAttribute("RECOMMEND_TABLE").getValue()).getPath(); %>
	                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
	                <% }  %>
	                </td>
			<td width="20%"><% if (pm.hasAttribute("RECOMMEND_TABLE")) { %><%=info%><% } %></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RELATED_PRODUCTS','l');">RELATED_PRODUCTS</a></td>
			<td>
	                <% if (pm.hasAttribute("RELATED_PRODUCTS")) {
	                        List thisRef = (List)pm.getAttribute("RELATED_PRODUCTS").getValue();
	                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
	                                Object ref = refList.next();
	                                if (ref instanceof CategoryRef) {
	                                %><%= ((CategoryRef)ref).getCategoryName() %><% } else {%><%= ((ProductRef)ref).getProductName() %><% } %><%= refList.hasNext()?",":"" %>
	                <%    }
	                        }%>
	               </td>
			<td><% if (pm.hasAttribute("RELATED_PRODUCTS")){%><%=info%><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes2'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">LAYOUT/NAV</td>
	</tr>
	</table>
	<div id="attributes2" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>PRODUCT_LAYOUT</td>
		<td><select name="PRODUCT_LAYOUT"><OPTION></OPTION>
        <%  int layoutValue=pm.getAttribute("PRODUCT_LAYOUT",-1);
    		String selString ;
    		for(Iterator itrTypes =EnumProductLayout.getLayoutTypes().iterator(); itrTypes.hasNext();){
    			EnumProductLayout layoutType=(EnumProductLayout)itrTypes.next();
    			if (layoutType.getId()==layoutValue) {
    			   selString="selected";
   			   } else {
    			   selString="";
   			   } %>
    			<option <%=selString%> value="<%=layoutType.getId()%>"><%=layoutType.getName()%></option>;
<%    		} %></select>
        </td>
		<td><% if (pm.hasAttribute("PRODUCT_LAYOUT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PRODUCT_LAYOUT")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>TEMPLATE_TYPE</td>
		<td><select name="TEMPLATE_TYPE"><OPTION></OPTION>
        <%  int tmplValue=pm.getAttribute("TEMPLATE_TYPE",-1);
    		for(Iterator itrTypes =EnumTemplateType.getTemplateTypes().iterator(); itrTypes.hasNext();){
    			EnumTemplateType tmplType=(EnumTemplateType)itrTypes.next();
    	%>
    			<option <%=tmplType.getId()==tmplValue ? "selected" : ""%> value="<%=tmplType.getId()%>"><%=tmplType.getName()%></option>;
<%    		} %></select>
        </td>
		<td><% if (pm.hasAttribute("TEMPLATE_TYPE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("TEMPLATE_TYPE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=ALSO_SOLD_AS','l');">ALSO_SOLD_AS</a></td>
		<td width="45%">
                <% if (pm.hasAttribute("ALSO_SOLD_AS")) {
                        List thisRef = (List)pm.getAttribute("ALSO_SOLD_AS").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                ProductRef ref = (ProductRef)refList.next();
                 %><%= ref.getProductName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %>
         </td>
		<td width="20%"><% if (pm.hasAttribute("ALSO_SOLD_AS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("ALSO_SOLD_AS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>ALSO_SOLD_AS_NAME</td>
		<td><input name="ALSO_SOLD_AS_NAME" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("ALSO_SOLD_AS_NAME")) ? pm.getAttribute("ALSO_SOLD_AS_NAME").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("ALSO_SOLD_AS_NAME")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("ALSO_SOLD_AS_NAME")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>NUTRITION_MULTIPLE</td>
		<td><select name="NUTRITION_MULTIPLE" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (pm.getAttribute("NUTRITION_MULTIPLE",false)) {%>selected<%}%>>TRUE</option>
		<option <% if (!pm.getAttribute("NUTRITION_MULTIPLE",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (pm.hasAttribute("NUTRITION_MULTIPLE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("NUTRITION_MULTIPLE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PRIMARY_HOME','l');">PRIMARY_HOME</a></td>
		<td>
                <% if (pm.hasAttribute("PRIMARY_HOME")) {
                        CategoryRef thisRef = (CategoryRef)pm.getAttribute("PRIMARY_HOME").getValue();
                                %><%= thisRef.getCategoryName() %>
                <% } %>
                </td>
		<td><% if (pm.hasAttribute("PRIMARY_HOME")){%><%=info%><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=HOWTOCOOKIT_FOLDERS','l');">HOWTOCOOKIT_FOLDERS</a></td>
		<td>
                <% if (pm.hasAttribute("HOWTOCOOKIT_FOLDERS")) {
                        List thisRef = (List)pm.getAttribute("HOWTOCOOKIT_FOLDERS").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                CategoryRef ref = (CategoryRef)refList.next();
                 %><%= ref.getCategoryName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (pm.hasAttribute("HOWTOCOOKIT_FOLDERS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("HOWTOCOOKIT_FOLDERS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes3'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">HIDE & REDIRECT</td>
	</tr>
	</table>
	<div id="attributes3" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">NOT_SEARCHABLE</td>
		<td width="45%"><select name="NOT_SEARCHABLE" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (pm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>TRUE</option>
		<option <% if (!pm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>FALSE</option>
		</select></td>
		<td width="20%"><% if (pm.hasAttribute("NOT_SEARCHABLE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("NOT_SEARCHABLE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>REDIRECT_URL</td>
		<td><input name="REDIRECT_URL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("REDIRECT_URL")) ? pm.getAttribute("REDIRECT_URL").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("REDIRECT_URL")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("REDIRECT_URL")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes4'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">PRODUCT CONFIG</td>
	</tr>
	</table>
	<div id="attributes4" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">SELL_BY_SALESUNIT</td>
		<td width="45%"><input name="SELL_BY_SALES_UNIT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("SELL_BY_SALESUNIT")) ? pm.getAttribute("SELL_BY_SALESUNIT").getValue() :"" %>"></td>
		<td width="20%"><% if (pm.hasAttribute("SELL_BY_SALESUNIT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("SELL_BY_SALESUNIT")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>SALES_UNIT_LABEL</td>
		<td><input name="SALES_UNIT_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("SALES_UNIT_LABEL")) ? pm.getAttribute("SALES_UNIT_LABEL").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("SALES_UNIT_LABEL")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("SALES_UNIT_LABEL")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>QUANTITY_TEXT</td>
		<td><input name="QUANTITY_TEXT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("QUANTITY_TEXT")) ? pm.getAttribute("QUANTITY_TEXT").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("QUANTITY_TEXT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("QUANTITY_TEXT")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>QUANTITY_TEXT_SECONDARY</td>
		<td><input name="QUANTITY_TEXT_SECONDARY" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("QUANTITY_TEXT_SECONDARY")) ? pm.getAttribute("QUANTITY_TEXT_SECONDARY").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("QUANTITY_TEXT_SECONDARY")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("QUANTITY_TEXT_SECONDARY")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&domainTypeId=<%=EnumDomainType.VARIATION.getId()%>&attributeName=VARIATION_MATRIX','l');">VARIATION_MATRIX</a>
                <td><% if (pm.hasAttribute("VARIATION_MATRIX")) {
                        List thisRef = (List)pm.getAttribute("VARIATION_MATRIX").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
			        DomainRef ref = ((DomainRef)refList.next());
                %>
                <%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <% }
                 }  %>
                 </td>
		<td><% if (pm.hasAttribute("VARIATION_MATRIX")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("VARIATION_MATRIX")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&domainTypeId=<%=EnumDomainType.VARIATION.getId()%>&attributeName=VARIATION_OPTIONS','l');">VARIATION_OPTIONS</a>
                <td><% if (pm.hasAttribute("VARIATION_OPTIONS")) {
                        List thisRef = (List)pm.getAttribute("VARIATION_OPTIONS").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
			        DomainRef ref = ((DomainRef)refList.next());
                %>
                <%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <% }
                 }  %>
                 </td>
		<td><% if (pm.hasAttribute("VARIATION_OPTIONS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("VARIATION_OPTIONS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>SERVING_SUGGESTION</td>
		<td><textarea name="SERVING_SUGGESTION" style="width:250px;" cols="20" rows="1" wrap="VIRTUAL" class="textbox2"><%= (pm.hasAttribute("SERVING_SUGGESTION")) ? pm.getAttribute("SERVING_SUGGESTION").getValue() :"" %></textarea></td>
		<td><% if (pm.hasAttribute("SERVING_SUGGESTION")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("SERVING_SUGGESTION")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>PACKAGE_DESCRIPTION</td>
		<td><input name="PACKAGE_DESCRIPTION" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("PACKAGE_DESCRIPTION")) ? pm.getAttribute("PACKAGE_DESCRIPTION").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("PACKAGE_DESCRIPTION")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PACKAGE_DESCRIPTION")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>CONTAINER_WEIGHT_HALF_PINT</td>
		<td><input name="CONTAINER_WEIGHT_HALF_PINT" type="text" style="width:60px;" size="6" class="textbox2" value="<%= (pm.hasAttribute("CONTAINER_WEIGHT_HALF_PINT")) ? pm.getAttribute("CONTAINER_WEIGHT_HALF_PINT").getValue() :"" %>"> lb</td>
		<td><% if (pm.hasAttribute("CONTAINER_WEIGHT_HALF_PINT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("CONTAINER_WEIGHT_HALF_PINT")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>CONTAINER_WEIGHT_PINT</td>
		<td><input name="CONTAINER_WEIGHT_PINT" type="text" style="width:60px;" size="6" class="textbox2" value="<%= (pm.hasAttribute("CONTAINER_WEIGHT_PINT")) ? pm.getAttribute("CONTAINER_WEIGHT_PINT").getValue() :"" %>"> lb</td>
		<td><% if (pm.hasAttribute("CONTAINER_WEIGHT_PINT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("CONTAINER_WEIGHT_PINT")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>CONTAINER_WEIGHT_QUART</td>
		<td><input name="CONTAINER_WEIGHT_QUART" type="text" style="width:60px;" size="6" class="textbox2" value="<%= (pm.hasAttribute("CONTAINER_WEIGHT_QUART")) ? pm.getAttribute("CONTAINER_WEIGHT_QUART").getValue() :"" %>"> lb</td>
		<td><% if (pm.hasAttribute("CONTAINER_WEIGHT_QUART")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("CONTAINER_WEIGHT_QUART")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>UNIT_QUANTITY</td>
		<td><input name="UNIT_QUANTITY" type="text" style="width:60px;" size="6" class="textbox2" value="<%= (pm.hasAttribute("UNIT_QUANTITY")) ? pm.getAttribute("UNIT_QUANTITY").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("UNIT_QUANTITY")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("UNIT_QUANTITY")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>WINE_TYPE</td>
		<td><input name="WINE_TYPE" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("WINE_TYPE")) ? pm.getAttribute("WINE_TYPE").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("WINE_TYPE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("WINE_TYPE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>WINE_REGION</td>
		<td><input name="WINE_REGION" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("WINE_REGION")) ? pm.getAttribute("WINE_REGION").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("WINE_REGION")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("WINE_REGION")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>WINE_FYI</td>
		<td><input name="WINE_REGION" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("WINE_FYI")) ? pm.getAttribute("WINE_FYI").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("WINE_FYI")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("WINE_FYI")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&domainTypeId=<%=EnumDomainType.MEASURE.getId()%>&attributeName=UNIT_OF_MEASURE','l');">UNIT_OF_MEASURE</a></td>
		<td><% if (pm.hasAttribute("UNIT_OF_MEASURE")) { %>
                <table cellpadding="2" cellspacing="0" border="0" class="section">
                 <%
                        DomainValueRef dvref = (DomainValueRef) pm.getAttribute("UNIT_OF_MEASURE").getValue();
                        DomainValue dv = dvref.getDomainValue();                %>
                    <tr>
                    <td><%= dv.getDomain().getLabel() %></td>
                    <td> <%=dv.getLabel()%></td>
                 </tr>
                </table>
                <%   }  else { %><%= notSet %><%}%></td>
		<td><% if (pm.hasAttribute("UNIT_OF_MEASURE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("UNIT_OF_MEASURE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes5'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - IMAGES</td>
	</tr>
	</table>
	<div id="attributes5" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=ALTERNATE_IMAGE','l');">ALTERNATE_IMAGE</td>
		<td width="45%">
                <% if (pm.hasAttribute("ALTERNATE_IMAGE")) {
                                String thisMedia = ((MediaI)pm.getAttribute("ALTERNATE_IMAGE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td width="20%"><% if (pm.hasAttribute("ALTERNATE_IMAGE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("ALTERNATE_IMAGE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DESCRIPTIVE_IMAGE','l');">DESCRIPTIVE_IMAGE</td>
		<td>
                <% if (pm.hasAttribute("DESCRIPTIVE_IMAGE")) {
                                String thisMedia = ((MediaI)pm.getAttribute("DESCRIPTIVE_IMAGE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("DESCRIPTIVE_IMAGE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("DESCRIPTIVE_IMAGE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE','l');">PROD_IMAGE</td>
		<td>
                <% if (pm.hasAttribute("PROD_IMAGE")) {
                                String thisMedia = ((MediaI)pm.getAttribute("PROD_IMAGE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_IMAGE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE_CONFIRM','l');">PROD_IMAGE_CONFIRM</td>
		<td>
                <% if (pm.hasAttribute("PROD_IMAGE_CONFIRM")) {
                                String thisMedia = ((MediaI)pm.getAttribute("PROD_IMAGE_CONFIRM").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE_CONFIRM")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_IMAGE_CONFIRM")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE_DETAIL','l');">PROD_IMAGE_DETAIL</td>
		<td>
             <% if (pm.hasAttribute("PROD_IMAGE_DETAIL")) {
                                String thisMedia = ((MediaI)pm.getAttribute("PROD_IMAGE_DETAIL").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
             <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE_DETAIL")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_IMAGE_DETAIL")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE_FEATURE','l');">PROD_IMAGE_FEATURE</td>
		<td>
                <% if (pm.hasAttribute("PROD_IMAGE_FEATURE")) {
                                String thisMedia = ((MediaI)pm.getAttribute("PROD_IMAGE_FEATURE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE_FEATURE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_IMAGE_FEATURE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE_ROLLOVER','l');">PROD_IMAGE_ROLLOVER</td>
		<td>
                <% if (pm.hasAttribute("PROD_IMAGE_ROLLOVER")) {
                                String thisMedia = ((MediaI)pm.getAttribute("PROD_IMAGE_ROLLOVER").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE_ROLLOVER")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_IMAGE_ROLLOVER")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_IMAGE_ZOOM','l');">PROD_IMAGE_ZOOM</td>
		<td>
                <% if (pm.hasAttribute("PROD_IMAGE_ZOOM")) {
                                String prod_image = ((MediaI)pm.getAttribute("PROD_IMAGE_ZOOM").getValue()).getPath(); %>
                                        <%= prod_image.substring(prod_image.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_IMAGE_ZOOM")){%><%=info%><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_DESCRIPTION_NOTE','l');">PROD_DESCRIPTION_NOTE</td>
		<td>
                <% if (pm.hasAttribute("PROD_DESCRIPTION_NOTE")) {
                                String prod_image = ((MediaI)pm.getAttribute("PROD_DESCRIPTION_NOTE").getValue()).getPath(); %>
                                        <%= prod_image.substring(prod_image.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_DESCRIPTION_NOTE")){%><%=info%><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_QUALITY_NOTE','l');">PROD_QUALITY_NOTE</td>
		<td>
                <% if (pm.hasAttribute("PROD_QUALITY_NOTE")) {
                                String prod_image = ((MediaI)pm.getAttribute("PROD_QUALITY_NOTE").getValue()).getPath(); %>
                                        <%= prod_image.substring(prod_image.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_QUALITY_NOTE")){%><%=info%><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes6'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - MIXED</td>
	</tr>
	</table>
	<div id="attributes6" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FRESH_TIPS','l');">FRESH_TIPS</td>
		<td width="45%">
                <% if (pm.hasAttribute("FRESH_TIPS")) {
                        List thisMedia = (List)pm.getAttribute("FRESH_TIPS").getValue();
                      for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
			TitledMedia tm = ((TitledMedia)tmList.next());
                 %>
                <%= tm.getPath().substring(tm.getPath().lastIndexOf("/")+1) %><%= tmList.hasNext()?",":"" %>
                <%   }
                } %></td>
		<td width="20%"><% if (pm.hasAttribute("FRESH_TIPS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("FRESH_TIPS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PROD_DESCR','l');">PROD_DESCR</td>
		<td width="45%">
                <% if (pm.hasAttribute("PROD_DESCR")) {
                                String thisMedia = ((Html)pm.getAttribute("PROD_DESCR").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("PROD_DESCR")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_DESCR")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DONENESS_GUIDE','l');">DONENESS_GUIDE</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>DONENESS_GUIDE&<%=type%>txt','l')">
                <% if (pm.hasAttribute("DONENESS_GUIDE")) {
                        List thisMedia = (List)pm.getAttribute("DONENESS_GUIDE").getValue();
                        for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
			TitledMedia tm = ((TitledMedia)tmList.next());
                 %>
                <%= tm.getPath().substring(tm.getPath().lastIndexOf("/")+1) %><%= tmList.hasNext()?",":"" %>
                <% }
                 } %>
                </td>
		<td><% if (pm.hasAttribute("DONENESS_GUIDE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("DONENESS_GUIDE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FDDEF_FRENCHING','l');">FDDEF_FRENCHING</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>FDDEF_FRENCHING&<%=type%>txt','l')">
                 <% if (pm.hasAttribute("FDDEF_FRENCHING")) {
                                String thisMedia = ((TitledMedia)pm.getAttribute("FDDEF_FRENCHING").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("FDDEF_FRENCHING")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("FDDEF_FRENCHING")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FDDEF_GRADE','l');">FDDEF_GRADE</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>FDDEF_GRADE&<%=type%>txt','l')">
                 <% if (pm.hasAttribute("FDDEF_GRADE")) {
                                String thisMedia = ((TitledMedia)pm.getAttribute("FDDEF_GRADE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("FDDEF_GRADE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("FDDEF_GRADE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FDDEF_RIPENESS','l');">FDDEF_RIPENESS</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>FDDEF_RIPENESS&<%=type%>txt','l')">
                 <% if (pm.hasAttribute("FDDEF_RIPENESS")) {
                                String thisMedia = ((TitledMedia)pm.getAttribute("FDDEF_RIPENESS").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("FDDEF_RIPENESS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("FDDEF_RIPENESS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FDDEF_SOURCE','l');">FDDEF_SOURCE</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>FDDEF_SOURCE&<%=type%>txt','l')">
                <% if (pm.hasAttribute("FDDEF_SOURCE")) {
                                String thisMedia = ((TitledMedia)pm.getAttribute("FDDEF_SOURCE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("FDDEF_SOURCE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("FDDEF_SOURCE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=SALES_UNIT_DESCRIPTION','l');">SALES_UNIT_DESCRIPTION</td>
		<td>
                <% if (pm.hasAttribute("SALES_UNIT_DESCRIPTION")) {
                                String thisMedia = ((TitledMedia)pm.getAttribute("SALES_UNIT_DESCRIPTION").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (pm.hasAttribute("SALES_UNIT_DESCRIPTION")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("SALES_UNIT_DESCRIPTION")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=PARTIALLY_FROZEN','l');">PARTIALLY_FROZEN</a></td>
		<td>
                <% if (pm.hasAttribute("PARTIALLY_FROZEN")) {
                                String thisMedia = ((Html)pm.getAttribute("PARTIALLY_FROZEN").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %>
        </td>
		<td><% if (pm.hasAttribute("PARTIALLY_FROZEN")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PARTIALLY_FROZEN")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes7'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">RATING</td>
	</tr>
	</table>
	<div id="attributes7" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">SEASON_TEXT</td>
		<td width="45%"><input name="SEASON_TEXT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("SEASON_TEXT")) ? pm.getAttribute("SEASON_TEXT").getValue() :"" %>"></td>
		<td width="20%"><% if (pm.hasAttribute("SEASON_TEXT")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("SEASON_TEXT")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&domainTypeId=<%=EnumDomainType.RATING.getId()%>&attributeName=RATING','l');">RATING</a></td>
		<td>
                <% if (pm.isHomeOfAttribute("RATING")) { %>
                <table cellpadding="2" cellspacing="0" border="0" class="section">
                <%
                        List thisDomain = (List)pm.getAttribute("RATING").getValue();
                        for (Iterator domList=thisDomain.iterator(); domList.hasNext();) {
			                DomainValueRef dvref = ((DomainValueRef)domList.next());
                            DomainValue dv = dvref.getDomainValue();                %>
                    <tr>
                    <td><%= dv.getDomain().getLabel() %></td>
                    <td> <%=dv.getLabel()%></td>
                 </tr>
                <%      } %>
                </table>
                <%     }  else { %><%= notSet %><%}%>
	  	</td>
		<td><% if (pm.hasAttribute("RATING")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("RATING")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RATING_RELATED_IMAGE','l');">RATING_RELATED_IMAGE</a></td>
		<td>
           <% if (pm.hasAttribute("RATING_RELATED_IMAGE")) {
                String thisMedia = ((Image)pm.getAttribute("RATING_RELATED_IMAGE").getValue()).getPath(); %>
                <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
           <% } %></td>
		<td><% if (pm.hasAttribute("RATING_RELATED_IMAGE")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("RATING_RELATED_IMAGE")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>USAGE_LIST</td>
		<td><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>USAGE_LIST&<%=type%>dom','l')">
                <% if (pm.isHomeOfAttribute("USAGE_LIST")) {
                    List thisRef = (List)pm.getAttribute("USAGE_LIST").getValue();
                     for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
			            DomainValueRef dref = ((DomainValueRef)refList.next());
                %>
                <%= dref.getDomain().getName() %><%= refList.hasNext()?",":"" %>
                <%  }
                  } %>
                </td>
		<td><% if (pm.hasAttribute("USAGE_LIST")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("USAGE_LIST")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>PROD_PAGE_TEXT_RATINGS</td>
		<td><input name="PROD_PAGE_TEXT_RATINGS" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (pm.hasAttribute("PROD_PAGE_TEXT_RATINGS")) ? pm.getAttribute("PROD_PAGE_TEXT_RATINGS").getValue() :"" %>"></td>
		<td><% if (pm.hasAttribute("PROD_PAGE_TEXT_RATINGS")){%><a href="#" title="<%=attribF%><%=pm.getAttributeHome("PROD_PAGE_TEXT_RATINGS")%><%=valF%>?method?" class="icon"><%=info%></a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	</div>

	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" bgcolor="#CCCCCC"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
<%      if(pm.getPK()!=null) {	%>
		<td bgcolor="#0000CC" class="button"><a href="javascript:preview('/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td bgcolor="#0000CC" class="button">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table>
	</td>
	</tr>
	</table>
  <%--
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="< %= servletContext % >/images/clear.gif" width="1" height="2"></td></tr>
	<tr bgcolor="#000000" class="sectionHeader"><th width="2%">&nbsp;</th><th>Product Placement</th></tr>
	<tr><td>&nbsp;</td><td><img src="< %= servletContext % >/images/clear.gif" width="1" height="2"><br><font class="sectionSubHeader">PRIMARY HOME</font><br>
        < % if (pm.hasAttribute("PRIMARY_HOME")) {
                        CategoryRef thisRef = (CategoryRef)pm.getAttribute("PRIMARY_HOME").getValue();
                                % >< %= thisRef.getCategoryName() % >
                < % } % >
        </td></tr>
	<tr><td>&nbsp;</td><td><img src="< %= servletContext %>/images/clear.gif" width="1" height="4"><br><font class="sectionSubHeader">ALIASES</font><br>some_product_name_1a in bread/crumb/trail/1a<br>
	some_product_name_1b in bread/crumb/trail/1b<br>
	some_product_name_1c in bread/crumb/trail/1c
	</td></tr>
	</table>   --%>
	</div>

    </tmpl:put>
    </form>
</tmpl:insert>
</sa:ProductController>