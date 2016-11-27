var FreshDirect = FreshDirect || {};

(function (fd) {

	var $ = fd.libs.$;

	function reduceProductData(previous,current) {
		var $current = $(current),
			name = $current.data('productdata-name'),
			value = $current.val(),
			type = $current.attr('type');
		
		if( type === 'checkbox' ) {
			var propName = $current.prop('checked') ? 'value' : 'data-unchecked-value';
			value = $current.attr(propName);
		}
		if( type === 'radio' && !$current.prop('checked') ) {
			return previous;
		}
		previous[name] = value;
		return previous;
	}
	
	
	function reduceRequired(previous, current) {
		var $current = $(current),
			name = $current.data('productdata-name');
		previous.push(name);
		
		return previous;
	}

	function serializeProductWithRequired(element) {
		var $element = $(element),
			product={},
			configuration={},
			required=[];

		$.makeArray($element.find('[data-component="productData"],[data-component="salesunit"],[data-component="quantitybox.value"]')).reduce(reduceProductData,product);
		$.makeArray($element.find('[data-component="productDataConfiguration"]')).reduce(reduceProductData,configuration);

		if(Object.keys(configuration).length) {
			product.configuration = configuration;
		}

		$.makeArray($element.find('[data-atc-required="true"]')).reduce(reduceRequired,required);
		product.required = required;	
		product.DOMElement = $element;
		
		return product;
	}

	function serializeProduct(element) {
		var $element = $(element),
			product={},
			configuration={};

		$.makeArray($element.find('[data-component="productData"],[data-component="salesunit"],[data-component="quantitybox.value"]')).reduce(reduceProductData,product);
		$.makeArray($element.find('[data-component="productDataConfiguration"]')).reduce(reduceProductData,configuration);

		if(Object.keys(configuration).length) {
			product.configuration = configuration;
		}

		return product;
	}

	function searchUp(element) {
		return $(element).closest('[data-component="product"]')
	}

	function searchDown(element) {
		var $element = $(element);

		if( $element.data('component') === "product" ) {
			return $element;
		} else {
			return $element.find('[data-component="product"]');
		}
	}

  function getCartData(element) {
    var product = $(element).closest('[data-component="product"]'), qtybox, incart,
        cartdata = {
          min: 1,
          max: 99,
          step: 1
        };

    if (product) {
      incart = product.find('.incart-info,[data-component="incartinfo"]').first();
      qtybox = product.find('[data-component="quantitybox"]').first();

      if (incart.size() > 0) {
        cartdata.incart = +incart.attr('data-amount') || parseInt(incart.html(), 10) || 0;
      }

      if (qtybox.size() > 0) {
        cartdata.min = +qtybox.attr('data-min') || 1;
        cartdata.max = +qtybox.attr('data-max') || 99;
        cartdata.step = +qtybox.attr('data-step') || 1;
      }
    }

    return cartdata;
  }

	function serialize(element, collectRequired, ignoreUnavailable) {
		if ($.isArray(element)) { // serialize array of quickshopitems directly instead of dom element
			return  element.map(function(item){
                return {
                  atcItemId: item.itemId,
                  categoryId: item.catId,
                  configuration: item.configuration,
                  productId: item.productId,
                  quantity: item.quantity.quantity,
                  salesUnit: item.salesUnit.filter(function(su){return su.selected;})[0].id,
                  externalAgency: item.externalAgency,
                  externalSource: item.externalSource,
                  externalGroup: item.externalGroup,
                  skuCode: item.skuCode
                };
              });
		}
		var $element = $(element),
			multiproduct = $element.data('ref'),
			products;

		if(multiproduct) {
			products = searchDown($(multiproduct));
		} else {
			products = searchUp($element);
		}

		products = products.filter(function(elem){
			var $toggle = $('[data-component="productToggle"]', this);
			return $toggle.length === 0 || $toggle.prop("checked");
		});

    if (ignoreUnavailable) {
      products = products.filter(function (i, el) {
        var $el = $(el);

        return !$el.hasClass('unavailable');
      });
    }

		return collectRequired ? $.makeArray(products).map(serializeProductWithRequired) : $.makeArray(products).map(serializeProduct);

	}
	
	fd.modules.common.utils.register("modules.common", "productSerialize", serialize, fd);
	fd.modules.common.utils.register("modules.common", "getCartData", getCartData, fd);

}(FreshDirect));
