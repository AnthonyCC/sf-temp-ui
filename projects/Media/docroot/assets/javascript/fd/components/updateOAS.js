/*global jQuery,common,postscribe*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var OAS_UPDATER = "OAS_UPDATER";
  var listPos = [];
  var lastSitePage = null;
  var inProgressRequests = [];
  function OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
		return OAS_url + 'adstream_mjx.ads/' +
				OAS_sitepage + '/1' + OAS_rns + '@' +
				OAS_listpos + '?' + OAS_query;
  }
  
  function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query, showAsPopUp) {
    var scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos.join(','), OAS_query);
    if (showAsPopUp) {
    	if (inProgressRequests.indexOf(OAS_listpos.join(',')) !== -1) {
    		return;
    	} else {
    		inProgressRequests.push(OAS_listpos.join(','));
    	}
    }
    postscribe(document.body, '<script src="'+scriptUrl+'"></script>', {
        done: function () {
          done(OAS_listpos, showAsPopUp);
        }, error: function () {}
    });
  }

  function done(listPos, showAsPopUp) {
		listPos.forEach(function (pos) {
			var selector = "#oas_"+pos+",#oas_b_"+pos;
			$(selector).each(function(i,e){
				$(e).html('');
				postscribe($(e), '<script>OAS_RICH("'+pos+'");</script>', {
					error: function () {},
					done: function (pos) {
						var hasEmptyImage = false;
						$.each($('a[href*="/default/empty.gif/"]'), function(ii, ee) {
							hasEmptyImage = true;
							$(ee).attr("tabindex", "-1");
							$(ee).attr("role", "presentation");
							$(ee).attr("aria-hidden", "true");

							if (fd.utils.isDeveloper()) {
								console.log('updateOAS: done', pos, $(ee));
							}
						});
						// if oas doesn't return an empty image and we need to show the oas as popup
						if (showAsPopUp && !hasEmptyImage) {
							inProgressRequests[inProgressRequests.indexOf(listPos.join(','))] = null;
							setTimeout( function() {
								var oasLink = $(e).children().not('script').first();
								if (oasLink.length) {
									$(e).attr('data-tooltip', true);
									if (!oasLink.has('.tooptip-indicator').length) {
										$(e).append('<div class="tooptip-indicator"></div>')
									}
									$(e).show();
									if (fd.mobWeb) {
										$(e).css({
											'margin-top': '-' + (11 + $(e).height()) + 'px'
											});
										$('[data-tooltip] .tooptip-indicator').css({
											'margin-left': (($(e).parent().width() /2) - 11) + 'px'
										});
									} else {
										$(e).css({
											'margin-top': '-' + (11 + $(e).height()) + 'px',
											'margin-left': (-1 * (($(e).width() - $(e).parent().width()) /2)) + 'px'
											});
										$('[data-tooltip] .tooptip-indicator').css({
											'margin-left': (( $(e).width() /2) - 11) + 'px'
										});
									}
									$(e).fadeIn(1000);
								}
							}, 1000);
						}
					}
				});
			});
		});
	}

  Object.create(fd.common.signalTarget,{
    allowNull:{
      value:true
    },
    signal:{
      value:'descriptiveContent'
    },
    callback:{
      value:function(data) {
        var sitePage;

        if (data && data.oasSitePage) {
          sitePage = data.oasSitePage;
        } else {
          sitePage = 'www.freshdirect.com/browse';
        }

//        if (lastSitePage === sitePage) {
//          return;
//        } else {
//          lastSitePage = sitePage;
//        }

        if (data.contentId) {
          OAS_query = OAS_query.replace(/id=.*?&/, "id="+data.contentId+"&");
        }
        
        if (data.url) {
        	 try {
        		 OAS_query = OAS_query.replace(/searchParams=.*?&/, "searchParams=" + decodeURIComponent(data.url).match(/searchParams=([^&]+)&/)[1] + "&");
        	 } catch (e) {
        		 
        	 }
        }

        updateOAS(OAS_url, sitePage, OAS_rns, listPos, OAS_query);
      }
    }
  }).listen();

  function initListPoses(){
    $("[id^='oas_']").each(function(){ 
    	var curId = $(this).attr('id'), prefix ='', oasName = null;
    	if (curId.indexOf('oas_b_') === 0) {
    		prefix = 'oas_b_';
    	} else if (curId.indexOf('oas_') === 0) {
    		prefix = 'oas_';
    	}
    	listPos.push(curId.slice(prefix.length));
    });
  }

  initListPoses();

  fd.modules.common.utils.register("updateOAS", "done", done, fd);
  fd.modules.common.utils.register("quickshop.common", "updateOAS", updateOAS, fd);
  fd.modules.common.utils.register("modules.common", "updateOAS", updateOAS, fd);
}(FreshDirect));
