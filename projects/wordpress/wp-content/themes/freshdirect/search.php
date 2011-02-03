<?php
/**
 * The template for displaying Search Results pages.
 *
 * @package WordPress
 * @subpackage Twenty_Ten
 * @since Twenty Ten 1.0
 */

get_header(); ?>

		<div id="container">
			<div id="content" role="main">
<?php if ( have_posts() ) :?>
<?php
	global $wp_query;
	// $count = sizeof( $wp_query->posts );
	$count = $wp_query->found_posts;
	$postCount = "1 post";
	if ($count > 1) {
		$postCount = $count." posts";
	}
?>

				<div class="myfd-search-result myfd-archive-title"><strong style="color: black"><?php echo $postCount?> found related to "<?php echo get_search_query()?>" </strong></div>
				<?php include('fd_nav_above.php');?>
<?php if (function_exists('relevanssi_the_excerpt')) { relevanssi_the_excerpt(); }; ?>

				<?php
				/* Run the loop for the search to output the results.
				 * If you want to overload this in a child theme then include a file
				 * called loop-search.php and that will be used instead.
				 */
				 get_template_part( 'loop', 'search' );
				?>
<?php else : ?>
				<div id="post-0" class="post no-results not-found">
					<h2 class="entry-title"><?php _e( 'Nothing Found', 'twentyten' ); ?></h2>
					<div class="entry-content">
						<p><?php _e( 'Sorry, but nothing matched your search criteria. Please try again with some different keywords.', 'twentyten' ); ?></p>
						<?php get_search_form(); ?>
					</div><!-- .entry-content -->
				</div><!-- #post-0 -->
<?php endif; ?>
			</div><!-- #content -->
		</div><!-- #container -->

<?php get_sidebar(); ?>
<?php get_footer(); ?>
