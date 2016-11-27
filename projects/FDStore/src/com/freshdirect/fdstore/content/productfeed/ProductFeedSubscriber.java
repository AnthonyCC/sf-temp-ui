package com.freshdirect.fdstore.content.productfeed;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.fdstore.EnumEStoreId;

public class ProductFeedSubscriber implements Serializable {
	
	private String code;
	private String description;
	private ProductFeedSubscriberType type;
	private String url;
	private String userid;
	private String password;
	private String defaultUploadPath;
    private List<EnumEStoreId> stores;
	
	public ProductFeedSubscriber(String code, String description,
			ProductFeedSubscriberType type, String url, String userid,
            String password, String defaultUploadPath, List<EnumEStoreId> stores) {
		super();
		this.code = code;
		this.description = description;
		this.type = type;
		this.url = url;
		this.userid = userid;
		this.password = password;
		this.defaultUploadPath = defaultUploadPath;
        this.stores = stores;
	}
	
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	public ProductFeedSubscriberType getType() {
		return type;
	}
	public String getUrl() {
		return url;
	}
	public String getUserid() {
		return userid;
	}
	public String getPassword() {
		return password;
	}

	public String getDefaultUploadPath() {
		return defaultUploadPath;
	}

    public List<EnumEStoreId> getStores() {
        return stores;
    }

	@Override
	public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ProductFeedSubscriber [code=" + code + ", description=" + description + ", type=" + type + ", url=" + url + ", userid=" + userid + ", password="
                + password + ", defaultUploadPath=" + defaultUploadPath + ", store=");
        if (stores != null) {
            for (EnumEStoreId storeId : stores) {
                stringBuilder.append("{").append(storeId).append("} ");
            }
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
	}
}
