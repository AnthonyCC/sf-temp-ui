/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

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
        var searchElement = this.getInputsearchElement();
				event.preventDefault();
        searchElement.blur(); // making sure ipad keyboard disappears
        searchElement.attr('value', searchElement.val());
				$(document).trigger('qs_search');
			}
		},
    reset:{
      value: function(e) {
        var searchElement = this.getInputsearchElement();

        searchElement.val('').attr('value', '');
        this.handleEvent(e);
      }
    },
		getInputsearchElement:{
			value:function(){
				return ( this.searchElement || (this.searchElement=$('#searchTerm')) );
			}
		},
		callback:{
			value:function(data) {
        this.getInputsearchElement().val(data).attr("value", data);
			}
		},
		allowNull: {
			value: true // switch to accept signal with null body
		}
	});

	search.listen();
	$(document).on('submit','#qs_search',search.handleEvent.bind(search));
  $(document).on('click', '.reset-search', search.reset.bind(search));

	fd.modules.common.utils.register("quickshop.common", "search", search, fd);
}(FreshDirect));
