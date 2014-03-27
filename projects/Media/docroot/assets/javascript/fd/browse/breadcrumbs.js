/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var breadcrumbs = Object.create(WIDGET,{
    signal:{
      value:'breadCrumbs'
    },
    template:{
      value:browse.breadCrumb
    },
    placeholder:{
      value:'.browse-breadcrumbs'
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            menu = fd.browse.menu,
            parent = clicked.parents('[data-component="breadcrumbs"]');

        clickEvent.preventDefault();

        if (menu) {
          menu.setId(clicked.data('id'));
          menu.resetFilters();
        }

        parent.trigger('breadcrumbs-change');
      }
    }
  });

  breadcrumbs.listen();
  $(document).on('click',breadcrumbs.placeholder+' [data-component="breadcrumb"]',breadcrumbs.handleClick.bind(breadcrumbs));

  fd.modules.common.utils.register("browse", "breadcrumbs", breadcrumbs, fd);
}(FreshDirect));
