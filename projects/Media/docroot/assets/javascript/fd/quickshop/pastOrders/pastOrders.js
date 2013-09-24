/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var main = {};

  var DISPATCHER = fd.common.dispatcher;

  fd.quickshop.itemType = 'pastOrders';
  
  fd.quickshop.tabType = 'PAST_ORDERS';

  function update(data) {
    var jsondata = JSON.stringify(data || { 
          timeFrame:'timeFrameAll',
          sortId:'freq',
          searchTerm:null
        });

    window.location.hash = window.encodeURIComponent(jsondata);

    DISPATCHER.signal('server',{ 
          url: '/api/qs/pastOrders', 
          data: {
            data: jsondata
          },spinner:{
            timeout:500,
            element:'.qs-loader'
    }});
  }

  function serialize(e) {
    var data = $.extend({},
      fd.quickshop.common.pager.serialize($('[data-component="pager"]:first')),
      fd.quickshop.common.sorter.serialize(),
      fd.quickshop.pastOrders.timeFrames.serialize(),
      fd.quickshop.pastOrders.orders.serialize(),
      fd.quickshop.pastOrders.search.serialize(),
      fd.quickshop.common.preferences.serialize(),
      fd.quickshop.common.departments.serialize()
    );

    if(e !== 'pager') {
      data.activePage=0;
    }

    if(e === 'timeframe') {
      data.orderIdList=[];
      data.deptIdList=[];
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
      $(document).asEventStream('change','#timeframes input').map('timeframe'),
      $(document).asEventStream('change','#orders input[type="radio"]').map('orders'),
      $(document).asEventStream('change','#departments input[type="radio"]').map('departments'),
      $(document).asEventStream('change','#preferences input[type="checkbox"]').map('preferences')
  ]), immediateUIChanges = Bacon.mergeAll([
       $(document).asEventStream('qs_search').map('search'),
       $(document).asEventStream('page-change').map('pager'),
       $(document).asEventStream('sorter-change').map('sorter')
   ]);

  UIChanges.debounce(500).merge(immediateUIChanges).onValue(function(e){
    update(serialize(e));
  });

  	reload();
	Object.create(fd.common.signalTarget,{
		signal:{
			value:'main'
		},
		callback:{
			value:reload
		}
	}).listen();
	
  
  $(document).on('click','#reset',reset);

  fd.modules.common.utils.register("quickshop.pastOrders", "main", main, fd);
}(FreshDirect));


