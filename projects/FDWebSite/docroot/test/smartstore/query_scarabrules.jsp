<!doctype html>
<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.cms.AttributeDefI"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.ITable"%>
<%@page import="com.freshdirect.cms.EnumAttributeType"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.AttributeI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd'%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Scarab merchandising rules report</title>
		<style type="text/css">
			.imgdiv div { margin: 0px !important; }
		</style>
	</head>
	<body>
		<%
			String nodeKey = "CmsReport:scarabRules";
		
			ITable attribute = (ITable)ContentKey.decode( nodeKey ).getContentNode().getAttributeValue( "results" );
			
			AttributeDefI[] columnDefs = attribute.getColumnDefinitions();
			List<ITable.Row> rows = attribute.getRows();

			String trkC = "test-page";			

			List<ContentKey> listParentInclude = new ArrayList<ContentKey>();
			List<ContentKey> listParentExclude = new ArrayList<ContentKey>();
			List<ContentKey> listParentPromote = new ArrayList<ContentKey>();
			List<ContentKey> listParentDemote  = new ArrayList<ContentKey>();
			
			List<ContentKey> listChildInclude = new ArrayList<ContentKey>();
			List<ContentKey> listChildExclude = new ArrayList<ContentKey>();
			List<ContentKey> listChildPromote = new ArrayList<ContentKey>();
			List<ContentKey> listChildDemote  = new ArrayList<ContentKey>();
			
			for ( ITable.Row row : rows ) {
				Object[] values = row.getValues(); 
			
				String type = (String)values[1];
				if ( "SCARAB_YMAL_INCLUDE".equals( type ) ) {
					listParentInclude.add( ContentKey.decode( (String)values[0] ) );				
					listChildInclude.add( ContentKey.decode( (String)values[2] ) );				
				} else if ( "SCARAB_YMAL_EXCLUDE".equals( type ) ) {
					listParentExclude.add( ContentKey.decode( (String)values[0] ) );				
					listChildExclude.add( ContentKey.decode( (String)values[2] ) );				
				} else if ( "SCARAB_YMAL_PROMOTE".equals( type ) ) {
					listParentPromote.add( ContentKey.decode( (String)values[0] ) );				
					listChildPromote.add( ContentKey.decode( (String)values[2] ) );				
				} else if ( "SCARAB_YMAL_DEMOTE".equals( type ) ) {
					listParentDemote.add( ContentKey.decode( (String)values[0] ) );				
					listChildDemote.add( ContentKey.decode( (String)values[2] ) );				
				}			
			}
			
			String title, id;
			List<ContentKey> parentList, childList;
			
		%>

		<div style="font-size: large; text-transform: uppercase; font-weight: bold; margin: 5px;">Scarab Merchandising Rules</div>
		<div style="font-size: small; text-transform: lowercase; margin: 5px;"><a href="/cms-gwt/CsvExport?nodeKey=<%= nodeKey %>&attributeKey=results">Download in csv</a></div>		
		<div style="font-size: medium; margin: 5px;">
			Scarab merchandising rules control the scarab recommendations for a specific product or products in a category or department (first column).<br/>
			<br/>
			<u>There are four type of rules:</u><br/>
			<u>Include rules:</u> Only products. Adds the product to the top of the recommendations list, even if it was not in the original list.  This item will always be in the recommendations.<br/>
			<u>Exclude rules:</u> Products, categories or departments. Removes it from the recommendations list. This item will never appear in the recommendations. <br/>
			<u>Promote rules:</u> Products, categories or departments. Moves it to top of the recommendation list, if it was in the list. <br/>
			<u>Demote rules:</u> Products, categories or departments. Moves it to end of the recommendation list, if it was in the list.<br/>
			<br/>
			If categories or departments are assigned it affects all products that are in that category/department.<br/>
			If multiple rules would apply the precedence is (highest first) : include/exclude/promote/demote.<br/>
			<br/>
			<span style="font-size: small;">Click the section headers to open/close section.</span><br/>
		</div>
	
		<%
			id = "scarab_include";
			title = "Scarab Include rules";
			parentList = listParentInclude;
			childList = listChildInclude;			
		%>		
		<%@ include file="query_scarabrules_table.jspf" %>
 
		<%
			id = "scarab_exclude";
			title = "Scarab Exclude rules";
			parentList = listParentExclude;
			childList = listChildExclude;			
		%>		
		<%@ include file="query_scarabrules_table.jspf" %>		
	
		<%
			id = "scarab_promote";
			title = "Scarab Promote rules";
			parentList = listParentPromote;
			childList = listChildPromote;			
		%>		
		<%@ include file="query_scarabrules_table.jspf" %>		
	
		<%
			id = "scarab_demote";
			title = "Scarab Demote rules";
			parentList = listParentDemote;
			childList = listChildDemote;			
		%>		
		<%@ include file="query_scarabrules_table.jspf" %>		
					
	</body>
</html>