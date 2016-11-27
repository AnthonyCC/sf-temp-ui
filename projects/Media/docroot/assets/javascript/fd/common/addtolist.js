/*global jQuery,common*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
  
  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var listPopup = Object.create(POPUPWIDGET,{
      signal:{
          value:'listInfos'
        },
        allowNull:{
        value:true  
        },
        update: {
          value: function (data) {
            this.DISPATCHER.signal('server', {
              url: '/api/shoppinglist',
              data: {
                data:JSON.stringify(data || {})
              }
            });
          }
        },
        headerContent: {
          value: '',
          writable:true
        },
        customClass: {
          value: 'addtolistpopup'
        },
        helpTemplate: {
          value: 'common.listhelppopup'
        },
        helpHeader: {
          value: 'About Shopping Lists'
        },
        bodyTemplate: {
          value: null,
          writable:true
        },
        $trigger: {
          value: null,
          writable:true
        },
        trigger: {
          value: '',
          writable:true
        },
        popupId: {
          value: '',
          writable:true
        },
        popupConfig: {
          value: {
            openonclick: true,
            zIndex: 2001,
            overlayExtraClass: 'atlpopupoverlay',
            halign: 'right'
          }
        },
        hasClose:{
        value:true  
        },
        open: {
          value: function (e) {
            if (this.validateOpen && !this.validateOpen(e)) {
              return;
            }

            this.DISPATCHER.signal('server',{
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
        validName:{
        value: function( name, lists ) {
          var trimmed = name.trim(),
            result = new String('');
          /* APPDEV-4138 START*/
          for (i=0;i<lists.length;i++)
          {
        	  lists[i]=lists[i].toUpperCase();
          }
          /* APPDEV-4138 END */
          if(trimmed.length > 0){
        	  /* APPDEV-4138 START*/
		   /* if(lists.indexOf(name)>-1) {*/
        	
        	  if(lists.indexOf(name.toUpperCase())>-1) {
        	 /* APPDEV-4138 END */  
              result = new String( 'Oops! That name is taken!' );
              result.taken = true;
            } else {
              result.ok = true;
            }
          } else {
            result = new String('Name cannot be empty.');
            result.empty = true;
          }
          
          return result;
        }  
        },
        saveToList: {
          value: function (changes) {
            var listId = $('#'+this.popupId).find('select[name=selectList]').val();
            $('#'+this.popupId).find('button.enabled').removeClass('enabled');
            this.DISPATCHER.signal('server',{
                url: '/api/shoppinglist',
                method: 'POST',
                data: {
                  data: JSON.stringify({
                    listId: listId,
                    items: fd.components.AddToCart.atcFilter(
                          fd.modules.common.productSerialize(this.popup.$trigger))
                  })
                }
            });
          }
        },
        saveToNew: {
          value: function (changes) {
            var listName = $('#'+this.popupId).find('#addtolist-newList').val()
                validator = this.validName(listName,$.makeArray($('#addtolist-selectList option')).map(function(e){
                  return e.innerHTML;
                }));

            if (validator.ok) {
                $('#'+this.popupId).find('button.enabled').removeClass('enabled');
              this.DISPATCHER.signal('server',{
                  url: '/api/shoppinglist',
                  method: 'POST',
                  data: {
                    data: JSON.stringify({
                      listName: listName,
                      items: fd.components.AddToCart.atcFilter(
                            fd.modules.common.productSerialize(this.popup.$trigger))
                    })
                  }
              });
            } else {
              $('#'+this.popupId).find('.error').html(validator.toString());
            }
          }
        }
      });
  
  
  var addtolistpopup = Object.create(listPopup,{
    headerContent: {
      value: 'Add to List'
    },
    bodyTemplate: {
      value: common.addtolistpopup
    },
    $trigger:{
      value:null
    },
    trigger: {
      value: '[data-component=product] button.addtolist, [data-component=product] span.addtolist'
    },
    popupId: {
      value: 'addtolistpopup'
    },
    decorate: {
      value: function () {
        $(this.trigger).attr('aria-haspopup', 'true');
      }
    },
    validateOpen: {
      value: function (e) {
        var target = $(e.currentTarget),
            product = target.parents('[data-component="product"]').first(),
            req = product.find('[data-atl-required="true"]'),
            valid = true;

        req.each(function (i, el) {
          if ($(el).val() === "") {
            valid = false;
            $(el).addClass('missing-data');
            $(el).parents('.errorcontainer').first().addClass('haserror');
          } else {
            $(el).removeClass('missing-data');
            $(el).parents('.errorcontainer').first().removeClass('haserror');
          }
        });

        return valid;
      }
    },
    callback:{
      value:function(data){
        data = {data:data, url:encodeURIComponent(location.pathname+location.search)};
        fd.modules.common.widget.callback.call(this,data);
      }
    }
  });

  var createlistpopup = Object.create(listPopup,{
    headerContent: {
      value: 'Add All to List'
    },
    bodyTemplate: {
      value: common.createlistpopup
    },
    $trigger:{
      value:null
    },
    trigger: {
      value: '.qs-actions button.qs-addtolist'
    },
    decorate: {
      value: function () {
        $(this.trigger).attr('aria-haspopup', 'true');
      }
    },
    popupId: {
      value: 'createlistpopup'
    }
  });

  var listaddlistener = Object.create(fd.common.signalTarget,{
    signal:{
      value:'responseItems'
    },
    callback: {
      value: function (data) {
        var $popupBody;
        if (data && data.length === 1) {
          $popupBody = $('#'+addtolistpopup.popupId).find(addtolistpopup.bodySelector);
        } else if (data) {
          $popupBody = $('#'+createlistpopup.popupId).find(createlistpopup.bodySelector);
        }

        if (data && data[0]) {
          // display verification
          $popupBody.html(common.addtolistdone({responseItems: data}));

          // close create list popup
          // createlistpopup.close();
        }

      }
    }
  });

  listaddlistener.listen();
  createlistpopup.listen();
  createlistpopup.render();
  addtolistpopup.listen();
  addtolistpopup.render();

  $(document).on('click', addtolistpopup.trigger, addtolistpopup.open.bind(addtolistpopup));
  $(document).on('click', createlistpopup.trigger, createlistpopup.open.bind(createlistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-add.enabled', addtolistpopup.saveToList.bind(addtolistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-go.enabled', addtolistpopup.saveToNew.bind(addtolistpopup));
  $(document).on('click', '#' + addtolistpopup.popupId + ' .addtolist-button-ok', addtolistpopup.close.bind(addtolistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-add.enabled', createlistpopup.saveToList.bind(createlistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-go.enabled', createlistpopup.saveToNew.bind(createlistpopup));
  $(document).on('click', '#' + createlistpopup.popupId + ' .addtolist-button-ok', createlistpopup.close.bind(createlistpopup));

  fd.modules.common.utils.register("modules.common", "addtolistpopup", addtolistpopup, fd);
  fd.modules.common.utils.register("modules.common", "listValidator", listPopup.validName, fd);
  fd.modules.common.utils.register("modules.common", "createlistpopup", createlistpopup, fd);

}(FreshDirect));

