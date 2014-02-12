/*global jQuery*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;

	function filterValue(val) {
		return !val.invalid && val['salesUnit'] && parseFloat(val['quantity'])>0;
	};
	
	function atcFilter(items) {
		var productList; 
		
		return items.map(function(item){
			delete item.DOMElement;
			delete item.required;
			delete item.isAlcoholic;
			return item;
		});
	}

	function atcHandler(event){
		
		var productList = atcFilter(event.atcList),
			request = { items:productList };

		
		$.extend(request,event.ATCMeta,event.cmData);
		fd.components.atcInfo.setServerMessage(request.items);
		fd.common.dispatcher.signal('server',{
			url:'/api/addtocart',
			data:{data:JSON.stringify(request)},
			method:'POST'			
		});			
	}
	
	function requiredValidator(items){
		var valid = true;
		
		items.forEach(function(item){
			var required = item.required;
			if(required.length) {
				Object.keys(item).forEach(function(data){
					if(required.inArray(data) && (!item[data] || item[data]==='0') ) {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').addClass('missing-data');
						item.invalid = true;
						valid = false;
					}
				});
				Object.keys(item.configuration || {}).forEach(function(data){
					if(required.inArray(data) && !item.configuration[data]) {
						item.DOMElement.find('[data-productdata-name="'+data+'"]').addClass('missing-data');
						item.invalid = true;
						valid = false;
					}
				});
			}
		});
		
		return valid;
	}
	
	function usqValidator(items) {
		return items.some(function(item){
			return item.isAlcoholic === 'true'; 
		});		
	}
	
	
	var ATC_BUS = new Bacon.Bus();
	var BASIC_ATC = ATC_BUS.filter(function(event){ return requiredValidator(event.items)}).toProperty();
	
	Bacon.combineWith(function(value,usqState){
		value.usqState = usqState;
		return value;
	},BASIC_ATC.map(function(event){
		event.containsAlcoholic=usqValidator(event.items);
		return event;
	}),fd.USQWarning.Popup.state).flatMapLatest(function(value){
		if(value.containsAlcoholic && !value.usqState && fd.USQWarning.Popup.isClosed()){
			fd.USQWarning.Popup.open();
			return fd.USQWarning.Popup.state.changes().flatMapLatest(function(accepted){
				value.usqState = accepted;
				return Bacon.once(value);
			})
		} else {
			return Bacon.once(value);
		}
	}).map(function(value){
		var newItems = value.items;
		fd.USQWarning.Popup.close();
		if(value.containsAlcoholic && !value.usqState) {
			newItems = newItems.filter(function(item){
				return !item.isAlcoholic;
			});
		}
		value.items = newItems.filter(filterValue); 
		return value;
	}).filter(function(event){
		return event.items.length > 0;
	}).onValue(function(event){
		triggerATC(event.items,{},event.target);
	});

	
	function triggerATC(items,meta,triggerElement){
		$(triggerElement || document).trigger({
			type:'addToCart',
			atcList:items,
			ATCMeta:(meta || {}),
			valid:true,
			cmData:{}
		});		
	}
	
	function addToCart(element) {
		var items = fd.modules.common.productSerialize(element, true);
		ATC_BUS.push(items);
	}

	
	$(document).on('click','[data-component="ATCButton"]',function(e){
		var items = fd.modules.common.productSerialize(e.target, true);
		e.items = items;
		ATC_BUS.push(e);
	});

	$(document).on('addToCart',atcHandler);

	
  fd.modules.common.utils.register("components", "AddToCart", {
	  addToCart:addToCart,
	  requiredValidator:requiredValidator,
	  atcFilter:atcFilter,
	  triggerATC:triggerATC
  } , fd);

}(FreshDirect));

