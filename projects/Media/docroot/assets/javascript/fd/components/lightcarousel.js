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

    if (pager.length < 1) {
      pager = $('<ul class="pager"></ul>');
      carousel.append(pager);
    }

    pager.html(pagerContent);
  }

  /* hold math to avoid recalcs. assumes all carousels of N width should have the same width items */
  var changePageItemSizeCache = {};
  function changePage(carousel, direction, toPage) {
    carousel.removeClass('first').removeClass('last');
    var result;
    window.requestAnimationFrame(function() {
      var $mask = $('[data-component="carousel-mask"]', carousel );
      var maskWidth = $mask.width(),
          $list = $('[data-component="carousel-list"]', carousel ),
          elements = $list.children();

      if (!changePageItemSizeCache.hasOwnProperty(maskWidth)) {
          //decimals throw off calc
          changePageItemSizeCache[maskWidth] = elements.length ? Math.floor(elements[0].getBoundingClientRect().width) : 0;
      }

      var itemsize = changePageItemSizeCache[maskWidth],
          currentPage = carousel.data('carousel-page') || 0,
          itemPerPage = Math.floor(maskWidth / itemsize) || 1,
          nrPages = Math.ceil($list.children().length / itemPerPage),
          newPage, targetElem;

      elements.each(function(index, element) {
        $(element).attr('carousel-page-number', Math.floor(index / itemPerPage) + 1);
      });

      if (direction === "next") {
        newPage = nrPages - 1 > currentPage ? currentPage + 1 : nrPages - 1;
      } else if (direction === "prev") {
        newPage = currentPage > 0 ? currentPage - 1 : 0;
      } else {
        newPage = toPage || toPage === 0 ? toPage : currentPage;
      }
      if ( toPage === -1) {
        newPage = currentPage;
      }
      // str/int => int
      newPage = +newPage;

      try {
        targetElem = $(elements[newPage * itemPerPage]);
        result = $list.offset().left - targetElem.offset().left + parseInt(targetElem.css('margin-left'), 10) + $mask.offset().left;
      } catch (e) {}
      
      window.requestAnimationFrame(function() {
        if (newPage === 0) {
          carousel.addClass('first');
        }

        if (newPage >= nrPages - 1 ) {
          carousel.addClass('last');
        }
      });

      // product impression reporting based on pages
      var reportType = elements.filter('[data-impression-reported]').length ? 'impressionsPagination' : 'impressionsPushedCarousel';
      var vislibleProducts = elements.slice(newPage * itemPerPage, (newPage+1) * itemPerPage).filter(':not([data-impression-reported])');
      vislibleProducts.each(function (i, pEl) {
        pEl.setAttribute('data-impression-reported', 'true');
        
        //do criteo beacons
        if ($(pEl).attr('data-hooklogic-beacon-impress') !== undefined) {
          $(pEl).append('<img src="'+$(pEl).attr('data-hooklogic-beacon-impress')+'" alt="" style="display: none;" />');
          $(pEl).attr('data-hooklogic-beacon-impress', null); //remove
        }
      });

      if (vislibleProducts.length) {
        // give some time to the GTM module to initialize
        setTimeout(function () {
          fd.common.dispatcher.signal('productImpressions', {el: vislibleProducts, type: reportType});
        }, 10);
      }

      carousel.data('carousel-page', newPage);
      carousel.data('carousel-nrpages', nrPages);
      updatePager(carousel);

      if (newPage !== currentPage) {
        window.requestAnimationFrame(function() {
          carousel.addClass('stepping');
        });

        if (result || result === 0) {
          $('[data-component="carousel-list"]', carousel).offset({left :result});
        }

        setTimeout(function () {
          window.requestAnimationFrame(function() {
            carousel.removeClass('stepping');
          });
        }, 400);
        
        //lazy load on carousel "page" changes
        $(window).trigger('lazyLoad');
      }
    });

    return result;
  }


  var carousel = {
      updatePager: updatePager,
      changePage: changePage,
      handleClick:function(event){
        var carousel = $(event.currentTarget),
            direction;

        direction = $(event.target).data('carousel-nav');

        if (direction) {
          changePage(carousel, direction);
        }
      },
      initialize:function(){
        $('[data-component="carousel"]').each(function (i, el) {
          changePage($(el), null);
        });
      }
  };

  $(document).on('click','[data-component="carousel"]',carousel.handleClick.bind(carousel));
  $(document).on('keyup','[data-component="carousel"]', function (e) {
    if ((e.keyCode || e.which) === 9) {
      var carousel = $(e.currentTarget),
          $list = $('[data-component="carousel-list"]', carousel ),
          $mask = $('[data-component="carousel-mask"]', carousel ),
          elements = $list.children(),
          itemsize = elements[0].getBoundingClientRect().width,
          itemPerPage = Math.floor($mask.width() / itemsize) || 1,
          focusedElem = $(':focus').closest('li'),
          focusedElemIndex = elements.index(focusedElem),
          page = Math.floor(focusedElemIndex / itemPerPage);

      changePage(carousel, null, page);
    }
  });
  $(document).on('click','[data-component="carousel"] .pager [data-page]', function (e) {
    var el = $(e.currentTarget),
        page = el.attr('data-page');

    changePage(el.closest('[data-component="carousel"]'), null, page);
  });

  $(document).on('click','.last [data-carousel-view-all]',function (e) {
    var button = $(e.currentTarget);
    button.attr('data-carousel-show-view-all', 'true');
  });

  // initialize pager
  $(function() {
    $('[data-component="carousel"]').each(function (i, el) {
      changePage($(el), null);
    });
  });

  fd.modules.common.utils.register("components", "carousel", carousel, fd);
}(FreshDirect));
