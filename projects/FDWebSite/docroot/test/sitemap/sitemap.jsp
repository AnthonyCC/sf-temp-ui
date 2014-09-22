<%@ page import="com.freshdirect.cms.ContentKey" %>
<%@ page import="com.freshdirect.cms.application.CmsManager" %>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.JspWriter" %>

<%!
private void displayDepartment(DepartmentModel dep, JspWriter out) throws java.io.IOException {
  Collection<CategoryModel> categories = dep.getCategories();

    out.print("<li class=\"department\" id="+dep.getContentName()+">"+dep.getFullName()+"<span class=\"contentId\">"+dep.getContentName()+"</span>");

  if (categories.size() > 0) {
    out.print("<span class=\"handle\"></span>");
    out.print("<ul>");
    for (CategoryModel cat: categories) {
      displayCategory(cat, out);
    }
    out.print("</ul>");
  }

  out.print("</li>");
}

private void displayCategory(CategoryModel cat, JspWriter out) throws java.io.IOException {
  Collection<ProductModel> products = ContentFactory.getInstance().getProducts(cat);
  int pSize = products.size();
  Collection<CategoryModel> categories = cat.getSubcategories();

  out.print("<li class=\"category\" id=\""+cat.getContentName()+"\" data-products=\""+pSize+"\">"+cat.getFullName()+"<span class=\"contentId\">"+cat.getContentName()+"</span>");

  if (categories.size() > 0) {
    out.print("<span class=\"handle\"></span>");
    out.print("<ul>");
    for (CategoryModel scat: categories) {
      displayCategory(scat, out);
    }
    out.print("</ul>");
  }

  out.print("</li>");
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Category sitemap</title>
  <style>
    body {
      background-color: #ccffee;
    }
    ul > li {
      background-color: rgba(0,0,0,.05);
    }
    ul {
      list-style-type: none;
    }
    li > ul {
      display: none;
    }
    li.selected > ul {
      display: block;
    }
    span.handle {
      display: inline-block;
      width: 20px;
      font-weight: bold;
      text-align: center;
      cursor: pointer;
    }
    span.handle:after {
      content: '>';
    }
    .selected > span.handle:after {
      content: 'V';
    }
    [data-sumproducts]:before {
      display: inline-block;
      content: attr(data-sumproducts);
      color: blue;
      margin-right: 10px;
      width: 100px;
    }
    [data-sumproducts="0"] {
      color: #888;
    }
    .noempty [data-sumproducts="0"] {
      display: none;
    }
    [data-sumproducts="0"]:before {
      content: '';
    }
    span.contentId {
      display: inline-block;
      margin-left: 1em;
      color: red;
    }
  </style>
</head>
<body>
  <h1># of products in departments/categories</h1>
  <div class="control">
    <button id="btn_open_all">open all</button>
    <button id="btn_close_all">close all</button>
    <button id="btn_toggle_empty">show/hide empty nodes</button>
  </div>
<%
out.print("<ul>");

StoreModel store = ContentFactory.getInstance().getStore();
Collection<DepartmentModel> departments = store.getDepartments();
for (DepartmentModel dep: departments){
  displayDepartment(dep, out);
}
out.print("</ul>");
%>
<script>
var nodes = [].slice.call(document.querySelectorAll('li'));
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("handle")) {
    e.stopPropagation();
    e.target.parentNode.classList.toggle("selected");
  }
});
document.getElementById("btn_open_all").addEventListener("click", function (e) {
  nodes.forEach(function (node) {
    node.classList.add("selected");
  }); 
});
document.getElementById("btn_close_all").addEventListener("click", function (e) {
  nodes.forEach(function (node) {
    node.classList.remove("selected");
  }); 
});
document.getElementById("btn_toggle_empty").addEventListener("click", function (e) {
  document.body.classList.toggle("noempty");
});
nodes.forEach(function (node) {
  var subnodes = [].slice.call(node.querySelectorAll('[data-products]')),
      products = +node.getAttribute('data-products'),
      sumproducts = subnodes.reduce(function (p, c) {
        return p + +c.getAttribute('data-products');
      }, 0);

  node.setAttribute('data-sumproducts', sumproducts + products);
});
</script>
</body>
</html>
