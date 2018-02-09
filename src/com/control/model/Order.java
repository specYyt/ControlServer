/**
 * 
 */
package com.control.model;

/**
 * @author Admin
 *
 */
public class Order {

	private int  id;
	private String task_id;
	private String node_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	
	
}
