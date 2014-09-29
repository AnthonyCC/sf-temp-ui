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


private Data process(Data parentData, ProductContainer container, Boolean isQuick) {
	Data data = new Data();
	parentData.children.add(data);
	
	data.id = container.getContentName();
	data.name = container.getFullName();
	
	if (container instanceof CategoryModel){
		for (ProductModel prod : ((CategoryModel) container).getProducts()){
      if (isQuick) {
        data.countAvailable++;
      } else {
        if (prod.isDiscontinued()){
          data.countDiscontinued++;
        } else if (prod.isTempUnavailable()){
          data.countTempUnavailable++;
        } else {
          data.countAvailable++;
        }
      }
			data.countAll++;
		}
	}
	
	for (CategoryModel subCat : container.getSubcategories()){
		Data childData = process(data, subCat, isQuick);
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

    /* SVG */
    svg .node {
      cursor: pointer;
    }

    svg .node:hover {
      stroke: #000;
      stroke-width: 1.5px;
    }

    svg .node--leaf {
      fill: white;
    }

    svg .label {
      font: 11px "Helvetica Neue", Helvetica, Arial, sans-serif;
      text-anchor: middle;
      text-shadow: 0 1px 0 #fff, 1px 0 0 #fff, -1px 0 0 #fff, 0 -1px 0 #fff;
    }

    svg .label,
    svg .node--leaf {
      pointer-events: none;
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
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
response.flushBuffer();

Data rootData = new Data();
String pQuick = request.getParameter("quick");
Boolean isQuick = pQuick != null && pQuick.equals("true");

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
				Data deptData = process(superDeptData, dept, isQuick);
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
		Data deptData = process(emptySuperDeptData, dept, isQuick);
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
    <a id="btn_download_csv" download="sitemap.csv" href="#">Download CSV</a>
  </div>
  <ul id="content">
  </ul>
  <pre id="csvcontent">
  </pre>

<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
<script>
document.getElementById("spinner").style.display="none";
document.getElementById("log").style.display="none";

var data = <%=rootData%>,
    buff = [], csvbuff = [];

data.countAll = data.children.reduce(function (p, c) { return p + c.countAll; }, 0);

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
  var family = parents.concat([node]),
      line = [];


  line = [node.countAll, node.countAvailable, node.countTempUnavailable, node.countDiscontinued];

  parents.forEach(function (parent) {
    line.push(parent.name);
    line.push(parent.id);
  });

  line.push(node.name);
  line.push(node.id);

  csvbuff.push(line.map(function (item) {
    item = ""+item;
    return '"'+item.replace(/"/g, '""')+'"';
  }).join(',')+'\n');

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
  this.href = 'data:application/csv;charset=utf-8,'+encodeURIComponent(document.getElementById('csvcontent').childNodes[0].nodeValue);
});
</script>
<script>

var margin = 20,
    diameter = 960;

var color = d3.scale.linear()
    .domain([-1, 5])
    .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
    .interpolate(d3.interpolateHcl);

var pack = d3.layout.pack()
    .padding(2)
    .size([diameter - margin, diameter - margin])
    .value(function(d) { return d.countAll; })

var svg = d3.select("body").append("svg")
    .attr("width", diameter)
    .attr("height", diameter)
  .append("g")
    .attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");

var focus = data,
    root = data,
    nodes = pack.nodes(data),
    view;

var circle = svg.selectAll("circle")
    .data(nodes)
  .enter().append("circle")
    .attr("class", function(d) { return d.parent ? d.children ? "node" : "node node--leaf" : "node node--root"; })
    .style("fill", function(d) { return d.children ? color(d.depth) : null; })
    .on("click", function(d) { if (focus !== d) zoom(d), d3.event.stopPropagation(); });

var text = svg.selectAll("text")
    .data(nodes)
  .enter().append("text")
    .attr("class", "label")
    .style("fill-opacity", function(d) { return d.parent === root ? 1 : 0; })
    .style("display", function(d) { return d.parent === root ? null : "none"; })
    .text(function(d) { return d.name; });

var node = svg.selectAll("circle,text");

zoomTo([root.x, root.y, root.r * 2 + margin]);

function zoom(d) {
  var focus0 = focus; focus = d;

  var transition = d3.transition()
      .duration(d3.event.altKey ? 7500 : 750)
      .tween("zoom", function(d) {
        var i = d3.interpolateZoom(view, [focus.x, focus.y, focus.r * 2 + margin]);
        return function(t) { zoomTo(i(t)); };
      });

  transition.selectAll("text")
    .filter(function(d) { return d.parent === focus || this.style.display === "inline"; })
      .style("fill-opacity", function(d) { return d.parent === focus ? 1 : 0; })
      .each("start", function(d) { if (d.parent === focus) this.style.display = "inline"; })
      .each("end", function(d) { if (d.parent !== focus) this.style.display = "none"; });
}

function zoomTo(v) {
  var k = diameter / v[2]; view = v;
  node.attr("transform", function(d) { return "translate(" + (d.x - v[0]) * k + "," + (d.y - v[1]) * k + ")"; });
  circle.attr("r", function(d) { return d.r * k; });
}

d3.select(self.frameElement).style("height", diameter + "px");
</script>
</body>
</html>
