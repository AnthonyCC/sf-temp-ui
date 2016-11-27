package com.freshdirect.customer;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class ErpProductFamilyModel implements Serializable {
	private int version;
	private String action;
	private String familyId;
	private String productId;

	private String longDesc;
	private String shortDesc;
	private String materialNumber;
	private Set<String> matList;
	private List<String> skuList;
	
	private String grpId;
	private String deletegroup;
	private String msgtype;
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getMsgtext() {
		return msgtext;
	}
	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}
	private String msgtext;
	
	public String getDeletegroup() {
		return deletegroup;
	}
	public void setDeletegroup(String deletegroup) {
		this.deletegroup = deletegroup;
	}
	public String getGrpId() {
		return grpId;
	}
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getFamilyId() {
		return familyId;
	}
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public Set<String> getMatList() {
		return matList;
	}
	public void setMatList(Set<String> matList) {
		this.matList = matList;
	}
	public List<String> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}
	
	public String getMaterialNumber() {
		return materialNumber;
	}
	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	
	

}
