/*global jQuery,pdp,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var menu = Object.create(WIDGET,{
    signal:{
      value:'menuBoxes'
    },
    template:{
      value:browse.menu
    },
    placeholder:{
      value:'#leftnav'
    },
    serialize:{
      value:function(element){
        var el = (element) ? element : $(this.placeholder),
            idboxes = $('.menuBox[data-filter="id"]',el),
            result, filters = {};

        result = {
          id: this.id
        };

        $('[data-filter]').filter(function (i, el) {
          return $(el).data('filter') !== "id";
        }).each(function (i, el) {
          var selected = $('input', el).filter(function (i, el) { return el.checked; }).map(function (i, el) { return $(el).val(); });

          if (selected && selected.length) {
            filters[$(el).data('filter')] = [].slice.apply(selected);
          }
        });

        if (Object.keys(filters).length) {
          result.requestFilterParams = filters;
        }

        if ($('[data-component="menuitem"][data-urlparameter="all"] input').prop('checked')) {
          result.all = true;
        }

        return result;
      }
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            urlparameter = clicked.data('urlparameter'),
            parent = clicked.parents('[data-component="menu"]'),
            menubox = clicked.parents('[data-component="menubox"]'),
            id = null;

        if (clicked.hasClass('disabled')) {
          return;
        }

        if (menubox.data('type') !== 'MULTI') {
          $(".selected", menubox).removeClass('selected');
        }

        clicked.addClass('selected');
        if (menubox.data('filter') === 'id') {
          if (urlparameter === "all") {
            id = menubox.data('id');
          } else {  
            id = urlparameter;
          } 

          window.location.href='/browse.jsp?id='+id;
        }

        parent.trigger('menu-change');
      }
    }
  });

  menu.listen();
  $(document).on('click',menu.placeholder+' [data-component="menuitem"]', menu.handleClick.bind(menu));

  fd.modules.common.utils.register("pdp", "menu", menu, fd);
}(FreshDirect));
