/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;

	var search = Object.create(fd.common.signalTarget,{
		signal:{
			value:'searchTerm'
		},
		searchElement:{
			value:null,
			writable:true
		},
		serialize:{
			value:function(searchElement){
				return {
					searchTerm:(this.getInputsearchElement().val() || null)
				};
			}
		},
		handleEvent:{
			value:function(event){
				event.preventDefault();
				$(document).trigger('qs_search');
			}
		},
		getInputsearchElement:{
			value:function(){
				return ( this.searchElement || (this.searchElement=$('#searchTerm')) );
			}
		},
		callback:{
			value:function(data) {
				this.getInputsearchElement().val(data);
			}
		},
		allowNull: {
			value: true // switch to accept signal with null body
		}
	});

	search.listen();
	$(document).on('submit','#qs_search',search.handleEvent.bind(search));

	fd.modules.common.utils.register("quickshop.pastOrders", "search", search, fd);
}(FreshDirect));
