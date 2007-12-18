<%@ page import='com.freshdirect.fdstore.*, com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*, com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(servletContext.length(), pageURI.lastIndexOf("/")+1);

String deptId = request.getParameter("deptId");
String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");

boolean isDept = deptId!=null;
boolean isProd = prodId!=null;

boolean isCat = false;
        if (catId!=null && prodId == null) {
                isCat = true;
        } 

String attrib = request.getParameter("attrib");

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

boolean isInternal = false;
%>
<%
                Banner oneBanner = null;
                Image bannerImg=null;
                String bannerURL = null;
                ContentNodeModel bannerSubject = null;
                String contentLink = null;

                        Attribute thisBanner = cnm.getAttribute("BANNERS");
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
                                isInternal = true;
                                if ((contRef instanceof CategoryRef)   && !contRef.getRefName().equals(cnm.getContentName())) {
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
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - View/Edit Banner Target</tmpl:put>
<tmpl:put name='header' direct='true'>View/Edit Banner Target</tmpl:put>
<tmpl:put name='direction' direct='true'>For</tmpl:put>
<tmpl:put name='path' direct='true'><b><%= (bannerImg.getPath()).substring((bannerImg.getPath()).lastIndexOf("/")+1)  %></b></tmpl:put><%-- banner img path --%>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="#" class="button">&nbsp;> save <&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<div>
<table width="100%" cellpadding="0" cellspacing="4" border="0" class="pop">
<tr><td width="3%"><input type="radio" name="radiobutton" value="radiobutton" <%= !isInternal?"checked":""%>></td><td width="7%">url: </td><td width="90%"><textarea style="width:250px;" class="textbox1" size="25" rows="2" wrap="VIRTUAL"><%= !isInternal?(bannerURL):"---" %></textarea></td></tr>
<tr><td colspan="4" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
<tr><td><input type="radio" name="radiobutton" value="radiobutton" <%= isInternal?"checked":""%>></td><td>hierarchy: </td><td><b class="notice"><%= isInternal?(bannerURL):"" %></b></td></tr>
</table>
</div>
<%
	String tree = currentDir + "includes/tree.jsp";
%>
<div style="float:right;width:98%;height:30%;overflow-y:scroll;" class="textbox1">
<jsp:include page='<%=tree%>'/>
</div>
</tmpl:put>
</tmpl:insert>