package com.control.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.control.exception.MyException;
import com.control.model.Result;
import com.control.model.Task;
import com.control.util.DBCPUtil;

public class TaskDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

	// 添加结果
	public void addResult(Result result) {
		try {
			String sql = "insert into result(task_id, node_id, type, target, port, username,password, succeed_time) values(?,?,?,?,?,?,?,?)";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String join_time = df.format(new Date());
			Object params[] = { result.getTask_id(), result.getNode_id(), result.getType(), result.getTarget(),
					result.getPort(), result.getUsername(), result.getPassword(), join_time };
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("添加结果出错");
		}
	}

	// 添加任务
	public void addTask(Task task) {
		try {
			String sql = "insert into task(task_id,service_type,target,usernameurl,passwordurl,task_create_time, custom, split) values(?,?,?,?,?,?,?,?)";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String join_time = df.format(new Date());
			Object params[] = { task.getTask_id(), task.getService_type(), task.getTargeturl(), task.getUsernameurl(),
					task.getPasswordurl(), join_time, task.getCustom(), task.getSplit() };
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("添加任务出错");
		}
	}

	/*
	 * 根据任务ID取得任务结果
	 */
	public List<Result> getResults(String taskid){
		String sql = "select * from result where task_id = ?";
		Object[] params = { taskid };
		try {
			return qr.query(sql, new BeanListHandler<Result>(Result.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("查找任务结果出错");
		}
	}
	
	public boolean findTask(String taskid){
		String sql = "select  task_id from task where task_id = ?";
		Object[] params = { taskid };
		Task task = null;
		try {
			task = qr.query(sql, new BeanHandler<Task>(Task.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("查找任务ID出错");
		}
		if(task != null){
			return true;
		}else{
			return false;
		}
	}
	
}
