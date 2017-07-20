package com.newt.client;

import java.util.Date;
import java.util.List;

public class Items {
	private List<String> affectedPaths;
	private String commitId;
	private String comment;
	private String authorEmail;
	private Date date;
	private String msg;
	private String id;
	private Author author;
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public List<String> getAffectedPaths() {
		return affectedPaths;
	}
	public void setAffectedPaths(List<String> affectedPaths) {
		this.affectedPaths = affectedPaths;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
	
	
}
