<?php
/**
 * The Header for our theme.
 *
 * Displays all of the <head> section and everything up till <div id="main">
 *
 * @package WordPress
 * @subpackage Twenty_Ten
 * @since Twenty Ten 1.0
 */
?><!DOCTYPE html>
<html <?php language_attributes(); ?>>
<head>
<meta charset="<?php bloginfo( 'charset' ); ?>" />
<title><?php
	/*
	 * Print the <title> tag based on what is being viewed.
	 */
	global $page, $paged;

	wp_title( '|', true, 'right' );

	// Add the blog name.
	bloginfo( 'name' );

	// Add the blog description for the home/front page.
	$site_description = get_bloginfo( 'description', 'display' );
	if ( $site_description && ( is_home() || is_front_page() ) )
		echo " | $site_description";

	// Add a page number if necessary:
	if ( $paged >= 2 || $page >= 2 )
		echo ' | ' . sprintf( __( 'Page %s', 'twentyten' ), max( $paged, $page ) );

	?></title>
<link rel="profile" href="http://gmpg.org/xfn/11" />
<?php if (!isset($_GET["print"])) :?>
<link rel="stylesheet" type="text/css" media="all" href="<?php bloginfo( 'stylesheet_url' ); ?>" />
<?php else :?>
<link rel="stylesheet" type="text/css" media="all" href="<?php bloginfo( 'stylesheet_url' ); ?>.print.css" />
<?php endif?>
<link rel="pingback" href="<?php bloginfo( 'pingback_url' ); ?>" />
<?php 
	include('fd-assets.php');
	/* We add some JavaScript to pages with the comment form
	 * to support sites with threaded comments (when in use).
	 */
	if ( is_singular() && get_option( 'thread_comments' ) )
		wp_enqueue_script( 'comment-reply' );

	/* Always have wp_head() just before the closing </head>
	 * tag of your theme, or you will break many plugins, which
	 * generally use this hook to add elements to <head> such
	 * as styles, scripts, and meta tags.
	 */
	wp_head();
?>
</head>

<body <?php body_class('text10'); ?>>
<div id="wrapper" class="hfeed">
	<div id="header">
		<div id="masthead">
			<?php include('fd-header.php')?>
		</div><!-- #masthead -->
	</div><!-- #header -->

	<div id="main">
	<table class="main-frame"><tr>
		<td class="frame-corner"><img src="<?php bloginfo('template_url'); ?>/images/corner_top_left.png" width="5" height="5" border="0"></td>
		<td class="frame-top"></td>
		<td class="frame-corner"><img src="<?php bloginfo('template_url'); ?>/images/corner_top_right.png" width="5" height="5" border="0"></td>
	</tr></table>

	<div class="main-frame-side text12">
	<img src="<?php bloginfo('template_url'); ?>/media_stat/images/layout/clear.gif" height="15" width="733">
	<br>
	<div style="padding: 0px 25px; margin-bottom: 30px;" class="main-frame-side-header">
	<div id="fd-main-header" style="height: 79px;">
		<a href="<?php echo home_url()?>"><img src="<?php bloginfo('template_url'); ?>/images/thefeed_header.gif"></a>
		<div class="fd-social-links" style="position: absolute; bottom: 0px; right: 0px;">
		<div class="text10bold" style="text-align: center; padding-bottom: 3px;">Follow us!</div>
		<div>
		<a href="http://twitter.com/freshdirect" target="_blank"><img src="<?php bloginfo('template_url'); ?>/images/twitter-icon.png" width="16" height="16" border="0"></a>
		<a href="http://facebook.com/freshdirect" target="_blank"><img src="<?php bloginfo('template_url'); ?>/images/facebook-icon.png" width="16" height="16" border="0"></a>
		<a href="<?php echo home_url()?>/feed/" target="_blank"><img src="<?php bloginfo('template_url'); ?>/images/rss-icon.png" width="16" height="16" border="0"></a>
		</div>
		</div>
	</div>
	<div style="font-size: 0px; height: 2px; background-color: #dadada; margin-top: 20px;" class="fd-main-underneath-header">&nbsp;</div>
	</div>

