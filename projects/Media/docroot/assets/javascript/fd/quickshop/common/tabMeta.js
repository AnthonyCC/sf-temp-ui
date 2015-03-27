/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;
  var QSVersion = fd.utils.getActive('quickshop');

	var tabMeta = Object.create(fd.common.signalTarget,{
		signal:{
			value:'tabMeta'
		},
		callback:{
			value:function( tabmeta ) {
				$('.qs-content').removeClass('qs-no-past-orders');
				$('.qs-content').removeClass('qs-no-lists');
				$('[data-component="tabMeta"]').each(function(index){
					var $this = $(this),
						tabMetaId = $this.data('tabmeta');
					if( tabmeta.hasOwnProperty(tabMetaId) ) {
						this.innerHTML='('+tabmeta[tabMetaId]+')';
					}
					
					if(tabMetaId === 'pastorders' && tabmeta[tabMetaId] == 0) {
						$('.qs-content').addClass('qs-no-past-orders');
					}
					if(tabMetaId === 'lists' && tabmeta[tabMetaId] == 0) {
						$('.qs-content').addClass('qs-no-lists');
					}
				});
				this.isLoaded = true;
			}
		},
		update:{
			value:function(reload){
				if(!this.isLoaded || reload) {
					DISPATCHER.signal('server',{
						url: QSVersion === '2_0' ? '/api/qs/tabMeta' : '/api/reorder/tabMeta'
					});
				}
			}
		},
		isLoaded:{
			value:false,
			writable:true
		}
	});

  var tabMetaUpdater = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'shoppingListPageRefreshNeeded'
    },
    callback: {
      value: function (e) {
        if (e) {
          tabMeta.update(true);
        }
      }
    }
  });

	tabMeta.listen();
  tabMetaUpdater.listen();

	fd.modules.common.utils.register("quickshop.common", "tabMeta", tabMeta, fd);
}(FreshDirect));
