package com.newt.client;

public class CommitResponse {
	private Author author;
	private String url;
	private String message;
	private Author committer;
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Author getCommitter() {
		return committer;
	}
	public void setCommitter(Author committer) {
		this.committer = committer;
	}
}