<!DOCTYPE html>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@ page import="java.io.*" %>
<html>
<head>
<title>Layout Browser</title>
    <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
</head>
<body>
<%!
String printCat(JspWriter out, String deptStr, String catStr, CategoryModel category, int curDepth) {
	catStr += "<span data-cat="+category+" data-depth="+curDepth+" data-layout="+category.getLayoutType(-1)+">"+category + "(" + category.getLayoutType(-1) + ")</span>";
	printOut(out, deptStr + catStr);
	
	if (!category.getSubcategories().isEmpty()) {
		if (curDepth < 100) { /* just to prevent infinite loops if the cats are setup poorly */
			curDepth++;
			printOut(out, "<br class=\"depth\" />");
			for (CategoryModel subcat : category.getSubcategories()) {
				printCat(out, deptStr, catStr+" > ", subcat, curDepth);
			}
		}
	} else {
		printOut(out, "<br class=\"depth\" />");
	}

	return "";
}
void printOut (JspWriter out, String str) {
	try{
		out.println(str);
	} catch(Exception eek) {
		System.out.print(eek.getStackTrace());
	}
}
%>
<style>
	.highlighted {
		background-color: #ee4;
	}
	.dept-cont {
		font-family: monospace;
	}
	.dept-cont :first-child {
		background-color: #4ee;
		font-size: 1.2em;
	}
</style>
<div class="">
	Highlight Layout #: <input id="highlighter_text" type="text" style="text-align: center; width: 100px;" /><button id="highlighter">Highlight</button><button id="highlighter_clear">Clear</button>
</div>
<hr /><hr />
<%
	Set<ContentKey> deptKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.DEPARTMENT);
	int depth = 0;
	String deptStr = "";
	String catStr = "";
	for (ContentKey deptKey : deptKeys) {
		DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNodeByKey(deptKey);
		if ("Archive".equalsIgnoreCase(department.toString())) {
			//System.out.println("---MATCHED----"+department.toString());
			continue;
		} else {
			//System.out.println("-----------------"+department.toString());
		}
		if (department != null) {
			printOut(out, "<div class=\"dept-cont\">");
				deptStr = "<span data-dept="+department.toString()+" data-layout="+department.getLayoutType(-1)+">"+department.toString()+"("+department.getLayoutType(-1)+") > </span>";
				printOut(out, deptStr+"<br />");
				
	
				for (CategoryModel category : department.getSubcategories()) {
					printCat(out, deptStr, "", category, 1);
				}

			printOut(out, "</div>");
		}
		%><hr /><%
	}
%>
<script>
	$jq(document).ready(function() {
		$jq('#highlighter_clear').on('click', function(event) {
			$jq('.highlighted').removeClass('highlighted');
		});
		$jq('#highlighter').on('click', function(event) {
			$jq('#highlighter_clear').click();
			$jq('[data-layout="'+$jq('#highlighter_text').val()+'"]').addClass('highlighted');
		});
	});
</script>
</body>
</html>
