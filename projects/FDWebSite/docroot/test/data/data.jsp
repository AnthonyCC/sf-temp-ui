<%--

    if called without parameters, presents the user interface.

    otherwise use the following parmeters

    
    @param produce "true"


    Random sampling parameters.

    @param distro              (optional) string: file name of the distribution to be used in 
                                          the test repository, default: "__none"
    @param seed                (optional) seed to be used (only makes sense when distro != "__none")
    @param sample_size         (optional) integer: number of product ids to sample (when distro != "__none")

    Row filter parameters. All filters are optional. If not provided, not used.

    @param max_prods           (optional) integer: maximum number of rows to produced
    @param aval_days           (optional) integer: product available withind these many days
    @param notanaiv            (optional) "true": product is not unavailable
    @param nothidden           (optional) "true": product is not hidden
    @param groc                (optional) "true": product does not have a grocey category layout, "false" opposite
    @param layout              (optional) List, e.g. layout=PERISHABLE&layout=WINE -> perishable or wine
                                          possible values:
                                          "PERISHABLE", "COMPOSITE", "WINE", "COMPOSITE_PLUS", "PARTY_PLATTER",
                                            "MULTI_ITEM_MEAL", "COMPONENT_GROUP_MEAL", "CONFIGURED_PRODUCT"
    @param autoconfigurable    (optional) "true": product is autoconfigurable
    @param random              (optional) integer, with probability x/64 accept the product
    @param ymal                (optional) List, e.g. ymal=product&ymal=recipe -> product or recipe
                                          possible values: "product", "recipe" and "category"

    Column filters

    @param column_filters                 a comma separated list of column filters. The order matters,
                                          it will be the same order the columns are extracted.
                                          Possible values:
                                          "product_id":       product id
                                          "category_id":      category id
                                          "layout_path":      URL of layout
                                          "conent_type"
                                          "min_quantity":     minimum quantity allowed
                                          "avail_skus":       available skus for product
                                          "skus":             all skus for product
                                          "perm_skus":        all available skus with sales units and options
                                          "perm_options":     all options with avaialble skus and sales units
                                          "perm_salesunits":  all sales units with available skus and options

    @param varperm                        "all", "one", or "number" 
                                                              all: generate all permutations
                                                              one: pick one at random
                                                              number: interpret the varmax parameter

    @param varmax                          integer:           number of tuples to extract randomly from
                                                              the "perm_..." cartesian product

    Specifying "skus", "avail_skus" and any of "perm_.." is meaningless since it will generate the cartesion product
    (the GUI does not allow this). Also the "perm_..." columns make sense only if all of them are selected 
    (but extracted in user specified order) since they are all part of a cartesian product.

--%>

<%@ page import="org.apache.commons.collections.Predicate" %>
<%@ page import="org.apache.commons.collections.functors.AllPredicate" %>
<%@ page import="org.apache.commons.collections.functors.AnyPredicate" %>
<%@ page import="org.apache.commons.collections.functors.NotPredicate" %>
<%@ page import="com.freshdirect.fdstore.content.EnumLayoutType" %>

<%@ page import="java.util.Random" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.Enumeration" %>

<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.cms.ContentType" %>
<%@ page import="com.freshdirect.fdstore.content.EnumProductLayout" %>

<%@ page import="com.freshdirect.webapp.taglib.test.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.test.*" %>

<%@ taglib uri='freshdirect' prefix='fd' %>

<%

   String type = request.getParameter("type");

   ContentType contentType = null;

   boolean produce = "true".equals(request.getParameter("produce"));
   
   if ("products".equals(type)) contentType = FDContentTypes.PRODUCT;
   else if ("categories".equals(type)) contentType = FDContentTypes.CATEGORY;
   else if ("departments".equals(type)) contentType = FDContentTypes.DEPARTMENT;
   else if ("recipes".equals(type)) contentType = FDContentTypes.RECIPE;
   else if ("all".equals(type)) contentType = FDContentTypes.STORE;
   else if (produce || type != null) throw new Exception("Type " + type + " not supported");

   String extensions = "";
   if ("products".equals(type)) extensions=".sku,.pid,.url";
   else if ("categories".equals(type)) extensions=".cid,.url";
   else if ("recipes".equals(type)) extensions=".rid,.url";
   else if ("departments".equals(type)) extensions=".did,.url";
%>

<% 

   if (produce) {

      String distro = request.getParameter("distro");
      if (distro == null || "".equals(distro)) distro = "__none";

      int sampleSize = -1;
      String sampleSizeString = request.getParameter("sample_size");
      if (sampleSizeString != null && !"".equals(sampleSizeString)) sampleSize = Integer.parseInt(sampleSizeString);

      long seed = -1;
      String seedString = (String)request.getParameter("seed");
      if (seedString != null && !"".equals(seedString)) seed = Long.parseLong(seedString);

      boolean html = "html".equals(request.getParameter("output"));

      List predicates = new LinkedList();

      List columnExtractors = new LinkedList();

      int maxContent = -1;

%>

<% if ("products".equals(type)) { %>
   <%@include file="process_products.jspf" %>
<% } else if("categories".equals(type)) { %>
   <%@include file="process_categories.jspf" %>
<% } else if("departments".equals(type)) { %>
   <%@include file="process_departments.jspf" %>
<% } else if("recipes".equals(type)) { %>
   <%@include file="process_recipes.jspf" %>
<% } else if("all".equals(type)) { 

      Enumeration params = request.getParameterNames();
      while(params.hasMoreElements()) {
         String name = (String)params.nextElement();

         if (name.equals("max_content")) {
            String value = request.getParameter(name).trim();
            if (value != null && value.length() > 0) maxContent = Integer.parseInt(value);
         } else if (name.equals("nothidden")) {
            predicates.add(new NotPredicate(RowFilter.Hidden));
         } else if (name.equals("searchable")) {
            predicates.add(RowFilter.Searchable);
         } else if (name.equals("notorphan")) {
            predicates.add(new NotPredicate(RowFilter.Orphan));
         } else if (name.equals("column_order")) {
            String[] columns = request.getParameter(name).split("\\.");
            for(int i=0; i< columns.length; ++i) {
                if("id".equals(columns[i])) columnExtractors.add(ColumnExtractor.Id);
                else if ("spath".equals(columns[i])) columnExtractors.add(ColumnExtractor.ServletPath);
                else if ("bpath".equals(columns[i])) {
                   String bmax = (String)request.getParameter("bvar_max");
                   if (bmax == null || "".equals(bmax)) columnExtractors.add(new ColumnExtractor.BrowsePath());
                   else columnExtractors.add(new ColumnExtractor.BrowsePath(new Random(seed), Integer.parseInt(bmax)));
                }
            }
         }
      }

   } %>



<fd:GetContentBundle 
   contentType='<%= contentType %>'
   id='bundle' 
   maxContent='<%= maxContent %>'
   predicate='<%= new AllPredicate((Predicate[])predicates.toArray(new Predicate[0])) %>' 
   columnExtractors='<%= columnExtractors %>'
   distribution='<%= distro %>'
   sampleSize='<%= sampleSize %>'
   seed='<%= seed %>'
   
>

<%

   List caches = bundle.getCaches(columnExtractors);

   if (request.getParameter("count.x") != null) {
      int total = bundle.countElems(caches);
      if (html) {
%>
<html>
<head><title><%=type.toUpperCase()%> DATA</title>
</head>
<body>
<b><%=total%></b> records match.
</body>
</html>
<%
      } else { // not html
%>
<%=total%>
<%
     } // hot html
   } else { // count

      Iterator contentIterator = bundle.cacheIterator(caches,columnExtractors);

%>

<% if (html) { %>
<html>
<head><title><%=type.toUpperCase()%> DATA</title>
</head>

<body>
<table>
<tbody>
   <tr>
<%
      for(int i=0; i< columnExtractors.size(); ++i) {
%>
         <td><b><%= ((ColumnExtractor)columnExtractors.get(i)).getName() %></b></td>
<%
      }
   } else {
         response.setHeader("Content-Type", "text/csv");
   }

   int rowCount = 0;
   while(contentIterator.hasNext()) {
      List row = (List)contentIterator.next();
      if (html) { 
%>
   <tr>
<%   
      }  // if HTML

      for(Iterator c = row.iterator(); c.hasNext(); ) {
         if (html) {
%>
         <td><tt><%= c.next()%></tt></td>
<%        
         } else { // CVS
%><%= c.next()%><% if (c.hasNext()) {%>,<% } %><%
         } 
      } // each column
      ++rowCount;

      if (html) {
%>
   </tr>
<%
      } else { 
%>
<%    }
   }

   if (html) {
%>
   </tr>
</tbody>
</table>
</body>
</html>

<% } // html%>
<% } // not count %>
</fd:GetContentBundle>
<% } else { // SHOW DISPLAY %>

<html>
<head>
<title>CMS - TEST DATA GENERATOR</title>

<script language="javascript" src="/assets/javascript/common_javascript.js"></script>

<script type="text/javascript">

<% if ("products".equals(type)) { %>
   <%@include file="products.js" %>
<% } else if("categories".equals(type)) { %>
   <%@include file="categories.js" %>
<% } else if("departments".equals(type)) { %>
   <%@include file="departments.js" %>
<% } else if("recipes".equals(type)) { %>
   <%@include file="recipes.js" %>
<% } else { %>
   function my_init() {}
<% } %>


var columns = new Array();

var column_names = {
   "product_id"      : "Product Id",
   "category_id"     : "Category Id",
   "parent_id"       : "Patent Id",
   "dept_id"         : "Department Id",
   "rec_id"          : "Recipe Id",
   "id"              : "Content Id",
   "depth"           : "Depth",
   "layout_path"     : "Layout Path",
   "bpath"           : "Browse Path",
   "spath"           : "Servlet Path",
   "content_type"    : "Content Type",
   "content_key"     : "Content Key",
   "min_quantity"    : "Min Quantity",
   "avail_skus"      : "Sku Code",
   "skus"            : "Sku Code",
   "perm_skus"       : "Sku Code",
   "perm_options"    : "Options",
   "perm_salesunits" : "Sales Unit"
};

function init_columns() {
   for(column in column_names) {
      var colElem = document.getElementById(column);
      if (colElem) colElem.checked = false;
   }
   var order_string = "<%=request.getParameter("column_order")%>";
   var colsElem =  document.getElementById('column_order');
   if (!colsElem) return;
   colsElem.value = order_string;
   if (order_string == null || order_string == "") return;
   var current_order = order_string.split('.');
   columns = new Array();
   for(column in column_names) {
      var colElem = document.getElementById(column);
      if (colElem) colElem.checked = false;
   }

   for(i=0; i< current_order.length; ++i) {
      columns.push(current_order[i]);
      document.getElementById(current_order[i]).checked = true;
   }
}

function set_order() {
   var order = "";
   for(c = 0; c < columns.length; ++c) {
      if (c != 0) order += '.';
      order += columns[c];
   }
   document.getElementById('column_order').value = order;
}

function swap_columns(i,j) {
   var tmp = columns[i];
   columns[i] = columns[j];
   columns[j] = tmp;
}

function display_columns() {
   var colElem = document.getElementById('columns');
   if (!colElem) return;
   var html_list = '<font size="-1">';
   var p = 0;
   for(c = 0; c < columns.length; ++c) {
      column = columns[c];
      if (c > 0 && c < columns.length) {
         html_list += 
            '&nbsp;<img src="swap.gif" ' +
            'onclick="swap_columns(' + (c-1) + ',' + c + ');display_columns()"/>&nbsp;' 
      }
      html_list += '<span style="background-color: #ffdddd; color: #dd0000; font-weight: bold"> ' + column_names[column] + ' </span>';
           
   }
   html_list += '</small>';
   colElem.innerHTML = html_list;
   set_order();
}

function position(column) {
   for(i = 0; i< columns.length; ++i)  if (columns[i] == column) return i;
   return -1;
}

function remove_column(column) {
   var p = position(column);
   if (p == -1) return;
   for(i=p; i< columns.length-1; ++i) swap_columns(i,i+1);
   columns.pop();
}

function add_column(column) {
   var p = position(column);
   if(p != -1) return;
   columns.push(column);
}

function toggle_column(column) {
   var p = position(column);
   if (p != -1) {
     for(i=p; i< columns.length-1; ++i) swap_columns(i,i+1);
     columns.pop();
   } else columns.push(column);
   display_columns();
}



function select_distro(file,iid) {
   var delem = document.getElementById('distribution_' + iid);
   var d = document.getElementById('distro');
   if (!d || !delem) return;
   if (d.value == file) {
      d.value = "__none";
      delem.checked = false;
      return;
   } else d.value = file;

   for(i=0;;++i) {
      var elem = document.getElementById('distribution_' + i);
      if (!elem) break;
      if (i == iid) continue;
      elem.checked = false;
   }

   delem.checked = true;
}


</script>
</head>

<body onload="my_init(); init_columns();display_columns()">
   <font face="arial">
   <form action="/test/data/data.jsp" method="GET">

   <center>
   <table cellpadding="0" callspacing="0">
   <tbody>
   <tr height="5">
   <td colspan="3" bgcolor="#aaaaff"/>
   </tr>
   <tr>
   <td bgcolor="#aaaaff" width="5"/> 
   <td bgcolor="#aaaaff">


   <table width="800" border="0" cellpadding="0" cellspacing="0">
   <tbody>
   <tr>
   <td colspan="2" bgcolor="#ccccff" valign="center" align="left">

       <br/>
       <a href="/test/data/data.jsp"><img src="fd.gif" border="0"></a> - <b>CMS TEST DATA GENERATOR</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#about','large')"/>
       <br/><br/>
       
       <% if ("products".equals(type)) { %> <b>PRODUCSTS</b> <% } else { %> <a href="/test/data/data.jsp?type=products"><b>PRODUCTS</b></a> <% } %> |
       <% if ("categories".equals(type)) { %> <b>CATEGORIES</b> <% } else { %> <a href="/test/data/data.jsp?type=categories"><b>CATEGORIES</b></a> <% } %> |
       <% if ("departments".equals(type)) { %> <b>DEPARTMENTS</b> <% } else { %> <a href="/test/data/data.jsp?type=departments"><b>DEPARTMENTS</b></a> <% } %> |
       <% if ("recipes".equals(type)) { %> <b>RECIPES</b> <% } else { %> <a href="/test/data/data.jsp?type=recipes"><b>RECIPES</b></a> <% } %> |
       <% if ("all".equals(type)) { %> <b>ALL</b> <% } else { %> <a href="/test/data/data.jsp?type=all"><b>ALL</b></a> <% } %> 
       <br/><br/>
       
   </td>
   </tr>
<% if (type != null) { %>
   <tr>
   <td bgcolor="#ddddff"><b>Select Row Filters</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#rows','large')"/><br/><br/></td>
   <td bgcolor="#ddddff"><b>Select Columns</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#columns','large')"/><br/><br/></td>
   </tr>
   <tr>
   <td valign="top" bgcolor="#ddddff" width="400">
   <ul>
   <i>Random seed</i>: <input type="text" value="<%= System.currentTimeMillis()%>" name="seed"/> 
      <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#random','large')"/>
   <p>

   <li><b>Match at most <input type="text" size="7" name="max_content"/> content keys</b>
         <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#match','large')"/>, <i>and</i>
   <li><label><input type="checkbox" name="nothidden" value="true" checked /> Not hidden</label>, <i>and</i>
   <li><label><input type="checkbox" name="notorphan" value="true" checked /> Not orphan</label>, <i>and</i>
   <li><label><input type="checkbox" name="searchable" value="true"/> Searchable</label> <% if (!"all".equals(type)) { %>, <i>and</i> <% } %>
   

   <% if ("products".equals(type)) { %>
      <%@include file="products_rows.jspf" %>
   <% } else if("categories".equals(type)) { %>
      <%@include file="categories_rows.jspf" %>
   <% } else if("departments".equals(type)) { %>
      <%@include file="departments_rows.jspf" %>
   <% } else if("recipes".equals(type)) { %>
      <%@include file="recipes_rows.jspf" %>
   <% } else if ("all".equals(type)) { %>

   <% } %>

   </ul>
   </td>
   <td valign="top" width="400"  bgcolor="#ddddff">
   <ul>
   <% if ("products".equals(type)) { %>
      <%@include file="products_columns.jspf" %>
   <% } else if("categories".equals(type)) { %>
      <%@include file="categories_columns.jspf" %>
   <% } else if("departments".equals(type)) { %>
      <%@include file="departments_columns.jspf" %>
   <% } else if("recipes".equals(type)) { %>
      <%@include file="recipes_columns.jspf" %>
   <% } else if("all".equals(type)) { %>

   <li><label><input onclick="toggle_column('id')" id="id" type="checkbox" /> Content id</label>
   <li><label><input onclick="toggle_column('spath')" id="spath" type="checkbox" /> Servlet path</label>
   <li><label><input onclick="toggle_column('bpath')" id="bpath" type="checkbox" "/> Browse Path </label>
          (max <input id="bvar_max" name="bvar_max" type="text" value="" size="6"/> variations) 
	  <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#var','large')"/>

   <% } %>
   </ul>
   </td>
   </tr>

   <tr>
   <td colspan="2" bgcolor="#eeeeff" height="60" valign="top">
   <b>Adjust Column Order</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#adjust','large')"/>
   <p>
   <div id="columns"></div>
   </td>
   </tr>
   <tr>


   <fd:GetDistributions id="distributions" extensions="<%=extensions%>">
   <%
       if (distributions != null && distributions.size() > 0) {
   %>
   <tr>
   <td colspan="2" bgcolor="#ddddff">
   <input type="hidden" id="distro" name="distro" value="__none">
   <b>According to distribution</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#distro','large')"/>

       <p>
       Sample size: <input type="text" value="1000" name="sample_size"/><br/><br/>
       <p>
       <b>Available Distributions</b>
       <ul>
   <%
          int iid = 0;
          for(Iterator i = distributions.iterator(); i.hasNext(); ++iid) {
             String file = (String)i.next();
   %>
             <li><label><input id="distribution_<%=iid%>" type="checkbox" onclick="select_distro('<%=file%>',<%=iid%>)"/> <%=file%></label>
   <%
          }
   %>

       </ul>
   </td>
   </tr>
   <%
       }
   %>
   </fd:GetDistributions>

   <tr>
   <td bgcolor="#ccccff">
   <b>Output Format</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#output','large')"/>
   </td>
   <td bgcolor="#ccccff">
   <b>Get Results</b> <img src="miez.gif" alt="?" onclick="javascript:popup('help.html#results','large')"/>
   </td>
   </tr>
   <tr>
   <td bgcolor="#ccccff">
   <ul>
      <li>HTML <input type="radio" name="output" value="html" checked/>
      <li>CSV <input type="radio" name="output" value="csv" />
   </ul>
   </td>
   <td bgcolor="#ccccff">
   <input type="hidden" name="produce" value="true"/>
   <input type="hidden" id="column_order" name="column_order" value=""/>
   <input type="hidden" id="type" name="type" value="<%=type%>"/>
   <input type="image" name="go" src="csirke.gif"/>
   <input type="image" name="count" src="szamolj.gif"/>
   <br/><br/>
   </td>
   </tr>
<% } else { %>
   <tr>
   <td colspan="2" bgcolor="#eeeeff" height="300" valign="center">
   <center><img src="hal.gif"</center>
   </td>
   </tr>
<% } %>

   </tbody>
   </table>

   </td>
   <td bgcolor="#aaaaff" width="5"/>
   </tr>
   <tr height="5">
   <td colspan="3" bgcolor="#aaaaff"/>
   </tr>
   <tr>
   </tbody>
   </table>

   </center>
</form>
</font>


</body>
</html>

<% } %>
