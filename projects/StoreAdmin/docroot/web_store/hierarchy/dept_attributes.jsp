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
String saveHref = "javascript:departmentDetailsForm.submit();";

%>

<sa:DepartmentController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='dm'>

<%
if (dm.getPK()!=null) {
   action = "update";
}else {
    action = "create";
}

String cancelHref;
ContentNodeModel prevNode = null;
if (dm.getPK() !=null) {
   cancelHref = "dept_attributes.jsp?nodeId="+dm.getPK().getId()
                    +"&parentNodeId="+dm.getParentNode().getPK().getId()
                    +"&catId="+dm.getParentNode().getContentName();
}else {
   prevNode = ContentFactory.getInstance().getContentNode(request.getParameter("parentNodeId"));
   cancelHref = "index.jsp" ;
}

boolean isDept = false ; //deptId!=null;
boolean isCat = true; // catId!=null;

String add = "ADD";
String notSet = "<font class='notice'>Not set</font>";
String attribF = "attribute from: ";
String valF = " | value from: ";
String type="";
String attrib = "";

%>
<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
    <form name="departmentDetailsForm" method="post" action="dept_attributes.jsp" >
    <input type="hidden" name="action" value="<%=action%>">
<%  if (request.getParameter("parentNodeId")!=null && dm.getPK()==null) {    %>
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
	<span class="path">Current Path: <%@ include file="includes/breadcrumb.jspf"%> <%=dm.getPK()!=null ? dm.getFullName() : "<b>Adding new Department</b>"%></span>
    </td>
	</tr>
	<tr>
	<td valign="bottom">
		<table cellpadding="3" cellspacing="0" border="0">
		<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
<%      if (dm.getPK()!=null) { %>
		  <td class="tab_off"><a href="<%= servletContext %>/web_store/hierarchy/dept_contents.jsp?catId=<%=dm.getPK().getId()%>" class="tab">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</a></td>
<%      } else { %>
		  <td class="tab_off">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</td>
<%      } %>
		<td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="tab_d"><font class="tab_d">&nbsp;&nbsp;ATTRIBUTES&nbsp;&nbsp;</font></td>
		<td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    if (dm.getPK()!=null ){        %>
         <td class="tab_off"><a href="/StoreAdmin/web_store/hierarchy/attributes.jsp?action=create&parentNodeId=<%=dm.getPK().getId()%>" class="tab">&nbsp;ADD CATEGORY&nbsp;</a></td>
<%    } else {  %>
		 <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    }       %>
        </tr>
		</table>
	</td>
	<td align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center" valign="middle">
<%      if(dm.getPK()!=null) {	%>
		<td class="preview"><a href="javascript:preview('/department.jsp?deptId=<%=dm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td class="preview">&nbsp;PREVIEW&nbsp;</td>
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
	<td colspan="2" class="tab_d">
		<table width="100%" cellpadding="2">
		<tr>
		<td align="right" class="tabDetails">Last Modified: <%=dm.getLastModified()!=null ? modByFormat.format(dm.getLastModified()) : ""%> By: <%=dm.getLastModifiedBy()%></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>

	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section" bgcolor="#FDFCEA">
	<tr bgcolor="#000000"><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('properties'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader" colspan="4">View/Edit Folder Properties</th></tr>
	<tr><td colspan="5"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Department ID</td>
	<td width="45%"><input name="departmentId" type="text" style="width:250px;" size="25" class="textbox2" value="<%= dm.getContentName() %>"></td>
	<td width="20%">&nbsp;</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Full Name</td>
	<td ><input name="fullName" type="text" style="width:250px;" size="25" class="textbox2" value="<%= dm.getFullName() %>"></td>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	</tr>
	</table>

	<div style="position:relative;width:100%;height:77%;overflow-y:scroll;">
	<div id="properties" style="display: none; background-color:#FDFCEA;">
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">CONTENT_NODE_ID</td>
		<td width="45%"><input readonly type="text" style="width:250px;" size="25" class="textbox2" value="<%= (dm.getPK()!=null ? dm.getPK().getId() : "")%>"></td>
		<td width="20%"></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>ALT_TEXT</td>
		<td><input name="ALT_TEXT" type="text" style="width:250px;" size="25" class="textbox2" value="<%= dm.getAltText() %>"></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>BLURB</td>
		<td><input name="BLURB" type="text" style="width:250px;" class="textbox2" size="25" value="<%= dm.getBlurb() %>"></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>BLURB_TITLE</td>
		<td><input name="BLURB_TITLE" type="text" style="width:250px;" class="textbox2" size="25" value="<%= dm.getBlurbTitle() %>"></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>EDITORIAL_TITLE</td>
		<td><input name="EDITORIAL_TITLE" type="text" style="width:250px;" class="textbox2" size="25" value="<%= dm.getEditorialTitle() %>"></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>PRIORITY</td>
		<td><input name="PRIORITY" type="text" style="width:40px;" class="textbox2" size="4" value="<%= dm.getPriority() %>"></td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>MAX_ROW_COUNT</td>
		<td><input name="MAX_ROW_COUNT" type="text" style="width:40px;" class="textbox2" size="4" value="<%= dm.getMaxRowCount() %>"></td>
		<td></td>
	</tr>
	</table>
	</div>

	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
	<tr bgcolor="#000000"><th class="sectionHeader" width="2%"><a href="#" onclick="toggleDisplay('attributes'); return false" class="icon">&nbsp;&curren;&nbsp;</a></th><th class="sectionHeader">View/Edit Folder Attributes</th></tr>
	</table>

	<div id="attributes" style="display: show; background-color:#FDFCEA;"">
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%"><a href="#" onclick="toggleDisplay('attributes1'); return false" class="icon">&nbsp;&curren;&nbsp;</a></td>
	<td class="sectionSubHeader">PROMOS</td>
	</tr>
	</table>
	<div id="attributes1" style="display= none;">
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
                if (dm.hasAttribute("BANNERS")) {
                        Attribute thisBanner = dm.getAttribute("BANNERS");
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
                                if ((contRef instanceof CategoryRef)   && !contRef.getRefName().equals(dm.getContentName())) {
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
		<td width="20%"><% if (dm.hasAttribute("BANNERS")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("BANNERS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FEATURED_CATEGORIES','l');">FEATURED_CATEGORIES</a></td>
		<td>
                <% if (dm.hasAttribute("FEATURED_CATEGORIES")) {
                        List thisRef = (List)dm.getAttribute("FEATURED_CATEGORIES").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                CategoryRef ref = (CategoryRef)refList.next();
                 %><%= ref.getCategoryName() %><%= refList.hasNext()?",":"" %>
                <%    }
                        } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=FEATURED_PRODUCTS','l');">FEATURED_PRODUCTS</a></td>
		<td>
                <% if (dm.hasAttribute("FEATURED_PRODUCTS")) {
                        List thisRef = (List)dm.getAttribute("FEATURED_PRODUCTS").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                ProductRef ref = (ProductRef)refList.next();
                 %><%= ref.getProductName() %><%= refList.hasNext()?",":"" %>
                <%    }
                  } %></td>
		<td>&nbsp;</td>
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
		<td width="45%"><select name="select" style="width:80px;" class="pulldown1">
           <option <% if (dm.getAttribute("FAVORITE_ALL_SHOW_PRICE",false)) {%>selected<%}%>>TRUE</option>
           <option <% if (!dm.getAttribute("FAVORITE_ALL_SHOW_PRICE",false)) {%>selected<%}%>>FALSE</option>
           </select></td>
		<td width="20%"><% if (dm.hasAttribute("FAVORITE_ALL_SHOW_PRICE")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("FAVORITE_ALL_SHOW_PRICE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>LAYOUT</td>
		<td><select name="LAYOUT"><OPTION></OPTION>
        <%  int layoutValue=dm.getAttribute("LAYOUT",-1);
    		String selString ;
    		for(Iterator itrTypes =EnumLayoutType.getLayoutTypes().iterator(); itrTypes.hasNext();){
    			EnumLayoutType layoutType=(EnumLayoutType)itrTypes.next();
    			if (layoutType.getId()==layoutValue) {
    			   selString="selected";
   			   } else {
    			   selString="";
   			   } %>
    			<option <%=selString%> value="<%=layoutType.getId()%>"><%=layoutType.getName()%></option>;
<%    		} %></select>
        </td>
		<td><% if (dm.hasAttribute("LAYOUT")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("LAYOUT")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>TEMPLATE_TYPE</td>
		<td><select name="LAYOUT"><OPTION></OPTION>
        <%  int tmplValue=dm.getAttribute("TEMPLATE_TYPE",-1);
		System.out.println("!! tmplValue: " + tmplValue);
    		for(Iterator itrTypes =EnumTemplateType.getTemplateTypes().iterator(); itrTypes.hasNext();){
    			EnumTemplateType tmplType=(EnumTemplateType)itrTypes.next();
    	%>
    			<option <%=tmplType.getId()==tmplValue ? "selected" : ""%> value="<%=tmplType.getId()%>"><%=tmplType.getName()%></option>;
<%    		} %></select>
        </td>
		<td><% if (dm.hasAttribute("TEMPLATE_TYPE")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("TEMPLATE_TYPE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>LIST_AS</td>
		<td><select name="LIST_AS" class="textbox2">
		<option></option>
        <option value="full" <%= (dm.getAttribute("LIST_AS","").equalsIgnoreCase("full") ? "selected" : "") %>>Full Name</option>
        <option value="nav" <%= (dm.getAttribute("LIST_AS","").equalsIgnoreCase("nav") ? "selected" : "") %>>Nav Name</option>
        <option value="glance" <%= (dm.getAttribute("LIST_AS","").equalsIgnoreCase("glance") ? "selected" : "") %>>Glance Name</option>
        </select></td>
		<td><% if (dm.hasAttribute("LIST_AS")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("LIST_AS")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_contref_assoc.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_NAV','l');">DEPT_NAV</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_NAV")) {
                        List thisRef = (List)dm.getAttribute("DEPT_NAV").getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                CategoryRef ref = (CategoryRef)refList.next();
                 %><%= ref.getCategoryName() %><%= refList.hasNext()?",":"" %>
                <%    }
                  } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">RATING_CHECK_SUBFOLDERS</td>
		<td width="45%"><select name="RATING_CHECK_SUBFOLDERS" style="width:80px;" class="pulldown1">
		<option></option>
        <option <% if (dm.getAttribute("RATING_CHECK_SUBFOLDERS",false)) {%>selected<%}%>>TRUE</option>
        <option <% if (!dm.getAttribute("RATING_CHECK_SUBFOLDERS",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td width="20%">&nbsp;</td>
		</tr>
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td width="30%">RATING_BREAK_ON_SUBFOLDERS</td>
		<td width="45%"><select name="RATING_BREAK_ON_SUBFOLDERS" style="width:80px;" class="pulldown1">
		<option></option>
           <option <% if (dm.getAttribute("RATING_BREAK_ON_SUBFOLDERS",false)) {%>selected<%}%>>TRUE</option>
           <option <% if (!dm.getAttribute("RATING_BREAK_ON_SUBFOLDERS",false)) {%>selected<%}%>>FALSE</option>
          </select></td>
		<td width="20%">&nbsp;</td>
		</tr>
    	<tr>
    		<td>&nbsp;&nbsp;&nbsp;</td>
    		<td>BOTTOM_RIGHT_DISPLAY</td>
    		<td><input name="BOTTOM_RIGHT_DISPLAY" type="text" style="width:40px;" class="textbox2" size="4" value="<%= dm.getAttribute("BOTTOM_RIGHT_DISPLAY",0) %>"></td>
    		<td></td>
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
		<td width="45%"><select name="select" style="width:80px;" class="pulldown1">
		<option></option>
		<option <% if (dm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>TRUE</option>
		<option <% if (!dm.getAttribute("NOT_SEARCHABLE",false)) {%>selected<%}%>>FALSE</option></select></td>
		<td width="20%"><% if (dm.hasAttribute("NOT_SEARCHABLE")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("NOT_SEARCHABLE")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>REDIRECT_URL</td>
		<td><input name="" type="text" style="width:250px;" size="25" class="textbox2" value="<%= (dm.hasAttribute("REDIRECT_URL")) ? dm.getAttribute("REDIRECT_URL").getValue() :"" %>"></td>
		<td><% if (dm.hasAttribute("REDIRECT_URL")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("REDIRECT_URL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td>HIDE_URL</td>
		<td><input type="text" style="width:250px;" size="25" class="textbox2" value="<%= (dm.hasAttribute("HIDE_URL")) ? dm.getAttribute("HIDE_URL").getValue() :"" %>"></td>
		<td><% if (dm.hasAttribute("HIDE_URL")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("HIDE_URL")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
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
	<div id="attributes4" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_PHOTO','l');">DEPT_PHOTO</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_PHOTO") && dm.getPhoto()!=null) {
                                String thisMedia = dm.getPhoto().getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td>&nbsp;&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_PHOTO_SMALL','l');">DEPT_PHOTO_SMALL</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_PHOTO_SMALL") && dm.getPhotoSmall()!=null) {
                                String thisMedia = dm.getPhotoSmall().getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td>&nbsp;&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_TITLE','l');">DEPT_TITLE</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_TITLE") && dm.getTitleImage()!=null) {
                                String thisMedia = dm.getTitleImage().getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                 <% } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_MGR','l');">DEPT_MGR</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_MGR") && dm.getMgrPhoto()!=null) {
                                String thisMedia = dm.getMgrPhoto().getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                 <% } %></td>
		<td>&nbsp;</td>
		</tr>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_MGR_NONAME','l');">DEPT_MGR_NONAME</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_MGR_NONAME") ){
                                String thisMedia = dm.getMgrPhotoNoName().getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                 <% } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_MGR_BIO','l');">DEPT_MGR_BIO</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_MGR_BIO")) {
                                String thisMedia = ((MediaI)dm.getAttribute("DEPT_MGR_BIO").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                 <% } %></td>
		<td>&nbsp;</td>
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
	<div id="attributes5" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
		<tr>
		<td width="2%">&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=EDITORIAL','l');">EDITORIAL</a></td>
		<td width="45%"><a href="javascript:popup('pop_view_edit.jsp?<%=param%>&<%=attrib%>EDITORIAL&<%=type%>txt','l')">
                <% if (dm.hasAttribute("EDITORIAL")) {
                                String thisMedia = ((Html)dm.getAttribute("EDITORIAL").getValue()).getPath(); %>
                                        <%= thisMedia.substring(thisMedia.lastIndexOf("/")+1) %>
                <% } %></td>
		<td width="20%">&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPT_STORAGE_GUIDE_MEDIA','l');">DEPT_STORAGE_GUIDE_MEDIA</a></td>
		<td>
                <% if (dm.hasAttribute("DEPT_STORAGE_GUIDE_MEDIA")) {
                        MediaI thisMedia = (MediaI)dm.getAttribute("DEPT_STORAGE_GUIDE_MEDIA").getValue();
                 %><%= thisMedia.getPath().substring(thisMedia.getPath().lastIndexOf("/")+1) %>
                <%
                 } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=ASSOC_EDITORIAL','l');">ASSOC_EDITORIAL</a></td>
		<td>
                <% if (dm.hasAttribute("ASSOC_EDITORIAL")) {
                      List thisMedia = (List)dm.getAttribute("ASSOC_EDITORIAL").getValue();
                        for (Iterator mediaList=thisMedia.iterator(); mediaList.hasNext();) {
                                MediaI media = (MediaI)mediaList.next();
                 %><%= media.getPath().substring(media.getPath().lastIndexOf("/")+1) %><%= mediaList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td>&nbsp;</td>
		</tr>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript:popup('/StoreAdmin/pop_select_media.jsp?parentURI=<%=request.getRequestURI()%>&sessionName=<%=sessionName%>&attributeName=DEPARTMENT_BOTTOM','l');">DEPARTMENT_BOTTOM</a></td>
		<td>
                <% if (dm.hasAttribute("DEPARTMENT_BOTTOM")) {
                        List thisMedia = (List)dm.getAttribute("DEPARTMENT_BOTTOM").getValue();
                        for (Iterator mediaList=thisMedia.iterator(); mediaList.hasNext();) {
                                MediaI media = (Html)mediaList.next();
                 %><%= media.getPath().substring(media.getPath().lastIndexOf("/")+1) %><%= mediaList.hasNext()?",":"" %>
                <%    }
                 } %></td>
		<td><% if (dm.hasAttribute("CATEGORY_TOP_MEDIA")){%><a href="#" title="<%=attribF%><%=dm.getAttributeHome("CATEGORY_TOP_MEDIA")%><%=valF%>?method?" class="icon">&nbsp;<b>i</b>&nbsp;</a><%}%></td>
		</tr>
		</table>
	</div>
	</div> <%-- end scroll section --%>

	<div>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" bgcolor="#CCCCCC"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
<%      if(dm.getPK()!=null) {	%>
		<td class="preview"><a href="javascript:preview('/department.jsp?deptId=<%=dm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
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
	</div>

    </tmpl:put>

</tmpl:insert>
</sa:DepartmentController>