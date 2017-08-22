/*global jQuery,common,postscribe*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var OAS_UPDATER = "OAS_UPDATER";
  var listPos = [];
  var lastSitePage = null;
  
  function OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
		return OAS_url + 'adstream_mjx.ads/' +
				OAS_sitepage + '/1' + OAS_rns + '@' +
				OAS_listpos + '?' + OAS_query;
  }
  
  function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query) {
    var scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos.join(','), OAS_query);
    
    postscribe(document.body, '<script src="'+scriptUrl+'"></script>', {
        done: function () {
          done(OAS_listpos);
        }, error: function () {}
    });
  }

  function done(listPos) {
		listPos.forEach(function (pos) {
			var selector = "#oas_"+pos+",#oas_b_"+pos;
			$(selector).each(function(i,e){
				$(e).html('');
				postscribe($(e), '<script>OAS_RICH("'+pos+'");</script>', {
					error: function () {},
					done: function (pos) {
						$.each($('a[href*="/default/empty.gif/"]'), function(ii, ee) {
							$(ee).attr("tabindex", "-1");
							$(ee).attr("role", "presentation");
							$(ee).attr("aria-hidden", "true");

							if (fd.utils.isDeveloper()) {
								console.log('updateOAS: done', pos, $(ee));
							}
						});
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
