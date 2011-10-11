<?php
/**
 * The Template for displaying all single posts.
 *
 * @package WordPress
 * @subpackage Twenty_Ten
 * @since Twenty Ten 1.0
 */

get_header(); ?>
<?php include('utils/fd_the_content.php');?>
		<div id="container">
			<div id="content" role="main">

<?php if ( have_posts() ) while ( have_posts() ) : the_post(); ?>
<?php 
	$categories = get_the_category();
	if ($categories[0]->slug == "uncategorized" ) $categories = array();
	$content = fd_the_content();
?>
	<?php if ( count($categories) ) { ?><div class="myfd-category myfd-archive-title"><?php 
		$cat_name = $categories[0]->description or $categories[0]->name; $cat_id = $categories[0]->ID;
		echo $cat_name; ?></div><?php } ?>
	<div id="nav-above" class="navigation">	
		<a href="javascript: history.back()">Back</a>
	</div>

	<div class="myfd-post">			
			
			<div class="myfd-post-content">
			<?php if (isset($content["media"])) {
				echo $content["media"];
			}?>						
				<div class="myfd-title title16">
					<?php the_title(); ?>
				</div>
				<?php echo $content["content"]?>
			</div>

			<div class="post-links">
				<div><?php fd_the_day_link()?> by <?php the_author_posts_link() ?>
					<?php if ( count( $categories ) && $categories[0]->slug != "uncategorized" ) : $cat_name = $categories[0]->description or $categories[0]->name; $cat_id = $categories[0]->ID; ?>
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
		
<?php endwhile; // end of the loop. ?>
		<div class="navigation">
			<?php if ( count( $categories ) ) : $cat_name = $categories[0]->description or $categories[0]->name; $cat_id = $categories[0]->ID; ?>
				<a href="<?php echo get_category_link($categories[0]->cat_ID)?>">Back to <?php echo $cat_name;?></a>
			<?php endif; ?>
		</div>

			</div><!-- #content -->
		</div><!-- #container -->

<?php get_sidebar(); ?>
<?php get_footer(); ?>
