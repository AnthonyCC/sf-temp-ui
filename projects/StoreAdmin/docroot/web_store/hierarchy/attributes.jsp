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
String param = request.getQueryString();

String deptId = request.getParameter("deptId");
String catId = request.getParameter("catId");
String nodeId = request.getParameter("nodeId");

String action = request.getParameter("action")==null ? "" : request.getParameter("action");
String saveHref = "javascript:categoryDetailsForm.submit();";

String rowSpace = "<tr><td colspan=\"4\" bgcolor=\"#E9E8D6\" style=\"padding:0px;\"><img src=\"" + servletContext + "/images/clear.gif\" width=\"1\" height=\"1\"></td></tr>";
%>

<sa:CategoryController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='cm'>

<%
if (cm.getPK()!=null) {
   action = "update";
}else {
    action = "create";
}

String cancelHref;
ContentNodeModel prevNode = null;
if (cm.getPK() !=null) {
   cancelHref = "attributes.jsp?nodeId="+cm.getPK().getId()
                    +"&parentNodeId="+cm.getParentNode().getPK().getId()
                    +"&catId="+cm.getParentNode().getContentName();
}else {
   prevNode = ContentFactory.getInstance().getContentNode(request.getParameter("parentNodeId"));
   if(prevNode.getContentType().equals(ContentNodeI.TYPE_CATEGORY)) {
     cancelHref = "cat_contents.jsp?nodeId="+prevNode.getPK().getId()+"&parentNodeId="+prevNode.getParentNode().getPK().getId()+"&catId="+prevNode;
   } else {
     cancelHref = "dept_contents.jsp?nodeId="+prevNode.getPK().getId()+"&parentNodeId="+prevNode.getParentNode().getPK().getId()+"&catId="+prevNode;
   }
}

String attribF = "attribute from: ";
String valF = " | value from: ";
String type="";
String attrib = "";

%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
    <form name="categoryDetailsForm" method="post" action="attributes.jsp" >
    <input type="hidden" name="action" value="<%=action%>">
<%  if (request.getParameter("parentNodeId")!=null && cm.getPK()==null) {    %>
      <input type="hidden" name="parentNodeId" value="<%=request.getParameter("parentNodeId")%>">
<%  }
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
	<tr><td colspan="2" class="breadcrumb"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
	<span class="path">Current Path: <%@ include file="includes/breadcrumb.jspf"%> <%=cm.getPK()!=null ? cm.getFullName() : "<b>Adding new category</b>"%></span>
    </td>
	</tr>
	<tr>
	<td valign="bottom">
		<table cellpadding="3" cellspacing="0" border="0">
		<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
<%      if (cm.getPK()!=null) { %>
		  <td class="tab_off"><a href="<%= servletContext %>/web_store/hierarchy/cat_contents.jsp?catId=<%=catId%>" class="tab">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</a></td>
<%      } else { %>
		  <td class="tab_off">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</td>
<%      } %>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="tab_c"><font class="tab_c">&nbsp;&nbsp;ATTRIBUTES&nbsp;&nbsp;</font></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    if (cm.getPK()!=null ){        %>
         <td class="tab_off"><a href="/StoreAdmin/web_store/hierarchy/attributes.jsp?action=create&parentNodeId=<%=cm.getPK().getId()%>" class="tab">&nbsp;ADD CATEGORY&nbsp;</a></td>
<%    } else {  %>
         <td class="tab_off">&nbsp;ADD CATEGORY&nbsp;</a></td>
<%    }       %>
        </tr>
		</tr>
		</table>
	</td>
	<td align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center" valign="middle">
<%      if(cm.getPK()!=null) {	%>
		<td class="preview"><a href="javascript:preview('/category.jsp?catId=<%=cm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
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
	<td colspan="2" class="tab_c">
		<table width="100%" cellpadding="2">
		<tr>
		<td align="right" class="tabDetails">Last Modified: <%=cm.getLastModified()!=null ? modByFormat.format(cm.getLastModified()) : ""%> By: <%=cm.getLastModifiedBy()%></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>

	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section" bgcolor="#FDFCEA">
	<tr><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('properties'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader" colspan="4">View/Edit Folder Properties</th></tr>
	<tr><td colspan="5"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Category ID</td>
	<td width="45%"><input name="categoryId" type="text" style="width:250px;" size="25" class="textbox2" value="<%= cm.getContentName() %>"></td>
	<td width="20%">&nbsp;</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Full Name</td>
	<td ><input name="fullName" type="text" style="width:250px;" size="25" class="textbox2" value="<%= cm.getFullName() %>"></td>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr>
	</table>

<div style="position:relative; width:100%; height:77%; overflow-y:scroll; padding-right:0px;">
	<div id="properties" style="display:none; background-color:#FDFCEA;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
			<td width="2%">&nbsp;&nbsp;&nbsp;</td>
			<td width="30%">CONTENT_NODE_ID</td>
			<td width="45%"><input name="CONTENT_NODE_ID" readonly type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.getPK()!=null ? cm.getPK().getId() : "")%>"></td>
			<td width="20%"></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>ALT_TEXT</td>
			<td><input name="ALT_TEXT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= cm.getAltText() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>BLURB</td>
			<td><input name="BLURB" type="text" style="width:250px;" class="textbox2" size="25" value="<%= cm.getBlurb() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>BLURB_TITLE</td>
			<td><input name="BLURB_TITLE" type="text" style="width:250px;" class="textbox2" size="25" value="<%= cm.getBlurbTitle() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>EDITORIAL_TITLE</td>
			<td><input name="EDITORIAL_TITLE" type="text" style="width:250px;" class="textbox2" size="25" value="<%= cm.getEditorialTitle() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SIDE_NAV_BOLD</td>
			<td><select name="SIDE_NAV_BOLD" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.getSideNavBold()) {%>selected<%}%>>TRUE</option>
			<option <% if (!cm.getSideNavBold()) {%>selected<%}%>>FALSE</option>
			</select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SIDE_NAV_LINK</td>
			<td><select name="SIDE_NAV_LINK" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.getSideNavLink()) {%>selected<%}%>>TRUE</option>
			<option <% if (!cm.getSideNavLink()) {%>selected<%}%>>FALSE</option></select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SIDE_NAV_PRIORITY</td>
			<td><input name="SIDE_NAV_PRIORITY" type="text" style="width:40px;" size="4" class="textbox2" value="<%= cm.getSideNavPriority() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SIDE_NAV_SHOWCHILDREN</td>
			<td><% EnumShowChildrenType cmSideNavShowChildren = cm.getSideNavShowChildren(); %>
	                <select name="SIDE_NAV_SHOWCHILDREN" class="pulldown2">
					<option></option>
	                <option <%= (cmSideNavShowChildren!=null && cmSideNavShowChildren.equals(EnumShowChildrenType.ALWAYS_FOLDERS))?"selected":""  %> value="<%= EnumShowChildrenType.ALWAYS_FOLDERS.getId() %>"><%= EnumShowChildrenType.ALWAYS_FOLDERS.getName() %></option>
	                <option <%= (cmSideNavShowChildren!=null && cmSideNavShowChildren.equals(EnumShowChildrenType.BROWSE_PATH))?"selected":""  %> value="<%= EnumShowChildrenType.BROWSE_PATH.getId() %>"><%= EnumShowChildrenType.BROWSE_PATH.getName() %></option>
	                <option <%= (cmSideNavShowChildren!=null && cmSideNavShowChildren.equals(EnumShowChildrenType.ALWAYS))?"selected":""  %> value="<%= EnumShowChildrenType.ALWAYS.getId() %>"><%= EnumShowChildrenType.ALWAYS.getName() %></option>
	                <option <%= (cmSideNavShowChildren!=null && cmSideNavShowChildren.equals(EnumShowChildrenType.NEVER))?"selected":"" %> value="<%= EnumShowChildrenType.NEVER.getId() %>"><%= EnumShowChildrenType.NEVER.getName() %></option>
	                </select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SIDE_NAV_SHOWSELF</td>
			<td><select name="SIDE_NAV_SHOWSELF" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.getSideNavShowSelf()) {%>selected<%}%>>TRUE</option><option <% if (!cm.getSideNavShowSelf()) {%>selected<%}%>>FALSE</option></select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SHOW_SELF</td>
			<td><select name="SHOW_SELF" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.getShowSelf()) {%>selected<%}%>>TRUE</option><option <% if (!cm.getShowSelf()) {%>selected<%}%>>FALSE</option></select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SHOW_CHILDREN</td>
			<td><% EnumShowChildrenType cmShowChildren = cm.getShowChildren(); %>
	                <select name="SHOW_CHILDREN" class="pulldown2">
					<option></option>
	                <option <%= (cmShowChildren!=null && cmShowChildren.equals(EnumShowChildrenType.ALWAYS_FOLDERS))?"selected":""  %> value="<%= EnumShowChildrenType.ALWAYS_FOLDERS.getId() %>"><%= EnumShowChildrenType.ALWAYS_FOLDERS.getName() %></option>
	                <option <%= (cmShowChildren!=null && cmShowChildren.equals(EnumShowChildrenType.BROWSE_PATH))?"selected":""  %> value="<%= EnumShowChildrenType.BROWSE_PATH.getId() %>"><%= EnumShowChildrenType.BROWSE_PATH.getName() %></option>
	                <option <%= (cmShowChildren!=null && cmShowChildren.equals(EnumShowChildrenType.ALWAYS))?"selected":""  %> value="<%= EnumShowChildrenType.ALWAYS.getId() %>"><%= EnumShowChildrenType.ALWAYS.getName() %></option>
	                <option <%= (cmShowChildren!=null && cmShowChildren.equals(EnumShowChildrenType.NEVER))?"selected":""  %> value="<%= EnumShowChildrenType.NEVER.getId() %>"><%= EnumShowChildrenType.NEVER.getName() %></option>
	                </select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>PRIORITY</td>
			<td><input name="PRIORITY" type="text" style="width:40px;" class="textbox2" size="4" value="<%= cm.getPriority() %>"></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>FEATURED</td>
			<td><select name="FEATURED" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.isFeatured()) {%>selected<%}%>>TRUE</option><option <% if (!cm.isFeatured()) {%>selected<%}%>>FALSE</option></select></td>
			<td></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>SECONDARY CATEGORY</td>
			<td><select name="SECONDARY_CATEGORY" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.isSecondaryCategory()) {%>selected<%}%>>TRUE</option><option <% if (!cm.isSecondaryCategory()) {%>selected<%}%>>FALSE</option></select></td>
			<td></td>
		</tr>
		</table>
		</div>
	
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
		<tr bgcolor="#CCCC99"><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('attributes'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader">View/Edit Folder Attributes</th></tr>
		</table>

	<div id="attributes" style="display: show; background-color:#FDFCEA;"><%-- background-color: #FFFFFF; --%>
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
			<td width="30%"><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=BANNERS','l');">BANNERS</a></td>
			<td width="45%">
	                <%
	                Banner oneBanner = null;
	                Image bannerImg=null;
	                String bannerURL = null;
	                ContentNodeModel bannerSubject = null;
	                String contentLink = null;
	                if (cm.hasAttribute("XBANNERS")) {
	                        Attribute thisBanner = cm.getAttribute("BANNERS");
	                        if (thisBanner != null) {
	                            oneBanner = (Banner)thisBanner.getValue() ;
	                            if (oneBanner.getMedia()!=null) {
	                               bannerImg = (Image)oneBanner.getMedia();
	                            }
	
	                            if (oneBanner.isSubjectALink() && oneBanner.getContentLink()!=null) {
	                                contentLink = oneBanner.getContentLink();
	                                if (contentLink.toLowerCase().indexOf("javascript")!=-1) {
	                                    bannerURL = contentLink;
	                                } else {
	                                    bannerURL = response.encodeURL(contentLink);
	                                }
	                            } else {
	                                ContentRef  contRef = (ContentRef )oneBanner.getSubject();
	                                if ((contRef instanceof CategoryRef)   && !contRef.getRefName().equals(cm.getContentName())) {
	                                     bannerURL = response.encodeURL("/category.jsp?catId="+contRef.getRefName());
	                                } else if ( contRef instanceof ProductRef )  {
	                                        if (!((ProductRef)contRef).lookupProduct().isUnavailable()) {
	                                        bannerURL = response.encodeURL("/product.jsp?catId=" + contRef.getRefName() + "&productId=" + contRef.getRefName2());
	                                        }
	                                                } else if(contRef instanceof DepartmentRef) {
	                                                     bannerURL = response.encodeURL("/department.jsp?deptId=" + contRef.getRefName());
	                                }
	                            }
	                        }
	                        %>
	                        <i class="notice">Image:</i> <a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>BANNERS&<%=type%>img','l')"><%= (bannerImg.getPath()).substring((bannerImg.getPath()).lastIndexOf("/")+1)  %></a><br><i class="notice">Target:</i> <a href="javascript:popup('pop_view_edit_banner.jsp?<%=param%>&<%=attrib%>BANNERS','s')"><%= bannerURL %></a>
	                <%
	                        } %>
	                </td>
			<td width="20%"><% if (cm.hasAttribute("BANNERS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("BANNERS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FEATURED_PRODUCTS','l');">FEATURED_PRODUCTS</a></td>
			<td>
	                <% if (cm.hasAttribute("FEATURED_PRODUCTS")) {
	                        List thisRef = (List)cm.getAttribute("FEATURED_PRODUCTS").getValue();
	                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
	                                ProductRef ref = (ProductRef)refList.next();
	                 %><%= ref.getProductName() %><%= refList.hasNext()?",":"" %>
	                <%    }
	                        } %></td>
			<td><% if (cm.hasAttribute("FEATURED_PRODUCTS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("FEATURED_PRODUCTS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FEATURED_BRANDS&brandNodes=true','l');">FEATURED_BRANDS</a></td>
			<td>
	                <% if (cm.hasAttribute("FEATURED_BRANDS")) {
	                        List thisRef = (List)cm.getAttribute("FEATURED_BRANDS").getValue();
	                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
	                                BrandRef ref = (BrandRef)refList.next();
	                 %><%= ref.getBrandName() %><%= refList.hasNext()?",":"" %>
	                <%    }
	                  } %></td>
			<td><% if (cm.hasAttribute("FEATURED_BRANDS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("FEATURED_BRANDS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
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
			<td width="2%">&nbsp;&nbsp;&nbsp;</td>
			<td width="30%">FAVORITE_ALL_SHOW_PRICE</td>
			<td width="45%"><select name="FAVORITE_ALL_SHOW_PRICE" style="width:80px;" class="pulldown1">
			<option></option>
			<option <% if (cm.getAttribute("FAVORITE_ALL_SHOW_PRICE",false)) {%>selected<%}%>>TRUE</option>
			<option <% if (!cm.getAttribute("FAVORITE_ALL_SHOW_PRICE",false)) {%>selected<%}%>>FALSE</option></select></td>
			<td width="20%"><% if (cm.hasAttribute("FAVORITE_ALL_SHOW_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("FAVORITE_ALL_SHOW_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>LAYOUT</td>
			<td><select name="LAYOUT"><OPTION></OPTION>
	        <%  int layoutValue=cm.getAttribute("LAYOUT",-1);
	    		for(Iterator itrTypes =EnumLayoutType.getLayoutTypes().iterator(); itrTypes.hasNext();){
	    			EnumLayoutType layoutType=(EnumLayoutType)itrTypes.next();
	    	%>
	    			<option <%=layoutType.getId()==layoutValue ? "selected" : ""%> value="<%=layoutType.getId()%>"><%=layoutType.getName()%></option>;
	<%    		} %></select>
	        </td>
			<td><% if (cm.hasAttribute("LAYOUT")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("LAYOUT")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>TEMPLATE_TYPE</td>
		<td><select name="TEMPLATE_TYPE"><OPTION></OPTION>
        <%  int tmplValue=cm.getAttribute("TEMPLATE_TYPE",-1);
		System.out.println("!! tmplValue: " + tmplValue);
    		for(Iterator itrTypes =EnumTemplateType.getTemplateTypes().iterator(); itrTypes.hasNext();){
    			EnumTemplateType tmplType=(EnumTemplateType)itrTypes.next();
    	%>
    			<option <%=tmplType.getId()==tmplValue ? "selected" : ""%> value="<%=tmplType.getId()%>"><%=tmplType.getName()%></option>;
<%    		} %></select>
        </td>
		<td><% if (cm.hasAttribute("TEMPLATE_TYPE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("TEMPLATE_TYPE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>LIST_AS</td>
		<td>
		<select name="LIST_AS" class="textbox2">
		<option value></option>
        <option value="full" <%= (cm.getAttribute("LIST_AS","").equalsIgnoreCase("full") ? "selected" : "") %>>Full Name</option>
        <option value="nav" <%= (cm.getAttribute("LIST_AS","").equalsIgnoreCase("nav") ? "selected" : "") %>>Nav Name</option>
        <option value="glance" <%= (cm.getAttribute("LIST_AS","").equalsIgnoreCase("glance") ? "selected" : "") %>>Glance Name</option>
        </select>
		</td>
		<td><% if (cm.hasAttribute("LIST_AS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("LIST_AS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>COLUMN_NUM</td>
		<td><input name="COLUMN_NUM" type="text" style="width:40px;" size="4" class="textbox2" value="<%= (cm.hasAttribute("COLUMN_NUM")) ? cm.getAttribute("COLUMN_NUM").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("COLUMN_NUM")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("COLUMN_NUM")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>COLUMN_SPAN</td>
		<td><input name="COLUMN_SPAN" type="text" style="width:40px;" size="4" class="textbox2" value="<%= (cm.hasAttribute("COLUMN_SPAN")) ? cm.getAttribute("COLUMN_SPAN").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("COLUMN_SPAN")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("COLUMN_SPAN")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>FAKE_ALL_FOLDER</td>
		<td><select name="FAKE_ALL_FOLDER" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("FAKE_ALL_FOLDER",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("FAKE_ALL_FOLDER",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (cm.hasAttribute("FAKE_ALL_FOLDER")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("FAKE_ALL_FOLDER")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>SHOW_SIDE_NAV</td>
		<td><select name="SHOW_SIDE_NAV" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("SHOW_SIDE_NAV",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("SHOW_SIDE_NAV",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (cm.hasAttribute("SHOW_SIDE_NAV")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("SHOW_SIDE_NAV")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>TREAT_AS_PRODUCT</td>
		<td><select name="TREAT_AS_PRODUCT" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("TREAT_AS_PRODUCT",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("TREAT_AS_PRODUCT",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (cm.hasAttribute("TREAT_AS_PRODUCT")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("TREAT_AS_PRODUCT")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=HOW_TO_COOK_IT_PRODUCTS','l');">HOW_TO_COOK_IT_PRODUCTS</a></td>
		<td><% if (cm.hasAttribute("HOW_TO_COOK_IT_PRODUCTS")) {
                        List thisRef = (List)cm.getAttribute("HOW_TO_COOK_IT_PRODUCTS").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                              ProductRef ref = (ProductRef)refList.next();
               %><%= ref.getProductName() %><%= refList.hasNext()?",":"" %>
        <%              }
               } %></td>
		<td><% if (cm.hasAttribute("HOW_TO_COOK_IT_PRODUCTS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("HOW_TO_COOK_IT_PRODUCTS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=HOWTOCOOKIT_USAGE','l');">HOWTOCOOKIT_USAGE</a></td>
		<td>
                <% if (cm.hasAttribute("HOWTOCOOKIT_USAGE")) {
                        DomainRef thisRef = (DomainRef)cm.getAttribute("HOWTOCOOKIT_USAGE").getValue();
                                %><%= thisRef.getDomainName() %>
                 <% } %></td>
		<td><% if (cm.hasAttribute("HOWTOCOOKIT_USAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("HOWTOCOOKIT_USAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
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
	<div id="attributes3" style="display= none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">NOT_SEARCHABLE</td>
		<td width="45%"><select name="NOT_SEARCHABLE" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td width="20%"><% if (cm.hasAttribute("NOT_SEARCHABLE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("NOT_SEARCHABLE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>REDIRECT_URL</td>
		<td><input name="REDIRECT_URL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("REDIRECT_URL")) ? cm.getAttribute("REDIRECT_URL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("REDIRECT_URL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("REDIRECT_URL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=ALIAS','l');">ALIAS</a></td>
		<td>
                <% if (cm.hasAttribute("ALIAS")) {
                        CategoryRef thisRef = (CategoryRef)cm.getAttribute("ALIAS").getValue();
                                %><%= thisRef.getCategoryName() %>
                <% } %></td>
		<td><% if (cm.hasAttribute("ALIAS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("ALIAS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>HIDE_URL</td>
		<td><input name="HIDE_URL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("HIDE_URL")) ? cm.getAttribute("HIDE_URL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("HIDE_URL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("HIDE_URL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes4'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - IMAGES</td>
	</tr>
	</table>
	<div id="attributes4" style="display= none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CAT_LABEL','l');">CAT_LABEL</a></td>
		<td width="45%">
                <% if (cm.hasAttribute("CAT_LABEL")) {
                                String thisMedia = ((Image)cm.getAttribute("CAT_LABEL").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td width="20%"><% if (cm.hasAttribute("CAT_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CAT_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CAT_PHOTO','l');">CAT_PHOTO</a></td>
		<td>
                <% if (cm.hasAttribute("CAT_PHOTO")) {
                                String thisMedia = ((Image)cm.getAttribute("CAT_PHOTO").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (cm.hasAttribute("CAT_PHOTO")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CAT_PHOTO")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CAT_TITLE','l');">CAT_TITLE</a></td>
		<td>
                <% if (cm.hasAttribute("CAT_TITLE")) {
                                String thisMedia = ((Image)cm.getAttribute("CAT_TITLE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                 <% } %></td>
		<td><% if (cm.hasAttribute("CAT_TITLE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CAT_TITLE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes5'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">MEDIA - MIXED</td>
	</tr>
	</table>
	<div id="attributes5" style="display= none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=EDITORIAL','l');">EDITORIAL</a></td>
		<td width="45%">
                <% if (cm.hasAttribute("EDITORIAL")) {
                                String thisMedia = ((Html)cm.getAttribute("EDITORIAL").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td width="20%"><% if (cm.hasAttribute("CAT_TITLE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CAT_TITLE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FRESH_TIPS','l');">FRESH_TIPS</a></td>
		<td>
                <% if (cm.hasAttribute("FRESH_TIPS")) {
                        List thisMedia = (List)cm.getAttribute("FRESH_TIPS").getValue();
                        for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
			TitledMedia tm = ((TitledMedia)tmList.next());
                 %>
                <%= tm.getPath().substring(tm.getPath().lastIndexOf("/")+1) %><%= tmList.hasNext()?",":"" %>
                <% }
                 } %></td>
		<td><% if (cm.hasAttribute("FRESH_TIPS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("FRESH_TIPS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=ARTICLES','l');">ARTICLES</a></td>
		<td>
                <% if (cm.hasAttribute("ARTICLES")) {
                        List thisMedia = (List)cm.getAttribute("ARTICLES").getValue();
                        for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
			ArticleMedia am = ((ArticleMedia)tmList.next());
                 %>
                <%= am.getPath().substring(am.getPath().lastIndexOf("/")+1) %><%= tmList.hasNext()?",":"" %>
                <% }
                 } %></td>
		<td><% if (cm.hasAttribute("ARTICLES")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("ARTICLES")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CATEGORY_BOTTOM_MEDIA','l');">CATEGORY_BOTTOM_MEDIA</a></td>
		<td>
                <% if (cm.hasAttribute("CATEGORY_BOTTOM_MEDIA")) {
                        List thisMedia = (List)cm.getAttribute("CATEGORY_BOTTOM_MEDIA").getValue();
                        for (Iterator mediaList=thisMedia.iterator(); mediaList.hasNext();) {
                                Html media = (Html)mediaList.next();
                 %><%= media.getPath().substring(media.getPath().lastIndexOf("/")+1) %><%= mediaList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (cm.hasAttribute("CATEGORY_BOTTOM_MEDIA")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CATEGORY_BOTTOM_MEDIA")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CATEGORY_DETAIL_IMAGE','l');">CATEGORY_DETAIL_IMAGE</a></td>
		<td>
                <% if (cm.hasAttribute("CATEGORY_DETAIL_IMAGE")) {
                                String thisMedia = ((Image)cm.getAttribute("CATEGORY_DETAIL_IMAGE").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td><% if (cm.hasAttribute("CATEGORY_DETAIL_IMAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CATEGORY_DETAIL_IMAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=CATEGORY_TOP_MEDIA','l');">CATEGORY_TOP_MEDIA</a></td>
		<td>
                <% if (cm.hasAttribute("CATEGORY_TOP_MEDIA")) {
                        List thisMedia = (List)cm.getAttribute("CATEGORY_TOP_MEDIA").getValue();
                        for (Iterator mediaList=thisMedia.iterator(); mediaList.hasNext();) {
                                Html media = (Html)mediaList.next();
                 %><%= media.getPath().substring(media.getPath().lastIndexOf("/")+1) %><%= mediaList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (cm.hasAttribute("CATEGORY_TOP_MEDIA")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("CATEGORY_TOP_MEDIA")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		</table>
	</div>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes6'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">RATING</td>
	</tr>
	</table>
	<div id="attributes6" style="display= none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">RATING_BREAK_ON_SUBFOLDERS</td>
		<td width="45%"><select name="RATING_BREAK_ON_SUBFOLDERS" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("RATING_BREAK_ON_SUBFOLDERS",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("RATING_BREAK_ON_SUBFOLDERS",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td width="20%"><% if (cm.hasAttribute("RATING_BREAK_ON_SUBFOLDERS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING_BREAK_ON_SUBFOLDERS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RATING_CHECK_SUBFOLDERS</td>
		<td><select name="RATING_CHECK_SUBFOLDERS" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("RATING_CHECK_SUBFOLDERS",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("RATING_CHECK_SUBFOLDERS",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (cm.hasAttribute("RATING_CHECK_SUBFOLDERS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING_CHECK_SUBFOLDERS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RATING_PROD_NAME</td>
		<td><input name="RATING_PROD_NAME" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RATING_PROD_NAME")) ? cm.getAttribute("RATING_PROD_NAME").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RATING_PROD_NAME")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING_PROD_NAME")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>SEASON_TEXT</td>
		<td><input name="SEASON_TEXT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("SEASON_TEXT")) ? cm.getAttribute("SEASON_TEXT").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("SEASON_TEXT")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("SEASON_TEXT")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>PROD_PAGE_RATINGS</td>
		<td><input name="PROD_PAGE_RATINGS" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("PROD_PAGE_RATINGS")) ? cm.getAttribute("PROD_PAGE_RATINGS").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("PROD_PAGE_RATINGS")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("PROD_PAGE_RATINGS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RATING_GROUP_NAMES</td>
		<td><input name="RATING_GROUP_NAMES" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RATING_GROUP_NAMES")) ? cm.getAttribute("RATING_GROUP_NAMES").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RATING_GROUP_NAMES")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING_GROUP_NAMES")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_MORE_USAGE_LABEL</td>
		<td><input name="RG_MORE_USAGE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_MORE_USAGE_LABEL")) ? cm.getAttribute("RG_MORE_USAGE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_MORE_USAGE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_MORE_USAGE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_POP_USAGE_LABEL</td>
		<td><input name="RG_POP_USAGE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_POP_USAGE_LABEL")) ? cm.getAttribute("RG_POP_USAGE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_POP_USAGE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_POP_USAGE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_PRICE_LABEL</td>
		<td><input name="RG_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_PRICE_LABEL")) ? cm.getAttribute("RG_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_SIZE_PRICE_LABEL</td>
		<td><input name="RG_SIZE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_SIZE_PRICE_LABEL")) ? cm.getAttribute("RG_SIZE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_SIZE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_SIZE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_TASTE_PRICE_LABEL</td>
		<td><input name="RG_TASTE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_TASTE_PRICE_LABEL")) ? cm.getAttribute("RG_TASTE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_TASTE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_TASTE_TYPE_PRICE_LABEL</td>
		<td><input name="RG_TASTE_TYPE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_TASTE_TYPE_PRICE_LABEL")) ? cm.getAttribute("RG_TASTE_TYPE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_TASTE_TYPE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_TYPE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_TASTE_USE_PRICE_LABEL</td>
		<td><input name="RG_TASTE_USE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_TASTE_USE_PRICE_LABEL")) ? cm.getAttribute("RG_TASTE_USE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_TASTE_USE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_USE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_TEXTURE_PRICE_LABEL</td>
		<td><input name="RG_TEXTURE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_TEXTURE_PRICE_LABEL")) ? cm.getAttribute("RG_TEXTURE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_TEXTURE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TEXTURE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_TYPE_PRICE_LABEL</td>
		<td><input name="RG_TYPE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_TYPE_PRICE_LABEL")) ? cm.getAttribute("RG_TYPE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_TYPE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TYPE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_USAGE_LABEL</td>
		<td><input name="RG_USAGE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_USAGE_LABEL")) ? cm.getAttribute("RG_USAGE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_USAGE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_USAGE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>RG_USAGE_PRICE_LABEL</td>
		<td><input name="RG_USAGE_PRICE_LABEL" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (cm.hasAttribute("RG_USAGE_PRICE_LABEL")) ? cm.getAttribute("RG_USAGE_PRICE_LABEL").getValue() :"" %>"></td>
		<td><% if (cm.hasAttribute("RG_USAGE_PRICE_LABEL")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_USAGE_PRICE_LABEL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RELATED_PRODUCTS','l');">RATING</a></td>
		<td>
                <% if (cm.hasAttribute("RATING")) {
                        List thisRef = (List)cm.getAttribute("RATING").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %></a></td>
		<td><% if (cm.hasAttribute("RATING")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_assoc_prod.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RATING_HOME','l');">RATING_HOME</a></td>
		<td>
                <% if (cm.hasAttribute("RATING_HOME")) {
                        CategoryRef thisRef = (CategoryRef)cm.getAttribute("RATING_HOME").getValue();
                                %><%= thisRef.getCategoryName() %>
                <% } %></a></td>
		<td><% if (cm.hasAttribute("RATING_HOME")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RATING_HOME")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_MORE_USAGE','l');">RG_MORE_USAGE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_MORE_USAGE")) {
                        List thisRef = (List)cm.getAttribute("RG_MORE_USAGE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                } %></td>
		<td><% if (cm.hasAttribute("RG_MORE_USAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_MORE_USAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_POP_USAGE','l');">RG_POP_USAGE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_POP_USAGE")) {
                        List thisRef = (List)cm.getAttribute("RG_POP_USAGE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                } %></td>
		<td><% if (cm.hasAttribute("RG_POP_USAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_POP_USAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_SIZE_PRICE','l');">RG_SIZE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_SIZE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_SIZE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (cm.hasAttribute("RG_SIZE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_SIZE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_TASTE_PRICE','l');">RG_TASTE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_TASTE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_TASTE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (cm.hasAttribute("RG_TASTE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_TASTE_TYPE_PRICE','l');">RG_TASTE_TYPE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_TASTE_TYPE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_TASTE_TYPE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%       }
                 } %></td>
		<td><% if (cm.hasAttribute("RG_TASTE_TYPE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_TYPE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_TASTE_USE_PRICE','l');">RG_TASTE_USE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_TASTE_USE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_TASTE_USE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                 <%       }
                  } %></td>
		<td><% if (cm.hasAttribute("RG_TASTE_USE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TASTE_USE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_TEXTURE_PRICE','l');">RG_TEXTURE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_TEXTURE_PRICE")) {
                      List thisRef = (List)cm.getAttribute("RG_TEXTURE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                } %></td>
		<td><% if (cm.hasAttribute("RG_TEXTURE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TEXTURE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_TYPE_PRICE','l');">RG_TYPE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_TYPE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_TYPE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                } %></a></td>
		<td><% if (cm.hasAttribute("RG_TYPE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_TYPE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_USAGE','l');">RG_USAGE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_USAGE")) {
                        List thisRef = (List)cm.getAttribute("RG_USAGE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                } %></td>
		<td><% if (cm.hasAttribute("RG_USAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_USAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_domain.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=RG_USAGE_PRICE','l');">RG_USAGE_PRICE</a></td>
		<td>
                <% if (cm.hasAttribute("RG_USAGE_PRICE")) {
                        List thisRef = (List)cm.getAttribute("RG_USAGE_PRICE").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                DomainRef ref = (DomainRef)refList.next();
                 %><%= ref.getDomainName() %><%= refList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (cm.hasAttribute("RG_USAGE_PRICE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("RG_USAGE_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<%=rowSpace%>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>SHOW_RATING_RELATED_IMAGE</td>
		<td><select name="SHOW_RATING_RELATED_IMAGE" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (cm.getAttribute("SHOW_RATING_RELATED_IMAGE",false)) {%>selected<%}%>>TRUE</option><option <% if (!cm.getAttribute("SHOW_RATING_RELATED_IMAGE",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td><% if (cm.hasAttribute("SHOW_RATING_RELATED_IMAGE")){%><a href="#" title="<%=attribF%><%=cm.getAttributeHome("SHOW_RATING_RELATED_IMAGE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		</table>
	</div>

	</div> <%-- end scroll section --%>

	<%--div>
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" bgcolor="#CCCCCC"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
<%      if(cm.getPK()!=null) {	%>
		<td class="preview"><a href="javascript:preview('/category.jsp?catId=<%=cm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td class="button">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="cancel"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="save"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table><br>
	</td>
	</tr>
	</table>
	</div--%>

    </tmpl:put>

</tmpl:insert>
</sa:CategoryController>