<%@page import="java.io.PrintWriter"%><%@
page import="java.util.ArrayList"%><%@
page import="java.util.Comparator"%><%@
page import="java.util.Collections"%><%@
page import="java.util.List"%><%@
page import="com.freshdirect.cms.ContentKey"%><%@
page import="com.freshdirect.cms.fdstore.FDContentTypes"%><%@
page import="com.freshdirect.cms.application.CmsManager"%><%@
page import="com.freshdirect.fdstore.content.CategoryModel"%><%@
page import="com.freshdirect.fdstore.content.ContentFactory"%><%@
page import="com.freshdirect.fdstore.content.DepartmentModel"%><%@
taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %><%@
taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@
taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %><%

	DepartmentModel d = null;
	if (request.getParameter("deptId") != null) {
		d = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, request.getParameter("deptId"));
	}

	// Phase #1 collect {c e Cats | showSelf(c) = FALSE}
	List<CategoryModel> cats = new ArrayList<CategoryModel>();
	for (ContentKey key : CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY)) {
		CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
		if (!cat.isOrphan() && !cat.isShowSelf() /* && !cat.getDepartment().isHidden() */) {
			// filter to department (if given)
			if (d != null && ! d.equals(cat.getDepartment())) {
				continue;
			}
			cats.add(cat);
		}
	}
	
	if (request.getParameter("output") != null && request.getParameter("output").length() > 0) {
		// TODO: produce CVS output
		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment;filename=report.csv");
        PrintWriter w = response.getWriter();

        w.append("#id;SHOWSELF\r\n");
        for (CategoryModel cat : cats) {
			w.append(cat.getContentKey().getEncoded()).append(";true\r\n");        	
        }

        w.flush();
        w.close();

		return;
	}

	Collections.sort(cats, new Comparator<CategoryModel>() {
		public int compare(CategoryModel o1, CategoryModel o2) {
			DepartmentModel d1 = o1.getDepartment();
			DepartmentModel d2 = o2.getDepartment();
			if (d1 == null)
				return -1;
			if (d2 == null)
				return 1;
			int d = d1.getFullName().compareTo(d2.getFullName());
			if (d != 0)
				return d;
			// return d == 0 ? o1.getFullName().compareTo(o2.getFullName()) : d;
			String s1 = o1.getFullName();
			String s2 = o2.getFullName();
			if (s1 == null)
				return -1;
			if (s2 == null)
				return 1;
			return s1.compareTo(s2);		
		}
	});
	
	request.setAttribute("cats", cats);	
%><!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>Categories having 'SHOWSELF' attribute set to false</title>
	<style>

	x-table {
		width:100%;
	}
	
	span.pager {
		padding: 5px 5px;
	}

	a.disc {
		color: #ccc;
	}
	</style>
</head>
<body>
	<div>
		<div>This report page lists categories having <i>SHOWSELF</i> attribute set to <i>false</i>value.
		The download links point to files containing categories with fixed values (ie. SHOWSELF=true).
		</div>
		<div>
			<span>Found <b>${fn:length(cats)}</b> categories</span> <a href="?output=csv">Download the whole list (CSV format)</a>
		</div>
	</div>

	<c:set var="curDept" value="${null}"/>

	<table>
	<tr>
		<th>Category ID</th>
		<th>Category Name</th>
	</tr>
	<c:forEach var="cat" items="${cats}">
	<c:choose>

		<c:when test="${curDept == null || !(curDept eq cat.department)}"><c:set var="curDept" value="${cat.department}"/>
	<tr>
		<td colspan="2">
			<h3><a href="/department.jsp?deptId=${curDept.contentKey.id}">${curDept.fullName}</a></h3>
			<div><c:if test="${curDept.hidden}"><b>hidden</b></c:if> (<a href="?output=csv&deptId=${curDept.contentKey.id}">Download sub-list in CSV</a>)</div>
		</td>
	</tr>
		</c:when>

		<c:otherwise>
	<tr>
		<td>${cat.contentKey.id}</td>
		<td>
		
			<c:choose>
				<c:when test="${cat.hidden}">
					<a href="/category.jsp?catId=${cat.contentKey.id}" class="disc">${cat.fullName != null ? cat.fullName : '(anonymous)'}</a>
				</c:when>
				<c:otherwise>
					<a href="/category.jsp?catId=${cat.contentKey.id}">${cat.fullName != null ? cat.fullName : '(anonymous)'}</a>
				</c:otherwise>
			</c:choose>
		
		</td>
	</tr>
		</c:otherwise>
	</c:choose>


	</c:forEach>
	</table>
</body>
</html>