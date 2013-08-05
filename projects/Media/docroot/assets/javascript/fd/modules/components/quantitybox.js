/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	
	var QuantityBox = { initialized: false },
		incrementMultiplier={
			"quantitybox.inc":1,
			"quantitybox.dec":-1
		};

	function triggerEvent($quantitybox, newVal) {
		$quantitybox.trigger('quantity-change',{ newVal: newVal });
		$quantitybox.trigger('quantity-change-'+($quantitybox.find('input.qty').val()<newVal?'inc':'dec'));
	};

	function getInput($quantitybox) {
		return $('input[data-component="quantitybox.value"]',$quantitybox);
	};

	var getValue = function($quantitybox){
		if($quantitybox) {
			return getInput($quantitybox).val();
		} else {
			return undefined;
		}
	};


	$(document).on('click','[data-component="quantitybox"]',function(e){
		var $input,$this,mul,increment, newVal;

		$this=$(this);
		$input=getInput($this);
		mul=incrementMultiplier[$(e.target).data("component")];
		increment=$this.data("step")*mul;

		if(increment) {
			newVal=Math.max($this.data("min"),Math.min($this.data("max"),$input.val()*1+increment));
			triggerEvent($this,newVal);
			$input.val(newVal);
		}

	});

	$(document).on('keyup','[data-component="quantitybox"]',function(e){
		var $input,$this;

		$this=$(this);
		$input=getInput($this);
		triggerEvent($this,$input.val());
	});

/*
* plugin for quantitybox
*
* usage: $(quantitybox selector).FD.quantityBox("value")
* return value of the quantityboxes
*/

	var getBoxAndValue = function() {
		return { 
			quantityBox:this,
			value:getValue(this)
		};
	};

	var methods = {
		value : function( ) { 
		   return getValue(this[0]);
		},
		boxValue:function(){
			return this.map(getBoxAndValue);
		}
	};

	$.fn.quantityBox = function( method ) {

		// Method calling logic
		if ( methods[method] ) {
		  return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else {
		  $.error( 'Method ' +  method + ' does not exist on jQuery.FD.quantityBox' );
		}    
	};

	fd.modules.common.utils.register("modules.components", "QuantityBox", QuantityBox, fd);
}(FreshDirect));
