package com.freshdirect.webapp.ajax.product.data;

import java.util.ArrayList;
import java.util.List;
public class ProductPotatoData {

    private ProductData productData;
    private ProductExtraData productExtraData;
    private List<ProductData> adProducts = new ArrayList<ProductData>();
    private String AdPdpPageBeacon;

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public ProductExtraData getProductExtraData() {
        return productExtraData;
    }

    public void setProductExtraData(ProductExtraData productExtraData) {
        this.productExtraData = productExtraData;
    }

    public List<ProductData> getAdProducts() {
		return adProducts;
	}

	public void setAdProducts(List<ProductData> adProducts) {
		this.adProducts = adProducts;
	}

	public String getAdPdpPageBeacon() {
		return AdPdpPageBeacon;
	}

	public void setAdPdpPageBeacon(String adPdpPageBeacon) {
		AdPdpPageBeacon = adPdpPageBeacon;
	}
}
