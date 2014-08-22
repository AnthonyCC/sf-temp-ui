/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var OAS_UPDATER = "OAS_UPDATER";
  var listPos = ['CategoryNote', 'BrowseTop1', 'BrowseTop2', 'BrowseTop3', 'BrowseBottom1', 'BrowseBottom2'];
  var lastSitePage = null;
  
  function getIfr(listPos){
    var ifr = document.getElementById(OAS_UPDATER+listPos);
    if(!ifr) {
      ifr = document.createElement('IFRAME');
      ifr.id=OAS_UPDATER+listPos;
      ifr.src="about:blank";
      ifr.style.display="none";
      document.body.appendChild(ifr);
    }

    return ifr;
  }
  
  function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query, OAS_POS) {
    var ifr = getIfr(OAS_POS),
        scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query);
    

    if(ifr.contentDocument) {
      var html = common.updateOAS({
        scriptUrl:scriptUrl,
        OAS_POS:OAS_POS
      });
      ifr.contentDocument.write(html);
      ifr.contentDocument.close();
    }
    
  }

  var done = function(pos,s) {  
    var node = document.getElementById(pos) || document.getElementById('oas_b_'+pos);

    // remove iframe
    var ifr = document.getElementById(OAS_UPDATER+pos);
    if (ifr) {
      ifr.parentNode.removeChild(ifr);
    }

    // to prevent the cancellation of image downloading, add the content later
    setTimeout(function () {
      node.innerHTML = s;
      $jq('.oas-cnt:hidden').filter(function() { return !($jq(this).children().is('.emptyOAS')); }).show();
    }, 10);
    
  };
  
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

        listPos.forEach(function (lp) {
          try {
            updateOAS(OAS_url, sitePage, OAS_rns, lp, OAS_query, lp);
          } catch (e) {
            // console.log('OAS update failed: '+e);
          }
        });
      }
    }
  }).listen();


  fd.modules.common.utils.register("updateOAS", "done", done, fd);
  fd.modules.common.utils.register("quickshop.common", "updateOAS", updateOAS, fd);
}(FreshDirect));
