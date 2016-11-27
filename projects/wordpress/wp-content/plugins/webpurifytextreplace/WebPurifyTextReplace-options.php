<?php
/*
Plugin Name: WebPurify Profanity Filter
Plugin URI: http://www.webpurify.com/wp-plugin.php
Version: 2.7
Author: WebPurify
Author URI: http://www.webpurify.com
Description: Uses the powerful WebPurify Profanity Filter API to stop profanity in comments.
*/
/*  Copyright 2010  WebPurify  (email : support@webpurify.com)

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
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

// webpurify service url
define( 'WEBPURIFY_URL', 'http://api1.webpurify.com/services/rest/?' );

// load plug-in translation domain
load_plugin_textdomain( 'WebPurifyTextReplace' );

// add wordpress actions
add_action( 'admin_menu', 'webpurify_options_page' );
add_action( 'comment_post', 'webpurify_comment_post' );

// add buddypress actions
if ( function_exists( 'bp_loaded' ) ) {
    webpurify_bp_init();
}

/**
 * Options page callback
 */
function webpurify_options_page() {
    add_options_page( 'WebPurify Options', 'WebPurify', 'manage_options','webpurifytextreplace/WebPurifyTextReplace.php' );
}

/**
 * Filter comment
 * @global object $wpdb global instance of wpdb
 * @param integer $commentID comment id
 */
function webpurify_comment_post($commentID) {
    global $wpdb;

    $table_name = $wpdb->prefix . 'comments';
    $getcomment = 'SELECT comment_content from ' . $table_name . ' where comment_ID = ' . (int)$commentID;
    $content = $wpdb->get_var( $getcomment );

    $ar = webpurify_query( $content );

    if ( !empty( $ar ) ) {
    	$update_comment = 'UPDATE ' . $table_name . ' SET comment_content = \'' . mysql_escape_string( $ar ) . '\' where comment_ID = ' . (int)$commentID;
    	$results = $wpdb->query( $update_comment );
    }
}

/**
 * Filter buddypress content
 *
 * @param string $content content to be checked
 * @param mixed $a
 * @param mixed $b
 * @param mixed $c
 * @return string filtered content
 */
function webpurify_bp_filter($content,$a = "", $b="", $c="") {
    $ar = webpurify_query( $content );
    return empty( $ar ) ? $content : $ar;
}

/**
 * Query the web purify service
 *
 * @param string $content content to be filtered
 * @return string filtered content
 */
function webpurify_query($content) {
    $options = webpurify_get_options();

    $params = array(
        'api_key' => $options['userkey'],
        'method' => 'webpurify.live.replace',
        'text' => $content,
        'replacesymbol' => $options['repc'],
        'lang' => $options['wplang'],
        'cdata' => 1,
        'plugin' => 'wp'
    );

    $encoded_params = array();

    foreach ( $params as $k => $v ) {
        $encoded_params[] = urlencode( $k ) . '=' . urlencode( $v );
    }

    $url = WEBPURIFY_URL . implode( '&', $encoded_params );
    $response = simplexml_load_file( $url, 'SimpleXMLElement', LIBXML_NOCDATA );
    return $response->text;
}

/**
 * Init buddypress - hook to buddypress filters
 */
function webpurify_bp_init() {
    $filter = 'webpurify_bp_filter';

    $tags = array(
        'groups_activity_new_update_content',
        'groups_activity_new_forum_post_content',
        'groups_activity_new_forum_topic_content',
        'bp_activity_comment_content',
        'bp_activity_new_update_content',
        'bp_blogs_activity_new_comment_content',
        'group_forum_topic_title_before_save',
        'group_forum_topic_text_before_save',
        'bp_activity_post_update_content',
        'bp_activity_post_comment_content',
        'group_forum_post_text_before_save',
        'bp_get_activity_latest_update',
        'bp_get_member_latest_update',
		'bbp_get_reply_content',
        'bbp_get_topic_content',
		'bbp_get_topic_title'
    );


    foreach( $tags as $tag ) {
        add_filter( $tag, $filter );
    }
}

/**
 * Get this plug-in options, init if they don't exists.
 * @return array associative array of options (userkey, wplang, repc)
 */
function webpurify_get_options() {
    $options = array();
    $defaults = array(
        'userkey' => '',
        'wplang' => 'en',
        'repc' => '*'
    );

    $options['userkey'] = get_option( 'webpurify_userkey' );
    $options['wplang'] = get_option( 'webpurify_lang' );
    $options['repc'] = get_option( 'webpurify_r' );

    foreach( $options as $option_name => $option_value ) {
        if ( !$option_value && isset( $defaults[$option_name] ) ) {
            add_option( $option_name, $defaults[$option_name] );
            $options[$option_name] = $option_value;
        }
    }
    
    return $options;
}

/**
 * Get available languages
 * @return array associative array of languages in format language code => human name
 */
function webpurify_get_languages() {
    $languages = array(
        'en' => 'English',
        'ar' => 'Arabic',
        'zh' => 'Chinese',
        'fr' => 'French',
        'de' => 'German',
        'hi' => 'Hindi',
        'it' => 'Italian',
        'jp' => 'Japanese',
        'kr' => 'Korean',
        'pt' => 'Portuguese',
        'pa' => 'Punjabi',	   
        'ru' => 'Russian',
        'sp' => 'Spanish',        
        'th' => 'Thai',
        'tr' => 'Turkish'        
    );
    
    return $languages;
}
