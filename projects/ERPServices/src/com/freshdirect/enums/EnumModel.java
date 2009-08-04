package com.freshdirect.enums;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class EnumModel implements Serializable {

	private final String code;
	private final String name;
	private final String description;

	public final static Comparator COMP_NAME = new Comparator() {

		public int compare(Object o1, Object o2) {
			String name1 = ((EnumModel) o1).getName();
			String name2 = ((EnumModel) o2).getName();
			return name1.compareTo(name2);
		}

	};

	protected EnumModel(String code, String title, String description) {
		this.code = code;
		this.name = title;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o instanceof EnumModel) {
			EnumModel e = (EnumModel) o;
			return e.getClass().getName().equals(this.getClass().getName()) && e.code.equals(this.code);
		}
		return false;
	}

	protected static List loadEnums(Class daoClass) {
		return EnumManager.getInstance().loadEnums(daoClass);
	}

	public int hashCode() {
		return this.code.hashCode();
	}

	public String toString() {
		return this.getCode();
	}
}
