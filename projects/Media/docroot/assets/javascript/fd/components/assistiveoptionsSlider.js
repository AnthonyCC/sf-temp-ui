/*global*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;

  var assistiveoptionsSlider = Object.create(fd.components.slider,{
    signal:{
      value:'assistiveoptionsSlider'
    },
    template:{
      value:common.sliderWidget
    },
    placeholder:{
      value:'#assistive-options-slider'
    },
   onSuccess:{
   	value: function(data){
   			$("body").addClass("assistive-mode");
   			localStorage.setItem("assistive-enabled", true);
   		}
   },
  
   onFailure:{
	   	value: function(data){
	   		//on open get the localstorage value  and if the post fails then return to the local storage value
	   		$("body").removeClass("assistive-mode");
	   		localStorage.setItem("assistive-enabled", false);
	   		$(this.placeholder).find(":checkbox").prop("checked", false);
	   	}
	   },
	   handleChange:{
	       value:function(clickEvent){
//	    	   $(".slider:after").addClass("");
	    	   $(".spinner").show().delay(1000).fadeOut();
	    	   var isChecked = $(clickEvent.currentTarget).prop('checked');
	    	   if(isChecked) {
	    		   this.onSuccess();
	    	   }else {
	    		   this.onFailure();
	    	   }
	       }
	   }
    });
 
  assistiveoptionsSlider.listen();
  fd.modules.common.utils.register("components", "assistiveoptionsSlider", assistiveoptionsSlider, fd);
  $(document).on('change',assistiveoptionsSlider.placeholder+' input[type="checkbox"]', assistiveoptionsSlider.handleChange.bind(assistiveoptionsSlider));
  
}(FreshDirect));
