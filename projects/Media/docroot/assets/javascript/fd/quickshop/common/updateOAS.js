/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	
	var OAS_UPDATER = "OAS_UPDATER_",
		i=0;
	
	function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query, OAS_POS) {
		var ifr = document.createElement('IFRAME'),
			scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query);
		
		i++;
		ifr.id=OAS_UPDATER+i;
		ifr.src="about:blank";
		document.body.appendChild(ifr);

		if(ifr.contentDocument) {
			// OMG, MY ADS ARE BURNING
			ifr.contentDocument.write('<!DOCTYPE html><head><script src="'+scriptUrl+'"></script></head><body><script>OAS_RICH("'+OAS_POS+'");window.parent.FreshDirect.quickshop.common.updateOAS.done("'+OAS_POS+'",document.body.innerHTML)</script></body></html>');
		}
		
	}

	updateOAS.done = function(pos,s) {	
		document.getElementById(pos).innerHTML=s;
	};
	
	Object.create(fd.common.signalTarget,{
		allowNull:{
			value:true
		},
		signal:{
			value:'departmentChanged'
		},
		callback:{
			value:function(data) {
				var siteUrl = "www.freshdirect.com/quickshop/";
				if(data) {
					siteUrl = siteUrl + data + '/';
				}
				updateOAS(OAS_url, siteUrl, OAS_rns, OAS_listpos, OAS_query, "QSTop");
			}
		}
	}).listen();
	
	
	fd.modules.common.utils.register("quickshop.common", "updateOAS", updateOAS, fd);
}(FreshDirect));
