/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var main = {};

	var DISPATCHER = fd.common.dispatcher;

  fd.quickshop.itemType = 'fdlists';
  fd.quickshop.tabType = 'FD_LISTS';

	function update(data) {
		var jsondata = JSON.stringify(data || {
						sortId:'freq'
					});

		window.location.hash = window.encodeURIComponent(jsondata);

			DISPATCHER.signal('server',{
				url: '/api/qs/starterLists',
				data: {
					data: jsondata
		          },spinner:{
		              timeout:1500,
		              element:'.qs-loader'
			}});
	}

	function serialize(e) {
		var data = $.extend({},
			fd.quickshop.FDLists.fdlists.serialize(),
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
			$(document).asEventStream('list-change').map('starterlists'),
			$(document).asEventStream('page-change').map('pager'),
			$(document).asEventStream('viewtypeChanged').map('viewtype'),
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
	
	var firstTab = $('[data-component="tabbedRecommender"] [data-tabname]:first-child');
	fd.common.tabbedRecommender.selectTab($('[data-component="tabbedRecommender"]'),firstTab.data('tabname'),firstTab);
	
	fd.modules.common.utils.register("quickshop.FDLists", "main", main, fd);
}(FreshDirect));


