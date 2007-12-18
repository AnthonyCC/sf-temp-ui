<%@ page import='com.freshdirect.fdstore.*, com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*, com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%

String servletContext = request.getContextPath();

//String referrer = request.getHeader("referer");
//System.out.println(">>>>>>>>referrer "+referrer);

String deptId = request.getParameter("deptId");
String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");

boolean isDept = deptId!=null;
boolean isProd = prodId!=null;

boolean isCat = false;
        if (catId!=null && prodId == null) {
                isCat = true;
        } 

String prop = request.getParameter("prop");
String attrib = request.getParameter("attrib");

boolean isProp = prop!=null;
boolean isAttrib = attrib!=null;

ContentFactory contentFactory = ContentFactory.getInstance();

ContentNodeModel cnm = null;
DepartmentModel dm = null;
CategoryModel cm = null;
ProductModel pm = null;

if (isDept) { 
        cnm = (ContentNodeModel)contentFactory.getContentNodeByName(deptId);
        dm = (DepartmentModel)cnm;
} else if (isCat) {
        cnm = (ContentNodeModel)contentFactory.getContentNodeByName(catId);
        cm = (CategoryModel)cnm;
} else if (isProd) {
        cnm = (ContentNodeModel)contentFactory.getProductByName(catId,prodId);
        pm =  (ProductModel)contentFactory.getProductByName(catId,prodId);
} 

/* if (isDept) {  
        dm = (DepartmentModel)contentFactory.getContentNodeByName(deptId);
} else if (isCat) {
        cm = (CategoryModel)contentFactory.getContentNodeByName(catId);
} else if (isProd) {
        pm =  (ProductModel)contentFactory.getProductByName(catId,prodId);
} */

//cnm = (ContentNodeModel)contentFactory.getContentNodeByName(id);

String dir = "";
String type = "";
String option = ""; //tree option text

boolean isImg = request.getParameter("type").equalsIgnoreCase("img");
	if (isImg){
                dir = "media";
		type = "Image";
		option = "click to preview";
	}

boolean isTxt = request.getParameter("type").equalsIgnoreCase("txt");
	if (isTxt){
                dir = "media";
		type = "Text";
		option = "click to preview";
	}
	
boolean isCol = request.getParameter("type").equalsIgnoreCase("col");
	if (isCol){
                dir = "product";
		type = "Collection";
		option = "use ctrl to select multiple";
	}
	
boolean isDom = request.getParameter("type").equalsIgnoreCase("dom");
	if (isDom){
                dir = "domains";
		type = "Domains";
		option = "use ctrl to select multiple";
	}
	
%>

<%
boolean preview = false;
String state = "";
if (isImg || isTxt ) {
	state = request.getParameter("state");
	if ("preview".equalsIgnoreCase(state)) {
		preview = true;
	}	
}
%>

<link rel="stylesheet" href="<%= servletContext %>/common/css/store_admin.css" type="text/css">
<tmpl:insert template='/common/template/pop_rightnav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - View/Edit <%= type %></tmpl:put>
<tmpl:put name='header' direct='true'>View/Edit <%= type %></tmpl:put>
<tmpl:put name='navHeader' direct='true'><b>BROWSE <%= dir.toUpperCase() %></b><br>(<%= option %>)</tmpl:put>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" class="add_move"><tr><td class="button"><a href="#" class="button">&nbsp;&lt;&mdash; add&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>

<tr><td width="30%"><b><%= isProd?"Product":"Folder"%> ID</b></td><td rowspan="3">&nbsp;&nbsp;&nbsp;&nbsp;</td><td width="70%"><%= cnm.getContentName() %></td>
</tr>
<tr><td><b><%= isProd?"Product Full Name":"Folder Display Label" %></b></td><td><%= isProd?(pm.getFullName()):(cnm.getFullName()) %></td>
</tr>
<tr><td><b><%= isProp?"Property":"Atribute" %></b></td><td><%= isProp?prop:attrib %></td>
</tr>

<tr><td><b>Value From</b></td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><%= isProd?(pm.getAttributeHome(isProp?prop:attrib)):(cnm.getAttributeHome(isProp?prop:attrib)) %></td>
</tr>

<% if (!isTxt) { %>
<tr><td><b>Last Modified</b></td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>8:00pm 12/01/02&nbsp;&nbsp;&nbsp;&nbsp;<b>By</b>&nbsp;XA</td>
</tr>
<% } %>

<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
<tr><td colspan="3" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
</table>

<%-- START CONTENT --%>
<%-- IMG / TXT --%>
<% if (isImg || isTxt ) { %>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td colspan="3">
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="pop">
<% if (preview) {%>
<tr>
<td class="popHeader"><a href="#">CURRENT</a> | <b>PREVIEW</b></td>
<td align="right">
	<table cellpadding="1" cellspacing="3" border="0">
	<tr align="center" valign="middle">
	<td class="cancel"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td class="save"><a href="#" class="button">&nbsp;SAVE AS NEW&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	</table>
</td>
</tr>
<% } else { %>
<tr>
<td class="popHeader"><b>CURRENT</b></td>
<td align="right">
	<table cellpadding="1" cellspacing="3" border="0">
	<tr align="center" valign="middle">
	<td class="REMOVE"><a href="javascript:swapImage('media_image','<%= servletContext %>/images/clear.gif')" class="button">&nbsp;REMOVE&nbsp;</a></td>
	</tr>
	</table>
</td>
</tr>
<% } %>
</table>
</td></tr>
<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>

<% if (isImg) {
Image thisImage = null;
%>
<tr><td colspan="3" class="contrast" align="center" height="40"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
<% if (cnm.hasAttribute(attrib)) {
        if ("BANNERS".equalsIgnoreCase(attrib)) {
                Banner oneBanner = null;
                Attribute thisBanner = cnm.getAttribute(attrib);
                        if (thisBanner != null)
                            oneBanner = (Banner)thisBanner.getValue() ;
                            if (oneBanner.getMedia()!=null)
                               thisImage = (Image)oneBanner.getMedia();
        } else {
        thisImage = (Image)cnm.getAttribute(attrib).getValue();
        }
 %>
<img src="<%=thisImage.getPath()%>" width="<%=thisImage.getWidth()%>" height="<%=thisImage.getHeight()%>" name="media_image"><%} else {%><i>no <%= attrib %> available</i><%}%>
<br><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="12"></td></tr>
<tr><td width="30%"><b>Path</b></td><td rowspan="4">&nbsp;&nbsp;&nbsp;&nbsp;</td><td width="70%"><%= cnm.hasAttribute(attrib)?thisImage.getPath():"--"%></td>
<tr>
<td><b>Dimensions</b></td>
<td><%= cnm.hasAttribute(attrib)?thisImage.getWidth():0 %> w x <%= cnm.hasAttribute(attrib)?thisImage.getHeight():0 %> h</td>
</tr>
<tr>
<td><b>Size</b></td>
<td>11.2 KB</td>
</tr>
<tr>
<td><b>Loaded</b></td>
<td>10/14/02 9:35pm</td>
</tr>

<% } else if (isTxt) { 
String pathToMedia = "";
MediaI thisFile = null;
%>
<tr><td colspan="3" class="contrast" align="center" height="40"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
<% if (cnm.hasAttribute(attrib)) {
        if (cnm.getAttribute(attrib).getValue() instanceof List) {
                List thisMedia = (List)cnm.getAttribute(attrib).getValue();
                        for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
		                Html media = ((Html)tmList.next());
                                pathToMedia = media.getPath();
                        }
         } else {
        thisFile = (MediaI)cnm.getAttribute(attrib).getValue();
        pathToMedia = thisFile.getPath();
        }
%>
<textarea name="textfield" style="width:360px;" cols="20" rows="8" wrap="VIRTUAL" class="textbox1"><fd:IncludeMedia name="<%=pathToMedia%>" /></textarea><%} else {%><i>no text available</i><%}%>
<br><img src="<%= servletContext %>/images/clear.gif" width="1" height="6"></td></tr>
<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="12"></td></tr>
<tr><td width="30%"><b>Path</b></td><td rowspan="3">&nbsp;&nbsp;&nbsp;&nbsp;</td><td width="70%"><%=pathToMedia%></td>
<%--tr>
<td><b>Word Count</b></td>
<td>60</td>
</tr--%>
<tr>
<td><b>Modified</b></td>
<td>10/14/02 9:35pm&nbsp;&nbsp;&nbsp;&nbsp;<b>By</b>&nbsp;XA</td>
</tr>
<% } %>

<tr><td colspan="3" align="center""><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
</table>
<% } %>

<%-- COL / DOM --%>
<% if (isCol || isDom ) { %>
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="pop">
<tr>
<td colspan="2">
	<table cellpadding="3">
	<tr valign="top">
	<td class="menu"><a href="javascript:deleteThis('product','#');"><font size="4">&ndash;</font> <b>delete</b></a></td>
	</tr>
	</table>
</td>
<td colspan="2" align="right">
	<table cellpadding="1" cellspacing="3" border="0">
	<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
	<tr align="center" valign="middle">
	<td class="cancel"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td class="save"><a href="#" class="button">&nbsp;SAVE CHANGES&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	</table>
</td>
</tr>
<% if ("ARTICLES".equalsIgnoreCase(attrib)) { %>
<tr>
<td width="2%">&nbsp;&nbsp;</td>
<td width="80%" colspan="2"><b>Article</b></td>
<td width="18%"><b>Priority</b></td>
</tr>
<%} else {%>
<tr>
<td width="2%">&nbsp;&nbsp;</td>
<td width="30%"><b>ID</b></td>
<td width="50%"><b><% if (isCol) {%>Full <%}%>Name</b></td>
<td width="18%"><b>Priority</b></td>
</tr>
<%}%>
<% if (isCol) {%>
<% if (cnm.hasAttribute(attrib)) {
        if ("ARTICLES".equalsIgnoreCase(attrib)) {
                List thisMedia = (List)cnm.getAttribute(attrib).getValue();
                Object im = null;
                for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
                        MediaI mi = ((MediaI)tmList.next());
                 %>
<tr>
<td><input type="checkbox" name="checkbox" value="checkbox"></td>
<td colspan="2"><%= mi.getPath().substring(mi.getPath().lastIndexOf("/")+1) %></td>
<td><input type="text" size="4" class="textbox1"></td>
</tr>
<%                      }
        } else if ((cnm.getAttribute(attrib).getValue()) instanceof List) {
                        Object currentMedia = null;
                        List thisMedia = (List)cnm.getAttribute(attrib).getValue();
                        for (Iterator tmList=thisMedia.iterator(); tmList.hasNext();) {
                                currentMedia = tmList.next();
                                if (currentMedia instanceof CategoryRef) {
                                CategoryRef thisRef = ((CategoryRef)currentMedia);
                                %>
                                <tr>
                                <td><input type="checkbox" name="checkbox" value="checkbox"></td>
                                <td><%= thisRef.getCategory() %></td>
                                <td><%= thisRef.getCategoryName() %></td>
                                <td><input type="text" size="4" class="textbox1"></td>
                                </tr>
                                <%
                                } else {
                                ProductRef thisRef = ((ProductRef)currentMedia);
                        %>
                                <tr>
                                <td><input type="checkbox" name="checkbox" value="checkbox"></td>
                                <td><%= thisRef.getCategory() %></td>
                                <td><%= thisRef.getCategoryName() %></td>
                                <td><input type="text" size="4" class="textbox1"></td>
                                </tr>
<%                              }
                        }
        } else if ((cnm.getAttribute(attrib).getValue()) instanceof CategoryRef) {
                CategoryRef thisRef = (CategoryRef)cnm.getAttribute(attrib).getValue();
%>
<tr>
<td><input type="checkbox" name="checkbox" value="checkbox"></td>
<td><%= thisRef.getCategory() %></td>
<td><%= thisRef.getCategoryName() %></td>
<td><input type="text" size="4" class="textbox1"></td>
</tr>
<%
        } else if ((cnm.getAttribute(attrib).getValue()) instanceof List) {
                        List thisRef = (List)cnm.getAttribute(attrib).getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
                                Object ref = refList.next();
                                if (ref instanceof CategoryRef) {
                                        cm = (CategoryModel)contentFactory.getContentNodeByName(((CategoryRef)ref).getCategoryName());
                                %>
<tr>
<td><input type="checkbox" name="checkbox" value="checkbox"></td>
<td><%= ((CategoryRef)ref).getCategoryName() %></td>
<td><%= cm.getFullName() %></td>
<td><input type="text" size="4" class="textbox1"></td>
</tr>
<% } else if ((cnm.getAttribute(attrib).getValue()) instanceof ProductRef){
        pm = (ProductModel)contentFactory.getProductByName(((ProductRef)ref).getCategoryName(),((ProductRef)ref).getProductName());
%>
<tr>
<td><input type="checkbox" name="checkbox" value="checkbox"></td>
<td><%= ((ProductRef)ref).getProductName() %></td>
<td><%= pm.getFullName() %></td>
<td><input type="text" size="4" class="textbox1"></td>
</tr>                      
<% } %>
<%  }
                    } 
     } %>                                
<% } %>

<% if (isDom) {%>
<% if (cnm.hasAttribute(attrib)) {
                        List thisRef = (List)cnm.getAttribute(attrib).getValue();
                        for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
			        DomainRef ref = ((DomainRef)refList.next());
                %>
<tr>
<td><input type="checkbox" name="checkbox" value="checkbox"></td>
<td> <%= ref.getDomain().getPK().getId() %></td>
<td> <%= ref.getDomainName() %></td>
<td><input type="text" size="4" class="textbox1"></td>
</tr>
                <%      } 
     } %>
<% } %>

<tr><td colspan="4" align="right">
	<table cellpadding="1" cellspacing="3" border="0">
	<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
	<tr align="center" valign="middle">
	<td class="cancel"><a href="#" class="button">&nbsp;CANCEL&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td class="save"><a href="#" class="button">&nbsp;SAVE CHANGES&nbsp;</a></td>
	<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	</table></td></tr>

<% } %>

</table>
</tmpl:put>

</tmpl:insert>

