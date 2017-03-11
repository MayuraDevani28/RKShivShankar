package com.shivshankar.classes;

public class SC3Object {
	int id;

	String name, IFSCCode, ImageURL;

	public SC3Object(int id, String name, String iFSCCode, String imgurl) {
		super();
		this.id = id;
		this.name = name;
		IFSCCode = iFSCCode;
		ImageURL = imgurl;
	}

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIFSCCode() {
		return IFSCCode;
	}

	public void setIFSCCode(String iFSCCode) {
		IFSCCode = iFSCCode;
	}

}
