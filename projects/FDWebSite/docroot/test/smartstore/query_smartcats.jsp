<!doctype html>
<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.content.Recommender"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.Recipe"%>
<%@page import="com.freshdirect.fdstore.content.YmalSet"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.AttributeDefI"%>
<%@page import="com.freshdirect.cms.ITable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd'%>
<%
	final String nodeKey = "CmsReport:smartCatRecs";

	if ( CmsManager.getInstance().getContentNode( ContentKey.decode( nodeKey ) ) == null ) {
		response.sendRedirect("test_notavail.jsp");
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Smart categories report</title>
		<style type="text/css">
			.imgdiv div { margin: 0px !important; }
		</style>
	</head>
	<body>
		<%
		
			ITable attribute = (ITable)ContentKey.decode( nodeKey ).getContentNode().getAttributeValue( "results" );
			
			AttributeDefI[] columnDefs = attribute.getColumnDefinitions();
			List<ITable.Row> rows = attribute.getRows();
			
			String trkC = "test-page";			
		%>

		<div style="font-size: large; text-transform: uppercase; font-weight: bold; margin: 5px;">Smart Categories</div>
		<div style="font-size: small; text-transform: lowercase; margin: 5px;"><a href="/cms-gwt/CsvExport?nodeKey=<%= nodeKey %>&attributeKey=results">Download in csv</a></div>		
		<div style="font-size: medium; margin: 5px;">
			Smart categories are categories with its products dynamically calculated.<br/>
			A scripted recommender is assigned for the category which will provide its contents.<br/>
			<br/>
			This page lists all smart-categories in the system.<br/>
			Use the <i>'test page'</i> link to view recommender details and smart category contents.<br/>
		</div>
		
		<table style="border: 1px solid; border-collapse: collapse;">
			<tr style="border: 1px solid; border-collapse: collapse;">
				<td style="border: 1px solid; border-collapse: collapse;"><%= columnDefs[0].getLabel() %></td>
				<td style="border: 1px solid; border-collapse: collapse;"><%= columnDefs[1].getLabel() %></td>
			</tr>
			<% for( ITable.Row row : rows ) { 
				Object[] values = row.getValues(); 
				%>
				<tr style="border: 1px solid; border-collapse: collapse;">
					<% for( Object o : values ) { 
						ContentKey key = ContentKey.decode( (String)o );
						ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey( key );
						%>
						<td style="border: 1px solid; border-collapse: collapse; padding: 5px; vertical-align: bottom;">						
							<% if( node instanceof CategoryModel ) { %>
								<div class="imgdiv"><display:CategoryImage category="<%= (CategoryModel)node %>"/></div>
								<display:CategoryName category="<%= (CategoryModel)node %>" action="<%= FDURLUtil.getCategoryURI((CategoryModel)node,trkC) %>" />
								<div class="imgdiv" style="font-size: small; text-transform: lowercase;"><a href="/test/smartstore/smart_categories.jsp?catId=<%= key.getId() %>">View test page</a></div>								
							<% } else if( node instanceof ProductModel ) { %>
								<div class="imgdiv"><display:ProductImage product="<%= (ProductModel)node %>"/></div>
								<display:ProductName product="<%= (ProductModel)node %>" action="<%= FDURLUtil.getProductURI((ProductModel)node,trkC) %>" />
							<% } else if( node instanceof DepartmentModel ) { %>	
								<div class="imgdiv"><fd:IncludeImage image="<%= ((DepartmentModel)node).getPhoto() %>"/></div>
								<span><a href="<%= FDURLUtil.getDepartmentURI(node.getContentName(),trkC) %>"><%= node.getFullName() %></a></span>				
							<% } else if( node instanceof Recommender ) { %>
								<span><%= ((Recommender)node).getDescription() %></span>
							<% } else if( node instanceof Recipe ) { %>
								<div class="imgdiv"><fd:IncludeImage image="<%= ((Recipe)node).getPhoto() %>"/></div>
								<span><a href="<%="/recipe.jsp?recipeId=" + node.getContentName() + "&trk=" + trkC %>"><%= ((Recipe)node).getName() %></a></span>
							<% } %>
							<div style="color: gray;"><%= key.getEncoded() %></div>
						</td>
					<% } %>				
				</tr>
			<% } %>
		</table>
		
	</body>
</html>