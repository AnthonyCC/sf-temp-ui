/*global jQuery,browse,srch*/
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
    alignPairs: {
      value: function () {
        var crls = $('[data-component="carousel"]'),
            maxHeight = 0;

        if (crls.size() > 1) {
          crls.each(function () {  
            maxHeight = Math.max(maxHeight, $(this).height()); 
          });
          crls.each(function () {
            $(this).css({
              paddingBottom: maxHeight - $(this).height()
            });
          });
        }
      }
    },
    render:{
      value: function(data) {
        // render department header
        $('.browse-carousels-top').html(browse.topCarousels(data));
        $('.browse-carousels-bottom').html(browse.bottomCarousels(data));
        if (window.srch) {
          $('.srch-carousel').html(srch.carouselWrapper(data));
        }

        $('[data-component="carousel"]').each(function (i, el) {
          fd.components.carousel.changePage($(el), null);
        });

        this.alignPairs();
			}
    }
  });

  carousels.listen();
  carousels.alignPairs();

  fd.modules.common.utils.register("browse", "carousels", carousels, fd);
}(FreshDirect));
