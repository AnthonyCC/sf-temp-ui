(function ($) {
  $(".smartstore-carousel-item-grocery-layout").each(function (i, el) {
    $(el).delegate('.product-image-link, .product-image-burst-link', 'click', function (e) {
    	var productId = $(e.target).parents('.product-image-container').attr('id');
        var linkId = '#'+productId+'_product_name';
    	var $link = $(linkId);     	
    	$link.click();
        window.location.href = $link[0].href;
        e.preventDefault();
    });
  });
})(jQuery);