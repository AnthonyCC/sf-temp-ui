/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
    
  function updatePager(carousel) {
    var currentPage = +carousel.data('carousel-page'),
        nrPages = +carousel.data('carousel-nrpages'),
        pager = carousel.find('ul.pager'),
        i, pagerContent = "";

    for (i = 0; i < nrPages; i++) {
      pagerContent += '<li data-page="'+i+'"'+(i === +currentPage ? ' class="current"': '')+'><span>'+(i+1)+'</span></li>';
    }

    if (pager.size() < 1) {
      pager = $('<ul class="pager"></ul>');
      carousel.append(pager);
    }

    pager.html(pagerContent);
  }

  function changePage(carousel, direction, toPage) {

    var $mask = $('[data-component="carousel-mask"]', carousel ),
        $list = $('[data-component="carousel-list"]', carousel ),
        elements = $list.children(),
        itemsize = elements.first().outerWidth(true),
        currentPage = carousel.data('carousel-page') || 0,
        itemPerPage = Math.floor($mask.width() / itemsize),
        nrPages = Math.ceil($list.children().size() / itemPerPage),
        newPage, result, targetElem;

    carousel.removeClass('first').removeClass('last');

    if (direction === "next") {
      newPage = nrPages - 1 > currentPage ? currentPage + 1 : nrPages - 1;
    } else if (direction === "prev") {
      newPage = currentPage > 0 ? currentPage - 1 : 0;
    } else {
      newPage = toPage || toPage === 0 ? toPage : currentPage;
    }
    // str/int => int
    newPage = +newPage;

    try {
      targetElem = $(elements[newPage * itemPerPage]);
      result = $list.offset().left - targetElem.offset().left + parseInt(targetElem.css('margin-left'), 10);
    } catch (e) {}

    console.log(nrPages, newPage);

    if (newPage === 0) {
      carousel.addClass('first');             
    }

    if (newPage >= nrPages - 1) {
      carousel.addClass('last');
    }

    carousel.data('carousel-page', newPage);
    carousel.data('carousel-nrpages', nrPages);
    updatePager(carousel);

    if (newPage !== currentPage) {
      carousel.addClass('stepping');

      if (result || result === 0) {
        $('[data-component="carousel-list"]', carousel).css("left", result + "px");
      }

      setTimeout(function () {
        carousel.removeClass('stepping');
      }, 400);
    }

    return result;
  }

  
  var carousel = {
      changePage: changePage,
      handleClick:function(event){
        var carousel = $(event.currentTarget), 
            direction;

        direction = $(event.target).data('carousel-nav');

        if (direction) {
          changePage(carousel, direction);
        }
      }
  };
  
  $(document).on('click','[data-component="carousel"]',carousel.handleClick.bind(carousel));  
  $(document).on('click','[data-component="carousel"] .pager [data-page]', function (e) {
    var el = $(e.currentTarget),
        page = el.attr('data-page');

    changePage(el.closest('[data-component="carousel"]'), null, page);
  });

  // initialize pager
  $('[data-component="carousel"]').each(function (i, el) {
    changePage($(el), null);
  });

  fd.modules.common.utils.register("components", "carousel", carousel, fd);
}(FreshDirect));
