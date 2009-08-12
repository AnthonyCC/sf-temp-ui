package com.freshdirect.customer;

public class ErpCannedText {
	private String id;
	private String name;
	private EnumCannedTextCategory category;
	private String text;

	public ErpCannedText(String id, String name,
			EnumCannedTextCategory category, String text) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EnumCannedTextCategory getCategory() {
		return category;
	}

	public void setCategory(EnumCannedTextCategory category) {
		this.category = category;
	}

	public void setCategory(String category) {
		EnumCannedTextCategory cat = EnumCannedTextCategory.getEnum(category);
		if (cat == null)
			throw new IllegalArgumentException("no such canned text category: " + category);
		this.category = cat;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
