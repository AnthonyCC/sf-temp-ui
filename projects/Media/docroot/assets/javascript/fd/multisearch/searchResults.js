/*global multisrch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var SIGNALTARGET = fd.common.signalTarget;
  var DISPATCHER = fd.common.dispatcher;

  var searchResultList = Object.create(WIDGET,{
    signal:{
      value:'multiSearch'
    },
    template:{
      value:multisrch.searchResults
    },
    placeholder:{
      value:'#multisearch-results'
    },
    terms:{
      value:[],
      writable: true
    },
    render:{
      value:function(data){
        data = data || {};
        data.terms = data.terms || this.terms;

        var searchListEl = document.querySelector('[data-component="multisearch-list"]'),
            added = data.terms.filter(function (term) { return this.terms.indexOf(term) === -1;}.bind(this)),
            removed = this.terms.filter(function (term) { return data.terms.indexOf(term) === -1;});

        this.terms = this.terms.concat(added).filter(function (term) { return removed.indexOf(term) === -1;});

        if (!searchListEl) {
          WIDGET.render.call(this, data);
        } else {
          removed.forEach(function (item) {
            var el = document.querySelector('[data-searchresult="'+item+'"]');
            el.remove();
          });

          searchListEl.innerHTML = searchListEl.innerHTML +
            added.reduce(function (p, c) {
              return p + '<li data-searchresult="'+c+'">Loading search results for "'+c+'"...</li>';
            }, '');
        }

        // call search API with newly added terms
        added.forEach(function (term) {
          DISPATCHER.signal('server',{
            url: '/api/filter',
            data: {
              data: JSON.stringify({searchParams: term})
            },
            spinner: {
              timeout: 500,
              element: '.multisearch-results [data-searchterm="'+term+'"]'
            }
          });
        });
      }
    }
  });
  searchResultList.listen();

  var searchResult = Object.create(SIGNALTARGET,{
    signal:{
      value:'searchSections'
    },
    template:{
      value:multisrch.searchResultCarousel
    },
    callback:{
      value:function(data){
        var searchParams = data.searchParams || {},
            sections = data.sections || {},
            container = document.querySelector('.multisearch-results [data-searchresult="'+searchParams.searchParams+'"]');

        if (container && sections.sections && sections.sections[0] && sections.sections[0].products) {
          container.innerHTML = this.template({
            searchParams: searchParams,
            products: sections.sections[0].products
          });
        }
      }
    }
  });
  searchResult.listen();

  fd.modules.common.utils.register("modules.multisearch", "searchResultList", searchResultList, fd);
  fd.modules.common.utils.register("modules.multisearch", "searchResult", searchResult, fd);
}(FreshDirect));
