package com.shivshankar.classes;

public class FilterAttribute {

	String name, count;
	boolean isSelected = false;

	public FilterAttribute(String name, String string, boolean isSelected) {
		super();
		this.name = name;
		this.count = string;
		this.isSelected = isSelected;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
