/*global jQuery,browse,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var SIGNALTARGET = fd.common.signalTarget;

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
  
  var carouselType = Object.create(SIGNALTARGET,{
    signal:{
      value:'carouselType'
    },
    callback:{
      value:function(data){
        var queryParams = 'type=' + data.type;
        if (data.type==='search'){
          queryParams += '&productId=' + data.productId;
        }
        this.DISPATCHER.signal('server', {
          url: '/api/carousel?' + queryParams
        });
      }
    }
  });

  carouselType.listen();

  fd.modules.common.utils.register("browse", "carouselType", carouselType, fd);
}(FreshDirect));

(function (fd) {
  if (window.FreshDirect.browse.data.searchParams.pageType == 'PRES_PICKS') {
    fd.common.dispatcher.signal('server',{
      url:'/api/carousel?type=pres-picks',
      method:'GET'
    });
  }
}(FreshDirect));

(function (fd) {
  if (window.FreshDirect.browse.data.searchParams.pageType == 'SEARCH') {
	  try {
		fd.common.dispatcher.signal('server',{
		  url:'/api/carousel?type=search&productId=' + window.FreshDirect.browse.data.sections.sections[0].products[0].productId,
		  method:'GET'
		});  
	  } catch (e) {
		  /* no products, do nothing */
	  }
  }
}(FreshDirect));
