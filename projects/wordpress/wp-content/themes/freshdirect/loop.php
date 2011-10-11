<?php
/**
 * The loop that displays posts.
 *
 * The loop displays the posts and the post content.  See
 * http://codex.wordpress.org/The_Loop to understand it and
 * http://codex.wordpress.org/Template_Tags to understand
 * the tags used in it.
 *
 * This can be overridden in child themes with loop.php or
 * loop-template.php, where 'template' is the loop context
 * requested by a template. For example, loop-index.php would
 * be used if it exists and we ask for the loop with:
 * <code>get_template_part( 'loop', 'index' );</code>
 *
 * @package WordPress
 * @subpackage Twenty_Ten
 * @since Twenty Ten 1.0
 */
?>

<?php include('utils/fd_the_content.php');?>

<?php /* Display navigation to next/previous pages when applicable */ ?>

<?php /* If there are no posts to display, such as an empty archive page */ ?>
<?php if ( ! have_posts() ) : ?>
	<div id="post-0" class="post error404 not-found">
		<h1 class="entry-title"><?php _e( 'Not Found', 'twentyten' ); ?></h1>
		<div class="entry-content">
			<p><?php _e( 'Apologies, but no results were found for the requested archive. Perhaps searching will help find a related post.', 'twentyten' ); ?></p>
		</div><!-- .entry-content -->
	</div><!-- #post-0 -->
<?php endif; ?>

<div class="myfd-blog-feed">

<?php
	$firstPost = (get_query_var('paged')) ? 1 : 0;
?>

<?php while ( have_posts() ) : the_post(); ?>
	<?php 
		$categories = get_the_category();
	?>
	<?php $content = fd_the_content(); if ($firstPost == 0) : $firstPost = 1; ?>
	<h3 class="myfd-most-recent">MOST RECENT</h3>
	<div class="myfd-onlyone">
		<div class="myfd-post">			
			
			<div class="myfd-post-content">
			<?php if (isset($content["media"])) {
				echo $content["media"];
			}?>						
				<div class="myfd-title title16">
					<a href="<?php the_permalink(); ?>" title="<?php printf( esc_attr__( 'Permalink to %s', 'twentyten' ), the_title_attribute( 'echo=0' ) ); ?>" rel="bookmark"><?php the_title(); ?></a>
				</div>
				<?php echo $content["content"]?>
			</div>

			<div class="post-links">
				<div><?php fd_the_day_link()?> by <?php the_author_posts_link() ?> 
					<?php if ( count( $categories )  && $categories[0]->slug != "uncategorized" ) : $cat_name = $categories[0]->description or $categories[0]->name; $cat_id = $categories[0]->ID;
					?>
						in <a href="<?php echo get_category_link($categories[0]->cat_ID)?>"><?php echo $cat_name;?></a>
					<?php endif; ?>
				</div>
				
				<div class="myfd-linkbar">
					<span class="myfd-share">											
						<span class="a2a_kit a2a_default_style">
							<a class="a2a_dd myfd-share-text" href="http://www.addtoany.com/share_save?linkurl=<?php the_permalink(); ?>&amp;linkname=<?php the_title(); ?>">Share &#9660;</a>							
							<a class="a2a_button_facebook"></a>
							<a class="a2a_button_twitter"></a>
						</span><script type="text/javascript">												
							a2a_config.linkname="<?php the_title(); ?>";
							a2a_config.linkurl="<?php the_permalink(); ?>";
//							a2a.init('page'); 
						</script><script type="text/javascript" src="http://static.addtoany.com/menu/page.js"></script><span class="a2a_kit-fd a2a_default_style-fd">
							<a class="a2a_button_email" href="mailto:?subject=<?php ; rawurlencode(the_title())?>&amp;body=<?php rawurlencode(the_permalink()); ?>"><span class="a2a_img a2a_img_text a2a_i_email"></span>Email</a>
							<a class="a2a_button_print" href="<?php the_permalink(); ?>?print=true" target="myfd_print"><span class="a2a_img a2a_img_text a2a_i_email"></span>Print</a>
						</span>
					</span>
					<?php
						$tags_list = get_the_tag_list( 'Tags: <span class="link">', '</span> <span class="link">', '</span>' );
						if ( $tags_list ): ?>
							<?php echo $tags_list?>
						<?php endif?>					
				</div>
						
			</div>
		</div>
	</div>
	<?php else : $firstPostClass = ''; ?>	
		<?php if ($firstPost == 1) : $firstPost = 2; $firstPostClass = ' myfd-more-post-first'; ?>
			<h3 class="myfd-most-recent">MORE POSTS</h3>
		<?php endif;?>
		<div class="myfd-more-post<?php echo $firstPostClass; ?>">
			<div class="myfd-title title16">
				<a href="<?php the_permalink(); ?>" title="<?php printf( esc_attr__( 'Permalink to %s', 'twentyten' ), the_title_attribute( 'echo=0' ) ); ?>" rel="bookmark"><?php the_title(); ?></a>
			</div>
			<div><?php fd_the_day_link()?> by <?php the_author_posts_link() ?>
				<?php if ( count( $categories )  && $categories[0]->slug != "uncategorized" ) : $cat_name = $categories[0]->description or $categories[0]->name; $cat_id = $categories[0]->ID; ?>
					in <a href="<?php echo get_category_link($categories[0]->cat_ID)?>"><?php echo $cat_name;?></a>
				<?php endif; ?>
			</div>
		</div>
	<?php endif; // This was the if statement that broke the loop into three parts based on categories. ?>

<?php endwhile; // End the loop. Whew. ?>
</div>

<div id="nav-below" class="navigation">
<?php if ( $wp_query->max_num_pages > 1 ) : ?>
		<?php if(function_exists('wp_paginate')) {
	                wp_paginate();
	        } ?>
<?php endif; ?>
<a href="<?php echo home_url()?>">Back to the FreshDirect Feed</a>
</div>


<!--
<?php /* Display navigation to next/previous pages when applicable */ ?>
<?php if (  $wp_query->max_num_pages > 1 ) : ?>
				<div id="nav-below" class="navigation">
					<div class="nav-previous"><?php next_posts_link( __( '<span class="meta-nav">&larr;</span> Older posts', 'twentyten' ) ); ?></div>
					<div class="nav-next"><?php previous_posts_link( __( 'Newer posts <span class="meta-nav">&rarr;</span>', 'twentyten' ) ); ?></div>
				</div>
<?php endif; ?>
-->
