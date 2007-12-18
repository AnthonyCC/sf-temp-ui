<%@ page import='java.util.*'  %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

<fd:CheckLoginStatus />

<%!
        private static Calendar now = Calendar.getInstance();
%>

<%
int currentMonth = now.get(Calendar.MONTH);

String current_season = "";

int column1 = 0; //winter
int column2 = 1; //spring
int column3 = 2; //summer
int column4 = 3; //autumn

if (
	(currentMonth == Calendar.DECEMBER) || 
	(currentMonth == Calendar.JANUARY) || 
	(currentMonth == Calendar.FEBRUARY) 
	) {
	current_season = "winter";
	//use default order
}

if (
	(currentMonth == Calendar.MARCH) || 
	(currentMonth == Calendar.APRIL) || 
	(currentMonth == Calendar.MAY) 
	) {
	current_season = "spring";
	column1 = 1; //spring
	column2 = 2; //summer
	column3 = 3; //autumn
	column4 = 0; //winter
}

if (
	(currentMonth == Calendar.JUNE) ||
	(currentMonth == Calendar.JULY) ||
	(currentMonth == Calendar.AUGUST) 
	) {
	current_season = "summer";
	column1 = 2; //summer
	column2 = 3; //autumn
	column3 = 0; //winter
	column4 = 1; //spring
}

if (
	(currentMonth == Calendar.SEPTEMBER) || 
	(currentMonth == Calendar.OCTOBER) || 
	(currentMonth == Calendar.NOVEMBER) 
	) {
	current_season = "autumn";
	column1 = 3; //autumn
	column2 = 0; //winter
	column3 = 1; //spring
	column4 = 2; //summer
}

%>
<% 
String category = "/category.jsp?catId=";
String best = "<img src=\"/media_stat/images/template/fruit/best.gif\" width=\"44\" height=\"18\" border=\"0\">";
String yes = "<img src=\"/media_stat/images/template/fruit/yes.gif\" width=\"44\" height=\"18\" border=\"0\">";
String no = "<img src=\"/media_stat/images/template/fruit/no.gif\" width=\"44\" height=\"18\" border=\"0\">";
%>
<%
// ARRAYS
//SEASONS
String[] season = { 
				"<td colspan=\"3\" class=\"winter\"><span class=\"season\">WINTER</span></td>",
				"<td colspan=\"3\" class=\"spring\"><span class=\"season\">SPRING</span></td>",
				"<td colspan=\"3\" class=\"summer\"><span class=\"season\">SUMMER</span></td>",
				"<td colspan=\"3\" class=\"autumn\"><span class=\"season\">AUTUMN</span></td>" };
				
String[] season_months = { 
				"<td class=\"winter\">DEC</td><td class=\"winter\">JAN</td><td class=\"winter\">FEB</td>",
				"<td class=\"spring\">MAR</td><td class=\"spring\">APR</td><td class=\"spring\">MAY</td>",
				"<td class=\"summer\">JUN</td><td class=\"summer\">JUL</td><td class=\"summer\">AUG</td>",
				"<td class=\"autumn\">SEP</td><td class=\"autumn\">OCT</td><td class=\"autumn\">NOV</td>" };

// FRUITS
// DEC JAN FEB
// MAR APR MAY
// JUN JUL AUG
// SEP OCT NOV
String[] apples = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };
				
String[] apricots = { 
				"<td colspan=\"3\">"+ yes + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + best + "</td>",
				"<td colspan=\"3\">"+ best + best + no +"</td>",
				"<td colspan=\"3\">"+ no + no + yes +"</td>" };

String[] avocados = { 
				"<td colspan=\"3\">"+ best + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };

String[] bananas = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };

String[] blackberries = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + no +"</td>" };

String[] blueberries = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>" };

String[] cherries = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>" };

String[] raspberries = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ yes + best + best + "</td>",
				"<td colspan=\"3\">"+ best + yes + yes +"</td>" };

String[] strawberries = { 
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>" };

String[] oranges = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + best + "</td>" };

String[] grapefruits = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + best + "</td>" };

String[] tangerines = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>",
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + best + best + "</td>" };

String[] clementines = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ yes + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + yes +"</td>" };

String[] grapes = { 
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };

String[] melons = { 
				"<td colspan=\"3\">"+ best + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };

String[] nectarines = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + no + no +"</td>" };

String[] peaches = { 
				"<td colspan=\"3\">"+ no + no + no +"</td>",
				"<td colspan=\"3\">"+ no + no + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + no + no +"</td>" };

String[] pears = { 
				"<td colspan=\"3\">"+ best + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };

String[] plums = { 
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ yes + yes + yes +"</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + yes +"</td>" };

String[] tropical = { 
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>",
				"<td colspan=\"3\">"+ best + best + best + "</td>" };
%>

<tmpl:insert template='/common/template/dnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Fruit - Seasonal Guide</tmpl:put>
	
    	<tmpl:put name='content' direct='true'>
		<style type="text/css">
<!--
.season { 
font-family: Arial Black, Arial, Verdana, sans-serif;
font-size: 15px;
font-weight: normal;
}

.fruit {
font-family: Arial, Verdana, sans-serif;
color: #336600;
font-weight: bold;
font-size: 11px;
text-align: right;
padding-right: 6px;
}

.spring {
font-family: Arial, Verdana, sans-serif;
background-color: #FF99CC;
color: #FFFFFF;
font-weight: bold;
font-size: 12px;
text-align: center;
padding: 2px;
}

.summer {
font-family: Arial, Verdana, sans-serif;
background-color: #FFCC00;
color: #FFFFFF;
font-weight: bold;
font-size: 12px;
text-align: center;
padding: 2px;
}

.autumn {
font-family: Arial, Verdana, sans-serif;
background-color: #CC660B;
color: #FFFFFF;
font-weight: bold;
font-size: 12px;
text-align: center;
padding: 2px;
}

.winter {
font-family: Arial, Verdana, sans-serif;
background-color: #999999;
color: #FFFFFF;
font-weight: bold;
font-size: 12px;
text-align: center;
padding: 2px;
}
-->
</style>

<table width="668" border="0" cellspacing="0" cellpadding="0" align="left">
	<tr>
		<td><img src="/media_stat/images/template/fruit/lil_tony_season.jpg" width="106" height="118"><br><img src="/media_stat/images/layout/clear.gif" width="124" height="8"></td>
		<td colspan="15"><img src="/media_stat/images/template/fruit/fruit_season_hdr.gif" width="374" height="19"><br><br>
		Generally most fruit is available all year long, but the best way to assure the best taste is to buy during peak season. We've developed this seasonal guide to help you identify the tastiest fruits at any time of the year. Just remember to use this as a guideline only since local weather and farming conditions can always affect availability and quality.
		<br><br><img src="/media_stat/images/template/fruit/fruit_season_key.gif" width="543" height="20"></td>
	</tr>
	<tr> 
	    <td rowspan="2"></td>
	    <%= season[column1] %>
	    <td rowspan="2"></td>
	    <%= season[column2] %>
	    <td rowspan="2"></td>
	    <%= season[column3] %>
	    <td rowspan="2"></td>
	    <%= season[column4] %>
	</tr>
	<tr> 
		<%= season_months[column1] %>
		
		<%= season_months[column2] %>
		
		<%= season_months[column3] %>
		
		<%= season_months[column4] %>
	</tr>
	<tr height="4"> 
		<td><img src="/media_stat/images/layout/clear.gif" width="125" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="44" height="1"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>apl'">  
		<td class="fruit"><a href="<%=category%>apl">APPLES</a></td>
		<%= apples[column1] %>
		<td></td>
		<%= apples[column2] %>
		<td></td>
		<%= apples[column3] %>
		<td></td>
		<%= apples[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>sf'">  
		<td class="fruit"><a href="<%=category%>sf">APRICOTS</a></td>
		<%= apricots[column1] %>
		<td></td>
		<%= apricots[column2] %>
		<td></td>
		<%= apricots[column3] %>
		<td></td>
		<%= apricots[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>avocados_fruit'">  
		<td class="fruit"><a href="<%=category%>avocados_fruit">AVOCADOS</a></td>
		<%= avocados[column1] %>
		<td></td>
		<%= avocados[column2] %>
		<td></td>
		<%= avocados[column3] %>
		<td></td>
		<%= avocados[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>ban'">  
		<td class="fruit"><a href="<%=category%>ban">BANANAS</a></td>
		<%= bananas[column1] %>
		<td></td>
		<%= bananas[column2] %>
		<td></td>
		<%= bananas[column3] %>
		<td></td>
		<%= bananas[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>br'">  
		<td class="fruit"><a href="<%=category%>br">BLACKBERRIES</a></td>
		<%= blackberries[column1] %>
		<td></td>
		<%= blackberries[column2] %>
		<td></td>
		<%= blackberries[column3] %>
		<td></td>
		<%= blackberries[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>br'">  
		<td class="fruit"><a href="<%=category%>br">BLUEBERRIES</a></td>
		<%= blueberries[column1] %>
		<td></td>
		<%= blueberries[column2] %>
		<td></td>
		<%= blueberries[column3] %>
		<td></td>
		<%= blueberries[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>br'">  
		<td class="fruit"><a href="<%=category%>br">CHERRIES</a></td>
		<%= cherries[column1] %>
		<td></td>
		<%= cherries[column2] %>
		<td></td>
		<%= cherries[column3] %>
		<td></td>
		<%= cherries[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>br'">  
		<td class="fruit"><a href="<%=category%>br">RASPBERRIES</a></td>
		<%= raspberries[column1] %>
		<td></td>
		<%= raspberries[column2] %>
		<td></td>
		<%= raspberries[column3] %>
		<td></td>
		<%= raspberries[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>br'">  
		<td class="fruit"><a href="<%=category%>br">STRAWBERRIES</a></td>
		<%= strawberries[column1] %>
		<td></td>
		<%= strawberries[column2] %>
		<td></td>
		<%= strawberries[column3] %>
		<td></td>
		<%= strawberries[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>ctr'">  
		<td class="fruit"><a href="<%=category%>ctr">ORANGES</a></td>
		<%= oranges[column1] %>
		<td></td>
		<%= oranges[column2] %>
		<td></td>
		<%= oranges[column3] %>
		<td></td>
		<%= oranges[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>ctr'">  
		<td class="fruit"><a href="<%=category%>ctr">GRAPEFRUITS</a></td>
		<%= grapefruits[column1] %>
		<td></td>
		<%= grapefruits[column2] %>
		<td></td>
		<%= grapefruits[column3] %>
		<td></td>
		<%= grapefruits[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>ctr'">  
		<td class="fruit"><a href="<%=category%>ctr">TANGERINES</a></td>
		<%= tangerines[column1] %>
		<td></td>
		<%= tangerines[column2] %>
		<td></td>
		<%= tangerines[column3] %>
		<td></td>
		<%= tangerines[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>ctr'">  
		<td class="fruit"><a href="<%=category%>ctr">CLEMENTINES</a></td>
		<%= clementines[column1] %>
		<td></td>
		<%= clementines[column2] %>
		<td></td>
		<%= clementines[column3] %>
		<td></td>
		<%= clementines[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>grp'">  
		<td class="fruit"><a href="<%=category%>grp">GRAPES</a></td>
		<%= grapes[column1] %>
		<td></td>
		<%= grapes[column2] %>
		<td></td>
		<%= grapes[column3] %>
		<td></td>
		<%= grapes[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>mln'">  
		<td class="fruit"><a href="<%=category%>mln">MELONS</a></td>
		<%= melons[column1] %>
		<td></td>
		<%= melons[column2] %>
		<td></td>
		<%= melons[column3] %>
		<td></td>
		<%= melons[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>sf'">  
		<td class="fruit"><a href="<%=category%>sf">NECTARINES</a></td>
		<%= nectarines[column1] %>
		<td></td>
		<%= nectarines[column2] %>
		<td></td>
		<%= nectarines[column3] %>
		<td></td>
		<%= nectarines[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>sf'">  
		<td class="fruit"><a href="<%=category%>sf">PEACHES</a></td>
		<%= peaches[column1] %>
		<td></td>
		<%= peaches[column2] %>
		<td></td>
		<%= peaches[column3] %>
		<td></td>
		<%= peaches[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>pr'">  
		<td class="fruit"><a href="<%=category%>pr">PEARS</a></td>
		<%= pears[column1] %>
		<td></td>
		<%= pears[column2] %>
		<td></td>
		<%= pears[column3] %>
		<td></td>
		<%= pears[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>sf'">  
		<td class="fruit"><a href="<%=category%>sf">PLUMS</a></td>
		<%= plums[column1] %>
		<td></td>
		<%= plums[column2] %>
		<td></td>
		<%= plums[column3] %>
		<td></td>
		<%= plums[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr style="cursor: hand;" onClick="document.location='<%=category%>trp'"> 
		<td class="fruit"><a href="<%=category%>trp">TROPICAL</a></td>
		<%= tropical[column1] %>
		<td></td>
		<%= tropical[column2] %>
		<td></td>
		<%= tropical[column3] %>
		<td></td>
		<%= tropical[column4] %>
	</tr>
	<tr> 
		<td colspan="16" height="4"></td>
	</tr>
	<tr> 
		<td></td>
		<%= season_months[column1] %>
		<td></td>
		<%= season_months[column2] %>
		<td></td>
		<%= season_months[column3] %>
		<td></td>
		<%= season_months[column4] %>
	</tr>
</table>
<br clear="all"><br>
				
		</tmpl:put>
		
</tmpl:insert>
