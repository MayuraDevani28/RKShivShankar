package com.shivshankar.classes;

public class FilterAttribute {

	String id, name;
	boolean isSelected = false;

	public FilterAttribute(String id, String name, boolean isSelected) {
		super();
		this.id = id;
		this.name = name;
		this.isSelected = isSelected;
	}

	public String getname() {
		return name;
	}

	public void setname(String count) {
		this.name = name;
	}

	public String getid() {
		return id;
	}

	public void setid(String name) {
		this.id = name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
