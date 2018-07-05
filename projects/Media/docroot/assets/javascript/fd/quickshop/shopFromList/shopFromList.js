/*global Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var main = {};

  var DISPATCHER = fd.common.dispatcher;
  var QSVersion = fd.utils.getActive('quickshop');

  fd.quickshop.itemType = 'shopfromlists';
  fd.quickshop.tabType = 'CUSTOMER_LISTS';

  function update(data) {
    var jsondata = JSON.stringify(data || {
          sortId:'freq',
          tabType:fd.quickshop.tabType
        });

    window.location.hash = window.encodeURIComponent(jsondata);

    DISPATCHER.signal('server',{
      url: QSVersion === '2_0' ? '/api/qs/shoppingLists' : '/api/reorder/shoppingLists',
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
      ):
      $.extend({},
        fd.quickshop.shopFromList.shoppinglists.serialize(),
        fd.quickshop.common.search.serialize(),
        fd.quickshop.common.pager.serialize($('[data-component="pager"]:first')),
        fd.quickshop.common.sorter.serialize(),
        fd.quickshop.common.preferences.serialize(),
        fd.quickshop.common.departments.serialize()
      );

    if(e !== 'pager' && !$('.qs-content').first().hasClass('dontupdate')) {
      data.activePage=0;
    }

    if (e.to === 'shoppingListPageRefreshNeeded') {
      $('.qs-content').first().addClass('noscroll');
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
      } catch (e2) {
      }
    }

    update(hashdata);

  }

  var UIChanges = Bacon.mergeAll([
          DISPATCHER.bus.filter(function (value) {
            var differentList = false, currentList;

            if (value.to && value.to === "responseItems" && value.body) {
              currentList = fd.quickshop.shopFromList.shoppinglists.serialize().yourListId;

              if(currentList){
                differentList = value.body.filter(function (item) {
                  return item.listId === currentList;
                }).length === 0;

                if (differentList) {
                  $('.qs-content').first().addClass('dontupdate');
                } else {
                  $('.qs-content').first().removeClass('dontupdate');
                }
              }
            }
            
            return ('to' in value) && (value.to === 'shoppingListPageRefreshNeeded') && value.body;
          }).map(function (value) {
            // fd.quickshop.common.tabMeta.update(true);
            return value;
          }),
      $(document).asEventStream('list-change').map('shoppinglists'),
      $(document).asEventStream('page-change').map('pager'),
      $(document).asEventStream('sorter-change').map('sorter'),
      $(document).asEventStream('change','#departments input[type="radio"]').map('departments'),
      $(document).asEventStream('change','#preferences input[type="checkbox"]').map('preferences')
  ]), immediateUIChanges = Bacon.mergeAll([
      $(document).asEventStream('viewtypeChanged').map('viewtype')
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

  Object.create(fd.common.signalTarget,{
    signal:{
      value:'YOUR LISTS'
    },
    callback:{
      value: function(data) {
        //update remove all items confirm popup
        var headerText = 'Would you also like to delete this list?';
        var selectedList = [].concat( data.YOUR_LISTS.filter(function(d) { return !!d.selected; }) );
        if (selectedList.length && selectedList[0].name) {
          headerText = "Would you like to also delete &apos;"+fd.utils.escapeHtml(selectedList[0].name)+"&apos;?";
        }
        $('.qs-actions .qs-deletelist').attr('data-confirm-header', headerText);
      }
    }
  }).listen();

  var firstTab = $('[data-component="tabbedRecommender"] [data-tabname]:first-child');
  fd.common.tabbedRecommender.selectTab($('[data-component="tabbedRecommender"]'),firstTab.data('tabname'),firstTab);


  fd.modules.common.utils.register("quickshop.shopFromList", "main", main, fd);
}(FreshDirect));
