'use strict';


var FreshDirect = window.FreshDirect || {};
var GTMID = window.FreshDirect && window.FreshDirect.gtm && window.FreshDirect.gtm.key;
var GTMAUTH = window.FreshDirect && window.FreshDirect.gtm && window.FreshDirect.gtm.auth;
var GTMPREVIEW = window.FreshDirect && window.FreshDirect.gtm && window.FreshDirect.gtm.preview;
var dataLayer = window.dataLayer || [];

// data processors to transform incoming data and put it into the dataLayer
(function(fd) {
  var $=fd.libs.$;

  fd.gtm = fd.gtm || {};

  var sectionProducts = function (section) {
    var products = [];

    if (section.products) {
      products = products.concat(section.products);
    }

    if (section.sections) {
      section.sections.forEach(function (sec) {
        products = products.concat(sectionProducts(sec));
      });
    }

    return products;
  };

  var productTransform = function (product, idx, list) {
    var productData = {
      name: product.productName,
      id: product.productId,
      price: product.price,
      brand: product.brandName,
      category: product.catId,
      variant: product.variantId || 'default variant',
      list: list || fd.gtm.getList(),
      new_product: product.newProduct,
      sku: product.skuCode,
      in_stock: product.available
    };

    if (idx) {
      productData.position = idx;
    }

    return productData;
  };

  var safeName = function (text) {
    return text.toString().toLowerCase()
      .replace(/\s+/g, '_')         // Replace spaces with _
      .replace(/[^\w\-]+/g, '')     // Remove all non-word chars
      .replace(/__+/g, '_')         // Replace multiple _ with single _
      .replace(/^_+/, '')           // Trim _ from start of text
      .replace(/_+$/, '');          // Trim _ from the end of text
  };

  var resetImpressions = function () {
    FreshDirect.gtm.setValue('ecommerce.impressions', null);
    FreshDirect.gtm.setValue('ecommerce.impressions', []);
  };

  fd.gtm.PROCESSORS = {
    customer: function (custData) {
      dataLayer.push({
        customer: {
          county: custData.county || '',
          marketing_segment: custData.marketingSegment || '',
          order_count: custData.orderCount || 0,
          delivery_pass_status: custData.deliveryPassStatus || '',
          customer_id: custData.customerId || '',
          zip_code: custData.zipCode || '',
          user_id: custData.userId || '',
          user_status: custData.userStatus || '',
          user_type: custData.userType || '',
          login_type: custData.loginType || '',
          chef_table: custData.chefsTable || false,
          deliverypass: custData.deliveryPass || '',
          delivery_type: custData.deliveryType || '',
          cohort: custData.cohort || 'unknown',
          default_payment_type: custData.defaultPaymentType || ''
        },
        // variables for "old" tags
        'user-customer-type': custData.deliveryType || '',
        'user-ct-status': custData.chefsTable || false,
        'user-del-county': custData.county || '',
        'user-marketing-segment': custData.marketingSegment || '',
        'user-order-count': custData.orderCount || 0,
        'user-dp-status': custData.deliveryPassStatus || '',
        'user-customer-id': custData.customerId || '',
        // used in some events (why not using customer object?
        user_id: custData.userId
      });
    },
    pageType: function (pageData) {
      dataLayer.push({
        page_name: document.title,
        page_type: pageData.pageType,
        page_language: pageData.pageLanguage
      });
    },
    search: function (searchData) {
      dataLayer.push({
        search_keyword: searchData.searchKeyword,
        search_results: searchData.searchResults
      });
    },
    sections: function (sectionData) {
      var list = fd.gtm.getList(),
          products = sectionProducts(sectionData).map(function (product, idx) {
            return productTransform(product, idx+1, list);
          });

      fd.gtm.reportImpressions(products);
    },
    items: function (reorderItems) {
      var list = fd.gtm.getList(),
          products = reorderItems.map(function (product, idx) {
            return productTransform(product, idx+1, list);
          });

      fd.gtm.reportImpressions(products);
    },
    product: function (productData) {
      var product = productTransform(productData);

      delete product.list;

      dataLayer.push({
        ecommerce: {
          detail: {
            products: [product]
          }
        }
      });

      return {
        event: 'impressionsPushed'
      };
    },
    ATCData: function (ATCData) { // + cartLineChange
      var productData = ATCData.productData,
          qty = parseInt(productData.quantity, 10) || 0,
          addRemoveData = {
              products: [{
                id: productData.id,
                name: productData.name,
                price: productData.price,
                brand: productData.brand,
                category: productData.category,
                variant: productData.variant,
                new_product: productData.newProduct,
                sku: productData.sku,
                in_stock: true,
                quantity: qty > 0 ? qty : -qty
              }]
          },
          ecommerce = {},
          event = qty > 0 ? 'addToCart' : 'removeFromCart';

      ecommerce[qty > 0 ? 'add' : 'remove'] = addRemoveData;

      // send extra ATC-succes event for backward compatibility
      dataLayer.push({
        productId: productData.id,
        event: 'ATC-success'
      });

      dataLayer.push({
        ecommerce: ecommerce
      });

      return {event: event};
    },
    coStep: function (coStepData) {
      dataLayer.push({
        ecommerce: {
          checkout: {
            actionField: coStepData
          }
        }
      });
    },
    topNavClick: function (data) {
      dataLayer.push({
        eventCategory: 'header-nav',
        eventAction: 'click',
        eventLabel: data.title
      });
    },
    leftNavClick: function (data) {
      dataLayer.push({
        eventCategory: 'left-nav',
        eventAction: 'click',
        eventLabel: data.title
      });
    },
    sortClick: function (sortData) {
      dataLayer.push({
        eventCategory: 'sort-menu',
        eventAction: 'click',
        eventLabel: sortData.sortBy + (sortData.orderAscending ? ' ascending' : ' descending'),
        page_name: document.title
      });
    },
    zipCheckSuccess: function (data) {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;
      dataLayer.push({
        eventCategory: 'Lightbox Tracking',
        eventAction: 'zipcode-entered',
        user_id: customer.userId,
        zip_code: data.zipCode
      });

      return {event: 'lightbox-zipcode'};
    },
    zipCheckClosed: function () {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;
      dataLayer.push({
        eventCategory: 'Lightbox Tracking',
        eventAction: 'lightbox-closed',
        user_id: customer.userId
      });

      return {event: 'lightbox-zipcode'};
    },
    checkout: function (coData) {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer,
          ts = coData.selectedTimeslotValue;

      if (coData.newOrder === 'true' || coData.modifyOrder === 'true') {
        dataLayer.push({
          ecommerce: {
            purchase: {
              actionField: {
                id: coData.orderId + (+coData.modifiedOrderCount ? '-'+coData.modifiedOrderCount : '')|| '',
                payment_type: coData.paymentType || '',
                revenue: coData.revenue || 0,
                tax: coData.tax || 0,
                shipping: coData.shippingCost || 0,
                coupon: coData.couponCode && coData.couponCode.join(','),
                redemption_code: coData.redemptionCode && coData.redemptionCode.join(','),
                etipping: coData.etipping || 0
              },
              products: coData.products && Object.keys(coData.products).map(function (k) {
                var productData = coData.products[k];
                return {
                  id: productData.id,
                  name: productData.name,
                  price: productData.price,
                  brand: productData.brand,
                  category: productData.category,
                  variant: productData.variant,
                  new_product: productData.newProduct,
                  sku: productData.sku,
                  in_stock: true,
                  quantity: parseInt(productData.quantity, 10) || 0 // quantity should be an integer
                };
              }),
              delivery_type: coData.deliveryType || 'unknown',
              available_timeslot_value: ts && ts.deliveryDate+' '+ts.displayString || 'unknown',
              unavailable_timeslot_present: coData.unavailableTimeslotValue ? 'yes' : 'no'
            }
          }
        });

        dataLayer.push({
          'co-subtotal': coData.revenue || 0,
          'co-subtotal-nd': (+coData.revenue || 0) * 100,
          'co-neworder': coData.newOrder,
          'co-modifyorder': coData.modifyOrder,
          'co-orderid': coData.orderId,
          'co-totalcartitems': Object.keys(coData.products).map(function (k) { return +coData.products[k].quantity;}).reduce(function (p, c) { return p+c;}, 0),
          'co-usercounty': customer && customer.county,
          'co-discountamount': coData.discountAmount || 0,
          'co-discountamount-nd': (+coData.discountAmount || 0) *100,
          'co-productid': Object.keys(coData.products).map(function (k) { return coData.products[k].id;}).join(','),
          'co-validorders': customer && customer.orderCount,
          'co-promocode': coData.redemptionCode && coData.redemptionCode.join(',')
        });

      } else if (coData.products) {
        dataLayer.push({
          ecommerce: {
            checkout: {
              products: coData.products && Object.keys(coData.products).map(function (k) {
                var productData = coData.products[k];
                return {
                  id: productData.id,
                  name: productData.name,
                  price: productData.price,
                  brand: productData.brand,
                  category: productData.category,
                  variant: productData.variant,
                  new_product: productData.newProduct,
                  sku: productData.sku,
                  in_stock: true,
                  quantity: parseInt(productData.quantity, 10) || 0 // quantity should be an integer
                };
              })
            }
          }
        });
      }

      if (coData.modifyOrder === 'true') {
        return {event: 'modify-order-success'};
      }

      if (coData.newOrder === 'true') {
        // trigger extra checkout-success-newcustomer event for new customers
        if (customer && customer.orderCount <= 1) {
          dataLayer.push({
            event: 'checkout-success-newcustomer'
          });
        }

        return {event: 'checkout-success'};
      }

      return null;
    },
    cancelOrder: function (orderData) {
      if (orderData.orderId) {
        dataLayer.push({
          eventCategory: 'Orders',
          eventAction: 'cancelled-order',
          eventLabel: orderData.orderId
        });
        return {
          event: 'order-cancellation'
        };
      }

      return null;
    },
    login: function (loginData) {
      if (loginData.loginAttempt === 'success') {
        fd.gtm.updateDataLayer({loginSuccess: true});
      } else if (loginData.loginAttempt === 'socialLoginSuccess') {
        fd.gtm.updateDataLayer({socialLoginSuccess: true});
      } else if (loginData.loginAttempt === 'fail') {
        fd.gtm.updateDataLayer({loginFailure: true});
      }
    },
    loginSuccess: function () {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;

      if (customer) {
        // send legacy login event
        dataLayer.push({
          event: 'user-login'
        });

        // GA related login data
        dataLayer.push({
          eventCategory: 'Login',
          eventAction: 'login-success',
          user_id: customer.userId,
          user_status: customer.userStatus,
          login_type: customer.loginType,
          delivery_type: customer.deliveryType
        });

        return {event: 'user-login-success'};
      }

      return null;
    },
    loginFailure: function () {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;

      dataLayer.push({
        eventCategory: 'Login',
        eventAction: 'login-failure',
        login_type: customer && customer.loginType
      });

      return {event: 'user-login-failure'};
    },
    signup: function (signupData) {
      if (signupData.signupAttempt === 'success') {
        fd.gtm.updateDataLayer({signupSuccess: true});
      } else if (signupData.signupAttempt === 'socialSignupSuccess') {
        fd.gtm.updateDataLayer({socialSignupSuccess: true});
      } else if (signupData.signupAttempt === 'fail') {
        fd.gtm.updateDataLayer({signupFailure: true});
      }
    },
    signupSuccess: function () {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;

      if (customer) {
        // send legacy signup event
        dataLayer.push({
          event: 'user-signup'
        });

        // GA related login data
        dataLayer.push({
          eventCategory: 'Signup',
          eventAction: 'signup-success',
          user_id: customer.userId,
          user_status: customer.userStatus,
          login_type: customer.loginType
        });

        return {event: 'user-signup-success'};
      }

      return null;
    },
    signupFailure: function () {
      var customer = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.customer;

      dataLayer.push({
        eventCategory: 'Signup',
        eventAction: 'signup-failure',
        login_type: customer && customer.loginType
      });

      return {event: 'user-signup-failure'};
    }
  };
  fd.gtm.PROCESSORS.cartLineChange = fd.gtm.PROCESSORS.ATCData;
  fd.gtm.PROCESSORS.socialLoginSuccess = fd.gtm.PROCESSORS.loginSuccess;
  fd.gtm.PROCESSORS.socialSignupSuccess = fd.gtm.PROCESSORS.signupSuccess;

  fd.gtm.updateDataLayer = function (gtmData, gtmEvent) {
    if (gtmData) {
      Object.keys(gtmData).forEach(function (k) {
        var data = gtmData[k],
            dlObj = {},
            retEv;

        if (fd.gtm.PROCESSORS[k]) {
          retEv = fd.gtm.PROCESSORS[k](data);
          gtmEvent = gtmEvent || retEv;
        } else {
          dlObj[k] = data;
          dataLayer.push(dlObj);
        }

        if (fd.utils.isDeveloper()) {
          console.info('[gtm] dataLayer updated', dataLayer[dataLayer.length - 1]);
        }

      });
    }

    if (gtmEvent) {
      if (fd.utils.isDeveloper()) {
        console.info('[gtm] sending event: "'+gtmEvent.event+'" , callback: ', gtmEvent.callback);
      }

      dataLayer.push({
        event: gtmEvent.event,
        eventCallback: gtmEvent.callback
      });
    }
  };

  // product tile serialization
  fd.gtm.getProductData = function (productEl) {
    var productE = $(productEl).closest('[data-component="product"]'),
        productId = productE.attr('data-product-id'),
        productData,
        moduleE;

    if (productId) {
      productE = $('[data-product-id="'+productId+'"]').first();
    }

    productData = fd.modules.common.productSerialize(productE)[0];
    moduleE = $(productE).closest('.content-module')[0];

    productData.brand = productE.find('.portrait-item-header-name b').text();
    productData.name = productE.find('.product-name-no-brand').text() || productE.find('.portrait-item-header-name').text();
    productData.price = productE.attr('data-price');
    productData.in_stock = productE.attr('data-in-stock');
    productData.new_product = productE.attr('data-new-product');
    productData.variant = productE.attr('data-variant');
    productData.position = parseInt(productE.attr('data-position'), 10) || 0;
    productData.list = productE.attr('data-list') || productE.attr('data-virtual-category');

    if (!productData.position) {
      $('[data-component="product"]').each(function (i, el) {
        if (!productData.position && $(el).attr('data-product-id') === productE.attr('data-product-id')) {
          productData.position = i+1;
        }
      });
    }

    if (!productData.list && moduleE) {
      productData.list = moduleE.id;
    }

    if (productE.hasClass('transactional-related-body')) {
      productData.list = 'cross-sell';
      productData.position = 1;
    }

    if (productE.hasClass('carouselTransactionalItem') && !productData.list) {
      var carouselE = productE.closest('[data-component="tabbedRecommender"]');

      if (carouselE.length) {
        productData.list = safeName(carouselE.find('.tabs li.selected').text());
      } else {
        carouselE = productE.closest('.carousels');
        if (carouselE.length) {
          productData.list = safeName(carouselE.find('.header').text());
        }
      }
    }

    productData.list = fd.gtm.getList() + (productData.list ? '_'+productData.list : '');

    return productData;
  };

  // report product impressions for a DOM element
  fd.gtm.reportImpressionsEl = function (el, type) {
    var $el = $(el),
        products = [];

    type = type || 'impressionsPushed';

    var reportProduct = function (elm) {
      var productData = fd.gtm.getProductData(elm);

      return {
        id: productData.productId,
        name: productData.name,
        price: productData.price,
        brand: productData.brand,
        category: productData.categoryId,
        variant: productData.variant !== 'null' ? productData.variant : "default variant",
        new_product: productData.new_product,
        sku: productData.skuCode,
        in_stock: productData.in_stock,
        position: parseInt(productData.position, 10) || 0,
        list: productData.list
      };
    };

    $el.each(function (i, elm) {
      if ($(elm).is('[data-component="product"]')) {
        products.push(reportProduct(elm));
      }
    });

    $el.find('[data-component="product"]').each(function (i, elm) {
      if ($(elm).closest('[data-component="relateditem"]').length === 0) {
        products.push(reportProduct(elm));
      }
    });

    fd.gtm.reportImpressions(products, type);
  };

  // report product impressions
  fd.gtm.reportImpressions = function (products, type) {
    type = type || 'impressionsPushed';
    var checkAndReport = function () {
      if (fd.gtm.check()) {
        resetImpressions();
        dataLayer.push({
          ecommerce: {
            impressions: products
          },
          event: type
        });

        if (fd.utils.isDeveloper()) {
          console.info('[gtm] impressions reported: ', type, products);
        }
      } else {
        setTimeout(checkAndReport, 100);
      }
    };

    checkAndReport();
  };

  // get actual dataLayer value (for verification)
  fd.gtm.getValue = function (query) {
    return window.google_tag_manager[GTMID].dataLayer.get(query);
  };

  // set actual dataLayer value (for impressions reset)
  fd.gtm.setValue = function (prop, value) {
    return window.google_tag_manager[GTMID].dataLayer.set(prop, value);
  };

  // check if GTM is loaded
  fd.gtm.check = function () {
    return window.google_tag_manager && window.google_tag_manager[GTMID];
  };

  // get list parameter for products
  fd.gtm.getList = function () {
    var listName = fd.utils.getParameterByName('id'),
        urlPageType = fd.utils.getParameterByName('pageType'),
        pageType = fd.gtm.data && fd.gtm.data.googleAnalyticsData && fd.gtm.data.googleAnalyticsData.pageType && fd.gtm.data.googleAnalyticsData.pageType.pageType,
        searchParams = fd.utils.getParameterByName('searchParams');

    // pageName for PLP pages => last breadcrumb
    if ($('ul.breadcrumbs li').length) {
      listName = safeName($('ul.breadcrumbs li').last().text());
    } else if (searchParams) {
      listName = searchParams.replace(/\s+/g, '+');
    } else if ($('ul.qs-tabs li .selected').length) {
      pageType = 'reorder';
      listName = safeName($('ul.qs-tabs li .selected').text());
    }

    // change pageType to 'browse' for product and category list pages
    if ('product_list|category_list'.indexOf(pageType.toLowerCase()) > -1) {
      pageType = 'browse';
    }

    return ((urlPageType || pageType || 'browse')+(listName ? '_'+listName : '')).toLowerCase();
  };
}(FreshDirect));

// process data layer related attributes available at page load
(function(fd) {
  var $=fd.libs.$;
  var gtmData = fd.gtm && fd.gtm.data && fd.gtm.data.googleAnalyticsData;
  var browseData = fd.browse && fd.browse.data;
  var productData = fd.pdp && fd.pdp.data;

  if (gtmData) {
    fd.gtm.updateDataLayer(gtmData);
  }

  // browse/search related update
  if (browseData) {

    // general GTM update
    if (browseData.googleAnalyticsData && browseData.googleAnalyticsData.googleAnalyticsData) {
      fd.gtm.updateDataLayer(browseData.googleAnalyticsData.googleAnalyticsData);
    }

    // product list
    if (!productData && browseData.sections) {
      fd.gtm.updateDataLayer({sections: browseData.sections});
    }
  }

  // product details
  if (productData) {
    fd.gtm.updateDataLayer({product: productData});
  }

  // collect impressions from content modules
  $('.content-module.product-list').each(function (i, el) {
    fd.gtm.reportImpressionsEl(el);
  });

}(FreshDirect));

// listen for gtm related data
(function(fd) {
  // general gtm data update
  var gtmUpdate = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'googleAnalyticsData'
    },
    callback: {
      value: function (data) {
        if (data.googleAnalyticsData) {
          fd.gtm.updateDataLayer(data.googleAnalyticsData);
        }
      }
    }
  });

  gtmUpdate.listen();

  // product impression update (via DOM element list)
  var productImpressions = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'productImpressions'
    },
    callback: {
      value: function (data) {
        setTimeout(function () {
          if (data.el) {
            fd.gtm.reportImpressionsEl(data.el, data.type);
          } else if (data.products) {
            fd.gtm.reportImpressions(data.products, data.type);
          }
        }, 10);
      }
    }
  });

  productImpressions.listen();

  // product list update (browse)
  var sectionsUpdate = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'sections'
    },
    callback: {
      value: function (data) {
        setTimeout(function () {
          fd.gtm.updateDataLayer({sections: data});
        }, 10);
      }
    }
  });

  sectionsUpdate.listen();

  // product list update (reorder)
  var reorderUpdate = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'items'
    },
    callback: {
      value: function (data) {
        setTimeout(function () {
          fd.gtm.updateDataLayer({items: data.data});
        }, 10);
      }
    }
  });

  reorderUpdate.listen();

  // ATC result
  var atcSuccess = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'atcResult'
    },
    callback: {
      value: function (data) {
        data.forEach(function (atcItemInfo) {
          if (atcItemInfo.status === 'SUCCESS' &&
              atcItemInfo.googleAnalyticsData &&
              atcItemInfo.googleAnalyticsData.googleAnalyticsData) {

            fd.gtm.updateDataLayer(atcItemInfo.googleAnalyticsData.googleAnalyticsData);
          }
        });
      }
    }
  });

  atcSuccess.listen();

  var coStepUpdate = function (step, data) {
    var coStepData = {
    };

    if (step === 'address') {
      var selectedAddress = data.addresses.filter(function (address) { return address.selected; })[0];

      coStepData.step = 1;

      if (selectedAddress) {
        coStepData.delivery_type = selectedAddress.service_type;
      }
    } else if (step === 'payment') {
      var selectedPayment = !data.payments ? null : data.payments.filter(function (payment) { return payment.selected; })[0];

      if (!selectedPayment) { return; }

      coStepData.step = 3;
      coStepData.option = selectedPayment.type;
    } else if (step === 'timeslot') {

      coStepData.step = 2;

      // don't set the option field for timeslot
      if (data && data.timePeriod) {
        coStepData.available_timeslot_value = data && data.timePeriod+' '+data.month+'/'+data.dayOfMonth+'/'+data.year || 'unknown';
        coStepData.unavailable_timeslot_present = data.unavailableTimeslotValue ? 'yes' : 'no';
      } else {
        return; // no timeslot selected
      }
    }

    fd.gtm.updateDataLayer({
      coStep: coStepData
    });

    // send event only after user interaction
    if (fd.gtm._coUserInteraction) {
      fd.gtm._coUserInteraction = false;
      fd.gtm.updateDataLayer(null, {
        event: 'checkoutStep'
      });
    }
  };

  // Checkout - user interaction - drawer reset
  var coDrawerClick = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'ec-drawer-reset'
    },
    callback: {
      value: function (data) {
        if (data.active) {
          fd.gtm._coUserInteraction = true;
        }
      }
    }
  });

  coDrawerClick.listen();

  // Checkout - user interaction - drawer cancel
  var coDrawerCancel = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'ec-drawer-cancel'
    },
    callback: {
      value: function (data) {
        if (data.active) {
          var step;

          if (data.active === "address") {
            step = 1;
          } else if (data.active === "timeslot") {
            step = 2;
          } else if (data.active === "payment") {
            step = 3;
          }

          // send 'checkoutStep' event
          fd.gtm.updateDataLayer({
            coStep: {
              step: step
            }
          }, {
            event: 'checkoutStep'
          });
        }
      }
    }
  });

  coDrawerCancel.listen();

  // Checkout - address/payment/timeslot selection
  var coStep = Object.create(fd.common.signalTarget, {
    signal: {
      value: ['address','payment','timeslot']
    },
    callback: {
      value: function (data, signal) {
        coStepUpdate(signal, data);
      }
    }
  });

  coStep.listen();

  // Zip check popup
  var zipCheck = Object.create(fd.common.signalTarget, {
    signal: {
      value: ['zipCheckSuccess','zipCheckClosed']
    },
    callback: {
      value: function (data, signal) {
        var gtmData = {};
        gtmData[signal] = data;
        fd.gtm.updateDataLayer(gtmData);
      }
    }
  });

  zipCheck.listen();

}(FreshDirect));

// load google tag manager only in non-masquerade context
(function(fd) {
  var loadGTM = function (w,d,s,l,i) {
    w[l]=w[l]||[];
    w[l].push({
        'gtm.start':new Date().getTime(),
        event:'gtm.js'
    });

    var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),
        dl=l!=='dataLayer'?'&l='+l:'';

    j.async=true;
    j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl+(GTMAUTH ? '&gtm_auth='+GTMAUTH : '')+(GTMPREVIEW ? '&gtm_preview='+GTMPREVIEW+'&gtm_cookies_win=x' : '');
    f.parentNode.insertBefore(j,f);
  };

  if (GTMID && (!fd || !fd.user || !fd.user.masquerade)) {
    loadGTM(window, document, 'script', 'dataLayer', GTMID);
  }

}(FreshDirect));

// check for login or signup statuses
(function(fd) {
  // TODO remove after success.jsp has been finished
  if (fd.utils.readCookie('hasJustLoggedIn')) {
    fd.utils.eraseCookie('hasJustLoggedIn');
  }

  if (fd.utils.readCookie('hasJustSignedUp')) {
    fd.utils.eraseCookie('hasJustSignedUp');
  }
}(FreshDirect));

// custom analytics events
(function(fd) {
  var $=fd.libs.$;

  // product click
  $(document).on('click', '[data-component="product"] a[href]', function (e) {
    e.preventDefault();
    var target = $(e.target).closest('a').prop('href'),
        productData = fd.gtm.getProductData(e.target),
        goToProduct = function () {
          window.location.assign(target);
        };

    // failsafe
    var goTimeout = setTimeout(goToProduct, 300);

    fd.gtm.updateDataLayer({
      ecommerce: {
        click: {
          actionField: {
            list: productData.list || fd.gtm.getList()
          },
          products: [{
            id: productData.productId,
            name: productData.name,
            price: productData.price,
            brand: productData.brand,
            category: productData.categoryId,
            variant: productData.variant !== 'null' ? productData.variant : "default variant",
            new_product: productData.new_product,
            sku: productData.skuCode,
            in_stock: productData.in_stock,
            position: parseInt(productData.position, 10) || 0
          }]
        }
      }
    }, {
      event: 'productClick',
      callback: function () {
        if (goTimeout) {
          clearTimeout(goTimeout);
          goTimeout = null;
        }
        goToProduct();
      }
    });

  });

  // top nav click
  $(document).on('click', '[data-component="globalnav-menu"] a, .bottom-nav a', function (e) {
    e.preventDefault();
    var target = $(e.target).closest('a').prop('href'),
        title = $(e.target).closest('a').text(),
        goToTarget = function () {
          window.location.assign(target);
        };

    // failsafe
    var goTimeout = setTimeout(goToTarget, 300);

    fd.gtm.updateDataLayer({
      topNavClick: {
        title: title
      }
    }, {
      event: 'top-menu-clicked',
      callback: function () {
        if (goTimeout) {
          clearTimeout(goTimeout);
          goTimeout = null;
        }
        goToTarget();
      }
    });

  });

  // left nav click
  $(document).on('click', '[data-component="menuitem"] a, [data-component="menuitem"] label', function (e) {
    var title = $(e.target).text();

    if (title) {
      fd.gtm.updateDataLayer({
        leftNavClick: {
          title: title
        }
      }, {
        event: 'left-nav-clicked'
      });
    }

  });

  // sorter click
  $(document).on('sorter-change', function () {
    var sortData = fd.browse && fd.browse.sorter && fd.browse.sorter.serialize();

    if (sortData) {
      fd.gtm.updateDataLayer({
        sortClick: sortData
      }, {
        event: 'sort-menu-clicked'
      });
    }
  });
}(FreshDirect));
