package com.control.model;

public class Node {

	private String node_id;
	private String token;
	private String join_time;
	private String last_active_time;
	private int status;

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getJoin_time() {
		return join_time;
	}

	public void setJoin_time(String join_time) {
		this.join_time = join_time;
	}

	public String getLast_active_time() {
		return last_active_time;
	}

	public void setLast_active_time(String last_active_time) {
		this.last_active_time = last_active_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Node [node_id=" + node_id + ", token=" + token + ", join_time=" + join_time + ", last_active_time="
				+ last_active_time + ", status=" + status + "]";
	}

	

}
