<?php
/*
Plugin Name: Rich Text Biography
Plugin URI: http://www.ternstyle.us/products/plugins/wordpress/wordpress-rich-text-biography
Description: This plugin offers rich text capabilities for a user's biography when editing his/her profile.
Author: Matthew Praetzel
Version: 2.7
Author URI: http://www.ternstyle.us/
Licensing : http://www.ternstyle.us/license.html
*/

////////////////////////////////////////////////////////////////////////////////////////////////////
////	File:
////		tern_wp_rte_bio.php
////	Actions:
////		1) add rich text capabilities to Wordpress profile biography
////	Account:
////		Created on July 14th 2008
////	Version:
////		2.7
////
////	Written by Matthew Praetzel. Copyright (c) 2008 Matthew Praetzel.
////////////////////////////////////////////////////////////////////////////////////////////////////

/****************************************Commence Script*******************************************/

//                                *******************************                                 //
//________________________________** ADD EVENTS                **_________________________________//
//////////////////////////////////**                           **///////////////////////////////////
//                                **                           **                                 //
//                                *******************************                                 //
if($GLOBALS['pagenow'] == 'profile.php' or $GLOBALS['pagenow'] == 'user-edit.php') {
	add_action('init','tern_wp_rte_load_mce');
	add_action('wp_print_scripts','tern_wp_rte_scripts');
	if(floatval($GLOBALS['wp_version']) >= 2.7) {
		wp_enqueue_script('jquery');
		wp_enqueue_script('editor');
		wp_enqueue_script('thickbox');
		add_action('admin_head','wp_tiny_mce');
	}
}
add_action('profile_update','tern_wp_rte_save');
//                                *******************************                                 //
//________________________________** LOAD SCRIPTS              **_________________________________//
//////////////////////////////////**                           **///////////////////////////////////
//                                **                           **                                 //
//                                *******************************                                 //
function tern_wp_rte_load_mce() {
	if(floatval($GLOBALS['wp_version']) < 2.7) {
		wp_enqueue_script('tiny_mce');
	}
	wp_enqueue_script('tern_rte',get_bloginfo('home').'/wp-content/plugins/rich-text-biography/tern_wp_rte_bio.js');
}
function tern_wp_rte_scripts() {
	echo '<link rel="stylesheet" href="'.get_bloginfo('home').'/wp-content/plugins/rich-text-biography/tern_wp_rte_bio.css" type="text/css" media="all" />' . "\n";
}
//                                *******************************                                 //
//________________________________** PROCESS FIELDS            **_________________________________//
//////////////////////////////////**                           **///////////////////////////////////
//                                **                           **                                 //
//                                *******************************                                 //
function tern_wp_rte_save() {
	global $user_id;
	$a = array('description');
	foreach($a as $v) {
		$s = preg_replace("/([^\n\r]+)[\r\n]{1,2}/","<p>$1</p>",$_POST[$v]);
		update_usermeta($user_id,$v,$s);
	}
}

/****************************************Terminate Script******************************************/
?>