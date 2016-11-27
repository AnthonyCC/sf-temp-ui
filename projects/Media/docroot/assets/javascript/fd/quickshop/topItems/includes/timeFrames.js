/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var DISPATCHER = fd.common.dispatcher;

	function findSelected(element) {
		var a=this[element],
			timeFrame = a[0],
			test = timeFrame.selected;
		if(test) {
			DISPATCHER.signal('listheader',{title:timeFrame.name});
		}

		return test;
	};

	var timeFrames = Object.create(WIDGET,{
		signal:{
			value:'TIME FRAME'
		},
		template:{
			value:quickshop.timeFrames
		},
		placeholder:{
			value:'#timeframes'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $(this.placeholder);
				var val = $('input',el).serializeArray().pop();
				return { timeFrame: (val && val.value) || 'timeFrameAll' };
			}
		},
		render:{
			value:function(data){
				Object.keys(data).some(findSelected,data);
				WIDGET.render.call(this,data);				
			}
		},
		handleClick:{
			value:function(clickEvent){}
		}
	});

	timeFrames.listen();


	fd.modules.common.utils.register("quickshop.topItems", "timeFrames", timeFrames, fd);
}(FreshDirect));
