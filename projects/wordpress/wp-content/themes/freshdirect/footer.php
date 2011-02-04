<?php
/**
 * The template for displaying the footer.
 *
 * Contains the closing of the id=main div and all content
 * after.  Calls sidebar-footer.php for bottom widgets.
 *
 * @package WordPress
 * @subpackage Twenty_Ten
 * @since Twenty Ten 1.0
 */
?>
	<div style="clear: both; font-size: 0px;"></div>
	<div style="display: none" class="print-copyright">
	&copy; 2002 - 2011 FreshDirect. All Rights Reserved. 
	</div>
	</div><!-- main-frame-side -->
	</div><!-- #main -->
	<div class="myfd-main-footer">
		<div class="main-frame-footer-side">

		<div class="myfd-footer-cell" style="width: 54%;">
			<div style="margin: 25px;">
				<div><img src="<?php bloginfo('template_url'); ?>/media_stat/images/myfd/fd_logo_sm_brown.gif" border="0" alt="FreshDirect" name="FD_LOGO" width="195" height="38"></div>
			FreshDirect is one of the nation's leading online grocers, known for our convenient home delivery of fresh, delicious food. Our ever-expanding service area includes New York City as well as parts of New Jersey, Connecticut, Westchester and Nassau County. Click here to visit <a href="http://www.freshdirect.com" alt="FreshDirect" title="FreshDirect">www.freshdirect.com</a>.
			</div>
		</div>
		<div class="myfd-footer-cell" style="width: 45%;">
			<div style="margin: 25px 25px 25px 0px;">
				<h1 style="margin: 15px 0px 5px 0px; padding: 0px">SEARCH OUR BLOG</h1>
				<div style="margin-bottom: 20px;">For topics, tags, authors and more.</div>
				<?php get_search_form(); ?>
			</div>
		</div>
		<div style="clear: both; font-size: 0px;"></div>

		</div>

		<table class="main-frame"><tr>
			<td class="frame-corner"><img src="<?php bloginfo('template_url'); ?>/images/corner_bottom_left.png" width="5" height="5" border="0"></td>
			<td class="frame-bottom"></td>
			<td class="frame-corner"><img src="<?php bloginfo('template_url'); ?>/images/corner_bottom_right.png" width="5" height="5" border="0"></td>
		</tr></table>

	</div>

	<div id="footer" role="contentinfo">
		<div id="colophon">

<?php
	include('fd-footer.php');
	/* A sidebar in the footer? Yep. You can can customize
	 * your footer with four columns of widgets.
	 */
	get_sidebar( 'footer' );
?>

		</div><!-- #colophon -->
	</div><!-- #footer -->

</div><!-- #wrapper -->

<?php
	/* Always have wp_footer() just before the closing </body>
	 * tag of your theme, or you will break many plugins, which
	 * generally use this hook to reference JavaScript files.
	 */

	wp_footer();
?>
</body>
</html>
