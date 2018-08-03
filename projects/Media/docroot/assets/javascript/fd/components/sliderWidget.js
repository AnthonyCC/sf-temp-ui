/*global*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var slider = Object.create(WIDGET,{
    signal:{
      value:'slider'
    },
    template:{
      value:common.sliderWidget
    },
    placeholder:{
      value:'[data-component="slider"]'
    },
    callback:{
    	value: function(data) {
	    	if (data) {
	    		if (data.success) {
	    			this.onSuccess(data);
	    		} else {
	    			this.onFailure(data);
	    		}
	    	}
    	}
    },
   onSuccess:{
   	value: function(data){
   		//
   	}
   },
   onFailure:{
	   	value: function(data){
	   		//
	   	}
	   }
    });
  slider.listen();

  fd.modules.common.utils.register("components", "slider", slider, fd);
}(FreshDirect));
