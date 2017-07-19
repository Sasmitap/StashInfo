package com.newt.client;

import java.util.Date;
import java.util.List;

public class Items {
	
	private String sha;
	private Date date;
	private Author author;
	private String message;
	
	/*private List<String> affectedPaths;
	private String comment;
	private String msg;
	private String id;
	private String commitId;*/
	
	
	public Author getAuthor() {
		return author;
	}
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/*public List<String> getAffectedPaths() {
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
	}*/
	
	/*public String getMsg() {
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
	}*/
}
