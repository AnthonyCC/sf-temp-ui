<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
  <title>viewcart popup test page</title>
  <jwr:style src="/oldglobal.css"></jwr:style>
  <jwr:style src="/global.css"></jwr:style>
  <jwr:style src="/quickshop.css"></jwr:style>
  <jwr:style src="/viewcart.css"></jwr:style>
</head>

<body>
  <h1>customizePopup body</h1>

  <div class="customize-popup debug-line">
    <h1 class="header">Customize Your Items</h1>
    <p class="description">We cut meat, cheese and seafood to order. Tell us how you want it <span class="strong">(*required)</span></p>
    <div class="body">      
      <ol class="shopfromlists">
        <li class="itemlist-item">
          <div class="itemlist-item-productimage_wrapper">
            <div class="itemlist-item-burst_wrapper">
              <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt">
                <img src="/media/images/product/seafood/fish_fillets/fflt_flndr_flt_c.jpg" alt="" class="itemlist-item-productimage">
              </a>
              <img class="burst" src="/media_stat/images/bursts/brst_sm_fave.png" alt="">
            </div>
          </div>
          <div class="itemlist-item-header-buttons">
            <button class="deletefromlist">delete from list</button>
            <!--  button class="addtolist">add to list</button -->
          </div>
          <div class="itemlist-item-header">    
            <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt" class="itemlist-item-header-name">Local Wild Flounder Fillet</a> 
            <b class="expertrating smallrating rating-8">8</b>
            <img src="/media/brands/fd_seafood_popup_all/fish_yellow.gif" class="sustrating">
            <br>
            <i class="itemlist-item-header-configuration">7-9 OZ</i>
            
            <div class="itemlist-item-header-changecustomization">
              <!--  button data-component="customizeButton" data-hasapply="true">
                <span>Change Customization</span>
              </button -->
            </div>
            <div class="availability"></div>
          </div>
          <div class="itemlist-item-controls">
            <div class="itemlist-item-controls-subtotal">
              <input type="hidden" value="G02" data-productdata-name="salesUnit" data-component="productData">
              <div class="qtyinput" data-component="quantitybox" data-min="1" data-max="99" data-step="1" data-mayempty="true">
                <div style:"display:none;">Decrease quantity</div>
                <span class="quantity_minus" data-component="quantitybox.dec">-</span>
                <span class="qtywrapper">
                <input class="qty" type="text" value="1" maxlength="4" data-component="quantitybox.value" data-productdata-name="quantity" data-mayempty="true">
                </span>
                <div style:"display:none;">Increase quantity</div>
                <span class="quantity_plus" data-component="quantitybox.inc">+</span>
              </div>
            </div>
            <div class="itemlist-item-price">    $15.99/lb</div>
            <div class="itemlist-item-total" data-component="subtotal">| Subtotal: <b>$8.00</b> <b>.5lb</b> estimated</div>
           </div>
           <hr class="itemlist-separator" />
        </li>    
       <li class="itemlist-item">
        <div class="itemlist-item-productimage_wrapper">
          <div class="itemlist-item-burst_wrapper">
            <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt">
              <img src="/media/images/product/seafood/fish_fillets/fflt_flndr_flt_c.jpg" alt="" class="itemlist-item-productimage">
            </a>
            <img class="burst" src="/media_stat/images/bursts/brst_sm_fave.png" alt="">
          </div>
        </div>
        <div class="itemlist-item-header-buttons">
          <button class="deletefromlist">delete from list</button>
          <!-- button   class="addtolist">add to list</button -->
        </div>
        <div class="itemlist-item-header">    
          <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt" class="itemlist-item-header-name">Cheez-It Reduced Fat Crackers</a> 
          <b class="expertrating smallrating rating-8">8</b>
          <img src="/media/brands/fd_seafood_popup_all/fish_yellow.gif" class="sustrating">
          <br>
          <i class="itemlist-item-header-configuration">7-9 OZ</i>
          
          <!--  div class="itemlist-item-header-changecustomization">
            <button data-component="customizeButton" data-hasapply="true">
              <span>Change Customization</span>
            </button>
          </div -->
          <div class="availability"></div>
        </div>
        <div class="itemlist-item-customization">
            <p class="cust-title">Custom Options:</p>
            <select class="salesunit" 
                    data-component="salesunit" 
                    data-productdata-name="salesUnit" data-atc-required="true" data-atl-required="true">
                    <option value="">Thickness</option>
                    <option value="E02" selected="selected">1" THICK</option>
                    <option value="E03">1.5" THICK</option>
                    <option value="E04">2" THICK</option>
                    <option value="E05">2.5" THICK</option>
                    <option value="E06">3" THICK</option>
             </select>
          
             <button class="cssbutton grey css-help-button" 
             onclick="pop('/shared/popup.jsp?catId=mt_b_lcl_1&amp;prodId=bstk_flet_dfat_local&amp;attrib=SALES_UNIT_DESCRIPTION&amp;tmpl=large',335,375)">?</button>
          
            <select name="C_MT_BF_PAK" data-component="productDataConfiguration" data-productdata-name="C_MT_BF_PAK" data-atl-required="true" data-atc-required="true" class="missing-data">
              <option value="">Packaging</option>
              <option value="ST">Standard Pack - no charge</option>
              <option value="VP">Vacuum Pack - 0.50$ - no charge</option>
            </select>
            <button class="cssbutton grey css-help-button" onclick="pop('/shared/fd_def_popup.jsp?charName=C_MT_BF_PAK&amp;tmpl=small_pop&amp;title=Packaging',335,375)">?</button>
            
            <label>
              <input type="checkbox" name="C_MT_BF_TW4" data-unchecked-value="N" value="TO" data-component="productDataConfiguration" data-productdata-name="C_MT_BF_TW4">Tie This for Me
            </label>
            <button class="cssbutton grey css-help-button" onclick="pop('/shared/fd_def_popup.jsp?charName=C_MT_BF_TW4&amp;tmpl=small_pop&amp;title=Tie This for Me',335,375)">?</button>
        </div>
        <div class="itemlist-item-controls">
          <div class="itemlist-item-controls-subtotal">
            <input type="hidden" value="G02" data-productdata-name="salesUnit" data-component="productData">
            <div class="qtyinput" data-component="quantitybox" data-min="1" data-max="99" data-step="1" data-mayempty="true">
              <div style:"display:none;">Decrease quantity</div>
              <span class="quantity_minus" data-component="quantitybox.dec">-</span>
              <span class="qtywrapper">
              <input class="qty" type="text" value="1" maxlength="4" data-component="quantitybox.value" data-productdata-name="quantity" data-mayempty="true">
              </span>
              <div style:"display:none;">Increase quantity</div>
              <span class="quantity_plus" data-component="quantitybox.inc">+</span>
            </div>
            <!--  div class="subtotal hasPrice" data-component="subtotal">subtotal: <b>$8.00</b></div -->
          </div>
          <div class="itemlist-item-price">    $15.99/lb</div>
          <div class="itemlist-item-total" data-component="subtotal">| Subtotal: <b>$18.00</b> <b>.5lb</b> estimated</div>
         </div>
      </li>    
      </ol>
    </div>
    <div class="footer">
      <button class="cssbutton orange large fixwidth">Done</button>
    </div>
  </div> 

  <h1>customizePopup delete state</h1>
  <div class="customize-popup debug-line">
    <h1 class="warning">
      <span class="sign"></span>
      <div class="warning-content">Without customization, these items will be deleted from your cart.</div>
    </h1>
    <div class="body">      
      <ol class="shopfromlists">
        <li class="itemlist-item">
          <div class="itemlist-item-productimage_wrapper">
            <div class="itemlist-item-burst_wrapper">
              <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt">
                <img src="/media/images/product/seafood/fish_fillets/fflt_flndr_flt_c.jpg" alt="" class="itemlist-item-productimage">
              </a>
              <img class="burst" src="/media_stat/images/bursts/brst_sm_fave.png" alt="">
            </div>
          </div>
          <div class="itemlist-item-header-buttons">
            <button class="deletefromlist">delete from list</button>
            <!--  button class="addtolist">add to list</button -->
          </div>
          <div class="itemlist-item-header">    
            <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt" class="itemlist-item-header-name">Local Wild Flounder Fillet</a> 
            <b class="expertrating smallrating rating-8">8</b>
            <img src="/media/brands/fd_seafood_popup_all/fish_yellow.gif" class="sustrating">
            <br>
            <i class="itemlist-item-header-configuration">7-9 OZ</i>
            
            <div class="itemlist-item-header-changecustomization">
              <!--  button data-component="customizeButton" data-hasapply="true">
                <span>Change Customization</span>
              </button -->
            </div>
            <div class="availability"></div>
          </div>
          <div class="itemlist-item-controls">
            <div class="itemlist-item-controls-subtotal">
              <input type="hidden" value="G02" data-productdata-name="salesUnit" data-component="productData">
              <div class="qtyinput" data-component="quantitybox" data-min="1" data-max="99" data-step="1" data-mayempty="true">
              <div style:"display:none;">Decrease quantity</div>	
                <span class="quantity_minus" data-component="quantitybox.dec">-</span>
                <span class="qtywrapper">
                <input class="qty" type="text" value="1" maxlength="4" data-component="quantitybox.value" data-productdata-name="quantity" data-mayempty="true">
                </span>
                <div style:"display:none;">Increase quantity</div>
                <span class="quantity_plus" data-component="quantitybox.inc">+</span>
              </div>
            </div>
            <div class="itemlist-item-price">    $15.99/lb</div>
            <div class="itemlist-item-total" data-component="subtotal">| Subtotal: <b>$8.00</b> <b>.5lb</b> estimated</div>
           </div>
           <hr class="itemlist-separator" />
        </li>    
       <li class="itemlist-item">
        <div class="itemlist-item-productimage_wrapper">
          <div class="itemlist-item-burst_wrapper">
            <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt">
              <img src="/media/images/product/seafood/fish_fillets/fflt_flndr_flt_c.jpg" alt="" class="itemlist-item-productimage">
            </a>
            <img class="burst" src="/media_stat/images/bursts/brst_sm_fave.png" alt="">
          </div>
        </div>
        <div class="itemlist-item-header-buttons">
          <button class="deletefromlist">delete from list</button>
          <!-- button   class="addtolist">add to list</button -->
        </div>
        <div class="itemlist-item-header">    
          <a href="/pdp.jsp?productId=fflt_gsmans_flound&amp;catId=fflt" class="itemlist-item-header-name">Cheez-It Reduced Fat Crackers</a> 
          <b class="expertrating smallrating rating-8">8</b>
          <img src="/media/brands/fd_seafood_popup_all/fish_yellow.gif" class="sustrating">
          <br>
          <i class="itemlist-item-header-configuration">7-9 OZ</i>
          
          <!--  div class="itemlist-item-header-changecustomization">
            <button data-component="customizeButton" data-hasapply="true">
              <span>Change Customization</span>
            </button>
          </div -->
          <div class="availability"></div>
        </div>
        <div class="itemlist-item-customization">
            <p class="cust-title">Custom Options:</p>
            <select class="salesunit" 
                    data-component="salesunit" 
                    data-productdata-name="salesUnit" data-atc-required="true" data-atl-required="true">
                    <option value="">Thickness</option>
                    <option value="E02" selected="selected">1" THICK</option>
                    <option value="E03">1.5" THICK</option>
                    <option value="E04">2" THICK</option>
                    <option value="E05">2.5" THICK</option>
                    <option value="E06">3" THICK</option>
             </select>
          
             <button class="cssbutton grey css-help-button" 
             onclick="pop('/shared/popup.jsp?catId=mt_b_lcl_1&amp;prodId=bstk_flet_dfat_local&amp;attrib=SALES_UNIT_DESCRIPTION&amp;tmpl=large',335,375)">?</button>
          
            <select name="C_MT_BF_PAK" data-component="productDataConfiguration" data-productdata-name="C_MT_BF_PAK" data-atl-required="true" data-atc-required="true" class="missing-data">
              <option value="">Packaging</option>
              <option value="ST">Standard Pack - no charge</option>
              <option value="VP">Vacuum Pack - 0.50$ - no charge</option>
            </select>
            <button class="cssbutton grey css-help-button" onclick="pop('/shared/fd_def_popup.jsp?charName=C_MT_BF_PAK&amp;tmpl=small_pop&amp;title=Packaging',335,375)">?</button>
            
            <label>
              <input type="checkbox" name="C_MT_BF_TW4" data-unchecked-value="N" value="TO" data-component="productDataConfiguration" data-productdata-name="C_MT_BF_TW4">Tie This for Me
            </label>
            <button class="cssbutton grey css-help-button" onclick="pop('/shared/fd_def_popup.jsp?charName=C_MT_BF_TW4&amp;tmpl=small_pop&amp;title=Tie This for Me',335,375)">?</button>
        </div>
        <div class="itemlist-item-controls">
          <div class="itemlist-item-controls-subtotal">
            <input type="hidden" value="G02" data-productdata-name="salesUnit" data-component="productData">
            <div class="qtyinput" data-component="quantitybox" data-min="1" data-max="99" data-step="1" data-mayempty="true">
            <div style:"display:none;">Decrease quantity</div>
              <span class="quantity_minus" data-component="quantitybox.dec">-</span>
              <span class="qtywrapper">
              <input class="qty" type="text" value="1" maxlength="4" data-component="quantitybox.value" data-productdata-name="quantity" data-mayempty="true">
              </span>
              <div style:"display:none;">Increase quantity</div>
              <span class="quantity_plus" data-component="quantitybox.inc">+</span>
            </div>
            <!--  div class="subtotal hasPrice" data-component="subtotal">subtotal: <b>$8.00</b></div -->
          </div>
          <div class="itemlist-item-price">    $15.99/lb</div>
          <div class="itemlist-item-total" data-component="subtotal">| Subtotal: <b>$18.00</b> <b>.5lb</b> estimated</div>
         </div>
      </li>    
      </ol>
    </div>
    <div class="footer">
      <button class="cssbutton red large">Delete Items & Close</button>
      <button class="cssbutton black large">Cancel</button>
    </div>
  </div> 

</body>

</html>
