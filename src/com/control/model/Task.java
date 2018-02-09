package com.control.model;

public class Task {

	private String task_id;
	private String service_type;
	private String targeturl;
	private String usernameurl;
	private String passwordurl;
	private String custom;
	private int split;
	
	public Task() {

	}

	public Task(String task_id, String service_type, String targeturl, String usernameurl, String passwordurl, String custom, int split) {
		this.task_id = task_id;
		this.service_type = service_type;
		this.targeturl = targeturl;
		this.usernameurl = usernameurl;
		this.passwordurl = passwordurl;
		this.custom = custom;
		this.split = split;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTargeturl() {
		return targeturl;
	}

	public void setTargeturl(String targeturl) {
		this.targeturl = targeturl;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getUsernameurl() {
		return usernameurl;
	}

	public void setUsernameurl(String usernameurl) {
		this.usernameurl = usernameurl;
	}

	public String getPasswordurl() {
		return passwordurl;
	}

	public void setPasswordurl(String passwordurl) {
		this.passwordurl = passwordurl;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}

}
