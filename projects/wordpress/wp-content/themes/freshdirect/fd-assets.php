<script src="<?php bloginfo('template_url'); ?>/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
<script src="<?php bloginfo('template_url'); ?>/assets/javascript/1.9.0/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
<script src="<?php bloginfo('template_url'); ?>/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
<script src="<?php bloginfo('template_url'); ?>/assets/javascript/common_javascript.js" type="text/javascript" language="javascript"></script>
<script type="text/javascript" language="javascript" src="<?php bloginfo('template_url'); ?>/assets/javascript/cufon-yui.js"></script>
<script type="text/javascript" language="javascript" src="<?php bloginfo('template_url'); ?>/assets/javascript/EagleCufon.font.js"></script>
<script type="text/javascript" language="javascript" src="<?php bloginfo('template_url'); ?>/assets/javascript/EagleCufonBold.font.js"></script>
<script type="text/javascript" language="javascript">
	Cufon.replace('.myfd-category ', { fontFamily: 'EagleCufon' });
	Cufon.replace('.myfd-category strong', { fontFamily: 'EagleCufonBold' });
	Cufon.replace('.myfd-category b', { fontFamily: 'EagleCufonBold' });
</script>
<script>
var a2a_config = {};
a2a_config.color_link_text = "336600";
</script>

<!--[if IE]>
<style type="text/css">
.addtoany_list a img{filter:alpha(opacity=70)}
.addtoany_list a:hover img,.addtoany_list a.addtoany_share_save img{filter:alpha(opacity=100)}
</style>
<![endif]-->
<?php 
function getAdd2AnyLink(){
    $the_permalink= apply_filters('the_permalink', get_permalink());
	return $the_permalink."?fd_social-_-social_share-_-social_share-_-".rawurlencode($the_permalink);
}
?> 

<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/assets/css/modalbox.css" type="text/css" />
<?php if (strpos($_SERVER['HTTP_USER_AGENT'], 'Mac') !== false) { ?>
<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/assets/css/mac_ie.css" type="text/css" />
<?php } else { ?>
<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/assets/css/pc_ie.css" type="text/css" />
<?php } ?>
<!--[if lte IE 6]>
<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/assets/css/pngfix_ie6.css" type="text/css">
<![endif]--> 
<link href="<?php bloginfo('template_url'); ?>/assets/css/wine.css" rel="stylesheet" type="text/css" />
<!--[if lte IE 7]>
<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/assets/css/wine-ie.css" type="text/css">
<![endif]-->
<link href="<?php bloginfo('template_url'); ?>/assets/css/4mm.css" rel="stylesheet" type="text/css" />
<link href="<?php bloginfo('template_url'); ?>/assets/css/quickbuy.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript">
var OasConfig = {
		serverDomain: "<?php echo get_option('fdc_oasconfig_serverdomain') ?>",
		sitePage: "<?php echo get_option('fdc_oasconfig_sitepage') ?>"
};
</script>
<script src="<?php bloginfo('template_url'); ?>/js/oas.js" type="text/javascript" language="javascript"></script>
<link href="<?php bloginfo('template_url'); ?>/assets/css/common/globalnav_and_footer.css" rel="stylesheet" type="text/css" />
<link href="<?php bloginfo('template_url'); ?>/assets/css/common/typography.css" rel="stylesheet" type="text/css" />
<link href="<?php bloginfo('template_url'); ?>/assets/css/global.css" rel="stylesheet" type="text/css" />