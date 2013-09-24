/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var deletefromlistpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: 'delete this item from list'
    },
    customClass: {
      value: 'deletefromlistpopup'
    },
    helpTemplate: {
      value: 'common.listhelppopup'
    },
    helpHeader: {
      value: 'about shopping lists'
    },
    bodyTemplate: {
      value: common.deletefromlistpopup
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '[data-component=product] button.deletefromlist'
    },
    popupId: {
      value: 'deletefromlistpopup'
    },
    popupConfig: {
      value: {
        openonclick: true,
        halign: 'right'
      }
    },
    open: {
      value: function (e) {
        var $t = $(e.currentTarget);

        this.render({ itemName: $t.attr('data-itemname') });
        this.popup.show($t);
        this.popup.clicked = true;
      }
    },
    close: {
      value: function () {
        this.popup.hide();
        $('#'+this.popupId).find(this.bodySelector).html('');
      }
    },
    deleteFromList: {
      value: function (changes) {
        var listId = this.popup.$trigger.attr('data-listid'),
            itemId = this.popup.$trigger.attr('data-itemid'),
            items = fd.components.AddToCart.atcFilter(fd.modules.common.productSerialize(this.popup.$trigger)),
            item = items && items[0];

        if (item) {
          item.lineId = itemId;
          item.deleteItem = true;

          this.DISPATCHER.signal('server',{
              url: '/api/qs/shoppingLists/'+listId+'/'+itemId,
              data: { data: JSON.stringify(item) },
              method: 'PUT'
          });
        }

        this.close();
      }
    }
  });

  $(document).on('click', deletefromlistpopup.trigger, deletefromlistpopup.open.bind(deletefromlistpopup));
  $(document).on('click', '#' + deletefromlistpopup.popupId + ' .deletefromlist-button-delete', deletefromlistpopup.deleteFromList.bind(deletefromlistpopup));
  $(document).on('click', '#' + deletefromlistpopup.popupId + ' .deletefromlist-button-cancel', deletefromlistpopup.close.bind(deletefromlistpopup));

  fd.modules.common.utils.register("modules.common", "deletefromlistpopup", deletefromlistpopup, fd);

}(FreshDirect));

