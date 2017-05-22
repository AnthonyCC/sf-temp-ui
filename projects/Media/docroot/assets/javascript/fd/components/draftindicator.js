var FreshDirect = FreshDirect || {};

(function(fd){
  var $=fd.libs.$;

  if (fd && fd.activeDraft) {
    var box = document.createElement('div');
    box.innerHTML = "<b>Current draft:</b><br>"+fd.activeDraft+"<br>" + "<button onclick='FreshDirect.components.leavedraft()'>Leave draft!</button>";
    box.className = "draftIndicator";
    box.setAttribute('draft-name', fd.activeDraft);
    document.body.appendChild(box);
  }

  function stripUrlParams(url, paramsToStrip){
    paramsToStrip = paramsToStrip && paramsToStrip.slice() || [];
    return url.replace(/&?([^?=]+)=[^&]+/g, function(match, key) {
      return (paramsToStrip.indexOf(key) !== -1) ? '' : (paramsToStrip.push(key), match);
    });
   }

  var leaveDraft = function () {
    document.cookie = 'activeDraftId=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.cookie = 'activeDraftName=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    var url = stripUrlParams(window.location.href, ['draftName', 'draftId']);
    window.location.replace(url);
  }

  fd.modules.common.utils.register("components", "leavedraft", leaveDraft, fd);
}(FreshDirect));