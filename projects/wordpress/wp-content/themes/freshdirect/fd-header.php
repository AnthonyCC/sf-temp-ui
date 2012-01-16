<table width="970" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="182" ROWSPAN="3" valign="BOTTOM" style="height: 80px">
		    <a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/index.jsp"><img style="padding-bottom: 2px;" src="<?php bloginfo('template_url'); ?>/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect" name="FD_LOGO"></a>
 		</td>
		<td colspan="13" align="right" valign="bottom" class="reglog">
			<?php if(is_user_logged_in()){?>
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/logout.jsp"><b>Click here to log out</b></a>
			<?php } else {?>
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/login/login.jsp"><b>Click here to log in</b></a>
			<?php }?>	          
	    </td>
	</tr>

	<tr>
		<td colspan="13" class="vgap"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="21" alt="" border="0"></td>
	</tr>
	<tr valign="bottom">
		<td align="right" valign="bottom">
		<form name="search" action="<?php echo get_option('fdc_fd_storefront_base'); ?>/search.jsp" method="get" style="margin: 0; padding: 0">
			<table width="120" cellpadding="0" cellspacing="0" border="0">
				<tr valign="bottom">
					<td width="5">&nbsp;</td>
					<td align="left" colspan="2" width="115"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/search.jsp"><img style="padding-left: 4px" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_search.gif" width="46" height="11" alt="SEARCH" hspace="4" vspace="1" align="bottom" border="0"></a></td>
				</tr>
				<tr valign="bottom">
					<td width="5">&nbsp;</td>
					<td width="284"><input type="text" name="searchParams" style="width:284px;" maxlength="100" value="" class="search"></td>
					<td width="41"><input type="image" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_find.gif" style="width: 35px; height: 14px; border: 0; padding: 3px 3px;"></td>
				</tr>
			</table>
		</form></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>

		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/view_cart.jsp?trk=gnav" onMouseover="swapImage('NAV_CART_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart_r.gif')" onMouseout="swapImage('NAV_CART_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_view_cart.gif" name="NAV_CART_IMG" width="54" height="26" alt="VIEW CART" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="58"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/checkout/view_cart.jsp?trk=gnav" onMouseover="swapImage('NAV_CHECKOUT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out_r.gif')" onMouseout="swapImage('NAV_CHECKOUT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_check_out.gif" name="NAV_CHECKOUT_IMG" width="58" height="26" alt="CHECK OUT" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0" alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		
		<td width="70"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/your_account/delivery_info_avail_slots.jsp" onMouseover="swapImage('DELIVERY_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery_r.gif')" onMouseout="swapImage('DELIVERY_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/delivery.gif" name="DELIVERY_IMG" height="26" alt="DELIVERY INFO" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/quickshop/index.jsp" onMouseover="swapImage('NAV_QUICKSHOP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop_r.gif')" onMouseout="swapImage('NAV_QUICKSHOP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif" name="NAV_QUICKSHOP_IMG" width="54" height="26" alt="QUICK SHOP" border="0"></a></td>
		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="71"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/your_account/manage_account.jsp" onMouseover="swapImage('NAV_YOURACCOUNT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2_r.gif')" onMouseout="swapImage('NAV_YOURACCOUNT_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_your_account2.gif" name="NAV_YOURACCOUNT_IMG" width="71" height="25" alt="YOUR ACCOUNT" border="0"></a></td>

		<td width="11" align="center"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="11" height="1" border="0"  alt=""><br><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/999966.gif" width="1" height="24" alt=""></td>
		<td width="54"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/index.jsp" onMouseover="swapImage('NAV_HELP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help_r.gif')" onMouseout="swapImage('NAV_HELP_IMG','<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help.gif')"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/nav_button_get_help.gif" name="NAV_HELP_IMG" width="54" height="26" alt="GET HELP" border="0"></a></td>
	</tr>
</table>
	

<table width="970" border="0" cellpadding="0" cellspacing="0">
	<tr><td><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="6" alt="" /></td></tr>
	<tr><td><!-- <?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav/topnav_off.png -->
		<table border="0" cellpadding="0" cellspacing="0" class="globalnav">
		  <tr height="25" class="firstrow">
		    <td width="32" rowspan="2"><a href="/index.jsp"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/_home_u.png" alt="Home" width="32" height="25"/></a></td>
		    <td width="51"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=fru&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/fru.png" alt="Fruit" width="51" height="25"/></a></td>
		    <td width="78"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=veg&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/veg.png" alt="Vegetables" width="78" height="25"/></a></td>
		    <td width="48"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=mea&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/mea.png" alt="Meat" width="48" height="25"/></a></td>
		    <td width="66"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=sea&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/sea.png" alt="Seafood"/ width="66" height="25"></a></td>
		    <td width="44"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=del&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/del.png" alt="Deli" width="44" height="25"/></a></td>
		    <td width="56"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=che&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/che.png" alt="Cheese" width="56" height="25"/></a></td>
		    <td width="51"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=dai&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/dai.png" alt="Dairy" width="51" height="25"/></a></td>
		    <td width="132"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/00.png" width="132" height="25"/></td>
		    <td width="102"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=fdi&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/fdi.png" alt="4-Minute Meals" width="102" height="25"/></a></td>
		    <td width="99"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=rtc&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/rtc.png" alt="Ready to Cook" width="99" height="25"/></a></td>
		    <td width="76"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=hmr&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/hmr.png" alt="Heat & Eat" width="76" height="25"/></a></td>
		    <td width="58"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=bak&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/bak.png" alt="Bakery" width="58" height="25"/></a></td>
		    <td width="66"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=cat&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/cat.png" alt="Catering" width="66" height="25"/></a></td>
		    <td width="11"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/01_u.png" width="11" height="25"/></td>
		  </tr>
		</table>
	
		<table border="0" cellpadding="0" cellspacing="0">
		  <tr height="25" class="secondrow">
		    <td width="32" rowspan="2"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/index.jsp"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/_home_l.png" alt="Home" width="32" height="25"/></a></td>
		    <td width="92"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=wgd&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/wgd.png" alt="What's Good" width="92" height="25"/></a></td>
		    <td width="66"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=orgnat&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/orgnat.png" alt="Organic" width="66" height="25"/></a></td>
		    <td width="51"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=local&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/local.png" alt="Local" width="51" height="25"/></a></td>
		    <td width="61"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=kos&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/kos.png" alt="Kosher" width="61" height="25"/></a></td>
		    <td width="61"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=rec&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/rec.png" alt="Recipes" width="61" height="25"/></a></td>
		    <td width="152"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/02.png" width="152" height="25"/></td>
		    <td width="50"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=pas&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/pas.png" alt="Pasta" width="50" height="25"/></a></td>
		    <td width="58"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=cof&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/cof.png" alt="Coffee" width="58" height="25"/></a></td>
		    <td width="66"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=gro&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/gro.png" alt="Grocery" width="66" height="25"/></a></td>
		    <td width="109"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=hba&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/hba.png" alt="Health & Beauty" width="109" height="25"/></a></td>
		    <td width="57"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=big&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/big.png" alt="Buy Big" width="57" height="25"/></a></td>
		    <td width="58"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=fro&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/fro.png" alt="Frozen" width="58" height="25"/></a></td>
		    <td width="46"><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=usq&trk=gnav"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/usq.png" alt="USQ" width="46" height="25"/></a></td>
		    <td width="11"><img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/global_nav50/upper/01_l.png" width="11" height="25"/></td>
		  </tr>
		</table>
	</td></tr>
	<tr><td><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="3" alt="" /></td></tr>
</table>
