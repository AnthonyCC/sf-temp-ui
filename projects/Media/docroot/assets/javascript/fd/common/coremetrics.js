/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var DISPATCHER = fd.common.dispatcher;
	var $ = fd.libs.$;


	var coremetrics = Object.create(fd.common.signalTarget,{
		signal:{
			value:'coremetrics'
		},
		playOneItem:{
			value:function( coremetricsItem ){
				try{
					var fname = coremetricsItem.shift();
					window[fname].apply(window,coremetricsItem);
				} catch(e) {
					// console.log('cm: '+e);
				}
			}
		},
		callback:{
			value:function( coremetricsData ) {
				coremetricsData.each(this.playOneItem);
			}
		}
	});

	coremetrics.listen();
	
	function populateCmData(propertyName,event){
		var $ct = $(event.currentTarget);
		event.cmData[propertyName] = $ct.data('cm'+propertyName.toLowerCase());
	}
	
	$(document.body).on('addToCart','[data-cmsitefeature]',function(event){
		populateCmData('siteFeature',event);
	});

	$(document.body).on('addToCart','[data-cmeventsource]',function(event){
		populateCmData('eventSource',event);
	});

	$(document.body).on('addToCart','[data-cmvariantid]',function(event){
		populateCmData('variantId',event);
	});

	fd.modules.common.utils.register("components", "coremetrics", coremetrics, fd);
}(FreshDirect));
