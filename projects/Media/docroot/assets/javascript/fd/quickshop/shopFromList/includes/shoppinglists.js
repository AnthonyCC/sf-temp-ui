/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;
  var QSVersion = fd.utils.getActive("quickshop");

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

  var shoppinglists = Object.create(WIDGET,{
    signal:{
      value:'YOUR LISTS'
    },
    template:{
      value: QSVersion !== "2_0" ? quickshop.shoppinglistsQS22 : quickshop.shoppinglists
    },
    placeholder:{
      value:'#shoppinglists'
    },
    serialize:{
      value:function(element){
        var el = (element) ? element : $(this.placeholder);
        
        var selected = $('input[type="radio"]:checked',el).first();
        return { yourListId: (!!selected.size() && selected.val()) || null };
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
        var t = e.currentTarget;

        // dirty hack: clear preferences and departments
        this.DISPATCHER.signal('PREFERENCES', {});
        this.DISPATCHER.signal('DEPARTMENTS', {});
        $(this.placeholder).first().trigger('list-change');
      }
    }
  });

  shoppinglists.listen();
  
  $(document).on('click', shoppinglists.placeholder + ' .noitem input', function (e) { e.preventDefault();});
  $(document).on('change', shoppinglists.placeholder + ' input',shoppinglists.handleClick.bind(shoppinglists));

  fd.modules.common.utils.register("quickshop.shopFromList", "shoppinglists", shoppinglists, fd);
}(FreshDirect));
