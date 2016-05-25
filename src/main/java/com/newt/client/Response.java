package com.newt.client;

public class Response {
	private ChangeSet changeSet;
	private String mavenVersionUsed;
	private int number;
	private String url;
	public ChangeSet getChangeSet() {
		return changeSet;
	}

	public void setChangeSet(ChangeSet changeSet) {
		this.changeSet = changeSet;
	}

	public String getMavenVersionUsed() {
		return mavenVersionUsed;
	}

	public void setMavenVersionUsed(String mavenVersionUsed) {
		this.mavenVersionUsed = mavenVersionUsed;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
