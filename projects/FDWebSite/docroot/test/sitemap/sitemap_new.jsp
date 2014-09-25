<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.SuperDepartmentModel"%>
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
	int countAvailable;
	int countTempUnavailable;
	int countDiscontinued;
	List<Data> children = new ArrayList<Data>();
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		sb.append("id:\"").append(id).append("\",\n");
		sb.append("name:\"").append(name == null ? "" : name.replace("\"", "\\\"")).append("\",\n");
		sb.append("countAll:").append(countAll).append(",\n");
		sb.append("countAvailable:").append(countAvailable).append(",\n");
		sb.append("countTempUnavailable:").append(countTempUnavailable).append(",\n");
		sb.append("countDiscontinued:").append(countDiscontinued).append(",\n");
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


private Data process(Data parentData, ProductContainer container) {
	Data data = new Data();
	parentData.children.add(data);
	
	data.id = container.getContentName();
	data.name = container.getFullName();
	
	if (container instanceof CategoryModel){
		for (ProductModel prod : ((CategoryModel) container).getProducts()){
			if (prod.isDiscontinued()){
				data.countDiscontinued++;
			} else if (prod.isTempUnavailable()){
				data.countTempUnavailable++;
			} else {
				data.countAvailable++;
			}
			data.countAll++;
		}
	}
	
	for (CategoryModel subCat : container.getSubcategories()){
		Data childData = process(data, subCat);
		data.countAll 				+= childData.countAll;
		data.countAvailable 		+= childData.countAvailable;
		data.countTempUnavailable 	+= childData.countTempUnavailable;
		data.countDiscontinued 		+= childData.countDiscontinued;
	}
	return data;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>FreshDirect Sitemap</title>
  <style>
    body {
      font-family: Verdana, Arial;
      font-size: 12px;
      margin: auto;
      width: 970px;
      position: relative;
    }
    h1 {
    	color: rgb(51,102,0);
    }
    ul > li {
     /* background-color: rgba(112,139,74,.1);*/
    }
    
    ul > li {
     color: rgb(51,102,0);
    }

    ul > li > ul > li {
     color: #dd7711;
    }
    
    ul > li > ul > li > ul > li {
     color: rgb(51,102,0);
    }
    
    ul > li > ul > li > ul > li > ul > li{
     color: #dd7711;
    }
    
    ul > li > ul > li > ul > li > ul > li > ul > li{
     color: rgb(51,102,0);
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
      color: red;
    }
    .selected > span.handle:after {
      content: 'V';
      color: red;
    }
    
    [data-products]:before {
      /*display: inline-block;*/
      position: absolute;
      content: attr(data-products) " products (" attr(data-products-avail) " avail / " attr(data-products-tempunavail) " unav / " attr(data-products-disc) " disc)";
      /*color: #c27916;
      margin-right: 10px;
      width: 360px;*/
      right: 20px;
      white-space: nowrap;
    }
    [data-products="0"] {
      color: #888;
    }
    .noempty [data-products="0"] {
      display: none;
    }
    [data-products="0"]:before {
      content: '';
    }
    span.contentId {
      display: inline-block;
      margin-left: 0.3em;
      color: gray;
    }
    span.contentId:before {
    	content: "("
    }
    span.contentId:after {
    	content: ")"
    }
    a {
    	color: inherit;
    } 
    #csvcontent {
      display: none;
    }
    #content {
    	/*background-color: rgb(220,230,190);*/
    }
    header {
      text-align: center;
    }
    #log {
    	color: rgb(51,102,0);
    }
  </style>
</head>
<body>
  <header>
	  <img alt="freshdirect" src="/media_stat/images/template/quickshop/9managers_s.jpg">
	  <img alt="freshdirect" height="125" src="/media/images/navigation/department/local/dept_icons/dpt_local_whoslocal_map.gif">
	  <h1>Sitemap</h1>
	  
	  <img id="spinner" alt=spinner" src="/media_stat/images/navigation/spinner.gif">
  </header>
  <div id="log">Processing 
<%
response.flushBuffer();

Data rootData = new Data();

Set<DepartmentModel> processedDepartments = new HashSet<DepartmentModel>();

for (ContentKey superDeptKey : CmsManager.getInstance().getContentKeysByType(ContentType.get("SuperDepartment"))) {
	ContentNodeModel superDeptNode = ContentFactory.getInstance().getContentNodeByKey(superDeptKey);
	
	if (superDeptNode instanceof SuperDepartmentModel) {
		SuperDepartmentModel superDept = (SuperDepartmentModel) superDeptNode;
		Data superDeptData = new Data();
		rootData.children.add(superDeptData);
		superDeptData.id	= superDept.getContentName();
		superDeptData.name	= superDept.getFullName();
		
		for (DepartmentModel dept : superDept.getDepartments()){
			if (processedDepartments.add(dept)){
				out.println(" "+dept.getFullName()+"...");
				response.flushBuffer();
				Data deptData = process(superDeptData, dept);
				superDeptData.countAll 				+= deptData.countAll;
				superDeptData.countAvailable 		+= deptData.countAvailable;
				superDeptData.countTempUnavailable 	+= deptData.countTempUnavailable;
				superDeptData.countDiscontinued 	+= deptData.countDiscontinued;
			}
		}
	}
}

Data emptySuperDeptData = new Data();
rootData.children.add(emptySuperDeptData);
emptySuperDeptData.id	= "none";
emptySuperDeptData.name	= "No Super Department";

//remaining departments
for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
	if (!processedDepartments.contains(dept)) {
		out.println(" "+dept.getFullName()+"...");
		response.flushBuffer();
		Data deptData = process(emptySuperDeptData, dept);
		emptySuperDeptData.countAll 			+= deptData.countAll;
		emptySuperDeptData.countAvailable 		+= deptData.countAvailable;
		emptySuperDeptData.countTempUnavailable += deptData.countTempUnavailable;
		emptySuperDeptData.countDiscontinued 	+= deptData.countDiscontinued;
	}
}
%>
  </div>
  <div class="control">
    <button id="btn_open_all">open all</button>
    <button id="btn_close_all">close all</button>
    <button id="btn_toggle_empty">show/hide empty nodes</button>
    <button id="btn_download_csv">Download CSV</button>
  </div>
  <ul id="content">
  </ul>
  <pre id="csvcontent">
  </pre>

<script>
var data = <%=rootData%>,
    buff = [], csvbuff = [];

function displayNode (node, buff) {
  buff.push('<li id="'+node.id+'" data-products="'+node.countAll+'" data-products-avail="'+node.countAvailable+'" data-products-tempunavail="'+node.countTempUnavailable+'" data-products-disc="'+node.countDiscontinued+'"><a href="/browse.jsp?id='+node.id+'">'+node.name+'<span class="contentId">'+node.id+'</span></a>');

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

  csvbuff.push(node.countAll+';'+node.countAvailable+';'+node.countTempUnavailable+';'+node.countDiscontinued+';'+parents.map(function (parent) {
    return parent.name+';'+parent.id+';';
  }).join('')+node.name+';'+node.id+';\n');

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
document.getElementById("spinner").style.display="none";
document.getElementById("log").style.display="none";
</script>
</body>
</html>
