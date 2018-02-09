package com.control.model;

import com.alibaba.fastjson.annotation.JSONField;

public class TaskForNodes {
	
	@JSONField(serialize=false)
	private transient String id;
	private String task_id;
	private String node_id;
	private String service_type;
	private String target_url;
	private String username_url;
	private String password_url;
	@JSONField(serialize=false)
	private transient String task_status;
	private String task_create_time;
	private String custom;
	private String progress;
	
	public TaskForNodes() {

	}

	public TaskForNodes(String task_id, String node_id, String service_type, String target_url, String username_url,
			String password_url, String custom) {
		super();
		this.task_id = task_id;
		this.node_id = node_id;
		this.service_type = service_type;
		this.target_url = target_url;
		this.username_url = username_url;
		this.password_url = password_url;
		this.custom = custom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}

	public String getTask_create_time() {
		return task_create_time;
	}

	public void setTask_create_time(String task_create_time) {
		this.task_create_time = task_create_time;
	}

	public String getTarget_url() {
		return target_url;
	}

	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}

	public String getUsername_url() {
		return username_url;
	}

	public void setUsername_url(String username_url) {
		this.username_url = username_url;
	}

	public String getPassword_url() {
		return password_url;
	}

	public void setPassword_url(String password_url) {
		this.password_url = password_url;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

}
