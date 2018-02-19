/*global jQuery,browse*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";
  
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var cm = fd.components.coremetrics;

  var filtertags = Object.create(WIDGET,{
    signal:{
      value:'filterLabels'
    },
    template:{
      value:browse.filterTags
    },
    placeholder:{
      value:'.browse-filtertags'
    },
    handleClick:{
      value:function(clickEvent){

    	  var keyCode = clickEvent.keyCode;
    	  if (clickEvent.type=="keyup"&&(keyCode!=13)){
    		  return;
    	  } 

        var clicked = $(clickEvent.currentTarget),
            menu = FreshDirect.browse.menu,
            parent = clicked.parents('[data-component="filtertags"]');

        clickEvent.preventDefault();

        if (menu) {
          menu.removeFilter(clicked.data('parentid'), clicked.data('id'));
        }

        clicked.remove();
        if (cm) {
          cm.setEvent('noevent', true);
        }

        parent.trigger('filtertags-change');
      }
    }
  });

  filtertags.listen();
  $(document).on('click',filtertags.placeholder+' [data-component="filtertag"]',filtertags.handleClick.bind(filtertags));
  $(document).on('keyup',filtertags.placeholder+' [data-component="filtertag"]',filtertags.handleClick.bind(filtertags));
  fd.modules.common.utils.register("browse", "filtertags", filtertags, fd);
}(FreshDirect));
