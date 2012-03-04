<?php /* twitter api 
<script>!function(d,s,id){
	var js,fjs=d.getElementsByTagName(s)[0];
	if(!d.getElementById(id)){js=d.createElement(s);
	js.id=id;js.src="//platform.twitter.com/widgets.js";
	fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
</script>
*/?>

<table width="970" cellpadding="0" cellspacing="0" border="0">
	<tbody><tr>
		<td class="vgap">
			<img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="5" alt=""><br>
<center>

<!--[if IE 6]>
<div class="ie6">
<![endif]-->
<div class="globalnavcontainer" style="margin-top:5px">
	<div class="globalnav-left">
		<img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/globalnav_and_footer/footer_border_left.gif" width="11" height="22"/>
	</div>

	<div class="globalnav-footer">
		<div class="left">
			<span class="nodot" id="globalfooteritem-aboutus-pad"><a class="globalnavitem" id="globalfooteritem-aboutus" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=about&trk=bnav">About us</a></span>
			<span><a class="globalnavitem" id="globalfooteritem-careers" href="http://jobs-freshdirect.icims.com">Careers</a></span>
			<span><a class="globalnavitem" id="globalfooteritem-billorrights" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/category.jsp?catId=about_billrights&trk=bnav">Your Bill Of Rights</a></span>
		</div>
		<div class="right">
			<span class="nodot"><a class="globalnavitem globalnavitem-selected" id="globalfooteritem-myfd" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/myfd/index.jsp?trk=bnav">MYFD</a></span>
			<span><a class="globalnavitem" id="globalfooteritem-office" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=COS&trk=bnav">At The Office</a></span>
			<span><a class="globalnavitem" id="globalfooteritem-giftcards" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/gift_card/purchase/landing.jsp?trk=bnav">Gift Card</a></span>
			<span><a class="globalnavitem" id="globalfooteritem-newprod" href="<?php echo get_option('fdc_fd_storefront_base'); ?>/newproducts.jsp?trk=bnav">New Products</a></span>
		</div>		
	</div>
	<div class="globalnav-right">
		<img src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/globalnav_and_footer/footer_border_right.gif" width="11" height="22"/>
	</div>
</div>
<!--[if IE 6]>
</div>
<![endif]-->

</center>
	</td>
	</tr>

	<tr>
		<td align="center" class="text11bold" style="padding: 5px 0px;">
			<?php if (!fd_is_ie6()) { ?>
			<div class="addthis_toolbox addthis_default_style left" style="width: 270px; padding-top: 8px;">
				<a class="addthis_button_facebook_like" fb:like:href="http://www.facebook.com/FreshDirect" fb:like:layout="button_count" fb:like:width="95" fb:like:height="25"></a>
				<a class="addthis_button_twitter_follow_native" tf:screen_name="freshdirect" tf:width="80px" tf:show-screen-name="false" tf:show-count="false"></a>
				<div style="width: 81px; overflow: hidden;">
				<a class="addthis_button_google_plusone" g:plusone:href="https://plus.google.com/107881041343024996566" g:plusone:size="medium"></a>
				<div class="atclear"></div>
				</div>
				<div class="atclear"></div>
			</div>
			<script type="text/javascript">
				var addthis_config = {
					data_ga_property: 'UA-20535945-1',
					data_ga_social : true
				};
			</script>
			<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=ra-4d69806100449805"></script>
			<?php } else { ?>
			<div class="left" style="width: 270px; height: 20px; margin: 0px; padding: 0px;"></div>
			<?php } ?>
			<div style="float:left; width:1px; height:40px; margin: 0px 12px 0px 0px" class="dotted_separator_v"></div>
			<div style="float:left">
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/category.jsp?catId=mobile&trk=bnav"><img style="float: left" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/footer_parts_w/mobile_apps.gif" width="25" height="36" alt="Our Mobile Apps"/></a>
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/category.jsp?catId=mobile&trk=bnav" style="padding: 10px 5px 0px 5px; float: left">Our Mobile Apps</a>
			</div>
			<div style="float:left; width:1px; height:40px; margin: 0px 12px" class="dotted_separator_v"></div>
			<div style="float:left">
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=COS&trk=bnav"><img style="float: left" src="<?php bloginfo('template_url'); ?>/media_stat/images/navigation/footer_parts_w/at_the_office.gif" width="22" height="36" alt="At The Office"/></a>
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/department.jsp?deptId=COS&trk=bnav" style="padding: 10px 5px 0px 5px; float: left">At The Office</a>
			</div>
			<div style="float:left; width:1px; height:40px; margin: 0px 10px" class="dotted_separator_v"></div>
			<div style="float:right; padding: 10px 5px 0px 5px">
				<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/index.jsp">Home</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/your_account/manage_account.jsp">Your Account</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/index.jsp">Help/FAQ</a>
				&nbsp;&nbsp;<font color="#999999"><b>|</b></font>
				&nbsp;&nbsp;<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/index.jsp">Contact Us</a>
			</div>
		</td>
	</tr>
	<tr>
		<td align="center" class="text11bold">
			<div style="width:100%; height:1px" class="dotted_separator_h"></div>
		</td>
	<tr>
		<td align="center" class="text11" style="padding-top: 10px">
			&copy; 2002 - <?php echo date("Y"); ?> FreshDirect. All Rights Reserved.
			<br /><img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" width="1" height="6" alt="" />
			<br /><a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/privacy_policy.jsp">Privacy Policy</a>
			&nbsp;<font color="#999999">|</font>
			&nbsp;<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/terms_of_service.jsp">Customer Agreement</a>
			&nbsp;<font color="#999999">|</font>
			&nbsp;<a href="<?php echo get_option('fdc_fd_storefront_base'); ?>/help/aol_note.jsp">A note on images for AOL users</a>
		</td>
	</tr>
</tbody></table>
