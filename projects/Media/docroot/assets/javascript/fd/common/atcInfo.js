/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;


	var atcInfo = Object.create(fd.common.signalTarget,{
		signal:{
			value:'atcResult'
		},
		template:{
			value:common.atcInCartContent
		},
		setMessage:{
			value:function(item) {
				var element = $(document.getElementById(item.itemId || item.atcItemId ));
				if(element) {
					element.addClass('atc-info-message');
					element.html(this.template({
						amount:item.inCartAmount,
						message:'Adding to cart...',
						type:'ADDING'
					}));
				}
			}
		},
		setServerMessage:{
			value:function(atcList){
				atcList.forEach(this.setMessage,this);
			}
		},
		removeMessage:{
			value:function(){
				this.removeClass('atc-info-message');
			}
		},
		renderItem:{
			value:function(item) {
				var element = $(document.getElementById(item.itemId));
				if(element) {
					element.addClass('atc-info-message');
					element.closest('[data-component="product"]').find('[data-component="ATCButton"],[data-component="customizeButton"]').addClass('incart');
					element.html(this.template({
						amount:item.inCartAmount,
						message:item.message,
						type:item.status
					}));

					setTimeout(this.removeMessage.bind(element),3000);

				}
			}
		},
		callback:{
			value:function( atcResultList ) {
				atcResultList.forEach(this.renderItem,this);
				fd.modules.header.Cart.update();
			}
		}
	});

	atcInfo.listen();

	fd.modules.common.utils.register("components", "atcInfo", atcInfo, fd);
}(FreshDirect));
