var FreshDirect = FreshDirect || {};

(function(fd, $) {
  var statusUpdater = function (node, msg) {
    var $node,
        $item;

    if (!node) {
      return;
    }

    $node = $(node);

    $node.find('.grid-item-status-added').html(msg);
    $node.find('.grid-item-status-in-cart').html($node.find('.grid-item-status-added .in-cart').html());
    $node.addClass('grid-item-status-visible');

    setTimeout(function () {
      $node.removeClass('grid-item-status-visible');
    }, 3000);

    $item = $node.parents('.grid-item');

    if ($item) {
      $item.addClass('in-cart');
      $item.addClass('grid-item-disabled');
    }

  };
	
	// register in fd namespace
	fd.modules.common.utils.register("modules.search", "statusUpdater", statusUpdater, fd);
  // backward compatibility...
	fd.modules.common.utils.register("FDSearch", "statusUpdater", statusUpdater, window);
}(FreshDirect, jQuery));

