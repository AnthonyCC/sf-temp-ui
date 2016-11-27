/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var main = {};

  var DISPATCHER = fd.common.dispatcher;
  var QSVersion = fd.utils.getActive('quickshop');

  fd.quickshop.itemType = 'pastOrders';
  
  fd.quickshop.tabType = 'PAST_ORDERS';

  function update(data) {
    var jsondata = JSON.stringify(data || { 
          timeFrame:'timeFrameAll',
          sortId:'freq',
          searchTerm:null,
          tabType:fd.quickshop.tabType
        });

    window.location.hash = window.encodeURIComponent(jsondata);

    DISPATCHER.signal('server',{
          url: QSVersion === '2_0' ? '/api/qs/pastOrders' : '/api/reorder/pastOrders',
          data: {
            data: jsondata
          },spinner:{
            timeout:1500,
            element:'.qs-loader'
    }});
  }

	function serialize(e, searchOnly) {
		var data = searchOnly ?
      $.extend({},
        fd.quickshop.common.search.serialize()
      ) :
      $.extend({},
        fd.quickshop.common.pager.serialize($('[data-component="pager"]:first')),
        fd.quickshop.common.sorter.serialize(),
        fd.quickshop.pastOrders.timeFrames.serialize(),
        fd.quickshop.pastOrders.orders.serialize(),
        fd.quickshop.common.search.serialize(),
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
    data.tabType = fd.quickshop.tabType;

    return data;
  }

  var slowUpdateId = null;
  function slowUpdate(e, timeout, searchOnly) {
    timeout = timeout || 1500;

    if (slowUpdateId) {
      clearTimeout(slowUpdateId);
      slowUpdateId = null;
    }

    slowUpdateId = setTimeout(function () {
      update(serialize(e, searchOnly));
      slowUpdateId = null;
    }, timeout);
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
      $(document).asEventStream('orders-change').map('orders'),
      $(document).asEventStream('change','#departments input[type="radio"]').map('departments'),
      $(document).asEventStream('change','#preferences input[type="checkbox"]').map('preferences')
  ]), immediateUIChanges = Bacon.mergeAll([
      $(document).asEventStream('page-change').map('pager'),
      $(document).asEventStream('viewtypeChanged').map('viewtype'),
      $(document).asEventStream('sorter-change').map('sorter')
  ]);

  UIChanges.onValue(function(e){
    slowUpdate(e);
  });
  immediateUIChanges.onValue(function(e){
    slowUpdate(e, 0);
  });
  $(document).asEventStream('qs_search').map('search').onValue(function(e) {
    slowUpdate(e, 0, true);
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

	var firstTab = $('[data-component="tabbedRecommender"] [data-tabname]:first-child');
	fd.common.tabbedRecommender.selectTab($('[data-component="tabbedRecommender"]'),firstTab.data('tabname'),firstTab);
	

  
  fd.modules.common.utils.register("quickshop.pastOrders", "main", main, fd);
}(FreshDirect));


