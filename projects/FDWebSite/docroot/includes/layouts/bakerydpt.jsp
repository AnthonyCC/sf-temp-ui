<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%-- New bakery dept layout page --%>
<% %>

<display:InitLayout/>

<%
	int i = -1;
	CategoryModel[] tlcat = new CategoryModel[2];
	List<CategoryModel>[] catList = new ArrayList[2];

	for ( ContentNodeModel cn : sortedCollection ) {
		
		// skip if not category
		if ( ! (cn instanceof CategoryModel) )
			continue;
		
		if ( cn.getParentId().equals(departmentId) ) {
			// top-level category
			tlcat[++i] = (CategoryModel)cn;
			catList[i] = new ArrayList<CategoryModel>();
		} else if ( i >= 0 && i < 2 && catList[i] != null ) {
			// sub-category
			catList[i].add( (CategoryModel)cn );
		} 
		
	}
%>

<div id="bak_editorial_div" style="width: 550px; height: auto; display: block; overflow: hidden; position:relative; margin-bottom: 20px;">
	<%
	Html edtMed = currentFolder.getEditorial();
	if ( edtMed != null ) { %>	
		<fd:IncludeMedia name="<%= edtMed.getPath() %>"></fd:IncludeMedia>
	<% } %>
</div>

<div id="bak_media_div" style="width: 550px; height: 100%; display: block; overflow: hidden; position:relative; font-size: 0px;">
	
	<span id="bak_left_media" style="width: 324px; height: 100%; display: inline-block; overflow: hidden; font-size: 0px; vertical-align: top; border-right: 1px solid #cccccc; padding-right: 4px; float: left;">
		<%
		List<Html> topMed0 = tlcat[0].getTopMedia();
		if ( topMed0 != null && topMed0.size() > 0 ) { %>	
			<fd:IncludeMedia name="<%= topMed0.get(0).getPath() %>"></fd:IncludeMedia>
		<% } %>
	</span>
	
	<span id="bak_right_media" style="width: 216px; height: 100%; display: inline-block; overflow: hidden; font-size: 0px; vertical-align: top; position: relative; left: -1px; border-left: 1px solid #cccccc; padding-left: 4px; float: left;">
		<%
		List<Html> topMed1 = tlcat[1].getTopMedia();
		if ( topMed1 != null && topMed1.size() > 0 ) { %>	
			<fd:IncludeMedia name="<%= topMed1.get(0).getPath() %>"></fd:IncludeMedia>
		<% } %>
	</span>
	
</div>

<div id="bak_main_div" style="width: 550px; height: 100%; display: block; overflow: hidden; position:relative; font-size: 0px;">
	
	<span id="bak_left_column" style="width: 324px; display: inline-block; overflow: hidden; font-size: 0px; vertical-align: top; border:0px solid none; border-right: 1px solid #cccccc; padding-right: 4px; float: left;">
		<display:ContentNodeIterator id="cn_it_0" trackingCode="<%= trackingCode %>" itemsToShow="<%= catList[0] %>">
			<span class="text12" style="display: inline-block; overflow: hidden; vertical-align: bottom; padding-top:10px; width: 108px; height: 100px; float: left;">
				<display:CategoryImage category="<%= (CategoryModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:CategoryName category="<%= (CategoryModel)currentItem %>" action="<%= actionUrl %>" style="font-style:normal"/>
			</span>
		</display:ContentNodeIterator>
	</span>
	
	<span id="bak_right_column" style="width: 216px; display: inline-block; overflow: hidden; font-size: 0px; vertical-align: top; position: relative; left: -1px;  border:0px solid none; border-left: 1px solid #cccccc; padding-left: 4px; float: left;">
		<display:ContentNodeIterator id="cn_it_1" trackingCode="<%= trackingCode %>" itemsToShow="<%= catList[1] %>">
			<span class="text12" style="display: inline-block; overflow: hidden; vertical-align: bottom; padding-top:10px; width: 108px; height: 100px; float: left;">
				<display:CategoryImage category="<%= (CategoryModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:CategoryName category="<%= (CategoryModel)currentItem %>" action="<%= actionUrl %>" style="font-style:normal"/>
			</span>
		</display:ContentNodeIterator>
	</span>
	
</div>

