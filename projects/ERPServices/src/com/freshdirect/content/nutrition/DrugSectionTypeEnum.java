package com.freshdirect.content.nutrition;


public enum DrugSectionTypeEnum {
	
	INGREDIENT(1,"ingredient"),
	TABLE(2,"table"),
	FREE_TEXT(3,"freetext");
	
	private int id;
	private String name;
	
	private DrugSectionTypeEnum(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static DrugSectionTypeEnum getTypeById(int id){
		for(DrugSectionTypeEnum e : DrugSectionTypeEnum.values()){
			if(e.getId() == id){
				return e;
			}
		}
		throw new IllegalArgumentException("No enum found with id: " + id);
	}
	
	public static DrugSectionTypeEnum getTypeByName(String name){
		for(DrugSectionTypeEnum e : DrugSectionTypeEnum.values()){
			if(e.getName().equals(name)){
				return e;
			}
		}
		throw new IllegalArgumentException("No enum found with name: " + name);
	}

}
