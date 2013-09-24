/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var main = {};

	var DISPATCHER = fd.common.dispatcher;
  
  fd.quickshop.itemType = 'shopfromlists';
  fd.quickshop.tabType = 'CUSTOMER_LISTS';

	function update(data) {
    var jsondata = JSON.stringify(data || { 
					sortId:'freq'
				});

    window.location.hash = window.encodeURIComponent(jsondata);

		DISPATCHER.signal('server',{ 
			url: '/api/qs/shoppingLists', 
			data: {
				data: jsondata
      		},spinner:{
            	timeout:500,
            	element:'.qs-loader'
    	}});
	}

	function serialize(e) {
		var data = $.extend({},
			fd.quickshop.shopFromList.shoppinglists.serialize(),
			fd.quickshop.common.pager.serialize($('[data-component="pager"]:first')),
			fd.quickshop.common.sorter.serialize(),
			fd.quickshop.common.preferences.serialize(),
			fd.quickshop.common.departments.serialize()
		);

		if(e !== 'pager') {
			data.activePage=0;
		}

		delete data.itemCount;

		return data;		
	}

	function reset(){
		update();
	}
	
	function reload(){
	  var hashdata = null;

	  try {
	    // full json in hash
	    hashdata = JSON.parse(window.decodeURIComponent(window.location.hash.slice(1)));
	  } catch (e) {
	    try {
	      // query string in hash
	      hashdata = fd.modules.common.utils.getParameters();
	    } catch (e) {
	    } 
	  }

	  update(hashdata);
		
	}

	var UIChanges = Bacon.mergeAll([
	        DISPATCHER.bus.filter(function (value) { return ('to' in value) && (value.to === 'shoppingListPageRefreshNeeded') && value.body;}).map(function(value){
	        	fd.quickshop.common.tabMeta.update();
	        	return value;
	        }),
			$(document).asEventStream('list-change').map('shoppinglists'),
			$(document).asEventStream('page-change').map('pager'),
			$(document).asEventStream('sorter-change').map('sorter'),
			$(document).asEventStream('change','#departments input[type="radio"]').map('departments'),
			$(document).asEventStream('change','#preferences input[type="checkbox"]').map('preferences')
	]);

	UIChanges.debounce(500).onValue(function(e){
		update(serialize(e));
	});

	$(document).on('click','#reset',reset);

	reload();
	Object.create(fd.common.signalTarget,{
		signal:{
			value:'main'
		},
		callback:{
			value:reload
		}
	}).listen();
	

	fd.modules.common.utils.register("quickshop.shopFromList", "main", main, fd);
}(FreshDirect));


