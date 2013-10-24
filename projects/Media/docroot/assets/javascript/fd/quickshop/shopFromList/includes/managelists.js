/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var DISPATCHER = fd.common.dispatcher;

  var manageshoppinglists = Object.create(POPUPWIDGET,{
    signal:{
      value:'listInfos'
    },
    serialize:{
      value:function(element){
        return {};
      }
    },
    hasClose: {
      value: true
    },
    headerContent: {
      value: 'manage your shopping lists'
    },
    customClass: {
      value: 'lists manageshoppinglists'
    },
    helpTemplate: {
      value: 'common.managelisthelp'
    },
    helpHeader: {
      value: 'about list manager'
    },
    bodyTemplate: {
      value: quickshop.manageshoppinglists
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '#shoppinglists .shoppinglists-manage'
    },
    popupId: {
      value: 'manageShoppingListsPopup'
    },
    popupConfig: {
      value: {
        openonclick: true
      }
    },
    open: {
      value: function (e) {
        DISPATCHER.signal('server',{
            url: '/api/shoppinglist'
        });

        this.popup.show($(e.currentTarget));
        this.popup.clicked = true;
      }
    },
    close: {
      value: function () {
        this.popup.hide();
        $('#'+this.popupId).find(this.bodySelector).html('');
      }
    },
    confirm: {
      value: function () {
        var changes = [], 
            $cnt = $('#' + this.popupId),
            $popupBody = $cnt.find(this.bodySelector),
        	lists = $.makeArray($('ul.manage li.item .name span',$cnt)).map(function(item){
        		return item.innerHTML;
        	}),isValid=true;
        
        
        $('ul.manage li.item', $cnt).each(function (i, el) {
          var listItem,
              id = $('.name', el).attr('data-listid'),
              oldname = $('.name span', el).text(),
              name = $('.rename input', el).val(),
              def = !!$('.default input', el).attr('checked'),
              del = !!$('.delete input', el).attr('checked'),
              validator = fd.modules.common.listValidator(name,lists);

          if(!validator.taken || (validator.taken && oldname === name )) {
              if (name || def || del) {
                  listItem = {
                    listId: id,
                    oldname: oldname,
                    name: name || "",
                    "delete": del,
                    "default": def
                  };
                  changes.push(listItem);
                }        	  
          } else {
        	  isValid = false;
        	  $('.error',el).html(validator.toString());
          }
        });

        if(isValid){
            $popupBody.html(quickshop.manageshoppinglistsconfirm({changes: changes}));

            $popupBody.find('.qs-popup-save-button').on('click', function (e) {
              changes.forEach(function (e) { delete e.oldname; });
              this.save(changes);
              this.close();
            }.bind(this));        	
        }
      }
    },
    save: {
      value: function (changes) {
        DISPATCHER.signal('server',{
            url: '/api/shoppinglist',
            method: 'PUT',
            data: {data: JSON.stringify({listInfos: changes})}
        });
      }
    }
  });

  manageshoppinglists.listen();

  $(document).on('click', manageshoppinglists.trigger, manageshoppinglists.open.bind(manageshoppinglists));
  $(document).on('click', '#' + manageshoppinglists.popupId + ' .qs-popup-cancel-button', manageshoppinglists.close.bind(manageshoppinglists));
  $(document).on('click', '#' + manageshoppinglists.popupId + ' .qs-popup-confirm-button', manageshoppinglists.confirm.bind(manageshoppinglists));

  fd.modules.common.utils.register("quickshop.shopFromList", "manageshoppinglists", manageshoppinglists, fd);
}(FreshDirect));
