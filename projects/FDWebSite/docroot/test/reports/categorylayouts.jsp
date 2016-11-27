<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='logic' prefix='logic'%>
<!DOCTYPE html>
<html>
<head>
  <title>Category/layout test page</title>
  <%@ include file="/test/reports/includes/groupedlayouts.jspf" %>
</head>
<body>

	<h1>Category Layouts</h1>

	<fd:CategoryLayoutCollector />

  <ul id="categoryList">
    <c:forEach var="entry" items="${groupedResult}">
    <li>
      <c:choose>
        <c:when test="${entry.key==null}">
        <c:set var="categoryId" value="null"/>
          <a class="layoutlink" href="#null">
            <c:out value="Layout is null" />
          </a>
        </c:when>
        <c:otherwise>
        <c:set var="categoryId" value="${entry.key}"/>
          <a class="layoutlink" href="#<c:out value="${entry.key}"/>">
            Layout: <c:out value="${entry.key}" /> ID: <c:out value="${entry.key.id}" />
          </a>
        </c:otherwise>
      </c:choose>

      <div id="<c:out value="${categoryId}"/>" class="categories">
        <h2><c:out value="${categoryId}"/></h2>
        <ul>
          <c:forEach var="container" items="${entry.value}">
          <li>
          <c:if test="${container.editorial!=null || container.mediaContent!=null || 
          			   (container.contentKey.type.name=='Department' && container.departmentMiddleMedia!=null) || 
          			   (container.contentKey.type.name=='Department' && container.assocEditorial!=null) || 
          			   (container.contentKey.type.name=='Category' && container.separatorMedia!=null) ||
          			   (container.contentKey.type.name=='Category' && container.alternateContent!=null)}">
          	<span style="color:#FF8000">
          		Possible editorial found!
          	</span>
          	-- 
          	</c:if>
          <c:choose>        	
          	<c:when test="${container.contentKey.type.name=='Category'}">
            	Category: <strong><a href="<c:url value="/category.jsp"><c:param name="catId" value="${container}"/></c:url>"><c:out value="${container}"/></a></strong><c:out value=" (${container.fullName}) "/>
            	Parent: <strong><c:out value="${container.parentId}"/></strong><c:out value=" (${container.parentNode.fullName})"/>
          	</c:when>
          	<c:otherwise>
          		Department: <strong><a href="<c:url value="/department.jsp"><c:param name="deptId" value="${container}"/></c:url>"><c:out value="${container}"/></a></strong><c:out value=" (${container.fullName}) "/>
          	</c:otherwise>
          	</c:choose>
          </li>
          </c:forEach>
        </ul>
      </div>
    </li>
    </c:forEach>
  </ul>
<script>
  (function () {
    var $categoryDivs = $("div.categories");
    $("a.layoutlink").bind("click", function (e) { 
      var $this = $(this);
      $categoryDivs.hide();
      $this.next().show();
    });
  })();
</script>  
</body>
</html>
