package com.control.model;

public class Result {

	private String id;
	private String task_id;
	private String node_id;
	private String type;
	private String target;
	private int port;
	private String username;
	private String password;

	public Result() {

	}

	public Result(String task_id, String node_id, String type, String target, int port, String username,
			String password) {
		this.task_id = task_id;
		this.node_id = node_id;
		this.type = type;
		this.target = target;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Result [id=" + id + ", task_id=" + task_id + ", node_id=" + node_id + ", type=" + type + ", target="
				+ target + ", port=" + port + ", username=" + username + ", password=" + password + "]";
	}

	
}
