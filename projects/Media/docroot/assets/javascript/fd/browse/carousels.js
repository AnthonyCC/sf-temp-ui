/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var carousels = Object.create(WIDGET,{
    signal:{
      value:'carousels'
    },
    template:{
      value:browse.carousels
    },
    placeholder:{
      value:'.browse-carousels'
    },
    render:{
      value: function(data) {
        // render department header
        $('.browse-carousels-top').html(browse.topCarousels(data));
        $('.browse-carousels-bottom').html(browse.bottomCarousels(data));

        $('[data-component="carousel"]').each(function (i, el) {
          fd.components.carousel.changePage($(el), null);
        });

			}
    }
  });

  carousels.listen();

  fd.modules.common.utils.register("browse", "carousels", carousels, fd);
}(FreshDirect));
