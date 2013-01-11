<html><head><title>Sales Forecast</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="refresh" content="60">
<link rel="stylesheet" type="text/css" href="../css/sales.css">

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
		url: "http://api.freshdirect.com/athena/api/salesforecast",
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
		
		$('div#tab1 table#orders').append("<tr><th class=\"left\">Yesterday ("+Date.today().addDays(-1).toString("MM/dd/yyyy")+")</th><th class=\"right\">Orders</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+parseInt(orders[Date.today().addDays(-1).toString("yyyyMMdd")]['default']))
		.append("<tr><td class=\"left\">Forecast<td id=\"forecastY\" class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+parseInt(orders[Date.today().addDays(-8).toString("yyyyMMdd")]['default']))
		.append("<br><br>")
		.append("<tr><th class=\"left\">Today ("+Date.today().toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+parseInt(orders[Date.today().toString("yyyyMMdd")]['default']))
		.append("<tr><td class=\"left\">Forecast<td id=\"forecastT\" class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+parseInt(orders[Date.today().addDays(-7).toString("yyyyMMdd")]['default']))
		.append("<br><br>")
		.append("<tr><th class=\"left\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+parseInt(orders[Date.today().addDays(1).toString("yyyyMMdd")]['default']))
		.append("<tr><td class=\"left\">Forecast<td id=\"forecastTm\" class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+parseInt(orders[Date.today().addDays(-6).toString("yyyyMMdd")]['default']))
		.append("<br><br>")
		.append("<tr><th class=\"left\">"+Date.today().addDays(2).toString("MM/dd/yyyy")+"</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+parseInt(orders[Date.today().addDays(2).toString("yyyyMMdd")]['default']))
		.append("<tr><td class=\"left\">Forecast<td id=\"forecast2\" class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+parseInt(orders[Date.today().addDays(-5).toString("yyyyMMdd")]['default']));
		
		
		$('div#tab2 table#aos').append("<tr><th class=\"left\">Yesterday ("+Date.today().addDays(-1).toString('MM/dd/yyyy')+")</th><th class=\"right\">AOS Sub Total</th><th class=\"right\">AOS</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(-1).toString("yyyyMMdd")], orders[Date.today().addDays(-1).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(-1).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(-8).toString("yyyyMMdd")], orders[Date.today().addDays(-8).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(-8).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">Today ("+Date.today().toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+
				getSubTotal(aoss[Date.today().toString("yyyyMMdd")], orders[Date.today().toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(-7).toString("yyyyMMdd")], orders[Date.today().addDays(-7).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(-7).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(1).toString("yyyyMMdd")], orders[Date.today().addDays(1).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(1).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(-6).toString("yyyyMMdd")], orders[Date.today().addDays(-6).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(-6).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">"+Date.today().addDays(2).toString("MM/dd/yyyy")+"</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(2).toString("yyyyMMdd")], orders[Date.today().addDays(2).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(2).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+
				getSubTotal(aoss[Date.today().addDays(-5).toString("yyyyMMdd")], orders[Date.today().addDays(-5).toString("yyyyMMdd")]['default'])+
				"<td class=\"right\">"+aos[Date.today().addDays(-5).toString("yyyyMMdd")]['default']);
			
		
		$('div#tab1 td.right').number(true);
		$('div#tab2 td.right').formatCurrency();
		
		
		$('div#tab3 table#sales').append("<tr><th class=\"left\">Yesterday ("+Date.today().addDays(-1).toString("MM/dd/yyyy")+")</th><th class=\"right\">Sales</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+sales[Date.today().addDays(-1).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td  class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+sales[Date.today().addDays(-8).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">Today ("+Date.today().toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+sales[Date.today().toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+sales[Date.today().addDays(-7).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd/yyyy")+")</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+sales[Date.today().addDays(1).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td  class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+sales[Date.today().addDays(-6).toString("yyyyMMdd")]['default'])
		.append("<br><br>")
		.append("<tr><th class=\"left\">"+Date.today().addDays(2).toString("MM/dd/yyyy")+"</th>")
		.append("<tr class=\"actual\"><td>Actual<td class=\"right\">"+sales[Date.today().addDays(2).toString("yyyyMMdd")]['default'])
		.append("<tr><td class=\"left\"><td class=\"right\">")
		.append("<tr><td class=\"left\">Last Week<td class=\"right\">"+sales[Date.today().addDays(-5).toString("yyyyMMdd")]['default']);
		
		$('div#tab3 td.right').formatCurrency();
		
		//loading zone level
		$('div#tab1 table#zoneT').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-7).toString("MM/dd")+")</th><th class=\"right\">Today ("+Date.today().toString("MM/dd")+")</th>")
    	$('div#tab1 table#zoneTm').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-6).toString("MM/dd")+")</th><th class=\"right\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd")+")</th>")
    
		$('div#tab1 table#zoneT').append(getzoneinfo(orders[Date.today().addDays(-7).toString("yyyyMMdd")],orders[Date.today().toString("yyyyMMdd")],true,true));
		$('div#tab1 table#zoneTm').append(getzoneinfo(orders[Date.today().addDays(-6).toString("yyyyMMdd")],orders[Date.today().addDays(1).toString("yyyyMMdd")],true,true));
		
		$('div#tab2 table#zoneT2').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-7).toString("MM/dd")+")</th><th class=\"right\">Today ("+Date.today().toString("MM/dd")+")</th>")
    	$('div#tab2 table#zoneTm2').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-6).toString("MM/dd")+")</th><th class=\"right\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd")+")</th>")
    
		$('div#tab2 table#zoneT2').append(getzoneinfo(aos[Date.today().addDays(-7).toString("yyyyMMdd")],aos[Date.today().toString("yyyyMMdd")],false,false));
		$('div#tab2 table#zoneTm2').append(getzoneinfo(aos[Date.today().addDays(-6).toString("yyyyMMdd")],aos[Date.today().addDays(1).toString("yyyyMMdd")],false,false));
		
		$('div#tab3 table#zoneT3').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-7).toString("MM/dd")+")</th><th class=\"right\">Today ("+Date.today().toString("MM/dd")+")</th>")
    	$('div#tab3 table#zoneTm3').append("<tr><th class=\"left\">By Zone</th><th class=\"right\">Last Wk ("+Date.today().addDays(-6).toString("MM/dd")+")</th><th class=\"right\">Tomorrow ("+Date.today().addDays(1).toString("MM/dd")+")</th>")
    
		$('div#tab3 table#zoneT3').append(getzoneinfo(sales[Date.today().addDays(-7).toString("yyyyMMdd")],sales[Date.today().toString("yyyyMMdd")],false, true));
		$('div#tab3 table#zoneTm3').append(getzoneinfo(sales[Date.today().addDays(-6).toString("yyyyMMdd")],sales[Date.today().addDays(1).toString("yyyyMMdd")],false,true));
		
		$('#refresh').prepend("<b>Last Refresh: "+new Date().toString("h:mm tt")+"</b>");
		
		$('div#tab1 table#zoneT th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab1 table#zoneTm th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab2 table#zoneT2 th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab2 table#zoneTm2 th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab3 table#zoneT3 th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		$('div#tab3 table#zoneTm3 th').css({'font-weight':'bold', 'color':'#ffffff', 'background-color':'#000000'})
		
		$('div#tab2 table#zoneT2 td.right').formatCurrency();
		$('div#tab2 table#zoneTm2 td.right').formatCurrency();
		$('div#tab3 table#zoneT3 td.right').formatCurrency();
		$('div#tab3 table#zoneTm3 td.right').formatCurrency();
		$('div#tab1 table#zoneT td.right').number(true);
		$('div#tab1 table#zoneTm td.right').number(true);
		
		$('div#tab2 table#zoneT2 td.rightL').formatCurrency();
		$('div#tab2 table#zoneTm2 td.rightL').formatCurrency();
		$('div#tab3 table#zoneT3 td.rightL').formatCurrency();
		$('div#tab3 table#zoneTm3 td.rightL').formatCurrency();
		
		$('div#tab1 table#zoneT td.rightL').number(true);
		$('div#tab1 table#zoneTm td.rightL').number(true);
		
		
		$('div#tab1 table#zoneT td.rightL').css({ 'border-bottom':'1px solid black'})
		$('div#tab1 table#zoneTm td.rightL').css({ 'border-bottom':'1px solid black'})
		$('div#tab3 table#zoneT3 td.rightL').css({ 'border-bottom':'1px solid black'})
		$('div#tab3 table#zoneTm3 td.rightL').css({ 'border-bottom':'1px solid black'})
		
		
		$.ajax({
			type: "GET",
			url: "http://api.freshdirect.com/athena/api/forecast",
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
		
		}
		});
		
});

Object.size = function(obj) {
    var size = 0, key;
    
    for (key in obj) {
        if (key != 'default' && obj.hasOwnProperty(key)) size++;
	}
    return size;
};

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
	
	var size = Object.size(arr1), count=0;

	var html = "";
	var sum=0, sumlw=0;
	for(key in arr1)
		{
		if(key != 'default')
			{
				if(count == size -1){
					
				html = html +"<tr><td class=\"left\">"+key+"<td class=\"rightL\">";
				if(fmt) html = html + parseInt(arr1[key]); else html = html + arr1[key];
				if(aggregate) if(fmt) sumlw =sumlw+parseInt(arr1[key]); else sumlw =sumlw+parseFloat(arr1[key]);
				if(arr2.hasOwnProperty(key))
					{
					html = html + "<td class=\"rightL\">";
					if(fmt) html = html + parseInt(arr2[key]); else html = html + arr2[key];
					if(aggregate)  if(fmt) sum =sum+parseInt(arr2[key]); else sum =sum+parseFloat(arr2[key]);
					}
				}
				else{
					
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
				
				count++;
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

<div id="refresh"><img src="../images/fd.png" align="right"></img></div>
<ul class='tabs'>
    <li><a href='#tab1'>Orders</a></li>
    <li><a href='#tab2'>AOS</a></li>
    <li><a href='#tab3'>Sales</a></li>
</ul>
<div id='tab1'>
  
    <div class="float-left">
    <table id="orders" width="80%">
	</table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneT">
    </table>
    <br><br>
    <table id="zoneTm">
   	</table>
    </div>
    
</div>
<div id='tab2'>
    
     <div class="float-left">
    <table id="aos" width="80%">
	</table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneT2">
    </table>
    <br><br>
    <table id="zoneTm2">
   	</table>
    </div>
    
</div>
<div id='tab3'>
    
     <div class="float-left">
    <table id="sales" width="80%"></table>
	</div>
	
    <div class="float-right">
    
    <table id="zoneT3">
    </table>
    <br><br>
    <table id="zoneTm3">
   	</table>
    </div>
   
</div>
</div>
</body>
</html>