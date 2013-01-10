<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.util.Date"%>
<html><head><title>Sales Forecast</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- <meta http-equiv="refresh" content="60">-->

<style>

html, body {
	padding:0;
	margin:0 0 0 50;

}

#outerdiv {
   min-height:100%;
   position:relative;
   width:100%;
}


#header{
   position:absolute;
   top:0;
   width:80%;
   min-height:15px;   
   background-color:rgb(0,150,33);

}


#footer {
   position:fixed;
   bottom:0;
   width:75%;
   min-height:15px;   
   background-color:rgb(0,150,33);

}

.tabs li {
	list-style:none;
	display:inline;
}

.tabs a {
	padding:5px 10px;
	display:inline-block;
	background:#666;
	color:#fff;
	text-decoration:none;
}

.tabs a.active {
	background:#fff;
	color:#000;
	border-bottom:none;
}
div.float-left 
{ 
   	float:left;
 	top:0;
 	left:0;
 	width:50%;
}
div.float-right 
{ 
	 float:left;
	 top:0;
	 right:0;
	 width:50%;
}
ul.tabs{
  width:600px;
 margin: 0;
padding: 0;
list-style-type: none;
border-bottom: 1px solid #778;
}


tr.actual
{
color:red;
}
.left
{
text-align:left;
}
.right
{
text-align:right;
}
table.tbl
{
border:0;
border-collapse: collapse;
width:100%;
}
td, th
{
    padding: 10px; 
    font-family:verdana;
    font-size:12px;
}

#refresh{
	padding: 30 0 10 0;
	width:80%;   
	font-family:verdana;
    font-size:12px;
}

#tab1, #tab2, #tab3 {min-height:900px; width:80%}

table#zoneT,table#zoneTm,table#zoneTx,table#zoneTmx {border-collapse: collapse; width:100%}
table#zoneT tr:nth-last-child(2) td { border-bottom: solid 1px black;  }
table#zoneTm tr:nth-last-child(2) td { border-bottom: solid 1px black; }

</style>



<script src="../js/jquery-1.7.1.js" type="text/javascript"></script>
<script src="../js/date.js" type="text/javascript"></script>
<script src="../js/jquery.formatCurrency-1.4.0.js" type="text/javascript"></script>
<script src="../js/jquery.number.js" type="text/javascript"></script>
<script type="text/javascript">
// Wait until the DOM has loaded before querying the document
var orders = new Object();
var sales = new Object();
var aos = new Object();
var aoss = new Object();
var forecast = new Object();		

$(document).ready(function(){
	
	
	$('ul.tabs').each(function(){
		// For each set of tabs, we want to keep track of
		// which tab is active and it's associated content
		var $active, $content, $links = $(this).find('a');

		// If the location.hash matches one of the links, use that as the active tab.
		// If no match is found, use the first link as the initial active tab.
		$active = $($links.filter('[href="'+location.hash+'"]')[0] || $links[0]);
		$active.addClass('active');
		$content = $($active.attr('href'));

		// Hide the remaining content
		$links.not($active).each(function () {
			$($(this).attr('href')).hide();
		});

		// Bind the click event handler
		$(this).on('click', 'a', function(e){
			// Make the old tab inactive.
			$active.removeClass('active');
			$content.hide();

			// Update the variables with the new link and content
			$active = $(this);
			$content = $($(this).attr('href'));

			// Make the tab active.
			$active.addClass('active');
			$content.show();

			// Prevent the anchor's default click action
			e.preventDefault();
		});
	});
	
	// 0 - date
	// 1 - orders per date/zone
	// 2 - zone
	// 7 - orders per day
	
	// 8 - sales per day
	// 9 - AOS per day
	// 3 - sales per day/zone
	// 4 - AOS subtotal day/zone
	// 5 - AOS total day/zone
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/athena/api/salesforecast",
		dataType: "xml",
		success: function(xml) {
		$(xml).find('row').each(function(){
			
			var columns =$(this).children();
			if(contains($($(columns).get(0)).text(), orders) == -1)
				orders[$($(columns).get(0)).text()] = new Object();
			orders[$($(columns).get(0)).text()][$($(columns).get(2)).text()] = $($(columns).get(1)).text();	
			orders[$($(columns).get(0)).text()]['default'] = $($(columns).get(7)).text();
			
			if(contains($($(columns).get(0)).text(), sales) == -1)
				sales[$($(columns).get(0)).text()] = new Object();
			sales[$($(columns).get(0)).text()][$($(columns).get(2)).text()] = $($(columns).get(3)).text();
			sales[$($(columns).get(0)).text()]['default'] = $($(columns).get(8)).text();
			
			
			if(contains($($(columns).get(0)).text(), aos) == -1)
				aos[$($(columns).get(0)).text()] = new Object();
			aos[$($(columns).get(0)).text()]['default'] = $($(columns).get(9)).text();
			aos[$($(columns).get(0)).text()][$($(columns).get(2)).text()] = $($(columns).get(5)).text();
			
			if(contains($($(columns).get(0)).text(), aoss) == -1)
				aoss[$($(columns).get(0)).text()] = new Object();
			aoss[$($(columns).get(0)).text()][$($(columns).get(2)).text()] = $($(columns).get(4)).text();
			
			
			});
		
		$('div#tab1 td#actualY').html(parseInt(orders[Date.today().addDays(-1).toString("yyyyMMdd")]['default']));
		$('div#tab1 td#lwY').html(parseInt(orders[Date.today().addDays(-8).toString("yyyyMMdd")]['default']));
		$('div#tab1 td#actualT').html(parseInt(orders[Date.today().toString("yyyyMMdd")]['default']));
		$('div#tab1 td#lwT').html(parseInt(orders[Date.today().addDays(-7).toString("yyyyMMdd")]['default']));
		$('div#tab1 td#actualTm').html(parseInt(orders[Date.today().addDays(1).toString("yyyyMMdd")]['default']));
		$('div#tab1 td#lwTm').html(parseInt(orders[Date.today().addDays(-6).toString("yyyyMMdd")]['default']));
		if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")] !="undefined" ) 
				if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']  !="undefined")
		$('div#tab1 td#actual2').html(parseInt(orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']));
		$('div#tab1 td#lw2').html(parseInt(orders[Date.today().addDays(-5).toString("yyyyMMdd")]['default']));
		
		
		$('div#tab2 td#actualYS').html(getSubTotal(aoss[Date.today().addDays(-1).toString("yyyyMMdd")], orders[Date.today().addDays(-1).toString("yyyyMMdd")]['default']));
		$('div#tab2 td#lwYS').html(getSubTotal(aoss[Date.today().addDays(-8).toString("yyyyMMdd")],orders[Date.today().addDays(-8).toString("yyyyMMdd")]['default']));
		$('div#tab2 td#actualTS').html(getSubTotal(aoss[Date.today().toString("yyyyMMdd")], orders[Date.today().toString("yyyyMMdd")]['default']));
		$('div#tab2 td#lwTS').html(getSubTotal(aoss[Date.today().addDays(-7).toString("yyyyMMdd")], orders[Date.today().addDays(-7).toString("yyyyMMdd")]['default']));
		$('div#tab2 td#actualTmS').html(getSubTotal(aoss[Date.today().addDays(1).toString("yyyyMMdd")], orders[Date.today().addDays(1).toString("yyyyMMdd")]['default']));
		$('div#tab2 td#lwTmS').html(getSubTotal(aoss[Date.today().addDays(-6).toString("yyyyMMdd")], orders[Date.today().addDays(-6).toString("yyyyMMdd")]['default']));
		if(typeof aoss[Date.today().addDays(2).toString("yyyyMMdd")] !="undefined" ) 
				if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']  !="undefined")
		$('div#tab2 td#actual2S').html(getSubTotal(aoss[Date.today().addDays(2).toString("yyyyMMdd")], orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']));
		$('div#tab2 td#lw2S').html(getSubTotal(aoss[Date.today().addDays(-5).toString("yyyyMMdd")], orders[Date.today().addDays(-5).toString("yyyyMMdd")]['default']));
		
		$('div#tab1 td.right').number(true);
		
		$('div#tab2 td.right').formatCurrency();
		
		$('div#tab2 td#actualY').html(aos[Date.today().addDays(-1).toString("yyyyMMdd")]['default']);
		$('div#tab2 td#lwY').html(aos[Date.today().addDays(-8).toString("yyyyMMdd")]['default']);
		$('div#tab2 td#actualT').html(aos[Date.today().toString("yyyyMMdd")]['default']);
		$('div#tab2 td#lwT').html(aos[Date.today().addDays(-7).toString("yyyyMMdd")]['default']);
		$('div#tab2 td#actualTm').html(aos[Date.today().addDays(1).toString("yyyyMMdd")]['default']);
		$('div#tab2 td#lwTm').html(aos[Date.today().addDays(-6).toString("yyyyMMdd")]['default']);
		if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")] !="undefined" ) 
				if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']  !="undefined")
		$('div#tab2 td#actual2').html(aos[Date.today().addDays(2).toString("yyyyMMdd")]['default']);
		$('div#tab2 td#lw2').html(aos[Date.today().addDays(-5).toString("yyyyMMdd")]['default']);
		
		
		$('div#tab3 td#actualY').html(sales[Date.today().addDays(-1).toString("yyyyMMdd")]['default']);
		$('div#tab3 td#lwY').html(sales[Date.today().addDays(-8).toString("yyyyMMdd")]['default']);
		$('div#tab3 td#actualT').html(sales[Date.today().toString("yyyyMMdd")]['default']);
		$('div#tab3 td#lwT').html(sales[Date.today().addDays(-7).toString("yyyyMMdd")]['default']);
		$('div#tab3 td#actualTm').html(sales[Date.today().addDays(1).toString("yyyyMMdd")]['default']);
		$('div#tab3 td#lwTm').html(sales[Date.today().addDays(-6).toString("yyyyMMdd")]['default']);
		if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")] !="undefined" ) 
				if(typeof orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']  !="undefined")
		$('div#tab3 td#actual2').html(sales[Date.today().addDays(2).toString("yyyyMMdd")]['default']);
		$('div#tab3 td#lw2').html(sales[Date.today().addDays(-5).toString("yyyyMMdd")]['default']);
		
		$('div#tab3 td.right').formatCurrency();
		
		//loading zone level
		$('div#tab1 table#zoneT').append(getzoneinfo(orders[Date.today().addDays(-7).toString("yyyyMMdd")],orders[Date.today().toString("yyyyMMdd")],true,true));
		$('div#tab1 table#zoneTm').append(getzoneinfo(orders[Date.today().addDays(-6).toString("yyyyMMdd")],orders[Date.today().addDays(1).toString("yyyyMMdd")],true,true));
		
		$('div#tab2 table#zoneTx').append(getzoneinfo(aos[Date.today().addDays(-7).toString("yyyyMMdd")],aos[Date.today().toString("yyyyMMdd")],false,false));
		$('div#tab2 table#zoneTmx').append(getzoneinfo(aos[Date.today().addDays(-6).toString("yyyyMMdd")],aos[Date.today().addDays(1).toString("yyyyMMdd")],false,false));
		
		$('div#tab3 table#zoneT').append(getzoneinfo(sales[Date.today().addDays(-7).toString("yyyyMMdd")],sales[Date.today().toString("yyyyMMdd")],false, true));
		$('div#tab3 table#zoneTm').append(getzoneinfo(sales[Date.today().addDays(-6).toString("yyyyMMdd")],sales[Date.today().addDays(1).toString("yyyyMMdd")],false,true));
		
		$('div#tab1 table#zoneT th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab1 table#zoneTm th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab2 table#zoneTx th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab2 table#zoneTmx th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab3 table#zoneT th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab3 table#zoneTm th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		
		$('div#tab2 table#zoneTx td.right').formatCurrency();
		$('div#tab2 table#zoneTmx td.right').formatCurrency();
		$('div#tab3 table#zoneT td.right').formatCurrency();
		$('div#tab3 table#zoneTm td.right').formatCurrency();
		$('div#tab1 table#zoneT td.right').number(true);
		$('div#tab1 table#zoneTm td.right').number(true);
		
		
		}
		});
		$.ajax({
			type: "GET",
			url: "http://localhost:8080/athena/api/forecast",
			dataType: "xml",
			success: function(xml) {
			$(xml).find('row').each(function(){
				var columns =$(this).children();
				forecast[$($(columns).get(0)).text()] = $($(columns).get(1)).text();
				
				});
			
			$('div#tab1 td#forecastY').html(parseInt(forecast[Date.today().addDays(-1).toString("yyyyMMdd")]));
			$('div#tab1 td#forecastT').html(parseInt(forecast[Date.today().toString("yyyyMMdd")]));
			$('div#tab1 td#forecastTm').html(parseInt(forecast[Date.today().addDays(1).toString("yyyyMMdd")]));
			$('div#tab1 td#forecast2').html(parseInt(forecast[Date.today().addDays(2).toString("yyyyMMdd")]));
			
			
			}
			});
});

function contains(elem , arr)
{
for(key in arr)
	{
	if(key == elem)
		return 0;
	}
	return -1;
}

function getzoneinfo(arr1, arr2, fmt,aggregate)
{
	var html = "";
	var sum=0, sumlw=0;
	for(key in arr1)
		{
		if(key != 'default')
			{
				html = html +"<tr><td class=\"left\">"+key+"<td class=\"right\">";
				if(fmt) html = html + parseInt(arr1[key]); else html = html + arr1[key];
				if(aggregate) if(fmt) sumlw =sumlw+parseInt(arr1[key]); else sumlw =sumlw+parseFloat(arr1[key]);
				if(arr2.hasOwnProperty(key))
					{
					html = html + "<td class=\"right\">";
					if(fmt) html = html + parseInt(arr2[key]); else html = html + arr2[key];
					if(aggregate)  if(fmt) sum =sum+parseInt(arr2[key]); else sum =sum+parseFloat(arr2[key]);
					}
			}
		}
	if(aggregate)
	if(fmt)
		html = html + "<tr><td><td class=\"right\">" +sumlw+"<td class=\"right\">"+sum;
	else 
		html = html + "<tr><td><td class=\"right\">" +parseFloat(sumlw).toFixed(2)+"<td class=\"right\">"+parseFloat(sum).toFixed(2);
	
			
	return html;
}

function getSubTotal(arr, elem)
{
var total = 0;
for(key in arr)
	{
	total = total+ parseFloat(arr[key]); 
	}
	return parseFloat(total/elem).toFixed(2);
}
</script>
</head>
<body>
<div id="outerdiv">
<div id="header"></div>

<div id="refresh"><b>Last Refresh: <%=DateFormatUtils.format(new Date(), "hh:mm a") %></b><img src="../images/fd.png" align="right"></img></div>
<ul class='tabs'>
    <li><a href='#tab1'>Orders</a></li>
    <li><a href='#tab2'>AOS</a></li>
    <li><a href='#tab3'>Sales</a></li>
</ul>
<div id='tab1'>
  
    <div class="float-left">
    <table width="80%">
    <tr><th class="left">Yesterday (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "MM/dd/yyyy") %>)</th><th class="right">Orders</th>
	<tr class="actual"><td>Actual<td id="actualY" class="right">
	<tr><td class="left">Forecast<td id="forecastY" class="right">
	<tr><td class="left">Last Week<td id="lwY" class="right">
	
	<br><br>
	
    <tr><th class="left">Today (<%=DateFormatUtils.format(new Date(), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualT" class="right">
	<tr><td class="left">Forecast<td id="forecastT" class="right">
	<tr><td class="left">Last Week<td id="lwT" class="right">
	
	<br><br>
	
    <tr><th class="left">Tomorrow (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), 1), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualTm" class="right">
	<tr><td class="left">Forecast<td id="forecastTm" class="right">
	<tr><td class="left">Last Week<td id="lwTm" class="right">
	
	<br><br>
	
    <tr><th class="left"><%=DateFormatUtils.format(DateUtils.addDays(new Date(), 2), "MM/dd/yyyy") %></th>
	<tr class="actual"><td>Actual<td id="actual2" class="right">
	<tr><td class="left">Forecast<td id="forecast2" class="right">
	<tr><td class="left">Last Week<td id="lw2" class="right">
	</table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneT">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -7), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(new Date(), "MM/dd") %>)</th>
    </table>

    <br><br>
   
    <table id="zoneTm">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -6), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(DateUtils.addDays(new Date(),1), "MM/dd") %>)</th>
   	</table>
    </div>
    
</div>
<div id='tab2'>
    
     <div class="float-left">
    <table width="80%">
    <tr><th class="left">Yesterday (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "MM/dd/yyyy") %>)</th><th class="right">AOS Sub Total</th><th class="right">AOS</th>
	<tr class="actual"><td>Actual<td id="actualYS" class="right"><td id="actualY" class="right">
	<tr><td class="left">Forecast<td id="forecastYS" class="right"><td id="forecastY">
	<tr><td class="left">Last Week<td id="lwYS" class="right"><td id="lwY" class="right">
	
	<br><br>
	
    <tr><th class="left">Today (<%=DateFormatUtils.format(new Date(), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualTS" class="right"><td id="actualT" class="right">
	<tr><td class="left">Forecast<td id="forecastTS" class="right"><td id="forecastT">
	<tr><td class="left">Last Week<td id="lwTS" class="right"><td id="lwT" class="right">
	
	<br><br>
	
    <tr><th class="left">Tomorrow (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), 1), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualTmS" class="right"><td id="actualTm" class="right">
	<tr><td class="left">Forecast<td id="forecastTmS" class="right"><td id="forecastTm">
	<tr><td class="left">Last Week<td id="lwTmS" class="right"><td id="lwTm" class="right">
	
	<br><br>
	
    <tr><th class="left"><%=DateFormatUtils.format(DateUtils.addDays(new Date(), 2), "MM/dd/yyyy") %></th>
	<tr class="actual"><td>Actual<td id="actual2S" class="right"><td id="actual2" class="right">
	<tr><td class="left">Forecast<td id="forecast2S" class="right"><td id="forecast2">
	<tr><td class="left">Last Week<td id="lw2S" class="right"><td id="lw2" class="right">
	</table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneTx">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -7), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(new Date(), "MM/dd") %>)</th>
    </table>

    <br><br>
   
    <table id="zoneTmx">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -6), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(DateUtils.addDays(new Date(),1), "MM/dd") %>)</th>
   	</table>
    </div>
    
</div>
<div id='tab3'>
    
     <div class="float-left">
    <table width="80%">
    <tr><th class="left">Yesterday (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "MM/dd/yyyy") %>)</th><th class="right">Sales</th>
	<tr class="actual"><td>Actual<td id="actualY" class="right">
	<tr><td class="left">Forecast<td id="forecastY">
	<tr><td class="left">Last Week<td id="lwY" class="right">
	
	<br><br>
	
    <tr><th class="left">Today (<%=DateFormatUtils.format(new Date(), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualT" class="right">
	<tr><td class="left">Forecast<td id="forecastT">
	<tr><td class="left">Last Week<td id="lwT" class="right">
	
	<br><br>
	
    <tr><th class="left">Tomorrow (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), 1), "MM/dd/yyyy") %>)</th>
	<tr class="actual"><td>Actual<td id="actualTm" class="right">
	<tr><td class="left">Forecast<td id="forecastTm">
	<tr><td class="left">Last Week<td id="lwTm" class="right">
	
	<br><br>
	
    <tr><th class="left"><%=DateFormatUtils.format(DateUtils.addDays(new Date(), 2), "MM/dd/yyyy") %></th>
	<tr class="actual"><td>Actual<td id="actual2" class="right">
	<tr><td class="left">Forecast<td id="forecast2">
	<tr><td class="left">Last Week<td id="lw2" class="right">
	</table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneT">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -7), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(new Date(), "MM/dd") %>)</th>
    </table>

    <br><br>
   
    <table id="zoneTm">
    <tr><th class="left">By Zone</th><th class="left">Last Wk (<%=DateFormatUtils.format(DateUtils.addDays(new Date(), -6), "MM/dd") %>)</th><th class="left">
    Today (<%=DateFormatUtils.format(DateUtils.addDays(new Date(),1), "MM/dd") %>)</th>
   	</table>
    </div>
   
</div>
</div>
</body>
</html>