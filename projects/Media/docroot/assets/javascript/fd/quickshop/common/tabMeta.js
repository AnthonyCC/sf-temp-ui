/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;

	var tabMeta = Object.create(fd.common.signalTarget,{
		signal:{
			value:'tabMeta'
		},
		callback:{
			value:function( tabmeta ) {
				$('.qs-content').removeClass('qs-no-past-orders');
				$('.qs-content').removeClass('qs-no-lists');
				$('[data-component="tabMeta"]').each(function(){
					var $this = $(this),
						tabMetaId = $this.data('tabmeta');
					if( tabmeta.hasOwnProperty(tabMetaId) ) {
						this.innerHTML='('+tabmeta[tabMetaId]+')';
					}

					if(tabMetaId === 'pastorders' && tabmeta[tabMetaId] === 0) {
						$('.qs-content').addClass('qs-no-past-orders');
					}
					if(tabMetaId === 'lists' && tabmeta[tabMetaId] === 0) {
						$('.qs-content').addClass('qs-no-lists');
					}
				});
				this.isLoaded = true;
			}
		},
		update:{
			value:function(){
				if(!this.disabled && !this.isLoaded) {
					DISPATCHER.signal('server',{
						url: '/api/qs/tabMeta'
					});
				}
			}
		},
		disabled:{
			value:false,
			writable:true
		},
		isLoaded:{
			value:false,
			writable:true
		}
	});

  // Disabled for #APPDEV-4162
  tabMeta.disabled = true;

	tabMeta.listen();

	fd.modules.common.utils.register("quickshop.common", "tabMeta", tabMeta, fd);
}(FreshDirect));
