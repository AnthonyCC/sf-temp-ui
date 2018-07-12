/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

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
            selected = $('button.selected', el),
            sselection = $('select.selected', el).val(),
            sortOptions = {};

        if (selected.length) {
          sortOptions = {
            sortBy: selected.data('sortid'),
            orderAscending: selected.data('currentdirection')
          };
        } else if (sselection) {
          sortOptions = {
            sortBy: sselection
          };
        }

        return sortOptions;
      }
    },
    handleClick:{
      value:function(clickEvent){
        var clicked = $(clickEvent.currentTarget),
            parent = clicked.parents('[data-component="sorter"]');

        $('button.selected',parent).removeClass('selected');
        clicked.addClass('selected');

        clicked.data('currentdirection', clicked.data('direction'));

        parent.trigger('sorter-change');
      }
    },
    handleSelect:{
      value:function(event){
        var dropdown = $(event.currentTarget),
            parent = dropdown.parents('[data-component="sorter"]'),
            value = dropdown.val();

        $('button.selected',parent).removeClass('selected');
        $('select',parent).removeClass('selected').each(function (el, i) {
          if ($(el).val() !== value) {
            $(el).val(null);
          }
        });
        dropdown.addClass('selected');

        parent.trigger('sorter-change');
      }
    }
  });

  sorter.listen();

  $(document).on('click',sorter.placeholder+' button.sorter-element',sorter.handleClick.bind(sorter));
  $(document).on('change',sorter.placeholder+' select',sorter.handleSelect.bind(sorter));

  fd.modules.common.utils.register("browse", "sorter", sorter, fd);
}(FreshDirect));
