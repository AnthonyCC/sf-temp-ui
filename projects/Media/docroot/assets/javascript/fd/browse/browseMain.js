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

  // back button for multi-search results
  function msBack() {
    var tabcontent = $('.search-input .tabcontent'),
        msback = tabcontent.find('.msback');

    if (!msback.length && document.referrer.indexOf('multisearch') > -1) {
      msback = $('<div class="msback"><a href="'+document.referrer+'" class="cssbutton back white icon-arrow-left2-before">Back</a></div>');
      tabcontent.append(msback);
    }
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
    setTimeout(scrollToTop, 500);

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

  function scrollToTop() {
    var main = $('section.main').first(), crect;

    if (main && main.size()) {
      crect = main[0].getBoundingClientRect();

      if (crect.top < 0) {
    	$jq('html').animate({scrollTop: crect.top + $('body').scrollTop()}, '500');
      }
    }
  }

  function serialize(e, pagerRelated) {
    var pager = fd.browse.pager.serialize(),
        data = $.extend({},
          (pagerRelated || (pager && pager.activePage === 0)) ? pager : {},
          fd.browse.sorter && fd.browse.sorter.serialize(),
          fd.components.coremetrics ? fd.components.coremetrics.serialize() : {},
          fd.browse.menu && fd.browse.menu.serialize(),
          fd.browse.searchParams && fd.browse.searchParams.serialize(),
          fd.browse.searchTabs && fd.browse.searchTabs.serialize(),
          fd.browse.pageType && fd.browse.pageType.serialize()
        );
    
    /* add additional query params, if needed */
    var picksId = $.QueryString["picksId"]; /* comes from common_javascript.js */
    if (picksId) {
    	data = $.extend(data,
            {"picksId": picksId}
    	);
    }
    

    // remove id if searchParams are provided
    if (data.searchParams && data.id) {
      delete data.id;
    }

    if (pagerRelated) {
      setTimeout(scrollToTop, 500);
    }

    return data;
  }

  function setURL(params, title, doPush) {
    title = title || document.title;

    if (lastParams === params || params === null) {
      return;
    }

    if (window.history) {
      if (doPush && window.history.pushState) {
        window.history.pushState({searchParams: params}, title, window.location.pathname + '?' + window.decodeURIComponent(params));
      } else if (window.history.replaceState) {
        window.history.replaceState({searchParams: params}, title, window.location.pathname + '?' + window.decodeURIComponent(params));
      }

      if (fd.components && fd.components.navigationHighlighter) {
        fd.components.navigationHighlighter.reset();
      }
    }
  }

  function reload(){
    msBack();
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
        $(document).asEventStream('breadcrumbs-change').map('breadcrumbs'),
        $(document).asEventStream('searchParams-change').map('searchParams')
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


