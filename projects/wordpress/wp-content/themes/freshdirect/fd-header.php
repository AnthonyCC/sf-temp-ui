<script type="text/javascript" src="//libs.coremetrics.com/eluminate.js"></script>
<script type="text/javascript">
<?php
//defaults for test environment
if (!defined("COREMETRICS_CLIENT_ID")){
	define("COREMETRICS_CLIENT_ID", "60391309");
}
if (!defined("COREMETRICS_DATA_COLLECTION_METHOD")){
	define("COREMETRICS_DATA_COLLECTION_METHOD", "false");
}
if (!defined("COREMETRICS_DATA_COLLECTION_DOMAIN")){
	define("COREMETRICS_DATA_COLLECTION_DOMAIN", "testdata.coremetrics.com");
}
if (!defined("COREMETRICS_COOKIE_DOMAIN")){
	define("COREMETRICS_COOKIE_DOMAIN", "freshdirect.com");
}

//search parameters
$searchTermJsVar = get_search_query();
if ($searchTermJsVar == null){ 	
	$searchTermJsVar = "null";
	$searchCountJsVar = "null";
} else {
	$searchTermJsVar = '"' .$searchTermJsVar. '"';
	$searchCountJsVar = '"' .$wp_query->found_posts. '"';
}

//remove first slash
$uri = substr($_SERVER["REQUEST_URI"],1);
$page = "blog";
if (strlen($uri)>0){
	$page = $page . ": " . $uri;
}

?>
cmSetClientID("<?php echo COREMETRICS_CLIENT_ID?>", <?php echo COREMETRICS_DATA_COLLECTION_METHOD?>, "<?php echo COREMETRICS_DATA_COLLECTION_DOMAIN?>", "<?php echo COREMETRICS_COOKIE_DOMAIN?>");
cmCreatePageviewTag("<?php echo $page?>", "blog", <?php echo $searchTermJsVar ?>, <?php echo $searchCountJsVar ?>);
</script>

<style type="text/css">
#globalnavitem-flo { background-position: -292px -28px; width: 64px; }
#globalnavitem-pet { background-position: -358px -28px; width: 60px; }
#globalnavitem-cat { background-position: -877px 0px; width: 72px; }
#globalnavitem-bak { background-position: -814px 0px; width: 58px; }
#globalnavitem-hmr { background-position: -726px 0px; width: 83px; }
#globalnavitem-rtc { background-position: -610px 0px; width: 111px; }
#globalnavitem-fdi { background-position: -488px 0px; width: 117px; }
.globalnavitem { background: url('//freshdirect.com/media/layout/nav/images/topnav.png') no-repeat scroll 0 0 transparent; display: block; font-size: 13px; height: 23px; line-height: 23px; overflow: hidden; text-align: left; text-indent: -9999em; white-space: nowrap; }
</style>

<table width="970" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="182" ROWSPAN="3" valign="BOTTOM" style="height: 80px">
		    <a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/index.jsp"><img style="padding-bottom: 2px;" src="<?php bloginfo('template_url'); ?>/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect" name="FD_LOGO"></a>
 		</td>
		<td colspan="13" align="right" valign="bottom" class="reglog">
			<?php if(is_user_logged_in()){?>
				<a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/logout.jsp"><b>Click here to log out</b></a>
			<?php } else {?>
				<a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/login/login.jsp"><b>Click here to log in</b></a>
			<?php }?>	          
	    </td>
	</tr>

	<tr>
		<td colspan="13" class="vgap"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="21" alt="" border="0"></td>
	</tr>
	<tr valign="bottom">
		<td align="right" valign="bottom">
		<form name="search" action="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/search.jsp" method="get" style="margin: 0; padding: 0">
			<table width="120" cellpadding="0" cellspacing="0" border="0">
				<tr valign="bottom">
					<td width="5">&nbsp;</td>
					<td align="left" colspan="2" width="115"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/search.jsp"><img style="padding-left: 4px" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_search.gif" width="46" height="11" alt="SEARCH" hspace="4" vspace="1" align="bottom" border="0"></a></td>
				</tr>
				<tr valign="bottom">
					<td width="5">&nbsp;</td>
					<td width="284"><input type="text" name="searchParams" style="width:284px;" maxlength="100" value="" class="search"></td>
					<td width="41"><input type="image" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_find.gif" alt="Find" style="width: 35px; height: 14px; border: 0; padding: 3px 3px;"></td>
				</tr>
			</table>
		</form></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>

		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/view_cart.jsp?trk=gnav" onMouseover="swapImage('NAV_CART_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart_r.gif')" onMouseout="swapImage('NAV_CART_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart.gif" name="NAV_CART_IMG" width="54" height="26" alt="VIEW CART" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="58"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/checkout/view_cart.jsp?trk=gnav" onMouseover="swapImage('NAV_CHECKOUT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out_r.gif')" onMouseout="swapImage('NAV_CHECKOUT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out.gif" name="NAV_CHECKOUT_IMG" width="58" height="26" alt="CHECK OUT" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		
		<td width="70"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/your_account/delivery_info_avail_slots.jsp" onMouseover="swapImage('DELIVERY_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery_r.gif')" onMouseout="swapImage('DELIVERY_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery.gif" name="DELIVERY_IMG" height="26" alt="DELIVERY INFO" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/quickshop/index.jsp" onMouseover="swapImage('NAV_QUICKSHOP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop_r.gif')" onMouseout="swapImage('NAV_QUICKSHOP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif" name="NAV_QUICKSHOP_IMG" width="54" height="26" alt="QUICK SHOP" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="71"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/your_account/manage_account.jsp" onMouseover="swapImage('NAV_YOURACCOUNT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2_r.gif')" onMouseout="swapImage('NAV_YOURACCOUNT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2.gif" name="NAV_YOURACCOUNT_IMG" width="71" height="25" alt="YOUR ACCOUNT" border="0"></a></td>

		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/help/index.jsp" onMouseover="swapImage('NAV_HELP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help_r.gif')" onMouseout="swapImage('NAV_HELP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help.gif" name="NAV_HELP_IMG" width="54" height="26" alt="GET HELP" border="0"></a></td>
	</tr>
</table>



<!--[if IE 6]>
<div class="ie6">
<![endif]-->
<div class="globalnavcontainer" style="margin-top:6px">
	<div class="globalnav-left">
		<img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/globalnav_and_footer/header_border_left.png" width="11" height="50"/>
	</div>

	<div class="globalnav-top">
		<div class="left">
			<span class="nodot" id="globalnavitem-fru-pad"><a class="globalnavitem" id="globalnavitem-fru" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=fru&trk=gnav">Fruit</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-veg" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=veg&trk=gnav">Vegetables</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-mea" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=mea&trk=gnav">Meat</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-sea" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=sea&trk=gnav">Seafood</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-del" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=del&trk=gnav">Deli</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-che" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=che&trk=gnav">Cheese</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-dai" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=dai&trk=gnav">Dairy</a></span>
		</div>
		<div class="right">
			<span class="nodot"><a class="globalnavitem" id="globalnavitem-fdi" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=fdi&trk=gnav">4-Minute Meals</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-rtc" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=rtc&trk=gnav">Ready To Cook</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-hmr" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=hmr&trk=gnav">Heat &amp; Eat</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-bak" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=bak&trk=gnav">Bakery</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-cat" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=cat&trk=gnav">Catering</a></span>
		</div>
	</div>
	<div class="globalnav-middle"></div>
	<div class="globalnav-bottom">
		<div class="left">
			<span class="nodot" id="globalnavitem-wgd-pad"><a class="globalnavitem" id="globalnavitem-wgd" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=wgd&trk=gnav">What's Good</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-orgnat" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=orgnat&trk=gnav">Organic</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-local" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=local&trk=gnav">Local</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-kos" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=kos&trk=gnav">Kosher</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-flo" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=flo&trk=gnav">Flowers</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-pet" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=pet&trk=gnav">Pet</a></span>
		</div>
		<div class="right">
			<span class="nodot"><a class="globalnavitem" id="globalnavitem-pas" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=pas&trk=gnav">Pasta</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-cof" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=cof&trk=gnav">Coffee</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-gro" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=gro&trk=gnav">Grocery</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-hba" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=hba&trk=gnav">Health &amp; Beauty</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-big" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=big&trk=gnav">Buy Big</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-fro" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=fro&trk=gnav">Frozen</a></span>
			<span class="dot"><a class="globalnavitem" id="globalnavitem-usq" href="<?php echo get_option('fdc_fd_storefront_base', 'http://www.freshdirect.com'); ?>/department.jsp?deptId=usq&trk=gnav">Wine</a></span>
		</div>
	</div>
	<div class="globalnav-right">
		<img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/globalnav_and_footer/header_border_right.png" width="11" height="50"/>
	</div>
</div>
<!--[if IE 6]>
</div>
<![endif]-->