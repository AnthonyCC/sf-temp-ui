<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<html lang="en-US" xml:lang="en-US">
<head>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/oldglobal.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <jwr:style src="/expressco.css" media="all" />
  <jwr:script src="/fdlibs.js"  useRandomParam="false" />
  <style type="text/css">
  	body{
  		padding:20px;
  	}
  	body:after{
  		content:"";
  		height:100px;
  		display:block;
  	}
  	h2{
  		margin:20px 0 10px 0;
  	}
  	h3{
  		font-size:12px;
  	}
  </style>
</head>
<body class="view_cart">
  <h1>Product Sample Carousel @XC page</h1>
  <h2>With 1 item</h2>
  <h3>[product-count="1"], .productSamplingItem.item-count1</h3>
    
  <div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
    <div class="product-sample-carousel" product-count="1">
      <h2 class="product-sample-header" fd-toggle-trigger="product-sample-carousel">
      Congrats, you've qualified for a <span class="product-sample-free">FREE</span>
      Sample! Choose 2.</h2>

      <div data-component="carousel" data-cmeventsource="view_cart" data-apiendpoint=
      "/api/qs/ymal">
        <div data-component="carousel-mask" class="transactional light-carousel">
          <ul data-component="carousel-list" data-carousel-page="0">
            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count1"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "gro_pepsi_next2l" data-cat-id="gro_bever_soda_cola">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/grocery_30/gro_pepsi_next2l_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" />
                <img class="burst free" src="/media_stat/images/bursts/brst_sm_free.png"
                alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l"
                class="portrait-item-header-name"><b>Pepsi</b></a>

                <div class="product-name-no-brand">
                  Next
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;1.89&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" /><input type=
                  "hidden" data-component="productData" data-productdata-name="productId"
                  name="productId" value="gro_pepsi_next2l" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_bever_soda_cola" /><input type="hidden"
                  data-component="productData" data-productdata-name="skuCode" name=
                  "skuCode" value="GRO0078631" />
                </div>
              </div>

              <div class="atc-info" id=
              "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>
          </ul>
        </div>

        <div data-component="carousel-prev" data-carousel-nav="prev">
          previous
        </div>

        <div data-component="carousel-next" data-carousel-nav="next">
          next
        </div>
      </div>

      <div class="product-sample-carousel-bottom"></div>
    </div>
</div>
<!-- /with 1 item -->
  
  
  
  <h2>With 2 items</h2>
  <h3>[product-count="2"], .productSamplingItem.item-count2</h3>
  <div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
    <div class="product-sample-carousel" product-count="2">
      <h2 class="product-sample-header" fd-toggle-trigger="product-sample-carousel">
      Congrats, you've qualified for a <span class="product-sample-free">FREE</span>
      Sample! Choose 2.</h2>

      <div data-component="carousel" data-cmeventsource="view_cart" data-apiendpoint=
      "/api/qs/ymal">
        <div data-component="carousel-mask" class="transactional light-carousel">
          <ul data-component="carousel-list" data-carousel-page="0">
            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count2"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "hba_duracell_aa4pk" data-cat-id="gro_batt">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/hba_12/hba_duracell_aa4pk_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /> <img class=
                "burst free" src="/media_stat/images/bursts/brst_sm_free.png" alt=
                "free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" class=
                "portrait-item-header-name"><b>Duracell</b></a>

                <div class="product-name-no-brand">
                  AA Battery
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;3.79&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_hba_duracell_aa4pk_HBA0066838_gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="productId" name=
                  "productId" value="hba_duracell_aa4pk" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_batt" /><input type="hidden" data-component=
                  "productData" data-productdata-name="skuCode" name="skuCode" value=
                  "HBA0066838" />
                </div>
              </div>

              <div class="atc-info" id="atc_hba_duracell_aa4pk_HBA0066838_gro_batt"
              data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>

            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count2"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "gro_pepsi_next2l" data-cat-id="gro_bever_soda_cola">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/grocery_30/gro_pepsi_next2l_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" />
                <img class="burst free" src="/media_stat/images/bursts/brst_sm_free.png"
                alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l"
                class="portrait-item-header-name"><b>Pepsi</b></a>

                <div class="product-name-no-brand">
                  Next
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;1.89&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" /><input type=
                  "hidden" data-component="productData" data-productdata-name="productId"
                  name="productId" value="gro_pepsi_next2l" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_bever_soda_cola" /><input type="hidden"
                  data-component="productData" data-productdata-name="skuCode" name=
                  "skuCode" value="GRO0078631" />
                </div>
              </div>

              <div class="atc-info" id=
              "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>
          </ul>
        </div>

        <div data-component="carousel-prev" data-carousel-nav="prev">
          previous
        </div>

        <div data-component="carousel-next" data-carousel-nav="next">
          next
        </div>
      </div>

      <div class="product-sample-carousel-bottom"></div>
    </div>
</div>
<!-- /with 2 items -->
  
  <h2>With 3 items</h2>
  <h3>[product-count="3"], .productSamplingItem.item-count3</h3>
  <div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
    <div class="product-sample-carousel" product-count="3">
      <h2 class="product-sample-header" fd-toggle-trigger="product-sample-carousel">
      Congrats, you've qualified for a <span class="product-sample-free">FREE</span>
      Sample! Choose 2.</h2>

      <div data-component="carousel" data-cmeventsource="view_cart" data-apiendpoint=
      "/api/qs/ymal">
        <div data-component="carousel-mask" class="transactional light-carousel">
          <ul data-component="carousel-list" data-carousel-page="0">
            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count3"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "hrb_bsl" data-cat-id="hrb">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/veg_1/hrb_bsl_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" /> <img class="burst free"
                src="/media_stat/images/bursts/brst_sm_free.png" alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" class=
                "portrait-item-header-name">Basil</a>

                <div class="rating">
                  <b class="expertrating smallrating rating-4">4</b>
                </div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;2.99&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;2&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } , { &quot;price&quot;: &quot;2.5&quot;, &quot;lowerBound&quot;: &quot;2&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_hrb_bsl_VEG0011085_hrb" /><input type="hidden" data-component=
                  "productData" data-productdata-name="productId" name="productId" value=
                  "hrb_bsl" /><input type="hidden" data-productdata-name="productPageUrl"
                  name="productPageUrl" value=
                  "/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="hrb" /><input type="hidden" data-component=
                  "productData" data-productdata-name="skuCode" name="skuCode" value=
                  "VEG0011085" />
                </div>
              </div>

              <div class="atc-info" id="atc_hrb_bsl_VEG0011085_hrb" data-amount="0">
                <div class="incart-info" data-amount="0"></div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>

            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count3"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "hba_duracell_aa4pk" data-cat-id="gro_batt">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/hba_12/hba_duracell_aa4pk_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /> <img class=
                "burst free" src="/media_stat/images/bursts/brst_sm_free.png" alt=
                "free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" class=
                "portrait-item-header-name"><b>Duracell</b></a>

                <div class="product-name-no-brand">
                  AA Battery
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;3.79&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_hba_duracell_aa4pk_HBA0066838_gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="productId" name=
                  "productId" value="hba_duracell_aa4pk" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_batt" /><input type="hidden" data-component=
                  "productData" data-productdata-name="skuCode" name="skuCode" value=
                  "HBA0066838" />
                </div>
              </div>

              <div class="atc-info" id="atc_hba_duracell_aa4pk_HBA0066838_gro_batt"
              data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>

            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem item-count3"
            data-component="product" data-transactional-trigger="true" data-product-id=
            "gro_pepsi_next2l" data-cat-id="gro_bever_soda_cola">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/grocery_30/gro_pepsi_next2l_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" />
                <img class="burst free" src="/media_stat/images/bursts/brst_sm_free.png"
                alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l"
                class="portrait-item-header-name"><b>Pepsi</b></a>

                <div class="product-name-no-brand">
                  Next
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;1.89&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" /><input type=
                  "hidden" data-component="productData" data-productdata-name="productId"
                  name="productId" value="gro_pepsi_next2l" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_bever_soda_cola" /><input type="hidden"
                  data-component="productData" data-productdata-name="skuCode" name=
                  "skuCode" value="GRO0078631" />
                </div>
              </div>

              <div class="atc-info" id=
              "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>
          </ul>
        </div>

        <div data-component="carousel-prev" data-carousel-nav="prev">
          previous
        </div>

        <div data-component="carousel-next" data-carousel-nav="next">
          next
        </div>
      </div>

      <div class="product-sample-carousel-bottom"></div>
    </div>
</div>
  <h2>With 4 items</h2>
  <h3>[product-count="4"], .productSamplingItem</h3>
  <div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
    <div class="product-sample-carousel" product-count="4">
      <h2 class="product-sample-header" fd-toggle-trigger="product-sample-carousel">
      Congrats, you've qualified for a <span class="product-sample-free">FREE</span>
      Sample! Choose 2.</h2>

      <div data-component="carousel" data-cmeventsource="view_cart" data-apiendpoint=
      "/api/qs/ymal">
        <div data-component="carousel-mask" class="transactional light-carousel">
          <ul data-component="carousel-list" data-carousel-page="0">
            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem "
            data-component="product" data-transactional-trigger="true" data-product-id=
            "hrb_bsl" data-cat-id="hrb">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/veg_1/hrb_bsl_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" /> <img class="burst free"
                src="/media_stat/images/bursts/brst_sm_free.png" alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" class=
                "portrait-item-header-name">Basil</a>

                <div class="rating">
                  <b class="expertrating smallrating rating-4">4</b>
                </div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;2.99&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;2&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } , { &quot;price&quot;: &quot;2.5&quot;, &quot;lowerBound&quot;: &quot;2&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_hrb_bsl_VEG0011085_hrb" /><input type="hidden" data-component=
                  "productData" data-productdata-name="productId" name="productId" value=
                  "hrb_bsl" /><input type="hidden" data-productdata-name="productPageUrl"
                  name="productPageUrl" value=
                  "/pdp.jsp?productId=hrb_bsl&amp;catId=hrb" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="hrb" /><input type="hidden" data-component=
                  "productData" data-productdata-name="skuCode" name="skuCode" value=
                  "VEG0011085" />
                </div>
              </div>

              <div class="atc-info" id="atc_hrb_bsl_VEG0011085_hrb" data-amount="0">
                <div class="incart-info" data-amount="0"></div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>

            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem "
            data-component="product" data-transactional-trigger="true" data-product-id=
            "hba_duracell_aa4pk" data-cat-id="gro_batt">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/hba_12/hba_duracell_aa4pk_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /> <img class=
                "burst free" src="/media_stat/images/bursts/brst_sm_free.png" alt=
                "free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" class=
                "portrait-item-header-name"><b>Duracell</b></a>

                <div class="product-name-no-brand">
                  AA Battery
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;3.79&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_hba_duracell_aa4pk_HBA0066838_gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="productId" name=
                  "productId" value="hba_duracell_aa4pk" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=hba_duracell_aa4pk&amp;catId=gro_batt" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_batt" /><input type="hidden" data-component=
                  "productData" data-productdata-name="skuCode" name="skuCode" value=
                  "HBA0066838" />
                </div>
              </div>

              <div class="atc-info" id="atc_hba_duracell_aa4pk_HBA0066838_gro_batt"
              data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>

            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem "
            data-component="product" data-transactional-trigger="true" data-product-id=
            "gro_pepsi_next2l" data-cat-id="gro_bever_soda_cola">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/grocery_30/gro_pepsi_next2l_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" />
                <img class="burst free" src="/media_stat/images/bursts/brst_sm_free.png"
                alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l"
                class="portrait-item-header-name"><b>Pepsi</b></a>

                <div class="product-name-no-brand">
                  Next
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;1.89&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" /><input type=
                  "hidden" data-component="productData" data-productdata-name="productId"
                  name="productId" value="gro_pepsi_next2l" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_bever_soda_cola" /><input type="hidden"
                  data-component="productData" data-productdata-name="skuCode" name=
                  "skuCode" value="GRO0078631" />
                </div>
              </div>

              <div class="atc-info" id=
              "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>
            
            <li class=
            "portrait-item carouselTransactionalItem productSamplingItem "
            data-component="product" data-transactional-trigger="true" data-product-id=
            "gro_pepsi_next2l" data-cat-id="gro_bever_soda_cola">
              <div class=
              "portrait-item-productimage_wrapper carouselTransactionalItem productSamplingItem ">
              <div class="portrait-item-burst_wrapper"><img src=
              "/media/images/product/grocery_30/gro_pepsi_next2l_p.jpg?lastModify=null?publishId=2119"
                alt="" class="portrait-item-productimage" data-product-url=
                "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" />
                <img class="burst free" src="/media_stat/images/bursts/brst_sm_free.png"
                alt="free" /></div>
              </div>

              <div class="portrait-item-header">
                <a href="/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l"
                class="portrait-item-header-name"><b>Pepsi</b></a>

                <div class="product-name-no-brand">
                  Next
                </div>

                <div class="rating"></div>
              </div>

              <div class="portrait-item-price product-sample-free">
                FREE
              </div>

              <div class="portrait-item-controls" data-component="product-controls">
                <div class="portrait-item-controls-content">
                  <input type="hidden" name="salesUnit" value="EA" data-component=
                  "productData" data-productdata-name="salesUnit" /><input type="hidden"
                  value="1" data-productdata-name="quantity" data-component=
                  "productData" />

                  <div class="product-sample-qty-one">
                    Quantity: 1
                  </div>

                  <div class="portrait-item-addtocart" data-cmeventsource="view_cart">
                    <div class="subtotal" data-component="subtotal" data-template=
                    "common.subtotalTransactionalInner" data-prices=
                    "[ { &quot;price&quot;: &quot;1.89&quot;, &quot;lowerBound&quot;: &quot;0&quot;, &quot;upperBound&quot;: &quot;Infinity&quot;, &quot;pricingUnit&quot;: &quot;EA&quot; } ]"
                    data-suratio=
                    "[ { &quot;unit&quot;: &quot;EA&quot;, &quot;ratio&quot;: &quot;1&quot;, &quot;salesUnit&quot;: &quot;EA&quot; } ]"
                    data-cvprices="[ ]" data-qinc="1" data-qmin="1" data-qmax="1">
                    </div><span class="addtocart cssbutton orange small" data-component=
                    "ATCButton"><span class="message" alt="">Add to
                    Cart</span><ins class="iehelper">Added</ins></span>
                  </div><input type="hidden" data-component="productData"
                  data-productdata-name="atcItemId" name="atcItemId" value=
                  "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" /><input type=
                  "hidden" data-component="productData" data-productdata-name="productId"
                  name="productId" value="gro_pepsi_next2l" /><input type="hidden"
                  data-productdata-name="productPageUrl" name="productPageUrl" value=
                  "/pdp.jsp?productId=gro_pepsi_next2l&amp;catId=gro_bever_soda_2l" /><input type="hidden"
                  data-component="productData" data-productdata-name="categoryId" name=
                  "categoryId" value="gro_bever_soda_cola" /><input type="hidden"
                  data-component="productData" data-productdata-name="skuCode" name=
                  "skuCode" value="GRO0078631" />
                </div>
              </div>

              <div class="atc-info" id=
              "atc_gro_pepsi_next2l_GRO0078631_gro_bever_soda_cola" data-amount="1">
                <div class="incart-info" data-amount="1">
                  1 in cart
                </div>

                <div class="incart-message"></div>
              </div><input type="hidden" value="EA" data-productdata-name="salesUnit"
              data-component="productData" data-salesunit-original="EA" />
            </li>
          </ul>
        </div>

        <div data-component="carousel-prev" data-carousel-nav="prev">
          previous
        </div>

        <div data-component="carousel-next" data-carousel-nav="next">
          next
        </div>
      </div>

      <div class="product-sample-carousel-bottom"></div>
    </div>
</div>

<soy:import packageName="common"/>
<jwr:script src="/fdmodules.js"  useRandomParam="false" />
<jwr:script src="/fdcomponents.js"  useRandomParam="false" />

</body>
