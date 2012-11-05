(function ($) {
  $(".smartstore-carousel-item").each(function (i, el) {
    var $el = $(el),
        $link = $el.find('.product-name-link'),
        $qb = $el.find('.qbLaunchButton');
    $el.delegate('.product-image-link, .product-image-burst-link, .grid-item-image a, .grid-item-rating a, .grid-item-name a', 'click', function (e) {
      if ($link && $qb.length === 0) {
        $link.click();
        window.location.href = $link[0].href;
        e.preventDefault();
      }
    });
  });
})(jQuery);