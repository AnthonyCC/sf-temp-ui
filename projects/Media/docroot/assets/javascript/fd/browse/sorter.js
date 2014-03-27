/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var cm = fd.components.coremetrics;

  var sorter = Object.create(WIDGET,{
    signal:{
      value:'sortOptions'
    },
    template:{
      value:browse.sortBar
    },
    placeholder:{
      value:'#sorter'
    },
    reset:{
      value:function(){
        $(this.placeholder+' button.selected').removeClass('selected');
      }
    },
    serialize:{
      value:function(element){
        var el = (element) ? element : $(this.placeholder),
            selected = $('button.selected', el);
        return selected.length ? {
          sortBy:selected.data('sortid'),
          orderAscending:selected.data('currentdirection')
        } : {};
      }
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents('[data-component="sorter"]');

        $('button.selected',parent).removeClass('selected');
        clicked.addClass('selected');

        clicked.data('currentdirection', clicked.data('direction'));

        if (cm) {
          cm.setEvent('sort');
        }

        parent.trigger('sorter-change');
      }
    }
  });

  sorter.listen();
  $(document).on('click',sorter.placeholder+' button',sorter.handleClick.bind(sorter));

  fd.modules.common.utils.register("browse", "sorter", sorter, fd);
}(FreshDirect));
