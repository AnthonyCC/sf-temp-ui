/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
    
  function getIndent( carousel, nextPage) {

    var $mask = $('[data-component="carousel-mask"]', carousel ),
        $list = $('[data-component="carousel-list"]', carousel ),
        elements = $list.children(),
        itemsize = elements.first().outerWidth(true),
        currentPage = carousel.data('carousel-page') || 0,
        itemPerPage = Math.floor($mask.width() / itemsize),
        nrPages = Math.ceil($list.children().size() / itemPerPage),
        newPage, result, targetElem;

    if (nextPage) {
      carousel.removeClass('first');

      newPage = nrPages - 1 > currentPage ? currentPage + 1 : nrPages - 1;
    } else {
      carousel.removeClass('last');

      newPage = currentPage > 0 ? currentPage - 1 : 0;
    }

    try {
      targetElem = $(elements[newPage * itemPerPage]);
      result = $list.offset().left - targetElem.offset().left + parseInt(targetElem.css('margin-left'), 10);
    } catch (e) {}

    if (newPage === 0) {
      carousel.addClass('first');             
    }

    if (newPage >= nrPages - 1) {
      carousel.addClass('last');
    }

    carousel.data('carousel-page', newPage);

    return result;
  }

  
  var carousel = {
      handleClick:function(event){
            var carousel = $(event.currentTarget), 
                direction,indent;

            if( direction = $(event.target).data('carousel-nav') ) {
                
                carousel.addClass('stepping');
  
                indent = getIndent(carousel, direction === "next" );
  
                if (indent || indent === 0) {
                  $('[data-component="carousel-list"]', carousel ).css("left", indent + "px");
                }

                setTimeout(function () {
                  carousel.removeClass('stepping');
                }, 400);
            }
      }
  };
  
    $(document).on('click','[data-component="carousel"]',carousel.handleClick.bind(carousel));  

  fd.modules.common.utils.register("components", "carousel", carousel, fd);
}(FreshDirect));
