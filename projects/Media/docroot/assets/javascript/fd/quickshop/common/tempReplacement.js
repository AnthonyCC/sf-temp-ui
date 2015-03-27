/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;

	var tempReplacement = Object.create(fd.common.signalTarget,{
		signal:{
			value:'temporaryReplacement'
		},
		callback:{
			value:function( value ) {
        var mainEl = $('.qs-content').first();

        mainEl.addClass('noscroll');

				DISPATCHER.signal('main',{});
			}
		},
		update:{
			value:function(itemId){
				DISPATCHER.signal('server',{          
					url: '/api/qs/replacement', 
			        data: {
			        	itemId: itemId
			        }});
			}
		}
	});

	tempReplacement.listen();

	$(document).on('click','[data-component="item-replacement"]',function(e){
		var t=$(e.target),
			itemId=t.data('replacement-id'),
			alreadyReplaced=t.data('replacement')=="true";
		
		if(!alreadyReplaced && itemId) {
			tempReplacement.update(itemId);
		}	
	});
	
	
	
	fd.modules.common.utils.register("quickshop.common", "tempReplacement", tempReplacement, fd);
}(FreshDirect));
