<div id="nav-above" class="navigation">
<?php if ( $wp_query->max_num_pages > 1 ) : ?>
		<?php if(function_exists('wp_paginate')) {
	                wp_paginate();
	        } ?>
<?php endif; ?>
<a href="<?php echo home_url()?>">Back to the FreshDirect Feed</a>
</div>
