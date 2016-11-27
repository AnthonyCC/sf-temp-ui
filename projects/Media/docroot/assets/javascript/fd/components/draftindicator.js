var FreshDirect = FreshDirect || {};

(function(fd){
  if (fd && fd.activeDraft) {
    var box = document.createElement('div');
    box.innerHTML = "<b>Current draft:</b><br>"+fd.activeDraft+"<br>";
    if (fd.activeDraftDirectLink){
      box.innerHTML = box.innerHTML + "<a href="+fd.activeDraftDirectLink+">Direct draft link</a>"
    }
    box.className = "draftIndicator";
    box.setAttribute('draft-name', fd.activeDraft);
    document.body.appendChild(box);
  }
}(FreshDirect));
