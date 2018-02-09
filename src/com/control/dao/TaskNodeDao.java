package com.control.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.control.exception.MyException;
import com.control.model.Order;
import com.control.model.TaskForNodes;
import com.control.util.DBCPUtil;

public class TaskNodeDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(NodeDao.class);
	
	// 添加节点任务
	public void addTaskForNodes(TaskForNodes taskNode) {
		try {
			String sql = "insert into tasks_for_nodes(task_id,node_id, service_type,target_url,username_url,password_url,task_create_time, custom, progress) values(?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			Object params[] = { taskNode.getTask_id(), taskNode.getNode_id(), taskNode.getService_type(),
					taskNode.getTarget_url(), taskNode.getUsername_url(), taskNode.getPassword_url(), time ,taskNode.getCustom(), "0/0"};
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加节点任务出错"+e.getMessage());
			e.printStackTrace();
		}
	}

	// 取出节点任务
	public TaskForNodes getTaskForNodes(String Node_id) {
		try {
			String sql = "select * from tasks_for_nodes where node_id = ? and "
					+ "task_status = 'ready' order by task_create_time asc limit 1";
			Object params[] = { Node_id };
			return qr.query(sql, new BeanHandler<TaskForNodes>(TaskForNodes.class), params);
		} catch (SQLException e) {
			logger.error("取出节点任务出错"+e.getMessage());
			e.printStackTrace();
		}
		return new TaskForNodes();
	}

	// 更新任务状态
	public void updateTaskStatus(TaskForNodes task) {
		try {
			String sql = "update tasks_for_nodes set progress=? where task_id = ? and node_id = ? ";
			Object params[] = { task.getProgress(), task.getTask_id(), task.getNode_id() };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("更新任务状态出错"+e.getMessage());
			e.printStackTrace();
		}
	}

	// 根据任务ID获得任务进度
	public double getTaskProgress(String taskId){
		List<TaskForNodes> tasks;
		try {
			String sql = "select * from tasks_for_nodes where task_id = ?";
			Object params[] = { taskId };
			tasks = qr.query(sql, new BeanListHandler<TaskForNodes>(TaskForNodes.class), params);
		} catch (SQLException e) {
			throw new MyException("获得任务进度出错");
		}
		double sum1 = 0, sum2 = 0;
		for(TaskForNodes t:tasks){
			String s = t.getProgress();
			String[] nums = s.split("/");
			if(t.getTask_status().equals("run")){
				double s0 = Double.valueOf(nums[0]);
				double s1 = Double.valueOf(nums[1]);
				if(s1 != 0 && s0/s1 == 1){
					this.updateTaskStatus(t.getNode_id(), taskId, "complete");
				}
			}
			sum1 += Double.parseDouble(nums[0]);
			sum2 += Double.parseDouble(nums[1]);
		}
		if(sum2 == 0) return 0;
		return sum1/sum2;
	}

	/*
	 * 获得任务状态
	 */
	public String getTaskStatus(String taskId) {
		List<TaskForNodes> tasks = new ArrayList<>();
		try {
			String sql = "select * from tasks_for_nodes where task_id = ?";
			Object params[] = {taskId };
			tasks = qr.query(sql, new BeanListHandler<TaskForNodes>(TaskForNodes.class), params);
		} catch (SQLException e) {
			throw new MyException("获得任务状态出错");
		}
		
		for(TaskForNodes task :tasks){
			if(task.getTask_status().equals("stop")){
				return "stop";
			}
			if(!task.getTask_status().equals("complete")){
				return "run";
			}
		}
		return "complete";
	}

	/*
	 * 更新任务状态
	 */
	public void updateTaskStatus(String nodeId, String taskId, String status) {
		try {
			String sql = "update tasks_for_nodes set task_status = ? where task_id = ? and node_id = ?";
			Object params[] = { status, taskId, nodeId };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new MyException("更新任务状态出错");
		}
	}

	public List<String> getTaskByStatus(String taskId, String status) {
		List<String> result = new ArrayList<>();
		try {
			String sql = "select task_id from tasks_for_nodes where node_id = ? and task_status = ?";
			Object params[] = {taskId , status };
			result =  qr.query(sql, new BeanListHandler<String>(String.class), params);
		} catch (SQLException e) {
			throw new MyException("获得任务状态出错");
		}
		return result;
	}


	public void updateTaskStatus(String task_id, String status) {
		try {
			String sql = "update tasks_for_nodes set task_status = ? where task_id = ?";
			Object params[] = { status, task_id};
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new MyException("更新任务状态出错");
		}
	}



	public List<Order> findTasksForNodes(String task_id) {
		List<Order> result = new ArrayList<>();
		String sql = "select node_id from tasks_for_nodes where task_id = ?";
		Object params[] = {task_id};
		try {
			result =  qr.query(sql, new BeanListHandler<Order>(Order.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


	public void addOrder(String task_id, String node_id) {
		try {
			String sql = "insert into task_order(task_id,node_id) values(?,?)";
			Object params[] = { task_id, node_id};
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("添加停止命令");
		}
	}


	public List<String> getOrder(String node_id) {
		List<Order> orders = new ArrayList<>();
		List<String> result = new ArrayList<>();
		String sql = "select task_id from task_order where node_id = ?";
		Object params[] = {node_id};
		try {
			orders =  qr.query(sql, new BeanListHandler<Order>(Order.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Order order : orders){
			result.add(order.getTask_id());
		}
		return result;
	}


	public void deleteOrder(String node_id) {
		try {
			String sql = "delete from task_order where node_id = ?";
			Object params[] = {  node_id};
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("删除已停止的命令");
		}
	}
}
