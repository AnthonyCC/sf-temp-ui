/*global jQuery,common,postscribe*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var OAS_UPDATER = "OAS_UPDATER";
  var listPos = [];
  var lastSitePage = null;
  
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
      var cnt = $jq("#oas_b_"+pos);
      if (cnt.size()) {
        cnt.html('');
        postscribe(cnt[0], '<script>OAS_RICH("'+pos+'");</script>',{ error: function () {}
          });
      }
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

        if (lastSitePage === sitePage) {
          return;
        } else {
          lastSitePage = sitePage;
        }

        if (data.contentId) {
          OAS_query = OAS_query.replace(/id=.*?&/, "id="+data.contentId+"&");
        }

        updateOAS(OAS_url, sitePage, OAS_rns, listPos, OAS_query);
      }
    }
  }).listen();

  function initListPoses(){
    $("[id^='oas_b_']").each(function(){ 
      var prefix = 'oas_b_',
          oasName = $(this).attr('id').slice(prefix.length);

      if(oasName){ listPos.push(oasName); }
    });
  }

  initListPoses();

  fd.modules.common.utils.register("updateOAS", "done", done, fd);
  fd.modules.common.utils.register("quickshop.common", "updateOAS", updateOAS, fd);
}(FreshDirect));
