<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Rollout maps</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<div class="sub_nav"><span class="sub_nav_title">Rollout maps</span> (Month, Area Name)</div>
	<%
	String path = "/media_stat/images/template/email/";
	%>

	<div class="side_nav" style="width: 20%; height: 75%; float: left;">
		<div class="side_nav_module_header">Manhattan</div>
		<div class="side_nav_module_content" style="height: auto;">
		<i>2002</i><br>
		<a href="javascript:swapImage('map','<%=path%>UES_zone_map.gif')" class="info"><span class="time_stamp">11</span> Upper East Side 80 - 60</a><br>
		<a href="javascript:swapImage('map','<%=path%>UES_2_zone_map.gif')" class="info"><span class="time_stamp">12</span> Upper East Side 96 - 80</a><br>
		<br><i>2003</i><br>
		<a href="javascript:swapImage('map','<%=path%>westside_76_96.gif')" class="info"><span class="time_stamp">03</span> West Side 76 - 96</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_86_96.gif')" class="info"><span class="time_stamp">03</span> West Side 86 - 96</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_110_57.gif')" class="info"><span class="time_stamp">04</span> West Side 110 - 57</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_110w_76.gif')" class="info"><span class="time_stamp">04</span> West Side 110w - 76</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_76_60.gif')" class="info"><span class="time_stamp">04</span> West Side 76 - 60</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_57_49.gif')" class="info"><span class="time_stamp">05</span> West Side 57 - 49</a><br>
		<a href="javascript:swapImage('map','<%=path%>chelsea.gif')" class="info"><span class="time_stamp">05</span> Chelsea</a><br>
		<a href="javascript:swapImage('map','<%=path%>houston_14_3.gif')" class="info"><span class="time_stamp">05</span> Houston 14 - 3</a><br>
		<a href="javascript:swapImage('map','<%=path%>houston_14_D.gif')" class="info"><span class="time_stamp">05</span> Houston 14 - D</a><br>
		<a href="javascript:swapImage('map','<%=path%>houston_20.gif')" class="info"><span class="time_stamp">05</span> Houston 20</a><br>
		<a href="javascript:swapImage('map','<%=path%>liberty_morris.gif')" class="info"><span class="time_stamp">06</span> Liberty Morris</a><br>
		<a href="javascript:swapImage('map','<%=path%>lower_manhattan.gif')" class="info"><span class="time_stamp">06</span> Lower Manhattan</a><br>
		<a href="javascript:swapImage('map','<%=path%>morningside.gif')" class="info"><span class="time_stamp">06</span> Morningside</a><br>
		<a href="javascript:swapImage('map','<%=path%>tribeca.gif')" class="info"><span class="time_stamp">06</span> Tribeca</a><br>
		<a href="javascript:swapImage('map','<%=path%>tribeca_2.gif')" class="info"><span class="time_stamp">06</span> Tribeca +</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_49_34.gif')" class="info"><span class="time_stamp">06</span> West Side 49 - 34</a><br>
		<a href="javascript:swapImage('map','<%=path%>westside_30_14.gif')" class="info"><span class="time_stamp">06</span> West Side 30 - 14</a><br>
		<a href="javascript:swapImage('map','<%=path%>noho.gif')" class="info"><span class="time_stamp">07</span> NoHo</a><br>
		<a href="javascript:swapImage('map','<%=path%>soho.gif')" class="info"><span class="time_stamp">07</span> SoHo</a><br>
		<a href="javascript:swapImage('map','<%=path%>west_village.gif')" class="info"><span class="time_stamp">07</span> West Village</a><br>
		<a href="javascript:swapImage('map','<%=path%>west_village_2.gif')" class="info"><span class="time_stamp">08</span> West Village +</a><br>
		<a href="javascript:swapImage('map','<%=path%>nolita.gif')" class="info"><span class="time_stamp">10</span> NoLiTa</a><br>
		</div>
	</div>
	<div class="side_nav" style="width: 20%; height: 75%; float: left;">
		<div class="side_nav_module_header">Brooklyn</div>
		<div class="side_nav_module_content" style="height: auto;">
		<i>2003</i><br>
		<a href="javascript:swapImage('map','<%=path%>dumbo_bheights.gif')" class="info"><span class="time_stamp">09</span> Dumbo, Brooklyn Heights</a><br>
		<a href="javascript:swapImage('map','<%=path%>dumbo_bheights_2.gif')" class="info"><span class="time_stamp">09</span> Dumbo, Brooklyn Heights +</a><br>
		<a href="javascript:swapImage('map','<%=path%>cobblehill_carolgardens.gif')" class="info"><span class="time_stamp">10</span> Cobble Hill, Carol Gardens</a><br>
		<a href="javascript:swapImage('map','<%=path%>park_slope.gif')" class="info"><span class="time_stamp">10</span> Park Slope</a><br>
		<a href="javascript:swapImage('map','<%=path%>boerum_hill.gif')" class="info"><span class="time_stamp">11</span> Boerum Hill</a><br>
		<a href="javascript:swapImage('map','<%=path%>prospect_heights.gif')" class="info"><span class="time_stamp">12</span> Prospect Heights</a><br>
		</div>
		<br>
		<div class="side_nav_module_header" style="border-top: solid 1px;">Queens</div>
		<div class="side_nav_module_content" style="height: auto;">
		<i>2004</i><br>
		<span class="time_stamp">03</span> Astoria: 11102, 11103 & 11105.<br>
		<a href="javascript:swapImage('map','<%=path%>astoria_11106.gif')" class="info"><span class="time_stamp">03</span> Astoria</a><br>
		<a href="javascript:swapImage('map','<%=path%>forest_hills_11375.gif')" class="info"><span class="time_stamp">03</span> Forest Hills</a><br>
		<a href="javascript:swapImage('map','<%=path%>forest_hills_11374.gif')" class="info"><span class="time_stamp">03</span> Forest Hills +</a><br>
		</div>
	</div>
	
	<div class="content_scroll" style="width: 56%; height: 75%; float: left; padding-left: 20px;">
		<img src="/media_stat/crm/images/clear.gif" name="map">
	</div>
	
	<br clear="all">
	</tmpl:put>

</tmpl:insert>