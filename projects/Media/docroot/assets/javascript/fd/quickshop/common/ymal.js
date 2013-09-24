/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var DISPATCHER = fd.common.dispatcher;
	var FEATURE = 'CRAZY_QUICKSHOP';	// Note that this is not a real site-feature in smart-store, only a placeholder for the craziness for the top recommender, which can be virtually anything based on actual context

	var ymal = Object.create(WIDGET,{
		signal:{
			value:'recommenderResult'
		},
		template:{
			value:common.simpleCarousel
		},
		placeholder:{
			value:'#qs-ymal'
		},
		serialize:{
			value:function(element){
			}
		},
		handleClick:{
			value:function(clickEvent){
			}
		},
		callback:{
			value:function( value ){
				if(value.siteFeature === FEATURE ) {
					value.itemType = 'grid';
					value.items.forEach(function(item){
						item.itemId = 'atc_'+item.productId+'_'+item.skuCode+'_'+(Math.random()*10000).toString(24);
					});
					WIDGET.callback.call(this, value );					
				}
			}
		},
		update:{
			value:function(){
				DISPATCHER.signal('server',{
					url:'/api/qs/ymal',
					method:'GET',
					data: {
			            data: JSON.stringify({ feature: FEATURE, deptId: "mea" })	//FIXME: add real deptId from filter
			    }});
			}
		}
	});
	
	Object.create(fd.common.signalTarget,{
		signal:{
			value:'atcResult'
		},
		callback:{
			value:ymal.update.bind(ymal)
		}
	}).listen();
	
	if ($(ymal.placeholder).length) {
		ymal.listen();
		ymal.update();
	}

	fd.modules.common.utils.register("quickshop.common", "ymal", ymal, fd);
}(FreshDirect));
