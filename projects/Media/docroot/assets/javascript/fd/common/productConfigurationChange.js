var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var pcc = function (productId, delta, el) {
    $(el || document).trigger({
      type: 'productConfigurationChange',
      productId: productId,
      delta: delta
    });
  };

  $(document).on('change', '[data-productdata-name]', function (e) {
    var $t = $(e.currentTarget),
        $product = $t.parents('[data-component="product"]'),
        id = $product.find('[data-productdata-name="atcItemId"]').val() || $product.attr('data-product-id') || $product.attr('data-productid') || $product.find('[data-productdata-name="productId"]').val(),
        value = $t.val(),
        prop = $t.attr('data-productdata-name'),
        delta = {};

    delta[prop] = value;

    pcc(id, delta, $product);
  });

  $(document).on('productConfigurationChange', function (e) {
    var $product = $('[data-productdata-name="atcItemId"][value="'+e.productId+'"]').parents('[data-product-id], [data-productid]');

    if ($product.length < 1) {
      $product = $('[data-productid="'+e.productId+'"], [data-product-id="'+e.productId+'"]');
    }

    $product.addClass('changed');

    Object.keys(e.delta).forEach(function (k) {
      $product.find('[data-productdata-name="'+k+'"]').val(e.delta[k]);
      $product.find('[data-productdata-visualized="'+k+'"]').text(e.delta[k]);
    });
  });

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "productConfigurationChange", pcc, fd);
}(FreshDirect));
