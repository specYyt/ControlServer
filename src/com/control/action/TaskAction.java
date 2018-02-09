package com.control.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.control.dao.NodeDao;
import com.control.dao.TaskDao;
import com.control.dao.TaskNodeDao;
import com.control.model.Node;
import com.control.model.Order;
import com.control.model.Result;
import com.control.model.TaskForNodes;

public class TaskAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String Node_id;
	private String Task_id;
	private String Task_progress;
	private String type;
	private String target;
	private int port;
	private String username;
	private String password;

	private PrintWriter writer = null;
	private NodeDao nodedao = new NodeDao();
	private TaskDao taskdao = new TaskDao();
	private TaskNodeDao tasknodedao = new TaskNodeDao();
	private final Logger logger = Logger.getLogger(TaskAction.class);

	// 接收节点心跳请求
	public String check() {
		Node node = new Node();
		node.setNode_id(Node_id);
		if (nodedao.findNodeId(node)) {
			nodedao.updateNodeActiveTime(node);//更新心跳时间
		} else {
			logger.info(Node_id + "节点不存在");
		}
		if(Task_id != null){
			TaskForNodes taskForNodes = new TaskForNodes();
			taskForNodes.setNode_id(Node_id);
			taskForNodes.setTask_id(Task_id);
			taskForNodes.setProgress(Task_progress);
			tasknodedao.updateTaskStatus(taskForNodes);//更新任务状态
		}
		return SUCCESS;
	}

	// 把任务返回给节点
	public void sendTask() throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		writer = response.getWriter();
		Map<String, Object> maps = new HashMap<>();
		TaskForNodes task = tasknodedao.getTaskForNodes(Node_id);// 从数据库里面取一个任务
		if (task == null) {
			maps.put("result","null");
		}else{
			maps.put("task", task);
		}
		if(task != null) {
			tasknodedao.updateTaskStatus(Node_id, task.getTask_id() , "run");//修改为run
		}
		//获取需要停止的任务
		List<String> stopTask = tasknodedao.getOrder(Node_id);
		if(stopTask.size() != 0){
			 tasknodedao.deleteOrder(Node_id);
			 maps.put("stopTask", stopTask);
		}
		writer.print(JSON.toJSONString(maps));
	}

	// 接收任务结果
	public String getResult() {
		Result result = new Result(Task_id, Node_id, type, target, port, username, password);
		taskdao.addResult(result);
		return null;
	}
	
	// 根据任务ID 返回任务状态
	public void getTaskStatus() throws IOException{
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		writer = response.getWriter();
		String status= tasknodedao.getTaskStatus(Task_id);
		Document d = new Document("status", status);
		String jsonString = JSON.toJSONString(d);
		writer.print(jsonString);
	}
	
	
	// 根据任务ID 返回任务的总进度
	public void getTaskProgress() throws IOException{
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		writer = response.getWriter();
		double progress= tasknodedao.getTaskProgress(Task_id);
		java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
		String str = myformat.format(progress);    
		Document d = new Document("progress", str);
		String jsonString = JSON.toJSONString(d);
		writer.print(jsonString);
	}
	
	//根据任务ID 返回所有结果
	public String getTaskResults() throws IOException{
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		writer = response.getWriter();
		List<Result> results= taskdao.getResults(Task_id);
		String jsonString = JSON.toJSONString(results);
		writer.print(jsonString);
		return null;
	}
	
	/*
	 * 停止任务
	 */
	public void stopTask(){
		tasknodedao.updateTaskStatus(Task_id , "stop");//修改任务状态为stop
		
		List<Order> nodeIds = tasknodedao.findTasksForNodes(Task_id);
		//添加停止命令到Order表
		for(Order order : nodeIds)
			tasknodedao.addOrder(Task_id, order.getNode_id());
	}
	
	public String getNode_id() {
		return Node_id;
	}

	public void setNode_id(String node_id) {
		Node_id = node_id;
	}

	public String getTask_id() {
		return Task_id;
	}

	public void setTask_id(String task_id) {
		Task_id = task_id;
	}

	public String getTask_progress() {
		return Task_progress;
	}

	public void setTask_progress(String task_progress) {
		Task_progress = task_progress;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

}
