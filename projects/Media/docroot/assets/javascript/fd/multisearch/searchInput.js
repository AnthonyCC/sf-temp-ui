/*global multisrch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;
  var MAXTERMS = fd.multisearch.limit || 25;

  var searchInput = Object.create(WIDGET,{
    signal:{
      value:'multiSearchTerms'
    },
    template:{
      value:multisrch.searchInput
    },
    placeholder:{
      value:'#multisearch-input'
    },
    terms:{
      value:[],
      writable: true
    },
    handleSubmit:{
      value: function (e) {
        e.preventDefault();

        var $el = $('[data-component="multisearch-input"] input'),
            term = $el.val();

        this.addTerm(term);
        $el.val('');
      }
    },
    addTerm:{
      value: function (term) {
        var idx = this.terms.indexOf(term);

        if (term && this.terms.length < MAXTERMS) {
          if (idx === -1) {
            this.terms.unshift(fd.utils.escapeHtml(term));

            this.termsChanged();
          }
        }
      }
    },
    removeTerm:{
      value: function (e) {
        var termEl = $(e.currentTarget).parent(),
            term = termEl.attr('data-searchterm'),
            idx = this.terms.indexOf(term);

        if (idx > -1) {
          this.terms.splice(idx, 1);
        }

        this.termsChanged();
      }
    },
    termsChanged: {
      value: function (dontpush) {
        DISPATCHER.signal('multiSearch', {
          terms: this.terms
        });

        if (!dontpush) {
          // set new url
          var pathname = window.location.pathname;
          window.history.pushState({terms: this.terms}, "search: "+this.terms.join(', '), pathname + "?q=" + this.terms.join(','));
        }

        this.render();
      }
    },
    render:{
      value:function(data){
        data = data || {};
        data.terms = data.terms || this.terms;
        data.maxItems = MAXTERMS;

        if (this.terms !== data.terms) {
          this.terms = data.terms;
          this.termsChanged(data.dontpush);
        }

        WIDGET.render.call(this, data);
        FreshDirect.components.autoComplete.init(this.placeholder+' input');
        $('[data-component="multisearch-input"] input').val("");
      }
    }
  });
  searchInput.listen();

  $(document).on('submit', '[data-component="multisearch-input"] form', searchInput.handleSubmit.bind(searchInput));
  $(document).on('click', '[data-component="multisearch-input"] button.remove', searchInput.removeTerm.bind(searchInput));

  fd.modules.common.utils.register("modules.multisearch", "searchInput", searchInput, fd);
}(FreshDirect));

// initialize based on query parameters
(function (fd) {
  var DEFAULTLIST = fd.multisearch.defaultList ? fd.multisearch.defaultList.split(',') : [
    'eggs',
    'milk',
    'pizza',
    'yogurt',
    'butter',
    'carrots',
    'garlic',
    'bread',
    'onion',
    'chicken'
  ];

  setTimeout(function () {
    var q = fd.utils.getParameterByName('q').split(',').filter(function (kw) { return kw; });

    if (q.length === 0) {
      q = DEFAULTLIST;
    }

    q = q.filter(function (kw) { return kw; }).map(function (kw) { return kw.trim(); }).map(fd.utils.escapeHtml);

    fd.modules.multisearch.searchInput.render({
      terms: q
    });
  }, 10);
}(FreshDirect));

// subscribe for popstate if feature is active
(function (fd) {
  var el = document.querySelector('#multisearch-input');

  if (el) {
    window.onpopstate = function (e) {
      var state = e.state;
      fd.modules.multisearch.searchInput.render({
        terms: state.terms,
        dontpush: true
      });
    };
  }
}(FreshDirect));

// mobweb submen menu init
(function (fd) {
  var el = document.querySelector('.refine-btn-cont .modify-list-btn');

  if (el) {
    el.addEventListener('click', function (e) {
      var leftnav = document.querySelector('.leftnav');

      if (leftnav.style.display === 'none') {
        leftnav.style.display = 'block';
      } else {
        leftnav.style.display = 'none';
      }
    });
  }
}(FreshDirect));

