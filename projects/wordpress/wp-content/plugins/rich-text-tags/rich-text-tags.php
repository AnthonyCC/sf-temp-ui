<?php
/*
Plugin Name: Rich Text Tags, Categories, and Taxonomies
Plugin URI: http://www.seodenver.com/rich-text-tags/
Description: This plugin offers rich text editing capabilities for descriptions of tags, categories, and taxonomies.
Author: Katz Web Services, Inc.
Version: 1.6.2
Author URI: http://www.katzwebservices.com
*/

/*  Copyright 2012 Katz Web Services, Inc.  (email : info@katzwebservices.com)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


add_action('init', 'kws_rich_text_tags', 9999);
function kws_rich_text_tags() {
	
	global $wpdb, $user, $current_user, $pagenow, $wp_version;
	
	load_plugin_textdomain( 'rich-text-tags', false, '/rich-text-tags/languages' ); // I18n
	
	// ADD EVENTS
	if(
	$pagenow == 'edit-tags.php' ||
	$pagenow == 'categories.php' ||
	$pagenow == 'profile.php' ||
	$pagenow == 'user-edit.php'
	) {
		if(!user_can_richedit()) { return; }

		wp_enqueue_script('kws_rte', plugins_url('kws_rt_taxonomy.js', __FILE__), array('jquery'));

		$taxonomies = get_taxonomies();
		
		foreach($taxonomies as $tax) {
			add_action($tax.'_edit_form_fields', 'kws_add_form');
			add_action($tax.'_add_form_fields', 'kws_add_form');
		}
		
		if($pagenow == 'edit-tags.php' && isset($_REQUEST['action']) && $_REQUEST['action'] == 'edit' && empty($_REQUEST['taxonomy'])) {
			add_action('edit_term','kws_rt_taxonomy_save');
		}
		
		foreach ( array( 'pre_term_description', 'pre_link_description', 'pre_link_notes', 'pre_user_description' ) as $filter ) {
			remove_filter( $filter, 'wp_filter_kses' );
		}
		
		add_action('show_user_profile', 'kws_add_form', 1);
		add_action('edit_user_profile', 'kws_add_form', 1);
		add_action('edit_user_profile_update', 'kws_rt_taxonomy_save');
	}
	
	// Enable shortcodes in category, taxonomy, tag descriptions
	if(function_exists('term_description')) {
		add_filter('term_description', 'do_shortcode');
	} else {
		add_filter('category_description', 'do_shortcode');
	}
}

// PROCESS FIELDS
function kws_rt_taxonomy_save() {
	global $tag_ID;
	$a = array('description');
	foreach($a as $v) {
		wp_update_term($tag_ID,$v,$_POST[$v]);
	}
}


function kws_add_form($object = ''){
	global $pagenow;
	
	$css = '
	<style type="text/css">
		.wp-editor-container .quicktags-toolbar input.ed_button {
			width:auto;
		}
		.html-active .wp-editor-area { border:0;}
	</style>';

	// This is a profile page
	if(is_a($object, 'WP_User')) {
		$content = html_entity_decode(get_user_meta($object->ID, 'description', true));
		$editor_selector = $editor_id = 'description';
		?>
	<table class="form-table rich-text-tags">
	<tr>
		<th><label for="description"><?php _e('Biographical Info'); ?></label></th>
		<td><?php wp_editor($content, $editor_id, 
			array(
				'textarea_name' => $editor_selector, 
				'editor_css' => $css,
			)); ?><br />
		<span class="description"><?php _e('Share a little biographical information to fill out your profile. This may be shown publicly.'); ?></span></td>
	</tr>
<?php
	} 
	// This is a taxonomy
	else {
		$content = is_object($object) && isset($object->description) ? html_entity_decode($object->description) : '';
		
		if( in_array($pagenow, array('edit-tags.php')) ) {
			$editor_id = 'tag_description';
			$editor_selector = 'description';
		} else {
			$editor_id = $editor_selector = 'category_description';
		}
		
		?>
<tr class="form-field">
	<th scope="row" valign="top"><label for="description"><?php _ex('Description', 'Taxonomy Description'); ?></label></th>
	<td><?php wp_editor($content, $editor_id, 
		array(
			'textarea_name' => $editor_selector, 
			'editor_css' => $css,
		)); ?><br />
	<span class="description"><?php _e('The description is not prominent by default, however some themes may show it.'); ?></span></td>
</tr>
<?php 

	}

}

