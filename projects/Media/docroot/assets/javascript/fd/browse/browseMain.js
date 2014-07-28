/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      lastParams;

  if (fd.components.ifrPopup) {
    window.pop = function (url,width,height) {
      fd.components.ifrPopup.open({
        width:width,
        height:height,
        url:url
      });
    };
  }

  function pauseAllTransactionalPopupsTillContentChange(){
    fd.common.transactionalPopup && fd.common.transactionalPopup.close();
    $('.browseContent').addClass('no-transactional');
  }

  function update(data) {

    var jsondata = JSON.stringify(data || {
          timeFrame:'timeFrameAll',
          sortId:'freq',
          searchTerm:null
        });

    pauseAllTransactionalPopupsTillContentChange();

    DISPATCHER.signal('server',{
      url: '/api/filter',
      data: {
        data: jsondata
      },
      spinner: {
        timeout: 500,
        element: '.browseContent'
      } 
    });
  }

  function scrollToTopPager() {
    var ph = $('.pager-holder').first()[0],
        crect;

    if (ph) {
      crect = ph.getBoundingClientRect();

      if (crect.top < 0) {
        $(document).scrollTop($(document).scrollTop() + crect.top);
      }
    }
  }

  function serialize(e, pagerRelated) {
    var pager = fd.browse.pager.serialize(),
        data = $.extend({},
          (pagerRelated || (pager && pager.activePage === 0)) ? pager : {},
          fd.browse.sorter.serialize(),
          fd.components.coremetrics ? fd.components.coremetrics.serialize() : {},
          fd.browse.menu.serialize()
        );

    if (pagerRelated) {
      setTimeout(scrollToTopPager, 500);
    }

    return data;
  }

  function setURL(params, title, doPush) {
    title = title || document.title;

    if (lastParams === params) {
      return;
    }

    if (window.history) {
      if (doPush && window.history.pushState) {
        window.history.pushState({searchParams: params}, title, window.location.pathname + '?' + window.decodeURIComponent(params));
      } else if (window.history.replaceState) {
        window.history.replaceState({searchParams: params}, title, window.location.pathname + '?' + window.decodeURIComponent(params));
      }
    }
  }

  function reload(){
  }

  if (window.history && window.history.pushState) {
    window.onpopstate = function (e) {
      if (e.state && e.state.searchParams) {
        DISPATCHER.signal('server', {
          url: '/api/filter?'+window.decodeURIComponent(e.state.searchParams),
          spinner: {
            timeout: 500,
            element: '.browseContent'
          }
        });
        lastParams = e.state.searchParams;
      }
    };
  }

  var UIChanges = Bacon.mergeAll([
        $(document).asEventStream('menu-change').map('menu'),
        $(document).asEventStream('filtertags-change').map('filtertags')
      ]),
      immediateUIChanges = Bacon.mergeAll([
        $(document).asEventStream('sorter-change').map('sorter'),
        $(document).asEventStream('breadcrumbs-change').map('breadcrumbs')
      ]),
      pagerRelatedChanges = Bacon.mergeAll([
        $(document).asEventStream('page-change').map('pager')
      ]);

  setURL(FreshDirect.browse.data.descriptiveContent.url, FreshDirect.browse.data.descriptiveContent.pageTitle);

  UIChanges.debounce(500).merge(immediateUIChanges).onValue(function(e){
    update(serialize(e));
  });
  pagerRelatedChanges.onValue(function(e){
    update(serialize(e, true));
  });

  Object.create(fd.common.signalTarget,{
    signal:{
      value:'main'
    },
    callback:{
      value:reload
    }
  }).listen();

  // init calls
  reload();

  fd.modules.common.utils.register("browse", "main", {
    setURL: setURL,
    update: update,
    reload: reload
  }, fd);
}(FreshDirect));


