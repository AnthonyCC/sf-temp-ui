var soId,ticker,isInTransaction=false,lockPopupOn=false,erPanel = null;

function setSoId(_soId){
	soId=_soId;
}

function checkLock(){
	ticker=setInterval(isLocked,1000);
}

function isLocked() {
	if(isInTransaction) return;
	
	isInTransaction=true;
	
	YAHOO.util.Connect.asyncRequest('GET', '/api/so_api.jsp?action=isLocked&soId='+soId, {
			success: function(o) { 
					if (o.responseText=="true"){
						showLockPopup();

					} else if (o.responseText=="false") {
						clearTimeout(ticker);
						if(lockPopupOn) {
							lockPopupOn=false;
							window.location.reload();
						}
					}
					isInTransaction=false;
				},
			failure: function(o){
				isInTransaction=false;
			}
		});
}

function showLockPopup() {
	if(!lockPopupOn){
		lockPopupOn=true;
		
		YAHOO.util.Connect.asyncRequest("GET","/quickshop/so_locked_popup.jsp", {
				success: function(o) {
					erPanel = new YAHOO.widget.Panel("so_lock_msg", {
						fixedcenter: true,
						underlay: "none",
						close: false,
						visible: false,
						modal: true,
						draggable: false,
						width: "250px"
					});
					erPanel.setBody(o.responseText);
				
					erPanel.render(document.body);
					
	// 			override .yui-panel hidden setting
					YAHOO.util.Dom.get("so_lock_msg").style.overflow = "visible";
				
	// 			show panel
					erPanel.show();
				}
			});
	}
}
