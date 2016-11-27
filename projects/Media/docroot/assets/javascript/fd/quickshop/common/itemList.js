/*global quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  function setAdditionalInfos(v) {
    if(!v.itemId) {
      v.atcItemId="atcId-"+Date.now().toString(24)+'-'+Math.ceil(Math.random()*10000).toString(24);
    }
    if(v.quantity) {
      v.quantity.mayempty = true;
    }
    return v;
  }

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var QSVersion = fd.utils.getActive("quickshop");

  var itemList = Object.create(WIDGET,{
    signal:{
      value:'items'
    },
    template:{
      value: QSVersion !== "2_0" ? quickshop.itemlistQS22 : quickshop.itemlist
    },
    placeholder:{
      value:'#productlist'
    },
    scrollToTop: {
      value: function () {
        var mainEl = $('.qs-content').first(), crect;

        if (mainEl && mainEl.size()) {
          if (mainEl.hasClass('noscroll')) {
            mainEl.removeClass('noscroll');
          } else {
            crect = mainEl[0].getBoundingClientRect();

            if (crect.top < 0) {
              $.smoothScroll(crect.top + $('body').scrollTop());
            }
          }
        }
        if (fd.common.transactionalPopup) { fd.common.transactionalPopup.close(); }
      }
    },
    render:{
      value:function(data) {
        var $content = $('.qs-content').first();

        if ($content.hasClass('dontupdate')) {
          $content.removeClass('dontupdate');
          return;
        }

        data.itemType = fd.quickshop.itemType || 'general';
        data.searchTerm = $('#searchTerm').val();
        data.data=data.data.map(setAdditionalInfos);
        $content.attr('data-items', data.data.length);

        data.hasGrid = fd.quickshop.common.gridlistchange.getViewType() === 'grid';
        WIDGET.render.call(this,data);

        $('[data-component="product"]',$(this.placeholder)).each(function(index,element){
          fd.components.Subtotal.update(element);
        });
        fd.quickshop.common.tabMeta.update();
        //this.scrollToTop();
      }
    }
  });

  itemList.listen();

  fd.modules.common.utils.register("quickshop.common", "itemList", itemList, fd);
}(FreshDirect));
