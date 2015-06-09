package com.freshdirect.dataloader.sap.jco.server.param;

public class ProductFamilyParameter extends AbstractMaterialParameter 
{
	private String groupId;
	private String materialNumber;
	private String action; 
	private String deletegroup;
	private String msgtype;
	private String msgtext;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMaterialNumber() {
		return materialNumber;
	}
	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDeletegroup() {
		return deletegroup;
	}
	public void setDeletegroup(String deletegroup) {
		this.deletegroup = deletegroup;
	}
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
	

}