<?php
/**
 * RSS2 Feed Template for displaying RSS2 Posts feed.
 *
 * @package WordPress
 */



function get_fd_posts() {
	return query_posts();
}

function get_fd_category_rss($type = null) {
        if ( empty($type) )
                $type = get_default_feed();
        $categories = get_the_category();
        $tags = get_the_tags();
        $the_list = '';
	$fd_categories = array();
	$fd_tags = array();

        $filter = 'rss';
        if ( 'atom' == $type )
                $filter = 'raw';

        if ( !empty($categories) ) foreach ( (array) $categories as $category ) {
		$cat_desc = sanitize_term_field('name', $category->description, $category->term_id, 'category', $filter);
		$cat_name = sanitize_term_field('name', $category->name, $category->term_id, 'category', $filter);
		$the_list .= "<fd_category><![CDATA[" . @html_entity_decode( $cat_name, ENT_COMPAT, get_option('blog_charset') ) . "]]></fd_category>\n";
		$the_list .= "<fd_category_desc><![CDATA[" . @html_entity_decode( $cat_desc, ENT_COMPAT, get_option('blog_charset') ) . "]]></fd_category_desc>\n";
        }

        if ( !empty($tags) ) foreach ( (array) $tags as $tag ) {
		$fd_tag = sanitize_term_field('name', $tag->name, $tag->term_id, 'post_tag', $filter);
		$the_list .= "\t\t<fd_tag_link><![CDATA[<a href=\"" . get_tag_link($tag->term_id) . '">' . $tag->name . "</a>]]></fd_tag_link>\n";
        }


        return apply_filters('the_category_rss', $the_list, $type);
}



header('Content-Type: ' . feed_content_type('rss-http') . '; charset=' . get_option('blog_charset'), true);

echo '<?xml version="1.0" encoding="'.get_option('blog_charset').'"?'.'>'; ?>

<rss version="2.0"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:wfw="http://wellformedweb.org/CommentAPI/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:atom="http://www.w3.org/2005/Atom"
	xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
	xmlns:slash="http://purl.org/rss/1.0/modules/slash/"
	<?php do_action('rss2_ns'); ?>
>

<channel>
	<title><?php bloginfo_rss('name'); wp_title_rss(); ?></title>
	<atom:link href="<?php self_link(); ?>" rel="self" type="application/rss+xml" />
	<link><?php bloginfo_rss('url') ?></link>
	<description><?php bloginfo_rss("description") ?></description>
	<lastBuildDate><?php echo mysql2date('D, d M Y H:i:s +0000', get_lastpostmodified('GMT'), false); ?></lastBuildDate>
	<language><?php echo get_option('rss_language'); ?></language>
	<sy:updatePeriod><?php echo apply_filters( 'rss_update_period', 'hourly' ); ?></sy:updatePeriod>
	<sy:updateFrequency><?php echo apply_filters( 'rss_update_frequency', '1' ); ?></sy:updateFrequency>
	<?php do_action('rss2_head'); ?>
	<?php while( have_posts()) : the_post(); ?>
	<?php
	global $more;
	$more = 0;
	$post_time = get_post_time('Y-m-d H:i:s', true);
	$year = mysql2date('Y', $post_time, false);
	$month = mysql2date('m', $post_time, false);
	$day = mysql2date('d', $post_time, false);
	$categories = get_the_category();
	?>
	<item>
		<title><?php the_title_rss() ?></title>
		<link><?php the_permalink_rss() ?></link>
		<comments><?php comments_link_feed(); ?></comments>
		<pubDate><?php echo mysql2date('D, d M Y H:i:s +0000', $post_time, false); ?></pubDate>
		<dc:creator><?php the_author() ?></dc:creator>
		<fd_author_link><![CDATA[<?php the_author_posts_link() ?>]]></fd_author_link>
		<fd_post_day_link><![CDATA[<a class="myfd-day-link" href="<?php print get_day_link($year, $month, $day) ?>"><?php print "$month/$day/$year"?></a>]]></fd_post_day_link>
		<fd_category_link><![CDATA[<?php echo get_category_link($categories[0]->cat_ID) ?>]]></fd_category_link>
		<?php print get_fd_category_rss() ?>
		<fd_content><![CDATA[<?php the_content('(More...)') ?>]]></fd_content>
		<guid isPermaLink="false"><?php the_guid(); ?></guid>
		<wfw:commentRss><?php echo esc_url( get_post_comments_feed_link(null, 'rss2') ); ?></wfw:commentRss>
		<slash:comments><?php echo get_comments_number(); ?></slash:comments>
<?php rss_enclosure(); ?>
	<?php do_action('rss2_item'); ?>
	</item>
	<?php endwhile; ?>
</channel>
</rss>
