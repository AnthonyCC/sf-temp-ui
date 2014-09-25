<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.fdstore.content.ProductContainer"%>

<%!
public static class Data {
	String name;
	String id;
	int countAll;
	List<Data> children = new ArrayList<Data>();
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		sb.append("id:\"").append(id).append("\",\n");
		sb.append("name:\"").append(name == null ? "" : name.replace("\"", "\\\"")).append("\",\n");;
		sb.append("countAll:").append(countAll).append(",\n");;
		sb.append("children: [\n");
		boolean first = true;
		for (Data childData : children){
			if (first){
				first = false;
			} else { 
				sb.append(",");
			}
			
			sb.append(childData);
		}
		sb.append("]\n");
		sb.append("}").append("\n");
		return sb.toString();
	}
}


private void process(Data parentData, ProductContainer container) {
	Data data = new Data();
	parentData.children.add(data);
	
	data.id = container.getContentName();
	data.name = container.getFullName();
	
	if (container instanceof CategoryModel){
		CategoryModel cat = (CategoryModel) container;
		data.countAll = cat.getProducts().size();
	}
	
	for (CategoryModel subCat : container.getSubcategories()){
		process(data, subCat);
	}
}
%>

<%
Data data = new Data();
for (DepartmentModel dep: ContentFactory.getInstance().getStore().getDepartments()){
  process(data, dep);
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
    #csvcontent {
      display: none;
    }
  </style>
</head>
<body>
  <h1># of products in departments/categories</h1>
  <div class="control">
    <button id="btn_open_all">open all</button>
    <button id="btn_close_all">close all</button>
    <button id="btn_toggle_empty">show/hide empty nodes</button>
    <button id="btn_download_csv">CSV</button>
  </div>
  <ul id="content">
  </ul>
  <pre id="csvcontent">
  </pre>

<script>
var data = <%=data%>,
    buff = [], csvbuff = [];

function displayNode (node, buff) {
  buff.push('<li id="'+node.id+'" data-products="'+node.countAll+'">'+node.name+'<span class="contentId">'+node.id+'</span>');

  if (node.children && node.children.length) {
    buff.push('<span class="handle"></span><ul>');
    node.children.forEach(function (childnode) {
      buff = displayNode(childnode, buff);
    });
    buff.push('</ul>');
  }

  buff.push('</li>');
  return buff;
}

function displayCSVNode (node, csvbuff, parents) {
  parents = parents || [];
  var family = parents.concat([node]);

  csvbuff.push(node.countAll+';'+parents.map(function (parent) {
    return parent.name+';'+parent.id;
  }).join(';')+';'+node.name+';'+node.id+';\n');

  if (node.children && node.children.length) {
    node.children.forEach(function (childnode) {
      csvbuff = displayCSVNode(childnode, csvbuff, family);
    });
  }

  return csvbuff;
}

data.children.forEach(function (node) {
  buff = displayNode(node, buff);
});

document.getElementById('content').innerHTML = buff.join('');

data.children.forEach(function (node) {
  csvbuff = displayCSVNode(node, csvbuff);
});

document.getElementById('csvcontent').innerHTML = csvbuff.join('');
</script>

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
document.getElementById("btn_download_csv").addEventListener("click", function (e) {
  window.location.href = 'data:application/csv;charset=utf-8,'+encodeURIComponent(document.getElementById('csvcontent').childNodes[0].nodeValue);
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
