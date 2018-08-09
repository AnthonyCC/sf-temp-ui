/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  function findSelected(element) {
    var a = this[element],
        selected = null;

    a.forEach(function (el) {
      if(el.selected) {
        selected = el;
        DISPATCHER.signal('listheader',{title:el.name});
      }
    });

    return selected;
  }

  var fdlists = Object.create(WIDGET,{
    signal:{
      value:'STARTER LISTS'
    },
    template:{
      value:quickshop.fdlists
    },
    placeholder:{
      value:'#shoppinglists'
    },
    serialize:{
      value:function(element){
        var el = (element) ? element : $(this.placeholder);
        
        var selected = $('input[type="radio"]:checked',el).first();
        return { starterListId: (!!selected.length && selected.val()) || null };
      }
    },
    render:{
      value:function(data){
        Object.keys(data).some(findSelected,data);
        WIDGET.render.call(this,data);        
      }
    },
    handleClick:{
      value:function(e){

        // dirty hack: clear preferences and departments
        this.DISPATCHER.signal('PREFERENCES', {});
        this.DISPATCHER.signal('DEPARTMENTS', {});
        
        $(this.placeholder).first().trigger('list-change');
      }
    }
  });

  fdlists.listen();

  $(document).on('change', fdlists.placeholder + ' input',fdlists.handleClick.bind(fdlists));

  fd.modules.common.utils.register("quickshop.FDLists", "fdlists", fdlists, fd);
}(FreshDirect));
