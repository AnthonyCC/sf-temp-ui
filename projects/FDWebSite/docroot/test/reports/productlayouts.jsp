<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='logic' prefix='logic'%>
<!DOCTYPE hmtl>
<html>
<head>
  <title>Product layout test page</title>
  <%@ include file="/test/reports/includes/groupedlayouts.jspf" %></head>
<body>

	<h1>Product Layouts</h1>

	<fd:ProductLayoutCollector>
	  <ul id="categoryList">
	    <c:forEach var="entry" items="${groupedResult}">
	    <li>
			<c:set var="layout" value="${entry.key}"/>
	        <a class="layoutlink" href="#<c:out value="${layout.id}"/>">
    	      <c:out value="${layout.name}" /> (<c:out value="${layout.id}" />)
            </a>
	
	      <div id="<c:out value="${layout.id}"/>" class="categories">
	        <h2><c:out value="${layout.name} (${layout.id})"/></h2>
	        <ul>
	          <c:forEach var="product" items="${entry.value}">
            <li class="<c:out value="${product.parentNode}"/>">

				<c:set var="compGroupEditorial" value="${false}"/>
				<c:forEach var="compGroupMod" items="${product.componentGroups}">
					<c:if test="${compGroupMod.editorial!=null}">
						<c:set var="compGroupEditorial" value="${true}"/>
					</c:if>
				</c:forEach>

<%--
 	          				product.productDescription!=null || 
							compGroupEditorial ||
							product.newWineRegion[0]!=null
							
--%>
				
           		<c:if test="${
						product.productTerms!=null ||
						product.partallyFrozen!=null ||
						product.productDescriptionNote!=null ||
						product.productQualityNote!=null ||
						product.productBottomMedia!=null ||
						product.wineReview1!=null ||
						product.wineReview2!=null ||
						product.wineReview3!=null
						
					}">
         			<span style="color:#FF8000">Possible editorial found!</span>
          			-- 
          		</c:if>
           		<strong>Product: 
	           		<a href="<c:url value="/product.jsp">
	           					<c:param name="catId" value="${product.category}"/>
	           					<c:param name="productId" value="${product}"/>
	           				</c:url>">
	           				<c:out value="${product}"/> (<c:out value="${product.fullName}"/>)
	           		</a>
	           	</strong>

				[  
           		<c:if test="${
						product.parentNode.middleMedia!=null ||
						product.parentNode.bottomMedia!=null ||
						product.parentNode.editorial!=null
					}">
         			<span style="color:#FF8000">Possible editorial found!</span> -- 
          		</c:if>
				Parent: <c:out value="${product.parentNode}"/>]
	          	</li>
	          </c:forEach>
	        </ul>
	      </div>
	    </li>
	    </c:forEach>
	  </ul>
	</fd:ProductLayoutCollector>	
  <script>
    (function () {    
      var $categoryDivs = $("div.categories");
      $("a.layoutlink").bind("click", function (e) { 
        var $this = $(this);
        $categoryDivs.hide();
        $this.next().show();
      });
      $("ul li ul li").bind("mouseover", function (e) {
        var className = this.className;
        $("." + className).css("background-color", "#cca");
      }).bind("mouseout", function (e) {
        var className = this.className;
        $("." + className).css("background-color", "inherit");
      });
    })();
  </script>  
</body>
</html>
