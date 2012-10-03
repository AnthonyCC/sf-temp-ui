package com.freshdirect.content.nutrition;

public enum DrugConstantsEnum {
	
	FACTS("Drug Facts", DrugSectionTypeEnum.INGREDIENT, "section", 4, false, 1, null),
	S1("Separator", null, "separator", 0, false, 1, FACTS),
	ACTIVE_INGREDIENT("Active ingredient",null, "item", 1, false, 2, FACTS),
	PURPOSE("Purpose",null, "item", 1, false, 2, FACTS),
	
	USES("Uses ", DrugSectionTypeEnum.FREE_TEXT, "section", 0, false, 2, null),
	USES_TEXT("",null, "item", 0, false, 1, USES),
	USES_TEXT2("",null, "item", 0, true, 2, USES),
	
	WARNINGS("Warnings", DrugSectionTypeEnum.FREE_TEXT, "section", 0, false, 3, null),
	S2("Separator", null, "separator", 0, false, 1, WARNINGS),
	ASK_A_DOCTOR("Ask a doctor or pharmacist before use if you are ",null, "item", 1, false, 2, WARNINGS),
	ASK_A_DOCTOR2("",null, "item", 0, true, 3, WARNINGS),
	S3("Separator", null, "separator", 0, false, 4, WARNINGS),
	WHEN_USING("When using this product",null, "item", 1, false, 5, WARNINGS),
	WHEN_USING2("",null, "item", 0, true, 6, WARNINGS),
	S4("Separator", null, "separator", 0, false, 7, WARNINGS),
	IF_PREGNANT("If pregnant or breast feeding, ",null, "item", 1, false, 8, WARNINGS),
	IF_PREGNANT2("",null, "item", 0, false, 9, WARNINGS),
	KEEP_OUT("Keep out of reach of children.",null, "item", 1, true, 10, WARNINGS),
	
	DIRECTIONS("Directions", DrugSectionTypeEnum.TABLE, "section", 0, false, 4, null),
	ADULTS_AND_CHILDREN("adults and children 12 years and over",null, "item", 0, false, 1, DIRECTIONS),
	CHILDREN("children 6 years to under 12 years",null, "item", 0, false, 2, DIRECTIONS),
	CHILDREN_UNDER_6("children under 6 years",null, "item", 0, false, 3, DIRECTIONS),
	
	OTHER("Other information ",DrugSectionTypeEnum.FREE_TEXT, "section", 1, false, 5, null),
	INACTIVE("Inactive ingredients ", DrugSectionTypeEnum.FREE_TEXT, "section", 0, false, 6, null);
	
	
	private String text;
	private DrugSectionTypeEnum layoutType;
	private String itemType;
	private int importance;
	private boolean newline;
	private int position;
	private DrugConstantsEnum parent;
	
	private DrugConstantsEnum(String text, DrugSectionTypeEnum layoutType, String itemType, int importance, boolean newline, int position, DrugConstantsEnum parent){
		this.text = text;
		this.layoutType = layoutType;
		this.itemType = itemType;
		this.newline= newline;
		this.importance = importance;
		this.position = position;
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public DrugSectionTypeEnum getType() {
		return layoutType;
	}

	public String getItemType() {
		return itemType;
	}

	public int getImportance() {
		return importance;
	}

	public int getPosition() {
		return position;
	}

	public DrugConstantsEnum getParent() {
		return parent;
	}

	public boolean getNewline() {
		return newline;
	}
	
}
