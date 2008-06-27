<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%
Attribute attrib = null;
Set brands = null ; // set in the grocery_category_layout page. will be referenced by  i_bottom_template

String catId = request.getParameter("catId");
boolean isGroceryVirtual=false;

ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);

ProductModel prodModel = ContentFactory.getInstance().getProductByName(request.getParameter("prodCatId"), request.getParameter("productId")); 

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", prodModel == null ? currentFolder.getPath() : prodModel.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");
%>

<%
boolean noLeftNav = false;
String jspTemplate = null;
boolean showAlternateContent = false;
String alternateContentFile = null;
if (request.getParameter("groceryVirtual")!=null ) {
    isGroceryVirtual=true;
}
attrib=currentFolder.getAttribute("HIDE_URL");
if (!ContentFactory.getInstance().getPreviewMode()) {
    if (attrib!=null) {
        String redirectURL = response.encodeRedirectURL((String)attrib.getValue());
	   if (redirectURL.toUpperCase().indexOf("/CATEGORY.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
    }
}
attrib=currentFolder.getAttribute("REDIRECT_URL");
if (attrib!=null && !"nm".equalsIgnoreCase((String)attrib.getValue())  && !"".equals(attrib.getValue())) {
    String redirectURL = response.encodeRedirectURL((String)attrib.getValue());
	   if (redirectURL.toUpperCase().indexOf("/CATEGORY.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
}
attrib=currentFolder.getAttribute("CONTAINS_BEER");
FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
if(attrib != null && Boolean.TRUE.equals(attrib.getValue()) && !yser.isHealthWarningAcknowledged()){
	String redirectURL = "/health_warning.jsp?successPage=/category.jsp"+URLEncoder.encode("?"+request.getQueryString());
	response.sendRedirect(response.encodeRedirectURL(redirectURL));
}

attrib = currentFolder.getAttribute("SHOW_SIDE_NAV");
if (attrib!=null) {
    noLeftNav = !((Boolean)attrib.getValue()).booleanValue();
}
attrib = currentFolder.getAttribute("ALTERNATE_CONTENT");
if (attrib!=null) {
	showAlternateContent = true;
	alternateContentFile = ((MediaModel)attrib.getValue()).getPath();
}



int templateType=currentFolder.getAttribute("TEMPLATE_TYPE",1);
int layouttype = currentFolder.getAttribute("LAYOUT", -1);

if (noLeftNav && layouttype==EnumLayoutType.GROCERY_PRODUCT.getId()) noLeftNav= false;
//need to change the noLeftNav setting, if this is virtual grocery or the coffee_by region layout or grocery_category
//!!! Note: except for the virtual grocery folders, this should really be controlled by the show_Side_Nav attib, but
//    due to the painfull manual process of editing all the necessary folders, we've opted, temporarily, for this hack
if (!isGroceryVirtual && (layouttype==EnumLayoutType.COFFEE_BY_REGION.getId() || layouttype==EnumLayoutType.GROCERY_CATEGORY.getId() || layouttype==EnumLayoutType.THANKSGIVING_CATEGORY.getId())){
    noLeftNav=true;
} else if (isGroceryVirtual) noLeftNav=false;


// [APPREQ-77] Page uses include media type layout
boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_NO_NAV.getId()); // [APPREQ-77]

                                                                               
// Assign the correct template
if (isIncludeMediaLayout) {
	// [APPREQ-77] this special layout type needs 'naked' layout
	jspTemplate = "/common/template/no_nav.jsp";
	noLeftNav = false;
} else if (noLeftNav) {
    jspTemplate = "/common/template/right_dnav.jsp";
} else {
    if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
		// assuming only 1 wine store at a time
        jspTemplate = "/common/template/usq_sidenav.jsp";
    } else { //assuming the default (Generic) Template
        jspTemplate = "/common/template/both_dnav.jsp";
    }
}

%>
<tmpl:insert template='<%=jspTemplate%>'>
<%
        if (!noLeftNav) {
%>
<tmpl:put name='leftnav' direct='true'>
</tmpl:put>
<%
        }
%>

    <tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
// TODO duplicated -- boolean virtualGrocerySpecified = request.getParameter("groceryVirtual")!=null;
//if (layouttype==EnumLayoutType.FEATURED_ALL.getId()) layouttype=19;
boolean noCache =  (EnumLayoutType.GROCERY_PRODUCT.getId()==layouttype
                    || isGroceryVirtual /* virtualGrocerySpecified */
                    || EnumLayoutType.BULK_MEAT_PRODUCT.getId()==layouttype
                    || EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layouttype
                    || EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layouttype
                    || EnumLayoutType.THANKSGIVING_CATEGORY.getId()==layouttype
                    || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layouttype
                    || EnumLayoutType.HOLIDAY_MENU.getId()==layouttype
                    || EnumLayoutType.WINE_CATEGORY.getId()==layouttype
                    || EnumLayoutType.VALENTINES_CATEGORY.getId()==layouttype
                    || EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()==layouttype);
                    
%>

<oscache:cache key='<%= "catLayout_"+request.getQueryString() %>' time="300" refresh="<%= noCache %>">
<% try {

int tablewid = noLeftNav ? 550 : 400;

// Beginning of ifAlternateContent
if(alternateContentFile != null){
%><fd:IncludeMedia name='<%= alternateContentFile %>' /><%
} else {

	Attribute introCopyAttribute = currentFolder.getAttribute("EDITORIAL");
	String introCopy = introCopyAttribute==null?"":((Html)introCopyAttribute.getValue()).getPath();
            
	String introTitle = currentFolder.getEditorialTitle();
    
    // no other option wine trouble
    if(EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
      introCopy="";
      introTitle="";
    }
    
	boolean showLine=false;   // if true, the last gray line prior to the categories-display will be printed
	//  get the rating & ranking stuff
    Attribute tmpAttribute = currentFolder.getAttribute("RATING_GROUP_NAMES");
    StringBuffer rateNRankLinks = new StringBuffer();

    if (!isIncludeMediaLayout && EnumLayoutType.BULK_MEAT_PRODUCT.getId()!=layouttype
             && EnumLayoutType.VALENTINES_CATEGORY.getId()!=layouttype  && EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()!=layouttype ){ // don't paint intro stuff if we'll be using bulkMeat layout
	    if (tmpAttribute !=null) {
	        StringTokenizer stRRNames = new StringTokenizer((String)tmpAttribute.getValue(),",");
	        while (stRRNames.hasMoreTokens()) {
	            String rrName = stRRNames.nextToken().toUpperCase();
	            String ordrBy = "&orderBy=price";

	            // go find the attribute with that name and it's label
	            tmpAttribute = currentFolder.getAttribute(rrName);
	            if (tmpAttribute !=null) {
	                tmpAttribute = currentFolder.getAttribute(rrName);
	                List ra = (List)tmpAttribute.getValue();
	                if (ra.size() > 0) {
	                    Domain raDMV = ((DomainRef)ra.get(0)).getDomain();
	                    if (raDMV!=null) {
	                        ordrBy = "&orderBy="+raDMV.getName().toLowerCase();
	                    }
	                }
	            }
	            if (rateNRankLinks.length() > 1) rateNRankLinks.append("<td class=\"text11bold\">&nbsp;|&nbsp;</td>");
	            rateNRankLinks.append("<td class=\"text11bold\"><a href=\"");
	            rateNRankLinks.append(response.encodeURL("/rating_ranking.jsp?catId=" + currentFolder + "&ratingGroupName="+rrName+ordrBy));
	            rateNRankLinks.append("\"><b>");

	            // get the label for this rating group name.
	            tmpAttribute = currentFolder.getAttribute(rrName+"_LABEL");
	            if (tmpAttribute!=null) {
	                rateNRankLinks.append(((String)tmpAttribute.getValue())); //.toUpperCase());
	            } else {
	              rateNRankLinks.append((rrName.replace('_',' '))); //.toUpperCase());
	            }
	            rateNRankLinks.append("</b></a></td>");
	        }
	    }
%>
<% if (!noCache || EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layouttype
                || EnumLayoutType.HOLIDAY_MENU.getId()==layouttype  || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layouttype   ) { //not DFGS %>
<%-- start header stuff --%>
     <table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0">
<% if (FDStoreProperties.isAdServerEnabled()) { %>
	 <tr><td>
	 <SCRIPT LANGUAGE=JavaScript>
		<!--
		OAS_AD('CategoryNote');
		//-->
	</SCRIPT><br>
	 </td></tr>	 
	<%
}
	  if ( !"nm".equalsIgnoreCase(introTitle) && introTitle!=null && introTitle.trim().length() > 0) {
	        showLine=true;
	%>
	<tr><td align="center">
	<%      if(!introTitle.equals("")){%>
	<font class="title16"><%=introTitle%></font>
	<%      }
		    Attribute seasonTextAttrib = currentFolder.getAttribute("SEASON_TEXT");
		    if (seasonTextAttrib!=null) {
		%>
		<br><img src="/media_stat/images/layout/clear.gif" height="4" width="1"><br>
		<font class="text12orbold"><%=seasonTextAttrib.getValue()%></font>
	<%
		    }
%>
      </td></tr>
<%
	}
   if (layouttype==EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()){
               showLine=false;
      %>
     <tr><td><img src="/media_stat/images/layout/clear.gif" height="5" width="1"></td></tr>
	<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></td></tr>
     <% } %>
	<%
	    if (introCopy !=null && introCopy.trim().length()>0 && introCopy.indexOf("blank_file.txt") == -1 && !(layouttype==EnumLayoutType.GROCERY_CATEGORY.getId() && currentFolder.getAttribute("EDITORIAL")!=null && currentFolder.getAttribute("CAT_LABEL")!=null)) { //bypass beer
	        if (layouttype!=EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()) showLine=true;
	        if ( introCopy!=null && introCopy.trim().length() > 0 ) {
	%>
	<tr><td><img src="/media_stat/images/layout/clear.gif" height="5" width="1"><br><fd:IncludeMedia name='<%= introCopy %>'/><br><img src="/media_stat/images/layout/clear.gif" height="4" width="1"></td></tr>
	<%  }
	}
	%>
	<%
	    if (rateNRankLinks.length() > 0 ) {
	       showLine=true;
	%>
	<tr><td><img src="/media_stat/images/layout/clear.gif" height="7" width="1"></td></tr>
	<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></td></tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" height="4" width="1"></td></tr>
	<tr align="center"><td>
	<table cellpadding="0" cellspacing="0" border="0"><tr><td><img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*"><img src="/media_stat/images/layout/clear.gif" height="1" width="6"></td><td class="text11bold">Compare by:&nbsp;</td><%=rateNRankLinks%><td><img src="/media_stat/images/layout/clear.gif" height="1" width="6"><img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*"></td></tr></table></td></tr>
	<%
          }
	    if (showLine  && layouttype!=EnumLayoutType.HOLIDAY_MENU.getId() && layouttype!=EnumLayoutType.FEATURED_MENU.getId() ){
	%>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
	<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
	<%
		MultiAttribute brandtAttrib = (MultiAttribute)currentFolder.getAttribute("CATEGORY_TOP_MEDIA");

		if (brandtAttrib!=null) {
	%>
		<tr><td>
			<fd:IncludeMedia name="<%= ((Html)brandtAttrib.getValue(0)).getPath() %>" />
		</td></tr>
		<% } %>
	<%
	    } // end of if intro copy set to blank, do nothing
		//place the products in out temporary list..then assign it to the array itemsToDisplayArray
 %>
 </table>
 <%      if (rateNRankLinks.length() > 0 || ("hmr".equals(currentFolder.getParentNode().toString()) && currentFolder.getBlurb()==null )) {%><br><%}%>
<%  } else if (EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layouttype) {
 	//paint the category_detail_image, intro copy and full name
	attrib = currentFolder.getAttribute("CATEGORY_DETAIL_IMAGE");
	MediaModel catDetailImg = null;
	if (attrib!=null) {
		catDetailImg = (MediaModel)attrib.getValue();
	}
    
    Attribute editorialAttribute = currentFolder.getAttribute("EDITORIAL");
	String editorialApth = editorialAttribute==null?"":((Html)editorialAttribute.getValue()).getPath();
        
    
%>
    <table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center" colspan="3"><FONT CLASS="title18"><%=currentFolder.getFullName()%></font><br><br></td></tr>
	<tr valign="top"><td>
<%	if(catDetailImg!=null) { %>
		<img src="<%=catDetailImg.getPath()%>" width="<%=catDetailImg.getWidth()%>" height="<%=catDetailImg.getHeight()%>" border="0">
<%  } else { %><IMG src="/media_stat/images/layout/clear.gif" WIDTH="100" HEIGHT="1" border="0"><% } %>
	</td><td><IMG src="/media_stat/images/layout/clear.gif" WIDTH="5" HEIGHT="1" border="0"></td><td>
<%	if (editorialApth !=null) { %>
	  <fd:IncludeMedia name='<%= editorialApth %>'/></td>
<%	} else {%>&nbsp;<% } %>
	</td></tr>
	</table>
<% }
}%>

<%-- end header stuff --%>
		<% if ("hmr".equals(currentFolder.getParentNode().toString()) && currentFolder.getBlurb()!=null ){%><table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0"><tr><td>
<%=currentFolder.getBlurb()%><FONT CLASS="space4pix"><BR><BR></FONT></td></tr></table><br><%}%>
<%
if(EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) { %>
        <%@ include file="/includes/wine/i_wine_category.jspf" %>
<%    }  else  {  %>        
        <%@ include file="/common/template/includes/catLayoutManager.jspf" %>
<%      }     
    }
	/* Layout may have put a request attribute called brandsList, of type set...get it into the brands var */
  	if (request.getAttribute("brandsList")!=null) {
		brands = (Set)request.getAttribute("brandsList");
   	}
   	if (EnumLayoutType.BULK_MEAT_PRODUCT.getId() != layouttype
             && EnumLayoutType.VALENTINES_CATEGORY.getId()!=layouttype) { %>
		<%@ include file="/includes/i_bottom_template.jspf" %>
<% 	} %>

<% } catch (Exception ex) {
		ex.printStackTrace();
%>
	<oscache:usecached />
<% } %>
</oscache:cache>

</tmpl:put>
<%//@ include file="/includes/i_promotion_counter.jspf" %>
</tmpl:insert>
