/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;

	var customization = Object.create({},{
		getItem:{
			value:function(element){
				return fd.modules.common.productSerialize(element,true)
			}
		},
		applyCustomization:{
			value: function(event) {
				  var items = this.getItem(event.currentTarget);
				  
				  if(fd.components.AddToCart.requiredValidator(items)){
					  var request = { items:fd.components.AddToCart.atcFilter(items) };
						if(fd.quickshop.tabType) {
							request.tab = fd.quickshop.tabType;
						} 

						fd.common.dispatcher.signal('server',{
							url:'/api/qs/tempConfig',
							data:{data:JSON.stringify(request)},
							method:'POST'			
						});			
				  };
			}
		},
		saveCustomization:{
			value:function(event){
				var items = this.getItem(event.currentTarget);
				if(fd.components.AddToCart.requiredValidator(items)){
					items = fd.components.AddToCart.atcFilter(items);
					var item = items && items[0];
					if(item){
						fd.modules.common.editinlistpopup.saveChange(item.listId,item.lineId,item);
						$('#customizePopup').hide();
					}
				}
			}
		}
	});
	
	
	var itemUpdater = Object.create(fd.common.signalTarget,{
		signal:{
			value:'updateItem',
			writable:true
		},
		config:{
			value:{
				title:'',
				changed:false
			},
			writable:true
		},
		template:{
			value:quickshop.item
		},
		callback:{
			value:function( value ){
				this.update(value);
			}
		},
		update:{
			value:function( item ){
				var itemId = item.itemId,
					$item = $('[data-productid="'+itemId+'"]');
				$item.replaceWith(this.template($.extend({item:item,type:fd.quickshop.itemType},this.config)));
			}
		}
	});
	Object.create(itemUpdater).listen();

	
	var tempItemUpdater = Object.create(itemUpdater);
	
	tempItemUpdater.signal="updateItemTemp";
	tempItemUpdater.config = {
			title:'',
			changed:true,
			tempConfig:true
	}
	
	tempItemUpdater.listen();
	
	$(document).on('click','[data-component="applyCustomization"]',customization.applyCustomization.bind(customization));
	$(document).on('click','[data-component="saveCustomization"]',customization.saveCustomization.bind(customization));
	
	fd.modules.common.utils.register("quickshop.common", "applyCustomization", customization, fd);
}(FreshDirect));
